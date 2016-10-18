<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<s:url id="s_prmSearchUrl" value="/common/BINOLCM07_getPromotionInfo" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id ="promotionDialog" class="dialog hide">

    <input type="text" class="text" value="" id="promotionDialogSearch" onKeyup ="datatableFilter(this,0);"/>
    <a class="search"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>

  <hr class="space" />
  
    <table id="prm_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.prmvendorcode"/></th><%--促销产品厂商编码 --%>
               <th><s:text name="global.page.prmbarcode"/></th><%--促销产品条码 --%>
               <th><s:text name="global.page.prmproductname"/></th><%--促销产品名称 --%>
               <th><s:text name="global.page.prmproductprice"/></th><%--促销产品价格--%>
            </tr>
        </thead>
        <tbody id="prm_dataTableBody" >
        </tbody>
   </table>
   
   <div class="center clearfix">
        <button class="confirm" id="selectPromotion" onclick="selectPromotion();"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
   
</div>

<span id ="prmSearchUrl" style="display:none">${s_prmSearchUrl}</span>
<span id ="PopvendorTitle" style="display:none"><s:text name="global.page.PopvendorTitle"/></span><%--厂商信息 --%>