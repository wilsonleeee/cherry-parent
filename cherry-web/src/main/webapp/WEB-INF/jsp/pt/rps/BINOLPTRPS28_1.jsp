<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="saleTargetRptList" id="saleTargetRptMap">
		<ul>
			<s:if test='%{countModel == "0"}'>
			<%-- 柜台号 --%>
			<li><span><s:property value="departCode"/></span></li>
			<%-- 柜台名称 --%>
			<li><span><s:property value="departName"/></span></li>
			<%-- 业务负责人 --%>
			<li><span><s:property value="busniessPrincipal"/></span></li>
			<%-- 省份 --%>
			<li><span><s:property value="provinceName"/></span></li>
			<%-- 城市 --%>
			<li><span><s:property value="cityName"/></span></li>
			<%-- 渠道 --%>
			<li><span><s:property value="channelName"/></span></li>
			</s:if>
			<s:if test='%{countModel == "1"}'>
			<%-- 大区名称 --%>
			<li><span><s:property value="departName"/></span></li>
			</s:if>
			<%-- 当月实绩 --%>
			<li><span>
			  <s:if test="%{#saleTargetRptMap.amount != null}">
			    <s:text name="format.price"><s:param value="amount"></s:param></s:text>
			  </s:if>
			</span></li>
			<%-- 目标 --%>
			<li><span>
			  <s:if test="%{#saleTargetRptMap.targetMoney != null}">
			    <s:text name="format.price"><s:param value="targetMoney"></s:param></s:text>
			  </s:if>
			  <s:else>-</s:else>
			</span></li>
			<%-- 当月进度 --%>
			<li><span>
			  <s:if test="%{#saleTargetRptMap.percent != null}">
			    <s:property value="percent"/>
			  </s:if>
			  <s:else>-</s:else>
			</span></li>
			<%-- 时间进行率 --%>
			<li><span>
			  <s:if test="%{#saleTargetRptMap.datePercent != null}">
			    <s:property value="datePercent"/>
			  </s:if>
			  <s:else>-</s:else>
			</span></li>
			<%-- 剩余目标 --%>
			<li><span>
			  <s:if test="%{#saleTargetRptMap.lastMoney != null}">
			    <s:text name="format.price"><s:param value="lastMoney"></s:param></s:text>
			  </s:if>
			  <s:else>-</s:else>
			</span></li>
			<%-- 预计完成目标 --%>
			<li><span>
			  <s:if test="%{#saleTargetRptMap.predict != null}">
			    <s:text name="format.price"><s:param value="predict"></s:param></s:text>
			  </s:if>
			  <s:else>-</s:else>
			</span></li>
		</ul>
	</s:iterator>
</div>