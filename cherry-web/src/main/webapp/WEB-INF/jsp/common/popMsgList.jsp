<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/common/popMsgList.js"></script>
<div id="msgListTitle" class="hide"><s:text name="header.message" /></div>
<div id="msgListDialogInit" class="hide">
<div id="msgListDialog" class="hide">
    <span>
        <strong><s:text name="header.messageNotice"/></strong>
    </span>
    <div class="toolbar clearfix" style="margin-top:5px;">
	    <a id="btnSetAllRead" class="add" onclick="popMsgList.setAllMsgRead();">
	        <span class="ui-icon icon-confirm"></span>
	        <span class="button-text"><s:text name="header.setAllRead"/></span>
	    </a>
    </div>
    <div class="hide">
        <span id="header_messageStatus_read"><s:text name="header.messageStatus_1"/></span>
        <span id="header_messageStatus_unread"><s:text name="header.messageStatus_0"/></span>
    </div>
    <table id="msgList_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
        <thead>
            <tr>
               <th><s:text name="global.page.number"/></th><%--No.--%>
               <th><s:text name="header.messageTime"/></th><%--时间 --%>
               <th><s:text name="header.messageType"/></th><%--类型 --%>
               <th><s:text name="header.messageContent"/></th><%--内容 --%>
               <th><s:text name="header.messageStatus"/></th><%--状态 --%>
               <th><s:text name="global.page.option"/></th><%--操作 --%>
               <th></th><%--隐藏列--%>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />