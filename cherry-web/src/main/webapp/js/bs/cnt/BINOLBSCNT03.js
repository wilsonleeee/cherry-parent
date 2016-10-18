
function BINOLBSCNT03() {
	this.needUnlock = true;
};

BINOLBSCNT03.prototype = {
		
		// 保存柜台处理
		"updateCounter" : function(url) {
			if(!$('#mainForm').valid()) {
				return false;
			}
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
				url: url,
				param: $('#mainForm').serialize(),
				callback: callback
			});
		},
		"back" : function() {
			binolbscnt03.needUnlock=false;
			var tokenVal = $('#csrftoken',window.opener.document).val();
			$('#counterDetailUrl').find("input[name='csrftoken']").val(tokenVal);
			$('#counterDetailUrl').submit();
		},
		"close" : function() {
			window.close();
		},
		"selectResellerDepart" : function() {
			var param = "privilegeFlg=1&businessType=0&departType=3";
			var callback = function() {
	    		var checkedRadio = $("#departDialogInit").find("input[name='organizationId']:checked");
	    		var departId = "";
	    		var departName = "";
	    		if(checkedRadio){
	    			departId = checkedRadio.val();
		    		departName = checkedRadio.parent().parent().children("td").eq(2).find("span").text().escapeHTML();
	    		}
	    		$("#resellerDepartId").val(departId);
	    		$("#showResellerDepart").html(departName.unEscapeHTML());
	    	};
			var option = {
					"brandInfoId": $("#brandInfoId").val(),
					"checkType": "radio",
					"param": param,
					"click": callback
			};
			popAjaxDepDialog(option);
		}
		
};

var binolbscnt03 =  new BINOLBSCNT03();

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			counterCode: {required: true,maxlength: 15},
			counterNameIF: {required: true,maxlength: 50},
			counterKind: {required: true},
			counterNameShort: {maxlength: 20},
			nameForeign: {maxlength: 50},
			counterTelephone: {maxlength: 20},
			counterAddress: {maxlength: 100},
			counterSpace: {floatValid: [4,2]},
			employeeNum:{validIntNum:true},
			passWord:{maxlength: 15},
			expiringDateDate : {dateValid:true},
			expiringDateTime : {timeHHmmssValid:true},
			longitude : {lonValid:true},
			latitude : {latValid:true},
			posFlag : {required: true},
			busniessPrincipal: {maxlength: 50}
		}
	});
	
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		bscom03_showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		bscom03_showRegin(this,"cityTemp");
	});
	// 县级市选择
	$("#county").click(function(){
		// 显示县级市列表DIV
		bscom03_showRegin(this,"countyTemp");
	});
});

window.onbeforeunload = function(){
	if (binolbscnt03.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};



