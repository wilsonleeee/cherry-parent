<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.wp.BINOLWRSRP03">

<div class="section">
  <div class="section-header">
	<strong>
	  <span class="ui-icon icon-ttl-section-search-result"></span>
	  <s:text name="global.page.list"/>
	</strong>
  </div>
  <div class="section-content">
	<div class="toolbar clearfix">
	<cherry:show domId="BINOLWRSRP03EXP">
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
	    <s:text name="WRSRP03_sumQuantity"/>
		<span class="<s:if test='saleCountByClassInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="saleCountByClassInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="WRSRP03_sumAmount"/>
	    <span class="<s:if test='saleCountByClassInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="saleCountByClassInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	  </span>
	</div>
	<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	  <thead>
		<tr>              
	      <th><s:text name="WRSRP03_number" /></th>
	      <th><s:text name="WRSRP03_bigClassCode" /></th>
	      <th><s:text name="WRSRP03_bigClassName" /></th>
	      <th><s:text name="WRSRP03_smallClassCode" /></th>
	      <th><s:text name="WRSRP03_smallClassName" /></th>
	      <th><s:text name="WRSRP03_quantity" /></th>
	      <th><s:text name="WRSRP03_amount" /></th>
		</tr>
	  </thead> 
	  <tbody>
	    <s:set name="index" value="0" />
	    <s:iterator value="saleCountByClassInfo.saleCountByClassList" id="saleCountByClassMap">
	    <tr class="selectedColor">
	      <td><s:set name="index" value="#index+1" /><s:property value="%{#index}"/></td>
	      <td><s:property value="bigClassCode"/></td>
	      <td><s:property value="bigClassName"/></td>
	      <td><s:property value="smallClassCode"/></td>
	      <td><s:property value="smallClassName"/></td>
	      <td><s:text name="format.number"><s:param value="quantity"></s:param></s:text></td>
		  <td><s:text name="format.price"><s:param value="amount"></s:param></s:text></td>
	    </tr>
		    <s:iterator value="#saleCountByClassMap.smallClassList" id="smallClassMap">
		    <tr>
		      <td><s:set name="index" value="#index+1" /><s:property value="%{#index}"/></td>
		      <td></td>
		      <td></td>
		      <td>
		      <s:url id="detailInitUrl" action="BINOLWRSRP03_detailInit">
				<s:param name="bigClassId" value="%{#saleCountByClassMap.bigClassId}"></s:param>
				<s:param name="smallClassId" value="%{#smallClassMap.smallClassId}"></s:param>
				<s:param name="startDate" value="%{startDate}"></s:param>
				<s:param name="endDate" value="%{endDate}"></s:param>
				<s:param name="employeeId" value="%{employeeId}"></s:param>
				<s:param name="employeeName" value="%{employeeName}"></s:param>
				<s:param name="saleType" value="%{saleType}"></s:param>
			  </s:url>
			  <a href="${detailInitUrl}" class="popup" onclick="javascript:openWin(this);return false;">
				<s:property value="smallClassCode"/>
			  </a>
		      </td>
		      <td><s:property value="smallClassName"/></td>
		      <td><s:text name="format.number"><s:param value="quantity"></s:param></s:text></td>
		      <td><s:text name="format.price"><s:param value="amount"></s:param></s:text></td>
		    </tr>
		    </s:iterator>
	    </s:iterator>
	  </tbody>          
	</table>
       
  </div>
</div>


</s:i18n>          