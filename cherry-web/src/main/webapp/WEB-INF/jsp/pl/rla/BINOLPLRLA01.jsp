<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/pl/rla/BINOLPLRLA01.js"></script>

<cherry:form id="privilegeCherryForm"></cherry:form>
<s:i18n name="i18n.pl.BINOLPLRLA01">
	<div id="treeLayoutDiv" class="div-layout-all">
	  <div class="ui-layout-west">
	  <div style="min-width: 178px;">
	    <div class="treebox2-header"><strong><span class="ui-icon icon-tree"></span><s:text name="role_assign" /></strong></div>
	    <dl><dt class="jquery_tree" id="organizationTree"></dt></dl>
	  </div>
	  </div>
	  <div class="ui-layout-center">
	  <div style="min-width: 570px;" id="roleAssignInit"></div>
	  </div>
	</div>
</s:i18n>
<div class="hide">
<s:url action="BINOLPLRLA01_next" id="organizationInitUrl"></s:url>
<a href="${organizationInitUrl }" id="organizationInitUrl"></a>
<s:url action="BINOLPLRLA99_init" id="roleAssignUrl"></s:url>
<a href="${roleAssignUrl }" id="roleAssignUrl"></a>
</div>

