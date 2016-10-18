<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.wp.BINOLWSMNG02">
    <div id="headInfo">
        <s:text name="WSMNG02_sumQuantity"/>
        <span class="<s:if test='sumInfo.sumQuantity < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.number"><s:param value="sumInfo.sumQuantity"></s:param></s:text></strong>
        </span>
        <s:text name="WSMNG02_sumAmount"/>
        <span class="<s:if test='sumInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>">
            <strong><s:text name="format.price"><s:param value="sumInfo.sumAmount"></s:param></s:text></strong>
        </span>
    </div>
    <div id="aaData">
    <s:iterator value="deliverList" id="deliver">
        <s:url id="detailsUrl" action="BINOLWSMNG02_detailInit">
            <%-- 产品收发货ID --%>
            <s:param name="productDeliverId">${deliver.deliverId}</s:param>
        </s:url>
        <ul>
            <li>
                <%-- No. --%>
                <s:property value="RowNumber"/>
            </li>
            <li>
                <%-- 发货单号 --%>
                <a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;">
                    <s:property value="deliverNo"/>
                </a>
            </li>
            <li>
                <%-- 发货日期 --%>
                <s:if test='deliverDate != null && !"".equals(deliverDate)'>
                    <s:if test='dateRange > 5'>
                        <span style="color:red">
                            <s:property value="deliverDate"/>
                        </span>
                    </s:if>
                    <s:else>
                        <s:property value="deliverDate"/>
                    </s:else>
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
        </ul>
    </s:iterator>
    </div>
</s:i18n>