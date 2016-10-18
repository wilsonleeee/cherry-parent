<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="popMemberInfoList">
		<ul>
			<li>
				<input name="memberInfo" class="checkbox" value="<s:property value="memberInfo"/>"
				type="<s:if test='%{checkType == null || checkType == "checkbox"}'>checkbox</s:if><s:else>radio</s:else>"/>
			</li>
			<li><s:property value="memCode"/></li>
			<li><s:property value="memName"/></li>
			<li><s:property value="mobilePhone"/></li>
			<li><s:property value="departName"/></li>
		</ul>
	</s:iterator>
</div>