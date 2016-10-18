<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<s:iterator value="factoryList" id="factoryMap" status="R">
	<tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>">
		<td><input type="radio" name="manuFactId" value="<s:property value="#factoryMap.manuFactId"/>"/></td>
		<td><s:property value="#factoryMap.manuFactCode"/></td>
		<td class="factoryName"><s:property value="#factoryMap.factoryName"/></td>
	</tr>
</s:iterator>
