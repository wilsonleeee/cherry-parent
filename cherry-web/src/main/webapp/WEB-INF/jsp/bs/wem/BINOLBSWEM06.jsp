<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/wem/BINOLBSWEM06.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<s:i18n name="i18n.bs.BINOLBSWEM06">
	<%-- 查询URL --%>
	<s:url id="saleSearch_url" action="BINOLBSWEM06_search" />
	<s:hidden name="saleSearch_url" value="%{saleSearch_url}" />
	<s:url id="profitRebateReset_url" action="BINOLBSWEM06_profitRebateReset" />
	<s:hidden name="profitRebateReset_url" value="%{profitRebateReset_url}" />
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left">
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
		</div>
	</div>
<div style="display: none;">
	<div id="title"><s:text name="WEM06_Title" /></div>
	<div id="text"><p class="message"><span><s:text name="WEM06_Text" /></span></p></div>
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="showError"><s:text name="global.page.operateFaild" /></div>
</div> 
<div class="hide" id="dialogInit"></div>    
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="actionResultDisplay"></div>
	<div id="errorMessage"></div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
			<ul>
				<li><span><s:text name="WEM06_NER" /></span></li>
			</ul>
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
		<cherry:form id="mainForm1">
			<div class="box">
				<div class="box-header">
					<strong> <span class="ui-icon icon-ttl-search"></span> <%-- 查询条件 --%>
						<s:text name="WEM06_condition" />
					</strong>
				</div>
				<div class="box-content clearfix">
					<div class="column" style="width: 50%;">
						<p>
							<label> <s:text name="WEM06_SaleEmployeeCode" />
							</label> <input name="employeeCode" class="text">
						</p>
						<p>
							<label> <s:text name="WEM06_SaleEmployeeName" />
							</label> <input name="employeeName" class="text">
						</p>
						<p>
							<label> <s:text name="WEM06_SaleType" />
							</label> <input name="saleType" class="text">
						</p>
						<p>
							<label> <s:text name="WEM06_RebateFlag" /></label> 
							<select name="rebateFlag">
								<option value=""></option>
								<option value="1"><s:text name="WEM06_RebateFlag1" /></option>
								<option value="0"><s:text name="WEM06_RebateFlag0" /></option>
							</select>
						</p>
					</div>
					<div class="column last" style="width: 49%;">
						<p>
							<label> <s:text name="WEM06_Channel" />
							</label> <input name="channel" class="text">
						</p>
						<p>
							<label> <s:text name="WEM06_BillCode" />
							</label> <input name="billCode" class="text">
						</p>
						<p id="dataCover" class="date">
							<label><s:text name="WEM06_SaleTime" /></label>
							<span><s:textfield id="startDate" name="startDate" cssClass="date" /> - 
							<s:textfield id="endDate" name="endDate" cssClass="date" /></span>
						</p>
					</div>
				</div>
				<p class="clearfix">
					<%-- 查询 --%>
					<button class="right search" onclick="BINOLBSWEM06.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<span class="button-text"><s:text name="WEM06_search" /></span>
					</button>
				</p>
			</div>
		</cherry:form>
		<%-- 查询结果一览 --%>
		<div class="section hide" id="section">
			<div class="section-header">
				<strong> 
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="WEM06_results_list" />
				</strong>
			</div>
			<div class="section-content" id="result_list">
				<div class="toolbar clearfix">
					<span class="left">
		           	   <cherry:show domId="BINOLBSWEM06">
		           	    <a id="rebate" class="export" onclick="BINOLBSWEM06.profitRebateReset('${profitRebateReset_url}');return false;">
		                   <span class="ui-icon icon-export"></span>
		                   <span class="button-text"><s:text name="WEM06_Rebate"/></span>
		                </a>
		                 </cherry:show>
           			</span>
           			
					<span class="right">
						<a class="setting">
							<span class="ui-icon icon-setting"></span>
							<span class="button-text">
								<s:text name="WEM06_colSetting" />
							</span>
						</a>
					</span>
				</div>
				<div id="resultList">
					<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:checkbox name="allSelect" id="allSelect" onclick="BINOLBSWEM06.checkSelectAll(this)"/></th>
								<th><s:text name="WEM06_SaleRecordCode" /></th>
								<th><s:text name="WEM06_BillCodePre" /></th>
								<th><s:text name="WEM06_BillCode" /></th>
								<th><s:text name="WEM06_SaleType" /></th>
								<th><s:text name="WEM06_SaleTime" /></th>
								<th><s:text name="WEM06_SaleEmployeeCode" /></th>
								<th><s:text name="WEM06_SaleCount" /></th>
								<th><s:text name="WEM06_Amount" /></th>
								<th><s:text name="WEM06_SaleEmployeeName" /></th>
								<th><s:text name="WEM06_SaleProfit" /></th>
								<th><s:text name="WEM06_Channel" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
</s:i18n>
<script type="text/javascript">
    $('#startDate').cherryDate({
        beforeShow: function(input){
            var value = $('#endDate').val();
            return [value,'maxDate'];
        }
    });
    $('#endDate').cherryDate({
        beforeShow: function(input){
            var value = $('#startDate').val();
            return [value,'minDate'];
        }
    });
</script>