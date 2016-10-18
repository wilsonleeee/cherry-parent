var BINOLJNCOM03_GLOBAL = function () {

};
BINOLJNCOM03_GLOBAL.prototype = {
	"doRefresh" : false,
	/*
	 * 切换模板
	 */
	"change" : function (){
					// AJAX请求地址
					var url = $("#changeTemplateUrl").attr("href");
					$("#parentCsrftoken").val(parentTokenVal());
					// 查询参数序列化
					var params= $("#mainForm").serialize();
					cherryAjaxRequest( {
						url : url,
						param : params,
						callback : function(msg) {
							$("#grpRules").html(msg);
							$("#save-button").show();
						}
					});
				},
	/*
	 * 保存
	 */
	"saveGrp" : function (url){
					$("#ruleDetail").val(CAMPAIGN_TEMPLATE.getCamTempsVal());
					$("#parentCsrftoken").val(parentTokenVal());
					var param = $("#mainForm").serialize();
					if (1 == $("#hasRule").length) {
						param += "&" + $("#hasRule").serialize();
					}
					cherryAjaxRequest({
						url: url,
						param: param,
						callback: function(msg) {
							BINOLJNCOM03.doRefresh = true;
						},
						coverId: "#save-button"
					});
				},
				
	/*
	 * 查询活动
	 */
	"searchCamp" : function (){
		var url = $("#searchUrl").attr("href");
		 // 查询参数序列化
		 var params= $("#mainForm").serialize();
		 url = url + "?" + params;
		 // 显示结果一览
		 $("#sectionContent").show();
		// 表格设置
		 var tableSetting = {
				 // 表格ID
				 tableId : '#campDataTable',
				 // 数据URL
				 url : url,
				 // 表格列属性设置
				 aoColumns : [	
				              	{ "sName": "no","bSortable": false, "sWidth": "1%"}, 								// 1
				              	{ "sName": "campaignName", "sWidth": "25%"},											// 2
								{ "sName": "campaignFromDate", "sWidth": "15%"}, 			            			// 3
								{ "sName": "campaignToDate", "sWidth": "15%"},                          				// 4
								{ "sName": "campaignStatus", "sWidth": "10%"},                          				// 5
								{ "sName": "employeeName", "sWidth": "15%"},                          				// 6
								{ "sName": "operator","sClass":"center","bSortable": false, "sWidth": "19%"}],		// 7
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 固定列数
				fixedColumns : 2
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	},
	
	/*
	 * 规则测试
	 */
	"doRuleTest" : function (){
		var testUrl = $("#RuleTestUrl").prop("href");
		var token = getParentToken();
		testUrl += "?" + token;
		var params = $("#brandInfoId").serialize() + "&ruleDetail=" + CAMPAIGN_TEMPLATE.getCamTempsVal() 
		+ "&" + $("#campaignType").serialize() + "&" + $("#campaignGrpId").serialize() + "&loadKbn=0";
		testUrl += "&" + params;
		popup(testUrl, {width:800, height: 600});
	}
};
var BINOLJNCOM03 = new BINOLJNCOM03_GLOBAL();

window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
		if (BINOLJNCOM03.doRefresh) {
			// 刷新父页面
			window.opener.JNCOM02_search();
		}
	}
};

$(document).ready(function() {
	$('.tabs').tabs();
	$('.tabs').show();
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	BINOLJNCOM03.change();
//	if ($("#campDataTable").length > 0) {
//		// 查询活动
//		BINOLJNCOM03.searchCamp();
//	}
});
