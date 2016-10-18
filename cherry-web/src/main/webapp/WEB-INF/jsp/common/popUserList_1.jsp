<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="userList" id="userMap">
<ul>
<li>
<input type="radio" name="userId" value="<s:property value="#userMap.userId"/>"/>
</li>
<li><s:property value="#userMap.code"/></li>
<li><s:property value="#userMap.name"/></li>
</ul>
</s:iterator>
</div>