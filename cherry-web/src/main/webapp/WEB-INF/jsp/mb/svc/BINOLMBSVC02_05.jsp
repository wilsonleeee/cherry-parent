<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBSVC02">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="serviceList" id="service" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="cardCode"/></li>
<li><s:property value="cardType"/></li>
<li><s:property value="departName"/></li>
<li><s:property value="transactionTime" /></li>
<li><s:property value="billCode" /></li>
<li><s:property value="relevantCode" /></li>
<li><s:property value="transactionType" /></li>
<li><s:property value="amount" /></li>
<li><s:property value="giftAmount" /></li>
<li><s:property value="totalAmount" /></li>
<li><s:property value="serviceType" /></li>
<li><s:property value="discount" /></li>
<li><s:property value="serviceQuantity" /></li>
<li><s:property value="quantity" /></li>
<li><s:property value="totalQuantity" /></li>
</ul>
</s:iterator>
</div>
</s:i18n>