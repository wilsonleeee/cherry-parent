<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mb.BINOLMBVIS02">
<div id="aaData">
	<s:iterator value="visitPlanList" id="visitPlanMap">
		<ul>
			<li><span><s:property value="RowNumber"/></span></li>
			<li><span><s:property value="visitTypeName"/></span></li>
			<li><span>
			<s:set value="%{#visitPlanMap.visitDes }" var="visitDes"></s:set>
			<a title='<s:text name="mbvis02_visitDes" />|<s:property value="visitDes"/>' class="description">
			  <cherry:cut length="20" value="${visitDes }"></cherry:cut>
			</a>
			</span></li>
			<li><span><s:property value="startDate"/></span></li>
			<li><span>
			  <s:if test="%{#visitPlanMap.endDate == null}"><s:text name="mbvis02_planDate1" /></s:if>
			  <s:else><s:property value="endDate"/></s:else>
			</span></li>
			<li><span><s:property value='#application.CodeTable.getVal("1321", #visitPlanMap.visitState)' /></span></li>
			<li><span><s:property value="planDateTime"/></span></li>
			<li><span><s:property value="employeeName"/></span></li>
			<li>
				<s:if test="validFlag ==1"><span class='ui-icon icon-valid'></span></s:if>
				<s:elseif test="validFlag ==0"><span class='ui-icon icon-invalid'></span></s:elseif>
			</li>
			<li><span>
				<s:hidden name="visitPlanId"></s:hidden>
				<s:hidden name="modifyTime"></s:hidden>
				<s:hidden name="modifyCount"></s:hidden>
				<s:url action="BINOLMBVIS02_detail" id="detailUrl">
				  <s:param name="visitPlanId" value="visitPlanId"></s:param>
				</s:url>
				<a class="edit" href="${detailUrl }" onclick="javascript:openWin(this);return false;">
					<span class="ui-icon-document ui-icon "></span>
               		<span class="button-text"><s:text name="global.page.detail" /></span>
				</a>
				<s:url action="BINOLMBVIS02_updateInit" id="updateInitUrl">
				  <s:param name="visitPlanId" value="visitPlanId"></s:param>
				</s:url>
				<a class="edit" href="${updateInitUrl }" onclick="javascript:openWin(this);return false;">
					<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="global.page.edit" /></span>
				</a>
				<s:if test="validFlag ==1">
				<a class="delete disable" href="javascript:void(0);">
					<span class="ui-icon icon-disable"></span><span class="button-text"><s:text name="global.page.disable" /></span>
				</a>
				</s:if>
				<s:elseif test="validFlag ==0">
				<a class="delete enable" href="javascript:void(0);">
					<span class="ui-icon icon-enable"></span><span class="button-text"><s:text name="global.page.enable" /></span>
				</a>
				</s:elseif>
			</span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
