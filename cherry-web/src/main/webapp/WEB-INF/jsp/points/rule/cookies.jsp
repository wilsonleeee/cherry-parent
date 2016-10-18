<%@ page contentType="text/html; charset=gb2312" language="java"
	errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jsTree v.1.0 - cookies documentation</title>
<script type="text/javascript" src="/Cherry/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/Cherry/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="/Cherry/js/jquery/jquery.hotkeys.js"></script>
<script type="text/javascript" src="/Cherry/js/jquery/jquery.jstree.js"></script>

<link type="text/css" rel="stylesheet"
	href="/Cherry/css/points/!style1.css" />
<link type="text/css" rel="stylesheet"
	href="/Cherry/css/points/!style.css" />
<link rel="stylesheet" href="/Cherry/css/common/witpos3.css"
	type="text/css" />
<script type="text/javascript" src="/Cherry/js/jquery/!script.js"></script>

</head>
<body>
<div id="container">

<h1>jsTree v.1.0 - cookies plugin</h1>
<h2>Description</h2>
<div id="description">
<p>The <code>cookies</code> enables jstree to save the state of the
tree across sessions. What this does is save the opened and selected
nodes in a cookie, and reopen &amp; reselect them the next time the user
loads the tree. Depends on the <a
	href="http://plugins.jquery.com/project/cookie">jQuery.cookie</a>
plugin.</p>
</div>

<h2 id="configuration">Configuration</h2>
<div class="panel configuration">

<h3>save_opened</h3>
<p class="meta">A string (or <code>false</code>). Default is <code>"jstree_open"</code>.</p>
<p>The name of the cookie to save opened nodes in. If set to <code>false</code>
- opened nodes won't be saved.</p>

<h3>save_selected</h3>
<p class="meta">A string (or <code>false</code>). Default is <code>"jstree_select"</code>.</p>
<p>The name of the cookie to save selected nodes in. If set to <code>false</code>
- selected nodes won't be saved.</p>

<h3>auto_save</h3>
<p class="meta">A Boolean. Default is <code>true</code>.</p>
<p>If set to <code>true</code> jstree will automatically update the
cookies every time a change in the state occurs.</p>

<h3>cookie_options</h3>
<p class="meta">An object. Default is <code>{}</code>.</p>
<p>The options accepted by the <a
	href="http://plugins.jquery.com/project/cookie">jQuery.cookie</a>
plugin.</p>

</div>

<h2 id="demos">Demos</h2>
<div class="panel">
<p>Check your data plugin documentation (<a href="html_data.html">html_data</a>,
<a href="xml_data.html">xml_data</a>, <a href="json_data.html">json_data</a>)
or take a close look at these examples for information on how to specify
multilanguage nodes.</p>

<h3>Using the cookies plugin</h3>
<p>Go ahead and make changes to the tree and then <a
	href="javascript:document.location.reload();">refresh this page</a>.</p>
<div id="demo1" class="demo">
<ul>
	<li id="phtml_1"><a href="#">Root node 1</a>
	<ul>
		<li id="phtml_2"><a href="#">Child node 1</a></li>
		<li id="phtml_3"><a href="#">Child node 2</a></li>
	</ul>
	</li>
	<li id="phtml_4"><a href="#">Root node 2</a></li>
</ul>
</div>
<script type="text/javascript">
$(function () {
	$("#demo1").jstree({ 
		"plugins" : [ "themes", "html_data", "ui", "cookies" ]
	});
});
</script></div>

<h2 id="api">API</h2>
<div class="panel api">

<h3 id="save_cookie">.save_cookie ( event )</h3>
<p>Save the current state.</p>
<ul class="arguments">
	<li><code class="tp">string</code> <strong>event</strong>
	<p>Used internally with the <code>auto_save</code> option. Do not
	set this manually.</p>
	</li>
</ul>
</div>

</div>
</body>
</html>