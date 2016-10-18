<%@ page contentType="text/html; charset=gb2312" language="java"
	errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>checkbox</title>
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
<div id="demo" style="width: 99%; overflow: auto; margin-bottom: 5px;">
<input id="add_RuleRelation" value="addRuleRelation"
	style="display: block; float: left;" type="button""> <input
	id="add_RuleCondition" value="addRuleCondition"
	style="display: block; float: left;" type="button"> <input
	id="rename" value="rename" style="display: block; float: left;"
	type="button"> <input id="remove" value="remove"
	style="display: block; float: left;" type="button"> <input
	id="search" value="search" style="display: block; float: right;"
	type="button"> <input id="text"
	style="display: block; float: right;" type="text"></div>
<div id="demo1" class="demo">
<ul>
	<li class="jstree-open" id="node_1"><ins class="jstree-icon">&nbsp;</ins><a
		class="" href="#"><ins class="jstree-icon">&nbsp;</ins>会员相关</a>
	<ul>
		<li class="jstree-leaf" id="node_11"><ins class="jstree-icon">&nbsp;</ins><a
			href="#"><ins class="jstree-icon">&nbsp;</ins>会员生日</a></li>
		<li class="jstree-leaf" id="node_12"><ins class="jstree-icon">&nbsp;</ins><a
			href="#"><ins class="jstree-icon">&nbsp;</ins>会员年龄</a></li>
		<li class="jstree-last jstree-leaf" id="node_13"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>入会时间</a></li>
	</ul>
	</li>
	<li class="jstree-open" id="node_2"><ins class="jstree-icon">&nbsp;</ins><a
		class="" href="#"><ins class="jstree-icon">&nbsp;</ins>购买地点</a>
	<ul>
		<li class="jstree-leaf" id="node_21"><ins class="jstree-icon">&nbsp;</ins><a
			href="#"><ins class="jstree-icon">&nbsp;</ins>国家</a></li>
		<li class="jstree-leaf" id="node_22"><ins class="jstree-icon">&nbsp;</ins><a
			href="#"><ins class="jstree-icon">&nbsp;</ins>省</a></li>
		<li class="jstree-leaf" id="node_23"><ins class="jstree-icon">&nbsp;</ins><a
			href="#"><ins class="jstree-icon">&nbsp;</ins>市</a></li>
		<li class="jstree-leaf" id="node_24"><ins class="jstree-icon">&nbsp;</ins><a
			href="#"><ins class="jstree-icon">&nbsp;</ins>区</a></li>
		<li class="jstree-last jstree-leaf" id="node_25"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>柜台</a></li>
	</ul>
	</li>
	<li class="jstree-last jstree-open" id="node_3"><ins
		class="jstree-icon">&nbsp;</ins><a class=" href="#"><ins
		class="jstree-icon">&nbsp;</ins>购买时间</a>
	<ul>
		<li class="jstree-leaf" id="node_31"><ins class="jstree-icon">&nbsp;</ins><a
			href="#"><ins class="jstree-icon">&nbsp;</ins>开始时间</a></li>
		<li class="jstree-last jstree-leaf" id="node_32"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>结束时间</a></li>
	</ul>
	</li>
</ul>
</div>

<script type="text/javascript">
$(function(){
	//re name button click function
	$("#rename").click(function(){
		$("#demo1").jstree("rename");
	});
	//remove button click function
	$("#remove").click(function(){
		$("#demo1").jstree("remove");
	});		
	//add_RuleCondition button click function
	$("#add_RuleCondition").click(function(){
		$("#demo1").jstree("create"); 
	});
	//add_RuleRelation button click function
	$("#add_RuleRelation").click(function(){
		$("#demo1").jstree("create",null,"after","new",false,true); 
	});
	//search button click function
	$("#search").click(function(){
		$("#demo1").jstree("search", document.getElementById("text").value);
	});
	// Settings up the tree - using $(selector).jstree(options);
	$("#demo1")
	.jstree({ 
		//configure plugin json_data, only data could have function
		"json_data" :{
		 "ajax":{
			// the URL to fetch the data
		     "url":"_search_data.json",
		    // this function is executed in the instance's scope
			// the parameter is the node being loaded (may be -1, 0, or undefined when loading the root nodes)
		     "data":function(n){
			// the result is fed to the AJAX request `data` option
	              return{"operation" : "get_children", 
						"id" : n.attr ? n.attr("id").replace("node_","") : 1 
								};
	}
	}
	},
	//configure plugin search
	"search":{
		//async search,`ajax` config option is actually jQuery's object, only data can be function
		"ajax":{
		       "url":"_search_data.json",
		    // You get the search string as a parameter
			   "data" : function (str) {
				return { 
					"operation" : "search", 
					"search_str" : str 
				}; 
	}
	}
	},
	// For UI & core - the nodes to initially select and open will be overwritten by the cookie plugin
	//configure plugin ui handling selecting/deselecting/hovering nodes
		"ui" :{
		"initially_select" : ["node_11"]
	},
	//configure plugin core just openning the nodes up
	     "core" :{"initially_open" : ["node_1"]},
	  // the list of plugins to include
		"plugins" : [ "themes", "html_data", "ui", "checkbox", "demo1","crrm","sort","dnd","contexmenu","search"]
	})
	.bind("search.jstree",function(e,data){
		alert("found "+data.rslt.nodes.length+"nodes matching '"+data.rslt.str+"'.");
	});
});

</script></div>
</body>
</html>