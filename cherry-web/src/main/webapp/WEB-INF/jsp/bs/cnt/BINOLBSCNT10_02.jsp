<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSCNT10">
<div id="aaData">

	<s:iterator value="counterPointLimitDetailList" id="counterLimitDetailMap">
		<ul>
			<%-- 行号 --%>
			<li>
				<span>
					<s:url action="BINOLBSCNT10_searchSaleRecord" id="searchSaleRecordUrl">
						<s:param name="billNo" value="%{#counterLimitDetailMap.BillNo}"></s:param>
					</s:url>
					<input value="${searchSaleRecordUrl }" type="hidden"/>
					<s:property value="RowNumber"/>
				</span>
			</li>
			<!-- 业务类型 -->
			<li>

					<s:if test=' TradeType == 1 || TradeType == 2 '>
						<span class="saleTypeClass"><s:property value='#application.CodeTable.getVal("1419", TradeType)'/></span>
					</s:if>
					<s:else>
						<span><s:property value='#application.CodeTable.getVal("1419", TradeType)'/></span>
					</s:else>

			</li>
			<!-- 单据号 -->
			<li>
				<span>
					<s:property value="BillNo"/>
				</span>
			</li>
			<%-- 单据日期 --%>
			<li><span><s:property value="TradeTime"/></span></li>
			<%-- 金额 --%>
			<li><span><s:property value="Amount"/></span></li>
			<%-- 经销商额度 --%>
			<li><span><s:property value="PointChange" /></span></li>
			<%-- 会员卡号 --%>
			<li><span><s:property value="MemberCode" /></span></li>
			<%-- 备注 --%>
			<li><span><s:property value="Comment" /></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
