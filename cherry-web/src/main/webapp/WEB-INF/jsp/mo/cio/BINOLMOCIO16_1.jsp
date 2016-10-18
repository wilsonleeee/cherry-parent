<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOCIO16">
<div id="aaData">
	<s:iterator value="counterMessageImportBatchList" id="counterMessageImportBatchMap">
		<ul>
			<li><span>${RowNumber}</span></li>
			<li>
				<span>
					<s:url action="BINOLMOCIO17_init" id="cntMsgBatchUrl">
						<s:param name="importBatchId" value="%{#counterMessageImportBatchMap.importBatchId }" />
					</s:url>
					<a href="${cntMsgBatchUrl }" class="popup" onclick="javascript:openWin(this);return false;">
						<s:property value="importBatchCode"/>
					</a>
				</span>
			</li>	
			<li><span><s:property value="importDate"/></span></li>
			<li><span>（<s:property value="employeeCode"/>）<s:property value="employeeName"/></span></li>
			<li>
				<s:if test='null != isPublish && "0".equals(isPublish)'>
			        <span class="verified_unsubmit">
			        	<span><s:property value="#application.CodeTable.getVal('1306',isPublish)"/></span>
			        </span>
				</s:if>
				<s:if test='null != isPublish && "1".equals(isPublish)'>
			        <span class="task-verified">
			        	<span><s:property value="#application.CodeTable.getVal('1306',isPublish)"/></span>
			        </span>
				</s:if>
			</li>
			<li>
				<a class="description" style="cursor: pointer;"  title="<s:text name="CIO16_importReson" /> | <s:property value="comments" />">
			        <s:if test="%{null!=comments&&comments.length()>24}">
			          <s:property value="%{comments.substring(0, 20)}" />...
			 		</s:if>
			 		<s:else>
			          <s:property value="comments" />
			   		</s:else>
		        </a>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
