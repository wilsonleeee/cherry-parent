
function BINOLBSREG04() {};

BINOLBSREG04.prototype = {	
		// 改变品牌事件
		"changeBrandInfo" : function(object) {
			// 清空上级区域
			$('#higheRegionDiv').find('tbody').empty();
		},
		// 改变区域类型事件
		"changeRegionType" : function(object) {
			// 清空上级区域
			$('#higheRegionDiv').find('tbody').empty();
		},
		// 保存区域处理
		"addRegion" : function(url) {
			if(!$('#addRegionInfo').valid()) {
				return false;
			}
			$('#addRegionInfo :input').each(function(){
				$(this).val($.trim(this.value));
			});
			var callback = function(msg) {
				if(msg.indexOf('id="actionResultConfirm"') > -1) {
					if(confirm($(msg).find('span').text())) {
						$("#ignoreFlg").val("1");
						binolbsreg04.addRegion(url);
						return;
					}
				}
				if($('#actionResultBody').length > 0) {
					window.opener.bsreg01_refreshRegionNode();
				}
			};
			cherryAjaxRequest({
				url: url,
				param: $('#addRegionInfo').serialize(),
				callback: callback,
				formId: '#addRegionInfo'
			});
		}
};

var binolbsreg04 =  new BINOLBSREG04();

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	
	// 表单验证初期化
	cherryValidate({
		formId: 'addRegionInfo',
		rules: {
			regionCode: {required: true,maxlength: 10,alphanumeric: true},
			regionNameChinese: {required: true,maxlength: 50},
			regionType: {required: true},
			brandInfoId: {required: true},
			regionNameForeign: {maxlength: 50},
			zipCode: {maxlength: 10,alphanumeric: true},
			helpCode: {maxlength: 10,alphanumeric: true},
			teleCode: {maxlength: 5,digits: true}
		}
	});
});

window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};