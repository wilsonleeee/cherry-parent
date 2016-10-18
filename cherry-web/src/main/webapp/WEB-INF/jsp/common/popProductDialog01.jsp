<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="">

<div id ="productDialogOne" class="dialog hide">
	<input type="text" class="text" value="" id="productDialogOneSearch"  onKeyup ="datatableFilter(this,220);" maxlength="50"/>
    <a class="search" onclick="datatableFilter('#productDialogOneSearch',220)">
    	<span class="ui-icon icon-search"></span>
    	<span class="button-text"><s:text name="global.page.searchfor"/></span>
    </a>
  	<hr class="space" />
  	<table id="prtOne_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th>
               <s:if test='%{checkType == null || checkType == "checkbox"}'>
               		<input type="checkbox" id="prt_checkAll1"/>
               </s:if>
               	<label for="prt_checkAll1"><s:text name="global.page.Popselect"/></label>
               </th>         <%-- 选择 --%>
               <th><s:text name="global.page.barcode"/></th>           <%--产品条码--%>
               <th><s:text name="global.page.originalBrand"/></th>       <%-- 产品名称--%>
               <th><s:text name="global.page.productname"/></th>       <%-- 产品名称--%>
               <th><s:text name="global.page.classification"/></th>    <%-- 大分类--%>
               <th><s:text name="global.page.smallclassification"/></th><%-- 小分类--%>
               <th><s:text name="global.page.salePrice"/></th>          <%-- 销售价格--%>
               <th><s:text name="global.page.memPrice"/></th>          <%-- 会员价格--%>
               <th><s:text name="global.page.standardCost"/></th>       <%-- 结算价格--%>
               <th><s:text name="global.page.orderPrice"/></th>       	<%-- 采购价格--%>
               <th><s:text name="global.page.platinumPrice"/></th>       <%-- 白金价格--%>
               <th><s:text name="global.page.tagPrice"/></th>       	<%-- 吊牌价格--%>
               <th><s:text name="global.page.validFlag"/></th>       	<%-- 有效区分--%>
            </tr>
        </thead>
        <tbody id="dataTableBody"></tbody>
   	</table>
   	<div class="hide" id="productDialogOne_temp"></div>
	<s:url id="s_prtSearchOneUrl" value="/common/BINOLCM02_popPrtDialogOne" />
   	<span id ="prtSearchOneUrl" style="display:none">${s_prtSearchOneUrl}</span>
   	<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
   	<span id ="PopProTitle" style="display:none"><s:text name="global.page.PopProTitle"/></span><%--产品信息 --%>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>