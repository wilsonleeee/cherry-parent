<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="hasActionErrors()">
<div class="error" id="actionResultBody">
  <div class="center">
	<div class="ui-state-error clearfix">
	  	<s:actionerror/>
	</div>
    <button class="close" onclick="dialogClose();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
  </div>
</div>
</s:if>

<s:if test="hasActionMessages()">
<div class="success" id="actionResultBody">
  <div class="center">
	<div class="ui-state-success clearfix">
	  	<s:actionmessage/>
	</div>
    <button class="close" onclick="dialogClose();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
  </div>
</div>
</s:if>

