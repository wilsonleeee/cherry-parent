<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM07.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#participateTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#participateTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#participateTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#participateTimeStart').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'campaignHistoryForm',
		rules: {
			participateTimeStart: {dateValid: true},
			participateTimeEnd: {dateValid: true}
		}
	});
	binolmbmbm07.searchCampaignHistory();
});
</script>

<s:i18n name="i18n.mb.BINOLMBMBM07"> 	
 	<s:text name="global.page.select" id="select_default"/>
 	<div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm07_memCampaignHistory" /></span>
	</div>
	<div class="panel-content clearfix">
	
	<div class="section">
        <div class="section-content">
        
		<div class="box">
		<form id="campaignHistoryForm" class="inline" onsubmit="binolmbmbm07.searchCampaignHistory();return false;">
		  <s:hidden name="memberInfoId"></s:hidden>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column" style="width:50%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm07_activityName" /></label>
	          <s:textfield name="activityName" cssClass="text"></s:textfield>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm07_tradeNoIF" /></label>
	          <s:textfield name="tradeNoIF" cssClass="text"></s:textfield>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm07_batchNo" /></label>
	          <s:textfield name="batchNo" cssClass="text"></s:textfield>
	        </p>
	      </div>
	      <div class="column last" style="width:49%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm07_participateTime"/></label>
	          <span><s:textfield name="participateTimeStart" cssClass="date" cssStyle="width: 80px;"></s:textfield></span><span>-<s:textfield name="participateTimeEnd" cssClass="date" cssStyle="width: 80px;"></s:textfield></span>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm07_state" /></label>
	          <s:select list='#application.CodeTable.getCodes("1116")' listKey="CodeKey" listValue="Value" name="state" headerKey="" headerValue="%{#select_default}"></s:select>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm07_departName" /></label>
	          <s:textfield name="departName" cssClass="text"></s:textfield>
	        </p>
	      </div>
		  </div>
		  <p class="clearfix">
	          <button class="right search" onclick="binolmbmbm07.searchCampaignHistory();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
	      </p>
		</form>
		</div>
		
		<div id="campaignDataTableDiv" class="hide">
		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="campaignDataTable">
	      <thead>
	        <tr>
	          <th><s:text name="binolmbmbm07_activityName"/></th>
	          <th><s:text name="binolmbmbm07_subCampaignCode"/></th>
	          <th><s:text name="binolmbmbm07_groupName"/></th>
	          <th><s:text name="binolmbmbm07_campaignType"/></th>
	          <th><s:text name="binolmbmbm07_tradeNoIF"/></th>
	          <th><s:text name="binolmbmbm07_batchNo"/></th>
	          <th><s:text name="binolmbmbm07_departName"/></th>
	          <th><s:text name="binolmbmbm07_participateTime"/></th>
	          <th><s:text name="binolmbmbm07_informType"/></th>
	          <th><s:text name="binolmbmbm07_state"/></th>
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
	<s:url action="BINOLMBMBM07_searchCampaignHistory" id="searchCampaignHistoryUrl"></s:url>
	<a href="${searchCampaignHistoryUrl }" id="searchCampaignHistoryUrl"></a>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>  