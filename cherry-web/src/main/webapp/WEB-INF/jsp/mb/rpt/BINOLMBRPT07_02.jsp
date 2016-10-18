<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>

<body>
<div class="main container clearfix">
<div class="panel ui-corner-all">


<s:i18n name="i18n.mb.BINOLMBRPT07">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="mbrpt07_title"></s:text>&nbsp;&gt;&nbsp;<s:text name="mbrpt07_detailTitle"></s:text>
		</span>
	  </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryParam" class="inline" csrftoken="false">
          <s:hidden name="memberInfoId"></s:hidden>
          <s:hidden name="campaignCode"></s:hidden>
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column last" style="width:49%; height: auto;">
              <p>
                <label><s:text name="mbrpt07_dateRange"/></label>
                <span><s:textfield name="startDate" cssClass="date"></s:textfield></span>-<span><s:textfield name="endDate" cssClass="date"></s:textfield></span>
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
		  	<span id="headInfo"></span>
		  </div>
          <table id="resultDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="mbrpt07_number" /></th>
                <th><s:text name="mbrpt07_productName" /></th>
                <th><s:text name="mbrpt07_unitCode" /></th>
                <th><s:text name="mbrpt07_barCode" /></th>
                <th><s:text name="mbrpt07_buyQuantity" /></th>
                <th><s:text name="mbrpt07_buyAmount" /></th>
                <th><s:text name="mbrpt07_saleType" /></th>
                <th><s:text name="mbrpt07_saleTime" /></th>
                <th><s:text name="mbrpt07_departName" /></th>
                <th><s:text name="mbrpt07_employeeName" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLMBRPT07_searchSaleDetail" id="searchDetailUrl"></s:a>
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
	
	$("#searchButton").click(function(){
		searchList();
		return false;
	});
	
	function searchList() {
		var url = $("#searchDetailUrl").attr("href") + "?" + getSerializeToken();
		var params = $("#queryParam").serialize();
		if(params != null && params != "") {
			url = url + "&" + params;
		}
		$("#resultList").show();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId: '#resultDataTable',
				 // 数据URL
				 url: url,
				 // 表格默认排序
				 aaSorting: [[ 7, "desc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "number", "sWidth": "5%", "bSortable": false},
				             { "sName": "productName", "sWidth": "10%"},
				             { "sName": "unitCode", "sWidth": "10%"},
				             { "sName": "barCode", "sWidth": "10%"},
				             { "sName": "buyQuantity", "sWidth": "10%"},
				             { "sName": "buyAmount", "sWidth": "10%"},
				             { "sName": "saleType", "sWidth": "10%"},
				             { "sName": "saleTime", "sWidth": "15%"},
				             { "sName": "departName", "sWidth": "10%", "bSortable": false},
				             { "sName": "employeeName", "sWidth": "10%", "bSortable": false}],
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
	$("#searchButton").click();
	
});
</script>


</div>
</div>
</body>
</html>