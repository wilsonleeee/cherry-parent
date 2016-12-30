<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 产品分类选项值编辑URL --%>
<s:url id="edit_url" action="BINOLPTJCS01_editVal"><s:param name="brandInfoId">${brandInfoId}</s:param></s:url>
<%--启用和停用URL--%>
<s:url id="changeFlag_Url" action="BINOLPTJCS01_changeFlagVal"><s:param name="brandInfoId">${brandInfoId}</s:param></s:url>
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
				<%--<a href="#" onclick="BINOLPTJCS01.edit('${edit_url}','${propValId}','${status.index + 1}',1);return false;"><s:text name="global.page.edit"/></a>--%>
				<!--这里根据目前查询的状态显示出 停用&编辑  或者启用-->

				<s:if test='(category.showDisabled != null && "0"== category.showDisabled)'>
					<!-- 启用 -->
					<a href="" class="add" onclick="BINOLPTJCS01.changeFlag(this,'${changeFlag_Url}',1,'${propValId}');return false;">
						<span class="ui-icon icon-enable"></span>
						<span class="button-text"><s:text name="global.page.enable"/></span>
					</a>
				</s:if>
				<!--如果是启用的话 停用&编辑-->
				<s:else>
					<!-- 编辑 -->
					<a href="" class="edit" onclick="BINOLPTJCS01.edit('${edit_url}','${propValId}','${status.index + 1}',1);return false;">
						<span class="ui-icon icon-edit-big"></span>
						<span class="button-text"><s:text name="global.page.edit"/></span>
					</a>
					<!-- 停用 -->
					<a href="" class="delete" onclick="BINOLPTJCS01.changeFlag(this,'${changeFlag_Url}',0,'${propValId}');return false;">
						<span class="ui-icon icon-disable"></span>
						<span class="button-text"><s:text name="global.page.disable"/></span>
					</a>
				</s:else>
			</cherry:show>
			</span>
		</td>
	</tr>
</s:iterator>
</s:i18n>