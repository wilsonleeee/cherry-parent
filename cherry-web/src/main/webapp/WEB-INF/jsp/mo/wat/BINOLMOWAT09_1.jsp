<%-- BA考勤信息查询  datatable--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOWAT09">
    <div id="aaData">
    <s:iterator value="attendanceInfoList" id="attendanceinfo">
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="RowNumber"/>
        </li>
        <li>
            <%-- 柜台编号 --%>
            <s:if test='counterCode != null && !"".equals(counterCode)'>
                <s:property value="counterCode"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 柜台名称 --%>
            <s:if test='counterName != null && !"".equals(counterName)'>
                <s:property value="counterName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- BA工号--%>
            <s:if test='employeeCode != null && !"".equals(employeeCode)'>
                <s:property value="employeeCode"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- BA姓名 --%>
            <s:if test='employeeName != null && !"".equals(employeeName)'>
                <s:property value="employeeName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 考勤日期 --%>
            <s:if test='attendanceDate != null && !"".equals(attendanceDate)'>
                <s:property value="attendanceDate"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 上班时间 --%>
            <s:if test='arriveTime != null && !"".equals(arriveTime)'>
                <s:property value="arriveTime"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 下班时间 --%>
            <s:if test='leaveTime != null && !"".equals(leaveTime)'>
                <s:property value="leaveTime"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>