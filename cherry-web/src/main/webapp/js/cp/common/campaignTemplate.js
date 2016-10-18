var CAMPAIGN_TEMPLATE_dialogBody ="";
var CAMPAIGN_TEMPLATE_GLOBAL = function () {
    
};

CAMPAIGN_TEMPLATE_GLOBAL.prototype = {
		
		/*
		 * 添加扩展参数
		 */
//		"addExtArgs" : function(obj, key, value) {
//			if ($(obj).val() == "") {
//				var p = [];
//				p.push('"' + key + '":["' + value + '"]');
//				$(obj).val('{' + p.toString() + '}');
//			} else {
//			}
//		},
		
		/*
		 * 转换提交参数
		 */
		"convertParam" : function(name, value) {
			var rst = '"'+encodeURIComponent(name)+'":"'+
					encodeURIComponent($.trim(value.replace(/\\/g,'\\\\').replace(/"/g,'\\"')))+'"';
			return rst;
		},
		
		/*
		 * 根据checkbox来显示或者隐藏DIV
		 */
		"showDiv" : function(obj, divId) {
			if ($(obj).is(":checked")) {
				$(divId).show();
			} else {
				$(divId).hide();
			}
		},
		
		/*
		 * 信息显示处理
		 */
		"msgHandle" : function (msg) {
			var rst = 0;
			// Action执行后有错误或成功消息要返回的场合
			if(msg.indexOf('actionResultDiv') > -1 || msg.indexOf('fieldErrorDiv') > -1) {
				// 把Action错误或成功信息打在画面上
				$('#actionResultDisplay').html(msg);
				// 清空原有的错误信息
				$(':input').parent().removeClass('error');
				$(':input').parent().find('#errorText').remove();
				// ActionFieldError存在的场合
				if($('#fieldErrorDiv').length > 0) {
					// 取得新的错误信息
					var fieldErroVal = $('#fieldErrorDiv :input').val();
					fieldErroVal = fieldErroVal.replace(new RegExp("({|})","gm"),"");
					var fieldErros = fieldErroVal.split(',');
					// 根据新的错误信息逐个循环，把错误打在画面输入框对应的位置
					for(var i = 0; i < fieldErros.length; i++) {
						var field = fieldErros[i].split('=');
						if(field.length == 2) {
							var fieldKeys = field[0].split('-');
							var fieldVal = field[1].replace(new RegExp("([\[]{1})|(]{1})","gm"),"");
							var m = 0;
							if(fieldKeys.length == 2) {
								m = fieldKeys[1];
							}
							var $parent = $($(':input[name="'+$.trim(fieldKeys[0])+'"]:visible')[m]).parent();
							if($parent.length == 0){
								$parent = $($('.' + $.trim(fieldKeys[0]) + ':visible')[m]).parent();
							}
							$parent.addClass("error");
							$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
							$parent.find('#errorText').attr("title",'error|'+fieldVal);
							$parent.find('#errorText').cluetip({
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
						}
					}
				}
				rst = -1;
			} else if(msg.indexOf('id="actionResultBody"') > -1) {
				$('body').html(msg);
				rst = -1;
			} else if(msg.indexOf('id="AJAXSYSERROR"') > -1) {
				$('body').html(msg);
				rst = -1;
			}
			return rst;
		}, 
		/* ******************************************************** 基础模板提交参数方法 start **************************************************** */
		/*
		 * 共通基础模板信息
		 */
		"BASE_INFO" : function(id){
			var m = [];
			var $id = $("#" + id);
			if($id.parents(".no_submit").length > 0) {
				return null;
			}
			$id.find(':input').not(".no_submit").each(function(){
				if (($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
					return true;
				}
				if(this.value != '') {
					m.push('"'+this.name+'":"'+
							$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				}
			});
			return m.toString();
		},
		
		
		/* ******************************************************** 基础模板提交参数方法 end **************************************************** */
		
		
		
		/* ******************************************************** 业务模板提交参数方法  start **************************************************** */
		/*
		 * 共通业务模板信息
		 */
		"BUS_INFO" : function(id, busArr){
			var busInfo = [];
			if (busArr) {
				busInfo = busArr;
			}
			var combTemps = [];
			var $id = $("#" + id);
			if($id.parents(".no_submit").length > 0) {
				return null;
			}
			$id.find("[id^='BASE']").filter(function() {
				return $(this).parents("[id^='BUS']").first().attr("id") == id;
			}).each(function(){
				var id = $(this).attr("id");
				var rst = CAMPAIGN_TEMPLATE.getTempResult(id, 0);
				if (rst) {
					combTemps.push("{" + rst + "}");
				}
			});
			
			$id.find("[id^='BUS']").filter(function() {
				return $(this).parents("[id^='BUS']").first().attr("id") == id;
			}).each(function(){
				var id = $(this).attr("id");
				var rst = CAMPAIGN_TEMPLATE.getTempResult(id, 1);
				if (rst) {
					combTemps.push("{" + rst + "}");
				}
			});
			busInfo.push('"combTemps":' + "[" + combTemps.toString() + "]");
			if (!busArr) {
				$id.find(':input').not($(":input", "[id^='BASE']")).filter(function() {
					return $(this).parents("[id^='BUS']").first().attr("id") == id;
				}).not(".no_submit").each(function(){
					if (($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
						return true;
					}
					if(this.value != '') {
						busInfo.push('"'+this.name+'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					}
				});
			}
			return busInfo.toString();
		},
		
		/* ******************************************************** 业务模板提交参数方法  end **************************************************** */
		
		/*
		 * 取得提交的模板内容
		 */
		"getCamTempsVal" : function() {
			var camTemps = new Array();
			var camIndex = new Array();
			$("[id^='B']").not($("[id^='B']", "[id^='BUS']")).each(function (i){
				var id = $(this).attr("id");
				var index = parseInt(i);
				camIndex[index] = id;
			});
			$("[id^='BUS']").each(function() {
				if ($(this).parents("[id^='BUS']").length != 0) {
					return true;
				}
				var id = $(this).attr("id");
				var arr = id.split("_");
				var tempCode = arr[0];
				var isFun = typeof(eval("CAMPAIGN_TEMPLATE." + tempCode + "_INFO")) == "function";
				var rst;
				if (isFun) {
					rst = eval("CAMPAIGN_TEMPLATE." + tempCode + "_INFO('" + id + "')");
				} else {
					rst = eval("CAMPAIGN_TEMPLATE.BUS_INFO('" + id + "')");
				}
				if (rst) {
					for(var i = 0; i < camIndex.length;i++){
						if(id == camIndex[i]){
							camTemps[i] = "{" + rst + "}";
							break;
						}
					}
				}
			});
			// 基础模板
			var $baseTemp = $("[id^='BASE']").not($("[id^='BASE']", "[id^='BUS']"));
			$baseTemp.each(function() {
				var id = $(this).attr("id");
				var rst = CAMPAIGN_TEMPLATE.getTempResult(id, 0);
				if (rst) {
					for(var i = 0; i < camIndex.length;i++){
						if(id == camIndex[i]){
							camTemps[i] = "{" + rst + "}";
							break;
						}
					}
				}
			});
			var t = [];
			for(var i = 0; i < camTemps.length;i++){
				if(camTemps[i] != null){
					t.push(camTemps[i]);
				}
			}
			var camTempsVal = "[" + t.toString() + "]";
			return camTempsVal;
		},
		
		/*
		 * 取得模板提交数据
		 */
		"getTempResult" : function(id, kbn) {
			if (id) {
				var arr = id.split("_");
				var tempCode = arr[0];
				var isFun = typeof(eval("CAMPAIGN_TEMPLATE." + tempCode + "_INFO")) == "function";
				var rst;
				if (isFun) {
					rst = eval("CAMPAIGN_TEMPLATE." + tempCode + "_INFO('" + id + "')");
				} else {
					var funName;
					if (0 == kbn) {
						funName = "BASE_INFO";
					} else if (1 == kbn) {
						funName = "BUS_INFO";
					} else {
						return null;
					}
					rst = eval("CAMPAIGN_TEMPLATE." + funName + "('" + id + "')");
				}
				return rst;
			}
		},
		
		/*
		 * 取得需要验证的输入框
		 */
		"getTempRules" : function(formId) {
			var rules = {};
			$("[id^='BUS'], [id^='BASE']").each(function() {
				var id = $(this).attr("id");
				var arr = id.split("_");
				var tempCode = arr[0];
				var tempRule = CAMPAIGN_TEMPLATE.tempRules[tempCode];
				if (tempRule) {
					rules = $.extend(rules, tempRule);
				}
			});
			return rules;
		},
		
		/*
		 * 需要验证的输入框集合
		 */
		"tempRules" : {		
			
		},	
		
		/*
		 * 单次购买或者累积购买的小模板关系
		 */
		"BUS000001_Relation" : function(id) {
			var $id = $("#" + id);
			if ($("#check_" + id).is(":checked")) {
				var relation = "";
				// 关系
				var relationChar = $id.find("input[name='relationChar']").val();
			//	if (0 == baseRelatFlg) {
					$id.find("[id^='BASE']").each(function(){
						var baseId = $(this).attr("id");
						var arr = baseId.split("_");
						var tempCode = arr[0];
						if ("" == relation) {
							relation = "a" + tempCode + "z";
						} else {
							relation += " " + relationChar + " a" + tempCode + "z";
						}
					});
			//	}
				return relation;
			} else {
				return null;
			}
		},
		
		/*
		 * 取得提交的模板关系
		 */
		"getRelationVal" : function(busDefRelat) {
			var relationInfo = [];
			var busRelation;
			var busRelationAnd = "";
			var busRelationOr = "";
			$("div[id^='BUS']").each(function() {
				var id = $(this).attr("id");
				var arr = id.split("_");
				var tempCode = arr[0];
				var tempCode1 = arr[0];
				if ("BUS000002" == tempCode1) {
					tempCode1 = "BUS000001";
				}
				var isFun = typeof(eval("CAMPAIGN_TEMPLATE." + tempCode + "_Relation")) == "function";
				if (isFun) {
					var rst = eval("CAMPAIGN_TEMPLATE." + tempCode1 + "_Relation('" + id + "')");
					if (null != rst && "" != rst) {
						relationInfo.push('"' + tempCode + '":"' + rst + '"');
					}
				}
				var $id = $("#" + id);
				// 关系
				var busRelationChar = $id.find("input[name='busRelationChar']").val();
				if ("&&" == busRelationChar) {
					if ("" == busRelationAnd) {
						busRelationAnd = "a" + tempCode + "z";
					} else {
						busRelationAnd += " && a" + tempCode + "z";
					}
				} else {
					if ("" == busRelationOr) {
						busRelationOr = "a" + tempCode + "z";
					} else {
						busRelationOr += " || a" + tempCode + "z";
					}
				}
				if ("" == busRelationAnd) {
					busRelation = busRelationOr;
				} else {
					if ("" != busRelationOr) {
						busRelation = busRelationAnd + " && (" + busRelationOr + ")";
					} else {
						busRelation = busRelationAnd;
					}
				}
			});
			relationInfo.push('"relation":' + '"' + busRelation + '"');
			return "{" + relationInfo.toString() + "}";
		},
		
		/* 
		 * 初期化日期
		 * 
		 * Inputs:  String:date1		第一个日期
		 * 			String:date2		第二个日期
		 * 			String:type			类型
		 * 
		 */
		"initDate" : function (date1, date2, type) {
			$(date1).cherryDate({
				holidayObj: $("#dateHolidays").val(),
				beforeShow: function(input){
					var value = $(date2).val();
					return [value, type];
				}
			});
		},
		
		/*
		 * 改变克隆模块的ID
		 */
		"changeIndexTempCopy" : function (copyDivId) {
			var oldSuffix;
			var newSuffix;
			$(copyDivId).find("[id*='_TEMPCOPY']").each(function(i) {
				var id = $(this).attr("id");
				if (0 == i) {
					var copyIndex = id.indexOf("_TEMPCOPY");
					var tempId = id.substring(0, copyIndex);
					var idArr = tempId.split("_");
					var index = idArr[idArr.length - 1];
					var newIndex = parseInt(index) + 1;
					oldSuffix = "_" + index + '_TEMPCOPY';
					newSuffix = "_" + newIndex + '_TEMPCOPY';
				}
				var newId = id.replace(oldSuffix, newSuffix);
				$(this).attr("id", newId);
			});
			$(copyDivId).find("[name*='_TEMPCOPY']").each(function() {
				var newName = this.name.replace(oldSuffix, newSuffix);
				$(this).attr("name", newName);
			});
			$(copyDivId).find("[for*='_TEMPCOPY']").each(function() {
				var newFor = $(this).attr("for").replace(oldSuffix, newSuffix);
				$(this).attr("for", newFor);
			});
			$(copyDivId).find("[onclick*='_TEMPCOPY']").each(function() {
				var newClick = $(this).attr("onclick").replace(oldSuffix, newSuffix);
				$(this).attr("onclick", newClick);
			});
		},
		"addOption" : function (option) {
			jQuery.extend(this, option);
		},
		
		"addTempRules" : function (rules) {
			jQuery.extend(this.tempRules, rules);
		},
		
		// 添加规则
		"addRule" : function (obj,divId,parentClass){
			CAMPAIGN_TEMPLATE.changeIndexTempCopy("#add_" + divId);
			$("#" + divId).find("." + parentClass + "-content").first().append($("#add_" + divId).html());
			CAMPAIGN_TEMPLATE.changeIndexTempCopy("#add_" + divId);
			$(obj).parents("." + parentClass).find(".deleteClass").show();
			var size = $("#TEMPCOPY-SIZE-" + divId).val();
			if (size) {
				var sizeInt = parseInt(size);
				sizeInt++;
				$("#TEMPCOPY-SIZE-" + divId).val(sizeInt);
			}
		},
		
		// 删除规则
		"deleteRule" : function (obj,parent){
			$parent = $(obj).parents("." + parent).first();
			if ($parent.prev(".line_Yellow").length == 0) {
				if($parent.nextAll(".line_Yellow").length == 0){
					$parent.parent().nextAll(".line_Yellow").first().remove();
				}else{
					$parent.nextAll(".line_Yellow").first().remove();
				}
			} else {
				$parent.prev(".line_Yellow").remove();
			}
			$box = $(obj).parents("." + parent).last();
			if($box.find("." + parent).find(".deleteClass").length == 0){
				$box = $(obj).parents("." + parent).parent().parent();
			}
			if($box.find("." + parent).find(".deleteClass").length == 3){
				$box.find("." + parent).find(".deleteClass").hide();
			}
			$parent.remove();
			var id = $box.attr("id");
			var size = $("#TEMPCOPY-SIZE-" + id).val();
			if (size) {
				var sizeInt = parseInt(size);
				sizeInt--;
				$("#TEMPCOPY-SIZE-" + id).val(sizeInt);
			}
		},
		
		"selPrtIndex" : "",
		
		"openProPopup" : function (_this, dialogType, selPrtIndex, csrftoken){
			CAMPAIGN_TEMPLATE.selPrtIndex = selPrtIndex;
			if(dialogType == 0){
				CAMPAIGN_TEMPLATE_dialogBody = $('#productDialog').html();
			}else if(dialogType == -1){
				CAMPAIGN_TEMPLATE_dialogBody = $('#promotionDialog').html();
			}else{
				CAMPAIGN_TEMPLATE_dialogBody = $('#cateDialog').html();
			}
			if(csrftoken != null){
				var token = csrftoken;
			}else{
				var token = getParentToken();
			}
			var proAndprmPopParam = {
					thisObj : _this,
					  index : 1,
				  checkType : "checkbox",
				      modal : false,
				  autoClose : [],
				  dialogBody : CAMPAIGN_TEMPLATE_dialogBody,
				  csrftoken : token
			};
			var catePopParam = {
					thisObj : _this,
					  index : 1,
				  checkType : "checkbox",
				      modal : false,
				  autoClose : [],
				  dialogBody : CAMPAIGN_TEMPLATE_dialogBody,
				  csrftoken : token,
				teminalFlag : dialogType
			};
			if(dialogType == 0){
				popDataTableOfPrtInfo(proAndprmPopParam);
			}else if(dialogType == -1){
				popDataTableOfPrmInfo(proAndprmPopParam);
			}else{
				popDataTableOfCateInfo(catePopParam);
			}
		},
		


		/*
		 * 停用规则
		 */
		"stopRule" : function(suffix){
			$("#actionResultDisplay").empty();
			var _param = $("#brandSel").serialize();
			if ($("#memberClubId").length > 0) {
				_param += '&' + $("#memberClubId").serialize();
			}
			$("#ruleParams_" + suffix).find(":input").each(function() {
				_param = _param + '&' + encodeURIComponent(this.name)+'='+
				encodeURIComponent($.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"')));
			});
			$("#comParams").find(":input").each(function() {
				_param = _param + '&'+encodeURIComponent(this.name)+'='+
				encodeURIComponent($.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"')));
			});
			var url = $("#validUrl").attr("href");
			var dialogSetting = {
					dialogInit: "#dialogInit",
					text: $("#testMes").html(),
					width: 	500,
					height: 300,
					title: 	$("#title").text(),
					confirm: $("#sure").text(),
					cancel: $("#cancel").text(),
					confirmEvent: function(){CAMPAIGN_TEMPLATE.validHandle(url, _param);},
					cancelEvent: function(){removeDialog("#dialogInit");}
				};
			openDialog(dialogSetting);
		},

		/*
		 * 停用处理事件
		 */
		"validHandle" : function(url,param){
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : function(msg){
					removeDialog("#dialogInit");
					window.search();
				}
			});
		},
		
		/*
		 * 弹出新画面
		 */
		"popupWin" : function(obj, optKbn, copyFlag, suffix, configKbn) {
			$("#actionResultDisplay").empty();
			var m = [];
			// 品牌
			var brandInfo = CAMPAIGN_TEMPLATE.convertParam($("#brandSel").attr("name"), $("#brandSel").val());
			m.push(brandInfo);
			if ($("#memberClubId").length > 0) {
				m.push(CAMPAIGN_TEMPLATE.convertParam($("#memberClubId").attr("name"), $("#memberClubId").val()));
			}
			$("#comParams").find(":input").each(function() {
				var param = CAMPAIGN_TEMPLATE.convertParam(this.name, this.value);
				m.push(param);
			});
			$("#addComParams_" + suffix).find(":input").each(function() {
				var param = CAMPAIGN_TEMPLATE.convertParam(this.name, this.value);
				m.push(param);
			});
			// 编辑处理
			if (2 == optKbn) {
				$("#ruleParams_" + suffix).find(":input").each(function() {
					var param = CAMPAIGN_TEMPLATE.convertParam(this.name, this.value);
					m.push(param);
				});
			}
			var campParams = $("#campType").serialize() + '&' + $("#brandSel").serialize()
			+ "&optKbn=" + optKbn +  "&copyFlag=" + copyFlag + "&campParamInfo=" + "{" + m.toString() + "}";
			if ($("#memberClubId").length > 0) {
				campParams += '&' + $("#memberClubId").serialize();
			}
			if(configKbn){
				$("#configKbn").val(configKbn);
				campParams = campParams + '&' + $("#configKbn").serialize() + '&' + $("#campaignType").serialize() + "&confEditKbn=1";
			}
			var url = $(obj).attr("href") + "?" + campParams;
			openWinByUrl(url,{childModel:3});
		},
		
		"move" : function (obj, flag, tableId){
			var tHtml = '';
			var mHtml = '';
			if($("#" + tableId).find("tr").length > 1){
				if(flag == 0){
					mHtml = $(obj).parent().parent().html();
					$(obj).parent().parent().remove();
					var thHtml = $("#" + tableId).find(".thClass").html();
					$("#" + tableId).find(".thClass").remove();
					tHtml = $("#" + tableId).html();
					$("#" + tableId).empty();
					$("#" + tableId).html('<tr class="thClass">' + thHtml + '</tr><tr>' + mHtml + '</tr>');
					$("#" + tableId).append(tHtml);
				}else if(flag == 1){
					tHtml = $(obj).parent().parent().prev().html();
					mHtml = $(obj).parent().parent().html();
					$(obj).parent().parent().prev().html(mHtml);
					$(obj).parent().parent().html(tHtml);
				}else if(flag == 2){
					tHtml = $(obj).parent().parent().next().html();
					mHtml = $(obj).parent().parent().html();
					$(obj).parent().parent().next().html(mHtml);
					$(obj).parent().parent().html(tHtml);
				}else if(flag == 3){
					mHtml = $(obj).parent().parent().html();
					$(obj).parent().parent().remove();
					$("#" + tableId).append('<tr>' + mHtml + '</tr>');
				}
				CAMPAIGN_TEMPLATE.arrowsFlag(tableId);
			}
		},
		
		"arrowsFlag" : function (tableId){
			var trLength = $("#" + tableId).find("tr").not(".thClass").length;
			var trPriorityLength = $("#rulePriorityTable").find("tr").not(".thClass").length;
			var msgHtml = '';
			$("#rulePriorityTable").find("tr").not(".thClass").each(function (i){
				if(trPriorityLength != 1){
					if(i == 0){
						msgHtml = '<span style="height:16px; width:16px; display:block;" class="left"></span><span style="height:16px; width:16px; display:block;" class="left"></span><span class="arrow-down left" onClick="CAMPAIGN_TEMPLATE.move(this,2,' + "'rulePriorityTable'" + ');return false;"></span><span class="arrow-last left" onClick="CAMPAIGN_TEMPLATE.move(this,3,' + "'rulePriorityTable'" + ');return false;"></span>';
					}else if(i > 0 && i < trPriorityLength - 1){
						msgHtml = '<span class="arrow-first left" onClick="CAMPAIGN_TEMPLATE.move(this,0,' + "'rulePriorityTable'" + ');return false;"></span><span class="arrow-up left" onClick="CAMPAIGN_TEMPLATE.move(this,1,' + "'rulePriorityTable'" + ');return false;"></span><span class="arrow-down left" onClick="CAMPAIGN_TEMPLATE.move(this,2,' + "'rulePriorityTable'" + ');return false;"></span><span class="arrow-last left" onClick="CAMPAIGN_TEMPLATE.move(this,3,' + "'rulePriorityTable'" + ');return false;"></span>';
					}else{
						msgHtml = '<span class="arrow-first left" onClick="CAMPAIGN_TEMPLATE.move(this,0,' + "'rulePriorityTable'" + ');return false;"></span><span class="arrow-up left" onClick="CAMPAIGN_TEMPLATE.move(this,1,' + "'rulePriorityTable'" + ');return false;"></span>';
					}
					$(this).find("td").eq(4).html(msgHtml);
					$(this).find("td").eq(0).html(i + 1);
				}else{
					$(this).find("td").eq(4).html("");
					$(this).find("td").eq(0).html(1);
				}
			});
			if(tableId == 'rulePriorityDotConTable'){
				$("#" + tableId).find("tr").not(".thClass").each(function (i){
					$(this).find("td").eq(4).html("");
					$(this).find("td").eq(0).html(i + 1);
				});
			}
		},
		
		"saveRuleJson" : function (){
			var param = "";
			var tokenVal = parentTokenVal();
			$("#parentCsrftoken").val(tokenVal);
			$("#priorityPage").find(":input").each(function (){
				if(!($(this).is(':radio')) || $(this).is(":radio[checked]")){
					param = param + '&' + $(this).serialize();
				}
			});
			var url = $("#saveRuleConUrl").attr("href");
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : function(msg){
					var dialogSetting = {
							dialogInit: "#dialogInit",
							text: "<p class='message'><span>" + msg +"</span></p>",
							width: 	300,
							height: 250,
							title: 	$("#title").text(),
							confirm: $("#sure").text(),
							confirmEvent: function(){
								removeDialog("#dialogInit");
								window.close();
								window.opener.unlockParentWindow();
								window.opener.search();
							},
							closeEvent: function(){
								removeDialog("#dialogInit");
								window.close();
								window.opener.unlockParentWindow();
								window.opener.search();
							}
						};
					openDialog(dialogSetting);
				}
			});
		},
		
		"gotoConfig" : function (){
			var tokenVal = parentTokenVal();
			$("#parentCsrftoken").val(tokenVal);
			var m = [];
			$("#campaignInfo").find(":input").each(function (){
				var param = CAMPAIGN_TEMPLATE.convertParam(this.name, this.value);
				m.push(param);
			});
			$("#campParamInfo").val("{" + m.toString() + "}");
			$("#confEditKbn").val("1");
			$("#toNextForm").submit();
		}
};

var CAMPAIGN_TEMPLATE = new CAMPAIGN_TEMPLATE_GLOBAL();

function selectProduct(){
	// 取得选择的产品
	if($('#prt_dataTable').is(":visible")){
		var selectedProArr = $('#prt_dataTable input:checked');
	}else if($('#cate_dataTable').is(":visible")){
		var selectedProArr = $('#cate_dataTable input:checked');
	}else if($('#prm_dataTable').is(":visible")){
		var selectedProArr = $('#prm_dataTable input:checked');
	}
	// 已存在的产品
	var hasProArr = [];
	$("#prtSel_" + CAMPAIGN_TEMPLATE.selPrtIndex).find(".span_BASE000001").each(function (){
		var p = [];
		$(this).find(':input').each(function (){
			p.push('"'+ this.name +'":"'+
					$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
		});
		hasProArr.push("{" + p.toString() + "}");
	});
	// 添加产品
	for (var i=0;i<selectedProArr.length;i++){
		var flg = true;
		var $selectedPro = $(selectedProArr[i]);
		var selectedProInfoArr = window.JSON2.parse($selectedPro.val());
		if(selectedProInfoArr.cateFlag == "1"){
			// 产品分类全称
			var selectedCateId = selectedProInfoArr.cateId;
			var selectedCateName = selectedProInfoArr.cateName;
			var selectedCateFlag = selectedProInfoArr.cateFlag;
		}else{
			// 产品全称
			var selectedProName = selectedProInfoArr.nameTotal;
			// 产品id
			var selectedProId = selectedProInfoArr.proId;
		}
		for(var j=0;j<hasProArr.length;j++){
			var hasProInfoArr = hasProArr[j];
			hasProInfoArr = window.JSON2.parse(hasProInfoArr);
			if(selectedProInfoArr.cateFlag == "1"){
				// 产品分类全称
				var hasCateId = hasProInfoArr.cateId;
				if(selectedCateId == hasCateId){
					flg = false;
					break;
				}
			}else{
				// 产品id
				var hasProId = hasProInfoArr.proId;
				if(selectedProId == hasProId){
					flg = false;
					break;
				}
			}
		}
		// 该产品不存在时，添加产品
		if(flg){
			var inputHtml = "";
			if(selectedProInfoArr.cateFlag == "1"){
				inputHtml = '<input type="hidden" name="cateId" value="'+ selectedCateId + '" />' +
		           		'<input type="hidden" name="cateName" value="' + selectedCateName + '" />' +
			           '<input type="hidden" name="cateFlag" value="' + selectedCateFlag + '" />' +
			           '<span style="margin:0px 5px;">' + selectedCateName + '</span>';
			}else{
				inputHtml = '<input type="hidden" name="proId" value="' + selectedProId + '" />' +
						'<input type="hidden" name="nameTotal" value="' + selectedProName + '" />' +
			           '<span style="margin:0px 5px;">' + selectedProName + '</span>';
			}
			var span = '<span class="span_BASE000001">' + inputHtml + '<span class="close" onclick="CAMPAIGN_TEMPLATE_delete(this);return false;"><span class="ui-icon ui-icon-close"></span></span>' +
					   '</span></span>';
			$(".showPro_" + CAMPAIGN_TEMPLATE.selPrtIndex).append(span);
		}
	}
	if($("#prtSel_" + CAMPAIGN_TEMPLATE.selPrtIndex).find(".span_BASE000001").length == 1){
		$("#prtSel_" + CAMPAIGN_TEMPLATE.selPrtIndex).find('.close' , '.span_BASE000001').hide();
	}else{
		$("#prtSel_" + CAMPAIGN_TEMPLATE.selPrtIndex).find('.close' , '.span_BASE000001').show();
	}
	if($("#cateDialog").is(":visible")){
		// 关闭弹出框
		closeCherryDialog('cateDialog',CAMPAIGN_TEMPLATE_dialogBody);
	}else if($("#promotionDialog").is(":visible")){
		// 关闭弹出框
		closeCherryDialog('promotionDialog',CAMPAIGN_TEMPLATE_dialogBody);
	}else{
		// 关闭弹出框
		closeCherryDialog('productDialog',CAMPAIGN_TEMPLATE_dialogBody);
	}
	oTableArr[1]= null;	
}

function CAMPAIGN_TEMPLATE_delete(obj){
//	if($(obj).parent().parent().find(".span_BASE000001").length == 2){
//		$(obj).parent().parent().find('.close' , '.span_BASE000001').hide();
//	}
	// 删除产品
	$(obj).parent().remove();
}

function CAMPAIGN_TEMPLATE_openTable(obj) {
	if($(obj).parents(".proClass").length > 0){
		var $obj = $(obj).parents(".proClass").first().find("#choicePro");
		TEMP001.showTable($obj);
	}else if($(obj).parents(".box4-content").first().find("#choicePro").length > 0){
		var $obj = $(obj).parents(".box4-content").first().find("#choicePro");
		TEMP002.showTable($obj);
	}else if($(obj).parents(".cond-context").first().find("#buyPro").length > 0){
		var $obj = $(obj).parents(".cond-context").first().find("#buyPro");
		TEMP000.showTable($obj);
	}else if($(obj).parents(".prtClass").first().find("#choicePointPro").length > 0){
		var $obj = $(obj).parents(".prtClass").first().find("#choicePointPro");
		TEMP002.showTable($obj, 0);
	}else if($(obj).parents(".prmClass").first().find("#choicePrmPro").length > 0){
		var $obj = $(obj).parents(".prmClass").first().find("#choicePrmPro");
		TEMP002.showTable($obj, -1, '1');
	}
}
