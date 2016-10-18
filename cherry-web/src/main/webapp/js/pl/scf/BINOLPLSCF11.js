/*
 * 全局变量定义
 */
var plscf11_global = {};
//是否需要解锁
plscf11_global.needUnlock = true;

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
	if (plscf11_global.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};

function doClose(){
	window.close();
}



$(document).ready(function() {
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
} );


//返回
function plscf11_doBack(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken1").val(tokenVal);
	plscf11_global.needUnlock = false;
	$("#toCoderQueryForm").submit();
}

//参数序列化
function getParams(obj){
	return $(obj).find(":input").serialize();
}


//code值添加初始画面
function plscf11_saveCoder(url){
	if (!$('#mainForm').valid()) {
		return false;
	};
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken2").val(tokenVal);
//	plscf11_global.needUnlock = false;
//	$("#mainForm").submit();	
	var $form = $("#mainForm");
	// 基本信息
	var baseInfo = getParams("#base_info");
	// URL
	var param = baseInfo+"&"+$("#parentCsrftoken2");
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			//是否刷新父页面
			plscf11_global.doRefresh = true;
		},
		coverId: "#saveButton"
	});

}

