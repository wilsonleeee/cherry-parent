
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(function(){
	
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
	
	// 表单验证初期化
	cherryValidate({
		formId: 'addOrg',
		rules: {
			orgCode: {required: true,maxlength: 50},
			orgNameChinese: {required: true,maxlength: 50},
			orgNameForeign: {maxlength: 50},
			orgNameShort: {maxlength: 20},
			orgNameForeignShort: {maxlength: 20},
			foundationDate: {dateValid: true},
			longinName: {required: true,maxlength: 30},
			passWord: {required: true,maxlength: 30}
		}
	});
	
	$('#longinName').val("");
	$('#passWord').val("");
	
});	

// 添加组织处理
function bsdep07_saveOrg() {

	if(!$('#addOrg').valid()) {
		return false;
	}
	$('#addOrg :input').each(function(){
		$(this).val($.trim(this.value));
	});
	var param = $('#addOrg').find(':input').serialize();
	var callback = function(msg) {
		if($('#actionResultBody').length > 0) {
			if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		}
	};
	cherryAjaxRequest({
		url: $('#addOrgUrl').attr('href'),
		param: param,
		callback: callback
	});
}