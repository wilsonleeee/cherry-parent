<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRSRP03">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRSRP03_wrtitle"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRSRP03_title"></s:text>
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
               	<label><s:text name="WRSRP03_saleDate"/></label>
               	<s:textfield id="startDate" name="startDate" cssClass="date"/>-<s:textfield id="endDate" name="endDate" cssClass="date"/>
              </p>
              <p>
               	<label><s:text name="WRSRP03_saleType"/></label>
               	<s:select name="saleType" list="#application.CodeTable.getCodes('1055')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
               	<label><s:text name="WRSRP03_employeeId"/></label>
                <s:hidden name="employeeName"></s:hidden>
                <s:select list="employeeList" listKey="baInfoId" listValue="baName" name="employeeId" headerKey="" headerValue="%{#select_default}"></s:select>
              </p>
              <p>
               	<label><s:text name="WRSRP03_bigClassId"/></label>
                <s:hidden name="bigClassName"></s:hidden>
                <s:select list="classList" listKey="bigClassId" listValue="bigClassName" name="bigClassId" headerKey="" headerValue="%{#select_default}"></s:select>
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
      <div id="resultList"></div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLWRSRP03_search" id="searchSaleListUrl"></s:a>
<s:a action="BINOLWRSRP03_exportExcel" id="exportExcelUrl"></s:a>
<s:a action="BINOLWRSRP03_exportCsv" id="exportCsvUrl"></s:a>
</div>

<div class="hide">
<div id="loading"><s:text name="global.page.loading" /></div>
<s:hidden name="classJson"></s:hidden>
</div>

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
		var url = $("#searchSaleListUrl").attr("href");
		var params = $("#queryParam").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		$("#resultList").html($("#loading").html());
		var callback = function(msg) {
			$("#resultList").html(msg);
			
			$("#exportExcel").click(function(){
				var url = $("#exportExcelUrl").attr("href");
        		$("#queryParam").attr("action",url);
        		$("#queryParam").submit();
				return false;
			});
			
			$("#exportCsv").click(function(){
				exportReport({
					exportUrl:$("#exportCsvUrl").attr("href"),
					exportParam:$("#queryParam").serialize()
				});
				return false;
			});
		};
		cherryAjaxRequest({
    		url: url,
    		param: null,
    		callback: callback
    	});
	}
});
</script>
