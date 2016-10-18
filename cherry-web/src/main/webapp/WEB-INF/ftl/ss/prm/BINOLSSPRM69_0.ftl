<@s.i18n name="i18n.ss.BINOLSSPRM69">
<#assign enContinueList = [{'CodeKey':'0','Value':'停止匹配'},{'CodeKey':'1','Value':'继续匹配'}] />
<table id="ruleTable0" class="ruleTable" border="0" cellpadding="0" cellspacing="0" width="100%">
	<thead>
	 	<tr class="DISMOVE">
    		 <th scope="col" style="width: 5%;"><@s.text name="No"/></th><#-- No. -->
	         <th scope="col" style="width:25%"><@s.text name="SSPRM69_ruleName"/></th><#-- 规则名称 -->
	         <th scope="col" style="width:25%"><@s.text name="SSPRM69_ruleCode"/></th><#-- 规则码 -->
	         <th scope="col" style="width:20%"><@s.text name="SSPRM69_levelSet"/></th><#-- 优先级操作 -->
	         <th scope="col" style="width:25%"><@s.text name="SSPRM69_enContinue"/></th><#-- 规则成功匹配后 -->
	    </tr>
	</thead>
<#if ruleList?exists && ruleList?size gt 0>
	<tbody class="ruleBody">
		<#list ruleList as rule>
			<#if rule.ruleType != '1'>
				<tr class="<#if rule_index % 2==0>odd<#else>even</#if> ENMOVE" >
			 		<td>
			 			<span class="No">${rule_index + 1}</span>
			 			<input name="level" type="hidden" value="${(rule.level)!}">
			 		</td>
			      	<td>
			      		<@s.url id="prmRuleUrl" value="/ss/BINOLSSPRM68_init" >
					    	<@s.param name="activeID">${rule.activityId!}</@s.param>
					    	<@s.param name="pageNo">6</@s.param>
					    	<@s.param name="brandId">${brandId! }</@s.param>
					    </@s.url>
			      		<a style="color:#3366FF;" href="${prmRuleUrl!}" onclick="openWin(this);return false;">${rule.ruleName!}</a>
			      	</td>
			      	<td>
			      		${rule.ruleCode!}
			      		<input type="hidden" name="ruleCode" value="${rule.ruleCode!}" />
			      	</td>
			      	<td class="SORT"></td>
			      	<td>
			      		<select name="enContinue" disabled="true">
							<@getOptionList list=enContinueList val= rule.enContinue! />
						</select>
			      	</td>
			 	</tr>
		 	</#if>
		</#list>
	</tbody>
</table>
<table id="ruleTable1" class="ruleTable" style="border-top:10px;width:100%">
	<thead>
	 	<tr class="DISMOVE">
    		 <td scope="col" style="width: 5%;"></td>
	         <td scope="col" style="width:25%;"></td>
	         <td scope="col" style="width:25%;"></td>
	         <td scope="col" style="width:20%;"></td>
	         <td scope="col" style="width:25%;"></td>
	    </tr>
	</thead>
	<tbody class="ruleBody">
		<#list ruleList as rule>
			<#if rule.ruleType == '1'>
				<tr class="<#if rule_index % 2==0>odd<#else>even</#if> ENMOVE" style="background:#E7E7E7">
			 		<td scope="col">
			 			<span class="No">${rule_index + 1}</span>
			 			<input name="level" type="hidden" value="${(rule.level)!}">
			 		</td>
			      	<td scope="col">
			      		<@s.url id="prmRuleUrl" value="/ss/BINOLSSPRM68_init" >
					    	<@s.param name="activeID">${rule.activityId!}</@s.param>
					    	<@s.param name="pageNo">6</@s.param>
					    	<@s.param name="brandId">${brandId! }</@s.param>
					    </@s.url>
			      		<a style="color:#3366FF;" href="${prmRuleUrl!}" onclick="openWin(this);return false;">${rule.ruleName!}</a>
			      	</td>
			      	<td scope="col">
			      		${rule.ruleCode!}
			      		<input type="hidden" name="ruleCode" value="${rule.ruleCode!}" />
			      	</td>
			      	<td class="SORT" scope="col"></td>
			      	<td scope="col">
			      		<select name="enContinue" disabled="true">
							<@getOptionList list=enContinueList val= rule.enContinue! />
						</select>
			      	</td>
			 	</tr>
			</#if>
		</#list>
	</tbody>
<#else>
	<tr>
		<td colspan="5"><@s.text name="table_sZeroRecords"/></td>
	</tr>
</#if>
</table>
<#macro getOptionList list val>
<#list list as code>
	<option <#if val == code.CodeKey>selected="selected"</#if> value="${code.CodeKey!}"><@s.text name="${code.Value!}" /></option>
</#list>
</#macro>
</@s.i18n>