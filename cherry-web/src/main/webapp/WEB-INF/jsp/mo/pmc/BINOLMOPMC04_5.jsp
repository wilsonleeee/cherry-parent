<!-- 编辑补登销售的系统配置项 -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>

<script type="text/javascript">
	
	cherryValidate({
		formId: 'editConfigForm',	
		rules: {
			configValue:{required: true,integerValid: true}
		}	
	});
</script>
<s:i18n name="i18n.mo.BINOLMOPMC04">
<!-- 保存对补登销售记录天数的编辑 -->
<div class="hide">
	<s:url action="BINOLMOPMC04_updateConfigValue" id="updateConfigValue_url">
	</s:url>
	<a id="updateConfigValueUrl" href="${updateConfigValue_url}"></a>
</div>
<div class="clearfix">
   	 <form id="editConfigForm">
   		<div>
   		<s:hidden name="machineType"/>
   		<table style="margin:auto; width:100%;" class="detail">
   		<tr>
	 		<th><s:text name="PMC04_addModifyDate"/></th>
	 		<td>
	 			<span><s:textfield id="configValue" name="configValue" cssClass="text" maxlength="10"/></span>
	 			<span><s:text name="PMC04_day"/></span>
	 		</td>
		</tr>
   		</table>
   		</div>
   	</form>
</div>
</s:i18n>