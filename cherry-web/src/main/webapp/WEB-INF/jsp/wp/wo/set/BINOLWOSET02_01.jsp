<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="baAttendanceList" id="baAttendanceMap" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="employeeCode"/></li>
<li><s:property value="employeeName"/></li>
<li><s:property value="attendanceDateTime"/></li>
<li><s:property value='#application.CodeTable.getVal("1313", attendanceType)'/></li>
</ul>
</s:iterator>
</div>