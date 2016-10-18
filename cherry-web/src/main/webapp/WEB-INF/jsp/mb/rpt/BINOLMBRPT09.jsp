<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>

<s:i18n name="i18n.mb.BINOLMBRPT09">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="mbrpt09_memtitle"></s:text>&nbsp;&gt;&nbsp;<s:text name="mbrpt09_title"></s:text>
		</span>
	  </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryForm" class="inline">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
              	<label><s:text name="mbrpt09_campaignCode"/></label>
				<s:select list="campaignList" listKey="campaignCode" listValue="campaignName" name="campaignCode"></s:select>
              	<s:hidden name="campaignName"></s:hidden>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
                <label><s:text name="mbrpt09_dateRange"/></label>
                <span><s:textfield name="startDate" cssClass="date"></s:textfield></span>-<span><s:textfield name="endDate" cssClass="date"></s:textfield></span>
              </p>
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" id="searchButton">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>
         	<button class="right search" id="exportExcel">
	       	  <span class="ui-icon icon-file-export"></span><span class="button-text"><s:text name="global.page.exportExcel"/></span>
	       	</button>	
          </p>
        </cherry:form>
      </div>
      <div id="resultList" class="hide">
      <div class="section">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="global.page.list"/>
        	</strong>
        </div>
        <div class="section-content">
          <div class="toolbar clearfix">
		  	<span id="headInfo"></span>
		  </div>
          <table id="resultDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="mbrpt09_cityName" /></th>
                <th><s:text name="mbrpt09_counterCode" /></th>
                <th><s:text name="mbrpt09_counterName" /></th>
                <th><s:text name="mbrpt09_orderCount" /></th>
                <th><s:text name="mbrpt09_bookCount" /></th>
                <th><s:text name="mbrpt09_getCount" /></th>
                <th><s:text name="mbrpt09_getPercent" /></th>
                <th><s:text name="mbrpt09_memCount" /></th>
                <th><s:text name="mbrpt09_saleAmount" /></th>
                <th><s:text name="mbrpt09_salePercent" /></th>
                <th><s:text name="mbrpt09_newMemCount" /></th>
                <th><s:text name="mbrpt09_oldMemCount" /></th>
                <th><s:text name="mbrpt09_newMemSaleAmount" /></th>
                <th><s:text name="mbrpt09_oldMemSaleAmount" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLMBRPT09_search" id="searchUrl"></s:a>
<s:a action="BINOLMBRPT09_exportExcel" id="exportExcelUrl"></s:a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />

<script type="text/javascript">
$(function(){
	
	$('#startDate').cherryDate({
		beforeShow: function(input){
			var value = $('#endDate').val();
			return [value,'maxDate'];
		}
	});
	$('#endDate').cherryDate({
		beforeShow: function(input){
			var value = $('#startDate').val();
			return [value,'minDate'];
		}
	});
	
	$("#campaignCode").change(function(){
		if($(this).val() != '') {
			$("#campaignName").val($(this).find("option:selected").text());
		} else {
			$("#campaignName").val('');
		}
	});
	$("#campaignCode").change();
	
	// 表单验证配置
	cherryValidate({						
		formId: 'queryForm',
		rules: {
			startDate: {required: true,dateValid: true},
			endDate: {required: true,dateValid: true}
		}
	});
	
	$("#searchButton").click(function(){
		// 表单验证
		if(!$("#queryForm").valid()) {
			return false;
		}
		searchList();
		return false;
	});
	
	function searchList() {
		var url = $("#searchUrl").attr("href");
		var params = $("#queryForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		$("#resultList").show();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId: '#resultDataTable',
				 // 数据URL
				 url: url,
				 // 表格默认排序
				 aaSorting: [[ 0, "asc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "cityName", "sWidth": "10%", "bSortable": false},
				             { "sName": "departCode", "sWidth": "10%", "bSortable": false},
				             { "sName": "departName", "sWidth": "13%", "bSortable": false },
				             { "sName": "orderCount", "sWidth": "5%", "bSortable": false},
				             { "sName": "bookCount", "sWidth": "5%", "bSortable": false},
				             { "sName": "getCount", "sWidth": "5%", "bSortable": false},
				             { "sName": "getPercent", "sWidth": "5%", "bSortable": false},
				             { "sName": "memCount", "sWidth": "5%", "bSortable": false},
				             { "sName": "saleAmount", "sWidth": "5%", "bSortable": false},
				             { "sName": "salePercent", "sWidth": "5%", "bSortable": false},
				             { "sName": "newMemCount", "sWidth": "8%", "bSortable": false},
				             { "sName": "oldMemCount", "sWidth": "8%", "bSortable": false},
				             { "sName": "newMemSaleAmount", "sWidth": "8%", "bSortable": false},
				             { "sName": "oldMemSaleAmount", "sWidth": "8%", "bSortable": false}],
				// 横向滚动条出现的临界宽度
				sScrollX: "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
	}
	
	$("#exportExcel").click(function(){
		// 表单验证
		if(!$("#queryForm").valid()) {
			return false;
		}
		var url = $("#exportExcelUrl").attr("href");
		$("#queryForm").attr("action",url);
		$("#queryForm").submit();
		return false;
	});
	
});
</script>
