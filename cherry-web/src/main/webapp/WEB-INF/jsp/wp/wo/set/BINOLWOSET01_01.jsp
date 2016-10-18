<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="baInfoList" id="baInfoMap" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="employeeCode"/></li>
<li><s:property value="employeeName"/></li>
<li><s:property value="identityCard"/></li>
<li><s:property value="mobilePhone"/></li>
<li>
<s:hidden name="employeeId"></s:hidden>
<a class="edit" href="javascript:void(0);">
	<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="global.page.edit" /></span>
</a>
<a class="delete" href="javascript:void(0);">
	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete" /></span>
</a>
</li>
</ul>
</s:iterator>
</div>