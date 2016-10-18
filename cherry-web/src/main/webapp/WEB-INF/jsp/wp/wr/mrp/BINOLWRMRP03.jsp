<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRMRP03">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRMRP03_wreport"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRMRP03_memInfo"></s:text>
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
              	<label><s:text name="WRMRP03_birthDay"/></label>
                <span>
                <select name="birthDayMode" id="birthDayMode">
					<option value=""><s:text name="global.page.select"/></option>
					<option value="0"><s:text name="global.page.curWeek"/></option>
					<option value="1"><s:text name="global.page.curMonth"/></option>
					<option value="2"><s:text name="global.page.customBirthDay"/></option>
					<option value="3"><s:text name="global.page.birthDayRange"/></option>
				</select>
                </span>
                <span class="hide">
                <select name="birthDayMonth" style="width:50px;" id="birthDayMonth">
                  <option value=""><s:text name="global.page.month" /></option>
                </select>
                <select name="birthDayDate" style="width:50px;" id="birthDayDate">
                  <option value=""><s:text name="global.page.date" /></option>
                </select>
                </span>
                <span class="hide">
                <select name="birthDayMonthRangeStart" style="width:50px;" id="birthDayMonthRangeStart">
                  <option value=""><s:text name="global.page.month" /></option>
                </select>
                <select name="birthDayDateRangeStart" style="width:50px;" id="birthDayDateRangeStart">
                  <option value=""><s:text name="global.page.date" /></option>
                </select>-
                <select name="birthDayMonthRangeEnd" style="width:50px;" id="birthDayMonthRangeEnd">
                  <option value=""><s:text name="global.page.month" /></option>
                </select>
                <select name="birthDayDateRangeEnd" style="width:50px;" id="birthDayDateRangeEnd">
                  <option value=""><s:text name="global.page.date" /></option>
                </select>
                </span>
              </p>
              <p>
               	<label><s:text name="WRMRP03_memLevel"/></label>
               	<s:hidden name="levelName"></s:hidden>
               	<s:select list="memLevelList" listKey="memberLevelId" listValue="levelName" name="memberLevel" headerKey="" headerValue="%{#select_default}"></s:select>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
                <label><s:text name="WRMRP03_employeeId"/></label>
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
          <cherry:show domId="BINOLWRMRP03EXP">
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
                <th><s:text name="WRMRP03_memCode" /></th>
                <th><s:text name="WRMRP03_mobilePhone" /></th>
                <th><s:text name="WRMRP03_memName" /></th>
                <th><s:text name="WRMRP03_gender" /></th>
                <th><s:text name="WRMRP03_birthDay" /></th>
                <th><s:text name="WRMRP03_memLevel" /></th>
                <th><s:text name="WRMRP03_changablePoint" /></th>
                <th><s:text name="WRMRP03_employeeId" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLWRMRP03_search" id="searchMemInfoUrl"></s:a>
<s:a action="BINOLWRMRP03_exportCheck" id="exportCheckUrl"></s:a>
<s:a action="BINOLWRMRP03_exportExcel" id="exportExcelUrl"></s:a>
<s:a action="BINOLWRMRP03_exportCsv" id="exportCsvUrl"></s:a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />

<script type="text/javascript">
$(function(){
	
	binolwpcom01.selectBirthDay("birthDayMonth", "birthDayDate");
	binolwpcom01.selectBirthDay("birthDayMonthRangeStart", "birthDayDateRangeStart", true);
	binolwpcom01.selectBirthDay("birthDayMonthRangeEnd", "birthDayDateRangeEnd", true);
	
	$("#birthDayMode").change(function(){
		$(this).parent().next().hide();
		$(this).parent().next().next().hide();
		if($(this).val() == "2") {
			$(this).parent().next().show();
		} else if($(this).val() == "3") {
			$(this).parent().next().next().show();
		}
	});
	
	$("#memberLevel").change(function(){
		if($(this).val() != '') {
			$("#levelName").val($(this).find("option:selected").text());
		} else {
			$("#levelName").val('');
		}
	});
	
	$("#employeeId").change(function(){
		if($(this).val() != '') {
			$("#employeeName").val($(this).find("option:selected").text());
		} else {
			$("#employeeName").val('');
		}
	});
	
	$("#searchMem").click(function(){
		searchList();
		return false;
	});
	
	function searchList() {
		var url = $("#searchMemInfoUrl").attr("href");
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
				 aaSorting: [[ 0, "asc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "memberCode", "sWidth": "8%", "bSortable": false},
				             { "sName": "mobilePhone", "sWidth": "10%", "bSortable": false },
				             { "sName": "memberName", "sWidth": "8%", "bSortable": false },
				             { "sName": "gender", "sWidth": "8%", "bSortable": false },
				             { "sName": "birthDay", "sWidth": "8%", "bSortable": false },
				             { "sName": "levelName", "sWidth": "8%", "bSortable": false},
				             { "sName": "changablePoint", "sWidth": "8%", "bSortable": false},
				             { "sName": "employeeName", "sWidth": "8%", "bSortable": false}],
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
