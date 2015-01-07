---
layout: page
title: Rejected Ingress Portals
---
{% include JB/setup %}

## このサイトについて

このサイトは、Ingressで登録されなかったポータル情報を匿名で共有できるサイトです。
どういった申請が受け付けられないのかを知る参考にしていただければと思います。

情報提供は随時受け付けております。
Reject通知のメールを以下のアドレスまで転送してください。

    ingressreject@googlegroups.com

情報提供の方法や注意事項につきましては、[投稿方法](/post.html)をご覧ください。

## 新着ポータル

<ul class="posts">
  {% for post in site.tags.reject limit: 10 %}
    <a href="{{ BASE_PATH }}{{ post.url }}" alt="{{ post.title }}"><img src="{{ post.imgurl }}=w100" border="0"/></a>
  {% endfor %}
</ul>

## [too close](/tags.html#tooclose-ref)

too closeが理由でリジェクトされたポータルは、一番登録される可能性の高いものだと考えられます。

<ul class="posts">
  {% for post in site.tags.tooclose limit: 10 %}
    <a href="{{ BASE_PATH }}{{ post.url }}" alt="{{ post.title }}"><img src="{{ post.imgurl }}=w100" border="0"/></a>
  {% endfor %}
</ul>
