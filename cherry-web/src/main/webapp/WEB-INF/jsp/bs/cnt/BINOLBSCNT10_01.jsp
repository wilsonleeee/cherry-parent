<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSCNT10">
<div id="aaData">

	<s:iterator value="counterPointPlanDetailList" id="counterDetailMap">
		<ul>
			<%-- 最后变更时间 --%>
			<li><span><s:property value="UpdateTime"/></span></li>
			<!-- 变更情况 -->
			<li>
				<span>
					<s:if test=' ModifiedType == 0 '>
						<s:text name="binolbscnt10_using"/>-&gt;<s:text name="binolbscnt10_unused"/>
					</s:if>
					<s:else>
						<s:text name="binolbscnt10_unused"/>-&gt;<s:text name="binolbscnt10_using"/>
					</s:else>
				</span>
			</li>
			<!-- 说明 -->
			<li>
				<span>
					<s:property value="EmployeeName"/>
				</span>
			</li>

			<%-- 开启日期 --%>
			<li><span><s:property value="StartTime"/></span></li>
			<%-- 结束日期 --%>
			<li>
				<span>
					<s:if test=' EndTime == null || "2100-01-01".equals(EndTime) '>
						<s:text name="binolbscnt10_notHave"/>
					</s:if>
					<s:else>
						<s:property value="EndTime"/>
					</s:else>
				</span>
			</li>
			<%-- 备注 --%>
			<li><span><s:property value="Commemts" /></span></li>

		</ul>
	</s:iterator>
</div>
</s:i18n>
