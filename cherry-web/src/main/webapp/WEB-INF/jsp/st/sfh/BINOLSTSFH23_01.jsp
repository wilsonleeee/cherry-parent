<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTUNQ01">
	<div id="headInfo">
		 <s:text name="订货数量：" />
		<span class="<s:if test='sumInfo.sumApplyQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumApplyQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="发货数量：" />
	    <span class="<s:if test='sumInfo.sumTotalQuantity < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.number"><s:param value="sumInfo.sumTotalQuantity"></s:param></s:text></strong>
	    </span>
	    <s:text name="订货金额：" />
		<span class="<s:if test='sumInfo.sumTotalAmount < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.price"><s:param value="sumInfo.sumTotalAmount"></s:param></s:text></strong>
		</span>
	</div>
<div id="aaData">
		<s:iterator value="orderSelectList" id="orderList" >
			<ul>
			  <s:url id="detailsUrl" action="BINOLSTSFH03_init">
			  <%-- 订单单号   --%>
			  <s:param name="productOrderID">${binProductOrderID}</s:param>
		      </s:url>
				<li>
				<s:checkbox name="valid" fieldValue="%{#orderList.orderNo}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
			    <s:hidden name="orderNo" value="%{#orderList.orderNo}"></s:hidden>
			    <s:hidden name="VerifiedFlag" value="%{#orderList.VerifiedFlag}"></s:hidden>
			    </li>
			    <li><a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;">
			    <s:property	value="orderNo" /></a>
			    </li>
			    <li><span><s:property value="departName" /></span></li>
			    <li><span><s:property value="orderDate"/></span></li>
			    <li><span><s:property value="applyQuantity"/></span></li>
			    <li><span><s:property value="totalAmount"/></span></li>
			    <li><span><s:property value="expectDeliverDate"/></span></li>
			    <li><span><s:property value="totalQuantity"/></span></li>
				<li><span><s:property value="deliverDate"/></span></li>
				<li><span><s:property value='#application.CodeTable.getVal("1146", #orderList.VerifiedFlag)'/></span></li>
				
			</ul>
		</s:iterator>	
</div>
</s:i18n>