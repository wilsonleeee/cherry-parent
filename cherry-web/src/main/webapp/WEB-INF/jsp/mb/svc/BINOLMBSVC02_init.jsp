<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/svc/BINOLMBSVC02_init.js"></script>
<s:i18n name="i18n.mb.BINOLMBSVC02">
<div class="main container clearfix">
<div class="panel ui-corner-all">
	<div class="hide">
	</div>
	<div class="hide" id="dialogInit"></div>
	<div class="panel-header">
		<div id="actionResultDisplay"></div>
		<div id="errorMessage"></div>
		<div class="clearfix">
			<span class="breadcrumb left"> <span
				class="ui-icon icon-breadcrumb"></span> <s:text
					name="SVC02_cardManager" /> &gt; <s:text name="SVC02_saleView" /></span>
		</div>
	</div>
	<div class="panel-content">
		<div class="box">
			<form id="saleForm" class="inline">
				<div class="box-header">
					<%--主题活动搜索条件--%>
					<span class="ui-icon icon-ttl-search"></span> <strong><s:text
							name="SVC02_saleSearch" /></strong>
				</div>
				<div class="box-content clearfix">
					<div style="width: 50%; height: 100px;" class="column">
						<input id="cardCode" class="hide" name="cardCode" value="${cardCode }" >
						<p class="clearfix">
							<%--部门ID--%>
							<label><s:text name="SVC02_counterCode" /></label>
							<span><input id="counterName" class="text" name="counterName" value=""></span>
						</p>
						<p class="clearfix" style="">
							<%--交易类型--%>
							<label><s:text name="SVC02_transactionType" /></label>
							<span> <s:select name="transactionType" Cssstyle="width:186px;"
									list='#application.CodeTable.getCodes("1340")'
									listKey="CodeKey" listValue="Value" headerKey=""
									headerValue="%{selectAll}" value="''" />
							</span>
						</p>
					</div>
					<div style="width: 49%; height: 55px;" class="column last">
						<p class="date">
							<label><s:text name="SVC02_transactionTime"/></label>
		               		<span><s:textfield name="fromDate" cssClass="date"></s:textfield></span> - 
		              		<span><s:textfield name="toDate" cssClass="date"></s:textfield></span>
	               		</p>
					</div>
				</div>
				<p class="clearfix">
					<%--查询--%>
					<button class="right search" id="searchButton" onclick="BINOLMBSVC02_init.searchList();return false;">
						<span class="ui-icon icon-search-big"></span><span
							class="button-text"><s:text name="global.page.search" /></span>
					</button>
				</p>
			</form>
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
			</div>
				<div class="section-content">
					<div class="toolbar clearfix">
	          			<a id="saleExportExcel" class="export" href="javascript:void(0);">
          	  			<span class="ui-icon icon-export"></span>
          	  			<span class="button-text"><s:text name="SVC02_saleExportExcel"/></span>
          				</a>
	          			<a id="saleExportCsv" class="export" href="javascript:void(0);">
	          	  		<span class="ui-icon icon-export"></span>
	          	  		<span class="button-text"><s:text name="SVC02_saleExportCSV"/></span>
	          			</a>
		  			<span id="headInfo"></span>
		  			</div>
					<table id="resultSaleDataTable" cellpadding="0" cellspacing="0"
						border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:text name="SVC02_number" /></th>
								<th><s:text name="SVC02_cardNo" /></th>
								<th><s:text name="SVC02_cardType" /></th>
								<th><s:text name="SVC02_departName" /></th>
								<th><s:text name="SVC02_transactionTime" /></th>
								<th><s:text name="SVC02_billCode" /></th>
								<th><s:text name="SVC02_relevantCode" /></th>
								<th><s:text name="SVC02_transactionType" /></th>
								<th><s:text name="SVC02_transactionAmount" /></th>
								<th><s:text name="SVC02_giftAmount" /></th>
								<th><s:text name="SVC02_transactionTotalAmount" /></th>
								<th><s:text name="SVC02_discount" /></th>
								<th><s:text name="SVC02_validFlag" /></th>
								<th><s:text name="SVC02_operator" /></th>
							</tr>
						</thead>
					</table>
				</div>
		</div>
	</div>
	<div id="dialogConfirm" class="hide">
		<s:text name="SVC02_confirm" />
	</div>
	<div id="dialogCancel" class="hide">
		<s:text name="SVC02_cancel" />
	</div>
</div>
</div>
</s:i18n>
<div class="hide">
	<s:a action="BINOLMBSVC02_saleSearch" id="saleSearchUrl"></s:a>
	<s:a action="BINOLMBSVC02_saleExportExcel" id="saleExportExcelUrl"></s:a>
	<s:a action="BINOLMBSVC02_saleExportCsv" id="saleExportCsvUrl"></s:a>
	<s:a action="BINOLMBSVC02_saleExportCheck" id="saleExportCheckUrl"></s:a>
	<s:a action="BINOLMBSVC02_viewSaleDetailInit" id="viewSaleDetailInitUrl"></s:a>
</div>
<script type="text/javascript">
$("#fromDate").cherryDate({
	beforeShow: function(input){
		var value = $(input).parents("p").find("input[name='toDate']").val();
		return [value, "maxDate"];
	}
});
$("#toDate").cherryDate({
	beforeShow: function(input){
		var value = $(input).parents("p").find("input[name='fromDate']").val();
		return [value, "minDate"];
	}
});
cherryValidate({
	formId: "saleForm",		
	rules: {
		fromDate: {dateValid: true},
		toDate: {dateValid: true}
   }		
});
</script>
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<%-- ================== dataTable共通导入    END  ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp"
		flush="true" />
