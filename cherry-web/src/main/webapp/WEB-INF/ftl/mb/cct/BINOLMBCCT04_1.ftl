<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<@s.i18n name="i18n.mb.BINOLMBCCT04">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (memberList?? && memberList?size>0)>
	<#list memberList as memberInfo >
		<ul>
			<li>${(memberInfo.memCode)!?html}</li>
			<li>${(memberInfo.memName)!?html}</li>
			<li>${application.CodeTable.getVal("1006","${(memberInfo.gender)!?html}")}</li>
			<li>${(memberInfo.mobilePhone)!?html}</li>
			<li>${(memberInfo.counterName)!?html}</li>
			<li>${(memberInfo.birthDay)!?html}</li>
			<li>${(memberInfo.joinDate)!?html}</li>
			<li>
				<#if ((memberInfo.cardValidFlag)! == 1) >
					<span class='ui-icon icon-valid'></span>
				<#else>
					<span class='ui-icon icon-invalid'></span>
				</#if>
				${application.CodeTable.getVal("1137","${(memberInfo.cardValidFlag)!?html}")}
			</li>
			<li>
				<button onclick="BINOLMBCCT04.showDetail('${(memberInfo.memId)!}');return false;" class="search" type="button">
					<span class="ui-icon icon-search"></span>
					<span class="button-text"><@s.text name="mbcct.checkMember" /></span>
				</button>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>