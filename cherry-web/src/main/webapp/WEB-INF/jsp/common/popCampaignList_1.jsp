<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="campaignList" id="campaignMap">
<ul>
<li>
<input type="<s:if test='%{checkType == "checkbox"}'>checkbox</s:if><s:else>radio</s:else>" name="campaignCode" value='<s:property value="#campaignMap.campaignCode"/>'/>
<s:hidden name="campaignMode"></s:hidden>
</li>
<li><s:property value="#campaignMap.campaignName"/></li>
<li><s:property value="#campaignMap.campaignCode"/></li>
<li><s:property value='#application.CodeTable.getVal("1174", #campaignMap.campaignType)' /></li>
</ul>
</s:iterator>
</div>