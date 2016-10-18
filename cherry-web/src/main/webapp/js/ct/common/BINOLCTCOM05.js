var BINOLCTCOM05_GLOBAL = function () {

};

BINOLCTCOM05_GLOBAL.prototype = {
	/* 是否刷新一览画面 */
	"doRefresh" : false,
	
	"send" :function(url){
		if(!$('#resCodeForm').valid()) {
			return false;
		}
		// 参数序列化
		var param = $("#resCodeForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				if(msg.indexOf('id="fieldErrorDiv"') == -1) {
					$("#div_testview").html($("#testResultPage").html());
				}
			},
			coverId: "#pageButton"
		});
	}
}

var BINOLCTCOM05 = new BINOLCTCOM05_GLOBAL();

$(document).ready(function() {
	cherryValidate({
		formId: "resCodeForm",		
		rules: {
			resCodeList: {required: true}
	   }		
	});
});