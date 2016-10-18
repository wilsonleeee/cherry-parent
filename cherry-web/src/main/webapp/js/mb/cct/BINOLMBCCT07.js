var BINOLMBCCT07_GLOBAL = function () {
};

BINOLMBCCT07_GLOBAL.prototype = {
	"getCustomer" : function() {
		var $form = $('#rptMainForm');
		var url = $('#getCustomerListUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			reloadFlg:true,
			callback: function(data) {
				$("#getCustomerDiv").html(data);
			}
		});
	},
	
	"getCallLog" : function() {
		var $form = $('#rptMainForm');
		var url = $('#getCallLogListUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			reloadFlg:true,
			callback: function(data) {
				$("#getCallLogDiv").html(data);
			}
		});
	},
	
	"getIssue" : function() {
		var $form = $('#rptMainForm');
		var url = $('#getIssueListUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			reloadFlg:true,
			callback: function(data) {
				$("#getIssueDiv").html(data);
			}
		});
	}
}

var BINOLMBCCT07 = new BINOLMBCCT07_GLOBAL();

//刷新Cherry的session
function refreshCherryConnect() {
	if($("#refreshCherryConnectIframe").length == 0) {
		$("body").append('<iframe id="refreshCherryConnectIframe" style="display: none;"></iframe>');
	}
	$("#refreshCherryConnectIframe").attr("src",  $("#cherryRefreshSessionUrl").attr("href"));
}

$(document).ready(function() {
	$('.ui-tabs').tabs();
	BINOLMBCCT07.getCustomer();
	BINOLMBCCT07.getCallLog();
	BINOLMBCCT07.getIssue();
	
	setInterval(refreshCherryConnect, 1200000);
});
