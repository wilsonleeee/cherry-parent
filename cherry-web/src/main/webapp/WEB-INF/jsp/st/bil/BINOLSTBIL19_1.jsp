<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTBIL19">
	<div id="headInfo">
		<s:text name="BIL19_sumQuantity"/>
		<span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="BIL19_sumAmount"/>
	    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
    <div id="aaData">
    <s:iterator value="saleReturnRequestList" id="saleReturnRequestList">
        <s:url id="detailsUrl" action="BINOLSTBIL20_init">
            <%-- 退库申请单ID --%>
            <s:param name="saleReturnRequestID">${saleReturnRequestID}</s:param>
        </s:url>
    <ul>
        <%-- 选择 --%>
		<%--<li><input type="checkbox" id="checkbill" value='<s:property value="proReturnRequestID" />' onclick="checkBill(this);"/></li>--%>
        <%-- No. --%>
        <li><s:property value="RowNumber"/></li>
        <li>
            <%-- 退库单号--%>
            <a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;"><s:property value="billNo"/></a>
        </li>
        <li>
            <%-- 退货柜台 --%>
            <s:if test='counterName != null && !"".equals(counterName)'>
                <s:property value="'('+counterCode+')'+counterName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <span><s:property value="'('+employeeCode+')'+employeeName"/></span>
        </li>
        <li>
            <span><s:property value="memberCode"/></span>
        </li>
        <li>
            <%-- 总数量 --%>
            <s:if test="totalQuantity !=null">
            <s:if test="totalQuantity >= 0"><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 总金额 --%>
            <s:if test="totalAmount !=null">
            <s:if test="totalAmount >= 0"><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.price"><s:param value="totalAmount"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 退库申请时间 --%>
            <s:if test='tradeDate != null && !"".equals(tradeDate)'>
                <s:property value="tradeDate"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 退库申请单审核状态 --%>
            <s:if test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_UNSUBMIT.equals(verifiedFlag)'>
                <span class="verified_unsubmit">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:if>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_SUBMIT.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_AGREE.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_DISAGREE.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_DISAGREE1.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_SUBMIT1.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_SUBMIT2.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_AGREE2.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_DISAGREE2.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_SUBMIT3.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_AGREE3.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_DISAGREE3.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_SUBMIT4.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_AGREE4.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@RAAUDIT_FLAG_DISAGREE4.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1411", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:else><span></span></s:else>
        </li>
        <li>
            <%-- 审核员 --%>
            <s:if test='employeeIDAudit != null && !"".equals(employeeIDAudit)'>
                <s:property value="employeeIDAudit"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
    </ul>  
    </s:iterator>
    </div>
</s:i18n>