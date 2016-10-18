<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/st/sfh/BINOLSTSFH20.js"></script>
<%-- 后台销售单（Excel导出）URL --%>
<s:url action="BINOLSTSFH20_export" id="export_url"/>
<s:i18n name="i18n.st.BINOLSTSFH20">
	<form id="mainForm">
		<s:text name="global.page.all" id="select_default"/>
		<div class="panel ui-corner-all">
			<div id="div_main">
				<div class="panel-header">
					<div class="clearfix"> 
						<span class="breadcrumb left">	    
							<span class="ui-icon icon-breadcrumb"></span>
							<s:text name="SFH20_native1" />&nbsp;&gt;&nbsp;
							<s:text name="SFH20_native2" />
						</span>
					</div>
				</div>
				<div id="errorMessage"></div>
 				<div id="actionResultDisplay"></div>
 				<div class="panel-content clearfix">
 					<div class="box">
 						<input type="hidden" value="<s:property value="importBatchId"/>"  name="importBatchId" />
 						<div class="box-header"> 
 							<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
 						</div>
 						<div class="box-content clearfix">
 							<div class="column" style="width:50%; height: auto;">
 								<p>
						        	<label><s:text name="SFH20_billNo" /></label>
						            <span>
						            	<s:textfield name="billNo" cssClass="text" maxlength="40"></s:textfield>
									</span>
						        </p>
						        <p>
						          	<label class="left"><s:text name="SFH20_departSale" /></label>
			                        <table class="all_clean left"><tbody id="organization_ID"></tbody></table>
						            <a class="add" onclick="BINOLSTSFH20.popDepartBox(this,'in');">
			                            <span class="ui-icon icon-search"></span>
			                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
			                        </a>
						        </p>
						        <p>
			                        <label><s:text name="SFH20_customerType"/></label>
			                        <s:select id="customerType" name="customerType" list='#application.CodeTable.getCodes("1297")' 
			                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}" 
			                            onchange="BINOLSTSFH20.changeCustomerType();"/>
			                    </p>
			                    <p id="pCustomerName" class="hide">
			                        <label><s:text name="SFH20_departCustomer"/></label>
			                        <input type="hidden" name="customerOrganizationId" id="customerOrganizationId"></input>
			                        <input type="text" class="text" id="customerOrganization"></input>
			                    </p>
 							</div>
 							<div class="column last" style="width:49%; height: auto;">
 								<p>
						        	<label><s:text name="SFH20_importDate"/></label>
						            <span>
						            	<s:textfield id="importDateStart" name="importDateStart" cssClass="date"/>
						            </span> - 
						            <span>
						            	<s:textfield id="importDateEnd" name="importDateEnd" cssClass="date"/>
						            </span>
						        </p>
						        <p>
						        	<label><s:text name="SFH20_importResult"/></label>
						            <span>
						            	<s:select list='#application.CodeTable.getCodes("1250")' listKey="CodeKey" listValue="Value" id="importResult" name="importResult"  
										headerKey="" cssStyle="width:120px;" headerValue="%{#select_default}"></s:select>
						            	
						            </span>
						        </p>
						        <p>
						        	<!-- 单据状态 -->
						        	<label><s:text name="SFH20_billStatus"/></label>
						            <span>
						            	<s:select list='#application.CodeTable.getCodes("1294")' listKey="CodeKey" listValue="Value" id="billState" name="billState"  
										headerKey="" cssStyle="width:120px;" headerValue="%{#select_default}"></s:select>
						            	
						            </span>
						        </p>
 							</div>
 						</div>
 						<p class="clearfix">
	 						<button class="right search" onclick="BINOLSTSFH20.search();return false;">
					      		<span class="ui-icon icon-search-big"></span>
						      	<span class="button-text">
						      		<s:text name="global.page.search"></s:text>
					      		</span>
				      		</button>
 						</p>
 					</div>
 					<div class="section hide" id="backstageSaleExcelInfo">
 						<div class="section-header">
 							<strong>
							  	<span class="ui-icon icon-ttl-section-search-result"></span>
							  	<s:text name="global.page.list"></s:text>
						  	</strong>
 						</div>
	 					<div class="section-content">
	 						<div class="toolbar clearfix">
	 							<cherry:show domId="BINOLSTSFH19EXP">
									<a  class="export left" onclick="javascript:BINOLSTSFH20.exportExcel(this);return false;"  href="${export_url}">
										<span class="ui-icon icon-export"></span>
										<span class="button-text"><s:text name="SFH20_exportExcel_btn"></s:text></span>
									</a>
							  	</cherry:show>
							  	<span class="right">
						  			<a class="setting">
						  				<span class="ui-icon icon-setting"></span>
					              		<span class="button-text"><s:text name="global.page.colSetting"/></span>
				              		</a>
				              	</span>
	 						</div>
	 						<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="backstageSaleExcelDataTable">
	 							<thead>
							        <tr>
										<th><s:text name="NO."></s:text></th>
										<th><s:text name="SFH20_billNo"></s:text></th>
										<th><s:text name="SFH20_departSale"></s:text></th>
										<th><s:text name="SFH20_depotNameSale"></s:text></th>
										<th><s:text name="SFH20_logicInventoryNameSale"></s:text></th>
										<th><s:text name="SFH20_departCustomer"></s:text></th>
										<th><s:text name="SFH20_depotNameCustomer"></s:text></th>
										<th><s:text name="SFH20_logicInventoryNameCustomer"></s:text></th>
										<th><s:text name="SFH20_saleDate"></s:text></th>
										<th><s:text name="SFH20_saleBillType"></s:text></th>
										<th><s:text name="SFH20_customerType"></s:text></th>
										<th><s:text name="SFH20_settlement"></s:text></th>
										<th><s:text name="SFH20_currency"></s:text></th>
										<th><s:text name="SFH20_contactPerson"></s:text></th>
										<th><s:text name="SFH20_deliverAddress"></s:text></th>
										<th><s:text name="SFH20_totalQuantity"></s:text></th>
										<th><s:text name="SFH20_totalAmount" /></th>
										<th><s:text name="SFH20_importDate"></s:text></th>
										<th><s:text name="SFH20_importResult"></s:text></th>
										<th><s:text name="SFH20_billStatus"></s:text></th>
							        </tr>
								</thead>
								<tbody></tbody>
	 						</table>
	 					</div>
 					</div>
 				</div>
			</div>
		</div>
		<div class="hide">
			<s:url action="BINOLSTSFH20_search" id="searchUrl" />
			<a href="${searchUrl}" id="search_Url"></a>
			<div id="popuTitle"><s:text name="SFH20_popuTitle" /></div>
			<div id="popuOK"><s:text name="global.page.ok" /></div>
			<div id="processing"><span class="dataTables_processing"></span></div>
		</div>
	</form>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== 弹出后台销售部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通END ======================= --%>
<div id="detailData" class="hide"></div>
<script type="text/javascript">
	$(document).ready(function() {
		BINOLSTSFH20.search();
       	var holidays = '${holidays }';
       	$('#importDateStart').cherryDate({
        	holidayObj: holidays,
           	beforeShow: function(input){
            	var value = $('#importDateEnd').val();
               	return [value,'maxDate'];
           	}
       });
       $('#importDateEnd').cherryDate({
           	holidayObj: holidays,
           	beforeShow: function(input){
               	var value = $('#importDateStart').val();
               	return [value,'minDate'];
           	}
       });
	});
</script>