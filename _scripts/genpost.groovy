import javax.mail.*
import javax.mail.search.*
import java.util.Properties

class Const {
	static final mailServer = "imap.gmail.com"
	static final mailPort = 993
	static final postpath = "_posts/generated/"
}

def genPost(String mailId, Date date, String content) {
	println "generating post"
	println content
	String imgurl = content.find(/<img [^>]*?src=.+?https?:\/\/.+?\/[\w\-_]+/) {str-> str.substring(str.indexOf("http"))}
	if (imgurl == null) return "image url is not found."
	println "imgurl: " + imgurl
//	String id = imgurl.substring(imgurl.lastIndexOf("/") + 1)
//	if (id == null) return "image id is not found."
	String filename = date.format("yyyy-MM-dd-") + mailId + ".html"
	println "filename: " + filename

	String location = null
	if (content =~ /www\.ingress\.com\/intel/) {
		location = content.find(/https:\/\/www\.ingress\.com\/intel[^<"]+/) {str->str.find(/ll=[\d,\.]+/)}
		location = location.substring(3)
//		location = location.substring(0, location.lastIndexOf(","))
	}
	println "location: " + location
	String portalname = content.find(/complete: [^<]+/) {str->str.substring(10)}
	if (portalname == null) return "portal name is not found."
	println "portalname: " + portalname
	String masked = content.find(/From:.*?Ingress Operations.*<\/div>/) {str->str.replaceAll(/To: .+?<br>/, "To: **<br>")}
	if (masked == null) return "ingress operation mail is not found."
	String tags = "reject"
	if (masked =~ /too close/) {
		tags += " tooclose"
	}
	new File(Const.postpath, filename).withWriter('UTF-8') { writer ->
		writer << """---
layout: portal
category: reject
tags: ${tags}
id: ${mailId}
imgurl: ${imgurl}
portalname: ${portalname}
sentdate: ${date}
title: ${mailId} - ${portalname}
"""
		if (location) writer << "location: \"${location}\"\n"
		writer << "---\n"
		writer << masked
	}
	return null
}

// mail box scan

Properties props = new Properties()
props.setProperty("mail.store.protocol", "imaps")
props.setProperty("mail.imaps.host", Const.mailServer)
props.setProperty("mail.imaps.port", Const.mailPort.toString())

def session = Session.getDefaultInstance(props, null)
def store = session.getStore("imaps")
user = args[0]
password = args[1]
store.connect(Const.mailServer, user, password)
def folder = store.getFolder("INBOX")
folder.open(Folder.READ_WRITE)
folder.search(new AndTerm(new FlagTerm(new Flags(Flags.Flag.SEEN), false), new BodyTerm("Ingress Operations"))).each { msg ->
	println "Subject: ${msg.subject}"
	println "Sent on: ${msg.sentDate}"
	id = msg.subject.find(/^\[ingressreject:\d+\]/) {str-> str.find(/\d+/)}
	if (id == null) {println "mail id is not found"; return}
	if (msg.isMimeType("multipart/alternative")) {
		Multipart parent = (Multipart)msg.content
		for (i = 0; i < parent.getCount(); i ++) {
			Part part = parent.getBodyPart(i)
			if (part.isMimeType("text/html") && part.content.contains("not to accept")) {
				err = genPost(id, msg.sentDate, part.content)
				if (err) {
					// TODO: reply error mail
					println err
				}
			}
		}
//		msg.setFlag(Flags.Flag.SEEN, false) // for debug
	}
}
