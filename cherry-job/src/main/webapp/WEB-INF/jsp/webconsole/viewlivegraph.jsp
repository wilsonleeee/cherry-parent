<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="workflowCanvas" style="position:relative;height:900px;width:508px;">
<div id="logInfo" style="display:none;width:150px;z-index:90;float:right;overflow:hidden;
	top:-60px;position: absolute;"></div>
<%@ taglib prefix="s" uri="/struts-tags" %>  
</div>
<script type="text/javascript" src="js/wz_jsgraphics.js"></script>
<script type="text/javascript">
var currentStepIds = ${currentStepsJson};
if(currentStepIds.file == 0){
	$("#workflowCanvas").html("<p><label style='font-weight:bold;text-align:right;margin-top:100px;margin-left:100px;'>该品牌的工作流文件未上传！请上传！</label><p>");
}else{
	var steps = ${stepsJson};
	//set up graphics
	var jg = new jsGraphics("workflowCanvas");
	jg.setColor("#ff0000");
	jg.setStroke(1);
	var widthAdjust = 250;
	var heightAdjust = 30;
	var xAdjust = 300;
	var yAdjust = 100;
	//parsing xml and paint;
	jg.clear();
	drawStep(-1);
}

function drawStart(str,y){
	jg.setColor("#A9A9A9");
	jg.fillEllipse(xAdjust + widthAdjust/4,y,widthAdjust/2,heightAdjust);
	jg.setColor("#000000");
	jg.drawString(str, xAdjust + widthAdjust/4 + 20 , y);
	if(str == "Start"){
		jg.drawLine(xAdjust + widthAdjust/2,y + heightAdjust,xAdjust + widthAdjust/2,y + heightAdjust * 2);
		jg.drawLine(xAdjust + widthAdjust/2,y + heightAdjust * 2,xAdjust + widthAdjust/2 - 2,y + heightAdjust * 2 - 5);
		jg.drawLine(xAdjust + widthAdjust/2,y + heightAdjust * 2,xAdjust + widthAdjust/2 + 2,y + heightAdjust * 2 - 5);
	}else{
		jg.drawLine(xAdjust + widthAdjust/2,y - heightAdjust,xAdjust + widthAdjust/2,y);
		jg.drawLine(xAdjust + widthAdjust/2,y,xAdjust + widthAdjust/2 - 2,y - 5);
		jg.drawLine(xAdjust + widthAdjust/2,y,xAdjust + widthAdjust/2 + 2,y - 5);
	}
}

function drawStep(stepIndex){
	jg.clear();
	drawStart("Start",yAdjust - heightAdjust * 2);
	jg.paint();
	var list = steps;
	if(stepIndex != -1){
		list = steps[stepIndex].actions;
	}
	for(var i = 0; i < list.length; i++){
		jg.setColor("#000000");
		if((stepIndex == -1 && currentStepIds.currentStepId == list[i].stepId) ||
				(stepIndex != -1 && currentStepIds.currentActionId == list[i].actionId)){
			jg.setColor("#FFFF00");
			jg.fillRect(xAdjust, heightAdjust * 2 * i + yAdjust,widthAdjust,heightAdjust);
			jg.setColor("#ff0000");
		}
		jg.drawRect(xAdjust, heightAdjust * 2 * i + yAdjust,widthAdjust,heightAdjust);
	    if(null == list[i].stepName){
	    	jg.drawString(list[i].actionName + "(ACTION)", xAdjust + 10,heightAdjust * 2  * i + yAdjust, 
	    	    	"drawStep(-1)", "mouseOut()", "mouseOut()");
	    }else{
		    jg.drawString(list[i].stepName + "(STEP)", xAdjust + 10,heightAdjust * 2  * i + yAdjust, 
				    "drawStep(" + i + ")", "mouseOver(" + i + ", this)", "mouseOut()");
	    }
	    jg.setColor("#000000");
	    if(i != 0){
	    	jg.drawLine(xAdjust + widthAdjust/2,heightAdjust * 2  * (i - 1) + yAdjust + heightAdjust,xAdjust + widthAdjust/2,heightAdjust * 2  * i + yAdjust);
	    	jg.drawLine(xAdjust + widthAdjust/2,heightAdjust * 2  * i + yAdjust,xAdjust - 2 + widthAdjust/2,heightAdjust * 2  * i + yAdjust - 5);
	    	jg.drawLine(xAdjust + widthAdjust/2,heightAdjust * 2 * i + yAdjust,xAdjust + 2 + widthAdjust/2,heightAdjust * 2  * i + yAdjust - 5);
	      }

		if(stepIndex == -1){
			if(list[i].results.length > 1){
				var resultList = list[i].results;
				for(var resIndex = 0; resIndex < resultList.length; resIndex++){
					if(resultList[resIndex] != i + 1 && resultList[resIndex] != i){
						var siteIndex = resultList[resIndex];
						jg.drawLine(xAdjust + widthAdjust,heightAdjust * 2  * i + yAdjust + heightAdjust/2,
								xAdjust + widthAdjust + 10 * (i + 1),heightAdjust * 2  * i + yAdjust + heightAdjust/2);
						jg.drawLine(xAdjust + widthAdjust + 10 * (i + 1),heightAdjust * 2  * i + yAdjust + heightAdjust/2,
								xAdjust + widthAdjust + 10 * (i + 1),heightAdjust * 2  * siteIndex + yAdjust + heightAdjust/2);
						jg.drawLine(xAdjust + widthAdjust + 10 * (i + 1),heightAdjust * 2  * siteIndex + yAdjust + heightAdjust/2,
								xAdjust + widthAdjust,heightAdjust * 2  * siteIndex + yAdjust + heightAdjust/2);
				    	jg.drawLine(xAdjust + widthAdjust,heightAdjust * 2  * siteIndex + yAdjust + heightAdjust/2,
				    			xAdjust + widthAdjust + 5,heightAdjust * 2  * siteIndex + yAdjust + heightAdjust/2 - 2);
				    	jg.drawLine(xAdjust + widthAdjust,heightAdjust * 2  * siteIndex + yAdjust + heightAdjust/2,
				    			xAdjust + widthAdjust + 5,heightAdjust * 2  * siteIndex + yAdjust + heightAdjust/2 + 2);
					}
				}
				
			}
		}
	}
	drawStart("Finish",heightAdjust * 2  * (i-1) + yAdjust + + heightAdjust * 2);
	jg.paint();
}

function mouseOver(stepIndex,event){
	var x = event.offsetTop;
	var y = event.offsetLeft;
	document.getElementById("logInfo").style.top=x+"px";
	document.getElementById("logInfo").style.left=y + 300 +"px";
	document.getElementById("logInfo").innerHTML=steps[stepIndex].stepName;
	document.getElementById("logInfo").style.display="block";
}

function mouseOut(){
	$("#logInfo").hide();
}
</script>