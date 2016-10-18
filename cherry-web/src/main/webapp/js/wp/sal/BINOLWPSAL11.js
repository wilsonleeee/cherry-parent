var BINOLWPSAL11_GLOBAL = function() {

};

BINOLWPSAL11_GLOBAL.prototype = {
	
	"saleContinue":function(){
		$("#dialogInit").find(".error").removeClass('error');
		$("#dialogInit").find("#errorText").remove();
		if(!$('#memberCodeForm').valid()) {
			// 给会员卡号输入框增加验证样式
			var errorText = $("#dgMemCode").parent().find("#errorText");
			$("#dgMemberCode").parent().attr("class","error");
			$("#dgMemberCode").attr("class","error");
			$("#dgMemberCode").after(errorText);
			return false;
		}
		var useMemberPrice = $("#useMemberPrice").val();
		var viewType = $("#dgViewType").val();
		if(viewType == "COL"){
			$("#btnContinue").attr("disabled",true);
			var memberCode = $("#dgMemCode").val();
			$("#searchStr").val(memberCode);
			$("#memberCode").val(memberCode);
			$('#txtMemberName').text(memberCode);
			
			if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
				// 计算会员价格和金额
				BINOLWPSAL02.calcuMemberSaleAmout();
			}
			// 将销售明细列表转换成Json字符串
			BINOLWPSAL02.getSaleDetailList();
			// 判断是否有支付方式
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
							height: 450,
							title: $("#collectDialogTitle").text(),
							closeEvent:function(){
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
						
						var baName = "";
						var baChooseModel = $("#baChooseModel").val();
						if(baChooseModel == "2"){
							baName = $("#baCode option:selected").text();
						}else{
							baName = $("#baName").val();
						}
						var counterCode = $("#counterCode").val();
//							var baName = $("#baCode option:selected").text();
//							var baName = $("#baName").val();
						var totalAmount = $("#totalAmount").val();
						var collectUrl = $("#collectUrl").attr("href");
						var params = "collectPageType=NSSR&counterCode=" + counterCode + "&baName=" + baName 
								+ "&memberName=" + memberCode + "&totalAmount=" +　totalAmount;
						cherryAjaxRequest({
							url: collectUrl,
							param: params,
							callback: function(data) {
								$("#btnContinue").removeAttr("disabled");
								$("#dialogInit").html(data);
								if($("#serviceTb tr").length<=0){
									$("#serverPay").hide();
								}
//									var czkPay = $("#NEW_CZK_PAY").val();
//									var czkType = $("#czkType").val();
//									if(czkPay!="Y"){
//										$("#serverPay").hide();
//									}else if(czkType!="2"){
//										$("#serverPay").hide();
//									}
							}
						});
					}
				}
			});
		}else{
			var memberCode = $("#dgMemCode").val();
			$("#searchStr").val(memberCode);
			$("#memberCode").val(memberCode);
			$('#txtMemberName').text(memberCode);
			
			if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
				// 计算会员价格和金额
				BINOLWPSAL02.calcuMemberSaleAmout();
			}			
			// 将销售明细列表转换成Json字符串
			BINOLWPSAL02.getSaleDetailList();
			
			// 可见文本框回车事件解绑
			$("#addMemberPageDiv").find("input:text:visible").unbind();
			// 关闭弹出窗口
			closeCherryDialog('dialogInit',WPCOM_dialogBody);
			// 清空弹出框内容
			$('#dialogInit').html("");
			// 最后一行第一个可见的文本框获得焦点
			BINOLWPSAL02.firstInputSelect();
			// 解除退货和补登按钮禁用
			$("#btnReturnsGoods").removeAttr("disabled");
			$("#btnReturnsGoods").attr("class","btn_top");
			$("#btnAddHistoryBill").removeAttr("disabled");
			$("#btnAddHistoryBill").attr("class","btn_top");
			// 解除清空购物车禁用
			$("#btnEmptyShoppingCart").removeAttr("disabled");
			$("#btnEmptyShoppingCart").attr("class","btn_top");
		}
	},
	
	"sendMessage" :function(){
		$("#CheckMessageDiv").addClass("hide");
		if(!$('#addMemberForm').valid()) {
			return false;
		}
		var dialogId = 'mobileCheckDialog';
		var $dialog = $("#" + dialogId);
		if($dialog.length == 0) {
			$("body").append('<div style="display:none" id="'+dialogId+'"></div>');
		} else {
			$dialog.empty();
		}
		$("#mobileCheckDiv").empty();
		var url = $("#mobileCheckInit").attr("href");
		var param = $("#addMemberForm").serialize();
		if(param) {
			url = url + "?" + param;
		}
		var mobile = $("#dgMobilePhone").val();
		param = "mobilePhone=" + mobile;
		cherryAjaxRequest({
			url: url,
			param:param,
			callback: function(msg){
				//如果返回的不是错误信息验证页面则跳转到发送验证码页面
				if(msg.indexOf("fieldErrorDiv")<0){
					$dialog.html(msg);
					// 弹出验证框
					var dialogSetting = {
						dialogInit: "#" + dialogId,
						text: msg,
						width: 	400,
						height: 310,
						
						title: 	'短信验证',
						confirm: '验证',
						cancel : '返回',
						confirmEvent: function(){

							$("#errorMessageDiv").addClass("hide");
							//获取手机号码以及输入的校验码
							var mobileNumber = $("#mobilePhoneC").val();
							var couponCode = $("#mobileCheckDialog #couponCode").val();
							var par = "mobilePhone="+mobileNumber+"&couponCode="+couponCode;
							cherryAjaxRequest({
								url:$("#couponCheck").attr("href"),
								param:par,
								callback:function(flag){
									if(flag==0){
										//移除验证窗口
										removeDialog("#" + dialogId);
										//保存会员
										BINOLWPSAL11.save();
									}else if(flag==1){
										$("#errorMessageDiv").removeClass("hide");
									}
								}
							});
						},
						cancelEvent: function(){
							removeDialog("#" + dialogId);
						}
					};
					openDialog(dialogSetting);
				
				}
			}
		});
	},
	
	"save":function(){
		$("#dialogInit").find(".error").removeClass('error');
		$("#dialogInit").find("#errorText").remove();
		if(!$('#addMemberForm').valid()) {
			return false;
		}
		$("#btnSave").attr("disabled",true);
		var useMemberPrice = $("#useMemberPrice").val();
		var $form = $('#addMemberForm');
		var params = $form.serialize();
		var addMemberUrl = $("#addMemberInfoUrl").attr("href");
		cherryAjaxRequest({
			url: addMemberUrl,
			param: params,
			callback: function(data) {
				$("#btnSave").removeAttr("disabled");
				if(data != undefined && data != null && data != ""){
					if(data == "ERROR"){
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"会员记录保存失败", 
							type:"MESSAGE", 
							focusEvent:function(){
								$("#dgMemberCode").select();
							}
						});
					}else if(data == "SEARCHERROR"){
						var memberCode = $("#dgMemberCode").val();
						var memberName = $("#dgMemberName").val();
						var mobilePhone = $("#dgMobilePhone").val();
						var joinDate = $("#nowDate").val();
						$('#memberCode').val(memberCode);
						$('#memberName').val(memberName);
						$('#mobilePhone').val(mobilePhone);
						$('#txtMemberName').text(memberName);
						$('#spanMemberCode').text(memberCode);
						$('#spanMemberName').text(memberName);
						$('#spanJoinDate').text(joinDate);
						$("#searchStr").val(memberCode);
						
						if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
							// 计算会员价格和金额
							BINOLWPSAL02.calcuMemberSaleAmout();
						}
						// 将销售明细列表转换成Json字符串
						BINOLWPSAL02.getSaleDetailList();
						
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
						
						// 判断是否有支付方式
						var paymentSizeUrl = $("#paymentSizeUrl").attr("href");
						cherryAjaxRequest({
							url: paymentSizeUrl,
							param: null,
							callback: function(data) {
								var isCA = $("#isCA").val();
								var viewType = $("#dgViewType").val();
								if(Number(data)<1 && "Y"==isCA){
									BINOLWPSAL02.directPayment();
								}else if(viewType == "COL"){
									var dialogSetting = {
										dialogInit: "#dialogInit",
										width: 700,
										height: 450,
										title: $("#collectDialogTitle").text(),
										closeEvent:function(){
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
									
									var baName = "";
									var baChooseModel = $("#baChooseModel").val();
									if(baChooseModel == "2"){
										baName = $("#baCode option:selected").text();
									}else{
										baName = $("#baName").val();
									}
									var counterCode = $("#counterCode").val();
//										var baName = $("#baCode option:selected").text();
//										var baName = $("#baName").val();
									var totalAmount = $("#totalAmount").val();
									var collectUrl = $("#collectUrl").attr("href");
									var param = "collectPageType=NSSR&counterCode=" + counterCode + "&baName=" + baName 
											+ "&memberName=" + memberName + "&totalAmount=" +　totalAmount;
									cherryAjaxRequest({
										url: collectUrl,
										param: param,
										callback: function(data) {
											$("#dialogInit").html(data);
										}
									});
								}else{
									// 显示提示信息
									BINOLWPSAL02.showMessageDialog({
										message:"会员入会成功，但是获取会员活动信息可能出现异常，请点击会员查询按钮尝试刷新", 
										type:"MESSAGE", 
										focusEvent:function(){
											// 会员查询条件输入框获得焦点
											$("#searchStr").focus();
										}
									});
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
								}
							}
						});
					}else{
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
						$("#searchStr").val(memberInfo.memberCode);
						
						var memberCode = memberInfo.memberCode;
						if(useMemberPrice == "Y" && memberCode != null && memberCode != undefined && memberCode.toString().trim() != ""){
							// 计算会员价格和金额
							BINOLWPSAL02.calcuMemberSaleAmout();
						}
						// 将销售明细列表转换成Json字符串
						BINOLWPSAL02.getSaleDetailList();
						
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
						// 获取入会后是否显示收款页面配置
						var showCollectAfterJoin = $("#showCollectAfterJoin").val();
						// 获取页面显示类型配置
						var viewType = $("#dgViewType").val();
						if(showCollectAfterJoin == "N"){
							viewType = "SCH";
						}
						if(viewType == "COL"){
							if(memberInfo.memberValidState == "T"){
								// 显示提示信息
								BINOLWPSAL02.showMessageDialog({
									message:"会员已存在，将使用当前已存在的会员信息", 
									type:"MESSAGE", 
									focusEvent:function(){
										// 判断是否有支付方式
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
														height: 450,
														title: $("#collectDialogTitle").text(),
														closeEvent:function(){
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
													
													var baName = "";
													var baChooseModel = $("#baChooseModel").val();
													if(baChooseModel == "2"){
														baName = $("#baCode option:selected").text();
													}else{
														baName = $("#baName").val();
													}
													var counterCode = $("#counterCode").val();
//														var baName = $("#baCode option:selected").text();
//														var baName = $("#baName").val();
													var totalAmount = $("#totalAmount").val();
													var collectUrl = $("#collectUrl").attr("href");
													var param = "collectPageType=NSSR&counterCode=" + counterCode + "&baName=" + baName 
															+ "&memberName=" + memberInfo.memberName + "&totalAmount=" +　totalAmount;
													cherryAjaxRequest({
														url: collectUrl,
														param: param,
														callback: function(data) {
															$("#dialogInit").html(data);
														}
													});
												}
											}
										});
									}
								});
							}else{
								// 判断是否有支付方式
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
												height: 450,
												title: $("#collectDialogTitle").text(),
												closeEvent:function(){
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
											
											var baName = "";
											var baChooseModel = $("#baChooseModel").val();
											if(baChooseModel == "2"){
												baName = $("#baCode option:selected").text();
											}else{
												baName = $("#baName").val();
											}
											var counterCode = $("#counterCode").val();
//												var baName = $("#baCode option:selected").text();
//												var baName = $("#baName").val();
											var totalAmount = $("#totalAmount").val();
											var collectUrl = $("#collectUrl").attr("href");
											var param = "collectPageType=NSSR&counterCode=" + counterCode + "&baName=" + baName 
													+ "&memberName=" + memberInfo.memberName + "&totalAmount=" +　totalAmount;
											cherryAjaxRequest({
												url: collectUrl,
												param: param,
												callback: function(data) {
													$("#dialogInit").html(data);
												}
											});
										}
									}
								});
							}
						}else{
							if(memberInfo.memberValidState == "T"){
								// 显示提示信息
								BINOLWPSAL02.showMessageDialog({
									message:"会员已存在，将使用当前已存在的会员信息", 
									type:"MESSAGE", 
									focusEvent:function(){
										// 最后一行第一个可见的文本框获得焦点
										BINOLWPSAL02.firstInputSelect();
									}
								});
							}else{
								// 显示提示信息
								BINOLWPSAL02.showMessageDialog({
									message:"信息已保存，会员入会成功", 
									type:"SUCCESS", 
									focusEvent:function(){
										// 最后一行第一个可见的文本框获得焦点
										BINOLWPSAL02.firstInputSelect();
									}
								});
							}
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
							BINOLWPSAL02.search();
						}
						var refresh = function() {
							// 查询符合条件的促销活动
							var setParams = {};
							setParams.memberInfoId = memberInfo.memberInfoId;
							setParams.mobilePhone = memberInfo.mobilePhone;
							setParams.changablePoint = memberInfo.changablePoint;
							BINOLWPSAL02.getPromotion(setParams);
						}
						setTimeout(refresh,500);
					}
				}else{
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"会员记录保存失败", 
						type:"MESSAGE", 
						focusEvent:function(){
							$("#dgMemberCode").select();
						}
					});
				}
			}
		});
	},
	
	"notJoinMember":function(){
		// 将销售明细列表转换成Json字符串
		BINOLWPSAL02.getSaleDetailList();
		
		// 判断是否有支付方式
		var paymentSizeUrl = $("#paymentSizeUrl").attr("href");
		cherryAjaxRequest({
			url: paymentSizeUrl,
			param: null,
			callback: function(data) {
				var isCA = $("#isCA").val();
				var viewType = $("#dgViewType").val();
				if(Number(data)<1 && "Y"==isCA){
					BINOLWPSAL02.directPayment();
				}else if(viewType == "COL"){
					$("#btnNotJoin").attr("disabled",true);
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
					var baName = "";
					var baChooseModel = $("#baChooseModel").val();
					if(baChooseModel == "2"){
						baName = $("#baCode option:selected").text();
					}else{
						baName = $("#baName").val();
					}
					var counterCode = $("#counterCode").val();
//					var baName = $("#baCode option:selected").text();
//					var baName = $("#baName").val();
					var totalAmount = $("#totalAmount").val();
					var collectUrl = $("#collectUrl").attr("href");
					var param = "collectPageType=NSSR&counterCode=" + counterCode + "&baName=" + baName 
							+ "&totalAmount=" +　totalAmount;
					cherryAjaxRequest({
						url: collectUrl,
						param: param,
						callback: function(data) {
							$("#btnNotJoin").removeAttr("disabled");
							$("#dialogInit").html(data);
							if($("#serviceTb tr").length<=0){
								$("#serverPay").hide();
							}
//							var czkPay = $("#NEW_CZK_PAY").val();
//							var czkType = $("#czkType").val();
//							if(czkPay!="Y"){
//								$("#serverPay").hide();
//							}else if(czkType!="2"){
//								$("#serverPay").hide();
//							}
						}
					});
				}else{
					// 可见文本框回车事件解绑
					$("#addMemberPageDiv").find("input:text:visible").unbind();
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
					// 最后一行第一个可见文本框获得焦点
					BINOLWPSAL02.firstInputSelect();
				}
			}
		});
	},
	
	"cancel":function(){
		//清空之前的促销列表
		BINOLWPSAL02.removePromotionList();
		// 可见文本框回车事件解绑
		$("#addMemberPageDiv").find("input:text:visible").unbind();
		
		var viewType = $("#dgViewType").val();
		if(viewType == "COL"){
			// 还原按钮样式
			$("#btnCollect").attr("class","btn_top");
			// 增加新行
			BINOLWPSAL02.firstInputSelect();
		}
		// 关闭弹出窗口
		closeCherryDialog('dialogInit',WPCOM_dialogBody);
		// 清空弹出框内容
		$('#dialogInit').html("");
		
		// 添加一个空行
		BINOLWPSAL02.addNewLine();
		
		// 解除退货和补登按钮禁用
		$("#btnReturnsGoods").removeAttr("disabled");
		$("#btnReturnsGoods").attr("class","btn_top");
		$("#btnAddHistoryBill").removeAttr("disabled");
		$("#btnAddHistoryBill").attr("class","btn_top");
		// 解除清空购物车禁用
		$("#btnEmptyShoppingCart").removeAttr("disabled");
		$("#btnEmptyShoppingCart").attr("class","btn_top");
		// 会员搜索条件输入框获得焦点
		$("#searchStr").focus();
		
		// 判断是否有支付方式
		var isCA = $("#isCA").val();
		if("Y"==isCA){
			var paymentSizeUrl = $("#paymentSizeUrl").attr("href");
			// 将销售明细列表转换成Json字符串
			BINOLWPSAL02.getSaleDetailList();
			cherryAjaxRequest({
				url: paymentSizeUrl,
				param: null,
				callback: function(data) {
					if(Number(data)<1){
						BINOLWPSAL02.directPayment();
					}
				}
			});
		}
	},
	
	"changeMemberCode": function(obj){
		var $this = $(obj);
		var $thisVal =$this.val();
		if($thisVal != undefined && $thisVal != ""){
			var memberCode = $thisVal.toString().trim();
			$("#dgMemCode").val(memberCode);
		}else{
			$("#dgMemCode").val("");
		}
	},
	
	// 生日框初始化处理
	"birthDayInit":function(){
		for(var i = 1; i <= 12; i++) {
			$("#birthMonthValue").append('<option value="'+i+'">'+i+'</option>');
		}
		$("#birthMonthValue").change(function(){
			var $date = $("#birthDayValue");
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
	
	// 省、市、县级市的联动查询
	"getNextRegin": function(obj, text, grade) {
		var $obj = $(obj);
		// 区域ID
		var id =  $obj.attr("id");
		// 区域名称
		var name =  $obj.text();
		// 下一级标志
		var nextGrade = 1;
		$("#cityText").parent().parent().removeClass('error');
		$("#cityText").parent().parent().find('#errorText').remove();
		// 选择省的场合
		if(grade == 1) {
			// 设置省名称
			$("#provinceText").text(name);
			// 省hidden值修改
			if(id && id.indexOf("_") > 0) {
				var arrayId = id.split("_");
				$("#regionId").val(arrayId[0]);
				$("#provinceId").val(arrayId[1]);
				id = arrayId[1];
			} else {
				$("#provinceId").val(id);
			}
			// 城市不存在的场合
			if($('#cityId').length == 0) {
				return false;
			}
			// 清空城市信息
			$('#cityId').val("");
			$("#cityText").text(text);
			$('#cityTemp').empty();
			// 清空县级市信息
			$('#countyId').val("");
			$("#countyText").text(text);
			$('#countyTemp').empty();
			nextGrade = 2;
		} 
		// 选择城市的场合
		else if(grade == 2) {
			// 设置城市名称
			$("#cityText").text(name);
			// 城市hidden值修改
			$("#cityId").val(id);
			// 县级市不存在的场合
			if($('#countyId').length == 0) {
				return false;
			}
			// 清空县级市信息
			$('#countyId').val("");
			$("#countyText").text(text);
			$('#countyTemp').empty();
			nextGrade = 3;
		} 
		// 选择县级市的场合
		else if(grade == 3) {
			// 设置县级市名称
			$("#countyText").text(name);
			// 县级市hidden值修改
			$("#countyId").val(id);
			return false;
		}
		if(id == undefined || id == '') {
			return false;
		}
		var url = $('#querySubRegionUrl').val();
		var param = 'regionId=' + id;
		var callback = function(msg) {
			if(msg) {
				// 全部
				var defaultText = $('#defaultText').text();
				var json = eval('('+msg+')'); 
				var str = '<ul class="u2" style="width: 500px;"><li onclick="BINOLWPSAL11.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+defaultText+'</li>';
			    for (var one in json){
			        str += '<li id="'+json[one]["regionId"]+'" onclick="BINOLWPSAL11.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+escapeHTMLHandle(json[one]["regionName"])+'</li>';
			    }
			    str += '</ul>';
			    if(grade == 1) {
			    	$("#cityTemp").html(str);
			    } else if(grade == 2) {
			    	$("#countyTemp").html(str);
			    }
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},
	
	// 显示省、市或县级市信息
	"showRegin": function(object, reginDiv) {
		var $reginDiv = $('#'+reginDiv);
		if($reginDiv.is(':hidden') && $reginDiv.find('li').length > 0) {
			var opos = $(object).position();
			var oleft = parseInt(opos.left + 8, 10);
			var otop = parseInt(opos.top + $(object).outerHeight() + 10, 10);
			$reginDiv.css({'left': oleft + "px", 'top': otop });
			$reginDiv.show();
			
			if(reginDiv != 'provinceTemp') {
				$('#provinceTemp').hide();
			}
			if(reginDiv != 'cityTemp') {
				$('#cityTemp').hide();
			}
			if(reginDiv != 'countyTemp') {
				$('#countyTemp').hide();
			}
			var firstFlag = true;
			$("body").unbind('click');
			// 隐藏弹出的DIV
			$("body").bind('click',function(event){
				if(!firstFlag) {
					$reginDiv.hide();
					$("body").unbind('click');
				}
				firstFlag = false;
			});
		}
	},
	
	"synMoblie" : function(Object){
		var $this = $(Object);
		var memCode = $this.val();
		$("#dgMobilePhone").val(memCode);
		$("#dgMemCode").val(memCode);
	}
};

var BINOLWPSAL11 = new BINOLWPSAL11_GLOBAL();

$(document).ready(function(){
	// 表单验证初期化,根据配置项来看生日是否为必填项
	var birthFlag=$("#birthFlag").val();
	if(birthFlag == "Y"){
		cherryValidate({
			formId: 'addMemberForm',
			rules: {
				dgMemberCode: {required: true, memberValid: $("#memberRule").val(), maxlength: 20},
				dgMemberName: {required: true, maxlength: 30},
				dgBaInfoId: {required: true},
				dgMobilePhone: {required: true, phoneValid: $("#mobileRule").val(), maxlength: 20},
				dgTelephone: {telValid: true, maxlength: 20},
				dgBirthday: {required: true, dateValid: true},
				birthMonthValue: {required: true},
				birthDayValue: {required: true},
				dgEmail: {emailValid: true, maxlength: 60},
				dgGender: {required: true},
				dgReferrer: {maxlength: 20, memberValid: $("#memberRule").val()},
				dgIdentityCard: {maxlength: 18},
				dgBlogId: {maxlength: 30},
				dgMessageId: {maxlength: 30},
				dgMemo: {maxlength: 500},
				dgAddress: {maxlength: 100},
				dgPostCode: {zipValid:true, maxlength: 10}
			}
		});
	}else{
		cherryValidate({
			formId: 'addMemberForm',
			rules: {
				dgMemberCode: {required: true, memberValid: $("#memberRule").val(), maxlength: 20},
				dgMemberName: {required: true, maxlength: 30},
				dgBaInfoId: {required: true},
				dgMobilePhone: {required: true, phoneValid: $("#mobileRule").val(), maxlength: 20},
				dgTelephone: {telValid: true, maxlength: 20},
				dgEmail: {emailValid: true, maxlength: 60},
				dgGender: {required: true},
				dgReferrer: {maxlength: 20, memberValid: $("#memberRule").val()},
				dgIdentityCard: {maxlength: 18},
				dgBlogId: {maxlength: 30},
				dgMessageId: {maxlength: 30},
				dgMemo: {maxlength: 500},
				dgAddress: {maxlength: 100},
				dgPostCode: {zipValid:true, maxlength: 10}
			}
		});
	}
	
	cherryValidate({
		formId: 'memberCodeForm',
		rules: {
			dgMemCode: {required: true, memberValid: $("#memberRule").val(), maxlength: 20},
			dgMemberCode: {required: true, memberValid: $("#memberRule").val(), maxlength: 20}
		}
	});
	
	$("#addMemberPageDiv").find("input:text:visible")
	.bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			BINOLWPSAL11.saleContinue();
		}
	});
	
	// 初始化月日选择框
	BINOLWPSAL11.birthDayInit();
	
	// 初始化日历控件
	/*$("#dgBirthday").cherryDate({
		beforeShow: function(input){
			var value = $("#nowDate").val();
			return ["minValue", value];
		}
	});*/
	$('#dgBirthday').cherryDate({yearRange: 'c-100:c'});
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		BINOLWPSAL11.showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		BINOLWPSAL11.showRegin(this,"cityTemp");
	});
	// 区县选择
	$("#county").click(function(){
		// 显示城市列表DIV
		BINOLWPSAL11.showRegin(this,"countyTemp");
	});
	
	$("#dgBaInfoId").val($("#dgBaInfoCode").val());
	
	$("#dgMemberCode").select();
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#dialogInit').html();
});

