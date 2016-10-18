<%--订货单(excel导入)明细 --%>
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
<s:i18n name="i18n.st.BINOLSTSFH18">
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
							<%-- 订货单号 --%>
							<th><s:text name="SFH18_billNo" /></th>
							<td><s:property value="prtOrderExcelInfo.billNo" /></td>
							<%-- 导入日期 --%>
							<th><s:text name="SFH18_importDate" /></th>
							<td><s:property value="prtOrderExcelInfo.importDate" /></td>
						</tr>
						<tr>
							<%-- 订货部门 --%>
							<th><s:text name="SFH18_departNameOrder" /></th>
							<td>
								<s:property value="prtOrderExcelInfo.departNameOrder" />
							</td>
							<%-- 发货部门 --%>
							<th><s:text name="SFH18_departNameAccept" /></th>
							<td>
								<s:property value="prtOrderExcelInfo.departNameAccept" />
							</td>
						</tr>
						<tr>
							<%-- 订货实体仓库 --%>
							<th><s:text name="SFH18_inventoryName" /></th>
							<td>
								<s:property value="prtOrderExcelInfo.inventoryName" />
							</td>
							<%-- 发货实体仓库 --%>
							<th><s:text name="SFH18_inventoryNameAccept" /></th>
							<td>
								<s:property value="prtOrderExcelInfo.inventoryNameAccept" />
							</td>
						</tr>
						<tr>
							<%-- 订货逻辑仓库 --%>
							<th><s:text name="SFH18_logicInventoryName" /></th>
							<td><s:property value="prtOrderExcelInfo.logicInventoryName" /></td>
							<%-- 发货逻辑仓库 --%>
							<th><s:text name="SFH18_logicInventoryNameAccept" /></th>
							<td><s:property value="prtOrderExcelInfo.logicInventoryNameAccept" /></td>
						</tr>
						<tr>
							<%-- 导入状态 --%>
							<th><s:text name="SFH18_importResult" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1250',prtOrderExcelInfo.importResult)"/>
							</td>
							<%-- 制单员工 --%>
							<th><s:text name="SFH18_employee" /></th>
							<td>
								<s:property value="prtOrderExcelInfo.employeeName" />
							</td>
						</tr>
						<tr>
							<%-- 订货处理状态 --%>
							<th><s:text name="SFH18_tradeStatus" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1142',prtOrderExcelInfo.tradeStatus)"/>
							</td>
							<%-- 导入理由 --%>
							<th><s:text name="SFH18_comments" /></th>
							<td><s:property value='prtOrderExcelInfo.comments' /></td>
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
				<s:text name="SFH18_results_list" /> </strong>
		</div>
		<div class="section-content">
				<div id="dataTable" style="overflow-x: auto; overflow-y: hidden">
					<table id="detailDataTable1" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<%-- 编号 --%>
								<th class="center"><s:text name="SFH18_No" /></th>
								<%-- 厂商编码 --%>
								<th class="center"><s:text name="SFH18_unitCode" /></th>
								<%-- 产品条码 --%>
								<th class="center"><s:text name="SFH18_barCode" /></th>
								<%-- 产品名称 --%>
								<th class="center"><s:text name="SFH18_nameTotal" /></th>
								<%-- 单价 --%>
								<th class="center"><s:text name="SFH18_price" /></th>
								<%-- 数量 --%>
								<th class="center"><s:text name="SFH18_quanlity" /></th>
								<%-- 金额 --%>
								<th class="center"><s:text name="SFH18_amount" /></th>
								<%-- 错误原因--%>
								<th class="center"><s:text name="SFH18_errorMsg" /></th>
							</tr>
						</thead>
						<tbody id="databody">
							<s:iterator value="prtOrderExcelDetailList" status="status">
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
											<a class="description" style="cursor: pointer;"  title="<s:text name="SFH18_errorMsg" /> | <s:property value="errorMsg" />">
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
						<td class="center"><s:text name="SFH18_totalQuantity" /></td>
						<%-- 总数量 --%>
						<td class="center"><s:text name="SFH18_totalAmount" /></td>
						<%-- 总金额--%>
					</tr>
					<tr>
						<td class="center">
							<span><s:property value="prtOrderExcelInfo.totalQuantity" /></span>
						</td>
						<td class="center">
							<span>
								<s:text name="format.price">
									<s:param value="prtOrderExcelInfo.totalAmount"></s:param>
								</s:text>
							</span>
						</td>
					</tr>
				</table>
				<hr class = "space" />
		</div>
	</div>
</s:i18n>