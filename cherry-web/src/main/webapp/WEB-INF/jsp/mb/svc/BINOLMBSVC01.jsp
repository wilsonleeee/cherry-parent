<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/mb/common/BINOLMBCOM01.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/mb/svc/BINOLMBSVC01.js?v=20160702"></script>
<script type="text/javascript">
	//节日
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

<s:i18n name="i18n.mb.BINOLMBSVC01">
	<div class="hide">
		<s:url id="s_addMainRuleInit" value="/mb/BINOLMBSVC01_addMainRuleInit" />
		<a id="addMainRuleInitUrl" href="${s_addMainRuleInit}"></a>
		<s:url id="s_stopRule" value="/mb/BINOLMBSVC01_stopRule" />
		<a id="stopRuleUrl" href="${s_stopRule}"></a>
	</div>
	<div class="panel-header">
		<div id="actionResultDisplay"></div>
		<div id="errorMessage"></div>
		<div class="clearfix">
			<span class="breadcrumb left"> <span
				class="ui-icon icon-breadcrumb"></span> <s:text
					name="SVC01_svcTitle" /> &gt; <s:text name="SVC01_ruleDetail" /></span>
			<span class="right"> <%-- 规则添加按钮 --%> 
					<a href="/Cherry/mb/BINOLMBSVC01_addRuleInit.action" class="add"
						onclick="javascript:openWin(this);return false;"> <span
						class="button-text"><s:text name="SVC01_ruleAdd" /></span> <span
						class="ui-icon icon-add"></span>
					</a>
			</span>
		</div>
	</div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="ruleForm" class="inline">
				<div class="box-header">
					<%--规则搜索条件--%>
					<span class="ui-icon icon-ttl-search"></span> <strong><s:text
							name="SVC01_ruleSearch" /></strong>
				</div>
				<div class="box-content clearfix">
					<div style="width: 50%; height: 100px;" class="column">
						<p class="clearfix">
							<%--优惠规则名称--%>
							<label style="width: 80px;"><s:text name="SVC01_subDiscountName"/></label>
							<span><input id="ruleName" class="text" name="ruleName"></span>
						</p>
						<p class="clearfix">
							<%--规则参与柜台--%>
							<label style="width: 80px;"><s:text name="SVC01_ruleCounter" /></label>
							<span>
							    <s:hidden name="organizationId"></s:hidden>
							    <span id="counterDiv" style="line-height: 18px;"></span>
							    <s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
							    <a class="add" onclick="binolmbcom01.popCounterList('${searchCounterInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
						    </span>
						</p>
					</div>
					<div style="width: 49%; height: 55px;" class="column last">
						<p class="date">
		                	<%--规则时间--%>
		                  	<label><s:text name="SVC01_ruleDate"/></label>
		                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
		                  	- 
		                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
		                </p>
					</div>
				</div>
				<p class="clearfix">
					<%--查询--%>
					<button class="right search" id="searchButton">
						<span class="ui-icon icon-search-big"></span><span
							class="button-text"><s:text name="global.page.search" /></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="resultList" class="hide">
			<div class="section">
				<div class="section-header">
					<strong> <span
						class="ui-icon icon-ttl-section-search-result"></span> <s:text
							name="global.page.list" />
					</strong>
					<span class="right">
						<a href="#" class="setting">
						<span class="button-text"><s:text name="global.page.colSetting"/></span>
		 				<span class="ui-icon icon-setting"></span>
						</a>
					</span>
				</div>
				<div class="section-content">
					<table id="resultRuleDataTable" cellpadding="0" cellspacing="0"
						border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:text name="SVC01_number" /></th>
								<th><s:text name="SVC01_subDiscountName" /></th>
								<th><s:text name="SVC01_discountBeginDate" /></th>
								<th><s:text name="SVC01_discountEndDate" /></th>
								<th><s:text name="SVC01_rechargeType" /></th>
								<th><s:text name="SVC01_applyCntCount" /></th><!-- 储值规则限定柜台数 -->
								<th><s:text name="SVC01_usedCntCount" /></th><!-- 发送储值业务柜台数 -->
								<th><s:text name="SVC01_involveNumber" /></th><!-- 参与储值人数 -->
								<th><s:text name="SVC01_rechargeValueActual" /></th>
								<th><s:text name="SVC01_giftAmount" /></th>
								<th><s:text name="SVC01_operator" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="dialog2 clearfix" style="display: none"
		id="stop_rule_dialog">
		<p class="clearfix">
		<p class="message">
			<span><s:text name="SVC01_stopRuleMessage" /></span>
		</p>
	</div>
	<div class="dialog2 clearfix" style="display: none"
		id="restart_rule_dialog">
		<p class="clearfix">
		<p class="message">
			<span><s:text name="SVC01_restartRuleMessage" /></span>
		</p>
	</div>
	<div id="restartDialogTitle" class="hide">
		<s:text name="SVC01_restartRuleTitle" />
	</div>
	<div id="dialogTitle" class="hide">
		<s:text name="SVC01_stopRuleTitle" />
	</div>
	<div id="dialogConfirm" class="hide">
		<s:text name="SVC01_confirm" />
	</div>
	<div id="dialogCancel" class="hide">
		<s:text name="SVC01_cancel" />
	</div>
</s:i18n>
<div class="hide">
<s:a action="BINOLMBSVC01_search" id="searchUrl"></s:a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />