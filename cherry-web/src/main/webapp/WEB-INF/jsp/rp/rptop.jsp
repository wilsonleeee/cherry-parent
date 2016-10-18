<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%> 
<ct:form id="mainForm" action="">
<s:if test='%{biFlag.equals("TRUE")}'>
<s:i18n name="i18n.rp.RPINDEX">
	 <div class="panel-header">
	        <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="lbltitle1"/></span> </div>
	 </div>
	 <div class="panel-content">
	<s:url id="openBIWindow" value="openBIWindow"></s:url>	
	
	<a href="${openBIWindow}" class="popup" id="bilink" onclick="javascript:openWin(this);return false;"><s:text name="lbllink"/></a>
	</div>
	<script type=text/javascript>
	$("#bilink").click();		
	</script>
</s:i18n>
</s:if>
</ct:form>