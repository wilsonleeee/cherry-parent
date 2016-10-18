<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM14.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM14"> 	
 	<s:text name="global.page.select" id="select_default"/>
 	<div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm14_memInfoRecordInfo" /></span>
	</div>
	<div class="panel-content clearfix">
	
	<div class="section">
        <div class="section-content">
        
		<div class="box">
		<form id="memInfoRecordForm" class="inline" onsubmit="binolmbmbm14.searchMemInfoRecord();return false;" >
		  <s:hidden name="memberInfoId"></s:hidden>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column" style="width:50%; height: auto;">
	        <p>
	          <label><s:text name="binolmbmbm14_modifyCounter"/></label>
	          <s:textfield name="modifyCounter" cssClass="text"/>
	        </p>
	        <p>
	          <label><s:text name="binolmbmbm14_modifyEmployee"/></label>
	          <s:textfield name="modifyEmployee" cssClass="text"/>
	        </p>
	        <p>
	          <label><s:text name="binolmbmbm14_batchNo"/></label>
	          <s:textfield name="batchNo" cssClass="text"/>
	        </p>
	      </div>
	      <div class="column last" style="width:49%; height: auto;">
	        <p>
	          <label><s:text name="binolmbmbm14_modifyType"/></label>
	          <s:select list='#application.CodeTable.getCodes("1241")' listKey="CodeKey" listValue="Value" name="modifyType" headerKey="" headerValue="%{#select_default}"></s:select>
	        </p>
	        <p>
	          <label><s:text name="binolmbmbm14_modifyTime"/></label>
	          <span><s:textfield name="modifyTimeStart" cssClass="date" cssStyle="width: 80px;"/></span><span>-<s:textfield name="modifyTimeEnd" cssClass="date" cssStyle="width: 80px;"/></span>
	        </p>
	        <p>
	          <label><s:text name="binolmbmbm14_remark"/></label>
	          <s:textfield name="remark" cssClass="text"/>
	        </p>
	      </div>
		  </div>
		  <p class="clearfix">
	          <button class="right search" onclick="binolmbmbm14.searchMemInfoRecord();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
	      </p>
		</form>
		</div>
		
		<div id="memInfoRecordDataTableDiv" class="hide">
		<div class="toolbar clearfix">
	      <span class="left">
	     	<a id="export" class="export" onclick="binolmbmbm14.exportExcel();return false;">
	          <span class="ui-icon icon-export"></span>
	          <span class="button-text"><s:text name="global.page.export"/></span>
	        </a>
	      </span>
	    </div>
		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table memEventDataTable" width="100%" id="memInfoRecordDataTable">
	      <thead>
	        <tr>
	          <th><s:text name="binolmbmbm14_memCode"></s:text></th>
	          <th><s:text name="binolmbmbm14_modifyType"></s:text></th>
	          <th><s:text name="binolmbmbm14_batchNo"></s:text></th>
	          <th><s:text name="binolmbmbm14_modifyTime"></s:text></th>
	          <th><s:text name="binolmbmbm14_modifyCounter"></s:text></th>
	          <th><s:text name="binolmbmbm14_modifyEmployee"></s:text></th>
	          <th><s:text name="binolmbmbm14_remark"></s:text></th>
	        </tr>
	      </thead>
	      <tbody>
	      </tbody>
	    </table>
	    </div>
    
    	</div>
    </div>
    
    </div>
      
</s:i18n>   

<div class="hide">
	<s:url action="BINOLMBMBM14_search" id="memInfoRecordUrl"></s:url>
	<a href="${memInfoRecordUrl }" id="memInfoRecordUrl"></a>
	<s:url id="exportUrl" action="BINOLMBMBM14_export" ></s:url>
	<a id="exportUrl" href="${exportUrl}"></a>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>  