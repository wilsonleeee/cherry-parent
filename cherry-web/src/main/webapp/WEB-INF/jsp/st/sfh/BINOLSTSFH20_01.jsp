<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTSFH20">
<div id="aaData">
	<s:iterator value="backstageSaleExcelList" id="backstageSaleExcelMap">
		<ul>
			<li><span>${RowNumber}</span></li>
			<li>
				<s:url action="BINOLSTSFH20_getBackstageSaleExcelDetail" id="backstageSaleExcelDetailUrl">
					<s:param name="backstageSaleExcelId" value="%{#backstageSaleExcelMap.backstageSaleExcelId }" />
				</s:url>
				<a href="${backstageSaleExcelDetailUrl }" class="popup" onclick="javascript:BINOLSTSFH20.popu(this);return false;">
					<s:property value="billNo"/>
				</a>
			</li>
			<li>
				<span>
					<s:property value="departSale"/>
				</span>
			</li>	
			<li>
				<span>
					<s:property value="depotNameSale"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="logicInventoryNameSale"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="departCustomer"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="depotNameCustomer"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="logicInventoryNameCustomer"/>
				</span>
			</li>
			
			<li>
				<span>
					<s:property value="saleDate"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="#application.CodeTable.getVal('1293',saleBillType)"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="#application.CodeTable.getVal('1297',customerType)"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="#application.CodeTable.getVal('1175',settlement)"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="#application.CodeTable.getVal('1296',currency)"/>
				</span>
			</li>
			
			<li>
				<span>
					<s:property value="contactPerson"/>
				</span>
			</li>
			<li>
				<span>
					<s:property value="deliverAddress"/>
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
			<li>
				<!-- 单据状态 -->
				<s:if test='null != billState && !"".equals(billState)'>
					<s:if test="billState == @com.cherry.cm.core.CherryConstants@BILLSTATE_SL_DRAFT">
					<span class="verified_unsubmit">
						<span><s:property value='#application.CodeTable.getVal("1294",billState)' /></span>
					</span>
					</s:if>
					<s:elseif test="billState == @com.cherry.cm.core.CherryConstants@BILLSTATE_SL_VERIFIED">
					<span class="verifying">
						<span><s:property value='#application.CodeTable.getVal("1294",billState)' /></span>
					</span>
					</s:elseif>
					<s:elseif test="billState == @com.cherry.cm.core.CherryConstants@BILLSTATE_SL_UNDELIVER">
					<span class="verifying">
						<span style="color:#333333"><s:property value='#application.CodeTable.getVal("1294",billState)' /></span>
					</span>
					</s:elseif>
					<s:elseif test="billState == @com.cherry.cm.core.CherryConstants@BILLSTATE_SL_ABANDON">
					<span class="verified_rejected">
						<span><s:property value='#application.CodeTable.getVal("1294",billState)' /></span>
					</span>
					</s:elseif>
					<s:elseif test="billState == @com.cherry.cm.core.CherryConstants@BILLSTATE_SL_FINISH">
					<span class="verified">
						<span><s:property value='#application.CodeTable.getVal("1294",billState)' /></span>
					</span>
					</s:elseif>
				</s:if>
				<s:else>
					<span class="verified_unsubmit">
						<span><s:text name="SFH20_unSubmit"/></span>
					</span>
				</s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
