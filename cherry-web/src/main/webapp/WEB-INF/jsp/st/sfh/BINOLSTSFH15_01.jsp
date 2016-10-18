<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.st.BINOLSTSFH15">
<div id="headInfo">
    <s:text name="SFH15_sumQuantity"/>
    <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
        <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
    </span>
    <s:text name="SFH15_sumAmount"/>
    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
        <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
    </span>
</div>
<div id="aaData">
	<s:iterator value="saleOrdersList" id="saleOrders">
		<s:url id="detailsUrl" action="BINOLSTSFH16_init">
			<%-- 销售单单号   --%>
			<s:param name="saleId">${saleId}</s:param>
		</s:url>
		<ul>
			<li><input type="checkbox" id="checkbill" value='<s:property value="saleId" />' onclick="checkBill(this);"/></li>
			<li><a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;"><s:property value="saleOrderNo" /></a></li>
			<li><s:property value="importBatch" /></li>
			<li><s:property value="customerName" /></li>
			<li><s:property value="contactPerson" /></li>
			<li><s:property value="deliverAddress" /></li>
			<li>
				<s:if test='null != customerType && !"".equals(customerType)'>
					<s:property value='#application.CodeTable.getVal("1297",customerType)' />
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li>
				<s:if test='null != billType && !"".equals(billType)'>
					<s:property value='#application.CodeTable.getVal("1293",billType)' />
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li><s:property value="saleOrganization"/></li>
			<li><s:property value="saleEmployee"/></li>
			<li><%-- 销售总数量 --%>
				<s:if test="totalQuantity != null">
					<s:if test="totalQuantity >= 0"><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li><%-- 整单折扣前金额 --%>
				<s:if test="originalAmount != null">
					<s:if test="originalAmount >= 0"><s:text name="format.price"><s:param value="originalAmount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="originalAmount"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li><%-- 整单折扣率 --%>
				<s:if test="discountRate != null">
					<s:if test="discountRate >= 50"><s:text name="format.price"><s:param value="discountRate"></s:param></s:text> %</s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="discountRate"></s:param></s:text> %</span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li><%-- 整单折扣金额--%>
				<s:if test="discountAmount != null">
					<s:if test="discountAmount >= 0"><s:text name="format.price"><s:param value="discountAmount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="discountAmount"></s:param></s:text></span></s:else>
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
			<li><span><s:property value="expectFinishDate"/></span></li>
			<li><span><s:property value="saleDate"/></span></li>
			<%--<li><span><s:property value="saleTime"/></span></li>--%>
			<li><span><s:property value="billTicketTime"/></span></li>
			<li><s:property value="employeeName"/></li>
			<li><%-- 处理状态  --%>
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
						<span><s:text name="stsfh15.unSubmit"/></span>
					</span>
				</s:else>
            </li>
			<%-- <li>
				<s:if test="printStatus == @com.cherry.cm.core.CherryConstants@PRINT_STATUS_1">
					<s:url id="queryPrintLog" action="../common/BINOLCM99_queryPrintLog">
						<s:param name="billId">${saleId}</s:param>
						<s:param name="pageId">BINOLSTSFH16</s:param>
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
			</li> --%>
		</ul>
	</s:iterator>
</div>
</s:i18n>
