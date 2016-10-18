<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- 盘点申请一览List --%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.wp.BINOLWSMNG06">
	<div id="headInfo">
        <s:text name="MNG06_sumrealQuantity"/>
        <span class="<s:if test='sumInfo.sumrealQuantity  < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumrealQuantity "></s:param></s:text></strong>
        </span>
        <s:text name="MNG06_sumQuantity"/>
        <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="MNG06_sumAmount"/>
        <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
        </span>
	</div>
    <div id="aaData">
    <s:iterator value="takingList" id="taking">
        <s:url id="detailsUrl" action="BINOLWSMNG06_initDetail">
            <%-- 盘点单ID --%>
            <s:param name="stockTakingId">${stockTakingId}</s:param>
        </s:url>
    <ul>
        <%-- 选择 --%>
        <li><input type="checkbox" id="checkbill" value='<s:property value="stockTakingId" />' onclick="checkBill(this);"/></li>
        <li>
            <%-- 盘点单号--%>
            <a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;"><s:property value="stockTakingNo"/></a>
        </li>
        <li>
            <%-- 盘点日期 --%>
            <s:if test='tradeDateTime != null && !"".equals(tradeDateTime)'>
                <s:property value="tradeDateTime"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 实盘总数量 --%>
            <s:if test='@com.cherry.cm.core.CherryConstants@CAAUDIT_FLAG_UNSUBMIT.equals(verifiedFlag)'>
                <s:if test="realQuantity_MNG06_UNSUBMIT !=null">
                <s:if test="realQuantity_MNG06_UNSUBMIT >= 0"><s:text name="format.number"><s:param value="realQuantity_MNG06_UNSUBMIT"></s:param></s:text></s:if>
                <s:else><span class="highlight"><s:text name="format.number"><s:param value="realQuantity_MNG06_UNSUBMIT"></s:param></s:text></span></s:else>
                </s:if>
                <s:else>
                    &nbsp;
                </s:else>
            </s:if>
            <s:else>
	            <s:if test="realQuantity !=null">
	            <s:if test="realQuantity >= 0"><s:text name="format.number"><s:param value="realQuantity"></s:param></s:text></s:if>
	            <s:else><span class="highlight"><s:text name="format.number"><s:param value="realQuantity"></s:param></s:text></span></s:else>
	            </s:if>
	            <s:else>
	                &nbsp;
	            </s:else>
            </s:else>
        </li>
        <li>
            <%-- 盘差总数量 --%>
            <s:if test="summQuantity !=null">
            <s:if test="summQuantity >= 0"><s:text name="format.number"><s:param value="summQuantity"></s:param></s:text></s:if>
            <s:else><span class="highlight"><s:text name="format.number"><s:param value="summQuantity"></s:param></s:text></span></s:else>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 盘差总金额 --%>
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
            <%-- 盘点审核状态 --%>
            <s:if test='@com.cherry.cm.core.CherryConstants@CAAUDIT_FLAG_UNSUBMIT.equals(verifiedFlag)'>
                <span class="verified_unsubmit">
                    <span><s:property value='#application.CodeTable.getVal("1322", verifiedFlag)'/></span>
                </span>
            </s:if>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CAAUDIT_FLAG_SUBMIT.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1322", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CAAUDIT_FLAG_AGREE.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1322", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CAAUDIT_FLAG_DISAGREE.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1322", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CAAUDIT_FLAG_DISAGREE1.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1322", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CAAUDIT_FLAG_SUBMIT1.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1322", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CAAUDIT_FLAG_SUBMIT2.equals(verifiedFlag)'>
                <span class="verifying">
                    <span><s:property value='#application.CodeTable.getVal("1322", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CAAUDIT_FLAG_AGREE2.equals(verifiedFlag)'>
                <span class="verified">
                    <span><s:property value='#application.CodeTable.getVal("1322", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@CAAUDIT_FLAG_DISAGREE2.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1322", verifiedFlag)'/></span>
                </span>
            </s:elseif>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD.equals(verifiedFlag)'>
                <span class="verified_rejected">
                    <span><s:property value='#application.CodeTable.getVal("1322", verifiedFlag)'/></span>
                </span>
            </s:elseif>
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
    </ul>  
    </s:iterator>
    </div>
</s:i18n>