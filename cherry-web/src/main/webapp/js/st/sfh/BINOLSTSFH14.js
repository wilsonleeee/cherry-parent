var BINOLSTSFH14_GLOBAL = function() {

};

BINOLSTSFH14_GLOBAL.prototype = {
	/**
	  * 删除掉画面头部的提示信息框
	  * @return
      */
	"clearActionMsg":function(){
		// 移除重复数据行样式
		$('#databody').find(".errTRbgColor").removeClass('errTRbgColor');
		BINOLSTSFH14.changeOddEvenColor();
		// 删除错误提示内容
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
	},
	
	"changeCustomerType":function(obj){
		var $this = $(obj);
		var $thisVal = $this.val().toString();
		$("#customerOrganization").val("");
		$("#customerOrganizationId").val("");
		$('#contactPerson').val("");
		$('#deliverAddress').val("");
		$('#curPerson').val("");
		$('#curAddress').val("");
		$("#customerDepot").children().each(function(i){
			if(i > 0){
				$(this).remove();
			}
		});
		//取消文本框的自动完成。
		$('#customerOrganization').unautocomplete();
		if($thisVal == "1"){
			$("#trCustomerDepot").show();
			var option = {
					elementId:"customerOrganization",
					targetId:"customerOrganizationId",
					targetDetail:true,
					afterSelectFun:BINOLSTSFH14.cgAfterSelectFun,
					showNum:20
			};
			organizationBinding(option);
		}else if($thisVal == "2"){
			$("#trCustomerDepot").hide();
			var option = {
					elementId:"customerOrganization",
					targetId:"customerOrganizationId",
					targetDetail:true,
					afterSelectFun:BINOLSTSFH14.bpAfterSelectFun,
					showNum:20
			};
			bussinessPartnerBinding(option);
		}
	},
	
	"addNewLine":function(){
		// 设置全选状态
		$('#allSelect').prop("checked",false);
		
		// 拖动排序
		$("#mainTable #databody").sortable({
			update: function(event,ui){
				BINOLSTSFH14.changeOddEvenColor();
				BINOLSTSFH14.bindInput();
			}
		});
		
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
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH14.changechkbox(this);"/></td>';
			html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/><input type="hidden" id="unitCode" name="unitCode"></td>';
			html += '<td><span id="spanBarCode"></span><input type="hidden" id="barCode" name="barCode"></td>';
			html += '<td><span id="spanProductName"></span></td>';
			html += '<td><span id="spanModuleCode"></span></td>';
			html += '<td><span id="spanPrice"></span><input type="hidden" id="pricePay" name="pricePay"></td>';
			html += '<td class="center"><input  value="" name="quantityuArr" id="quantityuArr" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="7" onchange="BINOLSTSFH14.changeQuantity(this)" onkeyup="BINOLSTSFH14.changeQuantity(this)"  cssStyle="width:98%"/></td>';
			html += '<td class="center"><input  value="" name="discountRateArr" id="discountRateArr" cssClass="text-number" style="width:60px;text-align:right;" size="10" maxlength="7" onchange="BINOLSTSFH14.changeDiscountRate(this)" onkeyup="BINOLSTSFH14.keyUpChangeDiscountRate(this)" cssStyle="width:98%"/></td>';
			html += '<td class="center"><input  value="" name="discountPriceArr" id="discountPriceArr" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="12" onchange="BINOLSTSFH14.changeDiscountPrice(this)" onkeyup="BINOLSTSFH14.keyUpChangeDiscountPrice(this)" cssStyle="width:98%"/></td>';
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
			BINOLSTSFH14.setProductBinding(unitCode);
	
			$("#unitCodeBinding_"+nextIndex).focus();
			
			BINOLSTSFH14.changeOddEvenColor();
			BINOLSTSFH14.bindInput();
		}
	},
	
	"setProductBinding":function(id){
		productBinding({elementId:id,showNum:20,targetDetail:true,afterSelectFun:BINOLSTSFH14.pbAfterSelectFun});
	},
	
	/**
	 * 产品下拉输入框选中后执行方法
	 */
	"pbAfterSelectFun":function(info){
		BINOLSTSFH14.clearActionMsg();
		$('#'+info.elementId).val(info.unitCode);
		//验证产品厂商ID是否重复
		var flag = true;
		$.each($('#databody >tr'), function(i){
			if($(this).find("#productVendorIDArr").val() == info.prtVendorId){
				flag = false;
				// 给重复行添加样式
				$(this).attr("class","errTRbgColor");
				// 显示错误提示信息
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
			$('#'+info.elementId).parent().parent().find("#spanPrice").html(BINOLSTSFH14.formateMoney(info.price,2));
			
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
			BINOLSTSFH14.changeQuantity($nextInputText);
			
			$nextInputText.select();
		}
	},
	
	"bpAfterSelectFun":function(info){
		$('#customerOrganizationId').val(info.partnerId);
		
		$("#customerOrganizationId").parent().removeClass('error');
		$("#customerOrganizationId").parent().find('#errorText').remove();
		
		var param ="partnerid="+info.partnerId;
		//更改了部门后，取得该部门所拥有的仓库
		var url = $('#getBussPartnerAjaxUrl').attr("href")+"?csrftoken="+$('#csrftoken').val();
		ajaxRequest(url,param,BINOLSTSFH14.changeBussinessPartnerInfo);
	},
	
	"cgAfterSelectFun":function(info){
		$('#customerOrganizationId').val(info.orgId);
		$('#customerOrganization').val(info.name);
		
		$("#customerOrganizationId").parent().removeClass('error');
		$("#customerOrganizationId").parent().find('#errorText').remove();
		
		var param ="organizationid="+info.orgId;
		//更改了部门后，取得该部门所拥有的仓库
		var url = $('#getDepartAjaxUrl').attr("href")+"?csrftoken="+$('#csrftoken').val();
		ajaxRequest(url,param,BINOLSTSFH14.changeCustomerDepartInfo);
	},
	
	"ogAfterSelectFun":function(info){
		$('#organizationId').val(info.orgId);
		$('#organization').val(info.name);
		
		$("#organizationId").parent().removeClass('error');
		$("#organizationId").parent().find('#errorText').remove();
		
		var param ="organizationid="+info.orgId;
		//更改了部门后，取得该部门所拥有的仓库
		var url = $('#getdepotAjaxUrl').attr("href")+"?csrftoken="+$('#csrftoken').val();
		ajaxRequest(url,param,BINOLSTSFH14.changeSaleDepotDropDownList);
	},
	
	"changeOddEvenColor":function(){
		$("#databody tr:odd").not(".warningTRbgColor").attr("class","even");
		$("#databody tr:even").not(".warningTRbgColor").attr("class","odd");
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
			BINOLSTSFH14.bindInputTR(trParam);
		})
	},
	
	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，备注按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLSTSFH14"){
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
								BINOLSTSFH14.addNewLine();
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
		BINOLSTSFH14.clearActionMsg();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var $thisVal =$this.val().toString();
		$thisTd.parent().removeClass('warningTRbgColor');
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
			$thisTd.parent().find("#noDiscountAmount").val(BINOLSTSFH14.formateMoney(noDiscountAmount,2));
			// 计算折扣金额
			BINOLSTSFH14.calcuDiscountPrice(obj);
			// 计算折扣后金额
			BINOLSTSFH14.calcuDiscountAmount(obj);
		}
		BINOLSTSFH14.calcuTatol();
	},
	
	// 更改折扣率时触发的方法
	"changeDiscountRate":function(obj){
		BINOLSTSFH14.clearActionMsg();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var $thisVal =$this.val().toString();
		$thisTd.parent().removeClass('warningTRbgColor');
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
			BINOLSTSFH14.calcuDiscountPrice(obj);	
		}
		// 计算折扣后金额
		BINOLSTSFH14.calcuDiscountAmount(obj);
		// 计算总金额
		BINOLSTSFH14.calcuTatol();
	},
	
	// 更改折扣金额时触发的方法
	"changeDiscountPrice":function(obj){
		BINOLSTSFH14.clearActionMsg();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var $thisVal =$this.val().toString();
		$thisTd.parent().removeClass('warningTRbgColor');
		
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
			BINOLSTSFH14.calcuDiscountRate(obj);
		}
		// 计算折扣后金额
		BINOLSTSFH14.calcuDiscountAmount(obj);
		// 计算总金额
		BINOLSTSFH14.calcuTatol();
	},
	
	// 更改整单折扣率时触发的方法
	"changeBillDiscountRate":function(obj){
		BINOLSTSFH14.clearActionMsg();
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
			BINOLSTSFH14.calcuBillDiscountPrice();
		}
		BINOLSTSFH14.calcuBillDiscountAmount();
		
	},
	
	// 更改整单折扣金额时触发的方法
	"changeBillDiscountPrice":function(obj){
		BINOLSTSFH14.clearActionMsg();
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
			BINOLSTSFH14.calcuBillDiscountRate();
		}
		BINOLSTSFH14.calcuBillDiscountAmount();
	},
	
	// 折扣率数字变化时触发的方法
	"keyUpChangeDiscountRate":function(obj){
		BINOLSTSFH14.clearActionMsg();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var $thisVal =$this.val().toString();
		$thisTd.parent().removeClass('warningTRbgColor');
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
			BINOLSTSFH14.calcuDiscountPrice(obj);	
			// 计算折扣后金额
			BINOLSTSFH14.calcuDiscountAmount(obj);
			// 计算总金额
			BINOLSTSFH14.calcuTatol();
		}
	},
	
	// 折扣金额数字变化时触发的方法
	"keyUpChangeDiscountPrice":function(obj){
		BINOLSTSFH14.clearActionMsg();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var $thisVal =$this.val().toString();
		$thisTd.parent().removeClass('warningTRbgColor');
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
			BINOLSTSFH14.calcuDiscountRate(obj);
			// 计算折扣后金额
			BINOLSTSFH14.calcuDiscountAmount(obj);
			// 计算总金额
			BINOLSTSFH14.calcuTatol();
		}
	},
	
	// 整单折扣率数字变化时触发的方法
	"keyUpChangeBillDiscountRate":function(obj){
		BINOLSTSFH14.clearActionMsg();
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
			BINOLSTSFH14.calcuBillDiscountPrice();
			BINOLSTSFH14.calcuBillDiscountAmount();
		}
	},
	
	// 整单折扣金额数字变化时触发的方法
	"keyUpChangeBillDiscountPrice":function(obj){
		BINOLSTSFH14.clearActionMsg();
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
			BINOLSTSFH14.calcuBillDiscountRate();
			BINOLSTSFH14.calcuBillDiscountAmount();
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
		$thisTr.find(".tdAmount").text(BINOLSTSFH14.formateMoney(newAmount,2));
		$thisTr.find("#payAmount").val(BINOLSTSFH14.formateMoney(newAmount,2));
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
		$("#spanTotalAmount").html(BINOLSTSFH14.formateMoney(newBillAmount,2));
		$("#totalAmount").val(BINOLSTSFH14.formateMoney(newBillAmount,2));
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
		$("#spanTotalQuantity").html(BINOLSTSFH14.formateMoney(Number(totalQuantity),0));
		$("#totalQuantity").val(BINOLSTSFH14.formateMoney(Number(totalQuantity),0));
		$("#spanTotalAmount").html(BINOLSTSFH14.formateMoney(Number(totalAmount),2));
		$("#totalAmount").val(BINOLSTSFH14.formateMoney(Number(totalAmount),2));
		$("#billTotalAmount").val(BINOLSTSFH14.formateMoney(Number(totalAmount),2));
		// 计算整单折金额
		BINOLSTSFH14.calcuBillDiscountPrice();
		// 计算整单折后金额
		BINOLSTSFH14.calcuBillDiscountAmount();
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
		BINOLSTSFH14.clearActionMsg();
		$("#databody :checkbox").each(function(){
			var $this = $(this);
			if($this.prop("checked")){			
				$this.parent().parent().remove();
			}					
		});
		BINOLSTSFH14.changeOddEvenColor();
		$('#allSelect').prop("checked",false);
		$("input[type=checkbox]").prop("checked",false);
		if($('#databody >tr').length <= 0){
			BINOLSTSFH14.addNewLine();
		}
		// 重新计算总金额
		BINOLSTSFH14.calcuTatol();
	},
	
	"save":function(){
		if(!$('#mainForm').valid()) {
			return false;
		}
		if(!BINOLSTSFH14.checkData()){
			return false;
		}
		BINOLSTSFH14.getSaleDetailList();
		var url = $("#saveUrl").attr("href");
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				BINOLSTSFH14.clearPage();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"confirm":function(){
		if(!$('#mainForm').valid()) {
			return false;
		}
		if(!BINOLSTSFH14.checkData()){
			return false;
		}
		BINOLSTSFH14.getSaleDetailList();
		var url = $("#submitUrl").attr("href");
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				BINOLSTSFH14.clearPage();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"checkData":function(){
		BINOLSTSFH14.clearActionMsg();
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
	
	"clearPage" :function(){
		$("#tdSaleBill").text("");
		$("#saleOrderNo").val("");
		var refresh = function() {
			var getBillCodeurl = $("#getBillCodeUrl").attr("href");
			var getBillCodeparam = $("#mainForm").serialize();
			var getBillCodecallback = function(billCodeMsg){
				$("#tdSaleBill").text(billCodeMsg);
				$("#saleOrderNo").val(billCodeMsg);
			}
			cherryAjaxRequest({
				url:getBillCodeurl,
				param:getBillCodeparam,
				callback:getBillCodecallback
			});
		};
		setTimeout(refresh,200);
		
		$("#customerOrganization").val("");
		$("#customerOrganizationId").val("");
		$("#contactPerson").val("");
		$("#deliverAddress").val("");
		$("#curPerson").val("");
		$("#curAddress").val("");
		$("#salesStaff").val("");
		$("#salesStaffId").val("");
		$("#organization").val("");
		$("#organizationId").val("");
		$("#comments").val("");
		$("#expectFinishDate").val("");
		
		$.each($('#databody >tr'), function(i){
			if(i >= 0){
				$(this).remove();
			}
		});
		$("#rowNumber").val("1");
		$("#saleDetailList").val("");
		$("#totalDiscountRate").val("");
		$("#totalDiscountPrice").val("");
		$("#spanTotalQuantity").html("0");
		$("#spanTotalAmount").html("0.00");
		$("#totalQuantity").val("");
		$("#totalAmount").val("");
		$("#billTotalAmount").val("");
		$("#allSelect").prop("checked",false);
		
		$("#customerDepot").children().each(function(i){
			if(i > 0){
				$(this).remove();
			}
		});
		$("#customerDepot").attr("disabled",true);
		
		$("#saleDepot").children().each(function(i){
			if(i > 0){
				$(this).remove();
			}
		});
		$("#saleDepot").attr("disabled",true);
		
		// 清空页面数据后添加一个空行
		BINOLSTSFH14.addNewLine();
		// 更改鼠标焦点
		$("#customerOrganization").focus();
	},
	
	/**
	 * 修改客户部门仓库下拉框
	 * @param data
	 * @return
	 */
	"changeCustomerDepartInfo" :function(data){
		//data为json格式的文本字符串
		var departInfo = eval("("+data+")");    //包数据解析为json 格式  
		
		$('#contactPerson').val(departInfo.contactPerson);
		$('#deliverAddress').val(departInfo.address);
		$('#curPerson').val(departInfo.contactPerson);
		$('#curAddress').val(departInfo.address);
		var depot = departInfo.depotList;
		
		$("#customerDepot").children().each(function(i){
			if(i > 0){
				$(this).remove();
			}
		});
		$.each(depot, function(i){
			$("#customerDepot").append("<option value='"+ depot[i].BIN_InventoryInfoID+"'>"+escapeHTMLHandle(depot[i].InventoryName)+"</option>"); 
			});	
		if($("#customerDepot option").length>1){
			$("#customerDepot").get(0).selectedIndex=1;
			if($("#customerOrganizationId").val() != null && $("#customerOrganizationId").val() !=""){
				$("#customerDepot").attr("disabled",false);
				$("#customerLogicDepot").attr("disabled",false);
				BINOLSTSFH14.clearActionMsg();
				$("#customerDepot").parent().removeClass('error');
				$("#customerDepot").parent().find('#errorText').remove();
				$("#customerLogicDepot").parent().removeClass('error');
				$("#customerLogicDepot").parent().find('#errorText').remove();
			}
		}else{
			$("#customerLogicDepot").attr("disabled",true);
			var allChildren = $("#customerDepot").children();
			$("#customerDepot").html("").append($(allChildren[0]).clone());
		}
	},
	
	/**
	 * 修改销售部门仓库下拉框
	 * @param data
	 * @return
	 */
	"changeSaleDepotDropDownList" :function(data){
		//data为json格式的文本字符串	
		var depot = eval("("+data+")");    //包数据解析为json 格式  
		$("#saleDepot").children().each(function(i){
			if(i > 0){
				$(this).remove();
			}
		});
		$.each(depot, function(i){
			$("#saleDepot").append("<option value='"+ depot[i].BIN_InventoryInfoID+"'>"+escapeHTMLHandle(depot[i].InventoryName)+"</option>"); 
			});	
		if($("#saleDepot option").length>1){
			$("#saleDepot").get(0).selectedIndex=1;
			if($("#organizationId").val() != null && $("#organizationId").val() !=""){
				$("#saleDepot").attr("disabled",false);
				$("#saleLogicDepot").attr("disabled",false);
				BINOLSTSFH14.clearActionMsg();
				$("#saleDepot").parent().removeClass('error');
				$("#saleDepot").parent().find('#errorText').remove();
				$("#saleLogicDepot").parent().removeClass('error');
				$("#saleLogicDepot").parent().find('#errorText').remove();
			}
		}else{
			$("#saleLogicDepot").attr("disabled",true);
			var allChildren = $("#saleDepot").children();
			$("#saleDepot").html("").append($(allChildren[0]).clone());
		}
	},
	
	"changeBussinessPartnerInfo" :function(data){
		var partner = eval("("+data+")");
		if(partner != undefined && partner != null && partner != ""){
			$('#contactPerson').val(partner.contactPerson);
			$('#deliverAddress').val(partner.deliverAddress);
			$('#curPerson').val(partner.contactPerson);
			$('#curAddress').val(partner.deliverAddress);
			BINOLSTSFH14.clearActionMsg();
		}
	},
	
	/**
	 * 导入产品
	 */
	"importProduct":function(){
		var dialogSetting = {
			dialogInit: "#dialogInit",
			width: 600,
			height: 220,
			title: $("#dialogTitle").text()
		};
		openDialog(dialogSetting);
		
		var importUrl = $("#initImportUrl").attr("href");
		var callback = function(msg) {
			$("#dialogInit").html(msg);
		};
		cherryAjaxRequest({
			url: importUrl,
			param: null,
			callback: callback
		});
	},
	
	"saleProductUpload" : function (){
		// 错误信息提示区
    	$errorMessage = $('#errorMessage');
    	// 清空错误信息
    	$errorMessage.empty();
    	$('#databody').find(".errTRbgColor").removeClass('errTRbgColor');
		$('#databody').find(".warningTRbgColor").removeClass('warningTRbgColor');
		
    	if($('#upExcel').val()==''){
    		var errHtml = this.getErrHtml($('#errMsgNoFile').val());
    		$errorMessage.html(errHtml);
    		$("#pathExcel").val("");
    		return false;
    	}
    	// 将销售明细列表转换成Json字符串
		BINOLSTSFH14.getSaleDetailList();
		
		var repeatFlag = "";
		if($("#importRepeatFlag").attr("checked")){
			repeatFlag = $("#importRepeatFlag").val();
		}
    	var url = $("#saleProductImportUrl").attr("href");
    	var params = {};
    	params.repeatFlag = repeatFlag;
    	params.saleDetailList = $("#saleDetailList").val();
    	params.csrftoken = $('#csrftoken').val();
    	
    	$("#loading").ajaxStart(function(){$(this).show();});
    	$("#loading").ajaxComplete(function(){$(this).hide();});
    	// 禁用导入按钮
    	$("#upload").prop("disabled",true);
    	$("#upload").addClass("ui-state-disabled");
    	$.ajaxFileUpload({
	        url: url,
	        secureuri:false,
	        data:params,
	        fileElementId:'upExcel',
	        dataType: 'html',
	        success: function (data){
	        	//释放导入按钮
	        	$("#upload").removeAttr("disabled",false);
	        	$("#upload").removeClass("ui-state-disabled");
	        	if(data.indexOf("errorMessage")>-1){
	        		$errorMessage.html(data);
	        	}else{
	        		// 移除空白行
	        		$.each($('#databody >tr'), function(i){
	        			if(i >= 0){
	        				if($(this).find("#productVendorIDArr").val()==""){
	        					$(this).remove();
	        				}
	        			}
	        		});
	        		var products = eval("("+data+")");
	        		$.each(products, function(i){
        				var rowRepeat = false;
	        			$.each($('#databody >tr'), function(j){
	        				if($(this).find("#productVendorIDArr").val() == products[i].productVendorId){
	        					rowRepeat = true;
	        					var $this = $(this).find("#quantityuArr");
	        					var $thisVal = $this.val().toString();
	        					if($thisVal == undefined || $thisVal == ""){
	        						$thisVal = "0";
	        					}
	        					var quantity = Number($thisVal) + Number(products[i].quantity);
	        					$this.val(quantity);
	        					
	        					var price = $(this).find("#priceUnitArr").val();
	        					if(price == undefined || price == ""){
	        						price = "0.00";
	        					}
	        					// 折扣前金额
	        					price = replaceAllComma(price);
	        					var noDiscountAmount = Number(price) * Number(quantity);
	        					$(this).find("#noDiscountAmount").val(BINOLSTSFH14.formateMoney(noDiscountAmount,2));
	        					// 计算折扣金额
	        					BINOLSTSFH14.calcuDiscountPrice($this);
	        					// 计算折扣后金额
	        					BINOLSTSFH14.calcuDiscountAmount($this);
	        					// 重复数据添加样式用于提示
	        					$(this).attr("class","warningTRbgColor");
	        				}
	        			});
	        			if(!rowRepeat){
	        				// 追加新行
	        				BINOLSTSFH14.appendNewRow(products[i]);
	        			}
	        		});
	        		// 计算总金额
	    			BINOLSTSFH14.calcuTatol();
					// 关闭弹出窗口
	        		removeDialog("#dialogInit");
	        	}
	        }
        });
    },
    
    // 导入数据时追加新行
    "appendNewRow":function(proInfo){
    	var amount = BINOLSTSFH14.formateMoney((Number(proInfo.salePrice) * Number(proInfo.quantity)),2);
		var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		var html = '<tr id="dataRow'+nextIndex+'">';
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH14.changechkbox(this);"/></td>';
		html += '<td>'+ proInfo.unitCode +'<input type="hidden" id="unitCode" name="unitCode" value="'+ proInfo.unitCode +'"/></td>';
		html += '<td>'+ proInfo.barCode +'<input type="hidden" id="barCode" name="barCode" value="'+  proInfo.barCode +'"/></td>';
		html += '<td>'+ proInfo.productName +'</td>';
		html += '<td></td>';
		html += '<td>'+ proInfo.salePrice +'<input type="hidden" id="pricePay" name="pricePay" value="'+  proInfo.salePrice +'"/></td>';
		html += '<td class="center"><input value="'+  proInfo.quantity +'" name="quantityuArr" id="quantityuArr" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="7" onchange="BINOLSTSFH14.changeQuantity(this)" onkeyup="BINOLSTSFH14.changeQuantity(this)"  cssStyle="width:98%"/></td>';
		html += '<td class="center"><input value="" name="discountRateArr" id="discountRateArr" cssClass="text-number" style="width:60px;text-align:right;" size="10" maxlength="7" onchange="BINOLSTSFH14.changeDiscountRate(this)" onkeyup="BINOLSTSFH14.keyUpChangeDiscountRate(this)" cssStyle="width:98%"/></td>';
		html += '<td class="center"><input value="" name="discountPriceArr" id="discountPriceArr" cssClass="text-number" style="width:90px;text-align:right;" size="10" maxlength="12" onchange="BINOLSTSFH14.changeDiscountPrice(this)" onkeyup="BINOLSTSFH14.keyUpChangeDiscountPrice(this)" cssStyle="width:98%"/></td>';
		html += '<td class="center tdAmount" style="text-align:right;">'+ amount +'</td>';
		html += '<td class="center"><input value="'+  proInfo.remarks +'" name="reasonArr" type="text" id="reasonArr" size="25" maxlength="50"  cssStyle="width:98%"/></td>';
		html += '<td style="display:none">'
        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+  proInfo.productVendorId +'"/>'
        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+  proInfo.productVendorId +'"/>'
        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+  proInfo.salePrice +'"/>'
        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value="'+ amount +'"/></td></tr>';
		$("#databody").append(html);
		// 改变单双行样式
		BINOLSTSFH14.changeOddEvenColor();
		// 绑定输入框事件
		BINOLSTSFH14.bindInput();
    },
    
    // 取得错误信息HTML
	"getErrHtml" : function (text){
		var errHtml = '<div class="actionError"><ul><li><span>';
		errHtml += text;
		errHtml += '</span></li></ul></div>';
		return errHtml;
	}
	
};

var BINOLSTSFH14 = new BINOLSTSFH14_GLOBAL();

function replaceAllComma(str){
	var re = /,/g;
	return str.replace(re,"");
}

$.fn.selectRange = function(start, end) {
	return this.each(function() {
		if (this.setSelectionRange) {
			this.focus();
			this.setSelectionRange(start, end);
		} else if (this.createTextRange) {
			var range = this.createTextRange();
			range.collapse(true);
			range.moveEnd('character', end);
			range.moveStart('character', start);
			range.select();
		}
	});
};

$(document).ready(function(){
	// 客户部门下拉框绑定
	var option1 = {
			elementId:"customerOrganization",
			targetId:"customerOrganizationId",
			targetDetail:true,
			afterSelectFun:BINOLSTSFH14.cgAfterSelectFun,
			showNum:20
	};
	organizationBinding(option1);
	// 业务员下拉框绑定
	var option2 = {
			elementId:"salesStaff",
			categoryCode:"ALL",
			showNum:20	
	};
	salesStaffBinding(option2);
	// 销售部门下拉框绑定[不带仅限，不包含柜台]
	var option3 = {
			elementId:"organization",
			targetId:"organizationId",
			targetDetail:true,
			afterSelectFun:BINOLSTSFH14.ogAfterSelectFun,
			showNum:20
	};
	organizationBinding(option3);
	
	$("#saleDate").cherryDate();
	$("#expectFinishDate").cherryDate({
		beforeShow: function(input){
			var value = $("#saleDate").val();
			return [value, "minDate"];
		}
	});
	$("#saleTime").timepicker({
		timeOnlyTitle: $("#timeOnlyTitle").val(),
		currentText: $("#currentText").val(),
		closeText: $("#closeText").val(),
		timeText: $("#timeText").val(),
		hourText: $("#hourText").val(),
		minuteText: $("#minuteText").val(),
		hourMax: 22
	});
	
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			customerOrganizationId: {required: true},
			salesStaffId: {required: true},
			organizationId: {required: true},
			saleDate: {dateValid: true},
			saleTime: {timeHHMMValid: true},
			expectFinishDate: {required: true, dateValid: true},
			contactPerson: {maxlength: 30},
			deliverAddress: {maxlength: 200},
			comments: {maxlength: 50}
		}
	});
	// 初始化时先添加一个空行
	BINOLSTSFH14.addNewLine();
	// 更改鼠标焦点
	$("#customerOrganization").focus();
});
