<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM12.js?v=20170211"></script>

<s:i18n name="i18n.mb.BINOLMBMBM12">
<s:text name="global.page.select" id="select_default"/>
<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left">
      <span class="ui-icon icon-breadcrumb"></span>
      <s:text name="binolmbmbm12_memberManage"></s:text>&nbsp;&gt;&nbsp;<s:text name="binolmbmbm12_memInfoRecordList"></s:text>
    </span> 
  </div>
</div>
<div class="panel-content clearfix">
<div class="box">
  <cherry:form id="memInfoRecordForm" class="inline" onsubmit="binolmbmbm12.searchMemInfoRecord();return false;">
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
    </div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%; height: auto;">
        <p>
          <label><s:text name="binolmbmbm12_memCode"/></label>
          <s:textfield name="memCode" cssClass="text"/>
        </p>
        <p>
          <label><s:text name="binolmbmbm12_modifyCounter"/></label>
          <s:textfield name="modifyCounter" cssClass="text"/>
        </p>
        <p>
          <label><s:text name="binolmbmbm12_modifyEmployee"/></label>
          <s:textfield name="modifyEmployee" cssClass="text"/>
        </p>
        <p>
          <label><s:text name="binolmbmbm12_batchNo"/></label>
          <s:textfield name="batchNo" cssClass="text"/>
        </p>
      </div>
      <div class="column last" style="width:49%; height: auto;">
        <p>
          <label><s:text name="binolmbmbm12_modifyTime"/></label>
          <span><s:textfield name="modifyTimeStart" cssClass="date"/></span><span>-<s:textfield name="modifyTimeEnd" cssClass="date"/></span>
        </p>
        <p>
          <label><s:text name="binolmbmbm12_modifyType"/></label>
          <s:select list='#application.CodeTable.getCodes("1241")' listKey="CodeKey" listValue="Value" name="modifyType" headerKey="" headerValue="%{#select_default}"></s:select>
        </p>
        <p>
          <label><s:text name="binolmbmbm12_remark"/></label>
          <s:textfield name="remark" cssClass="text"/>
        </p>
      </div>
	</div>
    <p class="clearfix">
      <button class="right search" onclick="binolmbmbm12.searchMemInfoRecord();return false;">
        <span class="ui-icon icon-search-big"></span>
        <span class="button-text"><s:text name="global.page.search"></s:text></span>
      </button>
    </p>
  </cherry:form>
</div>
<div class="section hide" id="memInfoRecord">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
  <div class="section-content">
    <div class="toolbar clearfix">
      <span class="left">
     	<cherry:show domId="BINOLMBMBM12_01">
     	<a id="export" class="export" onclick="binolmbmbm12.exportExcel();return false;">
          <span class="ui-icon icon-export"></span>
          <span class="button-text"><s:text name="global.page.export"/></span>
        </a>
        </cherry:show>
      </span>
    </div>
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="memInfoRecordDataTable">
      <thead>
        <tr>
          <th><s:text name="binolmbmbm12_number"></s:text></th>
          <th><s:text name="binolmbmbm12_memCode"></s:text></th>
          <th><s:text name="binolmbmbm12_modifyType"></s:text></th>
          <th><s:text name="binolmbmbm12_batchNo"></s:text></th>
          <th><s:text name="binolmbmbm12_modifyTime"></s:text></th>
          <th><s:text name="binolmbmbm12_modifyCounter"></s:text></th>
          <th><s:text name="binolmbmbm12_modifyEmployee"></s:text></th>
          <th><s:text name="binolmbmbm12_remark"></s:text></th>
          <th><s:text name="binolmbmbm12_showDetail"></s:text></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>
</div>
</s:i18n>  

<div class="hide">
<s:url action="BINOLMBMBM12_search" id="memInfoRecordUrl"></s:url>
<a href="${memInfoRecordUrl }" id="memInfoRecordUrl"></a>
<s:url id="exportUrl" action="BINOLMBMBM12_export" ></s:url>
<a id="exportUrl" href="${exportUrl}"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />