<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="memberInfoList" id="memberInfo" status="status">
<ul>
<li><span>
<s:url action="BINOLWRMRP01_search" id="searchMemInfoUrl">
<s:param name="memberInfoId" value="%{#memberInfo.memberInfoId}"></s:param>
</s:url>
<a href="${searchMemInfoUrl }"><s:property value="memberCode"/></a>
</span></li>
<li><span><s:property value="mobilePhone"/></span></li>
<li><span><s:property value="memberName"/></span></li>
<li>
  <s:if test='%{birthYear != null && !"".equals(birthYear) && birthMonth != null && !"".equals(birthMonth) && birthDay != null && !"".equals(birthDay)}'>
	<s:property value="birthYear"/>-<s:property value="birthMonth"/>-<s:property value="birthDay"/>
  </s:if>
</li>
<li><span><s:property value="levelName"/></span></li>
<li><span><s:property value="changablePoint"/></span></li>
<li><span><s:property value="joinDate"/></span></li>
</ul>
</s:iterator>
</div>