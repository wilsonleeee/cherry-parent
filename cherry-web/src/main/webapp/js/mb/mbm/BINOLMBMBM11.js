function BINOLMBMBM11(){};

BINOLMBMBM11.prototype = {
	"addMember": function(url) {
		if(!$('#mainForm').valid()) {
			return false;
		}
		var callback = function(msg) {
			if($('#actionResultBody').length > 0) {
				if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
			} else {
				if(msg.indexOf('memLevelConfirm') > -1) {
					var jsonObj = eval('('+msg+')');
					if(confirm(jsonObj.memLevelWarnInfo)) {
						$("#memLevelConfirm").val("1");
						var calbackConfirm = function(msg) {
							if($('#actionResultBody').length > 0) {
								if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
							}
						}
						cherryAjaxRequest({
							url: url,
							param: $('#mainForm').serialize(),
							callback: calbackConfirm
						});
					}
				}
			}
		};
		cherryAjaxRequest({
			url: url,
			param: $('#mainForm').serialize(),
			callback: callback
		});
	}
};

var binolmbmbm11 =  new BINOLMBMBM11();

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			memCode: {required: true,maxlength: 20},
			organizationId: {required: true},
			memName: {required: true,maxlength: 30},
			nickname : {maxlength: 30},
			employeeId: {required: true},
			mobilePhone: {phoneTelValid: "#telephone",maxlength: 20},
			joinDate: {required: true,dateValid: true},
			telephone: {telValid: true,maxlength: 20},
			birth: {required: true,dateValid: true},
			email: {emailValid: true,maxlength: 60},
			tencentQQ : {maxlength: 20},
			gender: {required: true},
			initTotalAmount: {floatValid: [16,2]},
			referrer: {maxlength: 20},
			identityCard: {maxlength: 18},
			blogId: {maxlength: 30},
			messageId: {maxlength: 30},
			memo1: {maxlength: 512},
			memo2: {maxlength: 512},
			address: {maxlength: 200},
			postcode: {zipValid:true,maxlength: 10}
		}
	});
	
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		binolmbcom01.showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		binolmbcom01.showRegin(this,"cityTemp");
	});
	
});

window.onbeforeunload = function() {
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};