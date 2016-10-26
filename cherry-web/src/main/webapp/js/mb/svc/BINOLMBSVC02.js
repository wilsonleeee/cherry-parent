
function BINOLMBSVC02() {
	
};

BINOLMBSVC02.prototype = {
		"stopCardDialog":function (cardId){
			var dialogId = "stop_card_dialog";
			var $dialog = $('#' + dialogId);
			$dialog.dialog({ 
				//默认不打开弹出框
				autoOpen: false,  
				//弹出框宽度
				width: 350, 
				//弹出框高度
				height: 250, 
				//弹出框标题
				title:$("#dialogTitle").text(), 
				//弹出框索引
				zIndex: 1,  
				modal: true, 
				resizable:false,
				//弹出框按钮
				buttons: [{
					text:$("#dialogConfirm").text(),//确认按钮
					click: function() {
						//点击确认后执行活动停用
						BINOLMBSVC02.stopCardPlan(cardId);
						$dialog.dialog("close");
					}
				},
				{	
					text:$("#dialogCancel").text(),//取消按钮
					click: function() {
						$dialog.dialog("close");
					}
				}],
				//关闭按钮
				close: function() {
					closeCherryDialog(dialogId);
				}
			});
			$dialog.dialog("open");
		},
		"stopCardPlan":function (cardId){
			var url = $('#stopCardUrl').attr("href");
			var param = "cardId="+cardId;
			cherryAjaxRequest({
				url:url,
				param:param,
				callback: function(data) {
					$("#stop_card_dialog").dialog("close");
					//刷新表格数据
					if(oTableArr[0] != null)
						oTableArr[0].fnDraw();
				}
			});
		},
		"cardRechargeInit":function(){
			var cardRechargeInitUrl=$('#cardRechargeInitUrl').attr('href');
			var params=$('#cardForm').serialize();
			var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 500,
					height: 300,
					title: "储值卡充值",
					closeEvent:function(){
						removeDialog("#dialogInit");
					}
				};
				openDialog(dialogSetting);
				cherryAjaxRequest({
					url: cardRechargeInitUrl,
					param:params,
					callback: function(data) {
						$("#dialogInit").html(data);
					}
				});
		},
		"searchList":function(){
			//清除提示信息
			$('#actionResultDisplay').empty();
			var url = $("#cardSearchUrl").attr("href");
			var params = $("#cardForm").serialize();
			if (params != null && params != "") {
				url = url + "?" + params;
			}
			$("#resultList").show();
			// 表格设置
			var tableSetting = {
				// datatable 对象索引
				index : 1,
				// 表格ID
				tableId : '#resultCardDataTable',
				// 数据URL
				url : url,
				// 表格默认排序
				aaSorting : [ [ 1, "asc" ] ],
				// 表格列属性设置
				aoColumns : [ {
					"sName" : "number",
					"sWidth" : "5%",
					"bSortable" : false
				}, {
					"sName" : "cardCode",
					"sWidth" : "10%",
					"bSortable" : true
				}, {
					"sName" : "mobilePhone",
					"sWidth" : "15%",
					"bSortable" : true
				}, {
					"sName" : "amount",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "depositAmount",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "totalAmount",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "lastDepositAmount",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "lastDepositTime",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "state",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "departName",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "cardType",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "operator",
					"sWidth" : "15%",
					"bSortable" : true
				}],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback : function() {

				},
				callbackFun : function(msg) {
					var $msg = $("<div></div>").html(msg);
					var $headInfo = $("#headInfo", $msg);
					if ($headInfo.length > 0) {
						$("#headInfo").html($headInfo.html());
					} else {
						$("#headInfo").empty();
					}
				}
			};
			// 调用获取表格函数
			getTable(tableSetting);
		},
		"openCardInit":function(){
			var openCardInitUrl=$('#openCardInitUrl').attr('href');
			var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 800,
					height: 500,
					title: "储值卡开卡",
					closeEvent:function(){
						removeDialog("#dialogInit");
					}
				};
				openDialog(dialogSetting);
				cherryAjaxRequest({
					url: openCardInitUrl,
					callback: function(data) {
						$("#dialogInit").html(data);
					}
				});
		},
		"initSaleDetail":function(url){
			var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 800,
					height: 500,
					title: "查看明细",
					closeEvent:function(){
						removeDialog("#dialogInit");
					}
				};
				openDialog(dialogSetting);
				cherryAjaxRequest({
					url: url,
					callback: function(data) {
						$("#dialogInit").html(data);
					}
				});
		},
		"resetPsdDialog":function(_this){
			BINOLMBSVC02.messageClean();
			var $that = $(_this);
			var mobilePhone = $that.parent().find("#mobilePhoneC").val();
			var cardCode = $that.parent().find("#cardCodeC").val();
			var departCode = $that.parent().find("#departCodeC").val();
			var dialogId = "reset_psd_dialog";
			var $dialog = $('#' + dialogId);
			$dialog.dialog({
				//默认不打开弹出框
				autoOpen: false,  
				//弹出框宽度
				width: 350, 
				//弹出框高度
				height: 250, 
				//弹出框标题
				title:$("#dialogResetPSDTitle").text(), 
				//弹出框索引
				zIndex: 2,  
				modal: true,
				resizable:false,
				//弹出框按钮
				buttons: [{
					text:$("#dialogConfirm").text(),//确认按钮
					click: function() {
						//点击确认后执行短信验证
						$dialog.dialog("close");
						BINOLMBSVC02.resetCheck(cardCode,mobilePhone,departCode);
					}
				},
				{	
					text:$("#dialogCancel").text(),//取消按钮
					click: function() {
						$dialog.dialog("close");
					}
				}],
				//关闭按钮
				close: function() {
					closeCherryDialog(dialogId);
				}
			});
			$dialog.dialog("open");
		},
		"resetCheck":function(cardCode,mobilePhone,departCode){
			var dialogId = 'mobileCheckDialog';
			var $dialog = $("#" + dialogId);
			if($dialog.length == 0) {
				$("body").append('<div style="display:none" id="'+dialogId+'"></div>');
			} else {
				$dialog.empty();
			}
			var param = "mobilePhone="+mobilePhone+"&cardCode="+cardCode+"&counterCode="+departCode;
			cherryAjaxRequest({
				url:$("#sendMessageInitUrl").attr("href"),
				param:param,
				callback:function(msg){
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
							$("#errorMessageDiv span").empty();
							$("#errorMessageDiv").addClass("hide");
							//确认按钮
							var verificationCode = $("#mobileCheckDialog  #verificationCode").val();
							param += "&verificationCode="+verificationCode;
							
							cherryAjaxRequest({
								url:"/Cherry/mb/BINOLMBSVC02_messageConfirm",
								param:param,
								callback:function(date){
									var rusult_map = eval("("+date+")");
									var resultCode=rusult_map.ERRORCODE;
									var resultMessage=rusult_map.ERRORMSG;
									if(resultCode==0){
										//修改成功
										//移除验证窗口
										removeDialog("#" + dialogId);
										BINOLMBSVC02.successShow("重置密码操作成功！");
									}else{
										$("#errorMessageDiv span").text(resultMessage);
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
			});
		},
		"abandonCard":function (cardId){
			BINOLMBSVC02.messageClean();
			var dialogId = "abandon_card_dialog";
			var $dialog = $('#' + dialogId);
			$dialog.dialog({ 
				//默认不打开弹出框
				autoOpen: false,  
				//弹出框宽度
				width: 350, 
				//弹出框高度
				height: 250, 
				//弹出框标题
				title:$("#dialogAbandonTitle").text(), 
				//弹出框索引
				zIndex: 1,  
				modal: true, 
				resizable:false,
				//弹出框按钮
				buttons: [{
					text:$("#dialogConfirm").text(),//确认按钮
					click: function() {
						//点击确认后执行储值卡废弃
						BINOLMBSVC02.abandonCardPlan(cardId);
						$dialog.dialog("close");
					}
				},
				{	
					text:$("#dialogCancel").text(),//取消按钮
					click: function() {
						$dialog.dialog("close");
					}
				}],
				//关闭按钮
				close: function() {
					closeCherryDialog(dialogId);
				}
			});
			$dialog.dialog("open");
		},
		"abandonCardPlan":function (cardId){
			var url = $('#abandonCardUrl').attr("href");
			var param = "cardId="+cardId;
			cherryAjaxRequest({
				url:url,
				param:param,
				callback: function(data) {
					$("#abandon_card_dialog").dialog("close");
					//刷新表格数据
					if(oTableArr[1] != null){
						oTableArr[1].fnDraw();
					}
						
				}
			});
		},
		//修改密码初始化选择修改方式
		"modifyPasswordInit":function(_this){
			BINOLMBSVC02.messageClean();
			var $that = $(_this);
			var dialogId = "modify_card_dialog";
			var $dialog = $('#' + dialogId);
			var mobilePhone = $that.parent().find("#mobilePhoneC").val();
			var cardCode = $that.parent().find("#cardCodeC").val();
			var departCode = $that.parent().find("#departCodeC").val();
			$dialog.dialog({ 
				//默认不打开弹出框
				autoOpen: false,  
				//弹出框宽度
				width: 350, 
				//弹出框高度
				height: 250, 
				//弹出框标题
				title:$("#dialogModifyTitle").text(), 
				//弹出框索引
				zIndex: 2,  
				modal: true, 
				resizable:false,
				//弹出框按钮
				buttons: [{
					text:$("#byOldPSD").text(),//密码修改
					click: function() {
						//URL
						var url = $("#oldPasswordInitUrl").attr("href");
						//PARAM
						var param = "cardCode="+cardCode+"&counterCode="+departCode;
						$dialog.dialog("close");
						BINOLMBSVC02.modifyPassword(cardCode, url, param);
					}
				},
				{	
					text:$("#byVerificationCode").text(),//短信验证
					click: function() {
						//URL
						var url = $("#sendMessageInitUrl").attr("href");
						//PARAM
						var param = "mobilePhone="+mobilePhone+"&cardCode="+cardCode+"&counterCode="+departCode;
						$dialog.dialog("close");
						BINOLMBSVC02.modifyPassword(cardCode, url, param);
					}
				},
				{	
					text:$("#dialogCancel").text(),//取消按钮
					click: function() {
						$dialog.dialog("close");
					}
				}],
				//关闭按钮
				close: function() {
					closeCherryDialog(dialogId);
				}
			});
			$dialog.dialog("open");
			
		},
		//修改密码
		"modifyPassword":function(cardCode,urlInit,paramInit){
			var dialogId = 'byVerificationCodeDialog';
			var $dialog = $("#" + dialogId);
			if($dialog.length == 0) {
				$("body").append('<div style="display:none" id="'+dialogId+'"></div>');
			} else {
				$dialog.empty();
			}
			cherryAjaxRequest({
				url:urlInit,
				param:paramInit,
				callback:function(msg){
					var htmlMsg='<span style="font-size:15px;">新密码</span>&nbsp;&nbsp;&nbsp;&nbsp;'
						+'<input style="display:none"/>'
						+'<input type="password" id="newPassword" name="newPassword" value="" autocomplete="off"/>';
					// 弹出验证框
					var dialogSetting = {
						dialogInit: "#" + dialogId,
						text: msg,
						width: 	400,
						height: 310,
						
						title: 	'修改储值卡密码',
						confirm: '确认',
						cancel : '返回',
						confirmEvent: function(){
							$("#errorMessageDiv span").empty();
							$("#errorMessageDiv").addClass("hide");
							//确认按钮
							var param = $("#modifyForm").serialize();
							cherryAjaxRequest({
								url:"/Cherry/mb/BINOLMBSVC02_passwordConfirm",
								param:param,
								callback:function(date){
									if(date==0){
										//修改成功
										//移除验证窗口
										removeDialog("#" + dialogId);
										BINOLMBSVC02.successShow("修改密码操作成功！");
									}else{
										$("#errorMessageDiv span").text(date);
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
					$("#modifyForm").append(htmlMsg);
				}
			});
		},
		"successShow":function(message){
			$("#successSpan").text(message);
			$("#successDiv").show()
		},
		"messageClean":function(){
			$("#successSpan").empty();
			$("#successDiv").hide()
		}
};

var BINOLMBSVC02 =  new BINOLMBSVC02();

$(document).ready(function() {
	$("#searchButton").click(function() {
		BINOLMBSVC02.searchList();
		return false;
	});


	
	$("#cardExportExcel").click(function(){
		if($("#resultDataTable").find(".dataTables_empty:visible").length==0) {
			var callback = function(msg) {
        		var url = $("#cardExportExcelUrl").attr("href");
        		$("#cardForm").attr("action",url);
        		$("#cardForm").submit();
        	};
        	exportExcelReport({
        		url: $("#cardExportCheckUrl").attr("href"),
        		param: $("#cardForm").serialize(),
        		callback: callback
        	});
        }
		return false;
	});
	
	$("#cardExportCsv").click(function(){
		if($("#resultDataTable").find(".dataTables_empty:visible").length==0) {
			exportReport({
				exportUrl:$("#cardExportCsvUrl").attr("href"),
				exportParam:$("#cardForm").serialize()
			});
		}
		return false;
	});
	
	$("#saleExportExcel").click(function(){
			var callback = function(msg) {
        		var url = $("#saleExportExcelUrl").attr("href");
        		$("#cardForm").attr("action",url);
        		$("#cardForm").submit();
        	};
        	exportExcelReport({
        		url: $("#saleExportCheckUrl").attr("href"),
        		param: $("#cardForm").serialize(),
        		callback: callback
        	});
		return false;
	});
	
	$("#saleExportCsv").click(function(){
			exportReport({
				exportUrl:$("#saleExportCsvUrl").attr("href"),
				exportParam:$("#cardForm").serialize()
			});
		return false;
	});
	// 柜台下拉框绑定
	var option = {
		elementId:"counterCode",
		showNum:20,
		targetId:"counterCode",
		targetDetail:true
	};
	counterSelectBinding(option);
	BINOLMBSVC02.searchList();
	
});
