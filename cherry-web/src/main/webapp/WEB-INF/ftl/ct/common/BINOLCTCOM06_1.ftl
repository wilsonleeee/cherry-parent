<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTPLN01">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (customerList?? && customerList?size>0)>
	<#list customerList as customerInfo >
		<ul class="TEMPLATEVALUE">
			<li>${(customerInfo.memCode)!?html}</li>
			<li>${(customerInfo.memName)!?html}</li>
			<li>
				<#if customerInfo.gender?exists >
					${application.CodeTable.getVal("1006","${(customerInfo.gender)!?html}")}
				</#if>
			</li>
			<li>${(customerInfo.mobilePhone)!?html}</li>
			<li>${(customerInfo.email)!?html}</li>
			<li>${(customerInfo.telephone)!?html}</li>
			<li>${(customerInfo.totalPoint)!?html}</li>
			<li>${(customerInfo.changablePoint)!?html}</li>
			<li>${(customerInfo.counterCode)!?html}</li>
			<li>${(customerInfo.counterName)!?html}</li>
			<li>
				<#if ((customerInfo.receiveMsgFlg)! == "0") >
					<span class='ui-icon icon-invalid'></span>
				<#else>
					<span class='ui-icon icon-valid'></span>
				</#if>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>