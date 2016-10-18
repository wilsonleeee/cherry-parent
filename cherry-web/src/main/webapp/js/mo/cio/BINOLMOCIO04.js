/**
 * @author user3
 */
window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};



function showQuestionDetail(questionList,id){
	try{
		var quesList = null;
		
		if(typeof questionList == "string"){
			quesList = eval('(' + questionList + ')');
		}else{
			quesList = questionList;
		}
		
		var paperPoint = quesList[0].point;
		var quesCount = quesList.length;
		if(paperPoint  == 0.0){
			for(var i = 0 ; i < quesCount ; i++){
				var _thisQuestion = quesList[i];
				var isRequired = _thisQuestion.isRequired;
				var question_html = '<div id="question_'
						+ i
						+ '" class="box4 clearfix" style="margin:0px 0px 10px 0px;padding:10px"><span class="clearfix" style="margin-bottom:5px;"><strong><span class="highlight">'
						+ _thisQuestion.displayOrder + '</span>：'
						+ _thisQuestion.questionItem.escapeHTML();
				var question_html_0 = '';
				if(null != isRequired && '1' == isRequired) {
					question_html_0 = '[<span class="red">'+BINOLMOCIO04_js_i18n.required+'</span>]'; 
				}
				question_html_0	+= '</strong></span></div>';
				$(id).append(question_html+question_html_0);
				var questionType = _thisQuestion.questionType;
				if(questionType == "1" || questionType == "2"){
					var choice_html = '<div class="clearfix"  style="margin:0px 10px 0px 10px" id="choiceMain_'+i+'"></div>';
					$(id+" #question_"+i).append(choice_html);
					var choiceDetail_html = "";
					var chioceNum = _thisQuestion.choiceNum;
					for(var charNum = 65 ;charNum <= 64+Number(chioceNum);charNum++){ 
						var choiceChar = String.fromCharCode(charNum);
						var choice = "option"+choiceChar;
						if(questionType == "1" ){
							choiceDetail_html = choiceDetail_html + '<span class="clearfix"><input type="radio" name="question_choiceDetail_'+i+'" disabled="disabled"/>'+choiceChar+'：'+_thisQuestion[choice].escapeHTML()+'</span>';
						}else{
							choiceDetail_html = choiceDetail_html + '<span class="clearfix"><input type="checkbox" disabled="disabled"/>'+choiceChar+'：'+_thisQuestion[choice].escapeHTML()+'</span>';
						}
					}
					$(id+" #choiceMain_"+i).append(choiceDetail_html);
				}else if(questionType == "0"){
					var ansLine1 = '<span class="clearfix" style="margin:0px 10px 0px 10px">_______________________________________________________________________________</span>';
					var ansLine2 = '<span class="clearfix" style="margin:0px 10px 0px 10px">_______________________________________________________________________________</span>';
					$(id+" #question_"+i).append(ansLine1);
					$(id+" #question_"+i).append(ansLine2);
				}
			}
		}else{
			for(var i = 0 ; i < quesCount ; i++){
				var _thisQuestion = quesList[i];
				var isRequired = _thisQuestion.isRequired;
				var question_html = '<div id="question_'
						+ i
						+ '" class="box4 clearfix" style="margin:0px 10px 10px 0px;padding:10px"><span class="clearfix" style="margin-bottom:5px;"><strong><span class="highlight" >'
						+ _thisQuestion.displayOrder + '</span>：'
						+ _thisQuestion.questionItem.escapeHTML() + '（'
						+ _thisQuestion.point + '分）';
				var question_html_0 = '';
				if(null != isRequired && '1' == isRequired) {
					question_html_0 = '[<span class="red">'+BINOLMOCIO04_js_i18n.required+'</span>]'; 
				}
				question_html_0	+= '</strong></span></div>';
				$(id).append(question_html+question_html_0);
				var questionType = _thisQuestion.questionType;
				if(questionType == "1" || questionType == "2"){
					var choice_html = '<div class="clearfix"  style="margin:0px 10px 0px 10px" id="choiceMain_'+i+'"></div>';
					$(id+" #question_"+i).append(choice_html);
					var choiceDetail_html = "";
					var chioceNum = _thisQuestion.choiceNum;
					for(var charNum = 65 ;charNum <= 64+Number(chioceNum);charNum++){ 
						var choiceChar = String.fromCharCode(charNum);
						var choice = "option"+choiceChar;
						var point = "point"+choiceChar;
						if(questionType == "1" ){
							choiceDetail_html = choiceDetail_html + '<span class="clearfix"><input type="radio" disabled="disabled" name="question_choiceDetail_'+i+'"/>'+choiceChar+'：'+_thisQuestion[choice].escapeHTML()+'（'+_thisQuestion[point]+'分）</span>';
						}else{
							choiceDetail_html = choiceDetail_html + '<span class="clearfix"><input type="checkbox" disabled="disabled"/>'+choiceChar+'：'+_thisQuestion[choice].escapeHTML()+'（'+_thisQuestion[point]+'分）</span>';
						}
					}
					$(id+" #choiceMain_"+i).append(choiceDetail_html);
				}else if(questionType == "0"){
					var ansLine1 = '<span class="clearfix" style="margin:0px 10px 0px 10px">_______________________________________________________________________________</span>';
					var ansLine2 = '<span class="clearfix" style="margin:0px 10px 0px 10px">_______________________________________________________________________________</span>';
					$(id+" #question_"+i).append(ansLine1);
					$(id+" #question_"+i).append(ansLine2);
				}
			}
		}
	}catch(e){
		return;
	}
}

function showQuestionByGroup(obj){
	var groupIndex = null;
	
	if(typeof obj == "object"){
		var objId = obj.id;
		groupIndex = (obj.id).split("_")[1];
	}else{
		groupIndex = obj;
	}
	
	var thisGroup = BINOLMOCIO04_questionList[groupIndex];
	
	var elementId = "#group_"+groupIndex+" #question";
	if($(elementId).html().toString().length == 0){
		showQuestionDetail(thisGroup,elementId);
	}
	if(typeof obj == "object" && $(elementId).is(":hidden")){
		$(elementId).show();
		$("#"+objId).find("span").toggleClass("ui-icon ui-icon-triangle-1-s");
		$("#"+objId).find("span").toggleClass("ui-icon ui-icon-triangle-1-n");
	}else if(typeof obj == "object"){
		$(elementId).hide();
		$("#"+objId).find("span").toggleClass("ui-icon ui-icon-triangle-1-n");
		$("#"+objId).find("span").toggleClass("ui-icon ui-icon-triangle-1-s");
	}
	
}

