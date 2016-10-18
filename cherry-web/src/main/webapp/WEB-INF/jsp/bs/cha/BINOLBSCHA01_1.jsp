<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLBSCHA01_1">
	<div id="aaData"><s:iterator value="channelList" id="channel">
		<s:url id="detailsUrl" action="BINOLBSCHA02_init">
			<%-- 渠道名称   --%>
			<s:param name="channelId">${channel.channelId}</s:param>
		</s:url>
		<ul>
			<li><s:checkbox id="validFlag" name="validFlag" value="false"
				fieldValue="%{#paperId}" onclick="checkSelect(this);" />
				<input type="hidden" id="channelIdArr" name="channelIdArr" value="<s:property value='channelId' />"></input>
            </li>
			<%-- No. --%>
			<li><s:property value="RowNumber" /></li>
			<li><%-- 渠道名称  --%> <a href="${detailsUrl}" class="left"
				onclick="javascript:openWin(this);return false;"> <s:property
				value="channelName" /></a></li>
			<li><%-- 渠道类型 --%> <s:if
				test='status != null && !"".equals(status)'>
				<s:property value='#application.CodeTable.getVal("1121",#channel.status)' />
			</s:if> <s:else>
            	&nbsp;
            </s:else></li>
			<li><%-- 加入日期 --%> <s:if	test='joinDate != null && !"".equals(joinDate)'>
				<s:property value="joinDate" />
			</s:if> <s:else>
            	&nbsp;
            </s:else></li>
			<li><s:if test='"1".equals(validFlag)'>
				<span class='ui-icon icon-valid'></span>
			</s:if><%-- 有效区分 --%> <s:else>
				<span class='ui-icon icon-invalid'></span>
			</s:else></li>
		</ul>
	</s:iterator></div>
</s:i18n>