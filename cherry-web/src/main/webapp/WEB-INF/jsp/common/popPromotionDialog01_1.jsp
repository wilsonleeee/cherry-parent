<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="popPrmProductInfoList" id="prmProductMap">
		<ul>
			<li>
				<input name="prmProductInfo" class="checkbox" value="<s:property value="#prmProductMap.prmProductInfo"/>"
				type="<s:if test='%{#prmProductMap.checkType == null || #prmProductMap.checkType == "checkbox"}'>checkbox</s:if><s:else>radio</s:else>"/>
			</li>
			<li><s:property value="#prmProductMap.barCode"/></li>
			<li><s:property value="#prmProductMap.nameTotal"/></li>
			<li><s:property value="#prmProductMap.primaryCategory"/></li>
			<li><s:property value="#prmProductMap.secondryCategory"/></li>
			<li><s:property value="#prmProductMap.smallCategory"/></li>
			<li><span><s:text name="format.price"><s:param value="#prmProductMap.standardCost"></s:param></s:text></span></li>
		</ul>
	</s:iterator>
</div>