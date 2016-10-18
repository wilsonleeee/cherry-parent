/*
 * 全局变量定义
 */
var binolssprm06_global = {};

//是否刷新父页面
binolssprm06_global.doRefresh = false;

//是否需要解锁
binolssprm06_global.needUnlock = true;

window.onbeforeunload = function(){
	if (binolssprm06_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (binolssprm06_global.doRefresh) {
				// 刷新父页面
				window.opener.search();
			}
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
		    primaryCategoryCode: {alphanumeric:true, maxlength: 4},	// 大分类代码
		    primaryCategoryNameCN: {maxlength: 20},	            // 大分类中文名
		    primaryCategoryNameEN: {maxlength: 20},	            // 大分类英文名
		    secondryCategoryCode: {alphanumeric:true,  maxlength: 4},// 中分类代码
		    secondryCategoryNameCN: {maxlength: 20},	        // 中分类中文名
		    secondryCategoryNameEN: {maxlength: 20},	        // 中分类英文名
		    smallCategoryCode: {alphanumeric:true,  maxlength: 4},	// 小分类代码
		    smallCategoryNameCN: {maxlength: 20},	            // 小分类中文名
		    smallCategoryNameEN: {maxlength: 20}                // 小分类英文名
		}		
	});
} );

/* 
 * 取得品牌信息ID参数
 * 
 */
function getParams() {
	// 参数(品牌信息ID)
	var params = $("#brandSel").serialize();
	var parentToken = getParentToken();
	params += "&" + parentToken;
	return params;
}

///* 
// * 刷新大分类中英文名
// * 
// * Inputs:  Object:obj		大分类带代码
// * 			String:url		AJAX请求地址 
// */
//function doPrimaryCate(obj,url) {
//	$("#primaryCateCN").val("");
//	$("#primaryCateEN").val("");
//	if ("" == $(obj).val()) {
//		return false;
//	}
//	// 参数
//	var params = $(obj).serialize() + "&" + getParams();
//	$.ajax({
//       url: url, 
//       type: 'post',
//       dataType: 'json',
//       data: params,
//       success: function(json) {
//					if (json) {
//						if (json["primaryCategoryCode"] != null){
//							$("#primaryCateCN").val(json["primaryCategoryNameCN"]);
//							$("#primaryCateEN").val(json["primaryCategoryNameEN"]);
//							$("#primaryCateCN").attr('readonly','readonly');
//							$("#primaryCateEN").attr('readonly','readonly');
//						}
//					}
//				}
//   });
//}

///* 
// * 刷新中分类中英文名 
// * Inputs:  Object:obj		中分类代码
// * 			String:url		AJAX请求地址 
// */
//function doSecondryCate(obj,url) {
//	$("#secondryCateCN").val("");
//	$("#secondryCateEN").val("");
//	if ("" == $(obj).val()) {
//		return false;
//	}
//	// 参数
//	var params = $(obj).serialize() + "&" + getParams();
//	$.ajax({
//        url: url, 
//        type: 'post',
//        dataType: 'json',
//        data: params,
//        success: function(json) {
//					if (json) {
//						if (json["secondryCategoryCode"] != null){
//							$("#secondryCateCN").val(json["secondryCategoryNameCN"]);
//							$("#secondryCateEN").val(json["secondryCategoryNameEN"]);
//							$("#secondryCateCN").attr('readonly','readonly');
//							$("#secondryCateEN").attr('readonly','readonly');
//						}
//					}
//				}
//    });
//}

///* 
// * 刷新小分类中英文名
// * 
// * Inputs:  Object:obj		小分类带代码
// * 			String:url		AJAX请求地址 
// */
//function doSmallCate(obj,url) {
//	$("#smallCateCN").val("");
//	$("#smallCateEN").val("");
//	if ("" == $(obj).val()) {
//		return false;
//	}
//	// 参数
//	var params = $(obj).serialize() + "&" + getParams();
//	$.ajax({
//       url: url, 
//       type: 'post',
//       dataType: 'json',
//       data: params,
//       success: function(json) {
//					if (json) {
//						if (json["smallCategoryCode"] != null){;
//							$("#smallCateCN").val(json["smallCategoryNameCN"]);
//							$("#smallCateEN").val(json["smallCategoryNameEN"]);
//							$("#smallCateCN").attr('readonly','readonly');
//							$("#smallCateEN").attr('readonly','readonly');
//						}
//					}
//				}
//   });
//}

//保存
function doSave(url) {
	addCheckRules();
	
	//设置大分类、中分类编号
	if($("#categoryType").val()==1){
		$("#primaryCategoryCode").val($("#primarySel").val());
	}else if($("#categoryType").val()==2){
		$("#primaryCategoryCode").val($("#primarySel").val());
		$("#secondryCategoryCode").val($("#secondrySel").val());
	}
	
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
	//分类类型
	params += "&categoryType=" + $("#categoryType").val();
	cherryAjaxRequest({
		url: url,
		param: params,
		callback: function(msg) {
		binolssprm06_global.doRefresh = true;	
		},
		coverId: "#pageButton"
	});
}

function doClose(){
	window.close();
}
	
/* 
 * 联动下拉框选项
 * 
 * Inputs:  String:url 		AJAX请求地址
 * 
 */
function getSelOpts(url) {
	$("#secondrySel").find("option:not(:first)").remove();
	// 参数
	var params = getParams();
	doAjaxMulSel({
		url: url,
		params: params,
		selects: [{selId:"#primarySel", selName: "primaryCateList", optVal: "primaryCateCode", optLab:"primaryCateName"}	// 大分类
		          ]		
	});
}
	
//分类类型修改后清空
function clearAddCategory(){
	$("#primaryCategoryCode").val("");
	$("#primaryCateCN").val("");
	$("#primaryCateEN").val("");
	$("#secondryCategoryCode").val("");
	$("#secondryCateCN").val("");
	$("#secondryCateEN").val("");
	$("#smallCategoryCode").val("");
	$("#smallCateCN").val("");
	$("#smallCateEN").val("");
	$("#categoryType0").addClass("hide");
	$("#categoryType1").addClass("hide");
	$("#categoryType2").addClass("hide");
	$("#primaryRow").addClass("hide");
	$("#secondryRow").addClass("hide");
	clearRules();
}

/* 
 * 新增分类
 * 
 */
function addCategory(){
	switch($("#categoryType").val()){
		case '0'://大分类
			$("#categoryType0").removeClass("hide");
			break;
		case '1'://中分类
			$("#primarySel").get(0).selectedIndex=0;
			$("#primaryRow").removeClass("hide");
			$("#categoryType1").removeClass("hide");
			break;
		case '2'://小分类
			$("#primarySel").get(0).selectedIndex=0;
			$("#secondrySel").find("option:not(:first)").remove();
			$("#primaryRow").removeClass("hide");
			$("#secondryRow").removeClass("hide");
			$("#categoryType2").removeClass("hide");
			break;
	}
}

/* 
 * 下拉框中分类
 * 
 */
function doSecondCate(obj, url){
	//只有分类类型为小分类时查询
	if($("#categoryType").val() == 2){
		$("#secondrySel").find("option:not(:first)").remove();
		if ("" == $(obj).val()) {
			return false;
		}
		// 参数
		var params = getParams() + "&" + $(obj).serialize();
		doAjax(url, "secondCateCode", "secondCateName", "#secondrySel", params);
	}
}

/*
 * 清除验证规则
 * 
 */
function clearRules(){
	$("#primaryCategoryCode").rules("remove", "required");
	$("#secondryCategoryCode").rules("remove", "required");
	$("#smallCategoryCode").rules("remove", "required");
	$("#primarySel").rules("remove", "required");
	$("#secondrySel").rules("remove", "required");
	
	$("#primaryCategoryCode").valid();
	$("#secondryCategoryCode").valid();
	$("#smallCategoryCode").valid();
	$("#primarySel").valid();
	$("#secondrySel").valid();
	
	$("#primaryCategoryCode").parent().find("#errorText").remove();
	$("#primaryCategoryCode").parent().removeClass("error");
	$("#secondryCategoryCode").parent().find("#errorText").remove();
	$("#secondryCategoryCode").parent().removeClass("error");
	$("#smallCategoryCode").parent().find("#errorText").remove();
	$("#smallCategoryCode").parent().removeClass("error");
	$("#primarySel").parent().find("#errorText").remove();
	$("#primarySel").parent().removeClass("error");
	$("#secondrySel").parent().find("#errorText").remove();
	$("#secondrySel").parent().removeClass("error");
}

/*
 * 添加验证规则
 * 
 */
function addCheckRules(){
	clearRules();
	var categoryTypeVal = $("#categoryType").val();
	if(categoryTypeVal == 0){
		$("#primaryCategoryCode").rules("add", {required: true});
	}else if(categoryTypeVal == 1){
		$("#secondryCategoryCode").rules("add", {required: true});
		$("#primarySel").rules("add", {required: true});
	}else if(categoryTypeVal == 2){
		$("#smallCategoryCode").rules("add", {required: true});
		$("#primarySel").rules("add", {required: true});
		$("#secondrySel").rules("add", {required: true});
	}
}