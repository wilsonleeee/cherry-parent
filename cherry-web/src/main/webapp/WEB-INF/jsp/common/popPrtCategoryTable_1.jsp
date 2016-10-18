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
			<li><input name="checkbox" type="<s:if test='%{checkType == "radio"}'>radio</s:if><s:else>checkbox</s:else>" 
				value="<s:property value="cateValInfo"/>"/></li>
			<li><s:property value="cateVal"/></li>
			<li><s:property value="cateValName"/></li>
		</ul>
	</s:iterator>
</div>