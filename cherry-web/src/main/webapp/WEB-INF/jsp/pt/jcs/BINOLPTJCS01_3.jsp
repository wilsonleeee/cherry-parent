<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 产品分类选项值编辑URL --%>
<s:url id="edit_url" action="BINOLPTJCS01_editVal"><s:param name="brandInfoId">${brandInfoId}</s:param></s:url>
<%-- ========================= 产品分类选项值一览 ============================= --%>
<s:i18n name="i18n.pt.BINOLPTJCS01">
<s:iterator value="categoryValList" status="status">
	<tr id="${status.index + 1}" class="<s:if test='#status.odd == true'>odd</s:if><s:else>even</s:else>" >
		<td>${status.index + 1}</td>
		<td><span><s:property value="propValueCherry"/></span></td>
		<td><span><s:property value="propValueCN"/></span></td>
		<td><span><s:property value="propValueEN"/></span></td>
		<td class="center">
			<span>
			<cherry:show domId="BINOLPTJCS0105">
				<a href="#" onclick="BINOLPTJCS01.edit('${edit_url}','${propValId}','${status.index + 1}',1);return false;"><s:text name="global.page.edit"/></a>
			</cherry:show>
			</span>
		</td>
	</tr>
</s:iterator>
</s:i18n>