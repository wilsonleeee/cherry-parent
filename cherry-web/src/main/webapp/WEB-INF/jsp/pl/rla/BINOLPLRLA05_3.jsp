<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pl.BINOLPLRLA05">
	<div id="aaData">
		<s:iterator value="rolesList" status="status">
			<ul>
				<%-- No. --%>
				<li>${RowNumber}</li>
				<%-- 角色名称 --%>
				<li><span title="<s:property value='Decription'/>"><s:property value="RoleName"/></span></li>
				<%-- 角色类型 --%>
				<li><span><s:property value="#application.CodeTable.getVal('1009',RoleKind)"/></span></li>
				<%-- 所属品牌 --%>
				<li><span><s:property value="'('+BrandCode+')'+BrandNameChinese"/></span></li>
				<%-- 查看员工   --%>
				<li>
					<a id="<s:property value='BIN_RoleID'/>" name="<s:property value='RoleKind'/>" class="delete" onclick="binOLPLRLA05.getEmployeesByRole(this);return false;">
						<span class="ui-icon-document ui-icon "></span>
                		<span class="button-text"><s:text name="RLA05_Employees"/></span>
					</a>
				</li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>