/*
 * 全局变量定义
 */
var binolssprm04_global = {};
//是否需要解锁
binolssprm04_global.needUnlock = true;

window.onbeforeunload = function(){
	if (binolssprm04_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

$(document).ready(function() {
	$('.tabs').tabs();
	$('.tabs').show();
	if (window.opener) {
		window.opener.lockParentWindow();
	}
} );

function editPrm(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	binolssprm04_global.needUnlock = false;
	$("#toEditForm").submit();
}
 
function doClose(){
	window.close();
}