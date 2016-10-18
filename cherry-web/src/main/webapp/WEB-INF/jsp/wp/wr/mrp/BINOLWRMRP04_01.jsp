<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="giftDrawList" id="giftDrawMap" status="status">
<ul>
<li><span><s:property value="RowNumber"/></span></li>
<li><span>
  <s:url id="detailsUrl" action="BINOLCPACT08_init" namespace="/cp">
	<s:param name="giftDrawId" value="%{#giftDrawMap.giftDrawId}"></s:param>
  </s:url>
  <a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
	<s:property value="billNo"/>
  </a>
</span></li>
<li><span><s:property value="tradeDateTime"/></span></li>
<li><span>
<s:url action="BINOLMBMBM10_init" id="memberDetailUrl" namespace="/mb">
	<s:param name="memberInfoId" value="%{#giftDrawMap.memberInfoId}"></s:param>
</s:url>
<a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
	<s:property value="memCode"/>
</a>
</span></li>
<li><span><s:property value="memName"/></span></li>
<li><span><s:property value="employeeName"/></span></li>
<li><span>
<s:if test='%{#giftDrawMap.quantity != null && !"".equals(#giftDrawMap.quantity)}'>
<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
</s:if>
</span></li>
<li><span>
<s:if test='%{#giftDrawMap.amount != null && !"".equals(#giftDrawMap.amount)}'>
<s:text name="format.price"><s:param value="amount"></s:param></s:text>
</s:if>
</span></li>
</ul>
</s:iterator>
</div>
<s:if test="giftDrawCountInfo != null && giftDrawCountInfo.size() != 0">
	<s:i18n name="i18n.wp.BINOLWRMRP04">
	<div id="headInfo">
		<s:text name="WRMRP04_sumQuantity"/>
		<span class="<s:if test='giftDrawCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="giftDrawCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="WRMRP04_sumAmount"/>
	    <span class="<s:if test='giftDrawCountInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="giftDrawCountInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	</s:i18n>
</s:if> 