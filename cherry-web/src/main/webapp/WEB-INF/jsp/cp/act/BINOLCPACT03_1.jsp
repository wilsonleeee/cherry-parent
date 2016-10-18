<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.cp.BINOLCPACT01">
<div id="aaData">
	  <s:iterator value="couponList" id="couponInfo">
    <ul> 
        <%--批次号 --%>
        <li><s:property value="batchCode"/></li>
        <%--Coupon码 --%>
        <li><s:property value="couponCode"/></li>
        <%--导入时间 --%>
        <li><s:property value="createTime"/></li>
    </ul>  
    </s:iterator>
</div>
</s:i18n>