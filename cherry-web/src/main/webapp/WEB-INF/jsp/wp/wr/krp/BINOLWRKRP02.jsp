<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRKRP02">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRKRP02_wrtitle"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRKRP02_title"></s:text>
		</span>
	  </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryParam" class="inline">
          <s:hidden name="type"></s:hidden>
          <s:hidden name="params"></s:hidden>
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
               	<label><s:text name="WRKRP02_date"/></label>
               	<s:textfield id="startDate" name="startDate" cssClass="date"/>-<s:textfield id="endDate" name="endDate" cssClass="date"/>
              </p>
              <p>
               	<label><s:text name="WRKRP02_bigClassId"/></label>
                <s:hidden name="bigClassName"></s:hidden>
                <s:select list="classList" listKey="bigClassId" listValue="bigClassName" name="catePropValId" headerKey="" headerValue="%{#select_default}"></s:select>
              </p>
              <p>
                <label><s:text name="WRKRP02_logicInventoryInfoId"/></label>
               	<s:hidden name="logicInventoryName"></s:hidden>
               	<s:select list="logicInventoryList" listKey="logicInventoryInfoId" listValue="logicInventoryName" name="lgcInventoryId" headerKey="" headerValue="%{#select_default}"></s:select>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
	            <label><s:text name="WRKRP02_productName"/></label>
              	<s:textfield name="nameTotal" cssClass="text"/>
              	<s:if test='"2".equals(type)'><input type="hidden" id="productId" name="productId" value=""/></s:if>
              	<s:else><input type="hidden" id="prtVendorId" name="prtVendorId" value=""/></s:else>
	          </p>
              <p>
               	<label><s:text name="WRKRP02_validFlag"/></label>
               	<s:iterator value='#application.CodeTable.getCodes("1137")'>
                	<input type="radio" name="validFlag" value='<s:property value="CodeKey" />' <s:if test="1 == CodeKey">checked</s:if>/><s:property value="Value" />
                </s:iterator>
              </p>
            </div>
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
          <cherry:show domId="BINOLWRKRP02EXP">
		  	<a id="exportExcel02" class="export" href="javascript:void(0);">
          	  <span class="ui-icon icon-export"></span>
          	  <span class="button-text"><s:text name="global.page.exportExcel02"/></span>
          	</a>
          	<a id="exportCsv" class="export" href="javascript:void(0);">
          	  <span class="ui-icon icon-export"></span>
          	  <span class="button-text"><s:text name="global.page.exportCsv"/></span>
          	</a>
          	<a id="exportExcel01" class="export" href="javascript:void(0);">
              <span class="ui-icon icon-export"></span>
              <span class="button-text"><s:text name="global.page.exportExcel01"/></span>
            </a>
            </cherry:show>
		  	<span id="headInfo"></span>
		  </div>
          <table id="resultDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="WRKRP02_number" /></th>
                <th><s:text name="WRKRP02_productName" /></th>
                <th><s:text name="WRKRP02_unitCode" /></th>
                <s:if test='!"2".equals(type)'><th><s:text name="WRKRP02_barCode" /></th></s:if>
                <th><s:text name="WRKRP02_moduleCode" /></th>
                <th><s:text name="WRKRP02_salePrice" /></th>
                <th><s:text name="WRKRP02_startQuantity" /></th>
                <th><s:text name="WRKRP02_startAmount" /></th>
                <th><s:text name="WRKRP02_inQuantity" /></th>
                <th><s:text name="WRKRP02_outQuantity" /></th>
                <th><s:text name="WRKRP02_endQuantity" /></th>
                <th><s:text name="WRKRP02_endAmount" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLWRKRP02_search" id="searchUrl"></s:a>
<s:a action="BINOLPTRPS11_exportCheck" namespace="/pt" id="exportCheckUrl"></s:a>
<s:a action="BINOLPTRPS11_export" namespace="/pt" id="exportExcelUrl"></s:a>
<s:a action="BINOLPTRPS11_exportSummary" namespace="/pt" id="exportSummaryUrl"></s:a>
<s:a action="BINOLPTRPS11_exportCsv" namespace="/pt" id="exportCsvUrl"></s:a>
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
	
	$("#catePropValId").change(function(){
		if($(this).val() != '') {
			$("#bigClassName").val($(this).find("option:selected").text());
		} else {
			$("#bigClassName").val('');
		}
	});
	
	$("#lgcInventoryId").change(function(){
		if($(this).val() != '') {
			$("#logicInventoryName").val($(this).find("option:selected").text());
		} else {
			$("#logicInventoryName").val('');
		}
	});
	
	if($('#productId').length > 0){
		// 产品选择绑定
		productBinding({elementId:"nameTotal",showNum:20,targetId:'productId',validFlag:'validFlag'});
	}else{
		// 产品选择绑定
		productBinding({elementId:"nameTotal",showNum:20,validFlag:'validFlag'});
	}
	
	// 表单验证配置
	cherryValidate({						
		formId: 'queryParam',
		rules: {
			startDate: {required: true,dateValid: true},
			endDate: {required: true,dateValid: true}
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
		
		var aoColumns = null;
		var $type = $('#type');
		if($type.val() == '1') {
			aoColumns = [{ "sName": "number", "sWidth": "5%", "bSortable": false},
			             { "sName": "nameTotal", "sWidth": "10%", "bSortable": false },
			             { "sName": "unitCode", "sWidth": "10%" },
			             { "sName": "barCode", "sWidth": "10%" },
			             { "sName": "moduleCode", "sWidth": "9%", "bSortable": false },
			             { "sName": "price", "sWidth": "8%", "bSortable": false },
			             { "sName": "startQuantity", "sWidth": "8%", "bSortable": false},
			             { "sName": "startAmount", "sWidth": "8%", "bSortable": false},
			             { "sName": "inQuantity", "sWidth": "8%", "bSortable": false},
			             { "sName": "outQuantity", "sWidth": "8%", "bSortable": false},
			             { "sName": "endQuantity", "sWidth": "8%", "bSortable": false},
			             { "sName": "endAmount", "sWidth": "8%", "bSortable": false}];
		} else {
			aoColumns = [{ "sName": "number", "sWidth": "5%", "bSortable": false},
			             { "sName": "nameTotal", "sWidth": "10%", "bSortable": false },
			             { "sName": "unitCode", "sWidth": "10%" },
			             { "sName": "moduleCode", "sWidth": "9%", "bSortable": false },
			             { "sName": "price", "sWidth": "8%", "bSortable": false },
			             { "sName": "startQuantity", "sWidth": "8%", "bSortable": false},
			             { "sName": "startAmount", "sWidth": "8%", "bSortable": false},
			             { "sName": "inQuantity", "sWidth": "8%", "bSortable": false},
			             { "sName": "outQuantity", "sWidth": "8%", "bSortable": false},
			             { "sName": "endQuantity", "sWidth": "8%", "bSortable": false},
			             { "sName": "endAmount", "sWidth": "8%", "bSortable": false}];
		}
		
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId: '#resultDataTable',
				 // 数据URL
				 url: url,
				 // 表格默认排序
				 aaSorting: [[ 2, "desc" ]],
				 // 表格列属性设置
				 aoColumns: aoColumns,
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
	
	$("#exportExcel02").click(function(){
		// 表单验证
		if(!$("#queryParam").valid()) {
			return false;
		}
		if($("#resultDataTable").find(".dataTables_empty:visible").length==0) {
			var callback = function(msg) {
        		var url = $("#exportExcelUrl").attr("href");
        		$("#queryParam").attr("action",url);
        		$("#queryParam").submit();
        	};
        	var url = $("#exportCheckUrl").attr("href");
        	exportExcelReport({
        		url: url,
        		param: $("#queryParam").serialize(),
        		callback: callback
        	});
        }
		return false;
	});
	
	$("#exportExcel01").click(function(){
		// 表单验证
		if(!$("#queryParam").valid()) {
			return false;
		}
		var url = $("#exportSummaryUrl").attr("href");
		$("#queryParam").attr("action",url);
		$("#queryParam").submit();
		return false;
	});
	
	$("#exportCsv").click(function(){
		// 表单验证
		if(!$("#queryParam").valid()) {
			return false;
		}
		if($("#resultDataTable").find(".dataTables_empty:visible").length==0) {
			var url = $("#exportCsvUrl").attr("href");
			exportReport({
				exportUrl:url,
				exportParam:$("#queryParam").serialize()
			});
		}
		return false;
	});
});
</script>
