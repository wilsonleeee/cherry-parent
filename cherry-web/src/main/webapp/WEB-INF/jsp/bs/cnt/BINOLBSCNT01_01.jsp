<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSCNT01">
<div id="aaData">
	<s:iterator value="counterList" id="counterMap">
		<ul>
			<li>
				<s:checkbox name="validFlag" fieldValue="%{#counterMap.validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
				<s:hidden name="counterInfoId" value="%{#counterMap.counterInfoId}"></s:hidden>
				<s:hidden name="organizationId" value="%{#counterMap.organizationId}"></s:hidden>
				<s:hidden name="counterCode" value="%{#counterMap.counterCode}"></s:hidden>
			</li>
			<%-- 柜台号 --%>
			<li>
				<s:url action="BINOLBSCNT02_init" id="counterDetailUrl">
					<s:param name="counterInfoId" value="%{#counterMap.counterInfoId}"></s:param>
				</s:url>
				<a href="${counterDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;">
					<s:property value="counterCode"/>
				</a>
			</li>
			<%-- 柜台中文名称 --%>
			<li><span><s:property value="counterNameIF"/></span></li>
			<%-- 柜台主管 --%>
			<li><span>
			  <s:if test='%{#counterMap.employeeCode != null && !"".equals(#counterMap.employeeCode)}'>
				(<s:property value="employeeCode"/>)<s:property value="employeeName"/>
			  </s:if>
			</span></li>
			<%-- 所属品牌 --%>
			<li><span><s:property value="brandNameChinese"/></span></li>
			<%-- 所属大区 --%>
			<li><span><s:property value="region"/></span></li>
			<%-- 所属省份 --%>
			<li><span><s:property value="province"/></span></li>
			<%-- 所属城市 --%>
			<li><span><s:property value="city"/></span></li>
			<%-- 运营模式 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1318", #counterMap.operateMode)' /></span></li>
			<%-- 所属系统 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1309", #counterMap.belongFaction)' /></span></li>
			<%-- 开票单位 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1319", #counterMap.invoiceCompany)' /></span></li>
			<%-- 渠道 --%>
			<li><span><s:property value="channelName"/></span></li>
			<%-- 经销商 --%>
			<li><span><s:property value="resellerName"/></span></li>
			<%-- 经销商部门 --%>
			<li><span><s:property value="resellerDepartName"/></span></li>
			<%-- 柜台状态 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1030", #counterMap.status)' /></span></li>
			<%-- 是否有POS机 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1308", #counterMap.posFlag)' /></span></li>
			<%-- 柜台员工数 --%>
			<li><span><s:property value="employeeNum"/></span></li>
			<%-- 柜台地址 --%>
			<li><span><s:property value="counterAddress"/></span></li>
			<%-- 柜台协同区分 --%>
			<s:if test="maintainCoutSynergy">
				<li><span>
				 	<s:property value='#application.CodeTable.getVal("1331", #counterMap.counterSynergyFlag)' />
				</span></li>
			</s:if>
			<%-- 业务负责人 --%>
			<li><span><s:property value="busniessPrincipal"/></span></li>
			<%-- 银联设备号 --%>
			<li><span><s:property value="equipmentCode"/></span></li>
			<%-- 柜台类型 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1403", #counterMap.managingType2)' /></span></li>
			<%-- 有效区分 --%>
			<li>
				<s:if test="validFlag ==1"><span class='ui-icon icon-valid'></span></s:if>
				<s:elseif test="validFlag ==0"><span class='ui-icon icon-invalid'></span></s:elseif>
				<s:else><span></span></s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
