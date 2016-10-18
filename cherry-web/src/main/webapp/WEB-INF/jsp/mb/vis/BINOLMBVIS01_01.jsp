<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBVIS01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="visitTaskInfoList" id="visitTaskInfoList">
<s:url id="detailsUrl" value="/mo/BINOLMOCIO04_init">
	<s:param name="paperId">${PaperID}</s:param>
</s:url>
<s:url id="PaperdetailsUrl" value="/mo/BINOLMORPT04_init">
	<s:param name="paperAnswerId">${BIN_PaperAnswerID}</s:param>
</s:url>
<ul>
<li><s:checkbox id="validFlag"  value="false" name="visitTaskIDs" value="false" fieldValue="%{BIN_VisitTaskID+'*'+TaskState+'*'+SynchroFlag+'*'+BIN_OrganizationID}" onclick="checkSelect(this);" /></li>
<li><span ><s:property value='#application.CodeTable.getVal("1208", VisitType)' /></span></li>
<li><span><s:property value="MemberName"/></span></li>
<li><span><s:property value="BirthDay"/></span></li>
<li><span><s:property value="MemberCode"/></span></li>
<li><span><s:property value="CounterNameIF"/></span></li>
<li><span><s:property value="StartTime"/></span></li>
<li><span><s:property value="EndTime"/></span></li>
<li><span><s:property value="EmployeeName"/></span></li>
<li>
 <s:if test='@com.cherry.cm.core.CherryConstants@TASK_FLAG_PEOCEED.equals(TaskState)'>
    <span class="verifying">
   		 <span><s:property value='#application.CodeTable.getVal("1207", TaskState)' /></span>
    </span>
 </s:if>
  <s:elseif test='@com.cherry.cm.core.CherryConstants@TASK_FLAG_CANCEL.equals(TaskState)'>
    <span class="task-verified_rejected">
   		 <span><s:property value='#application.CodeTable.getVal("1207", TaskState)' /></span>
    </span>
 </s:elseif>
  <s:elseif test='@com.cherry.cm.core.CherryConstants@TASK_FLAG_AGREE.equals(TaskState)'>
    <span class="task-verified">
   		 <span><s:property value='#application.CodeTable.getVal("1207", TaskState)' /></span>
    </span>
 </s:elseif>
</li>
<li><span><s:property value="VisitTime" /></span></li>
<li>
 <s:if test='@com.cherry.cm.core.CherryConstants@TASK_RESULT_1.equals(VisitResult)'>
    <span class="task-verified_rejected">
   		<span ><s:property value='#application.CodeTable.getVal("1209", VisitResult)' /></span>
    </span>
 </s:if>
  <s:elseif test='@com.cherry.cm.core.CherryConstants@TASK_RESULT_2.equals(VisitResult)'>
    <span class="task-verified_rejected">
   		<span ><s:property value='#application.CodeTable.getVal("1209", VisitResult)' /></span>
    </span>
 </s:elseif>
  <s:elseif test='@com.cherry.cm.core.CherryConstants@TASK_RESULT_3.equals(VisitResult)'>
    <span class="task-verified">
   		<span ><s:property value='#application.CodeTable.getVal("1209", VisitResult)' /></span>
    </span>
 </s:elseif>
   <s:elseif test='@com.cherry.cm.core.CherryConstants@TASK_RESULT_4.equals(VisitResult)'>
    <span class="task-verified_unsubmit">
   		<span ><s:property value='#application.CodeTable.getVal("1209", VisitResult)' /></span>
    </span>
 </s:elseif>
</li>
<li>
	<a id="51" class="delete" onclick="binolmbvis01.details(<s:property value="BIN_VisitTaskID"/>);return false;">
		<span class="ui-icon ui-icon-document"></span> 
		<span class="button-text"><s:text name="binolmbvis01_detailsTitle" /></span>
	</a>
	<s:if test='"0".equals(TaskState)'>
		<s:if test='PaperID!=NULL'>
			<a href="${detailsUrl}" class="delete" onclick="javascript:openWin(this);return false;">
				<span class="ui-icon icon-ttl-section-search-result"></span> 
				<span class="button-text"><s:text name="binolmbvis01_questionnaire" /></span>
			</a>
		</s:if>
	</s:if>
	<s:elseif test='"2".equals(TaskState)'>
		<s:if test='BIN_PaperAnswerID!=NULL'>
			<a href="${PaperdetailsUrl}" class="delete" onclick="javascript:openWin(this);return false;">
				<span class="ui-icon icon-ttl-section-info-edit"></span> 
				<span class="button-text"><s:text name="binolmbvis01_answer" /></span>
			</a>
		</s:if>
	</s:elseif>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>