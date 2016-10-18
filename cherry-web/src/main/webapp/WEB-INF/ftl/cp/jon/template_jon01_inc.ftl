<#include "/WEB-INF/ftl/common/popProductTable.ftl">
<#include "/WEB-INF/ftl/common/popProCateTable.ftl">
<#include "/WEB-INF/ftl/common/prmProductTable.ftl">
<#include "/WEB-INF/ftl/common/popRuleTable.ftl">
<#include "/WEB-INF/ftl/common/popPrmCategoryTable.ftl">
<script type="text/javascript" src="/Cherry/js/common/cherryTree.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<#-- 会员等级有效期查询URL -->
 <@s.url id="queryLevelDate_url" value="/cp/BINOLCPCOM03_queryLevelDate"/>
 <div class="hide">
 	<a id="queryLevelDateUrl" href="${queryLevelDate_url}"></a>
 </div>