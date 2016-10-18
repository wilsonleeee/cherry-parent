<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="null != logList">
<ul>
<s:iterator value="logList" status="status">
<li>
<span><s:if test="#status.index lt 9">0</s:if><s:property value='#status.index+1'/>.&nbsp;&nbsp;<s:property value="printTime"/>&nbsp;&nbsp;<s:property value="employeeName"/></span>
</li>
</s:iterator>
</ul>
</s:if>
