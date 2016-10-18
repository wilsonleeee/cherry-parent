<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.yh.BINOLYHREP01">
<div id="headInfo">
	<s:text name="YHREP01_usedCoupon"/>
	<span class="green">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumOrderCount"></s:param></s:text></strong>
	</span>
	<s:text name="YHREP01_stroke"/>
	<s:text name="YHREP01_sumAmount"/>
	<span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	</span>
	<s:text name="YHREP01_yuan"/>
    <br/>
    <hr class="space" />
    <s:text name="YHREP01_noUsedCoupon"/>
	<span class="green">
		<strong><s:text name="format.number"><s:param value="sumInfo.noUsedCouponOrderCount"></s:param></s:text></strong>
	</span>
	<s:text name="YHREP01_stroke"/>
	<s:text name="YHREP01_sumAmount"/>
	<span class="<s:if test='sumInfo.noUsedCouponOrderAmount < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.price"><s:param value="sumInfo.noUsedCouponOrderAmount"></s:param></s:text></strong>
	</span>
	<s:text name="YHREP01_yuan"/>
</div>
<div id="aaData">
	<s:iterator value="saleOrderDetailList" id="saleOrderDetailMap">
		<ul>
			<li><span><s:property value="RowNumber"/></span></li>
			<li><span><s:property value="billCode"/></span></li>
			<li><span><s:property value="couponCode"/></span></li>
			<li><span><s:property value="memberName"/></span></li>
			<li><span><s:property value="productName"/></span></li>
			<li><span><s:text name="format.price"><s:param value="detailAmount"/></s:text></span></li>
			<li><span><s:property value="resellerName1L"/></span></li>
			<li><span><s:property value="resellerName2L"/></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>