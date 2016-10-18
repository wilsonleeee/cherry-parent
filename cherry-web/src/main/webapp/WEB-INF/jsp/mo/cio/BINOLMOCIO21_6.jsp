<%-- 柜台消息停用启用结果 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<s:if test="hasActionErrors()">
    <div id="actionResultDiv">
        <p class="message"><span><s:actionerror/></span></p>
    </div>
</s:if>
<s:else>
    <s:i18n name="i18n.mo.BINOLMOCIO21">
        <div id="successDiv">
        <p class="success">
            <span><s:text name="disableOrEnable_success_message" /></span>
        </p>
        </div>
    </s:i18n>
</s:else>