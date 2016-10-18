function BINOLMBMBM28() {};

BINOLMBMBM28.prototype = {
	"addIssueAction": function(obj) {
		var $addIssueActionDiv = $(obj).parent().next();
		$addIssueActionDiv.find("#resolutionAdd").val("");
		$addIssueActionDiv.find("#actionBodyAdd").val("");
		$(obj).parent().hide();
		$addIssueActionDiv.show();
	},
	"removeIssueAction": function(obj) {
		$(obj).parent().parent().hide();
		$(obj).parent().parent().prev().show();
	},
	"saveIssueAction": function(obj, url) {
		var $issueActionForm = $(obj).parent().parent().find('#issueActionForm');
		var actionBody = $issueActionForm.find(":input[name=actionBodyAdd]").val();
		if(actionBody != '') {
			$("#actionResultDialogDisplay").empty();
			var param = $issueActionForm.serialize();
			var addIssueActionCallback = function(msg) {
				if($('#fieldErrorDiv').length == 0 && $("#actionResultDiv").length == 0) {
					if($("#issueDetailDialog").length > 0) {
		    			$("#issueDetailDialog").html(msg);
		    		} else {
		    			$("#addIssueDiv").html(msg);
		    		}
				}
			};
			cherryAjaxRequest({
	    		url: url,
	    		param: param,
	    		isDialog: true,
				resultId: "#actionResultDialogDisplay",
	    		callback: addIssueActionCallback
	    	});
		}
	},
	"editIssueAction": function(obj) {
		var $editIssueActionDiv = $(obj).parent().parent().next().next();
		var $actionBodyHtml = $(obj).parent().parent().next();
		if($editIssueActionDiv.is(":hidden")) {
			$editIssueActionDiv.find("#actionBodyAdd").val($editIssueActionDiv.find("#oldActionBody").val());
			$actionBodyHtml.hide();
			$editIssueActionDiv.show();
		} else {
			$actionBodyHtml.show();
			$editIssueActionDiv.hide();
		}
	},
	"delIssueAction": function(obj, url) {
		if(confirm($("#delIssueActionText").text())) {
			var $issueActionForm = $(obj).parent().parent().parent().find('#issueActionForm');
			var param = $issueActionForm.serialize();
			$("#actionResultDialogDisplay").empty();
			var delIssueActionCallback = function(msg) {
				if($('#fieldErrorDiv').length == 0 && $("#actionResultDiv").length == 0) {
					if($("#issueDetailDialog").length > 0) {
		    			$("#issueDetailDialog").html(msg);
		    		} else {
		    			$("#addIssueDiv").html(msg);
		    		}
				}
			};
			cherryAjaxRequest({
	    		url: url,
	    		param: param,
	    		isDialog: true,
				resultId: "#actionResultDialogDisplay",
	    		callback: delIssueActionCallback
	    	});
		}
	},
	"editIssueInit": function(url) {
		var editIssueInitCallback = function(msg) {
			if($("#issueDetailDialog").length > 0) {
    			$("#issueDetailDialog").html(msg);
    		} else {
    			$("#addIssueDiv").html(msg);
    		}
		}
		cherryAjaxRequest({
    		url: url,
    		param: null,
    		callback: editIssueInitCallback
    	});
	},
	"delIssue": function(url) {
		if(confirm($("#delIssueText").text())) {
			$("#actionResultDialogDisplay").empty();
			var delIssueCallback = function(msg) {
				if($('#fieldErrorDiv').length == 0 && $("#actionResultDiv").length == 0) {
					$("#addIssueButton").click();
					if($("#issueDataTable").length > 0) {
						if(oTableArr[0] != null)oTableArr[0].fnDraw();
					}
					if($("#issueDetailDialog").length > 0) {
						closeCherryDialog('issueDetailDialog');
						if(oTableArr[152] != null)oTableArr[152].fnDraw();
						if(oTableArr[153] != null)oTableArr[153].fnDraw();
					}
				}
			};
			cherryAjaxRequest({
	    		url: url,
	    		param: null,
	    		isDialog: true,
				resultId: "#actionResultDialogDisplay",
	    		callback: delIssueCallback
	    	});
		}
	},
	"contract": function(obj) {
		if($(obj).is('.icon_arrow_b')) {
			$(obj).removeClass('icon_arrow_b').addClass('icon_arrow_r');
			$(obj).parent().next().hide();
		} else {
			$(obj).removeClass('icon_arrow_r').addClass('icon_arrow_b');
			$(obj).parent().next().show();
		}
	},
	"backIssueDetail": function(url) {
    	var issueDetailCallback = function(msg) {
    		if($("#issueDetailDialog").length > 0) {
    			$("#issueDetailDialog").html(msg);
    		} else {
    			$("#addIssueDiv").html(msg);
    		}
    	};
    	cherryAjaxRequest({
    		url: url,
    		param: null,
    		callback: issueDetailCallback
    	});
	}
};

var binolmbmbm28 =  new BINOLMBMBM28();

$(function() {
	$(".crm_issue_action").mouseover(function(){
		$(this).addClass('crm_issue_action_focused');
		$(this).find("#operateDiv").show();
	});
	$(".crm_issue_action").mouseout(function(){
		$(this).removeClass('crm_issue_action_focused');
		$(this).find("#operateDiv").hide();
	});
	$(".issueLink").click(function(){
		var url = $("#searchIssueDetailUrl").attr("href");
		var param = "issueNo="+$(this).text();
		var issueDetailCallback = function(msg) {
    		if($("#issueDetailDialog").length > 0) {
    			$("#issueDetailDialog").html(msg);
    		} else {
    			$("#addIssueDiv").html(msg);
    		}
    	};
    	cherryAjaxRequest({
    		url: url,
    		param: param,
    		callback: issueDetailCallback
    	});
		return false;
	});
});

