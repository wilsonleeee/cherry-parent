var binolbscha02_global = {};
binolbscha02_global.needUnlock = true;

$(document).ready(function() {
	$('.tabs').tabs();
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});

function edit(url){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	binolbscha02_global.needUnlock = false;
	$("#toEditForm").submit();
}

function doClose(){
	window.close();
}

window.onbeforeunload = function(){
	if (binolbscha02_global.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};