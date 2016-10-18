<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRMRP05">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRMRP05_wreport"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRMRP05_memInfo"></s:text>
		</span>
	  </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryParam" class="inline">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
              	<label><s:text name="WRMRP05_date"/></label>
                <s:textfield id="startDate" name="startDate" cssClass="date"/>-<s:textfield id="endDate" name="endDate" cssClass="date"/>
              </p>
              <p>
               	<label><s:text name="WRMRP05_memCode"/></label>
               	<s:textfield name="memCode" cssClass="text"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
                <label><s:text name="WRMRP05_employeeId"/></label>
                <s:hidden name="employeeName"></s:hidden>
                <s:select list="employeeList" listKey="baInfoId" listValue="baName" name="employeeId" headerKey="" headerValue="%{#select_default}"></s:select>
              </p> 
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" id="searchMem">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
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
          <cherry:show domId="BINOLWRMRP05EXP">
		  	<a id="exportExcel" class="export" href="javascript:void(0);">
          	  <span class="ui-icon icon-export"></span>
          	  <span class="button-text"><s:text name="global.page.exportExcel"/></span>
          	</a>
          	<a id="exportCsv" class="export" href="javascript:void(0);">
          	  <span class="ui-icon icon-export"></span>
          	  <span class="button-text"><s:text name="global.page.exportCsv"/></span>
          	</a>
          	</cherry:show>
		  	<span id="headInfo"></span>
		  </div>
          <table id="resultDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="WRMRP05_number" /></th>
                <th><s:text name="WRMRP05_billCode" /></th>
                <th><s:text name="WRMRP05_saleTime" /></th>
                <th><s:text name="WRMRP05_memCode" /></th>
                <th><s:text name="WRMRP05_employeeId" /></th>
                <th><s:text name="WRMRP05_quantity" /></th>
                <th><s:text name="WRMRP05_amount" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLWRMRP05_search" id="searchUrl"></s:a>
<s:a action="BINOLWRMRP05_exportCheck" id="exportCheckUrl"></s:a>
<s:a action="BINOLWRMRP05_exportExcel" id="exportExcelUrl"></s:a>
<s:a action="BINOLWRMRP05_exportCsv" id="exportCsvUrl"></s:a>
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
	
	$("#employeeId").change(function(){
		if($(this).val() != '') {
			$("#employeeName").val($(this).find("option:selected").text());
		} else {
			$("#employeeName").val('');
		}
	});
	
	// 表单验证配置
	cherryValidate({						
		formId: 'queryParam',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
		}
	});
	
	$("#searchMem").click(function(){
		// 表单验证
		if(!$("#queryParam").valid()) {
			return false;
		}
		searchList();
		return false;
	});
	
	function searchList() {
		var url = $("#searchUrl").attr("href");
		var params = $("#queryParam").serialize();
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
				 aaSorting: [[ 2, "desc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "number", "sWidth": "5%", "bSortable": false},
				             { "sName": "billCode", "sWidth": "20%", "bSortable": false},
				             { "sName": "saleTime", "sWidth": "15%" },
				             { "sName": "memCode", "sWidth": "15%", "bSortable": false },
				             { "sName": "employeeName", "sWidth": "15%", "bSortable": false },
				             { "sName": "quantity", "sWidth": "15%", "bSortable": false},
				             { "sName": "amount", "sWidth": "15%", "bSortable": false}],
				// 横向滚动条出现的临界宽度
				sScrollX: "100%",
				fnDrawCallback: function() {
					
				},
				callbackFun: function(msg) {
			 		var $msg = $("<div></div>").html(msg);
			 		var $headInfo = $("#headInfo",$msg);
			 		if($headInfo.length > 0) {
			 			$("#headInfo").html($headInfo.html());
			 		} else {
			 			$("#headInfo").empty();
			 		}
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	}
	
	$("#exportExcel").click(function(){
		if($("#resultDataTable").find(".dataTables_empty:visible").length==0) {
			var callback = function(msg) {
        		var url = $("#exportExcelUrl").attr("href");
        		$("#queryParam").attr("action",url);
        		$("#queryParam").submit();
        	};
        	exportExcelReport({
        		url: $("#exportCheckUrl").attr("href"),
        		param: $("#queryParam").serialize(),
        		callback: callback
        	});
        }
		return false;
	});
	
	$("#exportCsv").click(function(){
		if($("#resultDataTable").find(".dataTables_empty:visible").length==0) {
			exportReport({
				exportUrl:$("#exportCsvUrl").attr("href"),
				exportParam:$("#queryParam").serialize()
			});
		}
		return false;
	});
});
</script>
