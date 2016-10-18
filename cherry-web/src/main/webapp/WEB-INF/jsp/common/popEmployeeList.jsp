<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<input type="text" class="text" onKeyup ="datatableFilter(this,30);" id="searchKey"/>
<a class="search" onclick="datatableFilter('#searchKey',30);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
<span class="gray" style="margin-left:5px;"><s:text name="global.page.directions"/></span>
<hr class="space" />
<table id="searchEmployeeDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
    <thead>
         <tr>
            <th><s:text name="global.page.Popselect"></s:text></th>
            <th><s:text name="global.page.employeeCode"></s:text></th>
            <th><s:text name="global.page.Employee"></s:text></th>
            <th><s:text name="global.page.Employeedepart"/></th>
         </tr>
     </thead>
     <tbody>
     </tbody>
</table>

<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<div class="hide">
<div id="searchEmployeeTitle"><s:text name="global.page.selectEmployee" /></div>
<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<s:url action="BINOLCM02_popEmployeeDialog" id="searchEmployeeListUrl"></s:url>
<a href="${searchEmployeeListUrl }" id="searchEmployeeListUrl"></a>
</div>