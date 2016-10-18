<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/pl/rla/BINOLPLRLA04.js"></script>

<s:i18n name="i18n.pl.BINOLPLRLA01">
  <s:text name="select_default" id="select_default"/>
  <div class="panel-content">
    <div class="box">
      <cherry:form id="searchUserForm" class="inline" onsubmit="searchUser();return false;">
        <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="search_condition" /></strong> </div>
        <div class="box-content clearfix">
          <div class="column" style="width:50%;">
            <p>
              <label><s:text name="user_name" /></label>
              <s:textfield name="longinName" cssClass="text"></s:textfield>
            </p>
            <s:if test="%{brandInfoList != null && !brandInfoList.isEmpty()}">
            <p>
	          <label><s:text name="brandInfo"></s:text></label>
	          <%-- 部门查询URL --%>
			  <s:url id="getDepart_url" value="/common/BINOLCM00_queryOrg"></s:url>
			  <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#select_default}" onchange="getOrgByBrandInfo(this,'%{#getDepart_url }','%{#select_default }');"></s:select>
	        </p>
	        </s:if>
          </div>
          <div class="column last" style="width:49%;">
          	<p>
              <label><s:text name="employee_name" /></label>
              <s:textfield name="employeeName" cssClass="text"></s:textfield>
            </p>
            <%-- 
            <p>
              <label><s:text name="organization_label" /></label>
              <s:url action="BINOLCM00_queryPosition" namespace="/common" id="queryPositionUrl"></s:url>
              <s:select id="orgId" list="orgList" name="organizationId" listKey="departId" listValue="departName" headerKey="" headerValue="%{select_default}" onchange="getPosition(this,'%{queryPositionUrl}','%{select_default}');" cssStyle="display:none;"></s:select>
              <a class="ui-select" style="margin-left: 0px;">
             	<span class="button-text"><input id="depart_text" type="text" class="ui-input" value="${select_default}" style="width:100px;"/></span>
               	<span id="depart_btn" class="ui-icon ui-icon-triangle-1-s"></span>
              </a>
            </p>
            <p>
              <label><s:text name="position_label" /></label>
              <select name="positionId" id="positionId" style="display:none;">
				<option value=""><s:text name="select_default" /></option>
			  </select>
			  <a class="ui-select" style="margin-left: 0px;">
              	<span class="button-text"><input id="position_text" type="text" class="ui-input" value="${select_default}" style="width:100px;"/></span>
                <span id="position_btn" class="ui-icon ui-icon-triangle-1-s"></span>
              </a>
            </p>
            --%>
          </div>
        </div>
        <p class="clearfix">
          <button class="right search" onclick="searchUser();return false;">
          	<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="search_button" /></span>
          </button>
        </p>
      </cherry:form>
    </div>
    <div class="section hide" id="userSection">
      <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="user_list_title" /></strong></div>
      <div class="section-content">
        <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="dateTable" width="100%">
          <thead>
            <tr>
              <th><s:text name="number" /></th>
              <th><s:text name="user_name" /></th>
              <th><s:text name="employee_name" /></th>
              <th><s:text name="brandInfo" /></th>
              <th><s:text name="organization_label" /></th>
              <th><s:text name="position_label" /></th>
              <th class="center"><s:text name="role_assign" /></th>
            </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>
    </div>
  </div>

<div class="hide">
<div id="userRoleAssignTitle"><s:text name="user_role_title" /></div>
<div id="dialogConfirm"><s:text name="dialog_confirm" /></div>
<div id="dialogCancel"><s:text name="dialog_cancel" /></div>
<div id="dialogClose"><s:text name="dialog_close" /></div>
<div id="saveSuccessTitle"><s:text name="save_success" /></div>
<div id="dialogInitMessage"><s:text name="dialog_init_message" /></div>
<div id="saveSuccessText"><div><p class="success"><span><s:text name="save_success_message" /></span></p></div></div>
<div id="select_default"><s:text name="select_default" /></div>
<div id="noUserRole"><s:text name="noUserRole" /></div>
<s:url action="BINOLPLRLA04_list" id="userListUrl"></s:url>
<a href="${userListUrl }" id="userListUrl"></a>
</div>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />    