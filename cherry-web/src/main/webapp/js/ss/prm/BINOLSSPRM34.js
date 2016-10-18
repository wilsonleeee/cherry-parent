/*
 * 全局变量定义
 */
var binOLSSPRM34_global = {};

//当前点击对象
binOLSSPRM34_global.thisClickObj = {};
//保存区分(0:不需要保存 1:需要保存)
binOLSSPRM34_global.saveKbn = "0";
//是否刷新父页面
binOLSSPRM34_global.doRefresh = false;

window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
		if (binOLSSPRM34_global.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
	}
};

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	initDetailTable();
} );

/* 
 * 删除选中的行
 * 
 * Inputs:  String:tableId 	表格ID
 * 		
 * 
 */
function delTableRow(tableId) {
	var $tr = $(tableId).find(":checkbox[checked]").parents('tr');
	if ($tr.length == 0) {
		return false;
	}
	$("#errorDiv").hide();
	$tr.each(function(){
		if ($(this).find("td.hide input[name='deliverDetailId']").length == 0) {
			$(this).remove();
		} else {
			$(this).attr("class" , "hide");
			$(this).find("td.hide input[name='validFlag']").val("0");
		}
	});
	// 不选
	$("#allSelect").attr("checked", false);
	// 设置行样式
	setRowClass(tableId);
	// 设置行号
	setRowNum(tableId);
	// 设置保存区分为需要保存状态
	needSave();
}

/* 
 * 添加新行
 * 
 * Inputs:  String:id 			需要添加新行的表格ID
 * 			String:rowId		新行ID
 * 
 */
function addTableRow(tableId, rowId) {
	$("#errorDiv").hide();
	// 行数
	$(tableId).append($(rowId).html());
	// 不选
	$("#allSelect").attr("checked", false);
	// 设置行样式
	setRowClass(tableId);
	// 设置行号
	setRowNum(tableId);
	// 设置保存区分为需要保存状态
	needSave();
}

/* 
 * 设置行号
 * 
 * Inputs:  String:tableId 	需要设置行号的表格ID
 * 
 */
function setRowNum(tableId){
	var $trs = $(tableId).find("tr:visible");
	for (var i=0 ; i<$trs.length; i++) {
		var tr = $trs[i];
		// 行号
		$(tr).find("td.rowNum").text(i+1);
	}
}

/* 
 * 打开产品选择页面
 * 
 * Inputs:  Object:obj 	选中的对象
 * 
 */
function openPromDialog(obj) {
	var parentToken = getParentToken();
	// 显示产品选择页面
	popDataTableOfPrmInfo(
			{thisObj: obj,
			organizationID: $('#deliverDepId').val(), 
			csrftoken: parentToken,
			priceType: "SalePrice",
			checkType: "radio"});
	binOLSSPRM34_global.thisClickObj = obj;
}

/*
* 更改促销品查询checkbox状态
* 
* Inputs:  Object:thisObj 	选中的对象
*/
//function changeChecked (thisObj){
//	// 取得更改checkbox后的状态
//	var checkState = $(thisObj).attr('checked');
//	if (checkState){
//		// 先全部取消check状态
//		$('#prm_dataTableBody .checkbox').attr('checked',false);
//		// 复原单个选中
//		$(thisObj).attr('checked',true);
//	}
//}

/*
 * 输入了发货数量
 * 
 * Inputs:  Object:thisObj 	选中的对象
 * 
 */
function changeAmount(thisObj){
	var count = Math.abs(parseInt($(thisObj).val(),10));
	var $tr = $(thisObj).parents("tr");
	if(isNaN(count)){		
		$(thisObj).val("");
		$tr.find("td.detailAmount").text("0.00");
		return;
	}	
	var money = count*$tr.find("td.hide input[name='price']").val();
	if(isNaN(money)){
		return;
	}
	$(thisObj).val(count);
	$tr.find("td.detailAmount").text(money.toFixed(2));
	needSave();
}

/*
 * 保存明细
 * 
 * Inputs:  String:url 	Ajax请求的地址
 * 
 */
function saveDetail(url, kbn){
	// 验证提交的数据
	if (!checkData()) {
		return false;
	}
	var deliverKbn = kbn || "0";
	var param = $("#mainForm").serialize() + "&deliverKbn=" + deliverKbn +
				"&saveKbn=" + binOLSSPRM34_global.saveKbn;
	var addParam = [];
	$("#detailTableBody").find("tr").each(function(){
		var m = [];
		$(this).find(':input:not(:checkbox)').each(function(){
			if(this.value != '') {
				m.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
			}
		});
		addParam.push('{' + m.toString() + '}');
	});
	param += "&detailInfo=[" + addParam.toString() + "]&" + getParentToken(); 
	var callback = function(msg) {
		binOLSSPRM34_global.doRefresh = true;
		};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

/*
 * 设置保存区分为需要保存状态
 * 
 * 
 * 
 */
function needSave() {
	binOLSSPRM34_global.saveKbn = "1";
}

/*
 * 产品选择页面的确定按钮onclick事件
 * 
 * 
 * 
 */
function selectPromotion() {
	
	// 选中的产品
	var $selPrt = $('#prm_dataTableBody :radio:checked');
	if ($selPrt.length > 0) {
		var selectedValue = $selPrt.val();
		var selectedObj = window.JSON2.parse(selectedValue);
		if (prtInfo.length == 5) {
			var $tr = $(binOLSSPRM34_global.thisClickObj).parents("tr");
			// 促销品厂商编码
			$tr.find("span.unitCode").text(selectedObj.unitCode);
			// 促销品条码
			$tr.find("td.barCode").text(selectedObj.barCode);
			// 促销品名称
			$tr.find("td.nameTotal").text(selectedObj.nameTotal);
			// 促销品产品厂商ID
			$tr.find("td.hide input[name='prtVendorId']").val(selectedObj.promotionProductVendorId);
			// 促销品价格
			$tr.find("td.price").text(selectedObj.standardCost);
			$tr.find("td.hide input[name='price']").val(selectedObj.standardCost);
		}
		//清空已经输入的数量和计算出的金额
		$tr.find("#quantity").val("");
		$tr.find("td.detailAmount").html("0.00");
	}
	$('#promotionDialog').dialog( "destroy" );
}


/*
 * 验证提交的数据
 * 
 * 
 * 
 */
function checkData() {
	var $tr = $("#detailTableBody").find("tr:visible");
	var result = true;
	var errMsg;
	if ($tr.length > 0) {
		$tr.each(function(){
			if ($(this).find("td.hide input[name='prtVendorId']").val() == "") {
				$(this).attr("class", "errTRbgColor");
				result = false;
			}
		});
		if (!result) {
			errMsg = $("#errmsg1").html();
		}
	} else {
		result = false;
		errMsg = $("#errmsg2").html();
		
	}
	if (!result) {
		$("#errorDiv #errorSpan").html(errMsg);
		$("#errorDiv").show();
	}
	return result;
}
	

