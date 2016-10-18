<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<s:i18n name="i18n.mb.BINOLMBMBM09">
<div style="padding: 20px 0 0 10px" id="reMoveMemCounterDiv">
<form id="reMoveMemCounterForm">
<s:hidden name="batchCode"></s:hidden>
<s:hidden name="subType"></s:hidden>
<s:hidden name="oldOrgId" value="%{memInfoRecordInfo.newOrgId}"></s:hidden>
<s:hidden name="oldCounterCode" value="%{memInfoRecordInfo.newCounterCode}"></s:hidden>
<s:hidden name="newOrgId" value="%{memInfoRecordInfo.oldOrgId}"></s:hidden>
<s:hidden name="newCounterCode" value="%{memInfoRecordInfo.oldCounterCode}"></s:hidden>
<s:hidden name="newCounterKind" value="%{memInfoRecordInfo.oldCounterKind}"></s:hidden>
</form>
<div style="padding-bottom: 10px;font-size: 13px;">
<strong><s:text name="reMoveCouConfirm"></s:text></strong>
</div>
<div style="padding: 2px 0">
<label style="text-align: right;width: 90px"><s:text name="batchCode"></s:text></label>
<s:property value="batchCode"/>
</div>
<div style="padding: 2px 0">
<label style="text-align: right;width: 90px"><s:text name="modifyTime"></s:text></label>
<s:property value="memInfoRecordInfo.modifyTime"/>
</div>
<div style="padding: 2px 0">
<label style="text-align: right;width: 90px"><s:text name="oldCounter"></s:text></label>
<s:property value="memInfoRecordInfo.oldCounterName"/>(<s:property value="memInfoRecordInfo.oldCounterCode"/>)
</div>
<div style="padding: 2px 0">
<label style="text-align: right;width: 90px"><s:text name="newCounter"></s:text></label>
<s:property value="memInfoRecordInfo.newCounterName"/>(<s:property value="memInfoRecordInfo.newCounterCode"/>)
</div>
<div style="padding: 2px 0">
<label style="text-align: right;width: 90px"><s:text name="moveCount"></s:text></label>
<s:property value="memInfoRecordInfo.moveCount"/>
</div>
</div>
</s:i18n>