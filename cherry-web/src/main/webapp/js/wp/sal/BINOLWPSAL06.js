var WPCOM_dialogBody ="";
var BINOLWPSAL06_GLOBAL = function() {

};

BINOLWPSAL06_GLOBAL.prototype = {
	// 查询挂单记录
	"search" : function() {
		var $form = $('#getBillsForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "billCode", "sWidth" : "20%"},
		                    {"sName" : "businessDate", "sWidth" : "18%"},
		                    {"sName" : "hangTime", "sWidth" : "18%" ,"bVisible" : false},
		                    {"sName" : "memberCode", "sWidth" : "15%"},
		                    {"sName" : "baName", "sWidth" : "10%"},
		                    {"sName" : "quantity", "sWidth" : "8%"},
		                    {"sName" : "amount", "sWidth" : "10%"},
		                    {"sName" : "act", "sWidth" : "10%", "bSortable": false}
		                    ];
		var counterCode = $("#dgCounterCode").val();
		var url = $("#dgSearchUrl").attr("href");
		var params= $("#getBillsForm").serialize();
		params += "&counterCode=" + counterCode + "&" + getSerializeToken();
		url = url + "?" + params;
		
		// 表格设置
		var tableSetting = {
			// datatable 对象索引
			index : 2,
			// 表格ID
			tableId : "#unfinishedBillsTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 2, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 2 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			// 默认显示行数
			iDisplayLength : 5,
			fnDrawCallback : function() {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	// 提单
	"getBill":function(hangBillId){
		var baChooseModel = $("#baChooseModel").val();
		var dgGetBillUrl = $('#dgGetBillUrl').attr("href");
		var params = "hangBillId=" + hangBillId;
		cherryAjaxRequest({
			url:dgGetBillUrl,
			param:params,
			callback: function(data) {
				// 还原按钮样式
				$("#btnHangBills").attr("class","btn_top");
				if(data == "ERROR"){
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"提单失败", 
						type:"MESSAGE", 
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
				}else{
					var resultInfo = eval("("+data+")");
					var billInfo = resultInfo.billInfo;
					var billDetailList = resultInfo.billDetailList;
					// 处理整单原金额
			    	var totalOriginalAmount = billInfo.originalAmount;
			    	if(totalOriginalAmount == null || totalOriginalAmount == 'null' || totalOriginalAmount == undefined || totalOriginalAmount == ""){
			    		totalOriginalAmount = "";
			    	}else{
			    		totalOriginalAmount = Number(totalOriginalAmount).toFixed(2);
			    	}
					// 处理整单折扣率
			    	var totalDiscountRate = billInfo.totalDiscountRate;
			    	if((totalDiscountRate == null || totalDiscountRate == 'null' || totalDiscountRate == undefined || totalDiscountRate == "") && totalDiscountRate != 0.00){
			    		totalDiscountRate = "";
			    	}else{
			    		totalDiscountRate = Number(totalDiscountRate).toFixed(2);
			    	}
			    	// 处理整单去零金额
			    	var roundingAmount = billInfo.roundingAmount;
			    	if(roundingAmount == null || roundingAmount == 'null' || roundingAmount == undefined || roundingAmount == ""){
			    		roundingAmount = "";
			    	}else{
			    		roundingAmount = Number(roundingAmount).toFixed(2);
			    	}
			    	// 处理整单数量
			    	var totalQuantity = billInfo.totalQuantity;
			    	if(totalQuantity == null || totalQuantity == 'null' || totalQuantity == undefined || totalQuantity == ""){
			    		totalQuantity = "";
			    	}else{
			    		totalQuantity = Number(totalQuantity).toFixed(0);
			    	}
			    	// 处理整单实收金额
			    	var totalAmount = billInfo.totalAmount;
			    	if(totalAmount == null || totalAmount == 'null' || totalAmount == undefined || totalAmount == ""){
			    		totalAmount = "";
			    	}else{
			    		totalAmount = Number(totalAmount).toFixed(2);
			    	}
					// 判断单据是否为退货单据的情况
			    	if(billInfo.saleType == "SR"){
						$('#txtSaleType').text($('#saleTypeSR').val());
					}else{
						$('#txtSaleType').text($('#saleTypeNS').val());
					}
			    	// 判断单据是否为补登单据的情况
			    	if(billInfo.ticketType == "LA"){
						// 更改业务状态并显示状态提示
						$("#businessState").val("H");
						$("#txtAddHistoryBill").show();
						// 启用日期选择控件
						$("#businessDate").removeAttr("disabled");
						$("#searchPageDiv").find(".ui-datepicker-trigger").removeAttr("disabled");
					}else{
						// 更改业务状态并隐藏状态提示
						$("#businessState").val("N");
						$("#txtAddHistoryBill").hide();
						// 禁用日期选择控件
						$("#businessDate").attr("disabled",true);
						$("#searchPageDiv").find(".ui-datepicker-trigger").attr("disabled",true);
					}
			    	$('#saleType').val(billInfo.saleType);
					$('#spanBillCode').html(billInfo.billCode);
					$('#memberInfoId').val(billInfo.memberInfoId);
					$('#memberCode').val(billInfo.memberCode);
					$('#memberName').val(billInfo.memberName);
					$('#memberLevel').val(billInfo.memberLevel);
					$('#mobilePhone').val(billInfo.mobilePhone);
					$('#changablePoint').val(billInfo.changablePoint);
					$('#originalAmount').val(totalOriginalAmount);
					$('#totalDiscountRate').val(totalDiscountRate);
					$('#roundingAmount').val(roundingAmount);
					$('#baCode').val(billInfo.baCode);
					if(baChooseModel != "2"){
						$('#baName').val(billInfo.baName);
					}
					$('#businessDate').val(billInfo.businessDate);
					$('#searchStr').val(billInfo.searchStr);
					$("#spanTotalQuantity").html(totalQuantity);
					$("#spanTotalAmount").html(totalAmount);
					$('#totalQuantity').val(totalQuantity);
					$('#totalAmount').val(totalAmount);
					$('#billCode').val(billInfo.billCode);
					$('#saleDetailList').val(billInfo.saleDetailList);
					$('#promotionList').val(billInfo.promotionList);
					// 加载全部促销活动
					var setParams = {};
					setParams.showType = "ALL";
					setParams.memberInfoId = billInfo.memberInfoId;
					setParams.mobilePhone = billInfo.mobilePhone;
					setParams.changablePoint = billInfo.changablePoint;
					setParams.afterGetPromotionEvent = function(){
						// 清空现有行
						$.each($('#databody >tr'), function(i){
							if(i >= 0){
								$(this).remove();
							}
						});
						// 显示明细数据
						$.each(billDetailList, function(j){
							// 追加新行
							BINOLWPSAL06.appendNewRow(billDetailList[j]);
						});
						// 提单结束后更改主页按钮样式
						$("#btnCollect").removeAttr("disabled");
						$("#btnDiscount").removeAttr("disabled");
						$("#btnSearchBills").attr("disabled",true);
						$("#btnCollect").attr("class","btn_top");
						$("#btnDiscount").attr("class","btn_top");
						$("#btnSearchBills").attr("class","btn_top_disable");
						
						// 设置单据分类
						BINOLWPSAL02.setBillClassify();
						// 添加一个空行
						BINOLWPSAL02.addNewLine();
						// 显示会员信息
						if(billInfo.memberInfoId != null && billInfo.memberInfoId != 'null' && billInfo.memberInfoId != undefined && billInfo.memberInfoId != ""){
							$('#txtMemberName').text(billInfo.memberName);
							$('#spanMemberCode').text(billInfo.memberCode);
							$('#spanMemberName').text(billInfo.memberName);
							$('#spanTotalPoint').text(billInfo.totalPoint);
							$('#spanChangablePoint').text(billInfo.changablePoint);
							$('#spanJoinDate').text(billInfo.joinDate);
							$('#spanLastSaleDate').text(billInfo.lastSaleDate);
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
						}
						if($("#new_Czk_Pay"=="Y")){
							$("#newCzkPay").show();
						}
					}
					BINOLWPSAL02.getPromotion(setParams);
				}
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
			}
		});
	},
	
	// 提单时增加新行
    "appendNewRow":function(billDetailInfo){
    	// 处理订单ID
    	var orderId = billDetailInfo.orderId;
    	if(orderId == null || orderId == 'null' || orderId == undefined){
    		orderId = "";
    	}
    	// 处理订单ID
    	var couponCode = billDetailInfo.couponCode;
    	if(couponCode == null || couponCode == 'null' || couponCode == undefined){
    		couponCode = "";
    	}
    	// 处理订单ID
    	var isStock = billDetailInfo.isStock;
    	if(isStock == null || isStock == 'null' || isStock == undefined){
    		isStock = "";
    	}
    	// 处理子活动编码
    	var activityCode = billDetailInfo.activityCode;
    	if(activityCode == null || activityCode == 'null' || activityCode == undefined){
    		activityCode = "";
    	}
    	// 处理柜台活动编码
    	var counterActCode = billDetailInfo.counterActCode;
    	if(counterActCode == null || counterActCode == 'null' || counterActCode == undefined){
    		counterActCode = "";
    	}
    	// 处理厂商编码
    	var unitCode = billDetailInfo.unitCode;
    	if(unitCode == null || unitCode == 'null' || unitCode == undefined){
    		unitCode = "";
    	}
    	// 处理产品条码
    	var barCode = billDetailInfo.barCode;
    	if(barCode == null || barCode == 'null' || barCode == undefined){
    		barCode = "";
    	}
    	// 处理产品名称
    	var productName = billDetailInfo.productName;
    	if(productName == null || productName == 'null' || productName == undefined){
    		productName = "";
    	}
    	// 处理单价
    	var price = billDetailInfo.price;
    	if((price == null || price == 'null' || price == undefined || price == "") && price != 0.00){
    		price = "";
    	}else{
    		price = Number(price).toFixed(2);
    	}
    	// 处理会员价
    	var memberPrice = billDetailInfo.memberPrice;
    	if((memberPrice == null || memberPrice == 'null' || memberPrice == undefined || memberPrice == "") && memberPrice != 0.00){
    		memberPrice = "";
    	}else{
    		memberPrice = Number(memberPrice).toFixed(2);
    	}
    	// 处理白金价
    	if($("#isPlatinumPrice").val()=="Y"){
	    	var platinumPrice = billDetailInfo.platinumPrice;
	    	if((platinumPrice == null || platinumPrice == 'null' || platinumPrice == undefined || platinumPrice == "") && platinumPrice != 0.00){
	    		platinumPrice = "";
	    	}else{
	    		platinumPrice = Number(platinumPrice).toFixed(2);
	    	}
    	}
    	// 处理输入数量
    	var quantity = billDetailInfo.quantity;
    	if(quantity == null || quantity == 'null' || quantity == undefined || quantity == ""){
    		quantity = "";
    	}else{
    		quantity = Number(quantity).toFixed(0);
    	}
    	// 处理实际数量
    	var realQuantity = billDetailInfo.realQuantity;
    	if(realQuantity == null || realQuantity == 'null' || realQuantity == undefined || realQuantity == ""){
    		realQuantity = "";
    	}else{
    		realQuantity = Number(realQuantity).toFixed(0);
    	}
    	
    	// 处理库存数量
    	var stockQuantity = billDetailInfo.stockQuantity;
    	if(stockQuantity == null || stockQuantity == 'null' || stockQuantity == undefined || stockQuantity == ""){
    		stockQuantity = "";
    	}
    	// 处理组数量
    	var groupQuantity = billDetailInfo.groupQuantity;
    	if(groupQuantity == null || groupQuantity == 'null' || groupQuantity == undefined || groupQuantity == ""){
    		groupQuantity = "";
    	}else{
    		groupQuantity = Number(groupQuantity).toFixed(0);
    	}
    	// 处理折扣率数据
    	var discountRate = billDetailInfo.discountRate;
    	var discountRateValue = billDetailInfo.discountRate;
    	if((discountRate == null || discountRate == 'null' || discountRate == undefined || discountRate == "") && discountRate != 0.00){
    		discountRate = "";
    		discountRateValue = "";
    	}else{
    		discountRate = Number(discountRate).toFixed(2);
    		discountRateValue = Number(discountRateValue).toFixed(2);
    	}
    	// 处理折扣后价格数据
    	var realPrice = billDetailInfo.realPrice;
    	if((realPrice == null || realPrice == 'null' || realPrice == undefined || realPrice == "") && realPrice != 0.00){
    		realPrice = "";
    	}else{
    		realPrice = Number(realPrice).toFixed(2);
    	}
    	// 处理实收金额数据
    	var amount = billDetailInfo.amount;
    	if((amount == null || amount == 'null' || amount == undefined || amount == "") && amount != 0.00){
    		amount = "";
    	}else{
    		amount = Number(amount).toFixed(2);
    	}
    	// 处理原金额数据
    	var originalAmount = billDetailInfo.originalAmount;
    	if((originalAmount == null || originalAmount == 'null' || originalAmount == undefined || originalAmount == "") && originalAmount != 0.00){
    		originalAmount = "";
    	}else{
    		originalAmount = Number(originalAmount).toFixed(2);
    	}
    	// 处理折扣记录代号数据
    	var activityTypeCode = billDetailInfo.activityTypeCode;
    	if(activityTypeCode == null || activityTypeCode == 'null' || activityTypeCode == undefined){
    		activityTypeCode = "";
    	}
    	// 处理产品厂商ID数据
    	var productVendorId = billDetailInfo.productVendorId;
    	if(productVendorId == null || productVendorId == 'null' || productVendorId == undefined){
    		productVendorId = "";
    	}else if(productVendorId == "-9999"){
    		productVendorId = "HDZD"
    	}
    	var isDiscountFlag=$('#isDiscountFlag').val();
		if(isDiscountFlag == null || isDiscountFlag == 'null' || isDiscountFlag == undefined || isDiscountFlag == ""){
			isDiscountFlag = 'Y';
		}
    	// 处理需要的积分
    	var exPoint = billDetailInfo.exPoint;
    	if(exPoint == null || exPoint == 'null' || exPoint == undefined || exPoint == ""){
    		exPoint = "0";
    	}else{
    		exPoint = Number(exPoint).toFixed(0);
    	}
    	var activitySign = billDetailInfo.activitySign;
    	if(activitySign == null || activitySign == 'null' || activitySign == undefined){
    		activitySign = "";
    	}
    	var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		if(activityTypeCode != "ZDZK" && activityTypeCode != "ZDQL"){
			if(activityTypeCode == "CXHD"){
				if(discountRate != ""){
					discountRate = discountRate + "%";
				}
				var promotionInfoTitle = $("#promotionInfoTitle").val();
		    	var promotionCodeTitle = $("#promotionCodeTitle").val();
		    	var promotionNameTitle = $("#promotionNameTitle").val();
		    	var promotionDetailTitle = $("#promotionDetailTitle").val();
		    	// 判断记录是主记录还是明细记录，根据记录类型增加新行
				if(productVendorId == "HDZD"){
					var rowIndex = parseInt($("#rowCode").val())+1;
					$("#rowCode").val(rowIndex);
					if(counterActCode == "CPHD"){
						// 购物车Coupon券活动主记录
						var html = '<tr id="dataRow'+nextIndex+'">';
						html += '<td id="indexNo">'+rowIndex+'</td>';
						html += '<td><span id="spanUnitCode">'+ promotionInfoTitle +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value=""/><input id="unitCode" name="unitCode" type="hidden" value=""/></td>';
						html += '<td><span id="spanBarCode">'+ barCode + promotionCodeTitle +'</span><input id="barCode" name="barCode" type="hidden" value="'+ barCode +'"/></td>';
						html += '<td><span id="spanProductName">'+ productName + promotionNameTitle +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ productName +'"/></td>';
						html += '<td></td>';
						if($("#useMemberPrice").val() == "Y"){
							html += '<td><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
						}
						if($("#isPlatinumPrice").val()=="Y"){
							html += '<td><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
						}
						html += '<td class="center">'
								+'<span id="spanQuantity">'+ quantity +'</span>'
								+'<input id="promotionQuantity" name="promotionQuantity" type="hidden" value="'+ quantity +'"/>'
								+'<input id="quantityuArr" name="quantityuArr" type="hidden" value="'+ realQuantity +'"/>'
								+'<input id="groupQuantity" name="groupQuantity" type="hidden" value="'+ groupQuantity +'"/>'
								+'</td>';
						html += '<td></td>';
						html += '<td></td>';
						html += '<td id="tdAmount">'+ amount +'</td>';
						html += '<td><span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL02.deleteRow(this);return false;"></button></span></td>';
						html += '<td style="display:none">'
						+'<input type="hidden" id="orderId" name="orderId" value=""/>'
						+'<input type="hidden" id="couponCode" name="couponCode" value=""/>'
						+'<input type="hidden" id="isStock" name="isStock" value="'+isStock+'"/>'
						+'<input type="hidden" id="mainCode" name="mainCode" value="'+ activityCode +'"/>'
						+'<input type="hidden" id="counterActCode" name="counterActCode" value="CPHD"/>'
						+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value="'+ activityTypeCode +'"/>'
						+'<input type="hidden" id="activitySign" name="activitySign" value="'+ activitySign +'"/>'
				        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ productVendorId +'"/>'
				        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="' + productVendorId + '"/>'
				        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/>'
				        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
				        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value="'+ originalAmount +'"/>'
				        +'<input type="hidden" id="proType" name="proType" value=""/>'
				        +'<input type="hidden" id="exPoint" name="exPoint" value=""/>'
				        +'<input type="hidden" id="stockQuantity" name="stockQuantity" value="'+stockQuantity+'"/>'
				        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
						$("#databody").append(html);
					}else{
						// 购物车促销活动与积分兑换活动主记录
						var html = '<tr id="dataRow'+nextIndex+'">';
						html += '<td id="indexNo">'+rowIndex+'</td>';
						html += '<td><span id="spanUnitCode">'+ promotionInfoTitle +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value=""/><input id="unitCode" name="unitCode" type="hidden" value=""/></td>';
						html += '<td><span id="spanBarCode">'+ barCode + promotionCodeTitle +'</span><input id="barCode" name="barCode" type="hidden" value="'+ barCode +'"/></td>';
						html += '<td><span id="spanProductName">'+ productName + promotionNameTitle +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ productName +'"/></td>';
						html += '<td><span id="spanPrice">'+ price +'</span><input id="pricePay" name="pricePay" type="hidden" value="'+ price +'"/></td>';
						if($("#useMemberPrice").val() == "Y"){
							html += '<td><span id="spanMemberPrice"></span><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
						}
						if($("#isPlatinumPrice").val()=="Y"){
							html += '<td><span id="spanPlatinumPrice"></span><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
						}
						html += '<td class="center"><button id="btnLessen" class="wp_minus" onclick="BINOLWPSAL02.lessenQuantity(this);return false;"></button>'
								+'<input id="promotionQuantity" name="promotionQuantity" type="text" class="text" style="width:50px;margin:.2em 0;" value="'+ quantity +'" maxlength="4" onkeyup="BINOLWPSAL02.changeQuantity(this)"/>'
								+'<input id="quantityuArr" name="quantityuArr" type="hidden" value="'+ realQuantity +'"/>'
								+'<input id="groupQuantity" name="groupQuantity" type="hidden" value="'+ groupQuantity +'"/>'
								+'<button id="btnAdd" class="wp_plus" onclick="BINOLWPSAL02.addQuantity(this);return false;"></button></td>';
						
						if("N" == isDiscountFlag){
							//折扣率不可用时，改成一般显示
							html += '<td><span>'+ discountRate +'%</span><input class="hide" name="discountRateArr" id="discountRateArr" value="'+ discountRateValue +'"/></td>';
						}else{
							html += '<td>'+ discountRate +'<input id="discountRateArr" name="discountRateArr" type="hidden" class="text" style="width:40px;" value="'+ discountRateValue +'" /></td>';
						}
						
						html += '<td>'+ realPrice +'<input id="realPriceArr" name="realPriceArr" type="hidden" class="text" style="width:60px;" value="'+ realPrice +'" /></td>';
						html += '<td id="tdAmount">'+ amount +'</td>';
						html += '<td><span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL02.deleteRow(this);return false;"></button></span></td>';
						html += '<td style="display:none">'
						+'<input type="hidden" id="orderId" name="orderId" value=""/>'
						+'<input type="hidden" id="couponCode" name="couponCode" value=""/>'
						+'<input type="hidden" id="isStock" name="isStock" value="'+isStock+'"/>'
						+'<input type="hidden" id="mainCode" name="mainCode" value="'+ activityCode +'"/>'
						+'<input type="hidden" id="counterActCode" name="counterActCode" value=""/>'
						+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value="'+ activityTypeCode +'"/>'
						+'<input type="hidden" id="activitySign" name="activitySign" value="'+ activitySign +'"/>'
				        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ productVendorId +'"/>'
				        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="' + productVendorId + '"/>'
				        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ price +'"/>'
				        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
				        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value="'+ originalAmount +'"/>'
				        +'<input type="hidden" id="proType" name="proType" value=""/>'
				        +'<input type="hidden" id="exPoint" name="exPoint" value=""/>'
				        +'<input type="hidden" id="stockQuantity" name="stockQuantity" value="'+stockQuantity+'"/>'
				        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
						$("#databody").append(html);
					}
					// 对应促销选择框更新为选中状态
					var $thisCheckBox = $("#promotionData").find("#ckPromotion:[value='"+ activityCode +"']");
					$thisCheckBox.attr("checked","true");
				}else{
					// 购物车促销活动明细记录
					var html = '<tr id="dataRow'+nextIndex+'" class="green">';
					html += '<td></td>';
					html += '<td><span id="spanUnitCode">'+ promotionDetailTitle + unitCode +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value="'+ unitCode +'"/><input id="unitCode" name="unitCode" type="hidden" value="'+ unitCode +'"/></td>';
					html += '<td><span id="spanBarCode">'+ barCode +'</span><input id="barCode" name="barCode" type="hidden" value="'+ barCode +'"/></td>';
					html += '<td><span id="spanProductName">'+ productName +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ productName +'"/></td>';
					html += '<td><span id="spanPrice">'+ price +'</span><input id="pricePay" name="pricePay" type="hidden" value="'+ price +'"/></td>';
					if($("#useMemberPrice").val() == "Y"){
						html += '<td><span id="spanMemberPrice"></span><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
					}
					if($("#isPlatinumPrice").val()=="Y"){
						html += '<td><span id="spanPlatinumPrice"></span><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
					}
					html += '<td class="center">'
							+'<span id="spanQuantity">'+ realQuantity +'</span>'
							+'<input id="quantityuArr" name="quantityuArr" type="hidden" value="'+ realQuantity +'"/>'
							+'<input id="groupQuantity" name="groupQuantity" type="hidden" value="'+ groupQuantity +'"/>'
							+'</td>';
					if("N" == isDiscountFlag){
						//折扣率不可用时，改成一般显示
						html += '<td><span>'+ discountRate +'%</span><input class="hide" name="discountRateArr" id="discountRateArr" value="'+ discountRateValue +'"/></td>';
					}else{
						html += '<td>'+ discountRate +'<input id="discountRateArr" name="discountRateArr" type="hidden" class="text" style="width:40px;" value="'+ discountRateValue +'" /></td>';
					}
					html += '<td>'+ realPrice +'<input id="realPriceArr" name="realPriceArr" type="hidden" class="text" style="width:60px;" value="'+ realPrice +'" /></td>';
					html += '<td id="tdAmount">'+ amount +'</td>';
					html += '<td></td>';
					html += '<td style="display:none">'
					+'<input type="hidden" id="orderId" name="orderId" value="'+ orderId +'"/>'
					+'<input type="hidden" id="couponCode" name="couponCode" value="'+ couponCode +'"/>'
					+'<input type="hidden" id="isStock" name="isStock" value="'+ isStock +'"/>'
					+'<input type="hidden" id="mainCode" name="mainCode" value="'+ activityCode +'"/>'
					+'<input type="hidden" id="counterActCode" name="counterActCode" value="'+ counterActCode +'"/>'
					+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value="'+ activityTypeCode +'"/>'
					+'<input type="hidden" id="activitySign" name="activitySign" value=""/>'
			        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ productVendorId +'"/>'
			        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+ productVendorId +'"/>'
			        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ price +'"/>'
			        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
			        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value="'+ originalAmount +'"/>'
			        +'<input type="hidden" id="proType" name="proType" value="'+ billDetailInfo.proType +'"/>'
			        +'<input type="hidden" id="exPoint" name="exPoint" value="'+ exPoint +'"/>'
			        +'<input type="hidden" id="stockQuantity" name="stockQuantity" value="'+stockQuantity+'"/>'
			        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
					$("#databody").append(html);
				}
			}else{
				// 购物车正常销售商品记录
				var rowIndex = parseInt($("#rowCode").val())+1;
				$("#rowCode").val(rowIndex);
				// 非折扣记录的情况下追加行
				var html = '<tr id="dataRow'+nextIndex+'">';
				html += '<td id="indexNo">'+rowIndex+'</td>';
				html += '<td><span id="spanUnitCode">'+ unitCode +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value="'+ unitCode +'"/><input id="unitCode" name="unitCode" type="hidden" value="'+ unitCode +'"/></td>';
				html += '<td><span id="spanBarCode">'+ barCode +'</span><input id="barCode" name="barCode" type="hidden" value="'+ barCode +'"/></td>';
				html += '<td><span id="spanProductName">'+ productName +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ productName +'"/></td>';
				html += '<td><span id="spanPrice">'+ price +'</span><input id="pricePay" name="pricePay" type="hidden" value="'+ price +'"/></td>';
				if($("#useMemberPrice").val() == "Y"){
					html += '<td><span id="spanMemberPrice">'+ memberPrice +'</span><input id="memberPrice" name="memberPrice" type="hidden" value="'+ memberPrice +'"/></td>';
				}
				if($("#isPlatinumPrice").val()=="Y"){
					html += '<td><span id="spanPlatinumPrice">'+ platinumPrice +'</span><input id="platinumPrice" name="platinumPrice" type="hidden" value="'+ platinumPrice +'"/></td>';
				}
				html += '<td class="center"><button id="btnLessen" class="wp_minus" onclick="BINOLWPSAL02.lessenQuantity(this);return false;"></button>'
						+'<input id="quantityuArr" name="quantityuArr" type="text" class="text" style="width:50px;margin:.2em 0;" value="'+ realQuantity +'" maxlength="4" onchange="BINOLWPSAL02.changeQuantity(this)" onkeyup="BINOLWPSAL02.changeQuantity(this)"/>'
						+'<button id="btnAdd" class="wp_plus" onclick="BINOLWPSAL02.addQuantity(this);return false;"></button></td>';
				if("N" == isDiscountFlag){
					//折扣率不可用时，改成一般显示
					html += '<td><span>'+ discountRate +'%</span><input class="hide" name="discountRateArr" id="discountRateArr" value="'+ discountRateValue +'"/></td>';
				}else{
					html += '<td><input id="discountRateArr" name="discountRateArr" value="'+ discountRate +'" class="text" style="width:40px;" maxlength="6" onchange="BINOLWPSAL02.changeDiscountRate(this)" onkeyup="BINOLWPSAL02.keyUpChangeDiscountRate(this)"/>%</td>';
				}
				html += '<td><input id="realPriceArr" name="realPriceArr" value="'+ realPrice +'" class="text" style="width:60px;" maxlength="10" onchange="BINOLWPSAL02.changeRealPrice(this)" onkeyup="BINOLWPSAL02.keyUpchangeRealPrice(this)"/></td>';
				html += '<td id="tdAmount">'+ amount +'</td>';
				html += '<td><span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL02.deleteRow(this);return false;"></button></span></td>';
				html += '<td style="display:none">'
				+'<input type="hidden" id="orderId" name="orderId" value=""/>'
				+'<input type="hidden" id="couponCode" name="couponCode" value=""/>'
				+'<input type="hidden" id="isStock" name="isStock" value="'+isStock+'"/>'
				+'<input type="hidden" id="mainCode" name="mainCode" value=""/>'
				+'<input type="hidden" id="counterActCode" name="counterActCode" value=""/>'
				+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value="'+ activityTypeCode +'"/>'
				+'<input type="hidden" id="activitySign" name="activitySign" value=""/>'
		        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ productVendorId +'"/>'
		        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+ productVendorId +'"/>'
		        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ price +'"/>'
		        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
		        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value="'+ originalAmount +'"/>'
		        +'<input type="hidden" id="proType" name="proType" value="'+ billDetailInfo.proType +'"/>'
		        +'<input type="hidden" id="exPoint" name="exPoint" value=""/>'
		        +'<input type="hidden" id="stockQuantity" name="stockQuantity" value="'+stockQuantity+'"/>'
		        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
				$("#databody").append(html);
			}
		}else{
			// 整单折扣与整单去零的记录
			var rowIndex = parseInt($("#rowCode").val())+1;
			$("#rowCode").val(rowIndex);
			// 数据为折扣记录时追加行
			if(activityTypeCode == "ZDZK" && discountRate != ""){
				// 若为整单折扣记录时增加百分号显示
				discountRate = discountRate + "%";
			}
			var html = '<tr id="dataRow'+ nextIndex +'" class="red">';
			html += '<td id="indexNo">'+rowIndex+'</td>';
			html += '<td><span id="spanUnitCode"></span></td>';
			html += '<td><span id="spanBarCode"></span></td>';
			html += '<td><span id="spanProductName">'+ productName +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ productName +'" /></td>';
			html += '<td><span id="spanPrice"></span></td>';
			if($("#useMemberPrice").val() == "Y"){
				html += '<td><span id="spanMemberPrice"></span><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
			}
			if($("#isPlatinumPrice").val()=="Y"){
				html += '<td><span id="spanPlatinumPrice"></span><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
			}
			html += '<td class="center"></td>';
			html += '<td>'+ discountRate +'<input id="discountRateArr" name="discountRateArr" type="hidden" value="'+ discountRateValue +'" /></td>';
			html += '<td></td>';
			html += '<td id="tdAmount">'+ amount +'</td>';
			html += '<td><span class="but_delbox"><button class="wp_del" onclick="BINOLWPSAL02.deleteRow(this);return false;"></button></span></td>';
			html += '<td style="display:none">'
			+'<input type="hidden" id="orderId" name="orderId" value=""/>'
			+'<input type="hidden" id="couponCode" name="couponCode" value=""/>'
			+'<input type="hidden" id="isStock" name="isStock" value="'+isStock+'"/>'
			+'<input type="hidden" id="mainCode" name="mainCode" value=""/>'
			+'<input type="hidden" id="counterActCode" name="counterActCode" value=""/>'
			+'<input type="hidden" id="activityTypeCode" name="activityTypeCode" value="'+ activityTypeCode +'"/>'
			+'<input type="hidden" id="activitySign" name="activitySign" value=""/>'
	        +'<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>'
	        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
	        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/>'
	        +'<input type="hidden" id="payAmount" name="payAmount" value="'+ amount +'"/>'
	        +'<input type="hidden" id="noDiscountAmount" name="noDiscountAmount" value=""/>'
	        +'<input type="hidden" id="proType" name="proType" value="'+ billDetailInfo.proType +'"/>'
	        +'<input type="hidden" id="exPoint" name="exPoint" value=""/>'
	        +'<input type="hidden" id="stockQuantity" name="stockQuantity" value="'+stockQuantity+'"/>'
	        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
			$("#databody").append(html);
		}
		// 设置记录行样式
		var saleType = $('#saleType').val();
		if(saleType == "SR"){
			$('#txtSaleType').text($('#saleTypeSR').val());
			$("#saleTypeTotalQuantity").show();
			$("#saleTypeTotalAmount").show();
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
			$("#saleTypeTotalQuantity").hide();
			$("#saleTypeTotalAmount").hide();
			$("#saleTypeQuantity").hide();
			$("#saleTypeAmount").hide();
			// 重新设置间隔行样式
			BINOLWPSAL02.changeOddEvenColor();
		}
		
		var rowId = "dataRow"+nextIndex;
		BINOLWPSAL02.bindInput(rowId);
    },
	
	"cancel":function(){
		// 还原按钮样式
		$("#btnHangBills").attr("class","btn_top");
		
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
//			// 第一个可见的文本框获得焦点
//			$('#databody >tr').find("input:text:visible:first").select();
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
		// 解除清空购物车禁用
		$("#btnEmptyShoppingCart").removeAttr("disabled");
		$("#btnEmptyShoppingCart").attr("class","btn_top");
	},
    "sendMQ":function(billDetailInfo){
    	var dgSendMQCollectUrl=$("#dgSendMQCollect").attr("href");
    	var params="billId="+billDetailInfo;
    	cherryAjaxRequest({
			url: dgSendMQCollectUrl,
			param: params,
			callback: function(data) {
				if(data == "SUCCESS"){
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"操作成功", 
						type:"MESSAGE", 
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
					BINOLWPSAL06.search();
				}else{
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"操作失败", 
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
	"getBillState":function(billDetailInfo){
		var getPayResultBySendMQUrl=$("#getPayResultBySendMQ").attr("href");
		var params="billId="+billDetailInfo;
		cherryAjaxRequest({
			url: getPayResultBySendMQUrl,
			param: params,
			callback: function(data) {
				if(data == null || data == "" || data == undefined || data == "ERROR"){
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"操作失败",
						type:"MESSAGE",
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
					return;
				}
				var param_map = eval("("+data+")");
				var payState=param_map.payState;
				var payMessage=param_map.payMessage;
				if(payState == "SUCCESS"){
					BINOLWPSAL06.sendMQ(billDetailInfo);
					// 显示提示信息
					//BINOLWPSAL02.showMessageDialog({
					//	message:"操作成功",
					//	type:"MESSAGE",
					//	focusEvent:function(){
					//		// 最后一行第一个可见的文本框获得焦点
					//		BINOLWPSAL02.firstInputSelect();
					//	}
					//});
					//BINOLWPSAL06.search();
				}else{
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:payMessage,
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
};

var BINOLWPSAL06 = new BINOLWPSAL06_GLOBAL();

$(document).ready(function(){
	$('#billHangDateStart').cherryDate({
		beforeShow: function(input){
			var value = $('#saleDateEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#billHangDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#saleDateStart').val();
			return [value,'minDate'];
		}
	});
	// 清空弹出框内的表对象
	oTableArr[2]=null;
	// 调用挂单记录查询
	BINOLWPSAL06.search();
	// 弹出框取消按钮获得焦点
	$("#getBillsPageDiv").find("#btnCancel").focus();
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#dialogInit').html();
});


