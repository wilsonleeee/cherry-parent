<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<@s.url id="s_prtSearchUrl" value="/common/BINOLCM02_getProductInfo" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id ="productDialog" class="dialog hide">
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<input type="text" class="text" value="" id="productDialogSearch" onKeyup ="datatableFilter(this);" maxlength="50"/>
    <a class="search" onClick="datatableFilter(document.getElementById('productDialogSearch'))"><span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="global.page.searchfor" /></span></a>
  <hr class="space" />
   <table id="prt_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
   		<thead>
            <tr>
               <th><@s.text name="global.page.Popselect" /></th>
               <th><@s.text name="global.page.prtvendorcode" /></th>
               <th><@s.text name="global.page.barcode" /></th>
               <th><@s.text name="global.page.productname" /></th>
               <th><@s.text name="global.page.classification" /></th>
               <th><@s.text name="global.page.smallclassification" /></th>
               <th><@s.text name="global.page.salePrice" /></th>
               <th><@s.text name="global.page.standardCost"/></th>
            </tr>
           </thead>
        <tbody id="dataTableBody" >
        </tbody>
   </table>
   
    <hr>
   <div class="center clearfix">
        <button class="confirm" id="selectProducts" onClick="selectProduct();"><span class="ui-icon icon-confirm"></span><span class="button-text"><@s.text name="global.page.ok" /></span></button>
   </div>
   <span id ="prtSearchUrl" style="display:none">${s_prtSearchUrl}</span>
   <span id ="PopProTitle" style="display:none"><@s.text name="global.page.PopProTitle"/></span><#--产品信息 -->
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>
