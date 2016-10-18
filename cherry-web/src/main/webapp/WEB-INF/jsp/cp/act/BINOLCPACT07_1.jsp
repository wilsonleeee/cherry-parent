<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.cp.BINOLCPACT07">
	<div id="aaData">
		<s:iterator value="giftDrawList" id="giftDraw">
			<s:url id="detail_url" value="/cp/BINOLCPACT08_init">
				<s:param name="giftDrawId">${giftDraw.giftDrawId}</s:param>
			</s:url>
			<ul>
				<li>${RowNumber}</li>
				<%-- 编号 --%>
				<%--单据号 --%>
				<li><a href="${detail_url}" class="popup"
					onclick="BINOLCPACT07.openDetail(this);return false;"> <s:property
							value="billNoIF" />
				</a></li>
				<%--会员 --%>
				<li><s:property value="memberName" /></li>
				<%--会员手机--%>
				<li><s:property value="memberPhone" /></li>
				<%--会员类型--%>
				<li><span><s:property
							value='#application.CodeTable.getVal("1256", testType)' /></span></li>
				<%--领取柜台 --%>
				<li><s:property value="getCounter" /></li>
				<%-- 赠券条码 --%>
				<li><s:property value="couponCode" /></li>
				<%--领用时间 --%>
				<li><s:property value="getTime" /></li>
				<%--领取数量 --%>
				<li><s:text name="format.number">
						<s:param value="getQuantity"></s:param>
					</s:text></li>
				<%--领取金额 --%>
				<li><s:text name="format.price">
						<s:param value="getAmount"></s:param>
					</s:text></li>
				<%--领用类型 --%>
				<li><span><s:property
							value='#application.CodeTable.getVal("1178", giftDrawType)' /></span></li>
				<%--活动名称(SQL文中已为code加括号)--%>
				<li><s:property value="activityCode" />
					<s:property value="activityName" /></li>
				<%--操作人员 --%>
				<li><s:property value="employee" /></li>
				<%--备注--%>
				<li><s:property value="comments" /></li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>