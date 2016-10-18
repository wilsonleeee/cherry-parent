<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="saleByPrtList" id="saleByPrtMap" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="productName"/></li>
<li><s:property value="unitCode" /></li>
<li><s:property value="barCode"/></li>
<li><s:property value='#application.CodeTable.getVal("1190", moduleCode)'/></li>
<li><s:text name="format.price"><s:param value="salePrice"></s:param></s:text></li>
<li><s:text name="format.number"><s:param value="quantity"></s:param></s:text></li>
<li><s:text name="format.price"><s:param value="amount"></s:param></s:text></li>
</ul>
</s:iterator>
</div>
<s:if test="saleByPrtCountInfo != null && saleByPrtCountInfo.size() != 0">
	<s:i18n name="i18n.wp.BINOLWRSRP04">
	<div id="headInfo">
		<s:text name="WRSRP04_sumQuantity"/>
		<span class="<s:if test='saleByPrtCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="saleByPrtCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="WRSRP04_sumAmount"/>
	    <span class="<s:if test='saleByPrtCountInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="saleByPrtCountInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	</s:i18n>
</s:if> 
