/*
 * 全局变量定义
 */
var binolssprm11_global = {};

//是否需要解锁
binolssprm11_global.needUnlock = true;

window.onbeforeunload = function(){
	if (binolssprm11_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

$(document).ready(function() {
	if (window.opener) {
	       window.opener.lockParentWindow();
	    }
	cherryValidate({			
		formId: "mainForm",		
		rules: {		
		        itemClassNameCN: {maxlength: 50},	            // 类别中文名
		        itemClassNameEN: {maxlength: 50},	            // 类别英文名
		        itemClassCode: {required:true, maxlength: 20},  // 类别码
		        curClassCode: {maxlength: 4}                    // 类别特征码
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
	var newPath = $('#path').val();
	var higherCategoryPath = $('#higherCategoryPath').val();
	var prmCategoryId = $('#prmCategoryId').val();
	var callback = function(msg) {
		if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		if(window.opener.document.getElementById('categoryTree')) {
			window.opener.categoryDetail(prmCategoryId);
			window.opener.refreshCateNode(higherCategoryPath);
			if(newPath != higherCategoryPath) {
				window.opener.refreshCateNode(newPath);
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: params,
		callback: callback,
		coverId: "#pageButton"
	});
}

function doBack(url){
	var parentToken = getParentToken();
	url += "&" +parentToken;
	binolssprm11_global.needUnlock = false;
	window.location.href=url;
}

function doClose(){
	window.close();
}
