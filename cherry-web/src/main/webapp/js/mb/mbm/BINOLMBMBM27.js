function BINOLMBMBM27() {};

BINOLMBMBM27.prototype = {
	// 保存问题
    "addIssue": function(url) {
    	if(!$('#addIssueForm').valid()) {
			return false;
		}
    	$("#actionResultDialogDisplay").empty();
		var callback = function(msg) {
			if($('#fieldErrorDiv').length == 0 && $("#actionResultDiv").length == 0) {
				$("#addIssueDiv").html(msg);
				if($("#issueDataTable").length > 0) {
					if(oTableArr[0] != null)oTableArr[0].fnDraw();
				}
			}
		};
		cherryAjaxRequest({
			url: url,
			param: $('#addIssueForm').serialize(),
			isDialog: true,
			resultId: "#actionResultDialogDisplay",
			callback: callback
		});
    },
    // 选择担当者弹出框
	"popAssigneeList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#assignee").val($checkedRadio.val());
				var code = $checkedRadio.parent().next().text();
				var name = $checkedRadio.parent().next().next().text();
				var html = name != '' ? '(' + code + ')' + name : code;
				$("#assigneeDiv").html(html);
			}
		}
		var value = $("#assignee").val();
		popDataTableOfEmployeeList(url, null, value, callback);
	},
	// 选择报告人弹出框
	"popSpeakerList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#speaker").val($checkedRadio.val());
				var code = $checkedRadio.parent().next().text();
				var name = $checkedRadio.parent().next().next().text();
				var html = name != '' ? '(' + code + ')' + name : code;
				$("#speakerDiv").html(html);
			}
		}
		var value = $("#speaker").val();
		popDataTableOfEmployeeList(url, null, value, callback);
	},
	// 选择活动弹出框
	"popCampaignList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#campaignType").val($checkedRadio.next().val());
				$("#campaignCode").val($checkedRadio.val());
				var html = $checkedRadio.parent().next().text();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm27.delCampaignHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#campaignDiv").html(html);
			}
		}
		var value = $("#campaignCode").val();
		popDataTableOfCampaignList(url, null, value, callback);
	},
	// 删除选择的活动
	"delCampaignHtml": function() {
		$("#campaignType").val("");
		$("#campaignCode").val("");
		$("#campaignDiv").empty();
	},
	// 选择关联销售弹出框
	"popSaleRecordList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#billCode").val($checkedRadio.val());
				var html = $checkedRadio.val();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm27.delSaleRecordHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#billCodeDiv").html(html);
			}
		}
		var value = $("#billCode").val();
		var param = $("#memberInfoId").serialize();
		popDataTableOfSaleRecordList(url, param, value, callback);
	},
	// 删除选择的关联销售
	"delSaleRecordHtml": function() {
		$("#billCode").val("");
		$("#billCodeDiv").empty();
	},
	"selectIssueType": function(val) {
		var $first = $("#issueSubType").find('option:first');
		var option = '<option value="'+$first.val()+'">'+$first.text()+'</option>';
		var showFlag = false;
		if(val != "") {
			$("#issueSubTypeTemp").find('option').each(function(){
				var _val = $(this).val();
				var text = $(this).text();
				if(_val.split('_')[0] == val) {
					option += '<option value="'+_val+'">'+text+'</option>';
					showFlag = true;
				}
			});
		}
		$("#issueSubType").html(option);
		if(showFlag) {
			$("#issueSubType").show();
		} else {
			$("#issueSubType").hide();
		}
	}
};

var binolmbmbm27 =  new BINOLMBMBM27();

$(function() {
	$('#dueDate').cherryDate();
	issueBinding({
		elementId:"reIssueNo",
		showNum:10,
		selected :"code",
		param: $("#memberInfoId").serialize()
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'addIssueForm',
		rules: {
			issueSummary: {maxlength: 100},
			issueType: {required: true},
			dueDate: {dateValid: true},
			reIssueNo: {maxlength: 22},
			billCode: {maxlength: 35},
			description: {maxlength: 1000},
			actionBody: {maxlength: 1000}
		}
	});
	if($("#issueSubTypeTemp").find('option').length > 0) {
		$("#issueType").change(function(){
			var val = $(this).val();
			binolmbmbm27.selectIssueType(val);
		});
		$("#issueType").change();
	}
});

