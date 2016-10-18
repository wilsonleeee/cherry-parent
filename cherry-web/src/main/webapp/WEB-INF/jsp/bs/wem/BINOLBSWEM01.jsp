<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/wem/BINOLBSWEM01.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<s:i18n name="i18n.bs.BINOLBSWEM01">
	<%-- 查询URL --%>
	<s:url id="search_url" action="BINOLBSWEM01_search" />
	<s:hidden name="search_url" value="%{search_url}" />
	<s:url id="assignSearch_url" action="BINOLBSWEM01_assignSearch" />
	<s:hidden name="assignSearch_url" value="%{assignSearch_url}" />
	<s:url id="assign_url" action="BINOLBSWEM01_assign" />
	<s:hidden name="assign_url" value="%{assign_url}" />
	<s:text name="global.page.select" id="select_default" />
	
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left">
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
		</div>
	</div>
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="errorMessage"></div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
			<ul>
				<li><span><s:text name="EBS00017" /></span></li>
			</ul>
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div id="actionResultDisplay"></div>
	<div class="panel-content">
		<cherry:form id="mainForm">
			<input id="parentCsrftoken" name="csrftoken" value="${csrftoken }" class="hide"/>
			<div class="box">
				<div class="box-header">
					<strong> <span class="ui-icon icon-ttl-search"></span> <%-- 查询条件 --%>
						<s:text name="WEM01_condition" />
					</strong>
				</div>
				<div class="box-content clearfix">
					<div class="column" style="width: 50%;">
						<p>
							<label> <s:text name="WEM01_billCode" />
							</label> <input name="billCode" class="text">
						</p>
						<p>
							<label><s:text name="WEM01_applyName" /></label>
							<input name="applyName" class="text">
						</p>
						<p>
							<label><s:text name="global.page.memPhone" /></label>
							<input name="applyMobile" class="text">
						</p>
					</div>
					<div class="column last" style="width: 49%;">
						<p>
							<label><s:text name="WEM01_applyType" /></label>
							 <s:select name="applyType" list='#application.CodeTable.getCodes("1332")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}" />
						</p>
						<p>
							<label><s:text name="WEM01_status" /></label>
							<s:select list='#application.CodeTable.getCodes("1333")' listKey="CodeKey" listValue="Value" name="status" headerKey="" headerValue="%{#select_default}"></s:select>
						</p>
						
						<p id="dataCover" class="date">
							<label><s:text name="WEM01_applyTime" /></label>
							<span><s:textfield id="startDate" name="startDate" cssClass="date" /> - 
							<s:textfield id="endDate" name="endDate" cssClass="date" /></span>
						</p>
					</div>
				</div>
				<p class="clearfix">
					<%-- 查询 --%>
					<button class="right search" onclick="BINOLBSWEM01.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<span class="button-text"><s:text name="WEM01_search" /></span>
					</button>
				</p>
			</div>
		</cherry:form>

		<%-- 查询结果一览 --%>
		<div class="section hide" id="section">
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="WEM01_results_list" />
				</strong>
			</div>
			<div class="section-content" id="result_list">
				<div class="toolbar clearfix">
					<span class="right">
						<a class="setting">
							<span class="ui-icon icon-setting"></span>
							<span class="button-text">
								<s:text name="WEM01_colSetting" />
							</span>
						</a>
					</span>
				</div>
				<div id="resultList">
					<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:text name="No" /></th>
								<th><s:text name="WEM01_billCode" /></th>
								<th><s:text name="WEM01_applyType" /></th>
								<th><s:text name="WEM01_applyTime" /></th>
								<th><s:text name="WEM01_applyLevel" /></th>
								<th><s:text name="WEM01_applyName" /></th>
								<th><s:text name="WEM01_applyMobile" /></th>
								<th><s:text name="WEM01_applyOpenID" /></th>
								<th><s:text name="WEM01_applyProvince" /></th>
								<th><s:text name="WEM01_applyCity" /></th>
								<th><s:text name="WEM01_applyDesc" /></th>
								<th><s:text name="WEM01_superMobile" /></th>
								<th><s:text name="WEM01_oldSuperMobile" /></th>
								<th><s:text name="WEM01_assigner" /></th>
								<th><s:text name="WEM01_assignTime" /></th>
								<th><s:text name="WEM01_auditor" /></th>
								<th><s:text name="WEM01_auditLevel" /></th>
								<th><s:text name="WEM01_reason" /></th>
								<th><s:text name="WEM01_auditTime" /></th>
								<th><s:text name="WEM01_status" /></th>
								<th><s:text name="WEM01_validFlag" /></th>
								<th><s:text name="os.navigation.operator" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="hide" id="dialogInit"></div>
	<div style="display: none;">
		<div id="dialogConfirm">
			<s:text name="global.page.ok" />
		</div>
		<div id="dialogCancel">
			<s:text name="global.page.cancle" />
		</div>
		<div id="dialogClose">
			<s:text name="global.page.close" />
		</div>
		<div id="assignTitle"><s:text name="WEM01_assignSuper"/></div>
		<div id="assignConfirm"><s:text name="WEM01_assign"/></div>
	</div>
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
	<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>
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
