<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRSRP06">
<div class="section">
  <div class="section-header">
  	<strong>
  		<span class="ui-icon icon-ttl-section-search-result"></span>
  		<s:text name="global.page.list"/>
  	</strong>
  </div>
  <div class="section-content">
  	<div class="toolbar clearfix">
  	<cherry:show domId="BINOLWRSRP06EXP">
	  <a id="exportExcel" class="export" href="javascript:void(0);">
     	<span class="ui-icon icon-export"></span>
    	<span class="button-text"><s:text name="global.page.exportExcel"/></span>
      </a>
      <a id="exportCsv" class="export" href="javascript:void(0);">
    	<span class="ui-icon icon-export"></span>
    	<span class="button-text"><s:text name="global.page.exportCsv"/></span>
      </a>
      </cherry:show>
	  <span id="headInfo">
	    <s:text name="WRSRP06_sumQuantity"/>
		<span class="<s:if test='saleByDayCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="saleByDayCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="WRSRP06_sumAmount"/>
	    <span class="<s:if test='saleByDayCountInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="saleByDayCountInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	  </span>
	</div>
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
      <thead>
        <tr>              
          <th><s:text name="WRSRP06_number" /></th>
          <th><s:text name="WRSRP06_saleDate" /></th>
          <th><s:text name="WRSRP06_quantity" /></th>
          <th><s:text name="WRSRP06_amount" /></th>
          <th><s:text name="WRSRP06_joinRate" /></th>
          <th><s:text name="WRSRP06_memCount" /></th>
          <th><s:text name="WRSRP06_memAmountRate" /></th>
        </tr>
      </thead>   
      <tbody>
        <s:iterator value="saleByDateList" id="saleByDateMap" status="status">
        <tr class='<s:if test="%{#status.index%2==0}">even</s:if><s:else>odd</s:else>'>    
          <td><s:property value="%{#status.index+1}"/></td>
	      <td>
	      	<s:url id="detailInitUrl" action="BINOLWRSRP06_detailInit">
			  <s:param name="saleDate" value="%{saleDate}"></s:param>
			  <s:param name="employeeId" value="%{employeeId}"></s:param>
			  <s:param name="employeeName" value="%{employeeName}"></s:param>
			  <s:param name="saleType" value="%{saleType}"></s:param>
			  <s:param name="payTypeCode" value="%{payTypeCode}"></s:param>
			</s:url>
			<a href="${detailInitUrl}" class="popup" onclick="javascript:openWin(this);return false;">
			  <s:property value="saleDate"/>
			</a>
	      </td>
	      <td>
	        <s:if test="%{#saleByDateMap.quantity != null}">
	          <s:text name="format.number"><s:param value="quantity"></s:param></s:text>
	        </s:if>
	      </td>
	      <td>
	        <s:if test="%{#saleByDateMap.amount != null}">
	          <s:text name="format.price"><s:param value="amount"></s:param></s:text>
	        </s:if>
	      </td>
	      <td><s:property value="joinRate"/></td>
	      <td><s:property value="memCount"/></td>
	      <td><s:property value="memAmountRate"/></td>
	    </tr>
        </s:iterator>
      </tbody>        
    </table>
  </div>
</div>
</s:i18n>