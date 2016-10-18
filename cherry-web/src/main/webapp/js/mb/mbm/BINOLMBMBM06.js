function BINOLMBMBM06() {};

BINOLMBMBM06.prototype = {
	"updMemInfo": function(url) {
		if(!$('#mainForm').valid()) {
			return false;
		}
		// 保存时将手机号解禁（否则将不会提交此参数）
		if($("#memCardMobileSameFlag").val() != undefined && $("#memCardMobileSameFlag").val() == '1') {
			$("#mobilePhone").removeAttr("disabled");
		}
		var callback = function(msg) {
			
		};
		cherryAjaxRequest({
			url: url,
			param: $('#mainForm').serialize(),
			callback: callback
		});
    },
    "editMemCode": function(object) {
    	if($("#memCode").is(":hidden")) {
    		$("#memCode").show();
    		$("#memCode").prev().hide();
    		$(object).attr("style","margin-top: -3px;");
    		$("#editMemCodeButton").prev().removeClass("icon-edit").addClass("icon-delete");
    		$("#editMemCodeButton").html($("#cancleButtonText").html());
    		// 手机号与会员卡号关联
    		if($("#memCardMobileSameFlag").val() != undefined && $("#memCardMobileSameFlag").val() == '1') {
    			$("#mobilePhone").val($("#memCode").val());
    			// 手机号与会员卡号联动，不用自行修改手机号
    	    	$("#mobilePhone").attr("disabled","disabled");
    			if ($.browser.msie) {
    				$("#memCode").bind('propertychange',function(){
            			binolmbmbm06.linkageMobile();
            		});
    			} else {
    				$("#memCode").bind('input',function(){
            			binolmbmbm06.linkageMobile();
            		});
    			}
    			
    		}
    	} else {
    		$("#memCode").hide();
    		$("#memCode").prev().show();
    		$("#memCode").val($("#memCodeOld").val());
    		$(object).attr("style","");
    		$("#editMemCodeButton").prev().removeClass("icon-delete").addClass("icon-edit");
    		$("#editMemCodeButton").html($("#editButtonText").html());
    		// 把之前绑定的事件进行解绑
    		if($("#memCardMobileSameFlag").val() != undefined && $("#memCardMobileSameFlag").val() == '1') {
    			if ($.browser.msie) {
    				$("#memCode").unbind('propertychange');
    			} else {
    				$("#memCode").unbind('input');
    			}
    			// 取消换卡需要将手机号改为原来的值
    			$("#mobilePhone").val($("#mobilePhoneOld").val());
    			$("#mobilePhone").removeAttr("disabled");
    		}
    	}
    },
    
    /***
     * 会员卡号与手机号联动
     */
    "linkageMobile" : function() {
    	$("#mobilePhone").val($("#memCode").val());
    },
    
    "changeClub" : function(obj) {
    	if (!$("#referrerClub").is(":disabled")) {
    		binolmbmbm06.backRecMsg();
    	}
    	var url = $("#changeClubUrl").attr("href");
    	var param = $("#memberClubId").serialize() + "&" + $("#memberInfoId").serialize();
    	cherryAjaxRequest({
			url: url,
			param: param,
			callback:  function(msg){
				$("#clubTable").html(msg);
				if ($('#joinTimeClub').length > 0) {
					$('#joinTimeClub').cherryDate({yearRange: 'c-100:c',maxDate:'${sysDate}'});
				}
			}
		});
    },
    "editRecMsg" : function() {
    	$("#isReceiveMsg1").prop("disabled", false);
    	$("#isReceiveMsg0").prop("disabled", false);
    	$("#referrerClub").prop("disabled", false);
    	$("span.club-dtl").hide();
    	$("span.club-edit").show();
    	$("#editMsg").hide();
    	$("#saveMsg").show();
    	
    },
    "backRecMsg" : function() {
    	$("#isReceiveMsg1").prop("disabled", true);
    	$("#isReceiveMsg0").prop("disabled", true);
    	$("#referrerClub").prop("disabled", true);
    	$("span.club-edit").hide();
    	$("span.club-dtl").show();
    	$("#editMsg").show();
    	$("#saveMsg").hide();
    },
    "saveRecMsg" : function() {
    	var url = $("#saveRecUrl").attr("href");
    	var param = $("#memberClubId").serialize() + "&" + $("#memberInfoId").serialize();
    	//+ "&isReceiveMsg=" + $('input[name="isReceiveMsg"]:checked').val() + 
    	//"&" + $("#referrer").serialize() + "&" + $("#clubReferIdOld").serialize();
    	$("#clubTable").find(':input').each(function(){
			if ($(this).is(":disabled") || ($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
				return true;
			}
			$(this).val($.trim(this.value));
			param += "&" + $(this).serialize();
		});
    	cherryAjaxRequest({
			url: url,
			param: param,
			callback:  function(msg){
				if(window.JSON && window.JSON.parse) {
					var msgJson = window.JSON.parse(msg);
					if (msgJson["RESULT"] == "OK") {
						binolmbmbm06.changeClub();
					}
				}
			}
		});
    }
};

var binolmbmbm06 =  new BINOLMBMBM06();


$(document).ready(function() {
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			memCode: {required: true,maxlength: 20},
			organizationId: {required: true},
			memName: {required: true,maxlength: 30},
			nickname : {maxlength: 30},
			employeeId: {required: true},
			mobilePhone: {phoneTelValid: "#telephone",maxlength: 20},
			telephone: {telValid: true,maxlength: 20},
			birth: {required: true,dateValid: true},
			email: {emailValid: true,maxlength: 60},
			tencentQQ : {maxlength: 20},
			gender: {required: true},
			initTotalAmount: {floatValid: [16,2]},
			referrer: {maxlength: 20},
			identityCard: {maxlength: 18},
			blogId: {maxlength: 30},
			messageId: {maxlength: 30},
			memo1: {maxlength: 512},
			memo2: {maxlength: 512},
			address: {maxlength: 200},
			postcode: {zipValid:true,maxlength: 10}
		}
	});
	
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		binolmbcom01.showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		binolmbcom01.showRegin(this,"cityTemp");
	});
});

