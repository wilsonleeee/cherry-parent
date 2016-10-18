var BINOLWPSAL01_GLOBAL = function() {

};

BINOLWPSAL01_GLOBAL.prototype = {
		
};

//刷新Cherry的session
function refreshCherryConnect() {
	if($("#refreshCherryConnectIframe").length == 0) {
		$("body").append('<iframe id="refreshCherryConnectIframe" style="display: none;"></iframe>');
	}
	var url = $("#cherryRefreshSessionUrl").attr("href");
	url += "?" + getSerializeToken();
	$("#refreshCherryConnectIframe").attr("src", url);
}

var BINOLWPSAL01 = new BINOLWPSAL01_GLOBAL();

$(document).ready(function(){
	$("#counterSaleBtn").click();
	// 页面自动刷新防止Session过期
	setInterval(refreshCherryConnect, 600000);
});


