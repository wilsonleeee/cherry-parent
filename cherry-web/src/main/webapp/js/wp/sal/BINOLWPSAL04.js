var WPCOM_dialogBody ="";
var BINOLWPSAL04_GLOBAL = function() {

};

BINOLWPSAL04_GLOBAL.prototype = {
	
	// 计算整单折扣后金额
	"calcuBillDiscountAmount":function(){
		var billAmount = $("#dgOriginalAmount").val();
		var billDiscountRate = $("#dgTotalDiscountRate").val();
		if(billAmount == undefined || billAmount == ""){
			billAmount = "0.00";
		}
		if(billDiscountRate == undefined || billDiscountRate == ""){
			billDiscountRate = "100.00";
		}
		// 计算折扣后金额
		var newBillAmount = Number(billAmount) * (Number(billDiscountRate) / 100);
		$("#dgAfterDiscountAmount").val(newBillAmount.toFixed(2));
		$("#dgSpanAfterDiscountAmount").text(newBillAmount.toFixed(2));
	},
	
	// 计算折整单扣金额
	"calcuBillDiscountPrice":function(){
		var billAmount = $("#dgOriginalAmount").val();
		var billDiscountAmount = $("#dgAfterDiscountAmount").val();
		if(billAmount == undefined || billAmount == ""){
			billAmount = "0.00";
		}
		if(billDiscountAmount == undefined || billDiscountAmount == ""){
			billDiscountAmount = "0.00";
		}
		// 计算折扣金额
		var billDiscountPrice = Number(billAmount) - Number(billDiscountAmount);
		// 显示折扣金额
		$("#dgTotalDiscountPrice").val(billDiscountPrice.toFixed(2));
		$("#dgSpanTotalDiscountPrice").text(billDiscountPrice.toFixed(2));
	},
	
	// 计算整单去零后金额
	"calcuReceivableAmount":function(){
		// 舍入方式
		var roundType = $("#roundType").val();
		// 舍入位数
		var roundDigit = $("#roundDigit").val();
		var afterDiscountAmount = $("#dgAfterDiscountAmount").val();
		var receivableAmount = $("#dgAfterDiscountAmount").val();
		if(roundDigit == undefined || roundDigit == ""){
			// 舍入位数为空的情况下舍入至两位小数
			roundDigit = 2;
		}
		if(roundType == "1"){
			// 向下舍入
			receivableAmount = BINOLWPSAL04.decimalFloor(afterDiscountAmount, roundDigit);
		}else if(roundType == "2"){
			// 向上舍入
			receivableAmount = BINOLWPSAL04.decimalCeil(afterDiscountAmount, roundDigit);
		}else if(roundType == "3"){
			// 四舍五入
			receivableAmount = BINOLWPSAL04.decimalRound(afterDiscountAmount, roundDigit);
		}
		var roundingAmount = Number(afterDiscountAmount) - Number(receivableAmount);
		$("#dgReceivableAmount").val(Number(receivableAmount).toFixed(Number(roundDigit)));
		$("#dgRoundingAmount").val(roundingAmount.toFixed(2));
		$("#dgSpanRoundingAmount").text(roundingAmount.toFixed(2));
	},
	
	// 更改整单折扣率时触发的方法
	"changeBillDiscountRate":function(obj){
		var $this = $(obj);
		var $thisVal = $this.val().toString();
		var amount = $("#dgOriginalAmount").val();
		if(isNaN($thisVal)){
			$this.val("");
			$("#dgAfterDiscountAmount").val(amount);
			$("#dgSpanAfterDiscountAmount").text(amount);
		}else if(Number($thisVal) < 0 || Number($thisVal) > 100){
			$this.val("");
			$("#dgAfterDiscountAmount").val(amount);
			$("#dgSpanAfterDiscountAmount").text(amount);
		}else{
			if($thisVal != ""){
				var billDiscountRate = parseFloat(Number($this.val())).toFixed(2);
				if($thisVal.toString() != billDiscountRate.toString()){
					$this.val(billDiscountRate);
				}
			}
			// 计算整单折扣后金额
			BINOLWPSAL04.calcuBillDiscountAmount();
		}
		// 计算整单折扣金额
		BINOLWPSAL04.calcuBillDiscountPrice();
		// 计算整单去零
		BINOLWPSAL04.calcuReceivableAmount();
	},
	
	// 整单折扣率数字变化时触发的方法
	"keyUpChangeBillDiscountRate":function(obj){
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		var amount = $("#dgOriginalAmount").val();
		if($thisVal.indexOf(".")!=$thisVal.length){
			if(isNaN($thisVal)){
				$this.val("");
				$("#dgAfterDiscountAmount").val(amount);
				$("#dgSpanAfterDiscountAmount").text(amount);
			}else if(Number($thisVal) < 0 || Number($thisVal) > 100){
				$this.val("");
				$("#dgAfterDiscountAmount").val(amount);
				$("#dgSpanAfterDiscountAmount").text(amount);
			}else{
				// 计算整单折扣后金额
				BINOLWPSAL04.calcuBillDiscountAmount();
			}
			// 计算整单折扣金额
			BINOLWPSAL04.calcuBillDiscountPrice();
			// 计算整单去零
			BINOLWPSAL04.calcuReceivableAmount();
		}
	},
	
	// 更改调金额数值时触发的方法
	"changeReceivableAmount":function(obj){
		var $this = $(obj);
		var $thisVal = $this.val().toString();
		// 舍入位数
		var roundDigit = $("#roundDigit").val();
		// 折扣后的金额
		var afterDiscountAmount = $("#dgAfterDiscountAmount").val();
		// 折扣后的金额向下取整后的值
		var amountFloor = BINOLWPSAL04.decimalFloor(afterDiscountAmount, 0);
		// 折扣后的金额向上取整后的值
		var amountCeil = BINOLWPSAL04.decimalCeil(afterDiscountAmount, 0);
		
		if(roundDigit == undefined || roundDigit == ""){
			// 舍入位数为空的情况下舍入至两位小数
			roundDigit = 2;
		}
		// 原价
		var dgOriginalAmount = $("#dgOriginalAmount").val();
		if(isNaN($thisVal)){
			BINOLWPSAL04.calcuReceivableAmount();
		}else if(Number($thisVal) <= Number(dgOriginalAmount) && Number($thisVal) >= 0){
			var len = $thisVal.toString().split(".");
			if(len.length>1){
				if(len[1].length>2){
					$("#dgReceivableAmount").val(Number($thisVal).toFixed(2));
				};
			};
			// 折扣率
			var discountRate = Number($thisVal).toFixed(2)/Number(dgOriginalAmount)*100;
			$("#dgTotalDiscountRate").val(discountRate.toFixed(2));
			$("#dgAfterDiscountAmount").val((Number($thisVal)).toFixed(2));
			$("#dgSpanAfterDiscountAmount").text((Number($thisVal)).toFixed(2));
			$("#dgTotalDiscountPrice").val((Number(dgOriginalAmount)-Number($thisVal)).toFixed(2));
			$("#dgSpanTotalDiscountPrice").text((Number(dgOriginalAmount)-Number($thisVal)).toFixed(2));
			$("#dgSpanRoundingAmount").text(Number(0).toFixed(2));
			$("#dgRoundingAmount").val(Number(0).toFixed(2));
		}else{
			if($thisVal != ""){
				if(($thisVal.indexOf(".") + 1) == $thisVal.length){
					var receivableAmount = parseFloat(Number($this.val())).toFixed(roundDigit);
					if($thisVal.toString() != receivableAmount.toString()){
						$this.val(receivableAmount);
					}
				}
			}
			var roundingAmount = Number(afterDiscountAmount) - Number($this.val());
			$("#dgRoundingAmount").val(roundingAmount.toFixed(2));
			$("#dgSpanRoundingAmount").text(roundingAmount.toFixed(2));
		}
	},
	
	// 调金额数字变化时触发的方法
	"keyUpChangeReceivableAmount":function(obj){
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		// 折扣后的金额
		var afterDiscountAmount = $("#dgAfterDiscountAmount").val();
		// 折扣后的金额向上取整后的值
		var amountCeil = BINOLWPSAL04.decimalCeil(afterDiscountAmount, 0);
		// 原价
		var dgOriginalAmount = $("#dgOriginalAmount").val();
		if($thisVal.indexOf(".")!=$thisVal.length){
			if(isNaN($thisVal)){
				BINOLWPSAL04.calcuReceivableAmount();
			}else if(Number($thisVal) < 0 || Number($thisVal) > dgOriginalAmount){
				BINOLWPSAL04.calcuReceivableAmount();
			}else if(Number($thisVal) >= 0 && Number($thisVal) <= dgOriginalAmount){
				var len = $thisVal.toString().split(".");
				if(len.length>1){
					if(len[1].length>2){
						$("#dgReceivableAmount").val(Number($thisVal).toFixed(2));
					};
				};
				// 折扣率 
				var discountRate = Number($thisVal).toFixed(2)/Number(dgOriginalAmount)*100;
				$("#dgTotalDiscountRate").val(discountRate.toFixed(2));
				$("#dgAfterDiscountAmount").val((Number($thisVal)).toFixed(2));
				$("#dgSpanAfterDiscountAmount").text((Number($thisVal)).toFixed(2));
				$("#dgTotalDiscountPrice").val((Number(dgOriginalAmount)-Number($thisVal)).toFixed(2));
				$("#dgSpanTotalDiscountPrice").text((Number(dgOriginalAmount)-Number($thisVal)).toFixed(2));
				$("#dgSpanRoundingAmount").text(Number(0).toFixed(2));
				$("#dgRoundingAmount").val(Number(0).toFixed(2));
			}else{
				var roundingAmount = Number(afterDiscountAmount) - Number($thisVal);
				$("#dgRoundingAmount").val(roundingAmount.toFixed(2));
				$("#dgSpanRoundingAmount").text(roundingAmount.toFixed(2));
			}
		}
	},
	
	/*
	  // 此函数为跨弹出页面操作函数，若引用打折页面作为共通页面时需要注意此函数内的代码需要调整
	*/
	"appendDiscountRow":function(disInfo){
		var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		var rowIndex = parseInt($("#rowCode").val())+1;
		$("#rowCode").val(rowIndex);
		var html = '<tr id="dataRow'+ nextIndex +'" class="red">';
		html += '<td id="indexNo">'+ rowIndex +'</td>';
		html += '<td><span id="spanUnitCode"></span></td>';
		html += '<td><span id="spanBarCode"></span></td>';
		html += '<td><span id="spanProductName">'+ disInfo.productName +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ disInfo.productName +'" /></td>';
		html += '<td><span id="spanPrice"></span></td>';
		html += '<td><span id="spanMemberPrice"></span><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
		if($("#isPlatinumPrice").val()=="Y"){
			html += '<td><span id="spanPlatinumPrice"></span><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
		}
		html += '<td class="center"></td>';
		html += '<td>'+ disInfo.discountRate +'<input id="discountRateArr" name="discountRateArr" type="hidden" value="'+ disInfo.discountRateValue +'" /></td>';
		html += '<td></td>';
		html += '<td id="tdAmount">'+ disInfo.discountAmount +'</td>';
		html += '<td><span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL02.deleteRow(this);return false;"></button></span></td>';
		html += '<td style="display:none">'
		+'<input type="hidden" id="mainCode" name="mainCode" value=""/>'
		+'<input type="hidden" id="counterActCode" name="counterActCode" value=""/>'
		+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value="'+ disInfo.activityTypeCode +'"/>'
		+'<input type="hidden" id="activitySign" name="activitySign" value=""/>'
        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>'
        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/>'
        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ disInfo.discountAmount +'"/>'
        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value=""/>'
        +'<input type="hidden" id="proType" name="proType" value="'+ disInfo.proType +'"/>'
        +'<input type="hidden" id="exPoint" name="exPoint" value=""/>'
        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
		$("#databody").append(html);
		// 改变交替行样式
		BINOLWPSAL02.changeOddEvenColor();
    },
	
    /*
      // 此函数为跨弹出页面操作函数，若引用打折页面作为共通页面时需要注意此函数内的代码需要调整
    */
	"confirm":function(){
		// 还原按钮样式
		$("#btnDiscount").attr("class","btn_top");
		// 删除销售单据中的打折明细行
		BINOLWPSAL02.deleteDiscountRow();
		// 计算总金额
		BINOLWPSAL02.calcuTatol();
		
		var receivableAmount = $("#dgReceivableAmount").val();
		var totalDiscountRate = $("#dgTotalDiscountRate").val();
		var totalDiscountPrice = $('#dgTotalDiscountPrice').val();
		var roundingAmount = $('#dgRoundingAmount').val();
		// 对返回折扣后金额进行处理，防止按回车键时出现提交值不合法的情况
		if(receivableAmount != undefined && receivableAmount != ""){
			var realAmount = parseFloat(Number(receivableAmount)).toFixed(2);
			if(receivableAmount.toString() != realAmount.toString()){
				receivableAmount = realAmount;
			}
		}
		// 对返回折扣率进行处理，防止按回车键时出现提交值不合法的情况
		if(totalDiscountRate != undefined && totalDiscountRate != ""){
			var realDiscountRate = parseFloat(Number(totalDiscountRate)).toFixed(2);
			if(totalDiscountRate.toString() != realDiscountRate.toString()){
				totalDiscountRate = realDiscountRate;
			}
		}
		// 更改销售页面总计金额
		$("#totalAmount").val(receivableAmount);
		$("#spanTotalAmount").text(receivableAmount);
		// 更改销售页面隐藏的折扣率值
		$("#totalDiscountRate").val(totalDiscountRate);
		// 更改销售页面隐藏的折扣率值
		$("#roundingAmount").val(roundingAmount);
		
		// 销售页面追加明细数据行
		if(Number(totalDiscountPrice) != 0){
			var disInfo = {};
			disInfo.productName = $("#discountName").val();
			disInfo.discountRate = totalDiscountRate + "%";
			disInfo.discountRateValue = totalDiscountRate;
			disInfo.discountAmount = -Number(totalDiscountPrice);
			disInfo.activityTypeCode = "ZDZK";
			disInfo.proType = "P";
			BINOLWPSAL04.appendDiscountRow(disInfo);
		}
		if(Number(roundingAmount) != 0){
			var disInfo = {};
			disInfo.productName = $("#roundingName").val();
			disInfo.discountRate = "";
			disInfo.discountRateValue = "";
			disInfo.discountAmount = -Number(roundingAmount);
			disInfo.activityTypeCode = "ZDQL";
			disInfo.proType = "P";
			BINOLWPSAL04.appendDiscountRow(disInfo);
		}
		// 关闭弹出窗口
		closeCherryDialog('dialogInit',WPCOM_dialogBody);
//		// 最后一个可见的文本框获得焦点
//		$('#databody >tr').find("input:text:visible:last").select();
		// 添加一个空行
		BINOLWPSAL02.addNewLine();
		// 清空弹出框内容
		$('#dialogInit').html("");
		// 解除退货和补登按钮禁用
		$("#btnReturnsGoods").removeAttr("disabled");
		$("#btnReturnsGoods").attr("class","btn_top");
		$("#btnAddHistoryBill").removeAttr("disabled");
		$("#btnAddHistoryBill").attr("class","btn_top");
		// 解除清空购物车禁用
		$("#btnEmptyShoppingCart").removeAttr("disabled");
		$("#btnEmptyShoppingCart").attr("class","btn_top");
	},
	
	"cancel":function(){
		// 还原按钮样式
		$("#btnDiscount").attr("class","btn_top");
		
		closeCherryDialog('dialogInit',WPCOM_dialogBody);
//		// 获取最后一行
//		var $lastTr = $('#databody').find("tr:last");
//		// 判断最后一行数据是否为折扣数据或空行
//		if(($lastTr.find("#productVendorIDArr").val() != undefined && $lastTr.find("#productVendorIDArr").val() != "") 
//				&& ($lastTr.find("#activityTypeCode").val() != "ZDZK" && $lastTr.find("#activityTypeCode").val() != "ZDQL")){
//			var showSaleRows = $("#showSaleRows").val();
//			// 判断行数是否大于允许的行数
//			if($('#databody >tr').length < Number(showSaleRows)){
//				// 添加一个空行
//				BINOLWPSAL02.addNewLine();
//			}else{
//				// 最后一个可见的文本框获得焦点
//				$('#databody >tr').find("input:text:visible:last").select();
//			}
//		}else{
//			// 最后一个可见的文本框获得焦点
//			$('#databody >tr').find("input:text:visible:last").select();
//		}
		
		// 添加一个空行
		BINOLWPSAL02.addNewLine();
		// 清空弹出框内容
		$('#dialogInit').html("");
		// 解除退货和补登按钮禁用
		$("#btnReturnsGoods").removeAttr("disabled");
		$("#btnReturnsGoods").attr("class","btn_top");
		$("#btnAddHistoryBill").removeAttr("disabled");
		$("#btnAddHistoryBill").attr("class","btn_top");
	},
	
	// 向下舍入取值
	"decimalFloor":function(num, digit){
		if(!isNaN(num) && !isNaN(digit)){
			if(Number(digit) >= 0){
				var digitNum = Math.pow(10, digit);
				return Math.floor(num*digitNum)/digitNum;
			}else{
				return num;
			}
		}else{
			return num;
		}
	},
	
	// 向上舍入取值
	"decimalCeil":function(num, digit){
		if(!isNaN(num) && !isNaN(digit)){
			if(Number(digit) >= 0){
				var digitNum = Math.pow(10, digit);
				return Math.ceil(num*digitNum)/digitNum;
			}else{
				return num;
			}
		}else{
			return num;
		}
	},
	
	// 四舍五入取值
	"decimalRound":function(num, digit){
		if(!isNaN(num) && !isNaN(digit)){
			if(Number(digit) >= 0){
				var digitNum = Math.pow(10, digit);
				return Math.round(num*digitNum)/digitNum;
			}else{
				return num;
			}
		}else{
			return num;
		}
	}
};

var BINOLWPSAL04 = new BINOLWPSAL04_GLOBAL();

$(document).ready(function(){
	// 折扣页面可见文本框绑定回车事件
	$("#discountPageDiv").find("input:text:visible")
	.bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			BINOLWPSAL04.confirm();
		}
	});
	// 计算整单折扣后金额
	BINOLWPSAL04.calcuBillDiscountAmount();
	// 计算整单折扣金额
	BINOLWPSAL04.calcuBillDiscountPrice();
	// 计算整单去零后金额
	BINOLWPSAL04.calcuReceivableAmount();
	
	$("#dgTotalDiscountRate").select();
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#dialogInit').html();
});


