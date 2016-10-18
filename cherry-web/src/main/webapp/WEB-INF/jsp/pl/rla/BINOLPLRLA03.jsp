<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/pl/common/BINOLPLCOM01.js"></script>
<script type="text/javascript" src="/Cherry/js/pl/rla/BINOLPLRLA03.js"></script>

<cherry:form id="privilegeCherryForm"></cherry:form>
<s:i18n name="i18n.pl.BINOLPLRLA01">
	<div class="panel-header">
	  <h2><s:text name="position_role_title" /></h2>
	  <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="privilege_manage" />&nbsp;&gt;&nbsp;<s:text name="role_assign" />&nbsp;&gt;&nbsp;<s:text name="position_role_title" /></span> </div>
	</div>
	<div id="treeLayoutDiv" class="div-layout-all">
	  <div class="ui-layout-west">
	  <div style="min-width: 178px;">
	    <div class="treebox2-header"><strong><span class="ui-icon icon-tree"></span><s:text name="role_assign" /></strong></div>
	    <dl><dt class="jquery_tree" id="positionTree"></dt></dl>
	  </div>
	  </div>
	  <div class="ui-layout-center">
	  <div style="min-width: 570px;" id="roleAssignInit"></div>
	  </div>
	</div>
</s:i18n>

<div class="hide">
<s:url action="BINOLPLRLA03_next" id="positionInitUrl"></s:url>
<a href="${positionInitUrl }" id="positionInitUrl"></a>
<s:url action="BINOLPLRLA99_init" id="roleAssignUrl"></s:url>
<a href="${roleAssignUrl }" id="roleAssignUrl"></a>
</div>

