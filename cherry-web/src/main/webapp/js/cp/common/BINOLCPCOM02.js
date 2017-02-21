var BINOLCPCOM02_GLOBAL = function () {

};

BINOLCPCOM02_GLOBAL.prototype = {
		/* 是否刷新一览画面 */
		"doRefresh" : false,
		
		/* 是否打开父页面锁定*/
		"needUnlock" : true,
		
		/*
		 * 显示导航条
		 */
		"showSteps" : function() {
			$li = $("#ol_steps").find("li");
			var onStep = $("#onStep").val() || "0";
			var steps = $li.length;
			
			if (steps > 0) { 
				var widthNum = 100 / steps;
				widthNum = Math.floor(widthNum * 100) / 100;
				$li.each(function(index) {
					$(this).attr("style", "width:" + widthNum + "%;");
					if (index == parseInt(onStep) - 1) {
						// 前一步骤
						$(this).attr("class", "prev");
					} else if (index == parseInt(onStep)) {
						// 当前步骤
						$(this).attr("class", "on");
					} else {
						$(this).attr("class", "");
					}
					
					if (index == steps - 1) {
						$(this).addClass("last");
					}
				});
			}
		},
		
		/*
		 * 下一步动作
		 */
		"doNext" : function (actionId, actionKbn, configKbn){
			if ("back" != actionKbn && !$('#mainForm').valid()) {
				return false;
			};
			var tokenVal = parentTokenVal();
			$("#parentCsrftoken").val(tokenVal);
			if (actionId) {
				$("#actionId").val(actionId);
				$("#actionKbn").val(actionKbn);
			}
			if(configKbn){
				$("#configKbn").val(configKbn);
			}
			$("#camTemps").val(CAMPAIGN_TEMPLATE.getCamTempsVal());
			BINOLCPCOM02.needUnlock = false;
			$("#toNextForm").submit();
		},
		
		/*
		 * 保存草稿
		 */
		"doDraftSave" : function (url, saveActionId){
			if (!$('#mainForm').valid()) {
				return false;
			};
//			if (saveActionId) {
//				//$("#saveActionId").val(saveActionId);
//			}
			$("#camTemps").val(CAMPAIGN_TEMPLATE.getCamTempsVal());
			$("#parentCsrftoken").val(parentTokenVal());
			var param = $("#toNextForm").serialize();
			cherryAjaxRequest({
				url: url,
				param : param,
				callback: function(msg) {
					if (window.JSON && window.JSON.parse) {
						$("#ERROR_MSG").html(msg);
						// 打印错误信息
						var rst = CAMPAIGN_TEMPLATE.msgHandle(msg);
						$("#errorDiv").hide();
						$("#successDiv").hide();
						if (0 == rst){
							$("#ERROR_MSG").empty();
							$("#actionResultDisplay").empty();
							var msgJson = window.JSON.parse(msg);
							var selVal = msgJson['campSaveList'];
							var selLab = msgJson['message'];
							var selRes = msgJson['result'];
							var selStep = msgJson['saveStep'];
							if(null != selVal && null != selStep){
								$("#campSaveInfo").val(selVal);
								$("#saveStep").val(selStep);
							}
							if(selRes == "OK"){
								$("#successDiv").show();
								$("#successSpan").text(selLab);
								BINOLCPCOM02.doRefresh = true;
							}else{
								$("#errorDiv").show();
								$("#errorSpan").text(selLab);
							}
							$("#errorText").remove();
							$(".error").removeClass("error");
						}
					}
				},
				coverId: "#pageButton"
			});
		},
		
		/*
		 * 规则测试
		 */
		"doRuleTest" : function (){
			var testUrl = $("#RuleTestUrl").prop("href");
			var token = getParentToken();
			testUrl += "?" + token;
			var params = $("#brandInfoId").serialize() + "&" + $("#campaignType").serialize() + "&" + $("#templateType").serialize() +
			 "&" + $("#ruleMemberLevelId").serialize() +  "&campaignRule.memberLevelName=" + $("#ruleMemberLevelName").val() +
			 "&" + $("#ruleMemberLevelGrade").serialize()+ "&loadKbn=1";
			testUrl += "&" + params;
			popup(testUrl, {width:800, height: 600});
		}
};

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLCPCOM02.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLCPCOM02.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};

var BINOLCPCOM02 = new BINOLCPCOM02_GLOBAL();

$(document).ready(function() {
	$("#mainForm").find(":input.date").each(function() {
		if ($(this).hasClass("singleDate")) {
			$(this).cherryDate({
			       holidayObj:  $("#dateHolidays").val()
		       });	
		} else {
			var id = $(this).attr("id");
			var date1;
			var date2;
			var type = "maxDate";
			if (0 == id.indexOf("campFromDate_")) {
				date1 = "#" + id;
				date2 = "#campToDate_" + id.replace("campFromDate_", "");
			} else if (0 == id.indexOf("campToDate_")) {
				date1 = "#" + id;
				date2 = "#campFromDate_" + id.replace("campToDate_", "");
				type = "minDate";
			}
			CAMPAIGN_TEMPLATE.initDate(date1, date2, type);

			var type1 = "maxDate";
			var date3;
			var date4;
			if (0 == id.indexOf("saleFromDate_")) {
				date3 = "#" + id;
				date4 = "#saleToDate_" + id.replace("saleFromDate_", "");
			} else if (0 == id.indexOf("saleToDate_")) {
				date3 = "#" + id;
				date4 = "#saleFromDate_" + id.replace("saleToDate_", "");
				type1 = "minDate";
			}
			CAMPAIGN_TEMPLATE.initDate(date3, date4, type1);
		}
	});
	var rules = CAMPAIGN_TEMPLATE.getTempRules("#mainForm");
	if (rules) {
		CAMPAIGN_TEMPLATE.msgHandle($("#ERROR_MSG").html());
		cherryValidate({			
			formId: "mainForm",		
			rules: rules
		});
	}
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	BINOLCPCOM02.needUnlock = true;
});
