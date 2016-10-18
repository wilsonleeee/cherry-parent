<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp"
	flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/cp/act/BINOLCPACT08.js"></script>
<style>
.box-header {
	padding: 0.2em 0;
}

table.detail {
	margin-bottom: 3px;
}
</style>
<s:set name="language" value="session.language" />
<s:i18n name="i18n.cp.BINOLCPACT08">
	<div class="main container clearfix">
		<div class="panel ui-corner-all">
			<div class="panel-header">
				<div class="clearfix">
					<span class="breadcrumb left"><span
						class="ui-icon icon-breadcrumb"></span> <s:text name="ACT08_title" />&nbsp;(<s:text
							name="ACT08_giftDrawBillNo" />:<s:property
							value="getGiftDrawDetail.billNoIF" />)</span>
				</div>
			</div>
			<div class="panel-content">
				<div class="section">
					<div class="section-header">
						<strong> <span class="ui-icon icon-ttl-section-info"></span>
							<%-- 基本信息 --%> <s:text name="global.page.title" />
						</strong>
					</div>
					<div class="section-content">
						<div class="box-header"></div>
						<table class="detail" cellpadding="0" cellspacing="0">
							<tr>
								<%-- 领用单据号 --%>
								<th><s:text name="ACT08_billNo" /></th>
								<td><s:property value="getGiftDrawDetail.billNoIF" /></td>
								<%-- 领用日期 --%>
								<th><s:text name="ACT08_getTime" /></th>
								<td>${getGiftDrawDetail.getTime}</td>
							</tr>
							<tr>
								<%-- 关联单据号 --%>
								<th><s:text name="ACT08_relevanceNo" /></th>
								<td><s:property value="getGiftDrawDetail.relevanceNo" /></td>
								<!-- 领用类型 -->
								<th><s:text name="ACT08_giftDrawType" /></th>
								<td><span><s:property
											value='#application.CodeTable.getVal("1178", getGiftDrawDetail.giftDrawType)' /></span></td>
							</tr>
							<tr>
								<%-- 会员 --%>
								<th><s:text name="ACT08_member" /></th>
								<td><s:if test="getGiftDrawDetail.memberCode != null">
										<span><s:property
												value='"("+getGiftDrawDetail.memberCode+")"' /></span>
									</s:if> <span><s:property value='getGiftDrawDetail.memberName' /></span>
								</td>
								<%-- 领用柜台--%>
								<th><s:text name="ACT08_counter" /></th>
								<td><s:if test='getGiftDrawDetail.counterCode != null'>
										<span><s:property
												value='"("+getGiftDrawDetail.counterCode+")"' /></span>
									</s:if> <span><s:property value='getGiftDrawDetail.counterName' /></span>
								</td>
							</tr>
							<tr>
								<%-- 会员手机号 --%>
								<th><s:text name="ACT08_memberPhone" /></th>
								<td><span><s:property
											value='getGiftDrawDetail.memberPhone' /></span></td>
								<!-- coupon码 -->
								<th><s:text name="ACT08_couponCode" /></th>
								<td><span><s:property
											value='getGiftDrawDetail.couponCode' /></span></td>
							</tr>
							<tr>
								<%-- 会员生日 --%>
								<th><s:text name="ACT08_memberBirth" /></th>
								<td><span><s:property
											value='getGiftDrawDetail.memberBirth' /></span></td>
								<!-- 主题活动 -->
								<th><s:text name="ACT08_activity" /></th>
								<td><s:if test="getGiftDrawDetail.activityCode != null">
										<span><s:property
												value='"("+getGiftDrawDetail.activityCode+")"' /></span>
									</s:if> <span><s:property
											value='getGiftDrawDetail.activityName' /></span></td>

							</tr>
							<tr>
								<%-- 会员入会日期 --%>
								<th><s:text name="ACT08_joinDate" /></th>
								<td><span><s:property
											value='getGiftDrawDetail.joinDate' /></span></td>
								<%-- 操作员 --%>
								<th><s:text name="ACT08_employee" /></th>
								<td><s:if test="getGiftDrawDetail.employeeCode != null">
										<span><s:property
												value='"("+getGiftDrawDetail.employeeCode+")"' /></span>
									</s:if> <span><s:property
											value='getGiftDrawDetail.employeeName' /></span></td>

							</tr>
							<tr>
								<!-- 会员类型 -->
								<th><s:text name="ACT08_testType" /></th>
								<td><span><s:property
											value='#application.CodeTable.getVal("1256", getGiftDrawDetail.testType)' /></span></td>
								<!-- 备注（主表） -->
								<th><s:text name="ACT08_comment" /></th>
								<td><span><s:property
											value="getGiftDrawDetail.comments" /></span></td>
							</tr>
						</table>
						<div class="clearfix"></div>
					</div>
				</div>
				<div class="section">
					<div class="section-header">
						<%-- 礼品领用详细一览 --%>
						<strong> <span
							class="ui-icon icon-ttl-section-search-result"></span> <s:text
								name="ACT08_results_list" />
						</strong>
					</div>
					<div class="section-content">
						<div id="table_scroll"
							style="overflow-x: auto; overflow-y: hidden">
							<table cellpadding="0" cellspacing="0" border="0"
								class="jquery_table" width="100%">
								<thead>
									<tr>
										<th class="center" width="1%"><s:text name="ACT08_num" /></th>
										<%-- 编号 --%>
										<th class="center" width="10%"><s:text
												name="ACT08_unitCode" /></th>
										<%-- 厂商编码 --%>
										<th class="center" width="10%"><s:text
												name="ACT08_prtName" /></th>
										<%-- 产品名称 --%>
										<th class="center" width="10%"><s:text
												name="ACT08_barCode" /></th>
										<%-- 产品条码 --%>
										<th class="center" width="5%"><s:text name="ACT08_price" /></th>
										<%-- 价格 --%>
										<th class="center" width="5%"><s:text
												name="ACT08_quantity" /></th>
										<%-- 数量 --%>
										<th class="center" width="15%"><s:text
												name="ACT08_in_activities" /></th>
										<%-- 参与活动--%>
										<th class="center" width="20%"><s:text
												name="ACT08_comment" /></th>
										<%-- 备注 --%>
									</tr>
								</thead>
								<tbody>
									<s:iterator value="getGiftDrawPrtDetail" status="status">
										<tr
											class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
											<td><s:property value="#status.index+1" /></td>
											<td><span><s:property value="unitCode" /></span></td>
											<td><span><s:property value="productName" /></span></td>
											<td><span><s:property value="barCode" /></span></td>
											<td class="alignRight"><s:text name="format.price">
													<s:param value="price"></s:param>
												</s:text></td>
											<td class="alignRight"><s:text name="format.number">
													<s:param value="quantity"></s:param>
												</s:text></td>
											<%-- 参与活动 --%>
											<td class="center"><s:if test="inActivityCode != null">
													<span><s:property value='"("+inActivityCode+")"' /></span>
												</s:if> <span class="gray"><s:property
														value='inActivityName' /></span></td>
											<td><p>
													<s:property value="comment" />
												</p></td>
										</tr>
									</s:iterator>
								</tbody>
							</table>
						</div>
						<hr class="space" />
						<table cellpadding="0" cellspacing="0" width="30%" border="0"
							class="right editable">
							<tr>
								<th rowspan="2" scope="row" class="center"><s:text
										name="global.page.total" /></th>
								<%-- 合计 --%>
								<td class="center"><s:text name="ACT08_totalQuantity" /></td>
								<%-- 总数量 --%>
								<td class="center"><s:text name="ACT08_totalAmount" /></td>
								<%-- 总金额--%>
							</tr>
							<tr>
								<td class="center"><s:if
										test="getGiftDrawDetail.totalQuantity >= 0">
										<s:text name="format.number">
											<s:param value="getGiftDrawDetail.totalQuantity"></s:param>
										</s:text>
									</s:if> <s:else>
										<span class="highlight"><s:text name="format.number">
												<s:param value="getGiftDrawDetail.totalQuantity"></s:param>
											</s:text></span>
									</s:else></td>
								<td class="center"><s:if
										test="getGiftDrawDetail.totalAmount >= 0">
										<s:text name="format.price">
											<s:param value="getGiftDrawDetail.totalAmount"></s:param>
										</s:text>
									</s:if> <s:else>
										<span class="highlight"><s:text name="format.price">
												<s:param value="getGiftDrawDetail.totalAmount"></s:param>
											</s:text></span>
									</s:else></td>
							</tr>
						</table>
						<hr class="space" />
						<div class="center">
							<button class="close" onclick="window.close();">
								<span class="ui-icon icon-close"></span>
								<%-- 关闭 --%>
								<span class="button-text"><s:text
										name="global.page.close" /></span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</s:i18n>
