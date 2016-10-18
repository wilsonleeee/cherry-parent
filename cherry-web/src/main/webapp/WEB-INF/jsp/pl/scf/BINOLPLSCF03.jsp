<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>



<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF03.js"></script>



<s:i18n name="i18n.pl.BINOLPLSCF02">
<s:text name="global.page.select" id="select_default"></s:text>
<s:url action="BINOLPLSCF03_searchCodeByType" id="searchCodeByTypeUrl"></s:url>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="scf_title" />&nbsp;&gt;&nbsp;<s:text name="add_audit" /></span>  
  </div>
</div>

<div id="actionResultDisplay"></div>

<cherry:form id="addAuditInfo" csrftoken="false" onsubmit="plscf03_saveAudit();return false;">
<div class="panel-content clearfix">

<div class="section">
  <div class="section-content">
    <table class="detail" cellpadding="0" cellspacing="0">
  	<caption>
      <s:if test="%{brandInfoList != null}">
      <s:url action="BINOLPLSCF02_searchBuType" id="searchBuTypeUrl"></s:url>
      <s:text name="brandInfo"></s:text>：
      <%-- <s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId" onchange="plscf03_searchBuType('%{#searchBuTypeUrl}',this,'%{#select_default}');"></s:select>&nbsp;&nbsp; --%>
      <s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId"></s:select>&nbsp;&nbsp;
      </s:if>
      <span><s:text name="bussinessTypeCode"></s:text>：
      <s:select list='#application.CodeTable.getCodes("1138")' listKey="CodeKey" listValue="Value" name="bussinessTypeCode" headerKey="" headerValue="%{#select_default}"></s:select>
      <%-- <s:select list="bussinessTypeCodeList" listKey="configValue" listValue="commentsChinese" headerKey="" headerValue="%{#select_default}" name="bussinessTypeCode"></s:select> --%>
      <span class="highlight"><s:text name="global.page.required"></s:text></span>
      </span>
    </caption>
  	<tr>
        <th><s:text name="initiatorType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span>
        <select id="initiatorType" name="initiatorType" onchange="plscf03_searchCodeByType('${searchCodeByTypeUrl }',this,'${select_default }');">
        	<option value="">${select_default }</option>
        	<option value="1"><s:text name="type_user"></s:text></option>
        	<option value="2"><s:text name="type_pos"></s:text></option>
        	<option value="3"><s:text name="type_orc"></s:text></option>
        </select>
        </span></td>
        <th><s:text name="initiatorID"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span>
        <select id="initiatorID" name="initiatorID">
        <option value="">${select_default }</option>
        </select>
        </span></td>
      </tr>
      <tr>
        <th><s:text name="auditorType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span>
        <select id="auditorType" name="auditorType" onchange="plscf03_searchCodeByType('${searchCodeByTypeUrl }',this,'${select_default }');">
        	<option value="">${select_default }</option>
        	<option value="1"><s:text name="type_user"></s:text></option>
        	<option value="2"><s:text name="type_pos"></s:text></option>
        	<option value="3"><s:text name="type_orc"></s:text></option>
        </select>
        </span></td>
        <th><s:text name="auditorID"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span>
        <select id="auditorID" name="auditorID">
        <option value="">${select_default }</option>
        </select>
        </span></td>
      </tr>
    </table>      
  </div>
</div> 

<hr class="space" />
<div class="center">
  <s:a action="BINOLPLSCF03_addAudit" id="addAuditUrl" cssStyle="display: none;"></s:a>
  <button class="save" onclick="plscf03_saveAudit();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>
</cherry:form>

</div>
</div>
</div>  
</s:i18n>


          
     
















      
 