<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="com.cherry.cm.core.CherryConstants" %> 
<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>

<s:i18n name="i18n.pl.BINOLPLSCF02">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="auditPrivilegeList" id="auditPrivilegeInfo">
<ul>
<%-- <li><span>
<s:if test="%{#auditPrivilegeInfo.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
<s:else>${auditPrivilegeInfo.brandNameChinese }</s:else>
</span></li> --%>
<li><span><s:property value='#application.CodeTable.getVal("1138", #auditPrivilegeInfo.bussinessTypeCode)' /></span></li>
<li><span>
<s:if test='%{#auditPrivilegeInfo.initiatorType == "1"}'><s:text name="type_user"></s:text></s:if>
<s:if test='%{#auditPrivilegeInfo.initiatorType == "2"}'><s:text name="type_pos"></s:text></s:if>
<s:if test='%{#auditPrivilegeInfo.initiatorType == "3"}'><s:text name="type_orc"></s:text></s:if>
</span></li>
<li><span>
<s:if test='%{#auditPrivilegeInfo.initiatorType == "1"}'><s:property value="#auditPrivilegeInfo.initiatorLonginName"/></s:if>
<s:if test='%{#auditPrivilegeInfo.initiatorType == "2"}'><s:property value="#auditPrivilegeInfo.initiatorPositionName"/></s:if>
<s:if test='%{#auditPrivilegeInfo.initiatorType == "3"}'><s:property value="#auditPrivilegeInfo.initiatorDepartName"/></s:if>
</span></li>
<li><span>
<s:if test='%{#auditPrivilegeInfo.auditorType == "1"}'><s:text name="type_user"></s:text></s:if>
<s:if test='%{#auditPrivilegeInfo.auditorType == "2"}'><s:text name="type_pos"></s:text></s:if>
<s:if test='%{#auditPrivilegeInfo.auditorType == "3"}'><s:text name="type_orc"></s:text></s:if>
</span></li>
<li><span>
<s:if test='%{#auditPrivilegeInfo.auditorType == "1"}'><s:property value="#auditPrivilegeInfo.auditorLonginName"/></s:if>
<s:if test='%{#auditPrivilegeInfo.auditorType == "2"}'><s:property value="#auditPrivilegeInfo.auditorPositionName"/></s:if>
<s:if test='%{#auditPrivilegeInfo.auditorType == "3"}'><s:property value="#auditPrivilegeInfo.auditorDepartName"/></s:if>
</span></li>
<s:url action="BINOLPLSCF04_init" id="updateAuditInitUrl">
<s:param name="auditPrivilegeId" value="%{#auditPrivilegeInfo.auditPrivilegeId}"></s:param>
</s:url>
<s:url action="BINOLPLSCF05_deleteAudit" id="deleteAuditUrl"></s:url>
<li>
<cherry:show domId="BINOLPLSCF0202">
<a class="delete" href="${updateAuditInitUrl }" onclick="openWin(this);return false;">
	<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="global.page.edit" /></span>
</a>
</cherry:show>
<cherry:show domId="BINOLPLSCF0203">
<a class="delete" href="${deleteAuditUrl }" onclick="plscf02_deleteAuditInit(this);return false;">
	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete" /></span>
</a>
</cherry:show>
<span class="hide">
<s:hidden name="auditPrivilegeId" value="%{#auditPrivilegeInfo.auditPrivilegeId}"></s:hidden>
<s:hidden name="modifyTime" value="%{#auditPrivilegeInfo.updateTime}"></s:hidden>
<s:hidden name="modifyCount" value="%{#auditPrivilegeInfo.modifyCount}"></s:hidden>
</span>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>