
function BINOLBSREG03() {};

BINOLBSREG03.prototype = {
		// 更新区域处理
		"updateRegion" : function(url) {
			if(!$('#updateRegionInfo').valid()) {
				return false;
			}
			$('#updateRegionInfo :input').each(function(){
				$(this).val($.trim(this.value));
			});
			var regionId = $(':input[name=regionId]').val();
			var callback = function(msg) {
				if(msg.indexOf('id="actionResultConfirm"') > -1) {
					if(confirm($(msg).find('span').text())) {
						$("#ignoreFlg").val("1");
						binolbsreg03.updateRegion(url);
						return;
					}
				}
				if($('#actionResultBody').length > 0) {
					window.opener.bsreg01_refreshRegionNode();
					window.opener.bsreg01_regionDetail(regionId);
				}
			};
			cherryAjaxRequest({
				url: url,
				param: $('#updateRegionInfo').serialize(),
				callback: callback,
				formId: '#updateRegionInfo'
			});
		}
};

var binolbsreg03 =  new BINOLBSREG03();

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	
	// 表单验证初期化
	cherryValidate({
		formId: 'updateRegionInfo',
		rules: {
			regionCode: {required: true,maxlength: 10,alphanumeric: true},
			regionNameChinese: {required: true,maxlength: 50},
			regionType: {required: true},
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