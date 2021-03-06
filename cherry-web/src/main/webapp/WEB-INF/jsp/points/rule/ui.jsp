<%@ page contentType="text/html; charset=gb2312" language="java"
	errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jsTree v.1.0 - UI documentation</title>
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

<h1>jsTree v.1.0 - UI plugin</h1>
<h2>Description</h2>
<div id="description">
<p>The <code>UI</code> plugin handles selecting, deselecting and
hovering tree items.</p>
</div>

<h2 id="configuration">Configuration</h2>
<div class="panel configuration">
<h3>select_limit</h3>
<p class="meta">A number. Default is <code>-1</code>.</p>
<p>Defines how many nodes can be selected at a given time (<code>-1</code>
means unlimited).</p>

<h3>select_multiple_modifier</h3>
<p class="meta">A string. Default is <code>"ctrl"</code>.</p>
<p>The special key used to make a click add to the selection and not
replace it (<code>"ctrl"</code>, <code>"shift"</code>, <code>"alt"</code>,
<code>"meta"</code>).<br />
You can also set this to <code>"on"</code> making every click add to the
selection.</p>

<h3>selected_parent_close</h3>
<p class="meta">A string (or <code>false</code>). Default is <code>"select_parent"</code>.</p>
<p>What action to take when a selected node's parent is closed
(making the selected node invisible). Possible values are <code>false</code>
- do nothing, <code>"select_parent"</code> - select the closed node's
parent and <code>"deselect"</code> - deselect the node.</p>

<h3>initially_select</h3>
<p class="meta">An array. Default is <code>[]</code>.</p>
<p>Defines which nodes are to be automatically selected when the
tree finishes loading - a list of IDs is expected.</p>

</div>

<h2 id="demos">Demos</h2>
<div class="panel">

<h3>Using the UI plugin</h3>
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
		"ui" : {
			"select_limit" : 2,
			"select_multiple_modifier" : "alt",
			"selected_parent_close" : "select_parent",
			"initially_select" : [ "phtml_2" ]
		},
		"core" : { "initially_open" : [ "phtml_1" ] },
		"plugins" : [ "themes", "html_data", "ui" ]
	});
});
</script></div>

<h2 id="api">API</h2>
<div class="panel api">

<h3 id="_get_node">._get_node ( node , allow_multiple )</h3>
<p>Overrides the function form the <a href="core.html#_get_node">core</a>
module.<br />
if <code>node</code> is <code>undefined</code> or <code>null</code> and
<code>allow_multiple</code> is <code>true</code> all the currently
selected nodes are returned<br />
if <code>node</code> is <code>undefined</code> or <code>null</code> and
<code>allow_multiple</code> is NOT <code>true</code> only the last
selected node is returned.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree.</p>
	</li>
	<li><code class="tp">boolean</code> <strong>allow_multiple</strong>
	<p>Whether to return all selected nodes or only the last selected
	one if <code>obj</code> is <code>null</code>.</p>
	</li>
</ul>

<h3 id="save_selected">.save_selected ( )</h3>
<p>Saves away the current selection state of the tree (saves it in a
variable, so do not expect a restore after a refresh - for that
functionality refer to the <a href="cookies.html">cookies plugin</a>.
Used mostly internally. Triggers an event.</p>

<h3 id="reselect">.reselect ( )</h3>
<p>Restores the state of the tree using the variable saved in the
above function. Used mostly internally. Triggers an event.</p>

<h3 id="refresh">.refresh ( node )</h3>
<p>Overrides the function form the <a href="core.html#refresh">core</a>
module.<br />
Enables saving the selection state before the refresh and restoring it
afterwards.</p>

<h3 id="hover_node">.hover_node ( node )</h3>
<p>Sets the specified node as hovered. Triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree.</p>
	</li>
</ul>

<h3 id="dehover_node">.dehover_node ( )</h3>
<p>Removes the hover state from the currently hovered node (if there
is one). Triggers an event.</p>


<h3 id="select_node">.select_node ( node , check , event )</h3>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree.</p>
	</li>
	<li><code class="tp">bool</code> <strong>check</strong>
	<p>Whether to check the specified rules and do appropriate actions
	(check <code>select_limit</code>, deselect other nodes respectively,
	etc) or to just force selection of the node regardless of <code>select_limit</code>.</p>
	</li>
	<li><code class="tp">event</code> <strong>event</strong>
	<p>Used internally - when a click on a node caused this function to
	be executed.</p>
	</li>
</ul>

<div style="height: 1px; visibility: hidden; overflow: hidden;"><span
	id="toggle_select">&#160;</span></div>
<h3 id="deselect_node">.deselect_node ( node ), .toggle_select (
node )</h3>
<p>There functions control the selected state on a node. <code>deselect_node</code>
triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree.</p>
	</li>
</ul>

<h3 id="deselect_all">.deselect_all ( context )</h3>
<p>Deselects all selected nodes. If context is set - only the
selected nodes within that context are deselected. Triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>context</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree.</p>
	</li>
</ul>

<h3 id="get_selected">.get_selected ( context )</h3>
<p>Returns all selected nodes. If context is set - only the selected
nodes within that context are returned.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>context</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree.</p>
	</li>
</ul>

<h3 id="is_selected">.is_selected ( node )</h3>
<p>Returns whether the specified node is selected or not.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree.</p>
	</li>
</ul>

</div>

</div>
</body>
</html>