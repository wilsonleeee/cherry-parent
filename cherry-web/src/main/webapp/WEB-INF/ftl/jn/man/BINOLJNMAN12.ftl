<#include "/WEB-INF/ftl/common/head.inc.ftl">
<@s.i18n name="i18n.cp.BINOLCPCOM01">
	<div class="main container clearfix">
 <div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
          <#-- 积分规则 --><#-- 规则关系-->
         <@s.text name="cp.rulePoint" /> &gt; <@s.text name="cp.ruleRelatTxt" />
       </span>
    </div>
  </div>
  <div class="panel-content">
  	<@c.form id="mainForm" method="post" class="inline" csrftoken="false">
  	 			<table class="detail">
  	 				<tr>
  	 					<#-- 品牌名称 -->
	  	 				<th style="width:20%;"><@s.text name="cp.brandName" /></th>
	  	 				<td style="width:80%;" class="gray">${(relatInfo.brandName)!}</td>
  	 				</tr>
  	 				<tr>
  	 					<#-- 规则名称 -->
	  	 				<th><@s.text name="cp.ruleName" /></th>
	  	 				<td class="gray">${(relatInfo.campaignName)!}</td>
  	 				</tr>
  	 				<tr>
  	 					<#-- 规则类型 -->
	  	 				<th><@s.text name="cp.ruleType" /></th>
	  	 				<td class="gray">${application.CodeTable.getVal("1193","${(relatInfo.prType)!}")}</td>
  	 				</tr>
  	 				<#if (relatInfo.prType)! == "2" >
  	 				<tr>
  	 					<#-- 依附的宿主规则列表 -->
	  	 				<th><@s.text name="cp.mainRuleTxt" /></th>
	  	 				<td style="padding:10px;">
	  	 					<#if (relatInfo.mainRuleList?? && relatInfo.mainRuleList?size>0) >
	  	 					<table class="editable" style="width:60%;">
								<thead>
									<tr>
										<#-- 宿主规则名称 -->
										<th style="background:#FFFEDD; width:30%;"><@s.text name="cp.mainNameTxt" /></th>
										<#-- 所属规则配置 -->
										<th style="background:#FFFEDD; width:30%;"><@s.text name="cp.confTxt" /></th>
									</tr>
								</thead>
								<tbody class="rule_body">
									<#list relatInfo.mainRuleList as mainRule>
										<tr>
											<td>${(mainRule.campaignName)!}</td>
											<td>${(mainRule.groupName)!}</td>
										</tr>						
									</#list>
								</tbody>
							</table>
							<#else>
							<#-- 该规则未依附于任何宿主规则 -->
							<label class="gray"><@s.text name="cp.noMainTxt" /></label>
							</#if>
	  	 				</td>
  	 				</tr>
  	 				<#else>
  	 				<tr>
  	 					<#-- 包含的附属规则列表 -->
	  	 				<th><@s.text name="cp.extraTxt" /></th>
	  	 				<td style="padding:10px;">
	  	 					<#if (relatInfo.extraRuleList?? && relatInfo.extraRuleList?size>0) >
	  	 					<table class="editable" style="width:60%;">
								<thead>
									<tr>
										<#-- 附属规则名称 -->
										<th style="background:#FFFEDD; width:30%;"><@s.text name="cp.extraNameTxt" /></th>
										<#-- 所属规则配置 -->
										<th style="background:#FFFEDD; width:30%;"><@s.text name="cp.confTxt" /></th>
									</tr>
								</thead>
								<tbody class="rule_body">
									<#list relatInfo.extraRuleList as extraRule>
										<tr>
											<td>${(extraRule.campaignName)!}</td>
											<td>${(extraRule.groupName)!}</td>
										</tr>						
									</#list>
								</tbody>
							</table>
							<#else>
							<#-- 该规则未包含任何附属规则 -->
							<label class="gray"><@s.text name="cp.noExtraTxt" /></label>
							</#if>
	  	 				</td>
  	 				</tr>
  	 				<tr>
  	 				<#-- 所属组合规则列表 -->
	  	 				<th><@s.text name="cp.combTxt" /></th>
	  	 				<td style="padding:10px;">
	  	 				<#if (relatInfo.combRuleList?? && relatInfo.combRuleList?size>0) >
	  	 					<table class="editable" style="width:30%;">
								<thead>
									<tr>
										<#-- 组合规则名称 -->
										<th style="background:#FFFEDD; width:30%;"><@s.text name="cp.combNameTxt" /></th>
									</tr>
								</thead>
								<tbody class="rule_body">
									<#list relatInfo.combRuleList as combRule>
										<tr>
											<td>${(combRule.campaignName)!}</td>
										</tr>						
									</#list>
								</tbody>
							</table>
	  	 				</td>
  	 				</tr>
  	 			
  	 			<#else>
  	 			<#-- 该规则不属于任何组合规则 -->
				<label class="gray"><@s.text name="cp.noCombTxt" /></label>
				</#if>
				</#if>
				</table>
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