var BINOLCPACT12_1_global = function () {
	this.needUnlock = true;
};
BINOLCPACT12_1_global.prototype = {
		/**
		 * 反映到库存
		 * @return
		 */
		"saveToDB":function(){
			var param=$("#updateForm").formSerialize();
			param += "&csrftoken=" + getTokenVal();
			var url=$("#saveUrl").attr("href");
			var callback=function(msg){
				if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
			};
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:callback,
				coverId: "#div_main"
			});
		}
};
var BINOLCPACT12_1 = new BINOLCPACT12_1_global();

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
});
window.onbeforeunload = function(){
	if (BINOLCPACT12_1.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};