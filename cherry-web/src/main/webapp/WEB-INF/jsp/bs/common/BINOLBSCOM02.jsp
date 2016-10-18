<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM04.js"></script>
<div id="employeeDialog" class="hide">
    <input id="employeeText" type="text" class="text" onKeyup ="datatableFilter(this,1);"/>
    <a class="search" onclick="datatableFilter('#employeeText',1);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
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
</div>
<div id="likeEmployeeDialog" class="hide">
    <input id="likeEmployeeText" type="text" class="text" onKeyup ="datatableFilter(this,2);"/>
    <a class="search" onclick="datatableFilter('#likeEmployeeText',2);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="likeEmployee_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
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
        <button class="confirm" id="likeEmployeeConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
</div>
<div id="higherOrgDialog" class="hide">
    <input id="higherOrgText" type="text" class="text" onKeyup ="datatableFilter(this,3);"/>
    <a class="search" onclick="datatableFilter('#higherOrgText',3);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="higherOrg_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.departcode"/></th><%--代码 --%>
               <th><s:text name="global.page.departname"/></th><%--名称 --%>
               <th><s:text name="global.page.parttype"/></th><%--类型 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="higherOrgConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
   <span id ="EmployeeTitle" style="display:none"><s:text name="global.page.EmployeeTitle"/></span><%--员工信息 --%>
   <span id ="PopdepartTitle" style="display:none"><s:text name="global.page.PopdepartTitle"/></span><%--部门信息 --%>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLBSCOM01_popHigherOrg" id="popHigherOrg" />
<span id ="popHigherOrgUrl" style="display:none">${popHigherOrg}</span>
<s:url action="BINOLBSCOM01_popCounterEmp" id="popEmployee" />
<span id ="popEmployeeUrl" style="display:none">${popEmployee}</span>