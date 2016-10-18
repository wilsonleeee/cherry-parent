<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="pxInfoList" id="pxInfoMap" status="status">
<ul>
<li><span><s:property value="RowNumber"/></span></li>
<li><span>
  <s:url id="detailsUrl" action="BINOLPTRPS14_init" namespace="/pt">
	<s:param name="saleRecordId" value="%{#pxInfoMap.saleRecordId}"></s:param>
  </s:url>
  <a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
	<s:property value="billCode"/>
  </a>
</span></li>
<li><span><s:property value="saleTime"/></span></li>
<li><span>
<s:url action="BINOLMBMBM10_init" id="memberDetailUrl" namespace="/mb">
	<s:param name="memberInfoId" value="%{#pxInfoMap.memberInfoId}"></s:param>
</s:url>
<a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
	<s:property value="memCode"/>
</a>
</span></li>
<li><span><s:property value="memName"/></span></li>
<li><span><s:property value="employeeName"/></span></li>
<li><span>
<s:if test='%{#pxInfoMap.quantity != null && !"".equals(#pxInfoMap.quantity)}'>
<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
</s:if>
</span></li>
<li><span>
<s:if test='%{#pxInfoMap.amount != null && !"".equals(#pxInfoMap.amount)}'>
<s:text name="format.price"><s:param value="amount"></s:param></s:text>
</s:if>
</span></li>
</ul>
</s:iterator>
</div>
<s:if test="pxCountInfo != null && pxCountInfo.size() != 0">
	<s:i18n name="i18n.wp.BINOLWRMRP06">
	<div id="headInfo">
		<s:text name="WRMRP06_sumQuantity"/>
		<span class="<s:if test='pxCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="pxCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="WRMRP06_sumAmount"/>
	    <span class="<s:if test='pxCountInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="pxCountInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	</s:i18n>
</s:if> 