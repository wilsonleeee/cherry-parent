var BINOLSTJCS11_02=function(){};

BINOLSTJCS11_02.prototype = {
	"edit": function(url){
		url += "&" + parentTokenVal(),
		cherryAjaxRequest({
			url: url,
			callback: function(msg){
				$("body").html(msg);
			}
		});
	}
};
var BINOLSTJCS11_02 = new BINOLSTJCS11_02();

//画面初始状态加载
$(document).ready(function() {
	
});
