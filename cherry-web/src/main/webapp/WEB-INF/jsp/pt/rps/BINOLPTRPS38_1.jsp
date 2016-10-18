<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS38">
<div id="aaData">
	<s:iterator value="proStockList">
		<s:if test='"2".equals(type)'>
		<s:url id="detailsUrl" action="BINOLPTRPS38_getDetail">
			<s:param name="productId"><s:property value="productId"/></s:param>
			<s:param name="validFlag"><s:property value="validFlag"/></s:param>
		</s:url>
		</s:if>
		<s:else>
		<s:url id="detailsUrl" action="BINOLPTRPS38_getDetail">
			<s:param name="prtVendorId"><s:property value="prtVendorId"/></s:param>
		</s:url>
		</s:else>
		<ul>
			<%-- No. --%>
			<li><s:property value="RowNumber"/></li>
			<%-- 产品名称 --%>
			<li><a href="${detailsUrl}" class="popup" onclick="getDetail(this);return false;"><s:property value="nameTotal"/></a></li>
			<%-- 厂商编码  --%>
			<li><span><s:property value="unitCode"/></span></li>
			<%-- 产品条码   --%>
			<li><span><s:property value="barCode"/></span></li>
			<%-- 品牌  --%>
<%-- 			<li><span><s:property value='#application.CodeTable.getVal("1299", originalBrand)'/></span></li> --%>
			<%-- 计量单位   --%>
			<li><span><s:property value='#application.CodeTable.getVal("1190", moduleCode)'/></span></li>
            <%-- 价格 --%>
            <li>
                <s:text name="format.price"><s:param value="price"></s:param></s:text>
            </li>
            <%-- 库存 --%>
            <li>
            <s:if test="quantity >= 0">
            	<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
            </s:if>
			<s:else>
				<span class="highlight"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span>
			</s:else>
            </li>
		</ul>
	</s:iterator>
</div>
</s:i18n>