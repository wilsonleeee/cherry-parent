<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="saleCountInfoList" id="saleCountInfoMap">
		<ul>
			<%-- No. --%>
			<li>${saleCountInfoMap.RowNumber}</li>
			<s:if test='%{countModel == "0" || countModel == "1"}'>
			<%-- 柜台 --%>
			<li><span><s:property value="departName"/></span></li>
			<li><span><s:property value="busniessPrincipal"/></span></li>
			</s:if>
			<s:if test='%{countModel == "0" || countModel == "2"}'>
			<%-- 厂商编码 --%>
			<li><span><s:property value="unitCode"/></span></li>
			<%-- 产品条码   --%>
			<li><span><s:property value="barCode"/></span></li>
			<%-- 产品名称   --%>
			<li><span><s:property value="nameTotal"/></span></li>
			<%-- 销售类型   --%>
			<li><span><s:property value='#application.CodeTable.getVal("1106", #saleCountInfoMap.saleType)'/></span></li>
			</s:if>
			<%-- 数量 --%>
			<li><span>
			  <s:if test="%{#saleCountInfoMap.quantity != null}">
			    <s:text name="format.number"><s:param value="quantity"></s:param></s:text>
			  </s:if>
			</span></li>
			<%-- 金额 --%>
			<li><span>
			  <s:if test="%{#saleCountInfoMap.amount != null}">
			    <s:text name="format.price"><s:param value="amount"></s:param></s:text>
			  </s:if>
			</span></li>
			<s:if test='%{fiscalShow == "1"}'>
			<%-- 月度总数量 --%>
			<li><span><strong>
			  <s:if test="%{#saleCountInfoMap.monthQuantity != null}">
			    <s:text name="format.number"><s:param value="monthQuantity"></s:param></s:text>
			  </s:if></strong>
			</span></li>
			<%-- 月度总金额 --%>
			<li><span><strong>
			  <s:if test="%{#saleCountInfoMap.monthAmount != null}">
			    <s:text name="format.price"><s:param value="monthAmount"></s:param></s:text>
			  </s:if></strong>
			</span></li>
			</s:if><s:elseif test='%{fiscalShow == "2"}'>
			<%-- 年度总数量 --%>
			<li><span><strong>
			  <s:if test="%{#saleCountInfoMap.yearQuantity != null}">
			    <s:text name="format.number"><s:param value="yearQuantity"></s:param></s:text>
			  </s:if></strong>
			</span></li>
			<%-- 年度总金额 --%>
			<li><span><strong>
			  <s:if test="%{#saleCountInfoMap.yearAmount != null}">
			    <s:text name="format.price"><s:param value="yearAmount"></s:param></s:text>
			  </s:if></strong>
			</span></li>
			</s:elseif>
		</ul>
	</s:iterator>
</div>
<s:i18n name="i18n.pt.BINOLPTRPS15">
<s:if test='%{fiscalShow == "1"}'>
	<div id="fiscalMonthInfoDiv">
		<s:text name="binolptrps15_fiscalMonthInfo" />
		<s:property value="fiscalYearDisplay"/><s:text name="binolptrps15_fiscalYear" />
		<s:property value="fiscalMonthDisplay"/><s:text name="binolptrps15_fiscalMonth" />
		(<s:property value="fiscalMonthStart"/>~<s:property value="fiscalMonthEnd"/>)
	</div>
</s:if>
<s:elseif test='%{fiscalShow == "2"}'>
	<div id="fiscalMonthInfoDiv">
		<s:text name="binolptrps15_fiscalYearInfo" />
		<s:property value="fiscalYearDisplay"/><s:text name="binolptrps15_fiscalYear" />
		(<s:property value="fiscalMonthStart"/>~<s:property value="fiscalMonthEnd"/>)
	</div>
</s:elseif>
 
<s:if test='saleCountInfo != null'>
	<div id="saleCountInfoDiv">
		<s:text name="binolptrps15_totalQuantity"/>
		<span class="<s:if test='saleCountInfo.quantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="saleCountInfo.quantity"></s:param></s:text></strong>
		</span>
		<s:text name="binolptrps15_totalAmount"/>
	    <span class="<s:if test='saleCountInfo.amount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="saleCountInfo.amount"></s:param></s:text></strong>
	    </span>
	</div>
</s:if>
</s:i18n>