<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/sam/BINOLBSSAM03.js"></script>
<s:i18n name="i18n.bs.BINOLBSSAM03">
	<div class="hide">
	</div>
	<div class="panel-header">
		<div id="actionResultDisplay"></div>
		<div id="errorMessage"></div>
		<div class="clearfix">
			<span class="breadcrumb left"> <span
				class="ui-icon icon-breadcrumb"></span> <s:text
					name="SAM03_samTitle" /> &gt; <s:text name="SAM03_employeePayroll" /></span>
		</div>
	</div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="payrollForm" class="inline">
				<div class="box-header">
					<%--主题活动搜索条件--%>
					<span class="ui-icon icon-ttl-search"></span> <strong><s:text
							name="SAM03_payrollSearch" /></strong>
				</div>
				<div class="box-content clearfix">
					<div style="width: 50%; height: 55px;" class="column">
						<p class="clearfix">
							<label style="width: 80px;"><s:text name="SAM03_employeeName" /></label>
							<span><input id="employeeName" class="text" name="employeeName"></span>
						</p>
						<p class="clearfix">
							<label style="width: 80px;"><s:text name="SAM03_wagesYear" /></label>
							<span><input id="wagesYear" class="text" name="wagesYear"></span>
						</p>
					</div>
					<div style="width: 49%; height: 55px;" class="column last">
							<%--工资年份--%>
						<p class="clearfix">
							<label style="width: 80px;"><s:text name="SAM03_wagesMonth" /></label>
							<span><input id="wagesMonth" class="text" name="wagesMonth"></span>
						</p>
					</div>
				</div>
				<p class="clearfix">
					<%--查询--%>
					<button class="right search" id="searchButton" onclick="BINOLBSSAM03.searchList();return false;">
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
					<div class="toolbar clearfix">
		  				<a id="exportExcel" class="export" href="javascript:void(0);">
          	  			<span class="ui-icon icon-export"></span>
          	  			<span class="button-text"><s:text name="SAM03_ExportExcel"/></span>
          				</a>
	          			<a id="exportCsv" class="export" href="javascript:void(0);">
	          	  		<span class="ui-icon icon-export"></span>
	          	  		<span class="button-text"><s:text name="SAM03_ExportCSV"/></span>
	          			</a>
		  			<span id="headInfo"></span>
		  			</div>
					<table id="resultPayrollDataTable" cellpadding="0" cellspacing="0"
						border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:text name="SAM03_number" /></th>
								<th><s:text name="SAM03_organization" /></th>
								<th><s:text name="SAM03_employeeName" /></th>
								<th><s:text name="SAM03_workingHours" /></th>
								<th><s:text name="SAM03_workingDays" /></th>
								<th><s:text name="SAM03_usualOvertime" /></th>
								<th><s:text name="SAM03_specialOverTime" /></th>
								<th><s:text name="SAM03_saleTarget" /></th>
								<th><s:text name="SAM03_saleAmount" /></th>
								<th><s:text name="SAM03_bonusRate" /></th>
								<th><s:text name="SAM03_completionRate" /></th>
								<th><s:text name="SAM03_score" /></th>
								<th><s:text name="SAM03_wagesAmount" /></th>
								<th><s:text name="SAM03_memo" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
</s:i18n>
<div class="hide">
	<s:a action="BINOLBSSAM03_search" id="searchUrl"></s:a>
	<s:a action="BINOLBSSAM03_exportExcel" id="exportExcelUrl"></s:a>
	<s:a action="BINOLBSSAM03_exportCsv" id="exportCsvUrl"></s:a>
	<s:a action="BINOLBSSAM03_exportCheck" id="exportCheckUrl"></s:a>
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<%-- ================== dataTable共通导入    END  ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp"
		flush="true" />
</div>