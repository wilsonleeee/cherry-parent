<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBRPT11">
<div id="aaData">
<s:iterator value="subscribeList" id="subscribeMap" status="status">
<ul>
<li><span><s:property value="OpenID"/></span></li>
<li>
	<span>
		<s:if test='%{#subscribeMap.CounterCode != null && !"".equals(#subscribeMap.CounterCode)}'>
			(<s:property value="CounterCode"/>)
		</s:if>
		<s:property value="CounterName"/>
 	</span>
 </li>
<li>
	<span>
		<s:if test='%{#subscribeMap.EmployeeCode != null && !"".equals(#subscribeMap.EmployeeCode)}'>
			(<s:property value="EmployeeCode"/>)
		</s:if>
		<s:property value="EmployeeName"/>
	</span>
</li>
<li><span><s:if test='%{#subscribeMap.AgencyName != null && !"".equals(#subscribeMap.AgencyName)}'><s:property value="AgencyName"/></s:if></span></li>
<li><span><s:property value="SubscribeEventKey"/></span></li>
<li><span><s:property value="SubscribeTime"/></span></li>
<li>
	<span>
		<s:if test='%{#subscribeMap.FirstFlag != null && "1".equals(#subscribeMap.FirstFlag)}'>
			<s:text name="MBRPT11_first"/>
		</s:if>
		<s:else>
			<s:text name="MBRPT11_notFirst"/>
		</s:else>
	</span>
</li>
<li><span><s:property value="QRCodeName"/></span></li>
<li><span><s:property value="QRCodeImageUrl"/></span></li>
</ul>
</s:iterator>
</div>
</s:i18n>