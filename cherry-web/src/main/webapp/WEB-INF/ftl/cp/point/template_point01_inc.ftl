<#include "/WEB-INF/ftl/common/popProductTable.ftl">
<#include "/WEB-INF/ftl/common/popProCateTable.ftl">
<#--include "/WEB-INF/ftl/common/prmProductTable.ftl"-->
<#include "/WEB-INF/ftl/common/popRuleTable.ftl">
<#include "/WEB-INF/ftl/common/popPrmCategoryTable.ftl">
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<#-- 会员等级有效期查询URL -->
 <@s.url id="queryLevel_url" value="/cp/BINOLCPPOI01_queryLevel"/>
 <@s.url id="importCounter_Url" value="/cp/BINOLCPPOI01_importCounter"/>
  <@s.url id="importSpecPrt_Url" value="/cp/BINOLCPPOI01_importSpecPrt"/>
 <@s.url id="getTree_url" value="/cp/BINOLCPPOI01_getTree"/>
 <@s.url id="getCounterDetailByAjax_Url" value="/cp/BINOLCPPOI01_getCounterDetail"/>
 <@s.url action="BINOLCM02_initCampaignDialog" namespace="/common" id="searchCampaignInitUrl"/>
 <div class="hide">
 	<a id="queryLevelUrl" href="${queryLevel_url}"></a>
 	<a id="getTreeurl" href="${getTree_url}"></a>
 	<a id="getCounterDetailByAjaxUrl" href="${getCounterDetailByAjax_Url}"></a>
 	<a id="importCounterUrl" href="${importCounter_Url}"></a>
 	<a id="importSpecPrtUrl" href="${importSpecPrt_Url}"></a>
 </div>
 