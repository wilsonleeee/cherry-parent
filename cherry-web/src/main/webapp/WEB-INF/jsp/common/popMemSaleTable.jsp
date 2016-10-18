<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<table id="memSaleListDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
    <thead>
         <tr>
            <th><s:text name="global.page.number"></s:text></th>
            <th><s:text name="global.page.billCode"></s:text></th>
            <th><s:text name="global.page.eventDate"></s:text></th>
            <th><s:text name="global.page.countercode"></s:text></th>
            <th><s:text name="global.page.amount"></s:text></th>
            <th><s:text name="global.page.quantity"></s:text></th>
         </tr>
     </thead>
     <tbody>
     </tbody>
</table>

<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />


<div class="hide">
<div id="dialogTitle"><s:text name="global.page.dialogTitle" /></div>
<div id="dialogClose"><s:text name="global.page.dialogClose" /></div>
<s:url action="BINOLCM35_searchMemSaleList" id="searchMemSaleListUrl"></s:url>
<a href="${searchMemSaleListUrl }" id="searchMemSaleListUrl"></a>
</div>