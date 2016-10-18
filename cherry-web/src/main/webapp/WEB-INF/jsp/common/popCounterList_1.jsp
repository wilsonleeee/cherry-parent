<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="counterInfoList" id="counterInfoMap">
<ul>
<li>
<input type="radio" name="organizationId" value='<s:property value="#counterInfoMap.organizationId"/>'/>
<input type="hidden" name="counterCode" value='<s:property value="#counterInfoMap.counterCode"/>'/>
<input type="hidden" name="counterKind" value='<s:property value="#counterInfoMap.counterKind"/>'/>
</li>
<li><s:property value="#counterInfoMap.counterCode"/></li>
<li><s:property value="#counterInfoMap.counterNameIf"/></li>
</ul>
</s:iterator>
</div>