<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>



<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF03.js"></script>


<s:if test="auditInfo != null">
<s:i18n name="i18n.pl.BINOLPLSCF02">
<s:text name="global.page.select" id="select_default"></s:text>
<s:url action="BINOLPLSCF03_searchCodeByType" id="searchCodeByTypeUrl"></s:url>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="scf_title" />&nbsp;&gt;&nbsp;<s:text name="update_audit" /></span>  
  </div>
</div>

<div id="actionResultDisplay"></div>

<cherry:form id="addAuditInfo" csrftoken="false" onsubmit="plscf03_saveAudit();return false;">
<s:hidden name="auditPrivilegeId" value="%{auditInfo.auditPrivilegeId}"></s:hidden>
<s:hidden name="modifyTime" value="%{auditInfo.updateTime}"></s:hidden>
<s:hidden name="modifyCount" value="%{auditInfo.modifyCount}"></s:hidden>
<div class="panel-content clearfix">

<div class="section">
  <div class="section-content">
    <table class="detail" cellpadding="0" cellspacing="0">
  	<caption>
      <s:if test="%{brandInfoList != null}">
      <s:url action="BINOLPLSCF02_searchBuType" id="searchBuTypeUrl"></s:url>
      <s:text name="brandInfo"></s:text>：
      <%-- <s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId" onchange="plscf03_searchBuType('%{#searchBuTypeUrl}',this,'%{#select_default}');" value="%{auditInfo.brandInfoId}"></s:select>&nbsp;&nbsp; --%>
      <s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId" value="%{auditInfo.brandInfoId}"></s:select>&nbsp;&nbsp;
      </s:if>
      <s:else>
      <s:hidden name="brandInfoId" value="%{auditInfo.brandInfoId}"></s:hidden>
      </s:else>
      <span><s:text name="bussinessTypeCode"></s:text>：
      <s:select list='#application.CodeTable.getCodes("1138")' listKey="CodeKey" listValue="Value" name="bussinessTypeCode" headerKey="" headerValue="%{#select_default}" value="%{auditInfo.bussinessTypeCode}"></s:select>
      <%-- <s:select list="bussinessTypeCodeList" listKey="configValue" listValue="commentsChinese" headerKey="" headerValue="%{#select_default}" name="bussinessTypeCode" value="%{auditInfo.bussinessTypeCode}"></s:select> --%>
      <span class="highlight"><s:text name="global.page.required"></s:text></span>
      </span>
    </caption>
  	<tr>
        <th><s:text name="initiatorType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span>
        <select id="initiatorType" name="initiatorType" onchange="plscf03_searchCodeByType('${searchCodeByTypeUrl }',this,'${select_default }');">
        	<option value="">${select_default }</option>
        	<option value="1" <s:if test='%{auditInfo.initiatorType == "1"}'>selected</s:if>><s:text name="type_user"></s:text></option>
        	<option value="2" <s:if test='%{auditInfo.initiatorType == "2"}'>selected</s:if>><s:text name="type_pos"></s:text></option>
        	<option value="3" <s:if test='%{auditInfo.initiatorType == "3"}'>selected</s:if>><s:text name="type_orc"></s:text></option>
        </select>
        </span></td>
        <th><s:text name="initiatorID"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span>
        <s:select list="initiatorList" listKey="code" listValue="name" headerKey="" headerValue="%{#select_default}" name="initiatorID" value="%{auditInfo.initiatorID}"></s:select>
        </span></td>
      </tr>
      <tr>
        <th><s:text name="auditorType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span>
        <select id="auditorType" name="auditorType" onchange="plscf03_searchCodeByType('${searchCodeByTypeUrl }',this,'${select_default }');">
        	<option value="">${select_default }</option>
        	<option value="1" <s:if test='%{auditInfo.auditorType == "1"}'>selected</s:if>><s:text name="type_user"></s:text></option>
        	<option value="2" <s:if test='%{auditInfo.auditorType == "2"}'>selected</s:if>><s:text name="type_pos"></s:text></option>
        	<option value="3" <s:if test='%{auditInfo.auditorType == "3"}'>selected</s:if>><s:text name="type_orc"></s:text></option>
        </select>
        </span></td>
        <th><s:text name="auditorID"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span>
        <s:select list="auditorList" listKey="code" listValue="name" headerKey="" headerValue="%{#select_default}" name="auditorID" value="%{auditInfo.auditorID}"></s:select>
        </span></td>
      </tr>
    </table>      
  </div>
</div> 

<hr class="space" />
<div class="center">
  <s:a action="BINOLPLSCF04_updateAudit" id="addAuditUrl" cssStyle="display: none;"></s:a>
  <button class="save" onclick="plscf03_saveAudit();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>
</cherry:form>

</div>
</div>
</div>  
</s:i18n>
</s:if>
<s:else>
<jsp:include page="/WEB-INF/jsp/common/actionResultBody.jsp" flush="true" />
</s:else>


          
     
















      
 