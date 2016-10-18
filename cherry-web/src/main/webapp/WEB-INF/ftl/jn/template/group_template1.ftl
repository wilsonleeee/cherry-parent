<@s.i18n name="i18n.cp.BINOLCPCOM01">
<#-- 入会规则策略  -->
<#if camTemp.tempCode == "BASE000032">
	<div id="${camTemp.tempCode}_${index}" class="box4">
	<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
	<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
	<input type="hidden" name="RULE-TEMP-FLAG" value="1"/>
	<div class="box4-header">
		<strong><@s.text name="cp.setPriority" /><@s.text name="cp.setPriorityTip" /></strong>
	</div>
	<div class="box4-content" >
		<div class="toolbar clearfix" style="padding:2px;">
		</div>
		<div class="section" >
		<div class="section-header" >
			<Strong><@s.text name="cp.hasConfigRule" /></Strong>
		</div>
		<div class="section-content" >
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="proTable">
		<tbody id="rulePriorityTable">
            <tr class="thClass">
               <th width="5%"><@s.text name="cp.hanghao" /></th>
               <th width="25%"><@s.text name="cp.ruleName" /></th>
               <th width="20%"><@s.text name="cp.startTime" /></th>
               <th width="20%"><@s.text name="cp.endTime" /></th>
               <th width="20%"><@s.text name="cp.priority" /></th>
            </tr>
			<#if (camTemp.priorityRuleList?? && camTemp.priorityRuleList?size>0) >
			<#list camTemp.priorityRuleList as priorityRule>
					<tr><td>${(priorityRule_index  + 1)!}</td>
					<td>
						<#if "" != (priorityRule.campaignName)!>
							${(priorityRule.campaignName)!}<input type="hidden" name="campaignId" value="${(priorityRule.campaignId)!}"/>
						<#elseif "" != (priorityRule.strategyRuleName)!>
							${(priorityRule.strategyRuleName)!}
							<input type="hidden" name="strategyRuleName" value="${(priorityRule.strategyRuleName)!}"/>
							<input type="hidden" name="strategyRuleId" value="${(priorityRule.strategyRuleId)!}"/>
						</#if>
					</td>
					<td>${(priorityRule.campaignFromDate)!}</td>
					<td>${(priorityRule.campaignToDate)!}</td>
					<td>
					<#if (camTemp.priorityRuleList?size != 1)>
					<#if (priorityRule_index == 0)>
						<span style="height:16px; width:16px; display:block;" class="left"></span>
						<span style="height:16px; width:16px; display:block;" class="left"></span>
						<span class="arrow-down left" onClick="CAMPAIGN_TEMPLATE.move(this,2,'rulePriorityTable');return false;"></span>
						<span class="arrow-last left" onClick="CAMPAIGN_TEMPLATE.move(this,3,'rulePriorityTable');return false;"></span>
					<#elseif (priorityRule_index > 0 && priorityRule_index < (camTemp.priorityRuleList?size- 1))>
						<span class="arrow-first left" onClick="CAMPAIGN_TEMPLATE.move(this,0,'rulePriorityTable');return false;"></span>
						<span class="arrow-up left" onClick="CAMPAIGN_TEMPLATE.move(this,1,'rulePriorityTable');return false;"></span>
						<span class="arrow-down left" onClick="CAMPAIGN_TEMPLATE.move(this,2,'rulePriorityTable');return false;"></span>
						<span class="arrow-last left" onClick="CAMPAIGN_TEMPLATE.move(this,3,'rulePriorityTable');return false;"></span>
					<#elseif (priorityRule_index == (camTemp.priorityRuleList?size- 1)) >
						<span class="arrow-first left" onClick="CAMPAIGN_TEMPLATE.move(this,0,'rulePriorityTable');return false;"></span>
						<span class="arrow-up left" onClick="CAMPAIGN_TEMPLATE.move(this,1,'rulePriorityTable');return false;"></span>
					</#if>
					</#if>
					</td>
					</tr>
			</#list>
			</#if>
		</tbody>
		</table>
		</div>
		</div>
		
		<div class="section" >
		<div class="section-header" >
			<Strong><@s.text name="cp.notConPriority" /></Strong>
		</div>
		<div class="section-content" >
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="proTable">
		<tbody id="rulePriorityDotConTable">
            <tr class="thClass">
               <th width="5%"><@s.text name="cp.hanghao" /></th>
               <th width="25%"><@s.text name="cp.ruleName" /></th>
               <th width="20%"><@s.text name="cp.startTime" /></th>
               <th width="20%"><@s.text name="cp.endTime" /></th>
               <th width="20%"><@s.text name="cp.priority" /></th>
            </tr>
			<#if (camTemp.priorityRuleDotConList?? && camTemp.priorityRuleDotConList?size>0) >
			<#list camTemp.priorityRuleDotConList as priorityRule>
				<tr><td>${(priorityRule_index  + 1)!}</td>
				<td>
					<#if "" != (priorityRule.campaignName)!>
						${(priorityRule.campaignName)!}<input type="hidden" name="campaignId" value="${(priorityRule.campaignId)!}"/>
					<#elseif "" != (priorityRule.strategyRuleName)!>
						${(priorityRule.strategyRuleName)!}
						<input type="hidden" name="strategyRuleName" value="${(priorityRule.strategyRuleName)!}"/>
						<input type="hidden" name="strategyRuleId" value="${(priorityRule.strategyRuleId)!}"/>
					</#if>
				</td>
				<td>${(priorityRule.campaignFromDate)!}</td>
				<td>${(priorityRule.campaignToDate)!}</td>
				<td></td>
				</tr>
			</#list>
			</#if>
		</tbody>
		</table>
		</div>
	</div>
	</div>
	</div>
	
<#elseif camTemp.tempCode == "BASE000052">
	<div id="${camTemp.tempCode}_${index}" class="box4">
	<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
	<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
	<input type="hidden" name="RULE-TEMP-FLAG" value="1"/>
	<div class="box4-header">
		<strong><@s.text name="cp.showpriority" /></strong>
	</div>
	<div class="box4-content" >
		<div class="section" >
		<div class="section-header" >
			<Strong><@s.text name="cp.hasConfigRule" /></Strong>
		</div>
		<div class="section-content" >
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="proTable">
		<thead>
            <tr>
               <th width="5%"><@s.text name="cp.hanghao" /></th>
               <th width="25%"><@s.text name="cp.ruleName" /></th>
               <th width="20%"><@s.text name="cp.startTime" /></th>
               <th width="20%"><@s.text name="cp.endTime" /></th>
            </tr>
        </thead>
		<tbody>
			<#if (camTemp.priorityRuleList?? && camTemp.priorityRuleList?size>0) >
			<#list camTemp.priorityRuleList as priorityRule>
				<tr><td>${(priorityRule_index  + 1)!}</td>
				<td>
					<#if "" != (priorityRule.campaignName)!>
						${(priorityRule.campaignName)!}<input type="hidden" name="campaignId" value="${(priorityRule.campaignId)!}"/>
					<#elseif "" != (priorityRule.strategyRuleName)!>
						${(priorityRule.strategyRuleName)!}
						<input type="hidden" name="strategyRuleName" value="${(priorityRule.strategyRuleName)!}"/>
						<input type="hidden" name="strategyRuleId" value="${(priorityRule.strategyRuleId)!}"/>
					</#if>
				</td>
				<td>${(priorityRule.campaignFromDate)!}</td>
				<td>${(priorityRule.campaignToDate)!}</td>
				</tr>
			</#list>
			</#if>
		</tbody>
		</table>
		</div>
		</div>
		
		<div class="section" >
		<div class="section-header" >
			<Strong><@s.text name="cp.notConPriority" /></Strong>
		</div>
		<div class="section-content" >
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="proTable">
		<tbody>
            <tr class="thClass">
               <th width="5%"><@s.text name="cp.hanghao" /></th>
               <th width="25%"><@s.text name="cp.ruleName" /></th>
               <th width="20%"><@s.text name="cp.startTime" /></th>
               <th width="20%"><@s.text name="cp.endTime" /></th>
            </tr>
			<#if (camTemp.priorityRuleDotConList?? && camTemp.priorityRuleDotConList?size>0) >
			<#list camTemp.priorityRuleDotConList as priorityRule>
				<tr><td>${(priorityRule_index  + 1)!}</td>
				<td>
					<#if "" != (priorityRule.campaignName)!>
						${(priorityRule.campaignName)!}<input type="hidden" name="campaignId" value="${(priorityRule.campaignId)!}"/>
					<#elseif "" != (priorityRule.strategyRuleName)!>
						${(priorityRule.strategyRuleName)!}
						<input type="hidden" name="strategyRuleName" value="${(priorityRule.strategyRuleName)!}"/>
						<input type="hidden" name="strategyRuleId" value="${(priorityRule.strategyRuleId)!}"/>
					</#if>
				</td>
				<td>${(priorityRule.campaignFromDate)!}</td>
				<td>${(priorityRule.campaignToDate)!}</td>
				</tr>
			</#list>
			</#if>
		</tbody>
		</table>
		</div>
		</div>
	</div>
	</div>
</#if>
</@s.i18n> 