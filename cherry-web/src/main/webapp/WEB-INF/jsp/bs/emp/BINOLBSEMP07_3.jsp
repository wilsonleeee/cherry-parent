<!-- BA模式一览LIST -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSEMP07">
<div id="aaData">
		<s:iterator value="baModelCouponList" id="baModelCouponMap">
			<ul>
				<%-- NO. --%>
				<li>
					<input type="checkbox" id="checkBa" value='<s:property value="resellerInfoId" />' onclick="BINOLBSEMP07.checkBaRecord(this,'#dataTableBaModel_Cloned');"/>
					<s:hidden name="resellerInfoIdGrp" value="%{#baModelCouponMap.resellerInfoId}"></s:hidden>
				</li>
				<%-- 一级代理商名称 --%>
				<li><s:property value="parentResellerName"/></li>
				<%-- 二级代理商名称 --%>
				<li><s:property value="resellerName"/></li>
				<%-- 批次数量 --%>
				<li><span><s:property value="batchCount"/></span></li>
				<%-- 优惠券数 --%>
				<li><span><s:property value="couponCount"/></span></li>
			</ul>
		</s:iterator>
</div>
</s:i18n>
