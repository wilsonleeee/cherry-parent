<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.cp.BINOLCPACT01">
<div id="aaData">
	  <s:iterator value="memList" id="memberInfo">
    <ul> 
        <%--对象类型--%>
        <li><s:property value="#application.CodeTable.getVal('1198',customerType)"/></li>
        <%--会员卡号 --%>
        <li><s:property value="memCode"/></li>
        <%--名称 --%>
        <li><s:property value="memName"/></li>
        <%--手机号 --%>
        <li><s:property value="mobilePhone"/></li>
        <%--生日--%>
        <li><s:property value="birthDay"/></li>
        <li><s:property value="joinDate"/></li>
        <li>
        <s:if test='changablePoint==null || "".equals(changablePoint)'>0</s:if>
        <s:else>
         <s:text name="format.number">
       	 	<s:param value="changablePoint"></s:param>
         </s:text>
        </s:else>
        </li>
        <%--接收短信 --%>
        <li><s:property value="#application.CodeTable.getVal('1231',receiveMsgFlg)"/></li>
    </ul>  
    </s:iterator>
</div>
</s:i18n>