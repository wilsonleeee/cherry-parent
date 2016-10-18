<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBMBM04"> 	
<s:text name="global.page.select" id="select_default"/>
<div class="crm_top clearfix">
	<div><s:text name="binolmbmbm04_memPointTab" /></div>
	
	<span class="right" style="padding-top:6px;">
	<a class="add back">
		<span class="ui-icon icon-import"></span>
		<span class="button-text"><s:text name="global.page.back"/></span>
	</a>
	</span>
</div>
<div class="wp_main">
    <div class="wp_left">
        <div class="wp_content" style="margin-right: 0;padding-right: 0.5em">
        	<div class="wp_leftbox">	
 	
 	<div class="section">
        <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm04_pointInfo" />
          </strong>
	    </div>
        <div class="section-content">
          <table class="detail" style="margin-bottom: 3px;">
            <tr>
			  <th><s:text name="binolmbmbm04_freezePoint" /></th>
			  <td><span><s:property value="memPointInfo.totalPoint"/></span></td>
			  <th><s:text name="binolmbmbm04_changablePoint" /></th>
			  <td><span><s:property value="memPointInfo.changablePoint"/></span></td>
		    </tr>
		  </table>
          <table class="detail" style="margin-bottom: 3px;">
            <tr>
			  <th><s:text name="binolmbmbm04_totalPoint" /></th>
			  <td><span><s:property value="memPointInfo.totalPoint"/></span></td>
			  <th><s:text name="binolmbmbm04_totalChanged" /></th>
			  <td><span><s:property value="memPointInfo.totalChanged"/></span></td>
		    </tr>
		  </table>
          <table class="detail" style="margin-bottom: 3px;">
            <tr>
			  <th><s:text name="binolmbmbm04_curDisablePoint" /></th>
			  <td><span><s:property value="memPointInfo.curDisablePoint"/></span></td>
			  <th><s:text name="binolmbmbm04_curDealDate" /></th>
			  <td><span><s:property value="memPointInfo.curDealDate"/></span></td>
		    </tr>
		    <tr>
			  <th><s:text name="binolmbmbm04_totalDisablePoint" /></th>
			  <td><span><s:property value="memPointInfo.totalDisablePoint"/></span></td>
			  <th><s:text name="binolmbmbm04_preDisableDate" /></th>
			  <td><span><s:property value="memPointInfo.preDisableDate"/></span></td>
		    </tr>
		  </table>
		</div>    
	</div>
	
	<div class="section">
        <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm04_pointDetail" />
          </strong>
          <a id="expandConditionPoint" style="margin-left: 0px; font-size: 12px;" class="ui-select right">
	        <span style="min-width:50px;" class="button-text choice"><s:text name="global.page.condition" /></span>
	 		<span class="ui-icon ui-icon-triangle-1-n"></span>
	 	  </a>
	    </div>
        <div class="section-content">
        
		<div class="box hide" id="pointConditionDiv">
		<form id="pointDetailForm" class="inline">
		  <s:hidden name="memberInfoId"></s:hidden>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column" style="width:50%; height: auto;">
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm04_billCode" /></label>
	          <s:textfield name="billCode" cssClass="text"></s:textfield>
	        </p>
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm04_relevantSRCode" /></label>
	          <s:textfield name="relevantSRCode" cssClass="text"></s:textfield>
	        </p>
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm04_departCode" /></label>
	          <s:textfield name="departCode" cssClass="text"></s:textfield>
	        </p>
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm04_prtVendorId" /></label>
	          <s:textfield name="prtNamePoint" cssClass="text" maxlength="30"/>
			  <input type="hidden" id="prtVendorIdPoint" name="prtVendorId" value="" />
	        </p>
	      </div>
	      <div class="column last" style="width:49%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm04_changeDate" /></label>
	          <span><s:textfield name="changeDateStart" cssClass="date" cssStyle="width: 80px;"></s:textfield></span><span>-<s:textfield name="changeDateEnd" cssClass="date" cssStyle="width: 80px;"></s:textfield></span>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbptm04_memPoint" /></label>
	          <span><s:textfield name="memPointStart" cssClass="text" cssStyle="width:75px"></s:textfield></span><span>-<s:textfield name="memPointEnd" cssClass="text" cssStyle="width:75px"></s:textfield></span>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm04_billType" /></label>
	          <s:select list='#application.CodeTable.getCodes("1213")' listKey="CodeKey" listValue="Value" name="tradeType" headerKey="" headerValue="%{#select_default}"></s:select>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm04_campaignName" /></label>
	          <s:select list="campaignNameList" name="subCampaignId" listKey="subCampaignId" listValue="campaignName" headerKey="" headerValue="%{#select_default}"></s:select>
	        </p>
	      </div>
		  </div>
		  <p class="clearfix">
	          <button class="right search" id="searchPoint"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
	      </p>
		</form>
		</div>
		
		<div class="ui-tabs">

			<div class="clearfix">
			<ul class ="ui-tabs-nav left" id="tabSelect">
			  <li id="0" class="<s:if test='%{"0".equals(displayFlag)}'>ui-tabs-selected</s:if>" onclick="binolmbmbm04.changeTab(this);"><a><s:text name="binolmbmbm04_memSaleDisplayMode0"/></a></li>
			  <li id="1" class="<s:if test='%{!"0".equals(displayFlag)}'>ui-tabs-selected</s:if>" onclick="binolmbmbm04.changeTab(this);"><a><s:text name="binolmbmbm04_memSaleDisplayMode1"/></a></li>
			</ul>
			<span class="right" style="margin: 3px 12px 0px 0px;">
		     	<a id="export" class="export" onclick="binolmbmbm04.exportExcel();return false;">
		          <span class="ui-icon icon-export"></span>
		          <span class="button-text"><s:text name="global.page.export"/></span>
		        </a>
		    </span>
		    </div>
		    
		    <div class="ui-tabs-panel">
		      	<div id="pointDetailDataDiv"></div>
		      	<%-- ====================== 结果一览结束 ====================== --%>
				<div class="hide">
					<div id="binolmbmbm04_memberCode"><s:text name="binolmbmbm04_memberCode" /></div>
					<div id="binolmbmbm04_billCode"><s:text name="binolmbmbm04_billCode" /></div>
					<div id="binolmbmbm04_billType"><s:text name="binolmbmbm04_billType" /></div>
					<cherry:show domId="BINOLMBMBM10_29">
					<div id="binolmbmbm04_departCode"><s:text name="binolmbmbm04_departCode" /></div>
					</cherry:show>
					<div id="binolmbmbm04_changeDate"><s:text name="binolmbmbm04_changeDate" /></div>
					<div id="binolmbmbm04_amount"><s:text name="binolmbmbm04_amount" /></div>
					<div id="binolmbmbm04_quantity"><s:text name="binolmbmbm04_quantity" /></div>
					<div id="binolmbmbm04_point"><s:text name="binolmbmbm04_point" /></div>
					<div id="binolmbmbm04_proName"><s:text name="binolmbmbm04_proName" /></div>
					<div id="binolmbmbm04_unitCode"><s:text name="binolmbmbm04_unitCode" /></div>
					<div id="binolmbmbm04_barCode"><s:text name="binolmbmbm04_barCode" /></div>
					<div id="binolmbmbm04_saleType"><s:text name="binolmbmbm04_saleType" /></div>
					<div id="binolmbmbm04_proPrice"><s:text name="binolmbmbm04_proPrice" /></div>
					<div id="binolmbmbm04_proQuantity"><s:text name="binolmbmbm04_proQuantity" /></div>
					<div id="binolmbmbm04_proPoint"><s:text name="binolmbmbm04_proPoint" /></div>
					<div id="binolmbmbm04_pointType"><s:text name="binolmbmbm04_pointType" /></div>
					<div id="binolmbmbm04_combCampaignName"><s:text name="binolmbmbm04_combCampaignName" /></div>
					<div id="binolmbmbm04_mainCampaignName"><s:text name="binolmbmbm04_mainCampaignName" /></div>
					<div id="binolmbmbm04_subCampaignName"><s:text name="binolmbmbm04_subCampaignName" /></div>
					<div id="binolmbmbm04_reason"><s:text name="binolmbmbm04_reason" /></div>
					<div id="binolmbmbm04_relevantSRCode"><s:text name="binolmbmbm04_relevantSRCode" /></div>
					<div id="binolmbmbm04_relevantSRTime"><s:text name="binolmbmbm04_relevantSRTime" /></div>
					<div id="binolmbmbm04_hasRelevantSRCode"><s:text name="binolmbmbm04_hasRelevantSRCode" /></div>
				</div>
	      	</div>
	    
	    </div>
    
    	</div>
    </div>
    
    <div class="center clearfix">
      <button type="button" class="back">
   		<span class="ui-icon icon-back"></span>
   		<span class="button-text"><s:text name="global.page.back"/></span>
   	  </button>
    </div>
    
    </div>
        </div>
    
    </div>
</div>
</s:i18n>   

<div class="hide">
	<s:url action="BINOLWPMBM01_searchPointInfo" id="searchPointInfoUrl"></s:url>
	<a href="${searchPointInfoUrl }" id="searchPointInfoUrl"></a>
	<s:url id="exportPointUrl" action="BINOLWPMBM01_exportPoint" ></s:url>
	<a id="exportPointUrl" href="${exportPointUrl}"></a>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>  

<script type="text/javascript">
$(function() {
	$('#changeDateStart').cherryDate({
		beforeShow: function(input){
			var value = $('#changeDateEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#changeDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#changeDateStart').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'pointDetailForm',
		rules: {
			changeDateStart: {dateValid: true},
			changeDateEnd: {dateValid: true},
			memPointStart: {numberValid: true},
			memPointEnd: {numberValid: true}
		}
	});
	$('#expandConditionPoint').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-n')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#pointConditionDiv').show();
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			$('#pointConditionDiv').hide();
		}
	});
	productBinding({elementId:"prtNamePoint",showNum:20,targetId:"prtVendorIdPoint"});
	counterBinding({elementId:"departCode",showNum:20,selected:"code"});
	
	$("#searchPoint").click(function(){

    	var $pointDetailForm = $('#pointDetailForm');
    	if(!$pointDetailForm.valid()) {
			return false;
		}
		var url = $("#searchPointInfoUrl").attr("href");
		var displayFlag = $("#tabSelect").find(".ui-tabs-selected").attr("id");
		url += "?" + getSerializeToken();
		url += "&" + $pointDetailForm.serialize();
		url += '&displayFlag=' + displayFlag;
		$("#pointDetailDataDiv").empty();
		var table = '<table id="pointDetailDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table '+(displayFlag == '0' ? 'memEventDataTable' : '')+'" width="100%"><thead><tr>';
		if(displayFlag == '0') {
			table += '<th>'+ $("#binolmbmbm04_memberCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_billCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_billType").html() +'</th>';
			if($("#binolmbmbm04_departCode").length > 0) {
				table += '<th>'+ $("#binolmbmbm04_departCode").html() +'</th>';
			}
			table += '<th>'+ $("#binolmbmbm04_changeDate").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_amount").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_quantity").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_point").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_hasRelevantSRCode").html() +'</th>';
		} else {
			table += '<th>'+ $("#binolmbmbm04_memberCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_proName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_unitCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_barCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_saleType").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_proPrice").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_proQuantity").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_proPoint").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_pointType").html() +'</th>';
			if($("#binolmbmbm04_departCode").length > 0) {
				table += '<th>'+ $("#binolmbmbm04_departCode").html() +'</th>';
			}
			table += '<th>'+ $("#binolmbmbm04_changeDate").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_combCampaignName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_mainCampaignName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_subCampaignName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_reason").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_billCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_relevantSRCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_relevantSRTime").html() +'</th>';
		}
		table += '</tr></thead></table>';
		$("#pointDetailDataDiv").html(table);
		
		oTableArr = new Array(null,null);
		fixedColArr = new Array(null,null);
		var aoColumns = [];
		var aaSorting = [];
		if(displayFlag == '0') {
			aoColumns.push({ "sName": "memCode", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "billCode", "sWidth": "15%" });
			aoColumns.push({ "sName": "billType", "sWidth": "10%" });
			if($("#binolmbmbm04_departCode").length > 0) {
				aoColumns.push({ "sName": "departCode", "sWidth": "10%" });
			}
			aoColumns.push({ "sName": "changeDate", "sWidth": "15%" });
			aoColumns.push({ "sName": "amount", "sWidth": "10%" });
			aoColumns.push({ "sName": "quantity", "sWidth": "10%" });
			aoColumns.push({ "sName": "point", "sWidth": "10%" });
			aoColumns.push({ "sName": "hasRelevantSRCode", "sWidth": "10%", "bSortable": false });
			
			if($("#binolmbmbm04_departCode").length > 0) {
				aaSorting.push([4, "desc"]);
			} else {
				aaSorting.push([3, "desc"]);
			}
		} else {
			aoColumns.push({ "sName": "memCode", "sWidth": "5%", "bSortable": false });
			aoColumns.push({ "sName": "proName", "sWidth": "5%" });
			aoColumns.push({ "sName": "unitCode", "sWidth": "5%" });
			aoColumns.push({ "sName": "barCode", "sWidth": "5%" });
			aoColumns.push({ "sName": "saleType", "sWidth": "5%" });
			aoColumns.push({ "sName": "price", "sWidth": "5%" });
			aoColumns.push({ "sName": "quantity", "sWidth": "5%" });
			aoColumns.push({ "sName": "point", "sWidth": "5%" });
			aoColumns.push({ "sName": "pointType", "sWidth": "5%" });
			if($("#binolmbmbm04_departCode").length > 0) {
				aoColumns.push({ "sName": "departCode", "sWidth": "10%" });
			}
			aoColumns.push({ "sName": "changeDate", "sWidth": "10%" });
			aoColumns.push({ "sName": "combCampaignName", "sWidth": "5%", "bSortable": false });
			aoColumns.push({ "sName": "mainCampaignName", "sWidth": "5%", "bSortable": false });
			aoColumns.push({ "sName": "subCampaignName", "sWidth": "5%", "bSortable": false });
			aoColumns.push({ "sName": "reason", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "billCode", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "srCode", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "srTime", "sWidth": "10%", "bSortable": false });
			
			if($("#binolmbmbm04_departCode").length > 0) {
				aaSorting.push([10, "desc"]);
			} else {
				aaSorting.push([9, "desc"]);
			}
		}
		
		// 表格设置
		var tableSetting = {
				// 表格ID
				tableId : '#pointDetailDataTable',
				// 数据URL
				url : url,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index: 102,
				iDisplayLength: 10,
				fnDrawCallback : function() {
					if(displayFlag == '0') {
						$("#pointDetailDataTable").find('tr').click(function() {
							binolwpmbm01.searchDetail(this);
						});
					}
				}
		};
		tableSetting.aoColumns = aoColumns;
		tableSetting.aaSorting = aaSorting;
		// 调用获取表格函数
		getTable(tableSetting);
		return false;
	});
	
	$("#tabSelect li").click(function(){
		$(this).siblings().removeClass('ui-tabs-selected');
    	$(this).addClass('ui-tabs-selected');
    	$("#searchPoint").click();
	});
	
	$("#export").click(function(){
		if($(".dataTables_empty:visible").length==0) {
    		var url = $("#exportPointUrl").attr("href");
    		url += "?" + getSerializeToken();
    		url += "&" + $("#pointDetailForm").serialize();
            document.location.href = url;
        }
	});
	
	$(".back").click(function(){
		$("#memOtherDiv").hide();
		$("#memInitDiv").show();
		$("#memOtherDiv").empty();
		return false;
	});
	
	$("#searchPoint").click();
});
</script>