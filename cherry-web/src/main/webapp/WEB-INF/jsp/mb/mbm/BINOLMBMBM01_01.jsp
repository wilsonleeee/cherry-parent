<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="memberInfoList" id="memberInfo">
<ul>
<li><span><s:property value="RowNumber"/></span></li>
<li><span>
<s:url action="BINOLMBMBM10_init" id="memberDetailUrl">
	<s:param name="memberInfoId" value="%{#memberInfo.memberInfoId}"></s:param>
</s:url>
<a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;">
	<s:if test='%{name != null && !"".equals(name)}'><s:property value="name"/></s:if>
	<s:else><s:text name="binolmbmbm01_unknown"></s:text></s:else>
</a>
</span></li>
<li><span><s:property value="memCode"/></span></li>
<li><span><s:property value="mobilePhone"/></span></li>
<li><span><s:property value="levelName"/></span></li>
<li><span><s:property value="joinDate"/></span></li>
</ul>
</s:iterator>
</div>
</s:i18n>