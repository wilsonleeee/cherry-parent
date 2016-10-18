<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTTPL04">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (illegalCharList?? && illegalCharList?size>0)>
	<#list illegalCharList as illegalCharInfo >
		<@s.url id="disable_url" action="BINOLCTTPL04_update">
			<@s.param name="charId">${(illegalCharInfo.charId)!?html}</@s.param>
			<#if illegalCharInfo.validFlag?exists && illegalCharInfo.validFlag == '1'>
				<@s.param name="validFlag">0</@s.param>
			<#else>
				<@s.param name="validFlag">1</@s.param>
			</#if>
		</@s.url>
		<@s.url id="edit_url" action="BINOLCTTPL04_editInit">
			<@s.param name="charId">${(illegalCharInfo.charId)!?html}</@s.param>
		</@s.url>
		<ul>
			<li>${(illegalCharInfo.RowNumber)!?html}</li>
			<li>${(illegalCharInfo.charValue)!?html}</li>
			<li>
				${application.CodeTable.getVal("1203","${(illegalCharInfo.commType)!}")}
			</li>
			<li>
				<a class="description" style="color:#000" title="<@s.text name="cttpl.remark" /> | ${(illegalCharInfo.remark)!?html}">
					${(illegalCharInfo.cutRemark)!?html}
				</a>
			</li>
			<li>
				<#if ((illegalCharInfo.validFlag)! == "1") >
					<span class='ui-icon icon-valid'></span>
				<#else>
					<span class='ui-icon icon-invalid'></span>
				</#if>
			</li>
			<li>
				<@cherry.show domId="BINOLCTTPL04EDI">
					<a href="${edit_url}" class="edit" onclick="javascript:BINOLCTTPL04.initEditDialog(this);return false;">
						<span class="ui-icon icon-edit"></span>
						<span class="button-text"><@s.text name="global.page.edit" /></span>
					</a> 
				</@cherry.show>
				<#if ((illegalCharInfo.validFlag)! == "1") >
					<@cherry.show domId="BINOLCTTPL04DIS">
						<a href="${disable_url}" class="edit" onclick="javascript:BINOLCTTPL04.initDisable(this,'${(illegalCharInfo.validFlag)!?html}');return false;">
							<span class="ui-icon icon-disable"></span>
							<span class="button-text"><@s.text name="global.page.disable" /></span>
						</a>
					</@cherry.show>
				<#else>
					<@cherry.show domId="BINOLCTTPL04ENA">
						<a href="${disable_url}" class="edit" onclick="javascript:BINOLCTTPL04.initDisable(this,'${(illegalCharInfo.validFlag)!?html}');return false;">
							<span class="ui-icon icon-enable"></span>
							<span class="button-text"><@s.text name="global.page.enable" /></span>
						</a>
					</@cherry.show>
				</#if>
			</li>
		</ul> 
	</#list>
</#if>
</div>
</@s.i18n>