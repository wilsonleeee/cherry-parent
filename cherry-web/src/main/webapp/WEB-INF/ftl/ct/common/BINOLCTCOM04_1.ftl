<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTTPL02">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (templateList?? && templateList?size>0)>
	<#list templateList as templateInfo >
		<ul class="TEMPLATEVALUE">
			<li>${(templateInfo.RowNumber)!?html}</li>
			<li>${(templateInfo.templateCode)!?html}</li>
			<li>${(templateInfo.templateName)!?html}</li>
			<li>${application.CodeTable.getVal("1197","${(templateInfo.templateUse)!?html}")}</li>
			<li>
				<@s.text name="cttpl.contents" id="contents"/>
				<a title="${contents}|${(templateInfo.contents)!?html}" class="description">${(templateInfo.contentsCut)!?html}</a>
			</li>
			<li>${application.CodeTable.getVal("1198","${(templateInfo.customerType)!?html}")}</li>
			<li>
				<#if ((templateInfo.status)! == "1") >
					<span class='ui-icon icon-valid'></span>
				<#else>
					<span class='ui-icon icon-invalid'></span>
				</#if>
				${application.CodeTable.getVal("1137","${(templateInfo.status)!?html}")}
			</li>
			<li>
				<a class="edit" onclick="BINOLCTCOM04.chooseMsgTemplate(this);return false;">
					<span class="ui-icon icon-search"></span>
					<span class="button-text">
						<@s.text name="cttpl.choose" />
					</span>
					<input id="templateCode" value="${(templateInfo.templateCode)!?html}" class="hide" type="text" />
					<input id="contents" value="${(templateInfo.contents)!?html}" class="hide" type="text" />
				</a>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>