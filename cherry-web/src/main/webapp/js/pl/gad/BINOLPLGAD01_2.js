/*
 * 全局变量定义
 */
var GAD01_2_global = {};
//是否需要解锁
GAD01_2_global.needUnlock = true;

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	
});

window.onbeforeunload = function(){
	if (GAD01_2_global.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};

function doClose(){
	window.close();
//	window.opener.BINOLPLGAD01.search();
//	window.opener.search();
}

// 产品分类小工具配置保存
function doSave(url) {
	
	// 参数序列化
	var param = $("#editGadgedInfo").find(":input").serialize();
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	cherryAjaxRequest( {
		url : url,
		param : param,
		callback : function(msg) {
		}
	});
}

