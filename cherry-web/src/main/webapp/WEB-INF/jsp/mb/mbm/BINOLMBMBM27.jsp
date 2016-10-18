<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM27.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM27">
<cherry:form id="addIssueForm" csrftoken="false">
	<s:hidden name="customerType"></s:hidden>
	<s:hidden name="customerCode"></s:hidden>
	<s:hidden name="callId"></s:hidden>
  <s:hidden name="memberInfoId"></s:hidden>
  <div class="panel-content clearfix">  
  <div class="detail_box">
	<div id="actionResultDialogDisplay" style="margin-bottom: 5px;"></div>
	<table class="detail"> 
	  <tr>
		<th><s:text name="binolmbmbm27_issueSummary"></s:text></th>
		<td colspan="3"><span style="width:90%"><s:textfield name="issueSummary" cssClass="text" maxlength="100" cssStyle="width:100%"></s:textfield></span></td>
	  </tr>
	  <tr>
		<th><s:text name="binolmbmbm27_issueType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		<td colspan="3"><span>
		<s:select list='#application.CodeTable.getCodes("1272")' listKey="CodeKey" listValue="Value" name="issueType" value="1" cssStyle="width:200px" id="issueType"></s:select>
		<select name="issueSubType" id="issueSubType" style="width:200px;display:none;"><option value=""><s:text name="global.page.select"/></option></select>
		<s:select list='#application.CodeTable.getCodes("1329")' listKey="CodeKey" listValue="Value" name="issueSubTypeTemp" cssStyle="width:200px;display:none;" id="issueSubTypeTemp"></s:select>
		</span></td>
	  </tr>
	  <tr>
		<th><s:text name="binolmbmbm27_priority"></s:text></th>
		<td><span><s:select list='#application.CodeTable.getCodes("1271")' listKey="CodeKey" listValue="Value" name="priority" value="1"></s:select></span></td>
		<th><s:text name="binolmbmbm27_reIssueNo"></s:text></th>
	    <td><span style="width:90%"><s:textfield name="reIssueNo" cssClass="text" cssStyle="width:100%" maxlength="22"></s:textfield></span></td>
	  </tr>
	  <tr>
		<th><s:text name="binolmbmbm27_assignee"></s:text></th>
		<td><span>
		  <s:hidden name="assignee"></s:hidden>
		  <span id="assigneeDiv" style="line-height: 18px;"><s:property value="assigneeName"/></span>
		  <s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		  <a class="add" onclick="binolmbmbm27.popAssigneeList('${searchEmployeeInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		</span></td>
		<th><s:text name="binolmbmbm27_billCode"></s:text></th>
	    <td><span>
	      <s:hidden name="billCode"></s:hidden>
		  <span id="billCodeDiv" style="line-height: 18px;"></span>
		  <s:url action="BINOLCM02_initSaleRecordDialog" namespace="/common" id="searchSaleRecordInitUrl"></s:url>
		  <a class="add" onclick="binolmbmbm27.popSaleRecordList('${searchSaleRecordInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
	    </span></td>
	  </tr>
	  <tr>
		<th><s:text name="binolmbmbm27_speaker"></s:text></th>
		<td><span>
		  <s:hidden name="speaker"></s:hidden>
		  <span id="speakerDiv" style="line-height: 18px;"><s:property value="speakerName"/></span>
		  <s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		  <a class="add" onclick="binolmbmbm27.popSpeakerList('${searchEmployeeInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		</span></td>
		<th><s:text name="binolmbmbm27_campaignId"></s:text></th>
		<td><span>
		  <s:hidden name="campaignType"></s:hidden>
          <s:hidden name="campaignCode"></s:hidden>
          <span id="campaignDiv" style="line-height: 18px;"></span>
          <s:url action="BINOLCM02_initCampaignDialog" namespace="/common" id="searchCampaignInitUrl"></s:url>
          <a class="add" onclick="binolmbmbm27.popCampaignList('${searchCampaignInitUrl}');return false;">
		    <span class="ui-icon icon-search"></span>
		    <span class="button-text"><s:text name="global.page.Popselect" /></span>
	      </a>
		</span></td>
	  </tr>
	  <tr>
	    <th><s:text name="binolmbmbm27_dueDate"></s:text></th>
	    <td colspan="3"><span><s:textfield name="dueDate" cssClass="date" cssStyle="width:80px"></s:textfield></span></td>
	  </tr>
	  <tr>
		<th><s:text name="binolmbmbm27_description"></s:text></th>
		<td colspan="3"><span style="width:90%"><s:textarea name="description" cssClass="text" cssStyle="width:100%;height:32px;"></s:textarea></span></td>
	  </tr>
	</table>
	<table class="detail"> 
	  <tr>
		<th style="width:15%"><s:text name="binolmbmbm27_resolution"></s:text></th>
		<td style="width:85%"><span><s:select list='#application.CodeTable.getCodes("1117")' listKey="CodeKey" listValue="Value" name="resolution" value="0"></s:select></span></td>
	  </tr>
	  <tr>
	    <th style="width:15%"><s:text name="binolmbmbm27_actionBody"></s:text></th>
		<td style="width:85%"><span style="width:90%"><s:textarea name="actionBody" cssClass="text" cssStyle="width:100%;height:32px;"></s:textarea></span></td>
	  </tr>
	</table>
	<div class="center clearfix">
	  <s:url action="BINOLMBMBM27_add" id="addIssueUrl"></s:url>
	  <button onclick="binolmbmbm27.addIssue('${addIssueUrl}');return false;" class="save">
    	<span class="ui-icon icon-save"></span>
    	<span class="button-text"><s:text name="binolmbmbm27_saveIssueButtion"></s:text></span>
      </button>
	</div>
  </div>
  </div>
</cherry:form>

</s:i18n>