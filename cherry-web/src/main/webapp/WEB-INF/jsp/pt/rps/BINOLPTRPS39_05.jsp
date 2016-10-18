<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS39.js"></script>
<s:i18n name="i18n.pt.BINOLPTRPS39">
	<div class="hide"><s:text name="global.page.select" id="select_default" /></div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="mainForm" class="inline"
				onsubmit="search(); return false;">
				<div class="box-content clearfix">
					<div class="column" style="width: 50%;">
						<input class="hide" id="reminderType" name="reminderType" value="1" />
						<input class="hide" id="cargoType" name="cargoType" value="${cargoType}" />
						<p>
							<%-- 催单单据号 --%>
							<label><s:text name="rps39_reminderNo" /></label>
							<s:textfield name="reminderNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;" />
						</p>
						<%-- <p>
							延迟天数
							<label><s:text name="rps39_delayDate" /></label>
							<s:textfield name="delayDate" cssClass="text" maxlength="100" onblur="ignoreCondition(this);return false;" />
							<s:text name="global.page.birthDayUnit2" />
						</p> --%>
						<%-- 收货日期 --%>
						<p id="dateCover" class="date">
							<label><s:text name="rps39_receiveDate" /></label>
							<span><s:textfield name="startDate" cssClass="date" /></span>
							 - 
							<span><s:textfield name="endDate" cssClass="date" /></span>
						</p>
					</div>
					<div class="column last" style="width: 49%;">
						<!-- 反向催单查询条件 -->
						<p>
							<label><s:text name="rps39_remindStatus" /></label>
							<select id="reminderStatus" name="status">
								<option value="">${select_default }</option>
								<option value="0"><s:text name="rps39_unhandle"/></option>
								<option value="1"><s:text name="rps39_handled"/></option>
							</select>
						</p>
						<%--催单次数 --%>
						<p>
							<label><s:text name="rps39_reminderCount" /></label>
							<select id="reminderCount" name="reminderCount">
								<option value="">${select_default }</option>
								<option value="1"><s:text name="rps39_once"/></option>
								<option value="2"><s:text name="rps39_twice"/></option>
							</select>
						</p>
					</div>
					<%-- ======================= 组织联动共通导入开始  ============================= --%>
					<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
						<s:param name="showLgcDepot">1</s:param>
						<s:param name="businessType">1</s:param>
						<s:param name="operationType">1</s:param>
						<s:param name="mode">dpat,area,chan,dpot</s:param>
					</s:action>
					<%-- ======================= 组织联动共通导入结束  ============================= --%>
				</div>
				<p class="clearfix">
					<s:url id="rps39search_url" value="/pt/BINOLPTRPS39_search" />
					<a id="searchUrl" href="${rps39search_url }"></a>
					<button class="right search" type="submit" onclick="BINOLPTRPS39.search('1');return false">
						<span class="ui-icon icon-search-big"></span>
						<%-- 查询 --%>
						<span class="button-text"><s:text name="rps39_search" /></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="section" class="section hide">
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="global.page.list" />
				</strong>
			</div>
			<div class="section-content">
				<div class="toolbar clearfix">
					<span class="left">
						<%-- EXCEL导出URL --%>
						<s:if test='"N".equals(cargoType)'>
							<cherry:show domId="BINOLPTRPS39EX1">
								<s:url id="rpt39_excelUrl" value="/pt/BINOLPTRPS39_export" />
								<a id="export" class="export" onclick="BINOLPTRPS39.exportExcel('${rpt39_excelUrl}');return false;">
									<span class="ui-icon icon-export"></span>
									<span class="button-text">
										<s:text name="global.page.exportExcel" />
									</span>
								</a>
							</cherry:show>
						</s:if>
						<s:elseif test='"P".equals(cargoType)'>
							<cherry:show domId="BINOLSSPRM70EX1">
								<s:url id="rpt39_excelUrl" value="/pt/BINOLPTRPS39_export" />
								<a id="export" class="export" onclick="BINOLPTRPS39.exportExcel('${rpt39_excelUrl}');return false;">
									<span class="ui-icon icon-export"></span>
									<span class="button-text">
										<s:text name="global.page.exportExcel" />
									</span>
								</a>
							</cherry:show>
						</s:elseif>
					</span>
					<%-- 设置列 --%>
					<span id="colSettingDiv" class="right"> 
						<a href="#" class="setting"> <span class="ui-icon icon-setting"></span>
						<span class="button-text">
							<s:text name="rps39_colSetting" />
						</span>
					</a>
					</span>
				</div>
				<table id="dataTable01" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
						<tr>
							<%-- 单据号 --%>
							<th><s:text name="rps39_reminderNo" /></th>
							<%-- 发货单号 --%>
							<th><s:text name="rps39_deliverNo" /></th>
							<%-- 收货柜台 --%>
							<th><s:text name="rps39_receiveCounter" /></th>
							<%-- 货物类型  --%>
							<th><s:text name="rps39_cargoType" /></th>
							<%-- 收货日期  --%>
							<th><s:text name="rps39_receiveDate" /></th>
							<%-- 延迟天数 --%>
							<th><s:text name="rps39_delayDate" /></th>
							<%-- 数量 --%>
							<th><s:text name="rps39_amount" /></th>
							<%-- BA --%>
							<th>BA</th>
							<%-- 邮箱 --%>
							<th><s:text name="rps39_BASEmail" /></th>
							<%-- 电话 --%>
							<th><s:text name="rps39_BASTel" /></th>
							<%-- 货运单号 --%>
							<th><s:text name="rps39_expressID" /></th>
							<%-- 第一次催单时间 --%>
							<th><s:text name="rps39_fstRemindTime" /></th>
							<%-- 第二次催单时间 --%>
							<th><s:text name="rps39_sndRemindTime" /></th>
							<%-- 制单日期 --%>
							<th><s:text name="rps39_tradeDate" /></th>
							<%-- 备注 --%>
							<th><s:text name="rps39_comment" /></th>
							<%-- 催单 --%>
							<th><s:text name="rps39_remind" /></th>
							<%-- 催单状态 --%>
							<th><s:text name="rps39_receiptStatus" /></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<script type="text/javascript">
	// 节日
	var holidays = '${holidays }';
	$('#startDate').cherryDate({
		holidayObj : holidays,
		beforeShow : function(input) {
			var value = $('#endDate').val();
			return [value, 'maxDate'];
		}
	});

	$('#endDate').cherryDate({
		holidayObj : holidays,
		beforeShow : function(input) {
			var value = $('#startDate').val();
			return [value, 'minDate'];
		}
	});
</script>
<input type="hidden" id="defStartDate" value='' />
<input type="hidden" id="defEndDate" value='' />
