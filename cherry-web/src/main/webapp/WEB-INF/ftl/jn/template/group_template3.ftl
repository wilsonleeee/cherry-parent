<@s.i18n name="i18n.cp.BINOLCPCOM01">
<#if camTemp.tempCode == "BASE000029">
	<div id="${camTemp.tempCode}_${index}" class="box4">
	<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
	<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
	<input type="hidden" name="RULE-TEMP-FLAG" value="1"/>
	<div class="box4-header">
	<input type="checkbox" id="check_${camTemp.tempCode}_${index}" name="pointLimit" onClick="grptemp3.showTable(this,'box4','box4-content');" value="1" <#if ("0" != (camTemp.showFlg)!) && ((camTemp.pointLimit)! == "1" || "" == (camTemp.pointLimit)!)>checked="checked"</#if> />
	<strong><@s.text name="cp.pointLimit" /></strong></div>
	<div <#if "0" != (camTemp.showFlg)!>class="box4-content" <#else>class="box4-content hide"</#if>>
		<p style="margin-left:20px;" >
			<span class="bg_title"><input type="checkbox" id="oneLimit" name="oneLimit" value="1" <#if (camTemp.oneLimit)! == "1">checked="checked"</#if>/>
			<label for="oneLimit"><@s.text name="cp.oneLimit" /></label></span>
		</p>
		<p style="margin-left:40px">
			<input type="radio" name="onePoint" value="0"  class="radio" id="oneLimitMoney" <#if (camTemp.onePoint)! == "0">checked="checked"</#if>/>
			<label for="oneLimitMoney"><@s.text name="cp.useMoneyLimit" /></label>
			<span>
				<input type="text" class="input" style="width:30px;height:15px;" name="maxMenoy" value="${(camTemp.maxMenoy)!?html}"/>
				</span><span><@s.text name="cp.passPoint" />
			</span>
		</p>
		<p style="margin-left:40px">
			<input type="radio" class="radio" value="1" name="onePoint" id="oneLimitMoneyGive" <#if (camTemp.onePoint)! == "1">checked="checked"</#if>/>
			<label for="oneLimitMoneyGive"><@s.text name="cp.useMoneyLimit" /></label>
			<span>
				<input type="text" class="input" style="width:30px;height:15px;" name="maxMenoyGive" value="${(camTemp.maxMenoyGive)!?html}"/>
				</span><span><@s.text name="cp.passMoney" />
			</span>
			<span>
				<input type="text" name="multiple" class="input" style="width:30px;height:15px;" value="${(camTemp.multiple)!?html}"/>
				</span><span><@s.text name="cp.mulComPoint" />
			</span>
		</p>
		<p style="margin-left:40px">
			<input type="radio"  class="radio"  value="2" <#if (camTemp.onePoint)! == "2">checked="checked"</#if> id="oneLimitMark" name="onePoint"/>
			<label for="oneLimitMark"><@s.text name="cp.giveMonPass" /></label>
			<span>
				<input type="text" class="input" style="width:30px;height:15px;" name="maxPoint" value="${(camTemp.maxPoint)!?html}"/>
			</span>
			<span><label><@s.text name="cp.passMoney" /></label>
			</span>
		</p>
		<p style="margin-left:40px">
			<input type="radio"  class="radio"  value="3" <#if (camTemp.onePoint)! == "3">checked="checked"</#if> id="oneLimitGiveMark" name="onePoint"/>
			<label for="oneLimitGiveMark"><@s.text name="cp.giveMonPass" /></label>
			<span>
				<input type="text" class="input" name="maxGivePoint" value="${(camTemp.maxGivePoint)!?html}"/>
				</span><span><label><@s.text name="cp.passMoneyGive" /></label>
			</span>
			<span>
				<input type="text" class="input" name="mulGive" value="${(camTemp.mulGive)!?html}" />
				</span><span><label><@s.text name="cp.mulPoint" /></label>
			</span>
		</p>
		<div class="line_Yellow">
		</div>
		<p style="margin-left:20px;">
			<span class="bg_title"><input type="checkbox" id="allLimit" name="allLimit" onclick="TEMP002.showLimitDiv(this);" value="1" <#if (camTemp.allLimit)! == "1">checked="checked"</#if> />
			<label for="allLimit"><@s.text name="cp.dayLimit" /></label></span>
		</p>
		<p style="margin-left:40px">
			<input type="radio" class="radio" value="0" id="allLimitMoney" name="allPoint" <#if (camTemp.allPoint)! == "0">checked="checked"</#if>/>
			<label for="allLimitMoney"><@s.text name="cp.useMoneyLimit" /></label>
			<span>
				<input type="text" class="input" style="width:30px;height:15px;" name="allMaxMenoy" value="${(camTemp.allMaxMenoy)!?html}"/>
				</span><span><@s.text name="cp.passPoint" />
			</span>
		</p>
		<p style="margin-left:40px">
			<input type="radio" class="radio" value="1" id="allLimitMoneyGive" name="allPoint" <#if (camTemp.allPoint)! == "1">checked="checked"</#if>/>
			<label for="allLimitMoneyGive"><@s.text name="cp.useMoneyLimit" /></label>
			<span>
				<input type="text" class="input" style="width:30px;height:15px;" name="allMaxGive" value="${(camTemp.allMaxGive)!?html}"/>
				</span><span><@s.text name="cp.passMoney" />
			</span>
			<span>
				<input type="text" name="allMultiple" class="input" style="width:30px;height:15px;" value="${(camTemp.allMultiple)!?html}"/>
				</span><span><@s.text name="cp.mulComPoint" />
			</span>
		</p>
		<p style="margin-left:40px">
			<input type="radio" class="radio" value="2" id="allLimitMark" name="allPoint" <#if (camTemp.allPoint)! == "2">checked="checked"</#if>/>
			<label for="allLimitMark"><@s.text name="cp.giveMonPass" /></label>
			<span>
				<input type="text" class="input" style="width:30px;height:15px;" name="allLimitPoint" value="${(camTemp.allLimitPoint)!?html}"/>
				</span><span><label><@s.text name="cp.passMoney" /></label>
			</span>
		</p>
		<p style="margin-left:40px">
			<input type="radio"  class="radio"  value="3" <#if (camTemp.allPoint)! == "3">checked="checked"</#if> id="allLimitGiveMark" name="allPoint"/>
			<label for="allLimitGiveMark"><@s.text name="cp.giveMonPass" /></label>
			<span>
				<input type="text" class="input" name="maxAllGivePoint" value="${(camTemp.maxAllGivePoint)!?html}"/>
				</span><span><label><@s.text name="cp.passMoneyGive" /></label>
			</span>
			<span>
				<input type="text" class="input" name="mulAllGive" value="${(camTemp.mulAllGive)!?html}" />
				</span><span><label><@s.text name="cp.mulPoint" /></label>
			</span>
		</p>
	</div>
	</div>
<#elseif camTemp.tempCode == "BASE000030">
	<div id="${camTemp.tempCode}_${index}" class="box4">
	<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
	<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
	<input type="hidden" name="RULE-TEMP-FLAG" value="1"/>
	<div class="box4-header">
		<!--<input type="checkbox" id="check_${camTemp.tempCode}_${index}" onClick="grptemp3.showTable(this,'box4','box4-content');" name="pointLimit" value="1" <#if ("0" != (camTemp.showFlg)!) && ((camTemp.pointLimit)! == "1" || "" == (camTemp.pointLimit)!)>checked="checked"</#if> />-->
		<strong><@s.text name="cp.comRule" /></strong>
		<!--<input type="button" name="addJon" class="add right" value="添加" onClick="grptemp3.poptable(this,'0');" />-->
	</div>
	<div class="box4-content">
	 <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
            <thead>
              <tr>    
                <th width="10%"><@s.text name="cp.hanghao" /></th>       
                <th width="20%"><@s.text name="cp.baseRule" /></th>
                <th width="20%"><@s.text name="cp.startTime" /></th>
                <th width="20%"><@s.text name="cp.endTime" /></th>
                <th width="20%"><@s.text name="cp.extraRule" /></th>
              </tr>
            </thead>
            <tbody id="dataTable">
            	<#if (camTemp.combinationRule?? && camTemp.combinationRule?size>0) >
            	<#list camTemp.combinationRule as combinationInfo>
            		<tr id="${combinationInfo_index}">
            			<td>${combinationInfo_index + 1}</td>
            			<td><span id="campaignName" >${(combinationInfo.campaignName)!}</span><input type="hidden" name="campaignId" value="${(combinationInfo.campaignId)!}"/></td>
					    <td>${(combinationInfo.campaignFromDate)!}</td>
					    <td>${(combinationInfo.campaignToDate)!}</td>
            			<td id="td${combinationInfo_index}">
		            		<a class="add right" onClick="grptemp3.poptable(this,2,'${combinationInfo_index}');">
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><@s.text name="cp.addRule" /></span></a>
            				<#if (combinationInfo.extraRule?? && combinationInfo.extraRule?size>0)>
            				<#list combinationInfo.extraRule as extraRule>
            					<span>
            						<br>
            						<span id="extraRule${extraRule_index}">${(extraRule.campaignName)!?html}</span>
            						<input type="hidden" name="campaignId" value="${(extraRule.campaignId)!?html}"/>
            						<span onclick="grptemp3.deleteExtraRule(this);return false;" class="close">
            							<span class="ui-icon ui-icon-close"></span>
            						</span>
            					</span>
            				</#list>
            				</#if>
            			</td>
            		</tr>
            	</#list>
            	</#if>
            </tbody>
	</table>
	</div>
	</div>
<#elseif camTemp.tempCode == "BUS000042">
	<div id="${camTemp.tempCode}_${index}" class="box4">
	<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
	<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
	<input type="hidden" name="TEMPCOPYSIZE" id="TEMPCOPY-SIZE-${camTemp.tempCode}_${index}" value='${(camTemp.TEMPCOPYSIZE)!"1"}'/>
	<input type="hidden" name="RULE-TEMP-FLAG" value="1"/>
	<div id="errmessage" style="display:none">
		<span id="errmsg1"><@s.text name="cp.straNull" /></span>
		<span id="errmsg2"><@s.text name="cp.straDob" /></span>
	</div>
	<div class="box4-header">
	<input type="checkbox" id="check_${camTemp.tempCode}_${index}" onClick="grptemp3.showTable(this,'box4','box4-content');" name="pointLimit" value="1" <#if ("0" != (camTemp.showFlg)!) && ((camTemp.pointLimit)! == "1" || "" == (camTemp.pointLimit)!)>checked="checked"</#if> />
	<strong><@s.text name="cp.setStra" /></strong>
	<a class="add right" onClick="CAMPAIGN_TEMPLATE.addRule(this,'${camTemp.tempCode}_${index}', 'box4');" name="addJon">
		<span class="ui-icon icon-add"></span>
		<span class="button-text"><@s.text name="cp.addStra" /></span>
	</a>
	</div>
	<div <#if "0" != (camTemp.showFlg)!>class="box4-content" <#else>class="box4-content hide"</#if>>
		<#list camTemp.combTemps as combTemp>
	        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
		</#list>
		<div id="add_${camTemp.tempCode}_${index}" class="hide no_submit">
		<#list camTemp.addCombTemps as combTemp>
	          <@template index=index + "_" + combTemp_index + "_1_TEMPCOPY" camTemp=combTemp/>
		</#list>
		</div>
	</div>
	</div>
<#elseif camTemp.tempCode == "BASE000032">

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
						<#if (priorityRule.affilList?? && priorityRule.affilList?size>0) >
						<span class="gadget-icon icon-ibox-expand" onclick="grptemp3.showAffilRule(this);" style="display:inline-block"></span>
						</#if>
						<#if "" != (priorityRule.campaignName)!>
							${(priorityRule.campaignName)!}<input type="hidden" name="campaignId" value="${(priorityRule.campaignId)!}"/>
						<#elseif "" != (priorityRule.strategyRuleName)!>
							${(priorityRule.strategyRuleName)!}
							<input type="hidden" name="strategyRuleName" value="${(priorityRule.strategyRuleName)!}"/>
							<#if (priorityRule.strategyRuleList?? && priorityRule.strategyRuleList?size>0)>
								<div class="straCamId hide">
								<#list priorityRule.strategyRuleList as strategyRule>
									<input type="hidden" name="campaignId" value="${(strategyRule.campaignId)!}"/>
								</#list>
								</div>
							</#if>
						</#if>
						<#if (priorityRule.affilList?? && priorityRule.affilList?size>0) >
						<div class="affilRulePanel">
						<span style="display:block;margin-left:15px;"><label class="gray"><@s.text name="cp.affilRuleTxt" /></label></span>
						<#list priorityRule.affilList as affilRule>
						<span style="display:block;margin-left:15px;"><label class="gray">${(affilRule.campaignName)!}</label></span>
						</#list>
						</div>
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
		<#if (camTemp.defaultRuleList??) >
		<div class="section-header" >
			<Strong>默认积分规则</Strong>
		</div>
		<div class="section-content" >
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tbody id="defaultRuleTable">
		<#if (camTemp.defaultRuleList?size>0) >
			<#list camTemp.defaultRuleList as defaultRule>
			<tr><td width="5%">${(defaultRule_index  + 1)!}</td>
			<td width="25%">
				${(defaultRule.campaignName)!}
				<input type="hidden" name="campaignId" value="${(defaultRule.campaignId)!}"/>
				<input type="hidden" name="defaultFlag" value="1"/>
			</td>
			<td width="20%">${(defaultRule.campaignFromDate)!}</td>
			<td width="20%">${(defaultRule.campaignToDate)!}</td>
			<td width="20%">
			</td>
			</tr>
			</#list>
		</#if>
		</tbody>
		</table>
		</div>
		</div>
		</#if>
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
						<#if (priorityRule.strategyRuleList?? && priorityRule.strategyRuleList?size>0)>
							<#list priorityRule.strategyRuleList as strategyRule>
								<input type="hidden" name="campaignId" value="${(strategyRule.campaignId)!}"/>
							</#list>
						</#if>
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
					<#if (priorityRule.affilList?? && priorityRule.affilList?size>0) >
						<span class="gadget-icon icon-ibox-expand" onclick="grptemp3.showAffilRule(this);" style="display:inline-block"></span>
						</#if>
					<#if "" != (priorityRule.campaignName)!>
						${(priorityRule.campaignName)!}<input type="hidden" name="campaignId" value="${(priorityRule.campaignId)!}"/>
					<#elseif "" != (priorityRule.strategyRuleName)!>
						${(priorityRule.strategyRuleName)!}
						<input type="hidden" name="strategyRuleName" value="${(priorityRule.strategyRuleName)!}"/>
						<#if (priorityRule.strategyRuleList?? && priorityRule.strategyRuleList?size>0)>
							<#list priorityRule.strategyRuleList as strategyRule>
								<input type="hidden" name="campaignId" value="${(strategyRule.campaignId)!}"/>
							</#list>
						</#if> 
					</#if>
					<#if (priorityRule.affilList?? && priorityRule.affilList?size>0) >
						<div class="affilRulePanel">
						<span style="display:block;margin-left:15px;"><label class="gray"><@s.text name="cp.affilRuleTxt" /></label></span>
						<#list priorityRule.affilList as affilRule>
						<span style="display:block;margin-left:15px;"><label class="gray">${(affilRule.campaignName)!}</label></span>
						</#list>
						</div>
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
		<#if (camTemp.defaultRuleList??) >
		<div class="section-header" >
			<Strong>默认积分规则</Strong>
		</div>
		<div class="section-content" >
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tbody>
		<#if (camTemp.defaultRuleList?size>0) >
			<#list camTemp.defaultRuleList as defaultRule>
			<tr><td width="5%">${(defaultRule_index  + 1)!}</td>
			<td width="25%">
				${(defaultRule.campaignName)!}
			</td>
			<td width="20%">${(defaultRule.campaignFromDate)!}</td>
			<td width="20%">${(defaultRule.campaignToDate)!}</td>
			</tr>
			</#list>
		</#if>
		</tbody>
		</table>
		</div>
		</div>
		</#if>
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
						<#if (priorityRule.strategyRuleList?? && priorityRule.strategyRuleList?size>0)>
							<#list priorityRule.strategyRuleList as strategyRule>
								<input type="hidden" name="campaignId" value="${(strategyRule.campaignId)!}"/>
							</#list>
						</#if>
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
<#elseif camTemp.tempCode == "BASE000031" >
	<div id="${camTemp.tempCode}_${index}" class="box2" style="padding: 10px; border: 1px solid rgb(204, 204, 204);">
		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
		<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
		<input type="hidden" name="RULE-TEMP-FLAG" value="1"/>
		<div id="straName_${camTemp.tempCode}_${index}">
		<div class="toolbar clearfix" >
			<span style="float: right;">
                <a class="delete" onclick="grptemp3.deleteStrategyRule(this);">
	                <span class="ui-icon icon-delete"></span>
	                <span class="button-text"><@s.text name="cp.delete" /></span>
                </a>
			</span>
		</div>
		<div class="line_gray">
		</div>
		<p>
			<label style="width:80px;float:left;"><@s.text name="cp.straName" /></label>
			<span>
				<input type="text" id="strategyGroup_${camTemp.tempCode}_${index}" name="strategyRuleName" value="${(camTemp.strategyRuleName)!?html}"/>
			</span>
		</p>
		</div>
		<p>
			<label style="width:80px;float:left;"><@s.text name="cp.choiceStra" /></label>
			<@s.select list='#application.CodeTable.getCodes("1166")' listKey="CodeKey" listValue="Value" value="${(camTemp.strategyType)!?html}"  name="strategyType"/>
		</p>
		<p>
			<label style="width:80px;float:left;"><@s.text name="cp.choiceRule" /></label>
			<a name="addJon" onclick="grptemp3.poptable(this,'1',3,'ruleTable_${camTemp.tempCode}_${index}');" class="add left">
				<span class="ui-icon icon-add"></span>
				<span class="button-text"><@s.text name="cp.addRule" /></span>
			</a>
		<br/><table border="0" cellpadding="0" cellspacing="0" width="50%" class="proTable" style="margin-left:85px;">
				<thead>
					<tr>
						<th><@s.text name="cp.ruleName" /></th>
						<th><@s.text name="cp.act" /></th>
					</tr>
				</thead>
				<tbody id="ruleTable_${camTemp.tempCode}_${index}">
					<#if (camTemp.strategyRuleList?? && camTemp.strategyRuleList?size>0) >
					<#list camTemp.strategyRuleList as strategyRule>
						<tr>
						<td>${(strategyRule.campaignName)!}<input type="hidden" name="campaignId" value="${(strategyRule.campaignId)!}"/></td>
						<td>
							 <a class="delete" onclick="grptemp3.deleteStrategyRule(this,'ruleTable_${camTemp.tempCode}_${index}');">
				                <span class="ui-icon icon-delete"></span>
				                <span class="button-text"><@s.text name="cp.delete" /></span>
			                </a>
			            </td>
			            </tr>
					</#list>
					</#if>
				</tbody>
			</table>
		</p>
		<p><label style="width:80px;float:left;"><@s.text name="cp.jihuoRule" /></label><@s.text name="cp.straTip" /></p>
	</div>
	
	<#-- 计算设置确认画面  -->
<#elseif camTemp.tempCode == "BASE000058">
	<#if (camTemp.pointLimit)! == "1">
	<div class="box4">
	<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
	<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
	<div class="box4-header clearfix">
		<strong class="left"><@s.text name="cp.roundAndLimit" /></strong>
	</div>
	<div class="box4-content">
		<#if (camTemp.oneLimit)! == "1">
			<#if (camTemp.onePoint)! == "0">
				<p>
					<label><@s.text name="cp.oneUseMoney" /></label>
					<label style="color:red;">${(camTemp.maxMenoy)!?html}</label>
					<label><@s.text name="cp.passPoint" /></label>
				</p>
			<#elseif (camTemp.onePoint)! == "1">
				<p>
					<label><@s.text name="cp.oneUseMoney" /></label>
					<label style="color:red;">${(camTemp.maxMenoyGive)!?html}</label>
					<label><@s.text name="cp.passMoney" /></label>
					<label style="color:red;">${(camTemp.multiple)!?html}</label>
					<label><@s.text name="cp.mulComPoint" /></label>
				</p>
			<#elseif (camTemp.onePoint)! == "2">
				<p>
					<label><@s.text name="cp.oneUsePoint" /></label>
					<label style="color:red;">${(camTemp.maxPoint)!?html}</label>
					<label>分</label>
				</p>
			<#elseif (camTemp.onePoint)! == "3">
				<p>
					<label><@s.text name="cp.oneUseMoney" /></label>
					<label style="color:red;">${(camTemp.maxGivePoint)!?html}</label>
					<label><@s.text name="cp.passMoney" /></label>
					<label style="color:red;">${(camTemp.mulGive)!?html}</label>
					<label><@s.text name="cp.mulComPoint" /></label>
				</p>
			</#if>
		</#if>
		<#if (camTemp.allLimit)! == "1">
			<#if (camTemp.allPoint)! == "0">
				<p>
					<label><@s.text name="cp.allUseMoney" /></label>
					<label style="color:red;">${(camTemp.allMaxMenoy)!?html}</label>
					<label><@s.text name="cp.passPoint" /></label>
				</p>
			<#elseif (camTemp.allPoint)! == "1">
				<p>
					<label><@s.text name="cp.allUseMoney" /></label>
					<label style="color:red;">${(camTemp.allMaxGive)!?html}</label>
					<label><@s.text name="cp.passMoney" /></label>
					<label style="color:red;">${(camTemp.allMultiple)!?html}</label>
					<label><@s.text name="cp.mulComPoint" /></label>
				</p>
			<#elseif (camTemp.allPoint)! == "2">
				<p>
					<label><@s.text name="cp.allUsePoint" /></label>
					<label style="color:red;">${(camTemp.allLimitPoint)!?html}</label>
					<label>分</label>
				</p>
			<#elseif (camTemp.allPoint)! == "3">
				<p>
					<label><@s.text name="cp.allUseMoney" /></label>
					<label style="color:red;">${(camTemp.maxAllGivePoint)!?html}</label>
					<label><@s.text name="cp.passMoney" /></label>
					<label style="color:red;">${(camTemp.mulAllGive)!?html}</label>
					<label><@s.text name="cp.mulComPoint" /></label>
				</p>
			</#if>
		</#if>
	</div>
	</div>
	</#if>
</#if>
</@s.i18n> 