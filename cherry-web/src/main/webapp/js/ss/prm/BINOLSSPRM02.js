/*
 * 全局变量定义
 */
var binOLSSPRM02_global = {};
/*
 * 全局变量保存系统取号值
 */
var TZZK_code = null;

var DH_code = null;

var DHMY_code = null;
//是否刷新父页面
binOLSSPRM02_global.doRefresh = false;

//当前点击对象
binOLSSPRM02_global.thisClickObj = {};

//部门对象
binOLSSPRM02_global.departObj = {};

/* 
 * 页面关闭
 * 
 * 
 * 
 */
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
		if (binOLSSPRM02_global.doRefresh) {
			// 刷新父页面
			window.opener.search();
			TZZK_code = null;
			DH_code = null;
			DHMY_code = null;
		}
	}
};

/* 
 * 页面初期化方法
 * 
 * 
 * 
 */
$(document).ready(function() {
	var value=$("#promCate1").val();
	var virtualPrm=$("#virtualPrm").val();
	$("#promCate").val(value);
	if(virtualPrm!="3"){
		$("#promCate1").prop("disabled",true);
	}else{
		$("#promCate1").prop("disabled",false);
	}
	
	//判断促销类别是否是促销礼品
	validateType(value);
	if (window.opener) {
		window.opener.lockParentWindow();
	}
//	$("#filterText").keyup(
//		function(){
//			// 参数
//			var params = getParams();
//			var $manuFactId = $("#prtVendor").find("input[name='manuFactId']");
//			if ($manuFactId.length > 0) {
//				var idParam = "";
//				$manuFactId.each(function (){
//					if ("" != $(this).val()) {
//						idParam += "&" + $(this).serialize();
//					}
//				});
//				params += idParam;
//			}
//			tableFilter(this, params);
//			return false;
//		});
	// 开始销售日期
	initDate("#sellStartDate", "#sellEndDate", "maxDate");
	// 停止销售日期
	initDate("#sellEndDate", "#sellStartDate", "minDate");
	cherryValidate({			
		formId: "mainForm",		
		rules: {		
			unitCode: {
				required: true, 
				//prmCodeValid:true, 
				maxlength: 20
			},	// 厂商编码
			nameTotal: {required: true, byteLengthValid: [40]},	// 中文名
			promPrtCateId: {required: true},	// 类别
			volume: {floatValid: [6,2]},	// 容量
			nameAlias: {maxlength: 50},	// 别名
			weight: {floatValid: [6,2]},	// 重量
			nameShort: {maxlength: 20},	// 中文简称
			nameForeign: {byteLengthValid: [40]},	// 英文名
			standardCost: {number:true},	// 标准成本
			nameShortForeign: {maxlength: 20},	// 英文简称
			sellStartDate: {dateValid: true},	// 开始销售日期
			sellEndDate: {dateValid: true},	// 停止销售日期
			shelfLife: {digits: true, maxlength: 9},	// 保质期
			barCode: {
				required: true ,
//				prmCodeValid:true
				maxlength: 13
			}	// 促销产品条码
		}		
	});
} );

/* 
 * 取得参数
 * 
 * 
 * 
 */
function getParams() {
	// 参数(品牌信息ID)
	var params = $("#brandSel").serialize();
	var parentToken = getParentToken();
	params += "&" + parentToken;
	return params;
}

/* 
 * 促销产品保存
 * 
 * Inputs:  String:url 		AJAX请求地址
 * 
 */
function doSave(url) {
	if (!$('#mainForm').valid()) {
		return false;
	};
	// 参数序列化
	var param = null;
	$("#baseInfo").find(":input").each(function() {
		if (!$(this).is(":disabled")) {
			$(this).val($.trim(this.value));
			if (null == param) {
				param = $(this).serialize();
			} else {
				param += "&" + $(this).serialize();
			}
		}
	});
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			binOLSSPRM02_global.doRefresh = true;
		},
		coverId: "#pageButton"
	});
}

/* 
 * 联动下拉框选项
 * 
 * Inputs:  String:url 		AJAX请求地址
 * 
 */
function getSelOpts(url) {
	$("#secCateSel").find("option:not(:first)").remove();
	$("#smallCateSel").find("option:not(:first)").remove();
	// 参数
	var params = getParams();
	doAjaxMulSel({
		url: url,
		params: params,
		selects: [{selId:"#priCateSel", selName: "primaryCateList", optVal: "primaryCateCode", optLab:"primaryCateName"}	// 大分类
		          ]		
	});
	// 套装折扣
	if($("#promCate").val() == 'TZZK'){
		// 取得编号
		getCode(4);
	}else if($("#promCate").val() == 'DHCP'){
		// 取得编号
		getCode(5);
	}else{
		// 编码，条码初始化
		$("#unitCode").val("");
		$("#barCode").val("");
	}
}

/* 
 * 删除DIV
 * 
 * Inputs:  Object:obj 		删除按钮
 * 
 */
function delDivRow(obj) {
	$(obj).parents('table').remove();
}

/* 
 * 添加DIV
 * 
 * Inputs:  String:id		需要添加的DIV
 * 			String:rowId	新增加的DIV 		
 * 
 */
function addDivRow(id, rowId) {
	$(id).append($(rowId).html());
}

/* 
 * 添加价格DIV
 * 
 * 
 */
function addPriceDivRow() {
	// 添加价格DIV
	addDivRow('#salePriceInfo', '#salePriceRow');
	// 标准销售价格初期化
	initSalePrice();
}

/* 
 * 添加新行
 * 
 * Inputs:  String:id 			需要添加新行的表格ID
 * 			String:rowId		新行ID
 * 
 */
function SSPRM02_addTableRow(tableId, rowId) {
	// 添加DIV
	addDivRow(tableId, rowId);
	// 初期显示价格日期
	initPriceDate();
	// 设置行样式
	setRowClass(tableId);
}

/* 
 * 删除行
 * 
 * Inputs:  Object:obj 			需要删除的行
 * 			
 * 
 */
function SSPRM02_delTableRow(obj) {
	$(obj).parents("tr").remove();
	// 设置行样式
	setRowClass("#priceDialogBody");
}

/* 
 * 刷新生产厂商
 * 
 * Inputs:  String:URL		AJAX请求地址
 * 			
 * 
 */
function ajaxFactory(url) {
	// 删除生产厂商，除了默认生产厂商
	$("#prtVendor").find("table:not(:first)").remove();
	// 默认生产厂商输入框清空
	$("#defVendor tbody").html($("#prtVendorRow tbody").html());
	// 参数
	var params = getParams();
	$.ajax({
        url: url, 
        type: 'post',
        dataType: 'json',
        data: params,
        success: function(json) {
					if (json) {
						$("#defFactory").html(json["factoryName"]);
						$("#manuFactId").val(json["manuFactId"]);
					}
				}
    });
}

/* 
 * 显示厂商选择页面
 * 
 * Inputs:  Object:obj		选择厂商按钮
 * 			
 * 
 */
function showFactoryDialog(obj) {
	// 参数
	var params = getParams();
	var $manuFactId = $("#prtVendor").find("input[name='manuFactId']");
	if ($manuFactId.length > 0) {
		var idParam = "";
		$manuFactId.each(function (){
			if ("" != $(this).val()) {
				idParam += "&" + $(this).serialize();
			}
		});
		params += idParam;
	}
	popDataTableOfFactory(obj, params);
	binOLSSPRM02_global.thisClickObj = obj;
}

/* 
 * 选择生产厂商
 * 			
 * 
 */
function selectFactory() {
	// 选中的厂商
	var $selRadio = $("#factory_dataTable").find(":radio[checked]");
	if ($selRadio.length > 0) {
		var selVal = $selRadio.val();
		// 厂商名称
		var factoryName = $selRadio.parents("tr").find(".factoryName").text();
		var $parentObj = $(binOLSSPRM02_global.thisClickObj).parents("td");
		$parentObj.find("#errorText").remove();
		// 生产厂商ID
		$parentObj.find("input[name='manuFactId']").val(selVal);
		$(binOLSSPRM02_global.thisClickObj).prev("span").text(factoryName);
	}
	$('#factoryDialog').dialog( "destroy" );
}

/* 
 * 显示部门选择页面
 * 
 * Inputs:  Object:obj		选择厂商按钮
 * 			
 * 
 */
function SSPRM02_showDepartDialog(obj) {
	// 参数
	var params = getParams();
//	var $departInfoId = $("#priceDialogBody").find("input[name='departInfoId']");
//	if ($departInfoId.length > 0) {
//		var idParam = "";
//		$departInfoId.each(function (){
//			if ("" != $(this).val()) {
//				idParam += "&" + $(this).serialize();
//			}
//		});
//		params += idParam;
//	}
	popDataTableOfDepart(obj, params);
	binOLSSPRM02_global.departObj = obj;
}

/* 
 * 选择部门
 * 			
 * 
 */
function selectDepart() {
	// 选中的部门
	var $selRadio = $("#depart_dataTable").find(":radio[checked]");
	if ($selRadio.length > 0) {
		var selVal = $selRadio.val();
		// 部门名称
		var departName = $selRadio.parent().find("input[name='departName']").val();
		// 部门类型
		var depType = $selRadio.parent().find("input[name='depType']").val();
		var $parentObj = $(binOLSSPRM02_global.departObj).parent();
		$parentObj.find("#errorText").remove();
		// 部门ID
		$parentObj.find("input[name='departInfoId']").val(selVal);
		// 部门类型
		$parentObj.find("input[name='depType']").val(depType);
		$parentObj.find("span.left").text(departName);
	}
	$('#departDialog').dialog( "destroy" );
}

/* 
 * 下拉框中分类
 * 
 * Inputs:  Object:obj		大分类
 * 			String:url		AJAX请求地址
 * 			
 * 
 */
function doSecondCate(obj, url) {
	$("#secCateSel").find("option:not(:first)").remove();
	$("#smallCateSel").find("option:not(:first)").remove();
	if ("" == $(obj).val()) {
		return false;
	}
	// 参数
	var params = getParams() + "&" + $(obj).serialize();
	doAjax(url, "secondCateCode", "secondCateName", "#secCateSel", params);
}

/* 
 * 取得下拉框小分类
 * 
 * Inputs:  Object:obj		中分类
 * 			String:url		AJAX请求地址
 * 
 */
function doSmallCate(obj, url) {
	$("#smallCateSel").find("option:not(:first)").remove();
	if ("" == $(obj).val()) {
		return false;
	}
	// 参数
	var params = getParams() + "&" + $("#priCateSel").serialize() + "&" + $(obj).serialize();
	doAjax(url, "smallCateCode", "smallCateName", "#smallCateSel", params);
}

/* 
 * 初期化日期
 * 
 * Inputs:  String:date1		第一个日期
 * 			String:date2		第二个日期
 * 			String:type			类型
 * 
 */
function initDate(date1, date2, type) {
	$(date1).cherryDate({
		holidayObj: $("#dateHolidays").val(),
		beforeShow: function(input){
			var value = $(date2).val();
			return [value, type];
		}
	});
}

/*
 * 验证提交的数据
 * 
 * 
 * 
 */
function SSPRM02_checkData() {
	var result = true;
	// 验证厂商信息
	$("#prtVendor").find("table").each(
		function(){
			// 厂商ID
			var $manuFactId = $(this).find("input[name='manuFactId']");
			if (1 == $manuFactId.length && "" == $manuFactId.val()) {
				if (0 == $manuFactId.parent().find("#errorText").length) {
					$manuFactId.parent().append(errSpan($("#errmsg1").html()));
				}
				result = false;
			}
		});
	// 验证部门价格信息
	$("#priceDialogBody").find("tr").each(
		function() {
			// 部门ID
			var $departInfoId = $(this).find("input[name='departInfoId']");
			if (1 == $departInfoId.length && "" == $departInfoId.val()) {
				if (0 == $departInfoId.parent().find("#errorText").length) {
					$departInfoId.parent().append(errSpan($("#errmsg2").html()));
				}
				result = false;
			}
		});
	return result;
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
 * 上传促销品图片
 * 
 * 
 * 
 */
function doAjaxFileUpload() {
	var options = {
		success	: function (data, status)
		{	
			uploadSuccess(data.result, data.msg);
			if ("OK" == data.result) {
				// 保存后的图片路径
				var promImagePath = "<input type='hidden' name='promImagePath' value='" + data.imagePath + "'/>";
				$("#baseInfo").append(promImagePath);
			}
		} 
	};
	uploadFile(options);
}

/*
 * 部门价格初期化
 * 
 */
function initPriceDate() {
	$("#priceDialogBody").find("tr").each(function () {
		$(this).find(".date").unbind("cherryDate");
		$(this).find("input[name='startDate']").cherryDate({
			holidayObj: $("#dateHolidays").val(),
			beforeShow: function(input){
				var value = $(input).parents("tr").find("input[name='endDate']").val();
				return [value, "maxDate"];
				
			}
		});
		$(this).find("input[name='endDate']").cherryDate({
			holidayObj: $("#dateHolidays").val(),
			beforeShow: function(input){
				var value = $(input).parents("tr").find("input[name='startDate']").val();
				return [value, "minDate"];
			}
		});
	});
}

/*
 * 标准销售价格初期化
 * 
 */
function initSalePrice() {
	$("#salePriceInfo").find("table").each(function() {
		$(this).find(".date").unbind("cherryDate");
		$(this).find("input[name='priceStartDate']").cherryDate({
			holidayObj: $("#dateHolidays").val(),
			beforeShow: function(input){
				var value = $(input).parents("table").find("input[name='priceEndDate']").val();
				return [value, "maxDate"];
				
			}
		});
		$(this).find("input[name='priceEndDate']").cherryDate({
			holidayObj: $("#dateHolidays").val(),
			beforeShow: function(input){
				var value = $(input).parents("table").find("input[name='priceStartDate']").val();
				return [value, "minDate"];
			}
		});
	});
}

/*
 * 删除错误提示
 * 
 */
function removeErrorSpan(obj) {
	if ("" != $(obj).val()) {
		$(obj).parent().find("#errorText").remove();
	}
}

/*
 * 筛选厂商信息
 * 
 */
function factoryFilter(obj) {
	// 参数
	var params = getParams();
	var $manuFactId = $("#prtVendor").find("input[name='manuFactId']");
	if ($manuFactId.length > 0) {
		var idParam = "";
		$manuFactId.each(function (){
			if ("" != $(this).val()) {
				idParam += "&" + $(this).serialize();
			}
		});
		params += idParam;
	}
	tableFilter(obj, params);
}

//判断促销类别是否是促销礼品
function validateType(value){	
	if(value!=''){
		if(value!='CXLP'){
			 $("#validate_mode").css("display","none");
			 $("#mode1").css("display","none");
			 $("#mode").get(0).options[0].selected = true;
		}else{
			$("#validate_mode").css("display","");
			$("#mode1").css("display","");
			$("#mode").get(0).options[0].selected = true;//第一个值被选中  
		}
	}else{
		 $("#validate_mode").css("display","none");
		 $("#mode1").css("display","none");
		 $("#mode").get(0).options[0].selected = true;
	}
}

/*
 * 促销品类别改变
 * 
 */
function changeCate(_this){
	// 促销品类别
	var value = $(_this).val();
	$("#promCate").val(value);
	//判断促销类别是否是促销礼品
	validateType(value)
	// 兑换需要积分值
	var $exPoint = $("#exPoint");
	// 容量
	var $volume = $("#volume");
	// 重量
	var $weight = $("#weight");
	// 容量单位
	var $volumeUnit = $("#volumeUnit");
	// 重量单位
	var $weightUnit = $("#weightUnit");
	// 样式
	var $styleCode = $("#styleCode");
	// 使用方式
	var $operationStyle = $("#operationStyle");
	// 保质期
	var $shelfLife = $("#shelfLife");
	// 成本价格提示信息
	var $cost_tip = $("#cost_tip");
	// 管理库存
	var $isStock = $("#isStock");
	
	// 编码，条码初始化
	$("#unitCode").val("");
	$("#barCode").val("");
	
	// 积分兑礼
	if(value == 'DHCP'){
		$exPoint.prop("disabled",false);
		$isStock.find("option:last").prop("selected",true);
		getCode(5);
	} else if(value == 'DHMY'){
		$exPoint.prop("disabled",false);
		$isStock.find("option:last").prop("selected",true);
		getCode('A');
	} else{
		$exPoint.prop("disabled",true);
		$isStock.find("option:first").prop("selected",true);
	}

	// 套装折扣
	if(value == 'TZZK'){
		$volume.prop("disabled",true);
		$volumeUnit.prop("disabled",true);
		$weight.prop("disabled",true);
		$weightUnit.prop("disabled",true);
		$styleCode.prop("disabled",true);
		$shelfLife.prop("disabled",true);
		$operationStyle.prop("disabled",true);
		$cost_tip.show();
		$isStock.find("option:last").prop("selected",true);
		getCode(4);
	}else{
		$volume.prop("disabled",false);
		$volumeUnit.prop("disabled",false);
		$weight.prop("disabled",false);
		$weightUnit.prop("disabled",false);
		$styleCode.prop("disabled",false);
		$shelfLife.prop("disabled",false);
		$operationStyle.prop("disabled",false);
		$cost_tip.hide();
	}	
	// 整单去零
	if(value == 'ZDTL'){
		$("#unitCode").val('ZDTL');
		$("#barCode").val('ZDTL');
		$("#nameTotal").val($("#nameTotalMsg").val());
		$("#standardCost").val('0');
		$("#unitCode").prop("readOnly",true);
		$("#barCode").prop("readOnly",true);
		$("#nameTotal").prop("readOnly",true);
		$("#standardCost").prop("disabled",true);
		$isStock.find("option:last").prop("selected",true);
		$isStock.prop("disabled",true);
		$("#baseInfo").append("<input type='hidden' name='isStock' id='zdtlStock' value='0'/>");
	}else{
		$("#nameTotal").val("");
		$("#unitCode").prop("readOnly",false);
		$("#barCode").prop("readOnly",false);
		$("#nameTotal").prop("readOnly",false);
		$("#standardCost").val("");
		$("#standardCost").prop("disabled",false);
		$isStock.prop("disabled",false);
		if($("#baseInfo").find("#zdtlStock").length > 0){
			$("#baseInfo").find("#zdtlStock").remove();
		}
	}
	
	if(value == 'TZZK' || value == 'DHCP'|| value == 'DHMY'){
		$("#sellCost").show();
		$("#standardCost").hide();
	}else{
		$("#sellCost").hide();
		$("#standardCost").show();
	}
}

/**
 * 取得编号
 * @return
 */
function getCode(type){
	var code_global;
	if(type == 4){
		code_global = TZZK_code;
	}else if(type == 5){
		code_global = DH_code;
	}else if(type == 'A'){
		code_global = DHMY_code;
	}
	if(code_global == null || code_global == undefined){
		var url = "/Cherry/common/BINOLCM15_getSeqCode";
		var param = getParams() + "&type=" + type;
		ajaxRequest(url,param,function(code){
			$("#unitCode").val(code);
			$("#barCode").val(code);
			if(type == 4){
				TZZK_code = code;
			}else if(type == 5){
				DH_code = code;
			}else if(type == 'A'){
				DHMY_code = code;
			}
		});
	}else{
		$("#unitCode").val(code_global);
		$("#barCode").val(code_global);
	}
}
