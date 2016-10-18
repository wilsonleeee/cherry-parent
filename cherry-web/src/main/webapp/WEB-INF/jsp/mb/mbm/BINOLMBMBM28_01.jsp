<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM28_01.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM27">
<cherry:form id="editIssueForm" csrftoken="false">
  <s:hidden name="issueId"></s:hidden>
  <s:hidden name="oldResolutionAdd" value="%{issueDetailInfo.resolution}"></s:hidden>
  <div class="panel-content clearfix">  
  <div class="detail_box">
	<div id="actionResultDialogDisplay" style="margin-bottom: 5px;"></div>
	<table class="detail"> 
	  <tr>
		<th><s:text name="binolmbmbm27_issueSummary"></s:text></th>
		<td colspan="3"><span style="width:90%"><s:textfield name="issueSummaryAdd" cssClass="text" maxlength="100" cssStyle="width:100%" value="%{issueDetailInfo.issueSummary}"></s:textfield></span></td>
	  </tr>
	  <tr>
		<th><s:text name="binolmbmbm27_issueType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		<td colspan="3"><span>
		<s:select list='#application.CodeTable.getCodes("1272")' listKey="CodeKey" listValue="Value" name="issueTypeAdd" value="%{issueDetailInfo.issueType}" cssStyle="width:200px" id="issueTypeAdd"></s:select>
		<select name="issueSubTypeAdd" id="issueSubTypeAdd" style="width:200px;display:none;"><option value=""><s:text name="global.page.select"/></option></select>
		<s:hidden name="issueSubTypeAddTemp" value="%{issueDetailInfo.issueSubType}" id="issueSubTypeAddTemp"></s:hidden>
		<s:select list='#application.CodeTable.getCodes("1329")' listKey="CodeKey" listValue="Value" name="issueSubTypeTemp" cssStyle="width:200px;display:none;" id="issueSubTypeTemp"></s:select>
		</span></td>
	  </tr>
	  <tr>
		<th><s:text name="binolmbmbm27_priority"></s:text></th>
		<td><span><s:select list='#application.CodeTable.getCodes("1271")' listKey="CodeKey" listValue="Value" name="priorityAdd" value="%{issueDetailInfo.priority}"></s:select></span></td>
		<th><s:text name="binolmbmbm27_reIssueNo"></s:text></th>
	    <td><span style="width:90%"><s:textfield name="reIssueNoAdd" cssClass="text" cssStyle="width:100%" maxlength="22" value="%{issueDetailInfo.reIssueNo}"></s:textfield></span></td>
	  </tr>
	  <tr>
		<th><s:text name="binolmbmbm27_assignee"></s:text></th>
		<td><span>
		  <s:hidden name="assigneeAdd" value="%{issueDetailInfo.assignee}"></s:hidden>
		  <span id="assigneeDiv" style="line-height: 18px;"><s:property value="%{issueDetailInfo.assigneeName}"/></span>
		  <s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		  <a class="add" onclick="binolmbmbm28_01.popAssigneeList('${searchEmployeeInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		</span></td>
		<th><s:text name="binolmbmbm27_billCode"></s:text></th>
	    <td><span>
	      <s:hidden name="billCodeAdd" value="%{issueDetailInfo.billCode}"></s:hidden>
		  <span id="billCodeDiv" style="line-height: 18px;">
		  <s:if test='%{issueDetailInfo.billCode != null && !"".equals(issueDetailInfo.billCode)}'>
          <s:property value="%{issueDetailInfo.billCode}"/>
          <span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm28_01.delSaleRecordHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
          </s:if>
		  </span>
		  <s:url action="BINOLCM02_initSaleRecordDialog" namespace="/common" id="searchSaleRecordInitUrl"></s:url>
		  <a class="add" onclick="binolmbmbm28_01.popSaleRecordList('${searchSaleRecordInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
	    </span></td>
	  </tr>
	  <tr>
		<th><s:text name="binolmbmbm27_speaker"></s:text></th>
		<td><span>
		  <s:hidden name="speakerAdd" value="%{issueDetailInfo.speaker}"></s:hidden>
		  <span id="speakerDiv" style="line-height: 18px;"><s:property value="%{issueDetailInfo.speakerName}"/></span>
		  <s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		  <a class="add" onclick="binolmbmbm28_01.popSpeakerList('${searchEmployeeInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		</span></td>
		<th><s:text name="binolmbmbm27_campaignId"></s:text></th>
		<td><span>
		  <s:hidden name="campaignTypeAdd" value="%{issueDetailInfo.campaignType}"></s:hidden>
          <s:hidden name="campaignCodeAdd" value="%{issueDetailInfo.campaignCode}"></s:hidden>
          <span id="campaignDiv" style="line-height: 18px;">
          <s:if test="%{issueDetailInfo.campaignName != null}">
          <s:property value="%{issueDetailInfo.campaignName}"/>
          <span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm28_01.delCampaignHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
          </s:if>
          </span>
          <s:url action="BINOLCM02_initCampaignDialog" namespace="/common" id="searchCampaignInitUrl"></s:url>
          <a class="add" onclick="binolmbmbm28_01.popCampaignList('${searchCampaignInitUrl}');return false;">
		    <span class="ui-icon icon-search"></span>
		    <span class="button-text"><s:text name="global.page.Popselect" /></span>
	      </a>
		</span></td>
	  </tr>
	  <tr>
	    <th><s:text name="binolmbmbm27_dueDate"></s:text></th>
	    <td colspan="3"><span><s:textfield name="dueDateAdd" cssClass="date" cssStyle="width:80px" value="%{issueDetailInfo.dueDate}"></s:textfield></span></td>
	  </tr>
	  <tr>
		<th><s:text name="binolmbmbm27_description"></s:text></th>
		<td colspan="3"><span style="width:90%"><s:textarea name="descriptionAdd" cssClass="text" cssStyle="width:100%" value="%{issueDetailInfo.description}"></s:textarea></span></td>
	  </tr>
	</table>
	
	<table class="detail"> 
	  <tr>
	    <th style="width:15%"><s:text name="binolmbmbm27_resolution"></s:text></th>
		<td style="width:85%"><span><s:select list='#application.CodeTable.getCodes("1117")' listKey="CodeKey" listValue="Value" name="resolutionAdd" value="%{issueDetailInfo.resolution}"></s:select></span></td>
	  </tr>	
	  <tr>
		<th style="width:15%"><s:text name="binolmbmbm27_actionBody"></s:text></th>
		<td style="width:85%"><span style="width:90%"><s:textarea name="actionBodyAdd" cssClass="text" cssStyle="width:100%"></s:textarea></span></td>
	  </tr>	
	</table>
	<div class="center clearfix">
	  <s:url action="BINOLMBMBM28_editIssue" id="editIssueUrl"></s:url>
	  <button onclick="binolmbmbm28_01.editIssue('${editIssueUrl}');return false;" class="save">
    	<span class="ui-icon icon-save"></span>
    	<span class="button-text"><s:text name="binolmbmbm27_saveIssueButtion"></s:text></span>
      </button>
      <s:url action="BINOLMBMBM28_init" id="searchIssueDetailUrl">
        <s:param name="issueId" value="issueId"></s:param>
      </s:url>
      <button onclick="binolmbmbm28_01.cancelEditIssue('${searchIssueDetailUrl}');return false;" class="save">
    	<span class="ui-icon icon-cancel"></span>
    	<span class="button-text"><s:text name="binolmbmbm27_cancelIssueButtion"></s:text></span>
      </button>
	</div>
  </div>
  </div>
</cherry:form>

</s:i18n>