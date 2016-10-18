<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<@s.url id="editUrl" action="BINOLJNMAN06_editInit"/>
<@s.url id="detailUrl" action="BINOLJNMAN06_detailInit"/>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (campGrpList?? && campGrpList?size>0)>
 <#list campGrpList as campGrpInfo >
		<ul>
			<li>
				${(campGrpInfo.RowNumber)!?html}
			</li>
			<li><a href="${detailUrl}" onclick="javascript:BINOLJNMAN05.openWinConfig(this, '${(campGrpInfo.campaignGrpId)!?c}');return false;">${(campGrpInfo.groupName)!?html}</a></li>
			<li>${(campGrpInfo.descriptionDtl)!?html}</li>
			<li>${application.CodeTable.getVal("1194","${(campGrpInfo.validFlag)!?html}")}</li>
			<li>
				<@cherry.show domId="JNMAN05EDIT">
				<a href="${editUrl}" class="delete" onclick="javascript:BINOLJNMAN05.openWinConfig(this, '${(campGrpInfo.campaignGrpId)!?c}');return false;">
	                <span class="ui-icon icon-edit"></span>
	                <span class="button-text"><@s.text name="cp.edit" /></span>
                </a>
                </@cherry.show>
                <@cherry.show domId="JNMAN05START">
                <#if (campGrpInfo.validFlag)! == "1">
                	<a onclick="javascript:BINOLJNMAN05.configValid('0', '${(campGrpInfo.campaignGrpId)!?c}');return false;" class="delete" href="javascript:void(0);">
	                   <span class="ui-icon icon-disable"></span>
	                   <span class="button-text"><@s.text name="cp.disable" /></span>
	                </a>
	               <#else>
	               <a onclick="javascript:BINOLJNMAN05.configValid('1', '${(campGrpInfo.campaignGrpId)!?c}');return false;" class="add" href="javascript:void(0);">
	                   <span class="ui-icon icon-enable"></span>
	                   <span class="button-text"><@s.text name="cp.configStart" /></span>
	                </a>
	            </#if>
	             </@cherry.show>
			</li>
		</ul>
	</#list>
</#if></div>
</@s.i18n>