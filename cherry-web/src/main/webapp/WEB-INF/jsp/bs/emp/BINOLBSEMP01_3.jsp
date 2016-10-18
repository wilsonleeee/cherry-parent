<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSEMP01">
<div id="aaData">
	<s:if test="maintainBa">
		<s:iterator value="employeeList" id="emp">
			<s:set name="departName" value=""/>
			<s:set name="positionName" value=""/>
			<s:url id="detailsUrl" value="/basis/BINOLBSEMP02_init">
				<s:param name="employeeId"><s:property value="#emp.employeeId"/></s:param>
			</s:url>
			<ul>
				<%-- NO. --%>
				<li>
					<s:checkbox name="validFlag" fieldValue="%{#emp.validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
					<s:hidden name="userId" value="%{#emp.userId}"></s:hidden>
					<s:hidden name="longinName" value="%{#emp.longinName}"></s:hidden>
					<s:hidden name="employeeId" value="%{#emp.employeeId}"></s:hidden>
				</li>
				<%-- 员工代号 --%>
				<li>
					<a href="${detailsUrl }" class="popup" onclick="javascript:openWin(this);return false;">
						<s:property value="employeeCode"/>
					</a>
				</li>
				<%-- 登录帐号 --%>
				<li><s:property value="longinName"/></li>
				<%-- 员工姓名 --%>
				<li><span><s:property value="employeeName"/></span></li>
				<%-- 所属省份 --%>
				<li><span><s:property value="provinceName"/></span></li>
				<%-- 所属城市 --%>
				<li><span><s:property value="cityName"/></span></li>
				<%-- 所属岗位 --%>
				<li><span><s:property value="categoryName"/></span></li>
				<%-- 所属品牌 --%>
				<li><span><s:property value="brandName"/></span></li>
				<%-- 所属部门 --%>
				<li><span><s:property value="departName"/></span></li>
				<%-- 手机 --%>
				<li><span><s:property value="mobilePhone"/></span></li>
				<%-- 邮箱 --%>
				<li><span><s:property value="email"/></span></li>
				<%-- 有效区分 --%>
				<li>
					<s:if test="validFlag ==1"><span class='ui-icon icon-valid'></span></s:if>
					<s:elseif test="validFlag ==0"><span class='ui-icon icon-invalid'></span></s:elseif>
					<s:else><span></span></s:else>
					<s:if test='departureDate != null && #request.sysDate.compareTo(departureDate)>=0'>
						（<s:text name="employee.departureStatus"></s:text>）
					</s:if>
				</li>
			</ul>
		</s:iterator>
	</s:if>
	<s:else>
		<s:iterator value="employeeList" id="emp">
			<s:set name="departName" value=""/>
			<s:set name="positionName" value=""/>
			<s:url id="detailsUrl" value="/basis/BINOLBSEMP02_init">
				<s:param name="employeeId"><s:property value="#emp.employeeId"/></s:param>
			</s:url>
			<ul>
				<%-- NO. --%>
				<li>
					<s:if test='%{#emp.categoryCode == null || !"01".equals(#emp.categoryCode)}'>
						<s:checkbox name="validFlag" fieldValue="%{#emp.validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
						<s:hidden name="userId" value="%{#emp.userId}"></s:hidden>
						<s:hidden name="longinName" value="%{#emp.longinName}"></s:hidden>
						<s:hidden name="employeeId" value="%{#emp.employeeId}"></s:hidden>
					</s:if>
				</li>
				<%-- 员工代号 --%>
				<li>
					<a href="${detailsUrl }" class="popup" onclick="javascript:openWin(this);return false;">
						<s:property value="employeeCode"/>
					</a>
				</li>
				<%-- 登录帐号 --%>
				<li><s:property value="longinName"/></li>
				<%-- 员工姓名 --%>
				<li><span><s:property value="employeeName"/></span></li>
				<%-- 所属省份 --%>
				<li><span><s:property value="provinceName"/></span></li>
				<%-- 所属城市 --%>
				<li><span><s:property value="cityName"/></span></li>
				<%-- 所属岗位 --%>
				<li><span><s:property value="categoryName"/></span></li>
				<%-- 所属品牌 --%>
				<li><span><s:property value="brandName"/></span></li>
				<%-- 所属部门 --%>
				<li><span><s:property value="departName"/></span></li>
				<%-- 手机 --%>
				<li><span><s:property value="mobilePhone"/></span></li>
				<%-- 邮箱 --%>
				<li><span><s:property value="email"/></span></li>
				<%-- 有效区分 --%>
				<li>
					<s:if test="validFlag ==1"><span class='ui-icon icon-valid'></span></s:if>
					<s:elseif test="validFlag ==0"><span class='ui-icon icon-invalid'></span></s:elseif>
					<s:else><span></span></s:else>
					<s:if test='departureDate != null && #request.sysDate.compareTo(departureDate)>=0'>
						（<s:text name="employee.departureStatus"></s:text>）
					</s:if>
				</li>
			</ul>
		</s:iterator>
	</s:else>
	
</div>
</s:i18n>
