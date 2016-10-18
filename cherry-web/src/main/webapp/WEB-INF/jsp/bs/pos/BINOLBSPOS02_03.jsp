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
          <th><s:text name="positionName"></s:text></th>
          <td><span>${counterInfo.counterCode}</span></td>
          <th><s:text name="categoryName"></s:text></th>
          <td><span>${counterInfo.counterNameIF }</span></td>
        </tr>
        <tr>
          <th><s:text name="positionName"></s:text></th>
          <td><span><s:property value='#application.CodeTable.getVal("1031", counterInfo.counterKind)' /></span></td>
          <th><s:text name="categoryName"></s:text></th>
          <td><span><s:property value='#application.CodeTable.getVal("1032", counterInfo.counterLevel)' /></span></td>
        </tr>
        <tr>
          <th><s:text name="positionName"></s:text></th>
          <td><span><s:property value='#application.CodeTable.getVal("1030", counterInfo.status)' /></span></td> 
        </tr>
      </table>      
    </div>
  </div>  
  
  <%-- 
  <div class="section">
  <div class="section-header clearfix">
    <strong class="left"><span class="ui-icon icon-ttl-section-info"></span>管辖柜台</strong>
    <a class="add right" onclick="bspos04_addEmployee();return false;"><span class="ui-icon icon-add"></span><span class="button-text">添加柜台</span></a>
  </div>
  <div class="section-content">
  <div style="overflow-x:auto;overflow-y:hidden">
  <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <thead>
    <tr>
      <th class="tableheader" width="40%">员工编号</th>
      <th class="tableheader" width="60%">员工姓名</th> 
    </tr>
    </thead>
    <tbody id="priceDialogBody">
      <s:iterator value="%{employeeInfo.counterList}" id="counterMap">
      <tr>
      <td>
      ${counterMap.counterInfoId }
      </td>
      <td>
      ${counterMap.counterNameIF }
      </td>
      </tr>
      </s:iterator>
    </tbody>
  </table>
  </div>
  </div>--%>
</div> 

</div>
</s:i18n>