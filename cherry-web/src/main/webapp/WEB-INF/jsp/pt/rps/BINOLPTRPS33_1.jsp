<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS33">
<%-- 销售总数量及总金额 --%>
<s:if test='!(saleSumInfo != null && saleSumInfo.size() != 0)'>
    <div id="headInfo">
        <s:text name="RPS33_sumQuantity"/>
        <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
            <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="RPS33_sumAmount"/>
        <span title="包括运费(订单状态取消的除外)" class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
            <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
        </span>
        <s:text name="RPS33_sumExpressCost"/>
        <span title="不包括状态为取消的订单" class="<s:if test='sumInfo.sumExpressCost < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
            <strong><s:text name="format.price"><s:param value="sumInfo.sumExpressCost"></s:param></s:text></strong>
        </span>
    </div>
</s:if>
<div id="aaData">
    <s:iterator value="esOrderMainList" id="esOrderMain">
        <s:url id="detailsUrl" value="/pt/BINOLPTRPS34_init">
            <s:param name="esOrderMainID">${esOrderMain.esOrderMainID}</s:param>
        </s:url>
        <ul>
            <%-- No. --%>
            <li>${esOrderMain.RowNumber}</li>
            <%-- 单据号 --%>
            <li>
                <a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;">
                    <s:property value="billCode"/>
                </a>
            </li>
            <%-- 部门 --%>
            <li><span><s:property value="'('+departCode+')'+departName"/></span></li>
            <%-- 销售人员 --%>
            <li><span><s:property value="'('+employeeCode+')'+employeeName"/></span></li>
            <%-- 会员卡号 --%>
            <li>
                <span>
                    <s:url value="/mb/BINOLMBMBM10_init" id="memberDetailUrl">
                        <s:param name="memCode">${memberCode}</s:param>
                    </s:url>
                    <%-- 判断如果会员卡都是0，则不显示会员卡号链接 --%>
                    <s:if test="(memberCode.replace('0',' ')).trim().length() != 0">
                        <a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;">
                            <s:property value="memberCode"/>
                        </a>
                    </s:if>
                    <s:else>
                            <s:property value="memberCode"/>
                    </s:else>
                </span>
            </li>
            <%-- 业务类型   --%>
            <li><span><s:property value='#application.CodeTable.getVal("1055", saleType)'/></span></li>
            <%-- 消费者类型   --%>
            <li><span><s:property value='#application.CodeTable.getVal("1105", consumerType)'/></span></li>
            <%-- 单据类型 --%>
            <li><span><s:property value='#application.CodeTable.getVal("1261",ticketType)'/></span></li>
            <%-- 总数量 --%>
            <li>
                <s:if test="quantity >= 0"><span><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:if>
                <s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
            </li>
            <%-- 总金额  --%>
            <li>
                <s:if test="payAmount >= 0"><span><s:text name="format.price"><s:param value="payAmount"></s:param></s:text></span></s:if>
                <s:else><span class="highlight"><s:text name="format.price"><s:param value="payAmount"></s:param></s:text></span></s:else>
            </li>
            <%-- 运费  --%>
            <li>
                <s:if test="expressCost >= 0"><span><s:text name="format.price"><s:param value="expressCost"></s:param></s:text></span></s:if>
                <s:else><span class="highlight"><s:text name="format.price"><s:param value="expressCost"></s:param></s:text></span></s:else>
            </li>
            <%-- 时间 --%>
            <li><span class="right">${esOrderMain.billCreateTime}</span></li>
            <%-- 单据状态 --%>
            <li><span><s:property value='#application.CodeTable.getVal("1310", billState)'/></span></li>
            <%-- 是否预售 --%>
            <li><span><s:property value='#application.CodeTable.getVal("1344", preSale)'/></span></li>
        </ul>
    </s:iterator>
</div>
</s:i18n>