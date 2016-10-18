<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ page import="com.cherry.cm.core.CherryConstants" %> 

<%
	String  language =  String.valueOf(session.getAttribute(CherryConstants.SESSION_LANGUAGE));
%>

<s:set id="employeeIdCheck" value="%{employeeId}"></s:set>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="employeeList" id="employeeMap">
<ul>
<li>
	<input type="radio" name="employeeId" value="${employeeMap.employeeId }" <s:if test="%{#employeeMap.employeeId == #employeeIdCheck}">checked</s:if>/>
</li>
<li><span id="employeeName">${employeeMap.employeeName }</span></li>
<li><span id="departName">
	<s:if test="%{#employeeMap.extraInfoList != null && !#employeeMap.extraInfoList.isEmpty()}">
	<s:set name="organizationId" value=""/>
	<s:iterator value="#employeeMap.extraInfoList" id="extraInfo">
		<s:if test="#organizationId!=#extraInfo.organizationId">
			<s:set name="organizationId" value="#extraInfo.organizationId"/>
			<%if("en_US".equals(language)){ %>
			${extraInfo.nameForeign }
			<%} else { %>
			${extraInfo.departName }
			<%} %>
		</s:if>
		<s:else>&nbsp;</s:else>
		<br />
	</s:iterator>
	</s:if>
	<s:else>&nbsp;</s:else>
</span></li>
<li><span id="positionName">
	<s:if test="%{#employeeMap.extraInfoList != null && !#employeeMap.extraInfoList.isEmpty()}">
	<s:iterator value="#employeeMap.extraInfoList" id="extraInfo">
		<%if("en_US".equals(language)){ %>
			${extraInfo.positionNameForeign }
		<%} else { %>
			${extraInfo.positionName }
		<%} %>
		<br />
	</s:iterator>
	</s:if>
	<s:else>&nbsp;</s:else>
</span></li>
</ul>
</s:iterator>
</div>