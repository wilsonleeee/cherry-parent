<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<@s.i18n name="i18n.ct.BINOLCTRPT05">
	<div id="sEcho">${(sEcho)!?html}</div>
	<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
	<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
	<div id="aaData">
		<#if (saleDetailList?? && saleDetailList?size>0)>
			<#list saleDetailList as saleDetailInfo >
				<ul>
					<li>${(saleDetailInfo.RowNumber)!?html}</li>
					<li><span>${(saleDetailInfo.memCode)!?html}</span></li>
					<li><span>${(saleDetailInfo.memberName)!?html}</span></li>
					<li><span>${(saleDetailInfo.mobilephone)!?html}</span></li>
					<li><span>${(saleDetailInfo.billCode)!?html}</span></li>
					<li>
						<span>
							<@s.text name="format.price">
								<@s.param value="${(saleDetailInfo.amount)!?html}"></@s.param>
							</@s.text>
						</span>
					</li>
					<li>
						<span>
							<@s.text name="format.number">
								<@s.param value="${(saleDetailInfo.quantity)!?html}"></@s.param>
							</@s.text>
						</span>
					</li>
					<li><span>${(saleDetailInfo.saleDate)!?html}</span></li>	
				</ul>
			</#list>
		</#if>
	</div>
</@s.i18n>