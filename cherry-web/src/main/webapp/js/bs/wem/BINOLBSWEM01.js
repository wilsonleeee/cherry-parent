function BINOLBSWEM01() {};

BINOLBSWEM01.prototype = {	
	/**
	 * 清理执行结果信息
	 */
	"clearActionHtml" : function() {
		$("#errorMessage").empty();
		$("#actionResultDisplay").empty();
	},
		
	"search" : function(){
		BINOLBSWEM01.clearActionHtml();
		var url = $("#search_url").val();
		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		// 查询参数序列化
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#section").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			aaSorting : [[ 19, "asc" ]],
			// 表格列属性设置
			aoColumns : [
		             	{ "sName": "no","bSortable": false,"sClass":"center"},
			            { "sName": "billCode"},
			            { "sName": "applyType","sClass":"center"},
			            { "sName": "applyTime"},
			            { "sName": "applyLevel","sClass":"center","bVisible":false},
			            { "sName": "applyName","sClass":"center"},
			            { "sName": "applyMobile","sClass":"alignRight"},
			            { "sName": "applyOpenID","bVisible":false},
			            { "sName": "applyProvince","sClass":"center","bVisible":false},
			            { "sName": "applyCity","sClass":"center","bVisible":false},
			            { "sName": "applyDesc","bVisible":false},
			            { "sName": "superMobile","sClass":"alignRight"},
			            { "sName": "oldSuperMobile","sClass":"alignRight"},
			            { "sName": "assigner","bVisible":false},
						{ "sName": "assignTime","bVisible":false},
						{ "sName": "auditor","bVisible":false,"sClass":"alignRight"},
						{ "sName": "auditLevel","sClass":"center","bVisible":false},
						{ "sName": "reason","bVisible":false},
						{ "sName": "auditTime","bVisible":false},						
						{ "sName": "status","sClass":"center"},
						{ "sName": "validFlag","sClass":"center"},
						{ "sName": "act","sClass":"center","bSortable":false}
					],	
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	//分配
	"assign":function(url, thisObj, billCode){
		BINOLBSWEM01.clearActionHtml();
		var _this = this;
		var callback = function(data) {
			_this.assignDialogInit(data, thisObj, billCode);
			_this.assignSearch();
		};
		cherryAjaxRequest({
			url:url,
			callback:callback
		})
	},
	
	//弹出框初始化
	"assignDialogInit":function(text, thisObj, billCode) {
		BINOLBSWEM01.clearActionHtml();
		var _this = this;
		var dialogId = $("#dialogInit");
		if($(dialogId).length == 0) {
			$('body').append('<div style="display:none" id="dialogInit"></div> ');
		} else {
			$(dialogId).empty();
		}
		var dialogSetting = {
				dialogInit:dialogId,
				text:text,
				width:640,
				height:'auto',
				minHeight:240,
				zIndex:9999,
				title:$("#assignTitle").html(),
				confirm:$("#assignConfirm").html(),
				confirmEvent:function() {
					_this.assignHandle(thisObj, billCode);
				},
				cancel:$("#dialogClose").html(),
				cancelEvent:function() {
					removeDialog(dialogId);
				}
		};
		openDialog(dialogSetting);
	},
	
	"assignSearch":function() {
		BINOLBSWEM01.clearActionHtml();
		var url = $("#assignSearch_url").val();
		$('#empForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		var params = $("#empForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		url += "&csrftoken=" + $("#parentCsrftoken").val();
		var index = 2;
		if(oTableArr[index]){
			oTableArr[index] = null;
		}
		var tableSetting = {
				// 一页显示页数
				iDisplayLength:10,
				// 表格ID
				tableId : '#empDataTable',
				// 数据URL
				url : url,
				// 排序
				//aaSorting:[[1, "asc"]],
				// 表格列属性设置
				aoColumns : [  { "sName": "No","sWidth": "5%","bSortable": false,"sClass":"center"},
				               { "sName": "EmployeeCode","sWidth": "15%","sClass":"center"},
				               { "sName": "employeeName","sWidth": "15%","sClass":"center"},
				               { "sName": "mobilePhone","sWidth": "15%","sClass":"center"},
				               { "sName": "agentLevel","sWidth": "15%","sClass":"center"}],
				index : index,
				colVisFlag: false,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback: function(){
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	"assignFilter":function() {
		BINOLBSWEM01.clearActionHtml();
		var url = $("#assignSearch_url").val();
		$('#empForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		var params = $("#empForm").serialize();
		url += "?" + params;
		url += "&csrftoken=" + $("#parentCsrftoken").val();
		var _agentLevel = $("#agentLevel").val();
		if("" != _agentLevel && null != _agentLevel) {
			url += "&agentLevel=" + _agentLevel;
		}
		//过滤掉自己和直属下级
		var _mainEmployeeId = $.trim($('#mainEmployeeId').val());
		if(null != _mainEmployeeId && "" != _mainEmployeeId) {
			url +="&mainEmployeeId=" + _mainEmployeeId;
		}
		var oSettings = oTableArr[2].fnSettings();
		oSettings.sAjaxSource = url;
		oTableArr[2].fnDraw();
	},
	
	"assignHandle":function(thisObj, billCode) {
		BINOLBSWEM01.clearActionHtml();
		var url = $("#assign_url").val();
		var employeeId;
		var agentApplyId = thisObj;
		var $popTable = $("#dialogInit");
		var $popInputs = $popTable.find("tbody").find(":input");
		var mobilePhone;
		$popInputs.each(function() {
			var $input = $(this);
			if($input.attr("checked") == "checked") {
				mobilePhone = $input.val();
				employeeId = $input.next().html();
			} 
		})
		if("" != mobilePhone && null != mobilePhone) {
			var params = "agentApplyId=" + agentApplyId + "&mobilePhone=" + mobilePhone + "&billCode=" + billCode + "&employeeId=" + employeeId;
			var callback = function(data) {
				if(data.indexOf('class="actionSuccess"') > -1){
					if(oTableArr[0] != null)
						oTableArr[0].fnDraw();
					removeDialog($popTable);
				} else if(data.indexOf('class="actionError') > -1) {
					return false;
				} else {
					var dataJson = window.JSON.parse(data);
					//下属人数已满
					if(dataJson.resultCode == "-1") {
						alert("下级人数已满，无法分配！");
					}
					return false;
				}
			};
			cherryAjaxRequest({
				url:url,
				param:params,
				callback:callback
			})
		} else {
			alert("手机号为空, 不能进行分配!")
			return false;
		}
	}
};

var BINOLBSWEM01 =  new BINOLBSWEM01();


$(document).ready(function() {
	BINOLBSWEM01.search();
});
