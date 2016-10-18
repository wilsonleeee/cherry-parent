<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTBIL15">
	<div id="headInfo">
        <s:text name="BIL15_sumCheckQuantity"/>
        <span class="<s:if test='sumInfo.sumCheckQuantity  < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumCheckQuantity "></s:param></s:text></strong>
        </span>
		<s:text name="BIL15_sumQuantity"/>
		<span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="BIL15_sumAmount"/>
	    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
    <div id="aaData">
    <s:iterator value="proStocktakeRequestList" id="proStocktakeRequestList">
        <s:url id="detailsUrl" action="BINOLSTBIL16_init">
            <%-- 盘点申请单ID --%>
            <s:param name="proStocktakeRequestID">${proStocktakeRequestID}</s:param>
        </s:url>
    <ul>
        <%-- 选择 --%>
		<%--<li><input type="checkbox" id="checkbill" value='<s:property value="proStocktakeRequestID" />' onclick="checkBill(this);"/></li>--%>
        <%-- No. --%>
        <li><s:property value="RowNumber"/></li>
        <li>
            <%-- 盘点申请单号--%>
            <a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;"><s:property value="stockTakingNo"/></a>
        </li>
        <li>
            <%-- 部门 --%>
            <s:if test='departName != null && !"".equals(departName)'>
                <s:property value="departName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 盘点仓库 --%>
            <s:if test='inventoryName != null && !"".equals(inventoryName)'>
                <s:property value="inventoryName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
          <li>
            <%-- 逻辑仓库 --%>
            <s:if test='logicInventoryName != null && !"".equals(logicInventoryName)'>
                <s:property value="logicInventoryName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 实盘总数量 --%>
            <s:if test="totalCheckQuantity !=null">
            <s:if test="totalCheckQuantity >= 0"><s:text name="format.number"><s:param value="totalCheckQuantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="totalCheckQuantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 盘差总数量 --%>
            <s:if test="totalQuantity !=null">
            <s:if test="totalQuantity >= 0"><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 盘差总金额 --%>
            <s:if test="totalAmount !=null">
            <s:if test="totalAmount >= 0"><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 盘点类型 --%>
            <span><s:property value='#application.CodeTable.getVal("1054", stocktakeType)'/></span>
        </li>
        <li>
            <%-- 盘点申请时间 --%>
            <s:if test='tradeDateTime != null && !"".equals(tradeDateTime)'>
                <s:property value="tradeDateTime"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 审核状态 --%>
            <s:if test='@com.cherry.cm.core.CherryConstants@CRAUDIT_FLAG_UNSUBMIT.equals(verifiedFlag)'>
                <span class="verified_unsubmit">
                    <span><s:property value='#application.CodeTable.getVal("1238", verifiedFlag)'/></span>
                </span>
            </s:if>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CRAUDIT_FLAG_SUBMIT.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1238", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CRAUDIT_FLAG_AGREE.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1238", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CRAUDIT_FLAG_DISAGREE.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1238", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CRAUDIT_FLAG_DISAGREE1.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1238", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CRAUDIT_FLAG_SUBMIT1.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1238", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CRAUDIT_FLAG_SUBMIT2.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1238", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CRAUDIT_FLAG_AGREE2.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1238", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CRAUDIT_FLAG_DISAGREE2.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1238", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1238", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:else><span></span></s:else>
        </li>
        <li>
            <%-- 操作员 --%>
            <s:if test='employeeName != null && !"".equals(employeeName)'>
                <s:property value="employeeName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <%-- 打印状态 --%>
        <%--
		<li>
			<s:if test="printStatus == @com.cherry.cm.core.CherryConstants@PRINT_STATUS_1">
				<s:url id="queryPrintLog" action="../common/BINOLCM99_queryPrintLog">
					<s:param name="billId">${proReturnRequestID}</s:param>
					<s:param name="pageId">BINOLSTBIL15</s:param>
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
        --%>
    </ul>  
    </s:iterator>
    </div>
</s:i18n>