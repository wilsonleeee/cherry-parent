var BINOLBSSAM04_GLOBAL = function() {
};
BINOLBSSAM04_GLOBAL.prototype={
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
				BINOLBSSAM04.messageConfirm(dialogSetting.focusEvent);
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
			$("#mainForm").find(":input").each(function(){
				$(this).val($.trim(this.value));
			});
//			var $form = $("#mainForm");
//			$('#mainForm :input').each(function(){
//				$(this).val($.trim(this.value));
//			});
//			// 表单验证
//			if(!$form.valid()){
//				return;
//			}
			 // 查询参数序列化
			 var url = $("#searchUrl").attr("href");
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
					 aaSorting : [[5, "desc"]],
					 // 表格列属性设置
					 aoColumns : [	{ "sName": "No","sWidth": "10%","bSortable": false},
					              	{ "sName": "bonusEmployeePosition","sWidth": "10%"},				
									{ "sName": "saleEmployeePosition","sWidth": "10%"},
									{ "sName": "counterCode","sWidth": "20%"},
									{ "sName": "beginAmount","sWidth": "10%"},
									{ "sName": "endAmount","sWidth": "10%"},
									{ "sName": "bonusRate","sWidth": "10%"},
									{ "sName": "memo","sWidth": "10%"},
									{ "sName": "act","sWidth": "10%", "bSortable": false}],
					// 不可设置显示或隐藏的列	
					aiExclude :[0],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 固定列数
					fixedColumns : 3,
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
				var params = "recordId="+t;
				cherryAjaxRequest({
					url:url,
					param:params,
					callback:function(data){
						if(data=="SUCCESS"){
							BINOLBSSAM04.showMessageDialog({
								message:"删除成功！", 
								type:"SUCCESS"
							});
							BINOLBSSAM04.search();
						}else {
							BINOLBSSAM04.showMessageDialog({
								message:"删除失败！", 
								type:"MESSAGE"
							});
						}
					}
				});
			}
		},
		"updateSalesBonusRate": function(){
			var beginAmount = $("#beginAmount").val();
			var endAmount = $("#endAmount").val();
			if(Number(beginAmount)>Number(endAmount)){
				BINOLBSSAM04.showMessageDialog({
					message:"起算金额不能大于截止金额！", 
					type:"MESSAGE"
				});
				return false;
			}
			if(!$('#mainForm').valid()) {
				return false;
			}
			var url = $("#updateSalesBonusRateUrl").attr("href");
			var params = $("#mainForm").serialize();
			cherryAjaxRequest({
				url:url,
				param:params,
				callback:function(data){
					if(data=="SUCCESS"){
						BINOLBSSAM04.showMessageDialog({
							message:"修改成功！", 
							type:"SUCCESS",
							focusEvent:function(){
								BINOLBSSAM04.close();
							}
						});
					}else if(data=="REPEAT"){
						BINOLBSSAM04.showMessageDialog({
							message:"起止金额出现重叠，请修改数据！", 
							type:"MESSAGE"
						});
					}else {
						BINOLBSSAM04.showMessageDialog({
							message:"修改失败！", 
							type:"MESSAGE"
						});
					}
				}
			});
		},
		"addSalesBonusRate": function(){
			var beginAmount = $("#beginAmount").val();
			var endAmount = $("#endAmount").val();
			if(beginAmount>endAmount){
				BINOLBSSAM04.showMessageDialog({
					message:"起算金额不能大于截止金额！", 
					type:"MESSAGE"
				});
				return false;
			}
			if(!$('#mainForm').valid()) {
				return false;
			}
			var url = $("#addSalesBonusRateUrl").attr("href");
			var params = $("#mainForm").serialize();
			cherryAjaxRequest({
				url:url,
				param:params,
				callback:function(data){
					if(data=="SUCCESS"){
						BINOLBSSAM04.showMessageDialog({
							message:"添加成功！", 
							type:"SUCCESS",
							focusEvent:function(){
								BINOLBSSAM04.close();
							}
						});
					}else if(data=="REPEAT"){
						BINOLBSSAM04.showMessageDialog({
							message:"起止金额出现重叠，请修改数据！", 
							type:"MESSAGE"
						});
					}else {
						BINOLBSSAM04.showMessageDialog({
							message:"添加失败！", 
							type:"MESSAGE"
						});
					}
				}
			});
		},
		"checkNumber" : function(){
			var beginAmount = $("#beginAmount").val();
			var endAmount = $("#endAmount").val();
			var bonusRate = $("#bonusRate").val();
			if(isNaN(beginAmount) || beginAmount<0){
				$("#beginAmount").val("");
			}
			if(isNaN(endAmount) || endAmount<0){
				$("#endAmount").val("");
			}
			if(isNaN(bonusRate) || bonusRate<0 || bonusRate>100){
				$("#bonusRate").val("");
			}
		}
};
var BINOLBSSAM04 = new BINOLBSSAM04_GLOBAL();

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
		if (BINOLBSSAM04.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLBSSAM04.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};

$(function(){
	cherryValidate({
		formId: "mainForm",
		rules: {
			bonusEmployeePosition:{required: true},
			saleEmployeePosition:{required: true},
			counterCode:{required: true},
			beginAmount: {required: true},
			endAmount: {required: true},
			bonusRate: {required: true}
		}
	});
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			saleStartTime: {dateValid:true},	// 开始日期
			saleEndTime: {dateValid:true}	// 结束日期
		}		
	});
	// 柜台下拉框绑定
	var option = {
		elementId:"counterCode",
		showNum:20,
		targetId:"counterCode",
		targetDetail:true
	};
	counterSelectBinding(option);
	$('.ui-tabs').tabs();
	BINOLBSSAM04.needUnlock = true;
});