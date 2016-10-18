<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOCIO17">
<div id="aaData">
	<s:iterator value="counterMessageImportList" id="counterMessageImportMap">
		<ul>
			<li><span>${RowNumber}</span></li>
			<li>
				<s:url action="BINOLMOCIO17_getCntMsgImportDetail" id="cntMsgImportDetailUrl">
					<s:param name="counterMessageImportId" value="%{#counterMessageImportMap.counterMessageImportId }" />
				</s:url>
				<a href="${cntMsgImportDetailUrl }" class="popup" onclick="javascript:BINOLMOCIO17.popu(this);return false;">
					<s:property value="messageTitle"/>
				</a>
			</li>
			<li>
				<span>
					<s:property value="messageBody"/>
				</span>
			</li>	
			<li>
				<span>
					<s:property value="startValidDate"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="endValidDate"/>
				</span>
			</li>
			<li><span><s:property value="importDate"/></span></li>		
			<li>
				<s:if test='importResult==0'>
			        <span class="task-verified_rejected">
			        	<span><s:property value="#application.CodeTable.getVal('1250',importResult)"/></span>
			        </span>
				</s:if>
				<s:if test='importResult==1'>
			        <span class="task-verified">
			        	<span><s:property value="#application.CodeTable.getVal('1250',importResult)"/></span>
			        </span>
				</s:if>
			</li>
			<li><span><s:property value="#application.CodeTable.getVal('1177',publishStatus)"/></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
