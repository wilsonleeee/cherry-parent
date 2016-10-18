<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<@s.i18n name="i18n.mb.BINOLMBCCT09">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (callLogList?? && callLogList?size>0)>
	<#list callLogList as callLogInfo >
		<ul>
			<li>${(callLogInfo.callId)!?html}</li>
			<li>${(callLogInfo.cNo)!?html}</li>
			<li>${(callLogInfo.customerNumber)!?html}</li>
			<li>${(callLogInfo.customerCode)!?html}</li>
			<li>${(callLogInfo.customerName)!?html}</li>
			<li>
				<#if ((callLogInfo.isMember)! == 1) >
					<@s.text name="mbcct.memberCall" />
				<#else>
					<@s.text name="mbcct.notMemberCall" />
				</#if>
			</li>
			<li>${application.CodeTable.getVal("1288","${(callLogInfo.callType)!?html}")}</li>
			<li>${(callLogInfo.callTime)!?html}</li>
			<li>
				<#if (callLogInfo.issueId??) >
				<button onclick="BINOLMBCCT09.showIssue('${(callLogInfo.issueId)!}');return false;" class="search" type="button">
					<span class="ui-icon icon-search"></span>
					<span class="button-text"><@s.text name="mbcct.showIssue" /></span>
				</button>
				<#else>
				<button class="btn_disable search" disabled="disabled" type="button">
					<span class="ui-icon icon-search"></span>
					<span class="button-text"><@s.text name="mbcct.showIssue" /></span>
				</button>
				</#if>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>
