var binolbscha03_global = {};
binolbscha03_global.needUnlock = true;
var BINOLBSCHA03_doRefresh = false;

window.onbeforeunload = function() {
	if (window.opener) {
		window.opener.unlockParentWindow();
		if(BINOLBSCHA03_doRefresh){
			var url = $('#search_url', window.opener.document).val();
			window.opener.search(url);
			}
		}
};

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	$('#joinDate').cherryDate();	
	cherryValidate({			
		formId: "update",		
		rules: {
			channelCode: {required: true,maxlength: 50},	//渠道代码
		    channelName: {required: true,maxlength: 50},	//渠道名称
		    channelNameForeign: {maxlength: 50},
		    joinDate: {dateValid: true}
	   }		
});
});

function doBack(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	binolbscha03_global.needUnlock = false;
	$("#toDetailForm").submit();
}
	
function save() {
		if(!$('#update').valid()) {
			return false;
		}
		var param = $('#update').find(':input').serialize();
		var callback = function(msg) {
			if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		};
		cherryAjaxRequest({
			url: $('#CHA03_save').attr('href'),
			param: param,
			callback:  function(msg) {BINOLBSCHA03_doRefresh = true;}
		});
}