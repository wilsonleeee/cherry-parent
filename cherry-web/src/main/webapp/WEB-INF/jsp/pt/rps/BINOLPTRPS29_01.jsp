<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="saleRptList" id="saleRpt">
		<ul>
			<li><span><s:property value="RowNumber"/></span></li>
			<li><span><s:property value="batchCode"/></span></li>
			<li><span><s:property value="resellerCode"/></span></li>
			<li><span><s:property value="resellerName"/></span></li>
			<li><span><s:property value="counterCode"/></span></li>
			<li><span><s:property value="counterName"/></span></li>
			<li><span><s:property value="startTime"/></span></li>
			<li><span><s:property value="endTime"/></span></li>
			<li>
				<span>
					<s:if test="%{#saleRpt.totalQuantity != null}">
						<s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text>
					</s:if>
					<s:else>
						0
					</s:else>
				</span>
			</li>
			<li>
				<span>
					<s:if test="%{#saleRpt.totalAmount != null}">
						<s:text name="format.price"><s:param value="totalAmount"></s:param></s:text>
					</s:if>
					<s:else>
						0
					</s:else>
				</span>
			</li>
			<li>
				<s:if test="%{#saleRpt.totalBillCount != null}">
					<s:text name="format.number"><s:param value="totalBillCount"></s:param></s:text>
				</s:if>
				<s:else>
					0
				</s:else>
			</li>
		</ul>
	</s:iterator>
</div>