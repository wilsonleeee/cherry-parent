<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTIOS13">
<div id="aaData">
	<s:iterator value="productInDepotExcelBatchList" id="productInDepotExcelBatchMap">
		<ul>
			<li><span>${RowNumber}</span></li>
			<li>
				<span>
					<s:url action="BINOLSTIOS08_init" id="indepotExcelBatchUrl">
						<s:param name="importBatchId" value="%{#productInDepotExcelBatchMap.importBatchId }" />
					</s:url>
					<a href="${indepotExcelBatchUrl }" class="popup" onclick="javascript:openWin(this);return false;">
						<s:property value="importBatchCode"/>
					</a>
				</span>
			</li>	
			<li><span><s:property value="importDate"/></span></li>
			<li><span>（<s:property value="employeeCode"/>）<s:property value="employeeName"/></span></li>	
			<li>
				<a class="description" style="cursor: pointer;"  title="<s:text name="binolstios13_inDepotReson" /> | <s:property value="comments" />">
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
