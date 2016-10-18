<%--发货单(excel导入)明细 --%>
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
<s:i18n name="i18n.st.BINOLSTSFH13">
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
							<%-- 发货单号 --%>
							<th><s:text name="SFH13_billNo" /></th>
							<td><s:property value="prtDeliverExcelInfo.billNo" /></td>
							<%-- 导入日期 --%>
							<th><s:text name="SFH13_importDate" /></th>
							<td><s:property value="prtDeliverExcelInfo.importDate" /></td>
						</tr>
						<tr>
							<%-- 关联单据号 --%>
							<th><s:text name="SFH13_relevanceNo" /></th>
                            <td><s:property value="prtDeliverExcelInfo.relevanceNo" /></td>
							<%-- 发货部门 --%>
							<th><s:text name="SFH13_departNameTo" /></th>
							<td>
								<s:if test='prtDeliverExcelInfo.departCodeTo != null && !"".equals(prtDeliverExcelInfo.departCodeTo)'>
									（<s:property value="prtDeliverExcelInfo.departCodeTo" />）
								</s:if>
								<s:property value="prtDeliverExcelInfo.departNameTo" />
							</td>
						</tr>
						<tr>
							<%-- 发货部门 --%>
							<th><s:text name="SFH13_departNameFrom" /></th>
							<td>
								<s:if test='prtDeliverExcelInfo.departCodeFrom != null && !"".equals(prtDeliverExcelInfo.departCodeFrom)'>
									（<s:property value="prtDeliverExcelInfo.departCodeFrom" />）
								</s:if>
								<s:property value="prtDeliverExcelInfo.departNameFrom" />
							</td>
							<%-- 发货逻辑仓库 --%>
							<th><s:text name="SFH13_logicInventoryName" /></th>
							<td><s:property value="prtDeliverExcelInfo.logicInventoryName" /></td>
						</tr>
						<tr>
							<%-- 发货实体仓库 --%>
							<th><s:text name="SFH13_inventoryName" /></th>
							<td>
								<s:if test='prtDeliverExcelInfo.inventoryCode != null && !"".equals(prtDeliverExcelInfo.inventoryCode)'>
									（<s:property value="prtDeliverExcelInfo.inventoryCode" />）
								</s:if>
								<s:property value="prtDeliverExcelInfo.inventoryName" />
							</td>
							<%-- 制单员工 --%>
							<th><s:text name="SFH13_employee" /></th>
							<td>
								<s:if test='prtDeliverExcelInfo.employeeCode != null && !"".equals(prtDeliverExcelInfo.employeeCode)'>
									（<s:property value="prtDeliverExcelInfo.employeeCode" />）
								</s:if>
								<s:property value="prtDeliverExcelInfo.employeeName" />
							</td>
						</tr>
						<tr>
							<%-- 导入状态 --%>
							<th><s:text name="SFH13_importResult" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1250',prtDeliverExcelInfo.importResult)"/>
							</td>
							<%-- 导入理由 --%>
							<th><s:text name="SFH13_comments" /></th>
							<td><s:property value='prtDeliverExcelInfo.comments' /></td>
						</tr>
                        <tr>
							<%-- 发货状态 --%>
							<th><s:text name="SFH13_tradeStatus" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1141',prtDeliverExcelInfo.tradeStatus)"/>
							</td>
                            <%--预计到货日期 --%>
                            <th><s:text name="SFH13_planArriveDate" /></th>
                            <td><s:property value='prtDeliverExcelInfo.planArriveDate' /></td>

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
				<s:text name="SFH13_results_list" /> </strong>
		</div>
		<div class="section-content">
				<div id="dataTable" style="overflow-x: auto; overflow-y: hidden">
					<table id="detailDataTable1" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<%-- 编号 --%>
								<th class="center"><s:text name="SFH13_No" /></th>
								<%-- 厂商编码 --%>
								<th class="center"><s:text name="SFH13_unitCode" /></th>
								<%-- 产品条码 --%>
								<th class="center"><s:text name="SFH13_barCode" /></th>
								<%-- 产品名称 --%>
								<th class="center"><s:text name="SFH13_nameTotal" /></th>
                                <%-- 参考价格 --%>
                                <th class="center"><s:text name="SFH13_referencePrice" /></th>
								<%-- 单价 --%>
								<th class="center"><s:text name="SFH13_price" /></th>
								<%-- 数量 --%>
								<th class="center"><s:text name="SFH13_quanlity" /></th>
								<%-- 金额 --%>
								<th class="center"><s:text name="SFH13_amount" /></th>
								<%-- 错误原因--%>
								<th class="center"><s:text name="SFH13_errorMsg" /></th>
								<s:if test='"1".equals(configVal)'>
									<%-- 金蝶码--%>
									<th class="center"><s:text name="SFH13_erpCode" /></th>
								</s:if>
							</tr>
						</thead>
						<tbody id="databody">
							<s:iterator value="prtDeliverExcelDetailList" status="status">
								<tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
									<td id="dataTd1"><s:property value="#status.index+1" /></td>
									<td id="dataTd2"><span><s:property value="unitCode" /></span></td>
									<td id="dataTd3"><span><s:property value="barCode" /></span></td>
									<td id="dataTd4"><span><s:property value="nameTotal" /></span></td>
                                    <td id="dataTdRP">
                                        <span>
                                            <s:if test='null!=referencePrice'>
                                                <s:text name="format.price">
                                                    <s:param value="referencePrice"></s:param>
                                                </s:text>
                                            </s:if>
                                        </span>
                                    </td>
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
											<a class="description" style="cursor: pointer;"  title="<s:text name="SFH13_errorMsg" /> | <s:property value="errorMsg" />">
										        <s:if test="%{null!=errorMsg&&errorMsg.length()>16}">
										          <s:property value="%{errorMsg.substring(0, 12)}" />...
										 		</s:if>
										 		<s:else>
										          <s:property value="errorMsg" />
										   		</s:else>
									        </a>
								        </span>
									</td>
									<s:if test='"1".equals(configVal)'>
										<td id="dataTd10">
											<span>
												<s:if test='null != erpCode'>
														<s:property value="erpCode"></s:property>
												</s:if>
											</span>
										</td>
									</s:if>
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
						<td class="center"><s:text name="SFH13_totalQuantity" /></td>
						<%-- 总数量 --%>
						<td class="center"><s:text name="SFH13_totalAmount" /></td>
						<%-- 总金额--%>
					</tr>
					<tr>
						<td class="center">
							<span><s:property value="prtDeliverExcelInfo.totalQuantity" /></span>
						</td>
						<td class="center">
							<span>
								<s:text name="format.price">
									<s:param value="prtDeliverExcelInfo.totalAmount"></s:param>
								</s:text>
							</span>
						</td>
					</tr>
				</table>
				<hr class = "space" />
		</div>
	</div>
</s:i18n>