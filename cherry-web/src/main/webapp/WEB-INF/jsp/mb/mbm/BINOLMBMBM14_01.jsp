<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBMBM14"> 
<div id="aaData">
<s:iterator value="memInfoRecordList" id="memInfoRecordMap">
<ul>
<li><span>
<s:url action="BINOLMBMBM14_detailInit" id="searchMemInfoRecordDetailUrl">
<s:param name="memInfoRecordId" value="%{#memInfoRecordMap.memInfoRecordId}"></s:param>
</s:url>
<input value="${searchMemInfoRecordDetailUrl }" type="hidden"/>
<s:property value="memCode"/>
</span></li>
<li><span><s:property value='#application.CodeTable.getVal("1241", #memInfoRecordMap.modifyType)' /></span></li>
<li><span><s:property value="batchNo"/></span></li>
<li><span><s:property value="modifyTime"/></span></li>
<li><span><s:property value="modifyCounter"/></span></li>
<li><span><s:property value="modifyEmployee"/></span></li>
<li><span>
<s:set value="%{#memInfoRecordMap.remark }" var="remark"></s:set>
<a title='<s:text name="binolmbmbm14_remark" />|<s:property value="remark"/>' class="description">
  <cherry:cut length="20" value="${remark }"></cherry:cut>
</a>
</span></li>
</ul>
</s:iterator>
</div>
</s:i18n>