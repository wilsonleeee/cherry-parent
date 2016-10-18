<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>

<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/pos/BINOLBSPOS04.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/pos/BINOLBSPOS99.js"></script>



<s:i18n name="i18n.bs.BINOLBSPOS01">
<s:text name="select_default" id="select_default"></s:text>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <h2><s:text name="add_position"></s:text></h2>
</div>

<div id="actionResultDisplay"></div>

<cherry:form onsubmit="saveWithValid();return false;" id="addPositionInfo" csrftoken="false">
<div class="panel-content clearfix">
  <div class="section" id="positionBasic">
    <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="base_title"></s:text></strong>
    </div>
    <div class="section-content">
      <table class="detail" cellpadding="0" cellspacing="0">
        <caption>
     		<s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
     		<s:a action="BINOLBSPOS04_filter" cssStyle="display:none;" id="filterByBrandInfoUrl"></s:a>
     		<s:text name="brandInfo"></s:text>：<s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId" onchange="bspos04_changeBrand(this,'%{#select_default}');"></s:select>&nbsp;&nbsp;
     		</s:if>
     		<s:if test="%{orgList != null && !orgList.isEmpty()}">
     		<s:a action="BINOLBSPOS04_filterByOrg" cssStyle="display:none;" id="filterByOrgUrl"></s:a>
     		<s:text name="organizationId"></s:text>：<s:select list="orgList" name="organizationId" listKey="departId" listValue="departName" onchange="bspos04_changeOrg(this,'%{#select_default}');"></s:select>&nbsp;&nbsp;
     		</s:if>
     		<s:if test="%{higherPositionList != null && !higherPositionList.isEmpty()}">
     		<s:text name="directlyHigherPos"></s:text>：<s:select list="higherPositionList" name="path" listKey="path" listValue="positionName"></s:select>
    		</s:if>
    	</caption>
        <tr>
          <th><s:text name="positionName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td><span><s:textfield name="positionName" cssClass="text" maxlength="50"></s:textfield></span></td>
          <th><s:text name="categoryName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td><span><s:select list="positionCategoryList" name="positionCategoryId" listKey="positionCategoryId" listValue="ategoryName" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
        </tr>
        <tr>
          <th><s:text name="positionNameForeign"></s:text></th>
          <td><span><s:textfield name="positionNameForeign" cssClass="text" maxlength="50"></s:textfield></span></td>
          <th><s:text name="isManager"></s:text></th>
          <td><span><s:select list='#application.CodeTable.getCodes("1038")' listKey="CodeKey" listValue="Value" name="isManager" headerKey="" headerValue="%{#select_default}"></s:select></span></td> 
        </tr>
        <tr>
          <th><s:text name="positionDESC"></s:text></th>
          <td><span><s:textfield name="positionDESC" cssClass="text" maxlength="200"></s:textfield></span></td>
          <th><s:text name="foundationDate"></s:text></th>
          <td><span><s:textfield name="foundationDate" cssClass="text" cssStyle="width:80px;"></s:textfield></span></td>
        </tr>
        <tr>
          <th><s:text name="positionType"></s:text></th>
          <td><span><s:select list='#application.CodeTable.getCodes("1039")' listKey="CodeKey" listValue="Value" name="positionType" headerKey="" headerValue="%{#select_default}" onchange="changePosType();"></s:select></span></td>
          <th id="resellerInfoIdTh" style="display: none;"><s:text name="resellerName"></s:text></th>
          <td id="resellerInfoIdTd" style="display: none;"><span><s:select list="resellerInfoList" listKey="resellerInfoId" listValue="resellerName" name="resellerInfoId" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
        </tr>
      </table>      
    </div>
  </div> 
  
<div class="section">
  <div class="section-header clearfix">
    <strong class="left"><span class="ui-icon icon-ttl-section-info"></span>员工信息</strong>
    <a class="add right" onclick="bspos04_addEmployee();return false;"><span class="ui-icon icon-add"></span><span class="button-text">添加员工</span></a>
  </div>
  <div class="section-content">
  <div style="overflow-x:auto;overflow-y:hidden">
  <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <thead>
    <tr>
      <th class="tableheader" width="30%">员工姓名</th>
      <th class="tableheader" width="30%">部门</th>
      <th class="tableheader" width="40%">岗位</th>
    </tr>
    </thead>
    <tbody id="employeeBody"></tbody>
  </table>
  </div>
  </div>
</div>

<hr class="space" />
<div class="center">
  <s:a action="BINOLBSPOS04_add" id="addPositionUrl" cssStyle="display: none;"></s:a>
  <button class="save" onclick="savePosition();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="save_button"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>
</cherry:form>


</div>
</div>
</div>  
</s:i18n>

<table class="hide">
  <tbody id="employees">	
  	<tr>
      <td>
      <input type="hidden" name="employeeId"/>
      <span class="left" id="employeeName">
      		&nbsp;
      </span>
      <span class="close right" onclick="bspos04_delEmployee(this);"><span class="ui-icon ui-icon-close"></span></span>
	  <span class="popup right" onclick="bspos99_popDataTableOfEmployee(this,null);"><span class="ui-icon ui-icon-zoomin"></span></span>
      </td>
      <td><span id="departName"></span></td>
      <td><span id="positionName"></span></td>
    </tr>
  </tbody>  
</table>
<jsp:include page="/WEB-INF/jsp/bs/pos/BINOLBSPOS99.jsp" flush="true" />


          
     
















      
 