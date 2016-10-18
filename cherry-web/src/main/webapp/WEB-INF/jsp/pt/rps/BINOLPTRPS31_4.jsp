<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/pt/rps/BINOLPTRPS31_2.js"></script>

<s:i18n name="i18n.pt.BINOLPTRPS31">
	<%-- 指定代理商的推荐会员销售明细导出URL --%>
	<div class="hide">
	<s:url action="BINOLPTRPS31_baSaleExport" id="export_url"/>
	<s:url id="exporCheck_url" action="BINOLPTRPS31_baSaleExportCheck" ></s:url>
	<a id="exporCheckUrl" href="${exporCheck_url}"></a>
	</div>
	<form id="baSaleForm">
		<div class="panel ui-corner-all">
			<div id="div_main">
				<div class="panel-header">
					<div class="clearfix"> 
						<span class="breadcrumb left">	    
							<span class="ui-icon icon-breadcrumb"></span>
							<s:text name="RPS31_native1" />&nbsp;&gt;&nbsp;
							<s:text name="RPS31_native3" />
						</span>
					</div>
				</div>
				<div id="errorMessage"></div>
 				<div id="actionResultDisplay"></div>
 				<div class="panel-content clearfix">
 					<div class="box">
 						<input type="hidden" value='<s:property value="baInfoId"/>'  name="baInfoId" />
 						<input type="hidden" value='<s:property value="brandInfoId"/>'  name="brandInfoId" />
 						<input type="hidden" value='<s:property value="startDate"/>'  name="startDate" />
 						<input type="hidden" value='<s:property value="endDate"/>'  name="endDate" />
 						<div class="box-header"> 
 							<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
 						</div>
 						<div class="box-content clearfix">
 							<div class="column" style="width:50%; height: auto;">
 								<p>
									<label><s:text name="RPS31_baName" /></label>
									 <span>
										<input id="baName" type="text" class="text" disabled="disabled" value="<s:property value='%{baInfoMap.baName}'/>"/>
									</span>
								</p>
								<p>
									<label><s:text name="RPS31_memberCode" /></label>
									<s:textfield name="memberCode" cssClass="text" id="memberCode"/>
								</p>
								<p>
									<label><s:text name="RPS31_productName"/></label>
									<s:textfield name="nameTotal" cssClass="text"/>
									<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>
								</p>
 							</div>
 							<div class="column last" style="width:49%; height: auto;">
 								<p>
									<label><s:text name="RPS31_date"/></label>
									<input id="startDate" class="date" disabled="disabled" value='<s:property value="%{baInfoMap.startDate}"/>'></input>
									- 
									<input id="endDate" class="date" disabled="disabled" value='<s:property value="%{baInfoMap.endDate}"/>'></input>
								</p>
								<p>
									<label><s:text name="RPS31_mobliePhone"/></label>
									<s:textfield name="mobilePhone" cssClass="text" id="mobilePhone"/>
								</p>
 							</div>
 						</div>
 						<p class="clearfix">
	 						<button class="right search" onclick="BINOLPTRPS31_2.search();return false;">
								<span class="ui-icon icon-search-big"></span>
								<span class="button-text">
									<s:text name="global.page.search"></s:text>
								</span>
								</button>
 						</p>
 					</div>
 					<div class="section hide" id="baSaleInfo">
 						<div class="section-header">
 							<strong>
							  	<span class="ui-icon icon-ttl-section-search-result"></span>
							  	<s:text name="global.page.list"></s:text>
						  	</strong>
 						</div>
	 					<div class="section-content">
	 						<div class="toolbar clearfix">
	 							<cherry:show domId="PTRPS31BAEXP">
									<span style="margin-right:10px;">
								     	<a id="export" href="${export_url }" class="export" onclick="BINOLPTRPS31_2.exportExcel(this, '0');return false;">
								          <span class="ui-icon icon-export"></span>
								          <span class="button-text"><s:text name="global.page.exportExcel"/></span>
								        </a>
								        <%-- <a id="exportCsv" href="${export_url }" class="export" onclick="BINOLPTRPS31_1.exportExcel(this, '1');return false;">
								          <span class="ui-icon icon-export"></span>
								          <span class="button-text"><s:text name="global.page.exportCsv"/></span>
								        </a> --%>
								    </span>
							    </cherry:show>
							  	<span class="right">
						  			<a class="setting">
						  				<span class="ui-icon icon-setting"></span>
					              		<span class="button-text"><s:text name="global.page.colSetting"/></span>
				              		</a>
				              	</span>
	 						</div>
	 						<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="baSaleDataTable">
	 							<thead>
							        <tr>
										<th><s:text name="NO."></s:text></th>
										<th><s:text name="RPS31_billCode"></s:text></th>
										<th><s:text name="RPS31_baName"></s:text></th>
										<th><s:text name="RPS31_member"></s:text></th>
										<th><s:text name="RPS31_mobliePhone"></s:text></th>
										<th><s:text name="RPS31_memberBuyDate"></s:text></th>
										<th><s:text name="RPS31_unitCode"></s:text></th>
										<th><s:text name="RPS31_barCode"></s:text></th>
										<th><s:text name="RPS31_productName"></s:text></th>
										<th><s:text name="RPS31_quantity"></s:text></th>
										<th><s:text name="RPS31_amount"></s:text></th>
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
			<s:url action="BINOLPTRPS31_baSaleSearch" id="search_url" />
			<a href="${search_url}" id="searchUrl"></a>
			<div id="processing"><span class="dataTables_processing"></span></div>
		</div>
	</form>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<script type="text/javascript">
	$(document).ready(function() {
		BINOLPTRPS31_2.search();
		
		//产品选择绑定
		productBinding({elementId:"nameTotal",showNum:20});
		
	});
</script>
