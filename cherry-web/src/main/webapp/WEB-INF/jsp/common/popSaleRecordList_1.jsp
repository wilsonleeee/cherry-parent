<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="saleRecordList" id="saleRecordMap">
<ul>
<li><span><input type="radio" name="billCode" value="<s:property value="#saleRecordMap.billCode"/>"/></span></li>
<li><span><s:property value="billCode"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1055", #saleRecordMap.saleType)' /></span></li>
<li><span><s:property value="departCode"/></span></li>
<li><span><s:property value="saleTime"/></span></li>
<li><span>
<s:if test='%{#saleRecordMap.quantity != null && !"".equals(#saleRecordMap.quantity)}'>
	<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
</s:if>
</span></li>
<li><span>
<s:if test='%{#saleRecordMap.amount != null && !"".equals(#saleRecordMap.amount)}'>
	<s:text name="format.price"><s:param value="amount"></s:param></s:text>
</s:if>
</span></li>
</ul>
</s:iterator>
</div>