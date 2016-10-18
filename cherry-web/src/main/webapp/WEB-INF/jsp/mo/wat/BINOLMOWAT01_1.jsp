<%-- 终端实时监控 datatable--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<s:set id="DATE_PATTERN_24_HOURS"><%=CherryConstants.DATE_PATTERN_24_HOURS%></s:set>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOWAT01">
<s:if test='connectInfo != null'>
    <div id="headInfo">
        <s:text name="WAT01_normalConnectCount"/><span class="green"><strong><s:property value="connectInfo.normalConnectCount"></s:property></strong></span>
        &nbsp;
        <s:text name="WAT01_abnormalConnectCount"/><span class="green"><strong><s:property value="connectInfo.abnormalConnectCount"></s:property></strong></span>
    </div>
</s:if>
    <div id="aaData">
    <s:iterator value="machineInfoList" id="machineinfo">
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="RowNumber"/>
            <s:hidden name="connStatus" value="%{connStatus}"></s:hidden>
        </li>
        <li>
            <%-- 机器编号 --%>
            <s:if test='machineCode != null && !"".equals(machineCode)'>
                <s:property value="machineCode"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 所属品牌 --%>
            <s:if test='BrandNameChinese != null && !"".equals(BrandNameChinese)'>
                <s:property value="BrandNameChinese"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 类型 --%>
            <s:if test='machineType != null && !"".equals(machineType)'>
                <s:property value='#application.CodeTable.getVal("1101", machineType)'/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 软件版本 --%>
            <s:if test='softWareVersion != null && !"".equals(softWareVersion)'>
                <s:property value="softWareVersion"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 容量 --%>
            <s:if test='capacity != null && !"".equals(capacity)'>
                <s:property value="capacity"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 总流量 --%>
            <s:if test='internetFlow != null && !"".equals(internetFlow)'>
                <s:property value="internetFlow"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 累积上网时间 --%>
            <s:if test='internetTime != null && !"".equals(internetTime)'>
                <s:property value="internetTime"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 累积上网次数 --%>
            <s:if test='internetTimes != null && !"".equals(internetTimes)'>
                <s:property value="internetTimes"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 最后一次上传数据的时间 --%>
            <s:if test='uploadLasttime != null && !"".equals(uploadLasttime)'>
                <s:property value="uploadLasttime"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 最后一次下载同步数据的时间 --%>
            <s:if test='syncLasttime != null && !"".equals(syncLasttime)'>
                <s:property value="syncLasttime"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 通讯卡号 --%>
            <s:if test='phoneCode != null && !"".equals(phoneCode)'>
                <s:property value="phoneCode"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 省份 --%>
            <s:if test='provinceName != null && !"".equals(provinceName)'>
                <s:property value="provinceName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 城市 --%>
            <s:if test='cityName != null && !"".equals(cityName)'>
                <s:property value="cityName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 柜台 --%>
            <s:if test='counterCodeName != null && !"".equals(counterCodeName)'>
                <s:property value="counterCodeName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- IMSI号 --%>
            <s:if test='iMSIcode != null && !"".equals(iMSIcode)'>
                <s:property value="iMSIcode"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 机器启用的时间 --%>
            <s:if test='startTime != null && !"".equals(startTime)'>
                <s:property value="startTime"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 上一次开机时间--%>
            <s:if test='lastStartTime != null && !"".equals(lastStartTime)'>
                <s:property value="lastStartTime"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 上一次连接时间--%>
            <s:if test='lastConnTime != null && !"".equals(lastConnTime)'>
                <s:property value="lastConnTime"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 连接状态--%>
            <s:if test='connStatus != null && !"".equals(connStatus)'>
                <s:property value='#application.CodeTable.getVal("1122", connStatus)'/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <s:if test="searchType.equals('date')">
        <li>
            <%-- 连接天数--%>
            <s:if test='connDays != null && !"".equals(connDays)'>
                <s:property value="connDays"/>
            </s:if>
        </li>
        </s:if>
    </ul>
    </s:iterator>
    </div>
</s:i18n>