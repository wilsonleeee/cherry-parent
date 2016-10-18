<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="employeeList" id="employeeMap">
<ul>
<li><input type="checkbox" name="employeeId" value='<s:property value="#employeeMap.employeeId"/>' /></li>
<li><span id="employeeName"><s:property value="#employeeMap.employeeName"/></span></li>
<li><span id="departName"><s:property value="#employeeMap.departName"/></span></li>
<li><span id="categoryName"><s:property value="#employeeMap.categoryName"/></span></li>
</ul>
</s:iterator>
</div>