<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBMBM07">

<div class="detail_box">
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
          <th><s:text name="binolmbmbm07_billNo"/></th>
          <td><span><s:property value="campaignOrderDetail.billNo"/></span></td>
          <th><s:text name="binolmbmbm07_campOrderTime"/></th>
          <td><span><s:property value="campaignOrderDetail.campOrderTime"/></span></td>
       	</tr>
       	<tr>
       	  <th><s:text name="binolmbmbm07_groupName"/></th>
          <td><span><s:property value="campaignOrderDetail.campaignName"/></span></td>  
          <th><s:text name="binolmbmbm07_counterOrder"/></th>
          <td><span><s:property value="campaignOrderDetail.counterOrder"/></span></td>
       	</tr>
       	<tr>
          <th><s:text name="binolmbmbm07_orderState"/></th>
          <td><span><s:property value='#application.CodeTable.getVal("1116", campaignOrderDetail.state)' /></span></td>
          <th><s:text name="binolmbmbm07_counterGot"/></th>
          <td><span><s:property value="campaignOrderDetail.counterGot"/></span></td>		                
       	</tr>
       	<tr>
          <th><s:text name="binolmbmbm07_sendFlag"/></th>
          <td><span>
          	<s:if test ='"0".equals(campaignOrderDetail.sendFlag)'><s:text name="binolmbmbm07_sendFlag0"/></s:if>
	        <s:if test ='"1".equals(campaignOrderDetail.sendFlag)'><s:text name="binolmbmbm07_sendFlag1"/></s:if>
	        <s:if test ='"2".equals(campaignOrderDetail.sendFlag)'><s:text name="binolmbmbm07_sendFlag2"/></s:if>
	        <s:if test ='"3".equals(campaignOrderDetail.sendFlag)'><s:text name="binolmbmbm07_sendFlag3"/></s:if>
          </span></td>
          <th><s:text name="binolmbmbm07_getFromTime"/></th>
          <td><span><s:property value="campaignOrderDetail.getFromTime"/></span></td>			                
       	</tr>
       	<tr>
          <th><s:text name="binolmbmbm07_couponCode"/></th>
          <td><span><s:property value="campaignOrderDetail.couponCode"/></span></td>
          <th><s:text name="binolmbmbm07_getToTime"/></th>
          <td><span><s:property value="campaignOrderDetail.getToTime"/></span></td>			              
       	</tr>
 	  </table>
	</div>
</div>	

<div class="section">
    <div class="section-header">
      <strong>
      	<span class="ui-icon icon-ttl-section-search-result"></span>
        <s:text name="binolmbmbm07_prtInfoList" />
      </strong>
    </div>  	
  	<div class="section-content clearfix">
      <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="pointDetailTable" style="width: 100%;">
        <tbody>
          <tr>
            <th width="10%"><s:text name="binolmbmbm07_activityName" /></th>
            <th width="8%"><s:text name="binolmbmbm07_unitCode" /></th>
            <th width="8%"><s:text name="binolmbmbm07_barCode" /></th>
            <th width="8%"><s:text name="binolmbmbm07_nameTotal" /></th>
            <th width="6%"><s:text name="binolmbmbm07_price" /></th>
            <th width="6%"><s:text name="binolmbmbm07_pointRequired" /></th>
            <th width="6%"><s:text name="binolmbmbm07_quantity" /></th>
          </tr>
          <s:iterator value="campaignOrderDetail.prtInfoList" id="prtInfoMap" status="status">
          <tr>           
            <td>[<s:property value="subCampCode"/>]<s:property value="subCampName"/></td>
            <td><s:property value="unitCode" /></td>
            <td><s:property value="barCode" /></td>
            <td><s:property value="nameTotal"/></td>
            <td>
              <s:if test='%{amout != null && !"".equals(amout)}'>
            	<s:text name="format.price"><s:param value="amout"></s:param></s:text>
              </s:if>
            </td>
            <td>
              <s:if test='%{pointRequired != null && !"".equals(pointRequired)}'>
            	<s:text name="format.number"><s:param value="pointRequired"></s:param></s:text>
              </s:if>	
            </td>
            <td>
              <s:if test='%{quantity != null && !"".equals(quantity)}'>
            	<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
              </s:if>
            </td>
          </tr>
          </s:iterator>
        </tbody>
      </table>
      
      <hr class="space" />
	  <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
		<tr>
			<th rowspan="2" scope="row" class="center"><s:text name="binolmbmbm07_totalText" /></th>
			<td class="center"><s:text name="binolmbmbm07_totalQuantity" /></td>
			<td class="center"><s:text name="binolmbmbm07_totalAmout" /></td>
			<td class="center"><s:text name="binolmbmbm07_totalPointRequired" /></td>
		</tr>
		<tr>
			<td class="center">
			  <s:if test='%{campaignOrderDetail.quantity != null && !"".equals(campaignOrderDetail.quantity)}'>
				<s:if test="campaignOrderDetail.quantity >= 0">
					<s:text name="format.number"><s:param value="campaignOrderDetail.quantity"></s:param></s:text>
				</s:if> 
				<s:else>
					<span class="highlight"><s:text name="format.number"><s:param value="campaignOrderDetail.quantity"></s:param></s:text></span>
				</s:else>
		      </s:if>
			</td>
			<td class="center">
			  <s:if test='%{campaignOrderDetail.amout != null && !"".equals(campaignOrderDetail.amout)}'>	
				<s:if test="campaignOrderDetail.amout>= 0">
					<s:text name="format.price"><s:param value="campaignOrderDetail.amout"></s:param></s:text>
				</s:if> 
				<s:else>
					<span class="highlight"><s:text name="format.price"><s:param value="campaignOrderDetail.amout"></s:param></s:text></span>
				</s:else>
			  </s:if>	
			</td>
			<td class="center">
			  <s:if test='%{campaignOrderDetail.pointRequired != null && !"".equals(campaignOrderDetail.pointRequired)}'>	
				<s:if test="campaignOrderDetail.pointRequired>= 0">
					<s:text name="format.number"><s:param value="campaignOrderDetail.pointRequired"></s:param></s:text>
				</s:if> 
				<s:else>
					<span class="highlight"><s:text name="format.price"><s:param value="campaignOrderDetail.pointRequired"></s:param></s:text></span>
				</s:else>
			  </s:if>	
			</td>
		</tr>
	  </table>
    </div>
</div>
</div>  
	  	
</s:i18n>