/*
 * 全局变量定义
 */
var FAC02_global = {};
//是否需要解锁
FAC02_global.needUnlock = true;

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
});

window.onbeforeunload = function(){
	if (FAC02_global.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};

function doClose(){
	window.close();
}

// 编辑按钮
function editFac(){
	var parentToken = parentTokenVal();
	$("#parentCsrftoken").val(parentToken);
	FAC02_global.needUnlock = false;
	$("#mainForm").submit();
}