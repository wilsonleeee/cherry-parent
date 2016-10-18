<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRSRP07">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRSRP07_wreport"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRSRP07_saleInfo"></s:text>
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
               	<label><s:text name="WRSRP07_saleDate"/></label>
               	<s:textfield name="saleDateStart" cssClass="date"/>-<s:textfield name="saleDateEnd" cssClass="date"/>
              </p>
            </div>
            <%-- <div class="column last" style="width:49%; height: auto;">
              <p>
               	<label><s:text name="WRSRP07_bigClassId"/></label>
                <s:hidden name="bigClassName"></s:hidden>
                <s:select list="classList" listKey="bigClassId" listValue="bigClassName" name="bigClassId" headerKey="" headerValue="%{#select_default}"></s:select>
              </p>
            </div> --%>
          </div>
          <p class="clearfix">
         	<button class="right search" id="searchButton">
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
          <cherry:show domId="BINOLWRSRP07EXP">
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
          <table id="resultStoreRankingDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="WRSRP07_number" /></th>
                <th><s:text name="WRSRP07_counterCode" /></th>
                <th><s:text name="WRSRP07_counterNameIF" /></th>
                <th><s:text name="WRSRP07_joinRate" /></th>
                <th><s:text name="WRSRP07_memCount" /></th>
                <th><s:text name="WRSRP07_quantity" /></th>
                <th><s:text name="WRSRP07_amount" /></th>
                <th><s:text name="WRSRP07_ranking" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLWRSRP07_search" id="searchUrl"></s:a>
<s:a action="BINOLWRSRP07_exportExcel" id="exportExcelUrl"></s:a>
<s:a action="BINOLWRSRP07_exportCsv" id="exportCsvUrl"></s:a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />

<script type="text/javascript">
$(function(){
	
	$('#saleDateStart').cherryDate({
		beforeShow: function(input){
			var value = $('#saleDateEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#saleDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#saleDateStart').val();
			return [value,'minDate'];
		}
	});

	$("#bigClassId").change(function(){
		if($(this).val() != '') {
			$("#bigClassName").val($(this).find("option:selected").text());
		} else {
			$("#bigClassName").val('');
		}
	});
	
	// 表单验证配置
	cherryValidate({						
		formId: 'queryParam',
		rules: {
			saleDateStart: {dateValid: true},
			saleDateEnd: {dateValid: true}
		}
	});
	
	$("#searchButton").click(function(){
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
				 tableId: '#resultStoreRankingDataTable',
				 // 数据URL
				 url: url,
				 // 表格默认排序
				 aaSorting: [[ 7, "asc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "number", "sWidth": "5%", "bSortable": false},
				             { "sName": "counterCode", "sWidth": "15%", "bSortable": false },
				             { "sName": "counterNameIF", "sWidth": "15%", "bSortable": false },
				             { "sName": "joinRate", "sWidth": "10%", "bSortable": false },
				             { "sName": "memCount", "sWidth": "15%", "bSortable": false },
				             { "sName": "quantity", "sWidth": "15%", "bSortable": false },
				             { "sName": "amount", "sWidth": "15%", "bSortable": false},
				             { "sName": "ranking", "sWidth": "10%", "bSortable": false}],
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
		if($("#resultStoreRankingDataTable").find(".dataTables_empty:visible").length==0) {
			var url = $("#exportExcelUrl").attr("href");
    		$("#queryParam").attr("action",url);
    		$("#queryParam").submit();
        }
		return false;
	});
	
	$("#exportCsv").click(function(){
		if($("#resultStoreRankingDataTable").find(".dataTables_empty:visible").length==0) {
			exportReport({
				exportUrl:$("#exportCsvUrl").attr("href"),
				exportParam:$("#queryParam").serialize()
			});
		}
		return false;
	});
});
</script>
