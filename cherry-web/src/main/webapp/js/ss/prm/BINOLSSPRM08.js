/*
 * 全局变量定义
 */
var binolssprm08_global = {};

//是否需要解锁
binolssprm08_global.needUnlock = true;

window.onbeforeunload = function(){
	if (binolssprm08_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});

function editPrmType(url){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	binolssprm08_global.needUnlock = false;
	$("#toEditForm").submit();
};

function doClose(){
	window.close();
}