<%@ page contentType="text/html; charset=gb2312" language="java"
	errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jsTree v.1.0 - hotkeys documentation</title>
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

<h1>jsTree v.1.0 - hotkeys plugin</h1>
<h2>Description</h2>
<div id="description">
<p>The <code>hotkeys</code> plugin enables keyboard navigation and
shortcuts. Depends on the <a
	href="http://github.com/jeresig/jquery.hotkeys/">jquery.hotkeys
plugin</a></p>
</div>

<h2 id="configuration">Configuration</h2>
<div class="panel configuration">
<p>Expects an object:<br />
each key is the keyboard shortcut (for possible values check <a
	href="http://github.com/jeresig/jquery.hotkeys/">the hotkeys plugin</a>)<br />
each values is a function executed in the instance's context, the return
value is used as a return value for the event.</p>
<p>Simple example:</p>
<p><code>"del" : function () { this.remove(); }</code></p>

</div>

<h2 id="demos">Demos</h2>
<div class="panel">

<h3>Using the hotkeys plugin</h3>
<p>Try pressing <code>up</code>/<code>down</code>/<code>left</code>/<code>right</code>/<code>space</code>/<code>f2</code>/<code>del</code>.</p>
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
	  "core" : { "initially_open" : [ "phtml_1" ] },
	  "plugins" : ["themes","html_data","ui","crrm","hotkeys"]
	});
});
</script></div>

<h2 id="api">API</h2>
<div class="panel api">

<h3 id="enable_hotkeys">.enable_hotkeys ( )</h3>
<p>Enable shortcuts on the instance (enabled by default).</p>

<h3 id="disable_hotkeys">.disable_hotkeys ( )</h3>
<p>Disable shortcuts on the instance.</p>

</div>

</div>
</body>
</html>