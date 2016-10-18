<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM12">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="memInfoRecordList" id="memInfoRecordMap">
<ul>
<li><span><s:property value="RowNumber"/></span></li>
<li><span>
<s:if test='%{!"ALL".equals(#memInfoRecordMap.memCode)}'>
<s:url action="BINOLMBMBM10_init" id="memberDetailUrl">
	<s:param name="memberInfoId" value="%{#memInfoRecordMap.memId}"></s:param>
</s:url>
<a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
	<s:property value="memCode"/>
</a>
</s:if>
</span></li>
<li><span><s:property value='#application.CodeTable.getVal("1241", #memInfoRecordMap.modifyType)' /></span></li>
<li><span><s:property value="batchNo"/></span></li>
<li><span><s:property value="modifyTime"/></span></li>
<li><span><s:property value="modifyCounter"/></span></li>
<li><span><s:property value="modifyEmployee"/></span></li>
<li><span>
<s:set value="%{#memInfoRecordMap.remark }" var="remark"></s:set>
<a title='<s:text name="binolmbmbm12_remark" />|<s:property value="remark"/>' class="description">
  <cherry:cut length="20" value="${remark }"></cherry:cut>
</a>
</span></li>
<li>
<s:url action="BINOLMBMBM13_init" id="memInfoRecordIdDetailUrl">
	<s:param name="memInfoRecordId" value="%{#memInfoRecordMap.memInfoRecordId}"></s:param>
</s:url>
<a class="delete" href="${memInfoRecordIdDetailUrl }" onclick="openWin(this);return false;">
	<span class="ui-icon ui-icon-document"></span><span class="button-text"><s:text name="binolmbmbm12_showDetail"></s:text></span>
</a>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>