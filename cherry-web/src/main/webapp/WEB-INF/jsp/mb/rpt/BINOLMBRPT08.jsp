<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>

<s:i18n name="i18n.mb.BINOLMBRPT08">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="mbrpt08_memtitle"></s:text>&nbsp;&gt;&nbsp;<s:text name="mbrpt08_title"></s:text>
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
              	<label><s:text name="mbrpt08_campaignCode"/></label>
				<s:select list="campaignList" listKey="campaignCode" listValue="campaignName" name="campaignCode"></s:select>
              	<s:hidden name="campaignName"></s:hidden>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
                <label><s:text name="mbrpt08_dateRange"/></label>
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
     
      <div class="section">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="global.page.list"/>
        	</strong>
        </div>
        <div class="section-content">
          <div id="camCountInfoDiv"></div>
        </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLMBRPT08_search" id="searchUrl"></s:a>
<s:a action="BINOLMBRPT08_exportExcel" id="exportExcelUrl"></s:a>
<div id="table_sProcessing"><s:text name="table_sProcessing"/></div>
</div>

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
		var param = $("#queryForm").serialize();
		var callback = function(msg) {
			$("#camCountInfoDiv").html(msg);
		};
		$("#camCountInfoDiv").html($("#table_sProcessing").html());
		cherryAjaxRequest({
    		url: url,
    		param: param,
    		callback: callback,
    		isResultHandle: false
    	});
	}
	
	$("#exportExcel").click(function() {
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
