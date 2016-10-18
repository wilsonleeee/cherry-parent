<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/common/wz_jsgraphics.js"></script>
<script type="text/javascript" src="/Cherry/js/common/batchWarn.js"></script>
<div class="hide">
    <s:url action="BINOLCM40_getBatchExecuteResult" id="getBatchExecuteResultUrl" namespace="/common"></s:url>
    <a id="getBatchExecuteResultUrl" href="${getBatchExecuteResultUrl}"></a>
    <s:url action="BINOLCM40_getView" id="getViewUrl" namespace="/common"></s:url>
    <span id="header_WorkFlowView"><s:text name="header.WorkFlowView"></s:text></span>
</div>
<div class="left">
<span id="spanBatchWarn" class="hide">
    <span id="spanBatchWarnBlink" class="ui-icon icon-help-warning"></span>
    <span class="red"><strong><s:text name="header.BatchWarn"></s:text></strong></span>
    <a id="getViewUrl" class="popup" onclick="batchWarn.getBatchWFView();return false;" href="${getViewUrl}" style="color: red;"><strong><s:text name="header.openWorkFlowView"></s:text></strong></a>
    &nbsp;&nbsp;
</span>
</div>
<div id="divWorkFlowView" class="hide"></div>