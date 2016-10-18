/**
 * @author user3
 */
function showQuestion(obj1){
	var obj = ($(obj1).parent()).parent();
	if($(obj).find("#question").is(":hidden")){
		$(obj1).find("span").toggleClass("ui-icon ui-icon-triangle-1-n");
		$(obj1).find("span").toggleClass("ui-icon ui-icon-triangle-1-s");
		$(obj).find("#question").show();
		var checkQuestionGroupId = obj.attr("id");
		var questionList = eval('(' + BINOLMOCIO11_questionList + ')');
		var queListOfGroup = [];
		for(var index = 0 ; index < questionList.length ; index++ ){
			if(questionList[index].checkQuestionGroupId == checkQuestionGroupId){
				queListOfGroup.push(questionList[index]);
			}
		}
		for(var j = 0 ; j < queListOfGroup.length ; j++){
			var question_html = '<div id="question_'+j+'" class="clearfix" style="margin:0px 0px 10px 0px;padding:10px"><span class="clearfix" style="margin-bottom:5px;"><strong><span class="highlight" style="font-weight:bolder">'+queListOfGroup[j].displayOrder+'</span>：'+queListOfGroup[j].questionItem.escapeHTML()+'（'+queListOfGroup[j].maxPoint+'-'+queListOfGroup[j].minPoint+'分）</strong></span></div>';
			$(obj).find("#question").append(question_html);
			if(queListOfGroup[j].questionType == "0" || queListOfGroup[j].questionType == "2"){
				var choice_html = '<div class="clearfix"  style="margin:0px 10px 0px 10px" id="choiceMain_'+j+'"></div>';
				$(obj).find("#question_"+j).append(choice_html);
				var choice_num = 0;
				for(var key in queListOfGroup[j]){
					if(key.indexOf("option") > -1){
						choice_num++;
					}
				}
				var choiceDetail_html = "";
				for(var charNum = 65 ;charNum <= 64+Number(choice_num);charNum++){ 
					var choiceChar = String.fromCharCode(charNum);
					var choice = "option"+choiceChar;
					var point = "point"+choiceChar;
					if(queListOfGroup[j].questionType == "0" ){
						choiceDetail_html = choiceDetail_html + '<span class="clearfix"><input type="radio" name="question_choiceDetail_'+j+'" disabled="disabled"/>'+choiceChar+'：'+queListOfGroup[j][choice].escapeHTML()+'（'+queListOfGroup[j][point]+'分）</span>';
					}else{
						choiceDetail_html = choiceDetail_html + '<span class="clearfix"><input type="checkbox" disabled="disabled"/>'+choiceChar+'：'+queListOfGroup[j][choice].escapeHTML()+'（'+queListOfGroup[j][point]+'分）</span>';
					}
				}
				$(obj).find("#choiceMain_"+j).append(choiceDetail_html);
			}
//			else if(queListOfGroup[j].questionType == "0"){
//				var ansLine1 = '<span class="clearfix" style="margin:0px 10px 0px 10px">_____________________________________________________</span>';
//				var ansLine2 = '<span class="clearfix" style="margin:0px 10px 0px 10px">_____________________________________________________</span>';
//				$(obj).find("#question_"+j).append(ansLine1);
//				$(obj).find("#question_"+j).append(ansLine2);
//			}
		}
	}else{
		$(obj).find("#question").html("");
		$(obj).find("#question").hide();
		$(obj1).find("span").toggleClass("ui-icon ui-icon-triangle-1-s");
		$(obj1).find("span").toggleClass("ui-icon ui-icon-triangle-1-n");
	}
}
window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};