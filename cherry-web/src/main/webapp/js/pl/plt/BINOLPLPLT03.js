$(function(){
	
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
	
	// 表单验证初期化
	cherryValidate({
		formId: 'updatePltForm',
		rules: {
			departType: {required: true},
			positionCategoryId: {required: true},
			operationType: {required: true},
			businessType: {required: true},
			privilegeType: {required: true}
		}
	});
});	

window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

// 更新权限类型处理
function plplt03_savePlt() {

	if(!$('#updatePltForm').valid()) {
		return false;
	}
	var param = $('#updatePltForm').find(':input').serialize();
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	var refreshPrivilegeUrl = $("#refreshPrivilegeUrl").val();
	
	var callback = function(msg) {
		if($('#actionResultBody').length > 0) {
			if(refreshPrivilegeUrl) {
				window.opener.cherryAjaxRequest({
					url: refreshPrivilegeUrl,
					param: null,
					reloadFlg : true,
					callback: function(msg) {
					}
				});
			}
			if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		}
	};
	cherryAjaxRequest({
		url: $('#updatePltUrl').attr('href'),
		param: param,
		callback: callback
	});
}