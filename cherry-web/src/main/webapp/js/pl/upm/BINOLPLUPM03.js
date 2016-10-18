/*
 * 全局变量定义
 */
var binolplupm03_global = {};

//是否刷新父页面
binolplupm03_global.doRefresh = false;

//是否需要解锁
binolplupm03_global.needUnlock = true;

$(document).ready(function() {
	
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	cherryValidate({			
		formId: "updateUserForm",		
		rules: {		
		       passWord: {required: true, maxlength: 30},   // 密码
               confirmPW: {required: true, maxlength: 30}   // 确认密码
		}		
	});
	
} );

function doSaveUser(url) {
	if (!$('#updateUserForm').valid()) {
		return false;
	};
	// 参数序列化
	var params = null;
	$("#updateUserForm").find(":input").each(function() {
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
		binolplupm03_global.doRefresh = true;
		},
		coverId: "#pageButton"
	});
}

function doCloseUser(){
	window.close();
}

window.onbeforeunload = function(){
	if (binolplupm03_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (binolplupm03_global.doRefresh) {
				// 刷新父页面
				window.opener.searchUser();
			}
		}
	}
};