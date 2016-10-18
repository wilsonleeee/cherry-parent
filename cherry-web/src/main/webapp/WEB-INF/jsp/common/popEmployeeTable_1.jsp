<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="employeeList" id="employeeMap">
   <s:set name="departName" value=""/>
   <s:set name="positionName" value=""/>
   <ul>
    <li>
	  <input type="radio" name="employeeId" value="<s:property value="#employeeMap.employeeId"/>"/>
    </li>
    <li><span><s:property value="employeeCode"/></span></li>
    <li><span><s:property value="employeeName"/></span></li>
    <li>
       <span>
		 <s:iterator value="dpList">
		  <s:if test="#departName!=departName">
			<s:set name="departName" value="departName"/>
			<s:property value="departName"/>
		  </s:if>
		  <br />
		</s:iterator>
	  </span>
    </li>
    <li>
      <span>
		<s:iterator value="dpList">
		  <s:if test="#positionName!=positionName">
			<s:set name="positionName" value="positionName"/>
			<s:property value="positionName"/>
		  </s:if>
		  <br />
	   </s:iterator>
	  </span>
    </li>
  </ul>
</s:iterator>
</div>