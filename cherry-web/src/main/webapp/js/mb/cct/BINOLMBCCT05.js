var BINOLMBCCT05_GLOBAL = function () {
};

BINOLMBCCT05_GLOBAL.prototype = {
	"doSave" : function(url,pageType) {
		if(!$('#customerInfoForm').valid()) {
			return false;
		}
		var $form = $('#customerInfoForm');
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			callback: function(data) {
				if(pageType=="E"){
					closeCherryDialog('customerDialog',this);
					//刷新表格数据
					if(oTableArr[151] != null)
						oTableArr[151].fnDraw();
				}else{
					$("#addCustomerDiv").remove();
					$("#rightPageDiv").html($("#getIssueListBox").html());
					BINOLMBCCT01.getIssue();
				}
			}
		});
	},
	
	"doCancel" : function(url) {
		$("#addCustomerDiv").remove();
		$("#rightPageDiv").html($("#getIssueListBox").html());
		BINOLMBCCT01.getIssue();
	}
}

var BINOLMBCCT05 = new BINOLMBCCT05_GLOBAL();

$(document).ready(function() {
	
	$("#ccBirthDay").cherryDate({
		beforeShow: function(input){
			return ["minDate", "maxDate"];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'customerInfoForm',
		rules: {
			customerName: {required: true,maxlength: 30},
			ccMobilePhone: {phoneValid: $("#mobileRule").val(),maxlength: 20},
			ccTelephone: {telValid: true,maxlength: 20},
			ccBirthDay: {dateValid: true},
			ccEmail: {emailValid: true,maxlength: 60},
			company: {maxlength: 50},
			post: {maxlength: 50},
			industry: {maxlength: 50},
			ccZip: {maxlength: 6},
			ccMessageId: {maxlength: 50},
			ccProvince: {maxlength: 20},
			ccCity: {maxlength: 20},
			ccAddress: {maxlength: 100},
			ccMemo: {maxlength: 500}
		}
	});
	
});
