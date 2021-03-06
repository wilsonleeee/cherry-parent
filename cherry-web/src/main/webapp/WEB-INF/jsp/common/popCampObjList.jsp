<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<input type="text" class="text" onKeyup ="datatableFilter(this,100);" id="searchKey"/>
<a class="search" onclick="datatableFilter('#searchKey',100);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
<hr class="space" />
<table id="searchCampObjDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
    <thead>
    <tr>
        <th><s:text name="global.page.Popselect"></s:text></th>
        <th><s:text name="global.page.campaignCode"></s:text></th>
        <th><s:text name="global.page.campaignName"></s:text></th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>

<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<div class="hide">
    <div id="dialogTitle"><s:text name="global.page.selectCampaign" /></div>
    <div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
    <div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
    <s:url action="BINOLCM02_popCampObjDialog" id="searchCampObjListUrl"></s:url>
    <a href="${searchCampObjListUrl }" id="searchCampObjListUrl"></a>
</div>