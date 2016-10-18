<%--后台销售单(excel导入)明细 --%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<style>
.box-header {
	padding: 0.2em 0;
}

table.detail {
	margin-bottom: 3px;
}
</style>
<s:i18n name="i18n.st.BINOLSTSFH20">
	<div class="section">
		<div class="section-header">
			<strong><span class="ui-icon icon-ttl-section-info"></span>
				<%-- 基本信息 --%> 
				<s:text name="global.page.title" /> </strong>
		</div>
		<div class="section-content">
			<div>
				<div class="box-header"></div>
				<table class="detail">
					<tbody>
						<tr>
							<%-- 后台销售单号 --%>
							<th><s:text name="SFH20_billNo" /></th>
							<td><s:property value="backstageSaleExcelInfo.billNo" /></td>
							<%-- 导入日期 --%>
							<th><s:text name="SFH20_importDate" /></th>
							<td><s:property value="backstageSaleExcelInfo.importDate" /></td>
						</tr>
						<tr>
							<%--订单类型 --%>
							<th><s:text name="SFH20_saleBillType" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1293',backstageSaleExcelInfo.saleBillType)" />
							</td>
							<%-- 客户类型 --%>
							<th><s:text name="SFH20_customerType" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1297',backstageSaleExcelInfo.customerType)" />
							</td>
						</tr>
						<tr>
							<%-- 销售部门 --%>
							<th><s:text name="SFH20_departSale" /></th>
							<td>
								<s:property value="backstageSaleExcelInfo.departSale" />
							</td>
							<%-- 客户部门 --%>
							<th><s:text name="SFH20_departCustomer" /></th>
							<td>
								<s:property value="backstageSaleExcelInfo.departCustomer" />
							</td>
						</tr>
						<tr>
							<%-- 销售实体仓库 --%>
							<th><s:text name="SFH20_depotNameSale" /></th>
							<td>
								<s:property value="backstageSaleExcelInfo.inventorySale" />
							</td>
							<%-- 客户实体仓库 --%>
							<th><s:text name="SFH20_depotNameCustomer" /></th>
							<td>
								<s:property value="backstageSaleExcelInfo.inventoryCustomer" />
							</td>
						</tr>
						<tr>
							<%-- 销售逻辑仓库 --%>
							<th><s:text name="SFH20_logicInventoryNameSale" /></th>
							<td><s:property value="backstageSaleExcelInfo.logicInventorySale" /></td>
							<%-- 客户逻辑仓库 --%>
							<th><s:text name="SFH20_logicInventoryNameCustomer" /></th>
							<td><s:property value="backstageSaleExcelInfo.logicInventoryCustomer" /></td>
						</tr>
						<tr>
							<%-- 联系人 --%>
							<th><s:text name="SFH20_contactPerson" /></th>
							<td>
								<s:property value="backstageSaleExcelInfo.contactPerson" />
							</td>
							<%-- 送货地址 --%>
							<th><s:text name="SFH20_deliverAddress" /></th>
							<td>
								<s:property value="backstageSaleExcelInfo.deliverAddress" />
							</td>
						</tr>
						<tr>
							<%-- 销售日期 --%>
							<th><s:text name="SFH20_saleDate" /></th>
							<td>
								<s:property value="backstageSaleExcelInfo.saleDate" />
							</td>
							<%-- 送货日期 --%>
							<th><s:text name="SFH20_expectFinishDate" /></th>
							<td>
								<s:property value="backstageSaleExcelInfo.expectFinishDate" />
							</td>
						</tr>
						<tr>
							<%--结算方式 --%>
							<th><s:text name="SFH20_settlement" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1175',backstageSaleExcelInfo.settlement)" />
							</td>
							<%-- 币种 --%>
							<th><s:text name="SFH20_currency" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1296',backstageSaleExcelInfo.currency)" />
							</td>
						</tr>
						<tr>
							<%-- 导入状态 --%>
							<th><s:text name="SFH20_importResult" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1250',backstageSaleExcelInfo.importResult)"/>
							</td>
							<%-- 制单员工 --%>
							<th><s:text name="SFH20_employee" /></th>
							<td>
								<s:property value="backstageSaleExcelInfo.employee" />
							</td>
						</tr>
						<tr>
							<%-- 单据状态 --%>
							<th><s:text name="SFH20_billStatus" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1294',backstageSaleExcelInfo.billState)"/>
							</td>
							<%-- 导入理由 --%>
							<th><s:text name="SFH20_comments" /></th>
							<td><s:property value='backstageSaleExcelInfo.comments' /></td>
						</tr>
					</tbody>
				</table>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>

	<div class="section">
		<div class="section-header">
			<strong><span
				class="ui-icon icon-ttl-section-list"></span> 
				<%-- 明细一览 --%>
				<s:text name="SFH20_results_list" /> </strong>
		</div>
		<div class="section-content">
				<div id="dataTable" style="overflow-x: auto; overflow-y: hidden">
					<table id="detailDataTable1" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<%-- 编号 --%>
								<th class="center"><s:text name="SFH20_No" /></th>
								<%-- 厂商编码 --%>
								<th class="center"><s:text name="SFH20_unitCode" /></th>
								<%-- 产品条码 --%>
								<th class="center"><s:text name="SFH20_barCode" /></th>
								<%-- 产品名称 --%>
								<th class="center"><s:text name="SFH20_nameTotal" /></th>
								<%-- 单价 --%>
								<th class="center"><s:text name="SFH20_price" /></th>
								<%-- 数量 --%>
								<th class="center"><s:text name="SFH20_quanlity" /></th>
								<%-- 金额 --%>
								<th class="center"><s:text name="SFH20_amount" /></th>
								<%-- 错误原因--%>
								<th class="center"><s:text name="SFH20_errorMsg" /></th>
							</tr>
						</thead>
						<tbody id="databody">
							<s:iterator value="backstageSaleExcelDetailList" status="status">
								<tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
									<td id="dataTd1"><s:property value="#status.index+1" /></td>
									<td id="dataTd2"><span><s:property value="unitCode" /></span></td>
									<td id="dataTd3"><span><s:property value="barCode" /></span></td>
									<td id="dataTd4"><span><s:property value="nameTotal" /></span></td>
									<td id="dataTd5">
										<span>
											<s:if test='null!=price'>
												<s:text name="format.price">
													<s:param value="price"></s:param>
												</s:text>
											</s:if>
										</span>
									</td>
									<td id="dataTd6" style="width: 10%;"><span><s:property value="quantity" /></span></td>
									<td id="dataTd7">
										<s:if test='null!=price && null!=quantity'>
											<s:text name="format.price">
												<s:param value="price*quantity"></s:param>
											</s:text>
										</s:if>
									</td>
									<td id="dataTd8">
										<span>
											<a class="description" style="cursor: pointer;"  title="<s:text name="SFH20_errorMsg" /> | <s:property value="errorMsg" />">
										        <s:if test="%{null!=errorMsg&&errorMsg.length()>16}">
										          <s:property value="%{errorMsg.substring(0, 12)}" />...
										 		</s:if>
										 		<s:else>
										          <s:property value="errorMsg" />
										   		</s:else>
									        </a>
								        </span>
									</td>
								</tr>
							</s:iterator>
						</tbody>	
					</table>
				</div>
				<hr class = "space" />
				<table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
					<tr>
						<th rowspan="2" scope="row" class="center"><s:text name="global.page.total" /></th>
						<%-- 合计 --%>
						<td class="center"><s:text name="SFH20_totalQuantity" /></td>
						<%-- 总数量 --%>
						<td class="center"><s:text name="SFH20_totalAmount" /></td>
						<%-- 总金额--%>
					</tr>
					<tr>
						<td class="center">
							<span><s:property value="backstageSaleExcelInfo.totalQuantity" /></span>
						</td>
						<td class="center">
							<span>
								<s:text name="format.price">
									<s:param value="backstageSaleExcelInfo.totalAmount"></s:param>
								</s:text>
							</span>
						</td>
					</tr>
				</table>
				<hr class = "space" />
		</div>
	</div>
</s:i18n>