<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.bs.BINOLBSCNT10">

	<div class="detail_box">
		<div class="section">
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-info"></span>
					<s:text name="global.page.title"/>
				</strong>
			</div>
			<div class="section-content">
				<table class="detail" cellpadding="0" cellspacing="0">
					<tr>
						<th><s:text name="binolbscnt10_counter"/></th>
						<td><span>(<s:property value="saleRecordInfo.CounterCode"/>)<s:property value="saleRecordInfo.CounterNameIF"/></span></td>
						<th><s:text name="binolbscnt10_employeeCode"/></th>
						<td><span><s:property value="saleRecordInfo.EmployeeCode"/></span></td>
					</tr>
					<tr>
						<th><s:text name="binolbscnt10_billCode"/></th>
						<td><span><s:property value="saleRecordInfo.BillCode"/></span></td>
						<th><s:text name="binolbscnt10_saleType"/></th>
						<td>
							<span><s:property value='#application.CodeTable.getVal("1213", saleRecordInfo.SaleType)' /></span>
						</td>
					</tr>
					<tr>
						<th><s:text name="binolbscnt10_saleTime"/></th>
						<td><span><s:property value="saleRecordInfo.SaleTime"/></span></td>
						<th><s:text name="binolbscnt10_memberCardCount"/></th>
						<td><span><s:property value="saleRecordInfo.MemCode" /></span></td>
					</tr>
					<tr>
						<th><s:text name="binolbscnt10_Amount"/></th>
						<td>
							<span>
								<s:if test='%{saleRecordInfo.Amount != null && !"".equals(saleRecordInfo.Amount)}'>
									<s:text name="format.price"><s:param value="saleRecordInfo.Amount"/></s:text>
								</s:if>
							</span>
						</td>
						<th><s:text name="binolbscnt10_quantityTotal"/></th>
						<td>
							<span>
								<s:if test='%{saleRecordInfo.Quantity != null && !"".equals(saleRecordInfo.Quantity)}'>
								 	<s:text name="format.number"><s:param value="saleRecordInfo.Quantity"/></s:text>
								</s:if>
							</span>
						</td>
					</tr>

				</table>
			</div>
		</div>

		<div class="section">
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="binolbscnt10_saleRecordDetailInfo" />
				</strong>
			</div>
			<div class="section-content">
				<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="saleRecordDetailTable" style="width: 100%;">
					<tbody>
					<tr>
						<th width="16%"><s:text name="binolbscnt10_productName" /></th>
						<th width="16%"><s:text name="binolbscnt10_unitCode" /></th>
						<th width="16%"><s:text name="binolbscnt10_barCode" /></th>
						<th width="16%"><s:text name="binolbscnt10_saleDetailType" /></th>
						<th width="16%"><s:text name="binolbscnt10_price" /></th>
						<th width="16%"><s:text name="binolbscnt10_quantity" /></th>
					</tr>
					<s:iterator value="saleRecordInfo.saleRecordInfoDetail" id="saleRecordInfoDetailMap" status="status">
						<tr>

							<td class='<s:property value="tdClassValue" />'><s:property value="#saleRecordInfoDetailMap.NameTotal" /></td>
							<td class='<s:property value="tdClassValue" />'><s:property value="#saleRecordInfoDetailMap.UnitCode" /></td>
							<td class='<s:property value="tdClassValue" />'><s:property value="#saleRecordInfoDetailMap.BarCode" /></td>
							<td class='<s:property value="tdClassValue" />'><s:property value='#application.CodeTable.getVal("1106", #saleRecordInfoDetailMap.SaleType)' /></td>
							<td class='<s:property value="tdClassValue" />'>
								<s:if test='%{#saleRecordInfoDetailMap.PricePay != null && !"".equals(#saleRecordInfoDetailMap.PricePay)}'>
									<s:text name="format.price"><s:param value="#saleRecordInfoDetailMap.PricePay"></s:param></s:text>
								</s:if>
							</td>
							<td class='<s:property value="tdClassValue" />'>
								<s:if test='%{#saleRecordInfoDetailMap.Quantity != null && !"".equals(#saleRecordInfoDetailMap.Quantity)}'>
									<s:text name="format.number"><s:param value="#saleRecordInfoDetailMap.Quantity"></s:param></s:text>
								</s:if>
							</td>
						</tr>
					</s:iterator>
					</tbody>
				</table>
			</div>
		</div>
	</div>

</s:i18n>