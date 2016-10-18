<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.cp.BINOLCPACT12">
	<div id="aaData">
		<s:iterator value="campaignStockList" id="campaignStockMap">
			<s:url id="detail_url" value="/cp/BINOLCPACT12_detailInit">
				<s:param name="brandInfoId">${campaignStockMap.brandInfoId}</s:param>
				<s:param name="subCampCode">${campaignStockMap.subCampCode}</s:param>
				<s:param name="organizationId">${campaignStockMap.organizationId}</s:param>
			</s:url>
			<s:url id="updateInitUrl" value="/cp/BINOLCPACT12_updateInit">
				<s:param name="brandInfoId">${campaignStockMap.brandInfoId}</s:param>
				<s:param name="subCampCode">${campaignStockMap.subCampCode}</s:param>
				<s:param name="organizationId">${campaignStockMap.organizationId}</s:param>
			</s:url>
			<ul>
				<%-- 编号 --%>
				<li>${RowNumber}</li>
				<%--主题活动--%>
				<li><s:property value="campaign" /></li>
				<%--活动 --%>
				<li><s:property value="subcamp" /></li>
				<%--柜台--%>
				<li><s:property value="counter" /></li>
				<%--产品名称 --%>
				<%--<li><s:property value="productName" /></li>--%>
				<%--分配数量--%>
				<li><s:property value="totalQuantity" /></li>
				<%--剩余数量--%>
				<li><s:property value="currentQuantity" /></li>
				<%--安全数量--%>
				<li><s:property value="safeQuantity" /></li>
				<li>
	               	<a href="${detail_url}" class="edit" id="detailBtn" onclick="javascript:openWin(this);return false;">
	                   <span class="ui-icon ui-icon-document"></span>
	                   <span class="button-text"><s:text name="ACT12_detail"></s:text></span>
	               	</a>
	               	<cherry:show domId="CPACT12EDIT">
	               	<a href="${updateInitUrl}" class="add" id="updateBtn" onclick="javascript:openWin(this);return false;">
	                   <span class="ui-icon icon-edit"></span>
	                   <span class="button-text"><s:text name="ACT12_edit"></s:text></span>
	               	</a>
	               	</cherry:show>
				</li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>