<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>



<s:i18n name="i18n.bs.BINOLBSPOS01">

<div class="clearfix">
  <div class="section">
    <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="base_title"></s:text></strong>
    </div>
    <div class="section-content">
      <table class="detail" cellpadding="0" cellspacing="0">
        <tr>
          <th><s:text name="employee.code"/></th><%-- 雇员代号 --%>
          <td><span><s:property value="employeeInfo.employeeCode"/></span></td>
          <th><s:text name="employee.name"/></th><%-- 姓名 --%>
          <td><span><s:property value='employeeInfo.employeeName'/></span></td>
        </tr>
        <tr>
        	<th><s:text name="employee.gender"/></th><%-- 性别 --%>
          <td><span><s:property value='#application.CodeTable.getVal("1006", employeeInfo.gender)'/></span></td>
          <th><s:text name="employee.foreignName"/></th><%-- 外文名 --%>
          <td><span><s:property value='employeeInfo.employeeNameForeign'/></span></td>
        </tr>
        <tr>
          <th><s:text name="employee.academic"/></th><%-- 学历 --%>
          <td><span><s:property value='#application.CodeTable.getVal("1042", employeeInfo.academic)'/></span></td>
          <th><s:text name="employee.maritalStatus"/></th><%-- 婚姻状况 --%>
          <td><span><s:property value='#application.CodeTable.getVal("1043", employeeInfo.maritalStatus)'/></span></td>
        </tr>
        <tr>
         	<th><s:text name="employee.identityCard"/></th><%-- 身份证号 --%>
          <td><span><s:property value='employeeInfo.identityCard'/></span></td>
          <th><s:text name="employee.birthDay"/></th><%-- 生日 --%>
          <td><span><s:property value='employeeInfo.birthDay'/></span></td>
        </tr>
        <tr>
        	<th><s:text name="employee.phone"/></th><%-- 联系电话 --%>
          <td><span><s:property value='employeeInfo.phone'/></span></td>
          <th><s:text name="employee.mobilePhone"/></th><%-- 手机 --%>
          <td><span><s:property value='employeeInfo.mobilePhone'/></span></td>
        </tr>
        <tr>
          <th><s:text name="employee.email"/></th><%-- 电子邮箱 --%>
          <td><span><s:property value='employeeInfo.email'/></span></td>
          <th><s:text name="employee.validFlag"/></th><%-- 有效状态 --%>
          <td><span><s:property value='#application.CodeTable.getVal("1052", employeeInfo.validFlag)'/></span></td>
        </tr>
      </table>      
    </div>
  </div>  
  
  <div class="section">
  <div class="section-header clearfix">
    <strong class="left"><span class="ui-icon icon-ttl-section-info"></span>管辖柜台</strong>
    <%-- 
    <a class="add right" onclick="bspos04_addEmployee();return false;"><span class="ui-icon icon-add"></span><span class="button-text">添加柜台</span></a>
  	--%>
  </div>
  <div class="section-content">
  <div style="overflow-x:auto;overflow-y:hidden">
  <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <thead>
    <tr>
      <th class="tableheader" >柜台号</th>
      <th class="tableheader" >柜台名称</th> 
      <th class="tableheader" >柜台状态</th>
      <th class="tableheader" >柜台类别</th> 
      <th class="tableheader" >柜台等级</th> 
    </tr>
    </thead>
    <tbody id="priceDialogBody">
      <s:iterator value="%{employeeInfo.counterList}" id="counterMap">
      <tr>
      <td>${counterMap.counterCode }</td>
      <td>${counterMap.counterNameIF }</td>
      <td><s:property value='#application.CodeTable.getVal("1030", counterInfo.status)' /></td>
      <td><s:property value='#application.CodeTable.getVal("1031", counterInfo.counterKind)' /></td>
      <td><s:property value='#application.CodeTable.getVal("1032", counterInfo.counterLevel)' /></td>
      </tr>
      </s:iterator>
    </tbody>
  </table>
  </div>
  </div>
</div> 

</div>
</s:i18n>