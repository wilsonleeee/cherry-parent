<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/pl/rla/BINOLPLRLA05.js"></script>

<s:i18n name="i18n.pl.BINOLPLRLA05">
<div id="div_main"></div>
<div class="panel-header">
       <div class="clearfix"> 
        <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span> 
       </div>
</div>
<div id="actionResultDisplay"></div>
<div style="display:none">
	<s:text name="RLA05_Select" id="RLA05_Select"></s:text>
	<s:url id="getEmployees_url" value="/pl/BINOLPLRLA05_getEmployees"></s:url>
	<s:url id="getRoles_url" value="/pl/BINOLPLRLA05_getRoles"></s:url>
	<s:url id="getRolesByEmployee_url" value="/pl/BINOLPLRLA05_getRolesByEmployee"></s:url>
	<s:url id="getEmployeesByRole_url" value="/pl/BINOLPLRLA05_getEmployeesByRole"></s:url>
	<s:url id="getMenusByEmployee_url" value="/pl/BINOLPLRLA05_getMenusByEmployee"></s:url>
	
</div>
<div class="tabs panel-content">
	<cherry:form id="mainForm" class="inline">
	</cherry:form>
	<ul>
        <li><a href="#tabs-1" onclick="return false;"><s:text name="RLA05_FindRoles"/></a></li><%--根据员工查角色 --%>
        <li><a href="#tabs-2" onclick="return false;"><s:text name="RLA05_FindEmplyees"/></a></li><%--根据角色查员工 --%>
     </ul>
     <%--根据员工查角色 --%>
	<div id="tabs-1">
		<div>
			<form id="mainForm_FindRoles">
				<div class="box">
			     	<div class="box-header">
			           	<strong style="font-size:13px">
			           		<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
			           	</strong>
			        </div>
			        <div class="box-content clearfix">
			        	<div class="column" style="width:49%;">
			        		<%--员工CODE --%>
			        		<p>
			        			<label style="font-size:13px"><s:text name="RLA05_EmployeeCode"/></label>
				            	<s:textfield name="employeeCode" cssClass="text" maxlength="50" id="employeeCode"/>
				            </p>
				            <%--员工姓名 --%>
				            <p>
			        			<label style="font-size:13px"><s:text name="RLA05_EmployeeName"/></label>
				            	<s:textfield name="employeeName" cssClass="text" maxlength="50" id="employeeName"/>
				            </p>
				            <%--登录帐号 --%>
			        		<p>
			        			<label style="font-size:13px"><s:text name="RLA05_UserName"/></label>
				            	<s:textfield name="userName" cssClass="text" maxlength="50" id="userName"/>
			        		</p>
			        	</div>
			        	<div class="column last" style="width:50%;">
			        		<%--所属品牌 --%>
			        		<p>
			        			<s:if test="%{brandInfoList.size()> 1}">
			                    	<label style="font-size:13px"><s:text name="RLA05_BrandName"></s:text></label>
			                    	<s:select id="brandInfoId1" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{RLA05_Select}" cssStyle="width:100px;"></s:select>
			                	</s:if><s:else>
			                		<label style="font-size:13px"><s:text name="RLA05_BrandName"></s:text></label>
			                    	<s:select id="brandInfoId1" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
			                	</s:else>
			        		</p>
			        		<%--岗位类别 --%>
			        		<p>
			        			<label style="font-size:13px"><s:text name="RLA05_PostName"/></label>
				            	<s:select name="postId" list="positionCategoryList" listKey="positionCategoryId" listValue="categoryName" headerKey="" headerValue="%{RLA05_Select}" cssStyle="width:150px;"/>
			        		</p>
			        	</div>
			        </div>
			        <p class="clearfix">
	           			<%-- 查询 --%>
	           			<button class="right search" type="submit" onclick="binOLPLRLA05.getEmployees(); return false;" id="searchBut">
	           				<span class="ui-icon icon-search-big"></span>
	           				<span class="button-text" style="font-size:15px"><s:text name="global.page.search"/></span>
	           			</button>
	          		</p>
			    </div>
			</form>
			<%-- ====================== 结果一览开始 ====================== --%>
			<div id="section" class="section hide">
				<div class="section-header">
					<strong> 
					<span class="ui-icon icon-ttl-section-search-result"></span> 
					<s:text name="global.page.list" />
				 	</strong>
				</div>
				<div class="section-content">
					<div class="toolbar clearfix">
					</div>
					<table id="dataTable_FindRoles" cellpadding="0" cellspacing="0" border="0"
					class="jquery_table" width="100%">
					<thead>
						<tr>
							<%-- No. --%>
							<th class="center"><s:text name="No." /></th>
							<%-- （员工CODE）员工姓名 --%>
							<th><s:text name="RLA05_Employee" /></th>
							<%-- 登录帐号 --%>
							<th><s:text name="RLA05_UserName" /></th>
							<%-- 所在部门   --%>
							<th><s:text name="RLA05_DepartName" /></th>
							<%-- 所在岗位   --%>
							<th><s:text name="RLA05_PostName" /></th>
							<%-- 所属品牌   --%>
							<th><s:text name="RLA05_BrandName" /></th>
							<%-- 查看角色   --%>
							<th><s:text name="RLA05_Roles" /></th>
						</tr>
					</thead>
				</table>
			</div>
			</div>
			<%-- ====================== 结果一览结束 ====================== --%>
		</div>
	</div>
	<%--根据角色查员工 --%>
	<div id="tabs-2">
		<div>
			<form id="mainForm_FindEmplyees">
				<div class="box">
			     	<div class="box-header">
			           	<strong style="font-size:13px">
			           		<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
			           	</strong>
			        </div>
			        <div class="box-content clearfix">
			        	<div class="column" style="width:49%;">
			        		<%--所属品牌 --%>
			        		<p>
			        			<s:if test="%{brandInfoList.size()> 1}">
			                    	<label style="font-size:13px"><s:text name="RLA05_BrandName"></s:text></label>
			                    	<s:select id="brandInfoId1" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{RLA05_Select}" cssStyle="width:100px;"></s:select>
			                	</s:if><s:else>
			                		<label style="font-size:13px"><s:text name="RLA05_BrandName"></s:text></label>
			                    	<s:select id="brandInfoId1" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
			                	</s:else>
			        		</p>
			        		<%--角色名称--%>
				            <p>
			        			<label style="font-size:13px"><s:text name="RLA05_RoleName"/></label>
				            	<s:textfield name="roleName" cssClass="text" maxlength="50" id="roleName"/>
				            </p>
			        	</div>
			        	<div class="column last" style="width:50%;">
			        		<%--角色类别 --%>
			        		<p>
			        			<label style="font-size:13px"><s:text name="RLA05_RoleKind"/></label>
				            	<s:select name="roleKind" id="roleKind" list="#application.CodeTable.getCodes('1009')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RLA05_Select}" cssStyle="width:150px;"></s:select>
			        		</p>
			        	</div>
			        </div>
			        <p class="clearfix">
	           			<%-- 查询 --%>
	           			<button class="right search" type="submit" onclick="binOLPLRLA05.getRoles(); return false;" id="searchBut">
	           				<span class="ui-icon icon-search-big"></span>
	           				<span class="button-text" style="font-size:15px"><s:text name="global.page.search"/></span>
	           			</button>
	          		</p> 
			    </div>
			</form>
			<%-- ====================== 结果一览开始 ====================== --%>
			<div id="section" class="section hide">
				<div class="section-header">
					<strong> 
					<span class="ui-icon icon-ttl-section-search-result"></span> 
					<s:text name="global.page.list" />
				 	</strong>
				</div>
				<div class="section-content">
					<div class="toolbar clearfix">
					</div>
					<table id="dataTable_FindEmployee" cellpadding="0" cellspacing="0" border="0"
					class="jquery_table" width="100%">
					<thead>
						<tr>
							<%-- No. --%>
							<th><s:text name="No." /></th>
							<%-- 角色名称 --%>
							<th><s:text name="RLA05_RoleName" /></th>
							<%-- 角色类型 --%>
							<th><s:text name="RLA05_RoleKind" /></th>
							<%-- 所属品牌   --%>
							<th><s:text name="RLA05_BrandName" /></th>
							<%-- 查看员工   --%>
							<th><s:text name="RLA05_Employees" /></th>
						</tr>
					</thead>
				</table>
			</div>
			</div>
			<%-- ====================== 结果一览结束 ====================== --%>
		</div>
	</div>
</div>
<div style="display: none">
	<div id="dialogClose"><s:text name="global.page.close" /></div>
	<div id="RolesTitle"><s:text name="RLA05_RolesTitle" /></div>
	<div id="EmployeesTitle"><s:text name="RLA05_EmployeesTitle" /></div>
	<div id="ResourceTitle"><s:text name="RLA05_ResourceTitle" /></div>
	
	<input id="getEmployees" value="${getEmployees_url}"></input>
	<input id="getRoles" value="${getRoles_url}"></input>
	<input id="getRolesByEmployee" value="${getRolesByEmployee_url}"></input>
	<input id="getEmployeesByRole" value="${getEmployeesByRole_url}"></input>
	<input id="getMenusByEmployee" value="${getMenusByEmployee_url}"></input>
	
</div>
<div id="popRoleTable">
</div>
<div id="popEmployeeTable" class="hide">
	<s:hidden id="roleId" value=""></s:hidden>
	<s:hidden id="roleKind" value=""></s:hidden>
    <input type="text" class="text" onKeyup ="datatableFilter(this,12);" maxlength="50" id="departKw"/>
    <a class="search" onclick="datatableFilter('#departKw',12);return false;"><span class="ui-icon icon-search"></span><span class="button-text">查找</span></a>
  	<hr class="space" />
    <table id="popEmployeeDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
                <%-- No. --%>
				<th class="center"><s:text name="No." /></th>
				<%-- （员工CODE）员工姓名 --%>
				<th><s:text name="RLA05_Employee" /></th>
				<%-- 所在部门   --%>
				<th><s:text name="RLA05_DepartName" /></th>
				<%-- 所在岗位   --%>
				<th><s:text name="RLA05_PostName" /></th>
            </tr>
        </thead>
        <tbody></tbody>
   </table>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>