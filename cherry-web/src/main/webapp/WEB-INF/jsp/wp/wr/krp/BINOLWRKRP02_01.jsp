<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS11">
<div id="headInfo">
	<s:text name="RPS11_sumStartQuantity"/>
	<span class="<s:if test='sumInfo.sumStartQuantity < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumStartQuantity"></s:param></s:text></strong>
	</span>
	<s:text name="RPS11_sumInQuantity"/>
	<span class="<s:if test='sumInfo.sumInQuantity < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumInQuantity"></s:param></s:text></strong>
	</span>
	<s:text name="RPS11_sumOutQuantity"/>
	<span class="<s:if test='sumInfo.sumOutQuantity < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumOutQuantity"></s:param></s:text></strong>
	</span>
	<s:text name="RPS11_sumEndQuantity"/>
	<span class="<s:if test='sumInfo.sumEndQuantity < 0'>highlight</s:if><s:else>green</s:else>">
    	<strong><s:text name="format.number"><s:param value="sumInfo.sumEndQuantity"></s:param></s:text></strong>
    </span>
	<br/>
    <s:text name="RPS11_sumStartAmount"/>
    <span class="<s:if test='sumInfo.sumStartAmount < 0'>highlight</s:if><s:else>green</s:else>">
        <strong><s:text name="format.price"><s:param value="sumInfo.sumStartAmount"></s:param></s:text></strong>
    </span>
    <s:text name="RPS11_sumEndAmount"/>
    <span class="<s:if test='sumInfo.sumEndAmount < 0'>highlight</s:if><s:else>green</s:else>">
        <strong><s:text name="format.price"><s:param value="sumInfo.sumEndAmount"></s:param></s:text></strong>
    </span>
</div>
<div id="aaData">
	<s:iterator value="proStockList">
		<s:if test='"2".equals(type)'>
		<s:url id="detailsUrl" action="BINOLPTRPS12_init" namespace="/pt">
			<s:param name="productId"><s:property value="productId"/></s:param>
			<s:param name="validFlag"><s:property value="validFlag"/></s:param>
			<s:param name="startDate"><s:property value="startDate"/></s:param>
			<s:param name="endDate"><s:property value="endDate"/></s:param>
		</s:url>
		</s:if>
		<s:else>
		<s:url id="detailsUrl" action="BINOLPTRPS12_init" namespace="/pt">
			<s:param name="prtVendorId"><s:property value="prtVendorId"/></s:param>
			<s:param name="startDate"><s:property value="startDate"/></s:param>
			<s:param name="endDate"><s:property value="endDate"/></s:param>
		</s:url>
		</s:else>
		<ul>
			<%-- No. --%>
			<li><s:property value="RowNumber"/></li>
			<%-- 产品名称 --%>
			<li><a href='${detailsUrl}&params=${params}' class="popup" onclick="javascript:openWin(this);return false;"><s:property value="nameTotal"/></a></li>
			<%-- 厂商编码  --%>
			<li><span><s:property value="unitCode"/></span></li>
			<%-- 产品条码   --%>
			<s:if test='!"2".equals(type)'><li><span><s:property value="barCode"/></span></li></s:if>
			<%-- 计量单位   --%>
			<li><span><s:property value='#application.CodeTable.getVal("1190", moduleCode)'/></span></li>
            <%-- 价格 --%>
            <li>
                <s:text name="format.price"><s:param value="price"></s:param></s:text>
            </li>
			<%-- 期初结存 --%>
			<li>
				<s:if test="startQuantity >= 0"><s:text name="format.number"><s:param value="startQuantity"></s:param></s:text></s:if>
				<s:else><span class="highlight"><s:text name="format.number"><s:param value="startQuantity"></s:param></s:text></span></s:else>
			</li>
            <%-- 期初结存金额 --%>
            <li>
                <s:if test="startAmount >= 0"><s:text name="format.price"><s:param value="startAmount"></s:param></s:text></s:if>
                <s:else><span class="highlight"><s:text name="format.price"><s:param value="startAmount"></s:param></s:text></span></s:else>
            </li>
			<%-- 本期收入 --%>
			<li><s:text name="format.number"><s:param value="inQuantity"></s:param></s:text></li>
			<%-- 本期发出  --%>
			<li><s:text name="format.number"><s:param value="outQuantity"></s:param></s:text></li>
			<%-- 期末结存 --%>
			<li>
				<s:if test="endQuantity >= 0"><s:text name="format.number"><s:param value="endQuantity"></s:param></s:text></s:if>
				<s:else><span class="highlight"><s:text name="format.number"><s:param value="endQuantity"></s:param></s:text></span></s:else>
			</li>
            <%-- 期末结存金额 --%>
            <li>
                <s:if test="endAmount >= 0"><s:text name="format.price"><s:param value="endAmount"></s:param></s:text></s:if>
                <s:else><span class="highlight"><s:text name="format.price"><s:param value="endAmount"></s:param></s:text></span></s:else>
            </li>
		</ul>
	</s:iterator>
</div>
</s:i18n>