var BINOLJNMAN03_GLOBAL = function () {
    
};

BINOLJNMAN03_GLOBAL.prototype = {
		/*
		 * 是否刷新父页面
		 */
		"doRefresh" : false,
		
		/*
		 * 上一步处理
		 */
		"doBack" : function() {
			var tokenVal = parentTokenVal();
			$("#parentCsrftoken").val(tokenVal);
			$("#toBackForm").submit();
		},

		/*
		 * 保存处理
		 */
		"doSave" : function(url) {
			
			cherryAjaxRequest({
				url: url,
				callback: function(msg) {
					BINOLJNMAN03.doRefresh = true;
				},
				coverId: "#pageButton"
			});
		}
};

var BINOLJNMAN03 = new BINOLJNMAN03_GLOBAL();

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLJNMAN03.doRefresh) {
			// 刷新父页面
			window.opener.BINOLJNMAN01.changeCampaign();
		}
	}
	
};