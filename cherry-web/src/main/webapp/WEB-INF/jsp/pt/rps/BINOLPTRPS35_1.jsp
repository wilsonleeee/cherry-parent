<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="monthPingEffectList" id="monthPingEffectMap">
		<ul>
			<li><span><s:property value="RowNumber"/></span></li>
			<li><span><s:property value="counterCode"/></span></li>
			<li><span><s:property value="counterName"/></span></li>
			<li><span><s:text name="format.price"><s:param value="monthSaleAmount"/></s:text></span></li>
			<li><span><s:property  value="businessArea"/></span></li>
			<li><span><s:text name="format.number"><s:param value="numberOfDay"/></s:text></span></li>
			<li><span><s:property  value="monthPingEffect"/></span></li>
		</ul>
	</s:iterator>
</div>