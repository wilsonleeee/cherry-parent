<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="popProductInfoOneList" id="productMap">
		<ul>
			<li>
				<input name="productInfo" class="checkbox" value="<s:property value="#productMap.productInfo"/>"
				type="<s:if test='%{checkType == null || checkType == "checkbox"}'>checkbox</s:if><s:else>radio</s:else>"/>
			</li>
			<li><s:property value="#productMap.barCode"/></li>
			<li><s:property value="#application.CodeTable.getVal('1299',#productMap.originalBrand)"/>   </li>
			<li><s:property value="#productMap.nameTotal"/></li>
			<li><s:property value="#productMap.primaryCategoryBig"/></li>
			<li><s:property value="#productMap.primaryCategorySmall"/></li>
			<li><s:text name="format.price"><s:param value="#productMap.salePrice"></s:param></s:text></li>
			<li><s:text name="format.price"><s:param value="#productMap.memPrice"></s:param></s:text></li>
			<li><s:text name="format.price"><s:param value="#productMap.standardCost"></s:param></s:text></li>
			<li><s:text name="format.price"><s:param value="#productMap.orderPrice"></s:param></s:text></li>
			<li><s:text name="format.price"><s:param value="#productMap.platinumPrice"></s:param></s:text></li>
			<li><s:text name="format.price"><s:param value="#productMap.tagPrice"></s:param></s:text></li>
			<li>
				<s:if test='"1".equals(#productMap.validFlag)'><span class='ui-icon icon-valid'></span></s:if><%-- 有效区分 --%>
				<s:else><span class='ui-icon icon-invalid'></span></s:else>
			</li>
		</ul>
	</s:iterator>
</div>