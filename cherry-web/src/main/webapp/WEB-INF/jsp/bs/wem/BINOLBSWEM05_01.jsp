<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.bs.BINOLBSWEM05">
<div id="aaData">
	<s:iterator value="bonusList" id="bonus">
		<ul>
			<li><s:property value="employeeCode" /></li>
			<li><s:property value="employeeName" /></li>
			<li><s:property value="saleTime" /></li>
			<li>
				<s:if test='null != levelType && "C".equals(levelType)'>
					<s:text name="bswem05.currentLevelType"/>
				</s:if>
				<s:else>
					<s:text name="bswem05.lowerLevelType"/>
				</s:else>
			</li>
			<li><s:property value="billCode"/></li>
			<li><s:property value="saleEmployeeCode"/></li>
			<li><s:property value="saleEmployeeName"/></li>
			<li>
				<s:if test="saleType=='NS'"><s:text name="bswem05.NS"></s:text></s:if>
				<s:elseif test="saleType=='SR'"><s:text name="bswem05.SR"></s:text></s:elseif>
				<s:elseif test="saleType=='PX'"><s:text name="bswem05.PX"></s:text></s:elseif>
			</li>
			<li>
				<s:if test="incomeAmount != null">
					<s:if test="incomeAmount >= 0"><s:text name="format.price"><s:param value="incomeAmount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="incomeAmount"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li>
				<s:if test="saleAmount != null">
					<s:if test="saleAmount >= 0"><s:text name="format.price"><s:param value="saleAmount"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.price"><s:param value="saleAmount"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<li>
				<s:if test="quantity != null">
					<s:if test="quantity >= 0"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></s:if>
					<s:else><span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
