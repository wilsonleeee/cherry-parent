<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<@s.i18n name="i18n.mb.BINOLMBCCT08">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (customerList?? && customerList?size>0)>
	<#list customerList as customerInfo >
		<ul>
			<li>${(customerInfo.customerCode)!?html}</li>
			<li>${(customerInfo.customerName)!?html}</li>
			<li>${application.CodeTable.getVal("1006","${(customerInfo.gender)!?html}")}</li>
			<li>${(customerInfo.mobilePhone)!?html}</li>
			<li>${(customerInfo.telephone)!?html}</li>
			<li>${(customerInfo.birthDay)!?html}</li>
			<li>${(customerInfo.joinTime)!?html}</li>
			<li>${application.CodeTable.getVal("1286","${(customerInfo.customerType)!?html}")}</li>
			<li>${(customerInfo.company)!?html}</li>
			<li>${(customerInfo.post)!?html}</li>
			<li>${(customerInfo.industry)!?html}</li>
			<li>${(customerInfo.zip)!?html}</li>
			<li>${(customerInfo.messageId)!?html}</li>
			<li>${(customerInfo.email)!?html}</li>
			<li>${(customerInfo.province)!?html}</li>
			<li>${(customerInfo.city)!?html}</li>
			<li>${(customerInfo.address)!?html}</li>
			<li>
				<@s.text name="mbcct.memo" id="memo"/>
				<a title="${memo}|${(customerInfo.memo)!?html}" class="description" style="color:black">${(customerInfo.memoCut)!?html}</a>
			</li>
			<li>${application.CodeTable.getVal("1231","${(customerInfo.isReceiveMsg)!?html}")}</li>
			<li>
				<button onclick="BINOLMBCCT08.edit('${(customerInfo.customerCode)!}');return false;" class="search" type="button">
					<span class="ui-icon icon-edit"></span>
					<span class="button-text"><@s.text name="mbcct.editCustomer" /></span>
				</button>
				<button onclick="BINOLMBCCT08.showDetail('${(customerInfo.customerCode)!}');return false;" class="search" type="button">
					<span class="ui-icon icon-search"></span>
					<span class="button-text"><@s.text name="mbcct.showDetail" /></span>
				</button>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>
