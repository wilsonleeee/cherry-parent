<#if (templateName)! != "">
<#include "/WEB-INF/ftl/${(templateName)!?html}_inc.ftl">
</#if>
<#-- �������URL -->
 <@s.url id="RuleTest_url" value="/cp/BINOLCPCOM04_init"/>
 <div class="hide"><a id="RuleTestUrl" href="${RuleTest_url}"></a></div>
