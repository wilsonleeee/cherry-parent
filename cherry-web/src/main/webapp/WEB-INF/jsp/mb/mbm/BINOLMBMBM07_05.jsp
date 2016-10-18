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
	  <span><s:text name="binolmbmbm07_memCampaignOrder" /></span>
	</div>
	<div id="actionResultDisplay"></div>
	<div class="panel-content clearfix">
	
	<div class="section">
        <div class="section-content">
		
		<div id="campaignDataTableDiv" style="overflow: auto;width: 100%;">
		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table memEventDataTable" width="100%">
	      <thead>
	        <tr>
	          <th><s:text name="binolmbmbm07_billNo"/></th>
	          <th><s:text name="binolmbmbm07_groupName"/></th>
	          <th><s:text name="binolmbmbm07_counterOrder"/></th>
	          <th><s:text name="binolmbmbm07_counterGot"/></th>
	          <th><s:text name="binolmbmbm07_couponCode"/></th>
	          <th><s:text name="binolmbmbm07_campOrderTime"/></th>
	          <th><s:text name="binolmbmbm07_totalQuantity"/></th>
	          <th><s:text name="binolmbmbm07_totalAmout"/></th>
	          <th><s:text name="binolmbmbm07_operate"/></th>
	        </tr>
	      </thead>
	      <tbody id="campaignDataTable">
	        <s:iterator value="campaignOrderList" id="campaignOrderMap" status="status">
	        <tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
	          <td>
	            <s:property value="billNo"/>
	            <s:url action="BINOLMBMBM07_searchOrderDetail" id="searchOrderDetailUrl">
				  <s:param name="campOrderId" value="%{#campaignOrderMap.campOrderId}"></s:param>
				</s:url>
				<input value="${searchOrderDetailUrl }" type="hidden"/>
	          </td>
	          <td><s:property value="campaignName"/></td>
	          <td><s:property value="counterOrder"/></td>
	          <td><s:property value="counterGot"/></td>
	          <td><s:property value="couponCode"/></td>
	          <td><s:property value="campOrderTime"/></td>
	          <td>
	            <s:if test='%{#campaignOrderMap.quantity != null && !"".equals(#campaignOrderMap.quantity)}'>
	            	<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
	            </s:if>
	          </td>
	          <td>
	          	 <s:if test='%{#campaignOrderMap.amout != null && !"".equals(#campaignOrderMap.amout)}'>
	            	<s:text name="format.price"><s:param value="amout"></s:param></s:text>
	            </s:if>
	          </td>
	          <td class="operateTd">
		        <s:if test="%{#campaignOrderMap.cancelStatus == 1}">
		        <s:url action="BINOLCPACT06_optionRun" namespace="/cp" id="cancelCampaignUrl">
	        		<s:param name="campOrderId" value="campOrderId"></s:param>
	        		<s:param name="newState">CA</s:param>
	        	</s:url>
	        	<a class="add" href="" onclick="binolmbmbm07.cancelCampaignInit('${cancelCampaignUrl}');return false;">
		           <span class="ui-icon icon-disable"></span>
		           <span class="button-text"><s:text name="binolmbmbm07_cancelCampaignButton" /></span>
		        </a>
		        </s:if>
	          </td>
	        </tr>
	        </s:iterator>
	      </tbody>
	    </table>
	    </div>
    
    	</div>
    </div>
    
    </div>
    
    <div class="hide">
    	<div id="cancelCampaignDialogTitle"><s:text name="binolmbmbm07_cancelCampaignDialogTitle" /></div>
    	<div id="cancelCampaignDialogText"><s:text name="binolmbmbm07_cancelCampaignDialogText" /></div>
		<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
		<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
		<s:url action="BINOLMBMBM07_searchCampaignOrder" id="searchCampaignOrderUrl"></s:url>
		<a id="searchCampaignOrderUrl" href="${searchCampaignOrderUrl}"></a>
    </div>
</s:i18n>