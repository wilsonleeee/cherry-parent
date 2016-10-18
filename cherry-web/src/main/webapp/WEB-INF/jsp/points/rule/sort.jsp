<%@ page contentType="text/html; charset=gb2312" language="java"
	errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jsTree v.1.0 - sort documentation</title>
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

<h1>jsTree v.1.0 - sort plugin</h1>
<h2>Description</h2>
<div id="description">
<p>The <code>sort</code> enables jstree to automatically sort all
nodes using a specified function. This means that when the user creates,
renames or moves nodes around - they will automatically sort.</p>
</div>

<h2 id="configuration">Configuration</h2>
<div class="panel configuration">

<p>Expects a function. The functions receives two arguments - two
nodes to be compared. Return <code>-1</code> or <code>1</code>. Default
is:</p>
<p><code>function (a, b) { return this.get_text(a) >
this.get_text(b) ? 1 : -1; }</code></p>

</div>

<h2 id="demos">Demos</h2>
<div class="panel">
<h3>Using the sort plugin</h3>
<input type="button" class="button" value="rename" id="rename" style="" />
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
<script type="text/javascript" class="source">
$(function () {
	$("#rename").click(function () { 
		$("#demo1").jstree("rename");
	});
	$("#demo1").jstree({ 
		"plugins" : [ "themes", "html_data", "ui", "crrm", "sort" ]
	});
});
</script></div>

<h2 id="api">API</h2>
<div class="panel api">

<h3 id="sort">.sort ( node )</h3>
<p>Sorts the children of the specified node - this function is
called automatically.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to the
	element.</p>
	</li>
</ul>
</div>

</div>
</body>
</html>