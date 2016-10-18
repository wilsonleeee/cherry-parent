<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTJCS12">
<div id="aaData">
	<s:iterator value="productRelationList" id="productRelation">
		<ul>
            <%-- No. --%>
			<li><s:property value="RowNumber"/></li>
			<%-- 电商商品标题 --%>
			<li><span><s:property value="esProductTitleName"/></span></li>
			<%-- SKU编码 --%>
			<li><span><s:property value="skuCode"/></span></li>
			<%-- 宝贝编码 --%>
			<li><span><s:property value="outCode"/></span></li>
			<%-- 厂商编码 --%>
			<li><span><s:property value="unitCode"/></span></li>
			<%-- 产品条码 --%>
			<li><span><s:property value="barCode"/></span></li>
			<%-- 产品名称 --%>
			<li><span><s:property value="nameTotal"/></span></li>
			<%-- 业务日期 --%>
			<li><span><s:property value="getDate"/></span></li>
			<%-- 产品对应关系是否改变 --%>
			<li>
				<s:if test="isRelationChange == 0">
					<span><font color="red"><s:text name="STJCS12_isRelationChangeException" /></font></span>
				</s:if> 
				<s:elseif test="isRelationChange == 1">
					<span><font color="green"><s:text name="STJCS12_isRelationChangeNormal" /></font></span>
				</s:elseif>
			</li>
			</ul>
	</s:iterator>
</div>
</s:i18n>
