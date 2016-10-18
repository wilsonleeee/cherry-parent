<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ page import="com.cherry.cm.core.CherryConstants" %> 
<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>

<%
	String  language =  String.valueOf(session.getAttribute(CherryConstants.SESSION_LANGUAGE));
%>

<s:i18n name="i18n.pl.BINOLPLRLA01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="userInfoList" id="userInfo">
<ul>
<li><s:property value="#userInfo.RowNumber"/></li>
<li><p><s:property value="#userInfo.longinName"/></p></li>
<li><p><s:property value="#userInfo.employeeName"/></p></li>
<li><p>
<s:if test="%{#userInfo.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
<s:else><s:property value="#userInfo.brandNameChinese"/></s:else>
</p></li>
<li><p><s:property value="#userInfo.departName"/></p></li>
<li><p><s:property value="#userInfo.categoryName"/></p></li>
<s:url action="BINOLPLRLA99_init" id="userRoleAssignInitUrl">
	<s:param name="userId" value="#userInfo.userId"></s:param>
	<s:param name="brandInfoId" value="#userInfo.brandInfoId"></s:param>
</s:url>
<li>
<a class="authority" href="${userRoleAssignInitUrl }" onclick="userRoleAssignInit(this);return false;"><span class="ui-icon icon-authority"></span><span class="button-text"><s:text name="assigin_button" /></span></a>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>