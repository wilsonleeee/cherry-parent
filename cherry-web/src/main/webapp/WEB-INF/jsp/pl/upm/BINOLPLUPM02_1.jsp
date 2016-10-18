<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.pl.BINOLPLUPM02">
<s:property value="pwConfInfo.minLength"/> <s:text name="upm02_char1"/> <s:property value="pwConfInfo.maxLength"/><s:text name="upm02_include"/>
     	<%-- 英文--%>
     	<s:if test='pwConfInfo.hasAlpha == "1"'><s:text name="upm02_hasAlpha"/></s:if>
     	<s:if test='pwConfInfo.hasAlpha == "1" && (pwConfInfo.hasNumeric == "1" || pwConfInfo.hasOtherChar == "1")'>
     	<s:text name="upm02_char2"/>
     	</s:if>
     	<%-- 数字--%>
     	<s:if test='pwConfInfo.hasNumeric == "1"'><s:text name="upm02_hasNumeric"/></s:if>
     	<s:if test='pwConfInfo.hasNumeric == "1" && pwConfInfo.hasOtherChar == "1"'>
     	<%-- 特殊符号--%>
     	<s:text name="upm02_hasOtherChar"/><s:property value="pwConfInfo.otherCharSpace"/><s:text name="upm02_hasOtherCharClose"/>
     	</s:if>
</s:i18n>