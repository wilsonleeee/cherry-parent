<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRMRP02">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRMRP02_wreport"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRMRP02_memInfo"></s:text>
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
              	<label><s:text name="WRMRP02_joinDate"/></label>
                <span>
				<select name="joinDateMode" id="joinDateMode">
					<option value=""><s:text name="global.page.select"/></option>
					<option value="0"><s:text name="global.page.curJoinDate"/></option>
					<option value="1"><s:text name="global.page.dynamic"/></option>
					<option value="2"><s:text name="global.page.custom"/></option>
				</select>
				</span>
				<span class="hide">
				<s:text name="global.page.joinDateDes" />
				<s:textfield name="joinDateRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
				<s:select list='#application.CodeTable.getCodes("1239")' listKey="CodeKey" listValue="Value" name="joinDateUnit" cssStyle="width:auto;"></s:select>
				<select name="joinDateUnitFlag" style="width:auto;">
					<option value="1"><s:text name="global.page.joinDateUnitFlag1" /></option>
					<option value="2"><s:text name="global.page.joinDateUnitFlag2" /></option>
				</select>
				</span>
				<span class="hide">
				<span><s:textfield name="joinDateStart" cssClass="date"/></span><span>-<s:textfield name="joinDateEnd" cssClass="date"/></span>
				</span>
              </p>
              <p>
               	<label><s:text name="WRMRP02_memLevel"/></label>
               	<s:hidden name="levelName"></s:hidden>
               	<s:select list="memLevelList" listKey="memberLevelId" listValue="levelName" name="memberLevel" headerKey="" headerValue="%{#select_default}"></s:select>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
                <label><s:text name="WRMRP02_employeeId"/></label>
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
          <cherry:show domId="BINOLWRMRP02EXP">
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
                <th><s:text name="WRMRP02_memCode" /></th>
                <th><s:text name="WRMRP02_mobilePhone" /></th>
                <th><s:text name="WRMRP02_memName" /></th>
                <th><s:text name="WRMRP02_gender" /></th>
                <th><s:text name="WRMRP02_joinDate" /></th>
                <th><s:text name="WRMRP02_memLevel" /></th>
                <th><s:text name="WRMRP02_changablePoint" /></th>
                <th><s:text name="WRMRP02_employeeId" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLWRMRP02_search" id="searchMemInfoUrl"></s:a>
<s:a action="BINOLWRMRP02_exportCheck" id="exportCheckUrl"></s:a>
<s:a action="BINOLWRMRP02_exportExcel" id="exportExcelUrl"></s:a>
<s:a action="BINOLWRMRP02_exportCsv" id="exportCsvUrl"></s:a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />

<script type="text/javascript">
$(function(){
	
	$('#joinDateStart').cherryDate({
		beforeShow: function(input){
			var value = $('#joinDateEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#joinDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#joinDateStart').val();
			return [value,'minDate'];
		}
	});
	
	$("#joinDateMode").change(function(){
		$(this).parent().next().hide();
		$(this).parent().next().next().hide();
		if($(this).val() == "1") {
			$(this).parent().next().show();
		} else if($(this).val() == "2") {
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
				 aaSorting: [[ 4, "desc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "memberCode", "sWidth": "15%", "bSortable": false},
				             { "sName": "mobilePhone", "sWidth": "10%", "bSortable": false },
				             { "sName": "memberName", "sWidth": "15%", "bSortable": false },
				             { "sName": "gender", "sWidth": "10%", "bSortable": false },
				             { "sName": "joinDate", "sWidth": "15%" },
				             { "sName": "levelName", "sWidth": "10%", "bSortable": false},
				             { "sName": "changablePoint", "sWidth": "10%", "bSortable": false},
				             { "sName": "employeeName", "sWidth": "15%", "bSortable": false}],
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
