<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/pt/rps/BINOLPTRPS29.js"></script>

<s:i18n name="i18n.pt.BINOLPTRPS29">
<div class="hide">
	<s:url action="BINOLPTRPS29_search" id="search_url"></s:url>
	<a href="${search_url }" id="searchUrl"></a>
	<s:url id="export_url" action="BINOLPTRPS29_export" ></s:url>
</div>
<div class="panel-header">
  <div class="clearfix"> 
	<span class="breadcrumb left">	    
	  <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
	</span>  
  </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline">
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           			<span class="highlight">（<s:text name="PTRPS.tips" />）</span>
           		</strong>
            </div>
            <div class="box-content clearfix">
               	<div class="column" style="width:50%;height:auto;">
	                <p>
	                  	<label><s:text name="PTRPS.batchCode"/><span class="highlight">*</span></label>
	                  	<span><input name="batchCode" class="text" /></span>
	                </p>    
		        </div>    
        		<div class="column last" style="width:49%;height:auto;">
	               	<p>
	                  	<label><s:text name="PTRPS.counterName"/></label>
	                  	<input id="counterName" name="counterName" class="text" />
	                </p>    
	            </div>
            </div> 
         	<p class="clearfix">
         		<button class="right search" type="button" onclick="BINOLPTRPS29.search();return false;">
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
				<s:text name="global.page.list" />
		 	</strong>
		</div>
		<div class="section-content">
			<div class="toolbar clearfix">
				<span style="margin-right:10px;">
					<cherry:show domId="PTRPS29EXPORT">
				     	<a id="export" href="${export_url }" class="export" onclick="BINOLPTRPS29.exportExcel(this, '0');return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="PTRPS.mainExcel"/></span>
				        </a>
				        <a id="exportCsv" href="${export_url }" class="export" onclick="BINOLPTRPS29.exportExcel(this, '1');return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="PTRPS.mainCsv"/></span>
				        </a>
			        </cherry:show>
			        <cherry:show domId="PTRPS29EXPDET">
				        <a id="export" href="${export_url }" class="export" onclick="BINOLPTRPS29.exportExcel(this, '2');return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="PTRPS.detailExcel"/></span>
				        </a>
				        <a id="exportCsv" href="${export_url }" class="export" onclick="BINOLPTRPS29.exportExcel(this, '3');return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="PTRPS.detailCsv"/></span>
				        </a>
			        </cherry:show>
			    </span>
				<span class="right"> 
					<%-- 设置列 --%> 
					<a class="setting"> 
						<span class="ui-icon icon-setting"></span> 
						<span class="button-text"><s:text name="global.page.colSetting" /></span>
					</a>
				</span>
			</div>
			<div id="saleRptDiv">
				<table class="jquery_table" style="width: 100%" id="dataTable">
					<thead>
						<tr>
							<th><s:text name="No." /></th>
							<th><s:text name="PTRPS.batchCode" /></th>
							<th><s:text name="PTRPS.resellerCode" /></th>
							<th><s:text name="PTRPS.resellerName" /></th>
							<th><s:text name="PTRPS.counterCode" /></th>
							<th><s:text name="PTRPS.counterName" /></th>
							<th><s:text name="PTRPS.startTime" /></th>
							<th><s:text name="PTRPS.endTime" /></th>
							<th class="center"><s:text name="PTRPS.totalQuantity" /></th>
							<th class="center"><s:text name="PTRPS.totalAmount" /></th>
							<th class="center"><s:text name="PTRPS.totalBillCount" /></th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />