<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM26.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM26"> 	
 	<s:text name="global.page.select" id="select_default"/>
 	<div class="crm_content_header clearfix">
	  <span class="left"><span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm26_title" /></span></span>
	</div>
	<div class="panel-content clearfix">
	
	<div class="section">
        <div class="section-content">
	  
		  <div class="box">
		  <form id="issueCherryForm" class="inline" onsubmit="binolmbmbm26.searchIssueList();return false;" >
		  <s:hidden name="memberInfoId"></s:hidden>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column" style="width:50%; height: auto;">
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm26_issueNo"/></label>
	          <s:textfield name="issueNoQ" cssClass="text"/>
	        </p>
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm26_issueSummary"/></label>
	          <s:textfield name="issueSummaryQ" cssClass="text"/>
	        </p>
	      </div>
	      <div class="column last" style="width:49%; height: auto;">
	      	<p>
	          <label style="width:80px;"><s:text name="binolmbmbm26_issueType"/></label>
	          <s:select list='#application.CodeTable.getCodes("1272")' listKey="CodeKey" listValue="Value" name="issueTypeQ" headerKey="" headerValue="%{#select_default}" id="issueTypeQ"></s:select>
	          <select name="issueSubTypeQ" id="issueSubTypeQ" style="width:80px;display:none;"><option value=""><s:text name="global.page.select"/></option></select>
			  <s:select list='#application.CodeTable.getCodes("1329")' listKey="CodeKey" listValue="Value" name="issueSubTypeTempQ" cssStyle="width:200px;display:none;" id="issueSubTypeTempQ"></s:select>
	        </p>
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm26_issueStatus"/></label>
	          <s:select list='#application.CodeTable.getCodes("1118")' listKey="CodeKey" listValue="Value" name="issueStatusQ" headerKey="" headerValue="%{#select_default}"></s:select>
	        </p>
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm26_resolution"/></label>
	          <s:select list='#application.CodeTable.getCodes("1117")' listKey="CodeKey" listValue="Value" name="resolutionQ" headerKey="" headerValue="%{#select_default}"></s:select>
	        </p>
	      </div>
	      
		  </div>
		  <p class="clearfix">
	          <button class="right search" onclick="binolmbmbm26.searchIssueList();return false;">
	          	<span class="ui-icon icon-search-big"></span>
	          	<span class="button-text"><s:text name="global.page.search"></s:text></span>
	          </button>
	      </p>
		  </form>
		  </div>
		  
	  	  <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="issueDataTable">
		      <thead>
		        <tr>
		          <th><s:text name="binolmbmbm26_issueNo"></s:text></th>
		          <th><s:text name="binolmbmbm26_issueSummary"></s:text></th>
		          <th><s:text name="binolmbmbm26_issueType"></s:text></th>
		          <th><s:text name="binolmbmbm26_assignee"></s:text></th>
		          <th><s:text name="binolmbmbm26_speaker"></s:text></th>
		          <th><s:text name="binolmbmbm26_priority"></s:text></th>
		          <th><s:text name="binolmbmbm26_issueStatus"></s:text></th>
		          <th><s:text name="binolmbmbm26_resolution"></s:text></th>
		          <th><s:text name="binolmbmbm26_createTime"></s:text></th>
		          <th><s:text name="binolmbmbm26_updateTime"></s:text></th>
		        </tr>
		      </thead>
		      <tbody>
		      </tbody>
		  </table>
		  
	      
		</div>
	</div>
	
	
    
    </div>

<div class="hide">
  <div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
  <div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
  <div id="dialogClose"><s:text name="global.page.dialogClose" /></div>
  <div id="dialogLoading"><s:text name="global.page.loading" /></div>
  <div id="issueDetailTitle"><s:text name="binolmbmbm26_issueDetailTitle" /></div>
  <s:url action="BINOLMBMBM26_search" id="searchIssueUrl"></s:url>
  <a href="${searchIssueUrl }" id="searchIssueUrl"></a>
</div>   
      
</s:i18n>   



<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>  