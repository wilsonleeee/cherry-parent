var BINOLCTCOM09_GLOBAL = function () {

};

BINOLCTCOM09_GLOBAL.prototype = {
	/* 是否刷新一览画面 */
	"doRefresh" : false,
	
	"send" :function(url){
		if(!$('#getPwdForm').valid()) {
			return false;
		}
		// 参数序列化
		var param = $("#getPwdForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				if(msg == "Y"){
					$("#div_getPwdView").html($("#getPwdSuccessResultPage").html());
				}else if(msg == "N"){
					var $parent = $("#userName").parent();
					$parent.removeClass('error');
					$parent.find("#errorText").remove();
					$parent.addClass("error");
					$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
					$parent.find('#errorText').attr("title",'error|请输入正确的用户名！');
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
				}else{
					$("#div_getPwdView").html($("#getPwdErrorResultPage").html());
				}
			},
			coverId: "#pageButton"
		});
	}
}

var BINOLCTCOM09 = new BINOLCTCOM09_GLOBAL();

$(document).ready(function() {
	$("#userName").focus();
	cherryValidate({
		formId: "getPwdForm",
		rules: {
			userName: {required: true},
			mobilePhone: {required: true, phoneValid: "^(1[34578])[0-9]{9}$", maxlength: 20}
	   }		
	});
});