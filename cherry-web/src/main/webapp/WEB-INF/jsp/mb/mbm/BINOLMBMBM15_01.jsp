<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<s:if test="hasActionErrors()">
<div id="messageDiv">
<p class="message"><span></span></p>
</div>
<div class="hide"><s:actionerror/></div>
</s:if>
<s:else>
<div id="messageDiv">
<p class="success"><span></span></p>
</div>
<div class="hide"><s:actionmessage/></div>
</s:else>