<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH18.js"></script>
<%-- 订货单（Excel导出）URL --%>
<s:url action="BINOLSTSFH18_export" id="export_url"/>
<s:i18n name="i18n.st.BINOLSTSFH18">
	<form id="mainForm">
		<s:text name="global.page.all" id="select_default"/>
		<div class="panel ui-corner-all">
			<div id="div_main">
				<div class="panel-header">
					<div class="clearfix"> 
						<span class="breadcrumb left">	    
							<span class="ui-icon icon-breadcrumb"></span>
							<s:text name="SFH18_native1" />&nbsp;&gt;&nbsp;
							<s:text name="SFH18_native2" />
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
						        	<label style="width:80px;"><s:text name="SFH18_billNo" /></label>
						            <span>
						            	<s:textfield name="billNo" cssClass="text" maxlength="40"></s:textfield>
									</span>
						        </p>
						        <p>
						          	<label class="left" style="width:80px;"><s:text name="SFH18_departNameOrder" /></label>
			                        <table class="all_clean left"><tbody id="organization_ID"></tbody></table>
						            <a class="add" onclick="BINOLSTSFH18.popDepartBox(this,'in');">
			                            <span class="ui-icon icon-search"></span>
			                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
			                        </a>
						        </p>
						        <p>
						          	<label class="left" style="width:80px;"><s:text name="SFH18_departNameAccept" /></label>
						          	<table class="all_clean left"><tbody id="outOrganization_ID"></tbody></table>
						            <a class="add" onclick="BINOLSTSFH18.popDepartBox(this,'out');">
			                            <span class="ui-icon icon-search"></span>
			                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
			                        </a>
						        </p>
 							</div>
 							<div class="column last" style="width:49%; height: auto;">
 								<p>
						        	<label><s:text name="SFH18_importDate"/></label>
						            <span>
						            	<s:textfield id="importDateStart" name="importDateStart" cssClass="date"/>
						            </span> - 
						            <span>
						            	<s:textfield id="importDateEnd" name="importDateEnd" cssClass="date"/>
						            </span>
						        </p>
						        <p>
						        	<label><s:text name="SFH18_importResult"/></label>
						            <span>
						            	<s:select list='#application.CodeTable.getCodes("1250")' listKey="CodeKey" listValue="Value" id="importResult" name="importResult"  
										headerKey="" cssStyle="width:120px;" headerValue="%{#select_default}"></s:select>
						            	
						            </span>
						        </p>
						        <p>
						        	<!-- 处理状态 -->
						        	<label><s:text name="SFH18_tradeStatus"/></label>
						            <span>
						            	<s:select list='#application.CodeTable.getCodes("1142")' listKey="CodeKey" listValue="Value" id="tradeStatus" name="tradeStatus"  
										headerKey="" cssStyle="width:120px;" headerValue="%{#select_default}"></s:select>
						            	
						            </span>
						        </p>
 							</div>
 						</div>
 						<p class="clearfix">
	 						<button class="right search" onclick="BINOLSTSFH18.search();return false;">
					      		<span class="ui-icon icon-search-big"></span>
						      	<span class="button-text">
						      		<s:text name="global.page.search"></s:text>
					      		</span>
				      		</button>
 						</p>
 					</div>
 					<div class="section hide" id="prtOrderExcelInfo">
 						<div class="section-header">
 							<strong>
							  	<span class="ui-icon icon-ttl-section-search-result"></span>
							  	<s:text name="global.page.list"></s:text>
						  	</strong>
 						</div>
	 					<div class="section-content">
	 						<div class="toolbar clearfix">
	 							<cherry:show domId="BINOLSTSFH17EXP">
									<a  class="export left" onclick="javascript:BINOLSTSFH18.exportExcel(this);return false;"  href="${export_url}">
										<span class="ui-icon icon-export"></span>
										<span class="button-text"><s:text name="SFH18_exportExcel_btn"></s:text></span>
									</a>
							  	</cherry:show>
							  	<span class="right">
						  			<a class="setting">
						  				<span class="ui-icon icon-setting"></span>
					              		<span class="button-text"><s:text name="global.page.colSetting"/></span>
				              		</a>
				              	</span>
	 						</div>
	 						<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="prtOrderExcelInfoDataTable">
	 							<thead>
							        <tr>
										<th><s:text name="NO."></s:text></th>
										<th><s:text name="SFH18_billNo"></s:text></th>
										<th><s:text name="SFH18_departNameOrder"></s:text></th>
										<th><s:text name="SFH18_inventoryName"></s:text></th>
										<th><s:text name="SFH18_logicInventoryName"></s:text></th>
										<th><s:text name="SFH18_departNameAccept"></s:text></th>
										<th><s:text name="SFH18_inventoryNameAccept"></s:text></th>
										<th><s:text name="SFH18_logicInventoryNameAccept"></s:text></th>
										<th><s:text name="SFH18_totalQuantity"></s:text></th>
										<th><s:text name="SFH18_totalAmount" /></th>
										<th><s:text name="SFH18_importDate"></s:text></th>
										<th><s:text name="SFH18_importResult"></s:text></th>
										<th><s:text name="SFH18_tradeStatus"></s:text></th>
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
			<s:url action="BINOLSTSFH18_search" id="searchUrl" />
			<a href="${searchUrl}" id="search_Url"></a>
			<div id="popuTitle"><s:text name="SFH18_popuTitle" /></div>
			<div id="popuOK"><s:text name="global.page.ok" /></div>
			<div id="processing"><span class="dataTables_processing"></span></div>
		</div>
	</form>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== 弹出订货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通END ======================= --%>
<div id="detailData" class="hide"></div>
<script type="text/javascript">
	$(document).ready(function() {
		BINOLSTSFH18.search();
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