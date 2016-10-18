/*
 * 全局变量定义
 */
var scf13_global = {};
//是否需要解锁
scf13_global.needUnlock = true;

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	cherryValidate({			
		formId: "mainForm",		
		rules: {		
			codeKey: {required: true, alphanumeric:true, maxlength: 4},	// CodeKey
			value1: {maxlength: 20},	// 值1
			value2: {maxlength: 20},	// 值2
			value3: {maxlength: 20},	// 值3
			grade: {digits: true, maxlength: 9},	// 级别
			codeOrder: {digits: true, maxlength: 9}	// 显示顺序
		}		
	});
});

window.onbeforeunload = function(){
	if (scf13_global.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};

function doClose(){
	window.close();
}

// 编辑按钮
function scf13Edit(){
	var parentToken = parentTokenVal();
	$("#parentCsrftoken").val(parentToken);
	scf13_global.needUnlock = false;
	$("#coderEditForm").submit();
}

//返回
function plscf13_doBack(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken1").val(tokenVal);
	scf13_global.needUnlock = false;
	$("#toDetailForm").submit();
}
//参数序列化
function plscf13_getParams(obj){
	return $(obj).find(":input").serialize();
}

//更新code值
function plscf13_update(url){
	if (!$('#mainForm').valid()) {
		return false;
	};
//	var tokenVal = parentTokenVal();
//	$("#parentCsrftoken").val(tokenVal);
	// 基本信息
	var baseInfo = plscf13_getParams("#base_info");
	// URL
	var param = baseInfo;
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
		scf13_global.doRefresh = true;
		},
		coverId: "#saveButton"
	});
}
