<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<s:i18n name="i18n.pl.BINOLPLPLT01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="privilegeTypeList" id="privilegeTypeInfo">
<ul>
<li><span><s:property value='#application.CodeTable.getVal("1047", #privilegeTypeInfo.category)' /></span></li>
<li><span>
<s:if test='%{#privilegeTypeInfo.category == "0"}'>
<s:property value='#application.CodeTable.getVal("1000", #privilegeTypeInfo.departType)' />
</s:if>
</span></li>
<li><span>
<s:if test='%{#privilegeTypeInfo.category == "1"}'>
<s:property value="#privilegeTypeInfo.categoryName"/>
</s:if>
</span></li>
<li><span><s:property value='#application.CodeTable.getVal("1048", #privilegeTypeInfo.businessType)' /></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1049", #privilegeTypeInfo.operationType)' /></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1050", #privilegeTypeInfo.privilegeType)' /></span></li>
<li><span>
<s:if test='%{#privilegeTypeInfo.exclusive == "0"}'>
<s:text name="notExclusive"></s:text>
</s:if>
<s:if test='%{#privilegeTypeInfo.exclusive == "1"}'>
<s:text name="exclusive"></s:text>
</s:if>
</span></li>
<s:url action="BINOLPLPLT03_init" id="updatePltInitUrl">
<s:param name="privilegeTypeId" value="%{#privilegeTypeInfo.privilegeTypeId}"></s:param>
</s:url>
<s:url action="BINOLPLPLT04_delete" id="deletePltUrl"></s:url>
<li>
<cherry:show domId="BINOLPLPLT0102">
<a class="delete" href="${updatePltInitUrl }" onclick="openWin(this);return false;">
	<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="global.page.edit" /></span>
</a>
</cherry:show>
<cherry:show domId="BINOLPLPLT0103">
<a class="delete" href="${deletePltUrl }" onclick="plplt01_deletePltInit(this);return false;">
	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete" /></span>
</a>
</cherry:show>
<span class="hide">
<s:hidden name="privilegeTypeId" value="%{#privilegeTypeInfo.privilegeTypeId}"></s:hidden>
<s:hidden name="modifyTime" value="%{#privilegeTypeInfo.updateTime}"></s:hidden>
<s:hidden name="modifyCount" value="%{#privilegeTypeInfo.modifyCount}"></s:hidden>
</span>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>