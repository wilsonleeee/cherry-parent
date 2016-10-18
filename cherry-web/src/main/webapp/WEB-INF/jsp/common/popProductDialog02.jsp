<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<s:url id="s_prtSearchUrl" value="/common/BINOLCM02_popPrtDialogTwo" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript">
function changValue(){
	if(document.getElementById("newProduct").checked){
		document.getElementById("newProductFlag").value="1";
	}else{
		document.getElementById("newProductFlag").value="0";
	}
	
}
</script>

<s:i18n name="i18n.st.BINOLSTSFH22">

<div id ="productDialog" class="dialog hide">
	<label><s:text name="SFH22_productBrandCode"/></label>
	<s:text id="selectAll" name="global.page.select"/>
	<s:select id="smallBrandCode" name="brandCodeSrh" list="brandCodeListSrh" listKey="OriginalBrand" listValue='#application.CodeTable.getVal("1299", OriginalBrand)' headerKey="" headerValue="请选择" /> 
    <label><s:text name="SFH22_productSort"/></label>
	<s:text id="sortListSrh" name="global.page.select"/>
	<s:select id="productList" name="sortListSrh" list="sortListSrh"  listKey="prtCatPropValueID" listValue="PropValueChinese" headerKey="" headerValue="请选择"/>
    <label><s:text name="SFH22_productName"/></label>
	<input type="text" class="text" value="" id="productDialogSearch"   maxlength="25"/><!-- onKeyup ="datatableFilter2(this,22);" -->
	<span>
	<input id="newProduct" type="checkbox"  name="newProduct" onclick="changValue()"/>
	<s:hidden name="newProductFlag" value="0"></s:hidden>
    <label><s:text name="SFH22_newProduct"/></label>
    </span>
	<s:if test="param!=null && param!=''">
		<input class="hide" name="originalBrand"  value="<s:property value='param'/>"/>
	</s:if>
    <a class="search" onclick="datatableFilter2('#productDialogSearch',25)">
    	<span class="ui-icon icon-search"></span>
    	<span class="button-text"><s:text name="global.page.searchfor"/></span>
    </a>
  	<hr class="space" />
  	<table id="prt_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th>
               <s:if test='%{checkType == null || checkType == "checkbox"}'>
               		<input type="checkbox" id="prt_checkAll"/>
               </s:if>
               	<label for="prt_checkAll"><s:text name="global.page.Popselect"/></label>
               </th>                                                    <%-- 选择 --%>
               <th><s:text name="global.page.prtvendorcode"/></th>      <%--厂商编码--%>
               <th><s:text name="global.page.barcode"/></th>            <%--产品条码--%>
               <th><s:text name="global.page.originalBrand"/></th>      <%-- 产品品牌--%>
               <th><s:text name="global.page.productname"/></th>        <%-- 产品名称--%>
               <th><s:text name="SFH22_stockAmount"/></th>                            <%-- 库存--%>
               <th><s:text name="global.page.classification"/></th>     <%-- 大分类--%>
               <th><s:text name="global.page.smallclassification"/></th> <%-- 小分类--%>
               <th><s:text name="SFH22_price"/></th>                         <%-- 销售价格--%>
            </tr>
        </thead>
        <tbody id="dataTableBody"></tbody>
   	</table>
   	<div class="hide" id="productDialog_temp"></div>
   	<span id ="prtSearchUrl" style="display:none">${s_prtSearchUrl}</span>
   	<span id ="global_page_addOrder" style="display:none"><s:text name="SFH22_joinOrder"/></span>
   	<span id ="global_page_close" style="display:none"><s:text name="SFH22_close"/></span>
   	<span id ="PopProTitle" style="display:none"><s:text name="SFH22_orderListModelSelectProduct"/></span><%--列表模式选择产品 --%>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>