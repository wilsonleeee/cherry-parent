<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM09">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="memSaleInfoList" id="memSaleInfoMap" status="status">
<ul>
<li><span>
<s:url action="BINOLMBMBM10_init" id="memberDetailUrl">
	<s:param name="memberInfoId" value="%{#memSaleInfoMap.memberId}"></s:param>
</s:url>
<a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
	<s:property value="memberCode"/>
</a>
</span></li>
<li><span>
<s:url id="saleDetailsUrl" value="/pt/BINOLPTRPS14_init">
	<s:param name="saleRecordId">${memSaleInfoMap.saleRecordId}</s:param>
	<s:param name="fromFlag" value="1"></s:param>
</s:url>
<a href="${saleDetailsUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
	<s:property value="billCode"/>
</a>
</span></li>
<li><span><s:property value='#application.CodeTable.getVal("1055", #memSaleInfoMap.saleType)' /></span></li>
<li><span><s:property value="saleTime"/></span></li>
<cherry:show domId="BINOLMBMBM10_29">
<li><span>
<s:if test='%{departName != null && !"".equals(departName)}'>
(<s:property value="departCode"/>)<s:property value="departName"/>
</s:if>
<s:elseif test='%{departCode != null && !"".equals(departCode)}'>
<s:property value="departCode"/>
</s:elseif>
</span></li>
</cherry:show>
<li><span>
<s:if test='%{#memSaleInfoMap.amount != null && !"".equals(#memSaleInfoMap.amount)}'>
	<s:text name="format.price"><s:param value="amount"></s:param></s:text>
</s:if>
</span></li>
<li><span>
<s:if test='%{#memSaleInfoMap.quantity != null && !"".equals(#memSaleInfoMap.quantity)}'>
	<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
</s:if>
</span></li>
</ul>
</s:iterator>
</div>

<s:if test="%{saleCountInfo != null}">
	<div id="saleCountInfo">
		<s:text name="binolmbmbm09_sumQuantity"/>
		<span class="<s:if test='saleCountInfo.quantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong>
			<s:if test="%{saleCountInfo.quantity != null}">
				<s:text name="format.number"><s:param value="saleCountInfo.quantity"></s:param></s:text>
			</s:if>
			<s:else>0</s:else>
			</strong>
		</span>
		<s:text name="binolmbmbm09_sumAmount"/>
	    <span class="<s:if test='saleCountInfo.amount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong>
	    	<s:if test="%{saleCountInfo.amount != null}">
				<s:text name="format.price"><s:param value="saleCountInfo.amount"></s:param></s:text>
			</s:if>
			<s:else>0</s:else>
	    	</strong>
	    </span>
	</div>
</s:if>
</s:i18n>