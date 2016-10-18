<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<s:url id="rpt_prtSearchUrl" value="/mb/BINOLMBRPT12_popPrtDialog" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="">
<div id ="rptProductDialog" class="dialog hide">
  	<hr class="space" />
  	<table id="rpt_prt_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th>
				选择
               </th>         <%-- 选择 --%>
               <th><s:text name="global.page.prtvendorcode"/></th>     <%--厂商编码--%>
               <th><s:text name="global.page.barcode"/></th>           <%--产品条码--%>
               <th><s:text name="global.page.productname"/></th>       <%-- 产品名称--%>
               <th><s:text name="global.page.classification"/></th>    <%-- 大分类--%>
               <th>销售数量</th>
            </tr>
        </thead>
        <tbody id="dataTableBody"></tbody>
   	</table>
   	<div class="hide" id="rptProductDialog_temp"></div>
   	<span id ="rptPrtSearchUrl" style="display:none">${rpt_prtSearchUrl}</span>
   	<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
   	<span id ="PopProTitle" style="display:none"><s:text name="global.page.PopProTitle"/></span><%--产品信息 --%>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>