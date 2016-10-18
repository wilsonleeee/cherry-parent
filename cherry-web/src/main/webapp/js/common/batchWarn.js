var batchWarn = function () {};

batchWarn.prototype = {
	"getBatchWFView":function(){
		var callback = function(msg){
			var dialogSetting = {
				dialogInit : "#divWorkFlowView",
				text:msg,
				zIndex : 1000,
				width : 600,
				height : 900,
				title : $("#header_WorkFlowView").text(),
				closeEvent: function(){
					batchWarn.removeDialog("#divWorkFlowView");
				}
			};
			openDialog(dialogSetting);
		};
		cherryAjaxRequest({
			url:$("#getViewUrl").attr("href"),
			callback:callback
		});
	},
	
	"removeDialog":function(dialogDiv){
		var dialogDivId = $(dialogDiv).attr("id");
		$(dialogDiv).dialog('destroy');
		$(dialogDiv).remove();
		$('#spanBatchWarn').append('<div style="display:none" id="'+dialogDivId+'"></div>');
	},
	
	/**
	 * 闪烁元素
	 * @param selector
	 */
	"blinkSelector":function(selector,time){
		$(selector).fadeOut(time, function(){
			$(selector).fadeIn(time, function(){
				batchWarn.blinkSelector(selector,time);
			});
		});
	}
};

var batchWarn = new batchWarn();

$(document).ready(function() {
	var callback = function(msg){
		var json = eval("("+msg+")");
		if(json.BatchWarn == "true"){
			$("#spanBatchWarn").removeClass("hide");
			batchWarn.blinkSelector("#spanBatchWarnBlink",300);		
		}
	};
	
	cherryAjaxRequest({
		url:$("#getBatchExecuteResultUrl").attr("href"),
		callback:callback
	});
} );