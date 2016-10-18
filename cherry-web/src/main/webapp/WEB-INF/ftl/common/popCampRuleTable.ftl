<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<div id ="campRuleDialog" class="dialog hide">
<input type="text" class="text" id="campRuleKw" onKeyup ="datatableFilter(this,25);" maxlength="50"/>
    <a class="search" onClick="datatableFilter('#campRuleKw',25);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="global.page.searchfor" /></span></a>
  <hr class="space" />
   <table id="campRuleTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
   		<thead>
            <tr>
               <th><@s.text name="cp.choice" /></th>
               <th><@s.text name="cp.ruleName" /></th>
            </tr>
           </thead>
        <tbody id="dataTableBody" >
        </tbody>
   </table>
   
    <hr>
   <div class="center clearfix">
        <button class="confirm" id="selCampRules"><span class="ui-icon icon-confirm"></span><span class="button-text"><@s.text name="global.page.ok" /></span></button>
   </div>
   <#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</div>
<@s.url id="p_ruleSearchUrl" value="/jn/common/BINOLJNCOM03_popCampRule" />
<span id ="campRuleSearchUrl" class="hide">${p_ruleSearchUrl}</span>