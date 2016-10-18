<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="productAllocationList" id="productAllocationMap" status="status">
    <s:url id="detailsUrl" action="BINOLWSMNG05_initDetail">
        <%-- 产品调入申请单ID --%>
        <s:param name="productAllocationID">${productAllocationMap.productAllocationID}</s:param>
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
            <%-- 单据号 --%>
            <a href="${detailsUrl}" style="color:#3366FF;" class="left" onclick="javascript:openWin(this);return false;">
                <s:property value="#productAllocationMap.allocationNoIF"/>
            </a>
        </li>
        <li><span><s:property value="#productAllocationMap.departNameIn"/></span></li>
        <li><span class="right"><s:property value="#productAllocationMap.totalQuantity"/></span></li>
        <li><span class="right"><s:property value="#productAllocationMap.totalAmount"/></span></li>
    </ul>
</s:iterator>
 <input type="hidden" name="checkboxType" value='<s:property value=""/>'/>
</div>