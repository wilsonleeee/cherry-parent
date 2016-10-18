<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>

<s:i18n name="i18n.mb.BINOLMBVIS04">
    <s:text name="global.page.select" id="select_default"/>
    <div class="panel-header">
	    <div class="clearfix"> 
	 	<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
	    </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryForm">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
		        <label><s:text name="mbvis04_visitTypeCode" /></label>
		        <s:select name="visitType" list="visitCategoryList" listKey="visitTypeCode" listValue="visitTypeName" headerKey="" headerValue="%{#select_default}"></s:select>
		        <s:hidden name="visitTypeName"></s:hidden>
		      </p>
		      <p>
		        <label><s:text name="mbvis04_visitTaskDate" /></label>
		        <s:textfield name="startDate" cssClass="date"></s:textfield>-<s:textfield name="endDate" cssClass="date"></s:textfield>
		      </p>
              <p>
               	<label><s:text name="mbvis04_memCode"/></label>
               	<s:textfield name="memCode" cssClass="text"/>
              </p>
              <p>
               	<label><s:text name="mbvis04_birthDay"/></label>
               	<s:select list="monthList" name="birthDayMonth" headerKey="" headerValue="%{#select_default}" cssStyle="width:auto;"></s:select><s:text name="mbvis04_birthDay1" />
          		<s:select list="dateList" name="birthDayDate" headerKey="" headerValue="%{#select_default}" cssStyle="width:auto;"></s:select><s:text name="mbvis04_birthDay2" />
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
               	<label><s:text name="mbvis04_counterCode"/></label>
          		<s:textfield name="counterCode" cssClass="text"/>
              </p>
              <p>
               	<label><s:text name="mbvis04_employeeCode"/></label>
          		<s:textfield name="employeeCode" cssClass="text"/>
              </p>
              <p>
		        <label><s:text name="mbvis04_visitTime" /></label>
		        <s:textfield name="visitTimeStart" cssClass="date"></s:textfield>-<s:textfield name="visitTimeEnd" cssClass="date"></s:textfield>
		      </p>
		      <p>
		        <label><s:text name="mbvis04_visitResult" /></label>
		        <s:select list='#application.CodeTable.getCodes("1209")' listKey="CodeKey" listValue="Value" name="visitResult" headerKey="" headerValue="%{#select_default}"></s:select>
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
      <div id="resultDiv" class="section hide">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="global.page.list"/>
        	</strong>
        </div>
        <div class="section-content">
          <div class="toolbar clearfix">
          	<a id="cancelTask" class="delete" href="javascript:void(0);">
              <span class="ui-icon icon-disable"></span>
              <span class="button-text"><s:text name="取消"/></span>
          	</a>    
		  	<a id="exportExcel" class="export" href="javascript:void(0);">
          	  <span class="ui-icon icon-export"></span>
          	  <span class="button-text"><s:text name="global.page.exportExcel"/></span>
          	</a>
          	<a id="exportCsv" class="export" href="javascript:void(0);">
          	  <span class="ui-icon icon-export"></span>
          	  <span class="button-text"><s:text name="global.page.exportCsv"/></span>
          	</a>
		  </div>
          <table id="resultTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>
                <th><input type="checkbox" name="checkAll" id="checkAll" /></th>
                <th><s:text name="mbvis04_visitTypeCode"/></th>
                <th><s:text name="mbvis04_memCode"/></th>
                <th><s:text name="mbvis04_memName"/></th>
                <th><s:text name="mbvis04_birthDay"/></th>
                <th><s:text name="mbvis04_counterCode"/></th>
                <th><s:text name="mbvis04_employeeCode"/></th>
                <th><s:text name="mbvis04_startDate"/></th>
                <th><s:text name="mbvis04_endDate"/></th>
                <th><s:text name="mbvis04_visitResult"/></th>
                <th><s:text name="mbvis04_visitTime"/></th>
                <th><s:text name="mbvis04_taskState"/></th>
                <th><s:text name="mbvis04_synchroFlag"/></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div>
    
<div class="hide">
<s:a action="BINOLMBVIS04_search" id="searchUrl"></s:a>
<s:a action="BINOLMBVIS04_cancel" id="cancelUrl"></s:a>
<s:a action="BINOLMBVIS04_exportCheck" id="exportCheckUrl"></s:a>
<s:a action="BINOLMBVIS04_exportExcel" id="exportExcelUrl"></s:a>
<s:a action="BINOLMBVIS04_exportCsv" id="exportCsvUrl"></s:a>

<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<div id="dialogClose"><s:text name="global.page.dialogClose" /></div>
<div id="dialogTitle"><s:text name="mbvis04_dialogTitle" /></div>
<div id="dialogText"><s:text name="mbvis04_dialogText" /></div>
</div>    
</s:i18n>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />

<script type="text/javascript">
$(function(){
	$('#startDate').cherryDate();
	$('#endDate').cherryDate({
		beforeShow: function(input){
			var value = $('#startDate').val();
			return [value,'minDate'];
		}
	});
	
	$('#visitTimeStart').cherryDate();
	$('#visitTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#visitTimeStart').val();
			return [value,'minDate'];
		}
	});
	
	employeeBinding({elementId:"employeeCode",showNum:20,selected:"code"});
	counterBinding({elementId:"counterCode",showNum:20,selected:"code"});
	
	$("#visitType").change(function(){
		if($(this).val() != '') {
			$("#visitTypeName").val($(this).find("option:selected").text());
		} else {
			$("#visitTypeName").val('');
		}
	});
	
	
	$("#searchButton").click(function(){
		search();
		return false;
	});
	
	function search() {
		var url = $("#searchUrl").attr("href");
		var params = $("#queryForm").serialize();
		url = url + "?" + params;

		$("#resultDiv").show();
		// 表格设置
		tableSetting = {
			// 表格ID
			tableId : '#resultTable',
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [[1, "asc"]],
			// 表格列属性设置
			aoColumns : [	
							{ "sName": "checkAll", "sWidth": "5%", "bSortable": false},
	                    	{ "sName": "visitType", "sWidth": "8%"},
	                    	{ "sName": "memCode", "sWidth": "8%"},
	                    	{ "sName": "memName", "sWidth": "8%", "bSortable": false},
	                    	{ "sName": "birthDay", "sWidth": "8%", "bSortable": false},
	                    	{ "sName": "counterCode", "sWidth": "8%"},
	                    	{ "sName": "employeeCode", "sWidth": "8%"},
	                    	{ "sName": "startTime", "sWidth": "8%"},
	                    	{ "sName": "endTime", "sWidth": "8%"},
	                    	{ "sName": "visitFlag", "sWidth": "7%"},
	                    	{ "sName": "visitTime", "sWidth": "10%"},
	                    	{ "sName": "taskState", "sWidth": "7%"},
	                    	{ "sName": "synchroFlag", "sWidth": "7%"}
 						],
			sScrollX : "100%",
			fnDrawCallback: function() {
				$("#resultDiv").find(":checkbox").click(function(){
					var $id = $("#resultTable");
					if($(this).attr('id') == "checkAll") {
						if(this.checked) {
							$id.find(':checkbox').prop("checked",true);
						} else {
							$id.find(':checkbox').prop("checked",false);
						}
					} else {
						if($id.find(':checkbox:not([checked])').length == 0) {
							$id.parent().prev().find('#checkAll').prop("checked",true);
						} else {
							$id.parent().prev().find('#checkAll').prop("checked",false);
						}
					}
				});
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	}
	
	$("#cancelTask").click(function(){
		if($("#cancelDialog").length == 0) {
			$("body").append('<div style="display:none" id="cancelDialog"></div>');
		} else {
			$("#cancelDialog").empty();
		}
		var url = $("#cancelUrl").attr("href");
		var param = $('#resultTable :input[checked]').serialize();
		if(!param) {
			return false;
		}
	    var dialogSetting = {
	    		dialogInit: "#cancelDialog",
	    		text: $("#dialogText").text(),
	    		width: 400,
	    		height: 200,
	    		title: $("#dialogTitle").text(),
	    		confirm: $("#dialogConfirm").text(),
	    		cancel: $("#dialogCancel").text(),
				confirmEvent: function(){
					var callback = function(msg) {
		        		if(msg) {
		        			var json = eval("("+msg+")");
		        			if(json.code == 'ok') {
		        				removeDialog("#cancelDialog");
		        				if(oTableArr[0] != null)oTableArr[0].fnDraw();
		        			} else {
		        				$("#cancelDialog").html(json.errorMes);
		        				$("#cancelDialog").dialog( "option", {
		        					buttons: [{
		        						text: $("#dialogClose").text(),
		        					    click: function(){removeDialog("#cancelDialog");}
		        					}]
		        				});
		        			}
		        		}
		        	};
		        	cherryAjaxRequest({
		        		url: url,
		        		param: param,
		        		callback: callback
		        	});
				},
				cancelEvent: function(){removeDialog("#cancelDialog");}
		};
	    openDialog(dialogSetting);
		return false;
	});
	
	$("#exportExcel").click(function(){
		if($("#resultTable").find(".dataTables_empty:visible").length==0) {
			var callback = function(msg) {
        		var url = $("#exportExcelUrl").attr("href");
        		$("#queryForm").attr("action",url);
        		$("#queryForm").submit();
        	};
        	exportExcelReport({
        		url: $("#exportCheckUrl").attr("href"),
        		param: $("#queryForm").serialize(),
        		callback: callback
        	});
        }
		return false;
	});
	
	$("#exportCsv").click(function(){
		if($("#resultTable").find(".dataTables_empty:visible").length==0) {
			exportReport({
				exportUrl:$("#exportCsvUrl").attr("href"),
				exportParam:$("#queryForm").serialize()
			});
		}
		return false;
	});
	
});

</script>
