var WPCOM_dialogBody ="";
var BINOLMBSVC03_1 = function() {

};

BINOLMBSVC03_1.prototype = {
		"cancel":function(){
			closeCherryDialog('dialogInit',WPCOM_dialogBody);
			// 清空弹出框内容
			$('#dialogInit').html("");
		},
		"radio":function(t){
			$("#giftAmount").val("");
			$("#amount").val("");
			$("#fwamount").val("");
			$("#fwk").find("input").val("");
			$("#fwk").find("input").attr("readonly","readonly");
			$("#tyk").find("input[name!='serviceType']").val("");
			$("#tyk").find("input").attr("disabled","disabled");
			var businessType = Number($("#businessType").val());
			var value_p = $(t).val();
			if((businessType==2 || businessType==3)  && Number(value_p) == -999){
				$("[name=serviceQuantity]").show();
				$("[name=subDiscountName]").siblings("div").text("");
				$("[name=serviceQuantity]").siblings("div").text("");
//				$("input[name='giftAmount']").removeAttr("disabled").removeAttr("readonly");
//				$("input[name='amount']").removeAttr("disabled").removeAttr("readonly");
				$("[name='serviceType']").removeAttr("disabled").removeAttr("readonly");
				$("[name='serviceQuantity']").removeAttr("disabled").removeAttr("readonly");
				$("#amountSpan").show();
				if(businessType==2){
					$("#amountSpan").show();
					$("#cAmount").show();
					$("#pAmount").hide();
				}else if(businessType==3){
					$("#amountSpan").show();
					$("#cAmount").hide();
					$("#pAmount").show();
				}
				return false;
			}else if((businessType==2 || businessType==3)  && Number(value_p) != -999){
				$("[name=subDiscountName]").siblings("div").show();
				$("[name=serviceQuantity]").siblings("div").show();
				$("[name=subDiscountName]").hide();
				$("[name=serviceQuantity]").hide();
				$("#amountSpan").hide();
			}
			if(businessType ==1  && Number(value_p) == -999){
				$("#giftAmount").parent().siblings().find("#subDiscountName").text("");
				$("#amountSpan").hide();
				$("#fwk").find("input[name='giftAmount']").removeAttr("readonly");
				$("#fwk").find("input[name='amount']").removeAttr("readonly");
				return false;
			}else if(businessType ==1 && Number(value_p) != -999){
				$("#amountSpan").hide();
			}
			var b=true;
			$.each($(t).parent().parent().siblings(),function(){
				var GiftAmount = $(this).find("#GiftAmount").val();
				var RechargeMinValue = $(this).find("#RechargeMinValue").val();
				var SubDiscountName = $(this).find("#SubDiscountName").val();
				var ServiceType = $(this).find("#ServiceType").val();
				var RechargeMaxValue = $(this).find("#RechargeMaxValue").val();
				var DiscountType = $(this).find("#DiscountType").val();
				var ServiceQuantity = $(this).find("#ServiceQuantity").val();
				var CardTypeD = $(this).find("#CardTypeD").val();
				if(businessType==1 && businessType==Number(CardTypeD)){
					if(Number(value_p) >= Number(RechargeMinValue) && Number(value_p) < Number(RechargeMaxValue)){
						$("#fwk").find("input").attr("readonly","readonly");
						$("#fwk").find("input").val("");
						$(":visible [name='amount']").val(RechargeMinValue);
						$("#giftAmount").val(GiftAmount);
						$("#subDiscountName").text(SubDiscountName);
						$("#amountSpan1").text(RechargeMinValue);
						$("#giftAmountSpan").text(GiftAmount);
						$("#subDiscountNameSpan").text(SubDiscountName);
						if(DiscountType==2){
							return false;
						}
					}
					if(""==$("#giftAmount").val()){
						$(":visible [name='amount']").parent().parent().find("#subDiscountName").text("");
						$("#amount").val(value_p);
					}
				}else if(businessType!=1 && businessType==Number(CardTypeD)){
					if(Number(value_p) >= Number(RechargeMinValue) && Number(value_p) < Number(RechargeMaxValue) && Number(DiscountType)==2){
						$("#fwamount").val(value_p);
//						$("#giftAmount").val(GiftAmount);
//						$("#"+ServiceType).find("[name='fwamount']").val(value_p);
						$("#"+ServiceType).find("[name='serviceQuantity']").val(ServiceQuantity);
						
						$("#"+ServiceType).find("#subDiscountNameDiv").text(SubDiscountName);
						$("#"+ServiceType).find("#serviceQuantityDiv").text(ServiceQuantity);
						
//						$("#fwamount").attr("readonly","readonly");
//						$("#giftAmount").attr("readonly","readonly");
						$("#"+ServiceType).find("[name='serviceQuantity']").attr("disabled",false).attr("readonly","readonly");
						$("#"+ServiceType).find("[name='serviceType']").attr("disabled",false);
						b=false;
					}
					if(b){
						$("#"+ServiceType).find("#subDiscountNameDiv").text("");
						$("#"+ServiceType).find("#serviceQuantityDiv").text("");
					}
					if(Number(value_p) >= Number(RechargeMinValue) && Number(value_p) < Number(RechargeMaxValue) && b){
						$("#fwamount").val(value_p);
//						$("#giftAmount").val(GiftAmount);
//						$("#"+ServiceType).find("[name='amount']").val(value_p);
						$("#"+ServiceType).find("[name='serviceQuantity']").val(ServiceQuantity);
						
						$("#"+ServiceType).find("#subDiscountNameDiv").text(SubDiscountName);
						$("#"+ServiceType).find("#serviceQuantityDiv").text(ServiceQuantity);
						
//						$("#fwamount").attr("readonly","readonly");
//						$("#giftAmount").attr("readonly","readonly");
						$("#"+ServiceType).find("[name='serviceQuantity']").attr("disabled",false).attr("readonly","readonly");
						$("#"+ServiceType).find("[name='serviceType']").attr("disabled",false);
					}
				}
			});
		},
		"mouseleaveFc":function(obj){
//			$('#countCode').attr("class","");
			$(obj).find('#errorText').remove();
			$(obj).removeClass("error");
		},
		"addErrorMessage":function($parent,errorMessage){
			$parent.addClass("error");
			$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
			$parent.find('#errorText').attr("title",'error|'+errorMessage);
			$parent.find('#errorText').cluetip({
		    	splitTitle: '|',
			    width: 150,
			    cluezIndex: 20000,
			    tracking: true,
			    cluetipClass: 'error', 
			    arrows: true, 
			    dropShadow: false,
			    hoverIntent: false,
			    sticky: false,
			    mouseOutClose: true,
			    closePosition: 'title',
			    closeText: '<span class="ui-icon ui-icon-close"></span>'
			});
		},
		"commit":function(){
			// 表单验证配置
			cherryValidate({						
				formId: 'rechargeForm',
				rules: {
					saleType: {required: true}
				}
			});
			if(!$('#rechargeForm').valid()) {
				return false;
			}
			var counterName=$('#counterName').val();
			var cardType = $("#businessType").val();
			if(cardType == 1){
				var amount=$('#amount').val();
				var giftAmount=$('#giftAmount').val();
				var amount_parent=$('#amount').parent();
				var giftAmount_parent=$('#giftAmount').parent();
				if(amount == null || amount == 'null' || amount == undefined || amount ==''){
					BINOLMBSVC03_1.addErrorMessage(amount_parent, '请输入对应的充值金额');
					return false;
				}
				if(isNaN(amount)){
					BINOLMBSVC03_1.addErrorMessage(amount_parent, '请输入数字');
				}
				if(isNaN(giftAmount)){
					BINOLMBSVC03_1.addErrorMessage(giftAmount_parent, '请输入数字');
				}
			}
			if(cardType == 2 || cardType == 3){
				var discountcount=$('#tyk input:visible:enabled[name="discountcount"]').length;
				var paySelect=$('#paySelect').val();
				var selectamount=$('#selectamount').val();
				var $parent_paySelect=$('#paySelect').parent();
				var giftamount=$('#giftamount').val();
				
				if(paySelect == -999 && (selectamount == null || selectamount == 'null' || selectamount == undefined || selectamount =='')){
					BINOLMBSVC03_1.addErrorMessage($('#selectamount').parent(), '请输入对应的充值金额');
					return false;
				}
				if(paySelect == -999 && isNaN(selectamount)){
					BINOLMBSVC03_1.addErrorMessage($('#selectamount').parent(), '请输入数字');
					return false;
				}
				if(paySelect == -999 && isNaN(giftamount)){
					BINOLMBSVC03_1.addErrorMessage($('#giftamount').parent(), '请输入数字');
					return false;
				}
			}
			var errorcount=$('.ui-icon.icon-error.tooltip-trigger-error').length;
			if(errorcount > 0){
				return false;
			}
			var rechargeDiscountList=$('#rechargeDiscountList').val();
			var counterCode=$('#counterCode').val();
			if(cardType!="" && cardType!=null){
				var counterCode=$('#counterName').val();
				var cardCode=$('#svc01_cardCode').val();
				var memo=$('#rechargeForm textarea[name="memo"]').val();
				var savingCardRechargeUrl=$('#savingCardRechargeUrl').attr('href');
				params="counterCode="+counterCode+"&cardType="+cardType+"&cardCode="+cardCode+"&memo="+memo;
				if(cardType == "1"){
					var amount=$('#amount').val();
					var giftAmount=$('#giftAmount').val();
					params+="&camount="+amount+"&giftAmount="+giftAmount;
				}
				if(cardType == "2" || cardType == "3"){
					var amount=$('#samount').val();
					if(amount == -999){
						amount=$('#fwamount').val();
					}
					var giftAmount_2=$('#giftamount').val();
					params+="&camount="+amount;
					var all=new Array();
					$('#tyk tr:gt(0)').each(function(){
						var serviceType=$(this).find('input:enabled[name="serviceType"]').val();
						var discountcount=$(this).find('input:enabled[name="serviceQuantity"]').val();
						if(serviceType == null || serviceType == 'null' || serviceType == undefined || serviceType ==''){
							return ;
						}
						if(discountcount == null || discountcount == 'null' || discountcount == undefined || discountcount ==''){
							discountcount=0;
						}
						var map={
								"ServiceType":parseInt(serviceType),
								"Quantity":parseInt(discountcount)
							};
						all.push(map);
					});
					var serviceArr=JSON.stringify(all);
					params+="&serviceArr="+serviceArr;
				}
				cherryAjaxRequest({
					url: savingCardRechargeUrl,
					param:params,
					callback: function(data) {
							var rusult_map = eval("("+data+")");
							var resultCode=rusult_map.resultCode;
							var resultMessage=rusult_map.resultMessage;
							if(resultCode == 1){
								var dialogId = "result_dialog";
								var $dialog = $('#' + dialogId);
								$dialog.find('span').text(resultMessage);
								$dialog.dialog({ 
									//默认不打开弹出框
									autoOpen: false,  
									//弹出框宽度
									width: 350, 
									//弹出框高度
									height: 250, 
									//弹出框标题
									title:$("#result_dialog_success").text(), 
									//弹出框索引
									zIndex: 1,  
									modal: true, 
									resizable:false,
									//弹出框按钮
									buttons: [{
										text:$("#dialogConfirm").text(),//确认按钮
										click: function() {
											BINOLMBSVC03_1.cancel();
											$dialog.dialog("close");
										}
									}],
									//关闭按钮
									close: function() {
										closeCherryDialog(dialogId);
									}
								});
								$dialog.dialog("open");
							}else{
								var dialogId = "result_dialog";
								var $dialog = $('#' + dialogId);
								$dialog.find('span').text(resultMessage);
								$dialog.dialog({ 
									//默认不打开弹出框
									autoOpen: false,  
									//弹出框宽度
									width: 350, 
									//弹出框高度
									height: 250, 
									//弹出框标题
									title:$("#result_dialog_error").text(), 
									//弹出框索引
									zIndex: 1,  
									modal: true, 
									resizable:false,
									//弹出框按钮
									buttons: [{
										text:$("#dialogConfirm").text(),//确认按钮
										click: function() {
//											BINOLMBSVC03_1.cancel();
											$dialog.dialog("close");
										}
									}],
									//关闭按钮
									close: function() {
										closeCherryDialog(dialogId);
									}
								});
								$dialog.dialog("open");
							}
					}
				});
			}
		},
		"businessTypeChange": function(){
			var businessType = $("#businessType").val();
			if(businessType=="1"){
				$("#opencxk").show();
				$("#purchaseTH2").show();
				$("#openfwk").hide();
			}else {
				if(businessType=="2"){
					$("#purchaseTH2").show();
					$("#purchaseTH").hide();
				}else if(businessType=="3"){
					$("#purchaseTH2").hide();
					$("#purchaseTH").show();
				}
				$("#opencxk").hide();
				$("#openfwk").show();
			}
			BINOLMBSVC03_1.radio($("#samount"));
		},
		"getServerList": function(){
			var obj = $("#serviceTb").clone();
			$("#serverList").show();
			$("#serverListShow").html("");
			$("#serverListShow").html(obj);
			
			closeCherryDialog('serverListDialogInit',WPCOM_dialogBody);
			// 清空弹出框内容
			$('#serverListDialogInit').html("");
			$("#serviceTb").removeAttr("id");
		},
		"serverListShow": function(){
			if($("#serverList").hasClass("hide")){
				if($("#serviceTb tr").length>0){
					$("#serverList").removeClass("hide");
					$("#choice1").hide();
					$("#choice2").show();
				}else {
					BINOLWPSAL02.showMessageDialog({
						message:"没有可选择的服务！", 
						type:"MESSAGE"
					});
				}
			}else {
				$("#serverList").addClass("hide");
				$("#choice1").show();
				$("#choice2").hide();
				$("#serviceTb").find("input").val("");
			}
		},
		"showDetailed": function(t){
			var cardCode = $(t).find("#dgCardCode").text();
			var obj = document.getElementById(cardCode).style.display;
			if(obj=="none"){
				$("#"+cardCode).show();
			}else {
				$("#"+cardCode).hide();
			}
			
		},
		"addErrorMessage":function($parent,errorMessage){
			$parent.addClass("error");
			$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
			$parent.find('#errorText').attr("title",'error|'+errorMessage);
			$parent.find('#errorText').cluetip({
		    	splitTitle: '|',
			    width: 150,
			    cluezIndex: 20000,
			    tracking: true,
			    cluetipClass: 'error', 
			    arrows: true, 
			    dropShadow: false,
			    hoverIntent: false,
			    sticky: false,
			    mouseOutClose: true,
			    closePosition: 'title',
			    closeText: '<span class="ui-icon ui-icon-close"></span>'
			});
		},
		"mouseleaveFc":function(obj){
//			$('#countCode').attr("class","");
			$(obj).parent().find('span').remove();
			$(obj).parent().attr("class","");
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
				BINOLMBSVC03_1.messageConfirm(dialogSetting.focusEvent);
			});
		}
	};




var BINOLMBSVC03_1 = new BINOLMBSVC03_1();

$(document).ready(function(){
	BINOLMBSVC03_1.businessTypeChange();
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#dialogInit').html();
});


