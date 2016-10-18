<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/bs/dep/BINOLBSPOS99.js"></script>
<div id="employeeDialog" class="hide">
    <input type="text" class="text" onKeyup ="datatableFilter(this,1);"/>
    <a class="search"><span class="ui-icon icon-search"></span><span class="button-text">查找</span></a>
  	<hr class="space" />
    <table id="employee_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th>选择</th>
               <th>员工姓名</th>
               <th>部门</th>
               <th>岗位</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="employeeConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text">确定</span></button>
   </div>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLBSPOS99_popEmployee" id="popEmployee" />
<span id ="popEmployeeUrl" style="display:none">${popEmployee}</span>
