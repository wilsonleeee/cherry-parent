<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="employeeList" id="employeeMap">
<ul>
<li>
<input type="radio" name="employeeId" value="<s:property value="#employeeMap.employeeId"/>"/>
<input type="hidden" name="employeeCode" value='<s:property value="#employeeMap.employeeCode"/>'/>
</li>
<li><s:property value="#employeeMap.employeeCode"/></li>
<li><s:property value="#employeeMap.employeeName"/></li>
<li><s:property value="#employeeMap.counterName"/></li>
</ul>
</s:iterator>
</div>