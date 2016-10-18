<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
<s:iterator value="counterEventList" id="counterEventMap">
<ul>
    <li><s:property value="RowNumber"/></li>
    <li><s:property value='#application.CodeTable.getVal("1125", #counterEventMap.eventNameId)' /></li>
    <li><s:property value="fromDate"/></li>
</ul>
</s:iterator>
</div>
