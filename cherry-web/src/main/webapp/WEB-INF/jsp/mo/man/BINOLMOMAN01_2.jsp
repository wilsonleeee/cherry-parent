<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<s:if test="hasActionErrors()">
<div id="errorMessageDiv">
<p class="message"><span></span></p>
</div>
<div class="hide"><s:actionerror/></div>
</s:if>
<s:else>
<s:i18n name="i18n.mo.BINOLMOMAN01">
<div id="successDiv">
<p class="success"><span><s:text name="MAN01_save_success_message" /></span></p>
</div>
</s:i18n>
</s:else>