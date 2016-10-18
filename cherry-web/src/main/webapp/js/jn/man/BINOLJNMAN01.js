var BINOLJNMAN01_GLOBAL = function() {

};
BINOLJNMAN01_GLOBAL.prototype = {

	/*
	 * 切换品牌
	 */
	"changeBrand" : function() {
		$("#actionResultDisplay").empty();
		// AJAX请求地址
		var url = $("#changeBrandUrl").attr("href");
		// 品牌ID
		var param = $("#brandSel").serialize();
		param += "&" + $('#csrftoken').serialize();
		if ($("#memberClubId").length > 0) {
			param += "&" + $("#memberClubId").serialize();
		}
		// 清空下拉框
		$("#levelTime").empty();
		$("#memberClubId").empty();
		var clubUrl = $("#searchClubUrl").attr("href");
		doAjax2(clubUrl, "memberClubId", "clubName", $("#memberClubId"), param, null, function(message){
			if ($("#memberClubId").find("option").length == 0) {
					var str = '<option value="">--未设置--</option>';
					$("#memberClubId").append(str);
			}
			// 重置下拉框的数据
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : function(msg) {
					if (window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
						for ( var one in msgJson) {
							var selVal = msgJson[one]['levelDate'];
							var selLab = msgJson[one]['levelDate'];
							var str = '<option value="' + selVal + '">'
									+ escapeHTMLHandle(selLab) + '</option>';
							$('#levelTime').append(str);
						}
						// 更新等级详细信息
						BINOLJNMAN01.changeCampaign();
					}
				}
			});
		});
		
},

/*
 * 切换活动
 */
"changeCampaign" : function() {
	// AJAX请求地址
	var url = $("#changeCampaignUrl").attr("href");
	// 品牌ID和TAB区分
	var param = $("#brandSel").serialize() + "&" + $("#campaignType").serialize();
	if ($("#memberClubId").length > 0) {
		param += "&" + $("#memberClubId").serialize();
	}
	cherryAjaxRequest( {
		url : url,
		param : param,
		callback : function(msg) {
			$("#camp-tabs").html(msg);
		}
	});
},

/*
 * 停用规则
 */
"stopRule" : function(suffix){
	$("#actionResultDisplay").empty();
	var memberLevelId;
	var campaignRuleId;
	var campaignType;
	$("#ruleParams_" + suffix).find(":input").each(function() {
		if(this.name == "memberLevelId"){
			memberLevelId = this.value;
		}else if(this.name == "campaignRuleId"){
			campaignRuleId = this.value;
		}
	});
	$("#comParams").find(":input").each(function() {
		if(this.name == "campaignType"){
			campaignType = this.value;
		}
	});
	var url = $("#validUrl").attr("href");
	var _param = "memberLevelId=" + memberLevelId + "&" + "campaignRuleId=" + campaignRuleId + "&" + "campaignType=" + campaignType;
	var dialogSetting = {
			dialogInit: "#dialogInit",
			text: $("#testMes").html(),
			width: 	500,
			height: 300,
			title: 	$("#title").text(),
			confirm: $("#sure").text(),
			cancel: $("#cancel").text(),
			confirmEvent: function(){BINOLJNMAN01.validHandle(url, _param);},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
	openDialog(dialogSetting);
},

/*
 * 停用处理事件
 */
"validHandle" : function(url,param){
	removeDialog("#dialogInit");
	cherryAjaxRequest( {
		url : url,
		param : param,
		callback : function(msg){
			search();
		}
	});
},

/*
 * 显示和隐藏活动信息
 */
"showCampaign" : function(flag) {
	$("#actionResultDisplay").empty();
	if (flag == 0) {
		$("#hideInfo").show();
		$("#showInfo").hide();
		$("#camp-tabs").show();
	} else {
		$("#showInfo").show();
		$("#hideInfo").hide();
		$("#camp-tabs").hide();
	}
}
};

function search () {
	BINOLJNMAN01.changeCampaign();
}

var BINOLJNMAN01 = new BINOLJNMAN01_GLOBAL();

$(document).ready(function() {
	BINOLJNMAN01.changeCampaign();
});
