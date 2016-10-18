<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.bs.BINOLBSSAM02">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="overTimeList" id="overTime" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="applyDepart"/></li>
<li><s:property value="workDepart" /></li>
<li><s:property value="applyEmployee" /></li>
<li><span><s:property value='#application.CodeTable.getVal("1359", #overTime.auditedState)' /></span></li>
<li><s:property value="overtimeDate" /></li>
<li><s:property value="overtimeBegin"/></li>
<li><s:property value="overtimeEnd"/></li>
<li><s:property value="overtimeHours"/></li>
<li><s:property value="overtimeMemo"/></li>
<li><s:property value="applyTime"/></li>
<li><s:property value="auditedEmployee"/></li>
<li><s:property value="auditedTime"/></li>
<li><s:property value="auditedMemo"/></li>
</ul>
</s:iterator>
</div>
</s:i18n>