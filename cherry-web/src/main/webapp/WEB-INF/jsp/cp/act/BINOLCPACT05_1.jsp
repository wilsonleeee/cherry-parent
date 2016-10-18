<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div class="crm_content_header">
	<span class="icon_crmnav"></span>
	<span><s:text name="活动执行" /> &gt; <s:text name="活动预约" /> </span>
</div>
<div id="div_main" class="panel-content">
	<div class="section">
		<s:if test="hasActionErrors()">
			<div class="actionError" id="actionResultDiv"><s:actionerror/></div>
		</s:if>
	</div>
</div>