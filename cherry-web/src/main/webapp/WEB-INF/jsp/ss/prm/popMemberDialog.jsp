<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<input type="text" class="text" onKeyup ="datatableFilter(this,191);" id="searchKey"/>
<a class="search" onclick="datatableFilter('#searchKey',191);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
<hr class="space" />
<table id="memberDialogDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
    <thead>
         <tr>
            <th>No.</th>
            <th>会员卡号</th>
            <th>手机号码</th>
         </tr>
     </thead>
     <tbody>
     </tbody>
</table>

<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<div class="hide">
<s:url id="memberDialog_Url" action="BINOLSSPRM73_memberDialog"/>
<a id="memberDialogUrl" href="${memberDialog_Url}"></a>
</div>