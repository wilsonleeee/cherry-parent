<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<@s.url id="editUrl" action="BINOLMBCLB02_edit"/>
<@s.url id="detailUrl" action="BINOLMBCLB02_detail"/>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (clubList?? && clubList?size>0)>
 <#list clubList as clubInfo >
		<ul>
			<li>
				${(clubInfo.RowNumber)!?html}
			</li>
			<li><a href="${detailUrl}" onclick="javascript:BINOLMBCLB01.openWinConfig(this, '${(clubInfo.memberClubId)!?c}');return false;">${(clubInfo.clubName)!?html}</a></li>
			<li>${(clubInfo.clubCode)!?html}</li>
			<li>${(clubInfo.descriptionDtl)!?html}</li>
			<li>
			<#if (clubInfo.validFlag)! == "1"><span class='ui-icon icon-valid'></span>
				<#elseif (clubInfo.validFlag)! == "0"><span class='ui-icon icon-invalid'></span>
				<#else><span></span></#if>
			</li>
			<li>
                <#if (clubInfo.validFlag)! == "1">
                	<a href="${editUrl}" class="delete" onclick="javascript:BINOLMBCLB01.openWinConfig(this, '${(clubInfo.memberClubId)!?c}');return false;">
	                <span class="ui-icon icon-edit"></span>
	                <span class="button-text"><@s.text name="cp.edit" /></span>
                	</a>
                	<a onclick="javascript:BINOLMBCLB01.clubValid(this, '${(clubInfo.memberClubId)!?c}');return false;" class="delete" href="javascript:void(0);">
	                   <span class="ui-icon icon-disable"></span>
	                   <span class="button-text"><@s.text name="cp.disable" /></span>
	                </a>
	            </#if>
			</li>
		</ul>
	</#list>
</#if></div>
</@s.i18n>