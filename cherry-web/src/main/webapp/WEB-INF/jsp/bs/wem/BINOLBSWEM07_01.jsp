<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSWEM07">
	<div id="aaData">
		<s:iterator value="bankTransferRecordList" id="resultMap" status="status">
			<ul>
				<li><s:property value="#status.index+iDisplayStart+1" /></li>
				<li><s:property value="collectionAccount"/></li>
				<li><s:property value="accountName"/></li>
				<li><s:property value="amount"/></li>
				<li><s:property value="comments"/></li>
				<li><span><s:property value='#application.CodeTable.getVal("1335", mainBank)'/></span></li>
				<li><s:property value="subBank"/></li>
				<li><s:property value="province"/></li>
				<li><s:property value="cityCounty"/></li>
				<li><s:property value="commissionMobile"/></li>
				<li><s:property value="commissionName"/></li>
				<li><s:property value="commissionCounter"/></li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>