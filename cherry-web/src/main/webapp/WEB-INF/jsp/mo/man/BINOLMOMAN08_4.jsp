<%-- 新增--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mo.BINOLMOMAN08">
	<div id="actionResultDisplay"></div>
	<form id="main_form">
		<table width="80%" class="detail" border="1" cellspacing="0"
			cellpadding="0">
			<tr>
				<th><s:text name="MAN08_configCode" /></th>
				<td><input style="width: 100%;" Type="test " maxlength="50"
					id="configCode" name="configCode"
					value="" /></td>
			</tr>
			<tr>
				<th><s:text name="MAN08_configNote" /></th>
				<td><input style="width: 100%;" Type="test " maxlength="50"
					id="configNote" name="configNote"
					value="" /></td>
			</tr>
			<tr>
				<th><s:text name="MAN08_configType" /></th>
				<td><input style="width: 100%;" Type="test " maxlength="50"
					id="configType" name="configType"
					value="" /></td>
			</tr>
			<tr>
				<th><s:text name="MAN08_configValue" /></th>
				<td><input style="width: 100%;" Type="test " maxlength="50"
					id="configValue" name="configValue"
					value="" /></td>
			</tr>
		</table>
	</form>
	<script type="text/javascript">
</script>
</s:i18n>