<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="productInfoList" id="product">
		<ul>
			<li><input name="productInfo" type="checkbox"  class="checkbox" value='<s:property value="#product.unitCode"/>_<s:property value="#product.nameTotal"/>'/></li>
			<li><s:property value="#product.unitCode"/></li>
			<li><s:property value="#product.nameTotal"/></li>
			<li><s:property value="#product.salePrice"/></li>
		</ul>
	</s:iterator>
</div>