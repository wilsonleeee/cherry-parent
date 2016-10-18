<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTSFH18">
<div id="aaData">
	<s:iterator value="prtOrderExcelList" id="prtOrderExcelMap">
		<ul>
			<li><span>${RowNumber}</span></li>
			<li>
				<s:url action="BINOLSTSFH18_getOrderExcelDetail" id="orderExcelDetailUrl">
					<s:param name="prtOrderExcelId" value="%{#prtOrderExcelMap.prtOrderExcelId }" />
				</s:url>
				<a href="${orderExcelDetailUrl }" class="popup" onclick="javascript:BINOLSTSFH18.popu(this);return false;">
					<s:property value="billNo"/>
				</a>
			</li>
			<li>
				<span>
					<s:property value="departNameOrder"/>
				</span>
			</li>	
			<li>
				<span>
					<s:property value="inventoryName"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="logicInventoryName"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="departNameAccept"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="inventoryNameAccept"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="logicInventoryNameAccept"/>
				</span>
			</li>
			<li><span><s:property value="totalQuantity"/></span></li>		
			<li><span><s:property value="totalAmount"/></span></li>	
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
			<li><span><s:property value="#application.CodeTable.getVal('1142',tradeStatus)"/></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
