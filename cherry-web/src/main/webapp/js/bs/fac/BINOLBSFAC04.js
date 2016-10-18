/*
 * 全局变量定义
 */
var BSFAC04_global = {};
//是否刷新父页面
BSFAC04_global.doRefresh = false;
//是否需要解锁
BSFAC04_global.needUnlock = true;

window.onbeforeunload = function(){
	if (BSFAC04_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (BSFAC04_global.doRefresh) {
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
$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
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

// 删除DIV块
function delDiv(_this) {
	$(_this).parent().parent().remove();
}
// 添加DIV块
function addDiv(target,source) {
	$(target).append($(source).html());
}
// 参数序列化
function getParams(obj){
	return $(obj).find(":input").serialize();
}
// 对象JSON化
function toJSON(obj){
	var JSON = [];
	$(obj).find(':input').each(function(){
		if(this.value != '') {
			JSON.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
		}
	});
	return "{"+JSON.toString()+"}";
}
// 对象JSON数组化
function toJSONArr(obj){
	var JSONArr = [];
	$(obj).each(function(){
		JSONArr.push(toJSON(this));
	});
	return "["+JSONArr.toString()+"]";
}
// 取得带token的URL
function getTokenUrl(url){
	// token
	var parentToken = getParentToken();
	return url + "?" + parentToken;
}
//由省份取得城市联动
function BSFAC04_getCity(obj,url,i18nText){
	$obj = $(obj);
	$city = $(obj).parent().parent().find(":input[name='cityId']");
	url = getTokenUrl(url) + "&regionId=" + $obj.val();
	// 城市初始化
	$city.empty();
	$city.append("<option  value=''>"+i18nText+"</option>");
	// AJAX请求
    doAjax2(url, "regionId", "regionName", $city);
}
//保存员工
function BSFAC04_saveFac(url){
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	// 基本信息
	var baseInfo = getParams("#base_info");
	// 地址信息
	var addressInfo = "addressInfo=" + toJSONArr("#b_address>table");
	// URL
	var param = baseInfo + "&" + addressInfo;
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			//是否刷新父页面
			BSFAC04_global.doRefresh = true;
		},
		coverId: "#saveButton"
	});
}