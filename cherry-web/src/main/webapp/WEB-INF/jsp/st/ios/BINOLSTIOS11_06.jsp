<%--退库申请单(excel导入)明细 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
<s:i18n name="i18n.st.BINOLSTIOS11">
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
							<%-- 单号 --%>
							<th><s:text name="IOS11_billNo" /></th>
							<td><s:property value="billExcelInfo.billNo" /></td>
							<%-- 导入日期 --%>
							<th><s:text name="IOS11_importDate" /></th>
							<td><s:property value="billExcelInfo.importDate" /></td>
						</tr>
						<tr>
							<%-- 退库部门 --%>
							<th><s:text name="IOS11_departNameFrom" /></th>
							<td>
								<s:property value="billExcelInfo.departNameFrom" />
							</td>
							<%-- 接收退库部门 --%>
							<th><s:text name="IOS11_departNameTo" /></th>
							<td>
								<s:property value="billExcelInfo.departNameTo" />
							</td>
						</tr>
						<tr>
							<%-- 退库实体仓库 --%>
							<th><s:text name="IOS11_inventoryNameFrom" /></th>
							<td>
								<s:property value="billExcelInfo.inventoryNameFrom" />
							</td>
							<%-- 接收退库实体仓库 --%>
							<th><s:text name="IOS11_inventoryNameTo" /></th>
							<td>
								<s:property value="billExcelInfo.inventoryNameTo" />
							</td>
						</tr>
						<tr>
							<%-- 退库逻辑仓库 --%>
							<th><s:text name="IOS11_logicInventoryNameFrom" /></th>
							<td><s:property value="billExcelInfo.logicInventoryNameFrom" /></td>
							<%-- 接收退库逻辑仓库 --%>
							<th><s:text name="IOS11_logicInventoryNameTo" /></th>
							<td><s:property value="billExcelInfo.logicInventoryNameTo" /></td>
						</tr>
						<tr>
							<%-- 导入状态 --%>
							<th><s:text name="IOS11_importResult" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1250',billExcelInfo.importResult)"/>
							</td>
							<%-- 制单员工 --%>
							<th><s:text name="IOS11_employee" /></th>
							<td>
								<s:property value="billExcelInfo.employeeName" />
							</td>
						</tr>
						<tr>
							<%-- 导入理由 --%>
							<th><s:text name="IOS11_comments" /></th>
							<td colspan="3"><s:property value='billExcelInfo.comments' /></td>
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
				<s:text name="IOS11_results_list" /> </strong>
		</div>
		<div class="section-content">
				<div id="dataTable" style="overflow-x: auto; overflow-y: hidden">
					<table id="detailDataTable1" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<%-- 编号 --%>
								<th class="center"><s:text name="IOS11_No" /></th>
								<%-- 厂商编码 --%>
								<th class="center"><s:text name="IOS11_unitCode" /></th>
								<%-- 产品条码 --%>
								<th class="center"><s:text name="IOS11_barCode" /></th>
								<%-- 产品名称 --%>
								<th class="center"><s:text name="IOS11_nameTotal" /></th>
								<%-- 单价 --%>
								<th class="center"><s:text name="IOS11_price" /></th>
								<%-- 数量 --%>
								<th class="center"><s:text name="IOS11_quanlity" /></th>
								<%-- 金额 --%>
								<th class="center"><s:text name="IOS11_amount" /></th>
								<%-- 错误原因--%>
								<th class="center"><s:text name="IOS11_errorMsg" /></th>
							</tr>
						</thead>
						<tbody id="databody">
							<s:iterator value="billExcelDetailList" status="status">
								<tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
									<td id="dataTd1"><s:property value="#status.index+1" /></td>
									<td id="dataTd2"><span><s:property value="unitCode" /></span></td>
									<td id="dataTd3"><span><s:property value="barCode" /></span></td>
									<td id="dataTd4"><span><s:property value="nameTotal" /></span></td>
									<td id="dataTd6">
										<span>
											<s:if test='null!=price'>
												<s:text name="format.price">
													<s:param value="price"></s:param>
												</s:text>
											</s:if>
										</span>
									</td>
									<td id="dataTd7" style="width: 10%;"><span><s:property value="quantity" /></span></td>
									<td id="dataTd8">
										<s:if test='null!=price && null!=quantity'>
											<s:text name="format.price">
												<s:param value="price*quantity"></s:param>
											</s:text>
										</s:if>
									</td>
									<td id="dataTd9">
										<span>
											<a class="description" style="cursor: pointer;"  title="<s:text name="IOS11_errorMsg" /> | <s:property value="errorMsg" />">
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
						<td class="center"><s:text name="IOS11_totalQuantity" /></td>
						<%-- 总数量 --%>
						<td class="center"><s:text name="IOS11_totalAmount" /></td>
						<%-- 总金额--%>
					</tr>
					<tr>
						<td class="center">
							<span><s:property value="billExcelInfo.totalQuantity" /></span>
						</td>
						<td class="center">
							<span>
								<s:text name="format.price">
									<s:param value="billExcelInfo.totalAmount"></s:param>
								</s:text>
							</span>
						</td>
					</tr>
				</table>
				<hr class = "space" />
		</div>
	</div>
</s:i18n>