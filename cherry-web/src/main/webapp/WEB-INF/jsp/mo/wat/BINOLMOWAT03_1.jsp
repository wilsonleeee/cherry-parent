<%-- 会员异常数据监控 datatable--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOWAT03">
    <div id="aaData">
    <s:iterator value="memberInfoList" id="counterinfo">
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="RowNumber"/>
        </li>
        <li>
            <%-- 会员卡号 --%>
            <s:if test='memberCode != null && !"".equals(memberCode)'>
                <s:property value="memberCode"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 会员姓名 --%>
            <s:if test='memberName != null && !"".equals(memberName)'>
                <s:property value="memberName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 购买次数 --%>
            <s:if test='count != null && !"".equals(count)'>
                <s:text name="format.number"><s:param value="count"/></s:text>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 总数量 --%>
            <s:if test='sumQuantity != null && !"".equals(sumQuantity)'>
                <s:text name="format.number"><s:param value="sumQuantity"/></s:text>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 总金额 --%>
            <s:if test='sumAmount != null && !"".equals(sumAmount)'>
                <s:text name="format.price"><s:param value="sumAmount"/></s:text>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>