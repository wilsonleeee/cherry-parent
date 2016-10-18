<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>

<body>
<div class="main container clearfix">
<div class="panel ui-corner-all">


<s:i18n name="i18n.wp.BINOLWRSRP03">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRSRP03_wrtitle"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRSRP03_detailTitle"></s:text>
		</span>
	  </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryParam" class="inline" csrftoken="false">
          <s:hidden name="startDate"></s:hidden>
          <s:hidden name="endDate"></s:hidden>
          <s:hidden name="employeeId"></s:hidden>
          <s:hidden name="employeeName"></s:hidden>
          <s:hidden name="saleType"></s:hidden>
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
               	<label><s:text name="WRSRP03_bigClassId"/></label>
               	<s:hidden name="bigClassName"></s:hidden>
               	<select name="bigClassId" id="bigClassId">
                  <option value=""><s:text name="global.page.select" /></option>
                </select>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
                <label><s:text name="WRSRP03_smallClassId"/></label>
                <s:hidden name="smallClassName"></s:hidden>
                <select name="smallClassId" id="smallClassId">
                  <option value=""><s:text name="global.page.select" /></option>
                </select>
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
          <cherry:show domId="BINOLWRSRP03EXP">
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
                <th><s:text name="WRSRP03_number" /></th>
                <th><s:text name="WRSRP03_productName" /></th>
                <th><s:text name="WRSRP03_unitCode" /></th>
                <th><s:text name="WRSRP03_barCode" /></th>
                <th><s:text name="WRSRP03_moduleCode" /></th>
                <th><s:text name="WRSRP03_salePrice" /></th>
                <th><s:text name="WRSRP03_quantity" /></th>
                <th><s:text name="WRSRP03_amount" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLWRSRP03_searchDetail" id="searchDetailUrl"></s:a>
<s:a action="BINOLWRSRP03_exportDetailExcel" id="exportExcelUrl"></s:a>
<s:a action="BINOLWRSRP03_exportDetailCsv" id="exportCsvUrl"></s:a>
<input id="pageName" value="detail" type="hidden"/>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />

<script type="text/javascript">
$(function(){
	
	var classList = eval('('+$('#classJson', window.opener.document).val()+')');
	var bigClassId = '${bigClassId}';
	var smallClassId = '${smallClassId}';
	
	function classInit() {
		for(var i = 0; i < classList.length; i++) {
			$("#bigClassId").append('<option value="'+classList[i].bigClassId+'">'+classList[i].bigClassName+'</option>');
		}
		$("#bigClassId").change(function(){
			if($(this).val() != '') {
				$("#bigClassName").val($(this).find("option:selected").text());
			} else {
				$("#bigClassName").val('');
			}
			$("#smallClassName").val('');
			
			var value = $(this).val();
			var $smallClassId = $("#smallClassId");
			var options = '<option value="">'+$smallClassId.find('option').first().html()+'</option>';
			if(value == "") {
				$smallClassId.html(options);
				return;
			}
			for(var i = 0; i < classList.length; i++) {
				if(value == classList[i].bigClassId) {
					var smallClassList = classList[i].list;
					for(var j = 0; j < smallClassList.length; j++) {
						options += '<option value="'+smallClassList[j].smallClassId+'">'+smallClassList[j].smallClassName+'</option>';
					}
					$smallClassId.html(options);
					break;
				}
			}
			
		});
		$("#smallClassId").change(function(){
			if($(this).val() != '') {
				$("#smallClassName").val($(this).find("option:selected").text());
			} else {
				$("#smallClassName").val('');
			}
		});
		
		$("#bigClassId").val(bigClassId);
		$("#bigClassId").change();
		$("#smallClassId").val(smallClassId);
		$("#smallClassId").change();
	}
	classInit();
	
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
				 aaSorting: [[ 6, "desc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "number", "sWidth": "5%", "bSortable": false},
				             { "sName": "productName", "sWidth": "15%", "bSortable": false },
				             { "sName": "unitCode", "sWidth": "15%", "bSortable": false },
				             { "sName": "barCode", "sWidth": "15%", "bSortable": false },
				             { "sName": "moduleCode", "sWidth": "10%", "bSortable": false },
				             { "sName": "salePrice", "sWidth": "10%", "bSortable": false },
				             { "sName": "quantity", "sWidth": "15%"},
				             { "sName": "amount", "sWidth": "15%"}],
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
	
	$("#exportExcel").click(function(){
		if($("#resultDataTable").find(".dataTables_empty:visible").length==0) {
			var url = $("#exportExcelUrl").attr("href") + "?" + getSerializeToken();
    		$("#queryParam").attr("action",url);
    		$("#queryParam").submit();
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


</div>
</div>
</body>
</html>