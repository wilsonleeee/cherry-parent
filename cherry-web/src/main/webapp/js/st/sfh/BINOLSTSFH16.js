var BINOLSTSFH16_GLOBAL = function() {
};

/* 是否刷新一览画面 */
var doRefresh = false;

//是否需要解锁
var needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

BINOLSTSFH16_GLOBAL.prototype = {
	/**
	  * 删除掉画面头部的提示信息框
	  * @return
      */
	"clearActionMsg":function(){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
	},
	
	"addNewLine":function(){
		BINOLSTSFH16.clearActionMsg();
		// 设置全选状态
		$('#allSelect').prop("checked",false);
		
		//已有空行不新增一行
		var addNewLineFlag = true;
		$.each($('#databody >tr'), function(i){
			if($(this).find("[name='unitCodeBinding']").is(":visible")){
				addNewLineFlag = false;
				$(this).find("[name='unitCodeBinding']").focus();
				return;
			}
		});
		
		if(addNewLineFlag){
			var nextIndex = parseInt($("#rowNumber").val())+1;
			$("#rowNumber").val(nextIndex);
			var html = '<tr id="dataRow'+nextIndex+'">';
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH16.changechkbox(this);"/></td>';
			html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/><input type="hidden" id="unitCode" name="unitCode"></td>';
			html += '<td><span id="spanBarCode"></span><input type="hidden" id="barCode" name="barCode"></td>';
			html += '<td><span id="spanProductName"></span></td>';
			html += '<td><span id="spanModuleCode"></span></td>';
			html += '<td><span id="spanPrice"></span><input type="hidden" id="pricePay" name="pricePay"></td>';
			html += '<td class="center"><input value="" name="quantityuArr" id="quantityuArr" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="7" onchange="BINOLSTSFH16.changeQuantity(this)" onkeyup="BINOLSTSFH16.changeQuantity(this)"  cssStyle="width:98%"/></td>';
			html += '<td class="center"><input value="" name="discountRateArr" id="discountRateArr" cssClass="text-number" style="width:60px;text-align:right;" size="10" maxlength="7" onchange="BINOLSTSFH16.changeDiscountRate(this)" onkeyup="BINOLSTSFH16.keyUpChangeDiscountRate(this)"  cssStyle="width:98%"/></td>';
			html += '<td class="center"><input value="" name="discountPriceArr" id="discountPriceArr" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="12" onchange="BINOLSTSFH16.changeDiscountPrice(this)" onkeyup="BINOLSTSFH16.keyUpChangeDiscountPrice(this)"  cssStyle="width:98%"/></td>';
			html += '<td class="center tdAmount" style="text-align:right;">0.00</td>';
			html += '<td class="center"><input value="" name="reasonArr" type="text" id="reasonArr" size="25" maxlength="50"  cssStyle="width:98%"/></td>';
			html += '<td style="display:none">'
	        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>'
	        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
	        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/>'
	        +'<input type="hidden" id="payAmount" name="payAmount" value=""/>'
	        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value=""/></td></tr>';
			$("#databody").append(html);
	
			var unitCode = "unitCodeBinding_"+nextIndex;
			BINOLSTSFH16.setProductBinding(unitCode);
	
			$("#unitCodeBinding_"+nextIndex).focus();
			
			BINOLSTSFH16.bindInput();
		}
	},
	
	"setProductBinding":function(id){
		productBinding({elementId:id,showNum:20,targetDetail:true,afterSelectFun:BINOLSTSFH16.pbAfterSelectFun});
	},
	
	/**
	 * 产品下拉输入框选中后执行方法
	 */
	"pbAfterSelectFun":function(info){
		$('#'+info.elementId).val(info.unitCode);
		//验证产品厂商ID是否重复
		var flag = true;
		$.each($('#databody >tr'), function(i){
			if($(this).find("#productVendorIDArr").val() == info.prtVendorId){
				flag = false;
				$("#errorSpan2").html($("#errmsg3").val());
				$("#errorDiv2").show();
				return;
			}
		});
		if(flag){
			$("#errorDiv2").hide();
			
			//设置隐藏值
			$('#'+info.elementId).parent().parent().find("[name='prtVendorId']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='productVendorIDArr']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='priceUnitArr']").val(info.price);
			$('#'+info.elementId).parent().parent().find("#unitCode").val(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#barCode").val(info.barCode);
			$('#'+info.elementId).parent().parent().find("#pricePay").val(info.price);
			
			//设置显示值
			$('#'+info.elementId).parent().parent().find("#spanUnitCode").html(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#spanBarCode").html(info.barCode);
			$('#'+info.elementId).parent().parent().find("#spanProductName").html(info.productName);
			$('#'+info.elementId).parent().parent().find("#spanModuleCode").html(info.moduleCode);
			$('#'+info.elementId).parent().parent().find("#spanPrice").html(BINOLSTSFH16.formateMoney(info.price,2));
			
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
			
			//跳到下一个文本框
			var nxtIdx = $('#'+info.elementId).parent().parent().find("input:text").index($('#'+info.elementId)) + 1;
			var $nextInputText =  $('#'+info.elementId).parent().parent().find("input:text:eq("+nxtIdx+")");

			//选中一个产品后后默认数量1
			var quantity = $('#'+info.elementId).parent().parent().find("#quantityuArr").val();
			if(quantity == ""){
				$('#'+info.elementId).parent().parent().find("#quantityuArr").val("1");
			}
			//计算金额（防止先输入数量后输入产品）
			BINOLSTSFH16.changeQuantity($nextInputText);
			
			$nextInputText.select();
		}
	},
	
	"changeOddEvenColor":function(){
		$("#databody tr:odd").attr("class","even");
		$("#databody tr:even").attr("class","odd");
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
	
	"bindInput":function(){
		var tableName = "mainTable";
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			BINOLSTSFH16.bindInputTR(trParam);
		})
	},
	
	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，备注按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentPageID").val() == "BINOLSTSFH16"){
				var key = e.which;
				if (key == 13) {
					//回车键
					e.preventDefault();
		            
					//当前文本框为下拉框可见且输入有问题如重复，停止跳到下一个
					var nextFlag = true;
					if($(this).attr("name") == "unitCodeBinding" && $(this).is(":visible")){
						nextFlag = false;
					}
					if(nextFlag){
						var nxtIdx = $inp.index(this) + 1;
						var $nextInputText = $(trParam+" input:text:eq("+nxtIdx+")");
						if($nextInputText.length>0){
							//跳到下一个文本框
							$nextInputText.focus();
						}else{
							//跳到下一行第一个文本框，如果没有下一行，新增一行
							if($(trParam).next().length==0){
								BINOLSTSFH16.addNewLine();
							}else{
								$(trParam).next().find("input:text:visible:eq(0)").focus();
							}
						}
					}
				}
			}
		});
	},
	
	// 更改销售数量时触发的方法
	"changeQuantity":function(obj){
		BINOLSTSFH16.clearActionMsg();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var $thisVal =$this.val().toString();
		if($thisVal == ""){
			$thisTd.nextAll(".tdAmount").text("0.00");
			$thisTd.find("#payAmount").val("0.00");
		}else if(isNaN($thisVal)){
			if($thisVal!="-"){
				$this.val("0");
				$thisTd.nextAll(".tdAmount").text("0.00");
				$thisTd.find("#payAmount").val("0.00");
			}
		}else{
			var price = $thisTd.parent().find("#priceUnitArr").val();
			if(price == undefined || price == ""){
				price = "0.00";
			}
			if($thisVal != ""){
				var quantity = parseInt(Number($this.val()));
				if($thisVal.toString() != quantity.toString()){
					$this.val(quantity);
				}
			}
			// 折扣前金额
			price = replaceAllComma(price);
			var noDiscountAmount = Number(price) * Number($this.val());
			$thisTd.parent().find("#noDiscountAmount").val(BINOLSTSFH16.formateMoney(noDiscountAmount,2));
			// 计算折扣金额
			BINOLSTSFH16.calcuDiscountPrice(obj);
			// 计算折扣后金额
			BINOLSTSFH16.calcuDiscountAmount(obj);
		}
		BINOLSTSFH16.calcuTatol();
	},
	
	// 更改折扣率时触发的方法
	"changeDiscountRate":function(obj){
		BINOLSTSFH16.clearActionMsg();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var $thisVal =$this.val().toString();
		if(isNaN($thisVal)){
			$this.val("");
			$thisTd.next().find("#discountPriceArr").val("0.00");
		}else if(Number($thisVal) < 0 || Number($thisVal) > 100){
			$this.val("");
			$thisTd.next().find("#discountPriceArr").val("0.00");
			$('#errorDiv2 #errorSpan2').html($('#errorDiscountRate').val());
			$('#errorDiv2').show();
		}else{
			if($thisVal != ""){
				var discountRate = Number($this.val()).toFixed(2);
				if($thisVal.toString() != discountRate.toString()){
					$this.val(discountRate);
				}
			}
			// 计算折扣金额
			BINOLSTSFH16.calcuDiscountPrice(obj);	
		}
		// 计算折扣后金额
		BINOLSTSFH16.calcuDiscountAmount(obj);
		// 计算总金额
		BINOLSTSFH16.calcuTatol();
	},
	
	// 更改折扣金额时触发的方法
	"changeDiscountPrice":function(obj){
		BINOLSTSFH16.clearActionMsg();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var $thisVal =$this.val().toString();
		var amount = $thisTd.nextAll().find("#noDiscountAmount").val();
		amount = replaceAllComma(amount);
		if(amount == undefined || amount == ""){
			amount = "0.00";
		}
		if(isNaN($thisVal)){
			$this.val("0.00");
			$thisTd.prev().find("#discountRateArr").val("");
		}else if(Number($thisVal) > amount){
			$this.val("0.00");
			$thisTd.prev().find("#discountRateArr").val("");
			$('#errorDiv2 #errorSpan2').html($('#errorDiscountPrice').val());
			$('#errorDiv2').show();
		}else{
			if($thisVal != ""){
				var discountPrice = parseFloat(Number($this.val())).toFixed(2);
				if($thisVal.toString() != discountPrice.toString()){
					$this.val(discountPrice);
				}
			}
			// 计算折扣率
			BINOLSTSFH16.calcuDiscountRate(obj);
		}
		// 计算折扣后金额
		BINOLSTSFH16.calcuDiscountAmount(obj);
		// 计算总金额
		BINOLSTSFH16.calcuTatol();
	},
	
	// 更改整单折扣率时触发的方法
	"changeBillDiscountRate":function(obj){
		BINOLSTSFH16.clearActionMsg();
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		if(isNaN($thisVal)){
			$this.val("");
			$("#totalDiscountPrice").val("0.00");
		}else if(Number($thisVal) < 0 || Number($thisVal) > 100){
			$this.val("");
			$("#totalDiscountPrice").val("0.00");
			$('#errorDiv2 #errorSpan2').html($('#errorDiscountRate').val());
			$('#errorDiv2').show();
		}else{
			if($thisVal != ""){
				var billDiscountRate = parseFloat(Number($this.val())).toFixed(2);
				if($thisVal.toString() != billDiscountRate.toString()){
					$this.val(billDiscountRate);
				}
			}
			BINOLSTSFH16.calcuBillDiscountPrice();
		}
		BINOLSTSFH16.calcuBillDiscountAmount();
		
	},
	
	// 更改整单折扣金额时触发的方法
	"changeBillDiscountPrice":function(obj){
		BINOLSTSFH16.clearActionMsg();
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		var billAmount = $("#billTotalAmount").val();
		billAmount = replaceAllComma(billAmount);
		if(billAmount == undefined || billAmount == ""){
			billAmount = "0.00";
		}
		if(isNaN($thisVal)){
			$this.val("0.00");
			$("#totalDiscountRate").val("");
		}else if(Number($thisVal) > billAmount){
			$this.val("0.00");
			$("#totalDiscountRate").val("");
			$('#errorDiv2 #errorSpan2').html($('#errorDiscountPrice').val());
			$('#errorDiv2').show();
		}else{
			if($thisVal != ""){
				var billDiscountPrice = parseFloat(Number($this.val())).toFixed(2);
				if($thisVal.toString() != billDiscountPrice.toString()){
					$this.val(billDiscountPrice);
				}
			}
			// 计算折扣率
			BINOLSTSFH16.calcuBillDiscountRate();
		}
		BINOLSTSFH16.calcuBillDiscountAmount();
	},
	
	// 折扣率数字变化时触发的方法
	"keyUpChangeDiscountRate":function(obj){
		BINOLSTSFH16.clearActionMsg();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var $thisVal =$this.val().toString();
		if($thisVal.indexOf(".")!=$thisVal.length){
			if(isNaN($thisVal)){
				$this.val("");
				$thisTd.next().find("#discountPriceArr").val("0.00");
			}else if(Number($thisVal) < 0 || Number($thisVal) > 100){
				$this.val("");
				$thisTd.next().find("#discountPriceArr").val("0.00");
				$('#errorDiv2 #errorSpan2').html($('#errorDiscountRate').val());
				$('#errorDiv2').show();
			}
			// 计算折扣金额
			BINOLSTSFH16.calcuDiscountPrice(obj);	
			// 计算折扣后金额
			BINOLSTSFH16.calcuDiscountAmount(obj);
			// 计算总金额
			BINOLSTSFH16.calcuTatol();
		}
	},
	
	// 折扣金额数字变化时触发的方法
	"keyUpChangeDiscountPrice":function(obj){
		BINOLSTSFH16.clearActionMsg();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var $thisVal =$this.val().toString();
		if($thisVal.indexOf(".")!=$thisVal.length){
			var amount = $thisTd.nextAll().find("#noDiscountAmount").val();
			amount = replaceAllComma(amount);
			if(amount == undefined || amount == ""){
				amount = "0.00";
			}
			if(isNaN($thisVal)){
				$this.val("0.00");
				$thisTd.prev().find("#discountRateArr").val("");
			}else if(Number($thisVal) != 0 && Number($thisVal) > amount){
				$this.val("0.00");
				$thisTd.prev().find("#discountRateArr").val("");
				$('#errorDiv2 #errorSpan2').html($('#errorDiscountPrice').val());
				$('#errorDiv2').show();
			}
			// 计算折扣率
			BINOLSTSFH16.calcuDiscountRate(obj);
			// 计算折扣后金额
			BINOLSTSFH16.calcuDiscountAmount(obj);
			// 计算总金额
			BINOLSTSFH16.calcuTatol();
		}
	},
	
	// 整单折扣率数字变化时触发的方法
	"keyUpChangeBillDiscountRate":function(obj){
		BINOLSTSFH16.clearActionMsg();
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		if($thisVal.indexOf(".")!=$thisVal.length){
			if(isNaN($thisVal)){
				$this.val("");
				$("#totalDiscountPrice").val("0.00");
			}else if(Number($thisVal) < 0 || Number($thisVal) > 100){
				$this.val("");
				$("#totalDiscountPrice").val("0.00");
				$('#errorDiv2 #errorSpan2').html($('#errorDiscountRate').val());
				$('#errorDiv2').show();
			}
			BINOLSTSFH16.calcuBillDiscountPrice();
			BINOLSTSFH16.calcuBillDiscountAmount();
		}
	},
	
	// 整单折扣金额数字变化时触发的方法
	"keyUpChangeBillDiscountPrice":function(obj){
		BINOLSTSFH16.clearActionMsg();
		var $this = $(obj);
		var $thisVal =$this.val().toString();
		if($thisVal.indexOf(".")!=$thisVal.length){
			var billAmount = $("#billTotalAmount").val();
			billAmount = replaceAllComma(billAmount);
			if(billAmount == undefined || billAmount == ""){
				billAmount = "0.00";
			}
			if(isNaN($thisVal)){
				$this.val("0.00");
				$("#totalDiscountRate").val("");
			}else if(Number($thisVal) != 0 && Number($thisVal) > billAmount){
				$this.val("0.00");
				$("#totalDiscountRate").val("");
				$('#errorDiv2 #errorSpan2').html($('#errorDiscountPrice').val());
				$('#errorDiv2').show();
			}
			// 计算折扣率
			BINOLSTSFH16.calcuBillDiscountRate();
			BINOLSTSFH16.calcuBillDiscountAmount();
		}
	},
	
	// 计算折扣率
	"calcuDiscountRate":function(obj){
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var amount = $thisTr.find("#noDiscountAmount").val();
		var discountPrice = $thisTr.find("#discountPriceArr").val();
		if(amount == undefined || amount == ""){
			amount = "0.00";
		}
		if(discountPrice == undefined || discountPrice == ""){
			discountPrice = "0.00";
		}
		amount = replaceAllComma(amount);
		discountPrice = replaceAllComma(discountPrice);
		// 计算折扣率
		if(amount != "0" && amount != "0.00"){
			if(discountPrice != "0.00"){
				var discountRate = (Number(amount) - Number(discountPrice)) / Number(amount) * 100;
				// 显示折扣率
				$thisTr.find("#discountRateArr").val(Number(discountRate).toFixed(2));
			}else{
				$thisTr.find("#discountRateArr").val("");
			}
		}else{
			$thisTr.find("#discountRateArr").val("");
		}
	},
	
	// 计算折扣金额
	"calcuDiscountPrice":function(obj){
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var amount = $thisTr.find("#noDiscountAmount").val();
		var discountRate = $thisTr.find("#discountRateArr").val();
		if(amount == undefined || amount == ""){
			amount = "0.00";
		}
		if(discountRate == undefined || discountRate == ""){
			discountRate = "100.00";
		}
		amount = replaceAllComma(amount);
		discountRate = replaceAllComma(discountRate);
		// 计算折扣金额
		var discountPrice = Number(amount) * ((100.00 - Number(discountRate)) / 100);
		// 显示折扣金额
		$thisTr.find("#discountPriceArr").val(discountPrice.toFixed(2));
	},
	
	// 计算折扣后价格
	"calcuDiscountAmount":function(obj){
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var amount = $thisTr.find("#noDiscountAmount").val();
		var discountPrice = $thisTr.find("#discountPriceArr").val();
		if(amount == undefined || amount == ""){
			amount = "0.00";
		}
		if(discountPrice == undefined || discountPrice == ""){
			discountPrice = "0.00";
		}
		amount = replaceAllComma(amount);
		discountPrice = replaceAllComma(discountPrice);
		// 计算折扣后金额
		var newAmount = Number(amount) - Number(discountPrice);
		// 显示折扣后金额
		$thisTr.find(".tdAmount").text(BINOLSTSFH16.formateMoney(newAmount,2));
		$thisTr.find("#payAmount").val(BINOLSTSFH16.formateMoney(newAmount,2));
	},
	
	// 计算整单折扣率
	"calcuBillDiscountRate":function(){
		var billAmount = $("#billTotalAmount").val();
		var billDiscountPrice = $("#totalDiscountPrice").val();
		if(billAmount == undefined || billAmount == ""){
			billAmount = "0.00";
		}
		if(billDiscountPrice == undefined || billDiscountPrice == ""){
			billDiscountPrice = "0.00";
		}
		billAmount = replaceAllComma(billAmount);
		billDiscountPrice = replaceAllComma(billDiscountPrice);
		// 计算折扣率
		if(billAmount != "0" && billAmount != "0.00"){
			if(billDiscountPrice != "0.00"){
				var billDiscountRate = (Number(billAmount) - Number(billDiscountPrice)) / Number(billAmount) * 100;
				// 显示折扣率
				$("#totalDiscountRate").val(Number(billDiscountRate).toFixed(2));
			}else{
				$("#totalDiscountRate").val("");
			}
		}else{
			$("#totalDiscountRate").val("");
		}
	},
	
	// 计算折整单扣金额
	"calcuBillDiscountPrice":function(){
		var billAmount = $("#billTotalAmount").val();
		var billDiscountRate = $("#totalDiscountRate").val();
		if(billAmount == undefined || billAmount == ""){
			billAmount = "0.00";
		}
		if(billDiscountRate == undefined || billDiscountRate == ""){
			billDiscountRate = "100.00";
		}
		billAmount = replaceAllComma(billAmount);
		billDiscountRate = replaceAllComma(billDiscountRate);
		// 计算折扣金额
		var billDiscountPrice = Number(billAmount) * ((100.00 - Number(billDiscountRate)) / 100);
		// 显示折扣金额
		$("#totalDiscountPrice").val(billDiscountPrice.toFixed(2));
	},
	
	// 计算整单折扣后金额
	"calcuBillDiscountAmount":function(){
		var billAmount = $("#billTotalAmount").val();
		var billDiscountPrice = $("#totalDiscountPrice").val();
		if(billAmount == undefined || billAmount == ""){
			billAmount = "0.00";
		}
		if(billDiscountPrice == undefined || billDiscountPrice == ""){
			billDiscountPrice = "0.00";
		}
		billAmount = replaceAllComma(billAmount);
		billDiscountPrice = replaceAllComma(billDiscountPrice);
		// 计算折扣后金额
		var newBillAmount = Number(billAmount) - Number(billDiscountPrice);
		// 显示折扣后金额
		$("#spanTotalAmount").html(BINOLSTSFH16.formateMoney(newBillAmount,2));
		$("#totalAmount").val(BINOLSTSFH16.formateMoney(newBillAmount,2));
	},
	
	/**
	 * 计算总数量，总金额
	 * */
	"calcuTatol":function(){
		var rows = $("#databody").children();
		var totalQuantity = 0;
		var totalAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
				var $tds = $(this).find("td");
				var $inputVal=$tds.eq(6).find(":input").val();
				$inputVal = replaceAllComma($inputVal);
				if(isNaN($inputVal)){
					$inputVal=0;
				}
				totalQuantity += Number($inputVal);
				var $tdVal=$tds.eq(9).text();
				$tdVal = replaceAllComma($tdVal);
				if(isNaN($tdVal)){
					$tdVal=0.00;
				}
				totalAmount += Number($tdVal);
			});
		}
		$("#spanTotalQuantity").html(BINOLSTSFH16.formateMoney(Number(totalQuantity),0));
		$("#totalQuantity").val(BINOLSTSFH16.formateMoney(Number(totalQuantity),0));
		$("#spanTotalAmount").html(BINOLSTSFH16.formateMoney(Number(totalAmount),2));
		$("#totalAmount").val(BINOLSTSFH16.formateMoney(Number(totalAmount),2));
		$("#billTotalAmount").val(BINOLSTSFH16.formateMoney(Number(totalAmount),2));
		// 计算整单折金额
		BINOLSTSFH16.calcuBillDiscountPrice();
		// 计算整单折后金额
		BINOLSTSFH16.calcuBillDiscountAmount();
	},
	
	"changechkbox":function(obj){
		var chked = $(obj).prop("checked");
		if(!chked){
			$('#allSelect').prop("checked",false);
			return false;
		}
		var flag = true;
		$("#databody :checkbox").each(function(i){
			if(i>=0){
					if($(this).prop("checked")!=true){
						flag=false;
					}	
				}
			});
		if(flag){
			$('#allSelect').prop("checked",true);
		}
	},
	
	/**
	 * 删除选中行
	 */
	"deleteRow":function(){
		if($("#databody #chkbox:checked").length == 0){
			return;
		}
		BINOLSTSFH16.clearActionMsg();
		$("#databody :checkbox").each(function(){
			var $this = $(this);
			if($this.prop("checked")){			
				$this.parent().parent().remove();
			}					
		});
		BINOLSTSFH16.changeOddEvenColor();
		$('#allSelect').prop("checked",false);
		$("input[type=checkbox]").prop("checked",false);
		if($('#databody >tr').length <= 0){
			BINOLSTSFH16.addNewLine();
		}
		// 重新计算总金额
		BINOLSTSFH16.calcuTatol();
	},
	
	"save":function(){
		if(!$('#mainForm').valid()) {
			return false;
		}
		if(!BINOLSTSFH16.checkData()){
			return false;
		}
		BINOLSTSFH16.getSaleDetailList();
		var url = $("#saveUrl").attr("href");
		var params = $("#mainForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: params,
			callback:function(msg){
				if(window.opener.oTableArr[1] != null)
					window.opener.oTableArr[1].fnDraw();
			}
		});
	},
	
	"confirm":function(){
		if(!$('#mainForm').valid()) {
			return false;
		}
		if(!BINOLSTSFH16.checkData()){
			return false;
		}
		BINOLSTSFH16.getSaleDetailList();
		var url = $("#submitUrl").attr("href");
		var params = $("#mainForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: params,
			callback:function(msg){
				if(window.opener.oTableArr[1] != null)
					window.opener.oTableArr[1].fnDraw();
			}
		});
	},
	
	"modifyOrder":function(){
		$("#divBillType").removeClass("hide");
		$("#divContactPerson").removeClass("hide");
		$("#divDeliverAddress").removeClass("hide");
		$("#divSettlement").removeClass("hide");
		$("#divCurrency").removeClass("hide");
		$("#divComments").removeClass("hide");
		$("#divTotalDiscountRate").removeClass("hide");
		$("#divTotalDiscountPrice").removeClass("hide");
		$("#showAddBtn").removeClass("hide");
		$("#showDelBtn").removeClass("hide");
		
		$("#spanBillType").addClass("hide");
		$("#spanContactPerson").addClass("hide");
		$("#spanDeliverAddress").addClass("hide");
		$("#spanSettlement").addClass("hide");
		$("#spanCurrency").addClass("hide");
		$("#spanComments").addClass("hide");
		$("#spanTotalDiscountRate").addClass("hide");
		$("#spanTotalDiscountPrice").addClass("hide");

		$.each($('#databody').find('tr'), function(n){
			$(this).find("#quantityuArr").show();
			$(this).find("#discountRateArr").show();
			$(this).find("#discountPriceArr").show();
			$(this).find("#reasonArr").show();
			
			$(this).find("#spanQuantity").addClass("hide");
			$(this).find("#spanDiscountRate").addClass("hide");
			$(this).find("#spanDiscountAmount").addClass("hide");
			$(this).find("#spanComment").addClass("hide");
		});
		$("#btnSave").show();
		$("#btnBack").css("display","");
		$("#btnEdit").css("display","none");
	},
	
	"back":function(){
		needUnlock = false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#saleOrdersDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#saleOrdersDetailUrl').submit();
	},
	
	"delete":function(){
		this.clearActionMsg();
		var url = $("#deleteUrl").attr("href");
		var params = $("#mainForm").serialize();
		var callback = function(msg){
			if(window.opener.oTableArr[1] != null)
				window.opener.oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	"checkData":function(){
		BINOLSTSFH16.clearActionMsg();
		var flag = true;
		//数量不能为空（空白发货单数量可以为空，但至少有一条数量不为空也不等于0的明细）
		$.each($('#databody >tr'), function(i){
			if(i>=0){
				if($(this).find("#quantityuArr").val()=="" || $(this).find("#quantityuArr").val()=="0"  || $(this).find("#quantityuArr").val()=="-" || $(this).find("#productVendorIDArr").val()==""){
					if($(this).find("#productVendorIDArr").val()==""){
						//没有选择产品的情况下进行下面的处理
						if($('#databody >tr').length > 1){
							//行数大于1的情况下移除空白行
							$(this).remove();
						}else{
							//行数小于或等于1的情况下保留空白行直接提示错误信息
							flag = false;
							$("#errorSpan2").html($("#errmsg1").val());
							$("#errorDiv2").show();
							return;
						}
					}else{
						//选择了产品但数量输入不正确的情况下提示错误信息
						flag = false;
						$("#errorSpan2").html($("#errmsg1").val());
						$("#errorDiv2").show();
						return;
					}
				}
			}
		});
		//检查有无行
		if($('#databody >tr').length <= 0){
			flag = false;
			$('#errorDiv2 #errorSpan2').html($('#errmsg2').val());
			$('#errorDiv2').show();
		}
		return flag;
	},
	
	// 将销售明细转换成Json格式
	"getSaleDetailList" :function(){
		var saleresult = "";
		var $msgBoxs = $("#databody");
		// 将明细列表记录转换成Json格式
		saleresult = Obj2JSONArr($msgBoxs.find("tr"));
		$("#saleDetailList").val(saleresult);
	},
	
	/**
	 * 工作流调用doaction前执行的js，返回doaction，将会执行doaction，否则不执行。
	 */
	"beforeDoAction":function(){
		if(!BINOLSTSFH16.checkData()){
			return false;
		}else{
			BINOLSTSFH16.getSaleDetailList();
			return "doaction";
		}
	}
	
};

var BINOLSTSFH16 = new BINOLSTSFH16_GLOBAL();

function replaceAllComma(str){
	var re = /,/g;
	return str.replace(re,"");
};

window.onbeforeunload = function(){
	if (window.opener) {
		if (doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};

$(document).ready(function(){
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			contactPerson: {maxlength: 30},
			deliverAddress: {maxlength: 200},
			comments: {maxlength: 50}
		}
	});
	
	BINOLSTSFH16.bindInput();
	
	$('.ui-tabs').tabs();
	needUnlock = true;
});

