var binOLBSPAT02_global = {};
binOLBSPAT02_global.needUnlock = true;
$(document).ready(function() {
	$('.tabs').tabs();
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		bscom03_showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		bscom03_showRegin(this,"cityTemp");
	});
	// 县级市选择
	$("#county").click(function(){
		// 显示县级市列表DIV
		bscom03_showRegin(this,"countyTemp");
	});
});

function doClose(){
	window.close();
}

window.onbeforeunload = function(){
	if (binOLBSPAT02_global.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
//添加单位
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(function(){
	
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
	cherryValidate({			
		formId: "add",		
		rules: {
			brandInfoId : {required: true},
		    code: {required: true,alphanumeric: true,maxlength: 15},
		    nameEn: {maxlength: 50},
		    nameCn: {required: true,maxlength: 50},
		    phoneNumber: {telValid:true,maxlength: 20},
		    postalCode:{zipValid:true,maxlength: 10},
		    provinceId:{required: true},
		    cityId : {required: true},
		    contactPerson:{maxlength: 200},
		    contactAddress:{maxlength: 200},
		    deliverAddress:{maxlength: 200}
	   }		
	});
});

//根据品牌ID筛选下拉列表
function changeBrandInfo(object,text) {
	// 清空省信息
	$('#provinceId').val("");
	$("#provinceText").text(text);
	$('#provinceTemp').empty();
	// 清空城市信息
	$('#cityId').val("");
	$("#cityText").text(text);
	$('#cityTemp').empty();
	// 清空县级市信息
	$('#countyId').val("");
	$("#countyText").text(text);
	$('#countyTemp').empty();
	if($(object).val() != "") {
		var callback = function(msg){
			if(msg) {
				// 默认地区
				var defaultTitle = $('#defaultTitle').text();
				// 全部
				var defaultText = $('#defaultText').text();
				var jsons = eval('('+msg+')');
				// 区域信息存在
				if(jsons.reginList.length > 0) {
					var html = '<div class="clearfix"><span class="label">'+defaultTitle+'</span><ul class="u2"><li onclick="bscom03_getNextRegin(this, \''+text+'\', 1);return false;">'+defaultText+'</li></ul></div>';
					for(var i in jsons.reginList) {
						html += '<div class="clearfix"><span class="label">'+escapeHTMLHandle(jsons.reginList[i].reginName)+'</span><ul class="u2">';
						for(var j in jsons.reginList[i].provinceList) {
							html += '<li id="'+jsons.reginList[i].provinceList[j].provinceId+'" onclick="bscom03_getNextRegin(this, \''+text+'\', 1);">'+escapeHTMLHandle(jsons.reginList[i].provinceList[j].provinceName)+'</li>';
						}
						html += '</ul></div>';
					}
					$('#provinceTemp').html(html);
				}
			}
		};
		cherryAjaxRequest({
			url: $('#filter_url').val(),
			param: $(object).serialize(),
			callback: callback
		});
	}
}

function save() {
	
	if(!$('#add').valid()) {
		return false;
	}
	var param = $('#add').find(':input').serialize();
	var callback = function(msg) {
		// 刷新父页面
		window.opener.BINOLBSPAT01.search();
	};
	cherryAjaxRequest({
		url: $('#PAT02_save').attr('href'),
		param: param,
		callback: callback	
	});
}
