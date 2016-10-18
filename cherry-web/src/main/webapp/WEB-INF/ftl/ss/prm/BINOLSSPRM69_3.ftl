<@s.i18n name="i18n.ss.BINOLSSPRM69">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
	<#if optionList?exists && optionList?size gt 0>
		<#list optionList as option >
			<ul>
				<li>
					<input type="radio" name="code" value="${option.code! }" <#if params.relationVal?exists && params.relationVal == option.code>checked="true"</#if> />
					<input type="hidden" name="name" value="${option.name! }" />
				</li>
				<li>${option.code! }</li>
				<li>${option.name! }</li>
			</ul>
		</#list>
	</#if>
</div>
</@s.i18n>