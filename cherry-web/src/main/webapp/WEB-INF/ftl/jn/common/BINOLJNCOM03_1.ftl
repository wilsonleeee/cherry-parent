<script type="text/javascript" src="/Cherry/js/jn/template/group_template${(campaignType)!?html}.js"></script>
<#macro template index camTemp>
	<#include "/WEB-INF/ftl/jn/template/group_template${(campaignType)!?html}.ftl">
</#macro>
<#if (grpTemplateList?? && grpTemplateList?size > 0)>
<input type="hidden" name="hasRule" value="1" id="hasRule"/>
<div class="section-header"><strong><span class="ui-icon icon-ttl-section-info-edit"></span>
            <#-- 规则设定 -->
           规则设定</strong></div>
            <div class="section-content">
            	
			        <#list grpTemplateList as grpTemplate>
			          <@template index=grpTemplate_index camTemp=grpTemplate/>
			        </#list>
		        
			</div>
</#if>