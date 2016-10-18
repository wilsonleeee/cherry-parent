<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="popProductInfoList" id="product">
		<ul>

			<s:if test='%{checkType != null && checkType == "radio"}'>
				<li><input name="productInfo" type="radio"  class="checkbox" value="<s:property value="#product.productInfo"/>"/></li>
			</s:if>
			<s:else>
				<li><input name="productInfo" type="checkbox"  class="checkbox" value="<s:property value="#product.productInfo"/>"/></li>
			</s:else>
			
			<li><s:property value="#product.unitCode"/></li>
			<li><s:property value="#product.barCode"/></li>
			<li><s:property value="#product.nameTotal"/></li>
			<li>
				<s:if test='#product.primaryCategoryBig !=null && !"".equals(#product.primaryCategoryBig)'>
					<s:property value="#product.primaryCategoryBig"/>
				</s:if>
				<s:else>
					&nbsp;
				</s:else>
			</li>
			<li>
				<s:if test='#product.primaryCategoryBig !=null && !"".equals(#product.primaryCategorySmall)'>
					<s:property value="#product.primaryCategorySmall"/>
				</s:if>
				<s:else>
					&nbsp;
				</s:else>
			</li>
			<li>
				<s:if test='#product.salePrice !=null && !"".equals(#product.salePrice)'>
					<s:text name="format.price"><s:param value="#product.salePrice"></s:param></s:text>
				</s:if>
				<s:else>
					&nbsp;
				</s:else>
			</li>
			<li>
				<s:if test='#product.standardCost !=null && !"".equals(#product.standardCost)'>
					<s:text name="format.price"><s:param value="#product.standardCost"></s:param></s:text>
				</s:if>
				<s:else>
					&nbsp;
				</s:else>
			</li>
		</ul>
	</s:iterator>
</div>