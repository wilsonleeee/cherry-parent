var BINOLJNMAN10_GLOBAL = function () {
    
};

BINOLJNMAN10_GLOBAL.prototype = {
		/* 是否刷新一览画面 */
		"doRefresh" : false,
		
		/* 是否打开父页面锁定*/
		"needUnlock" : true,
		/*
		 * 弹出普通规则一览
		 */
		"popGeneRule" : function (obj){
			$(obj).parent().attr("class", "");
			$(obj).parent().find("#errorText").remove();
			var callback = function() {
				BINOLJNMAN10.selGeneRule(obj);
			};
			BINOLJNMAN10.popRule(obj, "3", "1", callback);
		},
		
		/*
		 * 弹出规则一览
		 */
		"popRule" : function (obj, campType, ruleKbn, callback){
			var option = {
					dialogId : "#campRuleDialog", // 弹出框ID
					index : 25, // datatable组 的索引
					url : $("#campRuleSearchUrl").text(), // 请求地址
					param : $("#brandInfoId").serialize() + "&campaignType=" + campType
							+ "&pointRuleType=" + ruleKbn, // 参数
					tableId : "#campRuleTable",  // datatable ID
					aoColumns : [  { "sName": "checkbox","sWidth": "5%","bSortable": false}, 	// 0
									{ "sName": "campaignName","sWidth": "15%"}],                // 1
					title : $("#rulediaTitleTxt").text(),
					confirmBtn : "#selCampRules",
					callback : callback
			};
			popDataTableOfComm(option);
		},
		
		/*
		 * 选择普通规则
		 */
		"selGeneRule" : function (obj){
			// 选中的规则
			var selRuleArr = $('#campRuleTable input:checked');
			if (selRuleArr) {
				for (var i = 0;i < selRuleArr.length; i++){
					var addFlag = true;
					var ruleInfo = window.JSON2.parse($(selRuleArr[i]).val());
					var $tbody = $("#ruleBody");
					$tbody.find("input[name='campaignId']").each(function (){
						// 已选择的规则
						if (this.value == ruleInfo.campaignId) {
							addFlag = false;
							return false;
						}
					});
					if (addFlag) {
						var msgHtml = '<tr><td>' + ruleInfo.campaignName + '<input type="hidden" name="campaignId" value="' + ruleInfo.campaignId + '"/></td><td><a class="delete" onclick="BINOLJNMAN10.delGeneRule(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a></td></tr>';
						$tbody.append(msgHtml);
					}
				}
				BINOLJNMAN10.ruleTableShow();
			}
		},
		
		/*
		 * 删除普通规则
		 */
		"delGeneRule" : function (obj){
			$(obj).parents("tr:first").remove();
			BINOLJNMAN10.ruleTableShow();
		},
		
		/*
		 * 保存组合规则
		 */
		"saveComb" : function (){
			if (!$('#mainForm').valid()) {
				return false;
			};
			var combInfo = [];
			// 组合规则基本信息
			$('#combContent').find(':input:not(.no_submit)').not($(":input", "#ruleBody")).each(function(){
				if (($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
					return true;
				}
				if ($(this).is(":disabled")) {
					return true;
				}
				if(this.value != '') {
					combInfo.push('"'+this.name+'":"'+
							$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				}
			});
			// 组合规则选择的规则信息
			var geneRules = [];
			$('#ruleBody').find("tr").each(function(){
				var geneRule = [];
				$(this).find(':input').each(function(){
					if (($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
						return true;
					}
					if(this.value != '') {
						geneRule.push('"'+this.name+'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					}
				});
				geneRules.push("{" + geneRule.toString() + "}");
			});
			if (geneRules) {
				combInfo.push('"geneRules":' + "[" + geneRules.toString() + "]");
			}
			var param = "combInfo={" + combInfo.toString() + "}";
			var url = $("#saveCombUrl").attr("href");
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: function(msg) {
					BINOLJNMAN10.doRefresh = true;
				},
				coverId: "#pageComb"
			});
		},
		
		/*
		 * 显示或者隐藏普通规则列表
		 */
		"ruleTableShow" : function (){
			if ($("tr","#ruleBody").length == 0) {
				$("#ruleSelTable").hide();
			} else {
				$("#ruleSelTable").show();
			}
		}
		
}

var BINOLJNMAN10 = new BINOLJNMAN10_GLOBAL();
window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLJNMAN10.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLJNMAN10.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};

$(document).ready(function() {
	BINOLJNMAN10.ruleTableShow();
	cherryValidate({			
		formId: "mainForm",		
		rules: {		
			campaignName: {required: true, maxlength: 50}	// 组合名称
		}		
	});
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});