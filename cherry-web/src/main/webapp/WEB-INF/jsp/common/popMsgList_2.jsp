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
            <span><s:property value="RowNumber"/></span>
            
        </li>
        <li><span><s:property value="PublishDate"/></span></li>
        <li>
            <span>
                <s:property value='#application.CodeTable.getVal("1371", MessageType)'/>
            </span>
        </li>
        <li>
	        <span>
		        <s:property value="MessageTitle"/>
	        </span>
        </li>
        <li>
            <span>
                <a counter_message_id="<s:property value="BIN_CounterMessageID"/>" message_title="<s:property value="MessageTitle"/>" message_body='<s:property value="MessageBody"/>'
                    style="cursor:pointer;" onclick="popMsgList2.showDetail(this);return false;" href="#">
                    <s:property value="messageBody_temp"/>
                </a>
            </span>
        </li>
    </ul>
</s:iterator>
</div>