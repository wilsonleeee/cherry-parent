
function BINOLMBVIS01() {
	
};

BINOLMBVIS01.prototype = {
		
	"search" : function() {
		$("input@[id=allSelect]").prop("checked",false);
		var url = $("#VisitTaskListUrl").attr("href");
		var params= $("#VisitTaskInfoCherryForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#pointInfo").removeClass("hide");
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 index : 1,
				 // 表格默认排序
				 aaSorting : [[ 0, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "BIN_VisitTaskID","bSortable": false,"sWidth": "2%"}, 
				                { "sName": "visitType" },
				                { "sName": "memberName" ,"bVisible": false},
								{ "sName": "birthDay","bVisible": false },
								{ "sName": "memberCode" },
								{ "sName": "CounterNameIF" },
								{ "sName": "StartTime"},
								{ "sName": "EndTime"},
								{ "sName": "EmployeeName" },
								{ "sName": "taskState" },
								{ "sName": "visitTime" },
								{ "sName": "visitResult" },
								{ "sName": "operate","bSortable": false}],
				
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 滚动体的宽度
			sScrollXInner:"",
			// 固定列数
			fixedColumns : 2,
			
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
	 //弹出任務詳情按钮对话框			
	"details" : function(obj) {
		var url = $("#detailsurl").attr("href");
		var _this=this;
		 url = url + '?visitTaskID=' + obj;
		var callback = function(msg) {
			var dialogSetting = {
					dialogInit: "#dialogInit",
					bgiframe: true,
					width: 	600,
					height: 560,
					text: msg,
					title: 	$("#detailsTitle").text(),
					confirm: $("#confirm").text(),
					confirmEvent: function(){removeDialog("#dialogInit");}
				};
				
				//弹出框体
				openDialog(dialogSetting);
				
//				_this.getLogiDepotByAjax("main_form");
		};
		cherryAjaxRequest({
			url: url,
			callback: callback
		});
	},
	 //弹出回访详情按钮对话框			
	"taskDetails" : function(obj) {
		var url = $("#taskDetailsurl").attr("href");
		var _this=this;
		 url = url + '?visitTaskID=' + obj;
		var callback = function(msg) {
			var dialogSetting = {
					dialogInit: "#dialogInit",
					bgiframe: true,
					width: 	500,
					height: 270,
					text: msg,
					title: 	$("#taskDetailsTitle").text(),
					confirm: $("#confirm").text(),
					confirmEvent: function(){removeDialog("#dialogInit");}
				};
				
				//弹出框体
				openDialog(dialogSetting);
				
//				_this.getLogiDepotByAjax("main_form");
		};
		cherryAjaxRequest({
			url: url,
			callback: callback
		});
	},
	// 取消任務
	"cancelTask" : function(url) {
		if($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		var dom = $("#dataTable_Cloned").find("input[name='visitTaskIDs']:checked");
		for(var i = 0;i < dom.length; i++){
			var val = dom[i].value.split("*");
			if(val[1] !="0"){
				$("#errorMessage").html($("#errorMessageTempTwo").html());
				return false;
			}else{
				
			}
			
		}
		
		var param = $('#dataTable_Cloned :input[checked]').serialize();
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		cancelTaskConfirm(url, param, callback);
		
		
	},//变更执行者
	"changePerformer" : function(url) {
		if($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		var dom = $("#dataTable_Cloned").find("input[name='visitTaskIDs']:checked");
		var OrganizationID = "";
		var OrganizationIDTwo = "";
		for(var i = 0;i < dom.length; i++){
			var val = dom[i].value.split("*");
			if(val[1] !="0"){
				$("#errorMessage").html($("#errorMessageTempTwo").html());
				return false;
			}
			if(i=="0"){
				OrganizationID = val[3];
			}else{
				OrganizationIDTwo = val[3];
				if(OrganizationIDTwo!=OrganizationID){
					$("#errorMessage").html($("#errorMessageTempTwos").html());
					return false;
				}
			}
			
		}
		var url = url;
		var _this=this;
		 url = url + '?organizationID=' + OrganizationID;
		var callback = function(msg) {
			var dialogSetting = {
					dialogInit: "#dialogInit",
					bgiframe: true,
					width: 	400,
					height: 130,
					text: msg,
					title: 	$("#changePerformerTitle").text(),
					confirm: $("#confirm").text(),
					cancel: $("#cancel").text(),
					confirmEvent: function(){binolmbvis01.changePerformerConfirm();},
					cancelEvent: function(){removeDialog("#dialogInit");}
				};
				
				//弹出框体
				openDialog(dialogSetting);
				
		};
		cherryAjaxRequest({
			url: url,
			callback: callback
		});
	},//变更执行者确认
	"changePerformerConfirm" : function() {
		var url =$("#changePerformerConfirmurl").attr("href");
		var param = $('#dataTable_Cloned :input[checked]').serialize();
		param += "&" + $("#employeeIDs").serialize();
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		var title = $('#changePerformerTitle').text();
		var text = $('#changePerformerConfirm').html();
		var dialogSetting = {
			dialogInit: "#dialogInit",
			text: text,
			width: 	500,
			height: 300,
			title: 	title,
			confirm: $("#confirm").text(),
			cancel: $("#cancel").text(),
			confirmEvent: function(){changePerformerHandle(url, param, callback);},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};

		openDialog(dialogSetting);
	},
	/*
	 * 导出Excel
	 */
	"exportExcel" : function(urls) {
		// 无数据不导出
		if ($(".dataTables_empty:visible").length == 0) {
			if (!$('#VisitTaskInfoCherryForm').valid()) {
				return false;
			}
			;
			var url = urls;
			var params = $("#VisitTaskInfoCherryForm").find("div.column").find(":input")
					.serialize();
			params = params + "&csrftoken=" + $("#csrftoken").val();
			params = params + "&" + getRangeParams();
			url = url + "?" + params;
			window.open(url, "_self");
		}
	}
};

var binolmbvis01 =  new BINOLMBVIS01();

//取消任務确认
function cancelTaskConfirm(url, param, callback) {
	
	var title = $('#cancelTaskConfirm').text();
	var text = $('#cancelConfirm').html();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	500,
		height: 300,
		title: 	title,
		confirm: $("#confirm").text(),
		cancel: $("#cancel").text(),
		confirmEvent: function(){cancelTaskHandle(url, param, callback);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};

	openDialog(dialogSetting);
}

//取消任務處理
function cancelTaskHandle(url, param, callback) {
	
	var callback = function(msg) {
		$("#dialogInit").html(msg);
		if($("#errorMessageDiv").length > 0) {
			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			$("#dialogInit").dialog( "option", {
				buttons: [{
					text: $("#confirm").text(),
				    click: function(){removeDialog("#dialogInit");}
				}]
			});
		} else {
			removeDialog("#dialogInit");
			binolmbvis01.search();
			if(typeof(delCallback) == "function") {
				delCallback();
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: '#mainForm'
	});

}
//变更执行者處理
function changePerformerHandle(url, param, callback) {
	
	var callback = function(msg) {
		$("#dialogInit").html(msg);
		if($("#errorMessageDiv").length > 0) {
			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			$("#dialogInit").dialog( "option", {
				buttons: [{
					text: $("#confirm").text(),
				    click: function(){removeDialog("#dialogInit");}
				}]
			});
		} else {
			removeDialog("#dialogInit");
			binolmbvis01.search();
			if(typeof(delCallback) == "function") {
				delCallback();
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: '#mainForm'
	});

}

$(function(){
	binolmbvis01.search();
});
$(document).ready(function() {
	employeeBinding({elementId:"employeeName",showNum:20});
	counterBinding({elementId:"counterCodeName",showNum:20});
});
function checkSelectAll(checkbox){
	$("#errorMessage").empty();
	$('#dataTable_wrapper #dataTable_Cloned').find(":checkbox").prop("checked", $(checkbox).prop("checked"));
	if ($(checkbox).prop("checked") && $("input@[id=validFlag][checked]").length>0){
	}
};

function checkSelect(checkbox){
	$("#errorMessage").empty();
	if($(checkbox).prop("checked")) {
        if($("#dataTable_Cloned input@[id=validFlag]").length == $("#dataTable_Cloned input@[id=validFlag][checked]").length) {
            $("input@[id=allSelect]").prop("checked",true);
        }
    } else {
        $("input@[id=allSelect]").prop("checked",false);
    }
};