<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS31">
<div id="headInfo" style="margin-left:10px;">
	<s:text name="RPS31_sumMemberBuyOrderCount"/>
	<span class="<s:if test='sumInfo.sumMemberBuyOrderCount < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumMemberBuyOrderCount"></s:param></s:text></strong>
	</span>
	<s:text name="RPS31_sumMemberBuyAmount"/>
	<span class="<s:if test='sumInfo.sumMemberBuyAmount < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.price"><s:param value="sumInfo.sumMemberBuyAmount"></s:param></s:text></strong>
	</span>
	<s:text name="RPS31_sumBaSaleCount"/>
	<span class="<s:if test='sumInfo.sumBaSaleCount < 0'>highlight</s:if><s:else>green</s:else>">
		<strong><s:text name="format.number"><s:param value="sumInfo.sumBaSaleCount"></s:param></s:text></strong>
	</span>
	<s:text name="RPS31_sumBaSaleAmount"/>
	<span class="<s:if test='sumInfo.sumBaSaleAmount < 0'>highlight</s:if><s:else>green</s:else>">
    	<strong><s:text name="format.price"><s:param value="sumInfo.sumBaSaleAmount"></s:param></s:text></strong>
    </span>
    <br/>
    <hr class="space" />
</div>
<div id="aaData">
	<s:iterator value="baCommissionList" id="baCommissionMap">
		<ul>
			<li><span><s:property value="RowNumber"/></span></li>
			<li><span><s:property value="parentResellerName"/></span></li>
			<li><span><s:property value="resellerName"/></span></li>
			<li><span><s:text name="format.number"><s:param value="memberBuyOrderCount"/></s:text></span></li>
			<li>
			<s:if test='memberBuyAmount != 0'>
				<s:url action="BINOLPTRPS31_memberBuyInit" id="memberBuyInitUrl">
					<s:param name="baInfoId" value="%{#baCommissionMap.baInfoIdForMember }" />
					<s:param name="brandInfoId" value="%{#baCommissionMap.brandInfoId }" />
					<s:param name="startDate" value="%{#baCommissionMap.startDate }" />
					<s:param name="endDate" value="%{#baCommissionMap.endDate }" />
				</s:url>
				<a href="${memberBuyInitUrl}" class="popup" onclick="javascript:openWin(this);return false;">
					<span><s:text name="format.price"><s:param value="memberBuyAmount"/></s:text></span>
				</a>
			</s:if><s:else>
				<span><s:text name="format.price"><s:param value="memberBuyAmount"/></s:text></span>
			</s:else>
			</li>
			<li><span><s:text name="format.number"><s:param value="baSaleCount"/></s:text></span></li>
			<li>
				<s:if test='baSaleAmount != 0'>
					<s:url action="BINOLPTRPS31_baSaleInit" id="baSaleInitUrl">
						<s:param name="baInfoId" value="%{#baCommissionMap.baInfoIdForSale }" />
						<s:param name="brandInfoId" value="%{#baCommissionMap.brandInfoId }" />
						<s:param name="startDate" value="%{#baCommissionMap.startDate }" />
						<s:param name="endDate" value="%{#baCommissionMap.endDate }" />
					</s:url>
					<a href="${baSaleInitUrl}" class="popup" onclick="javascript:openWin(this);return false;">
						<span><s:text name="format.price"><s:param value="baSaleAmount"/></s:text></span>
					</a>
				</s:if><s:else>
					<span><s:text name="format.price"><s:param value="baSaleAmount"/></s:text></span>
				</s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>