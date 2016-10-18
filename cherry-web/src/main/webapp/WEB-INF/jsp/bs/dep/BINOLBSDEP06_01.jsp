<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<s:i18n name="i18n.bs.BINOLBSDEP06">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="organizationList" id="organization">
<ul>
<li><span>
<s:checkbox name="validFlag" fieldValue="%{#organization.validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
<s:hidden name="organizationInfoId" value="%{#organization.organizationInfoId}"></s:hidden>
</span></li>
<li><span><s:property value="#organization.orgCode"/></span></li>
<li><span><s:property value="#organization.orgNameChinese"/></span></li>
<li><span><s:property value="#organization.orgNameForeign"/></span></li>
<li><span><s:property value="#organization.orgNameShort"/></span></li>
<li><span><s:property value="#organization.orgNameForeignShort"/></span></li>
<li><s:if test="#organization.validFlag ==1"><span class='ui-icon icon-valid'></span></s:if><s:else><span class='ui-icon icon-invalid'></span></s:else></li>
<s:url action="BINOLBSDEP08_init" id="updateOrganizationInitUrl">
<s:param name="organizationInfoId" value="%{#organization.organizationInfoId}"></s:param>
</s:url>
<li>
<cherry:show domId="BINOLBSDEP0603">
<a class="delete" href="${updateOrganizationInitUrl }" onclick="openWin(this);return false;">
	<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="global.page.edit" /></span>
</a>
</cherry:show>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>