<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
<s:iterator value="counterSolutionList" id="counterSolutionMap">
  <%--  <tr>
		<td><s:property value="RowNumber"/></td>
		<td><s:property value="solutionCode"/></td>
		<td><s:property value="solutionName" /></td>
   </tr> --%>
 <ul>
    <li><s:property value="RowNumber"/></li>
    <li><s:property value="solutionCode" /></li>
    <li><s:property value="solutionName"/></li>
    <li><s:property value="startDate"/></li>
    <li><s:property value="endDate"/></li>
    <li> <%-- 有效区分 --%>
		<s:if test='"1".equals(validFlag)'>
			<span class='ui-icon icon-valid'></span>
		</s:if>
		<s:else>
			<span class='ui-icon icon-invalid'></span>
		</s:else>
    </li>
</ul> 
</s:iterator>
</div>
