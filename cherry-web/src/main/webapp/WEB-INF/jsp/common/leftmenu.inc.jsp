<%@ page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.*" %>
<%@ page import="org.w3c.dom.Document" %>
<%@ page import="org.w3c.dom.Element" %>
<%@ page import="org.w3c.dom.NodeList" %>
<%@ page import="org.w3c.dom.Node" %>
<%@ page import="org.w3c.dom.NamedNodeMap" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type=text/javascript>
var LastLeftID = "";   
function DoMenu(emid){
	//判断点击的菜单的层次
	var cengci =  emid.indexOf('B')>-1?2:1;
	 $aObj = $('#a' + emid);	 
	 $sObj = $('#s' + emid);

	//移除所有的css
	$("#u0leftmenu").find('.ui-corner-all').removeClass("ui-corner-all on");
	$("#u0leftmenu").find('a').removeClass("subon");

	//其它的同级菜单及同级菜单的子菜单去掉css
	$aObj.parents('li').children('a').addClass("ui-corner-all on");
    $sObj.toggleClass("ui-icon icon-list-expand");
    $sObj.toggleClass("ui-icon icon-list-contract");

	$uObj = $('#'+emid);
	if($uObj){
		$uObj.toggleClass("expanded");
		$uObj.toggleClass("collapsed");
	}
	if(cengci==2){
		$aObj.addClass("subon");
	}
	//IE圆角修复 & IE表单交互
	if((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0))
	{
		$('#u0leftmenu b.xtop').remove();	
		$('#u0leftmenu b.xbottom').remove();	
		$('#u0leftmenu a.on').append('<b class="xtop"><b class="xb1"></b><b class="xb2"></b><b class="xb3"></b><b class="xb4"></b></b><b class="xbottom"><b class="xb4"></b><b class="xb3"></b><b class="xb2"></b><b class="xb1"></b></b>');
	}
	return false;
}
</script>

<div class="panel ui-corner-all">
<form id="timeoutform" action="/Cherry/init" method="post"></form>
<div class="panel-header"><strong><span class="ui-icon icon-ttl-control"></span>&nbsp;</strong></div>
<div class="panel-content">  
<ul class="sidemenu" id="u0leftmenu">
<%
//String basePath = request.getContextPath();
//HashMap map = (HashMap)session.getAttribute(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
//if(map==null) return;
//Document doc =  (Document)map.get(request.getParameter("MENU_ID"));
//if(doc==null){return;}
//Node root=doc.getDocumentElement(); 
/** 如果root有子元素 */ 
//if(root.hasChildNodes()) 
// { 
   /** nodeList1 */ 
//    NodeList nodeList1 = root.getChildNodes(); 
   /** 循环取得ftp所有节点*/ 
//   for (int i=0;i<nodeList1.getLength();i++) 
//    { 
//	   Node tempNode1 = nodeList1.item(i);
//	   NamedNodeMap map1 = tempNode1.getAttributes();          
//       String linkName1 = "menuA" + i ;
%>

<s:iterator value="#session.leftmenuallcurrent.childList" id="leftMenu1" status="status1">
<li>
    <s:set name="linkname1" value="'menuA' + #status1.index"/>
	<a style="cursor:pointer;" id="a<s:property value="#linkname1"/>" onclick="DoMenu('<s:property value="#linkname1"/>');return false;" >
		<span id = "smenuA<s:property value="#status1.index"/>" class="ui-icon icon-list-contract"></span>
		<strong><s:text name="%{#leftMenu1.menuID}"/></strong>
	</a>
	 <ul id="menuA<s:property value="#status1.index"/>" class="collapsed">
	 <s:iterator value="#leftMenu1.childList" id="leftMenu2" status="status2">
	    <s:set name="linkname2" value="#linkname1+'B' + #status2.index"/>
	 	<li><a id="a<s:property value="#linkname2"/>"  style="cursor:pointer;" onclick="DoMenu('<s:property value="#linkname2"/>');ajaxSubmitLeftMenu($('#hiddenbasePath').val() +'<s:property value="#leftMenu2.menuURL"/>'+'?currentMenuID=<s:property value="#leftMenu2.menuID"/>&parentMenuID=<s:property value="#leftMenu1.menuID"/>');return false;"><span class="<s:property value="#leftMenu2.iconCSS"/>"></span><s:text name="%{#leftMenu2.menuID}" /></a></li>
	 </s:iterator>
	 </ul>
	</li>
</s:iterator>
 
 </ul>
</div>
</div> 
