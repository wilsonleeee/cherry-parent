var BINOLJNMAN02_GLOBAL = function () {
    
};

BINOLJNMAN02_GLOBAL.prototype = {
		/*
		 * 会员入会下一步处理
		 */
		"doNext" : function() {
			if (!$('#mainForm').valid()) {
				return false;
			};
			
			$("#camTemps").val(CAMPAIGN_TEMPLATE.getCamTempsVal());
			$("#relationInfo").val(CAMPAIGN_TEMPLATE.getRelationVal());
			var tokenVal = parentTokenVal();
			$("#parentCsrftoken").val(tokenVal);
			$("#toNextForm").submit();
		}
};
var BINOLJNMAN02 = new BINOLJNMAN02_GLOBAL();

$(document).ready(function() {
	CAMPAIGN_TEMPLATE.msgHandle($("#ERROR_MSG").html());
	var rules = CAMPAIGN_TEMPLATE.getTempRules("#mainForm");
	cherryValidate({			
		formId: "mainForm",		
		rules: rules
	});
});