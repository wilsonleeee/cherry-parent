
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(function(){
	
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
	
	cherryValidate({			
		formId: "add",		
		rules: {
			version: {required: true},
			downloadUrl: {required: true,maxlength: 50}
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
		url: $('#MUP04_save').attr('href'),
		param: param,
		callback: callback	
	});
}