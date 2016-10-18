<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- 入库/收货一览List --%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.wp.BINOLWSMNG02">
	<div id="headInfo">
		<s:text name="global.page.sumQuantity"/>：
		<span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
			<strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="global.page.sumAmount"/>：
	    <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
	    	<strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
    <div id="aaData">
    <s:iterator value="prtInDepotList" id="prtInDepotList">
        <s:url id="detailsUrl" action="BINOLWSMNG02_initDetail">
            <%-- 入库单ID --%>
            <s:param name="productInDepotId">${productInDepotID}</s:param>
            <s:param name="tradeType">${tradeType}</s:param>
        </s:url>
    <ul>
        <%-- 选择 --%>
        <li>
            <input type="checkbox" id="checkbill" value='<s:property value="productInDepotID" />' onclick="checkBill(this);"/>
            <input type="hidden" id="billTradeType" value='<s:property value="tradeType" />' />
        </li>
        <li>
            <%-- 入库单号--%>
            <a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;"><s:property value="billNo"/></a>
        </li>
        <li>
            <%-- 入库时间 --%>
            <s:if test='inDepotDate != null && !"".equals(inDepotDate)'>
                <s:property value="inDepotDate"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
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
        <%-- <li>
            <s:if test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_UNSUBMIT.equals(verifiedFlag)'>
                <span class="verified_unsubmit">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:if>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_SUBMIT.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_AGREE.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_DISAGREE.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_DISAGREE1.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_SUBMIT1.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_SUBMIT2.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_AGREE2.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@GRAUDIT_FLAG_DISAGREE2.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1305", verifiedFlag)'/></span>
                </span>
            </s:elseif>
        </li> --%>
        <%-- 入库状态 --%>
        <li><s:property value='#application.CodeTable.getVal("1266", tradeStatus)'/></li>
        <li>
            <%-- 操作员 --%>
            <s:if test='employeeName != null && !"".equals(employeeName)'>
                <s:property value="employeeName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
    </ul>  
    </s:iterator>
    </div>
</s:i18n>