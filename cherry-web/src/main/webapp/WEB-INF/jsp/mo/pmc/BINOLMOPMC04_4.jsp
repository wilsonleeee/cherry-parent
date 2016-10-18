<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="posMenuList" id="posMenuMap">
<ul>
<li>
	<input type="radio" name="parent" value='<s:property value="#posMenuMap.posMenuID"/>' />
</li>
<li><span id="menuCode"><s:property value="#posMenuMap.menuCode"/></span></li>
<li><span id="menuName"><s:property value="#posMenuMap.menuName"/></span></li>
<li><span id="menuType"><s:property value="#posMenuMap.menuType"/></span></li>
</ul>
</s:iterator>
</div>