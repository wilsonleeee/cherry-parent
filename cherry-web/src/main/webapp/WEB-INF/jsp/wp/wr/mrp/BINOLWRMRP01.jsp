<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRMRP01">
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRMRP01_wreport"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRMRP01_memInfo"></s:text>
		</span>
	  </div>
    </div>
    <div style="display: none" id="validateError">
    <div class="actionError">
       <ul><li><span></span></li></ul>         
    </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryParam" class="inline">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          	<span style="color:#999;"><s:text name="WRMRP01_remark" /></span>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
              	<label><s:text name="WRMRP01_memCode"/></label>
                <s:textfield name="memCode" cssClass="text"/>
              </p>
              <p>
               	<label><s:text name="WRMRP01_mobilePhone"/></label>
               	<s:textfield name="mobilePhone" cssClass="text"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
                <label><s:text name="WRMRP01_memName"/></label>
                <s:textfield name="memName" cssClass="text"/>
              </p>
              <p>
                <label><s:text name="WRMRP01_birthDay"/></label>
                <select name="birthDayMonth" style="width:50px;" id="birthDayMonth">
                  <option value=""><s:text name="WRMRP01_birthDayMonth" /></option>
                </select>
                <select name="birthDayDate" style="width:50px;" id="birthDayDate">
                  <option value=""><s:text name="WRMRP01_birthDayDate" /></option>
                </select>
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
      <div id="memList" class="hide">
      <div class="section">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="global.page.list"/>
        	</strong>
        </div>
        <div class="section-content">
          <table id="memDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="WRMRP01_memCode" /></th>
                <th><s:text name="WRMRP01_mobilePhone" /></th>
                <th><s:text name="WRMRP01_memName" /></th>
                <th><s:text name="WRMRP01_birthDay" /></th>
                <th><s:text name="WRMRP01_levelName" /></th>
                <th><s:text name="WRMRP01_changablePoint" /></th>
                <th><s:text name="WRMRP01_joinDate" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
      <div id="memDetail" class="hide"></div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLWRMRP01_search" id="searchMemInfoUrl"></s:a>
<s:a action="BINOLWRMRP01_list" id="searchMemListUrl"></s:a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

<script type="text/javascript">
$(function(){
	
	var binolwpmbm01Message = {
			validateError1: "请先输入条件",
			validateError2: "请输入生日",
			validateError3: "请输入姓名",
    		loading: "加载中...",
    		nodata: "无记录"
    };
    var language = $('#CHERRY_LANGUAGE').text();
    if(language == "zh_TW") {
    	binolwpmbm01Message = {
    			validateError1: "請先輸入條件",
    			validateError2: "請輸入生日",
				validateError3: "請輸入姓名",
	    		loading: "加載中...",
	    		nodata: "無記錄"
        };
    }
	
	// 生日框初始化处理
	function birthDayInit() {
		for(var i = 1; i <= 12; i++) {
			$("#birthDayMonth").append('<option value="'+i+'">'+i+'</option>');
		}
		$("#birthDayMonth").change(function(){
			var $date = $("#birthDayDate");
			var month = $(this).val();
			var options = '<option value="">'+$date.find('option').first().html()+'</option>';
			if(month == "") {
				$date.html(options);
				return;
			}
			var i = 1;
			var max = 0;
			if(month == '2') {
				max = 29;
			} else if(month == '4' || month == '6' || month == '9' || month == '11') {
				max = 30;
			} else {
				max = 31;
			}
			for(i = 1; i <= max; i++) {
				options += '<option value="'+i+'">'+i+'</option>';
			}
			$date.html(options);
		});
	}
	birthDayInit();
	
	// 验证是否存在查询条件
	function validateParam() {
		var result = "";
		var mobilePhoneQ = $("#queryParam").find(":input[name=mobilePhone]").val();
		var memCodeQ = $("#queryParam").find(":input[name=memCode]").val();
		var memNameQ = $("#queryParam").find(":input[name=memName]").val();
		var birthDayMonthQ = $("#queryParam").find(":input[name=birthDayMonth]").val();
		var birthDayDateQ = $("#queryParam").find(":input[name=birthDayDate]").val();
		if(mobilePhoneQ || memCodeQ) {
			result = "";
		} else {
			if(memNameQ && birthDayMonthQ && birthDayDateQ) {
				result = "";
			} else {
				if(memNameQ) {
					result = binolwpmbm01Message.validateError2;
				} else if(birthDayMonthQ && birthDayDateQ) {
					result = binolwpmbm01Message.validateError3;
				} else {
					result = binolwpmbm01Message.validateError1;
				}
			}
		}
		return result;
	}
	
	$("#searchMem").click(function(){
		var result = validateParam();
		if(result) {
			$("#validateError").show();
			$("#validateError").find("span").html(result);
			return false;
		}
		$("#validateError").hide();
		var url = $("#searchMemInfoUrl").attr("href");
		var params = $("#queryParam").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		$("#memList").hide();
		$("#memDetail").html(binolwpmbm01Message.loading);
		$("#memDetail").show();
		cherryAjaxRequest({
			url: url,
			param: null,
			callback: function(data) {
				if(data == "0") {
					$("#memDetail").html(binolwpmbm01Message.nodata);
				} else if(data == "1") {
					searchList();
				} else {
					$("#memDetail").html(data);
				}
			}
		});
		return false;
	});
	
	function searchList() {
		var url = $("#searchMemListUrl").attr("href");
		var params = $("#queryParam").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		$("#memDetail").hide();
		$("#memList").show();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId: '#memDataTable',
				 // 数据URL
				 url: url,
				 // 表格默认排序
				 aaSorting: [[ 6, "desc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "memCode", "sWidth": "8%", "bSortable": false},
				             { "sName": "mobilePhone", "sWidth": "10%", "bSortable": false },
				             { "sName": "memName", "sWidth": "8%", "bSortable": false },
				             { "sName": "birthDay", "sWidth": "8%", "bSortable": false },
				             { "sName": "levelName", "sWidth": "8%", "bSortable": false},
				             { "sName": "changablePoint", "sWidth": "8%", "bSortable": false},
				             { "sName": "joinDate", "sWidth": "8%" }],
				// 横向滚动条出现的临界宽度
				sScrollX: "100%",
				iDisplayLength: 5,
				fnDrawCallback: function() {
					$("#memDataTable").find('a').click(function() {
						$("#memList").hide();
						$("#memDetail").html(binolwpmbm01Message.loading);
						$("#memDetail").show();
						var url = $(this).attr("href");
						cherryAjaxRequest({
							url: url,
							param: null,
							callback: function(data) {
								$("#memDetail").html(data);
							}
						});
						return false;
					});
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	}
});
</script>
