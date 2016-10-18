<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM31">
<div id="headInfo">
	<s:text name="PRM31_sumStartQuantity"/>
	<span class="<s:if test='sumInfo.sumStartQuantity < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumStartQuantity"></s:param></s:text></strong>
	</span>
	<s:text name="PRM31_sumInQuantity"/>
	<span class="<s:if test='sumInfo.sumInQuantity < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumInQuantity"></s:param></s:text></strong>
	</span>
	<s:text name="PRM31_sumOutQuantity"/>
	<span class="<s:if test='sumInfo.sumOutQuantity < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumOutQuantity"></s:param></s:text></strong>
	</span>
	<s:text name="PRM31_sumEndQuantity"/>
	<span class="<s:if test='sumInfo.sumEndQuantity < 0'>highlight</s:if><s:else>green</s:else>">
    	<strong><s:text name="format.number"><s:param value="sumInfo.sumEndQuantity"></s:param></s:text></strong>
    </span>
</div>
<div id="aaData">
	<s:iterator value="proStockList">
		<s:url id="detailsUrl" action="BINOLSSPRM32_init">
			<s:param name="prmVendorId"><s:property value="prmVendorId"/></s:param>
			<s:param name="startDate"><s:property value="startDate"/></s:param>
			<s:param name="endDate"><s:property value="endDate"/></s:param>
		</s:url>
		<ul>
			<%-- No. --%>
			<li><s:property value="RowNumber"/></li>
			<%-- 产品名称 --%>
			<li>
				<a href="${detailsUrl}" class="popup" onclick="getDetail(this);return false;">
					<s:property value="nameTotal"/>
				</a>
			</li>
			<%-- 厂商编码  --%>
			<li><span><s:property value="unitCode"/></span></li>
			<%-- 产品条码   --%>
			<li><span><s:property value="barCode"/></span></li>
			<%-- 期初结存 --%>
			<li>
				<s:if test="startQuantity >= 0"><s:text name="format.number"><s:param value="startQuantity"></s:param></s:text></s:if>
				<s:else><span class="highlight"><s:text name="format.number"><s:param value="startQuantity"></s:param></s:text></span></s:else>
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
		</ul>
	</s:iterator>
</div>
</s:i18n>