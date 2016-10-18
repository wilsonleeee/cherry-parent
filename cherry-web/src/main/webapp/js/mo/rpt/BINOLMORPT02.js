
function showQuestion(obj){
	var $div = $(obj).next();
	if($div.is(":visible")) {
		$div.hide();
	} else {
		$div.show();
	}
}

$(document).ready(function() {
	if (window.opener) {
	    window.opener.lockParentWindow();
	}
});
window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};