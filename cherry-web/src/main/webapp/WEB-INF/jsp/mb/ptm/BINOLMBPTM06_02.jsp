<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBPTM06">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="pointInfoList" id="pointInfo">
	<ul>
		<li><span><s:property value="RowNumber"/></span></li>
		<li><span>
		<s:url action="BINOLMBMBM10_init" id="memberDetailUrl">
			<s:param name="memberInfoId" value="%{#pointInfo.memberId}"></s:param>
		</s:url>
		<a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
			<s:property value="memberCode"/>
		</a>
		</span></li>
		<li><span><s:property value="memberName"/></span></li>
		<li><span><s:property value="mobile"/></span></li>
		<li><span><s:property value="grantCardTime"/></span></li>
		<li><span class="right"><s:property value="grantPoints"/></span></li>
		<li><span><s:property value="grantPointTime"/></span></li>
		<li><span><s:property value='#application.CodeTable.getVal("1414", #pointInfo.newOldMemberFlag)' /></span></li>
		<li><span><s:property value="region"/></span></li>
		<li><span><s:property value="province"/></span></li>
		<li><span><s:property value="city"/></span></li>
		<li><span><s:property value="counterCode"/></span></li>
		<li><span><s:property value="counterName"/></span></li>		
	</ul>
</s:iterator>
</div>
</s:i18n>