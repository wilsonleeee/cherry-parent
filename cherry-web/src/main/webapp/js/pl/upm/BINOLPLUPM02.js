/*
 * 全局变量定义
 */
var binolplupm02_global = {};

//当前点击对象
binolplupm02_global.thisClickObj = {};

//是否刷新父页面
binolplupm02_global.doRefresh = false;

//是否需要解锁
binolplupm02_global.needUnlock = true;

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	cherryValidate({			
		formId: "addUserForm",		
		rules: {
		        employeeId: {required: true, maxlength: 30}, // 员工ID
		        loginName: {required: true, maxlength: 30},  //登入账号
		        passWord: {required: true, maxlength: 30},   // 密码
		        confirmPW: {required: true, maxlength: 30}   // 确认密码
		}		
	});
	
} );

/* 
 * 取得参数
 * 
 */
function getParams() {
	// 参数(品牌信息ID)
	var params = $("#brandSel").serialize();
	var parentToken = getParentToken();
	params += "&" + parentToken;
	return params;
}

/* 
 * 显示员工选择页面
 * 
 * Inputs:  Object:obj		选择员工按钮
 * 			
 * 
 */
function showEmployeeDialog(obj) {
	// 参数
	var params = getParams();
	var $employeeId = $("#userMap").find("input[name='employeeId']");
	if ($employeeId.length > 0) {
		var idParam = "";
		$employeeId.each(function (){
			if ("" != $(this).val()) {
				idParam += "&" + $(this).serialize();
			}
		});
		params += idParam;
	}
	popDataTableOfEmployee(obj, params);
	binolplupm02_global.thisClickObj = obj;
}

/* 
 * 选择员工
 * 			
 * 
 */
function selectEmployee() {
	// 选中的员工
	var $selRadio = $("#employee_dataTable").find(":radio[checked]");
	if ($selRadio.length > 0) {
		var selVal = $selRadio.val();
		// 员工名称
		var employeeName = $selRadio.parents("tr").find("td").first().next().next().text();
		var $parentObj = $(binolplupm02_global.thisClickObj).parents("td");
		$parentObj.find("#errorText").remove();
		// 员工ID
		$parentObj.find("input[name='employeeId']").val(selVal);
		$(binolplupm02_global.thisClickObj).prev("span").text(employeeName);
	}
	$('#employeeDialog').dialog( "destroy" );
}
/*
 * 添加错误信息
 * 
 * Inputs:  String:text		错误信息
 * 
 * 
 */
function errSpan(text) {
	// 新建显示错误信息的span
	var errSpan = document.createElement("span");
	$(errSpan).attr("class", "ui-icon icon-error tooltip-trigger-error");
	$(errSpan).attr("id", "errorText");
	$(errSpan).attr("title",'error|'+ text);
	$(errSpan).cluetip({
    	splitTitle: '|',
	    width: 150,
	    tracking: true,
	    cluetipClass: 'error', 
	    arrows: true, 
	    dropShadow: false,
	    hoverIntent: false,
	    sticky: false,
	    mouseOutClose: true,
	    closePosition: 'title',
	    closeText: '<span class="ui-icon ui-icon-close"></span>'
	});
	return errSpan;
}

function doSave(url) {
	if (!$('#addUserForm').valid()) {
		return false;
	};
	// 参数序列化
	var params = null;
	$("#addUserForm").find(":input").each(function() {
		$(this).val($.trim(this.value));
		if (null == params) {
			params = $(this).serialize();
		} else {
			params += "&" + $(this).serialize();
		}
	});
	cherryAjaxRequest({
		url: url,
		param: params,
		callback: function(msg) {
		binolplupm02_global.doRefresh = true;
		},
		coverId: "#pageButton"
	});
}

function doClose(){
	window.close();
}

window.onbeforeunload = function(){
	if (binolplupm02_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (binolplupm02_global.doRefresh) {
				// 刷新父页面
				window.opener.searchUser();
			}
		}
	}
};

/* 
 * 切换密码提示信息
 * 			
 * 
 */
function changePwText() {
	// AJAX请求地址
	var url = $("#changeUrl").attr("href");
	// 品牌ID
	var param = $("#brandSel").serialize();
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			$("#pwText").html(msg);
		}
	});
}

