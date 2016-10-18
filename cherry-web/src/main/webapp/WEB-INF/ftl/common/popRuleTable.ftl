<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<@s.url id="s_ruleSearchUrl" value="/jn/common/BINOLJNCOM03_popRuleInfo" />
<script type="text/javascript" src="/Cherry/js/jn/template/group_template3.js"></script>
<div id ="ruleDialog" class="dialog hide">

<input type="text" class="text" value="" id="ruleDialogSearch" onKeyup ="datatableFilter(this,'1');" maxlength="50"/>
    <a class="search" onClick="datatableFilter(document.getElementById('ruleDialogSearch'),'1')"><span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="global.page.searchfor" /></span></a>
  <hr class="space" />
   <table id="rule_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
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
        <button class="confirm" id="selectRules"><span class="ui-icon icon-confirm"></span><span class="button-text"><@s.text name="global.page.ok" /></span></button>
   </div>
   <span id ="ruleSearchUrl" style="display:none">${s_ruleSearchUrl}</span>
   <#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</div>