<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH15.js"></script>
<s:i18n name="i18n.st.BINOLSTSFH15">
	<s:url id="export_url" action="BINOLSTSFH15_export"/>
    <s:url id="exportDetail_url" action="BINOLSTSFH15_exportDetail"/>
	<div class="hide">
		<s:url id="search_url" value="/st/BINOLSTSFH15_search"/>
		<a id="searchUrl" href="${search_url}"></a>
	</div>
	<div class="panel-header">
		<div class="clearfix"> 
			<span class="breadcrumb left">      
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
		</div>
	</div>
	<div id="actionResultDisplay"></div>
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="errorMessage"></div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
		<div class="box">
			<s:text name="stsfh15.selectAll" id="selectAll"/>
			<cherry:form id="mainForm" class="inline">
				<input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLSTSFH15"/>
				<div class="box-header">
				<strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<s:text name="stsfh15.condition"/>
	            </strong>
				</div>
				<div class="box-content clearfix">
					<div class="column" style="height:120px;width:50%">
		            <p>
						<label><s:text name="stsfh15.saleOrderNo"/></label>
						<s:textfield name="saleOrderNo" cssClass="text" maxlength="40"/>
	                </p>
	                <!-- 导入批次 -->
	                <p>
						<label><s:text name="SFH15_importBatch"/></label>
						<s:textfield name="importBatch" cssClass="text" maxlength="25"/>
	                </p>
                    <p>
                        <label><s:text name="stsfh15.customerType"/></label>
                        <s:select id="customerType" name="customerType" list='#application.CodeTable.getCodes("1297")' 
                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}" 
                            onchange="BINOLSTSFH15.changeCustomerType();"/>
                    </p>
                    <p id="pCustomerName" class="hide">
                        <label><s:text name="stsfh15.customerName"/></label>
                        <input type="hidden" name="customerOrganizationId" id="customerOrganizationId"></input>
                        <input type="text" class="text" id="customerOrganization"></input>
                    </p>
					</div>
					<div class="column last" style="width:49%">      
					<p class="date">
						<label><s:text name="stsfh15.dateCondition"/></label>
						<span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
					</p>      
                    <p>
                        <label><s:text name="stsfh15.saleOrganization"/></label>
                        <input type="hidden" name="organizationId" id="organizationId"></input>
                        <input type="text" class="text" id="organization"></input>
                    </p>
					<p>
						<label><s:text name="stsfh15.billState"/></label>
						<s:select id="billState" name="billState" list='#application.CodeTable.getCodes("1294")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}"/>
					</p>
					</div>
					<%-- ======================= 组织联动共通导入开始  ============================= --%>
					<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
						<s:param name="businessType">3</s:param>
						<s:param name="operationType">1</s:param>
					</s:action>
					<%-- ======================= 组织联动共通导入结束  ============================= --%>
				</div>
				<p class="clearfix">
					<button class="right search"  onclick="BINOLSTSFH15.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<span class="button-text"><s:text name="stsfh15.search"/></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="saleOrdersSection" class="section hide">
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="stsfh15.resultlist"/>
				</strong>
			</div>
			<div class="section-content" id="result_list">
				<div class="toolbar clearfix">
					<cherry:show domId="BINOLSTSFH15EXP">
						<a class="export left" onclick="javascript:BINOLSTSFH15.exportExcel(this);return false;"  href="${export_url}">
							<span class="ui-icon icon-export"></span>
							<span class="button-text"><s:text name="global.page.export"></s:text></span>
						</a>
					</cherry:show>
                    <cherry:show domId="STSFH1502EXP">
                        <a class="export left" onclick="javascript:BINOLSTSFH15.exportExcel(this);return false;"  href="${exportDetail_url}">
                            <span class="ui-icon icon-export"></span>
                            <span class="button-text"><s:text name="global.page.exportExcel02"></s:text></span>
                        </a>
                    </cherry:show>
                    <cherry:show domId="BINOLSTSFH15PNT">
						<div id="print_param_hide" class="hide">
							<input type="hidden" name="pageId" value="BINOLSTSFH16"/>
		         		</div>
				 		<a style="margin-right:10px" onclick="openPrintApp('Print','#result_list');return false;" class="prints left">
							<span class="ui-icon icon-prints"></span>
							<span class="button-text"><s:text name="global.page.prints"/></span>
						</a>
		   			</cherry:show>
		   			<span id="headInfo" style=""></span>
					<span class="right">
						<a class="setting">
                            <span class="ui-icon icon-setting"></span>
            	            <span class="button-text">
	                            <s:text name="stsfh15.colSetting"/>
                            </span>
						</a>
					</span>
				</div>
				<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
						<tr>                 
							<th><input type="checkbox" id="allSelect" onclick="checkBillAll(this);"/><s:text name="global.page.selectAll"/></th><%-- 选择 --%>
							<th><s:text name="stsfh15.saleOrderNo"/></th><%-- 销售单号--%>
							<th><s:text name="SFH15_importBatch"/></th><%-- 导入批次--%>
							<th><s:text name="stsfh15.customerName"/></th><%-- 客户名称--%>
							<th><s:text name="stsfh15.contactPerson"/></th><%-- 客户联系人 --%>
							<th><s:text name="stsfh15.deliverAddress"/></th><%-- 送货地址 --%>
							<th><s:text name="stsfh15.customerType"/></th><%-- 客户类型 --%>
							<th><s:text name="stsfh15.billType"/></th><%-- 订单类型--%>
							<th><s:text name="stsfh15.saleOrganization"/></th><%-- 销售部门--%>
							<th><s:text name="stsfh15.saleEmployee"/></th><%-- 业务员--%>
							<th><s:text name="stsfh15.totalQuantity"/></th><%-- 销售总数量--%>
							<th><s:text name="stsfh15.originalAmount"/></th><%-- 整单折扣前金额--%>
							<th><s:text name="stsfh15.discountRate"/></th><%-- 整单折扣率--%>
							<th><s:text name="stsfh15.discountAmount"/></th><%-- 整单折扣金额--%>
							<th><s:text name="stsfh15.payAmount"/></th><%-- 实收金额--%>
							<th><s:text name="stsfh15.expectFinishDate"/></th><%-- 期望完成日期--%>
							<th><s:text name="stsfh15.saleDate"/></th><%-- 销售日期--%>
							<%--<th><s:text name="stsfh15.saleTime"/></th>--%><%-- 销售时间--%>
							<th><s:text name="stsfh15.billTicketTime"/></th><%-- 制单时间--%>
							<th><s:text name="stsfh15.employeeName"/></th><%-- 制单员工--%>
							<th><s:text name="stsfh15.billState"/></th><%-- 单据状态--%>
							<%-- <th><s:text name="global.page.printStatus"/></th> --%> <%-- 打印状态 --%>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>


