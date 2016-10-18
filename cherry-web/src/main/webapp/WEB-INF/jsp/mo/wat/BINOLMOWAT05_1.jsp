<%-- 考勤统计信息查询  datatable--%>
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
    <s:iterator value="attendanceInfoList" id="attendanceInfo">
    <s:url id="detailUrl" value="/mo/BINOLMOWAT05_detail">
		<s:param name="employeeId">${attendanceInfo.employeeId}</s:param>
		<s:param name="startAttendanceDate">${attendanceInfo.startAttendanceDate}</s:param>
		<s:param name="endAttendanceDate">${attendanceInfo.endAttendanceDate}</s:param>
		<s:param name="brandInfoId">${attendanceInfo.brandInfoId}</s:param>
		<s:param name="udiskSN">${attendanceInfo.udiskSN}</s:param>
	</s:url>
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="RowNumber"/>
        </li>
        <li>
            <%-- 员工姓名 --%>
            <a href="${detailUrl}" class="popup" onclick="javascript:openWin(this);return false;">
				<s:if test='employeeName != null && !"".equals(employeeName)'>
	                <s:property value="employeeName"/>
	            </s:if>
	            <s:else><s:property value="employeeCode"/></s:else>
			</a>
        </li>
        <li>
            <%-- 区域 --%>
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
            <%-- 岗位 --%>
            <s:if test='categoryName != null && !"".equals(categoryName)'>
                <s:property value="categoryName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- U盘序列号 --%>
            <s:if test='udiskSN != null && !"".equals(udiskSN)'>
                <s:property value="udiskSN"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 巡柜数 --%>
            <s:if test='arrCntCount != null && !"".equals(arrCntCount)'>
                <s:property value="arrCntCount"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 巡柜次数 --%>
            <s:if test='arrCntSum != null && !"".equals(arrCntSum)'>
                <s:property value="arrCntSum"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 在柜工作分钟 --%>
            <s:if test='stayMinutesSum != null && !"".equals(stayMinutesSum)'>
                <s:property value="stayMinutesSum"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>