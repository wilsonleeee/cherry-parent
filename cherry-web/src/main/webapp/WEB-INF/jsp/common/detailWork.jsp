<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div id="workflowCanvas" style="position:relative;height:120px;width:300px;" >
</div>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/wz_jsgraphics.js"></script>
<script type="text/javascript" src="/Cherry/js/common/modifyAuditor.js"></script>
<script type="text/javascript">
// 获得当前step信息
var currentStepIds = ${currentStepId};
// 获得所有要显示的step信息
var steps = ${stepList};
//set up graphics
var jg = new jsGraphics("workflowCanvas");
jg.setColor("#000000");
jg.setStroke(1);
// 调节流程图显示的位置
var xAdjust = 50;
var yAdjust = 50;
//parsing xml and paint;
// 显示流程图方法
drawStep();
//调用函数，获取数值
$(window).bind("resize", drawStep);
//window.onresize=drawStep; 

function drawStep(){
	if($("#tabs-2").length == 0){
		return;
	}
	// 清空当前面板
	jg.clear();
	// 取得table的宽度
	var td_width = document.getElementById("tabs-2").offsetWidth;
	//用于显示流程图宽度
	var stepsWidth = td_width * 0.8;
	//用于显示流程图最小的宽度
	var minWidth = 300;
	//用于显示流程图最大的宽度
	var maxWidth = 150*(steps.length-1);

	var _stepWidth = null;
	//默认总长度如果介于最大值和最小值之间则取默认的长度，如果小于最小值则去最小值，如果大于最大值则取最大值
	if(steps.length > 1){
		if(stepsWidth >= minWidth && stepsWidth <= maxWidth){
			_stepWidth = stepsWidth/(steps.length-1);
		}else if(stepsWidth < minWidth){
			var preStep_width = minWidth/(steps.length-1);
			_stepWidth = parseInt(preStep_width);
		}else{
			var preStep_width = maxWidth/(steps.length-1);
			_stepWidth = parseInt(preStep_width);
		}
	}else{
		_stepWidth = 170;
	}
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
				jg.drawString(steps[i].showText, xAdjust, yAdjust + 20);
			}else{
				jg.drawImage(src1, xAdjust + i * 15 + (i - 1)*_stepWidth, yAdjust, _stepWidth-14, 13);
			}
			if(i != 0){
				jg.drawImage(src2, xAdjust + i * 15 + i * _stepWidth, yAdjust, 14, 13);
				jg.drawString(steps[i].showText, xAdjust + i * 15 + i * _stepWidth, yAdjust + 20);
			}
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
		for(var i = 0;i < steps.length;i++){
			if("" == currentStepId){
				src1 = "/Cherry/workflowImage/cherryflow2.png";
				src2 = "/Cherry/workflowImage/cherryflow3.png";
			}else{
				if(i < currentStepId-1){
					src1 = "/Cherry/workflowImage/cherryflow2.png";
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
			jg.drawString(steps[i].showText, xAdjust + i * _stepWidth, yAdjust + 20);
		}
	}
	jg.paint();
}
/**
 * 单据跳转
 */
function detailWork_billJump(url){
	if(url != ""){
		OS_BILL_Jump_needUnlock = false;
		if(url.indexOf("/")==0){
		     url = '/Cherry'+url;
		}else{
		     url = '/Cherry/'+url;
		}
		$("#osBillDetailUrl").attr("action",url);
	    var tokenVal = parentTokenVal();
		$('#osBillDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#osBillDetailUrl').submit();
	}
}

function detailWork_showEmployee(text){
	text = $.trim(text);
	if(text != "" && text != "|"){
	    text = text.replace(/&lt;br\/&gt;/g, '<br/>');
	    $("#spanOSEmployee").attr("title",text);
	    $("#spanOSEmployee").cluetip({
	        splitTitle: '|',
	        width: '200',
	        height: 'auto',
	        cluetipClass: 'default',
	        cursor:'pointer',
	        arrows: false, 
	        dropShadow: false,
	        showTitle:false
	        }
	    );
	}
}

$(document).ready(function() {
	detailWork_showEmployee("|"+$("#spanEmpList").text());
} );

</script>
<style>
.osNextName {
    color: #CC6600;
}
</style>
<div class="section-header">
   		<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="global.page.workdetail"/></strong>
</div>
<div >
<table width="100%" cellspacing="0" cellpadding="0">
<tbody id="workFlowDetail">
  <tr>
	   <th width="15%">
	    <strong><s:text name="global.page.workTime"/></strong>
	   </th>
	   <th width="15%">
	    <strong><s:text name="global.page.workInfo"/></strong>
	   </th>
	   <th width="35%">
	    <strong><s:text name="global.page.workResult"/></strong>
	   </th>
	   <th width="15%">
	    <strong><s:text name="global.page.workPerson"/></strong>
	   </th>
	   <th width="20%">
	    <strong><s:text name="global.page.workDepart"/></strong>
	   </th>
  </tr>
  <s:iterator value="opLoglist" id="opLoglist">
    <tr>
       <td><s:property value="CreateTime"/></td>
       <td><s:property value="OpCodeValue"/></td>
       <td>
       		<p><s:property value="OpResultValue"/><s:if test='BillNo != null && OpResultValue != null' >&nbsp;：&nbsp;<a href="javascript:void(0);" style="color:#3366FF;" onclick="detailWork_billJump('<s:property value="OpenBillURL"/>');return false;"><s:property value="BillNo"/></a></s:if></p>
       		<s:if test='OpComments != null' ><p style="clear:both;"><a style="cursor:pointer;" title='<s:property value="OpComments"/>'><s:text name="global.page.opComments"/>&nbsp;：&nbsp;<s:property value="OpCommentsMore"/></a></p></s:if>
       </td>
       <td><s:property value="EmployeeName"/></td>
       <td><s:property value="DepartName"/></td>
    </tr>
  </s:iterator>
    <%-- ================== 下一步执行者 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/detailWork_1.jsp" flush="true" />
	<%-- ================== 下一步执行者    END  ======================= --%>
</tbody>
</table>
</div>
  <hr class="space" />
  
<div class="center">
    <%--当状态为审核中显示 --%>
    <cherry:show domId="ModifyAuditor">
	    <s:url action="BINOLCM26_init" id="showDialogUrl"></s:url>
	    <s:url action="BINOLCM25_refreshAuditor" id="refreshAuditorUrl"></s:url>
	    <div class="hide">
	        <a id="showDialogUrl" href="${showDialogUrl}"></a>
	        <a id="refreshAuditorUrl" href="${refreshAuditorUrl}"></a>
	        <a id="refreshOSBillUrl" href="<s:property value="audMap.currentURL"/>"></a>
	    </div>
	    <s:if test='!"999".equals(currentOperateVal)'>
	    <button class="edit" onclick="modifyAuditor.showDialog();return false;">
	        <span class="ui-icon icon-edit-big"></span>
	        <!-- 修改参与者 -->
	        <span class="button-text"><s:text name="global.page.modifyAuditor"/></span>
	    </button>
	    </s:if>
    </cherry:show>
    
	<button class="close" onclick="window.close();">
		<span class="ui-icon icon-close"></span>
		<span class="button-text"><s:text name="global.page.close"/></span>
	</button>
</div>
<div id="hideDivModifyActor" class="hide">
    <div id="dialogModifyAuditorInit" class="hide"></div>
    <span id="editTitile"><s:text name="global.page.editAuditor"/></span>
    <span id="ok"><s:text name="global.page.ok"/></span>
    <span id="cancle"><s:text name="global.page.cancle"/></span>
    <span id="close"><s:text name="global.page.close"/></span>
    <span id="successTitile"><s:text name="global.page.editSuccess"/></span>
</div>
<form id="osBillDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
</form>
