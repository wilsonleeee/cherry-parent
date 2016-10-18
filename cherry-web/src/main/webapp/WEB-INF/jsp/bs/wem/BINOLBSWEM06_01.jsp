<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSWEM06">
	<div id="aaData">
		<s:iterator value="salList" id="resultMap" status="status">
			<ul>
				<li>
					<s:checkbox id="validFlag" name="validFlag" value="false" onclick="BINOLBSWEM06.checkSelect(this);" />
					<input class="hide" name="saleRecordCodeP" value="<s:property value='saleRecordCode'/>"/>
				</li>
				<li><s:property value="saleRecordCode"/></li>
				<li><s:property value="billCodePre"/></li>
				<li><s:property value="billCode"/></li>
				<li><s:property value="saleType"/></li>
				<li><s:date name="saleTime" format="yyyy-MM-dd HH:mm:ss"/></li>
				<li><s:property value="employeeCode"/></li>
				<li><s:if test="saleCount!=null && saleCount!=''"><s:text name="format.number"><s:param value="saleCount"/></s:text></s:if></li>
				<li><s:if test="amount!=null && amount!=''"><s:text name="format.number"><s:param value="amount"/></s:text></s:if></li>
				<li><s:property value="employeeName"/></li>
				<li><s:if test="saleProfit!=null && saleProfit!=''"><s:text name="format.number"><s:param value="saleProfit"/></s:text></s:if></li>
				<li><s:property value="channel"/></li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>