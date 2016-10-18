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
		<s:iterator value="employeesList" status="status">
			
			<ul>
				<%-- No. --%>
				<li>${RowNumber}</li>
				<%-- （员工CODE）员工姓名 --%>
				<li><span><s:property value="'('+EmployeeCode+')'+EmployeeName"/></span></li>
				<%-- 登录帐号--%>
				<li><span><s:property value="LonginName"/></span></li>
				<%-- 所在部门  --%>
				<li><span><s:property value="'('+DepartCode+')'+DepartName"/></span></li>
				<%-- 所在岗位--%>
				<li><span><s:property value="CategoryName"/></span></li>
				<%-- 所属品牌 --%>
				<li><span><s:property value="'('+BrandCode+')'+BrandNameChinese"/></span></li>
				<%--查看角色--%>
				<li>
					<a id="<s:property value='BIN_EmployeeID'/>" class="delete" onclick="binOLPLRLA05.getRolesByEmployee(this);return false;">
						<span class="ui-icon-document ui-icon "></span>
                		<span class="button-text"><s:text name="RLA05_Roles"/></span>
					</a>
					<a id="<s:property value='BIN_EmployeeID'/>" class="delete" onclick="binOLPLRLA05.getMenusByEmployee(this);return false;">
						<span class="ui-icon-document ui-icon "></span>
                		<span class="button-text"><s:text name="RLA05_Resource"/></span>
					</a>
				</li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>