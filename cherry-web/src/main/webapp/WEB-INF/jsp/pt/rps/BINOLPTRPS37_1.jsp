<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="proStockList" >
		<ul>
			<%-- No. --%>
			<li><s:property value="RowNumber"/></li>
			<!-- 所属部门 -->
			<li><span>
				<s:if test='!"".equals(departCode)'>
					(<s:property value="departCode"/>)<s:property value="departName"/>
				</s:if><s:else>
					<s:property value="departName"/>
				</s:else>
			</span></li>
			<!-- 实体仓库 -->
			<li><span>
				<s:if test='!"".equals(depotCode)'>
					(<s:property value="depotCode"/>)<s:property value="depotName"/>
				</s:if><s:else>
					<s:property value="depotName"/>
				</s:else>
			</span></li>
			<!-- 逻辑仓库名称 -->
			<li><span>
				<s:if test='!"".equals(logicDepotCode)'>
					(<s:property value="logicDepotCode"/>)<s:property value="logicDepotName"/>
				</s:if><s:else>
					<s:property value="logicDepotName"/>
				</s:else>
			</span></li>
			<%-- 产品名称 --%>
			<li><s:property value="productName"/></li>
			<%-- 厂商编码  --%>
			<li><span><s:property value="unitCode"/></span></li>
			<%-- 产品条码   --%>
			<li><span><s:property value="barCode"/></span></li>
			<%-- 计量单位   --%>
			<li><span><s:property value='#application.CodeTable.getVal("1190", moduleCode)'/></span></li>
			<%-- 当前库存 --%>
			<li><s:text name="format.number"><s:param value="stockQuantity"></s:param></s:text></li>
		</ul>
	</s:iterator>
</div>