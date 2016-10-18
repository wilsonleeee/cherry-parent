<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<@s.i18n name="i18n.mb.BINOLMBCCT02">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (issueList?? && issueList?size>0)>
	<#list issueList as issueInfo >
		<ul>
			<li>${(issueInfo.rowNumber)!?html}</li>
			<li>${(issueInfo.issueNo)!?html}</li>
			<li>${(issueInfo.callTime)!?html}</li>
			<li>${(issueInfo.phoneNum)!?html}</li>
			<li>${(issueInfo.issueSummary)!?html}</li>
			<li>${application.CodeTable.getVal("1117","${(issueInfo.resolution)!?html}")}</li>
			<li>
				<button onclick="BINOLMBCCT02.showDetail('${(issueInfo.issueId)!}');return false;" class="search" type="button">
					<span class="ui-icon icon-search"></span>
					<span class="button-text"><@s.text name="mbcct.showDetail" /></span>
				</button>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>