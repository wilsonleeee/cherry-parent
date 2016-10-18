<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM05.js"></script>

<div id="lowerCounterDialog" class="hide">
    <input id="lowerCounterText" type="text" class="text" onKeyup ="datatableFilter(this,1);"/>
    <a class="search" onclick="datatableFilter('#lowerCounterText',1);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="lowerCounter_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
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
        <button class="confirm" id="lowerCounterConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
</div>
<div id="departEmpDialog" class="hide">
    <input id="departEmpText" type="text" class="text" onKeyup ="datatableFilter(this,2);"/>
    <a class="search" onclick="datatableFilter('#departEmpText',2);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="departEmp_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.Employeename"/></th><%--姓名 --%>
               <th><s:text name="global.page.telnumber"/></th><%--电话--%>
               <th><s:text name="global.page.phone"/></th><%--手机 --%>
               <th><s:text name="global.page.email"/></th><%--邮箱 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="departEmpConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
</div>
<div id="higherDepartDialog" class="hide">
    <input id="higherDepartText" type="text" class="text" onKeyup ="datatableFilter(this,3);"/>
    <a class="search" onclick="datatableFilter('#higherDepartText',3);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="higherDepart_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
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
        <button class="confirm" id="higherDepartConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLBSCOM01_popLowerCounter" id="popLowerCounter" />
<span id ="popLowerCounterUrl" style="display:none">${popLowerCounter}</span>
<s:url action="BINOLBSCOM01_popDepartEmp" id="popDepartEmp" />
<span id ="popDepartEmpUrl" style="display:none">${popDepartEmp}</span>
<s:url action="BINOLBSCOM01_popHigherDepart" id="popHigherDepart" />
<span id ="popHigherDepartUrl" style="display:none">${popHigherDepart}</span>
<span id ="EmployeeTitle" style="display:none"><s:text name="global.page.EmployeeTitle"/></span><%--员工信息 --%>
<span id ="PopdepartTitle" style="display:none"><s:text name="global.page.PopdepartTitle"/></span><%--部门信息 --%>