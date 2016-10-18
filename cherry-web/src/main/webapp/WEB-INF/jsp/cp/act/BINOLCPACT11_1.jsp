<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.cp.BINOLCPACT10">
	<div id="aaData">
		<s:iterator value="campHistoryList" id="list">
			<s:url id="detail_url" value="/cp/BINOLCPACT10_detailInit">
				<s:param name="saleRecordId">${list.saleRecordId}</s:param>
			</s:url>
			<ul>
				<%-- 编号 --%>
				<li>${RowNumber}</li>
				<%--活动单据--%>
				<li><s:property value="tradeNoIF" /></li>
				<%--批次号码 --%>
				<li><s:property value="batchNo" /></li>
				<%--活动名称 --%>
				<li><s:property value="subCampaignName" /></li>
				<%--会员名称--%>
				<li><s:property value="memName" /></li>
				<%--会员手机--%>
				<li><s:property value="mobilePhone" /></li>
				<%--活动柜台 --%>
				<%--<li><s:property value="departName" /></li>--%>
				<%--活动时间--%>
				<li><s:property value="optTime" /></li>
				<%--活动状态 --%>
				<li>
		        	<s:if test='"RV".equals(state)'><span class="verifying"></s:if>
		        	<s:elseif test='"AR".equals(state)'><span class="verified_unsubmit"></s:elseif>
		        	<s:elseif test='"OK".equals(state)'><span class="task-verified"></s:elseif>
		        	<s:else><span class="task-verified_rejected"></s:else>
		        	<span><s:property value="#application.CodeTable.getVal('1116',state)"/></span>
		        	</span>
		        </li>				
			</ul>
		</s:iterator>
	</div>
</s:i18n>