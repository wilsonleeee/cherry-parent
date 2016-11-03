<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:if test="hasActionErrors()">
	<div class="actionError" id="actionResultDiv">
	  	<s:actionerror/>
	</div>
</s:if>
<s:if test="hasActionMessages()">
	<div class="actionSuccess" id="actionResultDiv">
	  	<s:actionmessage escape="false"/>
	</div>
</s:if>