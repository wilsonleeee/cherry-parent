<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript">
$(document).ready(function() {
	var hasRelevantSRCode = '<s:property value="pointInfoMap.hasRelevantSRCode"/>';
	if(hasRelevantSRCode) {
		tableRowspan($("#pointDetailTable"),[2]);
		tableRowspanRef($("#pointDetailTable"),[3,4,5,6,7],1);
	} else {
		tableRowspanRef($("#pointDetailTable"),[2,3,4,5,6],1);
	}
});
</script>

<s:i18n name="i18n.mb.BINOLMBPTM03">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="binolmbpmt03_pointManage"/> &gt; <s:text name="binolmbpmt03_pointDetail"/>
		       </span>
		    </div>
	  	</div>
	  	<div class="panel-content">
	  		<%-- 基本信息 --%>
	  		<div class="section">
	            <div class="section-header">
	            	<strong>
	            		<span class="ui-icon icon-ttl-section-info"></span>
	            		<s:text name="global.page.title"/>
	            	</strong>
	            </div>
	            <div class="section-content">
	                <table class="detail" cellpadding="0" cellspacing="0">
		              	<tr>
			                <th><s:text name="binolmbptm02_memCode"/></th>
			                <td><span><s:property value="pointInfoMap.memCode"/></span></td>
			                <th><s:text name="binolmbptm03_billCode"/></th>
			                <td><span><s:property value="pointInfoMap.billCode"/></span></td>
		             	</tr>
		             	<tr>
			            	<th><s:text name="binolmbpmt03_amount"/></th>
			                <td><span>
			                <s:if test='%{pointInfoMap.amount != null && !"".equals(pointInfoMap.amount)}'>
			                <s:text name="format.price"><s:param value="pointInfoMap.amount"></s:param></s:text>
			                </s:if>
			                </span></td>
			                <th><s:text name="binolmbpmt03_changeDate"/></th>
			                <td><span><s:property value="pointInfoMap.changeDate"/></span></td>
		             	</tr>
		             	<tr>
			                <th><s:text name="binolmbpmt03_quantity"/></th>
			                <td><span>
			                <s:if test='%{pointInfoMap.quantity != null && !"".equals(pointInfoMap.quantity)}'>
			                <s:text name="format.number"><s:param value="pointInfoMap.quantity"></s:param></s:text>
			                </s:if>
			                </span></td>
			                <th><s:text name="binolmbptm03_tradeType"/></th>
			                <td><span><s:property value='#application.CodeTable.getVal("1213", pointInfoMap.billType)' /></span></td>		                
		             	</tr>
		             	<tr>
			                <th><s:text name="binolmbpmt03_point"/></th>
			                <td><span><s:property value="pointInfoMap.point"/></span></td>
			                <th><s:text name="binolmbpmt03_departName"/></th>
			                <td><span><s:property value="pointInfoMap.departCode"/></span></td>				                
		             	</tr>
		             	<tr>
			                <th><s:text name="binolmbpmt03_employeeName"/></th>
			                <td colspan="3"><span><s:property value="pointInfoMap.employeeCode"/></span></td>                
		             	</tr>
	         		</table>
	        	</div>
	        </div>
	           
            <%-- 明细信息 --%>
            <div class="section">
	          <div class="section-header">
		          <strong><span class="ui-icon icon-ttl-section-search-result"></span>
		          <s:text name="binolmbpmt03_pointInfoDetail" />
		          </strong>
	          </div>
	          <div class="section-content" style="overflow: auto;">
	            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="pointDetailTable" style="width: 100%;">
	              <tbody>
	                <tr>
	                  <s:if test="%{pointInfoMap.hasRelevantSRCode != null}">
	                  <th><s:text name="binolmbpmt03_relevantSRCode" /></th>
	                  </s:if> 
	                  <th width="10%"><s:text name="binolmbpmt03_proName" /></th>
	                  <th width="8%"><s:text name="binolmbpmt03_unitCode" /></th>
	                  <th width="8%"><s:text name="binolmbpmt03_barCode" /></th>
	                  <th width="8%"><s:text name="binolmbpmt03_saleType" /></th>
	                  <th width="6%"><s:text name="binolmbpmt03_proPrice" /></th>
	                  <th width="6%"><s:text name="binolmbpmt03_proQuantity" /></th>
	                  <th width="6%"><s:text name="binolmbpmt03_proPoint" /></th>
	                  <th width="8%"><s:text name="binolmbpmt03_pointType" /></th>
	                  <th width="10%"><s:text name="binolmbpmt03_combCampaignName" /></th>
	                  <th width="10%"><s:text name="binolmbpmt03_mainCampaignName" /></th>
	                  <th width="10%"><s:text name="binolmbpmt03_subCampaignName" /></th>
	                  <th width="10%"><s:text name="binolmbpmt03_reason" /></th>                  
	                </tr>
	                <s:iterator value="pointInfoMap.pointInfoDetail" id="pointInfoDetailMap" status="status">
	                <s:if test='%{#pointInfoDetailMap.relevantSRCode != null && !"".equals(#pointInfoDetailMap.relevantSRCode)}'><s:set name="tdClassValue" value="'red'"></s:set></s:if>
	                <tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">           
	                  <s:if test="%{pointInfoMap.hasRelevantSRCode != null}">
			          <td class="hide"><s:property value="#pointInfoDetailMap.relevantSRCode" />-<s:property value="#pointInfoDetailMap.prmPrtVendorId" /></td>
			          <td class='<s:property value="tdClassValue" />'><s:property value="#pointInfoDetailMap.relevantSRCode" /></td>
			          </s:if>
			          <s:else>
			          <td class="hide"><s:property value="#pointInfoDetailMap.prmPrtVendorId" /></td>
			          </s:else>      
	                  <td class='td_point <s:property value="tdClassValue" />'>
	                  <s:if test='%{"N".equals(#pointInfoDetailMap.saleType)}'>
	                    <s:property value="#pointInfoDetailMap.proName" />
	                  </s:if>
	                  <s:if test='%{"P".equals(#pointInfoDetailMap.saleType)}'>
	                    <s:property value="#pointInfoDetailMap.prmProName" />
	                  </s:if>
	                  </td>
	                  <td class='<s:property value="tdClassValue" />'><s:property value="#pointInfoDetailMap.unitCode" /></td>
	                  <td class='<s:property value="tdClassValue" />'><s:property value="#pointInfoDetailMap.barCode" /></td>
	                  <td class='<s:property value="tdClassValue" />'><s:property value='#application.CodeTable.getVal("1106", #pointInfoDetailMap.saleType)' /></td>
	                  <td class='<s:property value="tdClassValue" />'>
	                  <s:if test='%{#pointInfoDetailMap.price != null && !"".equals(#pointInfoDetailMap.price)}'>
	                    <s:text name="format.price"><s:param value="#pointInfoDetailMap.price"></s:param></s:text>
	                  </s:if>
	                  </td>
	                  <td class='<s:property value="tdClassValue" />'>
	                  <s:if test='%{#pointInfoDetailMap.quantity != null && !"".equals(#pointInfoDetailMap.quantity)}'>
	                    <s:text name="format.number"><s:param value="#pointInfoDetailMap.quantity"></s:param></s:text>
	                  </s:if>
	                  </td>
	                  <td class='<s:property value="tdClassValue" />'><s:property value="#pointInfoDetailMap.point" /></td>
	                  <td class='<s:property value="tdClassValue" />'><s:property value='#application.CodeTable.getVal("1214", #pointInfoDetailMap.pointType)' /></td>
	                  <td class='td_point <s:property value="tdClassValue" />'><s:property value="#pointInfoDetailMap.combCampaignName" /></td>
	                  <td class='td_point <s:property value="tdClassValue" />'><s:property value="#pointInfoDetailMap.mainCampaignName" /></td>
	                  <td class='td_point <s:property value="tdClassValue" />'><s:property value="#pointInfoDetailMap.subCampaignName" /></td>
	                  <td class='td_point <s:property value="tdClassValue" />'><s:property value="#pointInfoDetailMap.reason" /></td>
	                </tr>
	                </s:iterator>
	              </tbody>
	            </table>
	          </div>
            </div>
            
            <div class="center clearfix">
	            <button class="close" type="button"  onclick="window.close();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
	  	</div>
	</div>
</div>
</s:i18n>