<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM09">
<div id="aaData">
	<s:iterator value="prmCategoryList" id="prmCate">
		<s:url id="categoryInfoUrl" value="/ss/BINOLSSPRM12_init.action">
			<s:param name="prmCategoryId" value="#prmCate.prmCategoryId"></s:param>
		</s:url>
		<ul>
		    <%-- 选择 --%>
			 <!--<li><input name="prmCategoryInfo" type="checkbox" value="${prmCate.validFlag}_${prmCate.prmCategoryId}_${prmCate.modifyTime}_${prmCate.modifyCount}" class="checkbox" /></li>
			--><%-- 编号 --%>
			<li><s:property value="RowNumber"/></li>
			<%-- 类别码--%>
			<li>
				<s:if test="#prmCate.itemClassCode !='' && #prmCate.itemClassCode !=null">
				   <a href="${categoryInfoUrl}" class="popup" onclick="javascript:openWin(this);return false;">
				      <span class="left"><s:property value="itemClassCode"/></span>
				   </a>
				</s:if>
				<s:else>&nbsp;</s:else>
				<span class="hide"></span>
			</li>
			<%-- 类别名称 --%>
			<li>
				<s:if test="#prmCate.itemClassName !='' && #prmCate.itemClassName !=null"><s:property value="itemClassName"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 类别特征码 --%>
		    <li>
				<s:if test="#prmCate.curClassCode !='' && #prmCate.curClassCode !=null"><s:property value="curClassCode"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 有效区分 --%>
			<li>
				<s:if test="#prmCate.validFlag ==1"><span class='ui-icon icon-valid'></span></s:if>
				<s:else><span class='ui-icon icon-invalid'></span></s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
