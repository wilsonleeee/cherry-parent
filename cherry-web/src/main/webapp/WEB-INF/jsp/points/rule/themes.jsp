<%@ page contentType="text/html; charset=gb2312" language="java"
	errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jsTree v.1.0 - themes documentation</title>
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

<h1>jsTree v.1.0 - themes plugin</h1>
<h2>Description</h2>
<div id="description">
<p>The <code>themes</code> plugin controls the looks of jstree -
without this plugin you will get a functional tree, but it will look
just like an ordinary UL list.</p>
</div>

<h2 id="configuration">Configuration</h2>
<div class="panel configuration">
<h3>theme</h3>
<p class="meta">A string. Default is <code>"default"</code>.</p>
<p>The name of the theme to use to style the tree.</p>

<h3>url</h3>
<p class="meta">A string (or <code>false</code> if not used).
Default is <code>false</code>.</p>
<p>The location of the theme's CSS file, if set to <code>false</code>
jstree will look for the file in <code><em>&lt; theme folder
&gt;</em>/themes/<em>&lt; theme name &gt;</em>/style.css</code>. You can set the
theme folder using <code>$.jstree._themes = "PATH/TO/FOLDER/";</code>,
otherwise it is autodetected as <code><em>&lt;jquery.tree.js
location&gt;</em>/themes/</code>.</p>

<h3>dots</h3>
<p class="meta">A Boolean. Default is <code>true</code>.</p>
<p>Whether to show the connecting dots or not.</p>

<h3>icons</h3>
<p class="meta">A Boolean. Default is <code>true</code>.</p>
<p>Whether to show the node icons or not.</p>

</div>

<h2 id="demos">Demos</h2>
<div class="panel">

<h3>Using the themes plugin</h3>
<input type="button" class="button" value="toggle_dots" id="toggle_dots"
	style="float: left;" /> <input type="button" class="button"
	value="toggle_icons" id="toggle_icons" style="" />
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
	$("#toggle_dots, #toggle_icons").click(function () { 
		$("#demo1").jstree(this.value);
	});
	$("#demo1").jstree({ 
		"themes" : {
			"theme" : "default",
			"dots" : false,
			"icons" : false
		},
		"plugins" : [ "themes", "html_data" ]
	});
});
</script></div>

<h2 id="api">API</h2>
<div class="panel api">

<h3 id="set_theme">.set_theme ( name , url )</h3>
<p>Set the tree's theme. Triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">string</code> <strong>name</strong>
	<p>The name of the theme to use to style the tree.</p>
	</li>
	<li><code class="tp">string</code> <strong>url</strong>
	<p>The location of the theme's CSS file, if omitted jstree will
	look for the file in:<br />
	<code><em>&lt; theme folder &gt;</em>/themes/<em>&lt; name
	&gt;</em>/style.css</code>.<br />
	You can set the theme folder using:<br />
	<code>$.jstree._themes = "PATH/TO/FOLDER/";</code>, otherwise it is
	autodetected as <code><em>&lt;jquery.tree.js location&gt;</em>/themes/</code>.</p>
	</li>
</ul>
<h3 id="get_theme">.get_theme ( )</h3>
<p>Returns the name of the currently active theme.</p>

<div style="height: 1px; visibility: hidden;"><span id="hide_dots">&nbsp;</span><span
	id="toggle_dots">&nbsp;</span></div>
<h3 id="show_dots">.show_dots ( ), .hide_dots ( ), .toggle_dots ( )</h3>
<p>Show, hide or toggle the visibility of the dots connecting the
tree's nodes.</p>

<div style="height: 1px; visibility: hidden;"><span
	id="hide_icons">&nbsp;</span><span id="toggle_icons">&nbsp;</span></div>
<h3 id="show_icons">.show_icons ( ), .hide_icons ( ), .toggle_icons
( )</h3>
<p>Show, hide or toggle the visibility of the icons next to the
title of each the tree node.</p>

</div>

</div>
</body>
</html>