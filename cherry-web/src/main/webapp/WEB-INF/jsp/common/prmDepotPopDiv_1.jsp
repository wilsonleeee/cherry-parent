<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="popPrmProductInfoList" id="promotion">
		<ul>
			<li><input name="productInfo" type="checkbox"  class="checkbox" value="<s:property value="#promotion.unitCode"/>_<s:property value="#promotion.barCode"/>_<s:property value="#promotion.nameTotal"/>_<s:property value="#promotion.BIN_PromotionProductVendorID"/>_<s:property value="#promotion.salePrice"/>" onchange="changeChecked(this)"/></li>
			<li><s:property value="#promotion.unitCode"/></li>
			<li><s:property value="#promotion.barCode"/></li>
			<li><s:property value="#promotion.nameTotal"/></li>
			<li><s:property value="#promotion.salePrice"/></li>
		</ul>
	</s:iterator>
</div>