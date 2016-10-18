<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTSFH12">
<div id="aaData">
	<s:iterator value="prtDeliverExcelBatchList" id="prtDeliverExcelBatchMap">
		<ul>
			<li><span>${RowNumber}</span></li>
			<li>
				<span>
					<s:url action="BINOLSTSFH13_init" id="deliverExcelBatchUrl">
						<s:param name="importBatchId" value="%{#prtDeliverExcelBatchMap.importBatchId }" />
					</s:url>
					<a href="${deliverExcelBatchUrl }" class="popup" onclick="javascript:openWin(this);return false;">
						<s:property value="importBatchCode"/>
					</a>
				</span>
			</li>	
			<li><span><s:property value="importDate"/></span></li>
			<li><span>（<s:property value="employeeCode"/>）<s:property value="employeeName"/></span></li>	
			<li>
				<a class="description" style="cursor: pointer;"  title="<s:text name="SFH12_importReson" /> | <s:property value="comments" />">
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
