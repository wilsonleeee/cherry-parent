<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="memList" id="memberInfo" status="status">
		<ul>
			<li>
				<span>
				<s:url action="BINOLMBMBM10_init" id="memberDetailUrl">
					<s:param name="memberInfoId" value="%{#memberInfo.memId}"></s:param>
				</s:url>
				<a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
					<s:if test='%{name != null && !"".equals(name)}'></s:if>
					<s:property value="name"/></span>
					<s:else>未知</s:else>
				</a>
			</li>
			<li><span><s:property value="memCode"/></span></li>
			<li><span><s:property value="mobilePhone"/></span></li>
			<li><span><s:property value="birthDay"/></span></li>
			<li><span><s:property value="levelName"/></span></li>
			<li><span><s:property value="totalPoint"/></span></li>
			<li><span><s:property value="departCode"/></span></li>
			<li><span><s:property value="departName"/></span></li>
			<li><span><s:property value="jointDate"/></span></li>
		</ul>
	</s:iterator>
</div>