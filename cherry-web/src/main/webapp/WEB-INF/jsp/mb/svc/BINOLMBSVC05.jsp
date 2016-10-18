<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/mb/svc/BINOLMBSVC05.js"></script>
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
<s:i18n name="i18n.mb.BINOLMBSVC05">
	<div class="hide">
		<s:url id="s_search" value="/mb/BINOLMBSVC05_search" />
		<a id="searchTradeUrl" href="${s_search}"></a>
	</div>
	<div id="dialogInit" class="hide">
	</div>
	<div class="panel-header">
		<div id="actionResultDisplay"></div>
		<div id="errorMessage"></div>
		<div class="clearfix">
			<span class="breadcrumb left"> <span
				class="ui-icon icon-breadcrumb"></span> <s:text
					name="SVC05_svcTitle" /> &gt; <s:text name="SVC05_svcTitle2" /></span>
		</div>
	</div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="cardTradeForm" class="inline">
				<div class="box-header">
					<%--主题活动搜索条件--%>
					<span class="ui-icon icon-ttl-search"></span> <strong><s:text name="SVC05_svcTitle2" /></strong>
				</div>
				<div class="box-content clearfix">
					<div style="width: 50%; height: 100px;" class="column">
						<p class="date">
		                	<%--业务时间--%>
		                  	<label style="width: 80px;"><s:text name="SVC05_tradeTime"/> </label>
		                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
		                  	- 
		                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
		                </p>
						<p class="clearfix" style="">
							<%--储值卡状态--%>
							<label style="width: 80px;"><s:text
									name="SVC05_cardState" /></label> <span> <s:select
									name="state" Cssstyle="width:186px;"
									list='#application.CodeTable.getCodes("1339")'
									listKey="CodeKey" listValue="Value" headerKey=""
									headerValue="%{selectAll}" value="''" />
							</span>
						</p>
					</div>
					<div style="width: 49%; height: 55px;" class="column last">
						<p class="clearfix" style="">
							<%--交易类型--%>
							<label style="width: 80px;"><s:text name="SVC05_tradeType"/> </label> <span> <s:select
									name="transactionType" Cssstyle="width:186px;"
									list='#application.CodeTable.getCodes("1340")'
									listKey="CodeKey" listValue="Value" headerKey=""
									headerValue="%{selectAll}" value="''" />
							</span>
						</p>
						<p class="clearfix">
							<%--储值卡卡号--%>
							<label style="width: 80px;"><s:text name="SVC05_cardNo"/> </label> <span><input
								id="mobilePhone" class="text" name="cardCode"></span>
						</p>
						<p class="clearfix">
							<%--预留手机号--%>
							<label style="width: 80px;"><s:text
									name="SVC05_obligateMobile" /></label> <span><input
								id="mobilePhone" class="text" name="mobilePhone"></span>
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
					<table id="resultCardDataTable" cellpadding="0" cellspacing="0"
						border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:text name="SVC05_number" /></th>
								<th><s:text name="SVC05_tradeNo" /></th>
								<th><s:text name="SVC05_tradeTime" /></th>
								<th><s:text name="SVC05_tradeType" /></th>
								<th><s:text name="SVC05_amount"></s:text></th>
								<th><s:text name="SVC05_cardNo"></s:text></th>
								<th><s:text name="SVC05_obligateMobile"></s:text></th>
								<th><s:text name="SVC05_memo"></s:text></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="dialog2 clearfix" style="display: none"
		id="stop_card_dialog">
		<p class="clearfix">
		<p class="message">
			<span><s:text name="SVC02_stopCardMessage" /></span>
		</p>
	</div>
	<div id="dialogTitle" class="hide">
		<s:text name="SVC02_stopCardTitle" />
	</div>
	<div id="dialogConfirm" class="hide">
		<s:text name="SVC02_confirm" />
	</div>
	<div id="dialogCancel" class="hide">
		<s:text name="SVC02_cancel" />
	</div>
</s:i18n>
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<%-- ================== dataTable共通导入    END  ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp"
		flush="true" />
