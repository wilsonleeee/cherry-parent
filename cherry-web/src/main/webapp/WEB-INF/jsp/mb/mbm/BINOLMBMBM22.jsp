<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM22.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM22"> 	
 	<div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm22_title" /></span>
	</div>
	<div class="panel-content clearfix">
	
	<div class="section">
        <div class="section-content">
	  
		  <div class="box">
		  <form id="answerCherryForm" class="inline" onsubmit="binolmbmbm22.searchAnswerList();return false;" >
		  <s:hidden name="memberInfoId"></s:hidden>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column" style="width:50%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm22_paperName"/></label>
	          <s:textfield name="paperName" cssClass="text"/>
	        </p>
	      </div>
	      <div class="column last" style="width:49%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm22_checkDate"/></label>
	          <span><s:textfield name="checkDateStart" cssClass="date"/></span>-<span><s:textfield name="checkDateEnd" cssClass="date"/></span>
	        </p>
	      </div>
		  </div>
		  <p class="clearfix">
	          <button class="right search" onclick="binolmbmbm22.searchAnswerList();return false;">
	          	<span class="ui-icon icon-search-big"></span>
	          	<span class="button-text"><s:text name="global.page.search"></s:text></span>
	          </button>
	      </p>
		  </form>
		  </div>
		  
	  	  <table cellpadding="0" cellspacing="0" border="0" class="jquery_table memEventDataTable" width="100%" id="answerListDataTable">
		      <thead>
		        <tr>
		          <th><s:text name="binolmbmbm22_paperName"></s:text></th>
		          <th><s:text name="binolmbmbm22_counterName"></s:text></th>
		          <th><s:text name="binolmbmbm22_employeeName"></s:text></th>
		          <th><s:text name="binolmbmbm22_checkDate"></s:text></th>	      
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
	<s:url action="BINOLMBMBM22_search" id="searchAnswerListUrl"></s:url>
	<a href="${searchAnswerListUrl }" id="searchAnswerListUrl"></a>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>  