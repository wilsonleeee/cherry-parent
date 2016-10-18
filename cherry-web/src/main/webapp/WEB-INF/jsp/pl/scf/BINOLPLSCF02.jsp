<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF02.js"></script>


<s:i18n name="i18n.pl.BINOLPLSCF02">
<s:text name="global.page.select" id="select_default"></s:text>
	<div class="panel-header">
        <div class="clearfix"> 
        <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="scf_title" />&nbsp;&gt;&nbsp;<s:text name="scf_audit_title" /></span> 
        <s:url action="BINOLPLSCF03_init" id="addAuditInitUrl"></s:url>
        <span class="right"> 
        	<cherry:show domId="BINOLPLSCF0201">
        	<a class="add" onclick="plscf02_addAuditInit('${addAuditInitUrl }');return false;">
        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="add_audit" /></span>
        	</a> 
        	</cherry:show>
        </span> 
        </div>
	</div>
	<div class="panel-content">
        <div class="box">
		<cherry:form id="searchAuditForm" class="inline" onsubmit="plscf02_searchAudit();return false;">
            <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition" /></strong> </div>
            <div class="box-content clearfix">
              <div class="column" style="width:100%; border:none;">
                <s:if test="%{brandInfoList != null}">
                <p>
                  <label style="width:100px;"><s:text name="brandInfo" /></label>
                  <s:url action="BINOLPLSCF02_searchBuType" id="searchBuTypeUrl"></s:url>
                  <%-- <s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId" onchange="plscf02_searchBuType('%{#searchBuTypeUrl}',this,'%{#select_default}');"></s:select> --%>
                  <s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId"></s:select>
                </p>
                </s:if>
                <p>
                  <label style="width:100px;"><s:text name="bussinessTypeCode" /></label>
                  <s:select list='#application.CodeTable.getCodes("1138")' listKey="CodeKey" listValue="Value" name="bussinessTypeCode" headerKey="" headerValue="%{#select_default}"></s:select>
                  <%-- <s:select list="bussinessTypeCodeList" listKey="configValue" listValue="commentsChinese" name="bussinessTypeCode" headerKey="" headerValue="%{select_default}"></s:select> --%>
                </p>
              </div>
            </div>
            <p class="clearfix">
              <cherry:show domId="BINOLPLSCF0204">
              <button class="right search" onclick="plscf02_searchAudit();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search" /></span>
              </button>
              </cherry:show>
            </p>
		</cherry:form>
        </div>
        <div class="section hide" id="auditSection">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="global.page.list" /></strong></div>
          <div class="section-content">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="dateTable" width="100%">
              <thead>
                <tr>
                  <%-- <th><s:text name="brandInfo" /></th> --%>
                  <th><s:text name="bussinessTypeCode" /></th>
                  <th><s:text name="initiatorType" /></th>
                  <th><s:text name="initiatorID" /></th>
                  <th><s:text name="auditorType" /></th>
                  <th><s:text name="auditorID" /></th>
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
<div style="display: none;">
<s:a action="BINOLPLSCF02_auditList" id="auditListUrl"></s:a>
<div id="deleteAuditText"><p class="message"><span><s:text name="deleteAuditText" /></span></p></div>
<div id="deleteAuditTitle"><s:text name="deleteAuditTitle" /></div>
<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
<div id="dialogClose"><s:text name="global.page.close" /></div>
</div>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

