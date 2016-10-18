/*
 * 全局变量定义
 */
var binolssprm03_global = {};

//是否刷新父页面
binolssprm03_global.doRefresh = false;

//当前点击对象
binolssprm03_global.thisClickObj = {};

//部门对象
binolssprm03_global.departObj = {};

//是否需要解锁
binolssprm03_global.needUnlock = true;

window.onbeforeunload = function(){
	if (binolssprm03_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (binolssprm03_global.doRefresh) {
				// 刷新父页面
				window.opener.search();
			}
		}
	}
};

$(document).ready(function() {
	var value=$("#promCate1").val();	
	//判断促销类别是否是促销礼品
	if(value!=''){
		if(value!='CXLP'){
			 $("#validate_mode").css("display","none");
		}else{
			$("#validate_mode").css("display","");
		}
	}else{
		 $("#validate_mode").css("display","none");
	}
	if (window.opener) {
		window.opener.lockParentWindow();
	}
//	$("#filterText").keyup(
//			function(){
//				// 参数
//				var params = getParams();
//				var $manuFactId = $("#prtVendor").find("table:visible").find("input[name='manuFactId']");
//				if ($manuFactId.length > 0) {
//					var idParam = "";
//					$manuFactId.each(function (){
//						if ("" != $(this).val()) {
//							idParam += "&" + $(this).serialize();
//						}
//					});
//					params += idParam;
//				}
//				tableFilter(this, params);
//				return false;
//			});
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
			newBarCode: {
				required: true, 
			//	prmCodeValid:true
				maxlength:13
			}	// 促销产品条码
		}		
	});
	if($("#prmFacValid").find(".vendorSpan:visible").length > 0)
	{
		$("#usedBarcode").show();
	}
	if($("#prmFacInvalid").find(".vendorSpan:visible").length > 0)
	{
		$("#validBarcode").show();
	}
} );

/* 
 * 取得品牌参数
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
 * 联动下拉框选项
 * 
 * Inputs:  String:url 		AJAX请求地址
 * 
 */
function getBrandOpts(url) {
	$("#secCateSel").find("option:not(:first)").remove();
	$("#smallCateSel").find("option:not(:first)").remove();
	// 参数
	var params = getParams();
	doAjaxMulSel({
		url: url,
		params: params,
		selects: [{selId:"#priCateSel", selName: "PrimaryCateList", optVal: "primaryCateCode", optLab:"primaryCateName"},					// 大分类
		          {selId:"#moduleCodeSel", selName: "minPackTypeList", optVal: "packageTypeId", optLab:"packageName"}]		// 最小单位
	});
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
 * 添加厂商行DIV
 * 
 * Inputs:  String:id		需要添加的DIV
 * 			String:rowId	新增加的DIV 		
 * 
 */
function addDivRow(id, rowId) {
	var $table = $(id).find("table:visible");
	if (1 == $table.length) {
		$table.find("span.delBtn").show();
	}
	$(id).append($(rowId).html());
}

/* 
 * 删除厂商行DIV
 * 
 * Inputs:  Object:obj 		删除按钮
 * 			String:id		指定的DIV
 * 			String:keyName	关键字段的名称
 * 
 */
function delDivRow(obj, id, keyName) {
	var $table = $(obj).parents('table');
	if (0 == $table.find('"input[name=' + "'" + keyName + "'" + ']"').length) {
		$table.remove();
	} else {
		$table.hide();
		$table.find("input[name='validFlag']").val("0");
	}
	var $table = $(id).find("table:visible");
	if (1 == $table.length) {
		$table.find("span.delBtn").hide();
	}
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
 * 添加促销品部门销售价格新行
 * 
 * Inputs:  String:id 			需要添加新行的表格ID
 * 			String:rowId		新行ID
 * 
 */
function SSPRM03_addTableRow(tableId, rowId) {
	// 添加DIV
	addDivRow(tableId, rowId);
	// 初期显示价格日期
	initPriceDate();
	// 设置行样式
	setRowClass(tableId);
}

/* 
 * 删除促销品部门销售价格
 * 
 * Inputs:  Object:obj 			需要删除的行
 * 			
 * 
 */
function SSPRM03_delTableRow(obj) {
	var $tr = $(obj).parents("tr");
	if (0 == $tr.find("input[name='prmPriceDepartId']").length) {
		$tr.remove();
	} else {
		$tr.hide();
		$tr.attr("class", "del");
		$tr.find("input[name='validFlag']").val("0");
	}
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
	var $manuFactId = $("#prtVendor").find("table:visible").find("input[name='manuFactId']");
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
	binolssprm03_global.thisClickObj = obj;
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
		var $parentObj = $(binolssprm03_global.thisClickObj).parents("td");
		$parentObj.find("#errorText").remove();
		// 生产厂商ID
		$parentObj.find("input[name='manuFactId']").val(selVal);
		$(binolssprm03_global.thisClickObj).prev("span").text(factoryName);
	}
	$('#factoryDialog').dialog( "destroy" );
}

/* 
 * 显示部门选择页面
 * 
 * Inputs:  Object:obj		选择部门按钮
 * 			
 * 
 */
function SSPRM03_showDepartDialog(obj) {
	// 参数
	var params = getParams();
//	var $departInfoId = $("#priceDialogBody").find("tr").not(".del").find("input[name='departInfoId']");
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
	binolssprm03_global.departObj = obj;
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
		var $parentObj = $(binolssprm03_global.departObj).parent();
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
	$("#basicInfo").find(":input").not($(":input", "#prtVendor")).each(function() {
		if (!$(this).is(":disabled")) {
			$(this).val($.trim(this.value));
			if (null == param) {
				param = $(this).serialize();
			} else {
				param += "&" + $(this).serialize();
			}
		}
	});
	// 促销品厂商信息
	var manuFactInfo = [];
	$("#prtVendor").find("span.vendorSpan").each(function(){
		var m = [];
		$(this).find(':input').each(function(){
			if(this.value != '') {
				m.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent($.trim(this.value))+'"');
			}
		});
		manuFactInfo.push('{' + m.toString() + '}');
	});
	param += "&manuFactInfo=[" + manuFactInfo.toString() + "]";
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			binolssprm03_global.doRefresh = true;	
			if ($("#barCode").parent().hasClass("error")){
				$("#addBarCode").show();
			}
		},
		coverId: "#pageButton"
	});
}

/*
 * 验证提交的数据
 * 
 * 
 * 
 */
function SSPRM03_checkData() {
	var result = true;
	// 验证厂商信息
	$("#prtVendor").find("table:visible").each(
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
	$("#priceDialogBody").find("tr").not(".del").each(
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
	
/* 添加错误信息
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


function doBack(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	binolssprm03_global.needUnlock = false;
	$("#toDetailForm").submit();
}

function doClose(){
	window.close();
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
 * 上传促销品图片 
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
				$("#basicInfo").append(promImagePath);
			}
		} 
	};
	uploadFile(options);
}

/*
 * 显示全部部门价格
 * 
 */
function SSPRM03_showDepPrice() {
	// 显示按钮
	$("#show_all").hide();
	// 隐藏按钮
	$("#hide_all").show();
	// 部门价格
	$("#depPrice").show();
	// 分割线
	$("#page_hr").show();
}

/*
 * 隐藏全部部门价格
 * 
 */
function SSPRM03_hideDepPrice() {
	// 显示按钮
	$("#show_all").show();
	// 隐藏按钮
	$("#hide_all").hide();
	// 部门价格
	$("#depPrice").hide();
	// 分割线
	$("#page_hr").hide();
}

/*
 * 筛选部门
 * 
 */
function SSPRM03_filterDepart(obj) {
	
	var val = $(obj).val();
	$("#priceDialogBody").find("tr").not(".del").each(function() {
		// 部门名称
		var departName = $(this).find("span.departName").html().replace(/(^\s*)|(\s*$)/g, "");
		if ("&nbsp;" == departName) {
			$(this).remove();
		} else {
			if ("" == val) {
				$(this).show();
			} else {
				// 找到匹配的部门名称
				if (departName.indexOf(val) >= 0) {
					$(this).show();
				} else {
					$(this).hide();
				}
			}
		}
	});
	// 设置行样式
	setRowClass("#priceDialogBody");
}

/*
 * 部门价格初期化
 * 
 */
function initPriceDate() {
	  $("#priceDialogBody").find("tr").each(function () {
		$(this).find(".date").unbind("cherryDate");
		// 显示区分
		var $displayKbn = $(this).find("input[name='displayKbn']");
		if(0 == $displayKbn.length || "1" == $displayKbn.val()) {
			$(this).find("input[name='depStartDate']").cherryDate({
				holidayObj: $("#dateHolidays").val(),
				beforeShow: function(input){
					var value = $(input).parents("tr").find("input[name='depEndDate']").val();
					return [value, "maxDate"];
					
				}
			});
			$(this).find("input[name='depEndDate']").cherryDate({
				holidayObj: $("#dateHolidays").val(),
				beforeShow: function(input){
					var value = $(input).parents("tr").find("input[name='depStartDate']").val();
					return [value, "minDate"];
				}
			});
		} else {
			$(this).find("input[name='depEndDate']").cherryDate({
				holidayObj: $("#dateHolidays").val(),
				beforeShow: function(input){
					var value = $("#today").val();
					return [value, "minDate"];
				}
			});
		}
	});
}

/*
 * 标准销售价格初期化
 * 
 */
function initSalePrice() {
	$("#salePriceInfo").find("table").each(function() {
		$(this).find(".date").unbind("cherryDate");
		// 显示区分
		var $displayKbn = $(this).find("input[name='displayKbn']");
		if(0 == $displayKbn.length || "1" == $displayKbn.val()) {
			$(this).find("input[name='startDate']").cherryDate({
				holidayObj: $("#dateHolidays").val(),
				beforeShow: function(input){
					var value = $(input).parents("table").find("input[name='endDate']").val();
					return [value, "maxDate"];
					
				}
			});
			$(this).find("input[name='endDate']").cherryDate({
				holidayObj: $("#dateHolidays").val(),
				beforeShow: function(input){
					var value = $(input).parents("table").find("input[name='startDate']").val();
					return [value, "minDate"];
				}
			});
		} else {
			$(this).find("input[name='endDate']").cherryDate({
				holidayObj: $("#dateHolidays").val(),
				beforeShow: function(input){
					var value = $("#today").val();
					return [value, "minDate"];
				}
			});
		}
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
	var $manuFactId = $("#prtVendor").find("table:visible").find("input[name='manuFactId']");
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

/*
 * 删除促销品条码
 * 
 */
function deleteBarCode(dbtn){
	var title = $('#disableTitle').text();
	var text = $('#disableMessage').html();
	var dialogSetting = {
			dialogInit: "#dialogInit",
			text: text,
			width: 	500,
			height: 300,
			title: 	title,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){setDeleteFlag(dbtn);},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
}

function setDeleteFlag(dbtn){
	$(dbtn).parent().hide();
	$(dbtn).parent().find("input[name='deleteFlag']").each(function() {
		$(this).val("0");
	});
	removeDialog("#dialogInit");
}

/*
 * 启用促销品条码
 * 
 */
function startBarCode(obj){
	var title = $('#enableTitle').text();
	var text = $('#enableMessage').html();
	var dialogSetting = {
			dialogInit: "#dialogInit",
			text: text,
			width: 	500,
			height: 300,
			title: 	title,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){setStartFlag(obj);},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
}

function setStartFlag(obj){
	$(obj).hide();
	//$(obj).next().show();
	$(obj).prev().attr("style", "width:109px; height:14px; margin-right:27px;");
	$(obj).parent().find("input[name='validPrmFlag']").each(function() {
		$(this).val("1");
	});
	removeDialog("#dialogInit");
}


//判断促销类别是否是促销礼品
function validateType(value){	
	if(value!=''){
		if(value!='CXLP'){
			 $("#validate_mode").css("display","none");
			 $("#mode").get(0).options[0].selected = true;
		}else{
			$("#validate_mode").css("display","");
			$("#mode").get(0).options[0].selected = true;//第一个值被选中  
		}
	}else{
		 $("#validate_mode").css("display","none");
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
	validateType(value);
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
	// 积分兑礼
	if(value == 'DHCP'){
		$exPoint.prop("disabled",false);
	}else{
		$exPoint.prop("disabled",true);
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
	}else{
		$volume.prop("disabled",false);
		$volumeUnit.prop("disabled",false);
		$weight.prop("disabled",false);
		$weightUnit.prop("disabled",false);
		$styleCode.prop("disabled",false);
		$shelfLife.prop("disabled",false);
		$operationStyle.prop("disabled",false);
		$cost_tip.hide();
		$isStock.find("option:first").prop("selected",true);
	}
}
