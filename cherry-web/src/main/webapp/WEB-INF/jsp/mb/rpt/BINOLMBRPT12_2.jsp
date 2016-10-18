<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="prtList">
		<ul>
			<li>
				<input name="productInfo" class="checkbox" type="radio" value='<s:property value="productInfo"/>'/>
			</li>
			<li><s:property value="unitCode"/></li>
			<li><s:property value="barCode"/></li>
			<li><s:property value="nameTotal"/>   </li>
			<li><s:property value="primaryCategoryBig"/></li>
			<li><s:property value="quantity"/></li>
		</ul>
	</s:iterator>
</div>