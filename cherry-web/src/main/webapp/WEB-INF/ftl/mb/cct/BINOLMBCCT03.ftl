<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/mb/common/BINOLMBCOM01.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM10.js"></script>
<@s.i18n name="i18n.mb.BINOLMBMBM10">
<div class="crm_main container clearfix" style="margin-top:0px;">
    <div class="crm_left" id="crmMenuDiv">
    	<form id="memDetailForm">
			<@s.hidden name="memberInfoId"></@s.hidden>
		</form>
        <div class="crm_menu">
            <ul>
				<@s.url action="BINOLMBCCT03_topAction" id="memTopUrl"></@s.url>
				<li><a dir="${memTopUrl }" id="topMenu" class="menuOne"><@s.text name="binolmbmbm10_memTop" /></a></li>
				<li><a class="menuOne"><@s.text name="binolmbmbm10_memInfo" /></a>
                	<ul>
                    	<@s.url action="BINOLMBMBM02_init" id="memberDetailUrl"></@s.url>
                    	<li><a dir="${memberDetailUrl}"><@s.text name="binolmbmbm10_memInfoShow" /></a></li>
                    	<@s.url action="BINOLMBMBM21_init" id="referrerUrl">
							<@s.param name="referrerId" value="${(memberInfoMap.referrerId)!}"></@s.param>
							<@s.param name="memName" value="${(memberInfoMap.name)!}"></@s.param>
                    	</@s.url>
                    	<li><a dir="${referrerUrl}"><@s.text name="binolmbmbm10_referrerInfo" /></a></li>
                    	<@s.url action="BINOLMBMBM22_init" id="memAnswerUrl"></@s.url>
                    	<li><a dir="${memAnswerUrl}"><@s.text name="binolmbmbm10_memAnswerInfo" /></a></li>
                    	<#if (memberInfoMap?? && memberInfoMap.memInfoRegFlg?? && memberInfoMap.memInfoRegFlg==0) >
                    	<@cherry.show domId="BINOLMBMBM10_24">
                    	<@s.url action="BINOLMBMBM06_init" id="memberEditUrl"></@s.url>
                    	<li><a dir="${memberEditUrl}"><@s.text name="binolmbmbm10_memInfoUpd" /></a></li>
                    	</@cherry.show>
                    	</#if>
                    	<@cherry.show domId="BINOLMBMBM10_25">
                    	<@s.url action="BINOLMBMBM14_init" id="memInfoRecordUrl"></@s.url>
                    	<li><a dir="${memInfoRecordUrl}"><@s.text name="binolmbmbm10_memInfoRecord" /></a></li>
                    	</@cherry.show>
                    	<@cherry.show domId="BINOLMBMBM16">
                    	<@s.url action="BINOLMBMBM16_relegationInit" id="relegationUrl"></@s.url>
                    	<li><a dir="${relegationUrl}"><@s.text name="binolmbmbm10_memHistory" /></a></li>
                    	</@cherry.show>
                    	<#if (memberInfoMap?? && memberInfoMap.memInfoRegFlg?? && memberInfoMap.memInfoRegFlg==0 && memberInfoMap.memCode??) >
                    	<@cherry.show domId="BINOLMBMBM10_20">
                    	<@s.url action="BINOLMBMBM03_editinit" id="memProUpdUrl"></@s.url>
                    	<li><a dir="${memProUpdUrl}"><@s.text name="binolmbmbm10_memProUpd" /></a></li>
                    	</@cherry.show>
                    	</#if>
                    	<@cherry.show domId="BINOLMBMBM10_21">
                    	<@s.url action="BINOLMBMBM03_init" id="memProUpdRecordUrl"></@s.url>
                    	<li><a dir="${memProUpdRecordUrl}"><@s.text name="binolmbmbm10_memProUpdRecord" /></a></li>
                    	</@cherry.show>
                    </ul>
                </li>
                <li><a class="menuOne"><@s.text name="binolmbmbm10_memSaleInfo" /></a>
                	<ul>
                    	<@s.url action="BINOLMBMBM05_init" id="SaleDetailInitUrl"></@s.url>
                    	<li><a dir="${SaleDetailInitUrl}"><@s.text name="binolmbmbm10_memSaleInfoShow" /></a></li>
                    </ul>
                </li>
                <li><a class="menuOne"><@s.text name="binolmbmbm10_memPointInfo" /></a>
                	<ul>
                    	<@s.url action="BINOLMBMBM04_init" id="PointDetailInitUrl"></@s.url>
                    	<li><a dir="${PointDetailInitUrl}"><@s.text name="binolmbmbm10_memPointInfoShow" /></a></li>
                    	<#if (memberInfoMap?? && memberInfoMap.memInfoRegFlg?? && memberInfoMap.memInfoRegFlg==0 && memberInfoMap.memCode??) >
                    	<@cherry.show domId="BINOLMBMBM10_22">
                    	<@s.url action="BINOLMBMBM08_init" id="PointUpdateUrl"></@s.url>
                    	<li><a dir="${PointUpdateUrl}"><@s.text name="binolmbmbm10_memPointInfoUpd" /></a></li>
                    	</@cherry.show>
                    	</#if>
                    	<@cherry.show domId="BINOLMBMBM10_23">
                    	<@s.url action="BINOLMBMBM08_initPointHis" id="PointHisUrl"></@s.url>
                    	<li><a dir="${PointHisUrl}"><@s.text name="binolmbmbm10_memPointInfoUpdRecord" /></a></li>
                    	</@cherry.show>
                    </ul>
                </li>
                <li><a class="menuOne"><@s.text name="binolmbmbm10_memCampaignInfo" /></a>
                	<ul>
                    	<#if (memberInfoMap?? && memberInfoMap.memInfoRegFlg?? && memberInfoMap.memInfoRegFlg==0 && memberInfoMap.memCode??) >
                    	<@s.url action="BINOLMBMBM07_init" id="currentCampaignInitUrl"></@s.url>
                    	<li><a dir="${currentCampaignInitUrl}"><@s.text name="binolmbmbm10_memCampaignCurrent" /></a></li>
                    	<@s.url action="BINOLMBMBM07_searchCampaignOrder" id="searchCampaignOrderUrl"></@s.url>
                    	<li><a dir="${searchCampaignOrderUrl}"><@s.text name="binolmbmbm10_memCampaignOrder" /></a></li>
                    	</#if>
                    	<@s.url action="BINOLMBMBM07_campaignHistoryInit" id="campaignHistoryInitUrl"></@s.url>
                    	<li><a dir="${campaignHistoryInitUrl}"><@s.text name="binolmbmbm10_memCampaignHistory" /></a></li>
                    </ul>
                </li>
                <li><a class="menuOne"><@s.text name="binolmbmbm10_communicationInfo" /></a>
                	<ul>
                    	<@s.url action="BINOLMBMBM23_init" id="searchSmsSendDetailInitUrl">
                    		<@s.param name="mobilePhone" value="${(memberInfoMap.mobilePhone)!}"></@s.param>
                    	</@s.url>
                    	<li><a dir="${searchSmsSendDetailInitUrl}"><@s.text name="binolmbmbm10_smsSendDetail" /></a></li>
                    </ul>
                </li>
                <li><a class="menuOne"><@s.text name="binolmbmbm10_memIssue" /></a>
                	<ul>
                    	<@s.url action="BINOLMBMBM26_init" id="searchIssueInitUrl"></@s.url>
                    	<li><a dir="${searchIssueInitUrl}"><@s.text name="binolmbmbm10_memIssue" /></a></li>
                    </ul>
                </li>
                <#if (hasMemWarnInfo?? && hasMemWarnInfo==true) >
                <li><a class="menuOne"><@s.text name="binolmbmbm10_memWarnInfo" /></a>
                	<ul>
                    	<@s.url action="BINOLMBMBM20_init" id="memWarnInfoInitUrl"></@s.url>
                    	<li><a dir="${memWarnInfoInitUrl}"><@s.text name="binolmbmbm10_memWarnInfo" /></a></li>
                    </ul>
                </li>
                </#if>
            </ul>
        </div>
    </div>
	<#-- <div class="crm_middle" id="crmArrowDiv"><a class="crm_l_arrow"></a></div> -->
    <div class="crm_right" id="crmContentDiv">
		<div class="crm_content" id ="memDetailDiv"></div>
    </div>
</div>
</@s.i18n>