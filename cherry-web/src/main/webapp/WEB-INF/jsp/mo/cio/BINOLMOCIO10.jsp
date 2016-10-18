<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO10.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
$().ready(function() {
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
	.group-header span{
	position: relative;
	}
</style>
<s:i18n name="i18n.mo.BINOLMOCIO10">
<%-- ================== js国际化  START ======================= --%>
<jsp:include page="/WEB-INF/jsp/mo/cio/BINOLMOCIO10_1.jsp" flush="true" />
<%-- ================== js国际化导入    END  ======================= --%>
	<div id="CIO10_select" class="hide">
		<s:text name="global.page.all" />
	</div>
	<div class="panel ui-corner-all">
	<div class="panel-header">
	<%-- ###问卷制作 --%>
		<div class="clearfix"><span class="breadcrumb left"> 
			<span class="ui-icon icon-breadcrumb"></span>
			<s:text name="CIO10_manage" />&gt;
			<span id="pageTitle"><s:text name="CIO10_title" /></span> </span> <%--新增问卷 --%> 
			<a class="add right" id="addGroup">
	        	<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO10_addGroup"/></span>
	    	</a>
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
	      	<span class="gray"><s:text name="CIO10_userTipFirst"/></span>
	      	<span class="highlight">*</span>
	      	<span class="gray"><s:text name="CIO10_userTip"/></span>
      	</span>
      </p>
      <div>
      <form id="mainForm" class="inline">
      <s:url action="BINOLMOCIO10_saveCheckPaper" id="s_saveCheckPaper"></s:url>
      <table class="detail">
      	<tbody>
      		<tr>
      			<!-- 所属品牌 -->
      			<s:if test="%{brandInfoList != null}">
	      			<th><s:text name="CIO10_brand"/><span class="highlight">*</span></th>
	      			<td><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select></td>
      			</s:if>
      			<!-- 问卷总分 -->
      			<th><s:text name="CIO10_paperPoint"/><span class="highlight">*</span></th>
      			<td><input class="text" type="text" id ="maxPoint" maxlength="5" style="width:35px;height:15px" disabled="disabled" name="maxPoint"/></td>
      		</tr>
      		<tr>
      			<!-- 问卷权限 -->
      			<th><s:text name="CIO10_paperRight"/><span class="highlight">*</span></th>
      			<td><s:select name="paperRight" list="#application.CodeTable.getCodes('1126')" listKey="CodeKey" listValue="Value" id="paperRight"/></td>
      			<!-- 起止时间 -->
      			<th class="date"><s:text name="CIO10_Date"></s:text><span class="highlight">*</span></th>
      			<td>
	      			<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
		            <span>&nbsp;-&nbsp;</span> 
		            <span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
	            </td>
      		</tr>
      		<tr>
      			<%-- 问卷名称 --%>
      			<th><s:text name="CIO10_paperName"/><span class="highlight">*</span></th>
      			<td><input id="paperName" class="text" type="text" value="" name ="paperName" style="width:220px" maxlength="30" onblur="binOLMOCIO10.textCounter(this,30)"/></td>
      			<th></th>
      			<td></td>
      		</tr>
      	</tbody>
      </table>
      <div>
      	<input type = "text" name = "gropStr" id="gropStr" value="" style="display:none" />
   		<input type = "text" name = "queStr" id="queStr" value="" style="display:none" />
   		<input type = "text" name = "paperLevelStr" id="paperLevelStr" value="" style="display:none" />
      </div>
      
       </form>
       </div>
		<hr class="space" />
        <div class="box4" id="addQuestion">
        	<div class="box4-header clearfix" ><strong class="left"><s:text name="CIO10_addQuestion"/></strong>
        	</div>	
        	<div class="box4-content clearfix" id = "addQuestion_body" >
          	<div class="clearfix">
          		<div class="left" >
          		<%-- 
          		<a class="add right" id="addEssay">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO10_addEssay"/></span>
	        	</a>
	        	--%>
	        	<a class="add right" id="addApfill">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO10_addApfill"/></span>
	        	</a>
	        	<%--
	        	<a class="add right" id="addMultipleChoice">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO10_addMultipleChoices"/></span>
	        	</a>
	        	--%>
	        	<a class="add right" id="addSingleChoice">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO10_addSingleChoice"/></span>
	        	</a>
	        	</div>
          	</div>
          	<br/>
          	 <div class="box4" id="alreadyAddQuestion">
          	 	<div class="box4-header clearfix" ><strong class="left"><s:text name="CIO10_alreadyAddQuestion"/></strong></div>
          	 	<div class="box4-content clearfix" id = "alreadyAddQuestion_body" ></div>
          	 </div>
        </div>
      </div>
        <hr class="space" />
        <div class="center clearfix">
          <%-- 保存 --%>
          <button class="save" id ="previewPaper""><span class="ui-icon icon-ttl-section-search-result"></span><span class="button-text"><s:text name="CIO10_preview"/></span></button>
          <button class="confirm" type="button" id="savePaper"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="CIO10_btnPopOk"/></span></button>
        </div>
    </div>
    <div id="addQuestion_dialog_main"  class="hide">
     <div id="addQuestion_dialog">
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
    <%-- ================== 错误信息提示   END  ======================= --%>
   		 <div class="clearfix">
   		 	<s:text name="CIO10_addItem"/>
    		<div id="point" class="right">
    			<label><s:text name="CIO10_maxPoint"></s:text></label>
    			<input class="text" type="text" id ="maxPoint" maxlength="5" style="width:30px;height:15px"/>
    			-
    			<label><s:text name="CIO10_minPoint"></s:text></label>
    			<input class="text" type="text" id ="minPoint" maxlength="5" style="width:30px;height:15px"/>
    		</div>
    	</div>
    	<div class="clearfix">
    		<div class="left">
    			<textarea id="questionItem" style="width: 550px; height: 87px;resize:none;overflow-y:auto;" rows="3" cols="20" onpropertychange="return isMaxLen(this);" maxlength="60"></textarea>
    		</div>
    	</div>
    	<div class="clearfix" id="choiceOption">
    		<a class="add" id="addChoice">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO10_addAnswer"/></span>
	        </a>
	        <a class="delete" id="deleteChoice">
	        		<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="CIO10_deleteAnswer"/></span>
	        </a>
    	</div>
    	<hr class="space" />
    	<div id="optionDiv"></div>	
    </div>
    </div>
    <%--===================================添加分组 START=============================== --%>
    <div id="addGroup_main">
    	<div id = "addGroup_dialog" class="hide">
    		<label><s:text name="CIO10_groupName"/></label>
	    	<s:textfield name="groupName" cssClass="text" maxlength="50"/>
    	</div>
    </div>
    <%--===================================添加分组   END =============================== --%>
    
    <%--===================================显示分组 START=============================== --%>
    <div id="showGroup_main" class="hide">
    	<div id="group1" class="box4">
    		<div class="box4-header clearfix">
    			<input type="radio" id="index" class="left" name="group"/>
    			<div id="optionDisplay" style="cursor:pointer" title="点击此处显示或隐藏该分组问题"><strong class="left"></strong></div>
    			<div class="right">
    				<%--编辑分组按钮 --%>
    				<a id="edit" class="left" style="cursor:pointer" title="<s:text name="CIO10_edit"></s:text>">
    					<span class="ui-icon icon-edit"></span>
    				</a>
    				<%--删除分组按钮 --%>
    				<a id="delete" class="left" style="cursor:pointer" title="<s:text name="CIO10_delete"></s:text>">
    					<span class="ui-icon icon-delete"></span>
    				</a>
    				<%--上移分组按钮 --%>
    				<span  id="moveUp">
    					<span class="left" style="height:16px; width:16px; display:block;"></span>
    				</span>
    				<%--下移分组按钮 --%>
    				<span id="moveDown">
    					<span class="left" style="height:16px; width:16px; display:block;"></span>
    				</span>
    			</div>
    		</div>
    		<%--显示分组中问题 --%>
    		<div class="box4-content clearfix" id="alreadyAddQuestion" style="display:none">
    		</div>
    	</div>
    </div>
    <%--===================================显示分组   END =============================== --%>
    
    <%--===================================显示问卷评分水平 START=============================== --%>
    <div id="showPaperLevel_main" class="hide">
    	<div id="paperLevel">
    		<%--错误信息提示 --%>
    		 <div id="errorDiv" class="actionError" style="display:none">
        		<ul>
            		<li><span id="errorSpan"></span></li>
        		</ul>         
    		</div>
    		<%--问卷总分 --%>
    		<div class="group-header clearfix" style="background:#F8F8F8;border:1px solid #D6D6D6;">
    		<strong><s:text name="CIO10_showPaperMaxPoint"/>
    			<span id="paperMaxPoint"></span>
    		</strong>
    		</div>
    		<div class="group-content clearfix" id="alreadyAddPaperLevel">
    		<hr class="space" />
    		<div class="clearfix" id="levelOption">
    		<%--添加评分点击按钮 --%>
    		<a class="add" id="addLevel">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO10_addLevel"/></span>
	        </a>
    		</div>
    		<hr class="space" />
    		</div>
    	</div>
    </div>
    <%--===================================显示问卷评分水平 END  =============================== --%>
    
    <%--===================================问卷预览 Start  =============================== --%>
    <div id="paperPreview_mian" class="hide">
    	<div id="paperPreview">
    		<div class="panel-content" id="preview_mainPaper">
    		<table class="detail">
    		<tbody>
    		<tr>
    			<%--所属品牌 --%>
      			<th >
	      			<span><s:text name="CIO10_brand"/></span>
	      			<span class="highlight">*</span>
      			</th>
	      		<td id="preview_brandInfoId"></td>
	      			
      			<%--问卷权限 --%>
      			<th >
	      			<span><s:text name="CIO10_paperRight"/></span>
	      			<span class="highlight">*</span>
      			</th>
	      		<td id="preview_paperRight"></td>
       		</tr>
        	<tr>
        		<%--问卷总分 --%>
        		<th >
	      			<span><s:text name="CIO10_paperPoint"/></span>
	      			<span class="highlight">*</span>
      			</th>
	      		<td id="preview_maxPoint"></td>
	      		<%--起止时间 --%>
	      		<th >
	      			<span><s:text name="CIO10_Date"/></span>
	      			<span class="highlight">*</span>
      			</th>
	      		<td id="preview_date"></td>
       		</tr>
       		<tr>
        	<!-- <div class="left" style="width:490px;margin:0px 10px 10px 10px"> -->
          		<%-- 问卷名称 --%>
          		<th >
	      			<span><s:text name="CIO10_paperName"/></span>
	      			<span class="highlight">*</span>
      			</th>
	      		<td id="preview_paperName"></td>
	      		<th></th>
	      		<td></td>
      		 </tr>
      		 </tbody>
      		 </table>
       <div class="left" id="preview_question" style="width:100%">
       		
       </div>
    	</div>
    </div>
	</div>
	
    <%--===================================问卷预览       End  =============================== --%>
    
	<%--===================================警告信息 Start  =============================== --%>
	
 	 <div id="warmDialogInit_main" class="hide">
    	<div id="warmDialogInit">
    	<p class="message"><span id="warmMessage"></span></p>
    	</div>
     </div>
 	 
 	 <%--===================================警告信息       End  =============================== --%>
</s:i18n>
<span id="saveCheckPaper" style="display:none">${s_saveCheckPaper}</span>
<span id="paperId" class="hide"></span>
<%--错误信息提示 --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg" value='<s:text name="EMO00025"/>'/>
    <input type="hidden" id="errmsg1" value='<s:text name="EMO00026"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="EMO00033"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="EMO00028"/>'/>
    <input type="hidden" id="errmsg4" value='<s:text name="EMO00034"/>'/>
    <input type="hidden" id="errmsg5" value='<s:text name="EMO00035"/>'/>
    <input type="hidden" id="errmsg6" value='<s:text name="EMO00037"/>'/>
    <input type="hidden" id="errmsg7" value='<s:text name="EMO00027"/>'/>
</div>