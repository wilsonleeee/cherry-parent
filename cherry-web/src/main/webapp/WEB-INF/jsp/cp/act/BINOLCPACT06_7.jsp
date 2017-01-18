<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.cp.BINOLCPACT06">
<div id="aaData">
<s:iterator value="campDispatchOrderList" id="Order">
<s:url id="detailDispatch_url" action="BINOLCPACT06_getDispatchDetail">
	<s:param name="campOrderId"><s:property value='campOrderId'/></s:param>
	<%--<s:param name="editStatus"><s:property value='1'/></s:param>--%>
</s:url>
    <ul id="campNameId">
    	<li>${RowNumber}</li><%-- 编号 --%> 
    	<%--单据号 --%>
        <li><a href="${detailDispatch_url}" onclick="BINOLCPACT06_6.getPrtDispatchInfo(this,'0');return false;"><s:property value="billNo"/></a>
            <input class="hide" name="billNo" value='<s:property value="billNo"/>'>
            <input class="hide" name="receiverName" value='<s:property value="receiverName"/>'>
            <input class="hide" name="receiverMobile" value='<s:property value="receiverMobile"/>'>
            <input class="hide" name="deliveryProvince" value='<s:property value="deliveryProvince"/>'>
            <input class="hide" name="deliveryCity" value='<s:property value="deliveryCity"/>'>
            <input class="hide" name="deliveryCounty" value='<s:property value="deliveryCounty"/>'>
            <input class="hide" name="deliveryAddress" value='<s:property value="deliveryAddress"/>'>
        </li>
        <%--姓名 --%>
        <li><s:property value="name"/></li>
        <%--会员手机号 --%>
        <li><s:property value="mobile"/></li>
        <%--会员微信号 --%>
        <li><s:property value="messageId"/></li>
        <%--测试区分 --%>
        <li><s:property value="#application.CodeTable.getVal('1256',testType)"/></li>
        <%--预约柜台 --%>
        <li><s:property value="counterOrder"/><s:if test='null != counterOrderName && !"".equals(counterOrderName)'>[<s:property value="counterOrderName"/>]</s:if></li>
        <%--发货时间 --%>
        <li><s:property value="finishTime"/></li>
        <%--预约时间 --%>
        <li><s:property value="campOrderTime"/></li>
        <%--总数量 --%>
        <li><s:text name="format.number"><s:param value="quantity"></s:param></s:text></li>
        <%--总金额 --%>
        <li><s:text name="format.price"><s:param value="amout"></s:param></s:text></li>
        <%--活动状态 --%>
        <li>
            <s:if test='"RV".equals(state)'><span class="verifying"></s:if>
            <s:elseif test='"AR".equals(state)'><span class="verified_unsubmit"></s:elseif>
                <s:elseif test='"OK".equals(state)'><span class="task-verified"></s:elseif>
                    <s:else><span class="task-verified_rejected"></s:else>
        <span><s:property value="#application.CodeTable.getVal('1422',state)"/></span>
        </span>
        </li>
        <%--数据来源--%>
        <li><s:property value="#application.CodeTable.getVal('1011',dataChannel)"/></li>
        <li>
            <s:if test='"RV".equals(state)'>
                <button  class="left search" onclick="BINOLCPACT06_6.updBillInfo(this);">
                    <span class="ui-icon icon-ship-big"></span>
                    <span class="button-text"><s:text name="ACT06_send"/></span>
                </button>
            </s:if>
        </li>
    </ul>  
    </s:iterator>
</div>
</s:i18n>