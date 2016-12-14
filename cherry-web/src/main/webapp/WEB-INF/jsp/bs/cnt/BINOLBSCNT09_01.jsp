<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
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
	<s:iterator value="errorCounterList"  status="errorCounterMap">
		<tr>
			<%-- 编号 --%>
			<td>${errorCounterMap.count}</td>

			<%-- 柜台编码--%>
			<td><span style="margin: 5px;"><s:property value="counterCode"></s:property></span></td>

			<%-- 柜台名称--%>
			<td><span style="margin: 5px;"><s:property value="counterName"></s:property></span></td>

			<%-- 开始时间--%>
			<td><span style="margin: 5px;"><s:property value="startDate"></s:property></span></td>

			<%-- 结束时间--%>
			<td><span style="margin: 5px;"><s:property value="endDate"></s:property></span></td>

			<%--错误原因 --%>
			<td><span style="margin: 5px;color: red"><s:property value="errorMsg"></s:property></span></td>
		</tr>
	</s:iterator>
</table>

