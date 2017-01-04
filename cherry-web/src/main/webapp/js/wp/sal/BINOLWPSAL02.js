var BINOLWPSAL02_GLOBAL = function() {

};

BINOLWPSAL02_GLOBAL.prototype = {
	
	/* 是否刷新一览画面 */
	"doRefresh" : false,
		
	/* 是否打开父页面锁定*/
	"needUnlock" : true,


	"smartPromotionDialogRetFlag" : true,

	"clearActionClass":function(){
		// 移除重复数据行样式
		$('#databody').find(".errTRbgColor").removeClass('errTRbgColor');
		BINOLWPSAL02.changeOddEvenColor();
	},
		
	"addNewLine":function(){
		//已有空行不新增一行
		var addNewLineFlag = true;
		$.each($('#databody >tr'), function(i){
			if($(this).find("[name='unitCodeBinding']").is(":visible")){
				addNewLineFlag = false;
				$(this).find("[name='unitCodeBinding']").focus();
				return;
			}
		});
		// 行数大于10行时隐藏会员信息
		if($('#databody >tr').length >= 10){
			$("#memberPageDiv").hide();
		}
		
		if(addNewLineFlag){
			var nextIndex = parseInt($("#rowNumber").val())+1;
			$("#rowNumber").val(nextIndex);
			var rowIndex = parseInt($("#rowCode").val())+1;
			$("#rowCode").val(rowIndex);
			var html = '<tr id="dataRow'+nextIndex+'">';
			var discountType=$('#discountType').val();
			var isDiscountFlag=$('#isDiscountFlag').val();
			html += '<td id="indexNo">'+rowIndex+'</td>';
			html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value=""/><input id="unitCode" name="unitCode" type="hidden"/></td>';
			html += '<td><span id="spanBarCode"></span><input id="barCode" name="barCode" type="hidden"/></td>';
			html += '<td><span id="spanProductName"></span><input id="productNameArr" name="productNameArr" type="hidden"/></td>';
			html += '<td><span id="spanPrice"></span><input id="pricePay" name="pricePay" type="hidden"/></td>';
			if($("#useMemberPrice").val() == "Y"){
				html += '<td><span id="spanMemberPrice"></span><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
			}
			if($("#isPlatinumPrice").val()=="Y"){
				html += '<td><span id="spanPlatinumPrice"></span><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
			}
			html += '<td class="center"><button id="btnLessen" class="wp_minus" disabled="disabled" onclick="BINOLWPSAL02.lessenQuantity(this);return false;"></button>'
					+'<input id="quantityuArr" name="quantityuArr" type="text" class="text" style="width:50px;margin:.2em 0;" maxlength="4" value="" disabled="disabled" onchange="BINOLWPSAL02.changeQuantity(this)" onkeyup="BINOLWPSAL02.changeQuantity(this)"/>'
					+'<button id="btnAdd" class="wp_plus" disabled="disabled" onclick="BINOLWPSAL02.addQuantity(this);return false;"></button></td>';
			if("N" == isDiscountFlag){
				//折扣率不可用时，改成一般显示
				html += '<td><span></span><input class="hide" name="discountRateArr" id="discountRateArr" value=""/></td>';
			}else{
				html += '<td><input id="discountRateArr" name="discountRateArr" type="text" class="text" style="width:40px;" value="" maxlength="6" onchange="BINOLWPSAL02.changeDiscountRate(this)" onkeyup="BINOLWPSAL02.keyUpChangeDiscountRate(this)"/>%</td>';
			}
			html += '<td><input id="realPriceArr" name="realPriceArr" type="text" class="text" style="width:60px;" value="" maxlength="10" onchange="BINOLWPSAL02.changeRealPrice(this)" onkeyup="BINOLWPSAL02.keyUpchangeRealPrice(this)"/></td>';
			html += '<td id="tdAmount">0.00</td>';
			html += '<td><span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL02.deleteRow(this);return false;"></button></span></td>';
			html += '<td style="display:none">'
			+'<input type="hidden" id="orderId" name="orderId" value=""/>'
			+'<input type="hidden" id="couponCode" name="couponCode" value=""/>'
			+'<input type="hidden" id="isStock" name="isStock" value=""/>'
			+'<input type="hidden" id="mainCode" name="mainCode" value=""/>'
			+'<input type="hidden" id="counterActCode" name="counterActCode" value=""/>'
			+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value=""/>'
			+'<input type="hidden" id="activitySign" name="activitySign" value=""/>'
	        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>'
	        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
	        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/>'
	        +'<input type="hidden" id="payAmount" name="payAmount" value=""/>'
	        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value=""/>'
	        +'<input type="hidden" id="proType" name="proType" value=""/>'
	        +'<input type="hidden" id="exPoint" name="exPoint" value=""/>'
	        +'<input type="hidden" id="stockQuantity" name="stockQuantity" value=""/>'
	        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
			$("#databody").append(html);
			if("N" == discountType){
				//discountRateArr折扣  realPriceArr实价不可变动
				$("[name='discountRateArr']").attr("readonly","readonly").attr("class","text disabled");
				$("[name='realPriceArr']").attr("readonly","readonly").attr("class","text disabled");
			}
			var unitCode = "unitCodeBinding_"+nextIndex;
			// 绑定产品下拉列表
			BINOLWPSAL02.setProductBinding(unitCode);
			// 给输入框绑定事件等操作
			var rowId = "dataRow"+nextIndex;
			BINOLWPSAL02.bindInput(rowId);
			// 厂商编码输入框获得焦点
			$("#unitCodeBinding_"+nextIndex).focus();
			// 改变交替行样式
			BINOLWPSAL02.changeOddEvenColor();
			// 修改商品明细样式
			$($("#databody >tr")).each(function(){
				if($(this).find("td:first").attr("id") == undefined){
					$(this).addClass("green");
				}
			});
		}
	},
	
	"setProductBinding":function(id){
		var counterCode = $("#counterCode").val();
		cntProductBinding({elementId:id,showNum:20,counterCode:counterCode,targetDetail:true,afterSelectFun:BINOLWPSAL02.pbAfterSelectFun});
	},
	
	"pbAfterSelectFun":function(info){
		BINOLWPSAL02.clearActionClass();
		$('#'+info.elementId).val(info.unitCode);
		// 校验是否积分兑换单据并且所选产品是否可以参与积分兑换
		var isExchanged = info.IsExchanged;
		var billClassify = $("#billClassify").val();

		//积分兑换活动是否限定产品
		var isLimitProduct = $("#isLimitProduct").val();
		//表示需要限定产品，此时只有可以参与积分兑换的产品才能进行积分兑换活动
		if((isLimitProduct == 1 || isLimitProduct == "1")) {
			if ((isExchanged == 0 || isExchanged == "0") && billClassify == "DHHD") {
				// 显示提示信息
				BINOLWPSAL02.showMessageDialog({
					message: "您选择的产品不允许参加积分兑换",
					type: "MESSAGE",
					focusEvent: function () {
						// 当前输入框信息清空
						$('#' + info.elementId).val("");
						// 最后一行第一个可见的文本框获得焦点
						BINOLWPSAL02.firstInputSelect();
						// 修改商品明细样式
						$($("#databody >tr")).each(function () {
							if ($(this).find("td:first").attr("id") == undefined) {
								$(this).addClass("green");
							}
						});
					}
				});
				return;
			}
		}
		// 删除销售单据中的打折明细行
		BINOLWPSAL02.deleteDiscountRow();
		//验证产品厂商ID是否重复
		var merge = $("#merge").val();
		var flag = true;
		if(merge=='Y'){
			$.each($('#databody >tr'), function(i){
				var productVendorID = $(this).find("#productVendorIDArr").val();
				if(productVendorID != null && productVendorID != "" && productVendorID != undefined){
					if(productVendorID == info.prtVendorId){
						flag = false;
						// 给重复行添加样式
						$(this).attr("class","errTRbgColor");
						var $this = $(this).find("#quantityuArr");
						var quantity = $(this).find("#quantityuArr").val();
						var newQuantity = Number(quantity) + 1;
						// 给重复行的数量加1
						$this.val(newQuantity);
						// 计算折扣后金额
						BINOLWPSAL02.calcuRealAmount($this);
						// 计算总金额
						BINOLWPSAL02.calcuTatol();
					}
				}
			});
		}
		if(flag){
			var $thisTr = $('#'+info.elementId).parent().parent();
			//设置隐藏值
			$thisTr.find("#prtVendorId").val(info.prtVendorId);
			$thisTr.find("#productVendorIDArr").val(info.prtVendorId);
			$thisTr.find("#priceUnitArr").val(info.price);
			$thisTr.find("#memberPrice").val(info.memPrice);
			$thisTr.find("#unitCode").val(info.unitCode);
			$thisTr.find("#barCode").val(info.barCode);
			$thisTr.find("#productNameArr").val(info.productName);
			$thisTr.find("#pricePay").val(info.price);
			$thisTr.find("#proType").val("N");
			$thisTr.find("#isExchanged").val(info.IsExchanged);
			$thisTr.find("#stockQuantity").val(info.stockQuantity);//info.stockQuantity
			$thisTr.find("#isStock").val(info.isSocket);
			$thisTr.find("#isQuantity").val(info.quantity);
			
			//设置显示值
			$thisTr.find("#spanUnitCode").html(info.unitCode);
			$thisTr.find("#spanBarCode").html(info.barCode);
			$thisTr.find("#spanProductName").html(info.productName);
			$thisTr.find("#spanPrice").html(info.price);
			$thisTr.find("#spanMemberPrice").html(info.memPrice);

			if((undefined == info.platinumPrice || info.platinumPrice=="" || Number(info.platinumPrice)<=0) && $("#isPlatinumPrice").val()=="Y"){
				$thisTr.find("#spanPlatinumPrice").html(info.memPrice);
				$thisTr.find("#platinumPrice").val(info.memPrice);
			}else {
				$thisTr.find("#spanPlatinumPrice").html(info.platinumPrice);
				$thisTr.find("#platinumPrice").val(info.platinumPrice);
			}
			
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
			// 根据销售状态确定行样式
			var saleType = $("#saleType").val();
			if(saleType == "SR"){
				$thisTr.attr("class","red");
			}
			
			//跳到下一个文本框
			var nxtIdx = $thisTr.find("input:text").index($('#'+info.elementId)) + 1;
			var $nextInputText =  $thisTr.find("input:text:eq("+nxtIdx+")");
			
			$thisTr.find("#btnLessen").removeAttr("disabled");
			$thisTr.find("#quantityuArr").removeAttr("disabled");
			$thisTr.find("#btnAdd").removeAttr("disabled");
			//选中一个产品后给出默认数量
			var quantity = $thisTr.find("#quantityuArr").val();
			if(quantity == ""){
				$thisTr.find("#quantityuArr").val("1");
			}
			//计算金额（防止先输入数量后输入产品）
			BINOLWPSAL02.changeQuantity($nextInputText);
			
			var showSaleRows = $("#showSaleRows").val();
			// 判断行数是否大于允许的行数
			if($('#databody >tr').length < Number(showSaleRows)){
				// 增加一条新行
				BINOLWPSAL02.addNewLine();
			}else{
				// 显示提示信息
				BINOLWPSAL02.showMessageDialog({
					message:"不能再增加新的行了，如有需要请修改配置", 
					type:"MESSAGE", 
					focusEvent:function(){
						// 最后一行第一个可见的文本框获得焦点
						BINOLWPSAL02.firstInputSelect();
					}
				});
			}
		}else{
			// 当前输入框信息清空
			$('#'+info.elementId).val("");
		}
		var smartPromotion=$('#smartPromotion').val();
		if(smartPromotion == 'N'){//非智能促销状态
			$("#btnCollect").removeAttr("disabled");
			$("#btnDiscount").removeAttr("disabled");
			$("#btnSearchBills").attr("disabled",true);
			$("#btnCollect").attr("class","btn_top");
			$("#btnDiscount").attr("class","btn_top");
			$("#btnSearchBills").attr("class","btn_top_disable");
		}else if(smartPromotion == 'Y'){//智能促销状态
			$("#btnCollect").removeAttr("disabled");
			$("#btnDiscount").attr("class","btn_top_disable");
			$("#btnDiscount").attr("disabled",true);
			$("#btnSearchBills").attr("disabled",true);
			$("#btnCollect").attr("class","btn_top");
			$("#btnSearchBills").attr("class","btn_top_disable");
		}
	},
	
	"changeOddEvenColor":function(){
		// 根据销售状态确定行样式
		var saleType = $("#saleType").val();
		if(saleType != "SR"){
			$("#databody tr:odd").not(".warningTRbgColor,.green").attr("class","even");
			$("#databody tr:even").not(".warningTRbgColor,.green").attr("class","odd");
		}
	},
	
	"bindInput":function(rowId){
		
		// 修改商品明细样式
		$($("#databody >tr")).each(function(){
			if($(this).find("td:first").attr("id") == undefined){
				$(this).addClass("green");
			}
		});
		
		var tableName = "mainTable";
		var indexNo = 1;
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
//			BINOLWPSAL02.bindInputTR(trParam);
			// 更新序号
			if($(this).find("#indexNo").length > 0){
				$(this).find("#indexNo").text(indexNo);
				$("#rowCode").val(indexNo);
				indexNo++;
			}
		});
		var $thisTr = $("#"+rowId);
		// 输入框绑定F1到F9快捷键
		$thisTr.find("input:text:visible")
		.bind("keydown", "f1", function (e) { e.preventDefault();BINOLWPSAL02.initMatchRule_CloudPos(); })
		.bind("keydown", "f2", function (e) { e.preventDefault();BINOLWPSAL02.discount(); })
		.bind("keydown", "f3", function (e) { e.preventDefault();BINOLWPSAL02.hangBills(); })
		.bind("keydown", "f4", function (e) { e.preventDefault();BINOLWPSAL02.searchBills(); })
		.bind("keydown", "f5", function (e) { e.preventDefault();BINOLWPSAL02.showMember(); })
		.bind("keydown", "f6", function (e) { e.preventDefault();BINOLWPSAL02.emptyShoppingCart(); })
		.bind("keydown", "f7", function (e) { e.preventDefault();BINOLWPSAL02.returnsGoods(); })
		.bind("keydown", "f8", function (e) { e.preventDefault();BINOLWPSAL02.addHistoryBill(); })
		.bind("keydown", "f9", function (e) { if("Y"==new_Czk_Pay && "Y"==$("#rechargeAndOpendCardButton").val()){ e.preventDefault();BINOLWPSAL13.confirm(); } })
		.bind("keydown", "i", function (e) { e.preventDefault();BINOLWPSAL02.deleteLastPro(); })
		.bind('keydown', function (e) {
			var key = e.which;
			// 绑定回车键
			if(key == 13) {
				//默认事件取消
				e.preventDefault();
				if($(this).attr("name") != "unitCodeBinding"){
					// 最后一行第一个可见的文本框获得焦点
					BINOLWPSAL02.firstInputSelect();
				}
			}
		});
		// 数量输入框绑定加号和减号快捷键
		$thisTr.find("#quantityuArr").bind("keydown", function (e) {
			var key = e.which;
			if (key == 173 || key == 109) {
				e.preventDefault();
				BINOLWPSAL02.lessenQuantity($thisTr.find("#quantityuArr"));
			}
		}).bind("keydown", function (e) {
			var key = e.which;
			if (key == 61 || key == 107) {
				e.preventDefault();
				BINOLWPSAL02.addQuantity($thisTr.find("#quantityuArr"));
			}
		});
		// 记录行绑定“/”键
		$thisTr.bind("keydown", function (e) {
			var key = e.which;
			if(key == 111 || key == 191){
				//默认事件取消
				e.preventDefault();
				BINOLWPSAL02.showNumberDialog();
			}
		});
//		// 绑定上下键
//		$thisTr.bind("keydown", function (e) {
//			var key = e.which;
//			// 向上方向键
//			if (key == 38) {
//				e.preventDefault();
//				
//				$thisTr.prev().attr("class","errTRbgColor");
//				$thisTr.prev().find("#quantityuArr:visible").focus();
//			}
//			// 向下方向键
//			if (key == 40) {
//				e.preventDefault();
//				
//				$thisTr.next().attr("class","errTRbgColor");
//				$thisTr.next(".selected").find("#quantityuArr:visible").focus();
//			}
//		});
	},
	
	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，最后一个文本框按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			var key = e.which;
			if (key == 13) {
				//默认事件取消
				e.preventDefault();
				// 最后一行第一个可见的文本框获得焦点
				BINOLWPSAL02.firstInputSelect();
				
//				//当前文本框为下拉框可见且输入有问题如重复，停止跳到下一个
//				var nextFlag = true;
//				if($(this).attr("name") == "unitCodeBinding" && $(this).is(":visible")){
//					nextFlag = false;
//				}
//				if(nextFlag){
//					var nxtIdx = $inp.index(this) + 1;
//					var $nextInputText = $(trParam+" input:text:eq("+nxtIdx+")");
//					if($nextInputText.length>0){
//						//跳到下一个文本框
//						$nextInputText.focus();
//						$nextInputText.select();
//					}else{
//						//跳到下一行第一个文本框，如果没有下一行，新增一行
//						if($(trParam).next().find("input:text:visible").length==0){
//							// 删除销售单据中的打折明细行
//							BINOLWPSAL02.deleteDiscountRow();
//							
//							var showSaleRows = $("#showSaleRows").val();
//							// 判断行数是否大于允许的行数
//							if($('#databody >tr').length < Number(showSaleRows)){
//								// 增加一条新行
//								BINOLWPSAL02.addNewLine();
//							}else{
//								// 显示提示信息
//								BINOLWPSAL02.showMessageDialog({
//									message:"不能再增加新的行了，如有需要请修改配置", 
//									type:"MESSAGE", 
//									focusEvent:function(){
//										// 最后一行第一个可见的文本框获得焦点
//										BINOLWPSAL02.firstInputSelect();
//									}
//								});
//							}
//						}else{
//							$(trParam).next().find("input:text:visible:eq(0)").focus();
//						}
//					}
//				}
			}
		});
	},
	
	"showNumberDialog":function(){
		var dgOpenedState = $("#dgOpenedState").val();
		if(dgOpenedState != "Y"){
			if($('#databody >tr').length > 1 || ($('#databody >tr').length == 1 && !BINOLWPSAL02.isEmptyOrDiscount())){
				var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 400,
					height: 200,
					title: $("#saleNumberDialogTitle").text(),
					closeEvent:function(){
						// 最后一行第一个可见的文本框获得焦点
						BINOLWPSAL02.firstInputSelect();
						// 关闭弹出窗口
						removeDialog("#dialogInit");
					}
				};
				openDialog(dialogSetting);
				
				var pageInfo = $("#saleNumberDialogDiv").html();
				$("#dialogInit").html(pageInfo);
				
				// 清除现有样式
				BINOLWPSAL02.clearActionClass();
				
				// 添加隐藏的窗口打开标识元素
				var hideHtml = '<input type="hidden" id="dgOpenedState" name="dgOpenedState" value="Y"/>';
				$("#dialogInit").append(hideHtml);
				
				// 文本框绑定回车事件
				$("#dialogInit").find("input:text:visible")
					.bind("keydown", function (e) {
						var key = e.which;
						if (key == 13) {
							e.preventDefault();
							BINOLWPSAL02.setNumberConfirm();
						}
					});

				if(!BINOLWPSAL02.isEmptyOrDiscount()){
					// 最后一行不是空行或者折扣数据时
					$('#databody >tr:last').attr("class","errTRbgColor changeNumberRow");
					var quantity = $('#databody >tr:last').find("#quantityuArr").val();
					$("#dialogInit").find("#saleNumber").val(quantity);
				}else{
					// 最后一行是空行或折扣行情况下向上找到最后一条购物车记录
					var i = 2;
					var j = $('#databody >tr').length;
					var quantity = 1;
					while(i <= j){
						var $curtr = $('#databody >tr').eq(-i);
						var activityTypeCode = $curtr.find("#activityTypeCode").val();
						if(activityTypeCode != "ZDZK" && activityTypeCode != "ZDQL" && activityTypeCode != "CXHD"){
							$curtr.attr("class","errTRbgColor changeNumberRow");
							quantity = $curtr.find("#quantityuArr").val();
							break;
						}
						i++;
					}
					$("#dialogInit").find("#saleNumber").val(quantity);
				}				
				// 数量输入框获得焦点
				$("#dialogInit").find("#saleNumber").select();
			}
		}
	},
	
	"setNumberConfirm":function(){
		// 将输入框值赋给选中行
		var $this = $('#databody').find(".errTRbgColor.changeNumberRow").find("#quantityuArr");
		var quantity = $("#dialogInit").find("#saleNumber").val();
		if(quantity == undefined || quantity == ""){
			quantity = 1;
		}else if(isNaN(quantity)){
			quantity = 1;
		}else if(parseInt(Number(quantity)) < 1 || parseInt(Number(quantity)) > 9999){
			quantity = 1;
		}else{
			if(quantity.charAt(0) == "0" || quantity.charAt(0) == 0){
				quantity = 1;
			}
		}
		$this.val(quantity);
		// 计算折扣后金额
		BINOLWPSAL02.calcuRealAmount($this);
		// 计算总金额
		BINOLWPSAL02.calcuTatol();
		
		// 最后一行第一个可见的文本框获得焦点
		BINOLWPSAL02.firstInputSelect();
		// 关闭弹出窗口
		removeDialog("#dialogInit");
	},
	
	"isEmptyOrDiscount":function(){
		if($('#databody >tr').length > 0){
			// 获取最后一行
			var $lastTr = $('#databody').find("tr:last");
			// 判断最后一行记录是否为空或折扣数据
			if(($lastTr.find("#productVendorIDArr").val() != undefined && $lastTr.find("#productVendorIDArr").val() != "") 
					&& ($lastTr.find("#activityTypeCode").val() != "ZDZK" && $lastTr.find("#activityTypeCode").val() != "ZDQL")){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	},
	
	"keyupSaleNumber":function(obj){
		var $this = $(obj);
		var quantity = $this.val();
		if(quantity == undefined || quantity == ""){
			quantity = 1;
		}else if(isNaN(quantity)){
			quantity = 1;
		}else if(parseInt(Number(quantity)) < 1 || parseInt(Number(quantity)) > 9999){
			quantity = 1;
		}else{
			if(quantity.charAt(0) == "0" || quantity.charAt(0) == 0){
				quantity = 1;
			}
		}
		$this.val(quantity);
	},
	
//	"lastSaleRecordSelect":function(){
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
//			$('#databody >tr').eq(-2).find("#quantityuArr").val();
//		}
//	},
	
	"changeDetailQuantity":function(promotionQuantity, activityCode){
		$.each($('#databody >tr'), function(i){
			if($(this).find("#activityTypeCode").val() == "CXHD" && $(this).find("#productVendorIDArr").val() != "HDZD"){
				if($(this).find("#mainCode").val() == activityCode){
					var proQuantity = $(this).find("#groupQuantity").val();
					var newProQuantity = Number(proQuantity) * Number(promotionQuantity);
					$(this).find("#spanQuantity").text(newProQuantity);
					$(this).find("#quantityuArr").val(newProQuantity);
					// 计算折扣后金额
					var realPrice = $(this).find("#realPriceArr").val();
					if(realPrice == undefined || realPrice == ""){
						realPrice = "0.00";
					}
					// 计算折扣后金额
					var amount = Number(newProQuantity) * Number(realPrice);
					// 显示折扣后金额
					$(this).find("#tdAmount").text(amount.toFixed(2));
					$(this).find("#payAmount").val(amount.toFixed(2));
				}
			}
		});
	},
	
	// 更改销售数量时触发的方法
	"changeQuantity":function(obj){
		BINOLWPSAL02.clearActionClass();
		
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var $thisVal =$this.val().toString();
		//添加- =两个键的快捷键操作(用于增减数量)
		if($thisVal == "-"){
			var $btnLessen=$this.parent().find("#btnLessen");
			BINOLWPSAL02.lessenQuantity($btnLessen);
			return false;
		}else if($thisVal == "="){
			var $btnAdd=$this.parent().find("#btnAdd");
			BINOLWPSAL02.addQuantity($btnAdd);
			return false;
		}
		var activityCode = $thisTr.find("#mainCode").val();
		
		$thisTr.removeClass('warningTRbgColor');
		if($thisVal == ""){
			$this.val("1");
			var recType = $thisTr.find("#productVendorIDArr").val();
			if(recType == "HDZD"){
				var promotionQuantity = $this.val().toString();
				var groupQuantity = $thisTr.find("#groupQuantity").val();
				if(isNaN(promotionQuantity)){
					promotionQuantity = 0;
				}
				if(isNaN(groupQuantity)){
					groupQuantity = 0;
				}
				var newQuantity = Number(promotionQuantity) * Number(groupQuantity);
				$thisTr.find("#quantityuArr").val(newQuantity);
				
				// 更改明细记录数量
				BINOLWPSAL02.changeDetailQuantity(promotionQuantity, activityCode);
			}
		}else if(isNaN($thisVal)){
			$this.val("1");
			var recType = $thisTr.find("#productVendorIDArr").val();
			if(recType == "HDZD"){
				var promotionQuantity = $this.val().toString();
				var groupQuantity = $thisTr.find("#groupQuantity").val();
				if(isNaN(promotionQuantity)){
					promotionQuantity = 0;
				}
				if(isNaN(groupQuantity)){
					groupQuantity = 0;
				}
				var newQuantity = Number(promotionQuantity) * Number(groupQuantity);
				$thisTr.find("#quantityuArr").val(newQuantity);
				
				// 更改明细记录数量
				BINOLWPSAL02.changeDetailQuantity(promotionQuantity, activityCode);
			}
		}else{
			var memberLevel = $("#memberLevel").val();
			var memberCode = $("#memberCode").val();
			var useMemberPrice = $("#useMemberPrice").val();
			var firstBillPrice = $("#firstBillPrice").val();
			var isFirstBill = $("#isFirstBill").val();
			var price = $thisTr.find("#priceUnitArr").val();
			if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
				if(isFirstBill == "Y"){
					if(firstBillPrice == "Y"){
						price = $thisTr.find("#memberPrice").val();
					}
				}else{
					if(memberLevel=="2" && $("#isPlatinumPrice").val()=="Y"){
						price = $thisTr.find("#platinumPrice").val();
					}else {
						price = $thisTr.find("#memberPrice").val();
					}
				}
			}
			if(price == undefined || price == ""){
				price = "0.00";
			}
			var quantity = parseInt(Number($this.val()));
			if(quantity > 0){
				if($thisVal.toString() != quantity.toString()){
					$this.val(quantity);
				}
			}else{
				$this.val("1");
			}
			var recType = $thisTr.find("#productVendorIDArr").val();
			if(recType == "HDZD"){
				var promotionQuantity = $this.val().toString();
				var groupQuantity = $thisTr.find("#groupQuantity").val();
				if(isNaN(promotionQuantity)){
					promotionQuantity = 0;
				}
				if(isNaN(groupQuantity)){
					groupQuantity = 0;
				}
				var newQuantity = Number(promotionQuantity) * Number(groupQuantity);
				$thisTr.find("#quantityuArr").val(newQuantity);
				// 更改明细记录数量
				BINOLWPSAL02.changeDetailQuantity(promotionQuantity, activityCode);
				quantity = newQuantity;
				// 计算还可以使用的积分 
				var changablePoint = BINOLWPSAL02.calcuChangablePoint();
				if(changablePoint < 0){
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"积分不足", 
						type:"MESSAGE", 
						focusEvent:function(){
							$this.focus();
						}
					});
					$this.val("1");
					var newQuantity = 1 * Number(groupQuantity);
					$thisTr.find("#quantityuArr").val(newQuantity);
					// 更改明细记录数量
					BINOLWPSAL02.changeDetailQuantity(1, activityCode);
					quantity = newQuantity;
				}
			}
			// 折扣前金额
			var noDiscountAmount = Number(price) * Number(quantity);
			$thisTr.find("#noDiscountAmount").val(noDiscountAmount.toFixed(2));
		}
		if($thisTr.find("#activityTypeCode").val() != "CXHD"){
			// 计算折扣金额
			BINOLWPSAL02.calcuRealPrice(obj);
		}
		// 计算折扣后金额
		BINOLWPSAL02.calcuRealAmount(obj);
		// 计算总金额
		BINOLWPSAL02.calcuTatol();
	},
	
	// 更改折扣率时触发的方法
	"changeDiscountRate":function(obj){
		BINOLWPSAL02.clearActionClass();
		// 删除销售单据中的打折明细行
		BINOLWPSAL02.deleteDiscountRow();
		
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var $thisVal =$this.val().toString();
		
		var memberCode = $("#memberCode").val();
		var useMemberPrice = $("#useMemberPrice").val();
		var firstBillPrice = $("#firstBillPrice").val();
		var isFirstBill = $("#isFirstBill").val();
		var price = $thisTr.find("#priceUnitArr").val();
		if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
			if(isFirstBill == "Y"){
				if(firstBillPrice == "Y"){
					price = $thisTr.find("#memberPrice").val();
				}
			}else{
				price = $thisTr.find("#memberPrice").val();
			}
		}
		
		$thisTr.removeClass('warningTRbgColor');
		//是否允许售价高于原价
		var highPriceSal = $("#highPriceSal").val();
		if(highPriceSal == ""){
			highPriceSal = "N";
		}
		if(isNaN($thisVal)){
			$this.val("");
			$thisTr.find("#realPriceArr").val(price);
		}else if(Number($thisVal) < 0){
			$this.val("");
			$thisTr.find("#realPriceArr").val(price);
//			$('#errorDiv2 #errorSpan2').html($('#errorDiscountRate').val());
//			$('#errorDiv2').show();
		}else if(highPriceSal == "N" && Number($thisVal) >= 100){
			$this.val("");
			$thisTr.find("#realPriceArr").val(price);
		}else{
			if($thisVal != ""){
				var discountRate = Number($this.val()).toFixed(2);
				if($thisVal.toString() != discountRate.toString()){
					$this.val(discountRate);
				}
			}
			// 计算折扣后价格
			BINOLWPSAL02.calcuRealPrice(obj);	
		}
		// 计算折扣后金额
		BINOLWPSAL02.calcuRealAmount(obj);
		// 计算总金额
		BINOLWPSAL02.calcuTatol();
	},
	
	// 更改折扣后价格时触发的方法
	"changeRealPrice":function(obj){
		BINOLWPSAL02.clearActionClass();
		// 删除销售单据中的打折明细行
		BINOLWPSAL02.deleteDiscountRow();
		
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var $thisVal =$this.val().toString();
		$thisTr.removeClass('warningTRbgColor');
		
		var memberCode = $("#memberCode").val();
		var useMemberPrice = $("#useMemberPrice").val();
		var firstBillPrice = $("#firstBillPrice").val();
		var isFirstBill = $("#isFirstBill").val();
		var price = $thisTr.find("#priceUnitArr").val();
		if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
			if(isFirstBill == "Y"){
				if(firstBillPrice == "Y"){
					price = $thisTr.find("#memberPrice").val();
				}
			}else{
				price = $thisTr.find("#memberPrice").val();
			}
		}
		if(price == undefined || price == ""){
			price = "0.00";
		}
		//是否允许售价高于原价
		var highPriceSal = $("#highPriceSal").val();
		if(highPriceSal == ""){
			highPriceSal = "N";
		}
		if($thisVal == ""){
			$this.val(price);
			$thisTr.find("#discountRateArr").val("");
		}else if(isNaN($thisVal)){
			$this.val(price);
			$thisTr.find("#discountRateArr").val("");
		}else if(Number($thisVal) < 0){
			$this.val(price);
			$thisTr.find("#discountRateArr").val("");
//			$('#errorDiv2 #errorSpan2').html($('#errorDiscountPrice').val());
//			$('#errorDiv2').show();
		}else if(highPriceSal == "N" && Number($thisVal) > Number(price)){
			$this.val(price);
			$thisTr.find("#discountRateArr").val("");
		}else{
			var realPrice = parseFloat(Number($this.val())).toFixed(2);
			if($thisVal.toString() != realPrice.toString()){
				$this.val(realPrice);
			}
			// 计算折扣率
			BINOLWPSAL02.calcuDiscountRate(obj);
		}
		// 计算折扣后金额
		BINOLWPSAL02.calcuRealAmount(obj);
		// 计算总金额
		BINOLWPSAL02.calcuTatol();
	},
	
	// 折扣率数字变化时触发的方法
	"keyUpChangeDiscountRate":function(obj){
		var isDiscountFlag=$('#isDiscountFlag').val();
		if("N" == isDiscountFlag ){
			return false;
		}
		
		BINOLWPSAL02.clearActionClass();
		
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var $thisVal =$this.val().toString();
		
		var memberCode = $("#memberCode").val();
		var useMemberPrice = $("#useMemberPrice").val();
		var firstBillPrice = $("#firstBillPrice").val();
		var isFirstBill = $("#isFirstBill").val();
		var price = $thisTr.find("#priceUnitArr").val();
		if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
			if(isFirstBill == "Y"){
				if(firstBillPrice == "Y"){
					price = $thisTr.find("#memberPrice").val();
				}
			}else{
				price = $thisTr.find("#memberPrice").val();
			}
		}
		$thisTr.removeClass('warningTRbgColor');
		if($thisVal.indexOf(".")!=$thisVal.length){
			if(isNaN($thisVal)){
				$this.val("");
				$thisTr.find("#realPriceArr").val(price);
			}else if(Number($thisVal) < 0){
				$this.val("");
				$thisTr.find("#realPriceArr").val(price);
//				$('#errorDiv2 #errorSpan2').html($('#errorDiscountRate').val());
//				$('#errorDiv2').show();
			}else {
				//是否允许售价高于原价
				var highPriceSal = $("#highPriceSal").val();
				if(highPriceSal == ""){
					highPriceSal = "N";
				}
				if(highPriceSal == "N" && Number($thisVal) >= 100){
					$this.val("");
					$thisTr.find("#realPriceArr").val(price);
				}
			}
			// 计算折扣后价格
			BINOLWPSAL02.calcuRealPrice(obj);
			// 计算折扣后金额
			BINOLWPSAL02.calcuRealAmount(obj);
			// 计算总金额
			BINOLWPSAL02.calcuTatol();
		}
	},
	
	// 折扣价格数字变化时触发的方法
	"keyUpchangeRealPrice":function(obj){
		BINOLWPSAL02.clearActionClass();
		
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var $thisVal =$this.val().toString();
		
		var memberCode = $("#memberCode").val();
		var useMemberPrice = $("#useMemberPrice").val();
		var firstBillPrice = $("#firstBillPrice").val();
		var isFirstBill = $("#isFirstBill").val();
		var price = $thisTr.find("#priceUnitArr").val();
		if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
			if(isFirstBill == "Y"){
				if(firstBillPrice == "Y"){
					price = $thisTr.find("#memberPrice").val();
				}
			}else{
				price = $thisTr.find("#memberPrice").val();
			}
		}
		$thisTr.removeClass('warningTRbgColor');
		if($thisVal.indexOf(".")!=$thisVal.length){
			if(price == undefined || price == ""){
				price = "0.00";
			}
			if($thisVal == ""){
				$this.val(price);
				$thisTr.find("#discountRateArr").val("");
			}else if(isNaN($thisVal)){
				$this.val(price);
				$thisTr.find("#discountRateArr").val("");
			}else if(Number($thisVal) < 0){
				$this.val(price);
				$thisTr.find("#discountRateArr").val("");
//				$('#errorDiv2 #errorSpan2').html($('#errorDiscountPrice').val());
//				$('#errorDiv2').show();
			}else {
				//是否允许售价高于原价
				var highPriceSal = $("#highPriceSal").val();
				if(highPriceSal == ""){
					highPriceSal = "N";
				}
				if(highPriceSal == "N" && Number($thisVal) > Number(price)){
					$this.val("");
					$thisTr.find("#realPriceArr").val(price);
				}
			}
			// 计算折扣率
			BINOLWPSAL02.calcuDiscountRate(obj);
			// 计算折扣后金额
			BINOLWPSAL02.calcuRealAmount(obj);
			// 计算总金额
			BINOLWPSAL02.calcuTatol();
		}
	},
	
	// 计算折扣率
	"calcuDiscountRate":function(obj){
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		
		var memberCode = $("#memberCode").val();
		var useMemberPrice = $("#useMemberPrice").val();
		var firstBillPrice = $("#firstBillPrice").val();
		var isFirstBill = $("#isFirstBill").val();
		var price = $thisTr.find("#priceUnitArr").val();
		if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
			if(isFirstBill == "Y"){
				if(firstBillPrice == "Y"){
					price = $thisTr.find("#memberPrice").val();
				}
			}else{
				price = $thisTr.find("#memberPrice").val();
			}
		}
		var realPrice = $thisTr.find("#realPriceArr").val();
		if($thisTr.find("#activityTypeCode").val() != "CXHD"){
			if(price == undefined || price == ""){
				price = "0.00";
			}
			if(realPrice == undefined || realPrice == ""){
				realPrice = "0.00";
			}
			var isDiscountFlag=$('#isDiscountFlag').val();
			if("Y" == isDiscountFlag){
				// 计算折扣率
				if(price != "0" && price != "0.00"){
					if(realPrice != "0.00"){
						if(Number(realPrice) != Number(price)){
							var discountRate = Number(realPrice) / Number(price) * 100;
							// 显示折扣率
							$thisTr.find("#discountRateArr").val(Number(discountRate).toFixed(2));
						}else{
							$thisTr.find("#discountRateArr").val("");
						}
					}else{
						$thisTr.find("#discountRateArr").val("0.00");
					}
				}else{
					$thisTr.find("#discountRateArr").val("");
				}
			}else if("N" == isDiscountFlag){
				// 计算折扣率
				if(price != "0" && price != "0.00"){
					if(realPrice != "0.00"){
						if(Number(realPrice) != Number(price)){
							var discountRate = Number(realPrice) / Number(price) * 100;
							// 显示折扣率
							$thisTr.find("#discountRateArr").parent().find("span").text(Number(discountRate).toFixed(2)+"%");
							$thisTr.find("#discountRateArr").val(Number(discountRate).toFixed(2));
						}else{
							$thisTr.find("#discountRateArr").parent().find("span").text("");
							$thisTr.find("#discountRateArr").val("");
						}
					}else{
						$thisTr.find("#discountRateArr").parent().find("span").text("0.00%");
						$thisTr.find("#discountRateArr").val("0.00");
					}
				}else{
					$thisTr.find("#discountRateArr").parent().find("span").text("");
					$thisTr.find("#discountRateArr").val("");
				}
			}
		}
	},
	
	// 计算折扣后实际价格
	"calcuRealPrice":function(obj){
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var memberCode = $("#memberCode").val();
		var useMemberPrice = $("#useMemberPrice").val();
		var firstBillPrice = $("#firstBillPrice").val();
		var isFirstBill = $("#isFirstBill").val();
		var memberLevel = $("#memberLevel").val();
		var price = $thisTr.find("#priceUnitArr").val();
		if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
			if(isFirstBill == "Y"){
				if(firstBillPrice == "Y"){
					if(memberLevel == "2" && $("#isPlatinumPrice").val()=="Y"){
						price=$thisTr.find("#platinumPrice").val();
					}else {
						price = $thisTr.find("#memberPrice").val();
					}
				}
			}else{
				if(memberLevel == "2" && $("#isPlatinumPrice").val()=="Y"){
					price=$thisTr.find("#platinumPrice").val();
				}else {
					price = $thisTr.find("#memberPrice").val();
				}
			}
		}
		var discountRate = $thisTr.find("#discountRateArr").val();
		if(price == undefined || price == ""){
			price = "0.00";
		}
		if(discountRate == undefined || discountRate == ""){
			discountRate = "100.00";
		}
		// 计算折扣后金额
		var discountPrice = Number(price) * (Number(discountRate) / 100);
		// 显示折扣后金额
		$thisTr.find("#realPriceArr").val(discountPrice.toFixed(2));
	},
	
	// 计算折扣后实际金额
	"calcuRealAmount":function(obj){
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var quantity = $thisTr.find("#quantityuArr").val();
		var realPrice = $thisTr.find("#realPriceArr").val();
		var recType = $thisTr.find("#productVendorIDArr").val();
		if(recType == "HDZD"){
			quantity = $thisTr.find("#promotionQuantity").val();
		}
		if(quantity == undefined || quantity == ""){
			quantity = "0";
		}
		if(realPrice == undefined || realPrice == ""){
			realPrice = "0.00";
		}
		// 计算折扣后金额
		var amount = Number(quantity) * Number(realPrice);
		// 显示折扣后金额
		$thisTr.find("#tdAmount").text(amount.toFixed(2));
		$thisTr.find("#payAmount").val(amount.toFixed(2));
	},
	
	/**
	 * 计算总数量，总金额
	 * */
	"calcuTatol":function(){
		var rows = $("#databody").children();
		// 普通销售根据会员等级计算金额
		var memberLevel = $("#memberLevel").val();
		var totalQuantity = 0;
		var totalAmount = 0.00;
		var originalAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
				var proTypeVal = $(this).find("#activityTypeCode").val();
				var recTypeVal = $(this).find("#productVendorIDArr").val();
				
				if(proTypeVal == "CXHD"){
					if(recTypeVal == "HDZD"){
						// 计算实际数量
						var $inputVal=$(this).find("#quantityuArr").val();
						if(isNaN($inputVal)){
							$inputVal=0;
						}
						totalQuantity += Number($inputVal);
						// 计算实际金额
						var $tdVal=$(this).find("#tdAmount").text();
						if(isNaN($tdVal)){
							$tdVal=0.00;
						}
						totalAmount += Number($tdVal);
						// 计算原金额
						var $tdVal=$(this).find("#tdAmount").text();
						if(isNaN($tdVal)){
							$tdVal=0.00;
						}
						originalAmount += Number($tdVal);
					}
				}else{
					// 计算实际数量
					var $inputVal=$(this).find("#quantityuArr").val();
					if(isNaN($inputVal)){
						$inputVal=0;
					}
					totalQuantity += Number($inputVal);
					// 计算实际金额
					var $tdVal=$(this).find("#tdAmount").text();
					if(isNaN($tdVal)){
						$tdVal=0.00;
					}
					totalAmount += Number($tdVal);
					// 计算原金额
					if($(this).find("#activityTypeCode").val() != "ZDZK" && $(this).find("#activityTypeCode").val() != "ZDQL"){
						var $tdVal=$(this).find("#tdAmount").text();
						if(isNaN($tdVal)){
							$tdVal=0.00;
						}
						originalAmount += Number($tdVal);
					}
				}
			});
		}
		$("#spanTotalQuantity").html(Number(totalQuantity).toFixed(0));
		$("#spanTotalAmount").html(Number(totalAmount).toFixed(2));
		$("#totalQuantity").val(Number(totalQuantity).toFixed(0));
		$("#totalAmount").val(Number(totalAmount).toFixed(2));
		$("#originalAmount").val(Number(originalAmount).toFixed(2));
	},
	
	// 计算按会员价购买的价格和金额
	"calcuMemberSaleAmout":function(){
		//检查有无行
		if($('#databody >tr').length > 0){
			$.each($('#databody >tr'), function(i){
				// 跳过折扣行
				if($(this).find("#activityTypeCode").val() != "ZDZK" && $(this).find("#activityTypeCode").val() != "ZDQL" && $(this).find("#activityTypeCode").val() != "CXHD"){
					if($(this).find("#productVendorIDArr").val()!=""){
						var $obj = $(this).find("#discountRateArr");
						// 计算折扣后价格
						BINOLWPSAL02.calcuRealPrice($obj);
						// 计算折扣后金额
						BINOLWPSAL02.calcuRealAmount($obj);
					}
				}
			});
			// 计算总金额
			BINOLWPSAL02.calcuTatol();
		}
	},
	
	/**
	 * 删除选中行
	 */
	"deleteRow":function(obj){
		var $this = $(obj);
		var $thisTr = $this.parent().parent().parent();
		// 移除提示样式与信息
		BINOLWPSAL02.clearActionClass();
		// 移除选中行
		$thisTr.remove();
		// 判断删除的数据是否为折扣数据，若不是折扣数据则同时删除折扣数据
		if($thisTr.find("#activityTypeCode").val() != "ZDZK" && $thisTr.find("#activityTypeCode").val() != "ZDQL"){
			if($thisTr.find("#activityTypeCode").val() == "CXHD"){
				var mainCode = $thisTr.find("#mainCode").val();
				// 删除指定的促销活动行
				BINOLWPSAL02.deletePromotionRow(mainCode);
				// 移除对应选择框选中状态
				var $thisCheckBox = $("#promotionData").find("#ckPromotion:[value='"+ mainCode +"']");
				if(!!$thisCheckBox.attr("checked")){
					$thisCheckBox.removeAttr("checked");
				}
				// 设置单据分类
				BINOLWPSAL02.setBillClassify();
			}
			// 删除销售单据中的打折明细行
			BINOLWPSAL02.deleteDiscountRow();
			// 计算总金额
			BINOLWPSAL02.calcuTatol();
		}else{
			// 重新计算总金额
			BINOLWPSAL02.calcuTatol();
		}
		// 重新设置间隔行样式
		BINOLWPSAL02.changeOddEvenColor();
		
		var memberCode = $("#memberCode").val();
		if(memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
			// 行数大于10行时查询会员显示在记录行下部，小于10时显示在页面底部
			if($('#databody >tr').length >= 10){
				$("#memberPageDiv").attr("class","wp_bottom2");
				$("#memberContentDiv").removeAttr("class");
				$("#memberBoxDiv").removeAttr("class");
			}else{
				$("#memberPageDiv").attr("class","wp_bottom");
				$("#memberContentDiv").attr("class","wp_content_b");
				$("#memberBoxDiv").attr("class","wp_leftbox");
			}
		}
		// 检查是否还有有效数据行，若没有则收款和折扣按钮不可用
		if(!BINOLWPSAL02.checkData()){
			$("#btnCollect").attr("disabled",true);
			$("#btnDiscount").attr("disabled",true);
			$("#btnSearchBills").removeAttr("disabled");
			$("#btnCollect").attr("class","btn_top_disable");
			$("#btnDiscount").attr("class","btn_top_disable");
			$("#btnSearchBills").attr("class","btn_top");
		}
		// 判断删除的是否为最后一行数据，若为最后一行数据则新增一个空行，若不是最后一行数据则更新序号
		if($('#databody >tr').length <= 0){
			BINOLWPSAL02.addNewLine();
		}else{
			// 最后一行第一个可见的文本框获得焦点
			BINOLWPSAL02.firstInputSelect();
			// 更新序号
			var indexNo = 1;
			var rows = $("#databody").children();
			if(rows.length > 0){
				rows.each(function(i){
					if($(this).find("#indexNo").length > 0){
						$(this).find("#indexNo").text(indexNo);
						$("#rowCode").val(indexNo);
						indexNo++;
					}
				});
			}
		}
	},
	
	// 删除折扣行*
	"deleteDiscountRow":function(){
		if($('#databody >tr').length > 0){
			$.each($('#databody >tr'), function(i){
				if($(this).find("#activityTypeCode").val() == "ZDZK" || $(this).find("#activityTypeCode").val() == "ZDQL"){
					$(this).remove();
				}
			});
		}
	},
	
	// 删除指定促销记录行
	"deletePromotionRow":function(activityCode){
		if($('#databody >tr').length > 0){
			$.each($('#databody >tr'), function(i){
				if($(this).find("#mainCode").val() == activityCode){
					$(this).remove();
				}
			});
		}
		// 重新计算总金额
		BINOLWPSAL02.calcuTatol();
		// 重新设置间隔行样式
		BINOLWPSAL02.changeOddEvenColor();
		
		var memberCode = $("#memberCode").val();
		if(memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
			// 行数大于10行时查询会员显示在记录行下部，小于10时显示在页面底部
			if($('#databody >tr').length >= 10){
				$("#memberPageDiv").attr("class","wp_bottom2");
				$("#memberContentDiv").removeAttr("class");
				$("#memberBoxDiv").removeAttr("class");
			}else{
				$("#memberPageDiv").attr("class","wp_bottom");
				$("#memberContentDiv").attr("class","wp_content_b");
				$("#memberBoxDiv").attr("class","wp_leftbox");
			}
		}
		
		// 检查是否还有有效数据行，若没有则收款和折扣按钮不可用
		if(!BINOLWPSAL02.checkData()){
			$("#btnCollect").attr("disabled",true);
			$("#btnDiscount").attr("disabled",true);
			$("#btnSearchBills").removeAttr("disabled");
			$("#btnCollect").attr("class","btn_top_disable");
			$("#btnDiscount").attr("class","btn_top_disable");
			$("#btnSearchBills").attr("class","btn_top");
		}
		// 判断删除的是否为最后一行数据，若为最后一行数据则新增一个空行，若不是最后一行数据则更新序号
		if($('#databody >tr').length <= 0){
			BINOLWPSAL02.addNewLine();
		}else{
			// 最后一行第一个可见的文本框获得焦点
			$('#databody').find("tr:last").find("input:text:visible:first").focus();
			// 更新序号
			var indexNo = 1;
			var rows = $("#databody").children();
			if(rows.length > 0){
				rows.each(function(i){
					if($(this).find("#indexNo").length > 0){
						$(this).find("#indexNo").text(indexNo);
						$("#rowCode").val(indexNo);
						indexNo++;
					}
				});
			}
		}
	},
	
	// 删除除折扣行外的所有空行
	"deleteEmptyRow":function(){
		BINOLWPSAL02.clearActionClass();
		//检查有无行
		if($('#databody >tr').length > 0){
			$.each($('#databody >tr'), function(i){
				// 跳过折扣行
				if($(this).find("#activityTypeCode").val() != "ZDZK" && $(this).find("#activityTypeCode").val() != "ZDQL"){
					if($(this).find("#productVendorIDArr").val()==""){
						//移除空白行
						$(this).remove();
					}
				}
			});
		}
	},
	
	"addQuantity":function(obj){
		// 删除销售单据中的打折明细行
		BINOLWPSAL02.deleteDiscountRow();
		var $this = $(obj);
		var promotionQuantity = $this.parent().find("#promotionQuantity").val();
		var groupQuantity = $this.parent().find("#groupQuantity").val();
		var quantity = $this.parent().find("#quantityuArr").val();
		var recType = $this.parent().parent().find("#productVendorIDArr").val();
		var activityCode = $this.parent().parent().find("#mainCode").val();
//		var stockType=$('#stockType').val();
		
		
		if(recType == "HDZD"){
			if(promotionQuantity < 10000){
				if(isNaN(promotionQuantity)){
					promotionQuantity = 0;
				}
				if(isNaN(groupQuantity)){
					groupQuantity = 0;
				}
				var newPromotionQuantity = Number(promotionQuantity) + 1;
				var newQuantity = newPromotionQuantity * Number(groupQuantity);
				$this.parent().find("#promotionQuantity").val(newPromotionQuantity);
				$this.parent().find("#quantityuArr").val(newQuantity);
				// 更改明细记录数量
				BINOLWPSAL02.changeDetailQuantity(newPromotionQuantity, activityCode);
				// 计算还可以使用的积分 
				var changablePoint = BINOLWPSAL02.calcuChangablePoint();
				if(changablePoint < 0){
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"积分不足", 
						type:"MESSAGE", 
						focusEvent:function(){
							$this.parent().find("#promotionQuantity").focus();
						}
					});
					$this.parent().find("#promotionQuantity").val(promotionQuantity);
					var orgQuantity = promotionQuantity * Number(groupQuantity);
					$this.parent().find("#quantityuArr").val(orgQuantity);
					// 更改明细记录数量
					BINOLWPSAL02.changeDetailQuantity(promotionQuantity, activityCode);
				}
			}
		}else{
			if(quantity < 10000){
				var newQuantity = Number(quantity) + 1;
				$this.parent().find("#quantityuArr").val(newQuantity);
			}
		}
		// 计算折扣后金额
		BINOLWPSAL02.calcuRealAmount(obj);
		// 计算总金额
		BINOLWPSAL02.calcuTatol();
	},
	
	"lessenQuantity":function(obj){
		// 删除销售单据中的打折明细行
		BINOLWPSAL02.deleteDiscountRow();
		var $this = $(obj);
		var promotionQuantity = $this.parent().find("#promotionQuantity").val();
		var groupQuantity = $this.parent().find("#groupQuantity").val();
		var quantity = $this.parent().find("#quantityuArr").val();
		var recType = $this.parent().parent().find("#productVendorIDArr").val();
		var activityCode = $this.parent().parent().find("#mainCode").val();
		
		if(recType == "HDZD"){
			if(promotionQuantity > 1){
				if(isNaN(promotionQuantity)){
					promotionQuantity = 0;
				}
				if(isNaN(groupQuantity)){
					groupQuantity = 0;
				}
				var newPromotionQuantity = Number(promotionQuantity) - 1;
				var newQuantity = newPromotionQuantity * Number(groupQuantity);
				$this.parent().find("#promotionQuantity").val(newPromotionQuantity);
				$this.parent().find("#quantityuArr").val(newQuantity);
				// 更改明细记录数量
				BINOLWPSAL02.changeDetailQuantity(newPromotionQuantity, activityCode);
			}
		}else{
			if(quantity > 1){
				var newQuantity = Number(quantity) - 1;
				$this.parent().find("#quantityuArr").val(newQuantity);
			}
		}
		// 计算折扣后金额
		BINOLWPSAL02.calcuRealAmount(obj);
		// 计算总金额
		BINOLWPSAL02.calcuTatol();
//		if(stockType == "N"){
//			
//			var getStockUrl = $("#getStockUrl").attr("href");
//			var productVendorIDArr=$("#productVendorIDArr").val();
//			var form_depotId=$('#form_depotId').val();
//			var form_logicinventId=$('#form_logicinventId').val();
//			var params1="productVendorIDArr="+productVendorIDArr+"&depotId="+form_depotId+"&logicinventId="+form_logicinventId;
//			var $quantity=$this.parent().find("#quantityuArr");
//			cherryAjaxRequest({
//				url: getStockUrl,
//				param: params1,
//				callback: function(data) {
//					if(quantity>data||data<=0){
//						$quantity.empty(); 
//						$quantity.append("<input id='quantity' type='hidden' value='"+data+"'></input>");
//						BINOLWPSAL02.showMessageDialog({
//							message:"对不起，所选商品库存不足", 
//							type:"MESSAGE", 
//							focusEvent:function(){
//								// 最后一行第一个可见的文本框获得焦点
//								BINOLWPSAL02.firstInputSelect();
//							}
//						});
//					}
//				}
//			});
			
//		}
		
	},
	
	"checkData":function(){
		BINOLWPSAL02.clearActionClass();
		var flag = true;
		//检查有无行
		if($('#databody >tr').length > 0){
			$.each($('#databody >tr'), function(i){
				// 跳过折扣行和促销行
				if($(this).find("#activityTypeCode").val() != "ZDZK" && $(this).find("#activityTypeCode").val() != "ZDQL") {
					if($(this).find("#activityTypeCode").val() != "CXHD"){
						//数量不能为空（至少有一条数量不为空也不等于0的明细）
						if($(this).find("#quantityuArr").val()=="" || $(this).find("#quantityuArr").val()=="0" || $(this).find("#productVendorIDArr").val()==""){
							if($(this).find("#productVendorIDArr").val()==""){
								//没有选择产品的情况下进行下面的处理
								if($('#databody >tr').length > 1){
									flag = true;
								}else{
									//行数小于或等于1的情况下保留空白行返回false
									flag = false;
									return flag;
								}
							}else{
								//选择了产品但数量输入不正确的情况下返回false
								flag = false;
								return flag;
							}
						}
					}else{
						if($(this).find("#productVendorIDArr").val()=="HDZD"){
							if($(this).find("#promotionQuantity").val()=="" || $(this).find("#promotionQuantity").val()=="0"){
								flag = false;
								return flag;
							}
						}
					}
				}
			});
		}else{
			flag = false;
		}
		return flag;
	},
	
	"checkBuyFlag":function(){
		var flag = false;
		//检查有无行
		if($('#databody >tr').length > 0){
			$.each($('#databody >tr'), function(i){
				// 跳过折扣行和促销行
				if($(this).find("#activityTypeCode").val() != "ZDZK" && $(this).find("#activityTypeCode").val() != "ZDQL" && $(this).find("#activityTypeCode").val() != "CXHD") {
					//数量不能为空（至少有一条数量不为空也不等于0的明细）
					if($(this).find("#quantityuArr").val()!="" && $(this).find("#quantityuArr").val()!="0" && $(this).find("#productVendorIDArr").val()!=""){
						flag = true;
					}
				}
			});
		}
		return flag;
	},
	
	"checkIsExchanged":function(){
		var flag = false;
		//检查有无行
		if($('#databody >tr').length > 0){
			$.each($('#databody >tr'), function(i){
				// 跳过折扣行和促销行
				if($(this).find("#activityTypeCode").val() != "ZDZK" && $(this).find("#activityTypeCode").val() != "ZDQL" && $(this).find("#activityTypeCode").val() != "CXHD") {
					if($(this).find("#isExchanged").val() == 0 || $(this).find("#isExchanged").val() == "0"){
						// 添加样式
						$(this).attr("class","errTRbgColor");
						flag = true;
					}
				}
			});
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
	// 保存原始购物车信息HTML（智能促销）
	"getSaleDetailList_promotion" :function(){
		var saleresult = "";
		var $msgBoxs = $("#databody");
		// 将明细列表记录转换成Json格式
		saleresult = Obj2JSONArr($msgBoxs.find("tr"));
		$("#saleDetailList_json").val(saleresult);
		var html=$("#databody").html();
		$("#saleDetailList_promotion").html(html);
	},
	"lastInputSelect":function(){
		// 获取最后一行
		var $lastTr = $('#databody').find("tr:last");
		// 判断最后一行数据是否为折扣数据或空行
		if(($lastTr.find("#productVendorIDArr").val() != undefined && $lastTr.find("#productVendorIDArr").val() != "") 
				&& ($lastTr.find("#activityTypeCode").val() != "ZDZK" && $lastTr.find("#activityTypeCode").val() != "ZDQL")){
			var showSaleRows = $("#showSaleRows").val();
			// 判断行数是否大于允许的行数
			if($('#databody >tr').length < Number(showSaleRows)){
				// 添加一个空行
				BINOLWPSAL02.addNewLine();
			}else{
				// 最后一个可见的文本框获得焦点
				$('#databody >tr').find("input:text:visible:last").select();
			}
		}else{
			// 最后一个可见的文本框获得焦点
			$('#databody >tr').find("input:text:visible:last").select();
		}
	},
	
	"firstInputSelect":function(){
		// 获取最后一行
		var $lastTr = $('#databody').find("tr:last");
		// 判断最后一行数据是否为折扣数据或空行
		if(($lastTr.find("#productVendorIDArr").val() != undefined && $lastTr.find("#productVendorIDArr").val() != "") 
				&& ($lastTr.find("#activityTypeCode").val() != "ZDZK" && $lastTr.find("#activityTypeCode").val() != "ZDQL")){
			var showSaleRows = $("#showSaleRows").val();
			// 判断行数是否大于允许的行数
			if($('#databody >tr').length < Number(showSaleRows)){
				// 添加一个空行
				BINOLWPSAL02.addNewLine();
			}else{
				// 最后一个可见的文本框获得焦点
				$('#databody >tr').find("input:text:visible:last").select();
			}
		}else{
			// 最后一行第一个可见的文本框获得焦点
			$('#databody >tr').find("input:text:visible:first").select();
		}
	},
	
	"clickBa":function(){
		if($("#baCode option").size() <= 1){
			// 显示提示信息
			BINOLWPSAL02.showMessageDialog({
				message:"现在没有当班的营业员，请先进行考勤", 
				type:"MESSAGE", 
				focusEvent:function(){
					// 最后一行第一个可见的文本框获得焦点
					BINOLWPSAL02.firstInputSelect();
				}
			});
		}
	},
	
	"changeBa":function(){
		// 最后一行第一个可见的文本框获得焦点
		BINOLWPSAL02.firstInputSelect();
	},
	//获取智能促销的列表
	"initMatchRule_CloudPos":function(){
		var buttonState=$("#btnCollect" ).attr("disabled" );
        if(buttonState == "disabled"){
              return ;
        }
        var baChooseModel = $("#baChooseModel").val();
		var dgOpenedState = $("#dgOpenedState").val();
		if(dgOpenedState != "Y"){
			if(!$('#mainForm').valid()) {
				// 显示提示信息
				if(baChooseModel == "2"){
					baInfoCode = $("#baCode option:selected").val();
				}else{
					baInfoCode = $("#baCode").val();
				}
				if(baInfoCode == null || baInfoCode == undefined || baInfoCode.toString().trim() == ""){
					BINOLWPSAL02.showMessageDialog({
						message:"请选择营业员", 
						type:"MESSAGE", 
						focusEvent:function(){
							if(baChooseModel == "2"){
								// 最后一行第一个可见的文本框获得焦点
								BINOLWPSAL02.firstInputSelect();
							}else{
								$("#baName").focus();
							}
						}
					});
				}
				return false;
			}
		}

		//-----------------------------------------------------------------------

		function smartPromotionDialog(){
			//获取云POS是否开启智能促销的配置项，没有开启智能促销的情况下自动跳转到收款
			var smartPromotion=$('#smartPromotion').val();

			if(smartPromotion == 'N'){
				//获取云POS负库存销售时，是否给于提示并且继续销售配置项
				var stockSaleType=$('#stockSaleType').val();
				if(stockSaleType == 'Y'){
					var checkSocketFlag = true;
					$.each($('#databody >tr'), function(i){
						var quantityuArr = $(this).find("#quantityuArr").val();
						var stockQuantity = $(this).find("#stockQuantity").val();
						var isStock =  $(this).find("#isStock").val();
						if(isStock != null && isStock == 1){
							if(stockQuantity != null && stockQuantity != undefined && stockQuantity.toString().trim() != ""){
								if(Number(stockQuantity) < Number(quantityuArr)){
									// 添加样式提醒
									$(this).attr("class","errTRbgColor");
									checkSocketFlag = false;
								}
							}
						}
					});
					if(!checkSocketFlag){

						BINOLWPSAL02.showMessageDialog({
							message:"部分产品库存不足，请销售完成后核实库存数量！",
							type:"MESSAGE",
							focusEvent:function(){
								$("#btnMessageConfirm .button-text").html("确认");
							}
						});
						$("#btnMessageConfirm .button-text").html("确认，继续完成销售");
					}
				}
				return false;
			}
			return true;
		}


		//柜台积分计划
		function executeLimitPlanDialog(){
			//获取云POS是否开启积分计划配置项，没有开启的情况下自动跳转到收款
			var isExecuteLimitPlan = $('#isExecuteLimitPlan').val();

			var dialogOpenFlag = false;

			if(isExecuteLimitPlan == '1'){

				var currentPoint = parseInt($("#currentPoint").val());
				var planStatus = $("#planStatus").val();
				var minWarningPoint = parseInt($("#minWarningPoint").val());

				if( planStatus == '1' ) {

					if (currentPoint <= minWarningPoint) {
						dialogOpenFlag = true;
						var $dialog = $('#messageDialogDiv2');
						$dialog.dialog({
							//默认不打开弹出框
							autoOpen: false,
							//弹出框宽度
							width: 500,
							//弹出框高度
							height: 250,
							//弹出框标题
							title:$("#messageDialogTitle").text(),
							//弹出框索引
							zIndex: 99,
							modal: true,
							resizable:false,
							type: "MESSAGE",
							//关闭按钮
							close: function() {
								closeCherryDialog("messageDialogDiv2");

							}
						});

						$dialog.dialog("open");

						$("#messageContent2").show();
						//您目前的可发放额度为XXX，目前已低于警戒线XXX，请在20天内订货补充额度，否则雅芳将终止您参与积分计划的权利！
						$("#messageContentSpan2").text("您目前的可发放额度为" + currentPoint + '，目前已低于警戒线' + minWarningPoint + '，请在20天内订货补充额度，否则雅芳将终止您参与积分计划的权利！');

						// 给确认按钮绑定事件
						$("#btnMessageConfirm2").bind("click", function(){
							closeCherryDialog("messageDialogDiv2");
							if( smartPromotionDialog() ){
								BINOLWPSAL02.actionSmartPromotion();
							}else{
								BINOLWPSAL02.collect();
							}

						});

					}
				}

			}

			if(dialogOpenFlag == false){
				if( smartPromotionDialog() ){
					BINOLWPSAL02.actionSmartPromotion();
				}else{
					BINOLWPSAL02.collect();
				}
			}

		}

		executeLimitPlanDialog();

	},
	"removePromotionList":function(){
		//替换数据
		var saleDetailList_json=$("#saleDetailList_json").val();
		if(saleDetailList_json == null || saleDetailList_json == "" || saleDetailList_json == undefined){
			return;
		}else{
			//替换购物车为第一次保存的购物车
			//替换原有的html
			$("#databody").html($("#saleDetailList_promotion").html());
			var param_map = eval("("+saleDetailList_json+")");
			$(param_map).each(function(i){
				var unitCode=param_map[i].unitCode;
				var quantityuArr=param_map[i].quantityuArr;
				var realPriceArr=param_map[i].realPriceArr;
				var discountRateArr=param_map[i].discountRateArr;
				$("#databody tr").each(function(){
					//对应到指定的购物车中的产品
					if($(this).find("input[name='unitCode']").val() == unitCode){
						$(this).find("#realPriceArr").val(realPriceArr);
						$(this).find("#discountRateArr").val(discountRateArr);
						$(this).find("#quantityuArr").val(quantityuArr);
					}
				});
			});
		}
		$("#btnCollect").removeAttr("disabled");
		$("#btnCollect").attr("class","btn_top");
		$("#btnHangBills").removeAttr("disabled");
		$("#btnHangBills").attr("class","btn_top");
		// 添加一个空行
		BINOLWPSAL02.addNewLine();
		BINOLWPSAL02.calcuTatol();
	
	},
	"collect":function(){
		var baChooseModel = $("#baChooseModel").val();
		var dgOpenedState = $("#dgOpenedState").val();
		if(dgOpenedState != "Y"){
			if(!$('#mainForm').valid()) {
				// 显示提示信息
				if(baChooseModel == "2"){
					baInfoCode = $("#baCode option:selected").val();
				}else{
					baInfoCode = $("#baCode").val();
				}
//				var baInfoCode = $("#baCode option:selected").val();
//				var baInfoCode = $("#baCode").val();
				if(baInfoCode == null || baInfoCode == undefined || baInfoCode.toString().trim() == ""){
					BINOLWPSAL02.showMessageDialog({
						message:"请选择营业员", 
						type:"MESSAGE", 
						focusEvent:function(){
							if(baChooseModel == "2"){
								// 最后一行第一个可见的文本框获得焦点
								BINOLWPSAL02.firstInputSelect();
							}else{
								$("#baName").focus();
							}
						}
					});
				}
				return false;
			}
			
			// 判断页面上的数量是否大于库存
			if($("#stockType").val() == "N" && $("#stockSaleType").val() == "N"){
				if($('#databody >tr').length > 0){
					var checkSocketFlag = true;
					$.each($('#databody >tr'), function(i){
						var quantityuArr = $(this).find("#quantityuArr").val();
						var stockQuantity = $(this).find("#stockQuantity").val();
						var isStock =  $(this).find("#isStock").val();
						if(isStock != null && isStock == 1){
							if(stockQuantity != null && stockQuantity != undefined && stockQuantity.toString().trim() != ""){
								if(Number(stockQuantity) < Number(quantityuArr)){
									// 添加样式提醒
									$(this).attr("class","errTRbgColor");
									checkSocketFlag = false;
								}
							}
						}
					});
					if(!checkSocketFlag){
						BINOLWPSAL02.showMessageDialog({
							message:"销售列表中存在库存不足的商品", 
							type:"MESSAGE", 
							focusEvent:function(){
								// 最后一行第一个可见的文本框获得焦点
								BINOLWPSAL02.firstInputSelect();
							}
						});
						return false;
					}
				}
			}
			
			// 判断折扣是否在系统设置允许的范围内
			if($('#databody >tr').length > 0){
				var checkDiscountFlag = true;
				var minDiscount = $("#minDiscount").val();
				$.each($('#databody >tr'), function(i){
					var activityType = $(this).find("#activityTypeCode").val();
					var discountRate = $(this).find("#discountRateArr").val();
					if(activityType != "CXHD"){
						if(discountRate != null && discountRate != undefined && discountRate.toString().trim() != ""){
							if(Number(discountRate) < Number(minDiscount)){
								// 添加样式提醒
								$(this).attr("class","errTRbgColor");
								checkDiscountFlag = false;
							}
						}
					}
				});
				if(!checkDiscountFlag){
					BINOLWPSAL02.showMessageDialog({
						message:"存在低于系统设置允许的最低折扣率的记录", 
						type:"MESSAGE", 
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
					return false;
				}
			}
			// 如果是活动判断兑换的礼品是否达到合理的金额
			var billClassify = $("#billClassify").val();
			if(billClassify == "DHHD" || billClassify == "CXHD"){
				var checkQuantity = true;
				var checkFlag = true;
				var getProductFlag = false;
				var realTotalAmount = $("#totalAmount").val();
				// 活动为促销活动的情况下直接将判断是否包含正常商品的标识设为true，不做是否包含正常商品的限制
				if(billClassify == "CXHD"){
					getProductFlag = true;
				}
				if($('#databody >tr').length > 0){
					$.each($('#databody >tr'), function(i){
						var proTypeVal = $(this).find("#activityTypeCode").val();
						var recTypeVal = $(this).find("#productVendorIDArr").val();
						if(proTypeVal == "CXHD"){
							if(recTypeVal == "HDZD"){
								var proQuantity = $(this).find("#promotionQuantity").val();
								var activityCode = $(this).find("#mainCode").val();
								// 检查主单数量与明细记录数量是否一致
								$.each($('#databody >tr'), function(i){
									if($(this).find("#mainCode").val() == activityCode){
										if($(this).find("#productVendorIDArr").val() != "HDZD"){
											// 实际数量
											var realQuantity=$(this).find("#quantityuArr").val();
											if(isNaN(realQuantity) || realQuantity==""){
												realQuantity = 0;
											}
											// 组数量
											var groupQuantity=$(this).find("#groupQuantity").val();
											if(isNaN(groupQuantity) || groupQuantity=="" || groupQuantity=="0"){
												groupQuantity = 1;
											}
											// 输入数量
											var quantity = Number(realQuantity)/Number(groupQuantity);
											if(quantity != proQuantity){
												checkQuantity = false;
												return;
											}
										}
									}
								});
								if(checkQuantity){
									// 获取单个活动的礼品价格
									var proPrice = $(this).find("#priceUnitArr").val();
									if(Number(proPrice) < 0){
										if(Number(realTotalAmount) < Number(proPrice)){
											checkFlag = false;
											return;
										}
									}
								}else{
									return;
								}
							}else{
								var proType = $(this).find("#proType").val();
								// 兑换活动中设置了正常商品的情况下将判断是否包含正常商品的标识设为true
								if(proType != "P"){
									getProductFlag = true;
								}
							}
						}else{
							// 记录中未标记为促销的情况下说明该记录为正常商品，将判断是否包含正常商品的标识设为true
							if(recTypeVal != null && recTypeVal != undefined && recTypeVal != "")
							{
								getProductFlag = true;
							}
						}
					});
				}
				if(!checkQuantity){
					BINOLWPSAL02.showMessageDialog({
						message:"活动数据检查发现异常，请尝试重新选择活动记录", 
						type:"MESSAGE", 
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
					return false;
				}
				if(!checkFlag){
					BINOLWPSAL02.showMessageDialog({
						message:"使用的促销或兑换活动过多，请增加产品数量或减少促销活动数量", 
						type:"MESSAGE", 
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
					return false;
				}

				//表示需要限定产品，此时只有可以参与积分兑换的产品才能进行积分兑换活动
				if((isLimitProduct == 1 || isLimitProduct == "1")) {
					if (!getProductFlag) {
						BINOLWPSAL02.showMessageDialog({
							message: "积分兑换活动单据中必须包含要兑换的正常商品",
							type: "MESSAGE",
							focusEvent: function () {
								// 最后一行第一个可见的文本框获得焦点
								BINOLWPSAL02.firstInputSelect();
							}
						});
						return false;
					}
				}
			}else{
				//表示需要限定产品，此时只有可以参与积分兑换的产品才能进行积分兑换活动
				if((isLimitProduct == 1 || isLimitProduct == "1")) {
					if ($('#databody >tr').length > 0) {
						var checkActivitySign = true;
						$.each($('#databody >tr'), function (i) {
							var activitySignVal = $(this).find("#activitySign").val();
							if (activitySignVal == "DHHD") {
								checkActivitySign = false;
								return;
							}
						});
						if (!checkActivitySign) {
							BINOLWPSAL02.showMessageDialog({
								message: "单据检查发现异常，请尝试重新选择活动",
								type: "MESSAGE",
								focusEvent: function () {
									// 最后一行第一个可见的文本框获得焦点
									BINOLWPSAL02.firstInputSelect();
								}
							});
							return false;
						}
					}
				}
			}
			
			if(BINOLWPSAL02.checkData()){
				// 更改按钮样式
				$thisBtn = $("#btnCollect");
				BINOLWPSAL02.changeButtonCss($thisBtn);
				$("#btnReturnsGoods").attr("disabled",true);
				$("#btnReturnsGoods").attr("class","btn_top_disable");
				$("#btnAddHistoryBill").attr("disabled",true);
				$("#btnAddHistoryBill").attr("class","btn_top_disable");
				// 禁用清空购物车按钮
				$("#btnEmptyShoppingCart").attr("disabled",true);
				$("#btnEmptyShoppingCart").attr("class","btn_top_disable");
				// 删除空行
				BINOLWPSAL02.deleteEmptyRow();
				
				var baCode = "";
				var baName = "";
				if(baChooseModel == "2"){
					baCode = $("#baCode option:selected").val();
					baName = $("#baCode option:selected").text();
				}else{
					baCode = $("#baCode").val();
					baName = $("#baName").val();
				}
				var memberCode = $("#memberCode").val();
				var totalAmount = $("#totalAmount").val();
				var conditionAmount = $("#conditionAmount").val();
				var saleType = $("#saleType").val();
				var showAddMember = false;
				// 检查条件，确定是否需要显示会员入会页面
				if(saleType == "NS"){
					if(memberCode == null || memberCode == undefined || memberCode.toString().trim() == ""){
						if(conditionAmount != undefined && conditionAmount.trim() != ""){
							if(Number(totalAmount) >= Number(conditionAmount)){
								showAddMember = true;
							}
						}
					}
				}
				// 判断是否显示会员入会页面
				if(showAddMember){
					var dialogSetting = {
						dialogInit: "#dialogInit",
						width: 1000,
						height: 500,
						title: $("#addMemberDialogTitle").text(),
						closeEvent:function(){
							//清空之前的促销列表
							BINOLWPSAL02.removePromotionList();
							// 还原按钮样式
							$("#btnCollect").attr("class","btn_top");
							// 增加新行
							BINOLWPSAL02.firstInputSelect();
							// 移除弹出窗口
							removeDialog("#dialogInit");
							// 解除退货和补登按钮禁用
							$("#btnReturnsGoods").removeAttr("disabled");
							$("#btnReturnsGoods").attr("class","btn_top");
							$("#btnAddHistoryBill").removeAttr("disabled");
							$("#btnAddHistoryBill").attr("class","btn_top");
							// 解除清空购物车禁用
							$("#btnEmptyShoppingCart").removeAttr("disabled");
							$("#btnEmptyShoppingCart").attr("class","btn_top");
							// 会员查询输入框获得焦点
							$("#searchStr").focus();
						}
					};
					openDialog(dialogSetting);
					
					var addMemberUrl = $("#addMemberUrl").attr("href");
					var isMemberSaleFlag=$("#isMemberSaleFlag").val();
					var params = "viewType=COL&baCode="+ baCode +"&baName="+ baName+"&isMemberSaleFlag="+isMemberSaleFlag;
					cherryAjaxRequest({
						url: addMemberUrl,
						param: params,
						callback: function(data) {
							$("#dialogInit").html(data);
						}
					});
				}else{
					// 将销售明细列表转换成Json字符串
					BINOLWPSAL02.getSaleDetailList();
					var paymentSizeUrl = $("#paymentSizeUrl").attr("href");
					cherryAjaxRequest({
						url: paymentSizeUrl,
						param: null,
						callback: function(data) {
							var isCA = $("#isCA").val();
							if(Number(data)<1 && "Y"==isCA){
								BINOLWPSAL02.directPayment();
							}else {
								var dialogSetting = {
									dialogInit: "#dialogInit",
									width: 700,
									height: 500,
									title: $("#collectDialogTitle").text(),
									closeEvent:function(){
										//清空之前的促销列表
										BINOLWPSAL02.removePromotionList();
										// 还原按钮样式
										$("#btnCollect").attr("class","btn_top");
										// 最后一个文本框获得焦点
										BINOLWPSAL02.lastInputSelect();
										// 可见文本框回车事件解绑
										$("#collectPageDiv").find("input:text:visible").unbind();
										// 关闭弹出窗口
										removeDialog("#dialogInit");
										// 解除退货和补登按钮禁用
										$("#btnReturnsGoods").removeAttr("disabled");
										$("#btnReturnsGoods").attr("class","btn_top");
										$("#btnAddHistoryBill").removeAttr("disabled");
										$("#btnAddHistoryBill").attr("class","btn_top");
										// 解除清空购物车禁用
										$("#btnEmptyShoppingCart").removeAttr("disabled");
										$("#btnEmptyShoppingCart").attr("class","btn_top");
									}
								};
								openDialog(dialogSetting);
								
								var counterCode = $("#counterCode").val();
								var memberName = $("#memberName").val();
								if(memberName == null || memberName == undefined || memberName.trim() == ""){
									memberName = memberCode;
								}
								var collectUrl = $("#collectUrl").attr("href");
								var params = "collectPageType=NSSR&counterCode=" + counterCode + "&baName=" + baName 
										+ "&memberName=" + memberName + "&totalAmount=" +　totalAmount + "&memberCode=" + memberCode;
								cherryAjaxRequest({
									url: collectUrl,
									param: params,
									callback: function(data) {
										$("#dialogInit").html(data);
										/*var czkPay = $("#NEW_CZK_PAY").val();
										var czkType = $("#czkType").val();
										if(czkPay=="N"){
											$("#serverPay").hide();
										}else if(czkType!="2"){
											$("#serverPay").hide();
										}*/
									}
								});
							}
						}
					});
				}
			}
		}
	},
	"directPayment":function(){
		// 关闭弹出窗口
		removeDialog("#dialogInit");
		var params = $("#mainForm").serialize();
		var memberCode = $("#memberCode").val();
		var cardCode = $("#cardCode").val();
		if(cardCode!="" && cardCode!=undefined && cardCode!=null){
			params+="&cardCode="+cardCode;
		}
		var salCollectUrl = $("#salCollectUrl").attr("href");
		cherryAjaxRequest({
			url:salCollectUrl,
			param:params,
			callback:function(data){
				// 还原按钮样式
				$("#btnConfirm").removeAttr("disabled");
				$("#btnCollect").attr("class","btn_top");
				if(data != "ERROR" && data != ""){
					// 取得当前销售单ID
					$("#print_param_hide").find("#billId").val(data);
					var saleUrl = $("#saleUrl").attr("href");
					var counterCode = $("#counterCode").val();
					var param = "counterCode=" + counterCode;
					cherryAjaxRequest({
						url: saleUrl,
						param: param,
						callback: function(data1) {
							// 加载页面
							$("#webpos_main").html(data1);
							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"销售单已录入成功", 
								type:"SUCCESS", 
								focusEvent:function(){
									var baChooseModel = $("#baChooseModel").val();
									if(baChooseModel == "2"){
										// 最后一行第一个可见的文本框获得焦点
										BINOLWPSAL02.firstInputSelect();
									}else{
										$("#baName").focus();
									}
									var autoPrintBill = $("#autoPrintBill").val();
									// 获取配置确定是否需要打印小票
									if(autoPrintBill == "Y"){
										// 打印小票，参数0：销售，1：补打小票，2：退货
										printWebPosSaleBill("0");
									} else if(autoPrintBill == "ZJ") {
										var billCode = $("#billCode").val();
										var detailPrintHtml = $("#salePrintForm").find("#detailPrint");
										var originalAmountPrint = 0.0;
										var discountAmountPrint = 0.0;
										var sumQuantityPrint = 0;
										var sumAmountPrint = 0.0;
										// 整单去零
										var removeZero = 0.0;
										
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
											// 商品名称
											var productName = json[one].productNameArr;
											// 获取产品ID用于判断是否为活动主记录
											var recType = json[one].productVendorIDArr;
											var quantity = 0;
											if('HDZD' != recType && "-9999" != recType) {
												quantity = json[one].quantityuArr;
											} else {
												quantity = json[one].promotionQuantity;
											}
											// 原价
											var priceUnitArr = json[one].priceUnitArr;
											// 实价
											var realPriceArr = json[one].realPriceArr;
											// 折扣 ----原价-实价
											var discountRateArr = parseFloat(priceUnitArr) - parseFloat(realPriceArr);
											// 实收
											var payAmount = json[one].payAmount;
											
											// 将明细设置到打印页面
											detailPrintHtml.append('<tr><td colspan="5" height="10" class="left" style="border:solid 0px black"><font size="2px">'+productName+'</font></td></tr>');
											detailPrintHtml.append('<tr><td width="40%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
													+unitCode+'</font></td><td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
													+quantity+'</font></td><td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
													+priceUnitArr+'</font></td><td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
													+discountRateArr.toFixed(2)+'</font></td><td width="15%" height="10" class="left" style="border:solid 0px black"><font size="2px">'
													+payAmount+'</font></td></tr>');
											
											sumQuantityPrint = Number(sumQuantityPrint) + Number(quantity);
											sumAmountPrint = parseFloat(sumAmountPrint) + parseFloat(payAmount);
											originalAmountPrint = parseFloat(originalAmountPrint) + parseFloat(priceUnitArr) * parseFloat(quantity);
											discountAmountPrint = parseFloat(discountAmountPrint) + parseFloat(discountRateArr) * parseFloat(quantity);
											
										}
										// 填充合计内容
										$("#sumQuantityPrint").html('<font size="2px">'+sumQuantityPrint+'</font>');
										$("#originalAmountPrint").html('<font size="2px">'+originalAmountPrint.toFixed(2)+'</font>');
										$("#discountAmountPrint").html('<font size="2px">'+discountAmountPrint.toFixed(2)+'</font>');
										$("#sumAmountPrint").html('<font size="2px">'+sumAmountPrint.toFixed(2)+'</font>');
										
										// 填充整单数据内容
										$("#roundingAmountPrint").html('<font size="2px">'+removeZero.toFixed(2)+'</font>');
										// 支付方式，暂时未做支持
										$("#payTypeAmountPrint").html('<font size="2px">'+sumAmountPrint.toFixed(2)+'</font>');
										$("#payAmountPrint").html('<font size="2px">'+sumAmountPrint.toFixed(2)+'</font>');
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
//										LODOP.SET_SHOW_MODE("SKIN_CUSTOM_COLOR",'#FFFFFF');	
										// 直接文本
//										LODOP.ADD_PRINT_TEXT(50,10,260,39,"销售");
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
//										LODOP.PRINT_DESIGN();
										// 预览
//										LODOP.PREVIEW();
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
		});
	},
	"discount":function(){
		//当前按钮为disabled时，不做处理
		var buttonState=$("#btnDiscount").attr("disabled");
		if(buttonState == "disabled" ){
			return ;
		}
		var dgOpenedState = $("#dgOpenedState").val();
		if(dgOpenedState != "Y"){
			if(BINOLWPSAL02.checkData()){
				// 更改按钮样式
				$thisBtn = $("#btnDiscount");
				BINOLWPSAL02.changeButtonCss($thisBtn);
				$("#btnReturnsGoods").attr("disabled",true);
				$("#btnReturnsGoods").attr("class","btn_top_disable");
				$("#btnAddHistoryBill").attr("disabled",true);
				$("#btnAddHistoryBill").attr("class","btn_top_disable");
				// 禁用清空购物车按钮
				$("#btnEmptyShoppingCart").attr("disabled",true);
				$("#btnEmptyShoppingCart").attr("class","btn_top_disable");
				
				// 删除空行
				BINOLWPSAL02.deleteEmptyRow();
				
				var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 500,
					height: 400,
					title: $("#discountDialogTitle").text(),
					closeEvent:function(){
						// 还原按钮样式
						$("#btnDiscount").attr("class","btn_top");
						// 最后一个文本框获得焦点
						BINOLWPSAL02.lastInputSelect();
						// 关闭弹出窗口
						removeDialog("#dialogInit");
						// 解除退货和补登按钮禁用
						$("#btnReturnsGoods").removeAttr("disabled");
						$("#btnReturnsGoods").attr("class","btn_top");
						$("#btnAddHistoryBill").removeAttr("disabled");
						$("#btnAddHistoryBill").attr("class","btn_top");
						// 解除清空购物车禁用
						$("#btnEmptyShoppingCart").removeAttr("disabled");
						$("#btnEmptyShoppingCart").attr("class","btn_top");
					}
				};
				var originalAmount = $("#originalAmount").val();
				if(Number(originalAmount)<0){
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"金额小于零，不允许折扣！", 
						type:"MESSAGE", 
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
					return;
				}
				openDialog(dialogSetting);
//				var baName = $("#baCode option:selected").text();
//				var baName = $("#baName").val();
				var baName = "";
				var baChooseModel = $("#baChooseModel").val();
				if(baChooseModel == "2"){
					baName = $("#baCode option:selected").text();
				}else{
					baName = $("#baName").val();
				}
				var memberName = $("#memberName").val();
				var totalDiscountRate = $("#totalDiscountRate").val();
				var discountUrl = $("#discountUrl").attr("href");
				var params = "baName=" + baName + "&memberName=" + memberName + "&originalAmount=" +　originalAmount + "&totalDiscountRate=" +　totalDiscountRate;
				cherryAjaxRequest({
					url: discountUrl,
					param: params,
					callback: function(data) {
						$("#dialogInit").html(data);
					}
				});
			}
		}
	},
	
	"hangBills":function(){
		var buttonState=$("#btnHangBills" ).attr("disabled" );
        if(buttonState == "disabled" ){
              return ;
       }

		var dgOpenedState = $("#dgOpenedState").val();
		if(dgOpenedState != "Y"){
			// 更改按钮样式
			$thisBtn = $("#btnHangBills");
			BINOLWPSAL02.changeButtonCss($thisBtn);
			$("#btnReturnsGoods").attr("disabled",true);
			$("#btnReturnsGoods").attr("class","btn_top_disable");
			$("#btnAddHistoryBill").attr("disabled",true);
			$("#btnAddHistoryBill").attr("class","btn_top_disable");
			// 禁用清空购物车按钮
			$("#btnEmptyShoppingCart").attr("disabled",true);
			$("#btnEmptyShoppingCart").attr("class","btn_top_disable");
			
			if(BINOLWPSAL02.checkData()){
				// 删除空行
				BINOLWPSAL02.deleteEmptyRow();
				// 将销售明细列表转换成Json字符串
				BINOLWPSAL02.getSaleDetailList();
				
				var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 900,
					height: 360,
					title: $("#hangBillsDialogTitle").text(),
					closeEvent:function(){
						// 还原按钮样式
						$("#btnHangBills").attr("class","btn_top");
						// 最后一个文本框获得焦点
						BINOLWPSAL02.lastInputSelect();
						// 关闭弹出窗口
						removeDialog("#dialogInit");
						// 解除退货和补登按钮禁用
						$("#btnReturnsGoods").removeAttr("disabled");
						$("#btnReturnsGoods").attr("class","btn_top");
						$("#btnAddHistoryBill").removeAttr("disabled");
						$("#btnAddHistoryBill").attr("class","btn_top");
						// 解除清空购物车禁用
						$("#btnEmptyShoppingCart").removeAttr("disabled");
						$("#btnEmptyShoppingCart").attr("class","btn_top");
					}
				};
				openDialog(dialogSetting);
				
//				var baName = $("#baCode option:selected").text();
//				var baName = $("#baName").val();
				var baName = "";
				var baChooseModel = $("#baChooseModel").val();
				if(baChooseModel == "2"){
					baName = $("#baCode option:selected").text();
				}else{
					baName = $("#baName").val();
				}
				var memberName = $("#memberName").val();
				var counterCode = $("#counterCode").val();
				var hangBillsUrl = $("#hangBillsUrl").attr("href");
				var params = "baName=" + baName + "&memberName=" + memberName + "&counterCode=" + counterCode;
				cherryAjaxRequest({
					url: hangBillsUrl,
					param: params,
					callback: function(data) {
						$("#dialogInit").html(data);
					}
				});
			}else{
				// 提单操作
				var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 1000,
					height: 620,
					title: $("#getBillsDialogTitle").text(),
					closeEvent:function(){
						// 还原按钮样式
						$("#btnHangBills").attr("class","btn_top");
						// 最后一行第一个可见的文本框获得焦点
						BINOLWPSAL02.firstInputSelect();
						// 移除弹出窗口
						removeDialog("#dialogInit");
						// 解除退货和补登按钮禁用
						$("#btnReturnsGoods").removeAttr("disabled");
						$("#btnReturnsGoods").attr("class","btn_top");
						$("#btnAddHistoryBill").removeAttr("disabled");
						$("#btnAddHistoryBill").attr("class","btn_top");
						// 解除清空购物车禁用
						$("#btnEmptyShoppingCart").removeAttr("disabled");
						$("#btnEmptyShoppingCart").attr("class","btn_top");
					}
				};
				openDialog(dialogSetting);
				
//				var baName = $("#baCode option:selected").text();
//				var baName = $("#baName").val();
				var baName = "";
				var baChooseModel = $("#baChooseModel").val();
				if(baChooseModel == "2"){
					baName = $("#baCode option:selected").text();
				}else{
					baName = $("#baName").val();
				}
				var memberName = $("#memberName").val();
				var getBillsUrl = $("#getBillsUrl").attr("href");
				var params = "baName=" + baName + "&memberName=" + memberName;
				cherryAjaxRequest({
					url: getBillsUrl,
					param: params,
					callback: function(data) {
						$("#dialogInit").html(data);
					}
				});
			}
		}
	},
	
	"searchBills":function(){
		var buttonState=$("#btnSearchBills" ).attr("disabled" );
        if(buttonState == "disabled" ){
              return ;
       }
		
		var dgOpenedState = $("#dgOpenedState").val();
		if(dgOpenedState != "Y"){
			if(!BINOLWPSAL02.checkData()){
				// 更改按钮样式
				$thisBtn = $("#btnSearchBills");
				BINOLWPSAL02.changeButtonCss($thisBtn);
				$("#btnReturnsGoods").attr("disabled",true);
				$("#btnReturnsGoods").attr("class","btn_top_disable");
				$("#btnAddHistoryBill").attr("disabled",true);
				$("#btnAddHistoryBill").attr("class","btn_top_disable");
				// 禁用清空购物车按钮
				$("#btnEmptyShoppingCart").attr("disabled",true);
				$("#btnEmptyShoppingCart").attr("class","btn_top_disable");
				
				var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 1200,
					height: 650,
					title: $("#searchBillsDialogTitle").text(),
					closeEvent:function(){
						// 还原按钮样式
						$("#btnSearchBills").attr("class","btn_top");
						// 最后一行第一个可见的文本框获得焦点
						BINOLWPSAL02.firstInputSelect();
						// 关闭弹出窗口
						removeDialog("#dialogInit");
						// 解除退货和补登按钮禁用
						$("#btnReturnsGoods").removeAttr("disabled");
						$("#btnReturnsGoods").attr("class","btn_top");
						$("#btnAddHistoryBill").removeAttr("disabled");
						$("#btnAddHistoryBill").attr("class","btn_top");
						// 解除清空购物车禁用
						$("#btnEmptyShoppingCart").removeAttr("disabled");
						$("#btnEmptyShoppingCart").attr("class","btn_top");
					}
				};
				openDialog(dialogSetting);
				
				var searchBillsUrl = $("#searchBillsUrl").attr("href");
				cherryAjaxRequest({
					url: searchBillsUrl,
					param: null,
					callback: function(data) {
						$("#dialogInit").html(data);
					}
				});
			}
		}
	},
	
	"showMember":function(){
		var buttonState=$("#btnShowMember" ).attr("disabled" );
        if(buttonState == "disabled" ){
              return ;
        }

		var dgOpenedState = $("#dgOpenedState").val();
		if(dgOpenedState != "Y"){
			// 更改按钮样式
			$thisBtn = $("#btnShowMember");
			BINOLWPSAL02.changeButtonCss($thisBtn);
			
			// 关闭打开的弹出窗口
			closeCherryDialog('dialogInit',$('#dialogInit').html());
			// 清空弹出框内容
			$('#dialogInit').html("");
			// 清空会员页面
			$("#member_main").html("");
			// 解除退货和补登按钮禁用
			$("#btnReturnsGoods").removeAttr("disabled");
			$("#btnReturnsGoods").attr("class","btn_top");
			$("#btnAddHistoryBill").removeAttr("disabled");
			$("#btnAddHistoryBill").attr("class","btn_top");
			// 解除清空购物车禁用
			$("#btnEmptyShoppingCart").removeAttr("disabled");
			$("#btnEmptyShoppingCart").attr("class","btn_top");
			
			var memberCode = $("#memberCode").val();
			var searchStr = $("#searchStr").val();
			var showMemberUrl = $("#showMemberUrl").attr("href");
			var params = "memCode=" + memberCode + "&searchStr=" + searchStr;
			cherryAjaxRequest({
				url: showMemberUrl,
				param: params,
				callback: function(data) {
					$("#webpos_main").hide();
					$("#member_main").html(data);
					$("#member_main").show();
				}
			});
		}
	},
	
	"promotionText":function(obj){
		var $this = $(obj).parent().find("#ckAllPromotion");
		if(!!$this.attr("checked")){
			$this.removeAttr("checked");
		}else{
			$this.attr("checked","true");
		}
		BINOLWPSAL02.promotion($this);
	},
	
	"promotion":function(obj){
		var $this = $(obj);
		var changablePoint = $('#changablePoint').val();
		var memberInfoId = $('#memberInfoId').val();
		var mobilePhone = $('#mobilePhone').val();
		if(!!$this.attr("checked")){
			var setParams = {};
			setParams.showType = "ALL";
			setParams.memberInfoId = memberInfoId;
			setParams.mobilePhone = mobilePhone;
			setParams.changablePoint = changablePoint;
			BINOLWPSAL02.getPromotion(setParams);
		}else{
			var setParams = {};
			setParams.showType = "";
			setParams.memberInfoId = memberInfoId;
			setParams.mobilePhone = mobilePhone;
			setParams.changablePoint = changablePoint;
			BINOLWPSAL02.getPromotion(setParams);
		}
		// 最后一行第一个文本框获得焦点
		BINOLWPSAL02.firstInputSelect();
	},
	
	"returnsGoods":function(){
		var buttonState=$("#btnReturnsGoods" ).attr("disabled" );
        if(buttonState == "disabled" ){
              return ;
        }

		var dgOpenedState = $("#dgOpenedState").val();
		if(dgOpenedState != "Y" && !$("#btnReturnsGoods").prop("disabled")){
			var nowSaleType = $("#saleType").val();
			// 更改按钮样式
			$thisBtn = $("#btnReturnsGoods");
			BINOLWPSAL02.changeButtonCss($thisBtn);
			
			var returnsGoodsUrl = $("#returnsGoodsUrl").attr("href");
			var params = "saleType=" + nowSaleType;
			cherryAjaxRequest({
				url: returnsGoodsUrl,
				param: params,
				callback: function(data) {
					if(data != undefined && data != null && data != ""){
						var billInfo = eval("("+data+")");
						$('#saleType').val(billInfo.saleType);
						$('#billCode').val(billInfo.billCode);
						$('#spanBillCode').text(billInfo.billCode);
						if(billInfo.saleType == "SR"){
							$('#txtSaleType').text($('#saleTypeSR').val());
							$("#saleTypeTotalQuantitySr").show();
							$("#saleTypeTotalAmountSr").show();
							$("#saleTypeTotalQuantityNs").hide();
							$("#saleTypeTotalAmountNs").hide();
							$("#saleTypeQuantity").show();
							$("#saleTypeAmount").show();
							//检查有无行，若有则更改样式
							if($('#databody >tr').length > 0){
								$.each($('#databody >tr'), function(i){
									$(this).attr("class","red");
								});
							}
						}else{
							$('#txtSaleType').text($('#saleTypeNS').val());
							$("#saleTypeTotalQuantitySr").hide();
							$("#saleTypeTotalAmountSr").hide();
							$("#saleTypeTotalQuantityNs").show();
							$("#saleTypeTotalAmountNs").show();
							$("#saleTypeQuantity").hide();
							$("#saleTypeAmount").hide();
							// 重新设置间隔行样式
							BINOLWPSAL02.changeOddEvenColor();
							// 设置促销活动明细行样式
							if($('#databody >tr').length > 0){
								$.each($('#databody >tr'), function(i){
									if($(this).find("#activityTypeCode").val() == "CXHD"){
										if($(this).find("#productVendorIDArr").val()!="HDZD"){
											$(this).attr("class","green");
										}
									}
								});
							}
							// 移除按钮样式
							$("#buttonPageDiv").find(".btn_top_selected").attr("class","btn_top");
						}
						// 最后一行第一个文本框获得焦点
						BINOLWPSAL02.firstInputSelect();
					}else{
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"获取退货单据号异常", 
							type:"MESSAGE", 
							focusEvent:function(){
								// 最后一行第一个可见的文本框获得焦点
								BINOLWPSAL02.firstInputSelect();
							}
						});
					}
				}
			});
		}
	},
	
	"addHistoryBill":function(){
		var buttonState=$("#btnAddHistoryBill" ).attr("disabled" );
        if(buttonState == "disabled" ){
              return ;
        }
		
		var dgOpenedState = $("#dgOpenedState").val();
		if(dgOpenedState != "Y"){
			// 更改按钮样式
			$thisBtn = $("#btnAddHistoryBill");
			BINOLWPSAL02.changeButtonCss($thisBtn);
			
			var businessState = $("#businessState").val();
			if(businessState == "H"){
				// 更改业务状态并隐藏状态提示
				$("#businessState").val("N");
				$("#txtAddHistoryBill").hide();
				// 禁用日期选择控件
				$("#businessDate").attr("disabled",true);
				$("#searchPageDiv").find(".ui-datepicker-trigger").attr("disabled",true);
				// 业务日期还原为当前日期
				$("#businessDate").val($("#nowDate").val());
			}else{
				// 更改业务状态并显示状态提示
				$("#businessState").val("H");
				$("#txtAddHistoryBill").show();
				// 启用日期选择控件
				$("#businessDate").removeAttr("disabled");
				$("#searchPageDiv").find(".ui-datepicker-trigger").removeAttr("disabled");
				// 弹出日历选择
				$("#businessDate").focus();
			}
			// 最后一行第一个文本框获得焦点
			BINOLWPSAL02.firstInputSelect();
		}
	},
	
	"emptyShoppingCart":function(){
		var buttonState=$("#btnEmptyShoppingCart" ).attr("disabled" );
        if(buttonState == "disabled" ){
              return ;
       }
		
		var dgOpenedState = $("#dgOpenedState").val();
		if(dgOpenedState != "Y"){
			if($('#databody >tr').length > 0){
				var flag = false;
				$.each($('#databody >tr'), function(i){
					if($(this).find("#productVendorIDArr").val()!=""){
						flag = true;
						return false;
					}
				});
				if(flag){
					// 更改按钮样式
					$thisBtn = $("#btnEmptyShoppingCart");
					BINOLWPSAL02.changeButtonCss($thisBtn);
					
					var counterCode = $("#counterCode").val();
					var saleMainUrl = $("#saleMainUrl").attr("href");
					var param = "counterCode=" + counterCode;
					cherryAjaxRequest({
						url: saleMainUrl,
						param: param,
						callback: function(data) {
							$("#webpos_main").html(data);
						}
					});
				}else{
					// 重新加载促销
					var setParams = {};
					BINOLWPSAL02.getPromotion(setParams);
					// 清空会员信息
					BINOLWPSAL02.emptyMemberInfo();
					// 清空搜索框值
					$("#searchStr").val("");
				}
			}else{
				// 重新加载促销
				var setParams = {};
				BINOLWPSAL02.getPromotion(setParams);
				// 清空会员信息
				BINOLWPSAL02.emptyMemberInfo();
				// 清空搜索框值
				$("#searchStr").val("");
			}
			// 最后一行第一个文本框获得焦点
			BINOLWPSAL02.firstInputSelect();
		}
	},
	
	// 清空会员信息
	"emptyMemberInfo":function(){
		// 隐藏会员信息
		$("#memberPageDiv").hide();
		// 清空已有的会员相关信息
		$('#memberInfoId').val("");
		$('#memberCode').val("");
		$('#memberName').val("");
		$('#memberLevel').val("");
		$('#mobilePhone').val("");
		$('#changablePoint').val("");
		$('#txtMemberName').text("");
		$('#spanMemberCode').text("");
		$('#spanMemberName').text("");
		$('#spanTotalPoint').text("");
		$('#spanChangablePoint').text("");
		$('#spanJoinDate').text("");
		$('#spanLastSaleDate').text("");
	},
	
	"search":function(){
		// 清空会员信息
		BINOLWPSAL02.emptyMemberInfo();
		
		// 获取搜索条件
		var searchStr = $.trim($("#searchStr").val());
		var reg = new RegExp($("#memCodeRule").val());
		if (!reg.test(searchStr)) {
			// 显示提示信息
			BINOLWPSAL02.showMessageDialog({
				message:"请输入正确的会员卡号", 
				type:"MESSAGE", 
				focusEvent:function(){
					// 最后一行第一个可见的文本框获得焦点
					BINOLWPSAL02.firstInputSelect();
				}
			});
			return false;
		}
		if(searchStr != undefined && searchStr != null && searchStr != ""){
			var searchUrl = $("#searchUrl").attr("href");
			var param = "searchStr=" + searchStr+"&counterCode="+$("#counterCode").val();
			cherryAjaxRequest({
				url: searchUrl,
				param: param,
				callback: function(data) {
					if(data != undefined && data != null && data != ""){
						if(data == "MULTIPLE"){
							// 查询结果出现多条记录的情况下跳转至会员主页
							BINOLWPSAL02.showMember();
						}else{
							// 查询结果为单条记录的情况下显示记录详细内容
							var memberInfo = eval("("+data+")");
							$('#memberInfoId').val(memberInfo.memberInfoId);
							$('#memberCode').val(memberInfo.memberCode);
							$('#memberName').val(memberInfo.memberName);
							$('#memberLevel').val(memberInfo.memberLevel);
							$('#mobilePhone').val(memberInfo.mobilePhone);
							$('#changablePoint').val(memberInfo.changablePoint);
							$('#txtMemberName').text(memberInfo.memberName);
							$('#spanMemberCode').text(memberInfo.memberCode);
							$('#spanMemberName').text(memberInfo.memberName);
							$('#spanTotalPoint').text(memberInfo.totalPoint);
							$('#spanChangablePoint').text(memberInfo.changablePoint);
							$('#spanJoinDate').text(memberInfo.joinDate);
							$('#spanLastSaleDate').text(memberInfo.lastSaleDate);
							var new_Czk_Pay = $("#new_Czk_Pay").val();
							if("Y"==new_Czk_Pay){
								$("#newCzkPay").show();
							}
							// 行数大于10行时查询会员显示在记录行下部，小于10时显示在页面底部
							if($('#databody >tr').length >= 10){
								$("#memberPageDiv").attr("class","wp_bottom2");
								$("#memberContentDiv").removeAttr("class");
								$("#memberBoxDiv").removeAttr("class");
							}else{
								$("#memberPageDiv").attr("class","wp_bottom");
								$("#memberContentDiv").attr("class","wp_content_b");
								$("#memberBoxDiv").attr("class","wp_leftbox");
							}
							$("#memberPageDiv").show();
							// 查询符合条件的促销活动
							var setParams = {};
							setParams.memberInfoId = memberInfo.memberInfoId;
							setParams.mobilePhone = memberInfo.mobilePhone;
							setParams.changablePoint = memberInfo.changablePoint;
							setParams.counterCodeBelong = memberInfo.counterCodeBelong;
							setParams.firstSaleCounter = memberInfo.firstSaleCounter;
							setParams.orderCounterCode = memberInfo.orderCounterCode;
							BINOLWPSAL02.getPromotion(setParams);
							
							// 计算会员价格和金额
							BINOLWPSAL02.calcuMemberSaleAmout();
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					}else{
//						var baCode = $("#baCode option:selected").val();
//						var baName = $("#baCode option:selected").text();
//						var baCode = $("#baCode").val();
//						var baName = $("#baName").val();
						var baCode = "";
						var baName = "";
						var baChooseModel = $("#baChooseModel").val();
						if(baChooseModel == "2"){
							var baCode = $("#baCode option:selected").val();
							var baName = $("#baCode option:selected").text();
						}else{
							baName = $("#baCode").val();
							baName = $("#baName").val();
						}
						// 禁用退货和补登按钮
						$("#btnReturnsGoods").attr("disabled",true);
						$("#btnReturnsGoods").attr("class","btn_top_disable");
						$("#btnAddHistoryBill").attr("disabled",true);
						$("#btnAddHistoryBill").attr("class","btn_top_disable");
						// 禁用清空购物车按钮
						$("#btnEmptyShoppingCart").attr("disabled",true);
						$("#btnEmptyShoppingCart").attr("class","btn_top_disable");
						// 查询结果为空的情况
						var dialogSetting = {
							dialogInit: "#dialogInit",
							width: 1000,
							height: 450,
							title: $("#addMemberDialogTitle").text(),
							closeEvent:function(){
								// 最后一行第一个可见的文本框获得焦点
								BINOLWPSAL02.firstInputSelect();
								// 移除弹出窗口
								removeDialog("#dialogInit");
								// 解除退货和补登按钮禁用
								$("#btnReturnsGoods").removeAttr("disabled");
								$("#btnReturnsGoods").attr("class","btn_top");
								$("#btnAddHistoryBill").removeAttr("disabled");
								$("#btnAddHistoryBill").attr("class","btn_top");
								// 解除清空购物车禁用
								$("#btnEmptyShoppingCart").removeAttr("disabled");
								$("#btnEmptyShoppingCart").attr("class","btn_top");
							}
						};
						openDialog(dialogSetting);
						
						var addMemberUrl = $("#addMemberUrl").attr("href");
						var params = "viewType=SCH&baCode="+ baCode +"&baName="+ baName +"&memberCode="+ searchStr;
						cherryAjaxRequest({
							url: addMemberUrl,
							param: params,
							callback: function(data) {
								$("#dialogInit").html(data);
								// 查询符合条件的促销活动
								var setParams = {};
								BINOLWPSAL02.getPromotion(setParams);
							}
						});
					}
				}
			});
		}else{
			// 未输入查询条件的情况下，最后一行第一个可见的文本框获得焦点
			BINOLWPSAL02.firstInputSelect();
			// 重新加载促销
			var setParams = {};
			BINOLWPSAL02.getPromotion(setParams);
			// 计算会员价格和金额
			BINOLWPSAL02.calcuMemberSaleAmout();
		}
	},
	
	"getPromotion":function(setParams){
		var promotionChecked = [];
		var allCheckedFlag = true;
		// 将已选中选择框的值存入数组
		$.each($('#promotionData > ul'), function(i){
			if(!!$(this).find("#ckPromotion").attr("checked")){
				promotionChecked.push($(this).find("#ckPromotion").val());
			}
		});
		// 判断全选按钮是否选中
		if(!!$("#ckAllPromotion").attr("checked")){
			// 如果传入的显示类型不为全部则清空全选按钮
			if(setParams.showType == "ALL"){
				allCheckedFlag = true;
			}else{
				allCheckedFlag = false;
			}
		}else{
			allCheckedFlag = false;
		}
		
		var showTypeVal = "";
		if(setParams.showType != null && setParams.showType != 'null' && setParams.showType != undefined && setParams.showType != ""){
			showTypeVal = setParams.showType;
    	}
		var memberInfoIdVal = "";
		if(setParams.memberInfoId != null && setParams.memberInfoId != 'null' && setParams.memberInfoId != undefined && setParams.memberInfoId != ""){
			memberInfoIdVal = setParams.memberInfoId;
    	}
		var mobilePhoneVal = "";
		if(setParams.mobilePhone != null && setParams.mobilePhone != 'null' && setParams.mobilePhone != undefined && setParams.mobilePhone != ""){
			mobilePhoneVal = setParams.mobilePhone;
    	}
		var changablePointVal = "";
		if(setParams.changablePoint != null && setParams.changablePoint != 'null' && setParams.changablePoint != undefined && setParams.changablePoint != ""){
			changablePointVal = setParams.changablePoint;
    	}
		// 发卡柜台号
		var counterCodeBelongVal = "";
		if(setParams.counterCodeBelong != null && setParams.counterCodeBelong != 'null' && setParams.counterCodeBelong != undefined && setParams.counterCodeBelong != ""){
			counterCodeBelongVal = setParams.counterCodeBelong;
    	}
		// 首次购买柜台
		var firstSaleCounterVal = "";
		if(setParams.firstSaleCounter != null && setParams.firstSaleCounter != 'null' && setParams.firstSaleCounter != undefined && setParams.firstSaleCounter != ""){
			firstSaleCounterVal = setParams.firstSaleCounter;
    	}
		// 查询活动预约柜台
		var orderCounterCodeVal = "";
		if(setParams.orderCounterCode != null && setParams.orderCounterCode != 'null' && setParams.orderCounterCode != undefined && setParams.orderCounterCode != ""){
			orderCounterCodeVal = JSON.stringify(setParams.orderCounterCode);
    	}
		// 获取促销活动
		var promotionUrl = $("#promotionUrl").attr("href");
		var params = "showType="+ showTypeVal +"&memberInfoId="+ memberInfoIdVal +"&mobilePhone="+ mobilePhoneVal 
				+"&changablePoint="+ changablePointVal+"&counterCodeBelong=" +counterCodeBelongVal 
				+"&firstSaleCounter="+ firstSaleCounterVal +"&orderCounterCode="+ orderCounterCodeVal;
		cherryAjaxRequest({
			url: promotionUrl,
			param: params,
			callback: function(data) {
				$("#rightPageDiv").html(data);
				// 还原全选按钮
				if(allCheckedFlag){
					$("#ckAllPromotion").attr("checked","true");
				}else{
					$("#ckAllPromotion").removeAttr("checked");
				}
				// 还原选中的促销活动
				if(promotionChecked.length > 0){
					$.each(promotionChecked, function(i){
					   var $thisCheckBox = $("#promotionData").find("#ckPromotion:[value='"+ promotionChecked[i] +"']");
					   if($thisCheckBox.length > 0){
						   	$thisCheckBox.attr("checked","true");
					   }else{
							// 删除指定促销记录行
							BINOLWPSAL02.deletePromotionRow(promotionChecked[i]); 
					   }
					});
				}
				// 设置单据分类
				BINOLWPSAL02.setBillClassify();
				// 执行获取促销活动后触发非法
				if(typeof setParams.afterGetPromotionEvent == "function") {
					setParams.afterGetPromotionEvent();
				}
			}
		});
	},
	
	"calcuChangablePoint":function(){
		var changablePoint = $('#changablePoint').val();
		var usePoint = 0;
		var rows = $("#databody").children();
		if(rows.length > 0){
			rows.each(function(i){
				var proTypeVal = $(this).find("#activityTypeCode").val();
				var recTypeVal = $(this).find("#productVendorIDArr").val();
				if(proTypeVal == "CXHD"){
					if(recTypeVal == "HDZD"){
						// 输入数量
						var quantity = $(this).find("#promotionQuantity").val();
						// 活动所需积分
						var exPoint = $(this).find("#exPoint").val();
						// 单条记录使用积分
						var proUsePoint = quantity*Number(exPoint);
						// 累计使用积分
						usePoint += proUsePoint;
					}
					
//					if(recTypeVal != "HDZD"){
//						// 实际数量
//						var realQuantity=$(this).find("#quantityuArr").val();
//						if(isNaN(realQuantity) || realQuantity==""){
//							realQuantity = 0;
//						}
//						// 组数量
//						var groupQuantity=$(this).find("#groupQuantity").val();
//						if(isNaN(groupQuantity) || groupQuantity=="" || groupQuantity=="0"){
//							groupQuantity = 1;
//						}
//						// 输入数量
//						var quantity = Number(realQuantity)/Number(groupQuantity);
//						var exPoint = $(this).find("#exPoint").val();
//						if(isNaN(exPoint) || exPoint==""){
//							exPoint = 0;
//						}
//						// 单条记录使用积分
//						var proUsePoint = quantity*Number(exPoint);
//						// 累计使用积分
//						usePoint += proUsePoint;
//					}
				}
			});
		}
		// 计算剩余可用积分
		changablePoint = Number(changablePoint) - usePoint;
		return changablePoint.toFixed(0);
	},
	
	"checkPromotionText":function(obj){
		var $this = $(obj).parent().find("#ckPromotion");
		if(!!$this.attr("checked")){
			$this.removeAttr("checked");
		}else{
			$this.attr("checked","true");
		}
		BINOLWPSAL02.checkPromotion($this);
	},
	
	"checkPromotion":function(obj){
		// 修改商品明细样式
		$($("#databody >tr")).each(function(){
			if($(this).find("td:first").attr("id") == undefined){
				$(this).addClass("green");
			}
		});
		
		var $this = $(obj);
		if(!!$this.attr("checked")){
			// 获取销售最大允许录入行数
			var showSaleRows = $("#showSaleRows").val();
			// 判断行数是否大于允许的行数
			if($('#databody >tr').length >= Number(showSaleRows)){
				// 显示提示信息
				BINOLWPSAL02.showMessageDialog({
					message:"不能再增加新的行了，如有需要请修改配置", 
					type:"MESSAGE", 
					focusEvent:function(){
						// 最后一行第一个可见的文本框获得焦点
						BINOLWPSAL02.firstInputSelect();
					}
				});
				// 移除活动选中状态
				$this.removeAttr("checked");
				return;
			}
			// 判断是否需要购买
			var needBuyFlag = $this.parent().parent().find("#needBuyFlag").val();
			//云POS是否需要忽略需要购买的条件 Y为忽略 N为不忽略
			var isBuyFlag=$("#isBuyFlag").val();
			if(needBuyFlag == "1" && isBuyFlag == "N"){
				if(!BINOLWPSAL02.checkBuyFlag()){
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"您选择的活动需要购买产品才能参与", 
						type:"MESSAGE", 
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
					$this.removeAttr("checked");
					return;
				}
			}
			var activityClassify = $this.parent().parent().find("#activityClassify").val();
			var campaignValid = $this.parent().parent().find("#campaignValid").val();
			//积分兑换活动是否限定产品
			var isLimitProduct = $("#isLimitProduct").val();

			// 判断是否可以参与积分兑换
			if(activityClassify == "DHHD"){
				//表示需要限定产品，此时只有可以参与积分兑换的产品才能进行积分兑换活动
				if((isLimitProduct == 1 || isLimitProduct == "1")) {
					BINOLWPSAL02.clearActionClass();
					if (BINOLWPSAL02.checkIsExchanged()) {
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message: "购物车中存在不能参与积分兑换的产品",
							type: "MESSAGE",
							focusEvent: function () {
								// 最后一行第一个可见的文本框获得焦点
								BINOLWPSAL02.firstInputSelect();
							}
						});
						$this.removeAttr("checked");
						return;
					}
				}
			}else if(activityClassify == "LYHD"){//添加礼品领用活动类型的支持
					//不需要校验的情况	
					if(campaignValid == "0"){
							var subjectCode=$(obj).parents().parents().find('#subjectCode').val();
							var maincode=$(obj).parents().parents().find('#activityCode').val();
							var memberCode=$('#memberCode').val();
							var params="memberCode="+memberCode+"&subjectCode="+subjectCode+"&activityType=1"+"&maincode="+maincode;
							var initLYHDUrl_0=$("#initLYHDUrl_0").attr("href");
							var dialogSetting = {
									dialogInit: "#dialogInit",
									width: 300,
									height: 200,
									title: $("#getLYHD").text(),
									closeEvent:function(){
										BINOLWPSAL08_2.cancel();
										// 还原按钮样式
										$("#btnCollect").attr("class","btn_top");
										// 可见文本框回车事件解绑
										$("#collectPageDiv").find("input:text:visible").unbind();
										// 关闭弹出窗口
										removeDialog("#dialogInit");
										// 解除退货和补登按钮禁用
										$("#btnReturnsGoods").removeAttr("disabled");
										$("#btnReturnsGoods").attr("class","btn_top");
										$("#btnAddHistoryBill").removeAttr("disabled");
										$("#btnAddHistoryBill").attr("class","btn_top");
										// 解除清空购物车禁用
										$("#btnEmptyShoppingCart").removeAttr("disabled");
										$("#btnEmptyShoppingCart").attr("class","btn_top");
										// 最后一行第一个可见的文本框获得焦点
										BINOLWPSAL02.firstInputSelect();
									}
							};
							openDialog(dialogSetting);
							cherryAjaxRequest({
								url: initLYHDUrl_0,
								param: params,
								callback: function(data) {
									$("#dialogInit").html(data);
									$("#btnConfirm").focus();
								}
							});
							return;
						}else if (campaignValid == "1"){//本地校验的情况
							var subjectCode=$(obj).parents().parents().find('#subjectCode').val();
							var maincode=$(obj).parents().parents().find('#activityCode').val();
							var memberCode=$('#memberCode').val();
							var memberName=$('#memberName').val();
							var params="memberCode="+memberCode+"&subjectCode="+subjectCode+"&activityType=1"+"&maincode="+maincode+"&memberName="+memberName;
							var initLYHDUrl_1=$("#initLYHDUrl_1").attr("href");
							var dialogSetting = {
									dialogInit: "#dialogInit",
									width: 500,
									height: 300,
									title: $("#getLYHD").text(),
									closeEvent:function(){
										BINOLWPSAL08_3.cancel();
										// 还原按钮样式
										$("#btnCollect").attr("class","btn_top");
										// 可见文本框回车事件解绑
										$("#collectPageDiv").find("input:text:visible").unbind();
										// 关闭弹出窗口
										removeDialog("#dialogInit");
										// 解除退货和补登按钮禁用
										$("#btnReturnsGoods").removeAttr("disabled");
										$("#btnReturnsGoods").attr("class","btn_top");
										$("#btnAddHistoryBill").removeAttr("disabled");
										$("#btnAddHistoryBill").attr("class","btn_top");
										// 解除清空购物车禁用
										$("#btnEmptyShoppingCart").removeAttr("disabled");
										$("#btnEmptyShoppingCart").attr("class","btn_top");
										// 最后一行第一个可见的文本框获得焦点
										BINOLWPSAL02.firstInputSelect();
									}
							};
							openDialog(dialogSetting);
							cherryAjaxRequest({
								url: initLYHDUrl_1,
								param: params,
								callback: function(data) {
									$("#dialogInit").html(data);
								}
							});
							return;
						}else if (campaignValid == "3"){//校验会员卡号
							var subjectCode=$(obj).parents().parents().find('#subjectCode').val();
							var maincode=$(obj).parents().parents().find('#activityCode').val();
							var memberCode=$('#memberCode').val();
							var memberName=$('#memberName').val();
							var params="memberCode="+memberCode+"&subjectCode="+subjectCode+"&maincode="+maincode+"&memberName="+memberName;
							var initLYHDUrl_3=$("#initLYHDUrl_3").attr("href");
							var dialogSetting = {
									dialogInit: "#dialogInit",
									width: 500,
									height: 300,
									title: $("#getLYHD").text(),
									closeEvent:function(){
										BINOLWPSAL08_5.cancel();
										// 还原按钮样式
										$("#btnCollect").attr("class","btn_top");
										// 可见文本框回车事件解绑
										$("#collectPageDiv").find("input:text:visible").unbind();
										// 关闭弹出窗口
										removeDialog("#dialogInit");
										// 解除退货和补登按钮禁用
										$("#btnReturnsGoods").removeAttr("disabled");
										$("#btnReturnsGoods").attr("class","btn_top");
										$("#btnAddHistoryBill").removeAttr("disabled");
										$("#btnAddHistoryBill").attr("class","btn_top");
										// 解除清空购物车禁用
										$("#btnEmptyShoppingCart").removeAttr("disabled");
										$("#btnEmptyShoppingCart").attr("class","btn_top");
										// 最后一行第一个可见的文本框获得焦点
										BINOLWPSAL02.firstInputSelect();
									}
							};
							openDialog(dialogSetting);
							cherryAjaxRequest({
								url: initLYHDUrl_3,
								param: params,
								callback: function(data) {
									$("#dialogInit").html(data);
								}
							});
							return;
						}else if(campaignValid == "4"){//校验手机号
							var subjectCode=$(obj).parents().parents().find('#subjectCode').val();
							var maincode=$(obj).parents().parents().find('#activityCode').val();
							var memberCode=$('#memberCode').val();
							var memberName=$('#memberName').val();
							var params="memberCode="+memberCode+"&subjectCode="+subjectCode+"&maincode="+maincode+"&memberName="+memberName;
							var initLYHDUrl_4=$("#initLYHDUrl_4").attr("href");
							var dialogSetting = {
									dialogInit: "#dialogInit",
									width: 500,
									height: 300,
									title: $("#getLYHD").text(),
									closeEvent:function(){
										BINOLWPSAL08_4.cancel();
										// 还原按钮样式
										$("#btnCollect").attr("class","btn_top");
										// 可见文本框回车事件解绑
										$("#collectPageDiv").find("input:text:visible").unbind();
										// 关闭弹出窗口
										removeDialog("#dialogInit");
										// 解除退货和补登按钮禁用
										$("#btnReturnsGoods").removeAttr("disabled");
										$("#btnReturnsGoods").attr("class","btn_top");
										$("#btnAddHistoryBill").removeAttr("disabled");
										$("#btnAddHistoryBill").attr("class","btn_top");
										// 解除清空购物车禁用
										$("#btnEmptyShoppingCart").removeAttr("disabled");
										$("#btnEmptyShoppingCart").attr("class","btn_top");
										// 最后一行第一个可见的文本框获得焦点
										BINOLWPSAL02.firstInputSelect();
									}
							};
							openDialog(dialogSetting);
							cherryAjaxRequest({
								url: initLYHDUrl_4,
								param: params,
								callback: function(data) {
									$("#dialogInit").html(data);
								}
							});
							return;
						}
					}
			
			
			if(campaignValid == "2" || campaignValid == "5"){
				var dialogSetting = {
					dialogInit: "#checkCoupon_dialog",
					width: 500,
					height: 300,
					title: $("#checkCouponDialogTitle").text(),
					closeEvent:function(){
						$this.removeAttr("checked");
						// 最后一行第一个可见的文本框获得焦点
						BINOLWPSAL02.firstInputSelect();
						// 关闭弹出窗口
						removeDialog("#checkCoupon_dialog");
					}
				};
				openDialog(dialogSetting);
				
				var memberInfoId = $('#memberInfoId').val();
				var memberName = $("#memberName").val();
				var mobilePhone = $("#mobilePhone").val();
				var activityType = $this.parent().parent().find("#activityType").val();
				var subjectCode = $this.parent().parent().find("#subjectCode").val();
				var activityCode = $this.parent().parent().find("#activityCode").val();
				var checkCouponUrl = $("#checkCouponUrl").attr("href");
				var params = "campaignValid="+ campaignValid +"&memberInfoId="+ memberInfoId +"&memberName="+ memberName +"&mobilePhone="+ mobilePhone 
						+"&activityType="+ activityType +"&subjectCode="+ subjectCode +"&activityCode="+ activityCode;
				cherryAjaxRequest({
					url: checkCouponUrl,
					param: params,
					callback: function(data) {
						$("#checkCoupon_dialog").html(data);
					}
				});
			}else{
				// 删除空行
				BINOLWPSAL02.deleteEmptyRow();
				// 参与活动需要的积分
				var exNeedPoint = $this.parent().parent().find("#exNeedPoint").val();
				// 计算还可以使用的积分 
				var changablePoint = BINOLWPSAL02.calcuChangablePoint();
				if(changablePoint >= Number(exNeedPoint)){
					var memberInfoId = $('#memberInfoId').val();
					var mobilePhone = $('#mobilePhone').val();
					var activityType = $this.parent().parent().find("#activityType").val();
					var subjectCode = $this.parent().parent().find("#subjectCode").val();
					var activityCode = $this.parent().parent().find("#activityCode").val();
					
					var getProProductUrl = "";
					var params = "";
					var detailType = $this.parent().parent().find("#detailType").val();
					if(detailType == "1"){
						getProProductUrl = $("#getCouponProductUrl").attr("href");
						params = "memberInfoId="+ memberInfoId +"&mobilePhone="+ mobilePhone
								+"&subjectCode="+ subjectCode +"&activityCode="+ activityCode;
					}else{
						getProProductUrl = $("#getProProductUrl").attr("href");
						params = "activityType="+ activityType +"&memberInfoId="+ memberInfoId
								+"&subjectCode="+ subjectCode +"&activityCode="+ activityCode;
					}
					cherryAjaxRequest({
						url: getProProductUrl,
						param: params,
						callback: function(data) {
							if(data != undefined && data != null && data != ""){
								if(data == "ERROR"){
									if($('#databody >tr').length < Number(showSaleRows)){
										// 添加一个空行
										BINOLWPSAL02.addNewLine();
									}
									// 显示提示信息
									BINOLWPSAL02.showMessageDialog({
										message:"查询活动礼品信息失败", 
										type:"MESSAGE", 
										focusEvent:function(){
											// 最后一行第一个可见的文本框获得焦点
											BINOLWPSAL02.firstInputSelect();
										}
									});
									// 移除选择框选中状态
									$this.removeAttr("checked");
								}else if(data == "NOTMEMBER"){
									if($('#databody >tr').length < Number(showSaleRows)){
										// 添加一个空行
										BINOLWPSAL02.addNewLine();
									}
									// 显示提示信息
									BINOLWPSAL02.showMessageDialog({
										message:"您选择的会员信息不完整", 
										type:"MESSAGE", 
										focusEvent:function(){
											// 最后一行第一个可见的文本框获得焦点
											BINOLWPSAL02.firstInputSelect();
										}
									});
									// 移除选择框选中状态
									$this.removeAttr("checked");
								}else if(data == "TIMEEXPIRED"){
									if($('#databody >tr').length < Number(showSaleRows)){
										// 添加一个空行
										BINOLWPSAL02.addNewLine();
									}
									// 显示提示信息
									BINOLWPSAL02.showMessageDialog({
										message:"您选择的活动已经结束，请刷新界面！", 
										type:"MESSAGE", 
										focusEvent:function(){
											// 最后一行第一个可见的文本框获得焦点
											BINOLWPSAL02.firstInputSelect();
										}
									});
									// 移除选择框选中状态
									$this.removeAttr("checked");
								}else{
									// 删除折扣行
									BINOLWPSAL02.deleteDiscountRow();
									// 判断活动礼品明细获取源
									if(detailType == "1"){
										var promotionMap = eval("("+data+")");
										var couponOrderInfo = promotionMap.couponOrderInfo;
										var couponOrderProduct = promotionMap.couponOrderProduct;
										// 增加主记录行
										BINOLWPSAL02.appendCouponOrderInfo(couponOrderInfo);
										// 增加明细记录行
										$.each(couponOrderProduct, function(i){
											// 添加促销活动记录行
											BINOLWPSAL02.appendCouponOrderProduct(couponOrderProduct[i]);
										});
									}else{
										var promotionMap = eval("("+data+")");
										var promotionInfo = promotionMap.promotionInfo;
										var promotionProduct = promotionMap.promotionProduct;
										// 增加主记录行
										BINOLWPSAL02.appendPromotionInfo(promotionInfo);
										// 增加明细记录行
										$.each(promotionProduct, function(i){
											// 添加促销活动记录行
											BINOLWPSAL02.appendPromotionProduct(promotionProduct[i]);
										});
									}
									// 设置单据分类
									BINOLWPSAL02.setBillClassify();
									// 计算总金额
									BINOLWPSAL02.calcuTatol();
									
									if($('#databody >tr').length < Number(showSaleRows)){
										// 添加一个空行
										BINOLWPSAL02.addNewLine();
									}else{
										// 最后一个可见的文本框获得焦点
										$('#databody >tr').find("input:text:visible:last").select();
									}
									// 启用收款盒折扣按钮，禁用查单按钮
									$("#btnCollect").removeAttr("disabled");
									$("#btnDiscount").removeAttr("disabled");
									$("#btnSearchBills").attr("disabled",true);
									$("#btnCollect").attr("class","btn_top");
									$("#btnDiscount").attr("class","btn_top");
									$("#btnSearchBills").attr("class","btn_top_disable");
								}
							}else{
								if($('#databody >tr').length < Number(showSaleRows)){
									// 添加一个空行
									BINOLWPSAL02.addNewLine();
								}
								// 查询结果为空的情况下显示错误信息
								BINOLWPSAL02.showMessageDialog({
									message:"没有查询到符合条件的礼品信息", 
									type:"MESSAGE", 
									focusEvent:function(){
										// 最后一行第一个可见的文本框获得焦点
										BINOLWPSAL02.firstInputSelect();
									}
								});
								// 移除选择框选中状态
								$this.removeAttr("checked");
							}
						}
					});
				}else{
					// 恢复购物车空白行
					if($('#databody >tr').length < Number(showSaleRows)){
						// 添加一个空行
						BINOLWPSAL02.addNewLine();
					}
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"积分不足", 
						type:"MESSAGE", 
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
					// 移除选择框选中状态
					$this.removeAttr("checked");
				}
			}
		}else{
			// 移除促销活动记录行
			var activityCode = $this.parent().parent().find("#activityCode").val();
			// 删除指定促销记录行
			BINOLWPSAL02.deletePromotionRow(activityCode);
			// 设置单据分类
			BINOLWPSAL02.setBillClassify();
			// 最后一行第一个可见的文本框获得焦点
			BINOLWPSAL02.firstInputSelect();
		}
	},
	
	"setBillClassify":function(){
		// 判断是否使用兑换活动
		var isDHActivity = false;
		var isCXActivity = false;
		var saleType = $("#saleType").val();
		if($('#promotionData >ul').length > 0){
			$.each($('#promotionData >ul'), function(i){
				if($(this).find("#ckPromotion").is(":checked")){
					if($(this).find("#activityClassify").val() == "DHHD"){
						isDHActivity = true;
						return false;
					}else{
						isCXActivity = true;
					}
				}
			});
		}
		if(isDHActivity){
			// 若已选中退货状态则撤销
			if(saleType == "SR"){
				BINOLWPSAL02.returnsGoods();
			}
			$("#billClassify").val("DHHD");
			// 兑换活动禁用退货按钮
			$("#btnReturnsGoods").attr("disabled",true);
			$("#btnReturnsGoods").attr("class","btn_top_disable");
		}else{
			if(isCXActivity){
				$("#billClassify").val("CXHD");
			}else{
				$("#billClassify").val("");
			}
			// 退货按钮置为可用
			$("#btnReturnsGoods").removeAttr("disabled");
			$("#btnReturnsGoods").attr("class","btn_top");
		}
	},
	
	"appendPromotionInfo":function(promotionInfo){
		// 处理订单ID
    	var orderId = promotionInfo.orderId;
    	if(orderId == null || orderId == 'null' || orderId == undefined){
    		orderId = "";
    	}
		
		// 处理原价
    	var oldPrice = promotionInfo.oldPrice;
    	if(oldPrice == null || oldPrice == 'null' || oldPrice == undefined || oldPrice == ""){
    		oldPrice = "0.00";
    	}else{
    		oldPrice = Number(oldPrice).toFixed(2);
    	}
    	// 处理数量
    	var quantity = promotionInfo.quantity;
//    	var calcuQuantity = promotionInfo.quantity;
		if(quantity == null || quantity == 'null' || quantity == undefined || quantity == ""){
    		quantity = "0";
//    		calcuQuantity = "1";
    	}else{
    		quantity = Number(quantity).toFixed(0);
//    		calcuQuantity = Number(calcuQuantity).toFixed(0);
    	}
		// 处理实际价格
		var price = promotionInfo.price;
    	if(price == null || price == 'null' || price == undefined || price == ""){
    		price = "0.00";
    	}else{
    		price = Number(price).toFixed(2);
    	}
    	// 处理需要的积分
    	var exPoint = promotionInfo.exPoint;
    	if(exPoint == null || exPoint == 'null' || exPoint == undefined || exPoint == ""){
    		exPoint = "0";
    	}else{
    		exPoint = Number(exPoint).toFixed(0);
    	}
    	// 计算折扣率数据
    	var smartPromotion=$("#smartPromotion").val();
    	var discountRate = "100.00";
    	var discountRatePercent = "100.00%";
    		if("N" == smartPromotion){
    			if(Number(oldPrice) != 0.00){
    				if(Number(oldPrice) >= Number(price) && Number(price) != 0.00){
    					discountRate = (Number(price) / Number(oldPrice) * 100).toFixed(2);
    					discountRatePercent = discountRate + "%";
    				}else{
    					discountRate = "";
    					discountRatePercent = ""
    				}
    			}else{
    				discountRate = "";
    				discountRatePercent = ""
    			}
    		}else{
    			//智能促销接口计算出来的折扣率，直接显示，这里就不做计算了
    			var discount=promotionInfo.discount;
    			if(discount != undefined){
    				discountRate =discount;
    				discountRatePercent = discountRate + "%";
    			}
    		}
    	
    	
    	
    	// 计算实收金额数据
    	var amount = Number(price).toFixed(2);
    	// 计算原金额数据
    	var originalAmount = Number(oldPrice).toFixed(2);
    	
		// 促销记录代号
    	var activityTypeCode = "CXHD";
    	var prtMainBill = "HDZD";
    	var promotionInfoTitle = $("#promotionInfoTitle").val();
    	var promotionCodeTitle = $("#promotionCodeTitle").val();
    	var promotionNameTitle = $("#promotionNameTitle").val();
    	// 根据销售状态确定行样式
    	var trClass = "";
		var saleType = $("#saleType").val();
		if(saleType == "SR"){
			trClass = "red";
		}
    	// 生成序号
    	var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		var rowIndex = parseInt($("#rowCode").val())+1;
		$("#rowCode").val(rowIndex);
		// 追加促销行
		var html = '<tr id="dataRow'+nextIndex+'" class="'+ trClass +'">';
		var discountType=$('#discountType').val();
		html += '<td id="indexNo">'+rowIndex+'</td>';
		html += '<td><span id="spanUnitCode">'+ promotionInfoTitle +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value=""/><input id="unitCode" name="unitCode" type="hidden" value=""/></td>';
		html += '<td><span id="spanBarCode">'+ promotionInfo.mainCode + promotionCodeTitle +'</span><input id="barCode" name="barCode" type="hidden" value="'+ promotionInfo.mainCode +'"/></td>';
		html += '<td><span id="spanProductName">'+ promotionInfo.activityName + promotionNameTitle +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ promotionInfo.activityName +'"/></td>';
		html += '<td><span id="spanPrice">'+ oldPrice +'</span><input id="pricePay" name="pricePay" type="hidden" value="'+ oldPrice +'"/></td>';
		if($("#useMemberPrice").val() == "Y"){
			html += '<td><span id="spanMemberPrice"></span><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
		}
		if($("#isPlatinumPrice").val()=="Y"){
			html += '<td><span id="spanPlatinumPrice"></span><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
		}
		html += '<td class="center"><button id="btnLessen" class="wp_minus" onclick="BINOLWPSAL02.lessenQuantity(this);return false;"></button>'
				+'<input id="promotionQuantity" name="promotionQuantity" type="text" class="text" style="width:50px;margin:.2em 0;" value="1" maxlength="4" onkeyup="BINOLWPSAL02.changeQuantity(this)"/>'
				+'<input id="quantityuArr" name="quantityuArr" type="hidden" value="'+ quantity +'"/>'
				+'<input id="groupQuantity" name="groupQuantity" type="hidden" value="'+ quantity +'"/>'
				+'<button id="btnAdd" class="wp_plus" onclick="BINOLWPSAL02.addQuantity(this);return false;"></button></td>';
		html += '<td>'+ discountRatePercent +'<input id="discountRateArr" name="discountRateArr" type="hidden" class="text" style="width:40px;" value="'+ discountRate +'" /></td>';
		html += '<td>'+ price +'<input id="realPriceArr" name="realPriceArr" type="hidden" class="text" style="width:60px;" value="'+ price +'" /></td>';
		html += '<td id="tdAmount">'+ amount +'</td>';
		html += '<td><span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL02.deleteRow(this);return false;"></button></span></td>';
		html += '<td style="display:none">'
		+'<input type="hidden" id="orderId" name="orderId" value="'+orderId+'"/>'
		+'<input type="hidden" id="couponCode" name="couponCode" value=""/>'
		+'<input type="hidden" id="isStock" name="isStock" value=""/>'
		+'<input type="hidden" id="mainCode" name="mainCode" value="'+ promotionInfo.mainCode +'"/>'
		+'<input type="hidden" id="counterActCode" name="counterActCode" value=""/>'
		+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value="'+ activityTypeCode +'"/>'
		+'<input type="hidden" id="activitySign" name="activitySign" value="'+ promotionInfo.activitySign +'"/>'
        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ prtMainBill +'"/>'
        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="' + prtMainBill + '"/>'
        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ oldPrice +'"/>'
        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value="'+ originalAmount +'"/>'
        +'<input type="hidden" id="proType" name="proType" value="'+ promotionInfo.prtType +'"/>'
        +'<input type="hidden" id="exPoint" name="exPoint" value="'+ exPoint +'"/>'
        +'<input type="hidden" id="stockQuantity" name="stockQuantity" value=""/>'
        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
		$("#databody").append(html);
		if("N" == discountType){
			//discountRateArr折扣  realPriceArr实价不可变动
			$("[name='discountRateArr']").attr("readonly","readonly").attr("class","text disabled");
			$("[name='realPriceArr']").attr("readonly","readonly").attr("class","text disabled");
		}
		// 改变交替行样式
		BINOLWPSAL02.changeOddEvenColor();
		// 修改商品明细样式
		$($("#databody >tr")).each(function(){
			if($(this).find("td:first").attr("id") == undefined){
				$(this).addClass("green");
			}
		});
		var rowId = "dataRow"+nextIndex;
		BINOLWPSAL02.bindInput(rowId);
	},
	
	"appendPromotionProduct":function(productInfo){
		// 处理订单ID
    	var orderId = productInfo.orderId;
    	if(orderId == null || orderId == 'null' || orderId == undefined){
    		orderId = "";
    	}
		
		// 是否管理库存
		var isStock = productInfo.isStock;
		if(isStock == null || isStock == 'null' || isStock == undefined || isStock == ""){
			isStock = "0";
		}
		// 库存
		var stock = productInfo.stock;
		if(stock == null || stock == 'null' || stock == undefined || stock == ""){
			stock = "0";
    	}
		
    	// 处理单价
    	var oldPrice = productInfo.oldPrice;
    	if(oldPrice == null || oldPrice == 'null' || oldPrice == undefined || oldPrice == ""){
    		oldPrice = "0.00";
    	}else{
    		oldPrice = Number(oldPrice).toFixed(2);
    	}
    	// 处理数量
    	var quantity = productInfo.quantity;
    	if(quantity == null || quantity == 'null' || quantity == undefined || quantity == ""){
    		quantity = "1";
    	}else{
    		quantity = Number(quantity).toFixed(0);
    	}
    	// 处理实际价格数据
    	var realPrice = productInfo.price;
    	if(realPrice == null || realPrice == 'null' || realPrice == undefined || realPrice == ""){
    		realPrice = "0.00";
    	}else{
    		realPrice = Number(realPrice).toFixed(2);
    	}
    	// 计算折扣率数据
    	var discountRate = "100.00";
    	var discountRatePercent = "100.00%";
    	var smartPromotion=$("#smartPromotion").val();
    	if("N" == smartPromotion){
    		if(Number(oldPrice) != 0.00){
    			if(Number(oldPrice) >= Number(realPrice) && Number(realPrice) != 0.00){
    				discountRate = (Number(realPrice) / Number(oldPrice) * 100).toFixed(2);
    				discountRatePercent = discountRate + "%";
    			}else{
    				discountRate = "";
    				discountRatePercent = ""
    			}
    		}else{
    			discountRate = "";
    			discountRatePercent = ""
    		}
    	}else{
    		//智能促销接口计算出来的折扣率，直接显示，这里就不做计算了
    		var discount=productInfo.discount;
    		if(discount != undefined){
    			discountRate =discount;
    			discountRatePercent = discountRate + "%";
    		}
    	}
    	// 处理需要的积分
    	var exPoint = productInfo.exPoint;
    	if(exPoint == null || exPoint == 'null' || exPoint == undefined || exPoint == ""){
    		exPoint = "0";
    	}else{
    		exPoint = Number(exPoint).toFixed(0);
    	}
    	// 计算实收金额数据
    	var amount = (Number(quantity) * Number(realPrice)).toFixed(2);
    	// 计算原金额数据
    	var originalAmount = (Number(quantity) * Number(oldPrice)).toFixed(2);
    	// 促销记录代号
    	var activityTypeCode = "CXHD";
    	var promotionDetailTitle = $("#promotionDetailTitle").val();
    	// 根据销售状态确定行样式
    	var trClass = "green";
		var saleType = $("#saleType").val();
		if(saleType == "SR"){
			trClass = "red";
		}
    	// 生成序号
    	var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		// 追加促销行
		var html = '<tr id="dataRow'+nextIndex+'" class="'+ trClass +'">';
		var discountType=$('#discountType').val();
		html += '<td></td>';
		html += '<td><span id="spanUnitCode">'+ promotionDetailTitle + productInfo.unitCode +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value="'+ productInfo.unitCode +'"/><input id="unitCode" name="unitCode" type="hidden" value="'+ productInfo.unitCode +'"/></td>';
		html += '<td><span id="spanBarCode">'+ productInfo.barCode +'</span><input id="barCode" name="barCode" type="hidden" value="'+ productInfo.barCode +'"/></td>';
		html += '<td><span id="spanProductName">'+ productInfo.productName +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ productInfo.productName +'"/></td>';
		html += '<td><span id="spanPrice">'+ oldPrice +'</span><input id="pricePay" name="pricePay" type="hidden" value="'+ oldPrice +'"/></td>';
		if($("#useMemberPrice").val() == "Y"){
			html += '<td><span id="spanMemberPrice"></span><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
		}
		if($("#isPlatinumPrice").val()=="Y"){
			html += '<td><span id="spanPlatinumPrice"></span><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
		}
		html += '<td class="center">'
				+'<span id="spanQuantity">'+ quantity +'</span>'
				+'<input id="quantityuArr" name="quantityuArr" type="hidden" value="'+ quantity +'"/>'
				+'<input id="groupQuantity" name="groupQuantity" type="hidden" value="'+ quantity +'"/>'
				+'</td>';
		html += '<td>'+ discountRatePercent +'<input id="discountRateArr" name="discountRateArr" type="hidden" class="text" style="width:40px;" value="'+ discountRate +'" /></td>';
		html += '<td>'+ realPrice +'<input id="realPriceArr" name="realPriceArr" type="hidden" class="text" style="width:60px;" value="'+ realPrice +'" /></td>';
		html += '<td id="tdAmount">'+ amount +'</td>';
		html += '<td></td>';
		html += '<td style="display:none">'
		+'<input type="hidden" id="orderId" name="orderId" value="'+orderId+'"/>'
		+'<input type="hidden" id="couponCode" name="couponCode" value=""/>'
		+'<input type="hidden" id="isStock" name="isStock" value="'+ isStock +'"/>'
		+'<input type="hidden" id="mainCode" name="mainCode" value="'+ productInfo.mainCode +'"/>'
		+'<input type="hidden" id="counterActCode" name="counterActCode" value="'+ productInfo.counterActCode +'"/>'
		+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value="'+ activityTypeCode +'"/>'
		+'<input type="hidden" id="activitySign" name="activitySign" value=""/>'
        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ productInfo.productVendorId +'"/>'
        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+ productInfo.productVendorId +'"/>'
        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ oldPrice +'"/>'
        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value="'+ originalAmount +'"/>'
        +'<input type="hidden" id="proType" name="proType" value="'+ productInfo.proType +'"/>'
        +'<input type="hidden" id="exPoint" name="exPoint" value="'+ exPoint +'"/>'
        +'<input type="hidden" id="stockQuantity" name="stockQuantity" value="'+ stock +'"/>'
        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
		$("#databody").append(html);
		if("N" == discountType){
			//discountRateArr折扣  realPriceArr实价不可变动
			$("[name='discountRateArr']").attr("readonly","readonly").attr("class","text disabled");
			$("[name='realPriceArr']").attr("readonly","readonly").attr("class","text disabled");
		}
	},
	
	"appendCouponOrderInfo":function(couponOrderInfo){
    	// 处理数量
    	var quantity = couponOrderInfo.quantity;
//    	var calcuQuantity = couponOrderInfo.quantity;
		if(quantity == null || quantity == 'null' || quantity == undefined || quantity == ""){
    		quantity = "0";
//    		calcuQuantity = "1";
    	}else{
    		quantity = Number(quantity).toFixed(0);
//    		calcuQuantity = Number(calcuQuantity).toFixed(0);
    	}
		// 处理实收金额数据
		var amount = couponOrderInfo.amount;
    	if(amount == null || amount == 'null' || amount == undefined || amount == ""){
    		amount = "0.00";
    	}else{
    		amount = Number(amount).toFixed(2);
    	}
    	
		// 促销记录代号
    	var activityTypeCode = "CXHD";
    	var prtMainBill = "HDZD";
    	var promotionInfoTitle = $("#promotionInfoTitle").val();
    	var promotionCodeTitle = $("#promotionCodeTitle").val();
    	var promotionNameTitle = $("#promotionNameTitle").val();
    	// 根据销售状态确定行样式
    	var trClass = "";
		var saleType = $("#saleType").val();
		if(saleType == "SR"){
			trClass = "red";
		}
    	// 生成序号
    	var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		var rowIndex = parseInt($("#rowCode").val())+1;
		$("#rowCode").val(rowIndex);
		// 追加促销行
		var html = '<tr id="dataRow'+nextIndex+'" class="'+ trClass +'">';
		var discountType=$('#discountType').val();
		html += '<td id="indexNo">'+rowIndex+'</td>';
		html += '<td><span id="spanUnitCode">'+ promotionInfoTitle +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value=""/><input id="unitCode" name="unitCode" type="hidden" value=""/></td>';
		html += '<td><span id="spanBarCode">'+ couponOrderInfo.mainCode + promotionCodeTitle +'</span><input id="barCode" name="barCode" type="hidden" value="'+ couponOrderInfo.mainCode +'"/></td>';
		html += '<td><span id="spanProductName">'+ couponOrderInfo.activityName + promotionNameTitle +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ couponOrderInfo.activityName +'"/></td>';
		html += '<td></td>';
		html += '<td><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
		if($("#isPlatinumPrice").val()=="Y"){
			html += '<td><span id="spanPlatinumPrice"></span><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
		}
		html += '<td class="center">'
				+'<span id="spanQuantity">1</span>'
				+'<input id="promotionQuantity" name="promotionQuantity" type="hidden" value="1"/>'
				+'<input id="quantityuArr" name="quantityuArr" type="hidden" value="'+ quantity +'"/>'
				+'<input id="groupQuantity" name="groupQuantity" type="hidden" value="'+ quantity +'"/>'
				+'</td>';
		html += '<td></td>';
		html += '<td></td>';
		html += '<td id="tdAmount">'+ amount +'</td>';
		html += '<td><span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL02.deleteRow(this);return false;"></button></span></td>';
		html += '<td style="display:none">'
		+'<input type="hidden" id="orderId" name="orderId" value=""/>'
		+'<input type="hidden" id="couponCode" name="couponCode" value=""/>'
		+'<input type="hidden" id="isStock" name="isStock" value=""/>'
		+'<input type="hidden" id="mainCode" name="mainCode" value="'+ couponOrderInfo.mainCode +'"/>'
		+'<input type="hidden" id="counterActCode" name="counterActCode" value="CPHD"/>'
		+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value="'+ activityTypeCode +'"/>'
		+'<input type="hidden" id="activitySign" name="activitySign" value="'+ couponOrderInfo.activitySign +'"/>'
        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="' + prtMainBill + '"/>'
        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="' + prtMainBill + '"/>'
        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/>'
        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value="'+ amount +'"/>'
        +'<input type="hidden" id="proType" name="proType" value=""/>'
        +'<input type="hidden" id="exPoint" name="exPoint" value=""/>'
        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
		$("#databody").append(html);
		if("N" == discountType){
			//discountRateArr折扣  realPriceArr实价不可变动
			$("[name='discountRateArr']").attr("readonly","readonly").attr("class","text disabled");
			$("[name='realPriceArr']").attr("readonly","readonly").attr("class","text disabled");
		}
		// 改变交替行样式
		BINOLWPSAL02.changeOddEvenColor();
		
		// 修改商品明细样式
		$($("#databody >tr")).each(function(){
			if($(this).find("td:first").attr("id") == undefined){
				$(this).addClass("green");
			}
		});
		
		var rowId = "dataRow"+nextIndex;
		BINOLWPSAL02.bindInput(rowId);
	},
	
	"appendCouponOrderProduct":function(productInfo){
		// 处理订单ID
    	var orderId = productInfo.orderId;
    	if(orderId == null || orderId == 'null' || orderId == undefined){
    		orderId = "";
    	}
    	// 处理Coupon号
    	var couponCode = productInfo.couponCode;
    	if(couponCode == null || couponCode == 'null' || couponCode == undefined){
    		couponCode = "";
    	}
    	// 处理是否管理库存标识
    	var isStock = productInfo.isStock;
    	if(isStock == null || isStock == 'null' || isStock == undefined){
    		isStock = "";
    	}
    	// 处理单价
    	var oldPrice = productInfo.oldPrice;
    	if(oldPrice == null || oldPrice == 'null' || oldPrice == undefined || oldPrice == ""){
    		oldPrice = "0.00";
    	}else{
    		oldPrice = Number(oldPrice).toFixed(2);
    	}
    	// 处理数量
    	var quantity = productInfo.quantity;
    	if(quantity == null || quantity == 'null' || quantity == undefined || quantity == ""){
    		quantity = "1";
    	}else{
    		quantity = Number(quantity).toFixed(0);
    	}
    	// 处理实际价格数据
    	var realPrice = productInfo.price;
    	if(realPrice == null || realPrice == 'null' || realPrice == undefined || realPrice == ""){
    		realPrice = "0.00";
    	}else{
    		realPrice = Number(realPrice).toFixed(2);
    	}
    	// 计算折扣率数据
    	var discountRate = "100.00";
    	var discountRatePercent = "100.00%";
    	if(Number(oldPrice) != 0.00){
    		if(Number(oldPrice) >= Number(realPrice) && Number(realPrice) != 0.00){
    			discountRate = (Number(realPrice) / Number(oldPrice) * 100).toFixed(2);
    			discountRatePercent = discountRate + "%";
    		}else{
    			discountRate = "";
        		discountRatePercent = ""
    		}
    	}else{
    		discountRate = "";
    		discountRatePercent = ""
    	}
    	// 处理需要的积分
    	var exPoint = productInfo.exPoint;
    	if(exPoint == null || exPoint == 'null' || exPoint == undefined || exPoint == ""){
    		exPoint = "0";
    	}else{
    		exPoint = Number(exPoint).toFixed(0);
    	}
    	// 计算实收金额数据
    	var amount = (Number(quantity) * Number(realPrice)).toFixed(2);
    	// 计算原金额数据
    	var originalAmount = (Number(quantity) * Number(oldPrice)).toFixed(2);
    	// 库存
    	var stock = productInfo.stock;
    	// 促销记录代号
    	var activityTypeCode = "CXHD";
    	var promotionDetailTitle = $("#promotionDetailTitle").val();
    	// 根据销售状态确定行样式
    	var trClass = "green";
		var saleType = $("#saleType").val();
		if(saleType == "SR"){
			trClass = "red";
		}
    	// 生成序号
    	var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		// 追加促销行
		var html = '<tr id="dataRow'+nextIndex+'" class="'+ trClass +'">';
		var discountType=$('#discountType').val();
		html += '<td></td>';
		html += '<td><span id="spanUnitCode">'+ promotionDetailTitle + productInfo.unitCode +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value="'+ productInfo.unitCode +'"/><input id="unitCode" name="unitCode" type="hidden" value="'+ productInfo.unitCode +'"/></td>';
		html += '<td><span id="spanBarCode">'+ productInfo.barCode +'</span><input id="barCode" name="barCode" type="hidden" value="'+ productInfo.barCode +'"/></td>';
		html += '<td><span id="spanProductName">'+ productInfo.productName +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ productInfo.productName +'"/></td>';
		html += '<td><span id="spanPrice">'+ oldPrice +'</span><input id="pricePay" name="pricePay" type="hidden" value="'+ oldPrice +'"/></td>';
		if($("#useMemberPrice").val() == "Y"){
			html += '<td><span id="spanMemberPrice"></span><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
		}
		if($("#isPlatinumPrice").val()=="Y"){
			html += '<td><span id="spanPlatinumPrice"></span><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
		}
		html += '<td class="center">'
				+'<span id="spanQuantity">'+ quantity +'</span>'
				+'<input id="quantityuArr" name="quantityuArr" type="hidden" value="'+ quantity +'"/>'
				+'<input id="groupQuantity" name="groupQuantity" type="hidden" value="'+ quantity +'"/>'
				+'</td>';
		html += '<td>'+ discountRatePercent +'<input id="discountRateArr" name="discountRateArr" type="hidden" class="text" style="width:40px;" value="'+ discountRate +'" /></td>';
		html += '<td>'+ realPrice +'<input id="realPriceArr" name="realPriceArr" type="hidden" class="text" style="width:60px;" value="'+ realPrice +'" /></td>';
		html += '<td id="tdAmount">'+ amount +'</td>';
		html += '<td></td>';
		html += '<td style="display:none">'
		+'<input type="hidden" id="orderId" name="orderId" value="'+ orderId +'"/>'
		+'<input type="hidden" id="couponCode" name="couponCode" value="'+ couponCode +'"/>'
		+'<input type="hidden" id="isStock" name="isStock" value="'+ isStock +'"/>'
		+'<input type="hidden" id="mainCode" name="mainCode" value="'+ productInfo.mainCode +'"/>'
		+'<input type="hidden" id="counterActCode" name="counterActCode" value="'+ productInfo.counterActCode +'"/>'
		+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value="' + activityTypeCode + '"/>'
		+'<input type="hidden" id="activitySign" name="activitySign" value=""/>'
        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ productInfo.productVendorId +'"/>'
        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+ productInfo.productVendorId +'"/>'
        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ oldPrice +'"/>'
        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value="'+ originalAmount +'"/>'
        +'<input type="hidden" id="proType" name="proType" value="'+ productInfo.proType +'"/>'
        +'<input type="hidden" id="exPoint" name="exPoint" value="'+ exPoint +'"/>'
        +'<input type="hidden" id="stockQuantity" name="stockQuantity" value="'+ stock +'"/>'
        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
		$("#databody").append(html);
		if("N" == discountType){
			//discountRateArr折扣  realPriceArr实价不可变动
			$("[name='discountRateArr']").attr("readonly","readonly").attr("class","text disabled");
			$("[name='realPriceArr']").attr("readonly","readonly").attr("class","text disabled");
		}
	},
	
	// 按钮选中时更改样式
	"changeButtonCss":function(obj){
		var $this = $(obj);
		$("#buttonPageDiv").find(".btn_top_selected").attr("class","btn_top");
		$this.attr("class","btn_top_selected");
	},
	
	"showMessageDialog":function (dialogSetting){
		if(dialogSetting.type == "MESSAGE"){
			$("#messageContent").show();
			$("#successContent").hide();
			$("#messageContentSpan").text(dialogSetting.message);
		}else{
			$("#messageContent").hide();
			$("#successContent").show();
			$("#successContentSpan").text(dialogSetting.message);
		}
		var $dialog = $('#messageDialogDiv');
		$dialog.dialog({ 
			//默认不打开弹出框
			autoOpen: false,  
			//弹出框宽度
			width: 400, 
			//弹出框高度
			height: 200, 
			//弹出框标题
			title:$("#messageDialogTitle").text(), 
			//弹出框索引
			zIndex: 99,  
			modal: true, 
			resizable:false,
			//关闭按钮
			close: function() {
				closeCherryDialog("messageDialogDiv");
				if(typeof dialogSetting.focusEvent == "function") {
					dialogSetting.focusEvent();
				}
			}
		});
		$dialog.dialog("open");
		// 给确认按钮绑定事件
		$("#btnMessageConfirm").bind("click", function(){
			BINOLWPSAL02.messageConfirm(dialogSetting.focusEvent);
		});
	},
	
	"messageConfirm":function (focusEvent){
		closeCherryDialog("messageDialogDiv");
		if(typeof focusEvent == "function") {
			focusEvent();
		}
	},
	// 点击充值按钮打开输入卡号充值或开卡
//	"recharge":function(){
//		var searchStr = $("#searchStr").val();
//		if(searchStr!=""){
//			BINOLWPSAL13.confirm(searchStr);
//			return false;
//		}else{
//			var dialogSetting = {
//				dialogInit: "#dialogInit",
//				width: 500,
//				height: 400,
//				title: $("#rechargeDialogTitle").text(),
//				closeEvent:function(){
//					removeDialog("#dialogInit");
//				}
//			};
//			openDialog(dialogSetting);
//			var initUrl = $("#initUrl").attr("href");
//			cherryAjaxRequest({
//				url: initUrl,
//				callback: function(data) {
//					$("#dialogInit").html(data);
//					$("#dgCardCode").focus();
//					$("#dgCardCode").val($("#memberCode").val());
//				}
//			});
//		}
//	},
//	// 输入卡号界面
//	"getCardCode":function(){
//		var getCardCodeUrl = $("#cardCodeUrl").attr("href");
//		var dialogSetting = {
//				dialogInit: "#dialogInit",
//				width: 500,
//				height: 300,
//				title: $("#cardCode").text(),
//				closeEvent:function(){
//					removeDialog("#dialogInit");
//				}
//			};
//			openDialog(dialogSetting);
//			cherryAjaxRequest({
//				url: getCardCodeUrl,
//				param: null,
//				callback: function(data) {
//					$("#dialogInit").html(data);
//				}
//			});
//	},
	
	// 获取消费卡信息
	"getConsumptionCode":function(){
		var consumptionCodeUrl = $("#consumptionCode").attr("href");
		var memCode = $("#memberCode").val();
		var memberInfoId = $("#memberInfoId").val();
		consumptionCodeUrl+="?memCode="+memCode+"&memberInfoId="+memberInfoId;
		closeCherryDialog('dialogInit');
			cherryAjaxRequest({
				url: consumptionCodeUrl,
				param: getSerializeToken(),
				callback: function(data) {
					if(data=="NOEXIST"){
						BINOLWPSAL02.showMessageDialog({
							message:"该会员还未开通储值卡", 
							type:"MESSAGE"
						});
						return false;
					}else if(data=="Failure"){
						BINOLWPSAL02.showMessageDialog({
							message:"查询储值卡信息失败", 
							type:"MESSAGE"
						});
						return false;
					}else if(data=="ERROR"){
						BINOLWPSAL02.showMessageDialog({
							message:"很抱歉，接口连接失败！", 
							type:"MESSAGE"
						});
						return false;
					}else if(data=="SUCCESS"){
						var text = $("#CZKMessage").html();
						var title = $("#CZKTitle").text();
						var dialogSetting = {
								dialogInit: "#dialogInit",
								text: text,
								width: 	500,
								height: 300,
								title: 	title,
								confirm: $("#dialogConfirm").text(),
								cancel: $("#dialogCancel").text(),
								confirmEvent: function(){BINOLWPSAL02.relation();},
								cancelEvent: function(){removeDialog("#dialogInit");}
							};
							openDialog(dialogSetting);
						return false;
					}else {
						var dialogSetting = {
							dialogInit: "#dialogInit",
							width: 1200,
							height: 600,
							title: "储值卡信息",
							closeEvent:function(){
								removeDialog("#dialogInit");
							}
						};
						openDialog(dialogSetting);
						$("#dialogInit").html(data);
						$("#table_all tr:odd").not(".warningTRbgColor,.green").attr("class","even");
						$("#table_all tr:even").not(".warningTRbgColor,.green").attr("class","odd");
					}
				}
			});
	},
	"relation":function(){
		var url = $("#dgRelation").attr("href");
		var memCode = $("#memberCode").val();
		var mobilePhone=$("#mobilePhone").val();
		url += "?memCode="+memCode+"&mobilePhone="+mobilePhone;
		cherryAjaxRequest({
			url:url,
			param:getSerializeToken(),
			callback:function(data){
				closeCherryDialog('dialogInit');
				if(data=="SUCCESS"){
					BINOLWPSAL02.showMessageDialog({
						message:"关联成功！", 
						type:"SUCCESS",
						focusEvent:function(){
							BINOLWPSAL02.getConsumptionCode()
						}
					});
				}else if(data=="NOCARDID"){
					BINOLWPSAL02.showMessageDialog({
						message:"未找到相关储值卡！", 
						type:"MESSAGE"
					});
				}else if(data=="NOMEMBER"){
					BINOLWPSAL02.showMessageDialog({
						message:"未找到相关会员卡号！", 
						type:"MESSAGE"
					});
				}else if(data=="MULTIPLE"){
					BINOLWPSAL02.showMessageDialog({
						message:"查到多个会员，无法绑定！", 
						type:"MESSAGE"
					});
				}else {
					BINOLWPSAL02.showMessageDialog({
						message:"关联失败！", 
						type:"MESSAGE"
					});
				}
			}
		});
	},
	"keyupBusinessDate":function (obj){
		var inputValue = $(obj).val();
		var minValue = $("#businessBeginDate").val();
		var maxValue = $("#nowDate").val();
		var inputDate = new Date(inputValue);
		var minDate = new Date(minValue);
		var maxDate = new Date(maxValue);
		if(inputDate < minDate){
			$("#businessDate").val(minValue);
		}else if(inputDate > maxDate){
			$("#businessDate").val(maxValue);
		}
	},
	"deleteLastPro":function(){
		if($("#mainTable tr").length - 2 <0){
			return false;
		}
		var length=$("#mainTable tr").length-2;
		var deleteTr=$("#mainTable tr:eq("+length+")");
		var delButtonLength=deleteTr.find(".wp_del").length;
		if(delButtonLength == null || delButtonLength == 'null' || delButtonLength == undefined || delButtonLength =='' ||delButtonLength == 0){
			length=$("#mainTable tr").length-3;
			deleteTr=$("#mainTable tr:eq("+length+")");
			var delButton=deleteTr.find(".wp_del");
			delButton.click();
		}else{
			var delButton=deleteTr.find(".wp_del");
			delButton.click();
		}
	},
	//智能促销规则
	"getMatchRuleFun":function(){
		var saleDetailList=$('#saleDetailList').val();
		var baCode=$('#baCode').val();
		var memberCode=$('#memberCode').val();
		var totalDiscountRate = $("#mainForm #totalDiscountRate").val();
		var memberLevel=$("#memberLevel").val();
		var params="saleDetailList="+saleDetailList+"&baCode="+baCode+"&memberCode="+memberCode+"&memberLevel="+memberLevel;
		var initMatchRuleUrl=$("#initMatchRule").attr("href");
		var MatchRuleUrl=$('#getMatchRule').attr("href");
		var resultflag;
		cherryAjaxRequest({
			url: MatchRuleUrl,
			param: params,
			callback: function(data) {
				//智能促销去除最低折扣率限制
				$("#minDiscount").removeAttr("value");
				if(data == null || data == "" || data == undefined){
					BINOLWPSAL02.collect();
					return;
				}
				var param_map = eval("("+data+")");
				//标识后端有没有对应的规则返回，没有的话直接跳转收款
				resultflag=param_map.resultflag;
				if(resultflag == "0"){
					BINOLWPSAL02.collect();
					return;
				}else{
					// 删除销售单据中的打折明细行
					$("#btnCollect").attr("class","btn_top_disable");
					$("#btnCollect").attr("disabled","disabled");
					$("#btnDiscount").attr("class","btn_top_disable");
					$("#btnDiscount").attr("disabled","disabled");
					$("#btnHangBills").attr("class","btn_top_disable");
					$("#btnHangBills").attr("disabled","disabled");
					BINOLWPSAL02.deleteDiscountRow();
					var dialogSetting = {
						dialogInit: "#dialogInit",
						width: 500,
						height: 350,
						title: $("#initMatchRule_CloudPos").text(),
						closeEvent:function(){
							//清空之前的促销列表,替换为之前报错的html信息
							BINOLWPSAL02.removePromotionList();
							// 最后一个文本框获得焦点
							BINOLWPSAL02.lastInputSelect();
							// 可见文本框回车事件解绑
							$("#collectPageDiv").find("input:text:visible").unbind();
							// 关闭弹出窗口
							removeDialog("#dialogInit");
							// 解除退货和补登按钮禁用
							$("#btnReturnsGoods").removeAttr("disabled");
							$("#btnReturnsGoods").attr("class","btn_top");
							$("#btnAddHistoryBill").removeAttr("disabled");
							$("#btnAddHistoryBill").attr("class","btn_top");
							// 解除清空购物车禁用
							$("#btnEmptyShoppingCart").removeAttr("disabled");
							$("#btnEmptyShoppingCart").attr("class","btn_top");
							//解除收款按钮禁用
							$("#btnCollect").removeAttr("disabled");
							$("#btnCollect").attr("class","btn_top");
							$("#btnCollect").removeAttr("disabled");
							//解除挂单/提单
							$("#btnHangBills").removeAttr("disabled");
							$("#btnHangBills").attr("class","btn_top");
							$("#btnHangBills").removeAttr("disabled");
						}
					};
					openDialog(dialogSetting);
					cherryAjaxRequest({
						url: initMatchRuleUrl,
						param: params,
						callback: function(data) {
							$("#dialogInit").html(data);
							BINOLWPSAL08_1.appendNewRow();
						}
					});
				}
			}
		});
	},
	//实现智能促销
	"actionSmartPromotion":function(){
		// 将销售明细列表转换成Json字符串
		BINOLWPSAL02.getSaleDetailList();
		// 删除空行
		BINOLWPSAL02.deleteEmptyRow();
		BINOLWPSAL02.getSaleDetailList_promotion();
		//添加一个空行
		BINOLWPSAL02.addNewLine();
		var saleDetailList=$('#saleDetailList').val();
		var baCode=$('#baCode').val();
		var memberCode=$('#memberCode').val();
		var totalDiscountRate = $("#mainForm #totalDiscountRate").val();
		var memberLevel=$("#memberLevel").val();
		var params="saleDetailList="+saleDetailList+"&baCode="+baCode+"&memberCode="+memberCode+"&memberLevel="+memberLevel;
		var initMatchRuleUrl=$("#initMatchRule").attr("href");
		var MatchRuleUrl=$('#getMatchRule').attr("href");
		var resultflag;
		cherryAjaxRequest({
			url: MatchRuleUrl,
			param: params,
			callback: function(data) {
				//智能促销去除最低折扣率限制
				$("#minDiscount").removeAttr("value");
				if(data == null || data == "" || data == undefined){
					BINOLWPSAL02.collect();
					return;
				}
				var param_map = eval("("+data+")");
				//标识后端有没有对应的规则返回，没有的话直接跳转收款
				resultflag=param_map.resultflag;
				if(resultflag == "0"){
					BINOLWPSAL02.collect();
					return;
				}else{
					// 删除销售单据中的打折明细行
					$("#btnCollect").attr("class","btn_top_disable");
					$("#btnCollect").attr("disabled","disabled");
					$("#btnDiscount").attr("class","btn_top_disable");
					$("#btnDiscount").attr("disabled","disabled");
					$("#btnHangBills").attr("class","btn_top_disable");
					$("#btnHangBills").attr("disabled","disabled");
					BINOLWPSAL02.deleteDiscountRow();
					var dialogSetting = {
						dialogInit: "#dialogInit",
						width: 500,
						height: 350,
						title: $("#initMatchRule_CloudPos").text(),
						closeEvent:function(){
							//清空之前的促销列表,替换为之前报错的html信息
							BINOLWPSAL02.removePromotionList();
							// 最后一个文本框获得焦点
							BINOLWPSAL02.lastInputSelect();
							// 可见文本框回车事件解绑
							$("#collectPageDiv").find("input:text:visible").unbind();
							// 关闭弹出窗口
							removeDialog("#dialogInit");
							// 解除退货和补登按钮禁用
							$("#btnReturnsGoods").removeAttr("disabled");
							$("#btnReturnsGoods").attr("class","btn_top");
							$("#btnAddHistoryBill").removeAttr("disabled");
							$("#btnAddHistoryBill").attr("class","btn_top");
							// 解除清空购物车禁用
							$("#btnEmptyShoppingCart").removeAttr("disabled");
							$("#btnEmptyShoppingCart").attr("class","btn_top");
							//解除收款按钮禁用
							$("#btnCollect").removeAttr("disabled");
							$("#btnCollect").attr("class","btn_top");
							$("#btnCollect").removeAttr("disabled");
							//解除挂单/提单
							$("#btnHangBills").removeAttr("disabled");
							$("#btnHangBills").attr("class","btn_top");
							$("#btnHangBills").removeAttr("disabled");
						}
					};
					openDialog(dialogSetting);
					cherryAjaxRequest({
						url: initMatchRuleUrl,
						param: params,
						callback: function(data) {
							$("#dialogInit").html(data);
							BINOLWPSAL08_1.appendNewRow();
						}
					});
				}
			}
		});
	}

};

var BINOLWPSAL02 = new BINOLWPSAL02_GLOBAL();

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

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLWPSAL02.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLWPSAL02.needUnlock){
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
			baCode: {required: true},
			businessDate: {required: true, dateValid: true}
		}
	});
	// 初始化日历控件
	$("#businessDate").cherryDate({
		onSelectEvent: function(input){
			// 最后一行第一个可见的文本框获得焦点
			BINOLWPSAL02.firstInputSelect();
		},
		beforeShow: function(input){
			var minValue = $("#businessBeginDate").val();
			var maxValue = $("#nowDate").val();
			return [minValue, maxValue];
		}
	});
	// 营业员员下拉框绑定
	var option = {
		elementId:"baName",
		targetId:"baCode",
		showNum:20
	};
	baBinding(option);
	var new_Czk_Pay = $("#new_Czk_Pay").val();
	if("Y"!=new_Czk_Pay){
		$("#recharge").hide();
	}
	// 页面绑定F1到F8快捷键
	$(document).bind("keydown", "f1", function (e) { e.preventDefault();BINOLWPSAL02.initMatchRule_CloudPos(); })
		.bind("keydown", "f2", function (e) { e.preventDefault();BINOLWPSAL02.discount(); })
		.bind("keydown", "f3", function (e) { e.preventDefault();BINOLWPSAL02.hangBills(); })
		.bind("keydown", "f4", function (e) { e.preventDefault();BINOLWPSAL02.searchBills(); })
		.bind("keydown", "f5", function (e) { e.preventDefault();BINOLWPSAL02.showMember(); })
		.bind("keydown", "f6", function (e) { e.preventDefault();BINOLWPSAL02.emptyShoppingCart(); })
		.bind("keydown", "f7", function (e) { e.preventDefault();BINOLWPSAL02.returnsGoods(); })
		.bind("keydown", "f8", function (e) { e.preventDefault();BINOLWPSAL02.addHistoryBill(); })
		.bind("keydown", "f9", function (e) { if("Y"==new_Czk_Pay && "Y"==$("#rechargeAndOpendCardButton").val()){ e.preventDefault();BINOLWPSAL13.confirm(); } })
		.bind("keydown", "i", function (e) { e.preventDefault();BINOLWPSAL02.deleteLastPro(); });
	// 营业员选择框绑定F1到F8快捷键
	$("#baName").bind("keydown", "f1", function (e) { e.preventDefault();BINOLWPSAL02.initMatchRule_CloudPos(); })
		.bind("keydown", "f2", function (e) { e.preventDefault();BINOLWPSAL02.discount(); })
		.bind("keydown", "f3", function (e) { e.preventDefault();BINOLWPSAL02.hangBills(); })
		.bind("keydown", "f4", function (e) { e.preventDefault();BINOLWPSAL02.searchBills(); })
		.bind("keydown", "f5", function (e) { e.preventDefault();BINOLWPSAL02.showMember(); })
		.bind("keydown", "f6", function (e) { e.preventDefault();BINOLWPSAL02.emptyShoppingCart(); })
		.bind("keydown", "f7", function (e) { e.preventDefault();BINOLWPSAL02.returnsGoods(); })
		.bind("keydown", "f8", function (e) { e.preventDefault();BINOLWPSAL02.addHistoryBill(); })
		.bind("keydown", "f9", function (e) { if("Y"==new_Czk_Pay && "Y"==$("#rechargeAndOpendCardButton").val()){ e.preventDefault();BINOLWPSAL13.confirm(); } })
		.bind("keydown", "i", function (e) { e.preventDefault();BINOLWPSAL02.deleteLastPro(); });
	// 输入框绑定F1到F8快捷键
	$(".titleTools").bind("keydown", "f1", function (e) { e.preventDefault();BINOLWPSAL02.initMatchRule_CloudPos(); })
		.bind("keydown", "f2", function (e) { e.preventDefault();BINOLWPSAL02.discount(); })
		.bind("keydown", "f3", function (e) { e.preventDefault();BINOLWPSAL02.hangBills(); })
		.bind("keydown", "f4", function (e) { e.preventDefault();BINOLWPSAL02.searchBills(); })
		.bind("keydown", "f5", function (e) { e.preventDefault();BINOLWPSAL02.showMember(); })
		.bind("keydown", "f6", function (e) { e.preventDefault();BINOLWPSAL02.emptyShoppingCart(); })
		.bind("keydown", "f7", function (e) { e.preventDefault();BINOLWPSAL02.returnsGoods(); })
		.bind("keydown", "f8", function (e) { e.preventDefault();BINOLWPSAL02.addHistoryBill(); })
		.bind("keydown", "f9", function (e) { if("Y"==new_Czk_Pay && "Y"==$("#rechargeAndOpendCardButton").val()){ e.preventDefault();BINOLWPSAL13.confirm(); } })
		.bind("keydown", "i", function (e) { e.preventDefault();BINOLWPSAL02.deleteLastPro(); });
	// 营业员输入框绑定回车键
	$("#baName").bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			// 会员搜索输入框获得焦点
			$("#searchStr").focus();
		}
	});
	// 会员输入框绑定回车键
	$("#searchStr").bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			BINOLWPSAL02.search();
		}
	});
	//如果存在从会员查询中带来的会员卡号，直接执行会员查询方法
	var mobilePhoneQ=$("#mobilePhoneQ").val();
	if(mobilePhoneQ){
		$("#searchStr").val(mobilePhoneQ);
		$("#btnSearch").click();
	}
	$("#btnCollect").attr("disabled",true);
	$("#btnDiscount").attr("disabled",true);
	$("#btnSearchBills").removeAttr("disabled");
	$("#btnCollect").attr("class","btn_top_disable");
	$("#btnDiscount").attr("class","btn_top_disable");
	$("#btnSearchBills").attr("class","btn_top");
	$("#businessDate").attr("disabled",true);
	$("#searchPageDiv").find(".ui-datepicker-trigger").attr("disabled",true);
	
	var baChooseModel = $("#baChooseModel").val();
	if(baChooseModel == "2"){
		// BA选择项唯一的情况下默认选中
		if($("#baCode option").size() == 2){
			$("#baCode option").eq(1).attr("selected",true);
		}
	}
	
	if($("#rechargeAndOpendCardButton").val()=='N'){
		$("#recharge").hide();
	}
	// 初始化时先添加一个空行
	BINOLWPSAL02.addNewLine();
	
	if(baChooseModel != "2"){
		// 初始化时营业员输入框获得焦点
		$("#baName").focus();
	}
	// 加载促销
	var setParams = {};
	BINOLWPSAL02.getPromotion(setParams);
	BINOLWPSAL02.needUnlock = true;
});


