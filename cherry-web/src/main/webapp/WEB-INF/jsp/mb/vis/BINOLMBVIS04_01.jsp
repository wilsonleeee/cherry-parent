<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mb.BINOLMBVIS04">
<div id="aaData">
	<s:iterator value="visitTaskList" id="visitTaskMap">
		<ul>
			<li><input type="checkbox" name="visitTaskIdList" id="visitTaskId" value='<s:property value="visitTaskId"/>'/></li>
			<li><span><s:property value="visitTypeName"/></span></li>
			<li><span><s:property value="memCode"/></span></li>
			<li><span><s:property value="memName"/></span></li>
			<li><span><s:property value="birthDay"/></span></li>
			<li><span><s:property value="counterName"/></span></li>
			<li><span><s:property value="employeeName"/></span></li>
			<li><span><s:property value="startTime"/></span></li>
			<li><span><s:property value="endTime"/></span></li>
			<li>
			  <s:if test='@com.cherry.cm.core.CherryConstants@TASK_RESULT_1.equals(visitFlag)'>
			    <span class="task-verified_rejected">
			   		<span ><s:property value='#application.CodeTable.getVal("1209", visitFlag)' /></span>
			    </span>
			  </s:if>
			  <s:elseif test='@com.cherry.cm.core.CherryConstants@TASK_RESULT_2.equals(visitFlag)'>
			    <span class="task-verified_rejected">
			   		<span ><s:property value='#application.CodeTable.getVal("1209", visitFlag)' /></span>
			    </span>
			  </s:elseif>
			  <s:elseif test='@com.cherry.cm.core.CherryConstants@TASK_RESULT_3.equals(visitFlag)'>
			    <span class="task-verified">
			   		<span ><s:property value='#application.CodeTable.getVal("1209", visitFlag)' /></span>
			    </span>
			  </s:elseif>
			  <s:elseif test='@com.cherry.cm.core.CherryConstants@TASK_RESULT_4.equals(visitFlag)'>
			    <span class="task-verified_unsubmit">
			   		<span ><s:property value='#application.CodeTable.getVal("1209", visitFlag)' /></span>
			    </span>
			  </s:elseif>
			</li>
			<li><span><s:property value="visitTime"/></span></li>
			<li>
			  <s:if test='@com.cherry.cm.core.CherryConstants@TASK_FLAG_PEOCEED.equals(taskState)'>
			    <span class="verifying">
			   		 <span><s:property value='#application.CodeTable.getVal("1207", taskState)' /></span>
			    </span>
			  </s:if>
			  <s:elseif test='@com.cherry.cm.core.CherryConstants@TASK_FLAG_CANCEL.equals(taskState)'>
			    <span class="task-verified_rejected">
			   		 <span><s:property value='#application.CodeTable.getVal("1207", taskState)' /></span>
			    </span>
			  </s:elseif>
			  <s:elseif test='@com.cherry.cm.core.CherryConstants@TASK_FLAG_AGREE.equals(taskState)'>
			    <span class="task-verified">
			   		 <span><s:property value='#application.CodeTable.getVal("1207", taskState)' /></span>
			    </span>
			  </s:elseif>
			</li>
			<li><span><s:property value='#application.CodeTable.getVal("1206", synchroFlag)' /></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
