<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="departList" id="departInfoMap">
<ul>
<li>
  <s:if test='%{checkType != null && checkType == "radio"}'>
	<input type="radio" name="organizationId" value='<s:property value="#departInfoMap.organizationId"/>'/>
  </s:if>
  <s:else>
	<input type="checkbox" name="organizationId" value='<s:property value="#departInfoMap.organizationId"/>'/>
  </s:else>
</li>
<li><span><s:property value="#departInfoMap.departCode"/></span></li>
<li><span><s:property value="#departInfoMap.departName"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1000", #departInfoMap.type)' /></span></li>
</ul>
</s:iterator>
</div>