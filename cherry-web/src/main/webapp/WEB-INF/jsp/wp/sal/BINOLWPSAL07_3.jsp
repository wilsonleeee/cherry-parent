<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.wp.BINOLWPSAL07">
<div id="aaData">
	<s:iterator value="billList" id="searchBills">
		<ul>
			<li><s:property value="RowNumber" /></li>
			<li><s:property value="billCode" /></li>
			<li><s:property value="relevantCode" /></li>
			<li><s:property value="businessDate" /></li>
			<li><s:property value="memberCode" /></li>
			<li><s:property value="baName" /></li>
			<li><%-- 数量 --%>
				<s:if test="quantity != null">
					<s:if test="quantity >= 0"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li><%-- 实收金额 --%>
				<s:if test="payAmount != null">
					<s:if test="payAmount >= 0"><s:text name="format.price"><s:param value="payAmount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="payAmount"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li>
				<button id="dgBtnGetBillDetail" class="wp_search_s" type="button" style="float:left; margin:0 5px 0 0" onclick="BINOLWPSAL07.getSrBillDetail(this);return false;">
					<span class="icon_search"></span>
					<span class="wp_search_text"><s:text name="wpsal07.getBillDetail" /></span>
				</button>
				<input type="hidden" id="dgRelevantCode" name="dgRelevantCode" value="<s:property value='relevantCode'/>"/>
				<input type="hidden" id="dgBillCode" name="dgBillCode" value="<s:property value='billCode'/>"/>
				<input type="hidden" id="dgBaCode" name="dgBaCode" value="<s:property value='baCode'/>"/>
				<input type="hidden" id="dgBaName" name="dgBaName" value="<s:property value='baName'/>"/>
				<input type="hidden" id="dgMemberCode" name="dgMemberCode" value="<s:property value='memberCode'/>"/>
				<input type="hidden" id="dgMemberName" name="dgMemberName" value="<s:property value='memberName'/>"/>
				<input type="hidden" id="dgMemberLevel" name="dgMemberLevel" value="<s:property value='memberLevel'/>"/>
				<input type="hidden" id="dgCustomerType" name="dgCustomerType" value="<s:property value='customerType'/>"/>
				<input type="hidden" id="dgBusinessDate" name="dgBusinessDate" value="<s:property value='businessDate'/>"/>
				<input type="hidden" id="dgBusinessTime" name="dgBusinessTime" value="<s:property value='businessTime'/>"/>
				<input type="hidden" id="dgTotalQuantity" name="dgTotalQuantity" value="<s:property value='quantity'/>"/>
				<input type="hidden" id="dgTotalAmount" name="dgTotalAmount" value="<s:property value='payAmount'/>"/>
				<input type="hidden" id="dgTotalOriginalAmount" name="dgTotalOriginalAmount" value="<s:property value='originalAmount'/>"/>
				<input type="hidden" id="dgRoundingAmount" name="dgRoundingAmount" value="<s:property value='roundingAmount'/>"/>
				<input type="hidden" id="dgCostPoint" name="dgCostPoint" value="<s:property value='costPoint'/>"/>
				<input type="hidden" id="dgCostPointAmount" name="dgCostPointAmount" value="<s:property value='costPointAmount'/>"/>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
