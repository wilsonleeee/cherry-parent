var BINOLWPSAL09_GLOBAL = function() {

};

BINOLWPSAL09_GLOBAL.prototype = {
	// 提交绑定信息
	"confirm":function(){
		var bindUrl = $("#bindUrl").attr("href");
		var params = $("#bindCounterForm").serialize();
		cherryAjaxRequest({
			url: bindUrl,
			param: params,
			callback: function(data) {
				if(data == "SUCCESS"){
					var counterCode = $("#counterCode").val();
					var saleUrl = $("#saleUrl").attr("href");
					var params = "counterCode=" + counterCode;
					cherryAjaxRequest({
						url: saleUrl,
						param: params,
						callback: function(data) {
							$("#webpos_main").html(data);
						}
					});
				}else{
					alert("请输入正确的柜台号和密码");
				}
			}
		});
	}
};

var BINOLWPSAL09 = new BINOLWPSAL09_GLOBAL();

$(document).ready(function(){
	// 柜台下拉框绑定
	var option = {
			elementId:"counterCode",
			showNum:20,
			selected:"code"
	};
	counterBinding(option);
	// 柜台号输入框获得焦点
	$("#counterCode").focus();
});

