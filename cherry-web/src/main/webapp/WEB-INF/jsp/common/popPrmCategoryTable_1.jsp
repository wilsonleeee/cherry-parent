<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="resultList">
		<ul>
			<s:if test='%{checkType == "radio"}'>
				<li><input type="radio" name="checkbox" value="<s:property value="prmCateInfo"/>"/></li>
			</s:if>
			<s:else>
				<li><input type="checkbox" name="checkbox" value="<s:property value="prmCateInfo"/>"/></li>
			</s:else>
			<li><s:property value="cateCode"/></li>
			<li><s:property value="cateName"/></li>
		</ul>
	</s:iterator>
</div>