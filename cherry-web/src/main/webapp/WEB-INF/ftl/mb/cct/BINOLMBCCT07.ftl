<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
	<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
	<#include "/WEB-INF/ftl/common/head.inc.ftl">
	<link rel="stylesheet" href="/Cherry/css/common/blueprint/crm_web.css" type="text/css">
	<link rel="stylesheet" href="/Cherry/css/cherry/gadgets.css" type="text/css">
	<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
	<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
	<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
	<script type="text/javascript" src="/Cherry/js/mb/cct/BINOLMBCCT07.js"></script>
</head>
<#-- 引用国际化文件 -->
<@s.i18n name="i18n.mb.BINOLMBCCT07">
<body style="overflow-y:auto;">
	<@s.url id="getCustomerList_Url" action="BINOLMBCCT08_init"/>
	<@s.url id="getCallLogList_Url" action="BINOLMBCCT09_init"/>
	<@s.url id="getIssueList_Url" action="BINOLMBCCT10_init"/>
	<@s.url id="cherryRefreshSession_Url" action="BINOLCM05_isSessionTimeout" namespace="/common">
		<@s.param name="code">${(code)!?html}</@s.param>
	</@s.url>
	<div class="hide">
		<a id="getCustomerListUrl" href="${getCustomerList_Url}"></a>
		<a id="getCallLogListUrl" href="${getCallLogList_Url}"></a>
		<a id="getIssueListUrl" href="${getIssueList_Url}"></a>
		<a id="cherryRefreshSessionUrl" href="${cherryRefreshSession_Url}"></a>
	</div>
	<form id="rptMainForm" method="post" class="inline">
		<input type="hidden" id="csrftokenCode" name="code" value="${(code)!}">
		<input type="hidden" id="brandInfoId" name="brandInfoId" value="${(brandInfoId)!}"/>
	</form>
	<div  id="div_main" class="clearfix">
		<div id="actionResultDisplay"></div>
		<div class="ui-tabs" id="ui-tabs">
			<ul class="ui-tabs-nav clearfix">
		        <li><a href="#tabs-1"><@s.text name="mbcct.customerRpt"/></a></li>
		        <li><a href="#tabs-2"><@s.text name="mbcct.callLogRpt"/></a></li>
		        <li><a href="#tabs-3"><@s.text name="mbcct.issueRpt"/></a></li>
		    </ul>
			<div class="ui-tabs-panel" id="tabs-1">
				<div class="clearfix" id="getCustomerDiv">
				</div>
			</div>
			<div class="ui-tabs-panel" id="tabs-2">
				<div class="clearfix" id="getCallLogDiv">
				</div>
			</div>
			<div class="ui-tabs-panel" id="tabs-3">
				<div class="clearfix" id="getIssueDiv">
				</div>
			</div>
		</div>
		<#-- 弹出页面 -->
		<div class="hide" id="customerDialog"></div>
		<div class="hide" id="issueDetailDialog"></div>
		<div class="hide" id="customerDialogTitle"><@s.text name="mbcct.customerDialogTitle" /></div>
		<div class="hide" id="issueDetailDialogTitle"><@s.text name="mbcct.issueDetailDialogTitle" /></div>
	</div>
</body>
</@s.i18n>
</html>
