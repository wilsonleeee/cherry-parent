<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.bs.BINOLBSSAM02">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="payrollList" id="payroll" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="departName"/></li>
<li><s:property value="employeeName" /></li>
<li><s:property value="workingHours" /></li>
<li><s:property value="workingDays" /></li>
<li><s:property value="usualOvertime" /></li>
<li><s:property value="specialOvertime"/></li>
<li><s:property value="saleTarget"/></li>
<li><s:property value="saleAmount"/></li>
<li><s:property value="bonusRate"/></li>
<li><s:property value="completionRate"/></li>
<li><s:property value="score"/></li>
<li><s:property value="wagesAmount"/></li>
<li><s:property value="memo"/></li>
</ul>
</s:iterator>
</div>
</s:i18n>