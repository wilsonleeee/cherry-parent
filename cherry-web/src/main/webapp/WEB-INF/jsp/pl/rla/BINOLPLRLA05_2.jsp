<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.pl.BINOLPLRLA05">
	<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="roleInfoList">
		<thead>
			<tr>
				<%-- No. --%>
				<th class="center"><s:text name="No." /></th>
				<%-- 角色名称 --%>
				<th class="center"><s:text name="RLA05_RoleName" /></th>
				<%-- 角色类型 --%>
				<th class="center"><s:text name="RLA05_RoleKind" /></th>
				<%-- 开始时间 --%>
				<th class="center"><s:text name="RLA05_StartDate" /></th>
				<%-- 结束时间 --%>
				<th class="center"><s:text name="RLA05_EndDate" /></th>
				<%-- 权限类型 --%>
				<th class="center"><s:text name="RLA05_PrivilegeType" /></th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="rolesList" status="status">
				<tr>
					<%-- No. --%>
					<td>${status.index+1}</td>
					<%-- 角色名称 --%>
					<td><s:property value="RoleName"></s:property></td>
					<%-- 角色类型 --%>
					<td class="center"><s:property value="#application.CodeTable.getVal('1009',RoleKind)"></s:property></td>
					<%-- 开始时间 --%>
					<td class="center"><s:property value="StartDate"></s:property></td>
					<%-- 结束时间 --%>
					<td class="center"><s:property value="ExpireDate"></s:property></td>
					<%-- 权限类型--%>
					<td class="center"><s:property value="#application.CodeTable.getVal('1010',PrivilegeFlag)"></s:property></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:i18n>