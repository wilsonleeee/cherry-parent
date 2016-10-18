<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<input type="text" class="text" onKeyup ="datatableFilter(this,71);" id="searchKey"/>
<a class="search" onclick="datatableFilter('#searchKey',71);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
<hr class="space" />
<table id="searchSaleRecordDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
    <thead>
         <tr>
            <th><s:text name="global.page.Popselect"></s:text></th>
            <th><s:text name="global.page.billCode"></s:text></th>
            <th><s:text name="global.page.saleType"></s:text></th>
            <th><s:text name="global.page.saleCounter"/></th>
            <th><s:text name="global.page.saleTime"/></th>
            <th><s:text name="global.page.quantity"/></th>
            <th><s:text name="global.page.amount"/></th>
         </tr>
     </thead>
     <tbody>
     </tbody>
</table>

<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<div class="hide">
<div id="searchSaleRecordTitle"><s:text name="global.page.selectSaleRecord" /></div>
<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<s:url action="BINOLCM02_popSaleRecordDialog" id="searchSaleRecordListUrl"></s:url>
<a href="${searchSaleRecordListUrl }" id="searchSaleRecordListUrl"></a>
</div>