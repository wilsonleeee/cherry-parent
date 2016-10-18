<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="popProductInfoList" id="productMap0">
		<ul>
			<li>
				<input name="productInfo" class="checkbox" value="<s:property value="#productMap0.productInfo"/>"
				type="<s:if test='%{checkType == null || checkType == "checkbox"}'>checkbox</s:if><s:else>radio</s:else>"/>
			</li>
			<li><s:property value="#productMap0.unitCode"/></li>
			<li><s:property value="#productMap0.barCode"/></li>
			<li><s:property value="#application.CodeTable.getVal('1299',#productMap0.originalBrand)"/>   </li>
			<li><s:property value="#productMap0.nameTotal"/></li>
			<li><s:property value="#productMap0.primaryCategoryBig"/></li>
			<li><s:property value="#productMap0.primaryCategorySmall"/></li>
			<li><s:text name="format.price"><s:param value="#productMap0.salePrice"></s:param></s:text></li>
			<li><s:text name="format.price"><s:param value="#productMap0.memPrice"></s:param></s:text></li>
			<li><s:text name="format.price"><s:param value="#productMap0.standardCost"></s:param></s:text></li>
			<li><s:text name="format.price"><s:param value="#productMap0.orderPrice"></s:param></s:text></li>
			<li><s:text name="format.price"><s:param value="#productMap0.platinumPrice"></s:param></s:text></li>
			<li><s:text name="format.price"><s:param value="#productMap0.tagPrice"></s:param></s:text></li>
			<li>
				<s:if test='"1".equals(#productMap0.validFlag)'><span class='ui-icon icon-valid'></span></s:if><%-- 有效区分 --%>
				<s:else><span class='ui-icon icon-invalid'></span></s:else>
			</li>
		</ul>
	</s:iterator>
</div>