var BINOLJNCOM05_GLOBAL = function () {

};
BINOLJNCOM05_GLOBAL.prototype = {
		/*
		 * 优先级设置
		 */
		"prioritySave" : function (){
			// 参数序列化
			var param = null;
			// 基本信息
			$("#mainForm").find(":input").each(function() {
				if (($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
					return true;
				}
				if (!$(this).is(":disabled")) {
					$(this).val($.trim(this.value));
					if (null == param) {
						param = $(this).serialize();
					} else {
						param += "&" + $(this).serialize();
					}
				}
			});
			var url = $("#savePriorityUrl").attr("href");
			cherryAjaxRequest({
				url : url,
				param : param,
				callback : function(msg){
					var dialogSetting = {
							dialogInit: "#dialogPopConf",
							text: "<p class='message'><span>" + msg +"</span></p>",
							width: 	300,
							height: 250,
							title: 	"规则配置",
							confirm: "确认",
							confirmEvent: function(){
								removeDialog("#dialogPopConf");
								BINOLJNCOM05.doClose();
							},
							closeEvent: function(){
								removeDialog("#dialogPopConf");
								BINOLJNCOM05.doClose();
							}
						};
					openDialog(dialogSetting);
				}
			});
		},
		/*
		 * 跳转到规则配置页
		 */
		"addConfig" : function(kbn){
			var url;
			if ("0" == kbn) {
				// 新建配置
				url = $("#addUrl").attr("href");
			} else {
				// 编辑配置
				url = $("#editUrl").attr("href");
				$("#campGroupId").val($("#campaignGrpId").val());
			}
			$("#toConfForm").attr("action", url);
			var tokenVal = parentTokenVal();
			$("#parentCsrftoken").val(tokenVal);
			$("#toConfForm").submit();
		},
		/*
		 * 关闭页面
		 */
		"doClose" : function(){
			window.close();
			window.opener.unlockParentWindow();
			window.opener.search();
		}
}

var BINOLJNCOM05 = new BINOLJNCOM05_GLOBAL();