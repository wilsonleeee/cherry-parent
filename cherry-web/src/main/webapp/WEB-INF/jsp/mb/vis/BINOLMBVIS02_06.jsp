<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<table id="memSearchDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
    <thead>
         <tr>
            <th><s:text name="global.page.number"></s:text></th>
            <th><s:text name="global.page.memCode"></s:text></th>
            <th><s:text name="global.page.memName"></s:text></th>
            <th><s:text name="global.page.mobilePhone"></s:text></th>
            <th><s:text name="global.page.joinDate"></s:text></th>
            <th><s:text name="global.page.birthDay"></s:text></th>
            <th><s:text name="global.page.visCounter"></s:text></th>
         </tr>
     </thead>
     <tbody>
     </tbody>
</table>

<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />