var WPCOM_dialogBody ="";
var BINOLWPSAL07_GLOBAL = function() {

};

BINOLWPSAL07_GLOBAL.prototype = {
	
	"search":function(){
		if(!$('#searchBillsForm').valid()) {
			return false;
		}
		var $form = $('#searchBillsForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "5%", "bSortable": false},
		                    {"sName" : "billCode", "sWidth" : "20%"},
		                    {"sName" : "saleTime", "sWidth" : "15%"},
		                    {"sName" : "memberCode", "sWidth" : "15%"},
		                    {"sName" : "baName", "sWidth" : "10%"},
		                    {"sName" : "quantity", "sWidth" : "8%"},
		                    {"sName" : "payAmount", "sWidth" : "8%"},
		                    {"sName" : "act", "sWidth" : "15%", "bSortable": false}
		                    ];
		var url = $("#dgSearchUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		
		$("#searchBillsDiv").show();
		$("#returnHistoryBillsDiv").hide();
		// 表格设置
		var tableSetting = {
			// datatable 对象索引
			index : 3,
			// 表格ID
			tableId : "#searchBillsTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 1, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 1 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			// 默认显示行数
			iDisplayLength : 10,
			fnDrawCallback : function() {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	"showHistorySrBill":function(){
		if(!$('#searchBillsForm').valid()) {
			return false;
		}
		var $form = $('#searchBillsForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "5%", "bSortable": false},
		                    {"sName" : "billCode", "sWidth" : "20%"},
		                    {"sName" : "relevantCode", "sWidth" : "20%"},
		                    {"sName" : "saleTime", "sWidth" : "10%"},
		                    {"sName" : "memberCode", "sWidth" : "10%"},
		                    {"sName" : "baName", "sWidth" : "7%"},
		                    {"sName" : "quantity", "sWidth" : "8%"},
		                    {"sName" : "payAmount", "sWidth" : "10%"},
		                    {"sName" : "act", "sWidth" : "10%", "bSortable": false}
		                    ];
		var url = $("#dgReturnHistoryUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		
		$("#searchBillsDiv").hide();
		$("#returnHistoryBillsDiv").show();
		// 表格设置
		var tableSetting = {
			// datatable 对象索引
			index : 5,
			// 表格ID
			tableId : "#returnHistoryBillsTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 1, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 1 ],
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
	
	// 下载其它终端设备数据并根据条件查询单据
	"getAllBills":function(obj){
		if(!$('#searchBillsForm').valid()) {
			return false;
		}
		var $form = $('#searchBillsForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "5%", "bSortable": false},
		                    {"sName" : "billCode", "sWidth" : "20%"},
		                    {"sName" : "saleTime", "sWidth" : "15%"},
		                    {"sName" : "memberCode", "sWidth" : "15%"},
		                    {"sName" : "baName", "sWidth" : "10%"},
		                    {"sName" : "quantity", "sWidth" : "8%"},
		                    {"sName" : "payAmount", "sWidth" : "8%"},
		                    {"sName" : "act", "sWidth" : "15%", "bSortable": false}
		                    ];
		var url = $("#dgGetAllBillsUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		
		$("#searchBillsDiv").show();
		$("#returnHistoryBillsDiv").hide();
		// 表格设置
		var tableSetting = {
			// datatable 对象索引
			index : 3,
			// 表格ID
			tableId : "#searchBillsTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 1, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 1 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			// 默认显示行数
			iDisplayLength : 10,
			fnDrawCallback : function() {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	// 更改销售数量时触发的方法
	"changeSrQuantity":function(obj){
		var $this = $(obj);
		var $thisTr = $this.parent().parent();
		var $thisVal =$this.val().toString();
		var orgQuantity = $thisTr.find("#dgQuantity").val();
		if($thisVal == ""){
			$this.val("1");
		}else if(isNaN($thisVal)){
			$this.val("1");
		}else{
			var quantity = parseInt(Number($this.val()));
			if(quantity > 0 && quantity <= orgQuantity){
				if($thisVal.toString() != quantity.toString()){
					$this.val(quantity);
				}
			}else{
				$this.val("1");
			}
		}
	},
	
	"showMessageDialog":function (dialogSetting){
		if(dialogSetting.type == "MESSAGE"){
			$("#TuiHuo_messageSpan").text(dialogSetting.message);
			$("#TuiHuo_messageContent").show();
		}
		var $dialog = $('#TuiHuo_messageDiv');
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
				closeCherryDialog("TuiHuo_messageDiv");
			}
		});
		$dialog.dialog("open");
		// 给确认按钮绑定事件
		$("#TuiHuo_btnMessage").bind("click", function(){
			BINOLWPSAL07.messageConfirm();
		});
	},
	
	"messageConfirm":function (){
		closeCherryDialog("TuiHuo_messageDiv");
	},
	
	"returnsBillDialog":function(){

		// 是否可以退货判断
		var flag = true;
		// 云POS是否允许对未上传数据退货
		var POSTuihuo = $("#POSTuihuo").val();
		if(POSTuihuo.toString()!="Y"){
			if($('#getBillDetailbody >tr').length > 0){
				$.each($('#getBillDetailbody >tr'), function(i){
					var MQState = $(this).find("#dgMQState").val();
					if(Number(MQState)==1){
						flag = false;
					}
				})
			};
		}
		if(!flag){	
			BINOLWPSAL07.showMessageDialog({
				message:"数据未上传至后台，不允许退单！",
				type:"MESSAGE"
			});
			return;
		};

		// 获取退货金额
		var totalAmountVal = 0.00;
		var totalAmount = $("#dgCheckedTotalAmount").val();
		if(totalAmount != undefined && totalAmount != ""){
			totalAmountVal = Number(totalAmount).toFixed(2);
		}

		//是否允许会员退货后积分为负
		var isPermitMemPointNegative = $("#isPermitMemPointNegative").val();
		if(isPermitMemPointNegative == "N"){
			//退货后积分为负数，不允许退货
			var totalPoint = $("#totalPoint").val();//会员总积分
			var pointGet = $("#pointGet").val();//会员销售时所获得的积分
			var payAmount = $("#payAmount2").text();//销售实付金额

			if(payAmount != undefined && payAmount != "" && payAmount != 0){

				//所需扣减积分
				var toDelPoint = Number(totalAmountVal*pointGet/payAmount).toFixed(2);
				if( totalPoint - toDelPoint < 0 ){
					BINOLWPSAL07.showMessageDialog({
						message:"退货后积分为负数，不允许退货！",
						type:"MESSAGE"
					});
					return;
				}

			}

		}

		// 获取主页面原有销售状态
		var originalSaleType = $("#saleType").val();
		$("#originalSaleType").val(originalSaleType);
		$("#saleType").val("SR");

		
		var dialogSetting = {
			dialogInit: "#returnBill_dialog",
			width: 700,
			height: 600,
			zIndex: 10,
			title: $("#returnBillDialogTitle").text(),
			closeEvent:function(){
				var originalSaleType = $("#originalSaleType").val();
				// 还原主页原有销售状态
				$("#saleType").val(originalSaleType);
				// 可见文本框回车事件解绑
				$("#collectPageDiv").find("input:text:visible").unbind();
				// 关闭弹出窗口
				removeDialog("#returnBill_dialog");
			}
		};
		//openDialog(dialogSetting);
		
		var counterCode = $("#dgSrCounterCode").val();
		var baName = $("#dgCheckedBaName").val();
		var memberName = $("#dgCheckedMemberName").val();
		var billCode = $("#dgCheckedBillCode").val();
		// 获取收款页面地址
		var collectUrl = $("#collectUrl").attr("href");
		var params = "collectPageType=SRBL&saleType=SR&billCode=" + billCode + "&counterCode=" + counterCode + "&baName=" + baName 
				+ "&memberName=" + memberName + "&totalAmount=" +　totalAmountVal;
		cherryAjaxRequest({
			url: collectUrl,
			param: params,
			callback: function(data) {
				// 判断是否有支付方式
				var isCA = $("#isCA").val();
				var paymentSizeUrl = $("#paymentSizeUrl").attr("href");
				cherryAjaxRequest({
					url: paymentSizeUrl,
					param: null,
					callback: function(paymentSize) {
						if(Number(paymentSize)<1 && isCA=="Y"){
							$("#returnBill_dialog").hide();
							$("#returnBill_dialog").html(data);
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
						}else {
							openDialog(dialogSetting);
							$("#returnBill_dialog").html(data);
							// 微信退单时可输入金额
							if($("#WEPAY")!=undefined && $("#WEPAY").is(":visible")){
								$("#orgWechatPay").val($("#WEPAY").find("input").val());
								$("#WEPAY").find("input").removeAttr("readonly");
								if($("#cash")!=undefined && $("#cash").is(":visible")){
									$("#cash").removeAttr("readonly");
								}
							};
							// 支付宝退单时可输入金额
							if($("#PT")!=undefined && $("#PT").is(":visible")){
								$("#orgAliPay").val($("#PT").find("input").val());
								$("#PT").find("input").removeAttr("readonly");
								if($("#cash")!=undefined && $("#cash").is(":visible")){
									$("#cash").removeAttr("readonly");
								}
							};
							if($("#new_Czk_Pay").val()=="Y"){
								$("#cashCardSrInfo").show();
							}
							if($("#serviceTb tr").length>0){
								$("#serverList").removeClass("hide");
								$("#choice1").hide();
								$("#choice2").show();
								$("#serviceTb").find("input").attr("readonly","readonly");
								$("#rListShow").attr("disabled","disabled");
							}else {
								$("#serverPay").hide();
							}
						}
					}
				});
			}
		});
	},
	
	"returnsGoods":function(){
		// 将退货明细列表转换成Json字符串
		BINOLWPSAL07.getBillSrDetailStr();
		// 获取主页面原有销售状态
		var originalSaleType = $("#saleType").val();
		$("#originalSaleType").val(originalSaleType);
		$("#saleType").val("SR");
		// 计算退货金额
		var totalAmount = 0.00;
		// 是否可以退货判断
		var flag = true
		// 云POS是否允许对未上传数据退货
		var POSTuihuo = $("#POSTuihuo").val();
		if($("[name='dgActivityTypeCode']").length>0){
			var b=false;
			$($("[name='dgActivityTypeCode']")).each(function(){
				var dgActivityTypeCode = $(this).val();
				if(dgActivityTypeCode == "ZDZK" || dgActivityTypeCode == "ZDQL"){
					BINOLWPSAL07.showMessageDialog({
						message:"整单折扣或整单去零后不支持退货！",
						type:"MESSAGE"
					});
					b=true;
				}
			});
			if(b){
				return false;
			}
		}
		if($('#getBillDetailbody >tr').length > 0){
			$.each($('#getBillDetailbody >tr'), function(i){
				var MQState = $(this).find("#dgMQState").val();
				if(Number(MQState)==1 && POSTuihuo.toString()!="Y"){
					flag = false;
				}else {
					var returnsAmount = 0.00;
					// 退货数量
					var $inputVal = $(this).find("#returnsQuantity").val();
					if(isNaN($inputVal) || $inputVal == ""){
						$inputVal = 0;
					}
					// 折扣后价格
					var $priceVal = $(this).find("#dgRealPrice").val();
					if(isNaN($priceVal) || $priceVal == ""){
						$priceVal = 0.00;
					}
					var activityTypeCode = $(this).find("#dgActivityTypeCode").val();
					if(activityTypeCode == "ZDZK" || activityTypeCode == "ZDQL" || activityTypeCode == "DXJE"){
						var zkAmount = $(this).find("#dgAmount").val();
						returnsAmount = Number(zkAmount);
					}else{
						// 单条记录退货金额
						returnsAmount = Number($inputVal) * Number($priceVal);
					}
					// 计算退货总金额
					totalAmount += returnsAmount;
				}
			});
		}
		if(!flag){
			BINOLWPSAL07.showMessageDialog({
				message:"数据未上传至后台，不允许退货！",
				type:"MESSAGE"
			});
			return;
		}

		//是否允许会员退货后积分为负
		var isPermitMemPointNegative = $("#isPermitMemPointNegative").val();
		if(isPermitMemPointNegative == "N"){

			//退货后积分为负数，不允许退货
			var totalPoint = $("#totalPoint").val();//会员总积分
			var pointGet = $("#pointGet").val();//会员销售时所获得的积分
			var payAmount = $("#payAmount2").text();//销售实付金额

			if(payAmount != undefined && payAmount != "" && payAmount != 0){

				//所需扣减积分
				var toDelPoint = Number(totalAmount*pointGet/payAmount).toFixed(2);
				if( totalPoint - toDelPoint < 0 ){
					BINOLWPSAL07.showMessageDialog({
						message:"退货后积分为负数，不允许退货！",
						type:"MESSAGE"
					});
					return;
				}

			}

		}

		// 计算应退服务次数
		var serviceList = new Array();
		if($("#getServiceBillDetailbody >tr").length>0){
			$.each($("#getServiceBillDetailbody >tr"),function(){
				// 退货数量
				var quantity = $(this).find("#returnsQuantity").val();
				if(isNaN(quantity) || quantity == ""){
					quantity = 0;
				}
				var dgServiceType = $(this).find("#dgServiceType").val();
				if(dgServiceType!=undefined && dgServiceType!="" && dgServiceType!=null){
					var allQuantity = $(this).find("#dgQuantity").val();
					var obj = {"ServiceType":dgServiceType,"Quantity":quantity,"allQuantity":allQuantity};
					serviceList.push(obj);
				}
			})
		}
		
		totalAmount = totalAmount.toFixed(2);
		
		var dialogSetting = {
			dialogInit: "#returnBill_dialog",
			width: 700,
			height: 600,
			zIndex: 10,
			title: $("#returnBillDialogTitle").text(),
			closeEvent:function(){
				var originalSaleType = $("#originalSaleType").val();
				// 还原主页原有销售状态
				$("#saleType").val(originalSaleType);
				// 可见文本框回车事件解绑
				$("#collectPageDiv").find("input:text:visible").unbind();
				// 关闭弹出窗口
				removeDialog("#returnBill_dialog");
			}
		};
		//openDialog(dialogSetting);
		
		var counterCode = $("#dgSrCounterCode").val();
		var baName = $("#dgCheckedBaName").val();
		var memberName = $("#dgCheckedMemberName").val();
		var billCode = $("#dgCheckedBillCode").val();
		// 获取收款页面地址
		var collectUrl = $("#collectUrl").attr("href");
		var params = "collectPageType=SRDT&saleType=SR&billCode=" + billCode + "&counterCode=" + counterCode + "&baName=" + baName 
				+ "&memberName=" + memberName + "&totalAmount=" +　totalAmount;
		cherryAjaxRequest({
			url: collectUrl,
			param: params,
			callback: function(data) {
				// 判断是否有支付方式
				var isCA = $("#isCA").val();
				var paymentSizeUrl = $("#paymentSizeUrl").attr("href");
				cherryAjaxRequest({
					url: paymentSizeUrl,
					param: null,
					callback: function(paymentSize) {
						if(Number(paymentSize)<1 && isCA=="Y"){
							$("#returnBill_dialog").hide();
							$("#returnBill_dialog").html(data);
							var originalSaleType = $("#originalSaleType").val();
							// 退单
							BINOLWPSAL03.returnsGoodsCollect();
							// 还原主页原有销售状态
							$("#saleType").val(originalSaleType);
							// 可见文本框回车事件解绑
							$("#collectPageDiv").find("input:text:visible").unbind();
							// 关闭弹出窗口
							closeCherryDialog('returnBill_dialog',WPCOM_dialogBody);
							// 清空弹出框内容
							$('#returnBill_dialog').html("");
						}else {
							openDialog(dialogSetting);
							$("#returnBill_dialog").html(data);
							if($("#serviceTb tr").length>0){
								$("#serverPay").show();
								$("#choice2").show();
								$("#choice1").hide();
								$("#serverList").removeClass("hide");
							}else {
								$("#serverPay").hide();
							}
						}
					}
				});
			}
		});
	},
	
	"getBillDetail":function(obj){
		var $this = $(obj);
		var $thisTd = $this.parent();
		var billCode = $thisTd.find("#dgBillCode").val();
		// 隐藏单据页面，显示明细页面
		$("#getBillsPageDiv").hide();
		$("#getBillDetailPageDiv").show();
		// 显示补打小票按钮，隐藏退货和退单按钮
		$("#btnPrintBill").show();
		// 取得当前销售单ID
		$("#print_param_hide").find("#billId").val($thisTd.find("#saleIdForPrint").val());
		// 隐藏退货和退单按钮
		$("#btnReturnsBill").hide();
		$("#btnReturnGoods").hide();
		// 显示数量列
		$("#dgBillState").show();
		$("#dgOrgQuantity").show();
		// 隐藏退货数量和操作列
		$("#dgRowNumber").hide();
		$("#dgAllowQuantity").hide();
		$("#dgReturnsQuantity").hide();
		$("#dgReturnsAct").hide();
		
		var getBillDetailUrl = $("#dgGetBillDetailUrl").attr("href");
		var params = "billDetailShowType=DETAIL&billCode=" + billCode + "&" + getSerializeToken();
		cherryAjaxRequest({
			url:getBillDetailUrl,
			param:params,
			callback:function(data) {
				$("#getBillDetailbody").html(data);
				if($thisTd.find("#quantity").val()==0){
					$("#btnPrintBill").hide();
				}
				$("#status").show();
				$("#allQuantity").hide();
				BINOLWPSAL07.getServiceBillDetail(params);
			}
		});
	},
	"getServiceBillDetail":function(params){
		if($("#getBillDetailbody >tr").length<=0){
			$("#getBillDetailTable").hide();
		}else {
			$("#getBillDetailTable").show();
		}
		var gettServiceBillDetailUrl = $("#dgGetServiceBillDetailUrl").attr("href");
		cherryAjaxRequest({
			url:gettServiceBillDetailUrl,
			param:params,
			callback:function(data) {
				$("#getServiceBillDetailbody").html(data);
				if($("#getServiceBillDetailbody >tr").length<=0){
//					$("#getServiceBillDetailTable").hide();
					$("#serviceDiv").hide();
				}else {
//					$("#getServiceBillDetailTable").show();
					$("#serviceDiv").show();
				}
//				$("#printQuantity").hide();
//				$("#dgServiceReturnsAct").hide();
				BINOLWPSAL07.getPayTypeBillDetail(params);
			}
		});
	},
	"getPayTypeBillDetail":function(params){
		var getPayTypeBillDetailUrl = $("#dgGetPayTypeBillDetailUrl").attr("href");
		cherryAjaxRequest({
			url:getPayTypeBillDetailUrl,
			param:params,
			callback:function(data) {
				$("#getPayTypeDetailbody").html(data);
				if($("#getPayTypeDetailbody >tr").length<=0){
//					$("#getPayTypeDetailDiv").hide();
					$("#payTypeDiv").hide();
				}else {
//					$("#getPayTypeDetailDiv").show();
					$("#payTypeDiv").show();
				}
			}
		});
	},
	"getSrBillDetail":function(obj){
		var $this = $(obj);
		var $thisTd = $this.parent();
		var billCode = $thisTd.find("#dgBillCode").val();
		// 隐藏单据页面，显示明细页面
		$("#getBillsPageDiv").hide();
		$("#getBillDetailPageDiv").show();
		// 隐藏补打小票、退货和退单按钮
		$("#btnPrintBill").hide();
		// 清空用于打印的销售单ID
		$("#print_param_hide billId").val("");
		$("#btnReturnsBill").hide();
		$("#btnReturnGoods").hide();
		// 显示数量列
		$("#dgBillState").show();
		$("#dgOrgQuantity").show();
		// 隐藏退货数量和操作列
		$("#dgRowNumber").hide();
		$("#dgAllowQuantity").hide();
		$("#dgReturnsQuantity").hide();
		$("#dgReturnsAct").hide();
		
		var getBillDetailUrl = $("#dgGetSrBillDetailUrl").attr("href");
		var params = "billDetailShowType=DETAIL&billCode=" + billCode + "&" + getSerializeToken();
		cherryAjaxRequest({
			url:getBillDetailUrl,
			param:params,
			callback:function(data) {
				$("#getBillDetailbody").html(data);
				if($("#getBillDetailbody >tr").length<=0){
					$("#getBillDetailTable").hide();
				}else {
					$("#getBillDetailTable").show();
				}
				var dgRelevantCode = $thisTd.find("#dgRelevantCode").val();
				var pm = "billDetailShowType=DETAIL&billCode=" + dgRelevantCode + "&" + getSerializeToken();
				BINOLWPSAL07.getServiceBillDetail(pm);
			}
		});
	},
	
	"returnsBill":function(obj){
		var $this = $(obj);
		var $thisTd = $this.parent();
		var billCode = $thisTd.find("#dgBillCode").val();
		var baCode = $thisTd.find("#dgBaCode").val();
		var baName = $thisTd.find("#dgBaName").val();
		var memberCode = $thisTd.find("#dgMemberCode").val();
		var memberName = $thisTd.find("#dgMemberName").val();
		var memberLevel = $thisTd.find("#dgMemberLevel").val();
		var customerType = $thisTd.find("#dgCustomerType").val();
		var saleType = $thisTd.find("#dgSaleType").val();
		var businessDate = $thisTd.find("#dgBusinessDate").val();
		var businessTime = $thisTd.find("#dgBusinessTime").val();
		var totalQuantity = $thisTd.find("#dgTotalQuantity").val();
		var totalAmount = $thisTd.find("#dgTotalAmount").val();
		var totalOriginalAmount = $thisTd.find("#dgTotalOriginalAmount").val();
		var roundingAmount = $thisTd.find("#dgRoundingAmount").val();
		var costPoint = $thisTd.find("#dgCostPoint").val();
		var costPointAmount = $thisTd.find("#dgCostPointAmount").val();
		// 隐藏单据页面，显示明细页面
		$("#getBillsPageDiv").hide();
		$("#getBillDetailPageDiv").show();
		// 隐藏补打小票，显示退货和退单按钮
		$("#btnPrintBill").hide();
		// 清空用于打印的销售单ID
		$("#print_param_hide billId").val("");
		if(saleType=="PN"){
			// 显示退单按钮，隐藏退货按钮
			$("#btnReturnsBill").show();
			$("#btnReturnGoods").hide();
		}else{
			// 显示退货和退单按钮
			$("#btnReturnsBill").show();
			$("#btnReturnGoods").show();
		}
		// 隐藏数量列
		$("#dgBillState").hide();
		$("#dgOrgQuantity").hide();
		// 显示退货数量和操作列
		$("#dgRowNumber").show();
		$("#dgAllowQuantity").show();
		$("#dgReturnsQuantity").show();
		$("#dgReturnsAct").show();
		// 清空原有的选中单据的属性值
		$("#dgCheckedBillCode").val("");
		$("#dgCheckedBaCode").val("");
		$("#dgCheckedBaName").val("");
		$("#dgCheckedMemberCode").val("");
		$("#dgCheckedMemberName").val("");
		$("#dgCheckedMemberLevel").val("");
		$("#dgCheckedCustomerType").val("");
		$("#dgCheckedSaleType").val("");
		$("#dgCheckedBusinessDate").val("");
		$("#dgCheckedBusinessTime").val("");
		$("#dgCheckedTotalQuantity").val("");
		$("#dgCheckedTotalAmount").val("");
		$("#dgCheckedTotalOriginalAmount").val("");
		$("#dgCheckedRoundingAmount").val("");
		$("#dgCheckedCostPoint").val("");
		$("#dgCheckedCostPointAmount").val("");
		// 设置选中单据的属性
		$("#dgCheckedBillCode").val(billCode);
		$("#dgCheckedBaCode").val(baCode);
		$("#dgCheckedBaName").val(baName);
		$("#dgCheckedMemberCode").val(memberCode);
		$("#dgCheckedMemberName").val(memberName);
		$("#dgCheckedMemberLevel").val(memberLevel);
		$("#dgCheckedCustomerType").val(customerType);
		$("#dgCheckedSaleType").val(saleType);
		$("#dgCheckedBusinessDate").val(businessDate);
		$("#dgCheckedBusinessTime").val(businessTime);
		$("#dgCheckedTotalQuantity").val(totalQuantity);
		$("#dgCheckedTotalAmount").val(totalAmount);
		$("#dgCheckedTotalOriginalAmount").val(totalOriginalAmount);
		$("#dgCheckedRoundingAmount").val(roundingAmount);
		$("#dgCheckedCostPoint").val(costPoint);
		$("#dgCheckedCostPointAmount").val(costPointAmount);
		
		var getBillDetailUrl = $("#dgGetBillDetailUrl").attr("href");
		var params = "billCode=" + billCode + "&" + getSerializeToken();
		cherryAjaxRequest({
			url:getBillDetailUrl,
			param:params,
			callback:function(data) {
				$("#getBillDetailbody").html(data);
				if($("#getBillDetailbody >tr").length<=0){
					$("#getBillDetailTable").hide();
				}else {
					$("#getBillDetailTable").show();
				}
				// 初始化日历控件
				$("#businessReturnDate").cherryDate({
					onSelectEvent: function(input){
						// 最后一行第一个可见的文本框获得焦点
						BINOLWPSAL02.firstInputSelect();
					},
					beforeShow: function(input){
						var minValue = $("#returnbussinessDateStart").val();
						var maxValue = $("#returnbussinessDateEnd").val();
						return [minValue, maxValue];
					}
				});
				var gettServiceBillDetailUrl = $("#dgGetServiceBillDetailUrl").attr("href");
				cherryAjaxRequest({
					url:gettServiceBillDetailUrl,
					param:params,
					callback:function(data) {
						$("#getServiceBillDetailbody").html(data);
						$("#status").hide();
						$("#allQuantity").show();
						BINOLWPSAL07.getServiceBillDetail(params);
//						$("#printQuantity").show();
//						$("#dgServiceReturnsAct").show();
					}
				});
			}
		});
	},
	
	"deleteRow":function(obj){
		var $this = $(obj);
		var $thisTr = $this.parent().parent().parent();
		var activityCode = $thisTr.find("#dgActivityCode").val();
		var recType = $thisTr.find("#dgActivityTypeCode").val();
		// 移除选中行
		if(recType == "CXHD"){
			BINOLWPSAL07.deletePromotionRow(activityCode);
		}else{
			$thisTr.remove();
		}
		// 判断删除的是否为最后一行数据，若为最后一行数据则返回，若不是最后一行数据则更新序号
		if($('#getBillDetailbody >tr').length <= 0){
			$("#getBillDetailTable").hide();
			//BINOLWPSAL07.back();
		}else{
			// 更新序号
			var rows = $("#getBillDetailbody").children();
			if(rows.length > 0){
				rows.each(function(i){
					$(this).find("#rowNo").text(i+1);
				});
			}
		}
		
		if($('#getServiceBillDetailbody >tr').length <= 0){
			$("#serviceDiv").hide();
		}
		if($('#getBillDetailbody >tr').length <= 0 && $('#getServiceBillDetailbody >tr').length <= 0){
			BINOLWPSAL07.back();
		}
	},
	
	// 删除指定促销记录行
	"deletePromotionRow":function(activityCode){
		if($('#getBillDetailbody >tr').length > 0){
			$.each($('#getBillDetailbody >tr'), function(i){
				// 存在促销品的情况下如果删除促销品则删除全部产品，不允许只留下促销品不退货
				$(this).remove();
//				if($(this).find("#dgActivityCode").val() == activityCode){
//					$(this).remove();
//				}else if($(this).find("#dgActivityTypeCode").val() == "DXJE"){
//					$(this).remove();
//				}
			});
		}
	},
	
	// 将退货明细转换成Json格式
	"getBillSrDetailStr":function(){
		var saleresult = "";
		var $msgBoxs = $("#getBillDetailbody");
		// 将明细列表记录转换成Json格式
		saleresult = Obj2JSONArr($msgBoxs.find("tr"));
		$("#billSrDetailStr").val(saleresult);
	},
	
	"printBill":function(){
		// 补打小票，参数0：销售，1：补打小票
		printWebPosSaleBill("1");
	},
	
	"back":function(){
		$("#getBillsPageDiv").show();
		$("#getBillDetailPageDiv").hide();
	},
	
	"cancel":function(){
		// 还原按钮样式
		$("#btnSearchBills").attr("class","btn_top");
		
		closeCherryDialog('dialogInit',WPCOM_dialogBody);
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
			// 第一个可见的文本框获得焦点
			$('#databody >tr').find("input:text:visible:first").select();
		}
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
	},
	
	"keyupStartDate":function (obj){
		var inputValue = $(obj).val();
		var maxValue = $("#dgSaleDateEnd").val();
		var minDate = BINOLWPSAL07.dateAdd('m', maxValue, -1);
		var minValue = changeDateToString(minDate);
		var inputDate = new Date(inputValue);
		var maxDate = new Date(maxValue);
		if(inputDate < minDate){
			$("#dgSaleDateStart").val(minValue);
		}else if(inputDate > maxDate){
			$("#dgSaleDateStart").val(maxValue);
		}
	},
	
	"keyupEndDate":function (obj){
		var inputValue = $(obj).val();
		var minValue = $("#dgSaleDateStart").val();
		var maxDate = BINOLWPSAL07.dateAdd('m', minValue, 1);
		var maxValue = changeDateToString(maxDate);
		var inputDate = new Date(inputValue);
		var minDate = new Date(minValue);
		if(inputDate < minDate){
			$("#dgSaleDateEnd").val(minValue);
		}else if(inputDate > maxDate){
			$("#dgSaleDateEnd").val(maxValue);
		}
	},
	"keyupBusinessDate":function (obj){
		var inputValue = $(obj).val();
		var minValue = $("#businessBeginDate").val();
		var maxValue = $("#nowDate").val();
		var inputDate = new Date(inputValue);
		var minDate = new Date(minValue);
		var maxDate = new Date(maxValue);
		if(inputDate < minDate){
			$("#businessReturnDate").val(minValue);
		}else if(inputDate > maxDate){
			$("#businessReturnDate").val(maxValue);
		}
	}
};

var BINOLWPSAL07 = new BINOLWPSAL07_GLOBAL();

$(document).ready(function(){
	// 表单验证初期化
	cherryValidate({
		formId: 'searchBillsForm',
		rules: {
			dgSaleDateStart: {dateValid: true},
			dgSaleDateEnd: {dateValid: true}
		}
	});
	$("#dgSaleDateStart").cherryDate({
		beforeShow: function(input){
			var value = $("#dgSaleDateEnd").val();
			var minDate = BINOLWPSAL07.dateAdd('m', value, -1);
			return [changeDateToString(minDate), value];
		},
		onSelectEvent: function(input){
			// 查询按钮获得焦点
			$("#btnMemberSearch").focus();
		}
	});
	$("#dgSaleDateEnd").cherryDate({
		beforeShow: function(input){
			var value = $("#dgSaleDateStart").val();
			var maxDate = BINOLWPSAL07.dateAdd('m', value, 1);
			return [value, changeDateToString(maxDate)];
		},
		onSelectEvent: function(input){
			// 查询按钮获得焦点
			$("#btnMemberSearch").focus();
		}
	});
	// 清空弹出框内的表对象
	oTableArr[3]=null;
	oTableArr[5]=null;
	// 调用查询单据函数
	BINOLWPSAL07.search();
	// 绑定回车键
	$("#getBillsPageDiv").find("input:text:visible")
	.bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			BINOLWPSAL07.search();
		}
	});
	// 挂单页面确定按钮获得焦点
	$("#btnMemberSearch").focus();
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#dialogInit').html();
});


