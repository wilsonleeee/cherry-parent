<#include "/WEB-INF/ftl/common/head.inc.ftl">
<#include "/WEB-INF/ftl/cp/common/campaignTemplate.ftl">
<script type="text/javascript" src="/Cherry/js/cp/common/BINOLCPCOM04.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/template.css" type="text/css">
<#-- 测试 -->
<@s.url id="startRule_url" action="BINOLCPCOM04_startRule"/>
<@s.i18n name="i18n.cp.BINOLCPCOM04">
<div class="hide">
	<a id="startRuleUrl" href="${startRule_url}"></a>
</div>
<div class="main container clearfix" style="min-width:700px;">
    <div class="panel ui-corner-all">
    <div class="panel-header">
	   <div class="clearfix">
	    <span class="breadcrumb left"> 
	     <@s.text name="cp.ruleTest"/>
	    </span>
	   </div>
	  </div>
	  <div class="panel-content">
	  <div class="section">
	  		<div class="section-header"><strong><span class="ui-icon icon-ttl-section-info-edit"></span>
            <#-- 测试条件 -->
            <@s.text name="cp.testCondition"/></strong></div>
            <div class="section-content">
            <div style="padding:0 0 0 1.4em">
            <@c.form id="mainForm" method="post" class="inline" csrftoken="false">
            	<input type="hidden" name="csrftoken" id="parentCsrftoken" value="${(csrftoken)!?html}"/>
            	<input type="hidden" name="brandInfoId" id="brandInfoId" value="${(brandInfoId)!?html}"/>
            	<input type="hidden" name="camTemps" id="camTemps"/>
            	<input type="hidden" name="ruleDetail" id="ruleDetail" value="${(ruleDetail)!?html}"/>
            	<input type="hidden" name="campaignType" id="campaignType" value="${(campaignType)!?html}"/>
            	<input type="hidden" name="campaignGrpId" id="campaignGrpId" value="${(campaignGrpId)!?html}"/>
            	<input type="hidden" name="loadKbn" id="loadKbn" value="${(loadKbn)!?html}"/>
            	<#-- 会员等级ID -->
	        	<input type="hidden" name="campaignRule.memberLevelId" id="ruleMemberLevelId" value="${(campaignRule.memberLevelId)!?html}"/>
	        	<#-- 会员等级名称 -->
	        	<input type="hidden" name="campaignRule.memberLevelName" id="ruleMemberLevelName" value="${(campaignRule.memberLevelName)!?html}"/>
	        	<#-- 会员等级级别 -->
	        	<input type="hidden" name="campaignRule.memberLevelGrade" id="ruleMemberLevelGrade" value="${(campaignRule.memberLevelGrade)!?html}"/>
            </@c.form>
            
					<table border="0" class="test-condition" id="testCondition">
        			<tbody>
        			
					<#if (camTempList?? && camTempList?size > 0)>
				        <#list camTempList as tempInfo>
				          <@template index=tempInfo_index camTemp=tempInfo/>
				        </#list>
			        </#if>
					
					</tbody></table>
					</div>
					 <p class="clearfix">
              
              		<#-- 查询按钮 -->
              		<button class="right search" type="submit" onclick="BINOLCPCOM04.startRule();return false;">
              			<span class="ui-icon icon-search-big"></span><span class="button-text"><@s.text name="cp.test"/></span>
              		</button>
            		</p>
            </div>
            
      </div>
      <div class="section">
	  		<div class="section-header"><strong><span class="ui-icon icon-flag"></span>
            <#-- 测试结果 -->
            <@s.text name="cp.testRes"/></strong></div>
            <div id="ruleResult">
			 </div>
     </div>
     </div>
</div> 
</div> 
</@s.i18n>