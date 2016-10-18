<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%-- 修改密码结果 --%>
<s:if test="hasActionErrors()">
    <div id="errorMessageDiv">
        <p class="message"><span></span></p>
    </div>
    <div class="hide"><s:actionerror/></div>
</s:if>
<s:else>
    <div id="successDiv">
        <p class="success">
            <span><s:text name="global.page.editSuccess"/></span>
        </p>
    </div>
</s:else>