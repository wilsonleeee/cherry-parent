<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="null != detailList">
<div style="max-height:400px;overflow-y:auto;overflow-x:hidden">
<ul>
<s:iterator value="detailList" status="status">
<li>
<span><s:if test="#status.index lt 9">0</s:if><s:property value='#status.index+1'/>.
&nbsp;&nbsp;<s:property value="TradeNoIF"/>
&nbsp;&nbsp;<s:property value="StockInOutDate"/>
&nbsp;&nbsp;(<s:property value="EmployeeCode"/>)
<s:property value="EmployeeName"/>&nbsp;&nbsp;
<s:property value="Quantity"/></span>
</li>
</s:iterator>
</ul>
</div>
</s:if>
