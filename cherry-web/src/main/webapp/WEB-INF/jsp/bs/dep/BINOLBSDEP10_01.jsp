<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<s:i18n name="i18n.bs.BINOLBSDEP10">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="brandInfoList" id="brandInfo">
<ul>
<li><span>
<s:checkbox name="validFlag" fieldValue="%{#organization.validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
<s:hidden name="brandInfoId" value="%{#brandInfo.brandInfoId}"></s:hidden>
</span></li>
<li><span><s:property value="#brandInfo.brandCode"/></span></li>
<li><span><s:property value="#brandInfo.brandNameChinese"/></span></li>
<li><span><s:property value="#brandInfo.brandNameForeign"/></span></li>
<li><span><s:property value="#brandInfo.brandNameShort"/></span></li>
<li><span><s:property value="#brandInfo.brandNameForeignShort"/></span></li>
<li><s:if test="#brandInfo.validFlag ==1"><span class='ui-icon icon-valid'></span></s:if><s:else><span class='ui-icon icon-invalid'></span></s:else></li>
<s:url action="BINOLBSDEP12_init" id="updateBrandInitUrl">
<s:param name="brandInfoId" value="%{#brandInfo.brandInfoId}"></s:param>
</s:url>
<li>
<cherry:show domId="BINOLBSDEP1004">
<a class="delete" href="${updateBrandInitUrl }" onclick="openWin(this);return false;">
	<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="global.page.edit" /></span>
</a>
</cherry:show>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>