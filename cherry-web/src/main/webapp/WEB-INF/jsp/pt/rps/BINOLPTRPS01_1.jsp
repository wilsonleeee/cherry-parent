<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS01">
<s:if test='sumInfo != null'>
    <div id="headInfo">
        <s:text name="RPS01_sumQuantity"/>
        <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="RPS01_sumAmount"/>
        <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
        </span>
    </div>
</s:if>
    <div id="aaData">
    <s:iterator value="takingList" id="taking">
    <s:url id="detailsUrl" action="BINOLPTRPS21_init">
        <%-- 产品盘点ID --%>
        <s:param name="stockTakingId">${taking.stockTakingId}</s:param>
        <%-- 盈亏 --%>
        <s:param name="profitKbn">${profitKbn}</s:param>
    </s:url>
    <ul>
        <%-- No. --%>
        <li><s:property value="RowNumber"/></li>
        <li>
            <%-- 盘点单号 --%>
            <a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;"><s:property value="stockTakingNo"/></a>
        </li>
        <li>
            <%-- 部门名称 --%>
            <s:if test='departName != null && !"".equals(departName)'>
                <s:property value="departName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 仓库名称 --%>
            <s:if test='inventoryName != null && !"".equals(inventoryName)'>
                <s:property value="inventoryName"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 盘差 --%>
            <s:if test="summQuantity !=null">
            <s:if test="summQuantity >= 0"><s:text name="format.number"><s:param value="summQuantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="summQuantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 盘差金额 --%>
            <s:if test="summAmount !=null">
            <s:if test="summAmount >= 0"><s:text name="format.price"><s:param value="summAmount"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.price"><s:param value="summAmount"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 盘点类型 --%>
            <span><s:property value='#application.CodeTable.getVal("1054", takingType)'/></span>
        </li>
        <li>
            <%-- 盘点日期 --%>
            <s:if test='date != null && !"".equals(date)'>
                <s:property value="date"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 审核状态 --%>
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
                <span class="verified_rejected"><span>
                <s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE)'/>
                </span></span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1007", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:else><span></span></s:else>
        </li>
        <li>
            <%-- 盘点员 --%>
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