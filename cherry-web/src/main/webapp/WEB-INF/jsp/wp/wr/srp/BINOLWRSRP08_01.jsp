<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="heroList" id="heroList" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="employeeName"/></li>
<li><s:property value="employeeCode" /></li>
<li><s:text name="format.number"><s:param value="memCount"></s:param></s:text></li>
<li><s:text name="format.number"><s:param value="quantity"></s:param></s:text></li>
<li><s:text name="format.price"><s:param value="amount"></s:param></s:text></li>
<li><s:text name="format.number"><s:param value="ranking"></s:param></s:text></li>
</ul>
</s:iterator>
</div>
<s:if test="heroCountInfo != null && heroCountInfo.size() != 0">
	<s:i18n name="i18n.wp.BINOLWRSRP08">
	<div id="headInfo">
		<s:text name="WRSRP08_sumQuantity"/>
		<span class="<s:if test='heroCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="heroCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="WRSRP08_sumAmount"/>
	    <span class="<s:if test='heroCountInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="heroCountInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	</s:i18n>
</s:if> 
