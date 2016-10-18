/*
 * 全局变量定义
 */
var binolssprm12_global = {};

//是否需要解锁
binolssprm12_global.needUnlock = true;

window.onbeforeunload = function(){
	if (binolssprm12_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	initDetailTable();
	
} );

function editPrmCategory(url){
	var parentToken = getParentToken();
	url += "&" +parentToken;
	binolssprm12_global.needUnlock = false;
	window.location.href=url;
};

function doClose(){
	window.close();
}