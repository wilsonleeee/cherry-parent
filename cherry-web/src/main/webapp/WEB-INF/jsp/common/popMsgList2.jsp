<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/common/popMsgList2.js"></script>
<div id="msgListTitle" class="hide"><s:text name="header.message" /></div>
<div class="main container clearfix">
    <div class="panel ui-corner-all">
        <s:url id="TopAction_getMsgListSearchUrl" action="TopAction_getMsgList2_search"/>
        <s:hidden name="TopAction_getMsgListSearch" value="%{TopAction_getMsgListSearchUrl}"/>

        <s:url id="TopAction_setMsgReadUrl" action="TopAction_setMsgRead2"/>
        <s:hidden name="TopAction_setMsgRead" value="%{TopAction_setMsgReadUrl}"/>

        <div class="panel-content">
            <div class="box">
                <cherry:form id="mainForm" action="TopAction_getMsgList2_search" method="post" class="inline" onsubmit="popMsgList2.search();return false;">
                    <%--<p class="clearfix">
                        &lt;%&ndash; 柜台查询按钮 &ndash;%&gt;
                        <button class="right search" onclick="popMsgList2.search();return false;">
                            <span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
                        </button>
                    </p>--%>
                </cherry:form>
            </div>

            <div id="section" class="section hide">
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-search-result"></span>
                        <s:text name="global.page.list"/>
                    </strong>
                </div>
                <div class="section-content">

                    <table id="msgList_dataTable2" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                        <thead>
                            <tr>
                               <th><s:text name="global.page.number"/></th><%--No.--%>
                               <th><s:text name="header.messageTime"/></th><%--时间 --%>
                               <th><s:text name="header.messageType"/></th><%--类型 --%>
                               <th><s:text name="header.messageTitle"/></th><%--标题 --%>
                               <th><s:text name="header.messageContent"/></th><%--内容 --%>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div id="pop" style="display:none">
        <div id="region"></div>
        <div id="messageDatail" class="ui-dialog-content ui-widget-content" style="width: auto; min-height: 0px;" ></div>
        <div id="detail">
            <p class="clearfix center">
                <strong>
                    <span id="messageTitle"></span>
                </strong>
            </p>
            <div class="section">
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-info-edit"></span>
                        <s:text name="header.messageContent"/><%-- 问题详细 --%>
                    </strong>
                </div>
            </div>
            <div class="box2-active">
                <div class="box2 box2-content ui-widget" >
                    <div style="word-break:break-all;word-wrap:break-word;">
                        <label style="display: block;margin-right: 0px; " id="messageBody"></label>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />