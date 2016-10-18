<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ page import="com.cherry.cm.core.CherryConstants" %> 

<s:i18n name="i18n.bs.BINOLBSPOS06">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="positionCategoryList" id="positionCategory">
<s:url action="BINOLBSPOS07_init" id="posCategoryInfoUrl">
<s:param name="positionCategoryId" value="#positionCategory.positionCategoryId"></s:param>
</s:url>
<ul>
<li>
<s:checkbox name="validFlag" fieldValue="%{#positionCategory.validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
<s:hidden name="positionCategoryId" value="%{#positionCategory.positionCategoryId}"></s:hidden>
</li>
<li><a href="${posCategoryInfoUrl }" onclick="openWin(this);return false;"><s:property value="#positionCategory.categoryCode"/></a>
<s:if test='%{"01".equals(#positionCategory.categoryCode)}'><span class="red"><s:text name="bspos06_baRemark"/></span></s:if>
<s:elseif test='%{"02".equals(#positionCategory.categoryCode)}'><span class="red"><s:text name="bspos06_basRemark"/></span></s:elseif>
</li>
<li><span><s:property value="#positionCategory.categoryName"/></span></li>
<li><span><s:property value="#positionCategory.posGrade"/></span></li>
<li><s:if test="#positionCategory.validFlag ==1"><span class='ui-icon icon-valid'></span></s:if><s:else><span class='ui-icon icon-invalid'></span></s:else></li>
</ul>
</s:iterator>
</div>
</s:i18n>