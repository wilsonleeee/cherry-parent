<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/pms/BINOLCTPMS02.js"></script>
<s:i18n name="i18n.ct.BINOLCTPMS02">
	<div class="hide">
		<s:url id="s_editParam" value="/ct/BINOLCTPMS01_editParam" />
		<a id="editParamUrl" href="${s_editParam}"></a>
		<s:url id="s_editParamMany" value="/ct/BINOLCTPMS01_editParamMany" />
		<a id="editParamManyUrl" href="${s_editParamMany}"></a>
		<span id="editParamTitle"><s:text name="PMS02_editParamTitle"></s:text></span>
	</div>
	<div id="dialogInit"></div>
	<div class="dialog2 clearfix" style="display: none"
		id="editParam_dialog">
		<p class="clearfix">
		<p class="message">
			<span><s:text name="PMS02_editParamMessage" /></span>
		</p>
	</div>
	<div class="panel-header">
		<div id="actionResultDisplay"></div>
		<div id="errorMessage"></div>
		<div class="clearfix">
			<span class="breadcrumb left"> <span
				class="ui-icon icon-breadcrumb"></span> <s:text
					name="PMS02_pmsTitle" /> &gt; <s:text name="PMS02_phoneParamSetting" /></span>
		</div>
	</div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="phoneForm" class="inline">
				<div class="box-header">
					<%--短信配置搜索条件--%>
					<span class="ui-icon icon-ttl-search"></span> <strong><s:text
							name="PMS02_phoneSearch" /></strong>
				</div>
				<div class="box-content clearfix">
					<div style="width: 50%; height: 50px;" class="column">
						<p class="clearfix">
								<%--电话外呼供应商--%>
								<label style="width: 80px;"><s:text name="PMS02_phoneChannel" /></label>
								<span> <s:select name="configGroup" Cssstyle="width:186px;"
										list='#application.CodeTable.getCodes("1352")'
										listKey="CodeKey" listValue="Value" 
										headerValue="%{selectAll}"  onchange="BINOLCTPMS02.searchList();return false;"/>
								</span>
								<input name="supplierType" id="supplierType" value="3" class="hide"/>
						</p>
					</div>
				</div>
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
					<span class="right">
							<a class="edit" onclick="BINOLCTPMS02.editParam(1,this);return false;">
				       		<span class="ui-icon icon-edit"></span>
				       		<span class="button-text">
							<s:text name="PMS02_editMany" />
				        	</span>
    						</a>
					</span>
				</div>
				<div class="section-content">
					<table id="resultParamDataTable" cellpadding="0" cellspacing="0"
						border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:text name="PMS02_number" /></th>
								<th><s:text name="PMS02_paramName" /></th>
								<th><s:text name="PMS02_paramCode" /></th>
								<th><s:text name="PMS02_phoneConfigGroup" /></th>
								<th><s:text name="PMS02_paramKey" /></th>
								<th><s:text name="PMS02_paramValue" /></th>
								<th><s:text name="PMS02_operator" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="dialogConfirm" class="hide">
		<s:text name="PMS02_confirm" />
	</div>
	<div id="dialogCancel" class="hide">
		<s:text name="PMS02_cancel" />
	</div>
</s:i18n>

<div class="hide">
	<s:a action="BINOLCTPMS01_search" id="searchUrl"></s:a>
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<%-- ================== dataTable共通导入    END  ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp"
		flush="true" />
</div>