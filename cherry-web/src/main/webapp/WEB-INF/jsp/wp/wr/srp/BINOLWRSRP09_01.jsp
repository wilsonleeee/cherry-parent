<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRSRP09">
	<div class="section">
		<div class="section-header">
			<strong>
				<span class="ui-icon icon-ttl-section-search-result"></span>
				<s:text name="global.page.list"/>
			</strong>
		</div>
		<div class="section-content">
			<div class="toolbar clearfix">
				<span id="headInfo">&nbsp;&nbsp;
					<s:text name="WRSRP09_sumAmount"/>
					<span class="<s:if test='saleCountInfo.amount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
					<strong><s:text name="format.price"><s:param value="saleCountInfo.amount"></s:param></s:text></strong>
					</span>&nbsp;&nbsp;
					<s:text name="WRSRP09_sumQuantity"/>
					<span class="<s:if test='saleCountInfo.quantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
					<strong><s:text name="format.number"><s:param value="saleCountInfo.quantity"></s:param></s:text></strong>
					</span>&nbsp;&nbsp;
					<s:text name="WRSRP09_sumBill"/>
					<span class="<s:if test='saleCountInfo.saleCount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
					<strong><s:text name="format.number"><s:param value="saleCountInfo.saleCount"></s:param></s:text></strong>
					</span>&nbsp;&nbsp;
					<s:text name="WRSRP06_guestPrice"/>
					<span class="<s:if test='saleCountInfo.guestPrice < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
					<strong><s:text name="format.price"><s:param value="saleCountInfo.guestPrice"></s:param></s:text></strong>
					</span>&nbsp;&nbsp;
					<s:text name="WRSRP09_joinRate"/>
					<span class="<s:if test='saleCountInfo.joinRate < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
					<strong><s:text name="format.price"><s:param value="saleCountInfo.joinRate"></s:param></s:text></strong>
					</span>&nbsp;&nbsp;
					<s:text name="WRSRP09_averagePrice"/>
					<span class="<s:if test='saleCountInfo.averagePrice < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
					<strong><s:text name="format.price"><s:param value="saleCountInfo.averagePrice"></s:param></s:text></strong>
					</span>&nbsp;&nbsp;
					<s:text name="WRSRP09_grossProfitAmount"/>
					<span class="<s:if test='saleCountInfo.grossProfitAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
					<strong><s:text name="format.price"><s:param value="saleCountInfo.grossProfitAmount"></s:param></s:text></strong>
					</span>&nbsp;&nbsp;
					<s:text name="WRSRP09_grossMargin"/>
					<span class="<s:if test='saleCountInfo.grossMargin < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
					<strong><s:text name="format.percent"><s:param value="saleCountInfo.grossMargin"></s:param></s:text></strong>
					</span>
				</span>
			</div>
			<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
				<thead>
				<tr>
					<th><s:text name="WRSRP09_number" /></th>
					<th><s:text name="WRSRP09_payType" /></th>
					<th><s:text name="WRSRP09_payAmount" /></th>
				</tr>
				</thead>
				<tbody>
					<s:set name="index" value="0" />
					<s:iterator value="saleList" id="saleMap">
						<tr>
							<td><s:set name="index" value="#index+1" /><s:property value="%{#index}"/></td>
							<td><s:property value='#application.CodeTable.getVal("1175", payType)'/></td>
							<td><s:text name="format.price"><s:param value="totalPayAmount"></s:param></s:text></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
	</div>
</s:i18n>