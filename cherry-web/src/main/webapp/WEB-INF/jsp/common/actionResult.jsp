<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:if test="hasActionErrors()">
	<div class="actionError" id="actionResultDiv">
	  	<s:actionerror/>
	</div>
</s:if>
<s:if test="hasActionMessages()">
	<script type="text/javascript" src="/Cherry/js/common/wz_jsgraphics.js"></script>
	<script type="text/javascript" src="/Cherry/js/common/modifyAuditor.js"></script>
	<div class="actionSuccess" id="actionResultDiv" style="display:none">
	  	<s:actionmessage escape="false"/>
	</div>
	<div id = "message" class="panel-content" style="display:none">
		<table id="workFlowDetail_table" cellpadding="0" cellspacing="0" border="0" width="100%" style="position:relative;">
			<tr>
				<th style="width:160px;background: #F9F9F9;">
					<div id="actionResultDiv" class="workFlowSuccess">
					  	<ul class="actionMessage">
                			<li><span></span></li>
						</ul>
					</div>
				</th>
				<td style="width:auto;">
					<div id="workflowCanvas_1" style="position:relative;height:50px;width:100%;" ></div>
				</td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
		var messageStr = $.trim($("#actionResultDiv").find("span").text());
		try{
			var messageObj = window.JSON2.parse(messageStr);
			var messageBody = messageObj.MessageBody;
			var showWorkFlow = messageObj.ShowWorkFlow;
			var workFlowId = messageObj.WorkFlowID;
			var currentStepIds = null;
			var steps = null;
			var jg = new jsGraphics("workflowCanvas_1");
			
			var isInt = function(str){
				var reg = /^(-|\+)?\d+$/;
				return reg.test(str);
			};
			
			if( messageBody && showWorkFlow && isInt(workFlowId)  && workFlowId > 0){
				var url = "/Cherry/common/BINOLCM25_getWorkFlowSteps";
				var params= "WorkFlowID="+workFlowId;
				var callback = function(msg){
					var steps_obj = window.JSON2.parse(msg);
					// 获得当前step信息
					currentStepIds = steps_obj.currentStepId;
					steps = steps_obj.steps;
					if(!currentStepIds || currentStepIds.length == 0 || !steps || steps.length == 0){
						throw "ex1";
					}
					$("#workFlowDetail_table").find("span").html(messageBody);
					$("#message").show();
					drawStep();
					//调用函数，获取数值
					$(window).bind("resize", drawStep);
					//window.onresize=drawStep; 
				};
				cherryAjaxRequest({
					url:url,
					param:params,
					callback:callback
				});
			}else{
				throw "ex";
			}
		}catch(e){
			if(e == "ex" || e == "ex1" || e == "ex2"){
				$("#actionResultDiv").find("span").html(messageBody);
			}
			$("#actionResultDiv").show();
		}

		var drawStep = function (){
			if($("#workFlowDetail_table").length == 0 || steps == null){
				return;
			}
			// 取得当前画面上显示流程图的td宽度
			var td_width = document.getElementById("workFlowDetail_table").rows[0].cells[1].offsetWidth;
			//流程图箭头默认长度
			var defaultWidth = 140;
			//用于显示流程图最大的宽度
			var maxWidth = td_width * 0.8;
			//用于显示流程图最小的宽度
			var minWidth = td_width * 0.55;
			//默认长度下流程图的宽度
			if(steps.length-1 <= 0){
				throw "ex2";
			}
			var workFlowWidth_default = defaultWidth * (steps.length-1);
	
			var _stepWidth = null;
			//默认总长度如果介于最大值和最小值之间则取默认的长度，如果小于最小值则去最小值，如果大于最大值则取最大值
			if(workFlowWidth_default >= minWidth && workFlowWidth_default <= maxWidth){
				_stepWidth = defaultWidth;
			}else if(workFlowWidth_default < minWidth){
				var preStep_width = minWidth/(steps.length-1);
				_stepWidth = parseInt(preStep_width);
			}else{
				var preStep_width = maxWidth/(steps.length-1);
				_stepWidth = parseInt(preStep_width);
			}
			
			jg.setColor("#000000");
			jg.setStroke(1);
			// 调节流程图显示的位置
			var xAdjust = 20;
			var yAdjust = 11;
			
			// 显示流程图
			(function(){
				// 清空当前面板
				jg.clear();
				var currentStepId;
				var index = 0;
				var src1 = "/Cherry/workflowImage/cherryflow2.png";
				var src2 = "/Cherry/workflowImage/cherryflow3.png";
				// 获得当前step的显示位置
				if(null != currentStepIds.currentShowOrder){
					currentStepId = currentStepIds.currentShowOrder;
				}else{
					// 根据当前step的ID取得当前step的显示位置
					for(var i = 0;i < steps.length;i++){
						if(currentStepIds.currentId == steps[i].stepId){
							currentStepId = steps[i].OS_StepShowOrder - 1;
							break;
						}
						index++;
					}
				}
				// 当前step隐藏时的处理，通过step的id显示流程图
				if(index == steps.length){
					// 按照stepid排序
					for(var i = 0;i < steps.length;i++){
						for(var j = i + 1;j < steps.length;j++){
							if(steps[i].stepId > steps[j].stepId){
								var id;
								id = steps[i].stepId;
								steps[i].stepId = steps[j].stepId;
								steps[j].stepId = id;
								id = steps[i].showText;
								steps[i].showText = steps[j].showText;
								steps[j].showText = id;
							}
						}
					}
					// 按照排好序的list显示流程图
					for(var i = 0;i < steps.length;i++){
						if(i == 0){
							src1 = "/Cherry/workflowImage/cherryflow2.png";
						}else if(steps[i].stepId < currentStepIds.currentId){
							src1 = "/Cherry/workflowImage/cherryflow3.png";
							src2 = "/Cherry/workflowImage/cherryflow2.png";
						}else{
							src1 = "/Cherry/workflowImage/cherryflow4.png";
							src2 = "/Cherry/workflowImage/cherryflow1.png";
						}
						if(i == 0){
							jg.drawImage(src1, xAdjust, yAdjust, 14, 13);
						}else{
							jg.drawImage(src1, xAdjust + i * 15 + (i - 1)*_stepWidth, yAdjust, _stepWidth-14, 13);
						}
						if(i != 0){
							jg.drawImage(src2, xAdjust + i * 15 + i * _stepWidth, yAdjust, 14, 13);
						}
						jg.drawString(steps[i].showText, xAdjust + i * _stepWidth, yAdjust + 20);
					}
				}else{
					// 按照showOrder显示流程图
					// 按照showOrder排序
					for(var i = 0;i < steps.length;i++){
						for(var j = i + 1;j < steps.length;j++){
							if(steps[i].OS_StepShowOrder > steps[j].OS_StepShowOrder){
								var id;
								id = steps[i].OS_StepShowOrder;
								steps[i].OS_StepShowOrder = steps[j].OS_StepShowOrder;
								steps[j].OS_StepShowOrder = id;
								id = steps[i].showText;
								steps[i].showText = steps[j].showText;
								steps[j].showText = id;
							}
						}
					}
					// 显示流程图
					for(var i = 0;i < steps.length;i++){
						if("" == currentStepId){
							src1 = "/Cherry/workflowImage/cherryflow2.png";
							src2 = "/Cherry/workflowImage/cherryflow3.png";
						}else{
							if(i < currentStepId-1){
								src1 ="/Cherry/workflowImage/cherryflow2.png";
								src2 = "/Cherry/workflowImage/cherryflow3.png";
							}else if(i == currentStepId-1){
								src1 = "/Cherry/workflowImage/cherryflow2.png";
								src2 = "/Cherry/workflowImage/cherryflow4.png";
							}else{
								src1 = "/Cherry/workflowImage/cherryflow1.png";
								src2 = "/Cherry/workflowImage/cherryflow4.png";
							}
						}
						jg.drawImage(src1, xAdjust + i * _stepWidth, yAdjust, 14, 13);
						if(i != steps.length - 1){
							jg.drawImage(src2, xAdjust + i * _stepWidth + 15, yAdjust, _stepWidth-14, 14);
						}
						jg.drawString(steps[i].showText, xAdjust +  i * _stepWidth, yAdjust + 20);
					}
				}
				jg.paint();
			})();
			}
	</script>
</s:if>
<s:if test="hasFieldErrors()">
	<div id="fieldErrorDiv" Style="display:none">
		<input type="hidden" name="hidFieldErrorMsg" value="<s:property value='fieldErrors'/>" />		
	</div>	
</s:if>
