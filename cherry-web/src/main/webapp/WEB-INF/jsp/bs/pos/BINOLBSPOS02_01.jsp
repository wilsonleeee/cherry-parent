<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>




<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>


<s:i18n name="i18n.bs.BINOLBSPOS01">

<div class="clearfix">
  <div class="section">
    <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="base_title"></s:text></strong>
    </div>
    <div class="section-content">
      <table class="detail" cellpadding="0" cellspacing="0">
        <caption>
     		<s:text name="brandInfo"></s:text>：
     		<s:if test="%{positionInfo.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
		    <s:else>${positionInfo.brandNameChinese }</s:else>&nbsp;&nbsp;
		    <s:text name="organizationId"></s:text>：${positionInfo.departName}&nbsp;&nbsp;
		    <s:if test='%{positionInfo.higherPositionName != null && positionInfo.higherPositionName != ""}'>
     		<s:text name="directlyHigherPos"></s:text>：${positionInfo.higherPositionName }
     		</s:if>
    	</caption>
        <tr>
          <th><s:text name="positionName"></s:text></th>
          <td><span>${positionInfo.positionName}</span></td>
          <th><s:text name="categoryName"></s:text></th>
          <td><span>${positionInfo.categoryName }</span></td>
        </tr>
        <tr>
          <th><s:text name="positionNameForeign"></s:text></th>
          <td><span>${positionInfo.positionNameForeign }</span></td>
          <th><s:text name="isManager"></s:text></th>
          <td><span><s:property value='#application.CodeTable.getVal("1038", positionInfo.isManager)' /></span></td> 
        </tr>
        <tr>
          <th><s:text name="positionDESC"></s:text></th>
          <td><span>${positionInfo.positionDESC}</span></td>
          <th><s:text name="foundationDate"></s:text></th>
          <td><span>${positionInfo.foundationDate}</span></td>
        </tr>
        <tr>
          <th><s:text name="positionType"></s:text></th>
          <td><span><s:property value='#application.CodeTable.getVal("1039", positionInfo.positionType)' /></span></td>
          <s:if test='positionInfo.positionType == "1" || positionInfo.positionType == "2"'>
          <th><s:text name="resellerName"></s:text></th>
          <td><span>${positionInfo.resellerName}</span></td>
          </s:if>
        </tr>
      </table>      
    </div>
  </div>  
  
  <s:if test="%{positionInfo.employeeList != null && !positionInfo.employeeList.isEmpty()}">
  <div class="section">
  <div class="section-header clearfix">
    <strong class="left"><span class="ui-icon icon-ttl-section-info"></span>员工信息</strong>
  </div>
  <div class="section-content">
  	<s:iterator value="%{positionInfo.employeeList}" id="employeeInfo">  
  	  <table class="detail" cellpadding="0" cellspacing="0">
        <caption>
     		${employeeInfo.employeeName }
    	</caption>
        <tr>
          <th><s:text name="employee.code"/></th><%-- 雇员代号 --%>
          <td><span><s:property value="#employeeInfo.employeeCode"/></span></td>
          <th><s:text name="employee.name"/></th><%-- 姓名 --%>
          <td><span><s:property value='#employeeInfo.employeeName'/></span></td>
        </tr>
        <tr>
        	<th><s:text name="employee.gender"/></th><%-- 性别 --%>
          <td><span><s:property value='#application.CodeTable.getVal("1006", #employeeInfo.gender)'/></span></td>
          <th><s:text name="employee.foreignName"/></th><%-- 外文名 --%>
          <td><span><s:property value='#employeeInfo.employeeNameForeign'/></span></td>
        </tr>
        <tr>
          <th><s:text name="employee.academic"/></th><%-- 学历 --%>
          <td><span><s:property value='#application.CodeTable.getVal("1042", #employeeInfo.academic)'/></span></td>
          <th><s:text name="employee.maritalStatus"/></th><%-- 婚姻状况 --%>
          <td><span><s:property value='#application.CodeTable.getVal("1043", #employeeInfo.maritalStatus)'/></span></td>
        </tr>
        <tr>
         	<th><s:text name="employee.identityCard"/></th><%-- 身份证号 --%>
          <td><span><s:property value='#employeeInfo.identityCard'/></span></td>
          <th><s:text name="employee.birthDay"/></th><%-- 生日 --%>
          <td><span><s:property value='#employeeInfo.birthDay'/></span></td>
        </tr>
        <tr>
        	<th><s:text name="employee.phone"/></th><%-- 联系电话 --%>
          <td><span><s:property value='#employeeInfo.phone'/></span></td>
          <th><s:text name="employee.mobilePhone"/></th><%-- 手机 --%>
          <td><span><s:property value='#employeeInfo.mobilePhone'/></span></td>
        </tr>
        <tr>
          <th><s:text name="employee.email"/></th><%-- 电子邮箱 --%>
          <td><span><s:property value='#employeeInfo.email'/></span></td>
          <th><s:text name="employee.validFlag"/></th><%-- 有效状态 --%>
          <td><span><s:property value='#application.CodeTable.getVal("1052", #employeeInfo.validFlag)'/></span></td>
        </tr>
      </table> 
    </s:iterator>       
  </div>
</div> 
</s:if>

<hr class="space" />
<s:url action="BINOLBSPOS03_init" id="updatePositionUrl">
<s:param name="positionId" value="%{positionInfo.positionId}"></s:param>
</s:url>
<a href="${updatePositionUrl }" id="updatePositionUrl" style="display: none;"></a>
<div class="center">
  <cherry:show domId="BSPOS0101UPDPO">
  <button class="save" onclick="openWin('#updatePositionUrl');return false;"><span class="ui-icon icon-edit-big"></span><span class="button-text"><s:text name="edit_button"></s:text></span></button> <%-- <button class="delete"><span class="ui-icon icon-delete-big2"></span><span class="button-text">停用</span></button>--%>
  </cherry:show>
</div>
 
</div>
</s:i18n>