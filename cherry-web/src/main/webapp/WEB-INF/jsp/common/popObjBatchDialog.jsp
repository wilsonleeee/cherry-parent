<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<s:url id="objBatch_Url" value="/common/BINOLCM02_popObjBatchDialog" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="">
<div id ="objBatchDialog" class="dialog hide">
	<input type="text" class="text" value="" id="objBatchDialogSearch" 
		onKeyup ="datatableFilter(this,43);" maxlength="50"/>
    <a class="search" onclick="datatableFilter('#memberDialogSearch',43)">
    	<span class="ui-icon icon-search"></span>
    	<span class="button-text"><s:text name="global.page.searchfor"/></span>
    </a>
  	<hr class="space" />
  	<table id="objBatch_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th>       <%-- 选择 --%>
               <th><s:text name="global.page.ObjBatchNum"/></th>     <%--批次号--%>
               <th><s:text name="global.page.ObjBatchName"/></th>   <%--批次名称--%>
               <th><s:text name="global.page.ObjBatchComments"/></th>       <%--备注--%>
            </tr>
        </thead>
        <tbody id="objBatch_dataTableBody"></tbody>
   	</table>
   	<table class="hide"><tbody id="objBatchDialog_temp"></tbody></table>
   	<span id ="objBatchUrl" style="display:none">${objBatch_Url}</span>
   	<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
   	<span id ="PopObjBatchTitle" style="display:none"><s:text name="global.page.PopObjBatchTitle"/></span><%--对象批次信息 --%>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>