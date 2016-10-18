var BINOLJNMAN06_GLOBAL = function () {
    
};

BINOLJNMAN06_GLOBAL.prototype = {
		/* 是否刷新一览画面 */
		"doRefresh" : false,
		
		/* 是否打开父页面锁定*/
		"needUnlock" : true,
		/*
		 * 优先级调整
		 */
		"move" : function (obj, flag, tableId){
			var $tableId = $("#" + tableId);
			if($tableId.find("tr").length > 1){
				var tHtml;
				var $mtr = $(obj).parents("tr:first");
				var mHtml = $mtr.html();
				if(flag == 0){
					$mtr.remove();
					$tableId.find(".thClass").after('<tr>' + mHtml + '</tr>');
				}else if(flag == 1){
					tHtml = $mtr.prev().html();
					$mtr.prev().html(mHtml);
					$mtr.html(tHtml);
				}else if(flag == 2){
					tHtml = $mtr.next().html();
					$mtr.next().html(mHtml);
					$mtr.html(tHtml);
				}else if(flag == 3){
					$mtr.remove();
					$tableId.append('<tr>' + mHtml + '</tr>');
				}
				BINOLJNMAN06.arrowsFlag(tableId);
			}
		},
		
		/*
		 * 调整规则排序
		 */
		"arrowsFlag" : function (tableId){
			var trPriorityLength = $("#rulePriorityTable").find("tr").not(".thClass").length;
			var msgHtml;
			var amcHtml = $("#afterMatchCont").html();
			$("#rulePriorityTable").find("tr").not(".thClass").each(function (i){
				if(trPriorityLength != 1){
					if(i == 0){
						msgHtml = '<span style="height:16px; width:16px; display:block;" class="left"></span><span style="height:16px; width:16px; display:block;" class="left"></span><span class="arrow-down left" onClick="BINOLJNMAN06.move(this,2,' + "'rulePriorityTable'" + ');return false;"></span><span class="arrow-last left" onClick="BINOLJNMAN06.move(this,3,' + "'rulePriorityTable'" + ');return false;"></span>';
					}else if(i > 0 && i < trPriorityLength - 1){
						msgHtml = '<span class="arrow-first left" onClick="BINOLJNMAN06.move(this,0,' + "'rulePriorityTable'" + ');return false;"></span><span class="arrow-up left" onClick="BINOLJNMAN06.move(this,1,' + "'rulePriorityTable'" + ');return false;"></span><span class="arrow-down left" onClick="BINOLJNMAN06.move(this,2,' + "'rulePriorityTable'" + ');return false;"></span><span class="arrow-last left" onClick="BINOLJNMAN06.move(this,3,' + "'rulePriorityTable'" + ');return false;"></span>';
					}else{
						msgHtml = '<span class="arrow-first left" onClick="BINOLJNMAN06.move(this,0,' + "'rulePriorityTable'" + ');return false;"></span><span class="arrow-up left" onClick="BINOLJNMAN06.move(this,1,' + "'rulePriorityTable'" + ');return false;"></span>';
					}
					// 优先级
					$(this).find("td:eq(6)").html(msgHtml);
					// 行号
					$(this).find("td:eq(1)").html(i + 1);
				}else{
					// 优先级
					$(this).find("td:eq(6)").empty();
					// 行号
					$(this).find("td:eq(1)").html(1);
				}
				// 匹配成功时
				if (!$(this).find("td:eq(7)").html()) {
					$(this).find("td:eq(7)").html(amcHtml);
				}
			});
			if(tableId == 'rulePriorityDotConTable'){
				$("#rulePriorityDotConTable").find("tr").not(".thClass").each(function (i){
					// 优先级
					$(this).find("td:eq(6)").empty();
					// 行号
					$(this).find("td:eq(1)").html(i + 1);
					// 匹配成功时
					$(this).find("td:eq(7)").empty();
				});
			}
		},
		
		/*
		 * 弹出附属规则一览
		 */
		"popExtraRule" : function (obj){
			var callback = function() {
				BINOLJNMAN06.selExtraRule(obj);
			};
			BINOLJNMAN06.popRule(obj, "3", "2", callback);
		},
		
		/*
		 * 弹出规则一览
		 */
		"popRule" : function (obj, campType, ruleKbn, callback){
			var param = $("#brandInfoId").serialize() + "&campaignType=" + campType
			+ "&pointRuleType=" + ruleKbn;
			if ($("#memberClubId").length > 0) {
				param += "&" + $("#memberClubId").serialize();
			}
			var option = {
					dialogId : "#campRuleDialog", // 弹出框ID
					index : 25, // datatable组 的索引
					url : $("#campRuleSearchUrl").text(), // 请求地址
					param : param, // 参数
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
		 * 选择附属规则
		 */
		"selExtraRule" : function (obj){
			// 选中的规则
			var selRuleArr = $('#campRuleTable input:checked');
			if (selRuleArr) {
				for (var i = 0;i < selRuleArr.length; i++){
					var addFlag = true;
					var ruleInfo = window.JSON2.parse($(selRuleArr[i]).val());
					$(obj).parent().find("input[name='campaignId']").each(function (){
						// 已选择的规则
						if (this.value == ruleInfo.campaignId) {
							addFlag = false;
							return false;
						}
					});
					if (addFlag) {
						var msgHtml = '<span><br>' + ruleInfo.campaignName + '<input type="hidden" name="campaignId" value="' + ruleInfo.campaignId + '" /><span onclick="BINOLJNMAN06.delExtraRule(this);return false;" class="close"><span class="ui-icon ui-icon-close"></span></span></span>';
						$(obj).parent().append(msgHtml);
						// ID后缀
						var suffix = $(obj).parent().attr("id").replace("extra_", "");
						// 主规则ID
						var geneRuleId = $("#general_" + suffix).val();
						var extraMsg = '<span style="display:block;margin-left:15px;"><label class="gray">' + ruleInfo.campaignName + '</label><input type="hidden" name="campaignId" value="' + ruleInfo.campaignId + '" /></span>'
						$extraPanel = $("div.extraRulePanel", "#prio_" + geneRuleId);
						$extraPanel.append(extraMsg);
						$extraPanel.show();
					}
				}
			}
		},
		
		/*
		 * 删除附属规则
		 */
		"delExtraRule" : function (obj){
			// ID后缀
			var suffix = $(obj).parents("td:first").attr("id").replace("extra_", "");
			// 主规则ID
			var geneRuleId = $("#general_" + suffix).val();
			$extraPanel = $("div.extraRulePanel", "#prio_" + geneRuleId);
			var extraRuleId = $(obj).parent().find("input[name='campaignId']").first().val();
			$(obj).parent().remove();
			$extraPanel.find("input[name='campaignId']").each(function (){
				// 已选择的规则
				if (this.value == extraRuleId) {
					$(this).parent().remove();
					return false;
				}
			});
			if ($extraPanel.find("input[name='campaignId']").length == 0) {
				$extraPanel.hide();
			}
		},
		
		/*
		 *保存配置
		 */
		"saveConfig" : function (){
			if (!$('#mainForm').valid()) {
				return false;
			};
			// 参数序列化
			var param = null;
			// 基本信息
			$("#mainForm").find(":input").each(function() {
				if (!$(this).is(":disabled")) {
					$(this).val($.trim(this.value));
					if (null == param) {
						param = $(this).serialize();
					} else {
						param += "&" + $(this).serialize();
					}
				}
			});
			// 已启用规则
			var usedRules = "usedRules=" + BINOLJNMAN06.getPrioParams("#rulePriorityTable");
			// 默认规则
			var deftRule = "deftRule=" + BINOLJNMAN06.getPrioParams("#deftRuleTable");
			// 未启用规则 
			var unusedRules = "unusedRules=" + BINOLJNMAN06.getPrioParams("#rulePriorityDotConTable");
			if (null != param) {
				param += "&";
			}
			param += usedRules + "&" + deftRule + "&" + unusedRules + "&" + $("#execType").serialize();
			var limit = [];
			// 积分上限设置
			$("#limitContent :checkbox:checked").each(function() {
				var rst = CAMPAIGN_TEMPLATE.convertParam(this.name, this.value);
				limit.push(rst);
				var id = $(this).attr("id");
				// 积分设置模块
				var divId = "#" + id + "Div";
				$(divId + " :radio:checked").parent().find(":input").each(function(){
					var rst = CAMPAIGN_TEMPLATE.convertParam(this.name, this.value);
					limit.push(rst);
				});
			});
			if (limit) {
				param += "&limit={" + limit.toString() + "}";
			}
			// 支付方式区分
			var payTypeKbn = $("input[name='payTypeKbn']:checked").val();
			param += "&payTypeKbn=" + payTypeKbn;
			if ("1" == payTypeKbn) {
				var payTypeCodes = "";
				$("input[name='payTypeCodeType']:checked").each(function(){
					var $pid = $("#" + this.id + "_key");
					if ("" != payTypeCodes) {
						payTypeCodes += ",";
					}
					payTypeCodes += $pid.val();
				});
				param += "&payTypeCodes=" + payTypeCodes;
			}
			param += "&zkPrt=" + $(':input[name="zkPrt"]:checked').val();
			cherryAjaxRequest({
				url: $("#saveUrl").attr("href"),
				param: param,
				coverId: "#pageConfig",
				callback: function(msg) {
					BINOLJNMAN06.doRefresh = true;
				}
			});
		},
		/*
		 *取得优先级配置提交参数
		 */
		"getPrioParams" : function (id){
			var rules = [];
			$("tr:not(.thClass)", id).each(function() {
				var ruleInfo = [];
				// 主规则
				$(this).find(":input").not($(":input", "div.extraRulePanel")).each(function(){
					if (($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
						return true;
					}
					if(this.value != '') {
						ruleInfo.push('"'+this.name+'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					}
				});
				var extraRules = [];
				$("div.extraRulePanel", this).find(":input").each(function(){
					var extraRule = []
					if(this.value != '') {
						extraRule.push('"'+this.name+'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					}
					extraRules.push("{" + extraRule.toString() + "}");
				});
				ruleInfo.push('"extraRules":' + "[" + extraRules.toString() + "]");
				rules.push("{" + ruleInfo.toString() + "}");
			});
			return "[" + rules.toString() + "]";
		},
		
		/*
		 *启用/停用规则
		 */
		"ruleStart" : function (kbn){
			var $fromPanel;
			var $toPanel;
			// 停用规则
			if ("0" == kbn) {
				$fromPanel = $("#rulePriorityTable");
				$toPanel = $("#rulePriorityDotConTable");
				// 启用规则
			} else {
				$fromPanel = $("#rulePriorityDotConTable");
				$toPanel = $("#rulePriorityTable");
			}
			// 选中的规则
			var $selRules = $fromPanel.find("input[name='checkRule']:checked");
			if ($selRules.length > 0) {
				$selRules.each(function() {
					var $mtr = $(this).parents("tr:first");
					var mHtml = $mtr.html();
					$toPanel.append('<tr>' + mHtml + '</tr>');
					$mtr.remove();
				});
				BINOLJNMAN06.arrowsFlag("rulePriorityTable");
				BINOLJNMAN06.arrowsFlag("rulePriorityDotConTable");
				$("input[name='allCheckRule']").prop("checked",false);
			}
		},
		
		/*
		 *全选规则
		 */
		"selAllRules" : function (obj){
			var $rule = $(obj).parents("tbody:first").find("input[name='checkRule']");
			if ($(obj).is(":checked")) {
				$rule.prop("checked",true);
			} else {
				$rule.prop("checked",false);
			}
		},
		
		/*
		 *选择某个规则
		 */
		"selOneRule" : function (obj){
			var $rule = $(obj).parents("tbody:first").find("input[name='allCheckRule']");
			if (!$(obj).is(":checked")) {
				$rule.prop("checked",false);
			}
		},
		
		/*
		 *显示或隐藏积分上限
		 */
		"showLimit" : function (obj){
			var id = $(obj).attr("id");
			var $divId = $("#" + id + "Div");
			if($(obj).is(":checked")){
				$divId.show();
			}else{
				$divId.hide();
			}
		},
		/*
		 *选择支付方式
		 */
		"selPayType" : function (obj){
			var id = $(obj).attr("id");
			if (id == "payTypeCode_ALL") {
				if ($(obj).is(":checked")) {
					$("input[name='payTypeCodeType']").prop("checked",true);
				} else {
					$("input[name='payTypeCodeType']").prop("checked",false);
				}
			} else {
				if ($("input[name='payTypeCodeType']").not(":checked").length > 0) {
					$("#payTypeCode_ALL").prop("checked",false);
				} else {
					$("#payTypeCode_ALL").prop("checked",true);
				}
			}
			if ($("#payTypeKbn0").is(":checked")) {
				$("#payTypeKbn0").prop("checked",false);
				$("#payTypeKbn1").prop("checked",true);
			}
		}
		
		
};
var BINOLJNMAN06 = new BINOLJNMAN06_GLOBAL();
window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLJNMAN06.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLJNMAN06.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};

$(document).ready(function() {
	
	$('.tabs').tabs();
	$('.tabs').show();
	$( "#rulePriorityTable" ).sortable({
		update:function (){BINOLJNMAN06.arrowsFlag("rulePriorityTable");},
		connectWith: "tbody",
		items: "tr:not(.thClass)",
		cursor: "move",
		grid: [500, 20]
	});
	$( "#rulePriorityDotConTable" ).sortable({
		update:function (){BINOLJNMAN06.arrowsFlag("rulePriorityDotConTable");},
		connectWith: "tbody",
		items: "tr:not(.thClass)",
		cursor: "move"
	});
	
	$("#rulePriorityTable", "#rulePriorityDotConTable").disableSelection();
	cherryValidate({			
		formId: "mainForm",		
		rules: {		
			groupName: {required: true, maxlength: 50}	// 配置名称
		}		
	});
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});