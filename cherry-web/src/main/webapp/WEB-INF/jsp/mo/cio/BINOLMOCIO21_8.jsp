<%-- 柜台消息管理 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO21.js"></script>

<s:i18n name="i18n.mo.BINOLMOCIO21">
    <p align="center">             
     	<s:property value="%{departmentMessage.messageTitle}"/>                          
    </p>
     <p align="center">             
     	<s:text name='CIO21_publishDate'/>：<s:property value="%{departmentMessage.publishDate}"/>                          
    </p>
    <p align="left">             
     	<s:property value="%{departmentMessage.messageBody}"/>                          
    </p>
    <p class="clearfix">
        	<span>
        	<s:text name='CIO21_attachment'/>
        	<a title="<s:text name='CIO21_showDetail'/>" id="<s:property value="%{departmentMessage.filePathShow}"/>" name='<s:property value="%{departmentMessage.filePathShow}"/>' style="cursor:pointer;" href="<s:property value="%{departmentMessage.filePathShow}"/>">    	
        	<s:property value="%{departmentMessage.fileName}"/>   
        	</a>
        	</span>        	
        	</p>
 </s:i18n>
