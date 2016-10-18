<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- ======================= 产品共通 取得某分类List ================================== -->
<!-- 语言 -->
<s:set id="language" value="#session.language" />
<%-- 取得分类集合URL --%>
<s:url id="getCateList_url" action="BINOLPTJCS03_getCateList"/>
<s:i18n name="i18n.pt.BINOLPTJCS03">
<span>
	<span style="display:inline-block;">
	<input type="radio" name="propValId_<s:property value='propId'/>" checked="checked" value=""
		onclick="BINOLPTJCS00.bind(this,'${getCateList_url}',<s:property value="teminalFlag"/>)" />
	<s:text name="JCS03_default"/><%-- 默认值 --%>
	<s:if test="teminalFlag > 0"><input type="hidden" name="teminalFlag" value="<s:property value="teminalFlag"/>"></s:if>
	</span>
	<s:iterator value="cateList">
		<span style="display:inline-block;">
		<input type="radio" name="propValId_<s:property value='propId'/>" value='<s:property value="propValId"/>'
		onclick="BINOLPTJCS00.bind(this,'${getCateList_url}',<s:property value="teminalFlag"/>)"
			<s:if test="teminalFlag == 2 && status == 0">disabled ="disabled"</s:if>
			<s:if test="teminalFlag == 1 && status == 1">checked="checked"</s:if>/>
		<s:if test="#language.equals('en_US')"><s:property value="propValueEN"/></s:if><s:else><s:property value="propValueCN"/></s:else>
		</span>
	</s:iterator>
</span>
</s:i18n>