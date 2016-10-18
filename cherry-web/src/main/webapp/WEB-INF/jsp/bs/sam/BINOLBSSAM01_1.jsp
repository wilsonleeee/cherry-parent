<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.bs.BINOLBSSAM01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="scheduleList" id="schedule" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="workDate"/></li>
<li><span><s:property value='#application.CodeTable.getVal("1358", #schedule.type)' /></span></li>
<li><s:property value="workBeginTime" /></li>
<li><s:property value="workEndTime" /></li>
<li><s:property value="memo" /></li>
</ul>
</s:iterator>
</div>
</s:i18n>