<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="referList" id="referMap">
<ul>
<li><span><s:property value="name"/></span></li>
<li><span><s:property value="memCode"/></span></li>
<li><span><s:property value="mobilePhone" /></span></li>
<li><span>
<s:if test='%{birthYear != null && !"".equals(birthYear) && birthMonth != null && !"".equals(birthMonth) && birthDay != null && !"".equals(birthDay)}'>
<s:property value="birthYear"/>-<s:property value="birthMonth"/>-<s:property value="birthDay"/>
</s:if>
</span></li>
<li><span><s:property value="standardProvince"/></span></li>
<li><span><s:property value="standardCity"/></span></li>
<li><span><s:property value="counterName"/></span></li>
<li><span><s:property value="joinDate"/></span></li>
</ul>
</s:iterator>
</div>