<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="campaignHistoryList" id="campaignHistoryMap">
<ul>
<li><span><s:property value="actName"/></span></li>
<li><span><s:property value="mainCode"/></span></li>
<li><span><s:property value="actGroupName"/></span></li>
<li><span>
<s:if test="%{#campaignHistoryMap.campaignType == 0}">
	<s:property value='#application.CodeTable.getVal("1174", #campaignHistoryMap.actType)' />
</s:if>
<s:else>
	<s:if test='"CXHD".equals(#campaignHistoryMap.actGroupType)'>
	   	<s:property value="#application.CodeTable.getVal('1228',#campaignHistoryMap.actType)"/>
	</s:if>
	<s:if test='"LYHD".equals(#campaignHistoryMap.actGroupType)'>
	    <s:property value="#application.CodeTable.getVal('1247',#campaignHistoryMap.actType)"/>
	</s:if>
	<s:if test='"DHHD".equals(#campaignHistoryMap.actGroupType)'>
	   	<s:property value="#application.CodeTable.getVal('1229',#campaignHistoryMap.actType)"/>
	</s:if>
</s:else>
</span></li>
<li><span><s:property value="tradeNoIF"/></span></li>
<li><span><s:property value="batchNo"/></span></li>
<li><span><s:property value="departName"/></span></li>
<li><span><s:property value="participateTime"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1201", #campaignHistoryMap.informType)' /></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1116", #campaignHistoryMap.state)' /></span></li>
</ul>
</s:iterator>
</div>