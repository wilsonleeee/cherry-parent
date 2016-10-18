<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ page import="com.cherry.cm.core.CherryConstants" %> 
<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>

<s:i18n name="i18n.pl.BINOLPLRLM01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="roleInfoList" id="roleInfo">
<ul>
<li><s:property value="#roleInfo.RowNumber"/></li>
<li><p><s:property value="#roleInfo.roleName"/></p></li>
<li><p><s:if test="#roleInfo.decription != null && #roleInfo.decription != ''"><s:property value="#roleInfo.decription"/></s:if><s:else>&nbsp;</s:else></p></li>
<li><p>
	<s:property value='#application.CodeTable.getVal("1009", #roleInfo.roleKind)' />
</p></li>
<li><p>
<s:if test="%{#roleInfo.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
<s:else><s:property value="#roleInfo.brandNameChinese"/></s:else>
</p></li>
<s:url action="BINOLPLRLM04_init" id="updateRoleInitUrl"></s:url>
<s:url action="BINOLPLRLM03_init" id="roleAuthorityInitUrl"></s:url>
<s:url action="BINOLPLRLM05_delete" id="deleteRoleUrl"></s:url>
<li>
<a class="delete" href="${updateRoleInitUrl }" onclick="updateRoleInit(this);return false;">
	<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="update_button" /></span>
</a>
<a class="delete" href="${deleteRoleUrl }" onclick="deleteRoleInit(this);return false;">
	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="delete_button" /></span>
</a>
<a class="authority" href="${roleAuthorityInitUrl }" onclick="roleAuthorityInit(this);return false;">
	<span class="ui-icon icon-authority"></span><span class="button-text"><s:text name="authorize_button" /></span>
</a>
<span class="hide">
<s:hidden name="roleId" value="%{#roleInfo.roleId}"></s:hidden>
<s:hidden name="modifyTime" value="%{#roleInfo.updateTime}"></s:hidden>
<s:hidden name="modifyCount" value="%{#roleInfo.modifyCount}"></s:hidden>
</span>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>