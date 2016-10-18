<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<@s.url id="editUrl" action="BINOLCPCOM02_editInit" namespace="/cp"/>
<@s.url id="detailUrl" action="BINOLCPCOM02_detailInit" namespace="/cp"/>
<@s.url id="validUrl" action="BINOLCPCOM02_disable" namespace="/cp"/>
<@s.url id="detailComb" action="BINOLJNMAN06_detailComb" namespace="/jn"/>
<@s.url id="editComb" action="BINOLJNMAN06_editComb" namespace="/jn"/>
<@s.url id="relatView" action="BINOLJNMAN06_relatView" namespace="/jn"/>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (camtempList?? && camtempList?size>0)>
 <#list camtempList as camTempInfo >
		<ul>
			<li>
				<#if (camTempInfo.saveStatus)! == "0">
					${(camTempInfo.campaignName)!?html}<span style="color:red;"><@s.text name="cp.draf" /></span>
				<#else>
					<#if (camTempInfo.pointRuleType)! != "3">
						<a href="${detailUrl}" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '2', '0', '${camTempInfo_index}');return false;" >${(camTempInfo.campaignName)!?html}</a>
					<#else>
						<a href="${detailComb}" onclick="javascript:BINOLJNMAN04.openComb(this, '${(camTempInfo.campaignId)!?c}');return false;" >${(camTempInfo.campaignName)!?html}</a>
					</#if>
				</#if>
				<span id="ruleParams_${camTempInfo_index}" >
        		<input type="hidden" name="campaignId" value="${(camTempInfo.campaignId)!?c}"/>
        		<input type="hidden" name="workFlowId" value="${(camTempInfo.workFlowId)!?c}"/>
        		<input type="hidden" name="actionId" value="${(camTempInfo.actionId)!?c}"/></span>
				<div class="hide">
					<a id="validUrl" href="${validUrl}"></a>
				</div>
			</li>
			<li>${application.CodeTable.getVal("1193","${(camTempInfo.pointRuleType)!?html}")}</li>
			<li>${(camTempInfo.campaignFromDate)!?html}</li>
			<li>${(camTempInfo.campaigntoDate)!?html}</li>
			<li>
			<#if (camTempInfo.pointRuleType)! != "3">
			<@cherry.show domId="JNMAN04COPY">
				<#if (camTempInfo.saveStatus)! != "0" && (camTempInfo.defaultFlag)! != 1>
				<a href="${editUrl}" class="search" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '2', '1', '${camTempInfo_index}');return false;">
				<span class="ui-icon icon-copy"></span><span class="button-text"><@s.text name="cp.copy" /></span></a>
				</#if>
			</@cherry.show>
			</#if>
			<@cherry.show domId="JNMAN04EDIT">
				<#if (camTempInfo.pointRuleType)! != "3">
						<a href="${editUrl}" class="delete" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '2', '0', '${camTempInfo_index}');return false;">
	                <span class="ui-icon icon-edit"></span>
	                <span class="button-text"><@s.text name="cp.edit" /></span>
                	</a>
				<#else>
				<a href="${editComb}" class="delete" onclick="javascript:BINOLJNMAN04.openComb(this, '${(camTempInfo.campaignId)!?c}');return false;">
	                <span class="ui-icon icon-edit"></span>
	                <span class="button-text"><@s.text name="cp.edit" /></span>
                	</a>
				</#if>
				
            </@cherry.show>
            <@cherry.show domId="JNMAN04RELA">
            <#if (camTempInfo.pointRuleType)! != "3" && (camTempInfo.defaultFlag)! != 1>
            <a href="${relatView}" class="delete" onclick="javascript:BINOLJNMAN04.openComb(this, '${(camTempInfo.campaignId)!?c}');return false;">
	                <span class="ui-icon icon-book-open"></span>
	                <span class="button-text"><@s.text name="cp.relat" /></span>
             </a>
             </#if>
             </@cherry.show>
			<@cherry.show domId="JNMAN04DEL">
			<#if (camTempInfo.saveStatus)! == "0" || (camTempInfo.defaultFlag)! != 1>
      			<a href="${validUrl}" class="delete" onclick="javascript:CAMPAIGN_TEMPLATE.stopRule('${camTempInfo_index}');return false;"><span class="ui-icon icon-delete"></span>
      			<span class="button-text"><@s.text name="cp.disable" /></span></a>
      		</#if>
      		</@cherry.show>
			</li>
		</ul>
	</#list>
</#if></div>
</@s.i18n>