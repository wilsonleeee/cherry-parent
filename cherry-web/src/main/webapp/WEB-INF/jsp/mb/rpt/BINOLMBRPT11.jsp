<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<s:i18n name="i18n.mb.BINOLMBRPT11">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="MBRPT11_memtitle"></s:text>&nbsp;&gt;&nbsp;<s:text name="MBRPT11_title"></s:text>
		</span>
	  </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryForm" class="inline">
        	<input id="brandInfoId" name="brandInfoId" class="hide" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'/>
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
            <input type="checkbox" name="firstFlag" id="firstFlag" value="1" checked="checked"/><s:text name="MBRPT11_firstFlag"/>
            <input type="checkbox" name="keyFlag" id="keyFlag" value="1" checked="checked"/><s:text name="MBRPT11_keyExist"/>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
            	<p>
              	<label><s:text name="MBRPT11_agency"/></label>
				<span><s:textfield name="agencyName" /></span>
              </p>
              <p>
              	<label><s:text name="MBRPT11_counter"/></label>
				<span><s:textfield name="counter" /></span>
              </p>
              <p>
              	<label><s:text name="MBRPT11_counterBAS"/></label>
				<span><s:textfield name="counterBAS"/></span>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
           	 <p>
              	<label><s:text name="MBRPT11_openID"/></label>
				<span><s:textfield name="openID" /></span>
              </p>
              <p>
              	<label><s:text name="MBRPT11_subscribeEventKey"/></label>
				<span><s:textfield name="subscribeEventKey" /></span>
              </p>
              <p>
                <label><s:text name="MBRPT11_subscribeTime"/></label>
                <span><s:textfield name="startDate" cssClass="date"></s:textfield></span> - <span><s:textfield name="endDate" cssClass="date"></s:textfield></span>
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
          	<cherry:show domId="BINOLMBRPT11EXP">
			  	<a id="exportExcel" class="export" onclick="exportFile('Excel');return false;">
	          	  <span class="ui-icon icon-export"></span>
	          	  <span class="button-text"><s:text name="global.page.exportExcel"/></span>
	          	</a>
	          	<a id="exportCSV" class="export" onclick="exportFile('Csv');return false;">
	          	  <span class="ui-icon icon-export"></span>
	          	  <span class="button-text"><s:text name="global.page.exportCsv"/></span>
	          	</a>
          	</cherry:show>
		  	<span id="headInfo"></span>
		  	<span class="right">
           		<%-- 列设置按钮  --%>
           		<a href="#" class="setting">
           			<span class="button-text"><s:text name="global.page.colSetting"/></span>
    		 		<span class="ui-icon icon-setting"></span>
    		 	</a>
           	</span>
		  </div>
          <table id="resultDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="MBRPT11_openID" /></th>
                <th><s:text name="MBRPT11_counter" /></th><!-- 名称和code一起 -->
                <th><s:text name="MBRPT11_counterBAS" /></th>
                <th><s:text name="MBRPT11_agency" /></th>
                <th><s:text name="MBRPT11_subscribeEventKey" /></th>
                <th><s:text name="MBRPT11_subscribeTime" /></th>
                <th><s:text name="MBRPT11_isFirstFlag" /></th>
                <th><s:text name="MBRPT11_QRCodeName" /></th>
                <th><s:text name="MBRPT11_QRCodeImageUrl" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLMBRPT11_search" id="searchUrl"></s:a>
<s:a action="BINOLMBRPT11_export" id="exportUrl"></s:a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />

<script type="text/javascript">
$(function(){
	
	counterBinding({
		elementId:"counter",
		privilegeFlag:"1",
		showNum:20
	});
	
	counterBASBinding({
		elementId:"counterBAS",
		testUrl:"/mb/BINOLMBRPT11_getCounterBAS.action"+"?",
		showNum:20
	});
	
    var holidays = '${holidays }';
    $('#startDate').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#endDate').val();
            return [value,'maxDate'];
        }
    });
    $('#endDate').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#startDate').val();
            return [value,'minDate'];
        }
    });
    
    searchList();
	
	// 表单验证配置
	cherryValidate({						
		formId: 'queryForm',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
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
		var params = $("#queryForm").serialize();
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
				 aaSorting: [[ 6, "desc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "OpenID", "sWidth": "20%"},
				             { "sName": "CounterCode", "sWidth": "20%"},
				             { "sName": "EmployeeCode", "sWidth": "10%","bVisible": false},
				             { "sName": "AgencyName", "sWidth": "10%"},
				             { "sName": "SubscribeEventKey", "sWidth": "10%"},
				             { "sName": "SubscribeTime", "sWidth": "15%"},
				             { "sName": "FirstFlag", "sWidth": "5%","bVisible": false},
				             { "sName": "QRCodeName", "sWidth": "10%"},
				             { "sName": "QRCodeImageUrl", "sWidth": "20%","bVisible": false}
				             ],
				// 横向滚动条出现的临界宽度
				aiExclude :[0, 1],
				sScrollX: "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
	};
});

function exportFile(exportType) {
	if($("#resultDataTable").find(".dataTables_empty:visible").length==0) {
		var url = $("#exportUrl").attr("href");
		if("Excel" == exportType) {
			url += "?exportType=" + exportType;
			$("#queryForm").attr("action", url);
			$("#queryForm").submit();
		} else {
			var params = $("#queryForm").serialize();
			params += "&exportType=" + exportType;
			exportReport({
        		exportUrl:url,
        		exportParam:params            	
        	});
		}
    }
	return false;
};

function counterBASBinding(options) {
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath + options.testUrl+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			counterBAS: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中柜台主管名称
			selected:options.selected ? options.selected : "name",
			brandInfoId:function() { return $('#brandInfoId').val();}
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:200,
	    max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[1])+" "+ (row[0] ? "【"+escapeHTMLHandle(row[0])+"】" : "");
			}else{
				return escapeHTMLHandle(row[1])+" "+ (row[0] ? "【"+escapeHTMLHandle(row[0])+"】" : "");
			}
		}
	});
}
</script>
