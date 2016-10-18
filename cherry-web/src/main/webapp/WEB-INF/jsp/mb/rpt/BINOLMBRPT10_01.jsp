<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="saleDetailList" id="saleDetailMap" status="status">
<ul>
<li><span><s:property value="memberName"/></span></li>
<li><span><s:property value="memCode"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1055", saleType)'/></span></li>
<li><span><s:property value="departCode"/></span></li>
<li><span><s:property value="departName"/></span></li>
<li><span><s:property value="unitCode"/></span></li>
<li><span><s:property value="barCode"/></span></li>
<li><span><s:property value="productName"/></span></li>
<li><span><s:property value="saleTime"/></span></li>
<li><s:text name="format.price"><s:param value="buyAmount"></s:param></s:text></li>
</ul>
</s:iterator>
</div>
<s:if test="saleDetailCountInfo != null && saleDetailCountInfo.size() != 0">
	<s:i18n name="i18n.mb.BINOLMBRPT10">
	<div id="headInfo">
		<s:text name="mbrpt10_sumQuantity"/>
		<span class="<s:if test='saleDetailCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="saleDetailCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="mbrpt10_sumAmount"/>
	    <span class="<s:if test='saleDetailCountInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="saleDetailCountInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	</s:i18n>
</s:if> 