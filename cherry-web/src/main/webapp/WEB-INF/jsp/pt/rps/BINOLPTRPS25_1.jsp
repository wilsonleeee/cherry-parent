<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS25">
	<div id="headInfo">
		<s:text name="RPS25_sumQuantity"/>
		<span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="RPS25_sumAmount"/>
	    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
<div id="aaData">
	<s:iterator value="recordList">
		<s:url id="detailsUrl" value="/pt/BINOLPTRPS26_init">
			<s:param name="prtDeliverId">${prtDeliverId}</s:param>
		</s:url>
		<ul>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- 单据号 --%>
			<li>
				<a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
					<s:property value="deliverNoIF"/>
				</a>
			</li>
			<%-- 发货部门 --%>
			<li><span><s:property value="sendDepart"/></span></li>
			<%-- 发货仓库 --%>
			<li><span><s:property value="depotName"/></span></li>
			<%-- 接收部门 --%>
			<li><span><s:property value="receiveDepart"/></span></li>
            <%-- 总数量 --%>
            <li><span class="<s:if test='quantity < 0'>highlight</s:if>"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></li>
			<%-- 总金额  --%>
           	<li><span class="<s:if test='amount < 0'>highlight</s:if>"><s:text name="format.price"><s:param value="amount"></s:param></s:text></span></li>
			<%-- 日期 --%>
			<li><span><s:property value="date"/></span></li>
			<%-- 操作员 --%>
			<li><span><s:property value="employeeName"/></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>