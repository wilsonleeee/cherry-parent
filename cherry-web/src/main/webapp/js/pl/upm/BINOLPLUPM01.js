/*
 * 全局变量定义
 */
var binOLPLUPM01_global = {};

// 刷新区分
binOLPLUPM01_global.refresh = false;
$(document).ready(function() {
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
	searchUser();
} );

// 用户查询
function searchUser(){
	var url = $("#searchUrl").attr("href");
	// 查询参数序列化
	var params= $("#mainForm").serialize();
	url = url + "?" + params;
	// 显示结果一览
	$("#section").show();
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			// 表格列属性设置
			 aoColumns : [{ "sName": "no", "sWidth": "4%", "bSortable": false },                             //0： 编号
			                { "sName": "loginName", "sWidth": "18%", "bSortable": false },                   //1：登入账号
							{ "sName": "employeeCode", "sWidth": "18%", "bSortable": false },                //2: 员工代号
							{ "sName": "employeeName", "sWidth": "18%", "bSortable": false },                //3: 员工姓名
							{ "sName": "validFlag","sWidth": "10%","bSortable": false,"sClass":"center"},                       //4: 有效区分
							{ "sName": "Operate", "sWidth": "22%", "bSortable": false,"sClass": "center" }], //5: 按钮操作
			// 不可设置显示或隐藏的列
			aiExclude :[],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 0
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}

//删除用户处理
function deleteUser(url, param) {
	var callback = function(msg) {
		$("#dialogInit").html(msg);
		if($("#errorMessageDiv").length > 0) {
			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			$("#dialogInit").dialog( "option", {
				buttons: [{
					text: $("#dialogClose").text(),
				    click: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw(); }
				}]
			});
		} else {
			removeDialog("#dialogInit");
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: '#mainForm'
	});
}
//删除用户确认画面
function deleteUserInit(object,optFlag) {
	  var loginName = $(object).parent().parent().find('td:first-child').next().text();
	  var url = $(object).attr("href");
	  url = url + "?optFlag=" + optFlag;
	  var param = $(object).parent().find(':input').serialize();
	  var dialogSetting = {
		dialogInit: "#dialogInit",
		text: $("#deleteUserText").html(),
		width: 	500,
		height: 300,
		title: 	$("#deleteUserTitle").text()+':'+loginName,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){deleteUser(url, param);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	 };
	 openDialog(dialogSetting);
}

//恢复用户确认画面
function enableUserInit(object,optFlag) {
	  var loginName = $(object).parent().parent().find('td:first-child').next().text();
	  var url = $(object).attr("href");
	  url = url + "?optFlag=" + optFlag;
	  var param = $(object).parent().find(':input').serialize();
	  var dialogSetting = {
		dialogInit: "#dialogInit",
		text: $("#enableUserText").html(),
		width: 	500,
		height: 300,
		title: 	$("#enableUserTitle").text()+':'+loginName,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){deleteUser(url, param);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	 };
	 openDialog(dialogSetting);
}

// 弹出编辑画面
function popEditDialog(object) {
	// AJAX请求地址
	var url = $("#editUrl").attr("href");
	var param = $(object).parent().find("#userId").serialize();
	popDialog(url, param, $("#editDialogTitle").text());
}

/* 
 * 弹出Dialog画面
 * 
 * 
 * 
 */
function popDialog(url, param, title) {
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			$("#savePage").html(msg);
			cherryValidate({			
				formId: "updateUserForm",		
				rules: {		
				        passWord: {required: true, maxlength: 30}   // 密码
				}	
			});
			var dialogSetting = {
					bgiframe: true,
					top:100,
					width:700, 
					height:350,
					minWidth:700,
					minHeight:350,
					zIndex: 90,
					modal: true, 
					title: title,
					close: function(event, ui) {dialogClose();}
				};
			$('#saveDialog').dialog(dialogSetting);
		}
	});
}

/* 
 * 关闭编辑画面
 * 
 * 
 * 
 */
function doClose(id) {
	$(id).dialog( "destroy" ); 
	$(id).remove();
}

/* 
 * 保存处理
 * 
 * 
 * 
 */
function doSave(url) {
	if (!$('#updateUserForm').valid()) {
		return false;
	};
	// 参数序列化
	var param = $("#updateUserForm").serialize();
	// 禁用确定按钮
	$("#confirmBtn").attr("disabled", "disabled");
	cherryAjaxRequest({
		url: url,
		param: param,
		isDialog: true,
		resultId: "#saveActionResult",
		bodyId: "#saveDialog",
		callback: function(msg) {
			// 激活确定按钮
			$("#confirmBtn").removeAttr("disabled");
			binOLPLUPM01_global.refresh = true;
		}
	});
}

/* 
 * 关闭Dialog
 * 
 * 
 * 
 */
function dialogClose() {
	doClose('#saveDialog');
	if (binOLPLUPM01_global.refresh) {
		searchUser();
		binOLPLUPM01_global.refresh = false;
	}
}
