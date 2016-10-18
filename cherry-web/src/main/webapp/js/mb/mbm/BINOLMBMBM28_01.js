function BINOLMBMBM28_01() {};

BINOLMBMBM28_01.prototype = {
    // 选择担当者弹出框
	"popAssigneeList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#assigneeAdd").val($checkedRadio.val());
				var code = $checkedRadio.parent().next().text();
				var name = $checkedRadio.parent().next().next().text();
				var html = name != '' ? '(' + code + ')' + name : code;
				$("#assigneeDiv").html(html);
			}
		}
		var value = $("#assigneeAdd").val();
		popDataTableOfEmployeeList(url, null, value, callback);
	},
	// 选择报告人弹出框
	"popSpeakerList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#speakerAdd").val($checkedRadio.val());
				var code = $checkedRadio.parent().next().text();
				var name = $checkedRadio.parent().next().next().text();
				var html = name != '' ? '(' + code + ')' + name : code;
				$("#speakerDiv").html(html);
			}
		}
		var value = $("#speakerAdd").val();
		popDataTableOfEmployeeList(url, null, value, callback);
	},
	// 选择活动弹出框
	"popCampaignList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#campaignTypeAdd").val($checkedRadio.next().val());
				$("#campaignCodeAdd").val($checkedRadio.val());
				var html = $checkedRadio.parent().next().text();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm28_01.delCampaignHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#campaignDiv").html(html);
			}
		}
		var value = $("#campaignCodeAdd").val();
		popDataTableOfCampaignList(url, null, value, callback);
	},
	// 删除选择的活动
	"delCampaignHtml": function() {
		$("#campaignTypeAdd").val("");
		$("#campaignCodeAdd").val("");
		$("#campaignDiv").empty();
	},
	// 取消编辑
	"cancelEditIssue": function(url) {
		var cancelEditIssueCallback = function(msg) {
			if($("#issueDetailDialog").length > 0) {
    			$("#issueDetailDialog").html(msg);
    		} else {
    			$("#addIssueDiv").html(msg);
    		}
		}
		cherryAjaxRequest({
    		url: url,
    		param: null,
    		callback: cancelEditIssueCallback
    	});
	},
	// 编辑问题处理
	"editIssue": function(url) {
		if(!$('#editIssueForm').valid()) {
			return false;
		}
		$("#actionResultDialogDisplay").empty();
		var editIssueCallback = function(msg) {
			if($('#fieldErrorDiv').length == 0 && $("#actionResultDiv").length == 0) {
				if($("#issueDetailDialog").length > 0) {
	    			$("#issueDetailDialog").html(msg);
	    		} else {
	    			$("#addIssueDiv").html(msg);
	    		}
				if($("#issueDataTable").length > 0) {
					if(oTableArr[0] != null)oTableArr[0].fnDraw();
				}
			}
		}
		cherryAjaxRequest({
    		url: url,
    		param: $('#editIssueForm').serialize(),
    		isDialog: true,
			resultId: "#actionResultDialogDisplay",
    		callback: editIssueCallback
    	});
	},
	// 选择关联销售弹出框
	"popSaleRecordList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#billCodeAdd").val($checkedRadio.val());
				var html = $checkedRadio.val();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm28_01.delSaleRecordHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#billCodeDiv").html(html);
			}
		}
		var value = $("#billCodeAdd").val();
		var param = $("#memberInfoId").serialize();
		popDataTableOfSaleRecordList(url, param, value, callback);
	},
	// 删除选择的关联销售
	"delSaleRecordHtml": function() {
		$("#billCodeAdd").val("");
		$("#billCodeDiv").empty();
	},
	"selectIssueType": function(val, param) {
		var $first = $("#issueSubTypeAdd").find('option:first');
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
		$("#issueSubTypeAdd").html(option);
		if(showFlag) {
			if(param != "") {
				$("#issueSubTypeAdd").val(param);
			}
			$("#issueSubTypeAdd").show();
		} else {
			$("#issueSubTypeAdd").hide();
		}
	}
};

var binolmbmbm28_01 =  new BINOLMBMBM28_01();

$(function() {
	$('#dueDateAdd').cherryDate();
	issueBinding({
		elementId:"reIssueNoAdd",
		showNum:10,
		selected :"code",
		param: $("#memberInfoId").serialize()
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'editIssueForm',
		rules: {
			issueSummaryAdd: {maxlength: 100},
			issueTypeAdd: {required: true},
			dueDateAdd: {dateValid: true},
			reIssueNoAdd: {maxlength: 22},
			billCodeAdd: {maxlength: 35},
			descriptionAdd: {maxlength: 1000},
			actionBodyAdd: {maxlength: 1000}
		}
	});
	if($("#issueSubTypeTemp").find('option').length > 0) {
		$("#issueTypeAdd").change(function(){
			var val = $(this).val();
			binolmbmbm28_01.selectIssueType(val);
		});
		var val = $("#issueTypeAdd").val();
		var param = $("#issueSubTypeAddTemp").val();
		binolmbmbm28_01.selectIssueType(val, param);
	}
});

