/*
 * 全局变量定义
 */
var binolptjcs06_global = {};
//是否需要解锁
binolptjcs06_global.needUnlock = true;

window.onbeforeunload = function(){
	if (binolptjcs06_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

$(document).ready(function() {
	$('.tabs').tabs();
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});

function doClose(){
	window.close();
}

function editPro(url){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	binolptjcs06_global.needUnlock = false;
	$("#toAddForm").submit();
}

function cntPriceList(url){
	var tokenVal = parentTokenVal();
	$("#parentCsrftokenPrice").val(tokenVal);
	binolptjcs06_global.needUnlock = false;
	$("#toCntPriceListForm").submit();
}
