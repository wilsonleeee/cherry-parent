<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
	<s:url id="s_prmSearchUrl" value="/common/BINOLCM02_getPromotionInfo" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id ="promotionDialog" class="dialog hide">

    <input type="text" class="text" value="" id="promotionDialogSearch" onKeyup ="datatableFilter(this);" maxlength="50"/>
    <a class="search" onclick="datatableFilter(document.getElementById('promotionDialogSearch'))"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>

  <hr class="space" />
  
    <div class="ui-tabs"> 
    <ul class ="ui-tabs-nav clearfix" id="promotionCateTitle">
	    <s:iterator value='#application.CodeTable.getCodes("1139")' id="promotionCateInfo" status="status">
	    	<li id='<s:property value="#promotionCateInfo.CodeKey"/>' type="<s:property value='#application.CodeTable.getVal("1139",#promotionCateInfo.CodeKey)' />" class='<s:if test="%{#status.index == 0}">ui-tabs-selected</s:if>' onclick="promotionCateFilter(this);"><a><s:property value="#promotionCateInfo.Value"/></a></li>
	    </s:iterator>
 	</ul>
	<div class="ui-tabs-panel">
    <table id="prm_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.prmvendorcode"/></th><%--促销产品厂商编码--%>
               <th><s:text name="global.page.prmbarcode"/></th><%--促销产品条码--%>
               <th><s:text name="global.page.prmproductname"/></th><%--促销产品名称 --%>
               <th><s:text name="global.page.classification"/></th><%--大分类--%>
               <th><s:text name="global.page.inclassification"/></th><%--中分类 --%>
               <th><s:text name="global.page.smallclassification"/></th><%--小分类 --%>
               <th><s:text name="global.page.standardCost"/></th><%--成本价格--%>
            </tr>
        </thead>
        <tbody id="prm_dataTableBody" >
        </tbody>
   </table>
   </div>
   </div>
   
   <hr>
   <div class="center clearfix">
        <button class="confirm" id="selectPromotion" onclick="selectPromotion();"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
   <span id ="prmSearchUrl" style="display:none">${s_prmSearchUrl}</span>
   <span id ="PopPrmTitle" style="display:none"><s:text name="global.page.PopPrmTitle"/></span><%--促销产品信息 --%>

</div>

