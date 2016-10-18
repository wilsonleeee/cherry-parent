<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTJCS11">
<div id="aaData">
	<s:iterator value="BBCounterList" id="jcs" >
		<ul>
			<li>
				<span>
					<input name="allBBCounterInfoId" type="checkbox" value="<s:property value="BBCounterInfoId" />" onclick="BINOLSTJCS11.checkRecord(this,'#dataTable');" />
				</span>
			</li>
			<li><span><s:property value="RowNumber" /></span></li>
			<li><span><s:property value="batchCode" /></span></li>
			<li>
				<span>
					<s:url id="viewUrl" action="BINOLSTJCS11_view">
						<s:param name="BBCounterInfoId" value="BBCounterInfoId" />
						<s:param name="brandInfoId" value="brandInfoId" />
					</s:url>
					<a href="${viewUrl }" onclick="openWin(this);return false;"><s:property value="departCode" /></a>
				</span>
			</li>
			<li><span><s:property value="departName" /></span></li>
			<li><span><s:property value="startDate" /> <s:property value="startTime" /></span></li>
			<li><span><s:property value="endDate" /> <s:property value="endTime" /></span></li>
			<li><span><s:property value="author" /></span></li>
			<li><span><s:property value="comments" /></span></li>
			<li>
				<s:if test="validFlag ==1">
					<span class='ui-icon icon-valid'></span>
				</s:if>
				<s:elseif test="validFlag == 0">
					<span class='ui-icon icon-invalid'></span>
				</s:elseif>
				<s:else>
					<span></span>
				</s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>