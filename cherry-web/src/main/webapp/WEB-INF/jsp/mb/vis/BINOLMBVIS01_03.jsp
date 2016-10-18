<%-- 编辑--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBVIS01">
	<div id="actionResultDisplay"></div>
	<cherry:form id="main_form" csrftoken="false">
		<table width="80%" class="detail" border="1" cellspacing="0"
			cellpadding="0">
			<tr>
				<th><s:text name="binolmbvis01_memberName" /></th>
	       		<td><s:property value="VisitTaskDetails.MemberName" /></td>
	       		<th><s:text name="binolmbvis01_eData" /></th>
	       		<td><s:property value="VisitTaskDetails.VisitTaskName" /></td>
	       	</tr>	
	       	<tr>
				<th><s:text name="binolmbvis01_sData" /></th>
	       		<td><s:property value="VisitTaskDetails.VisitBeginTime" /></td>
	       		<th><s:text name="binolmbvis01_eData" /></th>
	       		<td><s:property value="VisitTaskDetails.VisitEndTime" /></td>
	       	</tr>	
	       	<tr>
				<th><s:text name="binolmbvis01_VisitCode" /></th>
	       		<td><s:property value="VisitTaskDetails.VisitTime" /></td>
	       		<th><s:text name="binolmbvis01_VisitCode" /></th>
	       		<td><s:property value="VisitTaskDetails.VisitCode" /></td>
	       	</tr>	
	       	<tr>
				<th><s:text name="binolmbvis01_visitResult" /></th>
	       		<td><s:property value='#application.CodeTable.getVal("1209", VisitTaskDetails.VisitFlag)' /></td>
	       		<th><s:text name="binolmbvis01_visitType" /></th>
	       		<td><s:property value='#application.CodeTable.getVal("1208", VisitTaskDetails.VisitTypeCode)' /></td>
	       	</tr>
		</table>
	</cherry:form>
	<script type="text/javascript">
</script>
</s:i18n>