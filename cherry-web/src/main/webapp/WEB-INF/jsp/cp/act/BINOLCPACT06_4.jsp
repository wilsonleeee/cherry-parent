<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.cp.BINOLCPACT06">
<div id="aaData">
<s:iterator value="campOrderList" id="Order">
<s:url id="detail_url" action="BINOLCPACT06_getOrderDetail">
	<s:param name="campOrderId"><s:property value='campOrderId'/></s:param>
</s:url>
<s:url id="option_url" action="BINOLCPACT06_optionRun">
	<s:param name="campOrderId"><s:property value='campOrderId'/></s:param>
	<s:param name="newState" value="newState"/>
</s:url>
<s:url id="conditon_url" action="BINOLCPACT06_getConditon">
</s:url>
    <ul id="campNameId">
    	<li>${RowNumber}</li><%-- 编号 --%> 
    	<%--单据号 --%>
        <li><a href="${detail_url}" onclick="BINOLCPACT06.getPrtInfo(this,'0');return false;"><s:property value="billNo"/></a></li>
        <%--会员卡号--%>
        <li><s:if test="memId != null && memId != 0"><s:property value="memNameCode"/></s:if><s:else><s:property value="name"/></s:else></li>
         <%--会员手机号 --%>
         <li><s:property value="mobile"/></li>
         <%--会员微信号 --%>
         <li><s:property value="messageId"/></li>
          <%--测试区分 --%>
        <li><s:property value="#application.CodeTable.getVal('1256',testType)"/></li>
        <%--预约柜台 --%>
        <li><s:property value="counterOrder"/><s:if test='null != counterOrderName && !"".equals(counterOrderName)'>[<s:property value="counterOrderName"/>]</s:if></li>
        <%--领取柜台 --%>
        <li><s:property value="counterGot"/><s:if test='null != counterGotName && !"".equals(counterGotName)'>[<s:property value="counterGotName"/>]</s:if></li>
        <%--COUPON码 --%>
        <li><s:property value="couponCode"/></li>
         <%--预约时间 --%>
        <li><s:property value="campOrderTime"/></li>
         <%--预约时间 --%>
        <li><s:property value="bookDate"/></li>
        <%--预约时间 --%>
        <li><s:property value="bookTimeRange"/></li>
        <%--预约时间 --%>
        <li><s:property value="cancelTime"/></li>
        <%--预约时间 --%>
        <li><s:property value="finishTime"/></li>
        <%--总数量 --%>
        <li><s:text name="format.number"><s:param value="quantity"></s:param></s:text></li>
        <%--总金额 --%>
        <li><s:text name="format.price"><s:param value="amout"></s:param></s:text></li>
        <%--数据来源--%>
        <li><s:property value="#application.CodeTable.getVal('1011',dataChannel)"/></li>
        <%--操作 --%>
        <li>
        	<a class="setting" href="${option_url}" onclick="BINOLCPACT06.confirmInit('${option_url}','${conditon_url}','<s:property value="billNo"/>');return false;">
        		<s:if test='"AR".equals(newState)'>
        		<span class="ui-icon icon-ship-big"></span> 
			    <span class="button-text"><s:text name="ACT06_send" /></span> 
        		</s:if>
			    <s:elseif test='"CA".equals(newState)'>
			    <span class="ui-icon icon-cancel_res"></span> 
			    <span class="button-text"><s:text name="ACT06_cancel" /></span> 
			    </s:elseif>
        	</a>
        </li>
    </ul>
    </s:iterator>
</div>
</s:i18n>