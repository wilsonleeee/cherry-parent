<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="failList" id="fail">
<ul>
<li>
    <input type="hidden" value="1111"/>
<s:property value="#fail.brandCode"/>
</li>
<li><s:property value="#fail.counterCode"/></li>
<li><s:property value="#fail.counterName"/></li>
<li><s:property value='#fail.errorMsg' /></li>
</ul>
</s:iterator>
</div>