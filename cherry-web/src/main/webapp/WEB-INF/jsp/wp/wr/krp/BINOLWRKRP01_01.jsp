<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="proStockList" id="bigClassMap">
<s:set name="bigIndex" value="0" />
<s:iterator value="list" id="smallClassMap">
<s:set name="smallIndex" value="0" />
<s:iterator value="list" id="productMap">
<ul>
<li><s:property value="RowNumber"/></li>
<s:if test="%{#bigIndex == 0}">
<s:set name="bigIndex" value="1" />
<li><s:property value="%{#bigClassMap.bigClassCode}"/></li>
<li><s:property value="%{#bigClassMap.bigClassName}"/></li>
</s:if>
<s:else>
<li></li>
<li></li>
</s:else>
<s:if test="%{#smallIndex == 0}">
<s:set name="smallIndex" value="1" />
<li><s:property value="%{#smallClassMap.smallClassCode}"/></li>
<li><s:property value="%{#smallClassMap.smallClassName}"/></li>
</s:if>
<s:else>
<li></li>
<li></li>
</s:else>
<li>
<s:url id="detailInitUrl" action="BINOLPTRPS12_init" namespace="/pt">
  <s:param name="prtVendorId" value="%{#productMap.productVendorId}"></s:param>
  <s:param name="startDate" value="%{startDate}"></s:param>
  <s:param name="endDate" value="%{endDate}"></s:param>
</s:url>
<a href='${detailInitUrl}&params=${params}' class="popup" onclick="javascript:openWin(this);return false;">
  <s:property value="productName" />
</a>
</li>
<li><s:property value="unitCode"/></li>
<li><s:property value="barCode"/></li>
<li><s:text name="format.number"><s:param value="quantity"></s:param></s:text></li>
</ul>
</s:iterator>
</s:iterator>
</s:iterator>
</div>
<s:if test="proStockCountInfo != null && proStockCountInfo.size() != 0">
	<s:i18n name="i18n.wp.BINOLWRKRP01">
	<div id="headInfo">
		<s:text name="WRKRP01_sumQuantity"/>
		<span class="<s:if test='proStockCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="proStockCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
	</div>
	</s:i18n>
</s:if> 