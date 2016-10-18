<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRSRP06">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRSRP06_wreport"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRSRP06_saleInfo"></s:text>
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
               	<label><s:text name="WRSRP06_saleDate"/></label>
               	<s:select list="yearList" name="year" cssStyle="width:auto;"></s:select>
  				<s:select list="monthList" name="month" cssStyle="width:auto;"></s:select>
              </p>
              <p>
               	<label><s:text name="WRSRP06_saleType"/></label>
               	<s:select name="saleType" list="#application.CodeTable.getCodes('1055')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
              </p>
              <p>
               	<label><s:text name="WRSRP06_channel"/></label>
               	<s:select name="channel" list="#application.CodeTable.getCodes('1011')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
                <label><s:text name="WRSRP06_employeeId"/></label>
                <s:hidden name="employeeName"></s:hidden>
                <s:select list="employeeList" listKey="baInfoId" listValue="baName" name="employeeId" headerKey="" headerValue="%{#select_default}"></s:select>
              </p>
              <p>
               	<label><s:text name="WRSRP06_payTypeCode"/></label>
               	<s:select name="payTypeCode" list="#application.CodeTable.getCodes('1175')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
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
<s:a action="BINOLWRSRP06_search" id="searchUrl"></s:a>
<s:a action="BINOLWRSRP06_exportExcel" id="exportExcelUrl"></s:a>
<s:a action="BINOLWRSRP06_exportCsv" id="exportCsvUrl"></s:a>
</div>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<div class="hide">
<div id="loading"><s:text name="global.page.loading" /></div>
</div>

<script type="text/javascript">
$(function(){
	
	$("#employeeId").change(function(){
		if($(this).val() != '') {
			$("#employeeName").val($(this).find("option:selected").text());
		} else {
			$("#employeeName").val('');
		}
	});
	
	$("#searchButton").click(function(){
		searchList();
		return false;
	});
	
	function searchList() {
		var url = $("#searchUrl").attr("href");
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
