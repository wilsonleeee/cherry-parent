<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBMBM23"> 
<div id="aaData">
<s:iterator value="smsSendDetailList" id="smsSendDetailMap">
<ul>
<li><span>
  <s:set value="%{#smsSendDetailMap.message }" var="message"></s:set>
  <a title='<s:text name="binolmbmbm23_message" />|<s:property value="message"/>' class="description">
	<cherry:cut length="30" value="${message }"></cherry:cut>
  </a>
</span></li>
<li><span><s:property value="mobilephone"/></span></li>
<li><span>
<s:if test="%{#smsSendDetailMap.dataSource == 1}">
<s:property value="planName"/>
</s:if>
<s:elseif test="%{#smsSendDetailMap.dataSource == 2}">
<s:property value='#application.CodeTable.getVal("1219", #smsSendDetailMap.planCode)' />
</s:elseif>
</span></li>
<li><span><s:property value="couponCode"/></span></li>
<li><span><s:property value="sendTime"/></span></li>
<li><span>

<s:url action="BINOLCTRPT03_send" namespace="/ct" id="sendMsgUrl">
	<s:param name="messageCode" value="messageCode"></s:param>
</s:url>
<a class="add" href="" onclick="binolmbmbm23.sendMsgInit('${sendMsgUrl}');return false;">
   <span class="ui-icon icon-ok"></span>
   <span class="button-text"><s:text name="binolmbmbm23_sendMsgInitDialogTitle" /></span>
</a>
</span></li>
</ul>
</s:iterator>
</div>
</s:i18n>