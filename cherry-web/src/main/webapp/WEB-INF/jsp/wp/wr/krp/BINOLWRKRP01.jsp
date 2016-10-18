<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRKRP01">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRKRP01_wrtitle"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRKRP01_title"></s:text>
		</span>
	  </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryParam" class="inline">
          <s:hidden name="params"></s:hidden>
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
               	<label><s:text name="WRKRP01_bigClassId"/></label>
               	<s:hidden name="bigClassName"></s:hidden>
               	<select name="bigClassId" id="bigClassId">
                  <option value=""><s:text name="global.page.select" /></option>
                </select>
              </p>
              <p>
                <label><s:text name="WRKRP01_smallClassId"/></label>
                <s:hidden name="smallClassName"></s:hidden>
                <select name="smallClassId" id="smallClassId">
                  <option value=""><s:text name="global.page.select" /></option>
                </select>
              </p>
              <p>
               	<label><s:text name="WRKRP01_logicInventoryInfoId"/></label>
               	<s:hidden name="logicInventoryName"></s:hidden>
               	<s:select list="logicInventoryList" listKey="logicInventoryInfoId" listValue="logicInventoryName" name="logicInventoryInfoId" headerKey="" headerValue="%{#select_default}"></s:select>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
	            <label><s:text name="WRKRP01_productName"/></label>
              	<s:textfield name="nameTotal" cssClass="text"/>
              	<s:if test='"2".equals(type)'><input type="hidden" id="productId" name="productId" value=""/></s:if>
              	<s:else><input type="hidden" id="prtVendorId" name="prtVendorId" value=""/></s:else>
	          </p>
              <p>
               	<label><s:text name="WRKRP01_validFlag"/></label>
               	<s:iterator value='#application.CodeTable.getCodes("1137")'>
                	<input type="radio" name="validFlag" value='<s:property value="CodeKey" />' <s:if test="1 == CodeKey">checked</s:if>/><s:property value="Value" />
                </s:iterator>
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
          <cherry:show domId="BINOLWRKRP01EXP">
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
                <th><s:text name="WRKRP01_number" /></th>
                <th><s:text name="WRKRP01_bigClassCode" /></th>
                <th><s:text name="WRKRP01_bigClassName" /></th>
                <th><s:text name="WRKRP01_smallClassCode" /></th>
                <th><s:text name="WRKRP01_smallClassName" /></th>
                <th><s:text name="WRKRP01_productName" /></th>
                <th><s:text name="WRKRP01_unitCode" /></th>
                <th><s:text name="WRKRP01_barCode" /></th>
                <th><s:text name="WRKRP01_quantity" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLWRKRP01_search" id="searchUrl"></s:a>
<s:a action="BINOLWRKRP01_exportCheck" id="exportCheckUrl"></s:a>
<s:a action="BINOLWRKRP01_exportExcel" id="exportExcelUrl"></s:a>
<s:a action="BINOLWRKRP01_exportCsv" id="exportCsvUrl"></s:a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />

<script type="text/javascript">
$(function(){
	
	var classList = eval('('+'${classJson}'+')');
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
	
	$("#logicInventoryInfoId").change(function(){
		if($(this).val() != '') {
			$("#logicInventoryName").val($(this).find("option:selected").text());
		} else {
			$("#logicInventoryName").val('');
		}
	});
	
	if($('#productId').length > 0){
		// 产品选择绑定
		productBinding({elementId:"nameTotal",showNum:20,targetId:'productId',validFlag:'validFlag'});
	}else{
		// 产品选择绑定
		productBinding({elementId:"nameTotal",showNum:20,validFlag:'validFlag'});
	}
	
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
				             { "sName": "bigClassCode", "sWidth": "10%", "bSortable": false },
				             { "sName": "bigClassName", "sWidth": "10%", "bSortable": false },
				             { "sName": "smallClassCode", "sWidth": "10%", "bSortable": false },
				             { "sName": "smallClassName", "sWidth": "10%", "bSortable": false },
				             { "sName": "productName", "sWidth": "15%", "bSortable": false },
				             { "sName": "unitCode", "sWidth": "15%", "bSortable": false},
				             { "sName": "barCode", "sWidth": "15%", "bSortable": false},
				             { "sName": "quantity", "sWidth": "10%", "bSortable": false}],
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
