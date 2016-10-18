<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<@s.i18n name="i18n.ct.BINOLCTRPT05">
	<div id="sEcho">${(sEcho)!?html}</div>
	<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
	<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
	<div id="aaData">
		<#if (joinDetailList?? && joinDetailList?size>0)>
			<#list joinDetailList as joinDetailInfo >
				<ul>
					<li>${(joinDetailInfo.RowNumber)!?html}</li>
					<li><span>${(joinDetailInfo.memCode)!?html}</span></li>
					<li><span>${(joinDetailInfo.memberName)!?html}</span></li>
					<li><span>${(joinDetailInfo.mobilephone)!?html}</span></li>
					<li>
						<span>
							<@s.text name="format.number">
								<@s.param value="${(joinDetailInfo.billQuantity)!?html}"></@s.param>
							</@s.text>
						</span>
					</li>
					<li>
						<span>
							<@s.text name="format.price">
								<@s.param value="${(joinDetailInfo.amount)!?html}"></@s.param>
							</@s.text>
						</span>
					</li>
					<li>
						<span>
							<@s.text name="format.number">
								<@s.param value="${(joinDetailInfo.quantity)!?html}"></@s.param>
							</@s.text>
						</span>
					</li>
				</ul>
			</#list>
		</#if>
	</div>
</@s.i18n>