<#include "/WEB-INF/ftl/common/head.inc.ftl" />
<#include "/WEB-INF/ftl/cp/common/campaignTemplate.ftl" />
<#include "/WEB-INF/ftl/cp/common/campaignSteps.ftl" />
<#include "/WEB-INF/ftl/cp/common/campaignButton.ftl" />
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/cp/common/BINOLCPCOM02.js"></script>
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/cherry/template.css" type="text/css">
 <@s.url id="draftSaveUrl" action="BINOLCPCOM02_draftSave"/>
 <@s.i18n name="i18n.cp.BINOLCPCOM01">
<div class="main container clearfix">
    <div class="panel ui-corner-all">
      <div class="panel-header">
        <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span>
        <a href="#"><@s.text name="cp.memnerAct" /></a> &gt; <a href="#"><@s.text name="cp.ruleInfo" /></a> </span> </div>
      </div>
        <div id="actionDisplay">
        	<div id="errorDiv" class="actionError" style="display:none;">
		      <ul>
		         <li><span id="errorSpan"></span></li>
		      </ul>			
          	</div>
          	<div id="successDiv" class="actionSuccess" style="display:none;">
	  		  <ul class="actionMessage">
               	 <li><span id="successSpan"></span></li>
			  </ul>
			 </div>
        </div>
        <div id="actionResultDisplay"></div>
      <div class="panel-content" id="div_main">
      <ol class="steps clearfix" id="ol_steps">
      <#-- if campStepKbn??>
        <@campaignSteps stepKbn=campStepKbn/>
      </#if -->
      <#if (stepNames?? && stepNames?size > 0)>
	        <#list stepNames as stepName>
	          <li><span><@s.text name="${stepName}"/></span></li>
	        </#list>
        </#if>
        </ol>
      
		<@c.form id="toNextForm" action="BINOLCPCOM02_init" method="post" csrftoken="false">
        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
        	<input type="hidden" name="brandInfoId" id="brandInfoId" value="${(brandInfoId)!?html}"/>
        	<input type="hidden" name="memberClubId" id="memberClubId" value="${(memberClubId)!}"/>
        	<input type="hidden" name="actionId" id="actionId" value="${(actionId)!}"/>
        	<input type="hidden" name="saveActionId" id="saveActionId" value="${(saveActionId)!}"/>
        	<input type="hidden" name="initActionId" id="initActionId" value="${(initActionId)!}"/>
        	<input type="hidden" name="actionKbn" id="actionKbn"/>
        	<input type="hidden" name="camTemps" id="camTemps"/>
        	<input type="hidden" name="onStep" id="onStep" value="${(onStep)!?html}"/>
        	<input type="hidden" name="saveStep" id="saveStep" value="${(saveStep)!}"/>
        	<input type="hidden" name="campParamInfo" id="campParamInfo" value="${(campParamInfo)!?html}"/> 
        	<input type="hidden" name="optKbn" id="optKbn" value="${(optKbn)!}"/>
        	<input type="hidden" name="configKbn" id="configKbn" value="${(configKbn)!}"/> 
        	<input type="hidden" name="copyFlag" id="copyFlag" value="${(copyFlag)!}"/> 
        	<input type="hidden" name="grpModifyCount" value='${(grpModifyCount)!}'/>
        	<input type="hidden" name="grpUpdateTime" value='${(grpUpdateTime)!}'/>
        	<#-- 会员活动保存信息 -->   
        	<input type="hidden" name="campSaveInfo" id="campSaveInfo" value="${(campSaveInfo)!?html}"/>
        	<#-- 会员等级ID -->
        	<input type="hidden" name="campInfo.memberLevelId" id="ruleMemberLevelId" value="${(campInfo.memberLevelId)!?html}"/>
        	<#-- 活动有效期开始日期 -->
        	<input type="hidden" name="campInfo.campaignFromDate" id="campaignFromDate" value="${(campInfo.campaignFromDate)!?html}"/>
        	<#-- 活动有效期结束日期 -->
        	<input type="hidden" name="campInfo.campaignToDate" id="campaignToDate" value="${(campInfo.campaignToDate)!?html}"/>
        	<#-- 规则类型 -->
        	<input type="hidden" name="campInfo.pointRuleType" id="pointRuleType" value="${(campInfo.pointRuleType)!?html}"/>
        	<#-- 保存其他有用信息 -->
        	<input type="hidden" name="extraInfo" id="extraInfo" value="${(extraInfo)!?html}"/>
        	<input type="hidden" name="extArgs" id="extArgs" value="${(extArgs)!?html}"/>
			<input type="hidden" name="campInfo.templateType" id="templateType" value="${(campInfo.templateType)!?html}"/>
        </@c.form>
		<input type="hidden" name="campaignType" id="campaignType" value="${(campInfo.campaignType)!?html}"/>
        <script type="text/javascript">
			BINOLCPCOM02.showSteps();
      	</script>
		<@c.form id="mainForm" method="post" class="inline" csrftoken="false">
		<#if (camTempList?? && camTempList?size > 0)>
	        <#list camTempList as tempInfo>
	          <@template index=tempInfo_index camTemp=tempInfo/>
	        </#list>
        </#if>
		</@c.form>
		<#if (templateName)! == "">
			<div class="box2-active">
		        <div class="box2 box2-content ui-widget">
		            <p class="gray center"><@s.text name="cp.notConfigTip" /></p>
		        </div>
	    	</div>
		</#if>
        <hr class="space" />
        <div class="center clearfix" id="pageButton">
       <#if (campBtns?? && campBtns?size > 0)>
	        <#list campBtns as campBtn>
	          <@campaignButton btnKbn=campBtn/>
	        </#list>
        <#else>
        <button class="close" onclick="window.close();return false;">
        	<span class="ui-icon icon-close"></span>
            <span class="button-text"><@s.text name="cp.close" /></span>
         </button>
        </#if>
        </div>
      </div>
    </div>
  </div>
  </@s.i18n>
