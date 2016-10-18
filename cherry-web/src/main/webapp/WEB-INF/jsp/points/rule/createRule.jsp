<%@ page contentType="text/html; charset=gb2312" language="java"
	errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>WITPOS 店务通</title>
<link rel="stylesheet" href="/Cherry/css/common/witpos3.css" type="text/css"
	title="" />
<link rel="stylesheet" href="/Cherry/css/jquery/themes/base/ui.all.css"
	type="text/css" />
<link rel="stylesheet" href="/Cherry/css/common/demos.css" type="text/css" />
<script language="javascript" src="/Cherry/js/jquery/jquery-1.4.2.min.js"></script>
<script language="javascript" src="/Cherry/js/jquery/jquery-ui.min.js"></script>
<script type="text/javascript" src="/Cherry/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/Cherry/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="/Cherry/js/jquery/jquery.hotkeys.js"></script>
<script type="text/javascript" src="/Cherry/js/jquery/jquery.jstree.js"></script>
<script type="text/javascript" src="/Cherry/js/jquery/!script.js"></script>
<script language="javascript"> 
   
var LastLeftID = "";    
   
function DoMenu(emid){    
     var obj = document.getElementById(emid);    
    obj.className = (obj.className.toLowerCase() == "expanded"?"collapsed":"expanded");    
     if((LastLeftID!="")&&(emid!=LastLeftID)){    
         document.getElementById(LastLeftID).className = "collapsed";    
     }    
        LastLeftID = emid;    
}      
</script>

<script type="text/javascript">
var strobj="";
var $clickParentObj;
	$(function() {
		$('#export_dropbox_content1').hide();
		
		$('#addRelation').click(function(){
				$('#export_dropbox_content1').show();
				$clickParentObj =$("#show_rule");
			});
		
		$("#submit_rel").click(
				function(){
					
					var addHtml ="";
					strobj = $("input[name='rel']:checked").val(); 
					var strVal=$("input[name='rel']:checked").val(); 
					if (strVal == "所有条件满足"){
						addHtml = "<ul id=\"allUL\" style=\"margin-left:30px;list-style-type:none;\"><li>所有条件满足</li></ul>";
		
					}else if (strVal == "任意条件满足"){
						addHtml = "<ul id=\"anyUL\" style=\"margin-left:30px;list-style-type:none;\"><li>任意条件满足</li></ul>";
						
					}	
					//alert ($clickParentObj.html()+addHtml);
					
					$clickParentObj.html($clickParentObj.html()+addHtml);
					//alert(strVal);
					//add code
					$('#export_dropbox_content1').hide();
				});
		$("#cancel_rel").click(
				function(){
					$('#export_dropbox_content1').hide();
				});
		
		$('#export_dropbox_content').hide();
		$('#addCondition').click(function(){
				$('#export_dropbox_content').show();
				$clickParentObj =$("#show_rule");
			});
		$("#submit").click(
				function(){
					var menu = $("#demo1").jstree("get_checked");
					for(var i=0;i<menu.size();i++){
						var addHtml ="";
						if (menu[i].id == "node_1"){
							addHtml = "<ul id=\"startUL\" style=\"margin-left:30px;list-style-type:none;\"><li><ul id=\"startUL\" style=\"margin-left:30px;list-style-type:none;\"><li>会员年龄<input type=\"text\" /></li><li>会员年龄<input type=\"text\" /></li><li>入会时间<input type=\"text\" /></li></ul></li></ul>";
							
						}
						else if (menu[i].id == "node_11"){
							addHtml = "<ul id=\"startUL\" style=\"margin-left:30px;list-style-type:none;\"><li>会员年龄<input type=\"text\" /></li></ul>";
							
						}else if (menu[i].id == "node_12"){
							addHtml = "<ul id=\"endUL\" style=\"margin-left:30px;list-style-type:none;\"><li>会员生日<input type=\"text\" /></li></ul>";
							
						}
						else if (menu[i].id == "node_13"){
							addHtml = "<ul id=\"endUL\" style=\"margin-left:30px;list-style-type:none;\"><li>入会时间<input type=\"text\" /></li></ul>";
							
						}else if (menu[i].id == "node_2"){
							addHtml = "<ul id=\"startUL\" style=\"margin-left:30px;list-style-type:none;\"><li><ul id=\"startUL\" style=\"margin-left:30px;list-style-type:none;\"><li>国家<input type=\"text\" /></li><li>省<input type=\"text\" /></li><li>市<input type=\"text\" /></li><li>区<input type=\"text\" /></li><li>柜台<input type=\"text\" /></li></ul></li></ul>";
							
						}else if (menu[i].id == "node_21"){
							addHtml = "<ul id=\"endUL\" style=\"margin-left:30px;list-style-type:none;\"><li>国家<input type=\"text\" /></li></ul>";
						}
						else if (menu[i].id == "node_22"){
							addHtml = "<ul id=\"endUL\" style=\"margin-left:30px;list-style-type:none;\"><li>省<input type=\"text\" /></li></ul>";
						}
						else if (menu[i].id == "node_23"){
							addHtml = "<ul id=\"endUL\" style=\"margin-left:30px;list-style-type:none;\"><li>市<input type=\"text\" /></li></ul>";
						}
						else if (menu[i].id == "node_24"){
							addHtml = "<ul id=\"endUL\" style=\"margin-left:30px;list-style-type:none;\"><li>区<input type=\"text\" /></li></ul>";
						}
						else if (menu[i].id == "node_25"){
							addHtml = "<ul id=\"endUL\" style=\"margin-left:30px;list-style-type:none;\"><li>柜台<input type=\"text\" /></li></ul>";
						}else if (menu[i].id == "node_3"){
							addHtml = "<ul id=\"startUL\" style=\"margin-left:30px;list-style-type:none;\"><li><ul id=\"startUL\" style=\"margin-left:30px;list-style-type:none;\"><li>开始时间<input type=\"text\" /></li><li>结束时间<input type=\"text\" /></li></ul></li></ul>";
						}else if (menu[i].id == "node_31"){
							addHtml = "<ul id=\"endUL\" style=\"margin-left:30px;list-style-type:none;\"><li>开始时间<input type=\"text\" /></li></ul>";
						}else if (menu[i].id == "node_32"){
							addHtml = "<ul id=\"endUL\" style=\"margin-left:30px;list-style-type:none;\"><li>结束时间<input type=\"text\" /></li></ul>";
						}
						$clickParentObj.html($clickParentObj.html()+addHtml);
						strobj+=menu[i].id+";";
					}
					//alert(strobj);
					$('#export_dropbox_content').hide();

				});
		$("#cancel").click(
				function(){
					$('#export_dropbox_content').hide();
				});
		
		//search button click function
		$("#search").click(
				function() {
					$("#demo1").jstree("search",
							document.getElementById("text").value);
				});
		// Settings up the tree - using $(selector).jstree(options);
		$("#demo1").jstree(
				{
					//configure plugin json_data, only data could have function
					"json_data" : {
						"ajax" : {
							// the URL to fetch the data
							"url" : "_search_data.json",
							// this function is executed in the instance's scope
							// the parameter is the node being loaded (may be -1, 0, or undefined when loading the root nodes)
							"data" : function(n) {
								// the result is fed to the AJAX request `data` option
								return {
									"operation" : "get_children",
									"id" : n.attr ? n.attr("id").replace(
											"node_", "") : 1
								};
							}
						}
					},
					//configure plugin search
					"search" : {
						//async search,`ajax` config option is actually jQuery's object, only data can be function
						"ajax" : {
							"url" : "_search_data.json",
							// You get the search string as a parameter
							"data" : function(str) {
								return {
									"operation" : "search",
									"search_str" : str
								};
							}
						}
					},
					// For UI & core - the nodes to initially select and open will be overwritten by the cookie plugin
					//configure plugin ui handling selecting/deselecting/hovering nodes
					"ui" : {
						"initially_select" : [ "node_11" ]
					},
					//configure plugin core just openning the nodes up
					"core" : {
						"initially_open" : [ "node_1" ]
					},
					// the list of plugins to include
					"plugins" : [ "themes", "html_data", "ui", "checkbox",
							"demo1", "crrm", "sort", "dnd", "contexmenu",
							"search" ]
				}).bind(
				"search.jstree",
				function(e, data) {
					alert("found " + data.rslt.nodes.length
							+ "nodes matching '" + data.rslt.str + "'.");
				});
	});
</script>
</head>
<body class="report_body">
<div id="container">
<div id="header"><jsp:include page="/WEB-INF/jsp/common/header.inc.jsp" flush="true" /></div>

<div
	style="margin: 0px; height: 25px; width: 100%; float: left; margin: 0 0 1em">
<b class="head_border"><b class="head_border_layerC"></b> <b
	class="head_border_layerB"></b> <b class="head_border_layerA"></b> </b>
<div id="subheader">
<div id="menu">
<ul>
	<li class='m_li_a'><a href="#" onFocus="this.blur()">会员管理</a></li>
	<li class='m_li_a'><a href="#" onFocus="this.blur()">积分管理</a></li>
	<li class='m_li_a'><a href="#" onFocus="this.blur()">活动管理</a></li>
	<li class='m_li_a'><a href="#" onFocus="this.blur()">XXX 管理</a></li>
	<li class='m_li_a'><a href="#" onFocus="this.blur()">XXX 管理</a></li>
	<li class='m_li_a'><a href="#" onFocus="this.blur()">XXX 管理</a></li>
</ul>
</div>
</div>
<b class="head_border"> <b class="head_border_layerA"></b> <b
	class="head_border_layerB"></b> <b class="head_border_layerC"></b> </b></div>


<div id="main_content"><b class="round_border"><b
	class="round_border_layer3"></b> <b class="round_border_layer2"></b> <b
	class="round_border_layer1"></b> </b>
<div class="round_border_content min_size">

<div style="width: 99%;"><b class="main_itle"> <b
	class="main_itle_layerC"></b> <b class="main_itle_layerB"></b> <b
	class="main_itle_layerA"></b> </b>
<div id="main_title">积分管理&gt;&gt;积分计算</div>
<b class="main_itle"> <b class="main_itle_layerA"></b> <b
	class="main_itle_layerB"></b> <b class="main_itle_layerC"></b> </b></div>

<div class="main_bt">积分规则制定：</div>
<table width="90%">
	<tr>
		<td style="margin-left: 20px;" width="21%">请添加积分规则关系<a
			id="addRelation" href="#"><img src="/Cherry/images/common/On.gif" /></a>
		</td>
		<td width="56%"></td>
		<td align="left" width="21%">请添加积分规则条件<a id="addCondition"
			href="#"><img src="/Cherry/images/common/On.gif" /></a></td>
	</tr>
</table>
<div id="show_rule"></div>
<div style="border: 1px solid #aaa;" id="export_dropbox_content1">
<div id="demo4" class="demo">
<table>
	<tr>
		<td><input id="node_00" type="radio" name="rel" value="所有条件满足" />所有条件满足</td>
	</tr>
	<tr>
		<td><input id="node_01" type="radio" name="rel" value="任意条件满足" />任意条件满足</td>
	</tr>
</table>
</div>
<div id="demo5" style="width: 99%; overflow: auto; margin-bottom: 5px;">
<input id="submit_rel" type="button" value="应用" style="float: left;">
<input id="cancel_rel" type="button" value="取消" style="float: left;">
</div>
</div>

<div style="border: 1px solid #aaa;" id="export_dropbox_content">
<div id="demo2" style="width: 99%; overflow: auto; margin-top: 5px;">
<input id="text" style="display: block; float: left;" type="text">
<input id="search" value="search" style="display: block; float: left;"
	type="button"></div>
<div id="demo1" class="demo">
<ul>
	<li class="jstree-open" id="node_1" value="会员相关"><ins
		class="jstree-icon">&nbsp;</ins><a class="" href="#"><ins
		class="jstree-icon">&nbsp;</ins>会员相关</a>
	<ul>
		<li class="jstree-leaf" id="node_11" value="会员生日"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>会员生日</a></li>
		<li class="jstree-leaf" id="node_12" value="会员年龄"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>会员年龄</a></li>
		<li class="jstree-last jstree-leaf" id="node_13" value="入会时间"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>入会时间</a></li>
	</ul>
	</li>
	<li class="jstree-open" id="node_2" value="购买地点"><ins
		class="jstree-icon">&nbsp;</ins><a class="" href="#"><ins
		class="jstree-icon">&nbsp;</ins>购买地点</a>
	<ul>
		<li class="jstree-leaf" id="node_21" value="国家"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>国家</a></li>
		<li class="jstree-leaf" id="node_22" value="省"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>省</a></li>
		<li class="jstree-leaf" id="node_23" value="市"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>市</a></li>
		<li class="jstree-leaf" id="node_24" value="区"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>区</a></li>
		<li class="jstree-last jstree-leaf" id="node_25" value="柜台"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>柜台</a></li>
	</ul>
	</li>
	<li class="jstree-last jstree-open" id="node_3" value="购买时间"><ins
		class="jstree-icon">&nbsp;</ins><a class=" href="#"><ins
		class="jstree-icon">&nbsp;</ins>购买时间</a>
	<ul>
		<li class="jstree-leaf" id="node_31" value="开始时间"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>开始时间</a></li>
		<li class="jstree-last jstree-leaf" id="node_32" value="结束时间"><ins
			class="jstree-icon">&nbsp;</ins><a href="#"><ins
			class="jstree-icon">&nbsp;</ins>结束时间</a></li>
	</ul>
	</li>
</ul>
</div>
<div id="demo3" style="width: 99%; overflow: auto; margin-top: 10px;">
<input id="cancel" type="button" value="取消" style="float: right;">
<input id="submit" type="button" value="应用" style="float: right;">
</div>
</div>

</div>
<b class="round_border"> <b class="round_border_layer1"></b> <b
	class="round_border_layer2"></b> <b class="round_border_layer3"></b> </b></div>

<div id="main_context">
<div class="menu-list" id="report_nav" style="float: left;"><b
	class="round_border"> <b class="round_border_layer3"></b> <b
	class="round_border_layer2"></b> <b class="round_border_layer1"></b> </b>
<div class="round_border_content">
<div id="left_Box1">
<div id="left_img" style="width: 30px; margin-top: 5px;"><img
	src="/Cherry/images/common/left_li01.gif"></div>
<div id="left_img">系统消息</div>
<div id="left_xiaoxi">系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息系统消息
</div>
</div>

</div>

<b class="round_border"> <b class="round_border_layer1"></b> <b
	class="round_border_layer2"></b> <b class="round_border_layer3"></b> </b>

<div class="custom_reporting"><b class="round_border"> <b
	class="round_border_layer3"></b> <b class="round_border_layer2"></b> <b
	class="round_border_layer1"></b> </b>
<div class="round_border_content">
<div id="left_Box2">
<div id="nav">
<ul>
	<li>
	<table width="165" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="15"><img src="/tutorial/images/li01.gif" /></td>
			<td align="left"><a href="#Menu=ChildMenu1"
				onclick="DoMenu('ChildMenu1')" onFocus="this.blur()">积分规则设定</a></td>
		</tr>
	</table>
	<ul id="ChildMenu1" class="collapsed">
		<li><a href="#" onFocus="this.blur()">新建模板</a></li>
		<li><a href="#" onFocus="this.blur()">搜索编辑模板</a></li>
		<li><a href="#" onFocus="this.blur()">积分失效设定</a></li>
		<li><a href="#" onFocus="this.blur()">默认积分规则设定</a></li>
		<li><a href="#" onFocus="this.blur()">模拟计算</a></li>
	</ul>
	</li>
	<li>
	<table width="165" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="15"><img src="/Cherry/images/common/li01.gif" /></td>
			<td align="left"><a href="#Menu=ChildMenu2"
			onclick="DoMenu('ChildMenu2')" onFocus="this.blur()">积分规则设定</a></td>
		</tr>
	</table>
	<ul id="ChildMenu2" class="collapsed">
		<li><a href="#" onFocus="this.blur()">新建模板</a></li>
		<li><a href="#" onFocus="this.blur()">搜索编辑模板</a></li>
		<li><a href="#" onFocus="this.blur()">积分失效设定</a></li>
		<li><a href="#" onFocus="this.blur()">默认积分规则设定</a></li>
		<li><a href="#" onFocus="this.blur()">模拟计算</a></li>
	</ul>
	</li>
</ul>
</div>
</div>
</div>
<b class="round_border"> <b class="round_border_layer1"></b> <b
	class="round_border_layer2"></b> <b class="round_border_layer3"></b> </b></div>
</div>
</div>

<div id="footer" style="float: left;"><jsp:include
	page="/WEB-INF/jsp/common/foot.inc.jsp" flush="true" /></div>

</div>
</body>
</html>