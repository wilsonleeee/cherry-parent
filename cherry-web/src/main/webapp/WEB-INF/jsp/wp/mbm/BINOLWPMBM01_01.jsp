<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="memberInfoList" id="memberInfo" status="status">
<ul>
<li>
<s:url action="BINOLWPMBM01_memReport" id="memReportUrl">
<s:param name="memberInfoId" value="%{#memberInfo.memberInfoId}"></s:param>
</s:url>
<input type="radio" name="memberInfoId" value='<s:property value="memberInfoId"/>'/>
<input value="${memReportUrl }" type="hidden" name="memReportUrl"/>
</li>
<li><span><s:property value="mobilePhone"/></span></li>
<li><span><s:property value="memberCode"/></span></li>
<li><span><s:property value="memberName"/></span></li>
<li>
  <s:if test='%{birthYear != null && !"".equals(birthYear) && birthMonth != null && !"".equals(birthMonth) && birthDay != null && !"".equals(birthDay)}'>
	<s:property value="birthYear"/>-<s:property value="birthMonth"/>-<s:property value="birthDay"/>
  </s:if>
</li>
<li><span><s:property value="levelName"/></span></li>
<li><span><s:property value="joinDate"/></span></li>
<li><span><s:property value="changablePoint"/></span></li>
<li><span><s:property value="changeDate"/></span></li>
<li><span><s:property value="totalAmount"/></span></li>
<li><span><s:property value="amount"/></span></li>
<li><span><s:property value="couponCount"/></span></li>
</ul>
</s:iterator>
</div>