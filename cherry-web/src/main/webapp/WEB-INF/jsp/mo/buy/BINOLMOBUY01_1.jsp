<%-- 考勤信息查询  datatable--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOBUY01">
    <div id="aaData">
    <s:iterator value="udiskAttendanceStatisticsList" id="attendanceinfo1">
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="#attendanceinfo1.RowNumber"/>
        </li>
        <li>
        	<s:property value="#attendanceinfo1.UdiskSN"/>
        </li>
        <li>
            <%-- 员工名称  --%>
            <s:property value="EmployeeCodeName"/>
        </li>
        <li>
            <%-- 岗位 --%>
            <s:property value="CategoryName"/>
        </li>
        <li>
            <%-- 大区--%>
            <s:property value="DepartCodeName"/>
        </li>
        <li>
            <%-- 城市 --%>
            <s:property value="RegionNameChinese"/>
        </li>
        <li>
            <%-- 巡柜柜台数--%>
            <s:property value="ArriveCountersNm"/>
        </li>
        <li>
            <%-- 巡柜天数 --%>
            <s:property value="ArriveDays"/>
        </li>
         <li>
            <%-- 平均每天拜访柜台数--%>
            <s:property value="ArriNumPerDay"/>
        </li>
         <li>
            <%-- 平均每家柜台停留时间(小时) --%>
            <s:property value="PerCoutArriTime"/>
        </li>
         <li>
            <%-- 关键时间段内在柜天数--%>
            <s:property value="ImportTimeDays"/>
        </li>
        <li>
            <%-- 拜访不同柜台数--%>
            <s:property value="ArriveDiffCountersNm"/>
        </li>
        <li>
            <%-- 应拜访柜台数--%>
           <s:property value="DepartNum"/>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>