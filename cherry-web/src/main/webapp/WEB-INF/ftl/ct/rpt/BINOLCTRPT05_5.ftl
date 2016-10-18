<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<@s.i18n name="i18n.ct.BINOLCTRPT05">
	<div id="sEcho">${(sEcho)!?html}</div>
	<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
	<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
	<div id="aaData">
		<#if (sendDetailList?? && sendDetailList?size>0)>
			<#list sendDetailList as sendDetailInfo >
				<ul>
					<li>${(sendDetailInfo.RowNumber)!?html}</li>
					<li><span>${(sendDetailInfo.memCode)!?html}</span></li>
					<li><span>${(sendDetailInfo.memName)!?html}</span></li>
					<li><span>${(sendDetailInfo.mobilephone)!?html}</span></li>
					<li><@s.text name="ctrpt.message" id="message"/>
						<a title="${message}|${(sendDetailInfo.message)!?html}" class="description" style="color:black">${(sendDetailInfo.messageCut)!?html}</a>
					</li>
					<li><span>${(sendDetailInfo.sendTime)!?html}</span></li>
					<li><span>${(sendDetailInfo.couponCode)!?html}</span></li>
				</ul>
			</#list>
		</#if>
	</div>
</@s.i18n>