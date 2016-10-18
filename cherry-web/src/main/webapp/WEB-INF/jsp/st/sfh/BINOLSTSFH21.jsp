<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/st/sfh/BINOLSTSFH21.js"></script>
<script type="text/javascript">
	//节日
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
<s:i18n name="i18n.st.BINOLSTSFH21">
<div class="hide">
	<s:url action="BINOLSTSFH21_search" id="search_url"></s:url>
	<a href="${search_url }" id="searchUrl"></a>
	<!-- excel导出 -->
	<s:url id="export_url" action="BINOLSTSFH21_export" ></s:url>
	<a href="${export_url }" id="exportUrl"></a>
	<s:url id="exporCheck_url" action="BINOLSTSFH21_exportCheck" ></s:url>
	<a id="exporCheckUrl" href="${exporCheck_url}"></a>
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
	               		<%-- 产品名称 --%>
	                  	<label><s:text name="SFH21_prtName"/></label>
	                  	<s:textfield name="nameTotal" cssClass="text"/>
	                  	<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>
	                </p>   
		        </div>    
        		<div class="column last" style="width:49%;height:auto;">
	               	<p>
	                  	<label><s:text name="SFH21_date"/></label>
	                  	<span><s:textfield name="startDate" cssClass="date"/></span>
	                  	 - 
	                  	<span><s:textfield name="endDate" cssClass="date"/></span>
	                </p>
	            </div>
            </div> 
         	<p class="clearfix">
         		<button class="right search" type="button" onclick="BINOLSTSFH21.search();return false;">
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
				<cherry:show domId="BINOLSTSFH21EXP">
					<span style="margin-right:10px;">
				     	<a id="export" href="${export_url }" class="export" onclick="BINOLSTSFH21.exportExcel(this, '0');return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="SFH21_mainExcel"/></span>
				        </a>
				        <a id="exportCsv" href="${export_url }" class="export" onclick="BINOLSTSFH21.exportExcel(this, '1');return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="SFH21_mainCsv"/></span>
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
							<th><s:text name="SFH21_departName" /></th>
							<th><s:text name="SFH21_startDate" /></th>
							<th><s:text name="SFH21_endDate" /></th>
							<th><s:text name="SFH21_FPC" /></th>
							<th><s:text name="SFH21_barCode" /></th>
							<th><s:text name="SFH21_prtForignName" /></th>
							<th><s:text name="SFH21_saleQuantity" /></th>
							<th><s:text name="SFH21_saleAmount" /></th>
							<th><s:text name="SFH21_stockQuantity" /></th>
							<th><s:text name="SFH21_unitsInTransit" /></th>
							<th><s:text name="SFH21_stockAmount" /></th>
							<th><s:text name="SFH21_propNameMid" /></th>
							<th><s:text name="SFH21_propNameBig" /></th>
							<th><s:text name="SFH21_Brand" /></th>
							<th><s:text name="SFH21_salePrice" /></th>
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
<%-- ================== Csv导出弹出框共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<%-- ================== Csv导出弹出框共通导入  END ======================= --%>