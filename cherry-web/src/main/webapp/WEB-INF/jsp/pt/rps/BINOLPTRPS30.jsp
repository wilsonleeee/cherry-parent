<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/pt/rps/BINOLPTRPS30.js"></script>

<s:i18n name="i18n.pt.BINOLPTRPS30">
<div class="hide">
	<s:url action="BINOLPTRPS30_search" id="search_url"></s:url>
	<a href="${search_url }" id="searchUrl"></a>
	<s:url id="export_url" action="BINOLPTRPS30_export" ></s:url>
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
	<cherry:form id="mainForm" class="inline">
		<cherry:brand name="brandInfoId" />
		<div class="box">	
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>
            </div>
            <div class="box-content clearfix">
               	<div class="column" style="width:50%;height:auto;">
	                <p>
	                  	<label><s:text name="PTRPS.counterName"/></label>
	                  	<input id="counterName" name="counterName" class="text" />
	                </p>    
		        </div>    
        		<div class="column last" style="width:49%;height:auto;">
	               	<p>
	                  	<label><s:text name="时间范围"/></label>
	                  	<span><input id="startDate" name="startDate" class="date" /></span>
	                  	-
	                  	<span><input id="endDate" name="endDate" class="date" /></span>
	                </p>
	            </div>
            </div> 
         	<p class="clearfix">
         		<button class="right search" type="button" onclick="BINOLPTRPS30.search();return false;">
         			<span class="ui-icon icon-search-big"></span>
         			<span class="button-text"><s:text name="global.page.search"/></span>
         		</button>
        	</p>
       	</div> 
    </cherry:form>  
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
				<cherry:show domId="PTRPS30EXPORT">
					<span style="margin-right:10px;">
				     	<a id="export" href="${export_url }" class="export" onclick="BINOLPTRPS30.exportExcel(this, '0');return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="PTRPS.mainExcel"/></span>
				        </a>
				        <a id="exportCsv" href="${export_url }" class="export" onclick="BINOLPTRPS30.exportExcel(this, '1');return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="PTRPS.mainCsv"/></span>
				        </a>
				    </span>
			    </cherry:show>
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
							<th><s:text name="PTRPS.regionName" /></th>
							<th><s:text name="PTRPS.resellerName" /></th>
							<th><s:text name="PTRPS.basName" /></th>
							<th><s:text name="PTRPS.counterName" /></th>
							<th><s:text name="PTRPS.saleAmount" /></th>
							<th><s:text name="PTRPS.saleCount" /></th>
							<th><s:text name="PTRPS.saleQuantity" /></th>
							<th><s:text name="PTRPS.saleQuantity1" /></th>
							<th><s:text name="PTRPS.memSaleAmount" /></th>
							<th><s:text name="PTRPS.notMemSaleAmout" /></th>
							<th><s:text name="PTRPS.promSaleAmount" /></th>
							<th><s:text name="PTRPS.inventoryAmount" /></th>
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