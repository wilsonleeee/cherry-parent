<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSFAC01">
<div id="aaData">
	<s:iterator value="factoryList" id="fac">
		<s:url id="detailsUrl" value="/basis/BINOLBSFAC02_init">
			<s:param name="manufacturerInfoId">${fac.manufacturerInfoId}</s:param>
		</s:url>
		<ul>
			<%-- NO. --%>
			<li>${fac.RowNumber}</li>
			<%--  厂商编码  --%>
			<li>
				<a href="${detailsUrl }" class="popup" onclick="javascript:openWin(this);return false;">
					<s:property value="manufacturerCode"/>
				</a>
			</li>
			<%-- 厂商名称 --%>
			<li><span><s:property value="factoryName"/></span></li>
			<%-- 厂商简称 --%>
			<li><span><s:property value="factoryNameShort"/></span></li>
			<%-- 法人代表 --%>
			<li><span><s:property value="legalPerson"/></span></li>
			<%-- 所属省份 --%>
			<li><span><s:property value="provinceName"/></span></li>
			<%-- 所属城市 --%>
			<li><span><s:property value="cityName"/></span></li>
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