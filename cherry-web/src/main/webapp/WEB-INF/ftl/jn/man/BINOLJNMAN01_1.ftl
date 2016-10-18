<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<@s.url id="addUrl" action="BINOLCPCOM02_topInit" namespace="/cp"/>
<@s.url id="editUrl" action="BINOLCPCOM02_editInit" namespace="/cp"/>
<@s.url id="validUrl" action="BINOLCPCOM02_disable" namespace="/cp"/>
<@s.url id="detailUrl" action="BINOLCPCOM02_detailInit" namespace="/cp"/>
<#if (camtempList?? && camtempList?size > 0) >
 <#list camtempList as camtempInfo>
	<div class="box4">
		<div class="box4-header clearfix"> 
			<strong class="left active">${(camtempInfo.levelName)!?html}</strong> 
				<div id="addComParams_${camtempInfo_index}" class="hide">
					<input type="hidden" name="memberLevelId" value="${(camtempInfo.memberLevelId)!?c}"/>
				</div>
				<div class="hide">
					<a id="validUrl" href="${validUrl}"></a>
				</div>
				<#if (camtempInfo.camTempRule?? && camtempInfo.camTempRule?size > 0) >
				<@cherry.show domId="JNMAN01ADD${(campaignType)!}">
					<#if ((campaignType)! == "2" && camtempInfo_index == (camtempList?size- 1) && "3" == (camtempInfo.foreverFlag)!)>
					<#else>
						<a href="${addUrl}" class="add right" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '1', '0', '${camtempInfo_index}');return false;" ><span class="ui-icon icon-add"></span>
						<span class="button-text"><@s.text name="cp.addNewRule" /></span></a>
					</#if>
				</@cherry.show>
				</div>
				<div class="box4-content clearfix">
           		<div class="overflow_container">
              		<table border="0" cellpadding="0" cellspacing="0" width="100%">
                  		 <tr>
                      		<th width="30%" scope="col"> <@s.text name="cp.ruleName" /></th>
                      		<th width="20%" scope="col"><@s.text name="cp.useStartDate" /></th>
                     		<th width="20%" scope="col"><@s.text name="cp.useEndDate" /></th>
                      		<th scope="col"><@s.text name="cp.actionCol" /></th>
                    	</tr>
                      <#list camtempInfo.camTempRule as camTempRule >
                        <tr>
                        	<td class="hide" id="ruleParams_${camtempInfo_index}_${camTempRule_index}">
                        		<input type="hidden" name="memberLevelId" value="${(camtempInfo.memberLevelId)!?c}"/>
                        		<input type="hidden" name="campaignId" value="${(camTempRule.campaignId)!?c}"/>
                        		<input type="hidden" name="workFlowId" value="${(camTempRule.workFlowId)!?c}"/>
                        		<input type="hidden" name="actionId" value="${(camTempRule.actionId)!?c}"/>
                        	</td>
                      		<td>
                      		<#if "0" == (camTempRule.saveStatus)!>
                      			${(camTempRule.campaignName)!?html}<span class="red"><@s.text name="cp.draf" /></span>
                      		<#else>
	                      		<a href="${detailUrl}" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '2', '0', '${camtempInfo_index}_${camTempRule_index}');return false;" >
	                      		${(camTempRule.campaignName)!?html}</a>
                      		</#if>
                      		</td>
                      		<td>${(camTempRule.campaignFromDate)!?html}</td>
                      		<td>${(camTempRule.campaigntoDate)!?html}</td>
                      		<td class="center">
                      		<#if ((campaignType)! == "2" && camtempInfo_index == (camtempList?size- 1) && "3" == (camtempInfo.foreverFlag)!)>
                      		<#else>
	                      		<@cherry.show domId="JNMAN01COPY${(campaignType)!}">
	                      		<#if (camTempRule.saveStatus)! != "0">
									<a href="${editUrl}" class="search" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '2', '1', '${camtempInfo_index}_${camTempRule_index}');return false;">
									<span class="ui-icon icon-copy"></span><span class="button-text"><@s.text name="cp.copy" /></span></a>
								</#if>
								</@cherry.show>
								<@cherry.show domId="JNMAN01EDIT${(campaignType)!}">
									<a href="${editUrl}" class="delete" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '2', '0', '${camtempInfo_index}_${camTempRule_index}');return false;">
						                <span class="ui-icon icon-edit"></span>
						                <span class="button-text"><@s.text name="cp.edit" /></span>
					                </a>
					            </@cherry.show>
                      		</#if>
                      		<@cherry.show domId="JNMAN01DEL${(campaignType)!}">
				      			<a href="${validUrl}" class="delete" onclick="javascript:CAMPAIGN_TEMPLATE.stopRule('${camtempInfo_index}_${camTempRule_index}');return false;"><span class="ui-icon icon-delete"></span>
                      			<span class="button-text"><@s.text name="cp.disable" /></span></a>
                      		</@cherry.show>
                      		</td>
                    	</tr>
                      </#list>
                     </table>
                  </div>
                  </div>
            <#else>
            	</div>
             	<div class="box4-content clearfix">
             	<#if ((campaignType)! == "2" && camtempInfo_index == (camtempList?size- 1) && "3" == (camtempInfo.foreverFlag)!) >
             		<p><@s.text name="cp.loseRuleTip" /></p>
             	<#else>
               		<p><@s.text name="cp.pointRuleTip" /><a href="${addUrl}" onclick="javascript:CAMPAIGN_TEMPLATE.popupWin(this, '1', '0', '${camtempInfo_index}');return false;" ><@s.text name="cp.here" /></a><@s.text name="cp.startSet" /></p>
             	</#if>
             	</div>
			</#if>
	</div>
  </#list>
</#if>
</@s.i18n> 
