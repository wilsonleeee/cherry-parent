
function updateInfo(url){
	if(!$('#mainForm').valid()) {
		return false;
	}

	param=$("#rangeCode").serialize();

	var callback = function(msg) {
		removeDialog("#dialogInit");
	};
	cherryAjaxRequest({
		url: url,
		param:param,
		callback: callback
	});
}

function operateInfo(url) {

	var dialogSetting = { 
			dialogInit: "#dialogInit",
			text: $("#operateContent").html(),
			width: 	500,
			height: 300,
			title: 	$("#operatetitle").text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){updateInfo(url);},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
}