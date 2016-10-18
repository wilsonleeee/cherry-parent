<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="saleList" id="saleMap" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li>
  <s:url id="detailsUrl" action="BINOLPTRPS14_init" namespace="/pt">
	<s:param name="saleRecordId" value="%{#saleMap.saleRecordId}"></s:param>
  </s:url>
  <a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
	<s:property value="billCode"/>
  </a>
</li>
<li><s:property value="employeeName"/></li>
<li>
  <s:url action="BINOLMBMBM10_init" id="memberDetailUrl" namespace="/mb">
	<s:param name="memberInfoId" value="%{#saleMap.memberId}"></s:param>
  </s:url>
  <a href="${memberDetailUrl}" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
	<s:property value="memCode"/>
  </a>
</li>
<li><s:property value='#application.CodeTable.getVal("1055", saleType)'/></li>
<li><s:text name="format.number"><s:param value="quantity"></s:param></s:text></li>
<li><s:text name="format.price"><s:param value="amount"></s:param></s:text></li>
<li>
<s:if test='%{#saleMap.discount != null && !"".equals(#saleMap.discount)}'>
<s:text name="format.price"><s:param value="discount"></s:param></s:text>
</s:if>
</li>
<li><s:property value="saleTime"/></li>
</ul>
</s:iterator>
</div>
<s:if test="saleCountInfo != null && saleCountInfo.size() != 0">
	<s:i18n name="i18n.wp.BINOLWRSRP02">
	<div id="headInfo">
		<s:text name="WRSRP02_sumQuantity"/>
		<span class="<s:if test='saleCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="saleCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="WRSRP02_sumAmount"/>
	    <span class="<s:if test='saleCountInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="saleCountInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	</s:i18n>
</s:if> 