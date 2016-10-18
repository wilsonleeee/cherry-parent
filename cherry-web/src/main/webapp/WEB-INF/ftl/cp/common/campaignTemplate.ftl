<script type="text/javascript" src="/Cherry/js/cp/common/campaignTemplate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<#include "/WEB-INF/ftl/cp/common/template_core_inc.ftl" />
<#if (templateName)! != "">
<script type="text/javascript" src="/Cherry/js/${(templateName)!?html}.js"></script>	
</#if>
<script type="text/javascript" src="/Cherry/js/cp/common/template_core.js"></script>	 
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>	
<input type="hidden" id="dateHolidays" name="dateHolidays" value="${(holidays)!}"/>
<#macro template index camTemp campMap={} campIndex=0>
	<#include "/WEB-INF/ftl/cp/common/template_core.ftl">
	<#if (templateName)! != "">
		<#include "/WEB-INF/ftl/${(templateName)!?html}.ftl">
	</#if>
</#macro>

<#macro getOptList list val>
<#list list as code>
	<option <#if val == code.CodeKey>selected="selected"</#if> value="${code.CodeKey!}"><@s.text name="${code.Value!}" /></option>
</#list>
</#macro>

<#-- 隐藏模板元数据 -->  
<#macro hideMetadata campTemplate>
	<#-- 模板编号 -->
	<input type="hidden" name="tempCode" value="${(campTemplate.tempCode)!?html}"/>
	<#-- 集合标识符 -->
    <input type="hidden" name="groupCode" value="${(campTemplate.groupCode)!?html}"/>
    <#-- 活动档次当前最大编号 -->
    <input type="hidden" name="MAX_DETAILNO" id="MAX_DETAILNO" value="${(campTemplate.MAX_DETAILNO)!0}"/>
    <#-- 模板的扩展元数据集合不为空时，进行遍历，生成每个元数据对应的页面标签 -->
    <#if campTemplate.metaDataList??>
        <#list campTemplate.metaDataList as metaDataInfo>
        	<#-- 元数据对应的页面标签 -->
          	<input type="hidden" name="${(metaDataInfo.DATA_KEY)!}" value="${(metaDataInfo.DATA_VAL)!}"/>
          	<#-- 如果该元数据是规则模板标识 时，需要增加隐藏规则ID属性 -->
          	<#if (metaDataInfo.DATA_KEY)! == "RULE_TEMP_FLAG">
          			<input type="hidden" name="ruleId" value="${(campTemplate.ruleId)!?html}"/>
          			<#-- 如果该元数据是子活动类型 时，需要增加隐藏子活动连番，子活动ID -->
          		<#elseif (metaDataInfo.DATA_KEY)! == "SUBCAMP_RULETYPE" >
          			<input type="hidden" name="SUBCAMP_DETAILNO" value="${(campTemplate.SUBCAMP_DETAILNO)!?html}"/>
          			<input type="hidden" name="campaignRuleId" value="${(campTemplate.campaignRuleId)!?html}"/>
          	</#if>
        </#list>
    </#if>
</#macro>