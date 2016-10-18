<!-- 代理商会员购买详细一览LIST -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="memberBuyInfoList" id="memberBuyInfoMap">
		<ul>
			<li><span><s:property value="RowNumber"/></span></li>
			<li><span><s:property value="billCode"/></span></li>
			<li>
				<span>
					<s:property value="baName"/>
				</span>
			</li>
			<li><span><s:property value="memberName"/></span></li>
			<li><span><s:property value="mobilePhone"/></span></li>
			<li><span><s:property value="memberBuyDate"/></span></li>
			<li><span><s:property value="unitCode"/></span></li>
			<li><span><s:property value="barCode"/></span></li>
			<li><span><s:property value="productName"/></span></li>
			<li><span><s:property value="memberBuyQuantity"/></span></li>
			<li><span><s:property value="memberBuyAmount"/></span></li>
		</ul>
	</s:iterator>
</div>