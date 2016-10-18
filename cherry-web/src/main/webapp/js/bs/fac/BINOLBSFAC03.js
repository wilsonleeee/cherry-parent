/*
 * 全局变量定义
 */
var BSFAC03_global = {};
//是否刷新父页面
BSFAC03_global.doRefresh = false;
//是否需要解锁
BSFAC03_global.needUnlock = true;

window.onbeforeunload = function(){
	if (BSFAC03_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (BSFAC03_global.doRefresh) {
				var url = $('#search_url', window.opener.document).val();
				// 刷新父页面
				window.opener.search(url);
			}
		}
	}
};

function doClose(){
	window.close();
}

//返回
function BSFAC03_doBack(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	BSFAC03_global.needUnlock = false;
	$("#toDetailForm").submit();
}
// 初始化
$(document).ready(function() {
	if (window.opener) {
	  window.opener.lockParentWindow();
	}
	// 省份对象
	var $province = $("#b_address table.detail").find(":input[name='provinceId']");
	// 城市取得URL
	var getCityUrl = $("#getCity_url").val();
	var i18nText_selt = $("#global_select").val();
	$province.each(function(){
		// AJAX取得城市List
		BSFAC03_getCity(this,getCityUrl,i18nText_selt);
	});
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			manufacturerCode: {required: true, alphanumeric:true},// 厂商编号
			   factoryNameCN: {required: true},// 厂商名称
				   telePhone: {telValid: true},// 电话
					  mobile: {phoneValid: $("#mobileRule").val()},// 手机
					 zipCode: {zipValid: true}// 邮编
		}
	});
} );
//删除DIV块
function BSFAC03_delDiv(_this, inputId1, inputId2) {
	var $obj = $(_this).parent().parent().parent();
	var $obj0 = $obj.parent();
	var radio = $obj.find(":radio[checked]");
	var $inputId1 = $obj.find("#"+inputId1);
	var $inputId2 = $obj.find("#"+inputId2);
	if($inputId1.length == 0){
		$obj.remove();
	}else{
		$obj.empty();
		$obj.addClass("hide");
		var hidden = '<input type="hidden" name="option" value="0"/>';
		hidden = hidden + '<input type="hidden" name="' + inputId1 + '" value="' + $inputId1.val() + '"/>';
		if($inputId2.length != 0){
			hidden = hidden + '<input type="hidden" name="' + inputId2 + '" value="' + $inputId2.val() + '"/>';
		}
		$obj.html(hidden);
	}
	// 删除的为默认显示
	if(radio.length == 1){
		$obj0.find(":radio").first().attr("checked",true);
	}
	// 查找target下隐藏的删除按钮
	var delButton = $obj0.find("span.button-del");
	if(delButton.length == 1){
		delButton.addClass("hide");
	}
}
// 添加DIV块
function BSFAC03_addDiv(target,source) {
	var $target= $(target);
	// 查找target下隐藏的删除按钮
	var delButton = $target.find("span.hide");
	if(delButton.length != 0){
		delButton.removeClass("hide");
	}
	$target.append($(source).html());
	// 查找target下隐藏的按钮
	var delButton = $target.find("span.button-del");
	if(delButton.length == 1){
		delButton.addClass("hide");
		delButton.parent().find(":radio").attr("checked",true);
	}
}

// 参数序列化
function BSFAC03_getParams(obj){
	return $(obj).find(":input").serialize();
}
// 对象JSON化
function BSFAC03_toJSON(obj){
	var JSON = [];
	$(obj).find(':input').each(function(){
		if($(this).is(":radio")){
			if($(this).attr("checked")){
				this.value = '1';
			}else{
				this.value = '0';
			}		
		}
		if(this.value != '') {
			JSON.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
		}
	});
	return "{"+JSON.toString()+"}";
}
// 对象JSON数组化
function BSFAC03_toJSONArr(obj){
	var JSONArr = [];
	$(obj).each(function(){
		JSONArr.push(BSFAC03_toJSON(this));
	});
	return "["+JSONArr.toString()+"]";
}

//取得带token的URL
function BSFAC03_getTokenUrl(url){
	// token
	var parentToken = getParentToken();
	return url + "?" + parentToken;
}
//由省份取得城市联动
function BSFAC03_getCity(obj,url,i18nText){
	var $obj = $(obj);
	var $city = $(obj).parent().parent().find(":input[name='cityId']");
	var cityId = $city.val();
	var url = BSFAC03_getTokenUrl(url) + "&regionId=" + $obj.val();
	// 城市初始化
	$city.empty();
	$city.append("<option value=''>"+i18nText+"</option>");
	// AJAX请求
    doAjax2(url, "regionId", "regionName", $city, '' ,cityId);
}
// 更新厂商
function BSFAC03_saveFac(url){
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	// 基本信息
	var baseInfo = BSFAC03_getParams("#base_info");
	// 地址信息
	var addressInfo = "addressInfo=" + BSFAC03_toJSONArr("#b_address>table");
	// URL
	var param = baseInfo + "&" + addressInfo;
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			BSFAC03_global.doRefresh = true;
		},
		coverId: "#saveButton"
	});
}
