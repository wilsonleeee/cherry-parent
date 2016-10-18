<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.jn.BINOLJNMAN15">
<@s.url id="editUrl" action="BINOLCPCOM02_editInit" namespace="/cp"/>
<@s.url id="detailUrl" action="BINOLCPCOM02_detailInit" namespace="/cp"/>
<@s.url id="validUrl" action="BINOLCPCOM02_disable" namespace="/cp"/>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (camtempList?? && camtempList?size>0)>
 <#list camtempList as camTempInfo >
		<ul>
			<li>
				${(camTempInfo.RowNumber)!?html}
			</li>
			<li>
				<#if (camTempInfo.saveStatus)! == "0">
					${(camTempInfo.campaignName)!?html}<span style="color:red;"><@s.text name="cp.draf" /></span>
				<#else>
					<a href="${detailUrl}" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '2', '0', '${camTempInfo_index}');return false;" >${(camTempInfo.campaignName)!?html}</a>
				</#if>
				<span id="ruleParams_${camTempInfo_index}" >
        		<input type="hidden" name="campaignId" value="${(camTempInfo.campaignId)!?c}"/>
        		<input type="hidden" name="workFlowId" value="-1"/>
        		<input type="hidden" name="actionId" value="-1"/></span>
				<div class="hide">
					<a id="validUrl" href="${validUrl}"></a>
				</div>
			</li>
			<li>${(camTempInfo.campaignFromDate)!?html}</li>
			<li>${(camTempInfo.campaigntoDate)!?html}</li>
			<li>${(camTempInfo.descriptionDtl)!?html}</li>
			<li>${application.CodeTable.getVal("1194","${(camTempInfo.vdFlag)!?html}")}</li>
			<li>
	
				<a href="${editUrl}" class="delete" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '2', '0', '${camTempInfo_index}');return false;">
	                <span class="ui-icon icon-edit"></span>
	                <span class="button-text"><@s.text name="global.page.edit" /></span>
                </a>
               
                <#if (camTempInfo.vdFlag)! == "1">
                	<a onclick="javascript:BINOLJNMAN15.ruleValid('0', this);return false;" class="delete" href="javascript:void(0);">
	                   <span class="ui-icon icon-disable"></span>
	                   <span class="button-text"><@s.text name="global.page.disable" /></span>
	                </a>
	               <#else>
	               <a onclick="javascript:BINOLJNMAN15.ruleValid('1', this);return false;" class="add" href="javascript:void(0);">
	                   <span class="ui-icon icon-enable"></span>
	                   <span class="button-text"><@s.text name="global.page.enable" /></span>
	                </a>
	            </#if>

			</li>
		</ul>
	</#list>
</#if></div>
</@s.i18n>