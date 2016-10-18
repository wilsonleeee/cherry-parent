<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBSVC05">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
	<s:iterator value="tradeList" id="trade" status="status">
		<ul>
			<li><s:property value="RowNumber"/></li>
			<li><s:property value="billCode"/></li>
			<li><s:property value="transactionTime"/></li>
			<li><s:property value='#application.CodeTable.getVal("1340", transactionType)'/></li>
			<li><s:property value="totalAmount"/></li>
			<li><s:property value="cardCode" /></li>
			<li><s:property value="mobilePhone"/></li>
			<li><s:property value="memo"/></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>