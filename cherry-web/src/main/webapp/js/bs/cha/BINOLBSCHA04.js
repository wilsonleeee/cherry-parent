
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(function(){
	
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
	$('#joinDate').cherryDate();	
	cherryValidate({			
		formId: "add",		
		rules: {
			brandInfoId: {required: true},
		    channelName: {required: true,maxlength: 50},	//渠道名称
		    channelNameForeign: {maxlength: 50},
		    joinDate: {dateValid: true}
	   }		
	});
});

function save() {
	
	if(!$('#add').valid()) {
		return false;
	}
	var param = $('#add').find(':input').serialize();
	var callback = function(msg) {
		if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
	};
	cherryAjaxRequest({
		url: $('#CHA04_save').attr('href'),
		param: param,
		callback: callback	
	});
}