<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  


<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="regionList" id="regionMap">
<ul>
<li><input type="radio" name="path" value='<s:property value="#regionMap.path"/>' /></li>
<li><span id="regionCode"><s:property value="#regionMap.regionCode"/></span></li>
<li><span id="regionName"><s:property value="#regionMap.regionName"/></span></li>
<li><span id="regionType"><s:property value='#application.CodeTable.getVal("1151", #regionMap.regionType)' /></span></li>
</ul>
</s:iterator>
</div>