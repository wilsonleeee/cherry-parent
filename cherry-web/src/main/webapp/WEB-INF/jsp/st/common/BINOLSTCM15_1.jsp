<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="headInfo">
    <span id="OD_DepotCodeName"><s:property value="inventoryMap.DepotCodeName"/></span>
    <span id="OD_LogicInventoryCodeName"><s:property value="inventoryMap.LogicInventoryCodeName"/></span>
</div>
<div id="aaData">
<s:iterator value="stockList" id="stockMap" status="status">
    <ul>
        <li><span><s:property value="#status.index+1"/></span></li>
        <li><span><s:property value="#stockMap.UnitCode"/></span></li>
        <li><span><s:property value="#stockMap.BarCode"/></span></li>
        <li><span><s:property value="#stockMap.ProductName"/></span></li>
        <li>
            <s:if test="#stockMap.startQuantity >= 0"><span class="right"><s:text name="format.number"><s:param value="#stockMap.startQuantity"></s:param></s:text></span></s:if>
            <s:else><span class="right highlight"><s:text name="format.number"><s:param value="#stockMap.startQuantity"></s:param></s:text></span></s:else>
        </li>
        <li>
            <s:if test="#stockMap.endQuantity >= 0"><span class="right" style="width:100%;text-align: right;background-color: #FFFCC2;"><s:text name="format.number"><s:param value="#stockMap.endQuantity"></s:param></s:text></span></s:if>
            <s:else><span class="right highlight" style="width:100%;text-align: right;background-color: #FFFCC2;"><s:text name="format.number"><s:param value="#stockMap.endQuantity"></s:param></s:text></span></s:else>
        </li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.inQuantity1"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.inQuantity2"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.inQuantity3"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.inQuantity4"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.inQuantity5"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.inQuantity6"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.inQuantity7"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.outQuantity1"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.outQuantity2"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.outQuantity3"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.outQuantity4"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.outQuantity5"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.outQuantity6"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.outQuantity7"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.outQuantity8"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.outQuantity9"/></s:text></span></li>
        <li><span class="right"><s:text name="format.number"><s:param value="#stockMap.outQuantity10"/></s:text></span></li>
    </ul>
</s:iterator>
</div>