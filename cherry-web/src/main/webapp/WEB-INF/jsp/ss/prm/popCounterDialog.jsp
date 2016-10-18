<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<input type="text" class="text" onKeyup ="datatableFilter(this,190);" id="searchKey"/>
<a class="search" onclick="datatableFilter('#searchKey',190);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
<hr class="space" />
<table id="counterDialogDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
    <thead>
         <tr>
            <th>No.</th>
            <th>门店号</th>
            <th>门店名称</th>
         </tr>
     </thead>
     <tbody>
     </tbody>
</table>

<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<div class="hide">
<s:url id="counterDialog_Url" action="BINOLSSPRM73_counterDialog"/>
<a id="counterDialogUrl" href="${counterDialog_Url}"></a>
</div>