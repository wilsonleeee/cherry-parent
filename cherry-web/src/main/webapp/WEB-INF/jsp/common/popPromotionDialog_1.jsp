<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="popPrmProductInfoList" id="prmProductMap0">
		<ul>
			<li>
				<input name="prmProductInfo" class="checkbox" value="<s:property value="#prmProductMap0.prmProductInfo"/>"
				type="<s:if test='%{#prmProductMap0.checkType == null || #prmProductMap0.checkType == "checkbox"}'>checkbox</s:if><s:else>radio</s:else>"/>
			</li>
			<li><s:property value="#prmProductMap0.unitCode"/></li>
			<li><s:property value="#prmProductMap0.barCode"/></li>
			<li><s:property value="#prmProductMap0.nameTotal"/></li>
			<li><s:property value="#prmProductMap0.primaryCategory"/></li>
			<li><s:property value="#prmProductMap0.secondryCategory"/></li>
			<li><s:property value="#prmProductMap0.smallCategory"/></li>
			<li><span><s:text name="format.price"><s:param value="#prmProductMap0.standardCost"></s:param></s:text></span></li>
		</ul>
	</s:iterator>
</div>