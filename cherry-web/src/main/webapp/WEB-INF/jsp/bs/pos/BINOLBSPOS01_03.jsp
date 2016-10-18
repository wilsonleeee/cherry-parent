<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ page import="com.cherry.cm.core.CherryConstants" %> 

<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>

<s:i18n name="i18n.bs.BINOLBSPOS01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="positionList" id="position">
<s:url action="BINOLBSPOS02_init" id="positionInfoUrl">
<s:param name="positionId" value="#position.positionId"></s:param>
</s:url>
<ul>
<%-- <li><input name="" type="checkbox" value="" class="checkbox" /></li>--%>
<li>${position.RowNumber }</li>
<li><a href="${positionInfoUrl }" onclick="openWin(this);return false;">${position.positionName }</a></li>
<li>${position.positionDESC }&nbsp;</li>
<li>${position.categoryName }&nbsp;</li>
<li>
<s:if test="%{#position.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
<s:else>${position.brandNameChinese }&nbsp;</s:else>
</li>
<li>${position.departName }&nbsp;</li>
<li><s:property value='#application.CodeTable.getVal("1038", #position.isManager)' />&nbsp;</li>
<li><s:property value='#application.CodeTable.getVal("1039", #position.positionType)' />&nbsp;</li>
<li>${position.resellerName }&nbsp;</li>
<li>${position.foundationDate }&nbsp;</li>
<li><s:if test="#position.validFlag ==1"><span class='ui-icon icon-valid'></span></s:if><s:else><span class='ui-icon icon-invalid'></span></s:else></li>
</ul>
</s:iterator>
</div>
</s:i18n>