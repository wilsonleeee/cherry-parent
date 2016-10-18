<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.wp.BINOLWPSAL12">
<div id="aaData">
	<s:iterator value="payResultList" id="payResult">
		<ul>
			<li><s:property value="billCode" /></li>
			<li><s:property value="businessDate" /></li>
			<li><s:property value="memberCode" /></li>
			<li><s:property value="memberName" /></li>
			<li><s:property value="payAmount" /></li>
			<li><s:property value="payState" /></li>
			<li><s:property value="payMessage" /><div class="hide" id="payStateDiv"><s:property value="payState" /></div></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
