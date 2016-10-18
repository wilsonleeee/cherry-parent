<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSEMP02">
    <div id="aaData">
    <s:iterator value="departPrivilegeList" id="departPrivilegeInfo">
    <ul>
        <li>
            (<s:property value="departCode"/>)<s:property value="departName"/>
        </li>
        <li>
            <s:property value='#application.CodeTable.getVal("1000", #departPrivilegeInfo.type)' />
        </li>
        <li>
            <s:if test="%{#departPrivilegeInfo.privilegeFlag == 1}"><s:text name="employee.followPrivilege"></s:text></s:if>
            <s:if test="%{#departPrivilegeInfo.privilegeFlag == 0}"><s:text name="employee.likePrivilege"></s:text></s:if>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>