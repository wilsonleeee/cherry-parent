<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/sam/BINOLBSSAM02.js"></script>
<s:i18n name="i18n.bs.BINOLBSSAM02">
	<div class="hide">
	</div>
	<div class="panel-header">
		<div id="actionResultDisplay"></div>
		<div id="errorMessage"></div>
		<div class="clearfix">
			<span class="breadcrumb left"> <span
				class="ui-icon icon-breadcrumb"></span> <s:text
					name="SAM02_samTitle" /> &gt; <s:text name="SAM02_overtimeSearch" /></span>
		</div>
	</div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="overTimeForm" class="inline">
				<div class="box-header">
					<%--主题活动搜索条件--%>
					<span class="ui-icon icon-ttl-search"></span> <strong><s:text
							name="SAM02_overtimeSearch" /></strong>
				</div>
				<div class="box-content clearfix">
					<div style="width: 50%; height: 55px;" class="column">
						<p class="clearfix">
							<%--审批状态--%>
							<label style="width: 80px;"><s:text name="SAM02_auditedState" /></label>
							<span> <s:select name="auditedState" Cssstyle="width:186px;"
									list='#application.CodeTable.getCodes("1359")'
									listKey="CodeKey" listValue="Value" headerKey=""
									headerValue="%{selectAll}" value="''" />
							</span>
						</p>
					</div>
					<div style="width: 49%; height: 55px;" class="column last">
						<p class="date">
							<label><s:text name="SAM02_overtimeDate"/></label>
		               		<span><s:textfield name="fromDate" cssClass="date"></s:textfield></span> - 
		              		<span><s:textfield name="toDate" cssClass="date"></s:textfield></span>
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
					<table id="resultOverTimeDataTable" cellpadding="0" cellspacing="0"
						border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:text name="SAM02_number" /></th>
								<th><s:text name="SAM02_applyDepart" /></th>
								<th><s:text name="SAM02_workDepart" /></th>
								<th><s:text name="SAM02_applyEmployee" /></th>
								<th><s:text name="SAM02_auditedState" /></th>
								<th><s:text name="SAM02_overtimeDate" /></th>
								<th><s:text name="SAM02_overtimeBegin" /></th>
								<th><s:text name="SAM02_overtimeEnd" /></th>
								<th><s:text name="SAM02_overtimeHours" /></th>
								<th><s:text name="SAM02_overtimeMemo" /></th>
								<th><s:text name="SAM02_applyTime" /></th>
								<th><s:text name="SAM02_auditedEmployee" /></th>
								<th><s:text name="SAM02_auditedTime" /></th>
								<th><s:text name="SAM02_auditedMemo" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
</s:i18n>
<div class="hide">
	<s:a action="BINOLBSSAM02_search" id="searchUrl"></s:a>
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<%-- ================== dataTable共通导入    END  ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp"
		flush="true" />
</div>