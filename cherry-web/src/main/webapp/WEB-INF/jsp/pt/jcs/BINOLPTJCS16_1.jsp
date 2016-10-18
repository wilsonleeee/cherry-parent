<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.st.BINOLSTIOS09">
<%-- 导入结果 --%>
<div id="successResult" style="display:none;">
	<s:property value="resultMap.solutionName"/>
	<s:text name="导入成功" />
</div>
<div id="failedResult" style="display:none;">
	<s:property value="resultMap.solutionName"/>
	<s:text name="导入失败" />
		<span id="fFailed"><s:property value="resultMap.errorCount"/></span>
</div>
</s:i18n>
