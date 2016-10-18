<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBPTM02">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="pointInfoList" id="pointInfo">
<ul>
<li><span><s:property value="RowNumber"/></span></li>
<li><span>
<s:url action="BINOLMBMBM10_init" id="memberDetailUrl">
	<s:param name="memberInfoId" value="%{#pointInfo.memId}"></s:param>
</s:url>
<a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
	<s:property value="memCode"/>
</a>
</span></li>
<li><span><s:property value="departCode"/></span></li>
<li><span><s:property value="billCode"/></span></li>
<li><span><s:property value="changeDate"/></span></li>
<li><span>
<s:if test='%{#pointInfo.amount != null && !"".equals(#pointInfo.amount)}'>
<s:text name="format.price"><s:param value="amount"></s:param></s:text>
</s:if>
</span></li>
<li><span>
<s:if test='%{#pointInfo.quantity != null && !"".equals(#pointInfo.quantity)}'>
<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
</s:if>
</span></li>
<li><span><s:property value="point"/></span></li>
<li>
<s:url action="BINOLMBPTM03_init" id="pointInfoDetailUrl">
	<s:param name="pointChangeId" value="%{#pointInfo.pointChangeId}"></s:param>
</s:url>
<a class="delete" href="${pointInfoDetailUrl }" onclick="openWin(this);return false;">
	<span class="ui-icon ui-icon-document"></span><span class="button-text"><s:text name="binolmbptm02_operate"></s:text></span>
</a>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>