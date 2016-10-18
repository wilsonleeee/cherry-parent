<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/ios/BINOLSTIOS08.js"></script>
<%-- 入库单（Excel导出）URL --%>
<s:url action="BINOLSTIOS08_export" id="export_url"/>
<s:i18n name="i18n.st.BINOLSTIOS08">
	<form id="mainForm">
		<s:text name="global.page.all" id="select_default"/>
		<div class="panel ui-corner-all">
			<div id="div_main">
				<div class="panel-header">
					<div class="clearfix"> 
						<span class="breadcrumb left">	    
							<span class="ui-icon icon-breadcrumb"></span>
							<s:text name="binolstios08_native1" />&nbsp;&gt;&nbsp;
							<s:text name="binolstios08_native2" />
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
						        	<label style="width:80px;"><s:text name="binolstios08_billNo" /></label>
						            <span>
						            	<s:textfield name="billNo" cssClass="text" maxlength="40"></s:textfield>
									</span>
						        </p>
						        <p>
						          	<label style="width:80px;"><s:text name="binolstios08_departCode" /></label>
						            <span>
						            	<s:textfield name="departCode" cssClass="text" maxlength="15"></s:textfield>
									</span>
						        </p>
						        <p>
						          	<label style="width:80px;"><s:text name="binolstios08_departName" /></label>
						            <span>
						            	<s:textfield name="departName" cssClass="text" maxlength="50"></s:textfield>
									</span>
						        </p>
 							</div>
 							<div class="column last" style="width:49%; height: auto;">
 								<p>
						        	<label><s:text name="binolstios08_inDepotDate"/></label>
						            <span>
						            	<s:textfield id="inDepotStartTime" name="inDepotStartTime" cssClass="date"/>
						            </span> - 
						            <span>
						            	<s:textfield id="inDepotEndTime" name="inDepotEndTime" cssClass="date"/>
						            </span>
						        </p>
						        <p>
						        	<label><s:text name="binolstios08_importResult"/></label>
						            <span>
						            	<s:select list='#application.CodeTable.getCodes("1250")' listKey="CodeKey" listValue="Value" id="importResult" name="importResult"  
										headerKey="" cssStyle="width:120px;" headerValue="%{#select_default}"></s:select>
						            	
						            </span>
						        </p>
						        <p>
						        	<label><s:text name="binolstios08_tradeStatus"/></label>
						            <span>
						            	<s:select list='#application.CodeTable.getCodes("1266")' listKey="CodeKey" listValue="Value" id="tradeStatus" name="tradeStatus"  
										headerKey="" cssStyle="width:120px;" headerValue="%{#select_default}"></s:select>
						            	
						            </span>
						        </p>
						        
 							</div>
 						</div>
 						<p class="clearfix">
	 						<button class="right search" onclick="BINOLSTIOS08.search();return false;">
					      		<span class="ui-icon icon-search-big"></span>
						      	<span class="button-text">
						      		<s:text name="global.page.search"></s:text>
					      		</span>
				      		</button>
 						</p>
 					</div>
 					<div class="section hide" id="productInDepotExcelInfo">
 						<div class="section-header">
 							<strong>
							  	<span class="ui-icon icon-ttl-section-search-result"></span>
							  	<s:text name="global.page.list"></s:text>
						  	</strong>
 						</div>
	 					<div class="section-content">
	 						<div class="toolbar clearfix">
	 							<cherry:show domId="STIOS09EXPORT">
									<a class="export left" onclick="javascript:BINOLSTIOS08.exportExcel(this);return false;"  href="${export_url}">
										<span class="ui-icon icon-export"></span>
										<span class="button-text"><s:text name="binolstios08_exportExcel_btn"></s:text></span>
									</a>
							  	</cherry:show>
							  	<span class="right">
						  			<a class="setting">
						  				<span class="ui-icon icon-setting"></span>
					              		<span class="button-text"><s:text name="global.page.colSetting"/></span>
				              		</a>
				              	</span>
	 						</div>
	 						<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="productInDepotExcelInfoDataTable">
	 							<thead>
							        <tr>
										<th><s:text name="NO."></s:text></th>
										<th><s:text name="binolstios08_billNo"></s:text></th>
										<th><s:text name="binolstios08_departName"></s:text></th>
										<th><s:text name="binolstios08_inventoryName"></s:text></th>
										<th><s:text name="binolstios08_logicInventoryName"></s:text></th>
										<th><s:text name="binolstios08_totalQuantity"></s:text></th>
										<th><s:text name="binolstios08_totalAmount" /></th>
										<th><s:text name="binolstios08_inDepotDate"></s:text></th>
										<th><s:text name="binolstios08_importResult"></s:text></th>
										<th><s:text name="binolstios08_tradeStatus"></s:text></th>
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
			<s:url action="BINOLSTIOS08_search" id="searchUrl" />
			<a href="${searchUrl}" id="search_Url"></a>
			<s:url value="BINOLSTIOS08_getIndepotExcelDetail" id="detaiURL"/>
			<a href="${detaiURL }" id="detai_URL"></a>
			<div id="popuTitle"><s:text name="binolstios08_popuTitle" /></div>
			<div id="popuOK"><s:text name="global.page.ok" /></div>
			<div id="processing"><span class="dataTables_processing"></span></div>
		</div>
	</form>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<div id="detailData" class="hide"></div>
<script type="text/javascript">
	$(document).ready(function() {
		BINOLSTIOS08.search();
       	var holidays = '${holidays }';
       	$('#inDepotStartTime').cherryDate({
        	holidayObj: holidays,
           	beforeShow: function(input){
            	var value = $('#inDepotEndTime').val();
               	return [value,'maxDate'];
           	}
       });
       $('#inDepotEndTime').cherryDate({
           	holidayObj: holidays,
           	beforeShow: function(input){
               	var value = $('#inDepotStartTime').val();
               	return [value,'minDate'];
           	}
       });
	});
</script>