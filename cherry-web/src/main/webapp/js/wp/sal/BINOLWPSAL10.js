var WPCOM_dialogBody ="";
var BINOLWPSAL10_GLOBAL = function() {

};

BINOLWPSAL10_GLOBAL.prototype = {
		
	"appendCouponOrderInfo":function(couponOrderInfo){
    	// 处理数量
    	var quantity = couponOrderInfo.quantity;
    	var calcuQuantity = couponOrderInfo.quantity;
		if(quantity == null || quantity == 'null' || quantity == undefined || quantity == ""){
    		quantity = "0";
    		calcuQuantity = "1";
    	}else{
    		quantity = Number(quantity).toFixed(0);
    		calcuQuantity = Number(calcuQuantity).toFixed(0);
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
		var isPlatinumPrice=$('#isPlatinumPrice').val();
		// 追加促销行
		var html = '<tr id="dataRow'+nextIndex+'" class="'+ trClass +'">';
		html += '<td id="indexNo">'+rowIndex+'</td>';
		html += '<td><span id="spanUnitCode">'+ promotionInfoTitle +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value=""/><input id="unitCode" name="unitCode" type="hidden" value=""/></td>';
		html += '<td><span id="spanBarCode">'+ couponOrderInfo.mainCode + promotionCodeTitle +'</span><input id="barCode" name="barCode" type="hidden" value="'+ couponOrderInfo.mainCode +'"/></td>';
		html += '<td><span id="spanProductName">'+ couponOrderInfo.activityName + promotionNameTitle +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ couponOrderInfo.activityName +'"/></td>';
		html += '<td></td>';
		if($("#useMemberPrice").val() == "Y"){
			html += '<td><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
		}
		if("Y" == isPlatinumPrice){
			html += '<td><input id="platinumPrice" name="platinumPrice" type="hidden"/></td>';
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
		
		// 改变交替行样式
		BINOLWPSAL02.changeOddEvenColor();
		
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
		var isPlatinumPrice=$('#isPlatinumPrice').val();
		html += '<td></td>';
		html += '<td><span id="spanUnitCode">'+ promotionDetailTitle + productInfo.unitCode +'</span><input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding" class="text" style="width:100px;" value="'+ productInfo.unitCode +'"/><input id="unitCode" name="unitCode" type="hidden" value="'+ productInfo.unitCode +'"/></td>';
		html += '<td><span id="spanBarCode">'+ productInfo.barCode +'</span><input id="barCode" name="barCode" type="hidden" value="'+ productInfo.barCode +'"/></td>';
		html += '<td><span id="spanProductName">'+ productInfo.productName +'</span><input id="productNameArr" name="productNameArr" type="hidden" value="'+ productInfo.productName +'"/></td>';
		html += '<td><span id="spanPrice">'+ oldPrice +'</span><input id="pricePay" name="pricePay" type="hidden" value="'+ oldPrice +'"/></td>';
		if($("#useMemberPrice").val() == "Y"){
			html += '<td><span id="spanMemberPrice"></span><input id="memberPrice" name="memberPrice" type="hidden"/></td>';
		}
		if("Y" == isPlatinumPrice){
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
        +'<input type="hidden" id="campaignValid" name="campaignValid" value="'+productInfo.campaignValid+'"/>'
        +'<input type="hidden" id="isExchanged" name="isExchanged" value="1"/></td></tr>';
		$("#databody").append(html);
	},
		
	"confirm":function(){
		if(!$('#checkCouponForm').valid()) {
			return false;
		}
		// 删除空行
		BINOLWPSAL02.deleteEmptyRow();
		// 获取销售最大允许录入行数
		var showSaleRows = $("#showSaleRows").val();
		
		var memberInfoId = $("#dgMemberInfoId").val();
		var couponCode = $("#dgCouponCode").val();
		var mobilePhone = $("#dgMobilePhone").val();
		var activityType = $('#dgActivityType').val();
		var subjectCode = $("#dgSubjectCode").val();
		var activityCode = $("#dgActivityCode").val();
		var checkCouponUrl = $("#dgCheckCouponUrl").attr("href");
		var params = "activityType="+ activityType +"&memberInfoId="+ memberInfoId +"&couponCode="+ couponCode 
			+"&mobilePhone="+ mobilePhone +"&subjectCode="+ subjectCode +"&activityCode="+ activityCode;
		cherryAjaxRequest({
			url: checkCouponUrl,
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
							message:"没有查询到礼品信息", 
							type:"MESSAGE", 
							focusEvent:function(){
								// 输入框获得焦点
								$("#dgCouponCode").focus();
							}
						});
					}else if(data == "NOTMEMBER"){
						if($('#databody >tr').length < Number(showSaleRows)){
							// 添加一个空行
							BINOLWPSAL02.addNewLine();
						}
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"请输入验证号进行查询", 
							type:"MESSAGE", 
							focusEvent:function(){
								// 输入框获得焦点
								$("#dgCouponCode").focus();
							}
						});
					}else{
						// 删除折扣行
						BINOLWPSAL02.deleteDiscountRow();
						
						var promotionMap = eval("("+data+")");
						var couponOrderInfo = promotionMap.couponOrderInfo;
						var couponOrderProduct = promotionMap.couponOrderProduct;
						// 增加主记录行
						BINOLWPSAL10.appendCouponOrderInfo(couponOrderInfo);
						// 增加明细记录行
						$.each(couponOrderProduct, function(i){
							// 添加促销活动记录行
							BINOLWPSAL10.appendCouponOrderProduct(couponOrderProduct[i]);
						});
						// 计算总金额
						BINOLWPSAL02.calcuTatol();
						// 设置单据分类
						BINOLWPSAL02.setBillClassify();
						
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
						
						// 文本框回车事件解绑
						$("#dgCouponCode").unbind();
						// 搜索框重新绑定回车事件
						$("#searchStr").bind("keydown", function (e) {
							var key = e.which;
							if (key == 13) {
								e.preventDefault();
								BINOLWPSAL02.search();
							}
						});
						// 关闭弹出窗口
						closeCherryDialog('checkCoupon_dialog',WPCOM_dialogBody);
						// 清空弹出框内容
						$('#checkCoupon_dialog').html("");
					}
				}else{
					if($('#databody >tr').length < Number(showSaleRows)){
						// 添加一个空行
						BINOLWPSAL02.addNewLine();
					}
					// 查询结果为空的情况下显示错误信息
					BINOLWPSAL02.showMessageDialog({
						message:"没有查询到礼品信息", 
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
	
	"cancel":function(){
		var activityCode = $("#dgActivityCode").val();
		// 移除对应选择框选中状态
		var $thisCheckBox = $("#promotionData").find("#ckPromotion:[value='"+ activityCode +"']");
		if(!!$thisCheckBox.attr("checked")){
			$thisCheckBox.removeAttr("checked");
		}
		// 文本框回车事件解绑
		$("#dgCouponCode").unbind();
		// 搜索框重新绑定回车事件
		$("#searchStr").bind("keydown", function (e) {
			var key = e.which;
			if (key == 13) {
				e.preventDefault();
				BINOLWPSAL02.search();
			}
		});
		// 最后一行第一个可见的文本框获得焦点
		BINOLWPSAL02.firstInputSelect();
		// 关闭弹出窗口
		closeCherryDialog('checkCoupon_dialog',WPCOM_dialogBody);
		// 清空弹出框内容
		$('#checkCoupon_dialog').html("");
	}
};

var BINOLWPSAL10 = new BINOLWPSAL10_GLOBAL();

$(document).ready(function(){
	// 搜索框回车事件解绑
	$("#searchStr").unbind();
	// 表单验证
	cherryValidate({
		formId: 'checkCouponForm',
		rules: {
			dgCouponCode: {required: true, maxlength: 20}
		}
	});
	
	// 文本框绑定回车事件
	$("#dgCouponCode").bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			BINOLWPSAL10.confirm();
		}
	});
	// 输入框获得焦点
	$("#dgCouponCode").focus();
	
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#checkCoupon_dialog').html();
});