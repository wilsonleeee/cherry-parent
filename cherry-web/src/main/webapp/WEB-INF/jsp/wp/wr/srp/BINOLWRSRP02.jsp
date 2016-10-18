<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/common/LodopFuncs.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/wr/srp/BINOLWRSRP02.js?V=20160913"></script>

<s:i18n name="i18n.wp.BINOLWRSRP02">
<div class="hide">
	<s:url id="s_getPrintALLUrl" value="/wr/BINOLWRSRP02_getPrintALL" />
		<a id="getPrintALLUrl" href="${s_getPrintALLUrl}"></a>
</div>
<div class="hide" id="printAllHTML">
		<div>
			<p style="margin:0;"><span id="startTimePrint"></span>到<span id="endTimePrint"></span>销售汇总</p>
			<p style="margin:0;">AVON</p>
			<p style="margin:0;" id="counterNamePrint"></p>
			<p style="margin:0;">营业员：<span id="employeeNamePrint"></span></p>
		</div>
		<table id="detailPrint" border="0" width="100%" height="16" style="border:solid 0px black;border-collapse:collapse;">
			<tr>
				<td width="60%" height="10" class="center" style="border:solid 0px black"><font size="2px">销售时间</font></td>
				<td width="40%" height="10" class="center" style="border:solid 0px black"><font size="2px">金额</font></td>
			</tr> 
		</table>
		<div>
			<p style="margin:0;">总计（元）：<span id="saleAmountPrint"></span></p>
		</div>
</div>
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRSRP02_wreport"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRSRP02_saleInfo"></s:text>
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
               	<label><s:text name="WRSRP02_billCode"/></label>
               	<s:textfield name="billCode" cssClass="text"/>
              </p>
              <p>
               	<label><s:text name="WRSRP02_saleRecordCode"/></label>
               	<s:textfield name="saleRecordCode" cssClass="text"/>
              </p>
              <p>
                <label><s:text name="WRSRP02_employeeId"/></label>
                <s:hidden name="employeeName"></s:hidden>
                <s:select list="employeeList" listKey="baInfoId" listValue="baName" name="employeeId" headerKey="" headerValue="%{#select_default}"></s:select>
              </p>
              <p>
               	<label><s:text name="WRSRP02_campaignCode"/></label>
               	<s:hidden name="campaignMode"></s:hidden>
		        <s:hidden name="campaignCode"></s:hidden>
		        <s:hidden name="campaignName"></s:hidden>
		        <span id="campaignDiv" style="line-height: 18px;"></span>
		        <a class="add" id="selectCampaign" href="javascript:void(0);">
				  <span class="ui-icon icon-search"></span>
				  <span class="button-text"><s:text name="global.page.Popselect" /></span>
				</a>
              </p>
              <p>
               	<label><s:text name="WRSRP02_memCode"/></label>
               	<s:textfield name="memCode" cssClass="text"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
               	<label><s:text name="WRSRP02_saleDate"/></label>
               	<s:textfield id="startDate" name="startDate" cssClass="date"/>-<s:textfield id="endDate" name="endDate" cssClass="date"/>
              </p>
              <p>
               	<label><s:text name="WRSRP02_saleType"/></label>
               	<s:select name="saleType" list="#application.CodeTable.getCodes('1055')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
              </p>
              <p>
               	<label><s:text name="WRSRP02_consumerType"/></label>
               	<s:select name="consumerType" list="#application.CodeTable.getCodes('1105')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
              </p>
              <p>
               	<label><s:text name="WRSRP02_payTypeCode"/></label>
               	<s:select name="payTypeCode" list="#application.CodeTable.getCodes('1175')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
              </p>
              <p>
               	<label><s:text name="WRSRP02_ticketType"/></label>
               	<s:select name="ticketType" list="#application.CodeTable.getCodes('1261')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
              </p>
              <p>
               	<label><s:text name="WRSRP02_channel"/></label>
               	<s:select name="channel" list="#application.CodeTable.getCodes('1011')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
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
          <cherry:show domId="BINOLWRSRP02PRI">
          	<a style="margin-right:10px;" onclick="BINOLWRSRP02.printALL(this);return false;" class="prints left">
						<span class="ui-icon icon-prints"></span>
						<span class="button-text"><s:text name="WRSRP02_printAll"/></span>
						</a>
          </cherry:show>
          	<cherry:show domId="BINOLWRSRP02EXP">
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
                <th><s:text name="WRSRP02_number" /></th>
                <th><s:text name="WRSRP02_billCode" /></th>
                <th><s:text name="WRSRP02_employeeId" /></th>
                <th><s:text name="WRSRP02_memCode" /></th>
                <th><s:text name="WRSRP02_saleType" /></th>
                <th><s:text name="WRSRP02_quantity" /></th>
                <th><s:text name="WRSRP02_amount" /></th>
                <th><s:text name="WRSRP02_discount" /></th>
                <th><s:text name="WRSRP02_saleDate" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLWRSRP02_search" id="searchSaleListUrl"></s:a>
<s:a action="BINOLCM02_initCampaignDialog" namespace="/common" id="searchCampaignInitUrl"></s:a>
<s:a action="BINOLWRSRP02_exportCheck" id="exportCheckUrl"></s:a>
<s:a action="BINOLWRSRP02_exportExcel" id="exportExcelUrl"></s:a>
<s:a action="BINOLWRSRP02_exportCsv" id="exportCsvUrl"></s:a>
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
		$("#resultList").show();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId: '#resultDataTable',
				 // 数据URL
				 url: url,
				 // 表格默认排序
				 aaSorting: [[ 8, "desc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "number", "sWidth": "5%", "bSortable": false},
				             { "sName": "billCode", "sWidth": "25%", "bSortable": false },
				             { "sName": "employeeId", "sWidth": "10%", "bSortable": false },
				             { "sName": "memCode", "sWidth": "10%", "bSortable": false },
				             { "sName": "saleType", "sWidth": "8%", "bSortable": false },
				             { "sName": "quantity", "sWidth": "10%"},
				             { "sName": "amount", "sWidth": "10%"},
				             { "sName": "discount", "sWidth": "10%", "bSortable": false},
				             { "sName": "saleTime", "sWidth": "12%"}],
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
	
	$("#selectCampaign").click(function(){
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				// 活动类型（0：会员活动，1：促销活动）
				if($checkedRadio.next().val() == ""){
					$("#campaignMode").val("0");
				}else {
					$("#campaignMode").val($checkedRadio.next().val());
				}
				$("#campaignCode").val($checkedRadio.val());
				var html = $checkedRadio.parent().next().text();
				//用于导出活动名称的查询条件
				$("#campaignName").val(html);
				html += '<span class="close" style="margin: 0 10px 2px 5px;"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#campaignDiv").html(html);
				$("#campaignDiv").find(".close").click(function(){
					$("#campaignMode").val("");
					$("#campaignCode").val("");
					$("#campaignName").val("");
					$("#campaignDiv").empty();
				});
			}
		}
		var url = $("#searchCampaignInitUrl").attr("href");
		var value = $("#campaignCode").val();
		popDataTableOfCampaignList(url, null, value, callback);
		return false;
	});
	
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
