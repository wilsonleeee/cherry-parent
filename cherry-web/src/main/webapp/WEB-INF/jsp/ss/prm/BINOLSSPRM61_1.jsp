<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- 促销品移库一览DataTable --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM61">
    <div id="headInfo">
        <s:text name="PRM61_sumQuantity"/>
        <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="PRM61_sumAmount"/>
        <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
        </span>
    </div>
    <div id="aaData">
    <s:iterator value="shiftList" id="promotionShiftID">
    <s:url id="detailsUrl" action="BINOLSSPRM62_init">
        <%--移库单ID --%>
        <s:param name="promotionShiftID">${promotionShiftID}</s:param>
    </s:url>
    <ul>
        <%-- 选择 --%>
        <%--<li><input type="checkbox" id="checkbill" value='<s:property value="productShiftID" />' onclick="checkBill(this);"/></li>--%>
        <%-- No. --%>
        <li><s:property value="RowNumber"/></li>
        <li>
            <%-- 发货单号 --%>
            <a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;">
                <s:property value="billNo"/>
            </a>
        </li>
        <li>
            <%-- 部门 --%>
            <s:if test='departName != null && !"".equals(departName)'>
                (<s:property value="departCode"/>)<s:property value="departName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 实体移出仓库 --%>
            <s:if test='fromDepot != null && !"".equals(fromDepot)'>
                (<s:property value="fromDepotCode"/>)<s:property value="fromDepot"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 逻辑移出仓库 --%>
            <s:if test='fromLogicInventory != null && !"".equals(fromLogicInventory)'>
                (<s:property value="fromLogicInventoryCode"/>)<s:property value="fromLogicInventory"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 逻辑移入仓库 --%>
            <s:if test='toLogicInventory != null && !"".equals(toLogicInventory)'>
                (<s:property value="toLogicInventoryCode"/>)<s:property value="toLogicInventory"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 总数量 --%>
            <s:if test='totalQuantity != null'>
                <s:text name="format.number"><s:param value="totalQuantity"></s:param></s:text>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 总金额 --%>
            <s:if test='totalAmount != null'>
                <s:text name="format.price"><s:param value="totalAmount"></s:param></s:text>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 移库日期 --%>
            <s:if test='operateDate != null && !"".equals(operateDate)'>
                <s:property value="operateDate"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <s:if test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT.equals(verifiedFlag)'>
                <span class="verified_unsubmit">
                    <span><s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT)'/></span>
                </span>
            </s:if>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
                </span>
            </s:elseif>
        </li>
        <%-- 备注 --%>
        <%--<li><s:property value="comments"/></li>--%>
        <%-- 打印状态 --%>
        <%--
        <li>
            <s:if test="printStatus == @com.cherry.cm.core.CherryConstants@PRINT_STATUS_1">
                <s:url id="queryPrintLog" action="../common/BINOLCM99_queryPrintLog">
                    <s:param name="billId">${promotionShiftID}</s:param>
                    <s:param name="pageId">BINOLSSPRM61</s:param>
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