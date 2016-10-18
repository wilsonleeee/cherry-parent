<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/mb/rpt/BINOLMBRPT06.js"></script>
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
<s:i18n name="i18n.mb.BINOLMBRPT06">
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
	                	<label style="width:100px;"><s:text name="RPT06_memberCode" /></label>
	                    <input type="text" class="text" name="memberCode"></input>
	                </p>
	                <p>
	                	<label style="width:100px;"><s:text name="RPT06_recommendedMemCode" /></label>
	                    <input type="text" class="text" name="recommendedMemCode"></input>
	                </p>
		        </div>    
        		<div class="column last" style="width:49%;height:auto;">
	                <p>
	                  	<label style="width:100px;"><s:text name="RPT06_date"/></label>
	                  	<span><s:textfield name="startDate" cssClass="date"/></span>
	                  	 - 
	                  	<span><s:textfield name="endDate" cssClass="date"/></span>
	                </p>
	            </div>
            </div> 
         	<p class="clearfix">
         		<button class="right search" type="button" onclick="BINOLMBRPT06.search();return false;">
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
			    <cherry:show domId="BINOLMBRPT06EXP">
					<span style="margin-right:10px;">
				     	<a id="export" class="export" onclick="BINOLMBRPT06.exportExcel('0');return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="RPT06_exportExcel"/></span>
				        </a>
				        <a id="exportCsv" class="export" onclick="BINOLMBRPT06.exportExcel('1');return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="RPT06_exportExcelCsv"/></span>
				        </a>
				    </span>
			    </cherry:show>
			    <span id="headInfo"></span>
				<span class="right"> 
					<%-- 设置列 --%>
					<a class="setting"> 
						<span class="ui-icon icon-setting"></span> 
						<span class="button-text"><s:text name="global.page.colSetting" /></span>
					</a>
				</span>
			</div>
			<div id="memberRecommendRptDiv">
				<table class="jquery_table" style="width: 100%" id="dataTable">
					<thead>
						<tr>
							<th><s:text name="No." /></th>
							<th><s:text name="RPT06_member" /></th>
							<th><s:text name="RPT06_recommendedMember" /></th>
							<th><s:text name="RPT06_mobilePhone" /></th>
							<th><s:text name="RPT06_orderCount" /></th>
							<th><s:text name="RPT06_saleQuantity" /></th>
							<th><s:text name="RPT06_saleAmount" /></th>
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
<div style="display: none;">
<s:url action="BINOLMBRPT06_search" id="search_url"></s:url>
<a id="searchUrl" href="${search_url}"></a>
<s:url action="BINOLMBRPT06_exportExcel" id="exportExcel_url"></s:url>
<a id="exportExcelUrl" href="${exportExcel_url}"></a>
<s:url id="exporCheck_url" action="BINOLMBRPT06_exportCheck" ></s:url>
<a id="exporCheckUrl" href="${exporCheck_url}"></a>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== CSV导出共通 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<%-- ================== CSV导出共通 END ======================= --%>