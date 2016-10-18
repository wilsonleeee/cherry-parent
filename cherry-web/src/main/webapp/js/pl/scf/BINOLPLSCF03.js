
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(function(){
	
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
	
	// 表单验证初期化
	cherryValidate({
		formId: 'addAuditInfo',
		rules: {
			bussinessTypeCode: {required: true},
			initiatorType: {required: true},
			initiatorID: {required: true},
			auditorType: {required: true},
			auditorID: {required: true}
		}
	});
	
});	

//根据品牌查询业务类型List
function plscf03_searchBuType(url,object,defaultSelect) {
	
	var $select = $('#bussinessTypeCode');
	$select.empty();
	$select.append('<option value="">'+defaultSelect+'</option>');
	$('#initiatorType').val('');
	$('#auditorType').val('');
	$('#initiatorID').empty();
	$('#initiatorID').append('<option value="">'+defaultSelect+'</option>');
	$('#auditorID').empty();
	$('#auditorID').append('<option value="">'+defaultSelect+'</option>');
	$select.parent().addClass("error");
	$('#initiatorType').parent().addClass("error");
	$('#auditorType').parent().addClass("error");
	$('#initiatorID').parent().addClass("error");
	$('#auditorID').parent().addClass("error");
	var param = $(object).serialize();
	var callback = function(msg) {
		if(msg) {
			var json = eval('('+msg+')');
			for(var i in json) {
				$select.append('<option value="'+json[i].configValue+'">'+escapeHTMLHandle(json[i].commentsChinese)+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

// 根据身份类型查询身份信息List
function plscf03_searchCodeByType(url,object,defaultSelect) {
	
	var $select = $(object).parents('td').next().next().find('select');
	$select.empty();
	$select.append('<option value="">'+defaultSelect+'</option>');
	$select.parent().addClass("error");
	var param = $(object).serialize();
	if($('#brandInfoId').length > 0) {
		param += '&' + $('#brandInfoId').serialize();
	}
	var callback = function(msg) {
		if(msg) {
			var json = eval('('+msg+')');
			for(var i in json) {
				$select.append('<option value="'+json[i].code+'">'+escapeHTMLHandle(json[i].name)+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

function plscf03_saveAudit() {
	
	if(!$('#addAuditInfo').valid()) {
		return false;
	}
	
	var param = $('#addAuditInfo').find(':input').serialize();
	var callback = function(msg) {
		if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
	};
	cherryAjaxRequest({
		url: $('#addAuditUrl').attr('href'),
		param: param,
		callback: callback
	});
	
}