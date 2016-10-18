<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="employeeDialog" class="hide">
	<div id="actionResultDisplay_pop"></div>
	<div id="errorDiv_pop" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan_pop"></span></li>
        </ul>         
    </div>
    <input id="employeeText" type="text" class="text" onKeyup ="datatableFilter(this,4);"/>
    <a class="search" onclick="datatableFilter('#employeeText',4);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="employee_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.departname"/></th><%--姓名 --%>
               <th><s:text name="global.page.Employeedepart"/></th><%--部门 --%>
               <th><s:text name="global.page.employeePost"/></th><%--岗位 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="employeeConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
   <input type="hidden" value="" id="_udiskInfoId"/>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<s:url action="popEmployee" id="popEmployee"/>
<span id ="popEmployeeUrl" style="display:none">${popEmployee}</span>