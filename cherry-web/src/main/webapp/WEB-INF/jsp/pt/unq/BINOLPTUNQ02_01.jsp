<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTUNQ02">
<div id="aaData">
		<s:iterator value="singleCodeSearchList" id="singleCodeInfo" >
			<ul>
			    <li><span><s:property value="#singleCodeInfo.RowNumber"/></span></li>
			    <li><span><s:property value="#singleCodeInfo.unitCode" /></span></li>
			    <li><span><s:property value="#singleCodeInfo.barCode"/></span></li>
			    <li><span><s:property value="#singleCodeInfo.nameTotal"/></span></li>
			    <li><span><s:property value="#singleCodeInfo.pointUniqueCode"/></span></li>
				<li><span><s:property value="#singleCodeInfo.relUniqueCode"/></span></li>
				<li><span><s:property value="#singleCodeInfo.boxCode"/></span></li>
			    <li><span><s:property value="#singleCodeInfo.createTime"/></span></li>
				<li><span><s:property value="#singleCodeInfo.primaryCategoryBig"/></span></li>
				<li><span><s:property value="#singleCodeInfo.primaryCategorySmall"/></span></li>
				<li><span><s:property value='#application.CodeTable.getVal("1395", #singleCodeInfo.activationStatus)'/></span></li>
				<li><span><s:property value='#application.CodeTable.getVal("1396", #singleCodeInfo.useStatus)'/></span></li>
			</ul>
		</s:iterator>	
</div>
</s:i18n>