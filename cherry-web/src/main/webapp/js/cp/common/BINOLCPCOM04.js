var BINOLCPCOM04_GLOBAL = function () {

};

BINOLCPCOM04_GLOBAL.prototype = {
		
		/*
		 * 开始测试
		 */
		"startRule" : function() {
			$("#camTemps").val(CAMPAIGN_TEMPLATE.getCamTempsVal());
			var url = $("#startRuleUrl").attr("href");
			// 参数序列化
			var params= $("#mainForm").serialize();
			$("#ruleResult").html('<span class="ui-icon icon-ajax"></span><span>测试中,请稍等...</span>');
			cherryAjaxRequest( {
				url : url,
				param : params,
				notdeftoken : true,
				callback : function(msg) {
					$("#ruleResult").html(msg);
				}
			});
		}
		
};

var BINOLCPCOM04 = new BINOLCPCOM04_GLOBAL();

$(document).ready(function() {
	$("#testCondition").find(":input.date").each(function() {
		if ($(this).hasClass("singleDate")) {
			$(this).cherryDate({
			       holidayObj:  $("#dateHolidays").val()
		       });	
		}
	});
});