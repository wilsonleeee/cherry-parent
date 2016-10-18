<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pl.BINOLPLUPM01">
<div id="aaData">
	<s:iterator value="userList" id="user">
		<ul>
		    <%-- 编号 --%>
			<li><s:property value="#user.RowNumber"/></li>
			<%-- 用户账号 --%>
		    <li><s:property value="#user.loginName"/></li>
			<%-- 用户代号 --%>
			<li><s:property value="#user.employeeCode"/></li>
			<%-- 用户名称 --%>
			<li><s:property value="#user.employeeName"/></li>
			<%-- 有效区分 --%>
			<li>
				<s:if test='"1".equals(#user.validFlag)'><span class='ui-icon icon-valid'></span></s:if>
				<s:else><span class='ui-icon icon-invalid'></span></s:else>
			</li>
			<%-- 用户信息编辑URL--%>
            <s:url id="edit_url" action="BINOLPLUPM03_init">
            	<s:param name="brandInfoId">${user.brandInfoId}</s:param>
            </s:url>
            <%-- 用户信息删除URL--%>
            <s:url id="delete_url" action="BINOLPLUPM01_operate"></s:url>
            <%-- 按钮操作--%>
		    <li>
		        <%-- 编辑按钮--%>
				<a id="editUrl" class="edit" onclick="javascript:popEditDialog(this);return false;" href="${edit_url}">
	               <span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="upm01_edit" /></span>
                </a>
                <s:if test='"1".equals(#user.validFlag)'>
                   <%-- 无效按钮--%>
                   <a id="deleteUserUrl" class="delete" href="${delete_url}" onclick="deleteUserInit(this,0);return false;">
	                  <span class="ui-icon icon-disable"></span><span class="button-text"><s:text name="upm01_delete" /></span></a>
                </s:if>
				<s:else>
				   <%-- 有效按钮--%>
                   <a id="enableUserUrl" class="add" href="${delete_url}" onclick="enableUserInit(this,1);return false;">
	                  <span class="ui-icon icon-enable"></span><span class="button-text"><s:text name="upm01_enable" /></span></a>
                </s:else>
                <span id="userParam" class="hide">
                   <s:hidden id="userId" name="userId" value="%{#user.userId}"></s:hidden>
                   <s:hidden name="modifyTime" value="%{#user.modifyTime}"></s:hidden>
                   <s:hidden name="modifyCount" value="%{#user.modifyCount}"></s:hidden>
                </span>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
