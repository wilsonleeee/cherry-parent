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
				<th><s:text name="binolmbvis01_Performer" /></th>
				<td>
				<select style="width:200px;" name="employeeIDs" id="employeeIDs" >
		            <s:iterator value="employeeList">
					         		<option value="<s:property value="BIN_EmployeeID" />*<s:property value="BaCode" />">
					         			<s:property value="BaName"/>
					         		</option>
					</s:iterator>
		        </select>	
				</td>
	       	</tr>	
		</table>
	</cherry:form>
	<script type="text/javascript">
</script>
</s:i18n>