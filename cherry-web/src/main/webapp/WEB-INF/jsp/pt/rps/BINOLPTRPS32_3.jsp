<!-- 代理商优惠券使用详细一览LIST -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="couponUsedDetailList" id="couponUsedDetailMap">
		<ul>
			<li><span><s:property value="RowNumber"/></span></li>
			<li><span><s:property value="billCode"/></span></li>
			<li><span><s:property value="baName"/></span></li>
			<li>
				<s:if test='null != couponUsedDetailMap.batchCode && !"".equals(couponUsedDetailMap.batchCode)'>
				<span>(<s:property value="batchCode"/>)<s:property value="batchName"/></span>
				</s:if><s:else>
				<span><s:property value="batchName"/></span>
				</s:else>
			</li>
			<li><span><s:property value="couponCode"/></span></li>
			<li><span><s:property value="usedDate"/></span></li>
			<li><span><s:property value="usedTime"/></span></li>
			<li><span><s:property value="usedChannel"/></span></li>
			<li><span><s:property value="memberCode"/></span></li>
			<li><span><s:property value="mobilePhone"/></span></li>
			<li><span><s:property value="quantity"/></span></li>
			<li><span><s:property value="amount"/></span></li>
			<li><span><s:property value="deductAmount"/></span></li>
		</ul>
	</s:iterator>
</div>