<%-- 任務详情--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBVIS01">
	<div id="actionResultDisplay"></div>
	<cherry:form id="main_form" csrftoken="false">
		<div class="section">
       		<div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="binolmbvis01_taskDetails"/><%-- 任务信息 --%>
              	</strong>
             </div>
             <div class="section-content clearfix" id="paperDetail">
             <table class="detail">
			<tr>
				<th><s:text name="binolmbvis01_visitTime" /></th>
	       		<td><s:property value="visitTask.VisitTime" /></td>
	       		<th><s:text name="binolmbvis01_visitType" /></th>
	       		<td><s:property value='#application.CodeTable.getVal("1208", visitTask.VisitType)' /></td>
	       	</tr>	
	       	<tr>
				<th><s:text name="binolmbvis01_sData" /></th>
	       		<td><s:property value="visitTask.StartTime" /></td>
	       		<th><s:text name="binolmbvis01_eData" /></th>
	       		<td><s:property value="visitTask.EndTime" /></td>
	       	</tr>
	       	<tr>
				<th><s:text name="binolmbvis01_synchroFlag" /></th>
	       		<td><s:property value='#application.CodeTable.getVal("1206", visitTask.SynchroFlag)' /></td>
	       		<th><s:text name="binolmbvis01_taskState" /></th>
	       		<td><s:property value='#application.CodeTable.getVal("1207", visitTask.TaskState)' /></td>
	       	</tr>
	       	<tr>
				<th><s:text name="binolmbvis01_firstBillNS" /></th>
	       		<td><s:property value="visitTask.FirstBillNS" /></td>
	       		<th><s:text name="binolmbvis01_lastBillNS" /></th>
	       		<td><s:property value="visitTask.LastBillNS" /></td>
	       	</tr>
	       	</table>
	       	</div>
	       	<div class="section">
       		<div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="binolmbvis01_member"/><%-- 会员信息 --%>
              	</strong>
             </div>
             <div class="section-content clearfix" id="paperDetail">
             <table class="detail">
	       	<tr>
				<th><s:text name="binolmbvis01_memCode" /></th>
	       		<td><s:property value="visitTask.MemberCode" /></td>
	       		<th><s:text name="binolmbvis01_memberName" /></th>
	       		<td><s:property value="visitTask.MemberName" /></td>
	       	</tr>
	       	<tr>
				<th><s:text name="binolmbvis01_mobilePhone" /></th>
	       		<td><s:property value="visitTask.MobilePhone" /></td>
	       		<th><s:text name="binolmbvis01_birthDay" /></th>
	       		<td><s:property value="visitTask.BirthDay" /></td>
	       	</tr>
	       	<tr>
	       		<th><s:text name="binolmbvis01_joinTime" /></th>
	       		<td><s:property value="visitTask.JoinTime" /></td>
	       	</tr>
             </table>
       		</div>
       		</div>
       		<s:if test='"2".equals(visitTask.TaskState)'>
       		<div class="section">
       		<div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="binolmbvis01_taskDetailsTitle"/><%-- 回访信息 --%>
              	</strong>
             </div>
             <div class="section-content clearfix" id="paperDetail">
             <table class="detail">
		       	<tr>
		       		<th><s:text name="binolmbvis01_visitTaskName" /></th>
		       		<td><s:property value="VisitTaskDetails.VisitTaskName" /></td>
		       		<th><s:text name="binolmbvis01_VisitCode" /></th>
		       		<td><s:property value="VisitTaskDetails.VisitCode" /></td>
		       	</tr>	
		       	<tr>
					<th><s:text name="binolmbvis01_sData" /></th>
		       		<td><s:property value="VisitTaskDetails.VisitBeginTime" /></td>
		       		<th><s:text name="binolmbvis01_eData" /></th>
		       		<td><s:property value="VisitTaskDetails.VisitEndTime" /></td>
		       	</tr>	
		       	<tr>
					<th><s:text name="binolmbvis01_taskVisitTime" /></th>
		       		<td><s:property value="VisitTaskDetails.VisitTime" /></td>
		       		<th><s:text name="binolmbvis01_visitType" /></th>
		       		<td><s:property value='#application.CodeTable.getVal("1208", VisitTaskDetails.VisitTypeCode)' /></td>

		       	</tr>	
		       	<tr>
					<th><s:text name="binolmbvis01_visitResult" /></th>
		       		<td><s:property value='#application.CodeTable.getVal("1209", VisitTaskDetails.VisitFlag)' /></td>		       		
		       	</tr>
             </table>
       		</div>
       		</div>
       	</s:if>
	</div>
	</cherry:form>
	<script type="text/javascript">
</script>
</s:i18n>