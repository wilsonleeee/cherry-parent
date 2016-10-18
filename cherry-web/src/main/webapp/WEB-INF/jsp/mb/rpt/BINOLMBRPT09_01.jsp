<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="countByCouterList" id="countByCouterMap" status="status">
<ul>
<li><span><s:property value="cityName"/></span></li>
<li><span><s:property value="departCode"/></span></li>
<li><span><s:property value="departName"/></span></li>
<li><span><s:property value="orderCount"/></span></li>
<li><span><s:property value="bookCount"/></span></li>
<li><span><s:property value="getCount"/></span></li>
<li><span><s:property value="getPercent"/></span></li>
<li><span><s:property value="memCount"/></span></li>
<li><span><s:property value="saleAmount"/></span></li>
<li><span><s:property value="salePercent"/></span></li>
<li><span><s:property value="newMemCount"/></span></li>
<li><span><s:property value="oldMemCount"/></span></li>
<li><span><s:property value="newMemSaleAmount"/></span></li>
<li><span><s:property value="oldMemSaleAmount"/></span></li>
</ul>
</s:iterator>
</div>