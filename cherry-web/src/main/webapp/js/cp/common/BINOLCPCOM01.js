var BINOLCPCOM01_GLOBAL = function () {

};
BINOLCPCOM01_GLOBAL.prototype = {
		
		/* 是否打开父页面锁定*/
		"needUnlock" : true,
		
		"doNext" : function (actionId){
						if (!$('#toNextForm').valid()) {
							return false;
						};
						var tokenVal = parentTokenVal();
						$("#parentCsrftoken").val(tokenVal);
						if (actionId) {
							$("#actionId").val(actionId);
						}
						BINOLCPCOM01.needUnlock = false;
						$("#toNextForm").submit();
					}
};

var BINOLCPCOM01 = new BINOLCPCOM01_GLOBAL();

window.onbeforeunload = function(){
	if (window.opener) {
		if(BINOLCPCOM01.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};

$(document).ready(function() {

	CAMPAIGN_TEMPLATE.initDate("#campFromDate", "#campToDate", "maxDate");
	CAMPAIGN_TEMPLATE.initDate("#campToDate", "#campFromDate", "minDate");

	cherryValidate({			
		formId: "toNextForm",		
		rules: {		
			"campInfo.campaignName": {required: true, maxlength: 50},	// 活动名称
			"campInfo.descriptionDtl": {maxlength: 300},	// 活动描述
			"campInfo.campaignFromDate":{required: true,dateValid: true},
			"campInfo.campaignToDate":{dateValid: true}
		}		
	});
	CAMPAIGN_TEMPLATE.msgHandle($("#ERROR_MSG").html());
	$('form [rel!=""]').cluetip({
		  width: 'auto',
		  height: 'auto',
		  titleAttribute: 'rel',
		  local: true,
		  positionBy: 'mouse',
		  topOffset: 15,
		  leftOffset: 15,
		  tracking: true,
		  cluetipClass: 'note', 
		  arrows: false, 
		  dropShadow: false,
		  hoverIntent: false,
		  sticky: false,
		  mouseOutClose: true,
		  closePosition: 'title',
		  closeText: '<span class="ui-icon ui-icon-close"></span>'
		});
	
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	BINOLCPCOM01.needUnlock = true;
});