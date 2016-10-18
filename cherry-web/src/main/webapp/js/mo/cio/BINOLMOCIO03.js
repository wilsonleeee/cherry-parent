/**
 * @author zgl
 */
var BINOLMOCIO03_code = 65;
var BINOLMOCIO03_quesNum = 1;
window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};

/**
 * 保存问卷
 * 
 * 
 */

function savePaper() {
	$('#errorDiv').hide();
	// 获取问题的个数
	var quesNum = $("#alreadyAddQuestion_body").children().length;
	if(quesNum < 1){
		$('#errorSpan').html($('#errmsg').val());
		$('#errorDiv').show();
		return false;
	}
	var question = new Array();
	var maxPoint = 0;
	for ( var i = 1; i <= quesNum; i++) {
		var obj = new Object();
		obj.arr = [];
		$("#question_" + i).find("span").each(function() {
			if ($(this).attr("id") != undefined) {
				var q = {};
				q.key = $(this).attr("id");
				q.value = ($(this).html()).unEscapeHTML();
				obj.arr.push(q);
				if($(this).attr("id")=="point"){
					maxPoint = maxPoint + Number($(this).html());
				}
			}
		});
		question.push(obj);
	}
	maxPoint = Math.round(maxPoint*100)/100;
	var queStr = JSON2.stringify(question);
//	var detailQuestion = queStr.replace(/&/g, '*amp*');
	$("#queStr").val(queStr);
	//新增问卷时的状态直接为2（可用）
	var param = "maxPoint="+maxPoint+"&paperStatus=2&csrftoken=" + getTokenVal() + "&" + $("#mainForm").serialize();
	var url = $("#url_savePaper").html();
	var opt = {
		url : url,
		param : param,
		callback : function(msg) {
//			if (msg.indexOf('actionMessage"') > -1) {
//				$("#alreadyAddQuestion_body").html("");
//				BINOLMOCIO03_code = 65;
//				BINOLMOCIO03_quesNum = 1;
//				$("#paperName").val("");
//				$("#maxPoint").val("");
//				$("#isHasMaxPoint").val('0');
//				$("#span_maxPoint").hide();
//				$("#alreadyAddQuestion").hide();
//			}
			if($('#actionResultBody').length > 0) {
				if(window.opener.oTableArr[0] != null)window.opener.binOLMOCIO02_search();
				//此方法已废弃
				//window.opener.disabPapOptBtn();
			}
			$("div").removeClass("container");
		}
	};
	cherryAjaxRequest(opt);
};

/**
 * 弹出添加单选或多选题问题dialog
 * 
 * 
 */
function showChoiceDialog(title, questionType) {
	setChoiceDialog(title, questionType);
	// 点击添加按钮动态添加选项
	$("#addChoice").click(function() {
		addChoice();
	});

	// 点击删除按钮删除选中的选项
	$("#deleteChoice").click(function() {
		deleteChoice();
	});
	$("#add_choiceQuestionItem").val("");
	$("#addChoice_dialog #optionDiv").html("");
	BINOLMOCIO03_code = 65;
	$("#addChoice_dialog").dialog("open");
//	$("#addChoice_dialog #add_choiceQuestionItem").change(function(){
//		obj(this);
//	});
//	$("#addChoice_dialog #add_choiceQuestionItem").bind('paste',function(){
//		window.setTimeout('$(\'#' + this.id + '\').trigger(\'keyup\')', 1);
//		//window.setTimeout("$("+this+").trigger('keyup')", 10);
//	});
	isPointPaper();
}

/**
 * 弹出添加填空题或简单题dialog
 * 
 * 
 */
function showFillDialog(title, questionType) {
	setFillDialog(title, questionType);
	$("#add_fillQuestionItem").val("");
	$("#fillQuestion_point").val("");
	BINOLMOCIO03_code = 65;
	$("#addFill_dialog").dialog("open");
//	$("#add_fillQuestionItem").change(function(){
//		alert(1);
//		obj(this);
//	});
//	$("#add_fillQuestionItem").bind('paste',function(){
//		window.setTimeout('$(\'#' + this.id + '\').trigger(\'keyup\')', 1);
//	});
	isPointPaper();
}

/**
 * 
 * 设置单选题或者多选题dialog
 * 
 * @param
 * 
 * 
 */
function setChoiceDialog(title, questionType) {
	$("#addChoice_dialog").dialog( {
		autoOpen : false,
		draggable : true,
		resizable : false,
		modal : true,
		title : title,
		height : 378,
		width : 560,
		buttons : [{
		    text:$("#nextQuestion").val(),
			click : function() {
				saveChoiceQuestion(questionType,$("#choice_isRequired").val());
//				BINOLMOCIO03_code = 65;
//				$("#add_choiceQuestionItem").val("");
//				$("#addChoice_dialog #optionDiv").html("");
			}
		}],
		close : function(event, ui) {
			BINOLMOCIO03_code = 65;
			choiceDialogClose();
			$("#addChoice_dialog #optionDiv").html("");
			$("#errorDiv1").hide();
		}
	});
}
/**
 * 
 * 设置填空题或者问答题dialog
 * 
 * @param
 * 
 * 
 */
function setFillDialog(title, questionType) {
	$("#addFill_dialog").dialog( {
		autoOpen : false,
		draggable : true,
		resizable : false,
		modal : true,
		title : title,
		height : 250,
		width : 570,
		buttons : [{
			text:$("#nextQuestion").val(),
			click : function() {
				saveFillQuestion(questionType);
//				BINOLMOCIO03_code = 65;
//				$("#add_fillQuestionItem").val("");
//				$("#fillQuestion_point").val("");
			}
		}],
		close : function(event, ui) {
			fillDialogClose();
			$("#errorDiv4").hide();
		}
	});
}

/**
 * 添加单选题或多选题问题选项
 * 
 * 
 */
function addChoice() {
	var numChoice = $("#addChoice_dialog input[id^=input_]").length;
	if(numChoice == 20){
//		alert($("#maxSelect").val());
		return false;
	}
	var select = String.fromCharCode(BINOLMOCIO03_code);
	var str = "";
	if ($("#isHasMaxPoint option:selected").val() == "0") {
		str = '<div id = "div_'
				+ select
				+ '" class="clearfix"><input type="checkbox" id="checkbox_'
				+ select
				+ '"/><label id="'
				+ select
				+ '"><span>'
				+ select
				+ '</span>：</label><input class="text" type="text" style="width:350px" id="input_'
				+ select + '" value="" maxlength="300"/></div>';
	} else {
		str = '<div id = "div_'
				+ select
				+ '" class="clearfix"><input type="checkbox" id="checkbox_'
				+ select
				+ '"/><label id="'
				+ select
				+ '"><span>'
				+ select
				+ '</span>：</label><input class="text" type="text" style="width:350px" id="input_'
				+ select
				+ '" value="" maxlength="300"/><label>'
				+ BINOLMOCIO03_js_i18n.point
				+ '：</label><input class="text" type="text" id="point_'
				+ select
				+ '" maxlength="5" style="width:30px;height:15px" value="0"/> </div>';
	}
	$("#addChoice_dialog #optionDiv").append(str);

	BINOLMOCIO03_code++;
	return "input_" + select;
}

/**
 * 删除单选题或多选题问题选项
 * 
 * 
 */
function deleteChoice() {
	var checked_id = new Array();
	var unchecked_id = new Array();
	$("input[type='checkbox']").each(function() {
		if ($(this).prop("checked")) {
			checked_id.push($(this).attr("id"));
		} else {
			unchecked_id.push($(this).attr("id"));
		}
	});
	$("input:checked").parent().remove();
	var checked_length = checked_id.length;
	if (checked_length > 0) {
		var old_id = 0;
		BINOLMOCIO03_code = BINOLMOCIO03_code - checked_length;
		for ( var i = 65; i < BINOLMOCIO03_code; i++) {
			var new_letter = String.fromCharCode(i);
			var old_letter = unchecked_id[old_id].split("_");
			$("#div_" + old_letter[1]).find("label").each(function(){
				if($(this).attr("id")!=undefined){
					$(this).html(
							"<span>" + new_letter + "：</span>");
				}
			});
			$("#div_" + old_letter[1]).attr("id", "div_" + new_letter);
			$("#checkbox_" + old_letter[1])
					.attr("id", "checkbox_" + new_letter);
			$("#input_" + old_letter[1]).attr("id", "input_" + new_letter);
			if ($("#isHasMaxPoint option:selected").val()=="0") {
				;
			} else {
				$("#point_" + old_letter[1]).attr("id", "point_" + new_letter);
			}
			$("#" + old_letter[1]).attr("id", "" + new_letter);
			old_id++;
		}
	}
}
/**
 * 设置是否为计分试卷
 * 
 * 
 */
function isPointPaper() {
	if ($("#isHasMaxPoint option:selected").val()=="0") {
//		$("#span_maxPoint").show();
//		$("#question_maxPoint").show();
		$("#fillQuestion_maxPoint").hide();
	} else {
//		$("#span_maxPoint").hide();
//		$("#question_maxPoint").hide();
		$("#fillQuestion_maxPoint").show();
//		$("#maxPoint").val("");
	}
}
/**
 * 关闭dialog
 * 
 * 
 */
function choiceDialogClose() {
	$("#addChoice_dialog").dialog('destroy');
	$("#addChoice_dialog").remove();
	$("#addChoice_dialog_main").append(addChoice_dialog_main);
}
/**
 * 关闭dialog
 * 
 * 
 */
function fillDialogClose() {
	closeCherryDialog('addFill_dialog');
	$("#addFill_dialog").dialog('destroy');
	$("#addFill_dialog").remove();
	$("#addFill_dialog_main").append(addFill_dialog_main);
}
/**
 * 初始化填充dialog窗体
 * 
 * 
 */
function fillDialog(QuestionTypeDialog) {
	$("#" + QuestionTypeDialog).html();
	var html = $("#question_dialog").html();
	$("#" + QuestionTypeDialog).append(html);
}
/**
 * 将单选题或多选题添加到主画面中
 * 
 * @param questionType
 *            问题类型
 * 
 */
function saveChoiceQuestion(questionType,isRequired) {
	$('#errorSpan1').html("");
	$("#errorDiv1").hide();
	if ($.trim($("#add_choiceQuestionItem").val()) == "") {
		$('#errorSpan1').html($('#errmsg1').val());
		$("#errorDiv1").show();
		return false;
	}
	try{
		if($("input[id^=input_]").length == 0){
			throw "e1";
		}
		$("input[id^=input_]").each(function(){
			if($.trim($(this).val()) == ""){
				throw "e1";
			}
		});
		$("input[id^=point_]").each(function(){
			if($.trim($(this).val()) == ""){
				throw "e3";
			}
			if(isPositiveFloat(Number($.trim($(this).val()))) == false){
				throw "e2";
			}
		});
	}catch(e){
		if(e == "e1"){
			$('#errorSpan1').html($('#errmsg2').val());
			$("#errorDiv1").show();
			return false;
		}
		if(e == "e2"){
			$('#errorSpan1').html($('#errmsg3').val());
			$("#errorDiv1").show();
			return false;
		}
		if(e == "e3"){
			$('#errorSpan1').html($('#errmsg4').val());
			$("#errorDiv1").show();
			return false;
		}
	}
	var chioceNum = BINOLMOCIO03_code - 65;
	var BINOLMOCIO03_quesNumPrevious = BINOLMOCIO03_quesNum-1;
	var ques_html = '<div class="clearfix" id="question_' + BINOLMOCIO03_quesNum + '" style="border-bottom: 1px dashed #ccc; padding-bottom: 5px; margin-bottom: 10px;"></div>';
	var cont_html = '<div class="left" style="width:430px"><span id="displayOrder" class="highlight">'
			+ BINOLMOCIO03_quesNum
			+ '</span>：<span id="questionItem">'
			+ $.trim($("#add_choiceQuestionItem").val()).escapeHTML() + '</span></div>';
	var butt_html="";
	if(BINOLMOCIO03_quesNum == 1){
		butt_html = '<div class="right"><a class="left" id="edit" title="'+BINOLMOCIO03_js_i18n.editQ+'" style="cursor:pointer"><span class="ui-icon icon-edit"></span></a><a class="left" id="delete" title="'+BINOLMOCIO03_js_i18n.deleteQ+'" style="cursor:pointer"><span class="ui-icon icon-delete"></span></a><span id="moveUp"><span class="left"  style="height:16px; width:16px; display:block;"></span></span><span id="moveDown"><span class="left"  style="height:16px; width:16px; display:block;"></span></span></div>';
	}else{
		$("#question_"+BINOLMOCIO03_quesNumPrevious).find("#moveDown").html('<a class="left" title="'+BINOLMOCIO03_js_i18n.moveDown+'"><span class="arrow-down"></span></a>');
		butt_html = '<div class="right"><a class="left" id="edit" title="'+BINOLMOCIO03_js_i18n.editQ+'" style="cursor:pointer"><span class="ui-icon icon-edit"></span></a><a class="left" id="delete" title="'+BINOLMOCIO03_js_i18n.deleteQ+'" style="cursor:pointer"><span class="ui-icon icon-delete"></span></a><span id="moveUp"><a class="left" title="'+BINOLMOCIO03_js_i18n.moveUp+'" style="cursor:pointer"><span class="arrow-up"/></a></span><span id="moveDown"><span class="left" style="height:16px; width:16px; display:block;"></span></span></div>';
	}
	var queType_html = '<span id="questionType" class="hide">' + questionType + '</span>';
	var isRequired_html = '<span id="isRequired" class="hide">' + isRequired + '</span>';
	$("#alreadyAddQuestion").show();
	$("#alreadyAddQuestion_body").append(ques_html);
	$("#question_" + BINOLMOCIO03_quesNum).append(cont_html);
	$("#question_" + BINOLMOCIO03_quesNum).append(butt_html);
	$("#question_" + BINOLMOCIO03_quesNum).append(queType_html);
	$("#question_" + BINOLMOCIO03_quesNum).append(isRequired_html);
	if (chioceNum >= 1) {
		var code1 = 65;
		for ( var i = 1; i <= chioceNum; i++) {
			char = String.fromCharCode(code1);
			$("#question_" + BINOLMOCIO03_quesNum).append(
					'<span class="hide" id="option' + char + '">'
							+ $.trim($("#" + char).next().val()).escapeHTML() + '</span>');
			code1++;
		}
	}
	if ($("#isHasMaxPoint option:selected").val()=="0") {
		;
	} else {
		var point = 0;
		if (chioceNum >= 1) {
			var code = 65;
			if(questionType == "2")
			{
			for ( var i = 1; i <= chioceNum; i++) {
				char = String.fromCharCode(code);
				$("#question_" + BINOLMOCIO03_quesNum).append(
						'<span class="hide" id="point' + char + '">'
								+ $.trim($("#point_" + char).val()) + '</span>');
				point = point + Number($.trim($("#point_" + char).val()));
				code++;
			}
			}else{
				for ( var i = 1; i <= chioceNum; i++) {
					char = String.fromCharCode(code);
					$("#question_" + BINOLMOCIO03_quesNum).append(
							'<span class="hide" id="point' + char + '">'
									+ Number($.trim($("#point_" + char).val())) + '</span>');
					if(point <=Number($.trim($("#point_" + char).val()))){
						point = Number($.trim($("#point_" + char).val()));
					}
					code++;
				}
			}
		}
		var point_html = '<span id="point" class="hide">' +point+ '</span>';
		$("#question_" + BINOLMOCIO03_quesNum).append(point_html);
	}
	$("#question_" + BINOLMOCIO03_quesNum).find("#delete").bind("click",
			function() {
					deleteQuestion($(this).parent().parent().attr("id"));
			});
	$("#question_" + BINOLMOCIO03_quesNum).find("#moveUp").bind("click",
			function() {
				moveUp($(this).parent().parent().attr("id"));
			});
	$("#question_" + BINOLMOCIO03_quesNum).find("#moveDown").bind("click",
			function() {
				moveDown($(this).parent().parent().attr("id"));
			});
	$("#question_" + BINOLMOCIO03_quesNum).find("#edit").bind("click",
			function() {
				editQuestion($(this).parent().parent().attr("id"));
			});
	BINOLMOCIO03_quesNum++;
	$("#errorDiv1").hide();
	BINOLMOCIO03_code = 65;
	$("#add_choiceQuestionItem").val("");
	$("#addChoice_dialog #optionDiv").html("");
	
	if ($("#alreadyAddQuestion_body").html().length != 29) {
		$("#isHasMaxPoint").attr("disabled","disabled");
	}else{
		$("#isHasMaxPoint").attr("disabled",true);
	}
}
/**
 * 将填空或者问答题添加到主画面中
 * 
 * @param questionType
 *            问题类型
 * 
 */
function saveFillQuestion(questionType) {
	$('#errorSpan4').html("");
	$("#errorDiv4").hide();
	if ($.trim($("#add_fillQuestionItem").val()) == "") {
		$('#errorSpan4').html($('#errmsg1').val());
		$("#errorDiv4").show();
		return false;
	}
	if($("#fillQuestion_maxPoint").is(":hidden") == false){
		if ($.trim($("#fillQuestion_point").val()) == "") {
			$('#errorSpan4').html($('#errmsg4').val());
			$("#errorDiv4").show();
			return false;
		}
		if (isPositiveFloat(Number($.trim($("#fillQuestion_point").val()))) == false) {
			$('#errorSpan4').html($('#errmsg3').val());
			$("#errorDiv4").show();
			return false;
		}
	}
	var BINOLMOCIO03_quesNumPrevious = BINOLMOCIO03_quesNum-1;
	var ques_html = '<div class="clearfix" id="question_' + BINOLMOCIO03_quesNum + '" style="border-bottom: 1px dashed #ccc; padding-bottom: 5px; margin-bottom: 10px;"></div>';
	var cont_html = '<div class="left" style="width:430px"><span id="displayOrder" class="highlight">'
			+ BINOLMOCIO03_quesNum
			+ '</span>：<span id="questionItem">'
			+ $.trim($("#add_fillQuestionItem").val()).escapeHTML() + '</span></div>';
	var butt_html="";
	if(BINOLMOCIO03_quesNum == 1){
		butt_html = '<div class="right"><a class="left" id="edit" title="'+BINOLMOCIO03_js_i18n.editQ+'" style="cursor:pointer"><span class="ui-icon icon-edit"></span></a><a class="left" id="delete" title="'+BINOLMOCIO03_js_i18n.deleteQ+'" style="cursor:pointer"><span class="ui-icon icon-delete"></span></a><span id="moveUp"><span class="left"  style="height:16px; width:16px; display:block;"></span></span><span id="moveDown"><span class="left"  style="height:16px; width:16px; display:block;"></span></span></div>';
	}else{
		$("#question_"+BINOLMOCIO03_quesNumPrevious).find("#moveDown").html('<a class="left" title="'+BINOLMOCIO03_js_i18n.moveDown+'"><span class="arrow-down"></span></a>');
		butt_html = '<div class="right"><a class="left" id="edit" title="'+BINOLMOCIO03_js_i18n.editQ+'" style="cursor:pointer"><span class="ui-icon icon-edit"></span></a><a class="left" id="delete" title="'+BINOLMOCIO03_js_i18n.deleteQ+'" style="cursor:pointer"><span class="ui-icon icon-delete"></span></a><span id="moveUp"><a class="left" title="'+BINOLMOCIO03_js_i18n.moveUp+'" style="cursor:pointer"><span class="arrow-up"/></a></span><span id="moveDown"><span class="left" style="height:16px; width:16px; display:block;"></span></span></div>';
	}
	var queType_html = '<span id="questionType" class="hide">' + questionType + '</span>';
	var isRequired_html = '<span id="isRequired" class="hide">' + $("#fill_isRequired").val() + '</span>';
	$("#alreadyAddQuestion").show();
	$("#alreadyAddQuestion_body").append(ques_html);
	$("#question_" + BINOLMOCIO03_quesNum).append(cont_html);
	$("#question_" + BINOLMOCIO03_quesNum).append(butt_html);
	$("#question_" + BINOLMOCIO03_quesNum).append(queType_html);
	$("#question_" + BINOLMOCIO03_quesNum).append(isRequired_html);
	if ($("#isHasMaxPoint option:selected").val()=="0") {
		;
	} else {
		var point_html = '<span id="point" class="hide">' + Number($.trim($(
				"#fillQuestion_point").val())) + '</span>';
		$("#question_" + BINOLMOCIO03_quesNum).append(point_html);
	}
	$("#question_" + BINOLMOCIO03_quesNum).find("#delete").bind("click",
			function() {
				deleteQuestion($(this).parent().parent().attr("id"));
			});
	$("#question_" + BINOLMOCIO03_quesNum).find("#moveUp").bind("click",
			function() {
				moveUp($(this).parent().parent().attr("id"));
			});
	$("#question_" + BINOLMOCIO03_quesNum).find("#moveDown").bind("click",
			function() {
				moveDown($(this).parent().parent().attr("id"));
			});
	$("#question_" + BINOLMOCIO03_quesNum).find("#edit").bind("click",
			function() {
				editQuestion($(this).parent().parent().attr("id"));
			});
	BINOLMOCIO03_quesNum++;
	$("#errorDiv4").hide();
	BINOLMOCIO03_code = 65;
	$("#add_fillQuestionItem").val("");
	$("#fillQuestion_point").val("");
	
	if ($("#alreadyAddQuestion_body").html().length != 29) {
		$("#isHasMaxPoint").attr("disabled","disabled");
	}else{
		$("#isHasMaxPoint").attr("disabled",true);
	}
}
/**
 * 删除问题
 * 
 * @param quesDivId
 *            要删除的问题的div的ID，一次只删除一个问题
 * 
 * 
 */
function deleteQuestion(quesDivId) {
	var this_id = quesDivId.split("_");
	var this_displayOrder = this_id[1];
	var next_displayOrder = Number(this_id[1])+1;
	var SecondLast_displayOrder = BINOLMOCIO03_quesNum - 2;
	if(BINOLMOCIO03_quesNum > 2){//问题不止一个
		if(this_displayOrder == 1){//删除的问题为第一个
			$("#question_"+next_displayOrder).find("#moveUp").html('<span class="left" style="height:16px; width:16px; display:block;"></span>');
		}else if(this_displayOrder == BINOLMOCIO03_quesNum-1){//删除的问题是最后一个
			$("#question_"+SecondLast_displayOrder).find("#moveDown").html('<span class="left" style="height:16px; width:16px; display:block;"></span>');
		}else{
			;
		}
	}else{
		;
	}
	$("#" + quesDivId).remove();
	BINOLMOCIO03_quesNum--;
	// 此时的问题个数为
	var quesNum = BINOLMOCIO03_quesNum - 1;
	// 记录下所有的没有删除的问题的id
	var questionId = [];
	$("#alreadyAddQuestion_body").children(".clearfix").each(function() {
		questionId.push($(this).attr("id"));

	});
	for ( var i = 1; i <= quesNum; i++) {

//		$("#" + questionId[i - 1]).find("#delete").unbind("click");
//		$("#" + questionId[i - 1]).find("#moveUp").unbind("click");
//		$("#" + questionId[i - 1]).find("#moveDown").unbind("click");
//		$("#" + questionId[i - 1]).find("#edit").unbind("click");
		$("#" + questionId[i - 1]).find("#displayOrder").html(i);
		$("#" + questionId[i - 1]).attr("id", "question_" + i);
//		$("#question_" + i).find("#delete").bind("click", function() {
//			deleteQuestion($(this).parent().parent().attr("id"));
//		});
//		$("#question_" + i).find("#moveUp").bind("click", function() {
//			moveUp($(this).parent().parent().attr("id"));
//		});
//		$("#question_" + i).find("#moveDown").bind("click", function() {
//			moveDown($(this).parent().parent().attr("id"));
//		});
//		$("#question_" + i).find("#edit").bind("click", function() {
//			editQuestion($(this).parent().parent().attr("id"));
//		});
	}
	//问题已经删完
	if(BINOLMOCIO03_quesNum == 1){
		$("#alreadyAddQuestion").hide();
		$("#isHasMaxPoint").removeAttr("disabled");
	}
}


/**
 * 调整问题的显示顺序，上移
 * 
 * @param quesDivId
 *            要上移的问题所在的div的ID，一次只上移一个位置
 * 
 * 
 */
function moveUp(quesDivId) {
	var this_id = quesDivId.split("_");
	var this_displayOrder = Number(this_id[1]);
	var up_displayOrder = Number(this_id[1]) - 1;
	//当点击到第一个问题的moveUp标签位置时直接跳出
	if(this_displayOrder == 1){
		return;
	}
	if(BINOLMOCIO03_quesNum > 3){//问题数至少为三个
		if (this_displayOrder == 2) {//要上移的问题是第二个问题
			$("#question_"+this_displayOrder).find("#moveUp").html('<span class="left"  style="height:16px; width:16px; display:block;"></span>');
			$("#question_"+up_displayOrder).find("#moveUp").html('<a class="left" title="'+BINOLMOCIO03_js_i18n.moveUp+'" style="cursor:pointer"><span class="arrow-up"/></a>');
		}else if(this_displayOrder == BINOLMOCIO03_quesNum - 1){//要上移的问题为最后一个问题
			$("#question_"+up_displayOrder).find("#moveDown").html('<span class="left"  style="height:16px; width:16px; display:block;"></span>');
			$("#question_"+this_displayOrder).find("#moveDown").html('<a class="left" title="'+BINOLMOCIO03_js_i18n.moveDown+'" style="cursor:pointer"><span class="arrow-down"/></a>');
		}else{
			;
		}
	}else if(BINOLMOCIO03_quesNum == 3){//问题数为两个的情况(上移只能是最后一个问题上移)
		if(this_displayOrder == BINOLMOCIO03_quesNum-1){
			$("#question_"+this_displayOrder).find("#moveUp").html('<span class="left"  style="height:16px; width:16px; display:block;"></span>');
			$("#question_"+this_displayOrder).find("#moveDown").html('<a class="left" title="'+BINOLMOCIO03_js_i18n.moveDown+'" style="cursor:pointer"><span class="arrow-down"/></a>');
			$("#question_"+up_displayOrder).find("#moveUp").html('<a class="left" title="'+BINOLMOCIO03_js_i18n.moveUp+'" style="cursor:pointer"><span class="arrow-up"/></a>');
			$("#question_"+up_displayOrder).find("#moveDown").html('<span class="left"  style="height:16px; width:16px; display:block;"></span>');
		}else{
			;
		}
	}else{
		;
	}
	// 先将要移动的问题上的所有的click时间删除
	$("#" + quesDivId).find("#delete").unbind("click");
	$("#" + quesDivId).find("#moveUp").unbind("click");
	$("#" + quesDivId).find("#moveDown").unbind("click");
	$("#" + quesDivId).find("#edit").unbind("click");
	$("#question_" + up_displayOrder).find("#delete").unbind("click");
	$("#question_" + up_displayOrder).find("#moveUp").unbind("click");
	$("#question_" + up_displayOrder).find("#moveDown").unbind("click");
	$("#question_" + up_displayOrder).find("#edit").unbind("click");
	
	// 先将要移动问题的显示的顺序替换
	$("#" + quesDivId).find("#displayOrder").html(up_displayOrder);
	$("#question_" + up_displayOrder).find("#displayOrder").html(this_displayOrder);
	// 记录下要移动的问题的上一个位置的问题的HTML，以便之后替换
	var up_div_html = $("#question_" + up_displayOrder).html();
	// 记录下当前问题的HTML，以便之后替换
	var this_div_html = $("#" + quesDivId).html();
	// 替换
	$("#" + quesDivId).html(up_div_html);
	$("#question_" + up_displayOrder).html(this_div_html);
	// 绑定click事件
	$("#" + quesDivId).find("#delete").bind("click", function() {
		deleteQuestion($(this).parent().parent().attr("id"));
	});
	$("#" + quesDivId).find("#moveUp").bind("click", function() {
		moveUp($(this).parent().parent().attr("id"));
	});
	$("#" + quesDivId).find("#moveDown").bind("click", function() {
		moveDown($(this).parent().parent().attr("id"));
	});
	$("#" + quesDivId).find("#edit").bind("click", function() {
		editQuestion($(this).parent().parent().attr("id"));
	});
	$("#question_" + up_displayOrder).find("#moveUp").bind("click",
			function() {
				moveUp($(this).parent().parent().attr("id"));
			});
	$("#question_" + up_displayOrder).find("#delete").bind("click",
			function() {
				deleteQuestion($(this).parent().parent().attr("id"));
			});
	$("#question_" + up_displayOrder).find("#moveDown").bind("click",
			function() {
				moveDown($(this).parent().parent().attr("id"));
			});
	$("#question_" + up_displayOrder).find("#edit").bind("click",
			function() {
				editQuestion($(this).parent().parent().attr("id"));
			});
}

/**
 * 调整问题顺序，下移
 * 
 * @param quesDivId
 *            要下移的问题的div的id，一次只下移一个位置
 * 
 * 
 */
function moveDown(quesDivId) {
	var this_id = quesDivId.split("_");
	var this_displayOrder = Number(this_id[1]);
	var down_displayOrder = Number(this_id[1]) + 1;
	//当点击到最后一个问题的moveUp标签位置时直接跳出
	if(this_displayOrder == BINOLMOCIO03_quesNum-1){
		return;
	}
	if(BINOLMOCIO03_quesNum > 3){//问题数至少三个
		if(this_displayOrder == 1){//要下移的问题为第 一个
			$("#question_"+this_displayOrder).find("#moveUp").html('<a class="left" title="'+BINOLMOCIO03_js_i18n.moveUp+'" style="cursor:pointer"><span class="arrow-up"/></a>');
			$("#question_"+down_displayOrder).find("#moveUp").html('<span class="left"  style="height:16px; width:16px; display:block;"></span>');
		}else if(this_displayOrder == BINOLMOCIO03_quesNum-2){//要下移的问题为倒数第二个
			$("#question_"+this_displayOrder).find("#moveDown").html('<span class="left"  style="height:16px; width:16px; display:block;"></span>');
			$("#question_"+down_displayOrder).find("#moveDown").html('<a class="left" title="'+BINOLMOCIO03_js_i18n.moveDown+'" style="cursor:pointer"><span class="arrow-down"></span></a>');
		}else{
			;
		}
	}else if(BINOLMOCIO03_quesNum == 3){//问题数为两个
		if(this_displayOrder == 1){//要下移的问题为第 一个
			$("#question_"+this_displayOrder).find("#moveUp").html('<a class="left" title="'+BINOLMOCIO03_js_i18n.moveUp+'" style="cursor:pointer"><span class="arrow-up"/></a>');
			$("#question_"+this_displayOrder).find("#moveDown").html('<span class="left"  style="height:16px; width:16px; display:block;"></span>');
			$("#question_"+down_displayOrder).find("#moveUp").html('<span class="left"  style="height:16px; width:16px; display:block;"></span>');
			$("#question_"+down_displayOrder).find("#moveDown").html('<a class="left" title="'+BINOLMOCIO03_js_i18n.moveDown+'" style="cursor:pointer"><span class="arrow-down"></span></a>');
		}else{
			;
		}
	}else{
		;
	}
	// 先将要移动的问题上的所有的click时间删除
	$("#" + quesDivId).find("#delete").unbind("click");
	$("#" + quesDivId).find("#moveUp").unbind("click");
	$("#" + quesDivId).find("#moveDown").unbind("click");
	$("#" + quesDivId).find("#edit").unbind("click");
	$("#question_" + down_displayOrder).find("#delete").unbind("click");
	$("#question_" + down_displayOrder).find("#moveUp").unbind("click");
	$("#question_" + down_displayOrder).find("#moveDown").unbind("click");
	$("#question_" + down_displayOrder).find("#edit").unbind("click");
	// 先将要移动问题的显示的顺序替换
	$("#" + quesDivId).find("#displayOrder").html(down_displayOrder);
	$("#question_" + down_displayOrder).find("#displayOrder").html(
			this_id[1]);
	// 记录下要移动的问题的下一个位置的问题的HTML，以便之后替换
	var down_div_html = $("#question_" + down_displayOrder).html();
	// 记录下当前问题的HTML，以便之后替换
	var this_div_html = $("#" + quesDivId).html();
	// 替换
	$("#" + quesDivId).html(down_div_html);
	$("#question_" + down_displayOrder).html(this_div_html);
	// 绑定click事件
	$("#" + quesDivId).find("#delete").bind("click", function() {
			deleteQuestion($(this).parent().parent().attr("id"));
	});
	$("#" + quesDivId).find("#moveUp").bind("click", function() {
		moveUp($(this).parent().parent().attr("id"));
	});
	$("#" + quesDivId).find("#moveDown").bind("click", function() {
		moveDown($(this).parent().parent().attr("id"));
	});
	$("#" + quesDivId).find("#edit").bind("click", function() {
		editQuestion($(this).parent().parent().attr("id"));
	});
	$("#question_" + down_displayOrder).find("#moveUp").bind("click",
			function() {
				moveUp($(this).parent().parent().attr("id"));
			});
	$("#question_" + down_displayOrder).find("#delete").bind("click",
			function() {
				deleteQuestion($(this).parent().parent().attr("id"));
			});
	$("#question_" + down_displayOrder).find("#moveDown").bind("click",
			function() {
				moveDown($(this).parent().parent().attr("id"));
			});
	$("#question_" + down_displayOrder).find("#edit").bind("click",
			function() {
				editQuestion($(this).parent().parent().attr("id"));
			});
}
/**
 * 
 * 
 * 
 */
function editQuestion(quesDivId) {
	var questionType = $("#" + quesDivId).find("#questionType").html();
	var isRequired = $("#" + quesDivId).find("#isRequired").html();
	if (questionType == '0') {
		setEditDialog(BINOLMOCIO03_js_i18n.editEssayTitle, questionType, isRequired);
	} else if (questionType == '1') {
		setEditDialog(BINOLMOCIO03_js_i18n.editSinChioceTitle, questionType, isRequired);
	} else if (questionType == '2') {
		setEditDialog(BINOLMOCIO03_js_i18n.editMulChioceTitle, questionType, isRequired);
	} else {
		setEditDialog(BINOLMOCIO03_js_i18n.editApfillTitle, questionType, isRequired);
	}
	showEditDialog(quesDivId, questionType, isRequired);
}

function setEditDialog(title, questionType, isRequired) {
	if (questionType == '0' || questionType == '3') {
		$("#addFill_dialog").dialog( {
			autoOpen : false,
			draggable : true,
			resizable : false,
			modal : true,
			title : title,
			height : 250,
			width : 570,
			buttons : [{
				text:$("#save").val(),
				click : function() {
					saveEditFillQuestion(questionType);
					
				}
			}],
			close : function(event, ui) {
				fillDialogClose();
				$("#addFill_dialog #optionDiv").html("");
				$("#errorDiv4").hide();
			}
		});
	} else {
		$("#addChoice_dialog").dialog( {
			autoOpen : false,
			draggable : true,
			resizable : false,
			modal : true,
			title : title,
			height : 378,
			width : 560,
			buttons : [{
				text:$("#save").val(),
				click : function() {
					saveEditChoiceQuestion(questionType);
					
				}
			}],
			close : function(event, ui) {
				BINOLMOCIO03_code = 65;
				choiceDialogClose();
				$("#addChoice_dialog #optionDiv").html("");
				$("#errorDiv1").hide();
			}
		});
	}
}
/**
 * 
 * 
 * 
 * 
 */

function showEditDialog(quesDivId, questionType, isRequired) {
	if (questionType == '0' || questionType == '3') {
		$("#addFill_dialog").dialog("open");
//		$("#add_fillQuestionItem").change(function(){
//			alert(1);
//			obj(this);
//		});
//		$("#add_fillQuestionItem").bind('paste',function(){
//			window.setTimeout('$(\'#' + this.id + '\').trigger(\'keyup\')', 1);
//		});
		$("#add_fillQuestionItem").val(($("#" + quesDivId).find("#questionItem").html()).unEscapeHTML());
		$("#fill_isRequired").val($("#" + quesDivId).find("#isRequired").html());
		if ($("#point").attr("id") != undefined) {
			$("#fillQuestion_maxPoint").show();
			$("#fillQuestion_point").val(
					$("#" + quesDivId).find("#point").html());
		}
		$("#addFill_dialog #optionDiv").append(
				'<span class="hide" id="que_div_id">' + quesDivId + '</span>');
	} else {
		$("#addChoice_dialog").dialog("open");
//		$("#addChoice_dialog #add_choiceQuestionItem").change(function(){
//			obj(this);
//		});
//		$("#addChoice_dialog #add_choiceQuestionItem").bind('paste',function(){
//			window.setTimeout('$(\'#' + this.id + '\').trigger(\'keyup\')', 1);
//			//window.setTimeout("$("+this+").trigger('keyup')", 10);
//		});
		$("#add_choiceQuestionItem").val(($("#" + quesDivId).find("#questionItem").html()).unEscapeHTML());
		$("#choice_isRequired").val($("#" + quesDivId).find("#isRequired").html());
		$("#" + quesDivId + " span[id^=option]").each(function() {
			var new_input_id = addChoice();
			$("#" + new_input_id).val(($(this).html()).unEscapeHTML());
		});
		if ($("#point").attr("id") != undefined) {
			$("#" + quesDivId + " span[id^=point]").each(function() {
				this_id = $(this).attr("id");
				var id_arr = this_id.split("");
				// alert(id_arr);
					$("#point_" + id_arr[id_arr.length - 1])
							.val($(this).html());
				});
		}
		// 点击删除按钮删除选中的选项
		$("#deleteChoice").click(function() {
			deleteChoice();
		});
		$("#addChoice").click(function() {
			addChoice();
		});
		$("#addChoice_dialog #optionDiv").append(
				'<span class="hide" id="que_div_id">' + quesDivId + '</span>');
	}
	isPointPaper();
}

function saveEditChoiceQuestion(questionType) {
	$('#errorSpan1').html("");
	$("#errorDiv1").hide();
	var isRequired = $('#choice_isRequired').val();
	if ($.trim($("#add_choiceQuestionItem").val()) == "") {
		$('#errorSpan1').html($('#errmsg1').val());
		$("#errorDiv1").show();
		return false;
	}
	try{
		if($("input[id^=input_]").length == 0){
			throw "e1";
		}
		$("input[id^=input_]").each(function(){
			if($.trim($(this).val()) == ""){
				throw "e1";
			}
		});
		$("input[id^=point_]").each(function(){
			if($.trim($(this).val()) == ""){
				throw "e3";
			}
			if(isPositiveFloat(Number($.trim($(this).val()))) == false){
				throw "e2";
			}
		});
		
	}catch(e){
		if(e == "e1"){
			$('#errorSpan1').html($('#errmsg2').val());
			$("#errorDiv1").show();
			return false;
		}
		if(e == "e2"){
			$('#errorSpan1').html($('#errmsg3').val());
			$("#errorDiv1").show();
			return false;
		}
		if(e == "e3"){
			$('#errorSpan1').html($('#errmsg4').val());
			$("#errorDiv1").show();
			return false;
		}
	}
	var que_div_id = $("#que_div_id").html();
	//记录编辑之前的按钮布局
	var butt_html = $("#"+que_div_id).find("div").filter(".right").html();
	var arr = que_div_id.split("_");
	$("#" + que_div_id).html("");
	var chioceNum = BINOLMOCIO03_code - 65;
	var cont_html = '<div class="left" style="width:430px"><span id="displayOrder" class="highlight">'
			+ arr[1]
			+ '</span>：<span id="questionItem">'
			+ $.trim($("#add_choiceQuestionItem").val()).escapeHTML() + '</span></div>';
	var queType_html = '<span id="questionType" class="hide">' + questionType + '</span>';
	$("#" + que_div_id).append(cont_html);
	var isRequired_html = '<span id="isRequired" class="hide">' + isRequired + '</span>';
	$("#" + que_div_id).append(isRequired_html);
	$("#" + que_div_id).append('<div class="right">'+butt_html+'</div>');
	$("#" + que_div_id).append(queType_html);
	if (chioceNum >= 1) {
		var code1 = 65;
		for ( var i = 1; i <= chioceNum; i++) {
			char = String.fromCharCode(code1);
			$("#" + que_div_id).append(
					'<span class="hide" id="option' + char + '">'
							+ $.trim($("#" + char).next().val()).escapeHTML() + '</span>');
			code1++;
		}
	}
	if ($("#isHasMaxPoint option:selected").val()=="0") {
		;
	} else {
		
		if (chioceNum >= 1) {
			var code = 65;
			var point = 0;
			if(questionType == "1")
			{
			for ( var i = 1; i <= chioceNum; i++) {
				char = String.fromCharCode(code);
				$("#" + que_div_id).append(
						'<span class="hide" id="point' + char + '">'
								+ $.trim($("#point_" + char).val()) + '</span>');
				
				code++;
				if(point <= Number($.trim($("#point_" + char).val()))){
					point = $.trim($("#point_" + char).val());
				}
			}
			}else{
				for ( var i = 1; i <= chioceNum; i++) {
					char = String.fromCharCode(code);
					$("#" + que_div_id).append(
							'<span class="hide" id="point' + char + '">'
									+ $.trim($("#point_" + char).val()) + '</span>');
					code++;
					point = point + Number($.trim($("#point_" + char).val()));
					}
				}
			
			}
		var point_html = '<span id="point" class="hide">' + point+ '</span>';
		$("#" + que_div_id).append(point_html);
		}
	$("#" + que_div_id).find("#delete").bind("click", function() {
			deleteQuestion($(this).parent().parent().attr("id"));
	});
	$("#" + que_div_id).find("#moveUp").bind("click", function() {
		moveUp($(this).parent().parent().attr("id"));
	});
	$("#" + que_div_id).find("#moveDown").bind("click", function() {
		moveDown($(this).parent().parent().attr("id"));
	});
	$("#" + que_div_id).find("#edit").bind("click", function() {
		editQuestion($(this).parent().parent().attr("id"));
	});
	$("#errorDiv1").hide();
	BINOLMOCIO03_code = 65;
	choiceDialogClose();
	$("#addChoice_dialog #optionDiv").html("");
}

function saveEditFillQuestion(questionType) {
	$('#errorSpan4').html("");
	$("#errorDiv4").hide();
	var isRequired = $("#fill_isRequired").val();
	if ($.trim($("#add_fillQuestionItem").val()) == "") {
		$('#errorSpan4').html($('#errmsg1').val());
		$("#errorDiv4").show();
		return false;
	}
	if($("#fillQuestion_maxPoint").is(":hidden") == false){
		if ($.trim($("#fillQuestion_point").val()) == "") {
			$('#errorSpan4').html($('#errmsg4').val());
			$("#errorDiv4").show();
			return false;
		}
		if (isPositiveFloat(Number($.trim($("#fillQuestion_point").val()))) == false) {
			$('#errorSpan4').html($('#errmsg3').val());
			$("#errorDiv4").show();
			return false;
		}
	}
	var que_div_id = $("#que_div_id").html();
	//记录编辑之前的按钮布局
	var butt_html = $("#"+que_div_id).find("div").filter(".right");
	var arr = que_div_id.split("_");
	$("#" + que_div_id).html("");
	var cont_html = '<div class="left" style="width:430px"><span id="displayOrder" class="highlight">'
			+ arr[1]
			+ '</span>：<span id="questionItem">'
			+ $.trim($("#add_fillQuestionItem").val()).escapeHTML() + '</span></div>';
	var queType_html = '<span id="questionType" class="hide">' + questionType + '</span>';
	var isRequired_html = '<span id="isRequired" class="hide">' + isRequired + '</span>';
	$("#" + que_div_id).append(cont_html);
	$("#" + que_div_id).append(butt_html);
	$("#" + que_div_id).append(queType_html);
	$("#" + que_div_id).append(isRequired_html);
	if ($("#isHasMaxPoint option:selected").val()=="0") {
		;
	} else {
		var point_html = '<span id="point" class="hide">' + Number($.trim($(
				"#fillQuestion_point").val())) + '</span>';
		$("#" + que_div_id).append(point_html);
	}
	$("#" + que_div_id).find("#delete").bind("click", function() {
			deleteQuestion($(this).parent().parent().attr("id"));
	});
	$("#" + que_div_id).find("#moveUp").bind("click", function() {
		moveUp($(this).parent().parent().attr("id"));
	});
	$("#" + que_div_id).find("#moveDown").bind("click", function() {
		moveDown($(this).parent().parent().attr("id"));
	});
	$("#" + que_div_id).find("#edit").bind("click", function() {
		editQuestion($(this).parent().parent().attr("id"));
	});
	$("#errorDiv4").hide();
	fillDialogClose();
	$("#addFill_dialog #optionDiv").html("");
}
/**
 * 设置问卷预览的dialog
 * 
 * 
 * 
 */
function setPreviewDialog(begin_html) {
	$("#previewPaper_dialog").dialog( {
		autoOpen : false,
		draggable : true,
		resizable : false,
		modal : true,
		title : $("#preview").val(),
		height : 630,
		width : 550,
		position:['bottom'],
		buttons : [{
			text:$("#close").val(),
			click : function() {
			previewDialogClose();
			$("#previewPaper_dialog").html(begin_html);
			}
		}],
		close : function(event, ui) {
			previewDialogClose();
			$("#previewPaper_dialog").html(begin_html);
		}
	});
}

/**
 * 问卷预览
 * 
 * 
 * 
 */
function previewPaper() {
	var begin_html = $("#previewPaper_dialog").html();
	setPreviewDialog(begin_html);
	$("#previewPaper_dialog").dialog("open");
	var begin_html = $("#previewPaper_dialog").html();
	showPaperMain();
	showQuestion();
}
/**
 * 展示问卷主要信息
 * 
 * 
 * 
 */

function showPaperMain() {
	$("#preview_brandInfoId").append(
			" " + $("#brandInfoId option:selected").text());
	// alert(0);
	$("#preview_paperType").append(" " + $("#paperType option:selected").text());
	$("#preview_paperName").append(" " + $.trim($("#paperName").val()).escapeHTML());
	var quesNum = $("#alreadyAddQuestion_body").children().length;
	var question = new Array();
	var maxPoint = 0;
	for ( var i = 1; i <= quesNum; i++) {
		$("#question_" + i).find("span").each(function() {
			if($(this).attr("id")=="point"){
				maxPoint = maxPoint + Number($(this).html());
			}
		});
	}
	$("#preview_maxPoint").append(" " + maxPoint);
	if ($.trim($("#startDate").val()) == "") {
		$("#preview_date").append(" ");
	} else {
		$("#preview_date").append(
				" " + $.trim($("#startDate").val()).escapeHTML() +' '+ $("#to").val()+' ' + $.trim($("#endDate").val()).escapeHTML());
	}

}
function previewDialogClose() {
//	closeCherryDialog('previewPaper_dialog');
	$("#previewPaper_dialog").dialog('destroy');
	$("#previewPaper_dialog").remove();
	$("#previewPaper_dialog_main").append(previewPaper_dialog_main);
}
function showQuestion() {
	var questionItem = null;
	var displayOrder = null;
	var questionType = null;
	var isRequired = null;
	var point = null;
	$("#alreadyAddQuestion_body div[id^=question_]")
			.each(
					function(i) {
						questionItem = $(this).find("#questionItem").html();
						displayOrder = $(this).find("#displayOrder").html();
						questionType = $(this).find("#questionType").html();
						isRequired = $(this).find("#isRequired").html();
						var div_id = $(this).attr("id");
						if ($(this).find("#point").html() != null) {
							point = $(this).find("#point").html();
							var html1_1 = '<div style="margin:0px 0px 10px 0px;padding:10px" class="box4 clearfix" id="que_detail'+i+'"><span style="margin-bottom:5px;" class="clearfix"><strong><span class="highlight">'
									+ displayOrder
									+ '</span>: '
									+ questionItem
									+ '('
									+ point + '分)';
							var html1_2 = '';
							if(null != isRequired && isRequired == '1') {
								html1_2 = '[<span class="red">'+BINOLMOCIO03_js_i18n.required+'</span>]'; 
							}
							var html1_3 = '</strong></span></div>';
							$("#preview_question").append(html1_1+html1_2+html1_3);
							if (questionType == "1") {
								var html2 = '<div class="clearfix left" id="' + i + '"></div>';
								$("#que_detail"+i).append(html2);
								$(this)
										.find("span[id^=option]")
										.each(
												function() {
													var id = $(this).attr("id");
													var idArr = id.split("");
													var choicePoint = $(
															"#"
																	+ div_id
																	+ " #point"
																	+ idArr[idArr.length - 1])
															.html();
													var html3 = '<div class = "clearfix"><input type="radio" disabled="disabled"/><span>'
															+ idArr[idArr.length - 1]
															+ '：'
															+ $(this).html()
															+ '（'
															+ choicePoint
															+ '分）</span></div>';
													$("#" + i).append(html3);
												});
							} else if (questionType == "2") {
								var html2 = '<div class="clearfix left" id="' + i + '"></div>';
								$("#que_detail"+i).append(html2);
								$(this)
										.find("span[id^=option]")
										.each(
												function() {
													var id = $(this).attr("id");
													var idArr = id.split("");
													var choicePoint = $(
															"#"
																	+ div_id
																	+ " #point"
																	+ idArr[idArr.length - 1])
															.html();
													var html3 = '<div class = "clearfix"><input type="checkBox" disabled="disabled"/><span>'
															+ idArr[idArr.length - 1]
															+ '：'
															+ $(this).html()
															+ '（'
															+ choicePoint
															+ '分）</span></div>';
													$("#" + i).append(html3);
												});
							} else if (questionType == "0") {
								var html2 = '<div class="clearfix left" id="' + i + '">_____________________________________________________________</div>';
								$("#que_detail"+i).append(html2);
							}
						} else {
							var html1_1 = '<div style="margin:0px 0px 10px 0px;padding:10px" class="box4 clearfix" id="que_detail'+i+'"><span style="margin-bottom:5px;" class="clearfix"><strong><span class="highlight">'
									+ displayOrder
									+ '</span>: '
									+ questionItem;
							var html1_2 = '';
							if(null != isRequired && isRequired == '1') {
								html1_2 = '[<span class="red">'+BINOLMOCIO03_js_i18n.required+'</span>]'; 
							}
							var html1_3 = '</strong></span></div>';
							$("#preview_question").append(html1_1+html1_2+html1_3);
							if (questionType == "1") {
								var html2 = '<div class="clearfix left" id="' + i + '"></div>';
								$("#que_detail"+i).append(html2);
								$(this)
										.find("span[id^=option]")
										.each(
												function() {
													var id = $(this).attr("id");
													var idArr = id.split("");
													var html3 = '<div class = "clearfix"><input type="radio" disabled="disabled"/><span>'
															+ idArr[idArr.length - 1]
															+ '：'
															+ $(this).html()
															+ '</span></div>';
													$("#" + i).append(html3);
												});
							} else if (questionType == "2") {
								var html2 = '<div class="clearfix left" id="' + i + '"></div>';
								$("#que_detail"+i).append(html2);
								$(this)
										.find("span[id^=option]")
										.each(
												function() {
													var id = $(this).attr("id");
													var idArr = id.split("");
													var html3 = '<div class = "clearfix"><input type="checkBox" disabled="disabled"/><span>'
															+ idArr[idArr.length - 1]
															+ '：'
															+ $(this).html()
															+ '</span></div>';
													$("#" + i).append(html3);
												});
							} else if (questionType == "0") {
								var html2 = '<div class="clearfix left" id="' + i + '">_____________________________________________________________</div>';
								$("#que_detail"+i).append(html2);
							}
						}
//						$("#preview_question").append(
//								"<p class='clearfix'> </p>");
					});
}

/**
 * 限制问题输入框中的字符大小
 * 
 * 
 * */
function obj(a){
	if($.trim($(a).val()).length>60){
		var str = $.trim($(a).val()).substring(0, 60);
		$(a).val(str);
	}
}
function isPositiveFloat(s){
	var patrn=/^((([1-9][0-9]{0,4}|0.[0-9]{1,2})|[1-9][0-9]{0,4}\.[0-9]{1,2})|0)$/; 
	if (!patrn.exec(s)) return false;
	return true;
}

function cio03isHour(obj,flag){
	var value = obj.value.toString();
	var patrn=/^([0][0-9])|([1][0-9])|([2][0-3])$/;
	if (!patrn.exec(value)) {
		if(flag){
			obj.value="23";
		}else{
			obj.value="00";
		}
	}
}

function cio03isMinOrSec(obj,flag){
	var value = obj.value.toString();
	var patrn=/^[0-5][0-9]$/;
	if (!patrn.exec(value)) {
		if(flag){
			obj.value="59";
		}else{
			obj.value="00";
		}
	}
}

function textCounter(obj, maxLength){
	var str = obj.value;
	str = subString(str,maxLength);
	obj.value = str;
}