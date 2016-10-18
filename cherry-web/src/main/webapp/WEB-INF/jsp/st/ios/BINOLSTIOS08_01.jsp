<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTIOS08">
<div id="aaData">
	<s:iterator value="productInDepotExcelList" id="productInDepotExcelMap">
		<ul>
			<li><span>${RowNumber}</span></li>
			<li>
				<s:url action="BINOLSTIOS08_getIndepotExcelDetail" id="indepotExcelDetailUrl">
					<s:param name="productInDepotExcelId" value="%{#productInDepotExcelMap.productInDepotExcelId }" />
				</s:url>
				<a href="${indepotExcelDetailUrl }" class="popup" onclick="javascript:BINOLSTIOS08.popu(this);return false;">
					<s:property value="billNo"/>
				</a>
			</li>
			<li>
				<span>
					<s:if test='departCode != null && !"".equals(departCode)'>
						(<s:property value="departCode"/>)
					</s:if>
					<s:property value="departName"/>
				</span>
			</li>	
			<li>
				<span>
					<s:if test='inventoryCode != null && !"".equals(inventoryCode)'>
						(<s:property value="inventoryCode"/>)
					</s:if>
					<s:property value="inventoryName"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="logicInventoryName"/>
				</span>
			</li>
			<li><span><s:property value="totalQuantity"/></span></li>		
			<li><span><s:property value="totalAmount"/></span></li>	
			<li><span><s:property value="inDepotDate"/></span></li>	
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
			<li><span><s:property value="#application.CodeTable.getVal('1266',tradeStatus)"/></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
