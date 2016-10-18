<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:i18n name="i18n.bs.BINOLBSWEM01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
	<s:iterator value="reservedCodeList" id="template" status="status">
		<ul>
			<li>
				<input type="radio" id="reservedCode" name="reservedCode" value="${template.reservedCode }"/>
			</li>
			<li><s:property value="reservedCode" /></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
