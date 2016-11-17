var WPCOM_dialogBody ="";
var BINOLWPSAL03_GLOBAL = function() {

};

BINOLWPSAL03_GLOBAL.prototype = {
	
	// 更改现金值时触发的方法
	"changeCashValue":function(obj){
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		if($thisVal != undefined && $thisVal != ""){
			if(isNaN($thisVal)){
				$this.val("");
			}else if(Number($thisVal) < 0){
				$this.val("");
			}else{
				var realValue = parseFloat(Number($this.val())).toFixed(2);
				if($thisVal.toString() != realValue.toString()){
					$this.val(realValue);
				}
			}
		}
		BINOLWPSAL03.calcuGiveChangeValue();
	},
	
	// 现金值变化时触发的方法
	"keyUpChangeCashValue":function(obj){
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		if($thisVal != undefined && $thisVal != ""){
			if($thisVal.indexOf(".")!=$thisVal.length){
				if(isNaN($thisVal)){
					$this.val("");
				}else if(Number($thisVal) < 0){
					$this.val("");
				}
				BINOLWPSAL03.calcuGiveChangeValue();
			}
		}else{
			BINOLWPSAL03.calcuGiveChangeValue();
		}		
	},
	
	// 更改卡类支付值时触发的方法
	"changeCardValue":function(obj){
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		if($thisVal != undefined && $thisVal != ""){
			if(isNaN($thisVal)){
				$this.val("");
			}else if(Number($thisVal) < 0){
				$this.val("");
			}else{
				var exceedValue = BINOLWPSAL03.calcuExceedValue();
				if(Number(exceedValue) > 0){
					var thisValue = Number($thisVal) - Number(exceedValue);
					if(Number(thisValue) > 0){
						$this.val(thisValue.toFixed(2));
					}else{
						$this.val("");
					}
					$("#cash").val("");
				}else{
					var realValue = parseFloat(Number($this.val())).toFixed(2);
					if($thisVal.toString() != realValue.toString()){
						$this.val(realValue);
					}
				}
			}
		}
		BINOLWPSAL03.calcuGiveChangeValue();
	},
	
	// 卡类支付值变化时触发的方法
	"keyUpChangeCardValue":function(obj){
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		if($thisVal != undefined && $thisVal != ""){
			if($thisVal.indexOf(".")!=$thisVal.length){
				if(isNaN($thisVal)){
					$this.val("");
				}else if(Number($thisVal) < 0){
					$this.val("");
				}else{
					var exceedValue = BINOLWPSAL03.calcuExceedValue();
					if(Number(exceedValue) > 0){
						var thisValue = Number($thisVal) - Number(exceedValue);
						if(Number(thisValue) > 0){
							$this.val(thisValue.toFixed(2));
						}else{
							$this.val("");
						}
						$("#cash").val("");
					}
				}
				BINOLWPSAL03.calcuGiveChangeValue();
			}
		}else{
			BINOLWPSAL03.calcuGiveChangeValue();
		}
	},
	
	// 更改积分支付值时触发的方法
	"changePointValue":function(obj){
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		if($thisVal != undefined && $thisVal != ""){
			if(isNaN($thisVal)){
				$this.val("");
				$("#exchangeCash").val("");
				$("#spanExchangeCash").text("");
			}else if(Number($thisVal) < 0){
				$this.val("");
				$("#exchangeCash").val("");
				$("#spanExchangeCash").text("");
			}else{
				// 计算积分抵扣金额
				BINOLWPSAL03.calcuPointExchangeValue($this.val());
				// 计算多付金额
				var exceedValue = BINOLWPSAL03.calcuExceedValue();
				if(Number(exceedValue) > 0){
					var pointRatio = $("#dgPointRatio").val().toString();
					var thisValue = Number($thisVal) - (Number(exceedValue) * Number(pointRatio));
					if(Number(thisValue) > 0){
						$this.val(thisValue.toFixed(0));
					}else{
						$this.val("");
						$("#exchangeCash").val("");
						$("#spanExchangeCash").text("");
					}
					$("#cash").val("");
				}else{
					var realValue = parseInt(Number($this.val()));
					if($thisVal.toString() != realValue.toString()){
						$this.val(realValue);
					}
				}
				// 重新计算积分抵扣金额
				BINOLWPSAL03.calcuPointExchangeValue($this.val());
			}
		}else{
			$("#exchangeCash").val("");
			$("#spanExchangeCash").text("");
		}
		BINOLWPSAL03.calcuGiveChangeValue();
	},
	
	// 更改积分支付值时触发的方法
	"keyUpChangePointValue":function(obj){
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		if($thisVal != undefined && $thisVal != ""){
			if(isNaN($thisVal)){
				$this.val("");
				$("#exchangeCash").val("");
				$("#spanExchangeCash").text("");
			}else if(Number($thisVal) < 0){
				$this.val("");
				$("#exchangeCash").val("");
				$("#spanExchangeCash").text("");
			}else{
				// 计算积分抵扣金额
				BINOLWPSAL03.calcuPointExchangeValue($this.val());
				// 计算多付金额
				var exceedValue = BINOLWPSAL03.calcuExceedValue();
				if(Number(exceedValue) > 0){
					var pointRatio = $("#dgPointRatio").val().toString();
					var thisValue = Number($thisVal) - (Number(exceedValue) * Number(pointRatio));
					if(Number(thisValue) > 0){
						$this.val(thisValue.toFixed(0));
					}else{
						$this.val("");
						$("#exchangeCash").val("");
						$("#spanExchangeCash").text("");
					}
					$("#cash").val("");
				}else{
					var realValue = parseInt(Number($this.val()));
					if($thisVal.toString() != realValue.toString()){
						$this.val(realValue);
					}
				}
				// 重新计算积分抵扣金额
				BINOLWPSAL03.calcuPointExchangeValue($this.val());
			}
		}else{
			$("#exchangeCash").val("");
			$("#spanExchangeCash").text("");
		}
		BINOLWPSAL03.calcuGiveChangeValue();
	},
	
	// 计算找零金额
	"calcuGiveChangeValue":function(){
		var saleType = $("#saleType").val();
		var receivableValue = $("#receivable").val().toString();
		var cashValue = $("#cash").val().toString();
		
		var resultValue = 0;
		$.each($("#payTbody >tr"),function(){
			var payCodeId = $(this).attr("id");
			if(payCodeId!="serverPay" && payCodeId!="serverList"){
				var payValue = $(this).find("input").val().toString();
				if(payValue != "" && payValue != "-"){
					resultValue+=Number(payValue);
				}
			}
		});
		resultValue-=Number(receivableValue);
		if(resultValue < 0){
			$("#giveChange").val("0.00");
			$("#spanGiveChange").text("0.00");
			if(saleType == "SR"){
				var newCash = Number(cashValue) - (resultValue);
				$("#cash").val(newCash.toFixed(2));
			}
		}else{
			var giveChangeValue = resultValue.toFixed(2);
			$("#giveChange").val(giveChangeValue);
			$("#spanGiveChange").text(giveChangeValue);
		}
		
		/*var cashValue = $("#cash").val().toString();
		var creditCardValue = $("#creditCard").val().toString();
		var bankCardValue = $("#bankCard").val().toString();
		var cashCardValue = $("#cashCard").val().toString();
		var aliPayValue = $("#aliPay").val().toString();
		var wechatPayValue = $("#wechatPay").val().toString();
		var exchangeCashValue = $("#exchangeCash").val().toString();
		// 处理空数据
		if(receivableValue == undefined || receivableValue == "" || receivableValue == "-" || Number(receivableValue) < 0){
			receivableValue = "0.00";
		}
		if(cashValue == undefined || cashValue == "" || cashValue == "-"){
			cashValue = "0.00";
		}
		if(creditCardValue == undefined || creditCardValue == "" || creditCardValue == "-"){
			creditCardValue = "0.00";
		}
		if(bankCardValue == undefined || bankCardValue == "" || bankCardValue == "-"){
			bankCardValue = "0.00";
		}
		if(cashCardValue == undefined || cashCardValue == "" || cashCardValue == "-"){
			cashCardValue = "0.00";
		}
		if(aliPayValue == undefined || aliPayValue == "" || aliPayValue == "-"){
			aliPayValue = "0.00";
		}
		if(wechatPayValue == undefined || wechatPayValue == "" || wechatPayValue == "-"){
			wechatPayValue = "0.00";
		}
		if(exchangeCashValue == undefined || exchangeCashValue == "" || exchangeCashValue == "-"){
			exchangeCashValue = "0.00";
		}
		
		var resultValue = (Number(cashValue) + Number(creditCardValue) + Number(bankCardValue) + Number(cashCardValue) + Number(aliPayValue) + Number(wechatPayValue) + Number(exchangeCashValue)) - (Number(receivableValue));
		if(resultValue < 0){
			$("#giveChange").val("0.00");
			$("#spanGiveChange").text("0.00");
			if(saleType == "SR"){
				var newCash = Number(cashValue) - (resultValue);
				$("#cash").val(newCash.toFixed(2));
			}
		}else{
			var giveChangeValue = resultValue.toFixed(2);
			$("#giveChange").val(giveChangeValue);
			$("#spanGiveChange").text(giveChangeValue);
		}*/
	},
	
	// 计算除现金外其它支付方式的收款金额与应收金额的差额
	"calcuExceedValue":function(){
		
		var receivableValue = $("#receivable").val().toString();
		if(receivableValue == undefined || receivableValue == "" || receivableValue == "-" || Number(receivableValue) < 0){
			receivableValue = "0.00";
		}
		var resultValue = 0;
		$.each($("#payTbody >tr"),function(){
			var payCodeId = $(this).attr("id");
			if(payCodeId!="serverPay" && payCodeId!="serverList" && payCodeId!="trCash"){
				var payValue = $(this).find("input").val().toString();
				if(payValue != "" && payValue != "-"){
					resultValue+=Number(payValue);
				}
			}
		});
		resultValue-=Number(receivableValue);
		return resultValue.toFixed(2);
		
		/*var receivableValue = $("#receivable").val().toString();
		var creditCardValue = $("#creditCard").val().toString();
		var bankCardValue = $("#bankCard").val().toString();
		var cashCardValue = $("#cashCard").val().toString();
		var aliPayValue = $("#aliPay").val().toString();
		var wechatPayValue = $("#wechatPay").val().toString();
		var exchangeCashValue = $("#exchangeCash").val().toString();
		// 处理空数据
		if(receivableValue == undefined || receivableValue == "" || receivableValue == "-" || Number(receivableValue) < 0){
			receivableValue = "0.00";
		}
		if(creditCardValue == undefined || creditCardValue == "" || creditCardValue == "-"){
			creditCardValue = "0.00";
		}
		if(bankCardValue == undefined || bankCardValue == "" || bankCardValue == "-"){
			bankCardValue = "0.00";
		}
		if(cashCardValue == undefined || cashCardValue == "" || cashCardValue == "-"){
			cashCardValue = "0.00";
		}
		if(aliPayValue == undefined || aliPayValue == "" || aliPayValue == "-"){
			aliPayValue = "0.00";
		}
		if(wechatPayValue == undefined || wechatPayValue == "" || wechatPayValue == "-"){
			wechatPayValue = "0.00";
		}
		if(exchangeCashValue == undefined || exchangeCashValue == "" || exchangeCashValue == "-"){
			exchangeCashValue = "0.00";
		}
		
		var resultValue = (Number(creditCardValue) + Number(bankCardValue) + Number(cashCardValue) + Number(aliPayValue) + Number(wechatPayValue) + Number(exchangeCashValue)) - (Number(receivableValue));
		return resultValue.toFixed(2);*/
	},
	
	// 计算收款金额与应收金额的差额
	"calcuRealPayValue":function(){
		var receivableValue = $("#receivable").val().toString();
		if(receivableValue == undefined || receivableValue == "" || receivableValue == "-" || Number(receivableValue) < 0){
			receivableValue = "0.00";
		}
		var resultValue = 0;
		$.each($("#payTbody >tr"),function(){
			var payCodeId = $(this).attr("id");
			if(payCodeId!="serverPay" && payCodeId!="serverList"){
				var payValue = $(this).find("input").val().toString();
				if(payValue != "" && payValue != "-"){
					resultValue+=Number(payValue);
				}
			}
		});
		resultValue-=Number(receivableValue);
		return resultValue.toFixed(2);
		/*
		var cashValue = $("#cash").val().toString();
		var creditCardValue = $("#creditCard").val().toString();
		var bankCardValue = $("#bankCard").val().toString();
		var cashCardValue = $("#cashCard").val().toString();
		var aliPayValue = $("#aliPay").val().toString();
		var wechatPayValue = $("#wechatPay").val().toString();
		var exchangeCashValue = $("#exchangeCash").val().toString();
		// 处理空数据
		if(receivableValue == undefined || receivableValue == "" || receivableValue == "-" || Number(receivableValue) < 0){
			receivableValue = "0.00";
		}
		if(cashValue == undefined || cashValue == "" || cashValue == "-"){
			cashValue = "0.00";
		}
		if(creditCardValue == undefined || creditCardValue == "" || creditCardValue == "-"){
			creditCardValue = "0.00";
		}
		if(bankCardValue == undefined || bankCardValue == "" || bankCardValue == "-"){
			bankCardValue = "0.00";
		}
		if(cashCardValue == undefined || cashCardValue == "" || cashCardValue == "-"){
			cashCardValue = "0.00";
		}
		if(aliPayValue == undefined || aliPayValue == "" || aliPayValue == "-"){
			aliPayValue = "0.00";
		}
		if(wechatPayValue == undefined || wechatPayValue == "" || wechatPayValue == "-"){
			wechatPayValue = "0.00";
		}
		if(exchangeCashValue == undefined || exchangeCashValue == "" || exchangeCashValue == "-"){
			exchangeCashValue = "0.00";
		}
		
		var resultValue = (Number(cashValue) + Number(creditCardValue) + Number(bankCardValue) + Number(cashCardValue) + Number(aliPayValue) + Number(wechatPayValue) + Number(exchangeCashValue)) - (Number(receivableValue));
		return resultValue.toFixed(2);*/
	},
	
	// 计算积分抵扣金额
	"calcuPointExchangeValue":function(value){
		var pointRatio = $("#dgPointRatio").val().toString();
		if(Number(pointRatio) > 0){
			if(Number(value) > 0){
				var exchangeCash = Number(value) / Number(pointRatio);
				$("#exchangeCash").val(exchangeCash.toFixed(2));
				$("#spanExchangeCash").text(exchangeCash.toFixed(2));
			}else{
				$("#exchangeCash").val("");
				$("#spanExchangeCash").text("");
			}
		}else{
			$("#exchangeCash").val("");
			$("#spanExchangeCash").text("");
		}
	},
	
	"returnsBillCollect":function (){
		var $form = $('#billInfoForm');
		var billCode = $("#dgCheckedBillCode").val();
		var returnBillUrl = $('#dgReturnBillUrl').attr("href");
		var businessReturnDate=$("#businessReturnDate").val();
		if(businessReturnDate == null || businessReturnDate == 'null' || businessReturnDate == undefined){
			businessReturnDate="";
		}
		var billParams = "billCode=" + billCode + "&" + $form.serialize() + "&" + $("#collectForm").serialize() +"&returnbussinessDate="+businessReturnDate;
		if($("#serviceTb tr").length>0){
			var serviceList = new Array();
			$.each($("#serviceTb tr"),function(){
				if($(this).find("[name='serviceQuantity']").val()!="" && $(this).find("[name='serviceQuantity']").val()!=undefined){
					var serviceType = $(this).attr("id");
					var serviceQuantity = $(this).find("[name='serviceQuantity']").val();
					if(serviceQuantity!="" && Number(serviceQuantity)>0){
						var obj = {"ServiceType":parseInt(serviceType),"Quantity":parseInt(serviceQuantity)};
						serviceList.push(obj);
					}
				}
			});
			if(serviceList!=""){
				serviceList = JSON.stringify(serviceList);
				serviceJsonList=serviceList;
			}
			billParams+="&serviceJsonList="+serviceList
		}
		if($("#payTbody >tr").not("#serverPay,#serverList").length>0){
			var paymentList = new Array();
			$.each($("#payTbody >tr").not("#serverPay,#serverList"),function(){
				var pi = $(this).find("input");
				var pv = pi.val();
				if(pv!=null && pv!=""){
					var obj = {"storePayCode":pi.attr("name"),"storePayValue":pi.attr("title"),"storePayAmount":pi.val()}
					paymentList.push(obj);
				}
			});
			paymentList = JSON.stringify(paymentList);
			billParams+="&paymentJsonList="+paymentList;
		}
		cherryAjaxRequest({
			url:returnBillUrl,
			param:billParams,
			callback: function(data) {
				$("#btnConfirm").removeAttr("disabled");
				BINOLWPSAL03.showReturnResult(data);
			}
		});
	},
	
	"returnsGoodsCollect":function (){
		var $form = $('#billInfoForm');
		var billCode = $("#dgCheckedBillCode").val();
		// 退货请求地址
		var returnsGoodsUrl = $('#dgReturnsGoodsUrl').attr("href");
		//补登退货时间
		var businessReturnDate =$("#businessReturnDate").val();
		if(businessReturnDate == null || businessReturnDate == 'null' || businessReturnDate == undefined){
			businessReturnDate="";
		}
		var params = "billCode=" + billCode + "&" + $form.serialize() + "&" + $("#collectForm").serialize()+"&returnbussinessDate="+businessReturnDate;
		if($("#serviceTb tr").length>0){
			var serviceList = new Array();
			$.each($("#serviceTb tr"),function(){
				if($(this).find("[name='serviceQuantity']").val()!="" && $(this).find("[name='serviceQuantity']").val()!=undefined){
					var serviceType = $(this).attr("id");
					var serviceQuantity = $(this).find("[name='serviceQuantity']").val();
					if(serviceQuantity!=""){
						var obj = {"ServiceType":parseInt(serviceType),"Quantity":parseInt(serviceQuantity)};
						serviceList.push(obj);
					}
				}
			});
			serviceList = JSON.stringify(serviceList);
			params+="&serviceJsonList="+serviceList
		}
		if($("#payTbody >tr").not("#serverPay,#serverList").length>0){
			var paymentList = new Array();
			$.each($("#payTbody >tr").not("#serverPay,#serverList"),function(){
				var pi = $(this).find("input");
				var pv = pi.val();
				if(pv!=null && pv!=""){
					var obj = {"storePayCode":pi.attr("name"),"storePayValue":pi.attr("title"),"storePayAmount":pi.val()}
					paymentList.push(obj);
				}
			});
			paymentList = JSON.stringify(paymentList);
			params+="&paymentJsonList="+paymentList;
		}
		cherryAjaxRequest({
			url:returnsGoodsUrl,
			param:params,
			callback:function(data) {
				$("#btnConfirm").removeAttr("disabled");
				BINOLWPSAL03.showReturnResult(data);
			}
		});
	},
	
	"showReturnResult":function(data){
		if(Number(data) > 0){
			var autoPrintBill = $("#autoPrintBill").val();
			// 支付成功的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退货成功", 
				type:"SUCCESS", 
				focusEvent:function(){
					BINOLWPSAL03.refreshTable();
					// 打印小票
					$("#print_param_hide").find("#billId").val(data);
					// 获取配置确定是否需要打印小票
					if(autoPrintBill == "Y"){
						// 打印小票，参数0：销售，1：补打小票，2：退货
						printWebPosSaleBill("2");
					}
				}
			});
		}else if(data == "LC"){
			// 非支付宝和微信支付的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，非支付宝或微信支付方式", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.refreshTable();
				}
			});
		}else if(data == "FA"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，连接支付宝接口失败", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "FC"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，连接微信支付接口失败", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "NR"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，支付接口连接超时", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "FWP"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，等待买家付款不允许退款", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "FTC"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，交易关闭不允许退款", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "FTP"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，等待卖家收款不允许退款", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "FTF"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，交易结束不允许退款", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "RFI"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，退款金额大于支付金额", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "FAIL"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，支付响应失败", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "BN"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，退款单据号为空", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "TN"){
			// 支付金额为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，没有输入通过支付宝或微信支付退款的金额", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "NC"){
			// 单据号为空的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，没有获取到支付宝接口配置", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "NAP"){
			// 没有给柜台配置收款账户信息的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，柜台没有配置支付宝收款账户信息", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "NWP"){
			// 没有给柜台配置收款账户信息的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，柜台没有配置微信支付收款账户信息", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "IFERROR"){
			// 出现未知异常被错误捕获的情况
			BINOLWPSAL02.showMessageDialog({
				message:"支付宝或微信支付退款失败", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "ERROR"){
			// 出现未知异常被错误捕获的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退货失败", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "STE0001"){
			// 没有给柜台配置收款账户信息的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，储值卡号为空", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "STE0012"){
			// 没有给柜台配置收款账户信息的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，交易金额格式不正确", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "STE0013"){
			// 没有给柜台配置收款账户信息的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，服务明细参数中存在不合法的数据", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "STE0015"){
			// 没有给柜台配置收款账户信息的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，没有查询到有效的储值卡信息", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else if(data == "STE0018"){
			// 没有给柜台配置收款账户信息的情况
			BINOLWPSAL02.showMessageDialog({
				message:"退款失败，不允许重复交易", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}else{
			// 未定义的返回结果，一般为支付宝和微信支付第三方接口返回的异常信息
			BINOLWPSAL02.showMessageDialog({
				message:"退货失败", 
				type:"MESSAGE", 
				focusEvent:function(){
					BINOLWPSAL03.webPaymentCancel();
				}
			});
		}
	},
	
	"refreshTable":function(){
		$("#getBillsPageDiv").show();
		$("#getBillDetailPageDiv").hide();
		//刷新表格数据
		if(oTableArr[3] != null)
			oTableArr[3].fnDraw();
	},
	
	"confirm":function(){
		var spanReceivable=Number($("#spanReceivable").html());
//		//校验是否为负金额销售
//		if(spanReceivable < 0){
//			// 显示提示信息
//			BINOLWPSAL02.showMessageDialog({
//				message:"当前应收金额为负数，请核实", 
//				type:"MESSAGE",
//				focusEvent:function(){
//					$("#btnConfirm").focus();
//				}
//			});
//			return false;
//		}
		var realPayValue = BINOLWPSAL03.calcuRealPayValue();
		var NEW_CZK_PAY = $("#NEW_CZK_PAY").val();
		if(realPayValue >= 0){
			
			var aliPay = $("#payTbody >tr input[name='PT']").val();
			var wechatPay = $("#payTbody >tr input[name='WEPAY']").val();
			var cashCard = $("#payTbody >tr input[name='CZK']").val();
			/*var aliPay = $("#aliPay").val();
			var wechatPay = $("#wechatPay").val();
			var cashCard = $("#cashCard").val();*/
			
			var serviceJsonList="";
			if($("#serviceTb tr").length>0){
				var serviceList = new Array();
				$.each($("#serviceTb tr"),function(){
					if($(this).find("[name='serviceQuantity']").val()!="" && $(this).find("[name='serviceQuantity']").val()!=undefined){
						var serviceType = $(this).attr("id");
						var serviceQuantity = $(this).find("[name='serviceQuantity']").val();
						if(serviceQuantity!="" && Number(serviceQuantity)>0){
							var obj = {"ServiceType":parseInt(serviceType),"Quantity":parseInt(serviceQuantity)};
							serviceList.push(obj);
						}
					}
				});
				if(serviceList!=""){
					serviceList = JSON.stringify(serviceList);
					serviceJsonList=serviceList;
				}
			}
			if(NEW_CZK_PAY=="N" && aliPay != null && aliPay != undefined && aliPay != "" 
				&& wechatPay != null && wechatPay != undefined && wechatPay != ""){
				// 显示提示信息
				BINOLWPSAL02.showMessageDialog({
					message:"支付宝支付和微信支付不能同时使用，请选择其中一种进行支付！", 
					type:"MESSAGE",
					focusEvent:function(){
						/*$("#wechatPay").select();*/
						$("#WEPAY").find("input").select();
					}
				});
			}else if(NEW_CZK_PAY=="Y" && 
					((aliPay != null && aliPay != undefined && aliPay != "" && wechatPay != null && wechatPay != undefined && wechatPay != "") ||
						(aliPay != null && aliPay != undefined && aliPay != "" && cashCard != null && cashCard != undefined && cashCard != "") ||
							(wechatPay != null && wechatPay != undefined && wechatPay != "" && cashCard != null && cashCard != undefined && cashCard != ""))){
				// 显示提示信息
				BINOLWPSAL02.showMessageDialog({
					message:"支付宝支付.微信支付.储值卡支付不能同时使用，请选择其中一种进行支付！", 
					type:"MESSAGE", 
					focusEvent:function(){
						/*$("#wechatPay").select();*/
						$("#WEPAY").find("input").select();
					}
				});
			}else{
				$("#btnConfirm").attr("disabled",true);
				$("#btnWebPaymentConfirm").attr("disabled",false);
				var saleType = $("#saleType").val();
				var collectPageType = $("#collectPageType").val();
				if(collectPageType == "NSSR"){
					var payType = "";
					if(saleType == "NS"){
						if(aliPay != null && aliPay != undefined && aliPay != "" && Number(aliPay) > 0){
							payType = "AL";
						}else if(wechatPay != null && wechatPay != undefined && wechatPay != "" && Number(wechatPay) > 0){
							payType = "WT";
						}else if((cashCard != null && cashCard != undefined
								&& cashCard != "" && Number(cashCard) > 0 && NEW_CZK_PAY=="Y") || serviceJsonList!="") {
							payType = "CZK";
						}else{
							payType = "LC";
						}
					}else{
						payType = "LC";
					}
					if(payType == "LC"){
						BINOLWPSAL03.billSubmit();
					}else if(payType == "CZK" && NEW_CZK_PAY=="Y") {
						if($("#verificationType").val()=="2"){
							$("#getVerificationCodeButton").show();
						}else {
							$("#getVerificationCodeButton").hide();
						}
						if($("#verificationType").val()=="1"){
							$("#verificationCode2").show();
							$("#verificationCode2").removeAttr("disabled");
							$("#verificationCode").hide();
							$("#verificationCode").attr("disabled","disabled");
						}else {
							$("#verificationCode").show();
							$("#verificationCode").removeAttr("disabled");
							$("#verificationCode2").hide();
							$("#verificationCode2").attr("disabled","disabled");
						}
						$("#collectPageDiv").hide();
						$("#setPaymentPageDiv").hide();
						$("#webPaymentPageDiv").show();
						$("#fg").hide();
						$("#FWK").show();
						$("#webPayCode").focus();
						if($("#memberCode").val()!=""){
							$("#cardCode").val($("#memberCode").val());
						}
					}else{
						$("#fg").show();
						$("#FWK").hide();
						$("#collectPageDiv").hide();
						$("#setPaymentPageDiv").hide();
						$("#webPaymentPageDiv").show();
						$("#webPayCode").focus();
					}
				}else if(collectPageType == "SRDT"){
					var originalSaleType = $("#originalSaleType").val();
					// 退货
					BINOLWPSAL03.returnsGoodsCollect();
					// 还原主页原有销售状态
					$("#saleType").val(originalSaleType);
					// 可见文本框回车事件解绑
					$("#collectPageDiv").find("input:text:visible").unbind();
					// 关闭弹出窗口
					closeCherryDialog('returnBill_dialog',WPCOM_dialogBody);
					// 清空弹出框内容
					$('#returnBill_dialog').html("");
				}else if(collectPageType == "SRBL"){
					var originalSaleType = $("#originalSaleType").val();
					// 退单
					BINOLWPSAL03.returnsBillCollect();
					// 还原主页原有销售状态
					$("#saleType").val(originalSaleType);
					// 可见文本框回车事件解绑
					$("#collectPageDiv").find("input:text:visible").unbind();
					// 关闭弹出窗口
					closeCherryDialog('returnBill_dialog',WPCOM_dialogBody);
					// 清空弹出框内容
					$('#returnBill_dialog').html("");
				}
			}
		}else{
			// 显示提示信息
			BINOLWPSAL02.showMessageDialog({
				message:"收款金额不足", 
				type:"MESSAGE", 
				focusEvent:function(){
					if($("#isCA").val()=="Y" && $("#defaultPay")!=undefined){
						// 默认输入框获得焦点
						$("#defaultPay").select();
					}else {
						$("#cash").select();
					}
				}
			});
		}
	},
	
	"cancel":function(){
		//清空之前的列表增加逻辑：购物车中实价改为最原始购物车数据
		BINOLWPSAL02.removePromotionList();
		var collectPageType = $("#collectPageType").val();
		if(collectPageType == "NSSR"){
			// 还原按钮样式
			$("#btnCollect").attr("class","btn_top");
//			// 获取最后一行
//			var $lastTr = $('#databody').find("tr:last");
//			// 判断最后一行数据是否为折扣数据或空行
//			if(($lastTr.find("#productVendorIDArr").val() != undefined && $lastTr.find("#productVendorIDArr").val() != "") 
//					&& ($lastTr.find("#activityTypeCode").val() != "ZDZK" && $lastTr.find("#activityTypeCode").val() != "ZDQL")){
//				var showSaleRows = $("#showSaleRows").val();
//				// 判断行数是否大于允许的行数
//				if($('#databody >tr').length < Number(showSaleRows)){
//					// 添加一个空行
//					BINOLWPSAL02.addNewLine();
//				}else{
//					// 最后一个可见的文本框获得焦点
//					$('#databody >tr').find("input:text:visible:last").select();
//				}
//			}else{
//				// 最后一个可见的文本框获得焦点
//				$('#databody >tr').find("input:text:visible:last").select();
//			}
			
			// 添加一个空行
			BINOLWPSAL02.addNewLine();
			// 可见文本框回车事件解绑
			$("#collectPageDiv").find("input:text:visible").unbind();
			// 关闭弹出窗口
			closeCherryDialog('dialogInit',WPCOM_dialogBody);
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
		}else{
			var originalSaleType = $("#originalSaleType").val();
			// 还原主页原有销售状态
			$("#saleType").val(originalSaleType);
			// 可见文本框回车事件解绑
			$("#collectPageDiv").find("input:text:visible").unbind();
			// 关闭弹出窗口
			closeCherryDialog('returnBill_dialog',WPCOM_dialogBody);
			// 清空弹出框内容
			$('#returnBill_dialog').html("");
		}
	},
	
	"billSubmit":function(){
		var printDetailJson = $('#saleDetailList').val();
//		var baName = $("#baCode option:selected").text();
//		var baName = $("#baName").val();
		var baName = "";
		var baChooseModel = $("#baChooseModel").val();
		if(baChooseModel == "2"){
			baName = $("#baCode option:selected").text();
		}else{
			baName = $("#baName").val();
		}
		var spanReceivable=$("#spanReceivable").html();//应收
		var spanGiveChange=parseFloat($("#spanGiveChange").html());//找零
		var spanTotalPoint=parseFloat($("#spanTotalPoint").html());//会员积分
		var txtCounterName=$("#txtCounterName").html();//柜台名称
		var counterPhone=$("#counterPhone").val();//柜台电话
		var counterAddress=$("#counterAddress").val();//柜台地址
		var memberCode = $("#memberCode").val();
		var cardCode = $("#cardCode").val();
		var saleType = $("#saleType").val();
		var autoPrintBill = $("#autoPrintBill").val();
		var counterCode = $("#dgCounterCode").val();
		var dgCollectUrl = $("#dgCollectUrl").attr("href");
		var params = $("#mainForm").serialize() + "&" + $("#collectForm").serialize();
		if(cardCode!="" && cardCode!=undefined && cardCode!=null){
			params+="&cardCode="+cardCode;
			var serviceJsonList="";
			if($("#serviceTb tr").length>0){
				var serviceList = new Array();
				$.each($("#serviceTb tr"),function(){
					if($(this).find("[name='serviceQuantity']").val()!="" && $(this).find("[name='serviceQuantity']").val()!=undefined){
						var serviceType = $(this).attr("id");
						var serviceQuantity = $(this).find("[name='serviceQuantity']").val();
						if(serviceQuantity!=""){
							var obj = {"ServiceType":parseInt(serviceType),"Quantity":parseInt(serviceQuantity)};
							serviceList.push(obj);
						}
					}
				});
				if(serviceList!=""){
					serviceList = JSON.stringify(serviceList);
					serviceJsonList=serviceList;
					params+="&serviceJsonList="+serviceJsonList;
				}
			}
		}
		var paymentJsonList = new Array();
		$.each($("#payTbody >tr"),function(){
			var payCodeId = $(this).attr("id");
			if(payCodeId!="serverPay" && payCodeId!="serverList"){
				var storePayCode = $(this).find("input").attr("name");
				var storePayCodeValue = $(this).find("input").val();
				var storePayCodeName = $(this).find("input").attr("title");
				var obj = {"storePayCode":storePayCode,"storePayCodeValue":storePayCodeValue,"storePayCodeName":storePayCodeName}
				paymentJsonList.push(obj);
			}
		});
		paymentJsonList=JSON.stringify(paymentJsonList);
		params+="&paymentJsonList="+paymentJsonList;
		
		var dgSaleUrl = $("#dgSaleUrl").attr("href");
		var param = "counterCode=" + counterCode;
		
		if(dgSaleUrl != undefined && dgSaleUrl != null && dgSaleUrl != "" 
			&& counterCode != undefined && counterCode != null && counterCode != ""){
			var billCode = $("#billCode").val();
			cherryAjaxRequest({
				url: dgCollectUrl,
				param: params,
				callback: function(data) {
					// 还原按钮样式
					$("#btnConfirm").removeAttr("disabled");
					$("#btnCollect").attr("class","btn_top");
					if(data != "ERROR" && data != "" && data!="SALEDETAILERROR" && data!="WECHATERROR"  && data!="SALEBACODEERROR"){
						// 取得当前销售单ID
						$("#print_param_hide").find("#billId").val(data);
						cherryAjaxRequest({
							url: dgSaleUrl,
							param: param,
							callback: function(data1) {
								// 加载页面
								$("#webpos_main").html(data1);
								// 显示提示信息
								BINOLWPSAL02.showMessageDialog({
									message:"销售单已录入成功", 
									type:"SUCCESS", 
									focusEvent:function(){
										if(baChooseModel == "2"){
											// 最后一行第一个可见的文本框获得焦点
											BINOLWPSAL02.firstInputSelect();
										}else{
											$("#baName").focus();
										}
										
										// 获取配置确定是否需要打印小票
										if(autoPrintBill == "Y"){
											// 打印小票，参数0：销售，1：补打小票，2：退货
											printWebPosSaleBill(saleType == "NS" ? "0" : "2");
										} else if(autoPrintBill == "ZJ") {
											var printBrandType=$("#printBrandType").val();
											if(printBrandType == "JPG" || printBrandType == "NZDM"){
												var detailPrintHtml = $("#salePrintForm").find("#detailPrint");
												var originalAmountPrint = 0.0;
												var discountAmountPrint = 0.0;
												var sumQuantityPrint = 0;
												var sumAmountPrint = 0.0;
												// 整单去零
												var removeZero = 0.0;
												$("#counterNamePrint").html('<font size="2px">***'+txtCounterName+'***</font>');//柜台名称
												$("#employeeNamePrint").html('<font size="2px">营业员：'+baName+'</font>');
												$("#saleDateTimePrint").html('<font size="2px">日期：'+(new Date()).toLocaleDateString()+' '+(new Date()).toLocaleTimeString()+'</font>');
												if(memberCode == undefined || '' == memberCode) {
													$("#memberCodePrint").html('<font size="2px">会员号：非会员</font>');
												} else {
													$("#memberCodePrint").html('<font size="2px">会员号：'+memberCode+'</font>');
												}
												$("#billCodePrint").html('<font size="2px">流水号：'+billCode+'</font>');
												
												// 确认后，购物车被清空
												var json = eval('('+printDetailJson+')');
												for (var one in json){
													// 商品编号
													var unitCode = json[one].unitCode;
													var barCode = json[one].barCode;
													// 商品名称
													var productName = json[one].productNameArr;
													// 获取产品ID用于判断是否为活动主记录
													var recType = json[one].productVendorIDArr;
													var quantity = 0;
													if('HDZD' != recType && "-9999" != recType) {
														quantity = json[one].quantityuArr;
													} else {
														// 活动主记录不显示
//													quantity = json[one].promotionQuantity;
														continue;
													}
													// 原价
													var priceUnitArr = json[one].priceUnitArr;
													// 实价
													var realPriceArr = json[one].realPriceArr;
													// 折扣 ----原价-实价
													var discountRateArr = parseFloat(priceUnitArr) - parseFloat(realPriceArr);
													// 实收
													var payAmount = json[one].payAmount;
													//折扣率（百分比）
													var discountRateArr_percent=json[one].discountRateArr;
													if(printBrandType == "JPG"){
														if(discountRateArr_percent == null || discountRateArr_percent == 'null' || discountRateArr_percent == undefined || discountRateArr_percent =='' || discountRateArr_percent == 'NAN'){
															discountRateArr_percent=100.00;
														}else{
															discountRateArr_percent=parseFloat(discountRateArr_percent);
														}
													}
													// 将明细设置到打印页面
													var html='<tr><td width="15%" colspan="4" height="10" class="left" style="border:solid 0px black"><font size="2px">'+barCode+'</font></td></tr>';
													html +='<tr><td width="15%" colspan="4" height="10" class="left" style="border:solid 0px black"><font size="2px">'+productName+'</font></td></tr>';
													html +='<tr><td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
														+quantity+'</font></td><td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
														+priceUnitArr+'</font></td><td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">';
													if(printBrandType == "JPG"){
														html += discountRateArr_percent.toFixed(2);
													}else{
														html += discountRateArr.toFixed(2);
													}
													html +='</font></td><td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
														+payAmount+'</font></td></tr>';
													detailPrintHtml.append(html);
													sumQuantityPrint = Number(sumQuantityPrint) + Number(quantity);
													sumAmountPrint = parseFloat(sumAmountPrint) + parseFloat(payAmount);
													originalAmountPrint = parseFloat(originalAmountPrint) + parseFloat(priceUnitArr) * parseFloat(quantity);
													discountAmountPrint = parseFloat(discountAmountPrint) + parseFloat(discountRateArr) * parseFloat(quantity);
													
												}
												//服务电话
												$("#counterPhonePrint").html("服务电话："+counterPhone);
												
												// 填充合计内容
												$("#sumQuantityPrint").html('<font size="2px">'+sumQuantityPrint+'</font>');//总数量
												$("#originalAmountPrint").html('<font size="2px">应收：'+originalAmountPrint.toFixed(2)+'</font>');//应收
												$("#discountAmountPrint").html('<font size="2px">折扣：'+discountAmountPrint.toFixed(2)+'</font>');//折扣
												$("#sumAmountPrint").html('<font size="2px">总金额：'+sumAmountPrint.toFixed(2)+'</font>');//总金额
												
												//会员积分
												spanTotalPoint=spanTotalPoint+originalAmountPrint;
												if(memberCode == undefined || '' == memberCode) {
													$("#memberIntegralPrint").parent().remove();
												}else{
													$("#memberIntegralPrint").html('<font size="2px">会员积分：'+spanTotalPoint.toFixed(2)+'</font>');
												}
												
												// 填充整单数据内容
												$("#roundingAmountPrint").html('<font size="2px">'+removeZero.toFixed(2)+'</font>');
												// 支付方式,取支付List做迭代
//											$("#payTypeAmountPrint").html('<font size="2px">'+sumAmountPrint.toFixed(2)+'</font>');
												var printActualPay=0;
												var paymentList = eval('('+paymentJsonList+')');
												for (var one in paymentList){
													var storePayCodeValue=paymentList[one].storePayCodeValue;//付钱的金额数
													var storePayCodeName=paymentList[one].storePayCodeName;//付款方式的中文
													if(storePayCodeValue !='' ){
														printActualPay += parseFloat(storePayCodeValue);
														$("#payChangePrintTr").before('<tr><td width="30%" height="10" class="left" style="border:solid 0px black"><font size="2px">'+storePayCodeName+'：</font></td>'
																+'<td width="48" height="10" class="center" style="border:solid 0px black"><font size="2px">'+parseFloat(storePayCodeValue).toFixed(2)+'</font></td></tr>');
													}
												}
												$("#payAmountPrint").html('<font size="2px">'+printActualPay.toFixed(2)+'</font>');//实付（客户通过支付方式一共给的钱）
												$("#payChangePrint").html('<font size="2px">'+spanGiveChange.toFixed(2)+'</font>');//找零
												//金苹果直接打印模版
												if(printBrandType == "JPG"){
													//去除订单号
													$("#billCodePrint").parent().remove();
													//去除会员积分
													$("#memberIntegralPrint").parent().remove();
													//去除文字提示
													$("#printTable2").remove();
													$("#printTable3 tr:gt(2)").remove();
												}
												
												/**
												 * SET_PRINT_PAGESIZE(intOrient,intPageWidth,intPageHeight,strPageName);
												 *	参数说明：
												 *	intOrient：打印方向及纸张类型
												 *	    1---纵向打印，固定纸张； 
												    2---横向打印，固定纸张；  
												    3---纵向打印，宽度固定，高度按打印内容的高度自适应(见样例18)；
												    0---方向不定，由操作者自行选择或按打印机缺省设置。
												intPageWidth：
												    纸张宽，单位为0.1mm 譬如该参数值为45，则表示4.5mm,计量精度是0.1mm。
												intPageHeight：
												    固定纸张时该参数是纸张高；高度自适应时该参数是纸张底边的空白高，计量单位与纸张宽一样。
												strPageName：
												    纸张类型名， intPageWidth等于零时本参数才有效，具体名称参见操作系统打印服务属性中的格式定义。
												    关键字“CreateCustomPage”会在系统内建立一个名称为“LodopCustomPage”自定义纸张类型。
												 * 
												 * 
												 * 
												 * */
												LODOP=getLodop();  
												LODOP.PRINT_INIT("销售小票");
												// 3:纵向；800：纸张宽度；10：纸张底边的空白高（0.1mm）；
												LODOP.SET_PRINT_PAGESIZE(3,800,10,"");
												// 根据打印机自适应,控制位置基点
												LODOP.SET_PRINT_MODE("POS_BASEON_PAPER",true);
												// 字段大小（文本）
												LODOP.SET_PRINT_STYLE("FontSize",8);
												// 线的粗细（文本）
												LODOP.SET_PRINT_STYLE("Bold",1);
												// 底色
//											LODOP.SET_SHOW_MODE("SKIN_CUSTOM_COLOR",'#FFFFFF');	
												// 直接文本
//											LODOP.ADD_PRINT_TEXT(50,10,260,39,"销售");
												// 页面打印
												/**
												 * ADD_PRINT_HTM(intTop,intLeft,intWidth,intHeight,strHtml)增加超文本项
												 * intTop:距离顶部距离
												 * intLeft：左边距
												 * intWidth：宽度，可用百分比
												 * intHeight：高度，可用百分比
												 * strHtml：页面
												 */
											LODOP.ADD_PRINT_HTM(1,0,"100%","100%",$("#salePrintForm").html());
												// 打印
											LODOP.PRINT();
												// 设计
//											LODOP.PRINT_DESIGN();
												// 预览
//												LODOP.PREVIEW();
											}else if(printBrandType == "AVON"){
												var detailPrintHtml = $("#salePrintForm_AVON").find("#detailPrint_AVON");
												var originalAmountPrint = 0.0;
												var discountAmountPrint = 0.0;
												var sumQuantityPrint = 0;
												var sumAmountPrint = 0.0;
												// 整单去零
												var removeZero = 0.0;
												$("#counterNamePrint_AVON").html('<font size="2px">'+txtCounterName+'</font>');//柜台名称
												$("#employeeNamePrint_AVON").html('<font size="2px">营业员：'+baName+'</font>');
												$("#saleDateTimePrint_AVON").html('<font size="2px">打印日期：'+(new Date()).toLocaleDateString()+' '+(new Date()).toLocaleTimeString()+'</font>');
												if(memberCode == undefined || '' == memberCode) {
													$("#memberCodePrint_AVON").html('<font size="2px">会员号：非会员</font>');
												} else {
													$("#memberCodePrint_AVON").html('<font size="2px">会员号：'+memberCode+'</font>');
												}
												$("#billCodePrint_AVON").html('<font size="2px">流水号：'+billCode+'</font>');
												
												// 确认后，购物车被清空
												var json = eval('('+printDetailJson+')');
												for (var one in json){
													// 商品编号
													var index=parseFloat(one)+1;
													var unitCode = json[one].unitCode;
													var barCode = json[one].barCode;
													// 商品名称
													var productName = json[one].productNameArr;
													// 获取产品ID用于判断是否为活动主记录
													var recType = json[one].productVendorIDArr;
													var quantity = 0;
													if('HDZD' != recType && "-9999" != recType) {
														quantity = json[one].quantityuArr;
													} else {
														// 活动主记录不显示
//													quantity = json[one].promotionQuantity;
														continue;
													}
													// 原价
													var priceUnitArr = json[one].priceUnitArr;
													// 实价
													var realPriceArr = json[one].realPriceArr;
													// 折扣 ----原价-实价
													var discountRateArr = parseFloat(priceUnitArr) - parseFloat(realPriceArr);
													// 实收
													var payAmount = BINOLWPSAL03.toDecimal2(json[one].payAmount);
													//折扣率（百分比）
													var discountRateArr_percent=json[one].discountRateArr;
													// 将明细设置到打印页面
//													var html='<tr><td width="15%" colspan="4" height="10" class="left" style="border:solid 0px black"><font size="2px">'+barCode+'</font></td></tr>';
													var html ='<tr><td width="15%" colspan="4" height="10" class="left" style="border:solid 0px black"><font size="2px">'+index+'.'+productName+'</font></td></tr>';
													html +='<tr><td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
														+quantity+'</font></td><td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
														+priceUnitArr+'</font></td>';
													html +='<td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
														+payAmount+'</font></td></tr>';
													detailPrintHtml.append(html);
													sumQuantityPrint = Number(sumQuantityPrint) + Number(quantity);
													sumAmountPrint = parseFloat(sumAmountPrint) + parseFloat(payAmount);
													originalAmountPrint = parseFloat(originalAmountPrint) + parseFloat(priceUnitArr) * parseFloat(quantity);
													discountAmountPrint = parseFloat(discountAmountPrint) + parseFloat(discountRateArr) * parseFloat(quantity);
													
												}
												//服务电话
												$("#counterPhonePrint_AVON").html("电话："+counterPhone);
												//地址
												$("#counterAddressPrint_AVON").html("地址："+counterAddress);
												// 填充合计内容
												$("#sumQuantityPrint_AVON").html('<font size="2px">'+sumQuantityPrint+'</font>');//总数量
												$("#originalAmountPrint_AVON").html('<font size="2px">'+originalAmountPrint.toFixed(2)+'</font>');//应收
												
												//会员积分
												spanTotalPoint=spanTotalPoint+originalAmountPrint;
												if(memberCode == undefined || '' == memberCode) {
													$("#memberIntegralPrint_AVON").parent().remove();
												}else{
													$("#memberIntegralPrint_AVON").html('<font size="2px">会员积分：'+spanTotalPoint.toFixed(2)+'</font>');
												}
												
												// 填充整单数据内容
												$("#roundingAmountPrint_AVON").html('<font size="2px">'+removeZero.toFixed(2)+'</font>');
												// 支付方式,取支付List做迭代
//											$("#payTypeAmountPrint").html('<font size="2px">'+sumAmountPrint.toFixed(2)+'</font>');
												var printActualPay=0;
												var paymentList = eval('('+paymentJsonList+')');
												for (var one in paymentList){
													var storePayCodeValue=paymentList[one].storePayCodeValue;//付钱的金额数
													var storePayCodeName=paymentList[one].storePayCodeName;//付款方式的中文
													if(storePayCodeValue !='' ){
														printActualPay += parseFloat(storePayCodeValue);
														$("#payChangePrintTr_AVON").before('<tr><td width="30%" height="10" class="left" style="border:solid 0px black"><font size="2px">'+storePayCodeName+'：</font></td>'
																+'<td width="48" height="10" class="center" style="border:solid 0px black"><font size="2px">'+parseFloat(storePayCodeValue).toFixed(2)+'</font></td></tr>');
													}
												}
												$("#payAmountPrint_AVON").html('<font size="2px">'+printActualPay.toFixed(2)+'</font>');//实付（客户通过支付方式一共给的钱）
												$("#payChangePrint_AVON").html('<font size="2px">'+spanGiveChange.toFixed(2)+'</font>');//找零
												/**
												 * SET_PRINT_PAGESIZE(intOrient,intPageWidth,intPageHeight,strPageName);
												 *	参数说明：
												 *	intOrient：打印方向及纸张类型
												 *	    1---纵向打印，固定纸张； 
												    2---横向打印，固定纸张；  
												    3---纵向打印，宽度固定，高度按打印内容的高度自适应(见样例18)；
												    0---方向不定，由操作者自行选择或按打印机缺省设置。
												intPageWidth：
												    纸张宽，单位为0.1mm 譬如该参数值为45，则表示4.5mm,计量精度是0.1mm。
												intPageHeight：
												    固定纸张时该参数是纸张高；高度自适应时该参数是纸张底边的空白高，计量单位与纸张宽一样。
												strPageName：
												    纸张类型名， intPageWidth等于零时本参数才有效，具体名称参见操作系统打印服务属性中的格式定义。
												    关键字“CreateCustomPage”会在系统内建立一个名称为“LodopCustomPage”自定义纸张类型。
												 * 
												 * 
												 * 
												 * */
												LODOP=getLodop();  
												LODOP.PRINT_INIT("销售小票");
												// 3:纵向；800：纸张宽度；10：纸张底边的空白高（0.1mm）；
												LODOP.SET_PRINT_PAGESIZE(3,800,10,"");
												// 根据打印机自适应,控制位置基点
//												LODOP.SET_PRINT_MODE("POS_BASEON_PAPER",true);
												// 字段大小（文本）
												LODOP.SET_PRINT_STYLE("FontSize",8);
												// 线的粗细（文本）
												LODOP.SET_PRINT_STYLE("Bold",1);
												// 底色
//											LODOP.SET_SHOW_MODE("SKIN_CUSTOM_COLOR",'#FFFFFF');	
												// 直接文本
//											LODOP.ADD_PRINT_TEXT(50,10,260,39,"销售");
												// 页面打印
												/**
												 * ADD_PRINT_HTM(intTop,intLeft,intWidth,intHeight,strHtml)增加超文本项
												 * intTop:距离顶部距离
												 * intLeft：左边距
												 * intWidth：宽度，可用百分比
												 * intHeight：高度，可用百分比
												 * strHtml：页面
												 */
											LODOP.ADD_PRINT_HTM(1,0,"100%","100%",$("#salePrintForm_AVON").html());
												// 打印
											LODOP.PRINT();
												// 设计
//											LODOP.PRINT_DESIGN();
												// 预览
//												LODOP.PREVIEW();
											
											}
										}
									}
								});
								
								
	//								$("#messageBoxDiv").show();
	//								$("#messageBoxDiv").animate({left:'600px'},1500,function(){
	//									$("#messageBoxDiv").hide();
	//									$("#messageBoxDiv").animate({left:'0px'});
	//								});
							}
						});
					}else{
						if(data=="SALEDETAILERROR"){
							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"销售数据检查发现异常，请尝试重新选择产品", 
								type:"MESSAGE", 
								focusEvent:function(){
									// 最后一行第一个可见的文本框获得焦点
									BINOLWPSAL02.firstInputSelect();
								}
							});
						}else if(data=="WECHATERROR"){
							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"微信卡券核销失败，请稍后再试", 
								type:"MESSAGE", 
								focusEvent:function(){
									// 最后一行第一个可见的文本框获得焦点
									BINOLWPSAL02.firstInputSelect();
								}
							});
						}else if(data=="SALEBACODEERROR"){
							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"BA号为空，请确认后再试",
								type:"MESSAGE",
								focusEvent:function(){
									// 最后一行第一个可见的文本框获得焦点
									BINOLWPSAL02.firstInputSelect();
								}
							});
						}else {
							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"收款失败", 
								type:"MESSAGE", 
								focusEvent:function(){
									// 最后一行第一个可见的文本框获得焦点
									BINOLWPSAL02.firstInputSelect();
								}
							});
						}
					}
				}
			});
		}else{
			// 显示提示信息
			BINOLWPSAL02.showMessageDialog({
				message:"页面加载失败，请重新尝试", 
				type:"MESSAGE", 
				focusEvent:function(){
					// 最后一行第一个可见的文本框获得焦点
					BINOLWPSAL02.firstInputSelect();
				}
			});
		}
		// 可见文本框回车事件解绑
		$("#collectPageDiv").find("input:text:visible").unbind();
		// 关闭弹出窗口
		closeCherryDialog('dialogInit',WPCOM_dialogBody);
		// 清空弹出框内容
		$('#dialogInit').html("");
	},
	
	"setPayment":function(){
		/*$("#collectPageDiv").hide();
		$("#setPaymentPageDiv").show();`
		$("#webPaymentPageDiv").hide();*/
		
		var setPaymentUrl = $("#dgPaymentInitUrl").attr("href");
		cherryAjaxRequest({
			url: setPaymentUrl,
			param: null,
			callback: function(data) {
				var flag = false;
				if(data != undefined && data != null && data != ""){
					var payment = eval("("+data+")");
					$.each(payment,function(i){
						flag = true;
						if(payment[i].check=="Y"){
							$("#setPaymentPageDiv").find("p").find("input[name='"+payment[i].storePayCode+"']").attr("checked","true");
						}
					});
					if(flag){
						$("#collectPageDiv").hide();
						$("#setPaymentPageDiv").show();
						$("#webPaymentPageDiv").hide();
					}else {
						BINOLWPSAL02.showMessageDialog({
							message:"没有可选的支付方式！", 
							type:"MESSAGE",
							focusEvent:function(){
							}
						});
					}
					/*if(payment.creditCardPayment == "CR"){
						$("#ckCreditCard").attr("checked","true");
					}else{
						$("#ckCreditCard").removeAttr("checked");
					}
					if(payment.bankCardPayment == "BC"){
						$("#ckBankCard").attr("checked","true");
					}else{
						$("#ckBankCard").removeAttr("checked");
					}
					if(payment.cashCardPayment == "CZK"){
						$("#ckCashCard").attr("checked","true");
					}else{
						$("#ckCashCard").removeAttr("checked");
					}
					if(payment.aliPayment == "AL"){
						$("#ckAliPay").attr("checked","true");
					}else{
						$("#ckAliPay").removeAttr("checked");
					}
					if(payment.wechatPayment == "WT"){
						$("#ckWechatPay").attr("checked","true");
					}else{
						$("#ckWechatPay").removeAttr("checked");
					}
					if(payment.pointsPayment == "EX"){
						$("#ckPoints").attr("checked","true");
					}else{
						$("#ckPoints").removeAttr("checked");
					}*/
				}
			}
		});
	},
	
	"checkBoxTextClick":function(obj){
		var $this = $(obj).parent().find(":checkbox");
		if(!!$this.attr("checked")){
			$this.removeAttr("checked");
		}else{
			$this.attr("checked","true");
		}
	},
	
	"setPaymentConfirm":function(){
		var paymentList = new Array();
		$.each($("#setPaymentPageDiv").find("p"),function(i){
			if($(this).find("input").attr("checked")){
				var obj = {"storePayCode":$(this).find("input").val(),"isCheck":"Y"}
				paymentList.push(obj);
			}else {
				var obj = {"storePayCode":$(this).find("input").val(),"isCheck":"N"}
				paymentList.push(obj);
			}
		});
		/*var creditCardPayment = $("#ckCreditCard").val();
		var bankCardPayment = $("#ckBankCard").val();
		var cashCardPayment = $("#ckCashCard").val();
		var aliPayment = $("#ckAliPay").val();
		var wechatPayment = $("#ckWechatPay").val();
		var pointsPayment = $("#ckPoints").val();
		if(!$("#ckCreditCard").attr("checked")){
			creditCardPayment = "";
		}
		if(!$("#ckBankCard").attr("checked")){
			bankCardPayment = "";
		}
		if(!$("#ckCashCard").attr("checked")){
			cashCardPayment = "";
		}
		if(!$("#ckAliPay").attr("checked")){
			aliPayment = "";
		}
		if(!$("#ckWechatPay").attr("checked")){
			wechatPayment = "";
		}*/
//		if(!$("#ckPoints").attr("checked")){
//			pointsPayment = "";
//		}
		paymentList = JSON.stringify(paymentList)
		var setPaymentUrl = $("#dgSetPaymentUrl").attr("href");
		var params = "paymentJsonList="+paymentList;
		/*var params = "creditCardPayment=" + creditCardPayment 
					+ "&bankCardPayment=" + bankCardPayment 
					+ "&cashCardPayment=" + cashCardPayment 
					+ "&aliPayment=" + aliPayment 
					+ "&wechatPayment=" + wechatPayment 
					+ "&pointsPayment=" + pointsPayment;*/
		cherryAjaxRequest({
			url: setPaymentUrl,
			param: params,
			callback: function(data) {
				if(data == "SUCCESS"){
					$.each($("#payTbody >tr").not("#serverPay,#serverList"),function(){
						var obj = $(this);
						var payCodeId = obj.attr("id");
						$.each($("#setPaymentPageDiv").find("p"),function(i){
							if($(this).find("input").attr("checked")){
								var payCode = $(this).find("input").val();
								if(payCode=="CA"){
									$("#trCash").show();
								}
								if(payCode==payCodeId){
									obj.show();
									if(payCode=="CZK"){
										var NEW_CZK_PAY = $("#NEW_CZK_PAY").val();
										if(NEW_CZK_PAY!="N"){
											$("#serverPay").show();
										}
									}
									return false;
								}
							}
						});
						$.each($("#setPaymentPageDiv").find("p"),function(i){
							if(!$(this).find("input").attr("checked")){
								var payCode = $(this).find("input").val();
								if(payCode=="CA"){
									$("#trCash").hide();
									$("#trCash").find("input").val("");
								}
								if(payCode==payCodeId){
									obj.find("input").val("");
									obj.hide();
									if(payCode=="CZK"){
										$("#serverPay").hide();
										var NEW_CZK_PAY = $("#NEW_CZK_PAY").val();
										if(NEW_CZK_PAY!="N"){
											$("#choice1").show();
											$("#choice2").hide();
											$("#serverList").addClass("hide");
											$("#serviceTb").find("input").val("");
										}
									}
									return false;
								}
							}
						});
					});
					var isCA=$("#isCA").val();
					if(!"Y"==isCA){
						$("#trCash").show();
					}
					
					/*if(creditCardPayment == "CR"){
						$("#trCreditCard").show();
					}else{
						$("#trCreditCard").hide();
					}
					if(bankCardPayment == "BC"){
						$("#trBankCard").show();
					}else{
						$("#trBankCard").hide();
					}
					if(cashCardPayment == "CZK"){
						$("#trCashCard").show();
						var NEW_CZK_PAY = $("#NEW_CZK_PAY").val();
						if(NEW_CZK_PAY!="N"){
							$("#serverPay").show();
						}
					}else{
						$("#trCashCard").hide();
						$("#serverPay").hide();
						var NEW_CZK_PAY = $("#NEW_CZK_PAY").val();
						if(NEW_CZK_PAY!="N"){
							$("#choice1").show();
							$("#choice2").hide();
							$("#serverList").addClass("hide");
							$("#serviceTb").find("input").val("");
						}
					}
					if(aliPayment == "AL"){
						$("#trAliPay").show();
					}else{
						$("#trAliPay").hide();
					}
					if(wechatPayment == "WT"){
						$("#trWechatPay").show();
					}else{
						$("#trWechatPay").hide();
					}*/
//					if(pointsPayment == "EX"){
//						$("#trPoint").show();
//					}else{
//						$("#trPoint").hide();
//					}
				}else{
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"支付方式设置失败", 
						type:"MESSAGE", 
						focusEvent:function(){
							$("#cash").focus();
						}
					});
				}
				$("#collectPageDiv").show();
				$("#setPaymentPageDiv").hide();
				$("#webPaymentPageDiv").hide();
			}
		});
	},
	
	"setPaymentCancel":function(){
		$("#collectPageDiv").show();
		$("#setPaymentPageDiv").hide();
		$("#webPaymentPageDiv").hide();
		if($("#isCA").val()=="Y" && $("#defaultPay")!=undefined){
			// 默认输入框获得焦点
			$("#defaultPay").select();
		}else {
			$("#cash").select();
		}
	},
	
	"webPaymentConfirm":function(){
		// 储值卡号
		var cardCode = $("#cardCode").val();
		// 云POS是否支持新储值卡支付
		var NEW_CZK_PAY = $("#NEW_CZK_PAY").val();
		var serviceJsonList="";
		if($("#serviceTb tr").length>0){
			var serviceList = new Array();
			$.each($("#serviceTb tr"),function(){
				if($(this).find("[name='serviceQuantity']").val()!="" && $(this).find("[name='serviceQuantity']").val()!=undefined){
					var serviceType = $(this).attr("id");
					var serviceQuantity = $(this).find("[name='serviceQuantity']").val();
					if(serviceQuantity!=""){
						var obj = {"ServiceType":parseInt(serviceType),"Quantity":parseInt(serviceQuantity)};
						serviceList.push(obj);
					}
				}
			});
			if(serviceList!=""){
				serviceList = JSON.stringify(serviceList);
				serviceJsonList=serviceList;
			}
		}
		
		$("#btnWebPaymentConfirm").attr("disabled",true);
		var aliPay = $("#payTbody >tr input[name='PT']").val();
		var wechatPay = $("#payTbody >tr input[name='WEPAY']").val();
		var cashCard = $("#payTbody >tr input[name='CZK']").val();
		/*var aliPay = $("#aliPay").val();
		var wechatPay = $("#wechatPay").val();
		var cashCard = $("#cashCard").val();*/
		var saleType = $("#saleType").val();
		var payAmount = "";
		var payType = "";
		if(saleType == "NS"){
			if(aliPay != null && aliPay != undefined && aliPay != "" && Number(aliPay) > 0){
				payAmount = aliPay;
				payType = "AL";
			}else if(wechatPay != null && wechatPay != undefined && wechatPay != "" && Number(wechatPay) > 0){
				payAmount = wechatPay;
				payType = "WT";
			}else if((cashCard != null && cashCard != undefined && cashCard != "" && Number(cashCard) > 0 && NEW_CZK_PAY=="Y") || serviceJsonList!="") {
				payAmount = cashCard;
				payType = "CZK";
			}else{
				payType = "LC";
			}
		}else{
			payType = "LC";
		}
		var billCode = $("#billCode").val();
		var counterCode = $("#dgCounterCode").val();
		var baCode = $("#baCode").val();
		var authCode = $("#webPayCode").val();
		if(payType=="CZK" || serviceJsonList!=""){
			authCode = cardCode;
		}
		var webPaymentUrl = $("#dgWebPaymentUrl").attr("href");
		var params = "billCode=" + billCode + "&counterCode=" + counterCode + "&baCode=" + baCode + 
				"&aliPay=" + aliPay + "&wechatPay=" + wechatPay + "&authCode=" + authCode + "&payType=" + payType;
		if(payType=="CZK" && NEW_CZK_PAY=="Y"){
			// 储值卡验证码
			var verificationType = $("#verificationType").val();
			var verificationCode = "";
			if(verificationType=="1"){
				verificationCode = $("#verificationCode2").val();
			}else {
				verificationCode = $("#verificationCode").val();
			}
			params+="&cashCard=" + cashCard + "&verificationCode=" + verificationCode 
				+ "&verificationType=" + verificationType + "&serviceJsonList=" + serviceJsonList ;
		}
		params += "&"+getSerializeToken();
		//先挂单之后再进行提价收款后台操作
		var dgHangBillUrl = $("#dgHangBillUrl").attr("href");
		//支付宝与微信挂单的支付明细数据
		var paymentJsonList = new Array();
		$.each($("#payTbody >tr"),function(){
			var payCodeId = $(this).attr("id");
			var storePayCode = $(this).find("input").attr("name");
			var storePayCodeValue = $(this).find("input").val();
			var storePayCodeName = $(this).find("input").attr("title");
			if(payCodeId!="serverPay" && payCodeId!="serverList"){
				if(storePayCodeValue == null || storePayCodeValue == "" || storePayCodeValue == undefined ){
					return;
				}
				var obj = {"storePayCode":storePayCode,"storePayCodeValue":storePayCodeValue,"storePayCodeName":storePayCodeName}
				paymentJsonList.push(obj);
			}
		});
		paymentJsonList=JSON.stringify(paymentJsonList);
		var params_main = $("#mainForm").serialize();
		params_main += "&payDetailList="+paymentJsonList;
		var billClassify=$("#billClassify").val();
		if(billClassify){
			params_main += "&billClassify="+billClassify;
		}
		var memberLevel=$("#memberLevel").val();
		if(memberLevel){
			params_main += "&memberLevel="+memberLevel;
		}
		
		var giveChange=$("#giveChange").val();
		if(giveChange){
			params_main += "&giveChange="+giveChange;
		}
		
		var comments=$("#comments").val();
		if(comments){
			params_main += "&comments="+comments;
		}
		var counterCode = $("#dgCounterCode").val();
		cherryAjaxRequest({
			url: dgHangBillUrl,
			param: params_main,
			callback: function(data) {
				if(data != "SUCCESS"){
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"网络支付前挂单失败，请稍后再试", 
						type:"MESSAGE", 
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
					return false;
				}else{
					$.ajax({
						url: webPaymentUrl,
						data: params,
						timeout: 5000,
						type:'post',
						success: function(data) {
							$("#btnWebPaymentConfirm").removeAttr("disabled");
							if(data == "SUCCESS"){
								// 支付成功的情况
								BINOLWPSAL02.showMessageDialog({
									message:"支付已完成", 
									type:"SUCCESS", 
									focusEvent:function(){
										$("#verificationCode2").val("");
										$("#verificationCode").val("");
										BINOLWPSAL03.billSubmit();
									}
								});
							}else if(data == "LC"){
								// 非支付宝和微信支付的情况
								BINOLWPSAL03.billSubmit();
							}else if(data == "FA"){
								// 单据号为空的情况
								BINOLWPSAL02.showMessageDialog({
									message:"连接支付宝接口失败", 
									type:"MESSAGE", 
									focusEvent:function(){
										BINOLWPSAL03.webPaymentCancel();
									}
								});
							}else if(data == "FC"){
								// 单据号为空的情况
								BINOLWPSAL02.showMessageDialog({
									message:"连接微信支付接口失败", 
									type:"MESSAGE", 
									focusEvent:function(){
										BINOLWPSAL03.webPaymentCancel();
									}
								});
							}else if(data == "NR"){
								// 单据号为空的情况
								BINOLWPSAL02.showMessageDialog({
									message:"连接超时，请通过查单确认支付结果", 
									type:"MESSAGE", 
									focusEvent:function(){
										// 自动跳转至查单页面
										var dialogSetting = {
											dialogInit: "#payResultDialogInit",
											width: 1000,
											height: 400,
											title: $("#getPayResultDialogTitle").text(),
											closeEvent:function(){
												// 移除弹出窗口
												removeDialog("#payResultDialogInit");
												// 退回收款页面
												BINOLWPSAL03.webPaymentCancel();
											}
										};
										openDialog(dialogSetting);
										
										var billTime = $("#businessDate").val();
										var memberCode = $("#memberCode").val();
										var memberName = $("#memberName").val();
										var payResultUrl = $("#dgPayResultUrl").attr("href");
										var params = "payType=" + payType + "&payBillCode=" + billCode + "&payBillTime=" + billTime + "&payMemberCode=" + memberCode +
										 		"&payMemberName=" + memberName + "&payAmount=" + payAmount + "&payCounterCode=" + counterCode;
										cherryAjaxRequest({
											url: payResultUrl,
											param: params,
											callback: function(data) {
												$("#payResultDialogInit").html(data);
											}
										});
									}
								});
							}else if(data == "PROCESSING"){
								// 单据号为空的情况
								BINOLWPSAL02.showMessageDialog({
									message:"支付处理中，请通过查单确认支付结果", 
									type:"MESSAGE", 
									focusEvent:function(){
										// 自动跳转至查单页面
										var dialogSetting = {
											dialogInit: "#payResultDialogInit",
											width: 1000,
											height: 400,
											title: $("#getPayResultDialogTitle").text(),
											closeEvent:function(){
												var payState = $("#payStateDiv").text();
												if(payState=="ERROR"){
													// 关闭弹出窗口
													closeCherryDialog('payResultDialogInit',WPCOM_dialogBody);
													// 退回收款页面
													BINOLWPSAL03.webPaymentCancel();
												}else if(payState=="SUCCESS"){
													BINOLWPSAL03.billSubmit();
													// 关闭弹出窗口
													closeCherryDialog('payResultDialogInit',WPCOM_dialogBody);
													closeCherryDialog('dialogInit', $('#returnBill_dialog').html());
												}else if(payState=="WAIT"){
													BINOLWPSAL02.showMessageDialog({
														message:"请耐心等待支付结果", 
														type:"MESSAGE"
													});
												}
												/*// 移除弹出窗口
												removeDialog("#payResultDialogInit");
												// 退回收款页面
												BINOLWPSAL03.webPaymentCancel();*/
											}
										};
										openDialog(dialogSetting);
										$("#payResultDialogInit").parents("div").find(".ui-dialog-titlebar-close.ui-corner-all").hide();
										var billTime = $("#businessDate").val();
										var memberCode = $("#memberCode").val();
										var memberName = $("#memberName").val();
										var payResultUrl = $("#dgPayResultUrl").attr("href");
										var params = "payType=" + payType + "&payBillCode=" + billCode + "&payBillTime=" + billTime + "&payMemberCode=" + memberCode +
										 		"&payMemberName=" + memberName + "&payAmount=" + payAmount + "&payCounterCode=" + counterCode;
										cherryAjaxRequest({
											url: payResultUrl,
											param: params,
											callback: function(data) {
												$("#payResultDialogInit").html(data);
											}
										});
									}
								});
							}else if(data == "FAIL"){
								// 单据号为空的情况
								BINOLWPSAL02.showMessageDialog({
									message:"支付响应失败", 
									type:"MESSAGE", 
									focusEvent:function(){
										BINOLWPSAL03.webPaymentCancel();
									}
								});
							}else if(data == "BN"){
								// 单据号为空的情况
								BINOLWPSAL02.showMessageDialog({
									message:"单据号为空", 
									type:"MESSAGE", 
									focusEvent:function(){
										BINOLWPSAL03.webPaymentCancel();
									}
								});
							}else if(data == "AN"){
								// 扫描码为空的情况
								BINOLWPSAL02.showMessageDialog({
									message:"请扫描或输入支付码", 
									type:"MESSAGE", 
									focusEvent:function(){
										$("#webPayCode").focus();
									}
								});
							}else if(data == "TN"){
								// 支付金额为空的情况
								var MSG = "";
								if(NEW_CZK_PAY=="Y"){
									MSG = "请输入需要通过支付宝、微信、储值卡支付的金额";
								}else{
									MSG = "请输入需要通过支付宝或微信支付的金额";
								}
								BINOLWPSAL02.showMessageDialog({
									message:MSG, 
									type:"MESSAGE", 
									focusEvent:function(){
										BINOLWPSAL03.webPaymentCancel();
									}
								});
							}else if(data == "VN" && NEW_CZK_PAY=="Y") {
								// 验证码为空的情况
								BINOLWPSAL02.showMessageDialog({
									message : "请输入验证码或支付码",
									type : "MESSAGE", 
									focusEvent:function(){
										$("#verificationCode").focus();
									}
								});
							}else if(data == "STE0014" && NEW_CZK_PAY=="Y") {
								// 验证码为空的情况
								BINOLWPSAL02.showMessageDialog({
									message : "储值卡不是激活状态！ ",
									type : "MESSAGE", 
									focusEvent:function(){
										$("#verificationCode").focus();
									}
								});
							}else if(data == "STE0015" && NEW_CZK_PAY=="Y") {
								// 验证码为空的情况
								BINOLWPSAL02.showMessageDialog({
									message : "没有查询到有效的储值卡信息！ ",
									type : "MESSAGE", 
									focusEvent:function(){
										$("#verificationCode").focus();
									}
								});
							}else if(data == "STE0016" && NEW_CZK_PAY=="Y") {
								// 验证码为空的情况
								BINOLWPSAL02.showMessageDialog({
									message : "余额不足！ ",
									type : "MESSAGE", 
									focusEvent:function(){
										$("#verificationCode").focus();
									}
								});
							}else if(data == "STE0017" && NEW_CZK_PAY=="Y") {
								// 验证码为空的情况
								BINOLWPSAL02.showMessageDialog({
									message : "身份校验失败！",
									type : "MESSAGE", 
									focusEvent:function(){
										$("#verificationCode").focus();
									}
								});
							}else if(data == "NC"){
								// 没有获取到支付宝配置的情况
								BINOLWPSAL02.showMessageDialog({
									message:"没有获取到支付宝配置信息", 
									type:"MESSAGE", 
									focusEvent:function(){
										BINOLWPSAL03.webPaymentCancel();
									}
								});
							}else if(data == "NAP"){
								// 没有给柜台配置收款账户信息的情况
								BINOLWPSAL02.showMessageDialog({
									message:"柜台没有配置支付宝收款账户信息", 
									type:"MESSAGE", 
									focusEvent:function(){
										BINOLWPSAL03.webPaymentCancel();
									}
								});
							}else if(data == "NWP"){
								// 没有给柜台配置收款账户信息的情况
								BINOLWPSAL02.showMessageDialog({
									message:"柜台没有配置微信支付收款账户信息", 
									type:"MESSAGE", 
									focusEvent:function(){
										BINOLWPSAL03.webPaymentCancel();
									}
								});
							}else if(data == "ERROR"){
								// 出现未知异常被错误捕获的情况
								BINOLWPSAL02.showMessageDialog({
									message:"支付出现异常", 
									type:"MESSAGE", 
									focusEvent:function(){
										BINOLWPSAL03.webPaymentCancel();
									}
								});
							}else{
								// 未定义的返回结果，一般为支付宝和微信支付第三方接口返回的异常信息
								BINOLWPSAL02.showMessageDialog({
									message:"支付失败", 
									type:"MESSAGE", 
									focusEvent:function(){
										BINOLWPSAL03.webPaymentCancel();
									}
								});
							}
						},
						complete : function(XMLHttpRequest,status){
							if(status=='timeout' ){//超时,status还有success,error等值的情况
								if(payType == "AL"|| payType == "WT" || payType == "CZK"){
									// 网络连接不稳定的情况
									BINOLWPSAL02.showMessageDialog({
										message:"网络连接不稳定，请通过查单确认支付结果", 
										type:"MESSAGE", 
										focusEvent:function(){
											// 自动跳转至查单页面
											var dialogSetting = {
													dialogInit: "#payResultDialogInit",
													width: 1000,
													height: 400,
													title: $("#getPayResultDialogTitle").text(),
													closeEvent:function(){
														var payState = $("#payStateDiv").text();
														if(payState=="ERROR"){
															// 关闭弹出窗口
															closeCherryDialog('payResultDialogInit',WPCOM_dialogBody);
															// 退回收款页面
															BINOLWPSAL03.webPaymentCancel();
														}else if(payState=="SUCCESS"){
															BINOLWPSAL03.billSubmit();
															// 关闭弹出窗口
															closeCherryDialog('payResultDialogInit',WPCOM_dialogBody);
															closeCherryDialog('dialogInit', $('#returnBill_dialog').html());
														}else if(payState=="WAIT"){
															BINOLWPSAL02.showMessageDialog({
																message:"请耐心等待支付结果", 
																type:"MESSAGE"
															});
														}
														/*// 移除弹出窗口
													removeDialog("#payResultDialogInit");
													// 退回收款页面
													BINOLWPSAL03.webPaymentCancel();*/
													}
											};
											openDialog(dialogSetting);
											$("#payResultDialogInit").parents("div").find(".ui-dialog-titlebar-close.ui-corner-all").hide();
											var billTime = $("#businessDate").val();
											var memberCode = $("#memberCode").val();
											var memberName = $("#memberName").val();
											var payResultUrl = $("#dgPayResultUrl").attr("href");
											var params = "payType=" + payType + "&payBillCode=" + billCode + "&payBillTime=" + billTime + "&payMemberCode=" + memberCode +
											"&payMemberName=" + memberName + "&payAmount=" + payAmount + "&payCounterCode=" + counterCode;
											cherryAjaxRequest({
												url: payResultUrl,
												param: params,
												callback: function(data) {
													$("#payResultDialogInit").html(data);
//													$("#payResultDialogInit").show();
												}
											});
										}
									});
									
								}
					　　　　}
						}
					});
				}
			}
		});
		
	},
	// 验证方式下拉框
	"verificationType" : function(){
		var verificationType = $("#verificationType").val();
		$("[name='verificationCode']").val("");
		if(verificationType=="2"){
			$("#getVerificationCodeButton").show();
			$("#printVerificationCode").show();
			$("#printPass").hide();
			$("#pringPaymentCode").hide();
		}else if(verificationType=="1"){
			$("#printPass").show();
			$("#getVerificationCodeButton").hide();
			$("#printVerificationCode").hide();
			$("#pringPaymentCode").hide();
		}else {
			$("#pringPaymentCode").show();
			$("#getVerificationCodeButton").hide();
			$("#printVerificationCode").hide();
			$("#printPass").hide();
		}
		if(verificationType=="1"){
			$("#verificationCode2").show();
			$("#verificationCode2").removeAttr("disabled");
			$("#verificationCode").hide();
			$("#verificationCode").attr("disabled","disabled");
		}else{
			$("#verificationCode2").hide();
			$("#verificationCode2").attr("disabled","disabled");
			$("#verificationCode").show();
			$("#verificationCode").removeAttr("disabled");
		}
	},
	// 获取验证码
	"getVerificationCode" : function() {
		// 验证码
		if ($("#cardCode").val() == "") {
			$("#cardCode").parent().addClass("error");
			$("#cardCode").parent().append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
			return false;
		}else {
			$("#cardCode").parent().removeClass("error");
			$("#cardCode").parent().find("#errorText").remove();
		}
		var cardCode = $('#cardCode').val();
		// 请求地址
		var getVerificationCodeUrl = $('#dgGetVerificationCode').attr("href");
		cherryAjaxRequest({
			url : getVerificationCodeUrl,
			param : "cardCode="+cardCode,
			callback : function(data){
				if(data!="0"){
					if(data=="SVE0010"){
						BINOLWPSAL02.showMessageDialog({
							message:"没有查询到有效的储值卡信息！", 
							type:"MESSAGE"
						});
					}else if("SVE0005"==data){
						BINOLWPSAL02.showMessageDialog({
							message:"验证码用途不正确！", 
							type:"MESSAGE"
						});
					}else if("SVE0006"==data){
						BINOLWPSAL02.showMessageDialog({
							message:"验证码短信发送失败", 
							type:"MESSAGE"
						});
					}else if("SVE0007"==data){
						BINOLWPSAL02.showMessageDialog({
							message:"生成验证码失败", 
							type:"MESSAGE"
						});
					}else{
						BINOLWPSAL02.showMessageDialog({
							message:"未定义错误！ERRORCODE="+data, 
							type:"MESSAGE"
						});
					}
				}
			}
		});
	},
	"R_ServiceQuantity":function(t){
		var serviceQuantity = $(t).val();
		var R_serviceQuantity = $(t).parent().find("#R_serviceQuantity").text();
		if(isNaN(serviceQuantity)){
			$(t).val(R_serviceQuantity);
		}else {
			if(Number(serviceQuantity)>Number(R_serviceQuantity) && Number(R_serviceQuantity)>0){
				$(t).val(R_serviceQuantity);
			}
		}
	},
	
	"webPaymentCancel":function(){
		$("#collectPageDiv").show();
		$("#setPaymentPageDiv").hide();
		$("#webPaymentPageDiv").hide();
		$("#webPayCode").val("");
		if($("#isCA").val()=="Y" && $("#defaultPay")!=undefined){
			// 默认输入框获得焦点
			$("#defaultPay").select();
		}else {
			$("#cash").select();
		}
		$("#btnConfirm").removeAttr("disabled");
	},
	"toDecimal2":function(x){
		var f = parseFloat(x);  
        if (isNaN(f)) {  
            return false;  
        }  
        var f = Math.round(x*100)/100;  
        var s = f.toString();  
        var rs = s.indexOf('.');  
        if (rs < 0) {  
            rs = s.length;  
            s += '.';  
        }  
        while (s.length <= rs + 1) {  
            s += '00';  
        }  
        return s;
	}
};

var BINOLWPSAL03 = new BINOLWPSAL03_GLOBAL();

$(document).ready(function(){
	// 收款页面可见文本框绑定回车事件
	$("#collectPageDiv").find("input:text:visible")
	.bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			BINOLWPSAL03.confirm();
		}
	});
	// 支付扫描输入框绑定回车事件
	$("#webPaymentPageDiv").find("input:text")
	.bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			BINOLWPSAL03.webPaymentConfirm();
		}
	});
	
	var saleType = $("#saleType").val();
	if(saleType == "SR"){
		$("#ui-dialog-title-dialogInit").text($("#collectTitleSR").val());
		$("#spanReceivableLable").text($("#receivableSR").val());
	}else{
		$("#ui-dialog-title-dialogInit").text($("#collectTitleNS").val());
		$("#spanReceivableLable").text($("#receivableNS").val());
	}
	// 现金输入框获得焦点
	if($("#isCA").val()=="Y" && $("#defaultPay")!=undefined){
		// 默认输入框获得焦点
		$("#defaultPay").select();
	}else {
		$("#cash").select();
	}
	var collectPageType = $("#collectPageType").val();
	if(collectPageType == "NSSR"){
		$("#aliPaySrInfo").hide();
		$("#wechatPaySrInfo").hide();
		// 非关联退货禁止使用支付宝和微信支付退款
		if(saleType == "SR"){
			/*$("#aliPay").attr("disabled",true);
			$("#wechatPay").attr("disabled",true);*/
			$("#PT").find("input").attr("disabled",true);
			$("#WEPAY").find("input").attr("disabled",true);
		}
		// 给弹出框窗体全局变量赋值
		WPCOM_dialogBody = $('#dialogInit').html();
	}else{
		$("#aliPaySrInfo").show();
		$("#wechatPaySrInfo").show();
		if(collectPageType == "SRBL"){
			// 退单情况下禁用所有金额输入框，防止修改
			$.each($("#payTbody >tr:visible"),function(){
				var payCodeId = $(this).attr("id");
				if(payCodeId!="serverPay" && payCodeId!="serverList"){
					$(this).find("input").attr("readonly","readonly");
				}
			});
			
			/*$("#cash").attr("readonly","readonly");
			$("#creditCard").attr("readonly","readonly");
			$("#bankCard").attr("readonly","readonly");
			$("#cashCard").attr("readonly","readonly");
			$("#aliPay").attr("readonly","readonly");
			$("#wechatPay").attr("readonly","readonly");
			$("#pointValue").attr("readonly","readonly");*/
		}else{
			// 退货情况下禁用空值输入框，防止修改
			$.each($("#payTbody >tr:visible"),function(){
				var payCodeId = $(this).attr("id");
				if(payCodeId!="serverPay" && payCodeId!="serverList"){
					var payValue = $(this).find("input").val();
					if(payValue == null || payValue == undefined || payValue == ""){
						$(this).find("input").attr("readonly","readonly");
					}
				}
			});
			/*var cash = $("#cash").val();
			if(cash == null || cash == undefined || cash == ""){
				$("#cash").attr("readonly","readonly");
			}
			var creditCard = $("#creditCard").val();
			if(creditCard == null || creditCard == undefined || creditCard == ""){
				$("#creditCard").attr("readonly","readonly");
			}
			var bankCard = $("#bankCard").val();
			if(bankCard == null || bankCard == undefined || bankCard == ""){
				$("#bankCard").attr("readonly","readonly");
			}
			var cashCard = $("#cashCard").val();
			if(cashCard == null || cashCard == undefined || cashCard == ""){
				$("#cashCard").attr("readonly","readonly");
			}
			var aliPay = $("#aliPay").val();
			if(aliPay == null || aliPay == undefined || aliPay == ""){
				$("#aliPay").attr("readonly","readonly");
			}
			var wechatPay = $("#wechatPay").val();
			if(wechatPay == null || wechatPay == undefined || wechatPay == ""){
				$("#wechatPay").attr("readonly","readonly");
			}
			var pointValue = $("#pointValue").val();
			if(pointValue == null || pointValue == undefined || pointValue == ""){
				$("#pointValue").attr("readonly","readonly");
			}*/
		}
		// 给弹出框窗体全局变量赋值
		WPCOM_dialogBody = $('#returnBill_dialog').html();
	}
});


