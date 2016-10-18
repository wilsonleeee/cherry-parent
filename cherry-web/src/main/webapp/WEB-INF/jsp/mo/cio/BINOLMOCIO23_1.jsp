<%-- 柜台消息管理Datatable --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOCIO23">
    <div id="aaData">
    <s:iterator value="departmentMessageList" id="departmentMessage">
     <s:url id="detailUrl" action="BINOLMOCIO23_detail">
        <%-- 柜台消息ID --%>
        <s:param name="departmentMessageId">${departmentMessage.departmentMessageId}</s:param>
    </s:url>
    <ul>
        <%-- No. --%>
        <li><s:property value="RowNumber"/></li>
        <li>
        <span>
				  <%-- 消息标题 --%>
				<a href="${detailUrl}" class="popup" onclick="javascript:openWin(this);return false;">
					<s:if test='messageTitle != null && !"".equals(messageTitle)'>
		                <s:property value="messageTitle"/>
		            </s:if>
		            <s:else>&nbsp;</s:else>
				</a>
				</span>
           
        </li>
        <li>
        <%-- 消息类型--%>
            <s:if test='messageType != null && !"".equals(messageType)'>
                <span><s:property value='#application.CodeTable.getVal("1413", messageType)'/></span>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
        	<%-- 生效开始日期 --%>
            <s:if test='startValidDate != null && !"".equals(startValidDate)'>
                <span><s:property value="startValidDate"/></span>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
        	<%--生效结束日期 --%>
            <s:if test='endValidDate != null && !"".equals(endValidDate)'>
                <span><s:property value="endValidDate"/></span>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
        	<%-- 发布日期 --%>
            <s:if test='publishDate != null && !"".equals(publishDate)'>
                <span><s:property value="publishDate"/></span>
            </s:if><s:else>
            	<span><label class="highlight"><s:text name="CIO21_unPublish"></s:text></label></span>
			</s:else>
        </li>
<%--         <li>
        	状态
            <s:if test='"0".equals(status)'>
                <span class="verified_rejected">
                	<span><s:property value='#application.CodeTable.getVal("1194", status)'/></span>
                </span>
            </s:if><s:elseif test='"1".equals(status)'>
	            <span class="verified">
	            	<span><s:property value='#application.CodeTable.getVal("1194", status)'/></span>
	            </span>
            </s:elseif>
            <s:else>&nbsp;</s:else>
        </li> --%>
    </ul>
    </s:iterator>
    </div>
</s:i18n>
