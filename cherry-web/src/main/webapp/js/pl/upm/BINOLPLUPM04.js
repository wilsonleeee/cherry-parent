/*
 * 全局变量定义
 */
var binOLPLUPM04_global = {};

// 刷新区分
binOLPLUPM04_global.refreshKbn = false;
/* 
 * 根据品牌取得密码安全配置信息
 * 
 * 
 * 
 */
function pwConfByBrand() {
	// AJAX请求地址
	var url = $("#changeUrl").attr("href");
	// 品牌ID
	var param = $("#brandSel").serialize() + "&csrftoken=" + getTokenVal();
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			$("#detailInfo").html(msg);
		}
	});
}

/* 
 * 弹出编辑画面
 * 
 * 
 * 
 */
function popEditDialog() {
	// AJAX请求地址
	var url = $("#editUrl").attr("href");
	// 品牌ID
	var param = $("#brandSel").serialize() + "&csrftoken=" + getTokenVal();
	popDialog(url, param, $("#editDialogTitle").text());
}

/* 
 * 弹出添加画面
 * 
 * 
 * 
 */
function popAddDialog() {
	// AJAX请求地址
	var url = $("#addUrl").attr("href");
	// 品牌ID
	var param = $("#brandSel").serialize() + "&csrftoken=" + getTokenVal();
	popDialog(url, param, $("#addDialogTitle").text());
}

/* 
 * 弹出Dialog画面
 * 
 * 
 * 
 */
function popDialog(url, param, title) {
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			$("#savePage").html(msg);
			cherryValidate({			
				formId: "saveForm",		
				rules: {		
					duration: {required: true, digits: true, maxlength: 9},	// 密码有效期
					remindAhead: {digits: true, maxlength: 2},	// 密码修改提醒
					repetitionInterval: {digits: true, maxlength: 2},	// 密码重复间隔
					complexity: {maxlength: 15},	// 密码复杂度
					pwLength: {required: true, digits: true, maxlength: 2},	// 密码最小长度
					maxLength: {required: true, digits: true, maxlength: 2},	// 密码最大长度
					retryTimes: {digits: true, maxlength: 2}	// 重试次数
				}		
			});
			var dialogSetting = {
					bgiframe: true,
					top:100,
					width:700, 
					height:500,
					minWidth:700,
					minHeight:500,
					zIndex: 10,
					modal: true, 
					title: title,
					close: function(event, ui) {dialogClose();}
				};
			$('#saveDialog').dialog(dialogSetting);
		}
	});
}

/* 
 * 关闭编辑画面
 * 
 * 
 * 
 */
function doClose(id) {
	$(id).dialog( "destroy" ); 
	$(id).remove();
}

/* 
 * 保存处理
 * 
 * 
 * 
 */
function doSave(url) {
	if (!$('#saveForm').valid()) {
		return false;
	};
	if (!CheckComplexity()) {
		return false;
	}
	var param = null;
	$("#saveForm").find(":input").not(":disabled").each(function() {
		if (!$(this).is(":checkbox") || $(this).is(":checkbox[checked]")) {
			$(this).val($.trim(this.value));
			if (null == param) {
				param = $(this).serialize();
			} else {
				param += "&" + $(this).serialize();
			}
		}
	});
	param += (param ? "&":"") + "csrftoken=" + getTokenVal();
	// 禁用确定按钮
	$("#confirmBtn").attr("disabled", "disabled");
	cherryAjaxRequest({
		url: url,
		param: param,
		isDialog: true,
		resultId: "#saveActionResult",
		bodyId: "#saveDialog",
		callback: function(msg) {
			// 激活确定按钮
			$("#confirmBtn").removeAttr("disabled");
			binOLPLUPM04_global.refreshKbn = true;
		}
	});
}

/* 
 * 关闭Dialog
 * 
 * 
 * 
 */
function dialogClose() {
	doClose('#saveDialog');
	if (binOLPLUPM04_global.refreshKbn) {
		// 刷新密码安全配置信息
		pwConfByBrand();
		binOLPLUPM04_global.refreshKbn = false;
	}
}

/* 
 * 控制其他字符输入框的状态
 * 
 * 
 * 
 */
function doOtherChar(obj) {
	if ($(obj).is(":checked")) {
		$("#otherCharText").show();
		$("#otherChar").removeAttr("disabled");
		document.getElementById("otherChar").focus();
	} else {
		$("#otherCharText").hide();
		$("#otherChar").attr("disabled", "disabled");
	}
}

/*
 * 添加错误信息
 * 
 * Inputs:  String:text		错误信息
 * 
 * 
 */
function errSpan(text) {
	// 新建显示错误信息的span
	var errSpan = document.createElement("span");
	$(errSpan).attr("class", "ui-icon icon-error tooltip-trigger-error");
	$(errSpan).attr("id", "errorText");
	$(errSpan).attr("title",'error|'+ text);
	$(errSpan).attr("style","");
	$(errSpan).cluetip({
    	splitTitle: '|',
	    width: 150,
	    tracking: true,
	    cluetipClass: 'error', 
	    arrows: true, 
	    dropShadow: false,
	    hoverIntent: false,
	    sticky: false,
	    mouseOutClose: true,
	    closePosition: 'title',
	    closeText: '<span class="ui-icon ui-icon-close"></span>'
	});
	return errSpan;
}

/*
 * 密码复杂度验证
 * 
 * 
 * 
 * 
 */
function CheckComplexity() {
	$("#errMsg").removeAttr("class");
	$("#errorText").remove();
	if (!$("#hasAlpha").is(":checked") && !$("#hasNumeric").is(":checked")) {
		$("#errMsg").append(errSpan($("#errMsgText1").text()));
		$("#errMsg").attr("class", "error");
		return false;
	}
	if ($("#hasOtherChar").is(":checked")) {
		var otherChar = $.trim($("#otherChar").val());
		if (null == otherChar || "" == otherChar) {
			$("#errMsg").append(errSpan($("#errMsgText2").text()));
			$("#errMsg").attr("class", "error");
			return false;
		}
		var str = otherChar.split('').sort().join('').replace(/(.)\1+/g, '$1');
		if (str.length != otherChar.length) {
			$("#errMsg").append(errSpan($("#errMsgText3").text()));
			$("#errMsg").attr("class", "error");
			return false;
		}
//		var regex = /^[_.*,]+$/;
//		if (!regex.test(otherChar)) {
//			$("#errMsg").append(errSpan($("#errMsgText4").text()));
//			$("#errMsg").attr("class", "error");
//			return false;
//		}
	}
	return true;
}
