<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM05">
<div id="aaData">
	<s:iterator value="prmTypeList" id="prmType">
		<s:url id="detailsUrl" value="/ss/BINOLSSPRM08_init.action">
			<s:param name="prmTypeId">${prmType.prmTypeId}</s:param>
		</s:url>
		<ul>
		    <%-- 选择 --%>
			<!--<li><input name="prmTypeInfo" type="checkbox" value="${prmType.validFlag}_${prmType.prmTypeId}_${prmType.modifyTime}_${prmType.modifyCount}" class="checkbox" /></li>
			--><%-- 编号 --%>
			<li><s:property value="RowNumber"/></li>
			<%-- 大分类代码 --%>
			<li>
			    <s:if test='#prmType.primaryCategoryCode !=null && !"".equals(#prmType.primaryCategoryCode)'>
				  <a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;"><s:property value="primaryCategoryCode"/></a>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 大分类名称 --%>
			<li>
				<s:if test='#prmType.primaryCategoryName !=null && !"".equals(#prmType.primaryCategoryName)'><s:property value="primaryCategoryName"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 中分类代码 --%>
		    <li>
				<s:if test='#prmType.secondryCategoryCode !=null && !"".equals(#prmType.secondryCategoryCode)'><s:property value="secondryCategoryCode"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 中分类名称 --%>
		    <li>
				<s:if test='#prmType.secondryCategoryName !=null && !"".equals(#prmType.secondryCategoryName)'><s:property value="secondryCategoryName"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 小分类代码--%>
			<li>
				<s:if test='#prmType.smallCategoryCode !=null && !"".equals(#prmType.smallCategoryCode)'><s:property value="smallCategoryCode"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 小分类名称--%>
			<li>
				<s:if test='#prmType.smallCategoryName !=null && !"".equals(#prmType.smallCategoryName)'><s:property value="smallCategoryName"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 有效区分 --%>
			<li>
				<s:if test='"1".equals(#prmType.validFlag)'><span class='ui-icon icon-valid'></span></s:if>
				<s:else><span class='ui-icon icon-invalid'></span></s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
