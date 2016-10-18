<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS39.js"></script>
<div class="hide"><s:text name="global.page.select" id="rps39_selectAll" /> </div>
<s:i18n name="i18n.pt.BINOLPTRPS39">
	<div class="panel-content">
		<div class="box">
			<cherry:form id="mainForm" class="inline" onsubmit="search(); return false;">
				<div class="box-content clearfix">
					<%-- 总部或者办事处用户登录 --%>
					<div class="column" style="width: 50%;">
						<input class="hide" id="reminderType" name="reminderType" value="0" />
						<input class="hide" id="cargoType" name="cargoType" value="${cargoType}" />
						<p>
							<%-- 催单单据号 --%>
							<label><s:text name="rps39_reminderNo" /></label>
							<s:textfield name="reminderNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;" />
						</p>
						<p>
							<%-- 延迟天数 --%>
							<label><s:text name="rps39_delayDate" /></label>
							<s:textfield name="delayDate" cssClass="text" maxlength="100" onblur="ignoreCondition(this);return false;" />
							<s:text name="global.page.birthDayUnit2" />
						</p>
						<%-- 收货日期 --%>
						<p id="dateCover" class="date">
							<label><s:text name="rps39_deliverDate" /></label>
							<span><s:textfield name="startDate" cssClass="date" /></span>
							 - 
							<span><s:textfield name="endDate" cssClass="date" /></span>
						</p>
					</div>
					<div class="column last" style="width: 49%;">
						<!-- 收货延迟查询条件 -->
						<s:if test='cargoType == "N"'>
							<p>
								<%-- 审核状态 --%>
								<label><s:text name="rps39_verifiedFlag" /></label>
								<s:select name="verifiedFlag" list='#application.CodeTable.getCodes("1180")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{rps39_selectAll}" />
							</p>
							<%--处理状态--%>
							<p>
								<label><s:text name="rps39_tradeStatus" /></label>
								<s:select name="tradeStatus" list='#application.CodeTable.getCodes("1141")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{rps39_selectAll}" />
							</p>
						</s:if>
						<s:elseif test='cargoType == "P"'>
							<p>
								<label><s:text name="rps39_verifiedFlag"/></label>
		                 		<s:select name="verifiedFlag" list='#application.CodeTable.getCodes("1007")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{rps39_selectAll}"/>
	                 		</p>
	                 		<p>
		                  		<label><s:text name="rps39_tradeStatus"/></label>
		                 		<s:select name="stockInFlag" list='#application.CodeTable.getCodes("1017")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{rps39_selectAll}"/>
		                 	</p>
						</s:elseif>
						<p>
							<label><s:text name="rps39_depType" /></label>
							<select id="depFlag" name="depFlag">
								<option value="inOrgan"><s:text name="rps39_receiveOrg"/></option>
								<option value="outOrgan"><s:text name="rps39_deliverOrg"/></option>
							</select>
						</p>
					</div>
					
					<%-- ======================= 组织联动共通导入开始  ============================= --%>
					<s:if test='cargoType == "N"'>
						<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
							<s:param name="showLgcDepot">1</s:param>
							<s:param name="businessType">1</s:param>
							<s:param name="operationType">1</s:param>
							<s:param name="mode">dpat,area,chan,dpot</s:param>
						</s:action>
					</s:if>
					<s:elseif test='cargoType == "P"'>
						<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
			                <s:param name="businessType">1</s:param>
			                <s:param name="operationType">1</s:param>
		                </s:action>
					</s:elseif>
					<%-- ======================= 组织联动共通导入结束  ============================= --%>
				</div>
				<s:url id="rps39search_url" value="/pt/BINOLPTRPS39_search" />
				<a id="searchUrl" href="${rps39search_url }"></a>
				<p class="clearfix">
					<button class="right search" type="submit" onclick="BINOLPTRPS39.search('0');return false">
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
					<%-- EXCEL导出URL --%>
					<span class="left">
						<!-- 产品 -->
						<s:if test='"N".equals(cargoType)'>
							<cherry:show domId="BINOLPTRPS39EX0">
								<s:url id="rpt39_excelUrl" value="/pt/BINOLPTRPS39_export" />
								<a id="export" class="export"
									onclick="BINOLPTRPS39.exportExcel('${rpt39_excelUrl}');return false;">
									<span class="ui-icon icon-export"></span>
									<span class="button-text">
										<s:text name="global.page.exportExcel" />
									</span>
								</a>
							</cherry:show>
						</s:if>
						<!-- 促销品 -->
						<s:elseif test='"P".equals(cargoType)'>
							<cherry:show domId="BINOLSSPRM70EX0">
								<s:url id="rpt39_excelUrl" value="/pt/BINOLPTRPS39_export" />
								<a id="export" class="export"
									onclick="BINOLPTRPS39.exportExcel('${rpt39_excelUrl}');return false;">
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
						<a href="#" class="setting">
							<span class="ui-icon icon-setting"></span>
							<span class="button-text">
								<s:text name="rps39_colSetting" />
							</span>
						</a>
					</span>
				</div>
				<table id="dataTable00" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
						<tr>
							<th><s:text name="rps39_deliverNo" /></th>
							<%-- 发单单号--%>
							<th><s:text name="rps39_deliverNoIF" /></th>
							<!-- 接口单号 -->
							<th><s:text name="rps39_relevanceNo" /></th>
							<!-- 关联单号  -->
							<th><s:text name="rps39_deliverOrg" /></th>
							<%-- 发货部门--%>
							<th><s:text name="rps39_receiverOrg" /></th>
							<%-- 收货部门--%>
							<th><s:text name="rps39_totalQuantity" /></th>
							<%-- 总数量--%>
							<th><s:text name="rps39_totalAmount" /></th>
							<%-- 总金额--%>
							<th><s:text name="rps39_date" /></th>
							<%-- 发货日期--%>
							<th><s:text name="rps39_delayDate" /></th>
							<%-- 发货日期--%>
							<th><s:text name="rps39_verifiedFlag" /></th>
							<%-- 审核状态--%>
							<th><s:text name="rps39_tradeStatus" /></th>
							<%-- 处理状态--%>
							<th><s:text name="rps39_employeeName" /></th>
							<!-- 发货员工 -->
							<th><s:text name="rps39_auditEmpName" /></th>
							<!-- 审核者 -- -->
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
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<script type="text/javascript">
// 节日
var holidays = '${holidays }';
$('#startDate').cherryDate({
	holidayObj: holidays,
	beforeShow: function(input){
		var value = $('#endDate').val();
		return [value,'maxDate'];
	}
});

$('#endDate').cherryDate({
	holidayObj: holidays,
	beforeShow: function(input){
		var value = $('#startDate').val();
		return [value,'minDate'];
	}
});

</script>
<input type="hidden" id="defStartDate" value='' />
<input type="hidden" id="defEndDate" value='' />