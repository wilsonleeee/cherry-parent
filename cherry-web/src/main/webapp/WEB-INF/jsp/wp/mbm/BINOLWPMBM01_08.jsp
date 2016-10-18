<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:if test="%{saleRecordDetail != null && !saleRecordDetail.isEmpty()}">
<s:i18n name="i18n.mb.BINOLMBMBM05">
<div class="detail_box">
<div class="section">
  <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm05_saleBaseDetail"/></strong>
  </div>
  <div class="section-content">
    <table class="detail" cellpadding="0" cellspacing="0">
      <tr>
	    <th><s:text name="binolmbmbm05_billCode"/></th>
	    <td><span><s:property value="saleRecordDetail.billCode"/></span></td>
	    <th><s:text name="binolmbmbm05_quantity"/></th>
	    <td><span>
	    <s:if test='%{saleRecordDetail.quantity != null && !"".equals(saleRecordDetail.quantity)}'>
	 		<s:text name="format.number"><s:param value="saleRecordDetail.quantity"></s:param></s:text>
	 	</s:if>
	    </span></td>
      </tr>
      <tr>
	    <th><s:text name="binolmbmbm05_saleTime"/></th>
	    <td><span><s:property value="saleRecordDetail.saleTime"/></span></td>
	    <th><s:text name="binolmbmbm05_amount"/></th>
	    <td><span>
	    <s:if test='%{saleRecordDetail.amount != null && !"".equals(saleRecordDetail.amount)}'>
	 		<s:text name="format.price"><s:param value="saleRecordDetail.amount"></s:param></s:text>
	 	</s:if>
	    </span></td>
      </tr>
      <tr>
	    <th><s:text name="binolmbmbm05_saleType"/></th>
	    <td><span><s:property value='#application.CodeTable.getVal("1055", saleRecordDetail.saleType)' /></span></td>
	    <th><s:text name="binolmbmbm05_discountAll"/></th>
	    <td><span><s:property value="saleRecordDetail.discount"/></span></td>
      </tr>
      <tr>
      	<th><s:text name="binolmbmbm05_billCodePre"/></th>
	    <td><span><s:property value="saleRecordDetail.billCodePre"/></span></td>
	    <th><s:if test='%{openCounter == "Y"}'><s:text name="binolmbmbm05_departCode"/></s:if></th>
	    <td>
	   	<s:if test='%{openCounter == "Y"}'>
	    <span><s:property value="saleRecordDetail.departCode"/></span>
	    </s:if>
	    </td>
      </tr>
      <tr>
	    <th><s:text name="binolmbmbm05_saleMemCode"/></th>
	    <td><span><s:property value="saleRecordDetail.memberCode"/></span></td>
	    <th><s:text name="binolmbmbm05_saleEmployee"/></th>
	    <td><span><s:property value="saleRecordDetail.employeeCode"/></span></td>
      </tr>
      <tr>
        <th><s:text name="binolmbmbm05_ticketType"/></th>
        <td><span><s:property value='#application.CodeTable.getVal("1261", saleRecordDetail.ticketType)' /></span></td>
      	<th><s:text name="binolmbmbm05_comments"/></th>
	    <td colspan="3"><span><s:property value="saleRecordDetail.comments"/></span>
        </td>
      </tr>
    </table>
  </div>
</div>  

<s:if test="%{saleRecordDetail.payTypeDetailList != null}">
<div class="section">
<div class="section-header">
	<strong>
		<span class="ui-icon icon-money"></span>
		<s:text name="binolmbmbm05_payTypeDetail"/>
	</strong>
</div>
<div class="section-content">
	<div class="group-content clearfix">
		<s:iterator value="saleRecordDetail.payTypeDetailList" id="payTypeDetailInfo">
            <s:set var="payTypeWidthPercent" value="25" />
            <s:set var="showSerialNumber" value="false"/>
            <s:if test='null != #payTypeDetailInfo.serialNumber && !#payTypeDetailInfo.serialNumber.equals("")'>
                <s:set var="payTypeWidthPercent" value="75" />
                <s:set var="showSerialNumber" value="true"/>
            </s:if>
			<span class="left" style="width:<s:property value="#payTypeWidthPercent"/>%;margin:3px 0;">
				<span class="ui-icon icon-arrow-crm"></span>
				<span class="bg_title"><s:property value='#application.CodeTable.getVal("1175", #payTypeDetailInfo.payTypeCode)'/></span>
				<span class="gray">
				<s:if test='%{#payTypeDetailInfo.payAmount != null && !"".equals(#payTypeDetailInfo.payAmount)}'>
			 		<s:text name="format.price"><s:param value="#payTypeDetailInfo.payAmount"/></s:text>
			 	</s:if>
				</span>
				<s:if test='#showSerialNumber == true'>
				    <span style="display:inline-block;">(<s:text name="binolmbmbm05_serialNumber"/>:<s:property value="#payTypeDetailInfo.serialNumber"/>)</span>
			    </s:if>
			</span>
		</s:iterator>
	</div>
</div>
</div>
</s:if>
  
<s:if test="%{saleRecordDetail.saleRecordDetailList != null}">  
<div class="section">
  <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="binolmbmbm05_saleDetail"/></strong>
  </div>  
  <div class="section-content">
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
      <thead>
		  <tr>
			<th><s:text name="binolmbmbm05_proName"/></th>
			<th><s:text name="binolmbmbm05_unitCode"/></th>
			<th><s:text name="binolmbmbm05_barCode"/></th>
			<th><s:text name="binolmbmbm05_amountPortion"/></th>
			<th><s:text name="binolmbmbm05_price"/></th>
			<th><s:text name="binolmbmbm05_pricePay"/></th>
			<th><s:text name="binolmbmbm05_quantity"/></th>
			<th><s:text name="binolmbmbm05_discount"/></th>
			<th><s:text name="binolmbmbm05_detailSaleType"/></th>
			<th><s:text name="binolmbmbm05_campaignName"/></th>
            <s:if test='sysConfigShowUniqueCode.equals("1")'>
                <th><s:text name="binolmbmbm05_UniqueCode"/></th>
            </s:if>
			<th><s:text name="binolmbmbm05_saleExt"/></th>
		  </tr>
      </thead>
      <tbody>
  	  <s:iterator value="%{saleRecordDetail.saleRecordDetailList}" id="saleRecordDetailMap">
	      <tr>
	 	    <td><span>
	 	    <s:if test='%{"N".equals(#saleRecordDetailMap.saleType)}'>
	 	    	<s:property value="#saleRecordDetailMap.prtName"/>
	 	    </s:if>
	 	    <s:else>
	 	    	<s:property value="#saleRecordDetailMap.prmName"/>
	 	    </s:else>
	 	    </span></td>
	 		<td><span><s:property value="#saleRecordDetailMap.unitCode"/></span></td>
	 		<td><span><s:property value="#saleRecordDetailMap.barCode"/></span></td>
	 		<td><span>
	 		<s:if test='%{#saleRecordDetailMap.amountPortion != null && !"".equals(#saleRecordDetailMap.amountPortion)}'>
	 		<s:text name="format.price"><s:param value="#saleRecordDetailMap.amountPortion"></s:param></s:text>
	 		</s:if>
	 		</span></td>
	 		<td><span>
	 		<s:if test='%{#saleRecordDetailMap.price != null && !"".equals(#saleRecordDetailMap.price)}'>
	 		<s:text name="format.price"><s:param value="#saleRecordDetailMap.price"></s:param></s:text>
	 		</s:if>
	 		</span></td>
	 		<td><span>
	 		<s:if test='%{#saleRecordDetailMap.pricePay != null && !"".equals(#saleRecordDetailMap.pricePay)}'>
	 		<s:text name="format.price"><s:param value="#saleRecordDetailMap.pricePay"></s:param></s:text>
	 		</s:if>
	 		</span></td>
	 		<td><span>
	 		<s:if test='%{#saleRecordDetailMap.quantity != null && !"".equals(#saleRecordDetailMap.quantity)}'>
	 		<s:text name="format.number"><s:param value="#saleRecordDetailMap.quantity"></s:param></s:text>
	 		</s:if>
	 		</span></td>
	 		<td><span><s:property value="#saleRecordDetailMap.discount"/></span></td>
	 		<td><span><s:property value='#application.CodeTable.getVal("1106", #saleRecordDetailMap.saleType)' /></span></td>
	 		<td><span><s:property value="#saleRecordDetailMap.activityName"/></span></td>
            <s:if test='sysConfigShowUniqueCode.equals("1")'>
                <%-- 唯一码 --%>
                <td><s:property value="#saleRecordDetailMap.uniqueCode"/></td>
            </s:if>
	 		<td><span><s:property value="#saleRecordDetailMap.saleExt"/></span></td>
		  </tr>
      </s:iterator>
	  </tbody>
    </table>
  </div>
</div>  
</s:if>
  
</div>
</s:i18n>
</s:if>


