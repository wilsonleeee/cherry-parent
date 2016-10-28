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
            <s:url id="updateUrl" action="BINOLMOMUP02_init">
                <%-- 渠道名称   --%>
                <s:param name="softwareVersionInfoId">${softVersionInfo.BIN_SoftwareVersionInfoID}</s:param>
            </s:url>
            <ul>
                <li>
                        <%-- No. --%>
                    <s:property value="#softVersionInfo.BIN_SoftwareVersionInfoID"/>
                </li>
                <li>
                    <a href="${updateUrl}" class="left" onclick="javascript:openWin(this);return false;">
                            <s:property value="#softVersionInfo.Version"/>
                    </a>
                </li>
                <li>
                        <%-- 下载地址  --%>
                    <s:property value="#softVersionInfo.DownloadURL"/>
                </li>
                <li>
                        <%-- MD5Key  --%>
                    <s:property value="#softVersionInfo.MD5Key"/>
                </li>
                <li>
                        <%-- 放开更新时间 --%>
                    <s:property value="#softVersionInfo.OpenUpdateTime"/>
                </li>
                <li>
                    <s:if test='"1".equals(ValidFlag)'>
                        <span class='ui-icon icon-valid'></span>
                    </s:if><%-- 有效区分 --%>
                    <s:else>
                        <span class='ui-icon icon-invalid'></span>
                    </s:else>
                </li>
            </ul>
        </s:iterator>
    </div>
</s:i18n>