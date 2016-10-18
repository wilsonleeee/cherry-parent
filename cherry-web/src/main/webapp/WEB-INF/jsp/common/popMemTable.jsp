<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<table id="memListDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
    <thead>
         <tr>
            <th><s:text name="global.page.number"></s:text></th>
            <th><s:text name="global.page.memName"></s:text></th>
            <th><s:text name="global.page.memCode"></s:text></th>
            <th><s:text name="global.page.joinDate"></s:text></th>
            <th><s:text name="global.page.levelName"></s:text></th>
            <th><s:text name="global.page.mobilePhone"></s:text></th>
         </tr>
     </thead>
     <tbody>
     </tbody>
</table>

<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />


<div class="hide">
<div id="dialogTitle"><s:text name="global.page.dialogTitle" /></div>
<div id="dialogClose"><s:text name="global.page.dialogClose" /></div>
<s:url action="BINOLCM35_searchMemList" id="searchMemListUrl"></s:url>
<a href="${searchMemListUrl }" id="searchMemListUrl"></a>
</div>