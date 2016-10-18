<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS05.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
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
<%-- 调拨记录查询URL --%>
<s:url id="search_url" value="/pt/BINOLPTRPS05_search"/>
<input type="hidden" value="${search_url}" id="search_url"/>

<div class="hide">
<!-- Excel导出URL -->
<s:url id="exportUrl" action="BINOLPTRPS05_export" ></s:url>
<a id="exportUrl" href="${exportUrl}"></a>
<!-- CRV导出URL -->
<s:url id="exportCsvUrl" action="BINOLPTRPS05_exportCsv" ></s:url>
<a id="exportCsvUrl" href="${exportCsvUrl}"></a>
<!-- 导出数据量限制 -->
<s:url id="exporChecktUrl" action="BINOLPTRPS05_exportCheck" ></s:url>
<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
</div>

<s:i18n name="i18n.pt.BINOLPTRPS05">
<div id="selectAll" class="hide"><s:text name="global.page.all"/></div>
<s:text name="global.page.all" id="RPS05_selectAll"/>
<div class="panel-header">
     	<%-- ###调拨记录查询 --%>
       <div class="clearfix"> 
       	<span class="breadcrumb left"> 
       		<span class="ui-icon icon-breadcrumb"></span>
       		<s:text name="RPS05_breadcrumb"/>&gt; <s:text name="RPS05_title"/> 
       	</span>  
       </div>
</div>
<%-- ================== 错误信息提示 START ======================= --%>
<div id="errorMessage"></div>
<%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline">
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span>
           			<s:text name="global.page.condition"/>
           		</strong>
        	</div>
			<div class="box-content clearfix">
				<div class="column" style="width: 50%;height:85px;">
					<p><%-- 调拨类型 --%>
						<label><s:text name="RPS05_allocationType" /></label>
						<s:select name="allocationType" list="#application.CodeTable.getCodes('1265')" listKey="CodeKey"
						    listValue="Value" />
					</p>
					<p><%-- 调拨单号 --%>
					<label><s:text name="RPS05_allocationNo" /></label>
					<s:textfield name="allocationNo" cssClass="text" /></p>
					<p><%-- 产品名称 --%>
			            <label><s:text name="RPS05_productName"/></label>
			                <s:textfield name="nameTotal" cssClass="text" maxlength="100"/>
				            <input type="hidden" id="prtVendorId" name="prtVendorId" value="" />
			        </p>
				</div>
				<div class="column last" style="width: 49%;height:85px;">
					<p class="date">
					<%-- 调拨日期 --%>
					<label><s:text name="RPS05_allocationDate" /></label> <span><s:textfield id="startDate"
						name="startDate" cssClass="date" /></span> - <span><s:textfield
						id="endDate" name="endDate" cssClass="date" /></span>
					</p>
					<p>
					<%-- 审核状态 --%>
					<label><s:text name="RPS05_verifiedFlag" /></label>
					<s:select name="verifiedFlag"
						list="#application.CodeTable.getCodes('1007')" listKey="CodeKey"
						listValue="Value" headerKey="" headerValue="%{RPS05_selectAll}" />
					</p>
				</div>
				<%-- ======================= 组织联动共通导入开始  ============================= --%>
				<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
					<s:param name="businessType">1</s:param>
	 				<s:param name="operationType">1</s:param>
				</s:action>
				<%-- ======================= 组织联动共通导入结束  ============================= --%>
			</div>
			<p class="clearfix">
	   			<%-- 查询 --%>
	   			<button class="right search" type="button" onclick="search('<s:property value="#search_url"/>')">
	   				<span class="ui-icon icon-search-big"></span>
	   				<span class="button-text"><s:text name="global.page.search"/></span>
	   			</button>
	        </p>
		</cherry:form>
	</div>
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section hide">
        <div class="section-header">
       		<strong>
       			<span class="ui-icon icon-ttl-section-search-result"></span>
       			<s:text name="global.page.list"/>
      		</strong>
       	</div>
      	<div class="section-content">
	        <div class="toolbar clearfix">
				<cherry:show domId="BINOLPTRPS05EXP">
		        	<span style="margin-right:10px;">
		        		<a id="export" class="export" onclick="BINOLPTRPS05.exportExcel();return false;">
		                   <span class="ui-icon icon-export"></span>
		                   <span class="button-text"><s:text name="global.page.exportExcel"/></span>
		                </a>
		                <a id="export" class="export" onclick="BINOLPTRPS05.exportCsv();return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="global.page.exportCsv"/></span>
				       </a>
		             </span>
		         </cherry:show>
				<span id="headInfo" style=""></span>
	       		<span class="right" id="colSettingBtn">
	       			<%-- 设置列 --%>
	       			<a class="setting">
	       				<span class="ui-icon icon-setting"></span>
	       				<span class="button-text"><s:text name="global.page.colSetting"/></span>
	       			</a>
	       		</span>
	         </div>
	         <div id="searchResultDiv"></div>
         	</div>
       </div>
       <%-- ====================== 结果一览结束 ====================== --%>
		<div class="hide">
			<div id="RPS05_num"><s:text name="RPS05_num" /></div>
			<div id="RPS05_allocationNo"><s:text name="RPS05_allocationNo" /></div>
			<div id="RPS05_sendOrg"><s:text name="RPS05_sendOrg" /></div>
			<div id="RPS05_receiveOrg"><s:text name="RPS05_receiveOrg" /></div>
			<div id="RPS05_totalQuantity"><s:text name="RPS05_totalQuantity" /></div>
			<div id="RPS05_totalAmount"><s:text name="RPS05_totalAmount" /></div>
			<div id="RPS05_dateOut"><s:text name="RPS05_dateOut" /></div>
			<div id="RPS05_dateIn"><s:text name="RPS05_dateIn" /></div>
			<div id="RPS05_verifiedFlag"><s:text name="RPS05_verifiedFlag" /></div>
			<div id="RPS05_employeeName"><s:text name="RPS05_employeeName" /></div>
			<div id="colSettingBtnDiv">
				<a class="setting">
	    			<span class="ui-icon icon-setting"></span>
	    			<span class="button-text"><s:text name="global.page.colSetting"/></span>
	    		</a>
    		</div>
		</div>
</div>	
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ============ 弹出dataTable 产品共通导入 START ================= --%>
<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />
<%-- ============ 弹出dataTable 产品共通导入 END =================== --%>
<%-- ============ 弹出导出CSV共通对话框 START ================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<%-- ============ 弹出导出CSV共通对话框 END================= --%>