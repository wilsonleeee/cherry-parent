<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTRPT06">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="memCommunResultInfo">
	<p>
		<strong><@s.text name="ctrpt06.ROI" />：</strong><span class="green"><strong><@s.text name="format.price"><@s.param value="${(memCommunResultInfo.ROI)!?html}"></@s.param></@s.text></strong></span>
		<strong><@s.text name="ctrpt06.resTotalRate" />：</strong><span class="green"><strong><@s.text name="format.price"><@s.param value="${(memCommunResultInfo.resTotalRate)!?html}"></@s.param></@s.text><@s.text name="global.page.percent" /></strong></span>
		<strong><@s.text name="ctrpt06.resDealRate" />：</strong><span class="green"><strong><@s.text name="format.price"><@s.param value="${(memCommunResultInfo.resDealRate)!?html}"></@s.param></@s.text><@s.text name="global.page.percent" /></strong></span>
		<strong><@s.text name="ctrpt06.resOrderRate" />：</strong><span class="green"><strong><@s.text name="format.price"><@s.param value="${(memCommunResultInfo.resOrderRate)!?html}"></@s.param></@s.text><@s.text name="global.page.percent" /></strong></span>
	</p>
	<p>
		<strong><@s.text name="ctrpt06.sendMsgCount" />：</strong><span class="green"><strong><@s.text name="format.number"><@s.param value="${(memCommunResultInfo.sendMsgCount)!?html}"></@s.param></@s.text></strong></span>
		<strong><@s.text name="ctrpt06.coverMemCount" />：</strong><span class="green"><strong><@s.text name="format.number"><@s.param value="${(memCommunResultInfo.coverMemCount)!?html}"></@s.param></@s.text></strong></span>
		<strong><@s.text name="ctrpt06.orderMemCount" />：</strong><span class="green"><strong><@s.text name="format.number"><@s.param value="${(memCommunResultInfo.orderMemCount)!?html}"></@s.param></@s.text></strong></span>
		<strong><@s.text name="ctrpt06.dealMemCount" />：</strong><span class="green"><strong><@s.text name="format.number"><@s.param value="${(memCommunResultInfo.dealMemCount)!?html}"></@s.param></@s.text></strong></span>
	</p>
</div>
<div id="aaData">
<#if (memCommunResultDetailList?? && memCommunResultDetailList?size>0)>
	<#list memCommunResultDetailList as memCommunResultDetailMap >
		<ul>
			<li>${(memCommunResultDetailMap.RowNumber)!?html}</li>
			<li>${(memCommunResultDetailMap.memberCode)!?html}</li>
			<li>${(memCommunResultDetailMap.memberName)!?html}</li>
			<li>${(memCommunResultDetailMap.orderQuantity)!?html}</li>
			<li>${(memCommunResultDetailMap.orderAmount)!?html}</li>
			<li>${(memCommunResultDetailMap.dealQuantity)!?html}</li>
			<li>${(memCommunResultDetailMap.dealAmount)!?html}</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>