var binOLWSMNG05_02_global = {};
//是否需要解锁
binOLWSMNG05_02_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if(binOLWSMNG05_02_global.needUnlock){
			if (window.opener) {
				window.opener.unlockParentWindow();
			}
		}
	}
};

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	var outBackFlag=$("#outBackFlag").val();
	if(outBackFlag == "N"){
		$("#btn-icon-inhibit").remove();
	}
} );

var BINOLWSMNG05_02 = function () {};
BINOLWSMNG05_02.prototype = {
	/**
	 * 输入了调拨数量
	 * 
	 * @param thisObj
	 */
	"changeCount":function(thisObj) {
		var $this = $(thisObj);
		var $td_obj = $this.parent();
		var $tr_obj = $td_obj.parent();
		var price = Number($tr_obj.find(":input[name='priceUnitArr']").val());
		var $amount = $td_obj.next();
		var count = $this.val();
		if(isNaN(parseInt(count))){
			count = 0;
			$this.val("");
		} else {
			count = Math.abs(parseInt(count));
			$(thisObj).val(count);
		}
		var amount = (count * price).toFixed(2);
		$amount.text(binOLWSMNG05_02.formateMoney(amount, 2));
		
		binOLWSMNG05_02.calcTotal();
	},
	
	/*
	 * 计算总金额，总数量
	 */
	"calcTotal":function(){
		//计算总金额、总数量
		var rows = $("#databody").children();
		var totalQuantity = 0;
		var totalAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
				var quantity = Number($(this).find("#quantityArr").val());
				var amount = Number($(this).find("[name='priceUnitArr']").val());
				totalQuantity += quantity;
				totalAmount += quantity * amount;
			});
		}
		$("#totalQuantity").html(binOLWSMNG05_02.formateMoney(totalQuantity,0));
		$("#totalAmount").html(binOLWSMNG05_02.formateMoney(totalAmount,2));
	},
	
	/*
	 * 用逗号分割金额、数量
	 */
	"formateMoney":function(money,num){
		money = parseFloat(money);
		money = String(money.toFixed(num));
		var re = /(-?\d+)(\d{3})/;
		while(re.test(money))
			money = money.replace(re,"$1,$2");
		return money;
	},
	
	"back":function(){
		binOLWSMNG05_02_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#productAllocationDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#productAllocationDetailUrl').submit();
	},
	//检验制单人是否为空及是否允许负操作（数量不可以大于库存）
	"checkLGData":function(){
//		var tradeEmployeeID = $("#tradeEmployeeID").val();
//		if(tradeEmployeeID == ""){
//			$("#errorSpan2").html($("#errmsg_EST00039").val());
//			$("#errorDiv2").show();
//			return false;
//		}else{
//			return true;
//		}
		var tradeEmployeeID = $("#tradeEmployeeID").val();
		var nsOperation = $("#nsOperation").val();
		if(nsOperation=='0'){
			var b= true;
			$.each($('#databody >tr'), function(i){
				//输入数量
				var quantity=$(this).find("#dataTd6 >input").val();
				//库存数量
				var stock=$(this).find("#dataTDStock").text();
				if(Number(quantity)>Number(stock) && Number(quantity) !=  Number(0)){
					b = false;
					$("#errorSpan2").html($("#errmsg_EST00042").val());
					$("#errorDiv2").show();
				}
			});
			if(b){
				$("#errorDiv2").hide();
				if(tradeEmployeeID == ""){
					$("#errorSpan2").html($("#errmsg_EST00039").val());
					$("#errorDiv2").show();
					return false;
				}else {
					return true;
				}
			}
		}
		if(nsOperation=='1'){
			if(tradeEmployeeID == ""){
				$("#errorSpan2").html($("#errmsg_EST00039").val());
				$("#errorDiv2").show();
				return false;
			}else {
				return true;
			}
		}
	},
	
	"beforeDoActionFun":function(){
		if(binOLWSMNG05_02.checkLGData()){
			return "doaction";
		}else{
			return "";
		}
	}
};
var binOLWSMNG05_02 = new BINOLWSMNG05_02();