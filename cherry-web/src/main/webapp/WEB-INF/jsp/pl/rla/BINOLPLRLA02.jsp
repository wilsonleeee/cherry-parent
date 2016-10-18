<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>

<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/Cherry/js/pl/rla/BINOLPLRLA02.js"></script>


<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>

<cherry:form id="privilegeCherryForm"></cherry:form>
<s:i18n name="i18n.pl.BINOLPLRLA01">
	<div id="treeLayoutDiv" class="div-layout-all">
	  <div class="ui-layout-west">
	  <div style="min-width: 178px;">
	    <div class="treebox2-header"><strong><span class="ui-icon icon-tree"></span><s:text name="role_assign" /></strong></div>
	    <dl><dt class="jquery_tree treebox_left tree" id="positionCtgTree"></dt></dl>
	  </div>
	  </div>
	  <div class="ui-layout-center">
	  <div style="min-width: 570px;" id="roleAssignInit"></div>
	  </div>
	</div>
</s:i18n>

<div class="hide">
<s:url action="BINOLPLRLA02_loadTree" id="positionCtgLoadTreeUrl"></s:url>
<a href="${positionCtgLoadTreeUrl }" id="positionCtgLoadTreeUrl"></a>
<s:url action="BINOLPLRLA99_init" id="roleAssignUrl"></s:url>
<a href="${roleAssignUrl }" id="roleAssignUrl"></a>
</div>

