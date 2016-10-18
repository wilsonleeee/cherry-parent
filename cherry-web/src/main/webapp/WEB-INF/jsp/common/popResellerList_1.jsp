<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="resellerList" id="resellerMap">
<ul>
<li><span><input type="radio" name="resellerCode" value="<s:property value="resellerCode"/>"/></span></li>
<li><span><s:property value="resellerCode"/></span></li>
<li><span><s:property value="resellerName"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1315", #resellerMap.levelCode)' /></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1314", #resellerMap.type)' /></span></li>						
</ul>
</s:iterator>
</div>