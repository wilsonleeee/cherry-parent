<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ page import="com.cherry.cm.core.CherryConstants" %> 

<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>

<s:i18n name="i18n.bs.BINOLBSDEP01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="organizationList" id="organization">
<s:url action="BINOLBSDEP02_init" id="organizationInfoUrl">
<s:param name="organizationId" value="#organization.organizationId"></s:param>
</s:url>
<ul>
<li>
<s:if test="%{#organization.type != 0 && #organization.type != 1 && #organization.type != 4}">
<s:checkbox name="validFlag" fieldValue="%{#organization.validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
<s:hidden name="organizationId" value="%{#organization.organizationId}"></s:hidden>
</s:if>
</li>
<li><span><a href="${organizationInfoUrl }" onclick="openWin(this);return false;"><s:property value="#organization.departCode"/></a></span></li>
<li><span><s:property value="#organization.departName"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1000", #organization.type)' /></span></li>
<li><span>
<s:if test="%{#organization.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
<s:else><s:property value="#organization.brandNameChinese"/></s:else>
</span></li>
<li><span><s:property value='#application.CodeTable.getVal("1030", #organization.status)' /></span></li>
<li><span><s:property value="#organization.regionName"/></span></li>
<li><span><s:property value="#organization.provinceName"/></span></li>
<li><span><s:property value="#organization.cityName"/></span></li>
<li><span><s:property value="#organization.countyName"/></span></li>
<%-- 部门协同区分 --%>
<s:if test="maintainOrgSynergy" >
	<li><span><s:property value='#application.CodeTable.getVal("1331", #organization.orgSynergyFlag)' /></span></li>
</s:if>
<li><s:if test="#organization.validFlag ==1"><span class='ui-icon icon-valid'></span></s:if><s:else><span class='ui-icon icon-invalid'></span></s:else></li>
</ul>
</s:iterator>
</div>
</s:i18n>