<%@ page contentType="text/html; charset=gb2312" language="java"
	errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jsTree Testing</title>
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

<h1>jsTree v.1.0 - Core</h1>
<h2>Description</h2>
<div id="description">
<h3>Including the files</h3>
<p>First of all, as jsTree is a jQuery component, you need to
include jQuery itself. jsTree v.1.0 requires jQuery version 1.4.2</p>

<div class="code_f"><pre class="brush:xml;">
&lt;script type="text/javascript" src="_lib/jquery.js"&gt;&lt;/script&gt;
</pre></div>

<p>Then you need to include jsTree:</p>

<div class="code_f"><pre class="brush:xml;">
&lt;script type="text/javascript" src="jquery.jstree.js"&gt;&lt;/script&gt;
</pre></div>

<p>Or you could use the minified version:</p>

<div class="code_f"><pre class="brush:xml;">
&lt;script type="text/javascript" src="jquery.jstree.min.js"&gt;&lt/script&gt;
</pre></div>

<p>You may change the path to whatever you like, but it is
recommended not to rename <code>jquery.tree.js</code> or <code>jquery.tree.min.js</code>
as the filenames may be used for path autodetection (for example in the
<code>themes</code> plugin, but if you really need to rename the file
most plugins will give you the option to set the path manually).</p>

<p>Additionally some plugins have dependencies - plugins that detect
a dependency is missing will throw an error.</p>

<h3>Creating and configuring an instance</h3>
<p>You can create a tree in the following manner:</p>

<div class="code_f"><pre class="brush:js;">
jQuery("some-selector-to-container-node-here").jstree([ config_object ]);
</pre></div>

<p>In the optional config object you specify all the options that
you want to set. Each plugin will describe its configuration and
defaults. In the <a href="#configuration">configuration section</a>
below you will find the options defined by the core. Each plugin's
options (even the core) are set in their own subobject, which is named
after the plugin. For example all of the core's options are set in the <code>core</code>
key of the config object:</p>
<div class="code_f"><pre class="brush:js;">
jQuery("some-selector-to-container-node-here")
	.jstree({
		core : {
			/* core options go here */
		}
	});
</pre></div>

<p class="note">Please note that if your options for a given plugin
are the same as the defaults you may omit those options or omit the
subobject completely (if you do not need to modify the defaults).</p>

<p>There is only one special config option that is not a part of any
plugin - this is the <code>plugins</code> option, which defines a list
of active plugins for the instance being created. Although many plugins
may be included, only the ones listed in this option will be active. The
only autoincluded "plugin" is the jstree core.</p>

<div class="code_f"><pre class="brush:js;">
jQuery("some-selector-to-container-node-here")
	.jstree({
		core : { /* core options go here */ },
		plugins : { "themes", "html_data", "some-other-plugin" }
	});
</pre></div>

<h3>Interacting with the tree</h3>

<p>To perform an operation programatically on a given instance you
can use two methods:</p>
<div class="code_f"><pre class="brush:js;">
/* METHOD ONE */
jQuery("some-selector-to-container-node-here")
	.jstree("operation_name" [, argument_1, argument_2, ...]);

/* METHOD TWO */
jQuery.jstree._reference(needle) 
	/* NEEDLE can be a DOM node or selector for the container or a node within the container */
	.operation_name([ argument_1, argument_2, ...]);
</pre></div>

<p>jsTree uses events to notify of any changes happening in the
tree. All events fire on the tree container in the <code>jstree</code>
namespace and are named after the function that triggered them. Please
note that for some events it is best to bind before creating the
instance. For example:</p>
<div class="code_f"><pre class="brush:js;">
jQuery("some-container")
	.bind("loaded.jstree", function (event, data) {
		alert("TREE IS LOADED");
	})
	.jstree({ /* configuration here */ });
</pre></div>
<p>Please note the second parameter <code>data</code>. Its structure
is as follows:</p>
<div class="code_f"><pre class="brush:js;">
{ 
	"inst" : /* the actual tree instance */, 
	"args" : /* arguments passed to the function */, 
	"rslt" : /* any data the function passed to the event */, 
	"rlbk" : /* an optional rollback object - it is not always present */
}
</pre></div>
<p>There is also one special event - <code>before.jstree</code>.
This events enables you to prevent an operation from executing. Look at
the <a href="#demos">demo</a> below.</p>

</div>

<h2 id="configuration">Configuration</h2>
<div class="panel configuration">

<h3>html_titles</h3>
<p class="meta">Boolean. Default is <code>false</code>.</p>
<p>Defines whether titles can contain HTML code.</p>

<h3>animation</h3>
<p class="meta">A number. Default is <code>500</code>.</p>
<p>Defines the duration of open/close animations. <code>0</code>
means no animation.</p>

<h3>initially_open</h3>
<p class="meta">An array. Default is <code>[]</code>.</p>
<p>Defines which nodes are to be automatically opened when the tree
finishes loading - a list of IDs is expected.</p>

</div>

<h2 id="demos">Demos</h2>
<div class="panel">

<h3>Binding to an event and executing an action</h3>
<input type="button" class="button" value="toggle_node" id="toggle_node"
	style="clear: both;" />
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
	$("#toggle_node").click(function () { 
		$("#demo1").jstree("toggle_node","#phtml_1");
	});
	$("#demo1")
		.bind("open_node.jstree close_node.jstree", function (e) {
			$("#log1").html("Last operation: " + e.type);
		})
		.jstree({ "plugins" : [ "themes", "html_data" ] });
	});
</script>
<p class="log" id="log1" style="clear: both;">&#160;</p>

<h3>Preventing an action</h3>
<p>This is the same demo as above, but this time the operation will
be prevented.</p>
<input type="button" class="button" value="toggle_node"
	id="toggle_node2" style="clear: both;" />
<div id="demo2" class="demo">
<ul>
	<li id="html_1"><a href="#">Root node 1</a>
	<ul>
		<li id="html_2"><a href="#">Child node 1</a></li>
		<li id="html_3"><a href="#">Child node 2</a></li>
	</ul>
	</li>
	<li id="html_4"><a href="#">Root node 2</a></li>
</ul>
</div>
<script type="text/javascript">
$(function () {
	$("#toggle_node2").click(function () { 
		$("#demo2").jstree("toggle_node","#html_1");
	});
	$("#demo2")
		.bind("open_node.jstree close_node.jstree", function (e) {
			$("#log2").html("Last operation: " + e.type);
		})
		.jstree({ "plugins" : [ "themes", "html_data" ] });
	});
</script> <script type="text/javascript" class="source">
$(function () {
	$("#demo2").bind("before.jstree", function (e, data) {
		if(data.func === "open_node") {
			$("#log2").html(data.args[0].attr("id"));
			e.stopImmediatePropagation();
			return false;
		}
	});
});
</script>
<p class="log" id="log2" style="clear: both;">&#160;</p>
<p>The important part is <code>e.stopImmediatePropagation();
return false</code>.</p>
</div>

<h2 id="api">API</h2>
<div class="panel api">
<p class="note">Use extra caution when working with functions
prefixed with an underscore - <code>_</code>!<br />
Those functions are probably for internal usage only.</p>


<h3 id="jstree.defaults">jQuery.jstree.defaults</h3>
<p class="meta">An object. Default is a collection of all included
plugin's defaults.</p>
<p>This object is exposed so that you can apply standart settings to
all future instances</p>

<h3 id="jstree.plugin">jQuery.jstree.plugin ( plugin_name ,
plugin_data )</h3>
<p>This function is used by developers to extend jstree (add
"plugins").</p>
<ul class="arguments">
	<li><code class="tp">string</code> <strong>plugin_name</strong>
	<p>The plugin name - it should be unique.</p>
	</li>
	<li><code class="tp">object</code> <strong>plugin_data</strong>
	<p>The plugin itself. It consists of <code>__init</code> &amp; <code>__destroy</code>
	functions, <code>defaults</code> object (that of course could be an
	array or a simple value) and a <code>_fn</code> object, whose keys are
	all the functions you are extending jstree with. You can overwrite
	functions (but you can in your function call the overriden old
	function), and you are responsible for triggering events and setting
	rollback points. You can omit any of the elements in the <code>plugin_data</code>
	param. Keep in mind jstree will automatically clear classes prepended
	with <code>jstree-</code> and all events in the <code>jstree</code>
	namespace when destroying a tree, so you do not need to worry about
	those.</p>
	<p>Read jstree's code for examples on how to develop plugins.</p>
	</li>
</ul>

<h3 id="jstree.rollback">jQuery.jstree.rollback ( rollback_object )</h3>
<p>This function will roll the tree back to the state specified by
the rollback object</p>
<ul class="arguments">
	<li><code class="tp">string</code> <strong>rollback_object</strong>
	<p>Normally you will get this object from the event you are
	handling. You can of course use <code>.get_rollback()</code> to get the
	current state of the tree as a rollback object.</p>
	<div class="code_f"><pre class="brush:js;">
$("some-container").bind("some-event.jstree", function (e, data) {
	$.jstree.rollback(data.rlbk);
});</pre></div>
	<p>Keep in mind that not all events will give you a rollback object
	- sometimes <code>data.rlbk</code> will be <code>false</code>.</p>
	</li>
</ul>

<h3 id="jstree._focused">jQuery.jstree._focused ()</h3>
<p>Returns the currently focused tree instance on the page. If not
interaction has been made - it is the last one to be created.</p>

<h3 id="jstree._reference">jQuery.jstree._reference ( needle )</h3>
<p>Returns the tree instance for the specified <code>needle</code>.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>needle</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to the
	tree container, or an element within the tree.</p>
	</li>
</ul>

<h3 id="jstree._instance">jQuery.jstree._instance ( index ,
container , settings )</h3>
<p>This function is used internally when creating new tree
instances. Calling this function by itself is not enough to create a new
instance. To create a tree use the documented method <code>$("selector").jstree([
options ])</code>.</p>

<h3 id="jstree._fn">jQuery.jstree._fn</h3>
<p>This object stores all functions included by plugins. It is used
internally as a prototype for all instances - do not modify manually.</p>

<h3 id="data">.data</h3>
<p>An object where all plugins store instance specific data. Do not
modify manually.</p>

<h3 id="get_settings">.get_settings ()</h3>
<p>Returns the instance's settings object - the defaults, extended
by your own config object.</p>

<h3 id="get_index">.get_index ()</h3>
<p>Returns the internal instance index.</p>

<h3 id="get_container">.get_container ()</h3>
<p>Returns the jQuery extended container node of the tree.</p>

<h3 id="_set_settings">._set_settings ( settings )</h3>
<p>Replace the settings object with the <code>settings</code> param.
Please note that not all plugins will react to the change. Unless you
know exactly what you are doing you'd be better off recreating the tree
with the new settings.</p>

<h3 id="init">.init ()</h3>
<p>This function is used internally when creating a new instance.
Triggers an event, which fires after the tree is initialized, but not
yet loaded.</p>

<h3 id="destroy">.destroy ()</h3>
<p>Destroys the instance - it will automatically remove all bound
events in the <code>jstree</code> namespace &amp; remove all classes
starting with <code>jstree-</code>. Triggers an event.</p>

<h3 id="save_opened">.save_opened ()</h3>
<p>Stores the currently open nodes before refreshing. Used
internally. Triggers an event.</p>

<h3 id="reopen">.reopen ( is_callback )</h3>
<p>Reopens all the nodes stored by <code>save_opened</code> or set
in the <code>initially_open</code> config option on first load. It is
called multiple times while reopening nodes - the <code>is_callback</code>
param determines if this is the first call (<code>false</code>) or not.
Used internally. Triggers an event.</p>

<h3 id="refresh">.refresh ( node )</h3>
<p>Refreshes the tree. Saves all open nodes, and reloads and then
reopens all saved nodes. Triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree. If set this will reload only the given node -
	otherwise - the whole tree. Passing <code>-1</code> also reloads the
	whole tree.</p>
	</li>
</ul>

<h3 id="loaded">.loaded ()</h3>
<p>A dummy function, whose purpose is only to trigger the loaded
event. This event is triggered once after the tree's root nodes are
loaded, but before any nodes set in <code>initially_open</code> are
opened.</p>

<h3 id="set_focus">.set_focus ()</h3>
<p>Makes the current instance the focused one on the page. Triggers
an event.</p>

<h3 id="is_focused">.is_focused ()</h3>
<p>Returns <code>true</code> if the current instance is the focused
one, otherwise returns <code>false</code>.</p>

<h3 id="_get_node">._get_node ( node )</h3>
<p>Return the jQuery extended <code>LI</code> element of the node, <code>-1</code>
if the container node is passed, or <code>false</code> otherwise.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree.</p>
	</li>
</ul>

<h3 id="_get_next">._get_next ( node , strict )</h3>
<p>Gets the <code>LI</code> element representing the node next to
the passed <code>node</code>. Returns <code>false</code> on failure.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree, whose next sibling we want.</p>
	</li>
	<li><code class="tp">bool</code> <strong>strict</strong>
	<p>If set to <code>true</code> only immediate siblings are
	calculated. Otherwise if the <code>node</code> is the last child of its
	parent this function will "jump out" and return the parent's next
	sibling, etc. Default is <code>false</code>.</p>
	</li>
</ul>

<h3 id="_get_prev">._get_prev ( node , strict )</h3>
<p>Gets the <code>LI</code> element representing the node previous
to the passed <code>node</code>. Returns <code>false</code> on failure.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree, whose previous sibling we want.</p>
	</li>
	<li><code class="tp">bool</code> <strong>strict</strong>
	<p>If set to <code>true</code> only immediate siblings are
	calculated. Otherwise if the <code>node</code> is the first child of
	its parent this function will "jump out" and return the parent itself.
	Default is <code>false</code>.</p>
	</li>
</ul>

<h3 id="_get_parent">._get_parent ( node )</h3>
<p>Gets the <code>LI</code> element representing the parent of the
passed <code>node</code>. Returns <code>false</code> on failure.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree, whose parent we want.</p>
	</li>
</ul>

<h3 id="_get_children">._get_children ( node )</h3>
<p>Gets the <code>LI</code> elements representing the children of
the passed <code>node</code>. Returns <code>false</code> on failure (or
if the node has no children).</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree, whose children we want. Use <code>-1</code> to
	return all root nodes.</p>
	</li>
</ul>

<h3 id="get_path">.get_path ( node , id_mode )</h3>
<p>Return the path to a node, either as an array of IDs or as an
array of node names.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element within the tree, whose path we want.</p>
	</li>
	<li><code class="tp">bool</code> <strong>id_mode</strong>
	<p>If set to <code>true</code> IDs are returned instead of the
	names of the parents. Default is <code>false</code>.</p>
	</li>
</ul>

<h3 id="open_node">.open_node ( node , callback , skip_animation )</h3>
<p>Opens a closed node, so that its children are visible. If the <code>animation</code>
config option is greater than <code>0</code> the children are revealed
using a slide down animation, whose duration is the value of the <code>animation</code>
config option in milliseconds. Triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element we want opened.</p>
	</li>
	<li><code class="tp">function</code> <strong>callback</strong>
	<p>A callback function executed once the node is opened. Used
	mostly internally, you'd be better of waiting for the event. You can
	skip this, by not specifying it, or by passing <code>false</code>.</p>
	</li>
	<li><code class="tp">bool</code> <strong>skip_animation</strong>
	<p>If set to <code>true</code> the animation set in the <code>animation</code>
	config option is skipped. Default is <code>false</code>.</p>
	</li>
</ul>

<h3 id="close_node">.close_node ( node , skip_animation )</h3>
<p>Closes an open node, so that its children are not visible. If the
<code>animation</code> config option is greater than <code>0</code> the
children are hidden using a slide up animation, whose duration is the
value of the <code>animation</code> config option in milliseconds.
Triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element we want closed.</p>
	</li>
	<li><code class="tp">bool</code> <strong>skip_animation</strong>
	<p>If set to <code>true</code> the animation set in the <code>animation</code>
	config option is skipped. Default is <code>false</code>.</p>
	</li>
</ul>

<h3 id="toggle_node">.toggle_node ( node )</h3>
<p>If a node is closed - this function opens it, if it is open -
calling this function will close it.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element we want toggled.</p>
	</li>
</ul>

<h3 id="open_all">.open_all ( node , original_obj )</h3>
<p>Opens all descendants of the <code>node</code> node.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element whose descendants you want opened. If this param is omitted or
	set to <code>-1</code> all nodes in the tree are opened.</p>
	</li>
	<li><code class="tp">mixed</code> <strong>original_obj</strong>
	<p>Used internally when recursively calling the same function - do
	not pass this param.</p>
	</li>
</ul>

<h3 id="close_all">.close_all ( node )</h3>
<p>Closes all descendants of the <code>node</code> node.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element whose descendants you want closed. If this param is omitted or
	set to <code>-1</code> all nodes in the tree are closed.</p>
	</li>
</ul>

<h3 id="clean_node">.clean_node ( node )</h3>
<p>Applies all necessary classes to the <code>node</code> and its
descendants. Used internally. Triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element you want cleaned. If this param is omitted or set to <code>-1</code>
	all nodes in the tree are cleaned.</p>
	</li>
</ul>

<h3 id="get_rollback">.get_rollback ()</h3>
<p>Get the current tree's state in the rollback format. Used mainly
internally by plugins.</p>

<h3 id="set_rollback">.set_rollback ( html , data )</h3>
<p>Rollback the tree. Used ONLY internally! Both arguments are part
of the rollback object. If you need to rollback - take a look at <a
	href="#jstree.rollback">jQuery.jstree.rollback()</a>. Triggers event.</p>

<h3 id="load_node">.load_node ( node , success_callback ,
error_callback )</h3>
<p>A dummy function that is overwritten by data plugins. Triggers
event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element you want loaded. Use <code>-1</code> for root nodes.</p>
	</li>
	<li><code class="tp">function</code> <strong>success_callback</strong>
	<p>A function to be executed once the node is loaded successfully -
	used internally. You should wait for the event.</p>
	</li>
	<li><code class="tp">function</code> <strong>error_callback</strong>
	<p>A function to be executed if the node is not loaded due to an
	error - used internally. You should wait for the event.</p>
	</li>
</ul>

<h3 id="_is_loaded">._is_loaded ( node )</h3>
<p>A dummy function that should return <code>true</code> if the
node's children are loaded or <code>false</code> otherwise.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to an
	element you want to check.</p>
	</li>
</ul>

<h3 id="create_node">.create_node ( node , position , js , callback
, is_loaded )</h3>
<p>Creates the DOM structure necessary for a new node. Triggers an
event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to the
	element you want to create in (or next to).</p>
	</li>
	<li><code class="tp">mixed</code> <strong>position</strong>
	<p>The position of the newly created node. This can be a zero based
	index to position the element at a specific point among the current
	children. You can also pass in one of those strings: <code>"before"</code>,
	<code>"after"</code>, <code>"inside"</code>, <code>"first"</code>, <code>"last"</code>.</p>
	</li>
	<li><code class="tp">object</code> <strong>js</strong>
	<p>The data for the newly created node. Consists of three keys:</p>
	<p style="margin-left: 25px;"><code class="tp">attr</code> - an
	object of attributes (same used for <code>jQuery.attr()</code>. You can
	omit this key;<br />
	<code class="tp">state</code> - a string - either <code>"open"</code>
	or <code>"closed"</code>, for a leaf node - omit this key;<br />
	<code class="tp">data</code> - a string or an object - if a string is
	passed it is used for the title of the node, if an object is passed
	there are two keys you can specify: <code>attr</code> and <code>title</code>;</p>
	</li>
	<li><code class="tp">function</code> <strong>callback</strong>
	<p>A function to be executed once the node is created - used
	internally. You should wait for the event.</p>
	</li>
	<li><code class="tp">bool</code> <strong>is_loaded</strong>
	<p>Specifies if the parent of the node is loaded or not - used ONLY
	internally.</p>
	</li>
</ul>

<h3 id="get_text">.get_text ( node )</h3>
<p>Returns the title of a node.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to the
	element whose title you need.</p>
	</li>
</ul>

<h3 id="set_text">.set_text ( node , text )</h3>
<p>Sets the title of a node. Triggers an event. This is used mostly
internally - wait for a <a href="#rename_node">.rename_node event</a> to
avoid confusion.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to the
	element whose title you want to change.</p>
	</li>
	<li><code class="tp">string</code> <strong>text</strong>
	<p>The new title.</p>
	</li>
</ul>

<h3 id="rename_node">.rename_node ( node , text )</h3>
<p>Sets the title of a node. Triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to the
	element whose title you want to change.</p>
	</li>
	<li><code class="tp">string</code> <strong>text</strong>
	<p>The new title.</p>
	</li>
</ul>

<h3 id="delete_node">.delete_node ( node )</h3>
<p>Removes a node. Triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to the
	element you want to remove.</p>
	</li>
</ul>

<h3 id="prepare_move">.prepare_move ( o , r , pos , cb , is_cb )</h3>
<p>This function is used internally to prepare all necessary
variables and nodes when moving a node around. It is automatically
called as needed - you do not need to call it manually. Triggers an
event.</p>

<h3 id="check_move">.check_move ()</h3>
<p>Checks if the prepared move is a valid one.</p>

<h3 id="move_node">.move_node ( node , ref , position , is_copy ,
is_prepared , skip_check )</h3>
<p>Moves a node to a new place. Triggers an event.</p>
<ul class="arguments">
	<li><code class="tp">mixed</code> <strong>node</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to the
	element you want to move.</p>
	</li>
	<li><code class="tp">mixed</code> <strong>ref</strong>
	<p>This can be a DOM node, jQuery node or selector pointing to the
	element which will be the reference element in the move. <code>-1</code>
	may be used too (to indicate the container node).</p>
	</li>
	<li><code class="tp">mixed</code> <strong>position</strong>
	<p>The new position of the moved node. This can be a zero based
	index to position the element at a specific point among the reference
	node's current children. You can also use one of these strings: <code>"before"</code>,
	<code>"after"</code>, <code>"inside"</code>, <code>"first"</code>, <code>"last"</code>.</p>
	</li>
	<li><code class="tp">bool</code> <strong>is_copy</strong>
	<p>Should this be a copy or a move operation.</p>
	</li>
	<li><code class="tp">bool</code> <strong>is_prepared</strong>
	<p>Used internally when this function is called recursively.</p>
	</li>
	<li><code class="tp">bool</code> <strong>skip_check</strong>
	<p>If this is set to <code>true</code> <a href="#check_move">check_move</a>
	is not called.</p>
	</li>
</ul>

<h3 id="_get_move">._get_move ()</h3>
<p>Returns the lastly prepared move. The returned object contains:<br />
<code>.o</code> - the node being moved<br />
<code>.r</code> - the reference node in the move<br />
<code>.ot</code> - the origin tree instance<br />
<code>.rt</code> - the reference tree instance<br />
<code>.p</code> - the position to move to (may be a string - <code>"last"</code>,
<code>"first"</code>, etc)<br />
<code>.cp</code> - the calculated position to move to (always a number)<br />
<code>.np</code> - the new parent<br />
</p>

</div>

</div>
</body>
</html>