<#include "/WEB-INF/ftl/common/head.inc.ftl">
<script type="text/javascript" src="/Cherry/js/jn/man/BINOLJNMAN10.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<@s.url id="saveComb_url" action="BINOLJNMAN06_saveComb"/>
	<div class="main container clearfix">
 <div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
         <@s.text name="cp.rulePoint" /> &gt; <@s.text name="cp.combRuleEdit" />
       </span>
    </div>
  </div>
  <div id="actionResultDisplay"></div>
  <div class="panel-content">
  	 <@c.form id="mainForm" method="post" class="inline" csrftoken="false">
  	 <#if (combRuleInfo??) >
  	 	<table class="detail" id="combContent">
  	 		<tr>
  	 			<th style="width:20%;"><@s.text name="cp.brandName" /></th>
  	 			<td style="width:80%;">
  	 				<@s.select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"  tabindex="1" disabled="true"/>
                 	<input type="hidden" name="brandInfoId" value="${(brandInfoId)!}" id="brandInfoId"/>
                 	<input type="hidden" name="campaignId" value="${(campaignId)!}"/>
                 	<input type="hidden" name="campUpdateTime" value="${(combRuleInfo.campUpdateTime)!}"/>
                 	<input type="hidden" name="campModifyCount" value="${(combRuleInfo.campModifyCount)!}"/>
  	 			</td>
  	 		</tr>
  	 		<tr>
  	 			<th><@s.text name="cp.combRuleName" /></th>
  	 			<td><input type="text" name="campaignName" value="${(combRuleInfo.campaignName)!}" class="text" maxlength="50"/></td>
  	 		</tr>
  	 		<tr>
  	 			<th><@s.text name="cp.ruleSel" /></th>
  	 			<td style="padding:10px;">
					<div class="toolbar clearfix" >
						<span style="float: left;">
							<a href="javascript:void(0);" class="add left" onclick="javascript:BINOLJNMAN10.popGeneRule(this);return false;" >
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><@s.text name="cp.ruleChoice" /></span>
							</a>
						<input type="hidden" name="addJon" class="no_submit"/>
						</span>
					</div>
					<table class="editable" style="width:100%;" id="ruleSelTable">
						<thead>
						<tr>
						<th style="background:#FFFEDD; width:75%;"><@s.text name="cp.ruleName" /></th>
						<th style="background:#FFFEDD; width:25%;"><@s.text name="cp.act" /></th>
						</tr>
						</thead>
						<tbody id="ruleBody">
							<#if (combRuleInfo.geneRules?? && combRuleInfo.geneRules?size>0) >
							<#list combRuleInfo.geneRules as geneRule>
								<tr>
									<td>${(geneRule.campaignName)!?html}<input type="hidden" name="campaignId" value="${(geneRule.campaignId)!}"/></td>
									<td><a class="delete" onclick="BINOLJNMAN10.delGeneRule(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><@s.text name="cp.delete" /></span></a></td>
								</tr>						
							</#list>
							</#if>
						</tbody>
					</table>
  	 			</td>
  	 		</tr>
  	 		<tr>
  	 			<th><@s.text name="cp.combMatchType" /></th>
  	 			<td><@s.select list='#application.CodeTable.getCodes("1192")' listKey="CodeKey" listValue="Value" name="matchType" cssStyle="width:128px;" value="${(combRuleInfo.matchType)!?html}"/></td>
  	 		</tr>
  	 		<tr>
  	 			<th><@s.text name="cp.configExecType" /></th>
  	 			<td><@s.select list='#application.CodeTable.getCodes("1166")' listKey="CodeKey" listValue="Value" name="combType" value="${(combRuleInfo.combType)!?html}"/></td>
  	 		</tr>
  	 	</table>
        </#if>
        </@c.form>   	
	<hr class="space">
	<div class="center clearfix" id="pageComb" >
		<button class="save" type="submit" onclick="BINOLJNMAN10.saveComb();return false;">
			<span class="ui-icon icon-save"></span>
			<span class="button-text"><@s.text name="cp.save" /></span>
		</button>
		<button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
			<span class="button-text"><@s.text name="cp.close" /></span>
		</button>
	</div>
	<div class="hide">
		<a id="saveCombUrl" href="${saveComb_url}"></a>
		<span id="rulediaTitleTxt"><@s.text name="cp.rulediaTitle" /></span>
	</div>
</div>
</div>
</div>
  <#include "/WEB-INF/ftl/common/popCampRuleTable.ftl">
  </@s.i18n>