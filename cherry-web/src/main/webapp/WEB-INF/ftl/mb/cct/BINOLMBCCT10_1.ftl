<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<@s.i18n name="i18n.mb.BINOLMBCCT10">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (issueList?? && issueList?size>0)>
	<#list issueList as issueInfo >
		<ul>
			<li>${(issueInfo.callId)!?html}</li>
			<li>${(issueInfo.customerCode)!?html}</li>
			<li>${(issueInfo.customerName)!?html}</li>
			<li>${(issueInfo.customerNumber)!?html}</li>
			<li>
			<a title='<@s.text name="mbcct.issueSummary" />|${(issueInfo.issueSummary)!?html}' class="description">
			<#if (issueInfo.issueSummary)!?length gt 20>
				${(issueInfo.issueSummary)!?substring(0,20)}
			<#else>
				${(issueInfo.issueSummary)!?html}
			</#if>
			</a>
			</li>
			<li>${application.CodeTable.getVal("1272","${(issueInfo.issueType)!?html}")}</li>
			<li>
			<#if (issueInfo.issueSubType)!?length gt 0>
				${application.CodeTable.getVal("1329","${(issueInfo.issueSubType)!?html}")}
			</#if>
			</li>
			<li>${application.CodeTable.getVal("1117","${(issueInfo.resolution)!?html}")}</li>
			<li>${(issueInfo.callTime)!?html}</li>
			<li>${(issueInfo.cNo)!?html}</li>
			<li>${(issueInfo.createTime)!?html}</li>
			<li>${(issueInfo.resolutionDate)!?html}</li>
			<li>
				<button onclick="BINOLMBCCT10.showDetail('${(issueInfo.issueId)!}');return false;" class="search" type="button">
					<span class="ui-icon icon-search"></span>
					<span class="button-text"><@s.text name="mbcct.showDetail" /></span>
				</button>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>
