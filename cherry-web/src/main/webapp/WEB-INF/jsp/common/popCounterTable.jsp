<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>

<div id ="counterDialog" class="hide">
    <input type="text" class="text" onKeyup ="datatableFilter(this,9);" id="counterKw"/>
    <a class="search" onclick="datatableFilter('#counterKw',9);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="counter_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.countercode"/></th><%--柜台号 --%>
               <th><s:text name="global.page.countername"/></th><%--柜台名称 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>

</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLCM02_getCounterList" namespace="/common" id="getCounterListUrl" />
<span id ="PopcounterTitle" style="display:none"><s:text name="global.page.PopcounterTitle"/></span><%--柜台信息 --%>
<span id ="getCounterListUrl" style="display:none">${getCounterListUrl}</span>
<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>