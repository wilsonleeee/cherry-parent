<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id="employeeDialog" class="hide">
    <input type="text" class="text" onKeyup ="datatableFilter(this,0);"/>
    <a class="search"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="employee_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.employeeCode"/></th><%--员工代号 --%>
               <th><s:text name="global.page.Employee"/></th><%--员工名称 --%>
               <th><s:text name="global.page.Employeedepart"/></th><%--部门 --%>
               <th><s:text name="global.page.employeePost"/></th><%--岗位 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="employeeConfirm" onclick="selectEmployee();"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLCM02_popEmployee" namespace="/common" id="popEmployee" />
<span id ="popEmployeeUrl" style="display:none">${popEmployee}</span>
<span id ="EmployeeTitle" style="display:none"><s:text name="global.page.EmployeeTitle"/></span><%--员工信息 --%>
