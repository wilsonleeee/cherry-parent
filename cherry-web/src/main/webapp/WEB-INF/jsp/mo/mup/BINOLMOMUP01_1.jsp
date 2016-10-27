<%-- 考勤信息查询  datatable--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOMUP01">
    <div id="aaData">
    <s:iterator value="softVersionInfoList" id="softVersionInfo">
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="#softVersionInfo.BIN_SoftwareVersionInfoID"/>
        </li>
        <li>
        	<s:property value="#softVersionInfo.Version"/>
        </li>
        <li>
            <%-- 下载地址  --%>
            <s:property value="#softVersionInfo.DownloadURL"/>
        </li>
        <li>
            <%-- 放开更新时间 --%>
            <s:property value="#softVersionInfo.OpenUpdateTime"/>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>