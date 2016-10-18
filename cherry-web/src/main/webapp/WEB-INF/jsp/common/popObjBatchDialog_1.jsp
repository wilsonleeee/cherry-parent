<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="popObjBatchList">
		<ul>
			<li>
			<input name="objBatchInfo" class="checkbox" value="<s:property value="searchCode"/>"
				type="<s:if test='%{checkType == null || checkType == "checkbox"}'>checkbox</s:if><s:else>radio</s:else>"/>
			</li>
			<li><s:property value="searchCode"/></li>
			<li><s:property value="recordName"/></li>
			<li><s:property value="comments"/></li>
		</ul>
	</s:iterator>
</div>