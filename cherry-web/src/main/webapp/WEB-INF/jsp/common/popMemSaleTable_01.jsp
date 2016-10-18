<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="memSaleInfoList" id="memSaleInfo" status="status">
		<ul>
		<li><span><s:property value="%{#status.count + iDisplayStart}"/></span></li>
		<li><span><s:property value="eventId"/></span></li>
		<li><span><s:property value="eventDate"/></span></li>
		<li><span><s:property value="counterCode"/></span></li>
		<li><span><s:property value="amount"/></span></li>
		<li><span><s:property value="quantity"/></span></li>
		</ul>
	</s:iterator>
</div>