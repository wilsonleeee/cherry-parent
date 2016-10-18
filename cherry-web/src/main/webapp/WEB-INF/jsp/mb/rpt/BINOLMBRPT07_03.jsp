<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="saleDetailList" id="saleDetailMap" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="productName"/></li>
<li><s:property value="unitCode" /></li>
<li><s:property value="barCode"/></li>
<li><s:text name="format.number"><s:param value="buyQuantity"></s:param></s:text></li>
<li><s:text name="format.price"><s:param value="buyAmount"></s:param></s:text></li>
<li><s:property value='#application.CodeTable.getVal("1055", saleType)'/></li>
<li><s:property value="saleTime"/></li>
<li><s:property value="departName"/></li>
<li><s:property value="employeeName"/></li>
</ul>
</s:iterator>
</div>
<s:if test="saleDetailCountInfo != null && saleDetailCountInfo.size() != 0">
	<s:i18n name="i18n.mb.BINOLMBRPT07">
	<div id="headInfo">
		<s:text name="mbrpt07_sumQuantity"/>
		<span class="<s:if test='saleDetailCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="saleDetailCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="mbrpt07_sumAmount"/>
	    <span class="<s:if test='saleDetailCountInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="saleDetailCountInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	</s:i18n>
</s:if> 
