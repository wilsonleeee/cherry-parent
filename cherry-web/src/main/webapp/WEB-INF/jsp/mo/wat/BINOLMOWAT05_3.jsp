<%-- 考勤信息查询  datatable--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOWAT05">
    <div id="aaData">
    <s:iterator value="attendanceInfoList" id="attendanceinfo">
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="RowNumber"/>
        </li>
        <li>
            <%-- 大区--%>
            <s:if test='region != null && !"".equals(region)'>
                <s:property value="region"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 省份 --%>
            <s:if test='province != null && !"".equals(province)'>
                <s:property value="province"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 城市 --%>
            <s:if test='city != null && !"".equals(city)'>
                <s:property value="city"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 部门--%>
            <s:if test='departName != null && !"".equals(departName)'>
                <s:property value="departName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 到柜时间 --%>
            <s:if test='arriveTime != null && !"".equals(arriveTime)'>
                <s:property value="arriveTime"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 离柜时间 --%>
            <s:if test='leaveTime != null && !"".equals(leaveTime)'>
                <s:property value="leaveTime"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 在柜工作分钟 --%>
            <s:if test='stayMinutes != null && !"".equals(stayMinutes)'>
                <s:property value="stayMinutes"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>