<%@ page contentType="text/html; charset=gb2312" language="java"
	errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jsTree v.1.0 - contextmenu documentation</title>
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

<h1>jsTree v.1.0 - contextmenu plugin</h1>
<h2>Description</h2>
<div id="description">
<p>The <code>contextmenu</code> plugin enables a contextual menu to
be shown, when the user right-clicks a node (or when triggered
programatically by the developer).</p>
</div>

<h2 id="configuration">Configuration</h2>
<div class="panel configuration">
<h3>show_at_node</h3>
<p class="meta">Boolean. Default is <code>true</code>.</p>
<p>Whether to show the context menu just below the node, or at the
clicked point exactly.</p>

<h3>items</h3>
<p>Expects an object or a function, which should return an object.
If a function is used it fired in the tree's context and receives one
argument - the node that was right clicked. The object format is:</p>
<div style="border: 1px solid gray"><pre class="brush:js">
{
// Some key
"rename" : {
	// The item label
	"label"				: "Rename",
	// The function to execute upon a click
	"action"			: function (obj) { this.rename(obj); },
	// All below are optional 
	"separator_before"	: false,	// Insert a separator before the item
	"separator_after"	: true,		// Insert a separator after the item
	// false or string - if does not contain `/` - used as classname
	"icon"				: false,
	"submenu"			: { 
		/* Collection of objects (the same structure) */
	}
}
/* MORE ENTRIES ... */
}
</pre></div>
</div>

<h2 id="demos">Demos</h2>
<div class="panel">

<h3>Using the contextmenu</h3>
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
		"plugins" : [ "themes", "html_data", "ui", "crrm", "contextmenu" ]
	});
});
</script></div>

<h2 id="api">API</h2>
<div class="panel api">

<h3 id="show_contextmenu">.show_contextmenu ( node , x, y )</h3>
<p>Shows the contextmenu next to a node. Triggered automatically
when right-clicking a node.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree.</p>
	</li>
	<li><code class="tp">number</code> <strong>x</strong>
	<p>The X-coordinate to show the menu at - may be overwritten by <code>show_at_node</code>.
	If you omit this the menu is shown aligned with the left of the node.</p>
	</li>
	<li><code class="tp">number</code> <strong>y</strong>
	<p>The Y-coordinate to show the menu at - may be overwritten by <code>show_at_node</code>.
	If you omit this the menu is shown just below the node.</p>
	</li>
</ul>

</div>

</div>
</body>
</html>