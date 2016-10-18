<%@ page contentType="text/html; charset=gb2312" language="java"
	errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jsTree v.1.0 - CRRM documentation</title>
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

<h1>jsTree v.1.0 - search plugin</h1>
<h2>Description</h2>
<div id="description">
<p>The <code>search</code> plugin enables searching for nodes whose
title contains a given string, works on async trees too. All found nodes
get the <code>jstree-search</code> class applied to their contained <code>a</code>
nodes - you can use that class to style search results.</p>
</div>

<h2 id="configuration">Configuration</h2>
<div class="panel configuration">

<h3>case_insensitive</h3>
<p class="meta">A boolean. Default is <code>false</code>.</p>
<p>Whether to search in a case-insensitive manner or not.</p>

<h3>ajax</h3>
<p class="meta">An object (or <code>false</code> if not used).
Default is <code>false</code>.</p>
<p>This object can be used to make a request to the server on each
search - useful if you are using async trees. That way you can return an
array of IDs that need to be loaded before the actual DOM search is
performed (so that all the nodes that will match the search are loaded).
For example if the user searches for "string", you get that on the
server side, check the database and find out that there is a node
containing that string. But the node is the child of some other node,
etc - so in your response you must return the path to the node (without
the node itself) as ids: <code>["#root_node","#child_node_3"]</code>.
This means that jstree will load those two nodes before doing the client
side search, ensuring that your node will be visible.</p>
<p>The ajax config object is pretty much the same as the <a
	href="http://api.jquery.com/jQuery.ajax/">jQuery ajax settings
object</a>.</p>
<p>The only difference is that you can set the <code>data</code>
option to a function, that will be executed in the current tree's scope
(<code>this</code> will be the tree instance) and gets the search string
as a paramater. Whatever you return in the function will be sent to the
server as data.</p>
<p>The <code>error</code> and <code>success</code> functions (if
present) also fire in the context of the tree, and if you return a value
in the <code>success</code> function it will be used as the array of
IDs.</p>

</div>

<h2 id="demos">Demos</h2>
<div class="panel">

<h3>Searching nodes</h3>
<p>Do not open the node - instead - just press the button.</p>
<input type="button" class="button" value="Search" id="search" style="" />
<div id="demo1" class="demo"></div>
<script type="text/javascript" class="source">
	$(function() {
		$("#search").click(function() {
			$("#demo1").jstree("search", "TARGET");
		});
		$("#demo1").jstree( {
			"json_data" : {
				"data" : [ {
					"attr" : {
						"id" : "root_node"
					},
					"data" : "A closed node",
					"state" : "closed"
				} ],
				"ajax" : {
					"url" : "_search_data.json",
					"data" : function(n) {
						return {
							id : n.attr ? n.attr("id") : 0
						};
					}
				}
			},
			"search" : {
				"ajax" : {
					"url" : "_search_result.json"
				}
			},
			"plugins" : [ "themes", "json_data", "search" ]
		}).bind(
				"search.jstree",
				function(e, data) {
					alert("Found " + data.rslt.nodes.length
							+ " nodes matching '" + data.rslt.str + "'.");
				});
	});
</script></div>

<h2 id="api">API</h2>
<div class="panel api">

<h3 id="search">.search ( str , skip_async )</h3>
<p>Searches for nodes matching the supplied string. Triggers an
event.</p>
<ul class="arguments">
	<li><code class="tp">string</code> <strong>str</strong>
	<p>The string to search for.</p>
	</li>
	<li><code class="tp">boolean</code> <strong>skip_async</strong>
	<p>If set to <code>true</code> - skip the async search (if setup in
	the config). This is used mostly internally.</p>
	</li>
</ul>

<h3 id="clear_search">.clear_search ( )</h3>
<p>Clears the current search. This function is automatically called
when doing a new search. Triggers an event.</p>

<h3 id="_search_open">._search_open ( is_callback )</h3>
<p>Used internally if async is setup in the config. This functions
loads the nodes returned by the server one by one.</p>

</div>

</div>
</body>
</html>