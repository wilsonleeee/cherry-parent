<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mb.BINOLMBRPT14">
    <div id="aaData">
        <s:iterator value="memberCompleteList" id="memberComplete">
            <ul>
                <li><s:property value="RowNumber"/></li>
                    <%-- 会员手机号 --%>
                <li><span>
                <s:url action="BINOLMBMBM10_init" id="memberDetailUrl">
                    <s:param name="memberInfoId" >${memberComplete.BIN_MemberInfoID}</s:param>
                </s:url>
                <a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
                    <s:property value="mobilePhone"/>
                </a>
                </span></li>
                <li><%-- 会员姓名  --%>
                    <s:property value="memberName" /></li>
                <li><%-- 完善度  --%>
                    <s:property value="totalPercent" />%</li>
            </ul>
        </s:iterator></div>
</s:i18n>