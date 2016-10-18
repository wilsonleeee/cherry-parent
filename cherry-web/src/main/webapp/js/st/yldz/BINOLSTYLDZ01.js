var BINOLSTYLDZ01_GLOBAL = function() {
};
BINOLSTYLDZ01_GLOBAL.prototype={
		/* 是否刷新一览画面 */
		"doRefresh" : false,
		/* 是否打开父页面锁定*/
		"needUnlock" : true,
		
		"close": function(){
			window.close();
			// 解除父页面锁定
			window.opener.unlockParentWindow();
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
				BINOLSTYLDZ01.messageConfirm(dialogSetting.focusEvent);
			});
		},
		
		"messageConfirm":function (focusEvent){
			closeCherryDialog("messageDialogDiv");
			if(typeof focusEvent == "function") {
				focusEvent();
			}
		},
		// 查询
		"search":function(){
			 // 查询参数序列化
			 var url = $("#searchUrl").attr("href");
//			 var params= $form.find("div.column").find(":input").serialize();
			 var params = "csrftoken=" +$("#csrftoken").val();
			 params = params + "&" +$("#mainForm").serialize();
			 url = url + "?" + params;
			 // 显示结果一览
			 $("#section").show();
			 // 表格设置
			 var tableSetting = {
					 // 表格ID
					 tableId : '#saleListTable',
					 // 数据URL
					 url : url,
					 // 排序列
					 aaSorting : [[1, "desc"]],
					 // 表格列属性设置
					 aoColumns : [	{ "sName": "No","sWidth": "10%","bSortable": false},
									{ "sName": "tradeDate","sWidth": "10%"},
									{ "sName": "tradeTime","sWidth": "10%"},
									{ "sName": "cardCode","sWidth": "10%"},
									{ "sName": "companyCode","sWidth": "10%"},
									{ "sName": "companyName","sWidth": "10%","bVisible": false},
									{ "sName": "posCode","sWidth": "10%","bVisible": false},
									{ "sName": "posBillCode","sWidth": "10%","bVisible": false},
									{ "sName": "sysBillCode","sWidth": "10%"},
									{ "sName": "hedgingBillCode","sWidth": "10%","bVisible": false},
									{ "sName": "referenceCode","sWidth": "10%","bVisible": false},
									{ "sName": "tradeType","sWidth": "10%"},
									{ "sName": "amount","sWidth": "10%"},
									{ "sName": "tradeResult","sWidth": "10%","bVisible": false},
									{ "sName": "tradeAnswer","sWidth": "10%","bVisible": false},
									{ "sName": "batchID","sWidth": "10%","bVisible": false},
									{ "sName": "act","sWidth": "10%", "bSortable": false}],
					// 不可设置显示或隐藏的列	
					aiExclude :[1],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 固定列数
					fixedColumns : 0,
					// 默认显示行数
					iDisplayLength : 10,
					fnDrawCallback:function(){}
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		"delete": function(t){
			if(t!=null && t!=undefined){
				var url = $("#deleteUrl").attr("href");
				var params = "billId="+t;
				cherryAjaxRequest({
					url:url,
					param:params,
					callback:function(data){
						if(data=="SUCCESS"){
							BINOLSTYLDZ01.showMessageDialog({
								message:"删除成功！", 
								type:"SUCCESS"
							});
							BINOLSTYLDZ01.search();
						}else {
							BINOLSTYLDZ01.showMessageDialog({
								message:"删除失败！", 
								type:"MESSAGE"
							});
						}
					}
				});
			}
		},
		"updateBankBill": function(){
			if(!$('#mainForm').valid()) {
				return false;
			}
			var url = $("#updateBankBillUrl").attr("href");
			var params = $("#mainForm").serialize();
			cherryAjaxRequest({
				url:url,
				param:params,
				callback:function(data){
					if(data=="SUCCESS"){
						BINOLSTYLDZ01.showMessageDialog({
							message:"修改成功！", 
							type:"SUCCESS",
							focusEvent:function(){
								BINOLSTYLDZ01.close();
							}
						});
					}else if("tradeDate"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"交易日期格式不正确！", 
							type:"MESSAGE"
						});
					}else if("tradeTime"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"交易时间格式不正确！", 
							type:"MESSAGE"
						});
					}else if("cardCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"卡号不能为空且长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("tradeType"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"交易类型不能为空且长度不能超过2！", 
							type:"MESSAGE"
						});
					}else if("companyCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"商户号不能为空且长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("companyName"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"商户名不能为空且长度不能超过50！", 
							type:"MESSAGE"
						});
					}else if("posCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"终端号不能为空且长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("posBillCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"终端流水号不能为空且长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("sysBillCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"系统流水号不能为空且长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("hedgingBillCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"冲正流水号长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("amount"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"金额格式不正确！", 
							type:"MESSAGE"
						});
					}else if("referenceCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"参考号不能为空且长度不能超过50！", 
							type:"MESSAGE"
						});
					}else if("tradeResult"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"交易结果不能为空且长度不能超过50！", 
							type:"MESSAGE"
						});
					}else if("tradeAnswer"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"交易应答长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("BANKBILLCODE"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"该日期参考号已经存在！", 
							type:"MESSAGE"
						});
					}else {
						BINOLSTYLDZ01.showMessageDialog({
							message:"修改失败！", 
							type:"MESSAGE"
						});
					}
				}
			});
		},
		"addBankBill": function(){
			if(!$('#mainForm').valid()) {
				return false;
			}
			var url = $("#addBankBillUrl").attr("href");
			var params = $("#mainForm").serialize();
			cherryAjaxRequest({
				url:url,
				param:params,
				callback:function(data){
					if(data=="SUCCESS"){
						BINOLSTYLDZ01.showMessageDialog({
							message:"添加成功！", 
							type:"SUCCESS",
							focusEvent:function(){
								BINOLSTYLDZ01.close();
							}
						});
					}else if("tradeDate"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"交易日期格式不正确！", 
							type:"MESSAGE"
						});
					}else if("tradeTime"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"交易时间格式不正确！", 
							type:"MESSAGE"
						});
					}else if("cardCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"卡号不能为空且长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("tradeType"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"交易类型不能为空且长度不能超过2！", 
							type:"MESSAGE"
						});
					}else if("companyCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"商户号不能为空且长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("companyName"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"商户名不能为空且长度不能超过50！", 
							type:"MESSAGE"
						});
					}else if("posCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"终端号不能为空且长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("posBillCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"终端流水号不能为空且长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("sysBillCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"系统流水号不能为空且长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("hedgingBillCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"冲正流水号长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("amount"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"金额格式不正确！", 
							type:"MESSAGE"
						});
					}else if("referenceCode"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"参考号不能为空且长度不能超过50！", 
							type:"MESSAGE"
						});
					}else if("tradeResult"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"交易结果不能为空且长度不能超过50！", 
							type:"MESSAGE"
						});
					}else if("tradeAnswer"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"交易应答长度不能超过30！", 
							type:"MESSAGE"
						});
					}else if("BANKBILLCODE"==data){
						BINOLSTYLDZ01.showMessageDialog({
							message:"该日期参考号已经存在！", 
							type:"MESSAGE"
						});
					}else {
						BINOLSTYLDZ01.showMessageDialog({
							message:"添加失败！", 
							type:"MESSAGE"
						});
					}
				}
			});
		}
};
var BINOLSTYLDZ01 = new BINOLSTYLDZ01_GLOBAL();

/**
* 页面初期处理
*/

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
		if (BINOLSTYLDZ01.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLSTYLDZ01.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};

$(function(){
	cherryValidate({
		formId: "mainForm",
		rules: {
			tradeDate:{required: true,maxlength:50},
			posCode: {required: true,maxlength:30},
			posBillCode: {required: true,maxlength:30}
		}
	});
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	$('#tradeDate').cherryDate({
	    beforeShow: function(input){
	    }
	});
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			saleStartTime: {dateValid:true},	// 开始日期
			saleEndTime: {dateValid:true}	// 结束日期
		}		
	});
	$('.ui-tabs').tabs();
	BINOLSTYLDZ01.needUnlock = true;
});