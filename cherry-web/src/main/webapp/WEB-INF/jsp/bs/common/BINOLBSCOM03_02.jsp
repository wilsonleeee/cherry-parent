<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<s:set id="employeeIdCheck" value="%{employeeId}"></s:set>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="employeeList" id="employeeMap">
<ul>
<li>
	<input type="radio" name="employeeId" value='<s:property value="#employeeMap.employeeId"/>' <s:if test="%{#employeeMap.employeeId == #employeeIdCheck}">checked</s:if>/>
</li>
<li><span id="employeeName"><s:property value="#employeeMap.employeeName"/></span></li>
<li><span id="phone"><s:property value="#employeeMap.phone"/></span></li>
<li><span id="mobilePhone"><s:property value="#employeeMap.mobilePhone"/></span></li>
<li><span id="email"><s:property value="#employeeMap.email"/></span></li>
</ul>
</s:iterator>
</div>