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
		<li><span><s:property value="memCode"/></span></li>
		<li><span><s:property value="memName"/></span></li>
		<li><span><s:property value="mobilePhone"/></span></li>
		<li><span><s:property value="joinDate"/></span></li>
		<li><span>
		<s:if test='%{birthYear != null && !"".equals(birthYear) && birthMonth != null && !"".equals(birthMonth) && birthDay != null && !"".equals(birthDay)}'>
		<s:property value="birthYear"/>-<s:property value="birthMonth"/>-<s:property value="birthDay"/>
		</s:if>
		</span></li>
		<li><span>
		<s:if test='%{counterName != null && !"".equals(counterName)}'>
		(<s:property value="counterCode"/>)<s:property value="counterName"/>
		</s:if>
		<s:elseif test='%{counterCode != null && !"".equals(counterCode)}'>
		<s:property value="counterCode"/>
		</s:elseif>
		</span></li>
		</ul>
	</s:iterator>
</div>