<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS42">
<%-- 销售总数量及总金额 --%>
<s:if test='(sumInfo != null && sumInfo.size() != 0)'>
	<div id="headInfo">
		<s:text name="RPS42_sumPrePayAmount"/>
		<span class="<s:if test='sumInfo.sumPrePayAmount < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.price"><s:param value="sumInfo.sumPrePayAmount"></s:param></s:text></strong>
		</span>
		<s:text name="RPS42_sumBuyQuantity"/>
	    <span class="<s:if test='sumInfo.sumBuyQuantity < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.number"><s:param value="sumInfo.sumBuyQuantity"></s:param></s:text></strong>
	    </span>
	    <s:text name="RPS42_sumLeftQuantity"/>
	    <span class="<s:if test='sumInfo.sumLeftQuantity < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.number"><s:param value="sumInfo.sumLeftQuantity"></s:param></s:text></strong>
	    </span>
	</div>
</s:if> 

<div id="aaData">
	<s:iterator value="preInfoList" id="preRecord">
		<s:url id="detailsUrl" value="/pt/BINOLPTRPS42_detail">
			<s:param name="prePayBillMainId">${preRecord.prePayBillMainId}</s:param>
		</s:url>
		<ul>
			<%-- No. --%>
			<li>
			<%-- <input type="checkbox" id="flag" name="prePayBillMainId" onclick="binolptrps42.checkSelect(this);" value="${preRecord.prePayBillMainId}"/> --%>
			${preRecord.RowNumber}</li>
			<%-- 预付单编号--%>
			<li>
				<a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
					<s:property value="prePayNo"/>
				</a>
			</li>
			<%--预留手机号 --%>
			<li><span><s:property value="telephone"/></span></li>
			<%-- 预付柜台 --%>
			<li><span><s:property value="'('+departCode+')'+departName"/></span></li>
			<%-- 预付时间 --%>
			<li><span><s:property value="prePayDate"/></span></li>
			<%-- 交易类型 --%>
			<li><span>
			<s:if test="transactionType.equals('NS')">			
				<s:text name="RPS42_NS"/>				
			</s:if> 
			<s:if test="transactionType.equals('SR')">						
				<s:text name="RPS42_SR"/>			
			</s:if></span></li>
			<%-- 预付金额  --%>
			<li><span><s:property value="prePayAmount"/></span></li>
			<%-- 购买数量   --%>
			<li><span>
			<s:if test="buyQuantity >= 0"><span><s:text name="format.number"><s:param value="buyQuantity"></s:param></s:text></span></s:if>
				<s:else><span class="highlight"><s:text name="format.number"><s:param value="buyQuantity"></s:param></s:text></span></s:else>			
			</span></li>
			<%-- 剩余数量 --%>
			<li><span>
			<s:if test="leftQuantity >= 0"><span><s:text name="format.number"><s:param value="leftQuantity"></s:param></s:text></span></s:if>
				<s:else><span class="highlight"><s:text name="format.number"><s:param value="leftQuantity"></s:param></s:text></span></s:else>			
			</span></li>
			<%-- 已提单数 --%>
			<li><span><s:property value="pickupTimes"/></span></li>
			<%-- 下次提货时间 --%>
			<li><span><s:property value="pickupDate"/></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>