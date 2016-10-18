<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<s:url id="s_prtSearchUrl" value="/common/BINOLCM02_getProductInfo" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id ="productDialog" class="dialog hide">

<input type="text" class="text" value="" id="productDialogSearch" onKeyup ="datatableFilter(this);" maxlength="50"/>
    <a class="search" onclick="datatableFilter(document.getElementById('productDialogSearch'))"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  <hr class="space" />
  
  <table id="prt_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th>         <%-- 选择 --%>
               <th><s:text name="global.page.prtvendorcode"/></th>     <%--厂商编码--%>
               <th><s:text name="global.page.barcode"/></th>           <%--产品条码--%>
               <th><s:text name="global.page.productname"/></th>       <%-- 产品名称--%>
               <th><s:text name="global.page.classification"/></th>    <%-- 大分类--%>
               <th><s:text name="global.page.smallclassification"/></th><%-- 小分类--%>
               <th><s:text name="global.page.salePrice"/></th>          <%-- 销售价格--%>
               <th><s:text name="global.page.standardCost"/></th>       <%-- 结算价格--%>
            </tr>
        </thead>
        <tbody id="dataTableBody" >
        </tbody>
   </table>
   
    <hr>
   <div class="center clearfix">
        <button class="confirm" id="selectProducts" onclick="selectProduct();"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
   <span id ="prtSearchUrl" style="display:none">${s_prtSearchUrl}</span>
   <span id ="PopProTitle" style="display:none"><s:text name="global.page.PopProTitle"/></span><%--产品信息 --%>
</div>