$(function(){
	if (window.opener) {
       window.opener.lockParentWindow();
    }
});
/*
 * 全局变量定义
 */
var binolbspos09_global = {};
//是否需要解锁
binolbspos09_global.needUnlock = true;
window.onbeforeunload = function(){
	if (binolbspos09_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

$(function(){
	
	// 表单验证初期化
	cherryValidate({
		formId: 'addPosCategoryInfo',
		rules: {
			categoryCode: {required: true,maxlength: 2},
			posGrade: {required: true,digits: true},
			categoryName: {required: true,maxlength: 20},
			categoryNameForeign: {maxlength: 20}
		}
	});
	
});

// 保存岗位类别
function savePosCategory() {
	if($('#addPosCategoryInfo').valid()) {
		$('#addPosCategoryInfo :input').each(function(){
			$(this).val($.trim(this.value));
		});
		var param = $('#addPosCategoryInfo').serialize();
		var callback = function(msg) {
			if($('#addPosCategoryInfo').length == 0) {
				if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
			}
		};
		cherryAjaxRequest({
			url: $('#addPosCategoryUrl').attr("href"),
			param: param,
			callback: callback,
			formId: '#posCategoryCherryForm'
		});
	}
}