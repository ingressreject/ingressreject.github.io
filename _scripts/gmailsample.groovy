import javax.mail.*
import javax.mail.search.*
import java.util.Properties

String mailServer = "imap.gmail.com"
int mailPort = 993

def genPost(Date date, String content) {
	println "generating post"
	String imgurl = content.find(/img src="https:\/\/.+?\/[\w\-]+/) {str-> str.substring(str.indexOf("https"))}
	String id = imgurl.substring(imgurl.lastIndexOf("/") + 1)
	String filename = date.format("yyyy-MM-dd-") + id + ".md"

	String location = ""
	if (content =~ /www\.google[^\/]+\/maps\/place/) {
		location = content.find(/https:\/\/www\.google.+?\/maps[^<"]+/) {str->str.find(/@[\d,\.z]+/)}
		println location
	}
	String portalname = content.find(/Rejected: [^<]+/) {str->str.substring(10)}
	println portalname
	String masked = content.find(/From: Ingress Operations.*?<\/div>/) {str->str.replaceAll(/To: .+?<br>/, "To: **<br>")}
	println masked
	println imgurl
	println filename
}

// mail box scan

Properties props = new Properties()
props.setProperty("mail.store.protocol", "imaps")
props.setProperty("mail.imaps.host", mailServer)
props.setProperty("mail.imaps.port", mailPort.toString())

def session = Session.getDefaultInstance(props, null)
def store = session.getStore("imaps")
user = args[0]
password = args[1]
store.connect(mailServer, user, password)
def folder = store.getFolder("INBOX")
folder.open(Folder.READ_WRITE)
folder.search(new AndTerm(new FlagTerm(new Flags(Flags.Flag.SEEN), false), new BodyTerm("Ingress Operations"))).each { msg ->
	println "Subject: ${msg.subject}"
	println "Sent on: ${msg.sentDate}"
	if (msg.isMimeType("multipart/alternative")) {
		Multipart parent = (Multipart)msg.content
		for (i = 0; i < parent.getCount(); i ++) {
			Part part = parent.getBodyPart(i)
			if (part.isMimeType("text/html") && part.content.contains("Rejected")) {
				genPost(msg.sentDate, part.content)
			}
		}
		msg.setFlag(Flags.Flag.SEEN, false) // for debug
	}
}
