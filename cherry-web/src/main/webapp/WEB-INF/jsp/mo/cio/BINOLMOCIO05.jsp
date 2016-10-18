<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO05.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
 var previewPaper_dialog_main = "";
 var questionList="";
 $().ready(function() {
	 previewPaper_dialog_main = $("#previewPaper_dialog_main").html();
	 cherryValidate({			
			formId: "mainForm",		
			rules: {
				startDate: {dateValid:true},	// 开始日期
				endDate: {dateValid:true}	// 结束日期
			}		
		});
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
		questionList = $("#questionList1").val();
		if (window.opener) {
		       window.opener.lockParentWindow();
		}
		$("#save").click(function(){
			// 动态计算总分
			calculationMaxPoint();
		});
		$("input[id^=point]").each(function(){
			$(this).focusout(function() {
				// 动态计算总分
				calculationMaxPoint();
			});
		});
		// 加载问题明细
		quesNaviLoad(questionList);
		
		// 此按钮已放弃使用
		/* $("#addQuestion").click(function(){
			addQuestion(this);
		}); */
	 });
</script>
<style>
	.group-header1 { border:1px solid #d6d6d6; height:23px; background:#F8F8F8; position:relative; text-indent:5px; }
	.group-header1 span {
    right: 0.15em;
    top: 0.15em;
	}
	.group-content1 {
    background: none repeat scroll 0 0 #FFFFFF;
    border: 1px solid #D6D6D6;
    padding: 10px;
    position: relative;
    top: -1px;
	}
	.text1{
    height: 16px;
    line-height: 16px;
    padding: 2px 2px 1px;
    vertical-align: baseline;
    width: 45px;
    border:1px solid #CCCCCC;
    background-color:#FFFFFF;
	}
</style>
<s:i18n name="i18n.mo.BINOLMOCIO05">
<s:if test="%{paperMap != null || !paperMap.isEmpty()}">
<%-- ================== js国际化  START ======================= --%>
<jsp:include page="/WEB-INF/jsp/mo/cio/BINOLMOCIO05_1.jsp" flush="true" />
<%-- ================== js国际化导入    END  ======================= --%>
<s:url id="savePaper_url" value="/mo/BINOLMOCIO05_savePaper"/>
<div class="panel ui-corner-all">
<div class="main clearfix">
<div class="panel-header">
     	<%-- ###问卷查询 --%>
       <div class="clearfix"> 
       	<span class="breadcrumb left"> 
       		<span class="ui-icon icon-breadcrumb"></span>
       		<s:if test='"0".equals(editFlag)'>
       			<s:text name="CIO05_manage"/>&gt; <s:text name="CIO05_title"/>
       		</s:if><s:elseif test='"1".equals(editFlag)'>
       			<s:text name="CIO05_manage"/>&gt; <s:text name="CIO05_copyTitle"/>
       		</s:elseif>
       	</span>
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
    <div class="panel-content" >
      <div>
      <form id="mainForm" class="inline">
      
      <div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="global.page.title"/><%-- 基本信息 --%>
              	</strong>
              </div>
      <div class="section-content">
      <table class="detail">
      	<tbody>
      		<tr>
      			<!-- 所属品牌 -->
      			<th><s:text name="CIO05_brandName"/><span class="highlight">*</span></th>
      			<td><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;" disabled="true"></s:select></td>
     			<!-- 问卷总分 -->
      			<th><s:text name="CIO05_maxPoint"/><span class="highlight">*</span></th>
      			<td><s:textfield name="maxPoint" cssClass="text1" value="%{paperMap.maxPoint}" disabled="true"></s:textfield></td>
      		</tr>
      		<tr>
      			<!-- 开始时间 -->
      			<th><s:text name="CIO05_startDate"></s:text><span class="highlight">*</span></th>
      			<td>
      				<span>
		   				<s:textfield id="startDate" name="startDate" cssClass="date" value="%{paperMap.startDate}"/>
		   				<input id="startHour" class="text" type="text" value="<s:property value='%{paperMap.startHour}'/>" name ="startHour" style="width:15px" maxlength="2" onblur="cio05isHour(this)"/>
		   				<s:text name="CIO05_hour"></s:text>
		   				<input id="startMinute" class="text" type="text" value="<s:property value='%{paperMap.startMinute}'/>" name ="startMinute" style="width:15px" maxlength="2" onblur="cio05isMinOrSec(this)"/>
		   				<s:text name="CIO05_minute"></s:text>
		   				<input id="startSecond" class="text" type="text" value="<s:property value='%{paperMap.startSecond}'/>" name ="startSecond" style="width:15px" maxlength="2" onblur="cio05isMinOrSec(this)"/>
		   				<s:text name="CIO05_second"></s:text>
   					</span>
      			</td>
      			<!-- 结束时间 -->
      			<th><s:text name="CIO05_endDate"></s:text><span class="highlight">*</span></th>
      			<td>
      				<span>
		            	<s:textfield id="endDate" name="endDate" cssClass="date" value="%{paperMap.endDate}"/>
		            	<input id="endHour" class="text" type="text" value="<s:property value='%{paperMap.endHour}'/>" name ="endHour" style="width:15px" maxlength="2" onblur="cio05isHour(this,true)"/>
	    				<s:text name="CIO05_hour"></s:text>
	    				<input id="endMinute" class="text" type="text" value="<s:property value='%{paperMap.endMinute}'/>" name ="endMinute" style="width:15px" maxlength="2" onblur="cio05isMinOrSec(this,true)"/>
	    				<s:text name="CIO05_minute"></s:text>
	    				<input id="endSecond" class="text" type="text" value="<s:property value='%{paperMap.endSecond}'/>" name ="endSecond" style="width:15px" maxlength="2" onblur="cio05isMinOrSec(this,true)"/>
	    				<s:text name="CIO05_second"></s:text>
	            	</span>
      			</td>
      		</tr>
      		<tr>
      			<!-- 问卷类型 -->
      			<th><s:text name="CIO05_paperType"/><span class="highlight">*</span></th>
				<td><s:select name="paperType" list="brandInfoList"
						listValue='#application.CodeTable.getVal("1107", paperMap.paperType)'
						listKey='%{paperMap.paperType}' id="paperType"
						disabled="true" /></td>
				<th></th>
      			<td></td>
      		</tr>
      		<tr>
      			<!-- 问卷名称 -->
      			<th><s:text name="CIO05_paperName"/><span class="highlight">*</span></th>
      			<td><s:textfield name="paperName" cssClass="text" cssStyle="width:200px" value="%{paperMap.paperName}" maxlength="28" onkeyup="return isMaxByteLen(this)"></s:textfield></td>
      			<th></th>
      			<td></td>
      		</tr>
      	</tbody>
      </table>
       </div>
       </div>
       <s:hidden value="%{paperMap.modifyCount}" name="modifyCount"></s:hidden>
       <s:hidden value="%{paperMap.updateTime}" name="updateTime"></s:hidden>
       <s:hidden value="%{paperMap.editFlag}" name="editFlag"></s:hidden>
       <input type = "text" name = "questionList" id="questionList" value="" style="display:none" />
       </form>
       </div>
       <hr class="space" />
       <%-- 
       <div id = "questionNavigation">
       		<div class="group-header1"><strong class="left"><s:text name="CIO05_questionNavigation"/></strong></div>
       		<div class="group-content1 clearfix" id = "questionNavigation_body">
       		</div>
       </div>
       --%>
       <hr class="space" />
       <div class="group" id="editQuestion">
       		<div class="box4-header clearfix" style="border:1px solid #FAD163"><strong class="left"><s:text name="CIO05_editQuestion"/></strong></div>
       		<div class="group-content1 clearfix" id = "editQuestion_body" style="border:1px solid #FAD163">
       			<div class="clearfix" style="padding-bottom:10px">
          			<div class="left">
          				<%-- <a class="add right" id="addEssay" onclick="showAddQuesDialog('0');">
	        				<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO05_addAssay"/></span>
	        			</a>
	        			<a class="add right" id="addApfill" onclick="showAddQuesDialog('3');">
	        				<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO05_addApFill"/></span>
	        			</a>
	        			<a class="add right" id="addMultipleChoice" onclick="showAddQuesDialog('2');">
	        				<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO05_addMulChioce"/></span>
	        			</a> --%>
	        			<a class="add right" id="addQuestionButton" onclick="showAddQuesDialog();">
	        				<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO05_addQuestion"/></span>
	        			</a>
	        		</div>
          		</div>
          		<div id="showQuesMain_body" class="clearfix"></div>
       			<div id = "div_main">
       				<div id="questionContent_body"></div>
       			</div>
       			<%-- 添加问题按钮
       			<div class="clearfix right" style="margin:10px 10px 10px 10px">
       				<a class="add" id="addQuestion">
	        			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO05_addQuestion"/></span>
	       			</a>
       			</div>
       			--%>
       		</div>
       </div>
    
	    <%-- 操作按钮 --%>
	    <div id="editButton" class="center clearfix">
	    	<!-- 复制问卷有预览功能(编辑也加入预览功能) -->
			<%-- <s:if test='"1".equals(editFlag)'> --%>
				<button class="save" id="previewPaper" onclick="cio05_previewPaper();">
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<span class="button-text"><s:text name="CIO05_preview" /></span>
				</button>
			<%-- </s:if> --%>
			<button class="save" id="savePaper" onclick="cio05_savePaper();">
	    		<span class="ui-icon icon-save"></span>
	    		<span class="button-text"><s:text name="global.page.save"/></span>
	     	</button>
	     	<button id="close" class="close" type="button"  onclick="window.close();return false;">
	    		<span class="ui-icon icon-close"></span>
	    		<span class="button-text"><s:text name="global.page.close"/></span>
	  		</button>
	    </div>
	    <hr class="space" />
	  </div>  
    </div>
</div>
<%-- ===============================预览div 开始==========================================--%>
<div id="previewPaper_dialog_main">
    <div id="previewPaper_dialog" class="hide">
    	<table class="detail" id="preview_mainPaper">
    		<tbody>
				<tr>
					<!-- 所属品牌 -->
					<th><s:text name="CIO05_brandName"/><span class="highlight">*</span></th>
					<td id="preview_brandInfoId"></td>
					<!-- 问卷总分 -->
					<th><s:text name="CIO05_maxPoint"/><span class="highlight">*</span></th>
					<td id="preview_maxPoint"></td>
				</tr>
				<tr>
					<!-- 问卷类型 -->
					<th><s:text name="CIO05_paperType"/><span class="highlight">*</span></th>
					<td id="preview_paperType"></td>
					<!-- 起止时间 -->
					<th><s:text name="CIO05_date"></s:text><span class="highlight">*</span></th>
					<td id="preview_date" class="date"></td>
				</tr>
				<tr>
					<!-- 问卷名称 -->
					<th><s:text name="CIO05_paperName"/><span class="highlight">*</span></th>
					<td id="preview_paperName"></td>
					<th></th>
					<td></td>
				</tr>
    		</tbody>
    	</table>
       <div class="section-content clearfix" id="preview_question"></div>
    </div>
</div>
<%-- ===============================预览div 结束=============================================--%>

<%-- 编辑问题div 开始--%>
<div id="editQuestion_model" class="hide">
	<div id="questionHeader_model">
		<div class="group-header1">
       		<div class="left">
       			<strong><span class="highlight"><s:text name="CIO05_question"/>：</span><span id="numberShow" class="highlight"></span><span id="questionType"></span></strong>
       		</div>
       		<div class="left" id="choice_questionType">
		        <s:select name="questionType" id="questionTypeChoice" list='#application.CodeTable.getCodes("1307")' listKey="CodeKey" listValue="Value"></s:select>
			</div>
       		<div class="right">
       			<strong class="left"><s:text name="CIO05_displayOrder"/>：<input type="text" class="text1" id="displayOrder" style="width:30px;margin:1px;"/></strong>
       		</div>
       </div>
	</div>
	<div id="choiQuesCon_model">
		<div class="group-content1 clearfix" id="question_content">
       		<div class="clearfix" style="margin:0px 10px 10px 10px">
       		<span class="left" id="choice_isRequired">
				<s:text name="CIO05_isRequired"/>
		        <s:select name="isRequired" id="isRequired" list="#application.CodeTable.getCodes('1291')" listKey="CodeKey" listValue="Value"></s:select>
			</span>
       			<textarea id="questionItem" style="width: 513px; height: 38px;resize:none;overflow-y:auto;" rows="3" cols="20"  maxlength="500"></textarea>
       		</div>
       		<div class="clearfix" style="margin:0px 10px 10px 10px">
    			<a class="add" id="addChoice">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO05_addChoice"/></span>
	       		</a>
    		</div>	
       	</div>
	</div>
	<div id="noChoiQuesCon_model">
		<div class="group-content1 clearfix" id = "question_content">
       		<div class="clearfix" style="margin:0px 10px 10px 10px">
       		<span class="left" id="noChoice_isRequired">
				<s:text name="CIO05_isRequired"/>
		        <s:select name="isRequired" id="isRequired" list="#application.CodeTable.getCodes('1291')" listKey="CodeKey" listValue="Value"></s:select>
			</span>
       		<div id="questionPoint">
       			<span><s:text name="CIO05_point"/>：</span>
    			<input type="text" class="text1" id="point"/>
    		</div>
       			<textarea id="questionItem" style="width: 513px; height: 38px;resize:none;overflow-y:auto;" rows="3" cols="20"  maxlength="500"></textarea>
       		</div>
       	</div>
	</div>
</div>
<%-- 编辑问题div 结束--%>

<div class="hide">
	<span id="sinChoice"><s:text name="CIO05_sinChoice"/></span>
	<span id="mulChioce"><s:text name="CIO05_mulChioce"/></span>
	<span id="apFill"><s:text name="CIO05_apFill"/></span>
	<span id="assay"><s:text name="CIO05_assay"/></span>
	<span id="sinChoice_title"><s:text name="CIO05_addSinChoice"/></span>
	<span id="mulChioce_title"><s:text name="CIO05_addMulChioce"/></span>
	<span id="apFill_title"><s:text name="CIO05_addApFill"/></span>
	<span id="assay_title"><s:text name="CIO05_addAssay"/></span>
	<span id="preview"><s:text name="CIO05_preview"/></span>
	<span id="closePreview"><s:text name="CIO05_close"/></span>
	<span id="to"><s:text name="CIO05_to"/></span>
</div>

<!-- 添加问题 -->
<%-- <div id="makeSureQuesType" class="hide ui-option ui-option-1">
	 <ul class="u1">
  	单选题
    	<li id ="addQuestionButton" class="options-1" onclick="showAddQuesDialog();"><s:text name="CIO05_addQuestion"/></li>
    多选题
    	<li id ="seleMulChoice" class="options-2" onclick="showAddQuesDialog('2');"><s:text name="CIO05_addMulChioce"/></li>
    填空题
    	<li id ="seleApFill" class="options-1" onclick="showAddQuesDialog('3');"><s:text name="CIO05_addApFill"/></li>
    问答题
    	<li id ="seleAssay" class="options-2" onclick="showAddQuesDialog('0');"><s:text name="CIO05_addAssay"/></li>
  </ul>
</div> --%>

<span id = "savePaper1" class="hide">${savePaper_url}</span>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="EMO00026"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="EMO00027"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="EMO00034"/>'/>
    <input type="hidden" id="errmsg4" value='<s:text name="EMO00028"/>'/>
    <input type="hidden" id="errmsg5" value='<s:text name="EMO00038"/>'/>
</div>
</s:if>
<s:else>
<jsp:include page="/WEB-INF/jsp/common/actionResultBody.jsp" flush="true" />
</s:else>
<input type="hidden" value="<s:property value='%{questionList}'/>" id="questionList1" name="questionList1"/>
</s:i18n>