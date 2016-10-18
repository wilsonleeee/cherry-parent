<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript" src="/Cherry/js/pl/common/BINOLPLCOM01.js"></script>
<script type="text/javascript" src="/Cherry/js/pl/rlm/BINOLPLRLM01.js"></script>


<s:i18n name="i18n.pl.BINOLPLRLM01">
<s:text name="select_default" id="select_default"></s:text>
	<div class="panel-header">
        <div class="clearfix"> 
        <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span> 
        <s:url action="BINOLPLRLM02_init" id="addRoleInitUrl"></s:url>
        <span class="right"> 
	        <a class="add" href="${addRoleInitUrl }" onclick="javascript:addRoleInit(this);return false;">
	        	<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="add_role" /></span>
	        </a> 
        </span> 
        </div>
	</div>
	<div class="panel-content">
        <div class="box">
		<cherry:form id="searchRoleForm" class="inline" onsubmit="searchRole();return false;">
            <s:hidden name="roleKind"></s:hidden>
            <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="search_condition" /></strong> </div>
            <div class="box-content clearfix">
              <div class="column" style="width:100%; border:none;">
                <p>
                  <label style="width:100px;"><s:text name="role_kind" /></label>
                  <a href="#" class="on" onclick="searchRoleByKind(this,'');return false;"><s:text name="condition_open" /></a> 
                  <s:iterator value='#application.CodeTable.getCodes("1009")' id="roleKindMap">
                  	| <a href="#" onclick="searchRoleByKind(this,'${roleKindMap.CodeKey }');return false;">${roleKindMap.Value }</a>
                  </s:iterator>
                </p>
                <p>
                  <label style="width:100px;"><s:text name="role_kw" /></label>
                  <input name="roleKw" type="text" class="text" />
                </p>
                <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
                <p>
                  <label style="width:100px;"><s:text name="brandInfo"></s:text></label>
                  <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#select_default}"></s:select>
                </p>
                </s:if>
              </div>
            </div>
            <p class="clearfix">
              <button class="right search" type="button" onclick="searchRole();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="search_button" /></span>
              </button>
            </p>
		</cherry:form>
        </div>
        <div class="section hide" id="roleSection">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="role_list_title" /></strong></div>
          <div class="section-content">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="dateTable" width="100%">
              <thead>
                <tr>
                  <th><s:text name="number" /></th>
                  <th><s:text name="role_name" /></th>
                  <th><s:text name="role_decription" /></th>
                  <th><s:text name="role_kind" /></th>
                  <th><s:text name="brandInfo" /></th>
                  <th class="center"><s:text name="operation_button" /></th>
                </tr>
              </thead>
              <tbody>
              </tbody>
            </table>
          </div>
        </div>
	</div>
<div class="hide" id="dialogInit"></div>
<div class="hide">
<div id="addRoleTitle"><s:text name="add_role" /></div>
<div id="updateRoleTitle"><s:text name="update_role" /></div>
<div id="deleteRoleTitle"><s:text name="delete_role" /></div>
<div id="roleAuthorizeTitle"><s:text name="role_authorize" /></div>
<div id="saveSuccessTitle"><s:text name="save_success" /></div>
<div id="dialogConfirm"><s:text name="dialog_confirm" /></div>
<div id="dialogCancel"><s:text name="dialog_cancel" /></div>
<div id="dialogClose"><s:text name="dialog_close" /></div>
<div id="dialogInitMessage"><s:text name="dialog_init_message" /></div>
<div id="deleteRoleText"><p class="message"><span><s:text name="delete_role_message" /></span></p></div>
<s:url action="BINOLPLRLM02_add" id="addRoleUrl"></s:url>
<a id="addRoleUrl" href="${addRoleUrl }"></a>
<s:url action="BINOLPLRLM04_update" id="updateRoleUrl"></s:url>
<a id="updateRoleUrl" href="${updateRoleUrl }"></a>
<s:url action="BINOLPLRLM01_list" id="roleListUrl"></s:url>
<a id="roleListUrl" href="${roleListUrl }"></a>
</div>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

