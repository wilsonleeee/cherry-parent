<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="checkPaperList" id="checkPaperMap">
<ul>
<li><input type="radio" name="checkPaperId" value='<s:property value="#checkPaperMap.checkPaperId"/>' /></li>
<li><s:property value="#checkPaperMap.paperName"/></li>
<li><s:property value='#application.CodeTable.getVal("1108", #checkPaperMap.paperStatus)' /></li>
</ul>
</s:iterator>
</div>