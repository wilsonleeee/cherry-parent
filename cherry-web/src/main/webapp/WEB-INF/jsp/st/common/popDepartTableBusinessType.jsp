<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/st/common/popDepartTableBusinessType.js"></script>
<div id="_departDialog_main" class="hide">
<div id="_departDialog" class="hide">
    <div id="_departDialog_errorDisplay"></div>
    <input type="text" class="text" onKeyup ="datatableFilter(this,30);" maxlength="50" id="inputString" value=""/>
    <a class="search" onclick="datatableFilter('#inputString',30);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="_departDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.departcode"/></th><%--代码--%>
               <th><s:text name="global.page.departname"/></th><%--名称 --%>
               <th><s:text name="global.page.parttype"/></th><%--类型 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="_departConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
</div>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="StIndex_getDepartInfo" namespace="/st" id="_popDepart" />
<span id ="_popDepartUrl" style="display:none">${_popDepart}</span>
<span id ="PopdepartTitle" style="display:none"><s:text name="global.page.PopdepartTitle"/></span><%--部门信息 --%>
<div id ="_departDialog_error" style="display:none"><div class="actionError"><ul><li><s:text name="global.page.selectdepart"/></li></ul></div></div>