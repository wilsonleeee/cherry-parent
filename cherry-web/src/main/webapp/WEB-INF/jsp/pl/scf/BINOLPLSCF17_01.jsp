<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pl.BINOLPLSCF17">
<div id="aaData">
	<s:iterator value="handlerList" id="handlerMap">
		<ul>
			<li>
				<s:checkbox name="validFlag" fieldValue="%{#handlerMap.validFlag}" onclick="BINOLPLSCF17.checkHandler(this, '#dataTableBody');"></s:checkbox>
				<input type="hidden" name="handlerId" value="<s:property value='handlerId'/>" />
				<input type="hidden" name="brandCd" value="<s:property value='brandCd'/>" />
			</li>
			<%-- 处理器名称 --%>
			<li><span><s:property value="handNameCN"/></span></li>
			<%-- 描述 --%>
			<li><span><s:property value="descriptionDtl"/></span></li>
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
