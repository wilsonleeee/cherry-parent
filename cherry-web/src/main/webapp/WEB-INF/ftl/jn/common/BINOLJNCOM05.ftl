<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<script type="text/javascript" src="/Cherry/js/jn/common/BINOLJNCOM05.js"></script>
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<#include "/WEB-INF/ftl/common/head.inc.ftl">
<@s.url id="savePriority_url" action="BINOLJNMAN06_savePriority" namespace="/jn"/>
<@s.url id="add_url" action="BINOLJNMAN06_init" namespace="/jn"/>
<@s.url id="edit_url" action="BINOLJNMAN06_editInit" namespace="/jn"/>
<div class="hide">
	<a id="savePriorityUrl" href="${savePriority_url}"></a>
	<a id="addUrl" href="${add_url}"></a>
	<a id="editUrl" href="${edit_url}"></a>
</div>
<div class="main container clearfix">
    <div id="priorityPage" class="panel ui-corner-all">
	    <div class="panel-header">
	        <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<@s.text name="cp.ruleConfig" />
	       </span>
	    </div>
	      </div>
            <div class="panel-content">
            <div class="box4">
                <div class="box4-header">
	                <strong><@s.text name="cp.configSuccess" /></strong>
	                <span class="ui-widget breadcrumb" style="position: relative;"><@s.text name="cp.configTip" /></span>
                </div>
                <div class="box4-content box2-active">
                <@c.form id="toConfForm" method="post" csrftoken="false">
		        	<input type="hidden" name="brandInfoId" value="${(brandInfoId)!?html}" />
		        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
		        	<input type="hidden" name="campaignGrpId" id="campGroupId"/>
		        </@c.form>
				<@c.form id="mainForm" method="post" class="inline" csrftoken="false">
					<input type="hidden" name="campaignId" id="campaignId" value="${(campaignId)!?html}" />
					<input type="hidden" name="brandInfoId" id="brandInfoId" value="${(brandInfoId)!?html}" />
	                <div class="group-header clearfix"><@s.text name="cp.configSortSetting" /></div>
	                <div class="group-content box2-active clearfix">
	                  <div class="box2 box2-content ui-widget">
	                  <#if (ruleConfigList?? && ruleConfigList?size>0) >
	                  	<p style="margin-top:30px;margin-left:400px;"><label style="width:120px;"><@s.text name="cp.ruleConfigName" /></label>
	                  		<span><@s.select id="campaignGrpId" name="campaignGrpId" list="ruleConfigList" listKey="campaignGrpId" listValue="groupName" /></span>
	                    </p>
	                    <p style="margin-top:10px;margin-left:400px;">
	                    <label style="width:120px;"><@s.text name="cp.configSort" /></label>
	                    <span>
	                    <input name="prioritySel" type="radio" value="0" id="first" class="radio" value="0" checked="checked"><label for="first"><@s.text name="cp.configSortFirst" /></label></span><span style="margin-right:50px;">
	                    <input name="prioritySel" type="radio" value="1" id="end" class="radio" value="1"><label for="end"><@s.text name="cp.configSortLast" /></label>
	                    </span>
	                    </p>
	                    <p class="center" style="margin-top:30px;"><span class="breadcrumb"><@s.text name="cp.configSortDep" />
	                    </span>
	                    <a href="javascript:void(0);" onclick="javascript:BINOLJNCOM05.addConfig('1');return false;" style="margin-left:5px;"><@s.text name="cp.jeepToConfig" /></a>
	                    </p>
	                    <#else>
	                    	<p class="center" style="margin-top:30px;"><span class="breadcrumb"><@s.text name="cp.noConfigSortDep" />
		                    </span>
		                    <a href="javascript:void(0);" onclick="javascript:BINOLJNCOM05.addConfig('0');return false;" style="margin-left:5px;"><@s.text name="cp.configCreate" /></a>
		                    </p>
	                   	</#if>
	                   	</div>
	                  </div>
                  </div>
               </@c.form>
            </div>
            <div class="center clearfix">
            <#if (ruleConfigList?? && ruleConfigList?size>0) >
          <button onClick="BINOLJNCOM05.prioritySave()" class="save"><span class="ui-icon icon-confirm"></span><span class="button-text"><@s.text name="cp.sure" /></span></button>
          <button onClick="BINOLJNCOM05.doClose();" class="close"><span class="ui-icon icon-close"></span><span class="button-text"><@s.text name="cp.cancel" /></span></button>
       		<#else>
       			<button class="close" onclick="BINOLJNCOM05.doClose()">
       				<span class="ui-icon icon-close"></span>
             		<span class="button-text"><@s.text name="cp.close" /></span>
         		</button>
       		</#if>
        </div>
      </div>
    </div>
    <div class="hide" id="dialogPopConf"></div>
  </@s.i18n>