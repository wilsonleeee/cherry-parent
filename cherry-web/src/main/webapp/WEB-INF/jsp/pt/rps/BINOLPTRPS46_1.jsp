<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTRPS46">
<div id="headInfo">
</div>
<div id="aaData">
	<s:iterator value="prtCheckoutInfoList">
		<ul>
			<%-- No. --%>
			<li><s:property value="RowNumber"/></li>
			<%-- 店铺号 --%>
			<li><s:property value="counterCode"/></li>
			<%-- 店铺名称  --%>
			<li><span><s:property value="counterName"/></span></li>
			<%-- 产品条码 --%>
			<li><s:property value="barCode"/></li>
			<%-- 产商编码 --%>
			<li><s:property value="unitCode"/></li>
			<%-- 产品名称  --%>
			<li><span><s:property value="nameTotal"/></span></li>
			<%-- 公司名称   --%>
			<li><span><s:property value='#application.CodeTable.getVal("1299", originalBrand)'/></span></li>
			<%-- 期初数量--%>
			<li>
				<s:if test="startQuantity >= 0"><s:text name="format.number"><s:param value="startQuantity"></s:param></s:text></s:if>
				<s:else><span class="highlight"><s:text name="format.number"><s:param value="startQuantity"></s:param></s:text></span></s:else>
			</li>
            <%-- 期初不含税金额 --%>
            <li>
                <s:if test="startAmountNet >= 0"><s:text name="format.price"><s:param value="startAmountNet"></s:param></s:text></s:if>
                <s:else><span class="highlight"><s:text name="format.price"><s:param value="startAmountNet"></s:param></s:text></span></s:else>
            </li>
			<%-- 本期收入 --%>
			<li><s:text name="format.number"><s:param value="inQuantity"></s:param></s:text></li>
			<%-- 本期收入不含税金额 --%>
			<li><s:text name="format.price"><s:param value="inAmountNet"></s:param></s:text></li>

			<%-- 本期发出  --%>
			<li><s:text name="format.number"><s:param value="outQuantity"></s:param></s:text></li>
			<%-- 本期发出不含税金额 --%>
			<li><s:text name="format.price"><s:param value="outAmountNet"></s:param></s:text></li>
			<%-- 期末结存 --%>
			<li>
				<s:if test="endQuantity >= 0"><s:text name="format.number"><s:param value="endQuantity"></s:param></s:text></s:if>
				<s:else><span class="highlight"><s:text name="format.number"><s:param value="endQuantity"></s:param></s:text></span></s:else>
			</li>
            <%-- 期末结存金额 --%>
            <li>
                <s:if test="endAmountNet >= 0"><s:text name="format.price"><s:param value="endAmountNet"></s:param></s:text></s:if>
                <s:else><span class="highlight"><s:text name="format.price"><s:param value="endAmountNet"></s:param></s:text></span></s:else>
            </li>
		</ul>
	</s:iterator>
</div>
</s:i18n>