<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<script type="text/javascript" src="/Cherry/js/cp/common/campaignTemplate.js"></script>
<#include "/WEB-INF/ftl/common/head.inc.ftl">
<@s.url id="saveRuleCon_url" value="/cp/BINOLCPCOM02_savePriorityRule"/>
<div class="main container clearfix">
	<div class="hide">
		<a id="saveRuleConUrl" href="${saveRuleCon_url}" />
	</div>
    <div id="priorityPage" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span>
        <a href="#"><@s.text name="cp.ruleConfig" /></a> </span> </div>
      </div>
            <div class="panel-content">
            <div class="box4">
                <div class="box4-header">
	                <strong><@s.text name="cp.configSuccess" /></strong>
	                <span class="ui-widget breadcrumb" style="position: relative;"><@s.text name="cp.configTip" /></span>
                </div>
                <div class="box4-content box2-active">
				<@c.form id="toNextForm" action="BINOLCPCOM02_init" method="post" csrftoken="false">
        			<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
        			<div id="campaignInfo" class="hide">
		                <input type="hidden" name="campaignId" id="campaignId" value="${(campaignId)!?html}" />
		                <input type="hidden" name="campaignType" id="campaignType" value="${(campaignType)!?html}" />
		                <input type="hidden" name="brandInfoId" id="brandInfoId" value="${(brandInfoId)!?html}" />
		                <input type="hidden" name="memberClubId" id="memberClubId" value="${(memberClubId)!}"/>
	        			<input type="hidden" name="configKbn" id="configKbn" value="1"/> 
	                </div>
					<input type="hidden" name="campInfo.campaignType" id="campaignType" value="${(campaignType)!?html}"/>
        			<input type="hidden" name="campParamInfo" id="campParamInfo" value="${(campParamInfo)!?html}"/> 
        			<input type="hidden" name="confEditKbn" id="confEditKbn" value="${(confEditKbn)!?html}"/> 
	                <div class="group-header clearfix"><@s.text name="cp.configPriority" /><span class="ui-widget breadcrumb" style="position: relative;"><@s.text name="cp.priorityConfig" /></span></div>
	                <div class="group-content box2-active clearfix">
	                  <div class="box2 box2-content ui-widget">
	                    <p class="center" style="margin-top:10px;"><span style="margin-right:50px;">
	                    <input name="prioritySet" type="radio" value="0" id="first" class="radio" value="0" checked="checked"><label for="first"><@s.text name="cp.firstDo" /></label></span><span style="margin-right:50px;">
	                    <input name="prioritySet" type="radio" value="1" id="end" class="radio" value="1"><label for="end"><@s.text name="cp.endDo" /></label></span></p>
	                    <p class="center" style="margin-top:30px;"><span class="breadcrumb"><@s.text name="cp.tipPoint" /></span>
	                    <a href="#" onclick="javascript:CAMPAIGN_TEMPLATE.gotoConfig();return false;" style="margin-left:5px;"><@s.text name="cp.jeepToConfig" /></a></p>
	                   	</div>
	                  </div>
                  </div>
               </@c.form>
            </div>
            <div class="center clearfix">
          <button onClick="javascript:CAMPAIGN_TEMPLATE.saveRuleJson()" class="back" type="button"><span class="ui-icon icon-confirm"></span><span class="button-text"><@s.text name="cp.sure" /></span></button>
          <button onClick="javascript:window.close();window.opener.search();" class="save" type="button"><span class="ui-icon icon-close"></span><span class="button-text"><@s.text name="cp.cancel" /></span></button>
        </div>
      </div>
    </div>
	<div class="hide" id="dialogInit"></div>
     <div class="hide">
       <span id="title"><@s.text name="cp.setPrioritytitle" /></span>
       <span id="sure"><@s.text name="cp.sure" /></span>
       <span id="cancel"><@s.text name="cp.cancel" /></span>      
       <span id="testMes"><p class='message'><span><@s.text name="cp.doSure" /></span></p></span>
      </div>
  </div>
  </@s.i18n>