<%-- 异常盘点次数监控 datatable 盘差--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOWAT04">
    <div id="aaData">
    <s:iterator value="abnormalGainQuantityList" id="abnormalGainQuantity">
    <s:url id="detailsUrl" value="/pt/BINOLPTRPS21_init">
        <%-- 产品盘点ID --%>
        <s:param name="stockTakingId">${abnormalGainQuantity.stockTakingId}</s:param>
    </s:url>
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="RowNumber"/>
        </li>
        <li>
            <%-- 盘点单号 --%>
            <s:if test='stockTakingNo != null && !"".equals(stockTakingNo)'>
                <a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;"><s:property value="stockTakingNo"/></a>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 部门编号 --%>
            <s:if test='departCode != null && !"".equals(departCode)'>
                <s:property value="departCode"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 部门名称 --%>
            <s:if test='departName != null && !"".equals(departName)'>
                <s:property value="departName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
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
            <%-- 日期 --%>
            <s:if test='date != null && !"".equals(date)'>
                <s:property value="date"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 操作员 --%>
            <s:if test='employeeName != null && !"".equals(employeeName)'>
                <s:property value="employeeName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>