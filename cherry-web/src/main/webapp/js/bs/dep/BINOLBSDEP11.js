
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
		formId: 'addBrand',
		rules: {
			brandCode: {required: true,alphanumeric: true,maxlength: 10},
			brandNameChinese: {required: true,maxlength: 50},
			brandNameShort: {maxlength: 20},
			brandNameForeign: {maxlength: 50},
			brandNameForeignShort: {maxlength: 20},
			foundationDate: {dateValid: true}
		}
	});
	
});	

// 添加品牌处理
function bsdep11_saveBrand() {
	
	if(!$('#addBrand').valid()) {
		return false;
	}
	$('#addBrand :input').each(function(){
		$(this).val($.trim(this.value));
	});
	var param = $('#addBrand').find(':input').serialize();
	var callback = function(msg) {
		if($('#actionResultBody').length > 0) {
			if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		}
	};
	cherryAjaxRequest({
		url: $('#addBrandUrl').attr('href'),
		param: param,
		callback: callback
	});
	
}