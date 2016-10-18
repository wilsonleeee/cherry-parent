var WPCOM_dialogBody ="";
var BINOLWPSAL13_GLOBAL = function() {

};

BINOLWPSAL13_GLOBAL.prototype = {
	    /*
	      // 储值卡
	    */
		
		"confirm2":function(){
			// 关闭弹出窗口
			closeCherryDialog('dialogInit');
			$('#dialogInit').html("");
		},
		"cancel":function(){
			closeCherryDialog('dialogInit');
			// 清空弹出框内容
			$('#dialogInit').html("");
		},
		
		"confirm3":function(){
			var cardCode = $("#cardForm").find("[name='cardCode']").val();
			if(cardCode==""){
				BINOLWPSAL02.showMessageDialog({
					message:"请输入卡号", 
					type:"MESSAGE"
				});
			}else {
				cherryAjaxRequest({
					url: $("#dgServerList").attr("href"),
					param: "cardCode="+cardCode,
					callback: function(data) {
						if(data=="NOEXIST"){
							BINOLWPSAL02.showMessageDialog({
								message:"卡号不存在", 
								type:"MESSAGE"
							});
						}else if(data=="1"){
							BINOLWPSAL02.showMessageDialog({
								message:"此卡为储蓄卡，没有服务可选！", 
								type:"MESSAGE"
							});
						}else {
							// 关闭弹出窗口
							closeCherryDialog('serverListDialogInit',WPCOM_dialogBody);
							$('#serverListDialogInit').html("");
							$("#serverListShow").html("");
							var dialogSetting = {
									dialogInit: "#serverListDialogInit",
									title: "选择服务",
									width: 500,
									height: 300,
									closeEvent:function(){
										removeDialog("#serverListDialogInit");
									}
							};
							openDialog(dialogSetting);
							$("#serverListDialogInit").html(data);
						}
					}
				});
			}
		},
		"cancel3":function(){
			closeCherryDialog('serverListDialogInit',WPCOM_dialogBody);
			// 清空弹出框内容
			$('#serverListDialogInit').html("");
		},
		"cancel4":function(){
			closeCherryDialog('revokeInitDialogInit',WPCOM_dialogBody);
			// 清空弹出框内容
			$('#revokeInitDialogInit').html("");
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
			$.each($(t).parent().siblings(),function(){
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
		// 充值
		"commit":function(){
			var businessType = $("#businessType").val();
			var rechargeUrl = $("#rechargeUrl").attr("href");
			var params = "";
			if(businessType!="" && businessType!=null){
				var amount = "";
				if(businessType=="1"){
					amount = $("#amount").val();
				}else {
					amount = $("#fwamount").val();
				}
				var cardCode = $("#cardCode").val();
				var memo = $(":visible[name='memo']").val();
				var giftAmount = $("#giftAmount").val();
				params = "businessType="+businessType+"&amount="+parseInt(amount)+"&cardCode="+cardCode+"&memo="+memo+"&giveAmount="+giftAmount;
				if(businessType!="1"){
					var serviceList = new Array();
					$.each($("#tyk tr"),function(){
						if($(this).find("[name='serviceQuantity']").val()!="" && $(this).find("[name='serviceQuantity']").val()!=undefined){
							var serviceType = $(this).attr("id");
							var serviceQuantity = $(this).find("[name='serviceQuantity']").val();
							var obj = {"ServiceType":parseInt(serviceType),"Quantity":parseInt(serviceQuantity)};
							serviceList.push(obj);
						}
					});
					serviceList = JSON.stringify(serviceList);
					params+="&serviceJsonList="+serviceList;
				};
				cherryAjaxRequest({
					url: rechargeUrl,
					param:params,
					callback: function(data) {
						if(data=="SUCCESS"){
//							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"充值成功", 
								type:"SUCCESS"
							});
							closeCherryDialog('dialogInit');
						}else if(data=="NA"){
//							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"充值金额不能小于零", 
								type:"MESSAGE"
							});
						}else if(data=="NAL"){
//							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"充值金额与服务不能同时为空", 
								type:"MESSAGE"
							});
						}else if(data=="NC"){
//							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"卡号为空", 
								type:"MESSAGE"
							});
						}else if(data=="NT"){
//							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"业务类型为空", 
								type:"MESSAGE"
							});
						}else if(data=="SRE0013"){
//							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:data, 
								type:"MESSAGE"
							});
						}else {
							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"充值失败"+data, 
								type:"MESSAGE"
							});
						}
					}
				})
			}
		},
		
		"createCard":function(){
			var	tel = $("#mobilePhone2").val(); //获取手机号
			var telReg = !!tel.match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/);
			//如果手机号码不能通过验证
			if(telReg == false){
				if(tel==""){
					BINOLWPSAL02.showMessageDialog({
						message:"手机号不能为空", 
						type:"MESSAGE"
					});
				}else {
					BINOLWPSAL02.showMessageDialog({
						message:"手机号不合法", 
						type:"MESSAGE"
					});
				}
				return false;
			}
/*			if(!$('#rechargeForm').valid()) {
				return false;
			}*/
//			var ps = BINOLWPSAL13.password();
//			if(ps==false){
//				return false;
//			}
//			var params = $('#rechargeForm').serialize();
			var amount = "";
			var businessType = $("#businessType").val();
			if(businessType=="1"){
				amount = $("#amount").val();
			}else {
				amount = $("#fwamount").val();
			}
			var cardCode = $("#cardCode").val();
			var memo = $(":visible[name='memo']").val();
			var mobilePhone = $("#mobilePhone2").val();
			var cardCode = $("#cardCode").val();
			var giftAmount = $("#giftAmount").val();
			var memberCode = $("#memberCode").val();
			var businessType = $("#businessType").val();
			params = "mobilePhone="+mobilePhone+"&amount="+parseInt(amount)+"&giveAmount="
						+giftAmount+"&cardCode="+cardCode+"&memCode="+memberCode+"&memo="+memo+"&businessType="+businessType;
			if(businessType!="1"){
				var serviceList = new Array();
				$.each($("#tyk tr"),function(){
					if($(this).find("[name='serviceQuantity']").val()!="" && $(this).find("[name='serviceQuantity']").val()!=undefined){
						var serviceType = $(this).attr("id");
						var serviceQuantity = $(this).find("[name='serviceQuantity']").val();
						var obj = {"ServiceType":parseInt(serviceType),"Quantity":parseInt(serviceQuantity)};
						serviceList.push(obj);
					}
				});
				serviceList = JSON.stringify(serviceList);
				params+="&serviceJsonList="+serviceList;
			};
			var createCardUrl = $("#createCard").attr("href");
			cherryAjaxRequest({
				url: createCardUrl,
				param: params,
				callback: function(data) {
					if(data=="SUCCESS"){
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"开卡成功", 
							type:"SUCCESS"
						});
						closeCherryDialog('dialogInit');
					}else if(data=="EXISTE"){
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"此卡已存在", 
							type:"MESSAGE"
						});
						closeCherryDialog('dialogInit');
					}else if(data=="SAE0011"){
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"密码超出长度限制！ ", 
							type:"MESSAGE"
						});
						closeCherryDialog('dialogInit');
					}else if(data=="SAE0013"){
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"充值金额格式不正确！ ", 
							type:"MESSAGE"
						});
						closeCherryDialog('dialogInit');
					}else if(data=="SAE0015"){
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"服务明细参数中存在不合法的数据！ ", 
							type:"MESSAGE"
						});
						closeCherryDialog('dialogInit');
					}else if(data=="SAE0024"){
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"预留手机号为空！ ", 
							type:"MESSAGE"
						});
						closeCherryDialog('dialogInit');
					}else if(data=="SAE0001"){
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"储值卡号为空！", 
							type:"MESSAGE"
						});
						closeCherryDialog('dialogInit');
					}else {
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"开卡失败"+data, 
							type:"MESSAGE"
						});
					}
				}
			});
		},
//		"password":function(){
//			var ps1 = $("#ps1").val();
//			var ps2 = $("#ps2").val();
//			var $parent1 = $("#ps1").parent();
//			var $parent2 = $("#ps2").parent();
//			if(ps1!=ps2 && ps1!="" && ps2!=""){
//				var errorMessage = "密码不一致！";
//				BINOLWPSAL13.addErrorMessage($parent1,errorMessage);
//				BINOLWPSAL13.addErrorMessage($parent2,errorMessage);
////				obj.addClass("error");
////				obj.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
//				return false;
//			}else {
//				BINOLWPSAL13.mouseleaveFc($parent1);
//				BINOLWPSAL13.mouseleaveFc($parent2);
//			}
//		},
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
		"confirm":function(){
			var buttonState=$("#recharge" ).attr("disabled" );
            if(buttonState == "disabled"){
                  return ;
            }
			
			var memberCode = $("#memberCode").val();
			var cardCode = "";
			// 查出有多张卡时为输入的卡号充值
			var dgCardCode = $("#dgCardCode").val();
			if(dgCardCode == ""){
				BINOLWPSAL02.showMessageDialog({
					message:"请输入储值卡号！", 
					type:"MESSAGE"
				});
				return false;
			}else if(dgCardCode != null && dgCardCode != undefined && dgCardCode != ""){
				cardCode = dgCardCode;
			}
			var url = $("#checkCardCode").attr("href");
			params = "memCode="+memberCode+"&cardCode="+cardCode;
			cherryAjaxRequest({
				url: url,
				param: params,
				callback: function(data) {
					if(data=="Failure"){
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"查询卡号失败", 
							type:"MESSAGE"
						});
						return false;
					}else if(data=="ERROR"){
						// 显示提示信息
						BINOLWPSAL02.showMessageDialog({
							message:"很抱歉，接口连接失败！", 
							type:"MESSAGE"
						});
						return false;
					}else if(memberCode==""){
						if(data=="MORE" || cardCode==""){
							cherryAjaxRequest({
								url: $("#initUrl").attr("href"),
								callback: function(data) {
									var dialogSetting = {
										dialogInit: "#dialogInit",
										width: 500,
										height: 300,
										closeEvent:function(){
											removeDialog("#dialogInit");
										}
									};
									openDialog(dialogSetting);
									$("#dialogInit").html(data);
								}
							});
						}else {
							var consumptionUrl = $("#consumption").attr("href");
							if(consumptionUrl!=null && consumptionUrl!=""){
								BINOLWPSAL13.getConsumption(consumptionUrl,data,dgCardCode);
							}
							closeCherryDialog('dialogInit');
						}
					}else if((data=="MORE" || data=="NOEXIST")){
						if(data=="NOEXIST" && cardCode!=""){
							BINOLWPSAL02.showMessageDialog({
								message:"卡号不正确", 
								type:"MESSAGE"
							});
						}else if(data=="MORE"){
							cherryAjaxRequest({
								url: $("#initUrl").attr("href"),
								callback: function(data) {
									var dialogSetting = {
										dialogInit: "#dialogInit",
										width: 500,
										height: 300,
										closeEvent:function(){
											removeDialog("#dialogInit");
										}
									};
									openDialog(dialogSetting);
									$("#dialogInit").html(data);
								}
							});
						}else {
							var consumptionUrl = $("#consumption").attr("href");
							if(consumptionUrl!=null && consumptionUrl!=""){
								BINOLWPSAL13.getConsumption(consumptionUrl,data,dgCardCode);
							}
							closeCherryDialog('dialogInit');
						}
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
					}else {
						var consumptionUrl = $("#consumption").attr("href");
						if(consumptionUrl!=null && consumptionUrl!=""){
							BINOLWPSAL13.getConsumption(consumptionUrl,data,dgCardCode);
						}
						closeCherryDialog('dialogInit');
					}
				}
			})
		},
		"getConsumption":function(consumptionUrl,data,dgCardCode){
			var consumption = "";
			var cardCode = "";
			var cardType = "";
			var amount = "";
			var mobilePhone = "";
			if(data != 'null' &&　data != "" && data != null && data != "NOEXIST"){
				consumption = eval("("+data+")");
				cardCode = consumption.CardCode;
				amount = consumption.Amount;
				mobilePhone = consumption.MobilePhone;
			}
			if(mobilePhone==""){
				mobilePhone = $("#mobilePhone").val();
			}
			var memberCode = $("#memberCode").val();
			var dialogSetting = {
				dialogInit: "#dialogInit",
				width: 900,
				height: 600,
				closeEvent:function(){
					removeDialog("#dialogInit");
				}
			};
			var params = "memberCode="+memberCode+"&cardCode="+cardCode;
			cherryAjaxRequest({
				url: consumptionUrl,
				param: params,
				callback: function(data) {
					$("#dialogInit").html(data);
					if(cardCode != ""){
						dialogSetting.title = $("#rechargeDialogTitle2").text();
					}else {
						dialogSetting.title = $("#rechargeDialogTitle3").text();
					}
					openDialog(dialogSetting);
					$("#dialogInit").html(data);
					if(cardCode != ""){
						$("#btnCrefirm").hide();
						$("#cardCode").val(cardCode);
						$("#cardCodeSpan").text(cardCode);
						$("#R_amountSpan").text(amount);
						$("#cardCodeSpanTD").text(amount);
						$("#mobilePhone2Span").text(mobilePhone);
						$("#mobilePhone2cls").hide();
					}else {
						$("#mobilePhone2").removeAttr("readonly");
						if(memberCode!=""){
							$("#cardCode").val(memberCode);
							$("#cardCodeSpan").text(memberCode);
						}else {
							$("#cardCode").val(dgCardCode);
							$("#cardCodeSpan").text(dgCardCode);
						}
						$("#btnConfirm").hide();
						$("#mobilePhone2").show();
						$("#mobilePhone2").val(mobilePhone);
						$("#mobilePhone2cls").show();
					}
				}
			});
		},
		"checkNumber":function(t){
			if(isNaN($(t).val())){
				$(t).val("");
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
			BINOLWPSAL13.radio($("#samount"));
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
		"transactionDetailInit": function(t){
			closeCherryDialog('dialogInit');
			var cardCode = $(t).parent().siblings("#dgCardCode").text();
			var dialogSetting = {
				title: "交易明细",
				dialogInit: "#dialogInit",
				width: 1200,
				height: 700,
				closeEvent:function(){
					removeDialog("#dialogInit");
				}
			};
			var url = $("#transactionDetailInit").attr("href");
			params = getSerializeToken()+"&cardCode="+cardCode;
			cherryAjaxRequest({
				url: url,
				param: params,
				callback: function(data) {
					// 关闭弹出窗口
//					closeCherryDialog('dialogInit');
					openDialog(dialogSetting);
					$("#dialogInit").html(data);
					BINOLWPSAL13.transactionDetail(cardCode);
				}
			})
		},
		"transactionDetail": function(){
			var url = $("#transactionDetail").attr("href");
			var params = getSerializeToken()+"&"+$("#transactionDetailForm").serialize();
			//表格列属性
			var aoColumnsArr = [
			                    {"sName" : "transactionTime", "sWidth": "20%"},
			                    {"sName" : "transactionType", "sWidth" : "19%"},
			                    {"sName" : "amount", "sWidth" : "19%"},
			                    {"sName" : "billCode", "sWidth" : "25%", "bSortable": false},
			                    {"sName" : "act", "sWidth" : "15%", "bSortable": false}
			                    ];
			url = url + "?" + params;
			// 表格设置
			var tableSetting = {
				// datatable 对象索引
				index : 30,
				// 表格ID
				tableId : "#transactionDetailTable",
				// 数据URL
				url : url,
				// 排序列
				aaSorting : [ [ 0, "desc" ] ],
				// 表格列属性设置
				aoColumns : aoColumnsArr,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 默认显示行数
				iDisplayLength : 10,
				fnDrawCallback : function() {
				}
			};
			// 调用获取表格函数
			getTable(tableSetting);
		},
		"revokeInit": function(t){
			var transactionType = $(t).parent().find("[name='transactionType']").val();
			if(transactionType=="RU"){
				BINOLWPSAL02.showMessageDialog({
					message:"单据已撤销！", 
					type:"MESSAGE"
				});
				return;
			}else if(transactionType!= "DE"){
				BINOLWPSAL02.showMessageDialog({
					message:"不是充值单据不能撤销！", 
					type:"MESSAGE"
				});
				return;
			}
			var url = $("#revokeInit").attr("href");
			var billCode = $(t).parent().find("[name='billCode']").val();
			var cardCode = $("#dgCardCode").val();
			var params = "billCode="+billCode+"&cardCode="+cardCode;
			var dialogSetting = {
				title: "充值撤销",
				dialogInit: "#revokeInitDialogInit",
				width: 600,
				height: 400,
				closeEvent:function(){
					removeDialog("#revokeInitDialogInit");
					$("#revokeInitDialogInit").html("");
				}
			};
			cherryAjaxRequest({
				url: url,
				param: params,
				callback: function(data) {
					// 关闭弹出窗口
					closeCherryDialog('revokeInitDialogInit',WPCOM_dialogBody);
					openDialog(dialogSetting);
					$("#revokeInitDialogInit").html(data);
					BINOLWPSAL13.verificationType();
				}
			})
		},
		"showService": function(t){
			$("#serviceTr").remove();
			$(t).parents("tr").siblings().find("#showService").show();
			$(t).parents("tr").siblings().find("#hideService").hide();
			var tr = "<tr class='detail' id='serviceTr'><td class='detail box2-active' colspan='5'> <div class='detail_box'>"+$(t).siblings("#serviceDetailDiv").html()+"</div></td></tr>";
			$(t).parents("tr").after(tr);
			$(t).parent().find("#hideService").show();
			$(t).parent().find("#showService").hide();
		},
		"hideService": function(t){
			$("#serviceTr").remove();
			$(t).parent().find("#showService").show();
			$(t).parent().find("#hideService").hide();
		},
		// 验证方式下拉框
		"verificationType" : function(){
			var verificationType = $("#verificationType").val();
			if(verificationType=="2"){
				$("#verificationCode1").hide();
				$("#verificationCode2").show();
				$("#getVerificationCodeButton").show();
			}else if(verificationType=="1"){
				$("#verificationCode1").show();
				$("#verificationCode2").hide();
				$("#getVerificationCodeButton").hide();
			}
		},
		// 获取验证码
		"getVerificationCode" : function() {
			var cardCode = $("#dgCardCode").val();
			// 请求地址
			var getVerificationCodeUrl = $('#dgGetVerificationCode').attr("href");
			cherryAjaxRequest({
				url : getVerificationCodeUrl,
				param : "cardCode="+cardCode+"&usesType=6",
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
		"revoke": function(){
			var billCode = $("#revokeBillCode").val();
			var cardCode = $("#dgCardCode").val();
			var verificationCode = $("#verificationCode").val();
			var verificationType = $("#verificationType").val();
			if(cardCode!=undefined && cardCode!=null && cardCode!="" &&
					billCode!=undefined && billCode!=null && billCode!=""){
				var url = $("#revoke").attr("href");
				var params=getSerializeToken()+"&cardCode="+cardCode+"&billCode="+billCode+"&verificationCode="+verificationCode+"&verificationType="+verificationType;
				cherryAjaxRequest({
					url: url,
					param: params,
					callback: function(data) {
						// 关闭弹出窗口
						closeCherryDialog('revokeInitDialogInit',WPCOM_dialogBody);
						/*BINOLWPSAL02.showMessageDialog({
							message:"验证码短信发送失败", 
							type:"MESSAGE"
						});*/
						if("SUCCESS"==data){
							BINOLWPSAL13.transactionDetail();
							BINOLWPSAL02.showMessageDialog({
								message:"操作成功！", 
								type:"SUCCESS"
							});
						}else if("SBE0003"==data){
							BINOLWPSAL02.showMessageDialog({
								message:"操作类型为撤销，单据号为空！", 
								type:"MESSAGE"
							});
						}else if("SBE0004"==data){
							BINOLWPSAL02.showMessageDialog({
								message:"储值卡号为空！", 
								type:"MESSAGE"
							});
						}else if("SBE0016"==data){
							BINOLWPSAL02.showMessageDialog({
								message:"没有查询到有效的储值卡信息！ ", 
								type:"MESSAGE"
							});
						}else if("SBE0017"==data){
							BINOLWPSAL02.showMessageDialog({
								message:"储值卡不是激活状态！ ", 
								type:"MESSAGE"
							});
						}else if("SBE0018"==data){
							BINOLWPSAL02.showMessageDialog({
								message:"身份校验失败！ ", 
								type:"MESSAGE"
							});
						}else if("SBE0019"==data){
							BINOLWPSAL02.showMessageDialog({
								message:"需要撤销的充值单据不存在！ ", 
								type:"MESSAGE"
							});
						}else if("SBE0020"==data){
							BINOLWPSAL02.showMessageDialog({
								message:"传入单据不是充值单据！ ", 
								type:"MESSAGE"
							});
						}else if("SBE0021"==data){
							BINOLWPSAL02.showMessageDialog({
								message:"充值单据已撤销，不允许重复操作！ ", 
								type:"MESSAGE"
							});
						}else if("SBE0022"==data){
							BINOLWPSAL02.showMessageDialog({
								message:"余额不足！", 
								type:"MESSAGE"
							});
						}else {
							BINOLWPSAL02.showMessageDialog({
								message:"未定义错误！", 
								type:"MESSAGE"
							});
						}
						$("#revokeInitDialogInit").html("");
					}
				})
			}
		},
		"showDetailed": function(t){
			$("table:visible").find("#serviceDetailTable").parents("tr").hide();
			$(t).parents("tr").siblings().find("#showDetailed").show();
			$(t).parents("tr").siblings().find("#hideDetailed").hide();	
			var cardCode = $(t).parents("tr").find("#dgCardCode").text();
			$("#"+cardCode).show();
			$(t).parent().find("#hideDetailed").show();
			$(t).parent().find("#showDetailed").hide();
		},
		"hideDetailed": function(t){
			var cardCode = $(t).parents("tr").find("#dgCardCode").text();
			$("#"+cardCode).hide();
			$(t).parent().find("#hideDetailed").hide();
			$(t).parent().find("#showDetailed").show();
		},
		"keyupStartDate":function (obj){
			var inputValue = $(obj).val();
			var maxValue = $("#toDate").val();
			var minDate = BINOLWPSAL07.dateAdd('m', maxValue, -1);
			var minValue = changeDateToString(minDate);
			var inputDate = new Date(inputValue);
			var maxDate = new Date(maxValue);
			if(inputDate < minDate){
				$("#fromDate").val(minValue);
			}else if(inputDate > maxDate){
				$("#fromDate").val(maxValue);
			}
		},
		
		"keyupEndDate":function (obj){
			var inputValue = $(obj).val();
			var minValue = $("#fromDate").val();
			var maxDate = BINOLWPSAL07.dateAdd('m', minValue, 1);
			var maxValue = changeDateToString(maxDate);
			var inputDate = new Date(inputValue);
			var minDate = new Date(minValue);
			if(inputDate < minDate){
				$("#toDate").val(minValue);
			}else if(inputDate > maxDate){
				$("#toDate").val(maxValue);
			}
		},
		// 日期计算
		"dateAdd":function(strInterval, dateValue, Number) {
			var dtTmp = new Date(dateValue);
			switch (strInterval) {
				case 's': return new Date(Date.parse(dtTmp) + (1000 * Number));
				case 'n': return new Date(Date.parse(dtTmp) + (60000 * Number));
				case 'h': return new Date(Date.parse(dtTmp) + (3600000 * Number));
				case 'd': return new Date(Date.parse(dtTmp) + (86400000 * Number));
				case 'w': return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number));
				case 'q': return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number * 3, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds()); 
				case 'm': return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds()); 
				case 'y': return new Date((dtTmp.getFullYear() + Number),dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds()); 
			}
		}
	};




var BINOLWPSAL13 = new BINOLWPSAL13_GLOBAL();

$(document).ready(function(){
/*	cherryValidate({
		formId: "rechargeForm",		
		rules: {
			cardCode: {required: true}
			mobilePhone: {required: true},
			password: {required: true},
			ps2: {required: true}
	   }		
	});*/
	// 清空弹出框内的表对象
	oTableArr[30]=null;
	// 充值/开卡
	$("#dgCardCode").bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			var cardCode = $("#dgCardCode").val();
			if(cardCode!=""){
				BINOLWPSAL13.confirm();
			}else {
				BINOLWPSAL13.openCard();
			}
		}
	});
	BINOLWPSAL13.businessTypeChange();
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#dialogInit').html();
});


