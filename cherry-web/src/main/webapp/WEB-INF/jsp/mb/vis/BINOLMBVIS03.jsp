<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBVIS03">
    <s:text name="global.page.select" id="select_default"/>
    <div class="panel-header">
	    <div class="clearfix"> 
	 	<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
	    <span class="right">
	    	<s:url action="BINOLMBVIS03_addInit" id="addInitUrl"></s:url>
	    	<a class="add" href="${addInitUrl }" onclick="javascript:openWin(this);return false;">
	    	  <span class="ui-icon icon-add"></span>
	    	  <span class="button-text"><s:text name="mbvis03_add"></s:text></span>
	    	</a>
	    </span>
	    </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryForm">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
              	<label style="width: 80px;"><s:text name="mbvis03_visitTypeCode"/></label>
                <s:textfield name="visitTypeCode" cssClass="text"/>
              </p>
              <p>
               	<label style="width: 80px;"><s:text name="mbvis03_visitTypeName"/></label>
               	<s:textfield name="visitTypeName" cssClass="text"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
               	<label><s:text name="mbvis03_validFlag"/></label>
               	<s:select name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#select_default}"></s:select>
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
      <div id="resultDiv" class="section hide">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="global.page.list"/>
        	</strong>
        </div>
        <div class="section-content">
          <table id="resultTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>
                <th><s:text name="mbvis03_number"/></th>
                <th><s:text name="mbvis03_visitTypeCode"/></th>
                <th><s:text name="mbvis03_visitTypeName"/></th>
                <th><s:text name="mbvis03_validFlag"/></th>
                <th><s:text name="mbvis03_operate"/></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div>
    
<div class="hide">
<s:a action="BINOLMBVIS03_search" id="searchUrl"></s:a>
<s:a action="BINOLMBVIS03_updateValid" id="updateValidUrl"></s:a>


<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<div id="dialogClose"><s:text name="global.page.dialogClose" /></div>
<div id="dialogTitle"><s:text name="mbvis03_updateValidTitle" /></div>
<div id="dialogText1"><s:text name="mbvis03_updateValidText1" /></div>
<div id="dialogText2"><s:text name="mbvis03_updateValidText2" /></div>
</div>    
</s:i18n>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

<script type="text/javascript">
$(function(){
	$("#searchButton").click(function(){
		search();
		return false;
	});
	
	function search() {
		var url = $("#searchUrl").attr("href");
		var params = $("#queryForm").serialize();
		url = url + "?" + params;

		$("#resultDiv").show();
		// 表格设置
		tableSetting = {
			 // 表格ID
			 tableId : '#resultTable',
			 // 数据URL
			 url : url,
			 // 排序列
			 aaSorting : [[1, "asc"]],
			 // 表格列属性设置
			 aoColumns : [	
	                    	{ "sName": "number", "sWidth": "5%","bSortable": false},
	                    	{ "sName": "visitTypeCode", "sWidth": "25%"},
	                    	{ "sName": "visitTypeName", "sWidth": "40%"},
	                    	{ "sName": "validFlag", "sWidth": "10%"},
	                    	{ "sName": "operate", "sWidth": "20%","bSortable": false}
 						],
			 sScrollX : "100%",
			 fnDrawCallback: function() {
					$("#resultTable").find(".delete").click(function(){
						var dialogText = $("#dialogText1").text();
						var validFlag = 0;
						if($(this).hasClass('enable')) {
							dialogText = $("#dialogText2").text();
							validFlag = 1;
						}
						if($("#updateValidDialog").length == 0) {
							$("body").append('<div style="display:none" id="updateValidDialog"></div>');
						} else {
							$("#updateValidDialog").empty();
						}
						var url = $("#updateValidUrl").attr("href");
						var param = $(this).parent().find("#visitCategoryId").serialize();
						param += "&" + $(this).parent().find("#modifyTime").serialize();
						param += "&" + $(this).parent().find("#modifyCount").serialize();
						param += "&validFlag=" + validFlag;
					    var dialogSetting = {
					    		dialogInit: "#updateValidDialog",
					    		text: dialogText,
					    		width: 400,
					    		height: 200,
					    		title: $("#dialogTitle").text(),
					    		confirm: $("#dialogConfirm").text(),
					    		cancel: $("#dialogCancel").text(),
								confirmEvent: function(){
									var callback = function(msg) {
						        		if(msg) {
						        			var json = eval("("+msg+")");
						        			if(json.code == 'ok') {
						        				removeDialog("#updateValidDialog");
						        				if(oTableArr[0] != null)oTableArr[0].fnDraw();
						        			} else {
						        				$("#updateValidDialog").html(json.errorMes);
						        				$("#updateValidDialog").dialog( "option", {
						        					buttons: [{
						        						text: $("#dialogClose").text(),
						        					    click: function(){removeDialog("#updateValidDialog");}
						        					}]
						        				});
						        			}
						        		}
						        	};
						        	cherryAjaxRequest({
						        		url: url,
						        		param: param,
						        		callback: callback
						        	});
								},
								cancelEvent: function(){removeDialog("#updateValidDialog");}
						};
					    openDialog(dialogSetting);
						return false;
					});
				}
		};
		
		// 调用获取表格函数
		getTable(tableSetting);
		
		
	}
});

</script>
