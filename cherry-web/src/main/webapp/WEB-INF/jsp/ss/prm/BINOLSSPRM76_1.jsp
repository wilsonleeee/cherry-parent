<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.ss.BINOLSSPRM75">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="couponList" id="couponInfo">
	<ul>
		<li><span><s:property value="#couponInfo.couponNo"/></span></li>
		<li>
			<span>		
				<s:property value="#application.CodeTable.getVal('1383',couponType)"/>
			</span>
		</li>
		<li><span><s:property value="#couponInfo.relationBill"/></span></li>
		<li><span>
			<s:property value="#couponInfo.billCode"/>
		</span></li>
		<li>
		<span><s:property value="#couponInfo.counterCode"/></span>
		</li>
		<li>
		<span><s:property value="#couponInfo.counterName"/></span>
		</li>
		<li><span>
			<s:property value="#couponInfo.memberName"/>
		</span></li>
		<li><span>
			<s:property value="#couponInfo.userMemCode"/>
		</span></li>
		<li><span>
			<s:property value="#couponInfo.userMobile"/>
		</span></li>
		<li><span><s:property value="#couponInfo.bpCode"/></span></li>
		<li><span><s:property value="#couponInfo.userBP"/></span></li>
		<li><span>
			<s:property value="#couponInfo.useTime"/>
		</span></li>
		<li>
			<span>		
				<s:property value="#application.CodeTable.getVal('1384',status)"/>
			</span>
		</li>
		<li><s:property value="#couponInfo.ruleCode"/></li>
		<li><span><s:property value="#couponInfo.couponCode"/></span></li>
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