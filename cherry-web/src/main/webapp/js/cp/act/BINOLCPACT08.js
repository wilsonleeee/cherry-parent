/**
 * 锁定、解锁主页面
 */
window.onbeforeunload = function() {
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(document).ready(function() {
	initDetailTable();
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});