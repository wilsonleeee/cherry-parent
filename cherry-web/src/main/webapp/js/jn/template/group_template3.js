var GRPTEMP3_dialogBody ="";
var GRPTEMP3_GLOBAL = function () {

};
GRPTEMP3_GLOBAL.prototype = {
		
		"extendOption" : {
			/**
			 * 积分上限信息
			 */
			"BASE000029_INFO" : function(id) {
				var baseInfo;
				var combTemps = [];
				var $id = $("#" + id);
				if ($("#check_" + id).is(":checked")) {
					baseInfo = eval("CAMPAIGN_TEMPLATE.BASE_INFO('" + id + "')");
				}
				return baseInfo;
			},
			
			/**
			 * 策略组信息
			 */
			"BUS000042_INFO" : function(id) {
				var busInfo;
				var combTemps = [];
				var m = [];
				var $id = $("#" + id);
				$id.find("[id^='BASE']").not($("[id^='BASE']",".no_submit")).each(function (){
					var baseId = $(this).attr("id");
					var p = grptemp3.getStraInfo(baseId);
					m.push("{" + p + "}");
				});
				$("#extraInfo").val('{"straList":[' + m.toString() + ']}');
				busInfo = eval("CAMPAIGN_TEMPLATE.BUS_INFO('" + id + "')");
				return busInfo;
			},
			
			/**
			 * 配置附加条件
			 */
			"BASE000030_INFO" : function (id){
				var m = [];
				var t = [];
				var $id = $("#" + id);
				$id.find(":input").not($(":input","#dataTable")).each(function (){
					m.push('"'+ this.name+'":"'+
							$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				});
				$id.find("#dataTable").find('tr').each(function(){
					var n = [];
					var h = [];
					$(this).find("td").eq(4).find(":input").each(function (){
						var p =[];
						p.push('"'+ this.name+'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						n.push('{' + p.toString() + '}');
					});
					$(this).find(":input").not($(":input",$(this).find("td").eq(4))).each(function (){
						h.push('"'+ this.name+'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					});
					h.push('"extraRule":[' + n.toString() + "]");
					t.push('{' + h.toString() + '}');
				});
				m.push('"combinationRule":[' + t.toString() + "]");
				//CAMPAIGN_TEMPLATE.addExtArgs("extArgs", "combinationRule", "[" + t.toString() + "]");
				$("#extArgs").val('{"combinationRule":[' + t.toString() + "]}");
				return m.toString();
			},
			
			/**
			 * 策略组
			 */
			"BASE000031_INFO" : function (id){
				var m = grptemp3.getStraInfo(id);
				return m;
			},
			

			/**
			 * 优先级设置画面
			 */
			"BASE000032_INFO" : function (id){
			var m = [];
			var n = [];
			var $id = $("#" + id);
			$id.find(":input").not($(":input","[id^='rulePriority']")).each(function (){
				m.push('"'+ this.name+'":"'+
						$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
			});
			$id.find("#rulePriorityTable").find('tr').not(".thClass").each(function(){
				var p = [];
				$(this).find(":input").not($(":input", ".straCamId")).each(function (){
					p.push('"'+ this.name+'":"'+
							$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					var keys = [];
					if(this.name == "campaignId"){
						keys.push('"' + this.value + '"');
					}else if(this.name == "strategyRuleName"){
						$(this).parent().find($(":input", ".straCamId")).each(function (){
							keys.push('"' + this.value + '"');
						});
					}
					p.push('"keys":[' + keys.toString() + ']');
				});
				n.push('{' + p.toString() + '}');
			});
			$id.find("#defaultRuleTable").find('tr').not(".thClass").each(function(){
				var p = [];
				$(this).find(":input").each(function (){
					p.push('"'+ this.name+'":"'+
							$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					var keys = [];
					if(this.name == "campaignId"){
						keys.push('"' + this.value + '"');
						p.push('"keys":[' + keys.toString() + ']');
					}
				});
				n.push('{' + p.toString() + '}');
			});
			m.push('"priorityRuleList":[' + n.toString() + "]");
			$("#extraInfo").val('[' + n.toString() + ']');
			return m.toString();
		}
		},
		
		"extendRules" : {
			"BASE000031" : {
				strategyGroup: {required: true}		// 策略组名称
			}
		},
		
		"showTable" : function(obj,parentClass,id){
			if($(obj).is(":checked")){
				$(obj).parents("." + parentClass).find("." + id).show();
			}else{
				$(obj).parents("." + parentClass).find("." + id).hide();
			}
		},
		
		"poptable" : function(_this, flag, idIndex, tableId){
			GRPTEMP3_dialogBody = $('#ruleDialog').html();
			var rulePopParam = {
					thisObj : _this,
					  index : 1,
				  checkType : "checkbox",
				      modal : true,
				  autoClose : [],
				  dialogBody : GRPTEMP3_dialogBody,
				  csrftoken : getParentToken(),
				  	 params : $("#toNextForm").serialize() + '&' + $("#campaignType").serialize() + '&' + "pointRuleType=" + flag
			};
			if(flag == 0){
				$("#selectRules").attr("onClick","grptemp3.selectProduct();");
			}else if(flag == 2){
				$("#selectRules").attr("onClick","grptemp3.addExtraRule(" + idIndex + ");");
			}else if(flag == 1){
				$("#selectRules").attr("onClick","grptemp3.addStrategyRule('" + tableId + "');");
			}
			grptemp3.popDataTableOfRuleInfo(rulePopParam);
		},
		
		"popDataTableOfRuleInfo" : function (rulePopParam){
			if(rulePopParam.index != null){
				oTableArr[rulePopParam.index]= null;	
			}else{
				oTableArr[0]= null;
			}
			// 操作对象
			var thisObj = rulePopParam.thisObj;
			var url = $("#ruleSearchUrl").html();
			url = url + "?"+ rulePopParam.csrftoken;
			if(rulePopParam.checkType != null){
				url = url + "&checkType="+rulePopParam.checkType;
			}else{
				url = url + "&checkType=checkbox";
			}
			url = url + "&" + rulePopParam.params;
			// datatable索引
			if (rulePopParam.index==null){
				rulePopParam.index = 0;
			}
			
			// 弹出后是否锁定底部页面
			if (rulePopParam.modal!=null){
				var modal  = rulePopParam.modal;
			}else{
				var modal = true;
			}
			
			if (rulePopParam.dialogBody!=null){
				var dialogBody  = rulePopParam.dialogBody;
			}else{
				var dialogBody = null;
			}
			var tableSetting = {
					 // 一页显示页数
					 iDisplayLength:5,
					 // 表格ID
					 tableId : '#rule_dataTable',
					 // 数据URL
					 url : url,
					 // 表格列属性设置
					 aoColumns : [  { "sName": "checkbox","sWidth": "5%","bSortable": false}, 	// 0
									{ "sName": "campaignName","sWidth": "15%"}],                     // 2
					index:rulePopParam.index,
					colVisFlag: true,
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// pop窗口弹出后回调函数
					fnDrawCallback:rulePopParam.calrulek
			 };
			
			// 调用获取表格函数
			getTable(tableSetting);
			var dialogSetting = {modal: modal,width:600, height:345, zIndex: 9999, resizable:false,title:'规则信息', 
					close: function(event, ui) {
				if (dialogBody!=null){
					closeCherryDialog('ruleDialog',dialogBody);				
					oTableArr[rulePopParam.index]= null;			// 其中数组中的0要根据自己的datatable序号而定，如果页面只存在一个datatable则为0	
				}else{
					$(this).dialog( "destroy" );
				}
				if (!modal && rulePopParam.autoClose!=null){
					$("body").unbind('click');
				}
			}};
			if(thisObj){
				var opos = $(thisObj).offset();
				oleft = parseInt(opos.left,10) ;
				otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
				dialogSetting.position = [ oleft , otop ];
			}
			$('#ruleDialog').dialog(dialogSetting);
		},
		
		"selectProduct" : function (){
			var selectedProArr = $('#rule_dataTable input:checked');
			var index = $("#dataTable").find("tr").length;
			var idIndex = parseInt(index);
			for (var i=0;i<selectedProArr.length;i++){
				var addIndex = parseInt(i);
				var id = idIndex + addIndex;
				var selectedProInfoArr = $(selectedProArr[i]);
				var $selectedPro = window.JSON2.parse(selectedProInfoArr.val());
				var hasFlag = 1;
				$("#dataTable").find("tr").each(function (){
					$(this).find("td").eq(2).find(":input").each(function (){
						if(this.name == "campaignId" && $(this).val() == $selectedPro.campaignId){
							hasFlag = 0;
							return null;
						}
					});
				});
				if(hasFlag){
					var msgHtml = '<tr id="' + id +'"><td>'+ id +'</td>' + 
					'<td>' + $selectedPro.campaignName + '</td>' + 
					'<td><span id="campaignName">' + $selectedPro.campaignName + '</span><input type="hidden" name="campaignId" value="' + $selectedPro.campaignId + '" /></td>' + 
					'<td id="td' + id + '"><a style="color:#3366FF; float: right;" href="#" onclick="grptemp3.poptable(this,2,' + id + ');return false;">添加规则</a></td>' + 
					'<td class="center"><span><a href="#" style="color:#3366FF;" onclick="grptemp3.saveRule('+ id +');return false;">确定</a></td></tr>';
					$("#dataTable").append(msgHtml);
				}
				}

			// 关闭弹出框
			closeCherryDialog('ruleDialog', GRPTEMP3_dialogBody);
		},

		"addExtraRule" : function (index){
			var selectedProArr = $('#rule_dataTable input:checked');
			for (var i=0;i<selectedProArr.length;i++){
				var selectedProInfoArr = $(selectedProArr[i]);
				var $selectedPro = window.JSON2.parse(selectedProInfoArr.val());
				var hasFlag = 1;
				$("#td" + index).find(":input").each(function (){
					if(this.name == "campaignId" && $(this).val() == $selectedPro.campaignId){
						hasFlag = 0;
						return null;
					}
				});
				if(hasFlag){
					var msgHtml = '<span><br><span id="extraRule' + i + '">' + $selectedPro.campaignName + '</span><input type="hidden" name="campaignId" value="' + $selectedPro.campaignId + '" /><span onclick="grptemp3.deleteExtraRule(this);return false;" class="close"><span class="ui-icon ui-icon-close"></span></span></span>';
					$("#td" + index).append(msgHtml);
				}
			}

			// 关闭弹出框
			closeCherryDialog('ruleDialog', GRPTEMP3_dialogBody);
		},
		
		"deleteExtraRule" : function (obj){
			$(obj).parent().remove();
		},
		
		"saveRule" : function (id){
			var $id = $("#" + id);
			var campaignName = $id.find("#campaignName").text();
			var campaignId = $("#campaignName").next().val();
			var msgHtml = '<td>'+ id +'</td>' + 
				'<td>' + campaignName + '</td>' + 
				'<td><span id="campaignName">' + campaignName + '</span><input type="hidden" name="campaignId" value="' + campaignId + '"/></td><td id="td' + id + '">';
			$id.find("[id^='extraRule']").each(function (i){
				if(i > 0){
					msgHtml = msgHtml + '<span><br>';
				}else{
					msgHtml = msgHtml + '<span>';
				}
				var extraRuleId = $(this).next().val();
				msgHtml = msgHtml + '<span id="extraRule' + i + '">' + $(this).text() + '</span><input type="hidden" name="campaignId" value="' + extraRuleId + '"/></span>';
			});
			msgHtml = msgHtml + '</td><td class="center"><span><a class="delete" onclick="grptemp3.editRule('+ id +');return false;"><span class="ui-icon icon-edit"></span><span class="button-text">编辑</span></a></td>';
			$("#" + id).html(msgHtml);
		},
		
		"editRule" : function (id){
			var $id = $("#" + id);
			var campaignName = $id.find("#campaignName").text();
			var campaignId = $("#campaignName").next().val();
			var msgHtml = '<td>'+ id +'</td>' + 
				'<td>' + campaignName + '</td>' + 
				'<td><span id="campaignName">' + campaignName + '</span><input type="hidden" name="campaignId" value="' + campaignId + '"/></td><td id="td' + id + '"><a class="add right" onClick="grptemp3.poptable(this,2,' + id + ');"><span class="ui-icon icon-add"></span><span class="button-text">添加规则</span></a>';
			$id.find("[id^='extraRule']").each(function (i){
				var extraRuleId = $(this).next().val();
				msgHtml = msgHtml + '<span><br><span id="extraRule' + i + '">' + $(this).text() + '</span><input type="hidden" name="campaignId" value="' + extraRuleId + '"/><span onclick="grptemp3.deleteExtraRule(this);return false;" class="close"><span class="ui-icon ui-icon-close"></span></span></span>';
			});
			msgHtml = msgHtml + '</td><td class="center"><span><a onclick="grptemp3.saveRule('+ id +');return false;" class="add" href=""><span class="ui-icon icon-enable"></span><span class="button-text">确定</span></a></td>';
			$("#" + id).html(msgHtml);
		},
		
		"deleteRule" : function (id){
			var $id = $("#" + id);
			$id.remove();
			$("#dataTable").find("tr").each(function (i){
				$(this).find("td").eq(0).html(i);
				$(this).find("td").eq(3).attr("id", "td" + i);
				$(this).attr("id", i);
			});
		},
		
		"addStrategyRule" : function (tableId){
			var selectedProArr = $('#rule_dataTable input:checked');
			var trNum = $("#" + tableId).find("tr").length;
			for (var i=0;i<selectedProArr.length;i++){
				var trIndex = parseInt(trNum) + parseInt(i);
				var selectedProInfoArr = $(selectedProArr[i]);
				var hasFlag = 1;
				var $selectedPro = window.JSON2.parse(selectedProInfoArr.val());
				$("#" + tableId).find("tr").each(function (){
					$(this).find("td").eq(1).find(":input").each(function (){
						if(this.name == "campaignId" && $(this).val() == $selectedPro.campaignId){
							hasFlag = 0;
							return null;
						}
					});
				});
				if(hasFlag){
					var msgHtml = '<tr><td>' + $selectedPro.campaignName + '<input type="hidden" name="campaignId" value="' + $selectedPro.campaignId + '"/></td>';
					if(tableId == "rulePriorityTable"){
						msgHtml = '<tr><td>' + trIndex + '</td><td>' + $selectedPro.campaignName + '<input type="hidden" name="campaignId" value="' + $selectedPro.campaignId + '"/></td>';
					}
					msgHtml = msgHtml + '<td><a class="delete" onclick="grptemp3.deleteStrategyRule(this,' + "'" + tableId + "'" + ');"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a></td></tr>';
					$("#" + tableId).append(msgHtml);
				}
			}
			// 关闭弹出框
			closeCherryDialog('ruleDialog', GRPTEMP3_dialogBody);
		},
		
		"deleteStrategyRule" : function (obj, tableId){
			if(tableId == null){
				var id = $(obj).parents(".box4").first().attr("id");
				var strategyRuleId = $(obj).parents(".box2").first().attr("id");
				var size = $("#TEMPCOPY-SIZE-" + id).val();
				if (size) {
					var sizeInt = parseInt(size);
					sizeInt--;
					$("#TEMPCOPY-SIZE-" + id).val(sizeInt);
				}
				$("#rulePriorityTable").find("tr").each(function (){
					$(this).find("td").eq(1).find(":input").each(function (){
						if(this.name == "strategyRuleId" && $(this).val() == strategyRuleId){
							$(this).parents("tr").first().remove();
							return null;
						}
					});
				});
				$(obj).parent().parent().parent().parent().remove();
			}
			if(tableId != null){
				$(obj).parent().parent().remove();
				CAMPAIGN_TEMPLATE.arrowsFlag(tableId);
			}
		},
		
		"saveStrategyRule" : function (obj, id, flag){
			var factId = id;
			if(id.indexOf("_TEMPCOPY") > 0){
				var idArr = id.split("_");
				var idIndex = idArr[3]/2;
				$(obj).parents(".box4-content").find(".box2[id^='BASE000031_2']").each(function (){
					var boxId = $(this).attr("id");
					if(boxId.indexOf("_TEMPCOPY") < 0){
						idIndex++;
					}
				});
				factId = 'BASE000031_2_' + idIndex;
			}
			if(flag == 0){
				var strategyRuleName = $(obj).parents("#straName_" + id).find("#strategyGroup_" + id).val();
				var $parent = $(obj).parents("#straName_" + id).find("#strategyGroup_" + id).parent();
				if("" == strategyRuleName){
					if (0 == $parent.find("#errorText").length) {
						$parent.attr("class", "error");
						$parent.append(grptemp3.errSpan($("#errmsg1").text()));
						return true;
					}
				}
				if ($parent.find("#errorText").length > 0) {
					$parent.removeAttr("class");
					$parent.find("#errorText").remove();
				}
				var flag = 1;
				$("#rulePriorityTable").find("tr").each(function (){
					$(this).find("td").eq(1).find(":input[name='strategyRuleId']").each(function (){
						var staId = staId = $(this).val();
						if(staId != factId && $(this).prev().attr("name") == "strategyRuleName" && $(this).prev().val() == strategyRuleName){
							if (0 == $parent.find("#errorText").length) {
								$parent.attr("class", "error");
								$parent.append(grptemp3.errSpan($("#errmsg2").text()));
								flag = 0;
								return true;
							}
						}
					});
				});
				if(flag){
					if ($parent.find("#errorText").length > 0) {
						$parent.removeAttr("class");
						$parent.find("#errorText").remove();
					}
					
					var hasFlag = 1;
					$("#rulePriorityTable").find("tr").each(function (){
						$(this).find("td").eq(1).find(":input").each(function (){
							if(this.name == "strategyRuleId" && $(this).val() == factId){
								$(this).prev().prev().text(strategyRuleName);
								hasFlag = 0;
								return null;
							}
						});
					});
					if(hasFlag){
						var trNum = $("#rulePriorityTable").find("tr").length;
						var trHtml = '<tr><td>' + trNum + 1 + '</td><td><span>' + strategyRuleName + 
									'</span><input type="hidden" name="strategyRuleName" value="' + strategyRuleName + '"/>' + 
									'<input type="hidden" name="strategyRuleId" value="' + factId + '"/></td><td></td><td></td></tr>';
						$("#rulePriorityTable").append(trHtml);
					}
					$(obj).parents("#straName_" + id).find("#strategyGroup_" + id).attr("disabled", "true");
					$(obj).hide();
					$(obj).next().show();
					CAMPAIGN_TEMPLATE.arrowsFlag('rulePriorityTable');
				}
			}else if(flag == 1){
				$(obj).parents("#straName_" + id).find("#strategyGroup_" + id).removeAttr("disabled");
				$(obj).hide();
				$(obj).prev().show();
			}
		},
		
		
		/*
		 * 添加错误信息
		 * 
		 * Inputs:  String:text		错误信息
		 * 
		 * 
		 */
		"errSpan" : function (text) {
			// 新建显示错误信息的span
			var errSpan = document.createElement("span");
			$(errSpan).attr("class", "ui-icon icon-error tooltip-trigger-error");
			$(errSpan).attr("id", "errorText");
			$(errSpan).attr("title",'error|'+ text);
			$(errSpan).cluetip({
		    	splitTitle: '|',
			    width: 150,
			    tracking: true,
			    cluetipClass: 'error', 
			    arrows: true, 
			    dropShadow: false,
			    hoverIntent: false,
			    sticky: false,
			    mouseOutClose: true,
			    closePosition: 'title',
			    closeText: '<span class="ui-icon ui-icon-close"></span>'
			});
			return errSpan;
		},
		
		"getStraInfo" : function (id){
			var m = [];
			var n = [];
			var $id = $("#" + id);
			if($id.parents(".no_submit").length > 0) {
				return null;
			}
			$id.find(":input").not($(":input","#ruleTable_" + id)).each(function (){
				m.push('"'+ this.name+'":"'+
						$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
			});
			$id.find("#ruleTable_" + id).find('tr').each(function(){
				var p = [];
				$(this).find(":input").each(function (){
					p.push('"'+ this.name+'":"'+
							$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				});
				n.push('{' + p.toString() + '}');
			});
			m.push('"strategyRuleList":[' + n.toString() + "]");
			return m.toString();
		},
		
		"showAffilRule" : function (obj){
			if ($(obj).hasClass("icon-ibox-expand")) {
				$(obj).parents("td").find(".affilRulePanel").hide();
				$(obj).attr("class", "gadget-icon icon-ibox-close");
			} else {
				$(obj).parents("td").find(".affilRulePanel").show();
				$(obj).attr("class", "gadget-icon icon-ibox-expand");
			}
		}
};

var grptemp3 = new GRPTEMP3_GLOBAL();
CAMPAIGN_TEMPLATE.addOption(grptemp3.extendOption);
CAMPAIGN_TEMPLATE.addTempRules(grptemp3.extendRules);
$(document).ready(function() {
	$( "#rulePriorityTable" ).sortable({
		update:function (){CAMPAIGN_TEMPLATE.arrowsFlag("rulePriorityTable");},
		connectWith: "tbody",
		items: "tr:not(.thClass)",
		cursor: "pointer",
		grid: [500, 20]
	});
	$( "#rulePriorityDotConTable" ).sortable({
		update:function (){CAMPAIGN_TEMPLATE.arrowsFlag("rulePriorityDotConTable");},
		connectWith: "tbody",
		items: "tr:not(.thClass)",
		cursor: "pointer"
	});
	
	$("#rulePriorityTable", "#rulePriorityDotConTable").disableSelection();
});