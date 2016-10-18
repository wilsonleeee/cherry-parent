/*
 * 全局变量定义
 */
var binolssprm07_global = {};

//是否刷新父页面
binolssprm07_global.doRefresh = false;

//是否需要解锁
binolssprm07_global.needUnlock = true;

window.onbeforeunload = function(){
	if (binolssprm07_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (binolssprm07_global.doRefresh) {
				// 刷新父页面
				window.opener.search();
			}
		}
	}
};

$(document).ready(function() {
	
	cherryValidate({			
		formId: "mainForm",		
		rules: {
		    primaryCategoryNameCN: {maxlength: 20},	            // 大分类中文名
		    primaryCategoryNameEN: {maxlength: 20},	            // 大分类英文名
		    secondryCategoryNameCN: {maxlength: 20},	        // 中分类中文名
		    secondryCategoryNameEN: {maxlength: 20},	        // 中分类英文名
		    smallCategoryNameCN: {maxlength: 20},	            // 小分类中文名
		    smallCategoryNameEN: {maxlength: 20}                // 小分类英文名
		}		
	});
} );

function doSave(url) {
	if (!$('#mainForm').valid()) {
		return false;
	};
	// 参数序列化
	var params = null;
	$("#mainForm").find(":input").each(function() {
		$(this).val($.trim(this.value));
		if (null == params) {
			params = $(this).serialize();
		} else {
			params += "&" + $(this).serialize();
		}
	});
	cherryAjaxRequest({
		url: url,
		param: params,
		callback: function(msg) {
		binolssprm07_global.doRefresh = true;
		},
		coverId: "#pageButton"
	});
}

function doBack(url){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	binolssprm07_global.needUnlock = false;
	$("#toDetailForm").submit();
}

function doClose(){
	window.close();
}
