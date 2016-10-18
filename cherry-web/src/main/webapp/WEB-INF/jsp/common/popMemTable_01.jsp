<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="memberInfoList" id="memberInfo" status="status">
		<ul>
		<li><span><s:property value="%{#status.count + iDisplayStart}"/></span></li>
		<li><span><s:property value="memName"/></span></li>
		<li><span><s:property value="memCode"/></span></li>
		<li><span><s:property value="joinDate"/></span></li>
		<li><span><s:property value="levelName"/></span></li>
		<li><span><s:property value="mobilePhone"/></span></li>
		</ul>
	</s:iterator>
</div>