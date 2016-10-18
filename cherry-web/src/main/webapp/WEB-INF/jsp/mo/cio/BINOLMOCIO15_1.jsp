<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOCIO15">
	<div id="aaData">
	<s:iterator value="rivalList" id="rival">
		<ul>
			<%-- No. --%>
			<li><s:property value="RowNumber" /></li>
			<li><%-- 单位ID  --%>
				<s:property	value="rivalNameCN" /></li>
			<li><%-- 名称  --%>
				<s:property	value="rivalNameEN" /></li>
			<li><%-- 操作  --%>
			<cherry:show domId="MOCIO15EDIT">
				<a class="add" onclick="BINOLMOCIO15.popEditDialog(this);return false;">
					<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="global.page.edit" /></span>
				</a>
			</cherry:show>
			<cherry:show domId="MOCIO15DEL">
				<a class="delete" onclick="BINOLMOCIO15.popDeleteDialog(this);return false;">
					<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="global.page.delete" /></span>
				</a>
			</cherry:show>
			<div>
   		 		<input type="hidden" id="rivalNameCN" name="rivalNameCN" value="<s:property value='rivalNameCN' />"></input>
				<input type="hidden" id="rivalNameEN" name="rivalNameEN" value="<s:property value='rivalNameEN' />"></input>
				<input type="hidden" id="rivalId" name="rivalId" value="<s:property value='binRivalID' />"></input>
				<input type="hidden" id="editBrandInfoId" name="editBrandInfoId" value="<s:property value='brandInfoId' />"></input>
   		 	</div>
			</li>
		</ul>
	</s:iterator></div>
</s:i18n>