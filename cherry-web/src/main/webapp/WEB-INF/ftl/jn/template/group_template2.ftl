<#-- 升降级规则策略  -->
<#if camTemp.tempCode == "BASE000032">
	<!--<#if (camTemp.priorityRuleList?? && camTemp.priorityRuleList?size>0) >-->
	<div id="${camTemp.tempCode}_${index}" class="box4">
	<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
	<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
	<input type="hidden" name="RULE-TEMP-FLAG" value="1"/>
	<div class="box4-header">
		<strong>设置优先级</strong>
	</div>
	<div class="box4-content" >
		<div class="toolbar clearfix" style="padding:2px;">
			<!--<a name="addJon" onclick="CAMPAIGN_TEMPLATE.poptable(this,'1',3,'rulePriorityTable');" class="add left">
				<span class="ui-icon icon-add"></span>
				<span class="button-text">添加规则</span>
			</a>-->
		</div>
		<div class="section" >
		<div class="section-header" >
			<Strong>已配置规则</Strong>
		</div>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="proTable">
		<thead>
            <tr>
               <th></th>
               <th>规则名称</th>
               <th>优先级</th>
               <th>操作</th>
            </tr>
          </thead>
		<tbody id="rulePriorityTable">
			<#if (camTemp.priorityRuleList?? && camTemp.priorityRuleList?size>0) >
			<#list camTemp.priorityRuleList as priorityRule>
				<#if ((priorityRule.deleteFlag)! != "1") >
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
					<#if "" != (priorityRule.campaignName)!>
						<td>
							<a class="delete" onclick="CAMPAIGN_TEMPLATE.deleteStrategyRule(this,'rulePriorityTable');">
				                <span class="ui-icon icon-delete"></span>
				                <span class="button-text">删除</span>
			                </a>
			            </td>
					<#elseif "" != (priorityRule.strategyRuleName)!>
						 <td></td>
					</#if>
					</tr>
				</#if>
			</#list>
			</#if>
		</tbody>
		</table>
		</div>
		
		<div class="section" >
		<div class="section-header" >
			<Strong>未配置规则</Strong>
		</div>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="proTable">
		<thead>
            <tr>
               <th></th>
               <th>规则名称</th>
               <th>优先级</th>
               <th>操作</th>
            </tr>
          </thead>
		<tbody id="rulePriorityDotConTable">
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
				<td><#if (priorityRule_index == 0)>
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
				</#if></td>
				<#if "" != (priorityRule.campaignName)!>
					<td>
						<a class="delete" onclick="CAMPAIGN_TEMPLATE.deleteStrategyRule(this,'rulePriorityTable');">
			                <span class="ui-icon icon-delete"></span>
			                <span class="button-text">删除</span>
		                </a>
		            </td>
				<#elseif "" != (priorityRule.strategyRuleName)!>
					 <td></td>
				</#if>
				</tr>
			</#list>
			</#if>
		</tbody>
		</table>
		</div>
	</div>
	</div>
	<!--</#if>-->
</#if>