<@s.i18n name="i18n.ct.BINOLCTTPL04">
<form id="editForm" method="post">
	<table class="detail">
		<tr>
			<th><@s.text name="cttpl.brandName" /></th>
			<td>
				<#if (brandList?? && brandList?size > 0) >
					<@s.select name="brandInfoId" list="brandList" listKey="brandInfoId" listValue="brandName"  />
				<#else>
					<input type="hidden" name="brandInfoId" value="${(brandInfoId)!}"/>
					${(brandName)!?html}
				</#if>
				<input class="hide" name="charId" value="${(illegalCharInfo.charId)!}"/>
			</td>
		</tr>
		<tr>
			<th><@s.text name="cttpl.charValue" /></th>
			<td><span><@s.textfield cssClass="text" name="charValue" id="charValue" value="${(illegalCharInfo.charValue)!}"  required="true" maxlength="50" /></span></td>
		</tr>
		<tr>
			<th><@s.text name="cttpl.commType" /></th>
			<td><@s.select name="commType" list='#application.CodeTable.getCodes("1203")' listKey="CodeKey" listValue="Value" value="${(illegalCharInfo.commType)!}" /></td>
		</tr>
		<tr>
			<th><@s.text name="cttpl.remark" /></th>
			<td><span><@s.textarea name="remark" id="remark" cssClass="text"  value="${(illegalCharInfo.remark)!}"></@s.textarea></span></td>
		</tr>
	</table>
</form>
</@s.i18n>