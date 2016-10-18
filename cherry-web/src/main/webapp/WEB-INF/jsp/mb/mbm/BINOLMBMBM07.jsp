<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM07.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#campaignDataTable").find('td').not($("#campaignDataTable").find('td.operateTd')).click(function() {
		binolmbmbm10.searchDetail($(this).parent()[0]);
	});
});
</script>

<s:i18n name="i18n.mb.BINOLMBMBM07"> 	
 	<div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm07_memCampaignCurrent" /></span>
	</div>
	<div id="actionResultDisplay"></div>
	<div class="panel-content clearfix">
	
	<div class="section">
        <div class="section-content">
		
		<div id="campaignDataTableDiv" style="overflow: auto;width: 100%;">
		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table memEventDataTable" width="100%">
	      <thead>
	        <tr>
	          <th><s:text name="binolmbmbm07_activityName"/></th>
	          <th><s:text name="binolmbmbm07_subCampaignCode"/></th>
	          <th><s:text name="binolmbmbm07_groupName"/></th>
	          <th><s:text name="binolmbmbm07_campaignType"/></th>
	          <th><s:text name="binolmbmbm07_campaignFromDate"/></th>
	          <th><s:text name="binolmbmbm07_campaignToDate"/></th>
	          <th><s:text name="binolmbmbm07_operate"/></th>
	        </tr>
	      </thead>
	      <tbody id="campaignDataTable">
	        <s:iterator value="campaignInfoList" id="campaignInfo" status="status">
	        <tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
	          <td>
	            <s:property value="subCampaignName"/>
	            <s:url action="BINOLMBMBM07_searchDetail" id="searchCampaignDetailUrl">
				  <s:param name="subCampaignId" value="%{#campaignInfo.subCampaignId}"></s:param>
				  <s:param name="actType" value="%{#campaignInfo.actType}"></s:param>
				</s:url>
				<input value="${searchCampaignDetailUrl }" type="hidden"/>
	          </td>
	          <td><s:property value="subCampaignCode"/></td>
	          <td><s:property value="campaignName"/></td>
	          <td>
	            <s:if test="%{#campaignInfo.actType == 0}">
					<s:property value='#application.CodeTable.getVal("1174", #campaignInfo.campaignType)' />
				</s:if>
				<s:else>
					<s:if test='"CXHD".equals(#campaignInfo.campaignType)'>
					   	<s:property value="#application.CodeTable.getVal('1228',#campaignInfo.subCampaignType)"/>
					</s:if>
					<s:if test='"LYHD".equals(#campaignInfo.campaignType)'>
					    <s:property value="#application.CodeTable.getVal('1247',#campaignInfo.subCampaignType)"/>
					</s:if>
					<s:if test='"DHHD".equals(#campaignInfo.campaignType)'>
					   	<s:property value="#application.CodeTable.getVal('1229',#campaignInfo.subCampaignType)"/>
					</s:if>
				</s:else>
	          </td>
	          <td><s:property value="campaignFromDate"/></td>
	          <td><s:property value="campaignToDate"/></td>
	          <td class="operateTd">
		        <s:if test="%{#campaignInfo.campaignOrderToDate != null}">
		        	<s:url action="BINOLCPACT05_confirm" namespace="/cp" id="orderCampaignUrl">
		        		<s:param name="orderMode" value="1"></s:param>
		        		<s:param name="subCampCode" value="subCampaignCode"></s:param>
		        		<s:param name="campMebJson" value="campMebJson"></s:param>
		        		<s:param name="gotCounter" value="gotCounter"></s:param>
		        	</s:url>
		        	<a class="add" href="" onclick="binolmbmbm07.orderCampaignInit('${orderCampaignUrl}');return false;">
			           <span class="ui-icon icon-enable"></span>
			           <span class="button-text"><s:text name="binolmbmbm07_orderCampaignButton" /></span>
			        </a>
		        </s:if>
		        <s:else><s:text name="binolmbmbm07_participate"/></s:else>
	          </td>
	        </tr>
	        </s:iterator>
	      </tbody>
	    </table>
	    </div>
    
    	</div>
    </div>
    
    </div>
    
    <div class="hide" id="orderCampaignDiv">
    	<div style="padding: 30px 0 0 20px">
    	<form id="orderCampaignForm">
    	<div style="margin-bottom: 25px">
    		<label style="margin-right: 10px;"><s:text name="binolmbmbm07_orderCampaignTimes" /></label>
    		<s:textfield name="times" value="1"></s:textfield>
    	</div>
    	<div>
			<label style="margin-right: 0"><s:text name="binolmbmbm07_orderCntCode" /></label>
			<span id="orderCntDiv"></span>
			<s:hidden name="organizationId"></s:hidden>
			<s:hidden name="orderCntCode"></s:hidden>
			<s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
			<a class="add" onclick="binolmbmbm07.popOrderCntDialog('${searchCounterInitUrl}');return false;">
			<span class="ui-icon icon-search"></span>
			<span class="button-text"><s:text name="global.page.Popselect" /></span>
			</a>
		</div>
		</form>
		</div>
    </div>
    <div class="hide">
    	<div id="orderCampaignDialogTitle"><s:text name="binolmbmbm07_orderCampaignDialogTitle" /></div>
		<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
		<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
		<s:url action="BINOLMBMBM07_init" id="currentCampaignInitUrl"></s:url>
		<a id="currentCampaignInitUrl" href="${currentCampaignInitUrl}"></a>
    </div>
</s:i18n>