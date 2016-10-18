<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTRPT04">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (msgDetailList?? && msgDetailList?size>0)>
	<#list msgDetailList as msgDetailInfo >
		<ul>
			<li>${(msgDetailInfo.RowNumber)!?html}</li>
			<li>${(msgDetailInfo.memCode)!?html}</li>
			<li>${(msgDetailInfo.memName)!?html}</li>
			<li>${(msgDetailInfo.mobilephone)!?html}</li>
			<li>${application.CodeTable.getVal("1203","${(msgDetailInfo.commType)!?html}")}</li>
			<li><@s.text name="ctrpt.message" id="message"/>
				<a title="${message}|${(msgDetailInfo.message)!?html}" class="description" style="color:black">${(msgDetailInfo.messageCut)!?html}</a>
			</li>
			<li>${(msgDetailInfo.sendTime)!?html}</li>
			<li>${application.CodeTable.getVal("1273","${(msgDetailInfo.errorType)!?html}")}</li>
			<li>
				<@s.text name="ctrpt.errorMsg" id="errorMsg"/>
				<a title="${errorMsg}|${(msgDetailInfo.errorText)!?html}" class="description" style="color:black">${(msgDetailInfo.errorTextCut)!?html}</a>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>