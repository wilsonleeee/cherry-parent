<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOWAT08">
<div id="aaData">
	<s:iterator value="mqNoticeList" id="mqNoticeMap" status="status">
		<ul>
			<li><s:property value="#status.index+1"/></li>
			<%-- 消息类型 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1316", subType)'/></span></li>
			<%-- 消息码 --%>
			<li><span><s:property value="errorCode"/></span></li>
			<%-- 时间 --%>
			<li><span><s:property value="time"/></span></li>
			<%-- 消息内容 --%>
			<li><span>
			  <s:set value="%{#mqNoticeMap.content }" var="content"></s:set>
			  <a title="" rel='<s:property value="content"/>' onclick="BINOLMOWAT08.showDetail(this);return false;" style="cursor:pointer;">
				<cherry:cut length="50" value="${content }"></cherry:cut>
			  </a>
			</span></li>
			<%-- 消息体 --%>
			<li><span>
			  <s:set value="%{#mqNoticeMap.messageBody }" var="messageBody"></s:set>
			  <a title="" rel='<s:property value="messageBody"/>' onclick="BINOLMOWAT08.showDetail(this);return false;" style="cursor:pointer;">
				<cherry:cut length="20" value="${messageBody }"></cherry:cut>
			  </a>
			</span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
