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
			<li>
				<s:if test='%{checkType != null && checkType == "radio"}'>
					<input name="productInfo" type="radio"  value="<s:property value="#promotion.prmProductInfo"/>" />
				</s:if>
				<s:else>
					<input name="productInfo" type="checkbox"  class="checkbox" value="<s:property value="#promotion.prmProductInfo"/>" />
				</s:else>
			</li>
			<li><s:property value="#promotion.unitCode"/></li>
			<li><s:property value="#promotion.barCode"/></li>
			<li><s:property value="#promotion.nameTotal"/></li>
			<li>
				<s:if test='#promotion.primaryCategory !=null && !"".equals(#promotion.primaryCategory)'>
					<s:property value="#promotion.primaryCategory"/>
				</s:if>
				<s:else>
					&nbsp;
				</s:else>
			</li>
			<li>
				<s:if test='#promotion.secondryCategory !=null && !"".equals(#promotion.secondryCategory)'>
					<s:property value="#promotion.secondryCategory"/>
				</s:if>
				<s:else>
					&nbsp;
				</s:else>
			</li>
			<li>
				<s:if test='#promotion.smallCategory !=null && !"".equals(#promotion.smallCategory)'>
					<s:property value="#promotion.smallCategory"/>
				</s:if>	
				<s:else>
					&nbsp;
				</s:else>
			</li>
			<li><span><s:text name="format.price"><s:param value="#promotion.standardCost"></s:param></s:text></span></li>
		</ul>
	</s:iterator>
</div>