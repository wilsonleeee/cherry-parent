var BINOLCTCOM01_GLOBAL = function () {

};

BINOLCTCOM01_GLOBAL.prototype = {
	/* 是否刷新一览画面 */
	"doRefresh" : false,
		
	/* 是否打开父页面锁定*/
	"needUnlock" : true,
	
	// 下一步
	"toNext" :function(url){
		if(!$('#baseInfoForm').valid()) {
			return false;
		}
		// 参数序列化
		var channelName = $('#channelId option:selected').text();
		var params= "channelName=" + channelName + "&" + $("#baseInfoForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: params,
			callback: function(msg) {
				if(msg.indexOf('id="fieldErrorDiv"') == -1) {
					$("#div_main").html(msg);
				}
			}
		});
	},
	
	"changeActivity" : function(url){
		// 参数序列化
		var params= $("#baseInfoForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: params,
			callback: function(msg) {
				$("#div_main").html(msg);
			}
		});
	},
	
	"changeChannel" : function(){
		$('#counterCode').val("");
		$('#counterName').val("");
		var channelId = $("#channelId option:selected").val();
		if(channelId != null && channelId != "" && channelId != undefined){
			$('#counterName').removeAttr("disabled");
			$('#counterName').prop("class","text");
			// 柜台下拉框绑定
			var option = {
				elementId:"counterName",
				showNum:20,
				targetId:"counterCode",
				paramType:"1",
				paramValue:channelId,
				targetDetail:true,
				afterSelectFun:BINOLCTCOM01.ctAfterSelectFun
			};
			counterSelectBinding(option);
		}else{
			$('#counterName').attr("disabled","true");
			$('#counterName').prop("class","text disabled");
		}
	},
	
	"ctAfterSelectFun":function(info){
		$('#counterCode').val(info.counterCode);
		$('#counterName').val(info.counterName);
	},
	
	"conditionUseChange" : function(){
		if(!$("#conditionUseFlag").attr("checked")){
			var dialogId = "conditionCancel_dialog";
			var $dialog = $('#' + dialogId);
			$dialog.dialog({ 
				//默认不打开弹出框
				autoOpen: false,  
				//弹出框宽度
				width: 450, 
				//弹出框高度
				height: 200, 
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
						//点击确认后执行
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
	
}

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLCTCOM01.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLCTCOM01.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};

var BINOLCTCOM01 = new BINOLCTCOM01_GLOBAL();

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	$("#cluetip-waitimage").remove();
	$("#cluetip").remove();
	// 表单验证初期化
	cherryValidate({
		formId: 'baseInfoForm',
		rules: {
			planName: {required: true,maxlength: 50},
			brandInfoId: {required: true},
			memo: {maxlength: 500}
		}
	});
	
	var channelId = $("#channelId option:selected").val();
	if(channelId != null && channelId != "" && channelId != undefined){
		$('#counterName').removeAttr("disabled");
		$('#counterName').prop("class","text");
		// 柜台下拉框绑定
		var option = {
			elementId:"counterName",
			showNum:20,
			targetId:"counterCode",
			paramType:"1",
			paramValue:channelId,
			targetDetail:true,
			afterSelectFun:BINOLCTCOM01.ctAfterSelectFun
		};
		counterSelectBinding(option);
	}else{
		$('#counterName').attr("disabled","true");
		$('#counterName').prop("class","text disabled");
		$('#counterCode').val("");
		$('#counterName').val("");
	}
	
	BINOLCTCOM01.needUnlock = true;
});















