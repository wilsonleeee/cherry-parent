<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWOSET02">
<s:text name="global.page.select" id="select_default"></s:text>    
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WOSET02_wotitle"></s:text>&nbsp;&gt;&nbsp;<s:text name="WOSET02_title"></s:text>
		</span>
	  </div>
    </div>
    <div class="panel-content">
      <div class="section">
        <div class="section-header">
          	<strong>
        		<s:text name="WOSET02_title"/>
        	</strong>
        </div>
        <div class="section-content">
        	<label><s:text name="WOSET02_employeeId" /></label>
        	<s:select list="employeeList" listKey="baInfoId" listValue="baName" name="employeeId" headerKey="" headerValue="%{#select_default}"></s:select>
        	<a class="add attendanceButton" href="javascript:void(0);" id="attendanceType1">
	    	  <span class="ui-icon icon-add"></span>
	    	  <span class="button-text"><s:text name="WOSET02_attendanceType1"></s:text></span>
	      	</a>
	      	<a class="add attendanceButton" href="javascript:void(0);" id="attendanceType0">
	    	  <span class="ui-icon icon-delete"></span>
	    	  <span class="button-text"><s:text name="WOSET02_attendanceType0"></s:text></span>
	      	</a>
        </div>
      </div>

      <div class="section">
        <div class="section-header">
        	<strong>
        		<s:text name="WOSET02_attendanceList"/>
        	</strong>
        </div>
        <div class="section-content">
        	<cherry:form id="queryParam" class="inline">
        	<label><s:text name="WOSET02_attendanceDateTime" /></label>
        	<span><s:textfield name="startDate" cssClass="date"/></span>-<span><s:textfield name="endDate" cssClass="date"/></span>
        	<label style="margin-left: 20px;"><s:text name="WOSET02_employeeId" /></label>
        	<s:select list="employeeList" listKey="baInfoId" listValue="baName" name="employeeIdQ" headerKey="" headerValue="%{#select_default}"></s:select>
        	<a class="add" href="javascript:void(0);" id="searchButton">
	    	  <span class="ui-icon icon-search"></span>
	    	  <span class="button-text"><s:text name="global.page.search"></s:text></span>
	      	</a>
	      	</cherry:form>
        </div>
        <div class="section-content">
          <table id="resultDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="WOSET02_number" /></th>
                <th><s:text name="WOSET02_employeeCode" /></th>
                <th><s:text name="WOSET02_employeeName" /></th>
                <th><s:text name="WOSET02_attendanceDateTime" /></th>
                <th><s:text name="WOSET02_attendanceType" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      
    </div>
    
<div class="hide">
<div id="loading"><s:text name="global.page.loading" /></div>
<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<div id="dialogClose"><s:text name="global.page.dialogClose" /></div>
<div id="attendanceError1"><s:text name="WOSET02_attendanceError1" /></div>
</div>   
</s:i18n>

<div class="hide">
<s:a action="BINOLWOSET02_search" id="searchUrl"></s:a>
<s:a action="BINOLWOSET02_add" id="addUrl"></s:a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

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
	
	// 表单验证配置
	cherryValidate({						
		formId: 'queryParam',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
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
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId: '#resultDataTable',
				 // 数据URL
				 url: url,
				 // 表格默认排序
				 aaSorting: [[ 3, "desc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "number", "sWidth": "5%", "bSortable": false},
				             { "sName": "employeeCode", "sWidth": "20%", "bSortable": false },
				             { "sName": "employeeName", "sWidth": "20%", "bSortable": false },
				             { "sName": "attendanceDateTime", "sWidth": "40%"},
				             { "sName": "attendanceType", "sWidth": "15%"}],
				// 横向滚动条出现的临界宽度
				sScrollX: "100%",
				iDisplayLength: 25,
				fnDrawCallback: function() {
					
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	}
	
	$(".attendanceButton").click(function(){
		if($("#employeeId").val() == '') {
			if($("#addDialog").length == 0) {
				$("body").append('<div style="display:none" id="addDialog"></div>');
			} else {
				$("#addDialog").empty();
			}
		    var dialogSetting = {
		    		dialogInit: "#addDialog",
		    		text: $("#attendanceError1").html(),
		    		width: 400,
		    		height: 200,
		    		title: '',
		    		confirm: $("#dialogClose").text(),
					confirmEvent: function(){
						removeDialog("#addDialog");
					},
					open: function(){
						$(this).bind("keypress.ui-dialog", function(event) { 
							if (event.keyCode == $.ui.keyCode.ENTER) {
								$(".ui-dialog-buttonpane button").first().click();
								return false;
							} 
						});
					}
			};
		    openDialog(dialogSetting);
			return false;
		}
		var id = $(this).attr("id");
		var addUrl = $("#addUrl").attr("href") + "?" + $("#employeeId").serialize();
		if(id == 'attendanceType1') {
			addUrl += '&attendanceType=1';
		} else {
			addUrl += '&attendanceType=0';
		}
		addAttendance(addUrl);
		return false;
	});
	
	function addAttendance(addUrl) {
		var callback = function(msg) {
			var json = eval("("+msg+")");
			if($("#addDialog").length == 0) {
				$("body").append('<div style="display:none" id="addDialog"></div>');
			} else {
				$("#addDialog").empty();
			}
		    var dialogSetting = {
		    		dialogInit: "#addDialog",
		    		text: json.errorMes,
		    		width: 400,
		    		height: 200,
		    		title: '',
		    		confirm: $("#dialogClose").text(),
					confirmEvent: function(){
						removeDialog("#addDialog");
						if(json.code == 'ok') {
	        				if(oTableArr[0] != null)oTableArr[0].fnDraw();
	        			}
					},
					open: function(){
						$(this).bind("keypress.ui-dialog", function(event) { 
							if (event.keyCode == $.ui.keyCode.ENTER) {
								$(".ui-dialog-buttonpane button").first().click();
								return false;
							} 
						});
					}
			};
		    openDialog(dialogSetting);
		};
		cherryAjaxRequest({
			url: addUrl,
			param: null,
			callback: callback
		});
	}
	
	$("#searchButton").click();
});
</script>
