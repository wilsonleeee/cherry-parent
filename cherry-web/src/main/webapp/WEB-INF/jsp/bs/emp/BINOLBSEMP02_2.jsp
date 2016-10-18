<%-- BAS考勤信息  datatable--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSEMP02">
    <div id="aaData">
    <s:iterator value="attendanceInfoList" id="attendanceinfo">
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="RowNumber"/>
        </li>
        <li>
            <%-- 柜台--%>
            <s:if test='departName != null && !"".equals(departName)'>
                <s:property value="departName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 到柜时间 --%>
            <s:if test='arriveTime != null && !"".equals(arriveTime)'>
                <s:date name="arriveTime" format="%{@com.cherry.cm.core.CherryConstants@DATE_PATTERN_24_HOURS}"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 离柜时间 --%>
            <s:if test='leaveTime != null && !"".equals(leaveTime)'>
                <s:date name="leaveTime" format="%{@com.cherry.cm.core.CherryConstants@DATE_PATTERN_24_HOURS}"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>