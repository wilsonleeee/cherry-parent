<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="deliverList" id="deliverMap">
    <ul>
        <li>
            <input type="radio" name="deliverId" value='<s:property value="#deliverMap.deliverId"/>'/>
            <input type="hidden" name="receiveDepId" value='<s:property value="#deliverMap.receiveDepId"/>'/>
            <input type="hidden" name="departNameReceive" value='<s:property value="#deliverMap.departNameReceive"/>'/>
        </li>
        <li><span><s:property value="#deliverMap.deliverRecNo"/></span></li>
        <li><span><s:property value="#deliverMap.departName"/></span></li>
        <li><span><s:property value="#deliverMap.departNameReceive"/></span></li>
        <li><span class="right"><s:property value="#deliverMap.totalQuantity"/></span></li>
        <li><span class="right"><s:property value="#deliverMap.totalAmount"/></span></li>
    </ul>
</s:iterator>
</div>