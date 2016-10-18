<%--导入销售目标有失败（无异常）返回的页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mo.BINOLMOCIO07">
<div id="errorDiv">
	<div class="actionError">
	    <ul>
	        <li>
	        	<span>
	        		<s:property value="message"/>
	        	</span>
	        </li>
	    </ul>         
	</div>
</div>
<table>
<s:iterator value="errorTargetList" id="targetMap" status="status">
	<tr id="${status.index + 1}" class="<s:if test='#status.odd == true'>odd</s:if><s:else>even</s:else>" >
		<%-- 序号 --%>
		<td><span style="margin: 5px;">${status.index + 1}</span></td>
        <%-- 类型 --%>
        <td><span style="margin: 5px;"><s:property value="typeShow"></s:property></span></td>
        <%-- 目标年月 --%>
        <td><span style="margin: 5px;"><s:property value="targetDate"></s:property></span></td>
        <%-- 代号--%>
        <td><span style="margin: 5px;"><s:property value="targetCode"></s:property></span></td>
        <%-- 名称--%>
        <td><span style="margin: 5px;"><s:property value="targetName"></s:property></span></td>
        <%-- 目标类型--%>
        <td><span style="margin: 5px;"><s:property value="targetTypeShow"></s:property></span></td>
        <%-- 活动代号--%>
        <td><span style="margin: 5px;"><s:property value="activityCodeShow"></s:property></span></td>
        <%-- 活动名称--%>
        <td><span style="margin: 5px;"><s:property value="activityNameShow"></s:property></span></td>
        <%-- 金额指标--%>
        <td><span style="margin: 5px;"><s:property value="targetMoney"></s:property></span></td>
        <%-- 数量指标--%>
        <td><span style="margin: 5px;"><s:property value="targetQuantity"></s:property></span></td>
        <%--错误原因 --%>        
        <td><span style="margin: 5px;"><s:property value="errorInfoList"></s:property></span></td>
	</tr>
</s:iterator>
</table>
</s:i18n>