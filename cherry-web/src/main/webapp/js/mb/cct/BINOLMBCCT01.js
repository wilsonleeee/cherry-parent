var BINOLMBCCT01_GLOBAL = function () {
};

BINOLMBCCT01_GLOBAL.prototype = {
	"addIssue" : function() {
		var $form = $('#ccMainForm');
		var url = $('#addIssueUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			reloadFlg:true,
			callback: function(data) {
				$("#addIssueDiv").html(data);
			}
		});
	},
	
	"newIssue" : function() {
		var $form = $('#ccMainForm');
		var url = $('#addIssueUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			callback: function(data) {
				$("#addIssueDiv").html(data);
				BINOLMBCCT01.getIssue();
			}
		});
	},
	
	"getIssue" : function() {
		var $form = $('#ccMainForm');
		var url = $('#getIssueUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			reloadFlg:true,
			callback: function(data) {
				$("#getIssueDiv").html(data);
				BINOLMBCCT02.search();
			}
		});
	},
	
	"getMemberInfo" : function() {
		var $form = $('#ccMainForm');
		var url = $('#getMemberInfoUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			reloadFlg:true,
			callback: function(data) {
				$("#getMemberInfoDiv").html(data);
			}
		});
	},
	
	"getMemberList" : function() {
		var $form = $('#ccMainForm');
		var url = $('#getMemberListUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			reloadFlg:true,
			callback: function(data) {
				$("#getMemberListDiv").html(data);
				BINOLMBCCT04.search();
			}
		});
	},
	
	"addCustomer" : function() {
		var $form = $('#ccMainForm');
		var url = $('#addCustomerUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			reloadFlg:true,
			callback: function(data) {
				$("#addCustomerDiv").html(data);
			}
		});
	},
	
	"memberSearch" : function() {
		if($("#searchEvent").val()=="S"){
			BINOLMBCCT01.getMemberList();
		}else{
			$("#phoneInBox").html($("#memberListBox").html());
			BINOLMBCCT01.getMemberList();
		}
	},
	
	"showPage" : function() {
		if($("#showPageValue").val()=="0"){
			$("#issueBox").hide();
			$("#showText").html($("#showTextDiv").html());
			$("#showPageValue").val("1");
			$("#memberInfoPage").attr("style","overflow-y: auto;padding: 0;margin: 0;");
		}else{
			$("#issueBox").show();
			$("#showText").html($("#hideTextDiv").html());
			$("#showPageValue").val("0");
			$("#memberInfoPage").attr("style","height:450px; overflow-y: auto;padding: 0;margin: 0;");
		}
	}
}

var BINOLMBCCT01 = new BINOLMBCCT01_GLOBAL();

//刷新Cherry的session
function refreshCherryConnect() {
	if($("#refreshCherryConnectIframe").length == 0) {
		$("body").append('<iframe id="refreshCherryConnectIframe" style="display: none;"></iframe>');
	}
	$("#refreshCherryConnectIframe").attr("src",  $("#cherryRefreshSessionUrl").attr("href"));
}

$(document).ready(function() {
	var showPage=$("#showPage").val();
	if(showPage=="1"){
		$("#phoneInBox").html($("#memberListBox").html());
		BINOLMBCCT01.getMemberList();
	}else if(showPage=="2"){
		$("#phoneInBox").html($("#memberCallBox").html());
		BINOLMBCCT01.addIssue();
		BINOLMBCCT01.getIssue();
		BINOLMBCCT01.getMemberInfo();
	}else if(showPage=="3"){
		$("#phoneInBox").html($("#nonMemberCallBox").html());
		BINOLMBCCT01.addIssue();
		BINOLMBCCT01.getIssue();
	}else if(showPage=="4"){
		$("#phoneInBox").html($("#newNonMemberCallBox").html());
		BINOLMBCCT01.addIssue();
		BINOLMBCCT01.addCustomer();
	}
	// 设置页面样式
	if($("#customerType").val()=="0"){
		$("#leftPageDiv").attr("style","width:50%;overflow-y: auto;padding: 5px 0 0 0;");
		$("#rightPageDiv").attr("style","width:49%;overflow-y: auto;padding: 5px 0 0 0;");
	}else{
		$("#leftPageDiv").attr("style","width:50%;height:430px;overflow-y: auto;padding: 5px 0 0 0;");
		$("#rightPageDiv").attr("style","width:49%;height:430px;overflow-y: auto;padding: 5px 0 0 0;");
	}
	
	setInterval(refreshCherryConnect, 600000);
});
