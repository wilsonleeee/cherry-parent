<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
	<s:iterator value="memberList" id="member">
		<ul>
			<li><s:property value="#member.brandCode"/></li>
			<li><s:property value="#member.memberCode"/></li>
			<li><s:property value="#member.mobile"/></li>
			<li><s:property value='#member.bpCode' /></li>
			<li><s:property value='#member.memberLevel' /></li>
			<li><s:property value='#member.name' /></li>
		</ul>
	</s:iterator>
</div>