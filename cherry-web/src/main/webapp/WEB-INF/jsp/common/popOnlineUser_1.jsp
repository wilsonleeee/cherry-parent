<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="onlineUserInfoList" id="onlineUserMap">
    <ul>
        <li><span><s:property value="#onlineUserMap.No"/></span></li>
        <li><span><s:property value="#onlineUserMap.LoginName"/></span></li>
        <li><span><s:property value="#onlineUserMap.LoginIP"/></span></li>
        <li><span><s:property value="#onlineUserMap.LoginTime"/></span></li>
        <li>
            <a title="<s:property value="#onlineUserMap.UserAgent"/>" style="cursor:pointer;color: #3366FF;">
                <span><s:property value="#onlineUserMap.UserAgentLite"/></span>
            </a>
        </li>
    </ul>
</s:iterator>
</div>