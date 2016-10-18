<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM75.js"></script>
<s:i18n name="i18n.ss.BINOLSSPRM75">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="couponList" id="couponInfo">
	<s:url id="edit_url" action="BINOLSSPRM73_editInit" namespace="/ss">
		<s:param name="ruleCode">${couponInfo.ruleCode}</s:param>
	</s:url>
	<ul>
		<li><span><s:property value="#couponInfo.couponNo"/></span></li>
		<li>
			<span>	
				<s:property value="#application.CodeTable.getVal('1384',status)"/>
			</span>
		</li>
		<li>
			<span>		
				<s:property value="#application.CodeTable.getVal('1383',couponType)"/>
			</span>
		</li>
		<li><a href="${edit_url}" onclick="openWin(this);return false;"><s:property value="#couponInfo.ruleCode"/></a></li>
		<li><span><s:property value="#couponInfo.couponCode"/></span></li>
		<li><span><s:property value="#couponInfo.userMemCode"/></span></li>
		<li><span><s:property value="#couponInfo.memberName"/></span></li>
		<li><span><s:property value="#couponInfo.userBP"/></span></li>
		<li><span><s:property value="#couponInfo.useTime"/></span></li>
		<li><span><s:property value="#couponInfo.relationBill"/></span></li>
		<li><span><s:property value="#couponInfo.billCode"/></span></li>
		<li><span><s:property value="#couponInfo.counterName"/></span></li>
		<li>
			<s:url id="usedInfo" action="BINOLSSPRM75_usedInfoInit" namespace="/ss">
				<s:param name="couponNo"><s:property value="#couponInfo.couponNo"/></s:param>
			</s:url>
			<a class="delete" href="${usedInfo}" onclick="openWin(this);return false;">
				<span class="ui-icon icon-copy"></span><span class="button-text"><s:text name="Search"/></span>
			</a>
		</li>
	</ul>
</s:iterator>
</div>
</s:i18n>