
$(function(){
	
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
	$('#tabs').tabs();
	// 表单验证初期化
	cherryValidate({
		formId: 'addDepPltInfo',
		rules: {
			departType: {required: true},
			operationType: {required: true},
			businessType: {required: true},
			privilegeType: {required: true}
		}
	});
	cherryValidate({
		formId: 'addPosPltInfo',
		rules: {
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

// 保存权限类型处理
function plplt02_savePlt() {

	var param = "";
	var refreshPrivilegeUrl = $("#refreshPrivilegeUrl").val();
	if($('#tabs-1').is(':visible')) {
		if(!$('#addDepPltInfo').valid()) {
			return false;
		}
		param = $('#tabs-1').find(':input').serialize();
	} else if($('#tabs-2').is(':visible')) {
		if(!$('#addPosPltInfo').valid()) {
			return false;
		}
		param = $('#tabs-2').find(':input').serialize();
	}
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
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
		url: $('#addPltUrl').attr('href'),
		param: param,
		callback: callback
	});
}