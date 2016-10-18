<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS32">
<div id="headInfo">
	<s:text name="RPS32_sumSynchedTimes"/>
	<span class="green">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumSynchedTimes"></s:param></s:text></strong>
	</span>
	<s:text name="RPS32_sumUsedTimes"/>
	<span class="green">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumUsedTimes"></s:param></s:text></strong>
	</span>
	<s:text name="RPS32_sumSaleAmount"/>
	<span class="<s:if test='sumInfo.sumSaleAmount < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.price"><s:param value="sumInfo.sumSaleAmount"></s:param></s:text></strong>
	</span>
	<s:text name="RPS32_sumDeductAmount"/>
	<span class="<s:if test='sumInfo.sumDeductAmount < 0'>highlight</s:if><s:else>green</s:else>">
    	<strong><s:text name="format.price"><s:param value="sumInfo.sumDeductAmount"></s:param></s:text></strong>
    </span>
</div>
<div id="aaData">
	<s:iterator value="baCouponUsedInfoList" id="baCouponUsedInfoMap">
		<ul>
			<li><span><s:property value="RowNumber"/></span></li>
			<li><span><s:property value="parentResellerName"/></span></li>
			<li><span><s:property value="resellerName"/></span></li>
			<li><span><s:property value="synchedTimes"/></span></li>
			<li>
				<s:if test='usedTimes != 0'>
					<s:url action="BINOLPTRPS32_baCouponUsedInit" id="baCouponUsedInitUrl">
						<s:param name="resellerCode" value="%{#baCouponUsedInfoMap.resellerCode }" />
						<s:param name="brandInfoId" value="%{#baCouponUsedInfoMap.brandInfoId }" />
						<s:param name="startDate" value="%{#baCouponUsedInfoMap.startDate }" />
						<s:param name="endDate" value="%{#baCouponUsedInfoMap.endDate }" />
					</s:url>
					<a href="${baCouponUsedInitUrl}" class="popup" onclick="javascript:openWin(this);return false;">
						<span><s:text name="format.number"><s:param value="usedTimes"/></s:text></span>
					</a>
				</s:if><s:else>
					<span><s:text name="format.number"><s:param value="usedTimes"/></s:text></span>
				</s:else>
			</li>
			<li><span><s:property value="saleAmount"/></span></li>
			<li><span><s:property value="deductAmount"/></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>