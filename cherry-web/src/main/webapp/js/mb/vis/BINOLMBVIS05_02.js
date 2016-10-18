function BINOLMBVIS05_02() {};

BINOLMBVIS05_02.prototype = {
		"confirm":function(){
			if(!$('#mainForm').valid()) {
				return false;
			}
			var confirm_url=$('#confirm_url').attr('href');
			var params=$("#mainForm").serialize()+ "&" + getSerializeToken();
			cherryAjaxRequest({
				url: confirm_url,
				param:params,
				callback: function(data) {
					if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
//					if(oTableArr[1] != null)oTableArr[1].fnDraw();
				}
			});
		}
};

window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

var BINOLMBVIS05_02 =  new BINOLMBVIS05_02();

$(document).ready(function() {
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
//	employeeBinding({elementId:"employeeCode",showNum:20,selected:"code",privilegeFlag:"1"});
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			tradeEmployeeID: {required: true},
			visitResult: {required: true}
		}
	});
	//去除未回访的选择状态
	$("#visitResult option").each(function(){
		if($(this).html() == "未回访" || $(this).html() == "未回訪"){
			$(this).remove();
		}
	});
});
