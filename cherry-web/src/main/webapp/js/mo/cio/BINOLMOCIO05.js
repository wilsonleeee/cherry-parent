/**
 * @author user3
 */
var paperId;
var BINOLMOCIO05_point;
window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};

/**
 * 动态计算总分
 * 
 * 
 * */
var calculationMaxPoint = function(){
	if(BINOLMOCIO05_point != 0){
		var maxPoint = 0;
		var qustOjb = eval('(' + questionList + ')');
		for(var k in qustOjb){
			if(k != "remove"){
				maxPoint = maxPoint + Number(qustOjb[k].point);
			}
		}
		maxPoint = Math.round(maxPoint*100)/100;
		$("#maxPoint").val(maxPoint);
	}
};
/**
 * 问题导航加载
 * 
 * 
 * */
function quesNaviLoad(str){
	$("#showQuesMain_body").html("");
	if(typeof str == "string"){
		var qustOjb = eval('(' + str + ')');
	}else{
		var qustOjb = str;
	}
	if(qustOjb[0] == null || qustOjb[0].paperId == null){ return;}
	paperId = qustOjb[0].paperId;
	BINOLMOCIO05_point = qustOjb[0].point;
	var length = qustOjb.length;
	var showQuesMain_body_html = "";
	for(var i = 1; i <= length; i++){
		var quesType = qustOjb[i-1].questionType;
		// 此问题是否必答
		var isRequired = qustOjb[i-1].isRequired;
		var title = "";
		if(quesType == "0"){
			title = $("#assay_title").html();
		}else if(quesType == "1"){
			title = $("#sinChoice_title").html();
		}else if(quesType == "2"){
			title = $("#mulChioce_title").html();
		}else{
			title = $("#apFill_title").html();
		}
		var str = "";
		if(i == 1){//第一个问题加载的内容
			str = '<div class="clearfix" style="border-bottom: 1px dashed #ccc; padding-bottom: 5px; margin-bottom: 10px;"><div class="left" style="width:430px"><span id='+qustOjb[i-1].displayOrder+' title='+title+' style="cursor:default"><span class="highlight">'+qustOjb[i-1].displayOrder+'</span>：'+qustOjb[i-1].questionItem.escapeHTML()+'</span></div><div class="right"><a class="left" id="edit_'+qustOjb[i-1].displayOrder+'" title='+BINOLMOCIO05_js_i18n.edit+' style="cursor:pointer"><span class="ui-icon icon-edit"></span></a><a class="left" id="delete_'+qustOjb[i-1].displayOrder+'" title='+BINOLMOCIO05_js_i18n.deleteButton+' style="cursor:pointer"><span class="ui-icon icon-delete"></span></a><span id="moveUp_'+qustOjb[i-1].displayOrder+'"><span class="left" style="height:16px; width:16px; display:block;"></span></span><span id="moveDown_'+qustOjb[i-1].displayOrder+'"><span class="left" style="height:16px; width:16px; display:block;"></span></span></div></div>';
		}else if(i > 1){//从第二个问题开始加载的内容
			$("#moveDown_"+qustOjb[i-2].displayOrder).html('<a class="left" title='+BINOLMOCIO05_js_i18n.moveDown+' style="cursor:pointer"><span class="arrow-down"></span></a>');
			str = '<div class="clearfix" style="border-bottom: 1px dashed #ccc; padding-bottom: 5px; margin-bottom: 10px;"><div class="left" style="width:430px"><span id='+qustOjb[i-1].displayOrder+' title='+title+' style="cursor:default"><span class="highlight">'+qustOjb[i-1].displayOrder+'</span>：'+qustOjb[i-1].questionItem.escapeHTML()+'</span></div><div class="right"><a class="left" id="edit_'+qustOjb[i-1].displayOrder+'" title='+BINOLMOCIO05_js_i18n.edit+' style="cursor:pointer"><span class="ui-icon icon-edit"></span></a><a class="left" id="delete_'+qustOjb[i-1].displayOrder+'" title='+BINOLMOCIO05_js_i18n.deleteButton+' style="cursor:pointer"><span class="ui-icon icon-delete"></span></a><span id="moveUp_'+qustOjb[i-1].displayOrder+'"><a class="left" title='+BINOLMOCIO05_js_i18n.moveUp+' style="cursor:pointer"><span class="arrow-up"/></a></span><span id="moveDown_'+qustOjb[i-1].displayOrder+'"><span class="left" style="height:16px; width:16px; display:block;"></span></span></div></div>';
		}else{
			;
		}
		//showQuesMain_body_html = showQuesMain_body_html + str;
		$("#showQuesMain_body").append(str);
		$("#showQuesMain_body").find("#edit_"+qustOjb[i-1].displayOrder).click(function(){
				setEditQuesDialog();
				$("#questionContent_body").dialog("open");
				showEditBox(this);
			});
		$("#showQuesMain_body").find("#delete_"+qustOjb[i-1].displayOrder).bind("click",function(){
			var this_id = $(this).attr("id");
			var indexArr = this_id.split("_");
			var index = Number(indexArr[1])-1;
			deleteQuestion(qustOjb,index);
		});
		//上移按钮事件绑定
		$("#showQuesMain_body").find("#moveUp_"+qustOjb[i-1].displayOrder).click(function(){
			var this_id = $(this).attr("id");
			var indexArr = this_id.split("_");
			var index = Number(indexArr[1])-1;
			moveUp(qustOjb,index);
		});
		//下移钮事件绑定
		$("#showQuesMain_body").find("#moveDown_"+qustOjb[i-1].displayOrder).click(function(){
			var this_id = $(this).attr("id");
			var indexArr = this_id.split("_");
			var index = Number(indexArr[1])-1;
			moveDown(qustOjb,index);
		});
	}
	//$("#showQuesMain_body").html(showQuesMain_body_html);
//	$("#showQuesMain_body").find("a").each(function(){
//		$(this).click(function(){
//			showEditBox(this);
//		});
//	});
}

/**
 * 显示问题
 * 
 * 
 * */
function showEditBox(obj){
	var qustOjb = eval('(' + questionList + ')'); 
	$("#questionContent_body").html("");
	$("#questionContent_body").append('<div id="errorDiv" class="actionError" style="display:none"><ul><li><span id="errorSpan"></span></li></ul></div>');
	var index1 = $(obj).attr("id");
	var index = index1.split("_");
	var editObj = qustOjb[index[1]-1];
	var questionType = editObj.questionType;
	// 此问题是否必须回答
	var isRequired = editObj.isRequired;
	
	$("#questionContent_body").append($("#questionHeader_model").html());
	$("#questionContent_body #displayOrder").focusout(function() {
		reSortDisplayOrder(Number($.trim($(this).val())),index[1]);
	});
	// 问题类型下拉框
	$("#questionContent_body #questionTypeChoice").val(questionType);

	$("#questionContent_body #numberShow").html(editObj.displayOrder);
	$("#questionContent_body #displayOrder").val(editObj.displayOrder);
	// 问题类型改变
	$("#questionContent_body #questionTypeChoice").change(function(){
		// 改变编辑对象的问题类型后重新加载页面
		editQuestionTypeChange(editObj);
	});
	
	// 加载编辑对象的原始数据
	if(questionType == "0" || questionType == "3"){
		$("#questionContent_body").append($("#noChoiQuesCon_model").html());
		// 是否必须回答赋值
		$("#questionContent_body #isRequired").val(isRequired);
		$("#questionContent_body #point").val(editObj.point);
		if(BINOLMOCIO05_point == 0){
			$("#questionContent_body #questionPoint").hide();
		}
		$("#questionContent_body #questionItem").val(editObj.questionItem);
	}else{
		$("#questionContent_body").append($("#choiQuesCon_model").html());
		// 是否必须回答赋值
		$("#questionContent_body #isRequired").val(isRequired);
		$("#questionContent_body #questionItem").val(editObj.questionItem);
		var chioceNum = 0;
		for(var k in editObj){
			if(k.indexOf("option")>-1){
				chioceNum ++;
			}
		}
		if(BINOLMOCIO05_point == 0.00){
			$("#questionContent_body #point").hide();
			for(var charNum = 65 ;charNum <= 64+Number(chioceNum);charNum++){ 
				var choiceChar = String.fromCharCode(charNum);
				var choice = "option"+choiceChar;
				var choice_html='<div class="clearfix" style="margin:0px 10px 5px 10px" id="choice_'+choiceChar+'"><span class="left" style="margin-top:3px" id="'+choiceChar+'">'+choiceChar+'：</span><input type="text" class="text left" id="'+choice+'" style="width:350px" value="'+editObj[choice]+'" maxlength="300" /><a id="delete" class="left" style="margin-top:3px" title='+BINOLMOCIO05_js_i18n.deleteButton+'><span class="ui-icon icon-delete"></span></a></div>';
				$("#questionContent_body #question_content").append(choice_html);
				$("#choice_"+choiceChar).find("#delete").bind("click",function(){
					deleteChoice($(this).parent().attr("id"));
				});
			}
			
		}else{
			for(var charNum = 65 ;charNum <= 64+Number(chioceNum);charNum++){
				var choiceChar = String.fromCharCode(charNum);
				var choice = "option"+choiceChar;
				var point = "point"+choiceChar;
				var choice_html='<div class="clearfix" style="margin:0px 10px 5px 10px" id="choice_'+choiceChar+'"><span id="'+choiceChar+'">'+choiceChar+'：</span><input type="text" class="text" id="'+choice+'" style="width:350px" value="'+editObj[choice]+'" maxlength="300" /><span>分值：</span><input type="text" class="text1" id="'+point+'" value="'+editObj[point]+'"/><a id="delete" class="right" title="删除"><span class="ui-icon icon-delete"></span></a></div>';
				$("#questionContent_body #question_content").append(choice_html);
				$("#choice_"+choiceChar).find("#delete").bind("click",function(){
					deleteChoice($(this).parent().attr("id"));
				});
			}
		}
		$("#questionContent_body #addChoice").click(function(){
			addChoice(editObj);
		});
	}
//	$("#questionContent_body #questionItem").keyup(function(){
//		controlStrLength(this);
//		
//	});
//	$("#questionContent_body #questionItem").bind('paste',function(){
//		window.setTimeout('$(\'#questionContent_body #' + this.id + '\').trigger(\'keyup\')', 1);
//	});
}

/**
 * 编辑问题时问题类型改变动作
 * @param editObj : 编辑对象
 */
function editQuestionTypeChange(editObj) {
	// 此问题是否必须回答
	var isRequired = $("#questionContent_body #isRequired").val();
	// 切换问题类型后
	var questionType_temp = $("#questionContent_body #questionTypeChoice").val();
	// 将原来的页面内容清除重新加载
	$("#questionContent_body").find("#question_content").remove();
	if(questionType_temp == "0" || questionType_temp == "3"){
		$("#questionContent_body").append($("#noChoiQuesCon_model").html());
		// 是否必须回答赋值
		$("#questionContent_body #isRequired").val(isRequired);
		$("#questionContent_body #point").val(editObj.point);
		if(BINOLMOCIO05_point == 0){
			$("#questionContent_body #questionPoint").hide();
		}
		$("#questionContent_body #questionItem").val(editObj.questionItem);
	}else{
		$("#questionContent_body").append($("#choiQuesCon_model").html());
		// 是否必须回答赋值
		$("#questionContent_body #isRequired").val(isRequired);
		$("#questionContent_body #questionItem").val(editObj.questionItem);
		var chioceNum = 0;
		for(var k in editObj){
			if(k.indexOf("option")>-1){
				chioceNum ++;
			}
		}
		if(BINOLMOCIO05_point == 0.00){
			$("#questionContent_body #point").hide();
			for(var charNum = 65 ;charNum <= 64+Number(chioceNum);charNum++){ 
				var choiceChar = String.fromCharCode(charNum);
				var choice = "option"+choiceChar;
				var choice_html='<div class="clearfix" style="margin:0px 10px 5px 10px" id="choice_'+choiceChar+'"><span class="left" style="margin-top:3px" id="'+choiceChar+'">'+choiceChar+'：</span><input type="text" class="text left" id="'+choice+'" style="width:350px" value="'+editObj[choice]+'" maxlength="300" /><a id="delete" class="left" style="margin-top:3px" title='+BINOLMOCIO05_js_i18n.deleteButton+'><span class="ui-icon icon-delete"></span></a></div>';
				$("#questionContent_body #question_content").append(choice_html);
				$("#choice_"+choiceChar).find("#delete").bind("click",function(){
					deleteChoice($(this).parent().attr("id"));
				});
			}
			
		}else{
			for(var charNum = 65 ;charNum <= 64+Number(chioceNum);charNum++){
				var choiceChar = String.fromCharCode(charNum);
				var choice = "option"+choiceChar;
				var point = "point"+choiceChar;
				var choice_html='<div class="clearfix" style="margin:0px 10px 5px 10px" id="choice_'+choiceChar+'"><span id="'+choiceChar+'">'+choiceChar+'：</span><input type="text" class="text" id="'+choice+'" style="width:350px" value="'+editObj[choice]+'" maxlength="300" /><span>分值：</span><input type="text" class="text1" id="'+point+'" value="'+editObj[point]+'"/><a id="delete" class="right" title="删除"><span class="ui-icon icon-delete"></span></a></div>';
				$("#questionContent_body #question_content").append(choice_html);
				$("#choice_"+choiceChar).find("#delete").bind("click",function(){
					deleteChoice($(this).parent().attr("id"));
				});
			}
		}
		$("#questionContent_body #addChoice").click(function(){
			addChoice(editObj);
		});
	}
}
/**
 * 
 * 添加选项
 * 
 * */
function addChoice(obj){
	var chioceNum = $("input[id^=option]").length;
	if(chioceNum == 20){
		alert(BINOLMOCIO05_js_i18n.addChoiceWarm);
		return false;
	}
	var charNum = 65+Number(chioceNum);
	var choiceChar = String.fromCharCode(charNum);
	var choice = "option"+choiceChar;
	if(BINOLMOCIO05_point == 0){
		$("#questionContent_body #point").hide();
		var choice_html='<div class="clearfix" style="margin:0px 10px 5px 10px" id="choice_'+choiceChar+'"><span class="left" style="margin-top:3px" id="'+choiceChar+'">'+choiceChar+'：</span><input type="text" class="text left" id="'+choice+'" style="width:350px" value="" maxlength="300" /><a id="delete" class="left" style="margin-top:3px" title='+BINOLMOCIO05_js_i18n.deleteButton+'><span class="ui-icon icon-delete"></span></a></div>';
		$("#questionContent_body #question_content").append(choice_html);
		$("#choice_"+choiceChar).find("#delete").bind("click",function(){
			deleteChoice($(this).parent().attr("id"));
		});
	}else{
		var point = "point"+choiceChar;
		var choice_html='<div class="clearfix" style="margin:0px 10px 5px 10px" id="choice_'+choiceChar+'"><span id="'+choiceChar+'">'+choiceChar+'：</span><input type="text" class="text" id="'+choice+'" style="width:350px" value="" maxlength="300" /><span>'+BINOLMOCIO05_js_i18n.point+'：</span><input type="text" class="text1" id="'+point+'" value=""/><a id="delete" class="right" title='+BINOLMOCIO05_js_i18n.deleteButton+'><span class="ui-icon icon-delete"></span></a></div>';
		$("#questionContent_body #question_content").append(choice_html);
		$("#choice_"+choiceChar).find("#delete").bind("click",function(){
			deleteChoice($(this).parent().attr("id"));
		});
	}
}
/**
 * 删除选项
 * 
 * 
 * */
function deleteChoice(choiceDiv){
	$("#"+choiceDiv).remove();
	var chioceNum = $("input[id^=option]").length;
	if(chioceNum > 0){
		var charNum = 65;
		$("input[id^=option]").each(function(){
			var char = String.fromCharCode(charNum);
			var id = $(this).attr("id");
			var idArr = id.split("");
			$(this).parent().attr("id","choice_"+char);
			$(this).prev().attr("id",char);
			$(this).prev().html(char+'：');
			if($("#point"+idArr[idArr.length-1]) != undefined){
				$("#point"+idArr[idArr.length-1]).attr("id","point"+char);
			}
			$(this).attr("id","option"+char);
			charNum++;
		});
	}
}
/**
 * 删除问题
 *  
 * 
 * 
 * */
function deleteQuestion(obj,key){
	$("#questionContent_body").html("");
	obj.remove(key);
	for(var i in obj){
		obj[i].displayOrder = Number(i)+1;
	}
	questionList = JSON2.stringify(obj);
	quesNaviLoad(obj);
}
/* 方法:Array.remove(dx)  
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
 * 保存问题为object
 * 
 * 
 * 
 * */
function saveQuestion(){
	var obj = eval('(' + questionList + ')');
	var questionNum = obj.length;
	if(Number($.trim($("#questionContent_body #displayOrder").val()))>questionNum || Number($.trim($("#questionContent_body #displayOrder").val())) < 1){
		alert(BINOLMOCIO05_js_i18n.reSortDisplayOrderWarm);
	}else{
		var key = Number($.trim($("#questionContent_body #displayOrder").val())) - 1;
		var queContObj = obj[key];
		var questionType = $("#questionContent_body #questionTypeChoice").val();
		var paperId = queContObj.paperId;
		$("#questionContent_body #errorSpan").html("");
		$("#questionContent_body #errorDiv").hide();
		if($.trim($("#questionContent_body #questionItem").val()) == ""){
			$("#questionContent_body #errorSpan").html($("#errmsg1").val());
			$("#questionContent_body #errorDiv").show();
			return false;
		}else{
			$("#questionContent_body #errorSpan").html("");
			$("#questionContent_body #errorDiv").hide();
		}
		try{
			// 未添加选项报错
			if((questionType == "1" || questionType == "2") && $("#questionContent_body input[id^=option]").length == 0){
				throw "ex0";
			}
			// 选项内容为空报错
			$("#questionContent_body input[id^=option]").each(function(){
				if($.trim($(this).val()) == ""){
					throw "ex1";
				}
			});
			$("#questionContent_body input[id^=point]").each(function(){
				if(!$(this).is(":hidden") && $.trim($(this).val()) == ""){
					throw "ex2";
				}
				// 分值就为浮点数
				if(isPositiveFloat(Number($.trim($(this).val()))) == false){
					throw "ex3";
				}
			});
			if($.trim($("#questionContent_body #displayOrder").val()).length == 0 || isNaN($.trim($("#questionContent_body #displayOrder").val()))){
				throw "ex4";
			}
		}catch(e){
			if(e == "ex0" || e == "ex1"){
				$("#questionContent_body #errorSpan").html($("#errmsg2").val());
				$("#questionContent_body #errorDiv").show();
				return false;
			}
			if(e == "ex2"){
				$("#questionContent_body #errorSpan").html($("#errmsg4").val());
				$("#questionContent_body #errorDiv").show();
				return false;
			}
			if(e == "ex3"){
				$("#questionContent_body #errorSpan").html($("#errmsg3").val());
				$("#questionContent_body #errorDiv").show();
				return false;
			}
			if(e == "ex4"){
				$("#questionContent_body #errorSpan").html($("#errmsg5").val());
				$("#questionContent_body #errorDiv").show();
				return false;
			}
		}
		
		var o = {
				questionType:questionType,
				paperId:paperId,
				displayOrder:Number($.trim($("#questionContent_body #displayOrder").val())),
				isRequired : $("#questionContent_body #isRequired").val(),
				questionItem:$.trim($("#questionContent_body #questionItem").val())
			};
		// 选择题的情况
		if(questionType == "1" || questionType == "2"){
			$("#questionContent_body input[id^=option]").each(function(){
				o[$(this).attr("id")] = $.trim($(this).val());
			});
			$("#questionContent_body input[id^=point]").each(function(){
				o[$(this).attr("id")] = $.trim($(this).val());
			});
		}
		
		if(BINOLMOCIO05_point != 0 ){
			var point = 0;
			if(questionType == "0" || questionType == "3"){
				// 问答题或者填空题直接取总分值
				o.point=$.trim($("#questionContent_body #point").val());
			}else if(questionType == "1"){
				// 单选题取选项中的最大分值作为问题分值
				$("#questionContent_body input[id^=point]").each(function(){
					if(Number($.trim($(this).val())) > Number(point)){
						point = Number($.trim($(this).val()));
					}
				});
				o.point = Number(point);
			}else{
				// 多选题取各个选项分值之和
				$("#questionContent_body input[id^=point]").each(function(){
					point = Number($.trim($(this).val())) + Number(point);
				});
				o.point = Number(point);
			}
		}else{
			// 非计分问卷取分值为0
			o.point = 0;
		}
		obj[key] = o;
		questionList = JSON2.stringify(obj);
		quesNaviLoad(obj);
		calculationMaxPoint();
		closeDialog();
		$("#questionContent_body").html("");
	}
}

/**
 * 重置问题的displayOrder
 * @param newIndex 新的位置
 * @param oldIndex 旧的的位置
 * 
 * 
 * */
function reSortDisplayOrder(newLocation,oldLocation){
	$("#questionContent_body #errorSpan").html("");
	$("#questionContent_body #errorDiv").hide();
	var qustOjb = eval('(' + questionList + ')');
	var questionNum = qustOjb.length;
	if($.trim(newLocation).length == 0){
		$("#questionContent_body #errorSpan").html($("#errmsg5").val());
		$("#questionContent_body #errorDiv").show();
		return false;
	}
	if(isNaN(newLocation)){
		$("#questionContent_body #errorSpan").html($("#errmsg5").val());
		$("#questionContent_body #errorDiv").show();
		return false;
	}
	if(Number(newLocation)>questionNum || Number(newLocation) < 1){
		alert(BINOLMOCIO05_js_i18n.reSortDisplayOrderWarm);
		return false;
	}
	var newIndOfObj = Number(newLocation) - 1;
	var oldIndOfObj = Number(oldLocation) - 1;
	qustOjb[oldIndOfObj].displayOrder = Number(newLocation);
	var o = qustOjb[oldIndOfObj];
	if(newIndOfObj > oldIndOfObj){
		for(var i = oldIndOfObj; i<newIndOfObj ; i++){
			qustOjb[i+1].displayOrder = Number(qustOjb[i+1].displayOrder) - 1;
			qustOjb[i] = qustOjb[i+1];
		}
		qustOjb[newIndOfObj] = o;
	}
	if(oldIndOfObj>newIndOfObj){
		for(var i = oldIndOfObj; i > newIndOfObj;i--){
			qustOjb[i-1].displayOrder = Number(qustOjb[i-1].displayOrder) + 1;
			qustOjb[i] = qustOjb[i-1];
		}
		qustOjb[newIndOfObj] = o;
	}
	questionList = JSON2.stringify(qustOjb);
	$("#questionContent_body #numberShow").html(newLocation); 
	quesNaviLoad(questionList);
}

/**
 * 添加问题
 * (此方法已不再使用)
 * 
 * */
//function addQuestion(obj){
//	var opos = $(obj).offset();
//	var oleft = Number(opos.left)-60;
//	var otop = parseInt(opos.top + $(obj).outerHeight(), 10);
//	if($('#makeSureQuesType').is(":hidden")){
//		$('#makeSureQuesType').css({'left': oleft + "px", 'top': otop + "px" }).show();
//	}else{
//		$('#makeSureQuesType').hide();
//	}
//}

/**
 * 显示问题添加框
 * 
 * 
 * */
function showAddQuesDialog(){
//	$('#makeSureQuesType').hide();
	setAddQuesDialog();
	$("#questionContent_body").dialog("open");
	dialogFill();
}
/**
 * 设置编辑问题dialog
 * 
 * 
 * */
function setEditQuesDialog(){
	$("#questionContent_body").html("");
	$("#questionContent_body").dialog( {
		autoOpen : false,
		draggable : true,
		resizable : false,
		modal : true,
		title : BINOLMOCIO05_js_i18n.editTitle,
		height : 378,
		width : 600,
		buttons : [{
			text:BINOLMOCIO05_js_i18n.saveButton,
			click:function(){
				saveQuestion();
			}
		}],
		close : function(event, ui) {
		closeDialog();
		$("#questionContent_body").html("");
		quesNaviLoad(questionList);
		}
	});
}
/**
 * 设置添加问题dialog
 * 
 * 
 * */
function setAddQuesDialog(){
	$("#questionContent_body").html("");
	$("#questionContent_body").dialog( {
		autoOpen : false,
		draggable : true,
		resizable : false,
		modal : true,
		title : BINOLMOCIO05_js_i18n.addTitle,
		height : 378,
		width : 600,
		buttons : [{
			text:BINOLMOCIO05_js_i18n.saveButton,
			click:function(){
				saveAddQuestion();
			}
		}],
		close : function(event, ui) {
			closeDialog();
			$("#questionContent_body").html("");
			quesNaviLoad(questionList);
		}
	});
}
/**
 * 添加问题dialog填充
 * @param questionType
 * 
 * */
function dialogFill(){
	var qustOjb = eval('(' + questionList + ')');
	var displayOrder = Number(qustOjb.length)+1;
	$("#questionContent_body").append('<div id="errorDiv" class="actionError" style="display:none"><ul><li><span id="errorSpan"></span></li></ul></div>');
	$("#questionContent_body").append($("#questionHeader_model").html());
	$("#questionContent_body #questionTypeChoice").change(function(){
		questionTypeChange();
	});
	// 问题类型
	var questionType = $("#questionContent_body #questionTypeChoice").val();
	if(questionType == "1" || questionType == "2"){
		// 选择题有添加按钮
		$("#questionContent_body").append($("#choiQuesCon_model").html());
		$("#questionContent_body #addChoice").click(function(){
			addChoice();
		});
	}else{
		$("#questionContent_body").append($("#noChoiQuesCon_model").html());
		if(BINOLMOCIO05_point == 0){
			$("#questionContent_body #questionPoint").hide();
		}
	}
	$("#questionContent_body #numberShow").html(displayOrder);
	$("#questionContent_body #displayOrder").val(displayOrder).attr("disabled",true);
//	$("#questionContent_body #questionItem").keyup(function(){
//		controlStrLength(this);
//	});
//	$("#questionContent_body #questionItem").bind('paste',function(){
//		window.setTimeout('$(\'#questionContent_body #' + this.id + '\').trigger(\'keyup\')', 1);
//	});
}

/**
 * 新增问题时，问题类型改变动作
 */
function questionTypeChange() {
	var questionType = $("#questionContent_body #questionTypeChoice").val();
	$("#questionContent_body").find("#question_content").remove();
	if(questionType == "1" || questionType == "2"){
		// 选择题有添加按钮
		$("#questionContent_body").append($("#choiQuesCon_model").html());
		$("#questionContent_body #addChoice").click(function(){
			addChoice();
		});
	}else{
		$("#questionContent_body").append($("#noChoiQuesCon_model").html());
		if(BINOLMOCIO05_point == 0){
			$("#questionContent_body #questionPoint").hide();
		}
	}
}

/**
 * 通用关闭警告弹出框
 * @param 	dialogParent:弹出框父标签ID
 * 			dialogDiv:弹出框ID
 * 
 */
function closeWarnDialog(dialogParent,dialogDiv,dialogHtml){
	$("#" + dialogDiv).dialog("destroy");
	$("#" + dialogDiv).remove();
	$("#" + dialogParent).html(dialogHtml);
}

/**
 * 关闭dialog
 * 
 * 
 * */
function closeDialog(){
	$("#questionContent_body").dialog('destroy');
	$("#questionContent_body").remove();
	$("#div_main").append('<div id="questionContent_body"></div>');
}
/**
 * 保存添加的问题
 * 
 * 
 * */
function saveAddQuestion(){
	$("#questionContent_body #errorSpan").html("");
	$("#questionContent_body #errorDiv").hide();
	// 下拉框中选择问题类型
	var questionType = $("#questionContent_body #questionTypeChoice").val();
	if($.trim($("#questionContent_body #questionItem").val()) == ""){
		$("#questionContent_body #errorSpan").html($("#errmsg1").val());
		$("#questionContent_body #errorDiv").show();
		return false;
	}else{
		$("#questionContent_body #errorSpan").html("");
		$("#questionContent_body #errorDiv").hide();
	}
	try{
		if((questionType == "1" || questionType == "2") && $("#questionContent_body input[id^=option]").length == 0){
			throw "ex0";
		}
		$("#questionContent_body input[id^=option]").each(function(){
			if($.trim($(this).val()) == ""){
				throw "ex1";
			}
		});
		$("#questionContent_body input[id^=point]").each(function(){
			if(!$(this).is(":hidden") && $.trim($(this).val()) == ""){
				throw "ex2";
			}
			if(isPositiveFloat(Number($.trim($(this).val()))) == false){
				throw "ex3";
			}
		});
		if($.trim($("#questionContent_body #displayOrder").val()).length == 0 || isNaN($("#questionContent_body #displayOrder").val())){
			throw "ex4";
		}
	}catch(e){
		if(e == "ex0" || e == "ex1"){
			$("#questionContent_body #errorSpan").html($("#errmsg2").val());
			$("#questionContent_body #errorDiv").show();
			return false;
		}
		if(e == "ex2"){
			$("#questionContent_body #errorSpan").html($("#errmsg4").val());
			$("#questionContent_body #errorDiv").show();
			return false;
		}
		if(e == "ex3"){
			$("#questionContent_body #errorSpan").html($("#errmsg3").val());
			$("#questionContent_body #errorDiv").show();
			return false;
		}
		if(e == "ex4"){
			$("#questionContent_body #errorSpan").html($("#errmsg5").val());
			$("#questionContent_body #errorDiv").show();
			return false;
		}
	}
	var displayOrder = $.trim($("#questionContent_body  #displayOrder").val());
	var index = Number(displayOrder)-1;
	var qustOjb = eval('(' + questionList + ')');
	var o = {
			questionType:questionType,
			paperId:paperId,
			displayOrder:Number($.trim($("#questionContent_body #displayOrder").val())),
			isRequired:$("#questionContent_body #isRequired").val(),
			questionItem:$.trim($("#questionContent_body #questionItem").val())
		};
	if(questionType == "1" || questionType == "2"){
		$("#questionContent_body input[id^=option]").each(function(){
			o[$(this).attr("id")] = $.trim($(this).val());
		});
		$("#questionContent_body input[id^=point]").each(function(){
			o[$(this).attr("id")] = $.trim($(this).val());
		});
	}
	if(BINOLMOCIO05_point != 0 ){
		var point = 0;
		if(questionType == "0" || questionType == "3"){
			o.point=$.trim($("#questionContent_body #point").val());
		}else if(questionType == "1"){
			$("#questionContent_body input[id^=point]").each(function(){
				if(Number($.trim($(this).val())) > Number(point)){
					point = Number($.trim($(this).val()));
				}
			});
			o.point = Number(point);
		}else{
			$("#questionContent_body input[id^=point]").each(function(){
				point = Number($.trim($(this).val())) + Number(point);
			});
			o.point = Number(point);
		}
	}
	qustOjb[index] = o;
	questionList = JSON2.stringify(qustOjb);
	quesNaviLoad(qustOjb);
	calculationMaxPoint();
	closeDialog();
	$("#questionContent_body").html("");
}
/**
 * 保存问卷
 * 
 * 
 * 
 * */
function cio05_savePaper(){
	var url = $("#savePaper1").html();
	if(questionList == "[]"){
		$('#errorSpan').html($('#errmsg1').val());
		$('#errorDiv').show();
		return false;
	}
	//var detailQuestion = questionList.replace(/&/g, '*amp*');
	$("#questionList").val(questionList);
	var param = $("#mainForm").serialize() + "&paperId=" + paperId
			+ "&maxPoint=" + $.trim($("#maxPoint").val()) + "&brandInfoId="
			+ $("#brandInfoId option:selected").val() + "&paperType="
			+ $("#paperType option:selected").val() + "&csrftoken=" + getTokenVal();
	var callback = function(msg){
		if($('#actionResultBody').length > 0) {
			if(window.opener.oTableArr[0] != null)window.opener.binOLMOCIO02_search();
			// 在BINOLMOCIO02.js中此方法已不再使用
			//window.opener.disabPapOptBtn();
		}
		$("div").removeClass("container");
	};
	cherryAjaxRequest({url:url,
		param:param,
		callback:callback
		});
}
/**
 * 向上移动
 *
 * 
 * */
function moveUp(obj,index){
	var up_index = Number(index)-1;
	var this_displayOrder = obj[index].displayOrder;
	var up_displayOrder = obj[up_index].displayOrder;
	//要上移的问题为第一个直接跳出
	if(this_displayOrder == 1){
		return;
	}
	obj[index].displayOrder = up_displayOrder;
	obj[up_index].displayOrder = this_displayOrder;
	var o = obj[up_index];
	obj[up_index] = obj[index];
	obj[index] = o;
	questionList = JSON2.stringify(obj);
	quesNaviLoad(questionList);
}
/**
 * 向下移动
 * 
 * 
 * */
function moveDown(obj,index){
	var down_index = Number(index)+1;
	var this_displayOrder = obj[index].displayOrder;
	//alert(this_displayOrder)
	var down_displayOrder = obj[down_index].displayOrder;
	obj[index].displayOrder = down_displayOrder;
	obj[down_index].displayOrder = this_displayOrder;
	var o = obj[down_index];
	obj[down_index] = obj[index];
	obj[index] = o;
	questionList = JSON2.stringify(obj);
	quesNaviLoad(questionList);
}

/**
 * 限制问题输入框中的字符大小
 * 
 * 
 * */
function controlStrLength(a){
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

function cio05isHour(obj,flag){
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

function cio05isMinOrSec(obj,flag){
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

/**
 * 问卷预览
 * 
 * 
 * 
 */
function cio05_previewPaper() {
	var begin_html = $("#previewPaper_dialog").html();
	cio05_setPreviewDialog(begin_html);
	$("#previewPaper_dialog").dialog("open");
	cio05_showPaperMain();
	cio05_showQuestion(questionList);
}

/**
 * 设置问卷预览的dialog
 * 
 * 
 * 
 */
function cio05_setPreviewDialog(begin_html) {
	$("#previewPaper_dialog").dialog( {
		autoOpen : false,
		draggable : true,
		resizable : false,
		modal : true,
		title : $("#preview").text(),
		height : 630,
		width : 550,
		position:['bottom'],
		buttons : [{
			text:$("#closePreview").text(),
			click : function() {
			cio05_previewDialogClose();
			$("#previewPaper_dialog").html(begin_html);
			}
		}],
		close : function(event, ui) {
			cio05_previewDialogClose();
			$("#previewPaper_dialog").html(begin_html);
		}
	});
}

/**
 * 关闭预览弹出框
 */
function cio05_previewDialogClose() {
//	closeCherryDialog('previewPaper_dialog');
	$("#previewPaper_dialog").dialog('destroy');
	$("#previewPaper_dialog").remove();
	$("#previewPaper_dialog_main").append(previewPaper_dialog_main);
}

/**
 * 展示问卷主要信息
 * 
 */
function cio05_showPaperMain() {
	$("#preview_brandInfoId").append(
			" " + $("#brandInfoId option:selected").text());
	$("#preview_paperType").append(" " + $("#paperType option:selected").text());
	$("#preview_paperName").append(" " + $.trim($("#paperName").val()).escapeHTML());
	$("#preview_maxPoint").append(" " + $("#maxPoint").val());
	if ($.trim($("#startDate").val()) == "" && $.trim($("#endDate").val()) == "") {
		$("#preview_date").append(" ");
	} else {
		$("#preview_date").append(
				" " + $.trim($("#startDate").val()).escapeHTML() +' '+ $("#to").text()+' ' + $.trim($("#endDate").val()).escapeHTML());
	}
}

/**
 * 展示问题详细（预览）
 */
function cio05_showQuestion(str) {
	var questionItem = null;
	var displayOrder = null;
	var questionType = null;
	var isRequired = null;
	var point = null;
	if (typeof str == "string") {
		var qustOjb = eval('(' + str + ')');
	} else {
		var qustOjb = str;
	}
	if (qustOjb[0] == null || qustOjb[0].paperId == null) {
		return;
	}
	for ( var i = 0; i < qustOjb.length; i++) {
		var questionOjb = qustOjb[i];
		displayOrder = questionOjb.displayOrder;
		questionItem = questionOjb.questionItem;
		point = questionOjb.point;
		questionType = questionOjb.questionType;
		isRequired = questionOjb.isRequired;
		var chioceNum = 0;
		for ( var k in questionOjb) {
			if (k.indexOf("option") > -1) {
				// 统计此问题中选择项数目
				chioceNum++;
			}
		}
		if ($("#maxPoint").val() != null
				&& Number($.trim($("#maxPoint").val())) > 0) {// 问卷为计分试卷
			var html1_1 = '<div style="margin:0px 0px 10px 0px;padding:10px" class="box4 clearfix" id="que_detail'
					+ i
					+ '"><span style="margin-bottom:5px;" class="clearfix"><strong><span class="highlight">'
					+ displayOrder
					+ '</span>: '
					+ questionItem
					+ '('
					+ point
					+ '分)';
			var html1_2 = '';
			if(null != isRequired && '1' == isRequired) {
				html1_2 = html1_2 = '[<span class="red">'+BINOLMOCIO05_js_i18n.required+'</span>]'; 
			}
			var html1_3 = '</strong></span></div>';
			$("#preview_question").append(html1_1+html1_2+html1_3);
			var html2 = '<div class="clearfix left" id="preView_' + i
					+ '"></div>';
			$("#que_detail" + i).append(html2);
			if (questionType == "0") {
				html2 = '<div class="clearfix left" id="preView_'
						+ i
						+ '">_____________________________________________________________</div>';
				$("#que_detail" + i).append(html2);
			} else {
				for ( var charNum = 65; charNum <= 64 + Number(chioceNum); charNum++) {
					var choiceChar = String.fromCharCode(charNum);
					var choice = "option" + choiceChar;
					var pointSingle = "point" + choiceChar;
					if (questionType == "1") {
						var html3 = '<div class = "clearfix"><input type="radio" disabled="disabled"/><span>'
								+ choiceChar
								+ '：'
								+ questionOjb[choice]
								+ '（'
								+ questionOjb[pointSingle] + '分）</span></div>';
						$("#preView_" + i).append(html3);
					} else if (questionType == "2") {
						var html3 = '<div class = "clearfix"><input type="checkBox" disabled="disabled"/><span>'
								+ choiceChar
								+ '：'
								+ questionOjb[choice]
								+ '（'
								+ questionOjb[pointSingle] + '分）</span></div>';
						$("#preView_" + i).append(html3);
					}
				}
			}
		} else {
			var html1_1 = '<div style="margin:0px 0px 10px 0px;padding:10px" class="box4 clearfix" id="que_detail'
					+ i
					+ '"><span style="margin-bottom:5px;" class="clearfix"><strong><span class="highlight">'
					+ displayOrder
					+ '</span>: '
					+ questionItem;
			var html1_2 = '';
			if(null != isRequired && '1' == isRequired) {
				html1_2 = html1_2 = '[<span class="red">'+BINOLMOCIO05_js_i18n.required+'</span>]'; 
			}
			var html1_3 = '</strong></span></div>';
			$("#preview_question").append(html1_1+html1_2+html1_3);
			var html2 = '<div class="clearfix left" id="preView_' + i
					+ '"></div>';
			$("#que_detail" + i).append(html2);
			if (questionType == "0") {
				html2 = '<div class="clearfix left" id="preView_'
						+ i
						+ '">_____________________________________________________________</div>';
				$("#que_detail" + i).append(html2);
			} else {
				for ( var charNum = 65; charNum <= 64 + Number(chioceNum); charNum++) {
					var choiceChar = String.fromCharCode(charNum);
					var choice = "option" + choiceChar;
					var pointSingle = "point" + choiceChar;
					if (questionType == "1") {
						var html3 = '<div class = "clearfix"><input type="radio" disabled="disabled"/><span>'
								+ choiceChar
								+ '：'
								+ questionOjb[choice]
								+ '</span></div>';
						$("#preView_" + i).append(html3);
					} else if (questionType == "2") {
						var html3 = '<div class = "clearfix"><input type="checkBox" disabled="disabled"/><span>'
								+ choiceChar
								+ '：'
								+ questionOjb[choice]
								+ '</span></div>';
						$("#preView_" + i).append(html3);
					}
				}
			}
		}
	}
}