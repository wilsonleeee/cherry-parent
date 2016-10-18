<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="employeesList" status="status">
		<ul>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- （员工CODE）员工姓名 --%>
			<li><span><s:property value="'('+EmployeeCode+')'+EmployeeName"/></span></li>
			<%-- 所在部门  --%>
			<li><span><s:property value="'('+DepartCode+')'+DepartName"/></span></li>
			<%-- 所在岗位--%>
			<li><span><s:property value="CategoryName"/></span></li>
		</ul>
	</s:iterator>
</div>