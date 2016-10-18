<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<@s.url id="s_cateSearchUrl" value="/common/BINOLCM02_getCateInfo" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id ="cateDialog" class="dialog hide">
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<input type="text" class="text" value="" id="cateDialogSearch" onKeyup ="datatableFilter(this);" maxlength="50"/>
    <a class="search" onClick="datatableFilter(document.getElementById('cateDialogSearch'))"><span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.find" /></span></a>
  <hr class="space" />
   <table id="cate_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
   		<thead>
            <tr>
               <th><@s.text name="cp.choice" /></th>
               <th><@s.text name="cp.cateCode" /></th>
               <th><@s.text name="cp.cateType" /></th>
               <th><@s.text name="cp.cateName" /></th>
            </tr>
           </thead>
        <tbody id="dataTableBody" >
        </tbody>
   </table>
   
    <hr>
   <div class="center clearfix">
        <button class="confirm" id="selectCates" onClick="selectProduct();"><span class="ui-icon icon-confirm"></span><span class="button-text"><@s.text name="cp.sure" /></span></button>
   </div>
   <span id ="cateSearchUrl" style="display:none">${s_cateSearchUrl}</span>
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>