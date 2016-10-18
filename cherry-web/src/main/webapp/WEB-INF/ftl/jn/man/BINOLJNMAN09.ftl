<#include "/WEB-INF/ftl/common/head.inc.ftl">
<@s.i18n name="i18n.cp.BINOLCPCOM01">
	<div class="main container clearfix">
 <div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
        <@s.text name="cp.rulePoint" /> &gt; <@s.text name="cp.combRuleDetail" />
       </span>
    </div>
  </div>
  <div class="panel-content">
  <@c.form id="mainForm" method="post" class="inline" csrftoken="false">
  	 <#if (combRuleInfo??) >
  	 			<table class="detail">
  	 				<tr>
	  	 				<th style="width:20%;"><@s.text name="cp.brandName" /></th>
	  	 				<td style="width:80%;" class="gray">${(combRuleInfo.brandName)!?html}</td>
  	 				</tr>
  	 				<tr>
	  	 				<th><@s.text name="cp.combRuleName" /></th>
	  	 				<td class="gray">${(combRuleInfo.campaignName)!?html}</td>
  	 				</tr>
  	 				<tr>
	  	 				<th><@s.text name="cp.ruleSel" /></th>
	  	 				<td style="padding:10px;">
	  	 					<table class="editable" style="width:100%;">
								<thead>
									<tr>
										<th style="background:#FFFEDD; width:75%;"><@s.text name="cp.ruleName" /></th>
										<th style="background:#FFFEDD; width:25%;"><@s.text name="cp.act" /></th>
									</tr>
								</thead>
								<tbody class="rule_body">
									<#if (combRuleInfo.geneRules?? && combRuleInfo.geneRules?size>0) >
									<#list combRuleInfo.geneRules as geneRule>
										<tr><td>${(geneRule.campaignName)!?html}</td><td></td></tr>						
									</#list>
									</#if>
								</tbody>
							</table>
	  	 				</td>
  	 				</tr>
  	 				<tr>
	  	 				<th><@s.text name="cp.combMatchType" /></th>
	  	 				<td class="gray">${application.CodeTable.getVal("1192","${(combRuleInfo.matchType)!?html}")}</td>
  	 				</tr>
  	 				<tr>
	  	 				<th><@s.text name="cp.configExecType" /></th>
	  	 				<td class="gray">${application.CodeTable.getVal("1166","${(combRuleInfo.combType)!?html}")}</td>
	  	 			</tr>
  	 			</table>
			</#if>     	
			</@c.form>
  <hr class="space">
  <div class="center clearfix" id="pageConfig">
        <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
             <span class="button-text"><@s.text name="cp.close" /></span>
         </button>
     </div>
  </div>
  </div>
  </div>
  </@s.i18n>