/*
 * 全局变量定义
 */
var BSEMP02_global = {};
//是否需要解锁
BSEMP02_global.needUnlock = true;

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
} );

window.onbeforeunload = function(){
	if (BSEMP02_global.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};

// 编辑用户
function bsemp02_editEmp(){
	BSEMP02_global.needUnlock = false;
	var parentToken = parentTokenVal();
	$("#parentCsrftoken").val(parentToken);
	$("#mainForm").submit();
}

// 关闭画面
function bsemp02_doClose(){
	window.close();
}

