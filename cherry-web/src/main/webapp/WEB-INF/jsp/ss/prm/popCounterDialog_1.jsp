<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="counterList" id="counterMap">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="#counterMap.counterCode"/></li>
<li><s:property value="#counterMap.counterName"/></li>
</ul>
</s:iterator>
</div>