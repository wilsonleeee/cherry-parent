<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>

<s:i18n name="i18n.ss.BINOLSSPRM29">
	<div id="headInfo">
		<s:text name="PRM29_sumQuantity"/>
		<span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="PRM29_sumAmount"/>
	    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
<div id="aaData">
	<s:iterator value="allocationList" id="pro">
		<s:url id="detailsUrl" value="BINOLSSPRM30_init">
			<s:param name="proAllocationId">${pro.proAllocationId}</s:param>
		</s:url>
		<ul>
			<%-- 选择 --%>
			<li><input type="checkbox" id="checkbill" value='<s:property value="proAllocationId" />' onclick="checkBill(this);"/></li>
			<%-- 调拨单号 --%>
			<li>
				<a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
					<s:property value="allocationNo"/>
				</a>
			</li>
			<%-- 调拨业务类型 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1034", tradeType)'/></span></li>
			<%-- 调拨申请部门   --%>
			<li><span>(<s:property value="sendDepartCode"/>)<s:property value="sendDepart"/></span></li>
			<%-- 调拨接受部门 --%>
			<li><span>(<s:property value="receiveDepartCode"/>)<s:property value="receiveDepart"/></span></li>
			<%-- 总数量 --%>
			<li><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></li>
			<%-- 总金额  --%>
			<li><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text></li>
			<%-- 调拨日期 --%>
			<li><span><s:property value="allocationDate"/></span></li>
			<%-- 审核区分 --%>
			<li> 
				<s:if test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT">
					<span class="verified_unsubmit">
						<span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
					</span>
				</s:if>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT">
					<span class="verifying">
						<span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
					</span>
				</s:elseif>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE">
					<span class="verified">
						<span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
					</span>
				</s:elseif>
				<s:elseif test="verifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE">
					<span class="verified_rejected">
						<span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
					</span>
				</s:elseif>
	            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD.equals(verifiedFlag)'>
	                <span class="verified_rejected">
	                    <span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
	                </span>
	            </s:elseif>
				<s:else><span></span></s:else>
			</li>
			<%-- 操作员 --%>
			<li><span><s:property value="employeeName"/></span></li>
			<%-- 打印状态 --%>
			<li>
				<s:if test="printStatus == @com.cherry.cm.core.CherryConstants@PRINT_STATUS_1">
					<s:url id="queryPrintLog" action="../common/BINOLCM99_queryPrintLog">
					<s:param name="billId">${proAllocationId}</s:param>
					<s:param name="pageId">BINOLSSPRM30</s:param>
				</s:url>
				<a class="printed" href="${queryPrintLog}" rel="${queryPrintLog}" title='<s:text name="global.page.printLog"/>'>
					<span><s:property value='#application.CodeTable.getVal("1169", printStatus)'/></span>
				</a>
				</s:if>
				<s:else>
					<span class="unprinted">
						<span><s:property value='#application.CodeTable.getVal("1169", printStatus)'/></span>
					</span>
				</s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>