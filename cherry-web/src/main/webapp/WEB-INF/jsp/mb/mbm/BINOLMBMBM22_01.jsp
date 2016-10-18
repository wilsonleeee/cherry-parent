<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="memAnswerList" id="memAnswerMap">
<ul>
<li><span>
<s:url action="BINOLMBMBM22_searchAnswerDetail" id="searchAnswerDetailUrl">
<s:param name="paperAnswerId" value="%{#memAnswerMap.paperAnswerId}"></s:param>
<s:param name="paperId" value="%{#memAnswerMap.paperId}"></s:param>
</s:url>
<input value="${searchAnswerDetailUrl }" type="hidden"/>
<s:property value="paperName"/>
</span></li>
<li><span><s:property value="counterName"/></span></li>
<li><span><s:property value="employeeName" /></span></li>
<li><span><s:property value="checkDate"/></span></li>
</ul>
</s:iterator>
</div>