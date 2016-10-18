<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM01">
<div id="aaData">
	<s:iterator value="promotionProList" id="prmPro">
		<s:url id="detailsUrl" value="/ss/BINOLSSPRM04_init.action">
			<s:param name="promotionProId">${prmPro.promotionProId}</s:param>
			<s:param name="brandInfoId">${brandInfoId}</s:param>
		</s:url>
		<ul>
		    <%-- 选择 --%>
			 <%-- <li><input name="promotionProInfo" type="checkbox" value="${prmPro.validFlag}_${prmPro.promotionProId}_${prmPro.modifyTime}_${prmPro.modifyCount}" class="checkbox" /></li> --%>
			<%-- 编号 --%>
			<li><s:property value="RowNumber"/></li>
			<%-- 促销品全名 --%>
			<li>			
				<s:if test='#prmPro.nameTotal != null && !"".equals(#prmPro.nameTotal)'>
				      <p><a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;"><s:property value="nameTotal"/></a></p>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 厂商编码 --%>
			<li>
				<s:if test='#prmPro.unitCode !=null && !"".equals(#prmPro.unitCode)'><s:property value="unitCode"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 促销品条码 --%>
			<li>
				<s:if test='#prmPro.barCode !=null && !"".equals(#prmPro.barCode)'><s:property value="barCode"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 促销品类型 --%>
			<li><span><s:property value="#application.CodeTable.getVal('1139',promCate)"/></span></li>
			<%-- 大分类 --%>
			<li>
				<s:if test='#prmPro.primaryCategoryName !=null && !"".equals(#prmPro.primaryCategoryName)'><s:property value="primaryCategoryName"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 中分类 --%>
			<li>
				<s:if test='#prmPro.secondryCategoryName !=null && !"".equals(#prmPro.secondryCategoryName)'><s:property value="secondryCategoryName"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 小分类 --%>
			<li>
				<s:if test='#prmPro.smallCategoryName !=null && !"".equals(#prmPro.smallCategoryName)'><s:property value="smallCategoryName"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 是否下发到POS --%>
			<li>
				 <span><s:property value='#application.CodeTable.getVal("1341",isPosIss)'/></span>
			</li>
			<%-- 促销品标准成本 --%>
		    <li>
				<s:if test='#prmPro.standardCost !=null && !"".equals(#prmPro.standardCost)'><s:text name="format.price"><s:param value="#prmPro.standardCost"></s:param></s:text></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 促销品类型 --%>
			<li style="text-align: left;">
				 <span><s:property value='#application.CodeTable.getVal("1345",mode)'/></span>
			</li>
			<%-- 促销品销售价格 --%>
		    <%--<li>
				<s:if test='#prmPro.salePrice !=null && !"".equals(#prmPro.salePrice)'><s:text name="format.price"><s:param value="#prmPro.salePrice"></s:param></s:text></s:if>
				<s:else>&nbsp;</s:else>
			</li>--%>
			<%-- 促销品状态 --%>
			<%--<li>
			    <span><s:property value='#application.CodeTable.getVal("1001", status)'/></span>
			</li> --%>
			<%-- 有效区分 --%>
			<li>
				<s:if test='"1".equals(#prmPro.validFlag)'><span class='ui-icon icon-valid'></span></s:if>
				<s:else><span class='ui-icon icon-invalid'></span></s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
