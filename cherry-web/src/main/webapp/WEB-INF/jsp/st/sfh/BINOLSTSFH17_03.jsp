<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.st.BINOLSTSFH17">
<%-- 导入结果 --%>
<div id="successResult" style="display:none;">
	<s:property value="resultMap.currentImportBatchCode"/>
	<s:text name="SFH17_result_total" />
	<span id="sTotal"><s:property value="resultMap.successCount"/></span>
	<s:text name="SFH17_result_success" />
	<span id="sSuccess"><s:property value="resultMap.successCount"/></span>
</div>
<div id="failedResult" style="display:none;">
	<s:property value="resultMap.currentImportBatchCode"/>
	<s:text name="SFH17_result_total" />
	<span id="fTotal"><s:property value="%{resultMap.successCount+resultMap.errorCount}"/></span>
	<s:text name="SFH17_result_success" />
	<span id="fSuccess"><s:property value="resultMap.successCount"/></span>
	<s:text name="SFH17_result_failed" />
	<span id="fFailed"><s:property value="resultMap.errorCount"/></span>
</div>
</s:i18n>
