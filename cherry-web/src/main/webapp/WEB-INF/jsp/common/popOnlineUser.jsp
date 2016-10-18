<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/popOnlineUser.js"></script>
<div id="onlineUserTitle" class="hide"><s:text name="header.onlineUser" /></div>
<div id="onlineUserDialogInit" class="hide">
<div id="onlineUserDialog" class="hide">
    <s:url action="TopAction_export" id="TopAction_downOnlineUserUrl" namespace="/lg"></s:url>
    <a id="TopAction_downOnlineUserUrl" href="${TopAction_downOnlineUserUrl}"></a>
    <div class="toolbar clearfix">
    <a id="export" class="export"  onclick="exportOnlineUserExcel();return false;">
        <span class="ui-icon icon-export"></span>
        <span class="button-text"><s:text name="global.page.export"/></span>
    </a>
    </div>
    <table id="onlineUser_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.number"/></th><%--No.--%>
               <th><s:text name="onlineUser.LoginName"/></th><%--登录帐号 --%>
               <th><s:text name="onlineUser.LoginIP"/></th><%--登录IP --%>
               <th><s:text name="onlineUser.LoginTime"/></th><%--登录时间 --%>
               <th><s:text name="onlineUser.UserAgent"/></th><%--浏览器版本 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
    <span id ="global_page_close" style="display:none;"><s:text name="global.page.close"/></span>
</div>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />