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
		<s:iterator value="resultList" id="list">
			<s:url id="detail_url" value="/cp/BINOLCPACT10_detailInit">
				<s:param name="saleRecordId">${list.saleRecordId}</s:param>
			</s:url>
			<ul>
				<li>${RowNumber}</li>
				<%-- 编号 --%>
				<%--单据号 --%>
				<li><a href="${detail_url}" class="popup"
					onclick="BINOLCPACT10.getPrtDetail(this);return false;"> <s:property
							value="billCode" />
				</a></li>
				<%--会员 --%>
				<li><s:property value="memNameCode" /></li>
				<%--会员手机 --%>
				<li><s:property value="mobilePhone" /></li>
				<%--会员类型--%>
				<li><span><s:property value='#application.CodeTable.getVal("1256", testType)' /></span></li>
				<%--兑换柜台 --%>
				<li><s:property value="departName" /></li>
				<%--领取数量 --%>
				<li><s:text name="format.number">
						<s:param value="quantity"></s:param>
				</s:text></li>
				<%--领取金额 --%>
				<li><s:text name="format.price">
						<s:param value="amount"></s:param>
					</s:text>
				</li>
					<%--领用时间 --%>
				<li><s:property value="saleTime" /></li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>