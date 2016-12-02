<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>

<s:i18n name="i18n.mb.BINOLMBVIS02">
<s:text name="global.page.select" id="select_default"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
	  <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="mbvis02_visitMng"/> &gt; <s:text name="mbvis02_updVisitPlan"/>
	       </span>
	    </div>
	  </div>
	  <div id="actionResultDisplay"></div>
	  <div class="panel-content clearfix">
        <cherry:form id="saveForm" class="inline" csrftoken="false">
			<s:hidden name="visitPlanId" value="%{visitPlanInfo.visitPlanId}"></s:hidden>
			<s:hidden name="modifyTime" value="%{visitPlanInfo.modifyTime}"></s:hidden>
			<s:hidden name="modifyCount" value="%{visitPlanInfo.modifyCount}"></s:hidden>
			<div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="mbvis02_planTitle"/>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
	                <tr>
				      <th style="width: 15%"><s:text name="mbvis02_visitCategoryId"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
				      <td style="width: 85%"><span>
				      	  <s:hidden name="visitTypeCode" value="%{visitPlanInfo.visitTypeCode}" id="visitTypeCode"/>
						  <s:property value="visitPlanInfo.visitTypeName"/>
				      </span></td>
				    </tr>
					<s:if test="%{visitPlanInfo.visitTypeCode == 'VISIT_TYPE_ACT'}">
					<tr>
						<th>活动名称<span class="highlight"><s:text name="global.page.required"></s:text></span></th>
						<td style="line-height: inherit;">
							<span>
							  <input name="campaignRuleID" type="hidden" id="campaignRuleID" value="${visitPlanInfo.campaignRuleID}"/>
							  <span id="campObjCampDiv">
								  (${visitPlanInfo.subCampaignCode})${visitPlanInfo.subCampaignName}
								  <span class="close" style="margin: 0 10px 2px 5px;" id="campObjCampClose"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
							  </span>
							  <span><s:checkbox name="campObjGroupType" fieldValue="1" value="%{visitPlanInfo.campObjGroupType}"/>仅沟通对象</span>
							  <a class="add" id="campObjButton">
								  <span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect" /></span>
							  </a>
							  <span class="error" id="campObjErrorDiv" style="color: red;margin-left: 5px;float: right;"></span>
							</span>
						</td>
					</tr>
					</s:if>
				    <tr>
				      <th><s:text name="mbvis02_visitObjType" /><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
					  <td colspan="3">
					    <span>
					  	<select name="visitObjType" id="visitObjType">
					  	  <option value=""><s:text name="global.page.select" /></option>
					  	  <option value="1" <s:if test="%{visitPlanInfo.visitObjType == 1}">selected="selected"</s:if>><s:text name="mbvis02_visitObjType1" /></option>
					  	  <option value="2" <s:if test="%{visitPlanInfo.visitObjType == 2}">selected="selected"</s:if>><s:text name="mbvis02_visitObjType2" /></option>
					  	</select>
					    </span>
					    <span class="hide">
					      <label id="visitObjLabel"></label>
					      <s:hidden name="visitObjJson" value="%{visitPlanInfo.visitObjJson}"></s:hidden>
					      <a id="visitObjQuery" class="search">
					        <span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect" /></span>
					      </a>
					    </span>
					    <span class="hide">
					      <label id="visitObjNameLabel">
					      <s:if test='%{visitPlanInfo.visitObjName != null && !"".equals(visitPlanInfo.visitObjName)}'>
					      <s:property value="visitPlanInfo.visitObjName"/>
					      <span class="close" style="margin: 0 0 2px 5px;">
							<span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span>
						  </span>
					      </s:if>
					      </label>
					      <s:hidden name="visitObjCode" value="%{visitPlanInfo.visitObjCode}"></s:hidden>
					      <s:hidden name="visitObjName" value="%{visitPlanInfo.visitObjName}"></s:hidden>
					      <a id="visitObjImport" class="search">
					        <span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect" /></span>
					      </a>
					    </span>
					    <span class="hide" style="float:right;">
					      <label></label>
					      <a id="searchVisitObj" class="search">
					        <span class="ui-icon icon-search"></span><span class="button-text"><s:text name="mbvis02_searchVisitObj" /></span>
					      </a>
					    </span>
					  </td>
				    </tr>
				    <tr>
				      <th><s:text name="mbvis02_visitDateType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
					  <td>
					    <span>
					  	<select name="visitDateType" id="visitDateType" style="width:auto;">
					  	  <option value=""><s:text name="global.page.select" /></option>
					  	  <option value="1" <s:if test="%{visitPlanInfo.visitDateType == 1}">selected="selected"</s:if>><s:text name="mbvis02_visitDateType1" /></option>
					  	  <option value="2" <s:if test="%{visitPlanInfo.visitDateType == 2}">selected="selected"</s:if>><s:text name="mbvis02_visitDateType2" /></option>
					  	  <option value="3" <s:if test="%{visitPlanInfo.visitDateType == 3}">selected="selected"</s:if>><s:text name="mbvis02_visitDateType3" /></option>
					  	  <option value="4" <s:if test="%{visitPlanInfo.visitDateType == 4}">selected="selected"</s:if>><s:text name="mbvis02_visitDateType4" /></option>
					  	</select>
					  	</span>
					  	<span class="hide">
						  	<s:textfield name="visitStartDate" cssClass="date" value="%{visitPlanInfo.visitStartDate}"></s:textfield>-
						  	<s:textfield name="visitEndDate" cssClass="date" value="%{visitPlanInfo.visitEndDate}"></s:textfield>
					  	</span>
					  	<span class="hide">
						  	<select name="visitDateRelative" style="width:auto;">
						  	  <option value="1"><s:text name="mbvis02_visitDateRelative1" /></option>
						  	  <option value="2" <s:if test="%{visitPlanInfo.visitDateRelative == 2}">selected="selected"</s:if>><s:text name="mbvis02_visitDateRelative2" /></option>
						  	</select>
						  	<s:textfield name="visitDateValue" cssClass="text" cssStyle="width:40px" value="%{visitPlanInfo.visitDateValue}"></s:textfield>
						  	<select name="visitDateUnit" style="width:auto;">
						  	  <option value="1"><s:text name="mbvis02_visitDateUnit1" /></option>
						  	  <option value="2" <s:if test="%{visitPlanInfo.visitDateUnit == 2}">selected="selected"</s:if>><s:text name="mbvis02_visitDateUnit2" /></option>
						  	</select>
						  	<label><s:text name="mbvis02_endDate" /></label>
						  	<s:textfield name="validValue" cssClass="text" cssStyle="width:40px" value="%{visitPlanInfo.validValue}"></s:textfield>
						  	<select name="validUnit" style="width:auto;">
						  	  <option value="1"><s:text name="mbvis02_visitDateUnit1" /></option>
						  	  <option value="2" <s:if test="%{visitPlanInfo.validUnit == 2}">selected="selected"</s:if>><s:text name="mbvis02_visitDateUnit2" /></option>
						  	</select>
					  	</span>
					  </td>
				    </tr>
				    <tr>
				      <th><s:text name="mbvis02_paperId"></s:text></th>
				      <td><span>
				      	<s:select name="paperId" list="paperList" listKey="paperId" listValue="paperName" headerKey="" headerValue="%{#select_default}" value="%{visitPlanInfo.paperId}"></s:select>
				      </span></td>
				    </tr>
				    <tr>
				      <th><s:text name="mbvis02_visitDes"></s:text></th>
				      <td><span>
				      	<s:textarea name="visitDes" cssClass="text" value="%{visitPlanInfo.visitDes}"></s:textarea>
				      </span></td>
				    </tr>
                </table>
			  </div>
			</div>
			<div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="mbvis02_planStrategy"/>
              	</strong>
              	<span class="error" id="execDateErrorDiv" style="color: red;margin-left: 5px;"></span>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
	                <tr>
				      <th style="width: 15%"><s:text name="mbvis02_startDate"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
				      <td style="width: 85%"><span>
				      	<s:textfield name="startDate" cssClass="date" value="%{visitPlanInfo.startDate}"></s:textfield>
				      </span></td>
				    </tr>
				    <tr>
				      <th><s:text name="mbvis02_endDate"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
				      <td>
				      	<span>
				      	<select id="planDate" name="planDate">
					  	  <option value="1"><s:text name="mbvis02_planDate1" /></option>
					  	  <option value="2" <s:if test="%{visitPlanInfo.endDate != null}">selected="selected"</s:if>><s:text name="mbvis02_planDate2" /></option>
					  	</select>
					  	</span>
					  	<span class="hide"><s:textfield name="endDate" cssClass="date" value="%{visitPlanInfo.endDate}"></s:textfield></span>
				      </td>
				    </tr>
                </table>
			  </div>
			</div>
		</cherry:form>	
            
            <div class="center clearfix">
       			<button class="save" type="button" id="saveButton">
            		<span class="ui-icon icon-save"></span>
            		<span class="button-text"><s:text name="global.page.save"/></span>
             	</button>
	            <button class="close" type="button" onclick="window.close();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
      </div>
</div>
</div>

<div class="hide">
<s:a action="BINOLMBVIS02_update" id="updateUrl"></s:a>
<s:a action="BINOLCM02_initCampObjDialog" id="initCampObjDialogUrl" namespace="/common"></s:a>

<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<div id="dialogClose"><s:text name="global.page.dialogClose" /></div>
<div id="dialogTitle"><s:text name="mbvis02_setVisitObj" /></div>
<div id="dialogTitle1"><s:text name="mbvis02_visitObjType" /></div>
<div id="execDateError"><s:text name="mbvis02_execDateError" /></div>
</div>
</s:i18n>

<script type="text/javascript">
$(function(){
	
	$('#startDate').cherryDate({
		minDate:'${sysDate}',
		beforeShow: function(input){
			var value = $('#endDate').val();
			return [value,'maxDate'];
		}
	});
	$('#endDate').cherryDate({
		beforeShow: function(input){
			var value = $('#startDate').val();
			if(!value) {
				value = '${sysDate}';
			}
			return [value,'minDate'];
		}
	});
	$('#visitStartDate').cherryDate({
		minDate:'${sysDate}',
		beforeShow: function(input){
			var value = $('#visitEndDate').val();
			return [value,'maxDate'];
		}
	});
	$('#visitEndDate').cherryDate({
		beforeShow: function(input){
			var value = $('#visitStartDate').val();
			if(!value) {
				value = '${sysDate}';
			}
			return [value,'minDate'];
		}
	});
	
	// 回访时间绑定事件
	$("#visitDateType").change(function(){
		var value = $(this).val();
		if(value == 1) {
			$(this).parent().next().show();
			$(this).parent().next().next().hide();
		} else if(value == 2 || value == 3 || value == 4) {
			$(this).parent().next().hide();
			$(this).parent().next().next().show();
		} else {
			$(this).parent().next().hide();
			$(this).parent().next().next().hide();
		}
	});
	
	// 有效期绑定事件
	$("#planDate").change(function(){
		var value = $(this).val();
		if(value == 1) {
			$(this).parent().next().hide();
		} else if(value == 2) {
			$(this).parent().next().show();
		} else {
			$(this).parent().next().hide();
		}
	});
	
	// 回访对象绑定事件
	$("#visitObjType").change(function(){
		var value = $(this).val();
		if(value == 1) {
			$(this).parent().next().show();
			$(this).parent().next().next().hide();
			$(this).parent().next().next().next().show();
		} else if(value == 2) {
			$(this).parent().next().hide();
			$(this).parent().next().next().show();
			$(this).parent().next().next().next().show();
		} else {
			$(this).parent().next().hide();
			$(this).parent().next().next().hide();
			$(this).parent().next().next().next().hide();
		}
	});
	
	// 设置回访对象查询条件
	$("#visitObjQuery").click(function(){
		if($("#searchConDialog").length == 0) {
			$("body").append('<div style="display:none" id="searchConDialog"></div>');
		} else {
			$("#searchConDialog").empty();
		}
		var url = getBaseUrl()+'/common/BINOLCM33_init';
		var params = "reqContent=" + $("#visitObjJson").val();
		var dialogSetting = {
			dialogInit: "#searchConDialog",
			width: 900,
			height: 550,
			title: $("#dialogTitle").text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			//确认按钮
			confirmEvent: function(){
				//将搜索条件弹出框信息json格式化
				var searchParam = getMemCommonSearchJson("searchConDialog");
				//var searchParam = $("#searchConDialog").find(":input").serializeForm2Json(false);
				if(searchParam){
					$("#visitObjJson").val(searchParam);
					getConInfo(searchParam);
				}
				// 关闭搜索条件弹出框
				removeDialog("#searchConDialog");
			},
			//关闭按钮
			cancelEvent: function(){removeDialog("#searchConDialog");}
		};
		openDialog(dialogSetting);
		cherryAjaxRequest({
			url: url,
			param : params,
			callback: function(msg) {
				$("#searchConDialog").html(msg);
			}
		});
	});
	
	// 取得查询条件描述信息
	function getConInfo(json) {
		var url = getBaseUrl()+'/common/BINOLCM33_conditionDisplay';
		var params = "reqContent=" + json;
		cherryAjaxRequest({
			url: url,
			param : params,
			callback: function(msg) {
				if(msg) {
					var close = '<span class="close" style="margin: 0 0 2px 5px;">'+
								'<span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span>'+
								'</span>';
					$("#visitObjLabel").html(msg+close);
					$("#visitObjLabel").find(".close").click(function(){
						$("#visitObjLabel").empty();
						$("#visitObjJson").val('');
					});
				} else {
					$("#visitObjLabel").empty();
					$("#visitObjJson").val('');
				}
			}
		});
	}
	
	// 回访对象EXCEL导入处理
	$("#visitObjImport").click(function(){
		if($("#visitObjImportDialog").length == 0) {
			$("body").append('<div style="display:none" id="visitObjImportDialog"></div>');
		} else {
			$("#visitObjImportDialog").empty();
		}
		var url = getBaseUrl()+'/mb/BINOLMBVIS02_importInit';
		var param = $("#visitObjCode").serialize() + '&' + $("#visitObjName").serialize();
		var dialogSetting = {
			dialogInit: "#visitObjImportDialog",
			width: 600,
			height: 400,
			title: $("#dialogTitle").text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			//确认按钮
			confirmEvent: function(){
				var visitObjCode = $("#visitObjImportDialog").find("#visitObjCodeImport").val();
				var visitObjName = $("#visitObjImportDialog").find("#visitObjNameImport").val();
				if(visitObjCode) {
					$("#visitObjCode").val(visitObjCode);
					$("#visitObjName").val(visitObjName);
					var close = '<span class="close" style="margin: 0 0 2px 5px;">'+
								'<span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span>'+
								'</span>';
					$("#visitObjNameLabel").html(visitObjName+close);
					$("#visitObjNameLabel").find(".close").click(function(){
						$("#visitObjNameLabel").empty();
						$("#visitObjCode").val('');
						$("#visitObjName").val('');
					});
				}
				removeDialog("#visitObjImportDialog");
			},
			//关闭按钮
			cancelEvent: function(){removeDialog("#visitObjImportDialog");}
		};
		openDialog(dialogSetting);
		cherryAjaxRequest({
			url: url,
			param : param,
			callback: function(msg) {
				$("#visitObjImportDialog").html(msg);
			}
		});
	});
	
	// 查询回访对象
	$("#searchVisitObj").click(function(){
		setVisitObjJson();
		var visitObjType = $("#visitObjType").val();
		var visitObjJson = $("#visitObjJson").val();
		var visitObjCode = $("#visitObjCode").val();
		if(visitObjType) {
			if(visitObjType == '1') {
				if(!visitObjJson) {
					return false;
				}
			} else if(visitObjType == '2') {
				if(!visitObjCode) {
					return false;
				}
			}
		} else {
			return false;
		}
		if($("#searchMemDialog").length == 0) {
			$("body").append('<div style="display:none" id="searchMemDialog"></div>');
		} else {
			$("#searchMemDialog").empty();
		}
		var dialogSetting = {
			dialogInit: "#searchMemDialog",
			text: '',
			width: 	800,
			height: 430,
			title: 	$("#dialogTitle1").text(),
			confirm: $("#dialogClose").text(),
			confirmEvent: function(){removeDialog("#searchMemDialog");}
		};
		openDialog(dialogSetting);
		var callback = function(msg) {
			$("#searchMemDialog").html(msg);
			
			if(oTableArr[10]) {
				oTableArr[10] = null;
			}
			var searchUrl = getBaseUrl()+'/mb/BINOLMBVIS02_searchMem' + '?' + getSerializeToken() + '&' + $("#visitObjType").serialize();
			if(visitObjType == '1') {
				searchUrl += '&' + $("#visitObjJson").serialize();
			} else if(visitObjType == '2') {
				searchUrl += '&' + $("#visitObjCode").serialize();
			}
			var tableSetting = {
					 // 表格ID
					 tableId : '#memSearchDataTable',
					 // 一页显示页数
					 iDisplayLength:10,
					 // 数据URL
					 url : searchUrl,
					 // 表格默认排序
					 aaSorting : [[ 4, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false}, 	
					                { "sName": "memCode", "sWidth": "15%", "bSortable": false},
									{ "sName": "memName", "sWidth": "15%", "bSortable": false},    
									{ "sName": "mobilePhone", "sWidth": "15%", "bSortable": false},
									{ "sName": "joinDate", "sWidth": "15%"},
									{ "sName": "birthDay", "sWidth": "15%", "bSortable": false},
									{ "sName": "counterCode", "sWidth": "15%", "bSortable": false}],               
					index:10
			 };
			// 调用获取表格函数
			getTable(tableSetting);
		};
		var url = getBaseUrl()+'/mb/BINOLMBVIS02_searchMemInit';
		cherryAjaxRequest({
			url: url,
			param: null,
			callback: callback
		});
	});
	
	cherryValidate({
		formId: 'saveForm',
		rules: {
			visitObjType: {required: true},
			visitDateType: {required: true},
			startDate: {required: true, dateValid: true},
			endDate: {dateValid: true},
			visitStartDate: {dateValid: true},
			visitEndDate: {dateValid: true},
			visitDateValue: {integerValid: true},
			validValue: {integerValid: true}
		}
	});
	
	function checkExecDate() {
		var visitDateType = $("#visitDateType").val();
		if(visitDateType == 1) {
			var visitEndDate = $("#visitEndDate").val();
			if(visitEndDate) {
				var startDate = $("#startDate").val();
				if(startDate > visitEndDate) {
					return false;
				} else {
					var endDate = $("#endDate").val();
					if(endDate) {
						if(endDate > visitEndDate) {
							return false;
						}
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}

	var visitTypeCodeCamp = "VISIT_TYPE_ACT";
	function checkCampObj() {
		var visitTypeCode = $("#visitTypeCode").val();
		if(visitTypeCode == visitTypeCodeCamp) {
			var campaignRuleID = $("#campaignRuleID").val();
			if(campaignRuleID) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
	
	$("#saveButton").click(function(){
		$("#execDateErrorDiv").empty();
		if(!$('#saveForm').valid()) {
			return false;
		}
		if(!checkExecDate()) {
			$("#execDateErrorDiv").html($("#execDateError").html());
			return false;
		}
		if(!checkCampObj()) {
			$("#campObjErrorDiv").html("必须选择一个活动");
			return false;
		}
		save();
		return false;
	});

	function setVisitObjJson() {
		var visitTypeCode = $("#visitTypeCode").val();
		if(visitTypeCode == visitTypeCodeCamp) {
			var visitObjJsonObj = {};
			var visitObjJson = $("#visitObjJson").val();
			if(visitObjJson) {
				visitObjJsonObj = eval("("+visitObjJson+")");
			}
			visitObjJsonObj.campaignRuleID = $("#campaignRuleID").val();
			if($("#campObjGroupType").is(':checked')) {
				visitObjJsonObj.campObjGroupType = $("#campObjGroupType").val();
			} else {
				visitObjJsonObj.campObjGroupType = '';
			}
			$("#visitObjJson").val(JSON.stringify(visitObjJsonObj));
		} else {
			var visitObjJson = $("#visitObjJson").val();
			if(visitObjJson) {
				var visitObjJsonObj = eval("("+visitObjJson+")");
				visitObjJsonObj.campaignRuleID = '';
				visitObjJsonObj.campObjGroupType = '';
				$("#visitObjJson").val(JSON.stringify(visitObjJsonObj));
			}
		}
	}
	
	function save() {
		setVisitObjJson();
		var url = $("#updateUrl").attr("href");
		var params = $("#saveForm").serialize();
		var callback = function(msg) {
			if($('#actionResultBody').length > 0) {
				if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
			}
		};
		cherryAjaxRequest({
			url: url,
			param : params,
			callback: callback
		});
	}
	$("#campObjButton").click(function(){
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#campaignRuleID").val($checkedRadio.val());
				var html = '(' + $checkedRadio.parent().next().text() + ')' + $checkedRadio.parent().next().next().text();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" id="campObjCampClose"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#campObjCampDiv").html(html);
				$("#campObjCampDiv").next().show();
				$("#campObjCampClose").click(function(){
					$("#campaignRuleID").val("");
					$("#campObjCampDiv").empty();
					$("#campObjCampDiv").next().hide();
				});
			}
		}
		var url = $("#initCampObjDialogUrl").attr("href");
		var value = $("#campaignRuleID").val();
		popCampObjList(url, null, value, callback);
		return false;
	});
	
	$("#visitObjType").change();
	$("#visitDateType").change();
	$("#planDate").change();
	if($("#visitObjJson").val()) {
		getConInfo($("#visitObjJson").val());
	}
	$("#visitObjName").find(".close").click(function(){
		$("#visitObjName").empty();
		$("#visitObjCode").val('');
	});
	$("#campObjCampClose").click(function(){
		$("#campaignRuleID").val("");
		$("#campObjCampDiv").empty();
		$("#campObjCampDiv").next().hide();
	});
});

</script>
