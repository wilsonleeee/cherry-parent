window.onbeforeunload = function(){
    if (window.opener) {
        window.opener.unlockParentWindow();
    }
};
$(document).ready(function() {
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
});