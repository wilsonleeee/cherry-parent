var BINOLCTCOM03_GLOBAL = function () {

};

BINOLCTCOM03_GLOBAL.prototype = {
		// 显示客户列表
		"showCustomerInit" :function(obj){
			var recordCode = $(obj).parents().find("#recordCode").val();
			var recordName = $(obj).parents().find("#recordName").val();
			var recordType = $(obj).parents().find("#recordType").val();
			var customerType = $(obj).parents().find("#customerType").val();
			var conditionInfo = $(obj).parents().find("#conditionInfo").val();
			var userId = $("#userId").val();
			var dialogSetting = {
				dialogInit: "#customerDialogInit",
				width: 1000,
				height: 450,
				title: $("#customerDialogTitle").text()
			};
			openDialog(dialogSetting);
			var url = $(obj).attr("href");
			var param = "recordCode=" + recordCode;
				param = param + "&userId=" + userId;
				param = param + "&recordName=" + recordName;
				param = param + "&recordType=" + recordType;
				param = param + "&customerType=" + customerType;
				param = param + "&conditionInfo=" + conditionInfo;
			var callback = function(msg) {
				$("#customerDialogInit").html(msg);
			};
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callback
			});
		},
		
		// 上一步
		"back" :function(url){
			if(!$('#setDetailForm').valid()) {
				return false;
			}
			//参数序列化
			var param = $("#setDetailForm").serialize();
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: function(msg) {
					$("#div_main").html(msg);
				}
			});
		},
		
		"doSave" :function(url){
			var overSetNumber = 100000;
			var overSetValue = $("#overSetNumber").val();
			if(isNaN(overSetValue) || overSetValue == null || overSetValue==""){
				overSetNumber = 100000;
			}else{
				overSetNumber = Number(overSetValue);
			}
			var allRecordCount = 0;
			var recordCountInput = $("#commSetInfoDiv").find("#recordCount").serializeArray();
		    jQuery.each(recordCountInput, function(i){
		    	var recordCount = recordCountInput[i].value;
		    	if(!isNaN(recordCount)){
		    		allRecordCount += Number(recordCount);
		    	}
		    });
		    // 判断沟通人数是否超出提示配置人数
		    if(allRecordCount > overSetNumber){
		    	$("#lblCustomerCount").text(allRecordCount);
				var dialogId = "overSetNumber_dialog";
				var $dialog = $('#' + dialogId);
				$dialog.dialog({ 
					//默认不打开弹出框
					autoOpen: false,
					//弹出框宽度
					width: 500,
					//弹出框高度
					height: 300,
					//弹出框标题
					title:$("#overSetDialogTitle").text(),
					//弹出框索引
					zIndex: 1,
					modal: true,
					resizable:false,
					//弹出框按钮
					buttons: [{
						text:$("#overSetDialogConfirm").text(),//确认按钮
						click: function() {
							//点击确认后执行保存操作
							var param = $("#setDetailForm").serialize();
							cherryAjaxRequest({
								url: url,
								param: param,
								callback: function(msg) {
									BINOLCTCOM03.doRefresh = true;
								},
								coverId: "#pageButton"
							});
							$dialog.dialog("close");
						}
					},
					{
						text:$("#overSetDialogCancel").text(),//取消按钮
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
		    }else{
		    	// 参数序列化
				var param = $("#setDetailForm").serialize() + "&userId=" + userId;
				cherryAjaxRequest({
					url: url,
					param: param,
					callback: function(msg) {
						BINOLCTCOM03.doRefresh = true;
					},
					coverId: "#pageButton"
				});
		    }
		}
}

var BINOLCTCOM03 = new BINOLCTCOM03_GLOBAL();

window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
		if (BINOLCTCOM03.doRefresh) {
			// 刷新父页面
			window.opener.BINOLCTPLN01.search();
		}
	}
};

$(document).ready(function() {
	// 初始化时移除从上一页面带入的错误信息提示DIV
	$("#cluetip-waitimage").remove();
	$("#cluetip").remove();
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});
