<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<s:set id="counterInfoIdCheck" value="%{counterInfoId}"></s:set>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="counterInfoList" id="counterInfoMap">
<ul>
    <li>
        <input type="radio" name="counterInfoId" value="<s:property value="#counterInfoMap.counterInfoId"/>"/>
    </li>
    <li><s:property value="#counterInfoMap.counterCode"/></li>
    <li><s:property value="#counterInfoMap.counterNameIf"/></li>
</ul>
</s:iterator>
</div>