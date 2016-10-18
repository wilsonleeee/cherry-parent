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
					<th></th>
					<td></td>
				</tr>
				<tr>
					<!-- 会员类型 -->
					<th><s:text name="ACT06_testType" /></th>
					<td><span><s:property value='#application.CodeTable.getVal("1256", campDetailMap.testType)' /></span></td>
					<%-- 领用柜台--%>
					<th><s:text name="ACT06_counterGot" /></th>
					<td>
					  <span>
					      <span style="line-height: 18px; margin-right: 10px;"><s:property value="%{campDetailMap.counterGot}"/></span>		      
					      <s:textfield name="counterCode" value="%{campDetailMap.counterGot}"   cssClass="text" maxlength="20" cssStyle="display:none;width:120px;"></s:textfield>
					      <a class="delete" id="editCountId" onclick="BINOLCPACT06.editCounter(this);"><span class="ui-icon icon-edit"></span><span class="button-text right" id="editCounterGotButton"><s:text name="global.page.edit"></s:text></span></a>
				      </span>
				     </td>
				</tr>
				
				<tr>
					<%-- 单据状态 --%>
					<th><s:text name="ACT06_state" /></th>
					<td><span id="campStateId"><s:property value="#application.CodeTable.getVal('1116',campDetailMap.state)"/></span></td>
					<!-- 领用开始 -->
					<th><s:text name="ACT06_getFromTime" /></th>
					<%-- <td><span><s:property value='campDetailMap.getFromTime' /></span></td>--%>
					<td>
					  <span>
					      <span style="line-height: 18px; margin-right: 10px;"><s:property value="%{campDetailMap.getFromTime}"/></span>	     
					      <s:textfield name="fromTime" value="%{campDetailMap.getFromTime}"   cssClass="date" maxlength="20" cssStyle="display:none;width:120px;"></s:textfield>
					      <a class="delete" id="editFromTimeId" onclick="BINOLCPACT06.editFromDate(this);"><span class="ui-icon icon-edit"></span><span class="button-text right" id="editFromTimeButton"><s:text name="global.page.edit"></s:text></span></a>
				      </span>
				     </td>
				</tr>
				<tr>
					<!-- 下发区分 -->
					<th><s:text name="ACT06_sendFlag" /></th>
					<td> 
						<span id="sendFlagId">
						<s:if test ='"0".equals(campDetailMap.sendFlag)'><s:text name="ACT06_sendFlag0"/></s:if>
				        <s:if test ='"1".equals(campDetailMap.sendFlag)'><s:text name="ACT06_sendFlag1"/></s:if>
				        <s:if test ='"2".equals(campDetailMap.sendFlag)'><s:text name="ACT06_sendFlag2"/></s:if>
				        <s:if test ='"3".equals(campDetailMap.sendFlag)'><s:text name="ACT06_sendFlag3"/></s:if>
				        </span>
			        </td>
					<%-- 领用结束--%>
					<th><s:text name="ACT06_getToTime" /></th>
					<td>
					  <span>
					      <span style="line-height: 18px; margin-right: 10px;"><s:property value="%{campDetailMap.getToTime}"/></span>	     
					      <s:textfield name="endTime" value="%{campDetailMap.getToTime}"   cssClass="date" maxlength="20" cssStyle="display:none;width:120px;"></s:textfield>
					      <a class="delete" id="editTimeId" onclick="BINOLCPACT06.editEndDate(this);"><span class="ui-icon icon-edit"></span><span class="button-text right" id="editEndTimeButton"><s:text name="global.page.edit"></s:text></span></a>
				      </span>
				     </td>
				</tr>
				<tr>
					<th><s:text name="ACT06_bookDate" /></th>
					<td><span><s:property value="campDetailMap.bookDate"/></span></td>
					<th><s:text name="ACT06_bookTimeRange" /></th>
					<td><span><s:property value="campDetailMap.bookTimeRange"/></span></td>
				</tr>
				<tr>
					<th><s:text name="ACT06_finishTime" /></th>
					<td><span><s:property value="campDetailMap.finishTime"/></span></td>
					<th><s:text name="ACT06_cancelTime" /></th>
					<td><span><s:property value="campDetailMap.cancelTime"/></span></td>
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

