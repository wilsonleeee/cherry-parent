<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="cateList">
		<ul>
			<li>
				<input name="bigCateInfo" class="checkbox" type="radio" value='<s:property value="bigCateInfo"/>'/>
			</li>
			<li><s:property value="primaryCategoryBig"/></li>
			<li><s:property value="cateCode"/></li>
			<li><s:property value="quantity"/></li>
		</ul>
	</s:iterator>
</div>