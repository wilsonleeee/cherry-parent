<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<s:if test="hasActionErrors()">
<div id="errorMessageDiv">
<p class="message"><span></span></p>
</div>
<div class="hide"><s:actionerror/></div>
</s:if>
<s:else>
<s:i18n name="i18n.pl.BINOLPLRLM01">
<div id="successDiv">
<p class="success"><span><s:text name="save_success_message" /></span></p>
<span class="hide">
<s:hidden name="roleId"></s:hidden>
<s:hidden name="roleName"></s:hidden>
<s:url action="BINOLPLRLM03_init" id="roleAuthorityInitUrl"></s:url>
<a href="${roleAuthorityInitUrl }" id="roleAuthorityInitUrl"></a>
</span>
</div>
</s:i18n>
</s:else>