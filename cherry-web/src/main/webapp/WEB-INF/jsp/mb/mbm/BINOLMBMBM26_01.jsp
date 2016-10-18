<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBMBM26"> 	
<div id="aaData">
<s:iterator value="issueList" id="issueMap">
<ul>
<li><span>
<s:url action="BINOLMBMBM28_init" id="searchIssueDetailUrl">
<s:param name="issueId" value="%{#issueMap.issueId}"></s:param>
</s:url>
<a onclick="binolmbmbm26.searchIssueDetail('${searchIssueDetailUrl }');return false;" style="cursor:pointer;">
<s:property value="issueNo"/>
</a>
</span></li>
<li><span>
<s:set value="%{#issueMap.issueSummary }" var="issueSummary"></s:set>
<a title='<s:text name="binolmbmbm26_issueSummary" />|<s:property value="issueSummary"/>' class="description" onclick="binolmbmbm26.searchIssueDetail('${searchIssueDetailUrl }');return false;">
  <cherry:cut length="20" value="${issueSummary }"></cherry:cut>
</a>
</span></li>
<li><span>
<s:property value='#application.CodeTable.getVal("1272", #issueMap.issueType)' />&nbsp;&nbsp;
<s:property value='#application.CodeTable.getVal("1329", #issueMap.issueSubType)' />
</span></li>
<li><span><s:property value="assigneeName"/></span></li>
<li><span><s:property value="speakerName"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1271", #issueMap.priority)' /></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1118", #issueMap.issueStatus)' /></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1117", #issueMap.resolution)' /></span></li>
<li><span><s:property value="createTime"/></span></li>
<li><span><s:property value="updateTime"/></span></li>
</ul>
</s:iterator>
</div>
</s:i18n>