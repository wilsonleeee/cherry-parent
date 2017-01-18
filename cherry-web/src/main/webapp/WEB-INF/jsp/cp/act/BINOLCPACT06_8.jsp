<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript">
var holidays = '${holidays}';
$('#fromTime').cherryDate({
});
$('#endTime').cherryDate({
	holidayObj: holidays,
	beforeShow: function(input){
		var from = new Date($('#getFromTimeOld').val());
		var today = new Date();
		if(today > from){
			from = today;
		}
		return [changeDateToString(from),'minDate'];
	}
});
</script>
<s:i18n name="i18n.cp.BINOLCPACT06">
 <%-- ================== 信息提示区 START ======================= --%>
   <div id="actionDisplay">
	<div id="errorDiv" class="actionError" style="display:none;">
		<ul>
		    <li><span id="errorSpan"></span></li>
		</ul>			
  	</div>
  	<div id="successDiv" class="actionSuccess" style="display:none;">
		<ul class="actionMessage">
	  		<li><span id="successSpan"></span></li>
	 	</ul>
	</div>
	</div>
	<div id="errorMessage"></div>
	<%-- ================== 信息提示区   END  ======================= --%>
<div class="section">
		<div class="section-header">
			<strong> <span class="ui-icon icon-ttl-section-info"></span>
				<%-- 基本信息 --%> <s:text name="global.page.title" />
			</strong>
		</div>
		<form id="detailForm" method="post" class="inline">
		<div class="section-content">
			<s:hidden name="campOrderId" value="%{campDetailMap.campOrderId}"></s:hidden>
			<s:hidden name="modifyTime" value="%{campDetailMap.modifyTime}"></s:hidden>
	        <s:hidden name="modifyCount" value="%{campDetailMap.modifyCount}"></s:hidden>
	        <s:hidden name="counterGotOld" value="%{campDetailMap.counterGot}"></s:hidden>
	        <s:hidden name="getToTimeOld" value="%{campDetailMap.getToTime}"></s:hidden>
	        <s:hidden name="getFromTimeOld" value="%{campDetailMap.getFromTime}"></s:hidden>
	        <s:hidden name="sendFlag" value="%{campDetailMap.sendFlag}"></s:hidden>
	        <s:hidden name="campState" value="%{campDetailMap.state}"></s:hidden>
	        <s:hidden name="flagCountId" value="0"></s:hidden>
	        <s:hidden name="flagFromTimeId" value="0"></s:hidden>
	        <s:hidden name="flagTimeId" value="0"></s:hidden>
	        <s:hidden name="tradeNoIF" value="%{campDetailMap.billNo}"></s:hidden>
			<table class="detail" cellpadding="0" cellspacing="0">
				<tr>
					<%-- 活动单据 --%>
					<th><s:text name="ACT06_tradeNoIF" /></th>
					<td><s:property value="campDetailMap.billNo" /></td>
					<%-- 预约时间 --%>
					<th><s:text name="ACT06_campOrderTime" /></th>
					<td>${campDetailMap.campOrderTime}</td>
				</tr>
				<tr>
					<%-- 会员 --%>
					<th><s:text name="ACT06_memNameCode" /></th>
					<td>
						<s:if test="campDetailMap.memId != null && campDetailMap.memId != 0"><s:property value="campDetailMap.memNameCode"/></s:if><s:else><s:property value="campDetailMap.name"/></s:else>
					</td>
					<%-- 预约柜台--%>
					<th><s:text name="ACT06_ordercountCode" /></th>
					<td>
						<span><s:property value="campDetailMap.counterOrder" /></span>
					</td>
				</tr>
				<tr>
					<%-- 会员手机号 --%>
					<th><s:text name="ACT06_mobile" /></th>
					<td><span><s:property value='campDetailMap.mobile' /></span></td>
					<!-- coupon码 -->
					<th><s:text name="ACT06_couponCode" /></th>
					<td><span><s:property value='campDetailMap.couponCode' /></span></td>
				</tr>
				<tr>
					<%-- 会员微信号 --%>
					<th><s:text name="ACT06_messageId" /></th>
					<td><span><s:property value='campDetailMap.messageId' /></span></td>
					<%-- 单据状态 --%>
					<th><s:text name="ACT06_state" /></th>
					<td><span id="campStateId"><s:property value="#application.CodeTable.getVal('1116',campDetailMap.state)"/></span></td>
				</tr>
				<tr>
					<!-- 会员类型 -->
					<th><s:text name="ACT06_testType" /></th>
					<td><span><s:property value='#application.CodeTable.getVal("1256", campDetailMap.testType)' /></span></td>
					<!-- 会员类型 -->
					<th><s:text name="ACT06_receiverName"/></th>
					<td><span><s:property value='campDetailMap.receiverName' /></span></td>
				</tr>
				
				<tr>
					<!-- 收货人电话 -->
					<th><s:text name="ACT06_receiverMobile"/></th>
					<td><span><s:property value='campDetailMap.receiverMobile' /></span></td>
					<!-- 收货人省份 -->
					<th><s:text name="ACT06_deliveryProvince"/></th>
					<td><span><s:property value='campDetailMap.deliveryProvince' /></span></td>
				</tr>
				<tr>
					<!-- 收货人城市 -->
					<th><s:text name="ACT06_deliveryCity"/></th>
					<td><span><s:property value='campDetailMap.deliveryCity' /></span></td>
					<!-- 收货人省份 -->
					<th><s:text name="ACT06_deliveryAddress"/></th>
					<td><span><s:property value='campDetailMap.deliveryAddress' /></span></td>
				</tr>
				<tr>
					<!-- 快递公司 -->
					<th><s:text name="ACT06_expressCode"/></th>
					<td><span><s:property value='#application.CodeTable.getVal("1421", campDetailMap.expressCode)' /></span></td>
					<!-- 快递单号 -->
					<th><s:text name="ACT06_expressNo"/></th>
					<td><span><s:property value='campDetailMap.expressNo' /></span></td>
				</tr>
				<tr>
					<th><s:text name="ACT06_confirmDispatchTime"/></th>
					<td><span><s:property value='campDetailMap.dispatchTime' /></span></td>
					<th></th>
					<td></td>
				</tr>
			</table>
			<div class="clearfix"></div>
		</div>
		</form>
	</div>
	<div class="section">
		<div class="section-header">
			<%-- 活动明细详细一览 --%>
			<strong> <span
				class="ui-icon icon-ttl-section-search-result"></span> <s:text
					name="ACT06_prt_list" />
			</strong>
		</div>
		<div class="section-content">
			<div id="table_scroll"
				style="overflow-x: auto; overflow-y: hidden">
				<table cellpadding="0" cellspacing="0" border="0"
					class="jquery_table" width="100%">
					<thead>
						<tr>
						  <th>No.</th>
						  <th><s:text name="ACT06_subCampName"/></th>
						  <th><s:text name="global.page.unitCode"/></th>
						  <th><s:text name="global.page.barCode"/></th>
						  <th><s:text name="global.page.nameTotal"/></th>
						  <th><s:text name="global.page.price"/></th>
						  <th><s:text name="global.page.exPoint"/></th>
						  <th><s:text name="global.page.quantity"/></th>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="prtList" status="status"><s:if test=""></s:if>
					    <s:if test='"DHMY".equals(prmCate)||"DHCP".equals(prmCate)||"TZZK".equals(prmCate)'><tr class="gray"></s:if>
					    <s:else><tr></s:else>
					    <td class="center" id="dataTd0"><s:property value="#status.index+1"/></td>
				        <%--活动名称--%>
				        <td>[<s:property value="subCampCode"/>]<s:property value="subCampName"/></td>
						<%--礼品编码--%>
				        <td><s:property value="unitCode"/></td>
				         <%--礼品条码 --%>
				         <td><s:property value="barCode"/></td>
				          <%--礼品名称--%>
				        <td><s:property value="nameTotal"/></td>
				        <%--金额 --%>
				         <td><s:text name="format.price"><s:param value="amout"></s:param></s:text></td>
				         <%-- 积分 --%>
				         <td><s:text name="format.number"><s:param value="pointRequired"></s:param></s:text></td>
				         <%--数量 --%>
				         <td><s:text name="format.number"><s:param value="quantity"></s:param></s:text></td>
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
					<td class="center"><s:text name="ACT06_quantity" /></td>
					<%-- 总数量 --%>
					<td class="center"><s:text name="ACT06_amout" /></td>
					<%-- 总金额--%>
					<td class="center"><s:text name="ACT06_pointRequired" /></td>
					<%-- 总金额--%>
				</tr>
				<tr>
					<td class="center"><s:if
							test="campDetailMap.quantity >= 0">
							<s:text name="format.number">
								<s:param value="campDetailMap.quantity"></s:param>
							</s:text>
						</s:if> <s:else>
							<span class="highlight"><s:text name="format.number">
									<s:param value="campDetailMap.quantity"></s:param>
								</s:text></span>
						</s:else></td>
					<td class="center">
						<s:if test='null!=campDetailMap.amout'>
							<s:if test="campDetailMap.amout>= 0">
									<s:text name="format.price"><s:param value="campDetailMap.amout"></s:param></s:text>
							</s:if> 
							<s:else>
								<span class="highlight">
									<s:text name="format.price"><s:param value="campDetailMap.amout"></s:param></s:text>
								</span>
							</s:else>
						</s:if>
						<s:else>0.00</s:else>
						</td>
					<td class="center">
					<s:if test='null!=campDetailMap.pointRequired'>
						<s:if test="campDetailMap.pointRequired>= 0">
							<s:text name="format.number"><s:param value="campDetailMap.pointRequired"></s:param></s:text>
						</s:if> 
						<s:else>
							<span class="highlight"><s:text name="format.price">
								<s:param value="campDetailMap.pointRequired"></s:param></s:text>
							</span>
						</s:else>
					</s:if>
					<s:else>0</s:else>
					</td>
				</tr>
			</table>
		</div>
	</div>
<div class="hide">
	<s:url id="updDetail_url" action="BINOLCPACT06_updDetail"/>
	<input type="hidden" id="updDetailUrl" value="${updDetail_url}" />
</div>
<span id="message_Error1" class="hide"><s:text name="ACT06_error1"/></span>
<span id="message_Error2" class="hide"><s:text name="ACT06_error2"/></span>
<span id="message_Error3" class="hide"><s:text name="ACT06_error3"/></span>
<span id="message_Error4" class="hide"><s:text name="ACT06_error4"/></span>
<span id="message_Error5" class="hide"><s:text name="ACT06_error5"/></span>
<span id="message_Error6" class="hide"><s:text name="ACT06_sendFlag0"/></span>
<span id="message_Error7" class="hide"><s:text name="ACT06_sendFlag1"/></span>
<span id="message_Error8" class="hide"><s:text name="ACT06_sendFlag2"/></span>
<span id="message_Error9" class="hide"><s:text name="ACT06_error6"/></span>
<span id="message_Error10" class="hide"><s:text name="ACT06_error7"/></span>
<span id="message_Error11" class="hide"><s:text name="ACT06_error8"/></span>
<span id="message_Error12" class="hide"><s:text name="ACT06_error9"/></span>
<span id="message_Error13" class="hide"><s:text name="ACT06_error10"/></span>
<span id="message_Error14" class="hide"><s:text name="ACT06_error11"/></span>
<span id="editButtonText" class="hide"><s:text name="global.page.edit"/></span>
</s:i18n>
<span id="cancleButtonText" class="hide"><s:text name="global.page.cancle"/></span>

