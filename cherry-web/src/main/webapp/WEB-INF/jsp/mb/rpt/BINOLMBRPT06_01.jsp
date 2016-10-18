<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mb.BINOLMBRPT06">
<div id="headInfo">
	<s:if test='sumInfo != null && sumInfo.size() != 0 && sumInfo.sumOrderCount > 0'>
	<s:text name="RPT06_sumOrderCount"/>
	<span class="green">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumOrderCount"></s:param></s:text></strong>
	</span>
	<s:text name="RPT06_sumSaleQuantity"/>
	<span class="<s:if test='sumInfo.sumSaleQuantity < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumSaleQuantity"></s:param></s:text></strong>
	</span>
	<s:text name="RPT06_sumSaleAmount"/>
	<span class="<s:if test='sumInfo.sumSaleAmount < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.price"><s:param value="sumInfo.sumSaleAmount"></s:param></s:text></strong>
	</span>
	</s:if>
</div>
<div id="aaData">
	<s:iterator value="memRecommendedRptList" id="memRecommendedRptMap">
		<ul>
			<li><span><s:property value="RowNumber"/></span></li>
			<li><span><s:property value="member"/></span></li>
			<li><span><s:property value="recommendedMember"/></span></li>
			<li><span><s:property value="mobilePhone"/></span></li>
			<li><span><s:text name="format.number"><s:param value="orderCount"/></s:text></span></li>
			<li><span><s:text name="format.number"><s:param value="saleQuantity"/></s:text></span></li>
			<li><span><s:text name="format.price"><s:param value="saleAmount"/></s:text></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>