<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTRPT02">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="totalInfo" >
	<@s.text name="ctrpt.sendMsgNum" /><@s.text name="global.page.total" />：<span class="green"><strong><@s.text name="format.number"><@s.param value="${(commRunTotalInfo.totalSendNum)!?html}"></@s.param></@s.text></strong></span>
	<@s.text name="ctrpt.sendMsgErrorNum" /><@s.text name="global.page.total" />：<span class="green"><strong><@s.text name="format.number"><@s.param value="${(commRunTotalInfo.totalSendErrorNum)!?html}"></@s.param></@s.text></strong></span>
</div>
<div id="aaData">
<#if (commRunDetailList?? && commRunDetailList?size>0)>
	<#list commRunDetailList as commRunInfo >
		<@s.url id="viewUrl" action="BINOLCTRPT03_init" namespace="/ct">
			<@s.param name="brandInfoId">${(commRunInfo.brandInfoId)!?html}</@s.param>
			<@s.param name="organizationInfoId">${(commRunInfo.organizationInfoId)!?html}</@s.param>
			<@s.param name="batchId">${(commRunInfo.batchId)!?html}</@s.param>
			<@s.param name="communicationCode">${(commRunInfo.communicationCode)!?html}</@s.param>
			<@s.param name="showType">detail</@s.param>
		</@s.url>
		<@s.url id="viewErrorUrl" action="BINOLCTRPT04_init" namespace="/ct">
			<@s.param name="brandInfoId">${(commRunInfo.brandInfoId)!?html}</@s.param>
			<@s.param name="organizationInfoId">${(commRunInfo.organizationInfoId)!?html}</@s.param>
			<@s.param name="batchId">${(commRunInfo.batchId)!?html}</@s.param>
			<@s.param name="communicationCode">${(commRunInfo.communicationCode)!?html}</@s.param>
			<@s.param name="showType">detail</@s.param>
		</@s.url>
		<ul>
			<li>${(commRunInfo.RowNumber)!?html}</li>
			<li>${(commRunInfo.batchId)!?html}</li>
			<li>${application.CodeTable.getVal("1219","${(commRunInfo.eventType)!?html}")}</li>
			<li>${application.CodeTable.getVal("1203","${(commRunInfo.commType)!?html}")}</li>
			<li>${application.CodeTable.getVal("1262","${(commRunInfo.runType)!?html}")}</li>
			<li>${(commRunInfo.runBeginTime)!?html}</li>
			<li>${(commRunInfo.sendMsgNum)!?html}</li>
			<li>${(commRunInfo.sendErrorNum)!?html}</li>
			<li>
				<#if ((commRunInfo.runStatus)! == 0) >
					<span class='ui-icon icon-valid'></span>
				<#else>
					<span class='ui-icon icon-invalid'></span>
				</#if>
			</li>
			<li>${(commRunInfo.runError)!?html}</li>
			<li>
				<@cherry.show domId="BINOLCTRPT02VIE">
				<a href="${viewUrl}" class="edit" onclick="javascript:openWin(this);return false;">
					<span class="ui-icon icon-copy"></span>
					<span class="button-text">
						<@s.text name="ctrpt.view" />
					</span>
				</a>
				</@cherry.show>
				<@cherry.show domId="BINOLCTRPT02ERR">
				<a href="${viewErrorUrl}" class="edit" onclick="javascript:openWin(this);return false;">
					<span class="ui-icon icon-copy"></span>
					<span class="button-text">
						<@s.text name="ctrpt.viewError" />
					</span>
				</a>
				</@cherry.show>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>