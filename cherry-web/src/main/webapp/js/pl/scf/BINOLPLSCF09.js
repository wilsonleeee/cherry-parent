/*
 * 全局变量定义
 */
var plscf09_global = {};
//是否刷新父页面
plscf09_global.doRefresh = false;
//是否需要解锁
plscf09_global.needUnlock = true;

window.onbeforeunload = function(){
	if (plscf09_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (plscf09_global.doRefresh) {
				// 刷新父页面
				window.opener.plscf06_searchCode();
			}
		}
	}
};

function doClose(){
	window.close();
}

//返回
function plscf09_doBack(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	plscf09_global.needUnlock = false;
	$("#toDetailForm").submit();
}
// 初始化
$(document).ready(function() {
	if (window.opener) {
	  window.opener.lockParentWindow();
	}
} );

//参数序列化
function plscf09_getParams(obj){
	return $(obj).find(":input").serialize();
}
//根据组织查询品牌List
function plscf09_searchBrand(url,object,defaultSelect) {
	
	var $select = $('#brandInfoId');
	$select.empty();
	//$select.append('<option value="">'+defaultSelect+'</option>');
	var param = $(object).serialize();
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	var callback = function(msg) {
		if(msg) {
			var json = eval('('+msg+')');
			for(var i in json) {
				$select.append('<option value="'+json[i].brandCode+'">'+json[i].brandName+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

//更新code管理值
function plscf09_update(url){
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	// 基本信息
	var baseInfo = plscf09_getParams("#base_info");
	// URL
	var param = baseInfo;
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
		plscf09_global.doRefresh = true;
		},
		coverId: "#saveButton"
	});
}