<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO03.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
var previewPaper_dialog_main = "";
var addChoice_dialog_main = "";
var addFill_dialog_main = "";
$().ready(function() {
	previewPaper_dialog_main = $("#previewPaper_dialog_main").html();
	addChoice_dialog_main = $("#addChoice_dialog_main").html();
	addFill_dialog_main = $("#addFill_dialog_main").html();
	var holidays = '${holidays }';
	$('#startDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#endDate').val();
			return [value,'maxDate'];
		}
	});
	$('#endDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#startDate').val();
			return [value,'minDate'];
		}
	});
	$("#addSingleChoice").click(function() {
		showChoiceDialog(BINOLMOCIO03_js_i18n.addSinChioceTitle, '1');
	});
	$("#addMultipleChoice").click(function() {
		showChoiceDialog(BINOLMOCIO03_js_i18n.addMulChioceTitle, '2');
	});
	$("#addApfill").click(function() {
		showFillDialog(BINOLMOCIO03_js_i18n.addApfillTitle, '3');
	});
	$("#addEssay").click(function() {
		showFillDialog(BINOLMOCIO03_js_i18n.addEssayTitle, '0');
	});
	if (window.opener) {
	       window.opener.lockParentWindow();
	}
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}		
	});
});
</script>
<s:i18n name="i18n.mo.BINOLMOCIO03">
<%-- ================== js国际化  START ======================= --%>
<jsp:include page="/WEB-INF/jsp/mo/cio/BINOLMOCIO03_1.jsp" flush="true" />
<%-- ================== js国际化导入    END  ======================= --%>
	<div id="CIO03_select" class="hide">
		<s:text name="global.page.all" />
	</div>
	<div class="panel ui-corner-all">
	<div class="panel-header">
	<%-- ###问卷制作 --%>
		<div class="clearfix"><span class="breadcrumb left"> 
			<span class="ui-icon icon-breadcrumb"></span>
			<s:text name="CIO03_manage" />&gt;
			<s:text name="CIO03_title" /> </span> <%--新增问卷 --%> 
		</div>
	</div>
	<div id="actionResultDisplay"></div>
	<%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
      <p class="clearfix">
      	<span class="right">
	      	<span class="gray"><s:text name="CIO03_userTipFirst"/></span>
	      	<span class="highlight">*</span>
	      	<span class="gray"><s:text name="CIO03_userTip"/></span>
      	</span>
      </p>
      <div>
       <form id="mainForm" class="inline">
      <s:url action="BINOLMOCIO03_savePaper" id="s_savePaper"></s:url>
      <table class="detail">
      	<tbody>
      		<tr>
      			<!-- 所属品牌 -->
      			<s:if test="%{brandInfoList != null}">
	      			<th><s:text name="CIO03_brand"/><span class="highlight">*</span></th>
	      			<td><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select></td>
      			</s:if>
      			<s:else>
	      			<th><s:text name="CIO03_brand"/><span class="highlight">*</span></th>
	      			<td></td>
      			</s:else>
      			<!-- 是否为计分试卷 -->
      			<th><s:text name="CIO03_isPoint"/><span class="highlight">*</span></th>
      			<td><s:select list="#application.CodeTable.getCodes('1040')" name="isHasMaxPoint" listKey="CodeKey" listValue="Value" onchange="isPointPaper();"></s:select></td>
      		</tr>
      		<tr>
      			<!-- 开始时间-->
      			<th><s:text name="CIO03_startTime"></s:text><span class="highlight">*</span></th>
      			<td class="date">
	      			<span>
	    				<s:textfield id="startDate" name="startDate" cssClass="date"/>
	    				<input id="startHour" class="text" type="text" value="00" name ="startHour" style="width:15px" maxlength="2" onblur="cio03isHour(this)"/>
	    				<s:text name="CIO03_hour"></s:text>
	    				<input id="startMinute" class="text" type="text" value="00" name ="startMinute" style="width:15px" maxlength="2" onblur="cio03isMinOrSec(this)"/>
	    				<s:text name="CIO03_minute"></s:text>
	    				<input id="startSecond" class="text" type="text" value="00" name ="startSecond" style="width:15px" maxlength="2" onblur="cio03isMinOrSec(this)"/>
	    				<s:text name="CIO03_second"></s:text>
	    			</span>
    			</td>
    			<!-- 结束时间 -->
      			<th><s:text name="CIO03_endTime"></s:text><span class="highlight">*</span></th>
      			<td class="date">
	      			<span>
		            	<s:textfield id="endDate" name="endDate" cssClass="date"/>
		            	<input id="endHour" class="text" type="text" value="23" name ="endHour" style="width:15px" maxlength="2" onblur="cio03isHour(this,true)"/>
	    				<s:text name="CIO03_hour"></s:text>
	    				<input id="endMinute" class="text" type="text" value="59" name ="endMinute" style="width:15px" maxlength="2" onblur="cio03isMinOrSec(this,true)"/>
	    				<s:text name="CIO03_minute"></s:text>
	    				<input id="endSecond" class="text" type="text" value="59" name ="endSecond" style="width:15px" maxlength="2" onblur="cio03isMinOrSec(this,true)"/>
	    				<s:text name="CIO03_second"></s:text>
		            </span>
	            </td>
      		</tr>
      		<tr>
      			<!-- 问卷类型 -->
      			<th><s:text name="CIO03_paperType"/><span class="highlight">*</span></th>
      			<td><s:select name="paperType" list="#application.CodeTable.getCodes('1107')" listKey="CodeKey" listValue="Value" id="paperType"/></td>
      			<th></th>
      			<td></td>
      		</tr>
      		<tr>
      			<!-- 问卷名称 -->
      			<th><s:text name="CIO03_paperName"/><span class="highlight">*</span></th>
      			<td><input id="paperName" class="text" type="text" value="" name ="paperName" style="width:200px" maxlength="28" onkeyup="return isMaxByteLen(this)"/></td>
      			<th></th>
      			<td></td>
      		</tr>
      	</tbody>
      </table>
      <div><input name="queStr" value="" type = "text" style="display:none" id="queStr"/></div>
       </form>
       </div>
		<hr class="space" />
        <div class="box4" id="addQuestion">
        	<div class="box4-header clearfix" ><strong class="left"><s:text name="CIO03_addQuestion"/></strong></div>	
        	<div class="box4-content clearfix" id = "addQuestion_body" >
          	<div class="clearfix" >
          		<div class="left">
          		<a class="add right" id="addEssay">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO03_addEssay"/></span>
	        	</a>
	        	<a class="add right" id="addApfill" style="display:none">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO03_addApfill"/></span>
	        	</a>
	        	<a class="add right" id="addMultipleChoice">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO03_addMultipleChoices"/></span>
	        	</a>
	        	<a class="add right" id="addSingleChoice">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO03_addSingleChoice"/></span>
	        	</a>
	        	</div>
          	</div>
          	<br/>
          	<!-- 已添加的问题 -->
          	 <div class="box4 hide" id="alreadyAddQuestion">
          	 	<div class="box4-header clearfix"><strong class="left"><s:text name="CIO03_alreadyAddQuestion"/></strong></div>
          	 	<div class="box4-content clearfix" id = "alreadyAddQuestion_body"></div>
          	 </div>
        </div>
      </div>
        <hr class="space" />
        <div class="center clearfix">
          <%-- 保存 --%>
          <button class="save" id ="previewPaper" onclick="previewPaper();"><span class="ui-icon icon-ttl-section-search-result"></span><span class="button-text"><s:text name="CIO03_preview"/></span></button>
          <button class="save" id ="savePaper" onclick="savePaper();"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"/></span></button>
        </div>
    </div>
    <div id="addChoice_dialog_main">
    <div id="addChoice_dialog" class="hide">
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv1" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan1"></span></li>
        </ul>         
    </div>
     <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>         
    </div>
     <div id="errorDiv3" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan3"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    	<div class="clearfix">
    		<div class="left">
    			<span class="left"><s:text name="CIO03_addItem"/></span>
    			<span class="right">
    				<s:text name="CIO03_isRequired"/>
                    <s:select id="choice_isRequired" list="#application.CodeTable.getCodes('1291')" name="isRequired" listKey="CodeKey" listValue="Value"></s:select>
                </span>
       			<textarea id="add_choiceQuestionItem"  maxlength="500" style="width: 520px; height: 38px;resize:none;overflow-y:auto;" rows="3" cols="20"></textarea>
    		</div>
    	</div>
    		<br/>
    	<div class="clearfix">
    		<a class="add" id="addChoice">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO03_addAnswer"/></span>
	        </a>
	        <a class="delete" id="deleteChoice">
	        		<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="CIO03_deleteAnswer"/></span>
	        </a>
    	</div>
    	 	<br/>
    	 <div id="optionDiv">
    	 </div>	
    </div>
    </div>
    <div id="addFill_dialog_main">
     <div id="addFill_dialog" class="hide">
       <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv4" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan4"></span></li>
        </ul>         
    </div>
     <div id="errorDiv5" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan5"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
   		 <div class="clearfix">
    		<div id="fillQuestion_maxPoint" class="hide left">
    			<label><s:text name="CIO03_point"></s:text></label>
    			<input class="text" type="text" id ="fillQuestion_point" maxlength="5" style="width:30px;height:15px"/> 
    		</div>
    	</div>
    	<div class="clearfix">
    		<div class="left">
    			<span><s:text name="CIO03_addItem"/></span>
    			<span class="right">
    				<s:text name="CIO03_isRequired"/>
                	<s:select id="fill_isRequired" list="#application.CodeTable.getCodes('1291')" name="isRequired" listKey="CodeKey" listValue="Value"></s:select>
    			</span>
    			<textarea id="add_fillQuestionItem"  maxlength="500" style="width: 530px; height: 87px;resize:none;overflow-y:auto;" rows="3" cols="20"></textarea>
    		</div>
    	</div>
    	<div id="optionDiv">
    	 </div>	
    </div>
    </div>
    <div id="previewPaper_dialog_main">
    <div id="previewPaper_dialog" class="hide">
    	<table class="detail" id="preview_mainPaper">
    		<tbody>
				<tr>
					<!-- 所属品牌 -->
					<th><s:text name="CIO03_brand"/><span class="highlight">*</span></th>
					<td id="preview_brandInfoId"></td>
					<!-- 问卷总分 -->
					<th><s:text name="CIO03_maxPoint"/><span class="highlight">*</span></th>
					<td id="preview_maxPoint"></td>
				</tr>
				<tr>
					<!-- 问卷类型 -->
					<th><s:text name="CIO03_paperType"/><span class="highlight">*</span></th>
					<td id="preview_paperType"></td>
					<!-- 起止时间 -->
					<th><s:text name="CIO03_Date"></s:text><span class="highlight">*</span></th>
					<td id="preview_date" class="date"></td>
				</tr>
				<tr>
					<!-- 问卷名称 -->
					<th><s:text name="CIO03_paperName"/><span class="highlight">*</span></th>
					<td id="preview_paperName"></td>
					<th></th>
					<td></td>
				</tr>
    		</tbody>
    	</table>
       <div class="section-content clearfix" id="preview_question"></div>
       
    </div>
    </div>
	</div>
<div class="hide">
	<input type="hidden" id="maxSelect" value='<s:text name="CIO03_maxSelect"/>'/>
	<input type="hidden" id="isDelete" value='<s:text name="CIO03_isDelete"/>'/>
	<input type="hidden" id="preview" value='<s:text name="CIO03_preview"/>'/>
	<input type="hidden" id="nextQuestion" value='<s:text name="CIO03_nextQuestion"/>'/>
	<input type="hidden" id="save" value='<s:text name="CIO03_save"/>'/>
	<input type="hidden" id="close" value='<s:text name="CIO03_close"/>'/>
	<input type="hidden" id="to" value='<s:text name="CIO03_to"/>'/>
</div>
</s:i18n>
<span id="url_savePaper" style="display:none">${s_savePaper}</span>
<span id="paperId" class="hide"></span>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg" value='<s:text name="EMO00025"/>'/>
    <input type="hidden" id="errmsg1" value='<s:text name="EMO00026"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="EMO00027"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="EMO00034"/>'/>
    <input type="hidden" id="errmsg4" value='<s:text name="EMO00028"/>'/>
</div>