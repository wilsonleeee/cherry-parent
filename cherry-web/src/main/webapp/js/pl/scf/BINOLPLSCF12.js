/*
 * 全局变量定义
 */
var scf12_global = {};
//是否需要解锁
scf12_global.needUnlock = true;

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
});

window.onbeforeunload = function(){
	if (scf12_global.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};

function doClose(){
	window.close();
}

// 编辑按钮
function scf12Edit(){
	var parentToken = parentTokenVal();
	$("#parentCsrftoken").val(parentToken);
	scf12_global.needUnlock = false;
	$("#coderEditForm").submit();
}

//返回
function plscf12_doBack(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken1").val(tokenVal);
	scf12_global.needUnlock = false;
	$("#toQueryForm").submit();
}

