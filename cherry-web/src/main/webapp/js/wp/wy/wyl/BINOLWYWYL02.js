var BINOLWYWYL02_GLOBAL = function() {
	
};

BINOLWYWYL02_GLOBAL.prototype = {
	
	"search":function(){
		$("#detailDiv").hide();
		if($('#databody >tr').length > 0){
			$.each($('#databody >tr'), function(i){
				$(this).remove();
			});
		}
		// 获取搜索条件
		var applyCoupon = $.trim($("#applyCoupon").val());
		if(applyCoupon != undefined && applyCoupon != null && applyCoupon != ""){
			var searchUrl = $("#searchUrl").attr("href");
			var param = "applyCoupon=" + applyCoupon;
			cherryAjaxRequest({
				url: searchUrl,
				param: param,
				callback: function(data) {
					if(data != undefined && data != null && data != ""){
						if(data == "ERROR"){
							BINOLWYWYL02.showMessageDialog({
								message:"查询申领单据出现异常", 
								type:"MESSAGE", 
								focusEvent:function(){
									$("#applyCouponEncry").focus();
								}
							});
						}else{
							var orderInfo = eval("("+data+")");
							var state = orderInfo.state;
							if(state == "OK"){
								BINOLWYWYL02.showMessageDialog({
									message:"已领用的二维码，领用时间：" + orderInfo.finishTime, 
									type:"MESSAGE", 
									focusEvent:function(){
										$("#applyCouponEncry").focus();
									}
								});
							}else{
								$('#billCode').val(orderInfo.billCode);
								$('#subType').val(orderInfo.subType);
								$('#memberInfoId').val(orderInfo.memberInfoId);
								$('#messageId').val(orderInfo.messageId);
								$('#memberName').val(orderInfo.memberName);
								$("#gender").val(orderInfo.gender);
								$("#orgMemberName").val(orderInfo.memberName);
								$("#orgGender").val(orderInfo.gender);
								$("#orgBirthDay").val(orderInfo.birthDay);
								$('#mobile').val(orderInfo.mobilePhone);
								$('#mobilePhone').text(orderInfo.mobilePhone);
								$('#counterName').text(orderInfo.counterName);
								$('#subCampaignName').val(orderInfo.campaignName);
								$('#actionTime').text(orderInfo.campaignOrderTime);
								$('#spanTotalQuantity').text(orderInfo.totalQuantity);
								$('#spanTotalAmount').text(orderInfo.totalAmount);
								$('#totalQuantity').val(orderInfo.totalQuantity);
								$('#totalAmount').val(orderInfo.totalAmount);
								$('#memo').val(orderInfo.remark);
								// 处理会员生日
								if(orderInfo.birthDay != undefined && orderInfo.birthDay != null && orderInfo.birthDay != ""){
									var birthMonth = orderInfo.birthDay.substring(0,2);
									var birthDay = orderInfo.birthDay.substring(2,4);
									// 生日月份
									$("#birthDayMonthQ").val(Number(birthMonth));
									// 初始化日期
									BINOLWYWYL02.dateInit(Number(birthMonth));
									// 生日日期
									$("#birthDayDateQ").val(Number(birthDay));
								}
								
								var detailList = orderInfo.detailList;
								// 增加明细记录行
								$.each(detailList, function(i){
									BINOLWYWYL02.appendProduct(detailList[i], orderInfo.campaignName);
								});
								
								$("#detailDiv").show();
								// 领用按钮获得焦点
								$("#btnGetGift").focus();
							}
						}
					}else{
						BINOLWYWYL02.showMessageDialog({
							message:"没有可以领用的单据", 
							type:"MESSAGE", 
							focusEvent:function(){
								$("#applyCouponEncry").focus();
							}
						});
					}
				}
			});
		}else{
			BINOLWYWYL02.showMessageDialog({
				message:"请扫描申领代码", 
				type:"MESSAGE", 
				focusEvent:function(){
					$("#applyCouponEncry").focus();
				}
			});
		}
	},
	
	"getGift":function(){
		var $form = $('#mainForm');
		var applyCoupon = $.trim($("#applyCoupon").val());
		var getGiftUrl = $("#getGiftUrl").attr("href");
		var params = "applyCoupon=" + applyCoupon + "&" + $form.serialize();
		cherryAjaxRequest({
			url: getGiftUrl,
			param: params,
			callback: function(data) {
				if(data != undefined && data != null && data != ""){
					if(data == "SUCCESS"){
						BINOLWYWYL02.showMessageDialog({
							message:"领用成功", 
							type:"SUCCESS", 
							focusEvent:function(){
								$("#applyCouponEncry").val("");
								$("#applyCoupon").val("");
								$("#applyCouponEncry").focus();
								$("#detailDiv").hide();
							}
						});
					}else{
						BINOLWYWYL02.showMessageDialog({
							message:"领用失败," + data, 
							type:"MESSAGE", 
							focusEvent:function(){
								$("#btnGetGift").focus();
							}
						});
					}
				}else{
					BINOLWYWYL02.showMessageDialog({
						message:"领用失败", 
						type:"MESSAGE", 
						focusEvent:function(){
							$("#btnGetGift").focus();
						}
					});
				}
			}
		});
	},
	
	"appendProduct":function(detailInfo, activityName){
    	// 处理数量
    	var quantity = detailInfo.quantity;
    	if(quantity == null || quantity == 'null' || quantity == undefined || quantity == ""){
    		quantity = "1";
    	}else{
    		quantity = Number(quantity).toFixed(0);
    	}
    	// 处理实际价格数据
    	var price = detailInfo.price;
    	if(price == null || price == 'null' || price == undefined || price == ""){
    		price = "0.00";
    	}else{
    		price = Number(price).toFixed(2);
    	}
    	// 生成序号
    	var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		// 追加促销行
		var html = '<tr id="dataRow'+nextIndex+'">';
		html += '<td>'+ nextIndex +'</td>';
		html += '<td>'+ detailInfo.unitCode +'</td>';
		html += '<td>'+ detailInfo.barCode +'</td>';
		html += '<td>'+ detailInfo.productName  +'</td>';
		html += '<td>'+ price +'</td>';
		html += '<td>'+ quantity +'</td>';
		html += '<td>'+ activityName +'</td>';
		html += '<td></td></tr>';
		$("#databody").append(html);
	},
	
	// 生日框初始化处理
	"birthDayInit":function(){
		for(var i = 1; i <= 12; i++) {
			$("#birthDayMonthQ").append('<option value="'+i+'">'+i+'</option>');
		}
		$("#birthDayMonthQ").change(function(){
			var $date = $("#birthDayDateQ");
			var month = $(this).val();
			var options = '<option value="">'+$date.find('option').first().html()+'</option>';
			if(month == "") {
				$date.html(options);
				return;
			}
			var i = 1;
			var max = 0;
			if(month == '2') {
				max = 29;
			} else if(month == '4' || month == '6' || month == '9' || month == '11') {
				max = 30;
			} else {
				max = 31;
			}
			for(i = 1; i <= max; i++) {
				options += '<option value="'+i+'">'+i+'</option>';
			}
			$date.html(options);
		});
	},
	
	"dateInit":function(month){
		var $date = $("#birthDayDateQ");
		var options = '<option value="">'+$date.find('option').first().html()+'</option>';
		if(month == "") {
			$date.html(options);
			return;
		}
		var i = 1;
		var max = 0;
		if(month == '2') {
			max = 29;
		} else if(month == '4' || month == '6' || month == '9' || month == '11') {
			max = 30;
		} else {
			max = 31;
		}
		for(i = 1; i <= max; i++) {
			options += '<option value="'+i+'">'+i+'</option>';
		}
		$date.html(options);
	},
	
	"changeCouponCode":function(obj){
		var $this = $(obj);
		var $thisVal = $this.val().toString();
		var couponCode = $("#applyCoupon").val();
		var couponLength = $thisVal.length;
		var newStrLength = couponCode.length;
		
		$this.val("").focus().val($thisVal);
		if(couponLength > 4){
			if(couponLength != newStrLength){
				if(couponLength > newStrLength){
					if($thisVal.indexOf("*") >= 0){
						// 增加字符的情况
						var lastChar = $thisVal.charAt(couponLength - 1);
						if(couponLength > 8){
							if(lastChar != "*"){
								var newCouponCode = couponCode + lastChar;
								$("#applyCoupon").val(newCouponCode);
							}
						}else{
							if(lastChar != "*"){
								var newCouponCode = couponCode + lastChar;
								var newVal = $thisVal.substring(0, (couponLength-1)) + "*";
								$("#applyCouponEncry").val(newVal);
								$("#applyCoupon").val(newCouponCode);
							}
						}
					}else{
						var newVal = "";
						if(couponLength > 8){
							newVal = $thisVal.substring(0, 4) + "****" + $thisVal.substring(8, couponLength);
						}else{
							newVal = $thisVal.substring(0, 4) + "****";
							newVal = newVal.substring(0, couponLength);
						}
						$("#applyCoupon").val($thisVal);
						$("#applyCouponEncry").val(newVal);
					}
				}else{
					// 删除字符的情况
					var newVal = couponCode.substring(0, couponLength)
					$("#applyCoupon").val(newVal);
				}
			}else{
				if($thisVal.indexOf("*") < 0){
					var newVal = "";
					if(couponLength > 8){
						newVal = $thisVal.substring(0, 4) + "****" + $thisVal.substring(8, couponLength);
					}else{
						newVal = $thisVal.substring(0, 4) + "****";
						newVal = newVal.substring(0, couponLength);
					}
					$("#applyCoupon").val($thisVal);
					$("#applyCouponEncry").val(newVal);
				}
			}
		}else{
			$("#applyCoupon").val($thisVal);
		}
	},
	
	"getCursorPosition":function (){
		var obj = document.getElementById("applyCouponEncry");
		if(typeof document.selection != "undefined"){
			obj.focus();
			var range = document.selection.createRange();
			range.moveStart('character', -obj.value.length);
			cursurPosition = range.text.length;
		}else{
			cursurPosition = obj.selectionStart;
		}
		return cursurPosition;
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
			BINOLWYWYL02.messageConfirm(dialogSetting.focusEvent);
		});
	},
	
	"messageConfirm":function (focusEvent){
		closeCherryDialog("messageDialogDiv");
		if(typeof focusEvent == "function") {
			focusEvent();
		}
	}
	
};

(function($){
	$.fn.textFocus=function(v){
		var range,len,v=v===undefined?0:parseInt(v);
		this.each(function(){
			if($.browser.msie){
				range = this.createTextRange();
				v===0?range.collapse(false):range.move("character",v);
				range.select();
			}else{
				len=this.value.length;
				v===0?this.setSelectionRange(len,len):this.setSelectionRange(v,v);
			}
			this.focus();
		});
		return this;
	}
})(jQuery);

var BINOLWYWYL02 = new BINOLWYWYL02_GLOBAL();

$(document).ready(function(){
	// 初始化月日选择框
	BINOLWYWYL02.birthDayInit();
	// 默认条件为女
	$("#gender option:[value='2']").attr("selected",true);
	// 状态选择框禁用
	$("#state").attr("disabled",true);
	// 申领代码输入框获得焦点
	$("#applyCouponEncry").focus();
	// 申领代码输入框绑定回车键
	$("#applyCouponEncry").bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			BINOLWYWYL02.search();
		}
	}).bind("click", function (){
		$("#applyCouponEncry").textFocus();
	});
	
});
