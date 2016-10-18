<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="memSaleList" id="memSaleMap" status="status">
<ul>
<li><span><s:property value="RowNumber"/></span></li>
<li><span>
  <s:url id="detailsUrl" action="BINOLPTRPS14_init" namespace="/pt">
	<s:param name="saleRecordId" value="%{#memSaleMap.saleRecordId}"></s:param>
  </s:url>
  <a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
	<s:property value="billCode"/>
  </a>
</span></li>
<li><span><s:property value="saleTime"/></span></li>
<li><span><s:property value="memCode"/></span></li>
<li><span><s:property value="employeeName"/></span></li>
<li><span>
<s:if test='%{#memSaleMap.quantity != null && !"".equals(#memSaleMap.quantity)}'>
<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
</s:if>
</span></li>
<li><span>
<s:if test='%{#memSaleMap.amount != null && !"".equals(#memSaleMap.amount)}'>
<s:text name="format.price"><s:param value="amount"></s:param></s:text>
</s:if>
</span></li>
</ul>
</s:iterator>
</div>
<s:if test="memSaleCountInfo != null && memSaleCountInfo.size() != 0">
	<s:i18n name="i18n.wp.BINOLWRMRP05">
	<div id="headInfo">
		<s:text name="WRMRP05_sumQuantity"/>
		<span class="<s:if test='memSaleCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="memSaleCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="WRMRP05_sumAmount"/>
	    <span class="<s:if test='memSaleCountInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="memSaleCountInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	</s:i18n>
</s:if> 