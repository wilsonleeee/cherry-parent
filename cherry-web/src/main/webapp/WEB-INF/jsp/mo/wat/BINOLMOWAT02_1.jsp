<%-- 销售异常数据监控 datatable--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOWAT02">
    <div id="aaData">
    <s:iterator value="counterInfoList" id="counterinfo">
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="RowNumber"/>
        </li>
        <li>
            <%-- 区域 --%>
            <s:if test='regionNameChinese != null && !"".equals(regionNameChinese)'>
                <s:property value="regionNameChinese"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 柜台号 --%>
            <s:if test='counterCode != null && !"".equals(counterCode)'>
                <s:property value="counterCode"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 柜台名 --%>
            <s:if test='counterNameIF != null && !"".equals(counterNameIF)'>
                <s:property value="counterNameIF"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 柜台状态 --%>
            <s:if test='counterStatus != null && !"".equals(counterStatus)'>
                <s:property value='#application.CodeTable.getVal("1030", counterStatus)'/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 柜台主管 --%>
            <s:if test='employeeName != null && !"".equals(employeeName)'>
                <s:property value="employeeName"></s:property>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 销售日期 --%>
            <s:if test='saleDate != null && !"".equals(saleDate)'>
                <s:property value="saleDate"></s:property>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 销售金额 --%>
            <s:if test='sumAmount != null && !"".equals(sumAmount)'>
                <s:text name="format.price"><s:param value="sumAmount"></s:param></s:text>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 销售数量 --%>
            <s:if test='sumQuantity != null && !"".equals(sumQuantity)'>
                <s:text name="format.number"><s:param value="sumQuantity"/></s:text>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>