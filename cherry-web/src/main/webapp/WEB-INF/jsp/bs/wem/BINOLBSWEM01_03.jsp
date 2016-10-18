<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:i18n name="i18n.bs.BINOLBSWEM01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
	<s:iterator value="empList" id="template" status="status">
		<ul>
			<li>
				<input type="radio" id="mobilePhone" name="mobilePhone" value="${template.mobilePhone }"/>
				<span class="hide" id="employeeId">${template.employeeId }</span>
			</li>
			<li><s:property value="employeeCode" /></li>
			<li><s:property value="employeeName" /></li>
			<li><s:property value="mobilePhone" /></li>
			<li><s:property value='#application.CodeTable.getVal("1000", #template.agentLevel)' /></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
