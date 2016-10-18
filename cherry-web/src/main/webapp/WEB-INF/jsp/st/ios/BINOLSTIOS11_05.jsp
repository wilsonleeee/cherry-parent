<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTIOS11">
<div id="aaData">
	<s:iterator value="billExcelList" id="billExcelMap">
		<ul>
			<li><span>${RowNumber}</span></li>
			<li>
				<s:url action="BINOLSTIOS11_getExcelDetail" id="orderExcelDetailUrl">
					<s:param name="proReturnRequestExcelID" value="%{#billExcelMap.proReturnRequestExcelID }" />
				</s:url>
				<a href="${orderExcelDetailUrl }" class="popup" onclick="javascript:BINOLSTIOS11_04.popu(this);return false;">
					<s:property value="billNo"/>
				</a>
			</li>
			<li>
				<span>
					<s:property value="departNameFrom"/>
				</span>
			</li>	
			<li>
				<span>
					<s:property value="inventoryNameFrom"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="logicInventoryNameFrom"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="departNameTo"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="inventoryNameTo"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="logicInventoryNameTo"/>
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
		</ul>
	</s:iterator>
</div>
</s:i18n>