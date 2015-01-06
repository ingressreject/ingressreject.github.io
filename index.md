---
layout: page
title: Rejected Ingress Portals
---
{% include JB/setup %}

## このサイトについて

このサイトは、Ingressで登録されなかったポータル情報を収集しています。

情報提供は随時受け付けております。
Reject通知のメールを以下のアドレスまで転送してください。

    ingressreject@googlegroups.com

メールの本文は加工しないでください。To:などのアドレス部分は削除、もしくはマスクして頂いて結構です。
また記載があってもこちらでマスクして掲載します。

転送メールの本文に、ポータルを申請した場所のGoogle Mapsのリンクを記載して頂けると、こちらで位置情報として掲載します。

よろしくお願いします。

- Enlightend Agent: yoshimov

## 新着ポータル

<ul class="posts">
  {% for post in site.tags.reject limit: 10 %}
    <a href="{{ BASE_PATH }}{{ post.url }}" alt="{{ post.title }}"><img src="{{ post.imgurl }}=w100" border="0"/></a>
  {% endfor %}
</ul>

## [too close](/tags.html#tooclose-ref)

too closeが理由でリジェクトされたポータルは、一番登録される可能性の高いものだと考えられます。
参考にしていただければと思います。

<ul class="posts">
  {% for post in site.tags.tooclose limit: 10 %}
    <a href="{{ BASE_PATH }}{{ post.url }}" alt="{{ post.title }}"><img src="{{ post.imgurl }}=w100" border="0"/></a>
  {% endfor %}
</ul>
