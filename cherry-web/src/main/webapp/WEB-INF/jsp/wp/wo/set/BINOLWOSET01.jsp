<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWOSET01">
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WOSET01_wotitle"></s:text>&nbsp;&gt;&nbsp;<s:text name="WOSET01_title"></s:text>
		</span>
		
		<span class="right">
			<a class="add" href="javascript:void(0);" id="syncBA">
		    	<span class="ui-icon icon-add"></span>
		    	<span class="button-text"><s:text name="WOSET01_syncBATitle"/></span>
		      </a>
	      <a class="add" href="javascript:void(0);" id="addBA">
	    	<span class="ui-icon icon-add"></span>
	    	<span class="button-text"><s:text name="global.page.add"></s:text></span>
	      </a>
	    </span>
	  </div>
	  <div class="hide" id="dialogInit"></div>
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
               	<label><s:text name="WOSET01_employeeCode"/></label>
               	<s:textfield name="employeeCodeQ" cssClass="text"/>
              </p>
              <p>
                <label><s:text name="WOSET01_employeeName"/></label>
                <s:textfield name="employeeNameQ" cssClass="text"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
               	<label><s:text name="WOSET01_identityCard"/></label>
               	<s:textfield name="identityCardQ" cssClass="text"/>
              </p>
              <p>
                <label><s:text name="WOSET01_mobilePhone"/></label>
                <s:textfield name="mobilePhoneQ" cssClass="text"/>
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
          <table id="resultDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="WOSET01_number" /></th>
                <th><s:text name="WOSET01_employeeCode" /></th>
                <th><s:text name="WOSET01_employeeName" /></th>
                <th><s:text name="WOSET01_identityCard" /></th>
                <th><s:text name="WOSET01_mobilePhone" /></th>
                <th><s:text name="WOSET01_operate" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
    
<div class="hide">
<div id="loading"><s:text name="global.page.loading" /></div>
<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<div id="dialogClose"><s:text name="global.page.dialogClose" /></div>
<div id="dialogAdd"><s:text name="global.page.add" /></div>
<div id="addBATitle"><s:text name="WOSET01_addBATitle" /></div>
<div id="updBATitle"><s:text name="WOSET01_updBATitle" /></div>
<div id="delBATitle"><s:text name="WOSET01_delBATitle" /></div>
<div id="delBAConfirm"><s:text name="WOSET01_delBAConfirm" /></div>
</div>   
</s:i18n>

<div class="hide">
<s:a action="BINOLWOSET01_search" id="searchBAInfoUrl"></s:a>
<s:a action="BINOLWOSET01_addInit" id="addInitUrl"></s:a>
<s:a action="BINOLWOSET01_add" id="addUrl"></s:a>
<s:a action="BINOLWOSET01_updateInit" id="updateInitUrl"></s:a>
<s:a action="BINOLWOSET01_update" id="updateUrl"></s:a>
<s:a action="BINOLWOSET01_delete" id="deleteUrl"></s:a>
<s:a action="BINOLWOSET01_check" id="checkUrl"></s:a>
<s:a action="BINOLWOSET01_syncBa" id="syncBAUrl"></s:a>
<s:hidden name="mode"></s:hidden>
<s:hidden name="mobileRule"></s:hidden>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

<script type="text/javascript">
$(function(){
	
	$("#searchButton").click(function(){
		searchList();
		return false;
	});
	
	function searchList() {
		var url = $("#searchBAInfoUrl").attr("href");
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
				 aaSorting: [[ 1, "asc" ]],
				 // 表格列属性设置
				 aoColumns: [{ "sName": "number", "sWidth": "5%", "bSortable": false},
				             { "sName": "employeeCode", "sWidth": "15%" },
				             { "sName": "employeeName", "sWidth": "20%", "bSortable": false },
				             { "sName": "identityCard", "sWidth": "30%", "bSortable": false },
				             { "sName": "mobilePhone", "sWidth": "15%", "bSortable": false },
				             { "sName": "operate", "sWidth": "15%", "bSortable": false }],
				// 横向滚动条出现的临界宽度
				sScrollX: "100%",
				iDisplayLength: 25,
				fnDrawCallback: function() {
					$("#resultDataTable").find(".edit").click(function(){
						
						if($("#updBADialog").length == 0) {
							$("body").append('<div style="display:none" id="updBADialog"></div>');
						} else {
							$("#updBADialog").empty();
						}
					    var dialogSetting = {
					    		dialogInit: "#updBADialog",
					    		text: $("#loading").text(),
					    		width: 450,
					    		height: 250,
					    		title: $("#updBATitle").val(),
					    		confirm: $("#dialogConfirm").text(),
					    		cancel: $("#dialogCancel").text(),
								confirmEvent: function(){
									if($("#updBADialog").find("#employeeId").length == 0) {
										removeDialog("#updBADialog");
										return false;
									}
									if(!$('#saveForm').valid()) {
										return false;
									}
									var callback = function(msg) {
						        		if(msg) {
						        			var json = eval("("+msg+")");
						        			if(json.code == 'ok') {
						        				removeDialog("#updBADialog");
						        				if(oTableArr[0] != null)oTableArr[0].fnDraw();
						        			} else {
						        				$("#updBADialog").html(json.errorMes);
						        				$("#updBADialog").dialog( "option", {
						        					buttons: [{
						        						text: $("#dialogClose").text(),
						        					    click: function(){removeDialog("#updBADialog");}
						        					}]
						        				});
						        			}
						        		}
						        	};
						        	cherryAjaxRequest({
						        		url: $("#updateUrl").attr("href"),
						        		param: $("#saveForm").serialize(),
						        		callback: callback
						        	});
								},
								cancelEvent: function(){removeDialog("#updBADialog");},
								open: function(){
									$(this).bind("keypress.ui-dialog", function(event) { 
										if (event.keyCode == $.ui.keyCode.ENTER) {
											$(".ui-dialog-buttonpane button").first().click();
											return false;
										} 
									});
								}
						};
					    openDialog(dialogSetting);
					    var addInitUrl = $("#updateInitUrl").attr("href");
						var param = $(this).parent().find("#employeeId").serialize() + '&' + $("#mode").serialize();
						var callback = function(msg) {
							$("#updBADialog").html(msg);
						};
						cherryAjaxRequest({
							url: addInitUrl,
							param: param,
							callback: callback
						});
						return false;
					});
					$("#resultDataTable").find(".delete").click(function(){
						
						if($("#delBADialog").length == 0) {
							$("body").append('<div style="display:none" id="delBADialog"></div>');
						} else {
							$("#delBADialog").empty();
						}
						var url = $("#deleteUrl").attr("href");
						var param = $(this).parent().find("#employeeId").serialize();
					    var dialogSetting = {
					    		dialogInit: "#delBADialog",
					    		text: $("#delBAConfirm").text(),
					    		width: 400,
					    		height: 200,
					    		title: $("#delBATitle").val(),
					    		confirm: $("#dialogConfirm").text(),
					    		cancel: $("#dialogCancel").text(),
								confirmEvent: function(){
									var callback = function(msg) {
						        		if(msg) {
						        			var json = eval("("+msg+")");
						        			if(json.code == 'ok') {
						        				removeDialog("#delBADialog");
						        				if(oTableArr[0] != null)oTableArr[0].fnDraw();
						        			} else {
						        				$("#delBADialog").html(json.errorMes);
						        				$("#delBADialog").dialog( "option", {
						        					buttons: [{
						        						text: $("#dialogClose").text(),
						        					    click: function(){removeDialog("#delBADialog");}
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
								cancelEvent: function(){removeDialog("#delBADialog");}
						};
					    openDialog(dialogSetting);
						return false;
					});
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	}
	
	$("#addBA").click(function(){
		if($("#addBADialog").length == 0) {
			$("body").append('<div style="display:none" id="addBADialog"></div>');
		} else {
			$("#addBADialog").empty();
		}
	    var dialogSetting = {
	    		dialogInit: "#addBADialog",
	    		text: $("#loading").text(),
	    		width: 400,
	    		height: 200,
	    		title: $("#addBATitle").val(),
	    		confirm: $("#dialogConfirm").text(),
	    		cancel: $("#dialogCancel").text(),
				confirmEvent: function(){
					if(!$('#saveForm').valid()) {
						return false;
					}
					var callback = function(msg) {
						$("#addBADialog").html(msg);
						if($("#addBADialog").find("#employeeId").length != 0 || $("#addBADialog").find("#mode").length != 0) {
							$("#addBADialog").dialog( "option", {
								width: 450,
					    		height: 250,
	        					buttons: [{
	        						text: $("#dialogAdd").text(),
	        					    click: function(){
	        					    	if(!$('#saveForm').valid()) {
	        								return false;
	        							}
	        					    	var callback = function(msg){
	        					    		if(msg) {
							        			var json = eval("("+msg+")");
							        			if(json.code == 'ok') {
							        				removeDialog("#addBADialog");
							        				if(oTableArr[0] != null)oTableArr[0].fnDraw();
							        			} else {
							        				$("#addBADialog").html(json.errorMes);
							        				$("#addBADialog").dialog( "option", {
							        					width: 400,
							        		    		height: 200,
							        					buttons: [{
							        						text: $("#dialogClose").text(),
							        					    click: function(){removeDialog("#addBADialog");}
							        					}]
							        				});
							        			}
							        		}
	        					    	};
	        					    	cherryAjaxRequest({
	        				        		url: $("#addUrl").attr("href"),
	        				        		param: $("#saveForm").serialize(),
	        				        		callback: callback
	        				        	});
	        					    }
	        					},{
	        						text: $("#dialogClose").text(),
	        					    click: function(){removeDialog("#addBADialog");}
	        					}]
	        				});
						} else {
							$("#addBADialog").dialog( "option", {
	        					buttons: [{
	        						text: $("#dialogClose").text(),
	        					    click: function(){removeDialog("#addBADialog");}
	        					}]
	        				});
						}
		        	};
		        	cherryAjaxRequest({
		        		url: $("#checkUrl").attr("href"),
		        		param: $("#saveForm").serialize(),
		        		callback: callback
		        	});
				},
				cancelEvent: function(){removeDialog("#addBADialog");},
				open: function(){
					$(this).bind("keypress.ui-dialog", function(event) { 
						if (event.keyCode == $.ui.keyCode.ENTER) {
							$(".ui-dialog-buttonpane button").first().click();
							return false;
						} 
					});
				}
		};
	    openDialog(dialogSetting);
	    var addInitUrl = $("#addInitUrl").attr("href");
		var callback = function(msg) {
			$("#addBADialog").html(msg);
		};
		cherryAjaxRequest({
			url: addInitUrl,
			param: $("#mode").serialize(),
			callback: callback
		});
	});
	
	$("#syncBA").click(function(){
		
		cherryAjaxRequest({
    		url: $("#syncBAUrl").attr("href"),
    		callback: function(data){
    			var param_map = eval("("+data+")");
    			var code=param_map.code;
    			var msg="";
    			if(code == "ok"){
    				msg="BA同步成功";
    			}else{
    				msg="BA同步失败，请稍后再试";
    			}
    			var dialogSetting = {
    					dialogInit: "#dialogInit",
    					text:  msg,
    					width: 	300,
    					height: 150,
    					title: 	"BA同步消息",
    					confirm: "确定",
    					confirmEvent: function(){removeDialog("#dialogInit");},
    					cancelEvent: function(){removeDialog("#dialogInit");}
    				};
    				openDialog(dialogSetting);
    		}
    	});
		searchList();
	});
	
	
	$("#searchButton").click();
});
</script>
