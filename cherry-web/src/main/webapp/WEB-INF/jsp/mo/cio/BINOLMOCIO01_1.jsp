<%-- 柜台消息管理Datatable --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOCIO01">
    <div id="aaData">
    <s:iterator value="counterMessageList" id="counterMessage">
    <s:url id="updateInitUrl" action="BINOLMOCIO01_updateInit">
        <%-- 柜台消息ID --%>
        <s:param name="counterMessageId">${counterMessage.counterMessageId}</s:param>
    </s:url>
    <s:url id="getMessageRegionUrl" action="BINOLMOCIO01_getMessageRegion">
        <%-- 柜台消息ID --%>
        <s:param name="counterMessageId">${counterMessage.counterMessageId}</s:param>
    </s:url>
    <s:url id="publishInitUrl" action="BINOLMOCIO14_init">
        <%-- 柜台消息ID --%>
        <s:param name="counterMessageId">${counterMessage.counterMessageId}</s:param>
        <%-- 品牌ID --%>
        <s:param name="brandInfoId">${counterMessage.brandInfoId}</s:param>
    </s:url>
    <s:url id="disableOrEnableUrl" action="BINOLMOCIO01_disableOrEnable">
        <%-- 柜台消息ID --%>
        <s:param name="counterMessageId">${counterMessage.counterMessageId}</s:param>
    </s:url>
    <s:url id="deleteMsgUrl" action="BINOLMOCIO01_delete">
        <%-- 柜台消息ID --%>
        <s:param name="counterMessageId">${counterMessage.counterMessageId}</s:param>
    </s:url>
    <ul>
        <%-- No. --%>
        <li><s:property value="RowNumber"/></li>
        <li>
        <%-- 消息标题 --%>
            <s:if test='messageTitle != null && !"".equals(messageTitle)'>
                <span><s:property value="messageTitle"/></span>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
        <%-- 消息内容 --%>
            <a title="<s:text name='CIO01_showDetail'/>" id="<s:property value="messageTitle"/>" name='<s:property value="messageBody"/>' style="cursor:pointer;" href="#" onclick="BINOLMOCIO01.showDetail(this);return false;">
	            <s:if test='messageBody_temp != null && !"".equals(messageBody_temp)'>
	                <span><s:property value="messageBody_temp"/></span>
	            </s:if>
	            <s:else>&nbsp;</s:else>
            </a>
        </li>
        <li>
        <%-- 导入批次--%>
            <s:if test='importBatch != null && !"".equals(importBatch)'>
                <span><s:property value="importBatch"/></span>
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
            	<span><label class="highlight"><s:text name="CIO01_unPublish"></s:text></label></span>
			</s:else>
        </li>
        <li>
        	<%-- 状态 --%>
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
        </li>
        <li>
        <%-- 操作 --%>
        	<!-- 未发布的情况【编辑、删除】 -->
       		<s:if test='"0".equals(issuedStatus)'>
	            <!-- 未发布的才能编辑 -->
            	<cherry:show domId="MOCIO01UPDATE">
	               	<a href="${updateInitUrl}" class="add" id="updateBtn" onclick="BINOLMOCIO01.editMsgInit(this);return false;">
	                   <span class="ui-icon icon-edit"></span>
	                   <span class="button-text"><s:text name="CIO01_edit"></s:text></span>
	               	</a>
	            </cherry:show>
            	<!-- 删除（未发布的才能删除） -->
	            <cherry:show domId="MOCIO01DELETE">
		            <a href="${deleteMsgUrl}" id="deleteBtn" class="delete" onclick="BINOLMOCIO01.delMsgInit(this);return false;">
		                <span class="ui-icon icon-disable"></span>
		                <span class="button-text"><s:text name="CIO01_delete"></s:text></span>
		            </a>
	            </cherry:show>
	            <!-- 未过期的才可发布(兼容旧版本的没有起止日期) -->
	            <s:if test='"0".equals(pastStatus) || (null == startValidDate && null == endValidDate)'>
		            <cherry:show domId="MOCIO01PUBLISH">
			            <a href="${publishInitUrl}" class="add" id="publishBtn" onclick="javascript:openWin(this,{height:600, width:700});return false;">
			                <span class="ui-icon icon-enable"></span>
			                <span class="button-text"><s:text name="CIO01_publish"></s:text></span>
			            </a>
		            </cherry:show>
	            </s:if>
       		</s:if>
       		<!-- 发布的分过期与未过期（包含起止日期为空的情况）两种情况 -->
       		<s:elseif test='"1".equals(issuedStatus)'>
       			<!-- 过期(只能查询发布) -->
        		<s:if test='"1".equals(pastStatus) && null != startValidDate && null != endValidDate'>
        			<a href="${getMessageRegionUrl}" class="add" id="regionBtn" onclick="BINOLMOCIO01.getMessageRegion(this);return false;">
	                   <span class="ui-icon icon-publish"></span>
	                   <span class="button-text"><s:text name="CIO01_region"></s:text></span>
	               	</a>
        		</s:if>
        		<!-- 未过期(兼容无起止日期) -->
	        	<s:else>
	       			<s:if test='"1".equals(status)'>
			        	<cherry:show domId="MOCIO01DISABLE">
			               	<a href="${disableOrEnableUrl}" class="delete" id="disableBtn" onclick='BINOLMOCIO01.disableOrEnableDialog(this,"0");return false;'>
			                   <span class="ui-icon icon-disable"></span>
			                   <span class="button-text"><s:text name="CIO01_disable"></s:text></span>
			               	</a>
				        </cherry:show>
			        </s:if>
			        <s:if test='"0".equals(status)'>
				        <cherry:show domId="MOCIO01ENABLE">
			               	<a href="${disableOrEnableUrl}" class="delete" id="enableBtn" onclick='BINOLMOCIO01.disableOrEnableDialog(this,"1");return false;'>
			                   <span class="ui-icon icon-enable"></span>
			                   <span class="button-text"><s:text name="CIO01_enable"></s:text></span>
			               	</a>
			            </cherry:show>
		            </s:if>
		            <cherry:show domId="MOCIO01PUBLISH">
			            <a href="${publishInitUrl}" class="add" id="publishBtn" onclick="javascript:openWin(this,{height:600, width:700});return false;">
			                <span class="ui-icon icon-enable"></span>
			                <span class="button-text"><s:text name="CIO01_publish"></s:text></span>
			            </a>
		            </cherry:show>
	        		<a href="${getMessageRegionUrl}" class="add" id="regionBtn" onclick="BINOLMOCIO01.getMessageRegion(this);return false;">
	                   <span class="ui-icon icon-publish"></span>
	                   <span class="button-text"><s:text name="CIO01_region"></s:text></span>
	               	</a>
	        	</s:else>
       		</s:elseif>
       		<!-- 用按渠道查看发布 -->
       		<s:hidden id="currentMessageId" name="currentMessageId" value="%{#counterMessage.counterMessageId}"></s:hidden>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>
