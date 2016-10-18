<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="sessionUnReadCount"><s:property value="#session.UnReadCount"/></div>
<div id="aaData">
<s:iterator value="msgList" id="messageMap">
    <ul>
        <li>
            <span><s:property value="#messageMap.No"/></span>
            <span class="hide" id="msgListNo_<s:property value="#messageMap.No"/>"><s:property value="#messageMap.MessageID"/></span>
        </li>
        <li><span><s:property value="#messageMap.time"/></span></li>
        <li>
            <span>
                <s:if test="#messageMap.type.equals('export')">
                    <s:text name="header.messageType_0"/>
                </s:if>
                <s:elseif test="#messageMap.type.equals('osworkflow')">
                    <s:text name="header.messageType_1"/>
                </s:elseif>
                <s:elseif test="#messageMap.type.equals('PRT')">
                    <s:text name="header.messageType_PRT"/>
                </s:elseif>
                <s:elseif test="#messageMap.type.equals('PRM')">
                    <s:text name="header.messageType_PRM"/>
                </s:elseif>
                <s:elseif test="#messageMap.type.equals('DPRT')">
                    <s:text name="header.messageType_DPRT"/>
                </s:elseif>
                <s:elseif test="#messageMap.type.equals('ACT')">
                    <s:text name="header.messageType_ACT"/>
                </s:elseif>
            </span>
        </li>
        <li>
	        <span>
		        <s:if test="#messageMap.type.equals('export')">
		            <span class="hide" id="msgDownloadFileURL_<s:property value="#messageMap.No"/>"><s:property value="#messageMap.content"/></span>
		            <s:property value="#messageMap.filename"/>&nbsp;<s:text name="header.msgDownloadInfo"/>
		            <button onclick="popMsgList.downloadFile('<s:property value="#messageMap.No"/>')"  type="button"  class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
		                <span class="ui-button-text"><s:text name="global.page.download"/></span>
		            </button><br/>
		            <s:text name="header.msgDownloadNotice"/>
		        </s:if>
		        <s:elseif test="#messageMap.type.equals('osworkflow')" >
                    <div>
                        <s:text name="global.page.workInfo"/>：<s:property value='#application.CodeTable.getVal("1131", #messageMap.OpCode)'/>&nbsp;&nbsp;
                        <s:url value="%{#messageMap.OpenBillURL}%{#messageMap.BillID}" id="billUrl"></s:url>
                        <s:text name="global.page.workResult"/>：<a href="${billUrl}"  style="color:#3366FF;" onclick="javascript:popMsgList.setMsgRead('<s:property value="#messageMap.MessageID"/>');openWin(this);return false;"><s:property value="#messageMap.BillNo"/></a> <s:property value='#application.CodeTable.getVal("1152", #messageMap.OpResult)'/> 
                    </div>
		        </s:elseif>
		        <s:else>
                    <s:property value="#messageMap.content"/>
		        </s:else>
	        </span>
        </li>
        <li>
            <span>
                <s:if test="#messageMap.readType.equals('1') ||  #messageMap.readType == 1">
                    <s:text name="header.messageStatus_1"/>
                </s:if>
                <s:else>
                    <s:text name="header.messageStatus_0"/>
                </s:else>
            </span>
        </li>
        <li>
            <s:if test="!#messageMap.readType.equals('1') && #messageMap.readType != 1">
	            <a id ='<s:property value="#messageMap.MessageID"/>' class="add" onclick="popMsgList.setMsgRead('<s:property value="#messageMap.MessageID"/>');">
	                <span class="ui-icon icon-confirm"></span>
	                <span class="button-text"><s:text name="header.messageSetRead"/></span>
	            </a>
            </s:if>
        </li>
        <li></li><%--隐藏列 --%>
    </ul>
</s:iterator>
</div>