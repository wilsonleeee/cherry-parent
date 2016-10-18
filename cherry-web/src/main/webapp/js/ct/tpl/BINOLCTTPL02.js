var BINOLCTTPL02_GLOBAL = function () {

};

BINOLCTTPL02_GLOBAL.prototype = {
	/* 是否刷新一览画面 */
	"doRefresh" : false,
		
	/* 是否打开父页面锁定*/
	"needUnlock" : true,
	
	"doSave" :function(url){
		if(!$('#templateForm').valid()) {
			return false;
		}
		if(!BINOLCTCOM04.validIllegalChar()){
			return false;
		}
		// 参数序列化
		var param =  $("#templateForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				BINOLCTTPL02.doRefresh = true;
			},
			coverId: "#pageButton"
		});
	},
	
	"doChange" :function(){
		var url = $('#doChangeUrl').html();
		var templateUse = $('#templateUse').val();
		var param = "templateUse=" + templateUse;
		cherryAjaxRequest({
			url:url,
			param:param,
			callback: function(msg) {
				//刷新数据
				$("#templateParam").html(msg);
			}
		});
	}
	
}

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLCTTPL02.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLCTTPL02.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}
};

var BINOLCTTPL02 = new BINOLCTTPL02_GLOBAL();

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	var text = $("#msgContents").val();
	$("#msgContentsTemp").val(text);
	if(text != ""){
		//判断信息是否存在签名，有则拆分签名
		$('#signature').val("");
		var length = text.length;
		var end = text.lastIndexOf("】");
		var start = text.lastIndexOf("【");
		if(end == (length-1)){
			$('#signature').val(text.substring(start+1,end));
			$("#msgContents").val(text.substring(0,start));
		}
		var $view = $("#contentsView");
		var $param = $("#templateParam");
		var $TextNum = $("#countTextNum");
		var $MsgNum = $("#countMsgNum");
		// 根据模板变量值的内容替换信息内容
		$param.find("li").each(
			function(){
				var paramValue = $(this).find("#paramValue").val();
				var comments = $(this).find("#comments").val();
				if(text.search(paramValue)>=0){
					text = text.replace(new RegExp(paramValue,"g"), comments);
				}
			}
		);
		// 显示经过替换后的信息内容
		$view.val(text);
		// 显示信息长度统计值
		$TextNum.html(text.length);
		// 当信息长度超出70个字符的倍数时更改信息条数提示信息
		if((text.length)/70>1){
			$MsgNum.html(parseInt((text.length)/70)+1);
		}else{
			$MsgNum.html("1");
		}
	}
	// 表单验证初期化
	cherryValidate({
		formId: 'templateForm',
		rules: {
			templateName: {required: true},
			msgContents: {required: true}
		}
	});
	BINOLCTTPL02.needUnlock = true;
});