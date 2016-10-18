<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<cherry:form></cherry:form>
<s:action name="BINOLWPMBM01_init" namespace="/wp" executeResult="true"></s:action>
<s:hidden name="wmFlag"></s:hidden>
<s:url id="counterSaleUrl" action="BINOLWPCM01_init" namespace="/wp" >
	<s:param name="pageType">SALE</s:param>
</s:url>
<a id="counterSaleBtn" class="hide" href="${counterSaleUrl}" ></a>