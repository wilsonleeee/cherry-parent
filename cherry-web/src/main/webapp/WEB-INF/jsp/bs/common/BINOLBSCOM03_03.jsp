<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  


<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="departList" id="departMap">
<ul>
<li>
	<input type="radio" name="higher" value='<s:property value="#departMap.path"/>' />
</li>
<li><span id="departCode"><s:property value="#departMap.departCode"/></span></li>
<li><span id="departName"><s:property value="#departMap.departName"/></span></li>
<li><span id="type">
<s:property value='#application.CodeTable.getVal("1000", #departMap.type)' />
<s:hidden name="departType" value="%{#departMap.type}"></s:hidden>
</span></li>
</ul>
</s:iterator>
</div>