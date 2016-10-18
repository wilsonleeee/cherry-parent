<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOWAT06">
<div id="aaData">
	<s:iterator value="mqWarnList" id="mqWarnMap">
		<ul>
			<li><s:checkbox name="id" fieldValue="%{#mqWarnMap.id}" value="false" onclick="mowat06_checkRecord(this,'#dataTable');"></s:checkbox></li>
			<%-- 业务类型 --%>
			<li><span><s:property value="TradeType"/></span></li>
			<%-- 单据号 --%>
			<li><span><s:property value="TradeNoIF"/></span></li>
			<%-- 错误类型 --%>
			<li><span>
			<s:if test='%{#mqWarnMap.ErrType != null && "1".equals(#mqWarnMap.ErrType)}'>
			<s:text name="binolmowat06_errType1" />
			</s:if>
			<s:else>
			<s:text name="binolmowat06_errType0" />
			</s:else>
			</span></li>
			<%-- 消息体 --%>
			<li><span>
			  <s:set value="%{#mqWarnMap.MessageBody }" var="MessageBody"></s:set>
			  <a title="" rel='<s:property value="MessageBody"/>' onclick="binolmowat06.showDetail(this);return false;" style="cursor:pointer;">
				<cherry:cut length="20" value="${MessageBody }"></cherry:cut>
			  </a>
			</span></li>
			<%-- 错误信息 --%>
			<li><span>
			  <s:set value="%{#mqWarnMap.ErrInfo }" var="ErrInfo"></s:set>
			  <a title="" rel='<s:property value="ErrInfo"/>' onclick="binolmowat06.showDetail(this);return false;" style="cursor:pointer;">
				<cherry:cut length="30" value="${ErrInfo }"></cherry:cut>
			  </a>
			</span></li>
			<%-- 时间 --%>
			<li><span><s:property value="PutTime"/></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
