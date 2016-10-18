<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mb.BINOLMBRPT04">
<div id="headInfo">
	<s:if test='sumInfo != null && sumInfo.size() > 0'>
	<s:text name="RPT04_sumTotalMemberNum"/>
	<span class="green">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumTotalMemberNum"></s:param></s:text></strong>
	</span>
	<s:text name="RPT04_sumTotalMemberSaleAmount"/>
	<span class="<s:if test='sumInfo.sumTotalMemberSaleAmount < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.price"><s:param value="sumInfo.sumTotalMemberSaleAmount"></s:param></s:text></strong>
	</span>
	<br/>
	<s:text name="RPT04_sumNewMemberNum"/>
	<span class="green">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumNewMemberNum"></s:param></s:text></strong>
	</span>
	<s:text name="RPT04_sumNewMemSaleAmount"/>
	<span class="<s:if test='sumInfo.sumNewMemberSaleAmount < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.price"><s:param value="sumInfo.sumNewMemberSaleAmount"></s:param></s:text></strong>
	</span>
	<s:text name="RPT04_sumNewMemberProportion"/>
	<span class="<s:if test='sumInfo.sumNewMemberProportion < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.price"><s:param value="sumInfo.sumNewMemberProportion"></s:param></s:text>%</strong>
	</span>
	<s:text name="RPT04_sumNewMemConsumeAverage"/>
	<span class="<s:if test='sumInfo.sumNewMemConsumeAverage < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.price"><s:param value="sumInfo.sumNewMemConsumeAverage"></s:param></s:text></strong>
	</span>
	<br/>
	<s:text name="RPT04_sumBuyBackMemberNum"/>
	<span class="green">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumBuyBackMemberNum"></s:param></s:text></strong>
	</span>
	<s:text name="RPT04_sumBuyBackMemSaleAmount"/>
	<span class="<s:if test='sumInfo.sumBuyBackMemSaleAmount < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.price"><s:param value="sumInfo.sumBuyBackMemSaleAmount"></s:param></s:text></strong>
	</span>
	</s:if>
</div>>
<div id="aaData">
	<s:iterator value="memberDevelopRptList" id="memberDevelopRptMap">
		<ul>
			<li><span><s:property value="RowNumber"/></span></li>
			<li><span><s:property value="counter"/></span></li>
			<li><span><s:property value="busniessPrincipal"/></span></li>
			<li><span><s:text name="format.number"><s:param value="totalMemberNum"/></s:text></span></li>
			<li><span><s:text name="format.price"><s:param value="totalMemberSaleAmount"/></s:text></span></li>
			<li><span><s:text name="format.number"><s:param value="newMemberNum"/></s:text></span></li>
			<li><span><s:text name="format.price"><s:param value="newMemberSaleAmount"/></s:text></span></li>
			<li><span><s:text name="format.number"><s:param value="buyBackMemberNum"/></s:text></span></li>
			<li><span><s:text name="format.price"><s:param value="buyBackMemSaleAmount"/></s:text></span></li>
			<li><span><s:text name="format.price"><s:param value="newMemberProportion"/></s:text>%</span></li>
			<li><span><s:text name="format.price"><s:param value="newMemConsumeAverage"/></s:text></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>