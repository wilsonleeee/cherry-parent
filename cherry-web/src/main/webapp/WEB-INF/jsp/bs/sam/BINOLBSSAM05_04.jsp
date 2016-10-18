<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.bs.BINOLBSSAM05">
<%-- 导入结果 --%>
<div id="successResult" style="display:none;">
	<s:property value="resultMap.currentImportBatchCode"/>
	<s:text name="BSSAM05_result_total" />
	<span id="sTotal"><s:property value="resultMap.successCount"/></span>
	<s:text name="BSSAM05_result_success" />
	<span id="sSuccess"><s:property value="resultMap.successCount"/></span>
</div>
<div id="failedResult" style="display:none;">
	<s:property value="resultMap.currentImportBatchCode"/>
	<s:text name="BSSAM05_result_total" />
	<span id="fTotal"><s:property value="%{resultMap.successCount+resultMap.failCount}"/></span>
	<s:text name="BSSAM05_result_success" />
	<span id="fSuccess"><s:property value="resultMap.successCount"/></span>
	<s:text name="BSSAM05_result_failed" />
	<span id="fFailed"><s:property value="resultMap.failCount"/></span>
</div>
</s:i18n>
