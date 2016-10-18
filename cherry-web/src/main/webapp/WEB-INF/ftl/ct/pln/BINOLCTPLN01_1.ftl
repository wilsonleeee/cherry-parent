<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTPLN01">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (communicationPlanList?? && communicationPlanList?size>0)>
	<#list communicationPlanList as planInfo >
		<@s.url id="viewUrl" action="BINOLCTCOM03_viewInit" namespace="/ct">
			<@s.param name="userId">${(planInfo.userId)!?html}</@s.param>
			<@s.param name="planCode">${(planInfo.planCode)!?html}</@s.param>
			<@s.param name="showType">view</@s.param>
		</@s.url>
		<@s.url id="editUrl" action="BINOLCTCOM01_init" namespace="/ct">
			<@s.param name="userId">${(planInfo.userId)!?html}</@s.param>
			<@s.param name="planCode">${(planInfo.planCode)!?html}</@s.param>
			<@s.param name="campaignCode">${(planInfo.campaignCode)!?html}</@s.param>
		</@s.url>
		<@s.url id="copyUrl" action="BINOLCTCOM01_init" namespace="/ct">
			<@s.param name="planCode">${(planInfo.planCode)!?html}</@s.param>
			<@s.param name="campaignCode">${(planInfo.campaignCode)!?html}</@s.param>
			<@s.param name="editType">1</@s.param>
		</@s.url>
		<ul>
			<li>${(planInfo.RowNumber)!?html}</li>
			<li>${(planInfo.planCode)!?html}</li>
			<li>${(planInfo.planName)!?html}</li>
			<li>${(planInfo.campaignCode)!?html}</li>
			<li>${(planInfo.campaignName)!?html}</li>
			<li>${(planInfo.channelName)!?html}</li>
			<li>${(planInfo.counterCode)!?html}</li>
			<li>${(planInfo.counterName)!?html}</li>
			<li>${application.CodeTable.getVal("1202","${(planInfo.status)!?html}")}</li>
			<li>${(planInfo.lastRunTime)!?html}</li>
			<li>${(planInfo.createTime)!?html}</li>
			<li>${(planInfo.memo1)!?html}</li>
			<li>
				<#if ((planInfo.validFlag)! == "1") >
					<span class='ui-icon icon-valid'></span>
				<#else>
					<span class='ui-icon icon-invalid'></span>
				</#if>
				${application.CodeTable.getVal("1196","${(planInfo.validFlag)!?html}")}
			</li>
			<li>
				<@cherry.show domId="BINOLCTPLN01VIE">
				<a href="${viewUrl}" class="edit" onclick="javascript:openWin(this);return false;">
					<span class="ui-icon ui-icon-document"></span>
					<span class="button-text">
						<@s.text name="ctpln.view" />
					</span>
				</a>
				</@cherry.show>
				<#if ((planInfo.validFlag)! == "1") >
					<@cherry.show domId="BINOLCTPLN01EDI">
					<a href="${editUrl}" class="edit" onclick="javascript:openWin(this);return false;">
		         		<span class="ui-icon icon-edit"></span>
		         		<span class="button-text">
							<@s.text name="ctpln.edit" />
		          		</span>
	             	</a>
	        		</@cherry.show>
	        		<@cherry.show domId="BINOLCTPLN01STO">
					<a class="delete" onclick="BINOLCTPLN01.stopCommPlanDialog('${(planInfo.planCode)!?html}');">
		         		<span class="ui-icon icon-delete"></span>
		         		<span class="button-text">
							<@s.text name="ctpln.stop" />
		          		</span>
	             	</a>
	        		</@cherry.show>
        		</#if>
        		<#if ((planInfo.campaignCode)! == "")>
					<@cherry.show domId="BINOLCTPLN01COP">
					<a href="${copyUrl}" class="edit" onclick="javascript:openWin(this);return false;">
						<span class="ui-icon icon-copy"></span>
						<span class="button-text">
							<@s.text name="ctpln.copy" />
						</span>
					</a>
					</@cherry.show>
				</#if>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>