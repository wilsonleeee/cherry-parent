<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTRPT06">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (memCommunStatisticsList?? && memCommunStatisticsList?size>0)>
	<#list memCommunStatisticsList as memCommunStatisticsMap >
		<ul>
			<li>${(memCommunStatisticsMap.RowNumber)!?html}</li>
			<li>${(memCommunStatisticsMap.channelName)!?html}</li>
			<li>${(memCommunStatisticsMap.counterCode)!?html}</li>
			<li>${(memCommunStatisticsMap.counterName)!?html}</li>
			<li>
				<@s.url id="communDetailInitUrl" action="BINOLCTRPT06_detailInit" namespace="/ct">
					<@s.param name="brandInfoId">${(memCommunStatisticsMap.brandInfoId)!?html}</@s.param>
					<@s.param name="communicationCode">${(memCommunStatisticsMap.communicationCode)!?html}</@s.param>
				</@s.url>
				<a href="${communDetailInitUrl}&communicationName=${(memCommunStatisticsMap.communicationName)!?html}" class="popup" onclick="javascript:openWin(this,{height:600, width:900});return false;">
					<span>
						<#if memCommunStatisticsMap.communicationName?exists && memCommunStatisticsMap.communicationName != ''>
							（${(memCommunStatisticsMap.communicationCode)!?html}）${(memCommunStatisticsMap.communicationName)!?html}
						<#else>
							${(memCommunStatisticsMap.communicationCode)!?html}
						</#if>
					</span>
				</a>
			</li>
			<li>${(memCommunStatisticsMap.sendMemCount)!?html}</li>
			<li>${(memCommunStatisticsMap.sendTime)!?html}</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>