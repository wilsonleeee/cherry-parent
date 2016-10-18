<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mb.BINOLMBVIS05">
<div id="aaData">
	<s:iterator value="visitTaskList" id="visitTaskMap">
		<ul>
			<li><span><s:property value="visitTypeName"/></span></li>
			<li><span><s:property value="birthDay"/></span></li>
			<li><span><s:property value="memName"/></span></li>
			<li><span><s:property value="memCode"/></span></li>
			<li><span><s:property value="memMobile"/></span></li>
			<li><span><s:property value="skinType"/></span></li>
			<li>
				<s:url id="edit_url" action="BINOLMBVIS05_editInit" namespace="/mb">
						<s:param name="memCode">${memCode}</s:param>
						<s:param name="visitTaskId">${visitTaskId}</s:param>
						<s:param name="startTime">${startTime}</s:param>
						<s:param name="endTime">${endTime}</s:param>
						<s:param name="visitType">${visitType}</s:param>
						<s:param name="memberID">${memberID}</s:param>
				</s:url>
				<s:if test='#visitTaskMap.visitResult == NULL || #visitTaskMap.visitResult == ""'>
					<a class="delete" href="${edit_url}" onclick="openWin(this);return false;">
						<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="mbvis05_memVis"/></span>
					</a>
				</s:if>
			</li>
			
		</ul>
	</s:iterator>
</div>
</s:i18n>
