<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<s:url id="rpt_cateSearchUrl" value="/mb/BINOLMBRPT12_popCateDialog" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="">
<div id ="rptCateDialog" class="dialog hide">
  	<hr class="space" />
  	<table id="rpt_prtCate_Table" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th>
				选择
               </th>         <%-- 选择 --%>
               <th><s:text name="global.page.classification"/></th>    
               <th>分类代码</th>         
               <th>销售数量</th>
            </tr>
        </thead>
        <tbody id="dataTableBody"></tbody>
   	</table>
   	<div class="hide" id="rptCateDialog_temp"></div>
   	<span id ="rptCateSearchUrl" style="display:none">${rpt_cateSearchUrl}</span>
   	<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>