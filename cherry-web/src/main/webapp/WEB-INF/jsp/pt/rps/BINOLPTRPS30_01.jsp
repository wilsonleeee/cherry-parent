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
			<li>
				<span>
					<s:if test="regionCode != null && regionCode != ''">
						（<s:property value="regionCode"/>）
					</s:if>
					<s:property value="regionName"/>
				</span>
			</li>
			<li>
				<span>
					<s:if test="resellerCode != null && resellerCode != ''">
						（<s:property value="resellerCode"/>）
					</s:if>
					<s:property value="resellerName"/>
				</span>
			</li>
			<li>
				<span>
					<s:if test="basCode != null && basCode != ''">
						（<s:property value="basCode"/>）
					</s:if>
					<s:property value="basName"/>
				</span>
			</li>
			<li>
				<span>
					<s:if test="counterCode != null && counterCode != ''">
						（<s:property value="counterCode"/>）
					</s:if>
					<s:property value="counterName"/>
				</span>
			</li>
			<li><span><s:text name="format.price"><s:param value="saleAmount"/></s:text></span></li>
			<li><span><s:text name="format.number"><s:param value="saleCount"/></s:text></span></li>
			<li><span><s:text name="format.number"><s:param value="saleQuantity"/></s:text></span></li>
			<li><span><s:text name="format.number"><s:param value="saleQuantity1"/></s:text></span></li>
			<li><span><s:text name="format.price"><s:param value="memSaleAmount"/></s:text></span></li>
			<li><span><s:text name="format.price"><s:param value="notMemSaleAmout"/></s:text></span></li>
			<li><span><s:text name="format.price"><s:param value="promSaleAmount"/></s:text></span></li>
			<li><span><s:text name="format.price"><s:param value="inventoryAmount"/></s:text></span></li>
		</ul>
	</s:iterator>
</div>