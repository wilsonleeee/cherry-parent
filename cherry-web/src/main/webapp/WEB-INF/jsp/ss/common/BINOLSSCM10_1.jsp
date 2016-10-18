<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="inDepotList" id="inDepotMap">
    <ul>
        <li>
            <input type="radio" name="inDepotID" value='<s:property value="#inDepotMap.inDepotID"/>'/>
        </li>
        <li><span><s:property value="#inDepotMap.billNoIF"/></span></li>
        <li><span><s:property value="#inDepotMap.departName"/></span></li>
        <li><span class="right"><s:property value="#inDepotMap.totalQuantity"/></span></li>
        <li><span class="right"><s:property value="#inDepotMap.totalAmount"/></span></li>
    </ul>
</s:iterator>
 <input type="hidden" name="checkboxType" value='<s:property value=""/>'/>
</div>