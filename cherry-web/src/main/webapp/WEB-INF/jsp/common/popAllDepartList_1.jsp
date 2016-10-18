<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="departList" id="departMap">
<ul>
<li>
<input type="radio" name="departId" value="<s:property value="#departMap.departId"/>"/>
</li>
<li><s:property value="#departMap.code"/></li>
<li><s:property value="#departMap.name"/></li>
</ul>
</s:iterator>
</div>