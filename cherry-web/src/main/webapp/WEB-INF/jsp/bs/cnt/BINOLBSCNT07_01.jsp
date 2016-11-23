<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSCNT07">
<div id="aaData">
	<s:iterator value="counterPointPlanList" id="counterMap">
		<ul>
			<li>
				<s:checkbox name="validFlag" fieldValue="%{#counterMap.validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
				<s:hidden name="counterInfoId" value="%{#counterMap.BIN_CounterInfoID}"></s:hidden>
				<s:hidden name="counterCode" value="%{#counterMap.counterCode}"></s:hidden>
			</li>
			<%-- 柜台号 --%>
			<li>
				<a href="" class="popup" onclick="javascript:openWin(this);return false;">
					<s:property value="CounterCode"/>
				</a>
			</li>
			<%-- 柜台名称 --%>
			<li><span><s:property value="counterName"/></span></li>

			<li><span></span></li>
			<li><span></span></li>

			<%-- 开启日期 --%>
			<li><span><s:property value="StartDate"/></span></li>
			<%-- 结束日期 --%>
			<li><span><s:property value="EndDate"/></span></li>
			<%-- 经销商额度 --%>
			<li><span><s:property value="CurrentPointLimit"/></span></li>
			<%-- 修改者 --%>
			<li><span><s:property value="employeeName"/></span></li>
			<%-- 备注 --%>
			<li><span><s:property value="Comment" /></span></li>

		</ul>
	</s:iterator>
</div>
</s:i18n>
