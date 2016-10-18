<%-- 编辑--%>
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
				<td><input style="width: 100%;" disabled="disabled "
					name="configCode"
					value="<s:property value="posConfig.ConfigCode" />" /></td>
			</tr>
			<tr>
				<th><s:text name="MAN08_configNote" /></th>
				<td><input style="width: 100%;" disabled="disabled "
					name="configNote"
					value="<s:property value="posConfig.ConfigNote"/>" /></td>
			</tr>
			<tr>
				<th><s:text name="MAN08_configType" /></th>
				<td><input style="width: 100%;" disabled="disabled "
					name="configType"
					value="<s:property value="posConfig.ConfigType"/>" /></td>
			</tr>
			<tr>
				<th><s:text name="MAN08_configValue" /></th>
				<td><input style="width: 100%;" Type="test " maxlength="50"
					id="configValue" name="configValue"
					value="<s:property value="posConfig.ConfigValue"/>" /></td>
			</tr>
		</table>
		<input type="hidden" name="posConfigID"
			value="<s:property value="posConfig.BIN_PosConfigID"/>" />
		<input type="hidden" name="organizationInfoId"
			value="<s:property value="posConfig.BIN_OrganizationInfoID"/>" />
		<input type="hidden" name="brandInfoId"
			value="<s:property value="posConfig.BIN_BrandInfoID"/>" />
		<input type="hidden" name="configCode"
			value="<s:property value="posConfig.ConfigCode"/>" />
		<input type="hidden" name="configNote"
			value="<s:property value="posConfig.ConfigNote"/>" />
		<input type="hidden" name="configType"
			value="<s:property value="posConfig.ConfigType"/>" />
		<input type="hidden" name="validFlag"
			value="<s:property value="posConfig.ValidFlag"/>" />
	</form>
	<script type="text/javascript">
</script>
</s:i18n>