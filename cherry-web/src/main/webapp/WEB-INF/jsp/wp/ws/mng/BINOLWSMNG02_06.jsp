<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="deliverList" id="deliverMap" status="status">
    <s:url id="detailsUrl" action="BINOLWSMNG02_detailInit">
        <%-- 产品收发货ID --%>
        <s:param name="productDeliverId">${deliverMap.deliverId}</s:param>
    </s:url>
    <ul>
        <li>
            <%--
            <input type="radio" name="deliverId" value='<s:property value="#deliverMap.deliverId"/>'/>
            <input type="hidden" name="receiveDepId" value='<s:property value="#deliverMap.receiveDepId"/>'/>
            <input type="hidden" name="departNameReceive" value='<s:property value="#deliverMap.departNameReceive"/>'/>
            --%>
            <s:property value='RowNumber'/>
        </li>
        <li>
            <%-- 发货单号 --%>
            <a href="${detailsUrl}" style="color:#3366FF;" class="left" onclick="javascript:openWin(this);return false;">
                <s:property value="#deliverMap.deliverRecNo"/>
            </a>
        </li>
        <%--<li><span><s:property value="#deliverMap.deliverRecNo"/></span></li>--%>
        <li><span><s:property value="#deliverMap.departName"/></span></li>
        <%--<li><span><s:property value="#deliverMap.departNameReceive"/></span></li>--%>
        <li><span class="right"><s:property value="#deliverMap.totalQuantity"/></span></li>
        <li><span class="right"><s:property value="#deliverMap.totalAmount"/></span></li>
    </ul>
</s:iterator>
 <input type="hidden" name="checkboxType" value='<s:property value=""/>'/>
</div>