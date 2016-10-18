/**
 * @author user3
 */

function BINOLMOCIO10() {
};

BINOLMOCIO10.prototype = {

	"dialogHtml" : "",
	"groupArray":[],
	"questionArray":[],
	"addGroupFrequency":0,
	"paperLevelArray":[],
	"flag":"0",
	"setGroupArray":function(groupArray){
		this.groupArray = groupArray;
	},
	"setQuestionArray":function(questionArray){
		this.questionArray = questionArray;
	},
	"setAddGroupFrequency":function(addGroupFrequency){
		this.addGroupFrequency = addGroupFrequency;
	},
	"setPaperLevelArray":function(paperLevelArray){
		this.paperLevelArray = paperLevelArray;
	},
	"setFlag":function(flag){
		this.flag = flag;
	},
	"openAddGroupDialog" : function(dialogParent, dialogDiv, dialogTitle
			,displayOrder) {
		this.recordDialogHtml(dialogDiv);
		this.setAddGroupDialog(dialogParent, dialogDiv, dialogTitle,displayOrder);
		$("#" + dialogDiv).dialog("open");
	},
	"setAddGroupDialog" : function(dialogParent, dialogDiv, dialogTitle
			,displayOrder) {
		var that = this;
		$("#" + dialogDiv).dialog( {
			autoOpen : false,
			draggable : true,
			resizable : false,
			modal : true,
			title : dialogTitle,
			height : 170,
			width : 380,
			buttons : [ {
				text : BINOLMOCIO10_js_i18n.save,
				click : function() {
					that.saveGroup(displayOrder);
					that.closeDialog(dialogParent, dialogDiv);
				}
			} ],
			close : function(event, ui) {
				that.closeDialog(dialogParent, dialogDiv);
			}
		});
	},
	"closeDialog" : function(dialogParent, dialogDiv) {
		var parentDiv = $("#" + dialogDiv).parent().attr("id");
		$("#" + dialogDiv).dialog("destroy");
		$("#" + dialogDiv).remove();
		$("#" + dialogParent).html(this.dialogHtml);
	},
	"recordDialogHtml" : function(dialogDiv) {
		var parentDiv = $("#" + dialogDiv).parent().attr("id");
		this.dialogHtml = $("#" + parentDiv).html();
	},
	"saveGroup":function(ediGrouDisOrder){
		if(ediGrouDisOrder == undefined){
			var groupNum = this.addGroupFrequency;
			var displayOrder = Number(this.groupArray.length)+1;
			var o ={
					groupName:$.trim($("#addGroup_dialog #groupName").val()),
					displayOrder:displayOrder,
					index:groupNum
			};
			this.groupArray.push(o);
		}else{
			var this_index = Number(ediGrouDisOrder)-1;
			this.groupArray[this_index].groupName = $.trim($("#addGroup_dialog #groupName").val());
		}
		this.addGroupFrequency++;
		this.showGroup();
	},
	"showGroup":function(){
		$("#alreadyAddQuestion_body").html("");
		var base_html = $("#showGroup_main").html();
		for(var idx in this.groupArray){
			if(idx != "remove"){
				var that = this;
				var o = this.groupArray[idx];
				var displayOrder = o.displayOrder;
				var up_displayOrder = Number(o.displayOrder)-1;
				if(displayOrder == 1){
					$("#alreadyAddQuestion_body").append(base_html);
					$("#alreadyAddQuestion_body").children().last().attr("id","group_"+displayOrder);
				}else if(displayOrder > 1){
					$("#alreadyAddQuestion_body").append(base_html);
					$("#alreadyAddQuestion_body").children().last().attr("id","group_"+displayOrder);
					$("#group_"+up_displayOrder).find("#moveDown").html('<a class="left" style="cursor:pointer" title='+BINOLMOCIO10_js_i18n.moveDown+'><span class="arrow-down"></span></a>');
					$("#group_"+displayOrder).find("#moveUp").html('<a class="left" style="cursor:pointer" title='+BINOLMOCIO10_js_i18n.moveUp+'><span class="arrow-up"></span></a>');
				}
				$("#group_"+displayOrder).find("strong").html(BINOLMOCIO10_js_i18n.makeGroup+displayOrder+'：'+o.groupName.escapeHTML());
				$("#group_"+displayOrder).find("input").attr("id",displayOrder);
				$("#group_"+displayOrder).find("#edit").click(function(){
					that.editGroup($(this).parent().prev().prev().attr("id"));
				});
				$("#group_"+displayOrder).find("#delete").click(function(){
					that.deleteGroup($(this).parent().prev().prev().attr("id"));
				});
				$("#group_"+displayOrder).find("#moveUp").click(function(){
					that.moveUpGroup($(this).parent().prev().prev().attr("id"));
				});
				$("#group_"+displayOrder).find("#moveDown").click(function(){
					that.moveDownGroup($(this).parent().prev().prev().attr("id"));
				});
				
				$("#group_"+displayOrder).find("#optionDisplay").click(function(){
					$(this).prev().prop("checked",true);
					if($(this).parent().next().is(":hidden")){
						$(this).parent().next().show();
						var id = $(this).parent().parent().attr("id");
						var groDisOrder = id.split("_")[1];
						that.showQuestion(groDisOrder);
					}else{
						$(this).parent().next().html("");
						$(this).parent().next().hide();
					}
				});
			}
		}
	},
	"deleteGroup":function(displayOrder){
		var this_index = Number(displayOrder)-1;
		var delete_Question = [];
		for(var key in this.questionArray){
			if(key != "remove" && this.questionArray[key] != undefined){
				if(this.questionArray[key].checkQuestionGroupId == this.groupArray[this_index].index){
					delete_Question.push(key);
				}
			}
		}
		this.groupArray.remove(this_index);
		for(var index in this.groupArray){
			if(index != "remove"){
				this.groupArray[index].displayOrder = Number(index)+1;
			}
		}
		for(var k in delete_Question){
			if(k != "remove"){
				var dx = delete_Question[k];
				this.questionArray[dx] = undefined;
			}
		}
		this.showGroup();
		this.calculationMaxPoint();
	},
	"editGroup":function(displayOrder){
		var this_index = Number(displayOrder)-1;
		var groupName = this.groupArray[this_index].groupName;
		this.openAddGroupDialog("addGroup_main","addGroup_dialog",BINOLMOCIO10_js_i18n.addGroupTitle,displayOrder);
		$("#addGroup_dialog #groupName").val(groupName);		
	},
	"moveUpGroup":function(displayOrder){
		if(displayOrder == "1"){
			return;
		}else{
			var this_index = displayOrder -1;
			this.groupArray[this_index].displayOrder = displayOrder - 1;
			this.groupArray[this_index - 1].displayOrder = displayOrder;
			var o = this.groupArray[this_index];
			this.groupArray[this_index] = this.groupArray[this_index - 1];
			this.groupArray[this_index - 1] = o;
			this.showGroup();
		}
	},
	"moveDownGroup":function(displayOrder){
		if(displayOrder == this.groupArray.length){
			return;
		}else{
			var this_index = displayOrder - 1;
			this.groupArray[this_index].displayOrder = Number(displayOrder) + 1;
			this.groupArray[Number(this_index) + 1].displayOrder = displayOrder;
			var o = this.groupArray[this_index];
			this.groupArray[this_index] = this.groupArray[Number(this_index) + 1];
			this.groupArray[Number(this_index) + 1] = o;
			this.showGroup();
		}
	},	
	"openWarmDialog":function(dialogParent, dialogDiv, dialogTitle) {
		this.recordDialogHtml(dialogDiv);
		this.setWarmDialog(dialogParent, dialogDiv, dialogTitle);
		$("#" + dialogDiv).dialog("open");
	},
	"setWarmDialog":function(dialogParent, dialogDiv, dialogTitle) {
		var that = this;
		$("#" + dialogDiv).dialog( {
			autoOpen : false,
			draggable : true,
			resizable : false,
			modal : true,
			title : dialogTitle,
			height : 200,
			width : 300,
			buttons : [ {
				text : BINOLMOCIO10_js_i18n.btnOk,
				click : function() {
					that.closeDialog(dialogParent, dialogDiv);
				}
			} ],
			close : function(event, ui) {
				that.closeDialog(dialogParent, dialogDiv);
			}
		});
	},
	"showWarmDialog":function(warmMessageId,showMessage) {
		this.openWarmDialog("warmDialogInit_main","warmDialogInit",BINOLMOCIO10_js_i18n.tipTitle);
		$("#"+warmMessageId).append(showMessage);
	},
	"openAddQuestionDialog":function(dialogParent, dialogDiv, dialogTitle,questionType,displayOrder,groDisOrder,editFlag){
		var that = this;
		if(this.groupArray.length == 0){
			this.showWarmDialog("warmMessage",BINOLMOCIO10_js_i18n.noGroupWarm);
		}else if(($("#alreadyAddQuestion_body").find("input[name='group']:checked").length == 1)|| (this.groupArray.length == 0)){
			this.recordDialogHtml(dialogDiv);
			this.setAddQuestionDialog(dialogParent, dialogDiv, dialogTitle,questionType,displayOrder,groDisOrder,editFlag);
			$("#addQuestion_dialog").dialog("open");
			if(questionType == "3" || questionType == "1"){
				$("#addQuestion_dialog").find("#choiceOption").hide();
			}else{
				$("#addQuestion_dialog").find("#addChoice").click(function(){
					that.addChoice();
				});
				$("#addQuestion_dialog").find("#deleteChoice").click(function(){
					that.deleteChoice();
				});
			}
//			$("#addQuestion_dialog #questionItem").keyup(function(){
//				that.limitStringLength(this);
//			});
//			$("#addQuestion_dialog #questionItem").bind('paste',function(){
//				window.setTimeout('$(\'#addQuestion_dialog #' + this.id + '\').trigger(\'keyup\')', 1);
//			});
		}else{
			this.showWarmDialog("warmMessage",BINOLMOCIO10_js_i18n.uncheckGroupWarm);
		}
	},
	"setAddQuestionDialog":function(dialogParent, dialogDiv, dialogTitle,questionType,displayOrder,groDisOrder,editFlag){
		var that = this;
		$("#addQuestion_dialog").dialog({
			autoOpen : false,
			draggable : true,
			resizable : false,
			modal : true,
			title : dialogTitle,
			height : 378,
			width : 600,
			buttons : [ {
				text : editFlag? BINOLMOCIO10_js_i18n.save:BINOLMOCIO10_js_i18n.nextQuestion,
				click : function() {
					that.saveQuestion(questionType,displayOrder,groDisOrder);
				}
			} ],
			close : function(event, ui) {
				that.closeDialog(dialogParent, dialogDiv);
			}
		});
	},
	"addChoice":function(){
		var choiceNum = $("#addQuestion_dialog").find("input[id^=option]").length;
		var asciiCode = 65+Number(choiceNum);
		var char = String.fromCharCode(asciiCode);
		var html = '<div class="clearfix" style="margin:0px 10px 5px 0px" id="choice_'+char+'"><input type="checkbox" id="'+char+'"/><label>'+char+'</label>：<input type="text" class="text" id="option'+char+'" value="" style="width:350px" maxlength="30"/>'+BINOLMOCIO10_js_i18n.point1+'：<input type="text" class="text" id="point'+char+'" value="" maxlength="5" style="width:30px;height:15px"/></div>';
		$("#addQuestion_dialog").find("#optionDiv").append(html);
	},
	"deleteChoice":function(){
		var asciiCode = 65;
		$("#addQuestion_dialog").find("input:checked").each(function(){
			$(this).parent().remove();
		});
		$("#addQuestion_dialog").find("div[id^=choice_]").each(function(){
			var char = String.fromCharCode(asciiCode);
			$(this).attr("id","choice_"+char);
			$(this).find("input[type=checkbox]").attr("id",char);
			$(this).find("label").html(char);
			$(this).find("input[id^=option]").attr("id","option"+char);
			$(this).find("input[id^=point]").attr("id","point"+char);
			asciiCode++;
		});
	},
	"saveQuestion":function(questionType,disOrder,groDisplayOrder){
		var flag1 = true;
		var flag2 = true;
		var that = this;
		var choicePointSum = 0;
		if($.trim($("#addQuestion_dialog #questionItem").val()) == ""){
			$('#errorSpan1').html($('#errmsg1').val());
			$("#errorDiv1").show();
			return;
		}else{
			$('#errorSpan1').html("");
			$("#errorDiv1").hide();
		}
		try{
			if(questionType == "0" || questionType == "2"){
				if($("#addQuestion_dialog").find("input[id^=option]").length == 0){
					throw "ex";
				}
				$("#addQuestion_dialog").find("input[id^=option]").each(function(){
					if($.trim($(this).val()) == ""){
						throw "ex";
					}
				});
			}
		}catch(e){
			$('#errorSpan1').html($('#errmsg7').val());
			$("#errorDiv1").show();
			return;
		}
		$("#addQuestion_dialog").find("input[id^=point]").each(function(){
			if($.trim($(this).val()) == ""){
				flag1 = false;
			}else if(that.isPositiveFloat(Number($.trim($(this).val())).toString()) == false){
				flag2 = false;
			}else{
				choicePointSum = choicePointSum + Number($.trim($(this).val()));
			}
		});
		if($.trim($("#addQuestion_dialog #maxPoint").val()) == "" || $.trim($("#addQuestion_dialog #minPoint").val()) == "" || flag1 == false){
			$('#errorSpan2').html($('#errmsg3').val());
			$("#errorDiv2").show();
			return;
		}else{
			$('#errorSpan2').html("");
			$("#errorDiv2").hide();
		}
		if(that.isPositiveFloat(Number($.trim($("#addQuestion_dialog #maxPoint").val())).toString()) == false || that.isPositiveFloat(Number($.trim($("#addQuestion_dialog #minPoint").val())).toString()) == false || flag2 == false){
			$('#errorSpan1').html($('#errmsg4').val());
			$("#errorDiv1").show();
			return;
		}else{
			$('#errorSpan1').html("");
			$("#errorDiv1").hide();
		}
		if(Number($.trim($("#addQuestion_dialog #maxPoint").val())) < Number($.trim($("#addQuestion_dialog #minPoint").val()))){
			$('#errorSpan1').html($('#errmsg2').val());
			$("#errorDiv1").show();
			return;
		}else{
			$('#errorSpan1').html("");
			$("#errorDiv1").hide();
		}
//		if(Number($.trim($("#addQuestion_dialog #maxPoint").val())) < choicePointSum){
//			$('#errorSpan1').html($('#errmsg6').val());
//			$("#errorDiv1").show();
//			return;
//		}else{
//			$('#errorSpan1').html("");
//			$("#errorDiv1").hide();
//		}
		if(groDisplayOrder == undefined){
			var displayOrder = 1;
			if(this.groupArray.length > 0){
				var groDisOrder = $("#alreadyAddQuestion_body").find("input[type=radio]:checked")[0].id;
				var checkQuestionGroupId = "";
				for(var key in this.groupArray){
					if(this.groupArray[key].displayOrder == groDisOrder){
						checkQuestionGroupId =  this.groupArray[key].index;
					}
				}
				for(var i in this.questionArray){
					if(this.questionArray[i]!=undefined && this.questionArray[i].checkQuestionGroupId == checkQuestionGroupId){
						displayOrder ++;
					}
				}
				var o = {
						questionItem:$.trim($("#addQuestion_dialog #questionItem").val()),
						maxPoint:Number($.trim($("#addQuestion_dialog #maxPoint").val())),
						minPoint:Number($.trim($("#addQuestion_dialog #minPoint").val())),
						checkQuestionGroupId:checkQuestionGroupId,
						displayOrder:displayOrder,
						questionType:questionType
				};
				if(questionType == "0" || questionType == "2"){
					$("#addQuestion_dialog").find("input[id^=option]").each(function(){
						o[$(this).attr("id")] = $.trim($(this).val());
					});
					$("#addQuestion_dialog").find("input[id^=point]").each(function(){
						o[$(this).attr("id")] = Number($.trim($(this).val()));
					});
				}
				this.questionArray.push(o);
				this.showQuestion(groDisOrder);
				this.clearAddQuestionDialog();
			}else{
				if(disOrder == undefined){
					for(var i in this.questionArray){
						if(this.questionArray[i]!=undefined && i !="remove"){
							displayOrder ++;
						}
					}
//					displayOrder = this.questionArray.length + Number(displayOrder);
					var o = {
							questionItem:$.trim($("#addQuestion_dialog #questionItem").val()),
							maxPoint:Number($.trim($("#addQuestion_dialog #maxPoint").val())),
							minPoint:Number($.trim($("#addQuestion_dialog #minPoint").val())),
							displayOrder:displayOrder,
							questionType:questionType
					};
					if(questionType == "0" || questionType == "2"){
						$("#addQuestion_dialog").find("input[id^=option]").each(function(){
							o[$(this).attr("id")] = $.trim($(this).val());
						});
						$("#addQuestion_dialog").find("input[id^=point]").each(function(){
							o[$(this).attr("id")] = Number($.trim($(this).val()));
						});
					}
					this.questionArray.push(o);
					this.showQuestion();
					this.clearAddQuestionDialog();
				}else{
					var o = {
							questionItem:$.trim($("#addQuestion_dialog #questionItem").val()),
							maxPoint:Number($.trim($("#addQuestion_dialog #maxPoint").val())),
							minPoint:Number($.trim($("#addQuestion_dialog #minPoint").val())),
							displayOrder:disOrder,
							questionType:questionType
					};
					if(questionType == "0" || questionType == "2"){
						$("#addQuestion_dialog").find("input[id^=option]").each(function(){
							o[$(this).attr("id")] = $.trim($(this).val());
						});
						$("#addQuestion_dialog").find("input[id^=point]").each(function(){
							o[$(this).attr("id")] = Number($.trim($(this).val()));
						});
					}
					for(var m in this.questionArray){
						if(m != "remove" && this.questionArray[m] != undefined){
							if(this.questionArray[m].displayOrder == disOrder){
								this.questionArray[m] = o;
							}
						}
					}
					this.showQuestion();
					this.closeDialog("addQuestion_dialog_main", "addQuestion_dialog");
				}
			}
			
		}else{
			var this_groupIndex = 0;
			for(var k in this.groupArray){
				if(this.groupArray[k].displayOrder == groDisplayOrder){
					this_groupIndex = this.groupArray[k].index;
				}
			}
			var temp_obj ={
					questionItem:$.trim($("#addQuestion_dialog #questionItem").val()),
					maxPoint:Number($.trim($("#addQuestion_dialog #maxPoint").val())),
					minPoint:Number($.trim($("#addQuestion_dialog #minPoint").val())),
					checkQuestionGroupId:this_groupIndex,
					displayOrder:disOrder,
					questionType:questionType
			};
			if(questionType == "0" || questionType == "2"){
				$("#addQuestion_dialog").find("input[id^=option]").each(function(){
					temp_obj[$(this).attr("id")] = $.trim($(this).val());
				});
				$("#addQuestion_dialog").find("input[id^=point]").each(function(){
					temp_obj[$(this).attr("id")] = Number($.trim($(this).val()));
				});
			}
			for(var l in this.questionArray){
				if(this.questionArray[l]!=undefined && this.questionArray[l].checkQuestionGroupId == this_groupIndex && this.questionArray[l].displayOrder == disOrder){
					this.questionArray[l] = temp_obj;
				}
			}
			this.closeDialog("addQuestion_dialog_main", "addQuestion_dialog");
			this.showQuestion(groDisplayOrder);
		}
		this.calculationMaxPoint();
	},
	"clearAddQuestionDialog":function(){
		$("#addQuestion_dialog #questionItem").val("");
		$("#addQuestion_dialog #maxPoint").val("");
		$("#addQuestion_dialog #minPoint").val("");
		$("#addQuestion_dialog #optionDiv").html("");
	},
	"showQuestion":function(groupDisPlaOrder){
		var that = this;
		var str="";
		if(groupDisPlaOrder != undefined){
			var groupDivId = "group_"+groupDisPlaOrder;
			var checkQuestionGroupId = 0;
			var tempArray = [];
			for(var key in this.groupArray){
				if(this.groupArray[key].displayOrder == groupDisPlaOrder){
					checkQuestionGroupId =  this.groupArray[key].index;
				}
			}
			for(var i in this.questionArray){
				if(this.questionArray[i] != undefined){
					if(this.questionArray[i].checkQuestionGroupId == checkQuestionGroupId){
						tempArray.push(this.questionArray[i]);
					}
				}
			}
			$("#"+groupDivId+" #alreadyAddQuestion").html("");
			for(var j in tempArray){
				var this_temp_displayOrder = tempArray[j].displayOrder;
				var up_this_displayOrder = Number(tempArray[j].displayOrder)-1;
				if( j != "remove"){
					if(this_temp_displayOrder == 1){
						str = '<div style="border-bottom: 1px dashed #ccc; padding-bottom: 5px; margin-bottom: 10px;" class="clearfix"><div class="left" style="width:430px"><span id='+tempArray[j].displayOrder+' style="cursor:default"><span class="highlight">'+tempArray[j].displayOrder+'</span>：'+tempArray[j].questionItem.escapeHTML()+'</span></div><div class="right"><a class="left" id="edit_'+tempArray[j].displayOrder+'_'+tempArray[j].questionType+'_'+tempArray[j].checkQuestionGroupId+'" title='+BINOLMOCIO10_js_i18n.editQ+' style="cursor:pointer"><span class="ui-icon icon-edit"></span></a><a class="left" id="delete_'+tempArray[j].displayOrder+'" title='+BINOLMOCIO10_js_i18n.deleteQ+' style="cursor:pointer"><span class="ui-icon icon-delete"></span></a><span id="moveUp_'+tempArray[j].displayOrder+'"><span class="left" style="height:16px; width:16px; display:block;"></span></span><span id="moveDown_'+tempArray[j].displayOrder+'"><span class="left" style="height:16px; width:16px; display:block;"></span></span></div></div>';
					}else if(this_temp_displayOrder > 1){
						$("#alreadyAddQuestion").find("#moveDown_"+up_this_displayOrder).html('<a class="left" title='+BINOLMOCIO10_js_i18n.moveDown+' style="cursor:pointer"><span class="arrow-down"></span></a>');
						str = '<div style="border-bottom: 1px dashed #ccc; padding-bottom: 5px; margin-bottom: 10px;" class="clearfix"><div class="left" style="width:430px"><span id='+tempArray[j].displayOrder+' style="cursor:default"><span class="highlight">'+tempArray[j].displayOrder+'</span>：'+tempArray[j].questionItem.escapeHTML()+'</span></div><div class="right"><a class="left" id="edit_'+tempArray[j].displayOrder+'_'+tempArray[j].questionType+'_'+tempArray[j].checkQuestionGroupId+'" title='+BINOLMOCIO10_js_i18n.editQ+' style="cursor:pointer"><span class="ui-icon icon-edit"></span></a><a class="left" id="delete_'+tempArray[j].displayOrder+'" title='+BINOLMOCIO10_js_i18n.deleteQ+' style="cursor:pointer"><span class="ui-icon icon-delete"></span></a><span id="moveUp_'+tempArray[j].displayOrder+'"><a class="left" title='+BINOLMOCIO10_js_i18n.moveUp+' style="cursor:pointer"><span class="arrow-up"/></a></span><span id="moveDown_'+tempArray[j].displayOrder+'"><span class="left" style="height:16px; width:16px; display:block;"></span></span></div></div>';
					}else{
						;
					}
					$("#"+groupDivId+" #alreadyAddQuestion").append(str);
					$("#"+groupDivId+" #alreadyAddQuestion").find("#edit_"+tempArray[j].displayOrder+"_"+tempArray[j].questionType+"_"+tempArray[j].checkQuestionGroupId).click(function(){
						var id = $(this).attr("id");
						var idArr = id.split("_");
						var disOder = idArr[1];
						var quetype = idArr[2];
						var groDis = 1;
						for(var o in that.groupArray){
							if(o != "remove"){
								if(that.groupArray[o].index == idArr[3]){
									groDis = that.groupArray[o].displayOrder;
								}
							}
						}
						that.editQuestion(disOder,quetype,groDis);
					});
					$("#"+groupDivId+" #alreadyAddQuestion").find("#delete_"+tempArray[j].displayOrder).click(function(){
						var id = $(this).attr("id");
						var idArr = id.split("_");
						var disOder = idArr[1];
						that.deleteQuestion(disOder,groupDisPlaOrder);
					});
					$("#"+groupDivId+" #alreadyAddQuestion").find("#moveUp_"+tempArray[j].displayOrder).click(function(){
						var id = $(this).attr("id");
						var idArr = id.split("_");
						var disOder = idArr[1];
						that.moveUpQuestion(disOder,groupDisPlaOrder);
					});
					$("#"+groupDivId+" #alreadyAddQuestion").find("#moveDown_"+tempArray[j].displayOrder).click(function(){
						var id = $(this).attr("id");
						var idArr = id.split("_");
						var disOder = idArr[1];
						that.moveDownQuestion(disOder,groupDisPlaOrder);
					});
				}
			}
			$("#"+groupDivId+" #alreadyAddQuestion").show();
		}else{
			$("#alreadyAddQuestion_body").html("");
			for(var j in this.questionArray){
				if( j != "remove" && this.questionArray[j] != undefined){
					var str = '<div style="border-bottom: 1px dashed #ccc; padding-bottom: 5px; margin-bottom: 10px;" class="clearfix"><div class="left" style="width:430px"><span id='+this.questionArray[j].displayOrder+' style="cursor:default"><span class="highlight">'+this.questionArray[j].displayOrder+'</span>：'+this.questionArray[j].questionItem.escapeHTML()+'</span></div><div class="right"><a class="left" id="edit_'+this.questionArray[j].displayOrder+'_'+this.questionArray[j].questionType+'_'+this.questionArray[j].checkQuestionGroupId+'" title='+BINOLMOCIO10_js_i18n.edit+' style="cursor:pointer"><span class="ui-icon icon-edit"></span></a><a class="left" id="delete_'+this.questionArray[j].displayOrder+'" title='+BINOLMOCIO10_js_i18n.deleteButton+' style="cursor:pointer"><span class="ui-icon icon-delete"></span></a><a class="left" id="moveUp_'+this.questionArray[j].displayOrder+'" title='+BINOLMOCIO10_js_i18n.moveUp+' style="cursor:pointer"><span class="arrow-up"/></a><a class="left" id="moveDown_'+this.questionArray[j].displayOrder+'" title='+BINOLMOCIO10_js_i18n.moveDown+' style="cursor:pointer"><span class="arrow-down"></span></a></div></div>';
					$("#alreadyAddQuestion_body").append(str);
					$("#alreadyAddQuestion_body").find("#edit_"+this.questionArray[j].displayOrder+"_"+this.questionArray[j].questionType+"_"+this.questionArray[j].checkQuestionGroupId).click(function(){
						var id = $(this).attr("id");
						var idArr = id.split("_");
						var disOder = idArr[1];
						var quetype = idArr[2];
						that.editQuestion(disOder,quetype);
					});
					$("#alreadyAddQuestion_body").find("#delete_"+this.questionArray[j].displayOrder).click(function(){
						var id = $(this).attr("id");
						var idArr = id.split("_");
						var disOder = idArr[1];
						that.deleteQuestion(disOder);
					});
					$("#alreadyAddQuestion_body").find("#moveUp_"+this.questionArray[j].displayOrder).click(function(){
						var id = $(this).attr("id");
						var idArr = id.split("_");
						var disOder = idArr[1];
						that.moveUpQuestion(disOder);
					});
					$("#alreadyAddQuestion_body").find("#moveDown_"+this.questionArray[j].displayOrder).click(function(){
						var id = $(this).attr("id");
						var idArr = id.split("_");
						var disOder = idArr[1];
						that.moveDownQuestion(disOder);
					});
				}
			}
		}
	},
	"editQuestion":function(displayOrder,questionType,groupDisOrder){
//		this.openAddQuestionDialog(dialogParent, dialogDiv, dialogTitle, questionType, displayOrder,groupDisOrder);
		if(questionType == "0"){
			this.openAddQuestionDialog("addQuestion_dialog_main", "addQuestion_dialog", BINOLMOCIO10_js_i18n.editSinChioceTitle, questionType, displayOrder,groupDisOrder,"editFlag");
		}else if(questionType == "3"){
			this.openAddQuestionDialog("addQuestion_dialog_main", "addQuestion_dialog", BINOLMOCIO10_js_i18n.editEssayTitle, questionType, displayOrder,groupDisOrder,"editFlag");
		}else if(questionType == "2"){
			this.openAddQuestionDialog("addQuestion_dialog_main", "addQuestion_dialog", BINOLMOCIO10_js_i18n.editMulChioceTitle, questionType, displayOrder,groupDisOrder,"editFlag");
		}else{
			this.openAddQuestionDialog("addQuestion_dialog_main", "addQuestion_dialog", BINOLMOCIO10_js_i18n.editApfillTitle, questionType, displayOrder,groupDisOrder,"editFlag");
		}
		this.fillEditDialog(displayOrder,questionType,groupDisOrder);
	},
	"fillEditDialog":function(displayOrder,questionType,groupDisOrder){
		var this_groupIndex = 0;
		for(var k in this.groupArray){
			if(k != "remove"){
				if(this.groupArray[k].displayOrder == groupDisOrder){
					this_groupIndex = this.groupArray[k].index;
				}
			}
		}
		var temp_question = {};
		if(groupDisOrder != undefined){
			for(var l in this.questionArray){
				if(l != "remove" && this.questionArray[l] != undefined){
					if(this.questionArray[l].checkQuestionGroupId == this_groupIndex && this.questionArray[l].displayOrder == displayOrder){
						temp_question = this.questionArray[l];
					}
				}
			}
		}else{
			for(var l in this.questionArray){
				if(l != "remove" && this.questionArray[l] != undefined){
					if(this.questionArray[l].displayOrder == displayOrder){
						temp_question = this.questionArray[l];
					}
				}
			}
		}
		$("#addQuestion_dialog #questionItem").val(temp_question.questionItem);
		$("#addQuestion_dialog #maxPoint").val(temp_question.maxPoint);
		$("#addQuestion_dialog #minPoint").val(temp_question.minPoint);
		if(questionType == "0" || questionType == "2"){
			var assiiCode = 65;
			for(var j in temp_question){
				if(j.indexOf("option")>-1){
					var char = String.fromCharCode(assiiCode);
					var point = "point"+char;
					var html = '<div class="clearfix" style="margin:0px 10px 5px 0px" id="choice_'+char+'"><input type="checkbox" id="'+char+'"/><label>'+char+'</label>：<input type="text" class="text" id="option'+char+'" value="'+temp_question[j]+'" style="width:350px" maxlength="30"/>分值：<input type="text" class="text" id="point'+char+'" value="'+temp_question[point]+'" maxlength="5" style="width:30px;height:15px"/></div>';
					$("#addQuestion_dialog").find("#optionDiv").append(html);
					assiiCode++;
				}
			}
		}
	},
	"deleteQuestion":function(displayOrder,groupDisplayOrder){
		if(groupDisplayOrder != undefined){
			var checkQuestionGroupId = 0;
			for(var key in this.groupArray){
				if(this.groupArray[key].displayOrder == groupDisplayOrder){
					checkQuestionGroupId =  this.groupArray[key].index;
				}
			}
			var index = "";
			for(var m in this.questionArray){
				if(this.questionArray[m] != undefined && this.questionArray[m].checkQuestionGroupId == checkQuestionGroupId && this.questionArray[m].displayOrder == displayOrder){
					index = m;
				}
			}
			this.questionArray.remove(index);
			var newDisplayOrder = 1;
			for(var j in this.questionArray){
				if(j != "remove" &&this.questionArray[j] != undefined){
					if(this.questionArray[j].checkQuestionGroupId == checkQuestionGroupId){
						this.questionArray[j].displayOrder = newDisplayOrder;
						newDisplayOrder++;
					}
				}
			}
			this.showQuestion(groupDisplayOrder);
//			alert(JSON2.stringify(this.questionArray));
		}else{
			var index = "";
			for(var n in this.questionArray){
				if(this.questionArray[n] !=undefined && this.questionArray[n].displayOrder == displayOrder){
					index = n ;
				}
			}
			this.questionArray.remove(index);
			for(var k in this.questionArray){
				if(this.questionArray[k] != undefined){
					this.questionArray[k].displayOrder = Number(k)+1;
				}
			}
			this.showQuestion();
		}
		this.calculationMaxPoint();
	},
	"moveUpQuestion":function(displayOrder,groupDisplayOrder){
		if(displayOrder == "1"){
			return;
		}else{
			if(groupDisplayOrder != undefined){
			
				var checkQuestionGroupId = 0;
				for(var key in this.groupArray){
					if(this.groupArray[key].displayOrder == groupDisplayOrder){
						checkQuestionGroupId =  this.groupArray[key].index;
					}
				}
				var index = "";
				var cloneThisQuestion = {};
				for(var m in this.questionArray){
					if(this.questionArray[m] != undefined && this.questionArray[m].checkQuestionGroupId == checkQuestionGroupId && this.questionArray[m].displayOrder == displayOrder){
						index = m;
						cloneThisQuestion = this.questionArray[m];
					}
				}
				this.questionArray[index].displayOrder = this.questionArray[index - 1].displayOrder;
				this.questionArray[index - 1].displayOrder = displayOrder;
				this.questionArray[index] = this.questionArray[index - 1];
				this.questionArray[index - 1] = cloneThisQuestion;
				this.showQuestion(groupDisplayOrder);
			}else{
				var this_index = displayOrder -1;
				this.questionArray[this_index].displayOrder = displayOrder - 1;
				this.questionArray[this_index - 1].displayOrder = displayOrder;
				var o = this.questionArray[this_index];
				this.questionArray[this_index] = this.questionArray[this_index - 1];
				this.questionArray[this_index - 1] = o;
				this.showQuestion();
			}
		}
	},
	"moveDownQuestion":function(displayOrder,groupDisplayOrder){
		if(groupDisplayOrder != undefined){
			var tem_quetion = [];
			for(var key in this.groupArray){
				if(this.groupArray[key].displayOrder == groupDisplayOrder){
					checkQuestionGroupId =  this.groupArray[key].index;
				}
			}
			for(var m in this.questionArray){
				if(this.questionArray[m] != undefined && this.questionArray[m].checkQuestionGroupId == checkQuestionGroupId){
					tem_quetion.push(this.questionArray[m]);
				}
			}
			if(displayOrder == tem_quetion.length){
				return;
			}else{
				var index = "";
				var cloneThisQuestion = {};
				for(var m in this.questionArray){
					if(this.questionArray[m] != undefined && this.questionArray[m].checkQuestionGroupId == checkQuestionGroupId && this.questionArray[m].displayOrder == displayOrder){
						index = m;
						cloneThisQuestion = this.questionArray[m];
					}
				}
				this.questionArray[index].displayOrder = this.questionArray[Number(index) + 1].displayOrder;
				this.questionArray[Number(index) + 1].displayOrder = displayOrder;
				this.questionArray[index] = this.questionArray[Number(index) + 1];
				this.questionArray[Number(index) + 1] = cloneThisQuestion;
				this.showQuestion(groupDisplayOrder);
			}
		}else{
			if(displayOrder == this.questionArray.length){
				this.showWarmDialog("warmMessage",BINOLMOCIO10_js_i18n.moveDown_warm);
			}else{
				var this_index = displayOrder - 1;
				this.questionArray[this_index].displayOrder = Number(displayOrder) + 1;
				this.questionArray[Number(this_index) + 1].displayOrder = displayOrder;
				var o = this.questionArray[this_index];
				this.questionArray[this_index] = this.questionArray[Number(this_index) + 1];
				this.questionArray[Number(this_index) + 1] = o;
				this.showQuestion();
			}
		}
	},
	"saveCheckPaper":function(){
		$('#errorDiv').hide();
		var that = this;
		var gropStr = JSON2.stringify(this.groupArray);
		var ajaxQuestionArray = [];
		for(var key in this.questionArray){
			if(key!="remove" && this.questionArray[key] != undefined){
				ajaxQuestionArray.push(this.questionArray[key]);
			}
		}
		var queStr = JSON2.stringify(ajaxQuestionArray);
		if(queStr.length == 2){
			$('#errorSpan').html($('#errmsg').val());
			$('#errorDiv').show();
			return false;
		}
		var paperLevelStr = JSON2.stringify(this.paperLevelArray);
		if(this.flag == "0"){
			var url = $("#saveCheckPaper").html();
			$("#paperLevelStr").val(paperLevelStr);
			$("#gropStr").val(gropStr);
			$("#queStr").val(queStr);
			var param = $("#mainForm").serialize() + "&maxPoint=" + $.trim($("#mainForm #maxPoint").val());
		}else{
			var url = $("#editPaper").html();
			$("#paperLevelStr").val(paperLevelStr);
			$("#gropStr").val(gropStr);
			$("#queStr").val(queStr);
			var param = $("#mainForm").serialize() + "&maxPoint=" + $.trim($("#mainForm #maxPoint").val())+"&paperId="+$("#paperId").html()+"&brandInfoId="+$("#brandInfoId option:selected").val();
		}
		param += "&csrftoken=" + getTokenVal();
		var callback=function(msg){
			that.paperLevelArray=[];
			if($('#actionResultBody').length > 0) {
				if(window.opener.oTableArr[0] != null)
				{
					window.opener.binOLMOCIO09_search();
				}
				window.opener.setBtnsdisable();
			}
			$("div").removeClass("container");
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
			});
	},
	"calculationMaxPoint":function(){
		var newQuestionArray = [];
		var maxPoint = 0;
		for(var key in this.questionArray){
			if(key!="remove" && this.questionArray[key] != undefined){
				newQuestionArray.push(this.questionArray[key]);
			}
		}
		for(var i in newQuestionArray){
			if(i != "remove"){
				var questionMaxPoint = newQuestionArray[i].maxPoint;
				maxPoint = Number(maxPoint)+Number(questionMaxPoint);
			}
		}
		maxPoint = Math.round(maxPoint*100)/100;
		$("#mainForm").find("#maxPoint").val(maxPoint);
	},
	"limitStringLength":function(obj){
		if($.trim($(obj).val()).length>60){
			var str = $.trim($(obj).val()).substring(0, 60);
			$(obj).val(str);
		}
	},
	"openPaperLevelDialog":function(dialogParent, dialogDiv,dialogTitle){
		var that = this;
		this.recordDialogHtml(dialogDiv);
		this.setPaperLevelDialog(dialogParent, dialogDiv,dialogTitle);
		$("#paperLevel").dialog("open");
		$("#paperMaxPoint").html($.trim($("#mainForm #maxPoint").val()));
		$("#paperLevel #addLevel").click(function(){
			that.addPaperLevel();
		});
	},
	"setPaperLevelDialog":function(dialogParent, dialogDiv,dialogTitle){
		var that = this;
		$("#paperLevel").dialog({
			autoOpen : false,
			draggable : true,
			resizable : false,
			modal : true,
			title : dialogTitle,
			height : 330,
			width : 570,
			buttons : [ {
				text : BINOLMOCIO10_js_i18n.save,
				click : function() {
					that.savePaperLevel();
				}
			} ],
			close : function(event, ui) {
				that.closeDialog(dialogParent, dialogDiv);
			}
		});
	},
	"addPaperLevel":function(){
		var paperLevelNumber = $("#alreadyAddPaperLevel").find("div[id^=paperLevel_]").length;
		var html='<div id="paperLevel_'+paperLevelNumber+'" class="clearfix"><span>'+BINOLMOCIO10_js_i18n.levelName+'</span><input class="text" type="text" id ="pointLevelName" maxlength="50" style="width:150px;height:15px"/><span>'+BINOLMOCIO10_js_i18n.levelTip+'<input class="text" type="text" id ="point" maxlength="5" style="width:30px;height:15px"/>'+BINOLMOCIO10_js_i18n.pointTip+'</span><a id="delete" class="right" style="cursor:pointer"><span class="ui-icon icon-delete"></span></a><hr class="space" /></div>';
		$("#alreadyAddPaperLevel").append(html);
		$("#paperLevel_"+paperLevelNumber).find("#delete").click(function(){
			$(this).parent().remove();
		});
	},
	"savePaperLevel":function(){
		var flag1 = true;
		var flag2 = true;
		$("#alreadyAddPaperLevel").find("div[id^=paperLevel_]").each(function(){
			if($.trim($(this).find("#pointLevelName").val()) == "" || $.trim($(this).find("#point").val()) == ""){
				flag1 = false;
			}
			if(Number($.trim($(this).find("#point").val())).toString() == "NaN"){
				flag2 = false;
			}
		});
		if(flag1 == false){
			$("#paperLevel #errorSpan").html($("#errmsg5").val());
			$("#paperLevel #errorDiv").show();
			return;
		}else{
			$("#paperLevel #errorSpan").html("");
			$("#paperLevel #errorDiv").hide();
		}
		if(flag2 == false){
			$("#paperLevel #errorSpan").html($("#errmsg4").val());
			$("#paperLevel #errorDiv").show();
			return;
		}else{
			$("#paperLevel #errorSpan").html("");
			$("#paperLevel #errorDiv").hide();
		}
		var that = this;
		$("#alreadyAddPaperLevel").find("div[id^=paperLevel_]").each(function(){
			var pointLevelOrder = Number($(this).attr("id").split("_")[1])+1;
			var o = {
				pointLevelOrder:pointLevelOrder,
				pointLevelName:$.trim($(this).find("#pointLevelName").val()),
				point:$.trim($(this).find("#point").val())
			};
			that.paperLevelArray.push(o);
		});
		this.saveCheckPaper();
		this.closeDialog("showPaperLevel_main", "paperLevel");
	},
	"openPaperPreviewDialog":function(dialogParent, dialogDiv,dialogTitle){
		var that = this;
		this.recordDialogHtml(dialogDiv);
		this.setPaperPreviewDialog(dialogParent, dialogDiv, dialogTitle);
		$("#paperPreview").dialog("open");
		this.fillPaperMain();
		this.fillGroup();
	},
	"setPaperPreviewDialog":function(dialogParent, dialogDiv,dialogTitle){
		var that = this;
		$("#paperPreview").dialog({
			autoOpen : false,
			draggable : true,
			resizable : true,
			modal : true,
			title : dialogTitle,
			height : 630,
			width : 550,
			buttons : [ {
				text : BINOLMOCIO10_js_i18n.close,
				click : function() {
					that.closeDialog(dialogParent, dialogDiv);
				}
			} ],
			close : function(event, ui) {
				that.closeDialog(dialogParent, dialogDiv);
			}
		});
	},
	"fillPaperMain":function(){
		$("#preview_brandInfoId").append(
				" " + $("#brandInfoId option:selected").text());
		$("#preview_paperName").append(" " + $.trim($("#paperName").val()).escapeHTML());
		$("#preview_paperRight").append(" "+$("#paperRight option:selected").text());
		if ($.trim($("#startDate").val()) == "") {
			$("#preview_date").append(" ");
		} else {
			$("#preview_date").append(
					" " + $.trim($("#startDate").val()) +' '+ BINOLMOCIO10_js_i18n.to +' '+ $.trim($("#endDate").val()));
		}
		$("#preview_maxPoint").append(" "+$.trim($("#mainForm #maxPoint").val()));
	},
	"fillGroup":function(){
		var that = this ;
		for(var i = 0 ; i < this.groupArray.length ; i++){
			var html = '<div id="group_'+this.groupArray[i].displayOrder+'" class="box4"><div id="groupHeader_'+this.groupArray[i].displayOrder+'" class="box4-header clearfix "><strong>'+this.groupArray[i].groupName.escapeHTML()+'</strong><div id="expand_'+this.groupArray[i].displayOrder+'" class="right"><span class="expand" style="cursor:pointer;margin:5px 0"><span class="ui-icon ui-icon-triangle-1-n"></span></span></div></div><div class="box4-content clearfix " id="question" style="display:none"></div></div>';
			$("#preview_question").append(html);
			// 展开合并按钮事件绑定处理
			$("#expand_"+this.groupArray[i].displayOrder).click(function(){
				if($(this).children('.expand').children('.ui-icon').is('.ui-icon-triangle-1-n')) {
					$(this).children('.expand').children('.ui-icon').removeClass(' ui-icon-triangle-1-n').addClass(' ui-icon-triangle-1-s');
				} else {
					$(this).children('.expand').children('.ui-icon').removeClass(' ui-icon-triangle-1-s').addClass(' ui-icon-triangle-1-n');
				}
				if($(this).parent('.box4-header').next('.box4-content').is(":hidden")){
					$(this).parent('.box4-header').next('.box4-content').show();
//					$(this).parent().attr("title",BINOLMOCIO10_js_i18n.hideTip);
					var idArray = $(this).parent().attr("id");
					var groupDisplayOrder = idArray.split("_")[1];
					that.fillQuestion(idArray,groupDisplayOrder);
				}else{
					$(this).parent('.box4-header').next('.box4-content').html("").hide();
//					$(this).attr("title",BINOLMOCIO10_js_i18n.showTip);
				}
			});

		}
	},
	"fillQuestion":function(id,groupDisplayOrder){
		var thisGroup_indx = Number(groupDisplayOrder) - 1;
		var temp_array = [];
		for(var i = 0 ; i< this.questionArray.length ; i++){
			if(this.questionArray[i] != undefined && this.questionArray[i].checkQuestionGroupId == this.groupArray[thisGroup_indx].index){
				temp_array.push(this.questionArray[i]);
			}
		}
//		alert(JSON2.stringify(temp_array));
		for(var j = 0 ; j < temp_array.length ; j++){
			var question_html = '<div id="question_'+j+'" class="clearfix" style="margin:0px 10px 10px 0px;padding:10px"><strong><span class="clearfix"><span class="highlight" style="font-weight:bolder">'+temp_array[j].displayOrder+'</span>：'+temp_array[j].questionItem.escapeHTML()+'（'+temp_array[j].maxPoint+'-'+temp_array[j].minPoint+'分）</span></strong></div>';
			$("#"+id).next().append(question_html);
			if(temp_array[j].questionType == "0" || temp_array[j].questionType == "2"){
				var choice_html = '<div class="clearfix"  style="margin:0px 10px 0px 10px" id="choiceMain_'+j+'"></div>';
				$("#"+id).next().find("#question_"+j).append(choice_html);
				var choice_num = 0;
				for(var key in temp_array[j]){
					if(key.indexOf("option") > -1){
						choice_num++;
					}
				}
				var choiceDetail_html = "";
				for(var charNum = 65 ;charNum <= 64+Number(choice_num);charNum++){ 
					var choiceChar = String.fromCharCode(charNum);
					var choice = "option"+choiceChar;
					var point = "point"+choiceChar;
					if(temp_array[j].questionType == "0" ){
						choiceDetail_html = choiceDetail_html + '<span class="clearfix"><input type="radio" disabled="disabled" name="question_choiceDetail_'+j+'"/>'+choiceChar+'：'+temp_array[j][choice].escapeHTML()+'（'+temp_array[j][point]+'分）</span>';
					}else{
						choiceDetail_html = choiceDetail_html + '<span class="clearfix"><input type="checkbox" disabled="disabled"/>'+choiceChar+'：'+temp_array[j][choice].escapeHTML()+'（'+temp_array[j][point]+'分）</span>';
					}
				}
				$("#"+id).next().find("#choiceMain_"+j).append(choiceDetail_html);
			}else if(temp_array[j].questionType == "0"){
				var ansLine1 = '<span class="clearfix" style="margin:0px 10px 0px 10px">_____________________________________________________</span>';
				var ansLine2 = '<span class="clearfix" style="margin:0px 10px 0px 10px">_____________________________________________________</span>';
				$("#"+id).next().find("#question_"+j).append(ansLine1);
				$("#"+id).next().find("#question_"+j).append(ansLine2);
			}
		}
	},
	"isPositiveFloat":function(s){
		var patrn=/^((([1-9][0-9]{0,4}|0.[0-9]{1,2})|[1-9][0-9]{0,4}\.[0-9]{1,2})|0)$/; 
		if (!patrn.exec(s)) return false;
		return true;
	},
	"textCounter":function(obj, maxLength){
		var str = obj.value;
		str = subString(str,maxLength);
		obj.value = str;
	}
	
};

/**
 * 未选择分组警告信息
 * @param 无
 */
function noGroupWarmConfirm() {
	var title = $('#tipTitle').text();
	var text = $('#noGroupWarmMsg').html();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	300,
		height: 200,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		confirmEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}

/**
 * 未选择所属分组警告信息
 * @param 无
 */
function uncheckGroupWarmConfirm() {
	var title = $('#tipTitle').text();
	var text = $('#uncheckGroupWarmMsg').html();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	300,
		height: 200,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		confirmEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}

/** 方法:Array.remove(dx)  
 * 功能:删除数组元素.  
 * 参数:dx删除元素的下标.  
 * 返回:在原数组上修改数组  
 */  
//经常用的是通过遍历,重构数组.   
Array.prototype.remove=function(dx)   
{   
  if(isNaN(dx)||dx>this.length){return false;}   
  for(var i=0,n=0;i<this.length;i++)   
  {   
      if(this[i]!=this[dx])   
      {   
          this[n++]=this[i];
      }   
  }   
  this.length-=1;
};

/**
 * 重写了escapeHTML方法，避免XSS注入
 * 
 * 
 * */
String.prototype.escapeHTML = function () {
	return this.replace(/&/g, '&amp;').replace(/>/g, '&gt;').replace(/</g, '&lt;').replace(/”/g, '&quot;');
};
String.prototype.unEscapeHTML = function(){
	return this.replace(/&amp;/g,'&').replace(/&gt;/g,'>').replace(/&lt;/g,'<').replace(/&quot;/g,'"');
};

var binOLMOCIO10 = new BINOLMOCIO10();
window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};

$(document).ready(
		function() {
			
			$("#addGroup").click(
					function() {
						binOLMOCIO10.openAddGroupDialog("addGroup_main",
								"addGroup_dialog",
								BINOLMOCIO10_js_i18n.addGroupTitle );
					});
//			$("#addEssay").click(function(){
//				binOLMOCIO10.openAddQuestionDialog("addQuestion_dialog_main", "addQuestion_dialog", BINOLMOCIO10_js_i18n.addEssayTitle,"0");
//			});
			$("#addApfill").click(function(){
				binOLMOCIO10.openAddQuestionDialog("addQuestion_dialog_main", "addQuestion_dialog", BINOLMOCIO10_js_i18n.addApfillTitle,"1");
			});
//			$("#addMultipleChoice").click(function(){
//				binOLMOCIO10.openAddQuestionDialog("addQuestion_dialog_main", "addQuestion_dialog", BINOLMOCIO10_js_i18n.addMulChioceTitle,"2");
//			});
			$("#addSingleChoice").click(function(){
				binOLMOCIO10.openAddQuestionDialog("addQuestion_dialog_main", "addQuestion_dialog", BINOLMOCIO10_js_i18n.addSinChioceTitle,"0");
			});
			$("#savePaper").click(function(){
				binOLMOCIO10.openPaperLevelDialog("showPaperLevel_main", "paperLevel", BINOLMOCIO10_js_i18n.editPointLevel);
			});
			$("#previewPaper").click(function(){
				binOLMOCIO10.openPaperPreviewDialog("paperPreview_mian", "paperPreview", BINOLMOCIO10_js_i18n.paperPreview);
			});
		});