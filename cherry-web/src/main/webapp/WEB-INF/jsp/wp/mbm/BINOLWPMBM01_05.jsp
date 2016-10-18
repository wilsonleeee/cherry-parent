<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBMBM05">
<s:text name="global.page.select" id="select_default"/>
<div class="crm_top clearfix">
	<div><s:text name="binolmbmbm05_memSaleTab" /></div>
	
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
            <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm05_saleCountInfo" />
          </strong>
	    </div>
        <div class="section-content" id="saleCountInfo">
		</div>    
	</div>
	
	<div class="section">
        <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm05_saleInfo" />
          </strong>
          
          <a id="expandConditionSale" style="margin-left: 0px; font-size: 12px;" class="ui-select right">
	        <span style="min-width:50px;" class="button-text choice"><s:text name="global.page.condition" /></span>
	 		<span class="ui-icon ui-icon-triangle-1-n"></span>
	 	  </a>
	    </div>
        <div class="section-content">
	  
		  <div class="box hide" id="saleConditionDiv">
		  <form id="saleCherryForm" class="inline" >
		  <s:hidden name="memberInfoId"></s:hidden>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column" style="width:50%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm05_billCode"/></label>
	          <s:textfield name="billCode" cssClass="text"/>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm05_prtVendorId"/></label>
	          <s:textfield name="nameTotal" cssClass="text" maxlength="30"/>
		      <input type="hidden" id="prtVendorIdM" name="prtVendorId" value="" />
	        </p>
	      </div>
	      <div class="column last" style="width:49%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm05_saleTime"/></label>
	          <span><s:textfield name="saleTimeStart" cssClass="date" cssStyle="width: 80px;"></s:textfield></span><span>-<s:textfield name="saleTimeEnd" cssClass="date" cssStyle="width: 80px;"></s:textfield></span>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm05_saleType"/></label>
	          <s:select name="saleType" list="#application.CodeTable.getCodes('1055')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
	        </p>
	      </div>
		  </div>
		  <p class="clearfix">
	          <button class="right search" id="searchSale"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
	      </p>
		  </form>
		  </div>
		  
	  	  <div class="ui-tabs">

			<div class="clearfix">
			<ul class ="ui-tabs-nav left" id="tabSelect">
			  <li id="0" class="<s:if test='%{"0".equals(displayFlag)}'>ui-tabs-selected</s:if>"><a><s:text name="binolmbmbm05_memSaleDisplayMode0"/></a></li>
			  <li id="1" class="<s:if test='%{!"0".equals(displayFlag)}'>ui-tabs-selected</s:if>"><a><s:text name="binolmbmbm05_memSaleDisplayMode1"/></a></li>
			</ul>
			<span class="right" style="margin: 3px 12px 0px 0px;">
		     	<a id="export" class="export">
		          <span class="ui-icon icon-export"></span>
		          <span class="button-text"><s:text name="global.page.export"/></span>
		        </a>
		    </span>
		    </div>

		  	<div class="ui-tabs-panel">
		      	<div id="memSaleDataDiv"></div>
		      	<%-- ====================== 结果一览结束 ====================== --%>
				<div class="hide">
					<div id="binolmbmbm05_saleMemCode"><s:text name="binolmbmbm05_saleMemCode" /></div>
					<div id="binolmbmbm05_billCode"><s:text name="binolmbmbm05_billCode" /></div>
					<div id="binolmbmbm05_saleType"><s:text name="binolmbmbm05_saleType" /></div>
					<cherry:show domId="BINOLMBMBM10_29">
					<div id="binolmbmbm05_departCode"><s:text name="binolmbmbm05_departCode" /></div>
					</cherry:show>
					<div id="binolmbmbm05_saleTime"><s:text name="binolmbmbm05_saleTime" /></div>
					<div id="binolmbmbm05_quantity"><s:text name="binolmbmbm05_quantity" /></div>
					<div id="binolmbmbm05_amount"><s:text name="binolmbmbm05_amount" /></div>
					<div id="binolmbmbm05_proName"><s:text name="binolmbmbm05_proName" /></div>
					<div id="binolmbmbm05_unitCode"><s:text name="binolmbmbm05_unitCode" /></div>
					<div id="binolmbmbm05_barCode"><s:text name="binolmbmbm05_barCode" /></div>
					<div id="binolmbmbm05_detailSaleType"><s:text name="binolmbmbm05_detailSaleType" /></div>
					<div id="binolmbmbm05_pricePay"><s:text name="binolmbmbm05_pricePay" /></div>
					<div id="binolmbmbm05_saleEmployee"><s:text name="binolmbmbm05_saleEmployee" /></div>
					<div id="binolmbmbm05_campaignName"><s:text name="binolmbmbm05_campaignName" /></div>
					<div id="binolmbmbm05_saleExt"><s:text name="binolmbmbm05_saleExt" /></div>
					<div id="binolmbmbm05_UniqueCode"><s:text name="binolmbmbm05_UniqueCode" /></div>
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

<s:a action="BINOLWPMBM01_searchSaleRecord" id="searchSaleRecordUrl" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_exportSale" id="exportSaleUrl" cssStyle="display: none;"></s:a>
<input type="hidden" id="sysConfigShowUniqueCode" value='<s:property value="sysConfigShowUniqueCode"/>'/>

<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<script type="text/javascript">
$(function(){
	$('#saleTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#saleTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#saleTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#saleTimeStart').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'saleCherryForm',
		rules: {
			saleTimeStart: {dateValid: true},
			saleTimeEnd: {dateValid: true}
		}
	});
	$('#expandConditionSale').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-n')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#saleConditionDiv').show();
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			$('#saleConditionDiv').hide();
		}
	});
	productBinding({elementId:"nameTotal",showNum:20, targetId:"prtVendorIdM"});
	
	$("#searchSale").click(function(){
		var $saleCherryForm = $('#saleCherryForm');
		if(!$saleCherryForm.valid()) {
			return false;
		}
		var url = $("#searchSaleRecordUrl").attr("href");
		var displayFlag = $("#tabSelect").find(".ui-tabs-selected").attr("id");
		url += "?" + getSerializeToken();
		url += "&" + $saleCherryForm.serialize();
		url += '&displayFlag=' + displayFlag;
		$("#memSaleDataDiv").empty();
		var sysConfigShowUniqueCode = $("#sysConfigShowUniqueCode").val();
		var table = '<table id="memSaleDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table '+(displayFlag == '0' ? 'memEventDataTable' : '')+'" width="100%"><thead><tr>';
		if(displayFlag == '0') {
			table += '<th>'+ $("#binolmbmbm05_saleMemCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_billCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_saleType").html() +'</th>';
			if($("#binolmbmbm05_departCode").length > 0) {
				table += '<th>'+ $("#binolmbmbm05_departCode").html() +'</th>';
			}
			table += '<th>'+ $("#binolmbmbm05_saleTime").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_quantity").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_amount").html() +'</th>';
		} else {
			table += '<th>'+ $("#binolmbmbm05_saleMemCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_proName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_unitCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_barCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_saleType").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_detailSaleType").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_quantity").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_pricePay").html() +'</th>';
			if($("#binolmbmbm05_departCode").length > 0) {
				table += '<th>'+ $("#binolmbmbm05_departCode").html() +'</th>';
			}
			table += '<th>'+ $("#binolmbmbm05_saleEmployee").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_saleTime").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_campaignName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_saleExt").html() +'</th>';
			if(sysConfigShowUniqueCode == "1"){
				table += '<th>'+ $("#binolmbmbm05_UniqueCode").html() +'</th>';
			}
			table += '<th>'+ $("#binolmbmbm05_billCode").html() +'</th>';
		}
		table += '</tr></thead></table>';
		$("#memSaleDataDiv").html(table);
		
		oTableArr[101] = null;
		fixedColArr[101] = null;
		var aoColumns = [];
		var aaSorting = [];
		if(displayFlag == '0') {
			aoColumns.push({ "sName": "memberCode", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "billCode", "sWidth": "25%" });
			aoColumns.push({ "sName": "saleType", "sWidth": "10%" });
			if($("#binolmbmbm05_departCode").length > 0) {
				aoColumns.push({ "sName": "departCode", "sWidth": "20%" });
			}
			aoColumns.push({ "sName": "saleTime", "sWidth": "15%" });
			aoColumns.push({ "sName": "quantity", "sWidth": "10%" });
			aoColumns.push({ "sName": "amount", "sWidth": "10%" });
			
			if($("#binolmbmbm05_departCode").length > 0) {
				aaSorting.push([4, "desc"]);
			} else {
				aaSorting.push([3, "desc"]);
			}
		} else {
			aoColumns.push({ "sName": "memberCode", "sWidth": "8%", "bSortable": false });
			aoColumns.push({ "sName": "prtName", "sWidth": "8%" });
			aoColumns.push({ "sName": "unitCode", "sWidth": "8%" });
			aoColumns.push({ "sName": "barCode", "sWidth": "8%" });
			aoColumns.push({ "sName": "saleType", "sWidth": "8%" });
			aoColumns.push({ "sName": "detailSaleType", "sWidth": "8%" });
			aoColumns.push({ "sName": "quantity", "sWidth": "8%" });
			aoColumns.push({ "sName": "pricePay", "sWidth": "8%" });
			if($("#binolmbmbm05_departCode").length > 0) {
				aoColumns.push({ "sName": "departCode", "sWidth": "13%" });
			}
			aoColumns.push({ "sName": "employeeCode", "sWidth": "13%" });
			aoColumns.push({ "sName": "saleTime", "sWidth": "10%" });
			aoColumns.push({ "sName": "activityName", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "saleExt", "sWidth": "10%", "bSortable": false });
			if(sysConfigShowUniqueCode == "1"){
				aoColumns.push({ "sName": "uniqueCode", "sWidth": "10%", "bSortable": false });
			}
			aoColumns.push({ "sName": "billCode", "sWidth": "13%", "bSortable": false });
			
			if($("#binolmbmbm05_departCode").length > 0) {
				aaSorting.push([10, "desc"]);
			} else {
				aaSorting.push([9, "desc"]);
			}
		}
		
		// 表格设置
		var tableSetting = {
				// 表格ID
				tableId : '#memSaleDataTable',
				// 数据URL
				url : url,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index: 101,
				iDisplayLength: 10,
				fnDrawCallback : function() {
					if(displayFlag == '0') {
						$("#memSaleDataTable").find('tr').click(function() {
							binolwpmbm01.searchDetail(this);
						});
					}
				},
				callbackFun: function(msg) {
			 		var $msg = $("<div></div>").html(msg);
			 		var $headInfo = $("#saleCountInfo",$msg);
			 		if($headInfo.length > 0) {
			 			$("#saleCountInfo").html($headInfo.html());
			 		} else {
			 			$("#saleCountInfo").empty();
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
    	$("#searchSale").click();
	});
	
	$("#export").click(function(){
		if($(".dataTables_empty:visible").length==0) {
    		var url = $("#exportSaleUrl").attr("href");
    		url += "?" + getSerializeToken();
    		url += "&" + $("#saleCherryForm").serialize();
            document.location.href = url;
        }
	});
	
	$(".back").click(function(){
		$("#memOtherDiv").hide();
		$("#memInitDiv").show();
		$("#memOtherDiv").empty();
		return false;
	});
	
	$("#searchSale").click();
});
</script>