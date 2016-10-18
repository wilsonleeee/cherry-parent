<!doctype html>
<html>
<#-- 引用国际化文件 -->
<@s.i18n name="i18n.mb.BINOLMBCCT01">
<head>
	<meta charset="utf-8">
	<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
	<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
	<#include "/WEB-INF/ftl/common/head.inc.ftl">
	<#include "/WEB-INF/ftl/mb/cct/BINOLMBCCT01_mac.ftl">
	<link rel="stylesheet" href="/Cherry/css/common/blueprint/crm_web.css" type="text/css">
	<link rel="stylesheet" href="/Cherry/css/cherry/gadgets.css" type="text/css">
	<script type="text/javascript" src="/Cherry/gadgets/js/core:rpc:pubsub:shindig-container.js?c=1&debug=1"></script>
	<script type="text/javascript" src="/Cherry/js/common/cherryGadget.js"></script>
	<script type="text/javascript" src="/Cherry/js/lib/sjcl.js"></script>
	<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
	<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
	<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
	<script type="text/javascript" src="/Cherry/js/mb/cct/BINOLMBCCT01.js"></script>
	<script type="text/javascript" src="/Cherry/js/mb/cct/BINOLMBCCT02.js"></script>
	<script type="text/javascript" src="/Cherry/js/mb/cct/BINOLMBCCT04.js"></script>
</head>
<body style="overflow-y:auto;">
	<@s.url id="addIssue_Url" action="BINOLMBMBM27_init"/>
	<@s.url id="getIssue_Url" action="BINOLMBCCT02_init"/>
	<@s.url id="getMemberInfo_Url" action="BINOLMBCCT03_Action"/>
	<@s.url id="getMemberList_Url" action="BINOLMBCCT04_init"/>
	<@s.url id="searchMember_Url" action="BINOLMBCCT04_search"/>
	<@s.url id="addCustomer_Url" action="BINOLMBCCT05_init"/>
	<@s.url id="updateCallLog_Url" action="BINOLMBCCT04_update"/>
	<@s.url id="getIssueInfo_Url" action="BINOLMBMBM28_init"/>
	<@s.url id="searchIssueList_url" action="BINOLMBCCT02_search"/>
	<@s.url id="cherryRefreshSession_Url" action="BINOLCM05_isSessionTimeout" namespace="/common">
		<@s.param name="code">${(code)!?html}</@s.param>
	</@s.url>
	<div class="hide">
		<a id="addIssueUrl" href="${addIssue_Url}"></a>
		<a id="getIssueUrl" href="${getIssue_Url}"></a>
		<a id="getMemberInfoUrl" href="${getMemberInfo_Url}"></a>
		<a id="getMemberListUrl" href="${getMemberList_Url}"></a>
		<a id="searchMemberUrl" href="${searchMember_Url}"></a>
		<a id="addCustomerUrl" href="${addCustomer_Url}"></a>
		<a id="updateCallLogUrl" href="${updateCallLog_Url}"></a>
		<a id="getIssueInfoUrl" href="${getIssueInfo_Url}"></a>
		<a id="searchIssueListUrl" href="${searchIssueList_url}"></a>
		<a id="cherryRefreshSessionUrl" href="${cherryRefreshSession_Url}"></a>
	</div>
	<form id="ccMainForm" method="post" class="inline">
		<input type="hidden" id="csrftokenCode" name="code" value="${(code)!}">
		<input type="hidden" id="ccBrandInfoId" name="ccBrandInfoId" value="${(ccBrandInfoId)!}"/>
		<input type="hidden" id="customerSysId" name="customerSysId" value="${(customerSysId)!}"/>
		<input type="hidden" id="memberInfoId" name="memberInfoId" value="${(memberInfoId)!}"/>
		<input type="hidden" id="customerCode" name="customerCode" value="${(customerSysId)!}"/>
		<input type="hidden" id="customerType" name="customerType" value="${(customerType)!}"/>
		<input type="hidden" id="isMember" name="isMember" value="${(isMember)!}"/>
		<input type="hidden" id="cno" name="cno" value="${(cno)!}"/>
		<input type="hidden" id="customerNumber" name="customerNumber" value="${(customerNumber)!}"/>
		<input type="hidden" id="customerNumberType" name="customerNumberType" value="${(customerNumberType)!}"/>
		<input type="hidden" id="customerAreaCode" name="customerAreaCode" value="${(customerAreaCode)!}"/>
		<input type="hidden" id="calltype" name="calltype" value="${(calltype)!}"/>
		<input type="hidden" id="callId" name="callId" value="${(callId)!}"/>
		<input type="hidden" id="taskId" name="taskId" value="${(taskId)!}"/>
		<div class="panel-header">
			<@getMemberSearchBox/>
		</div>
	</form>
	<input type="hidden" id="showPage" name="showPage" value="${(showPage)!}"/>
	<input type="hidden" id="pageIndex" name="pageIndex" value="200"/>
	<div class="clearfix" id="phoneInBox">
	</div>
	
	<#-- 会员来电（对应多条记录的情况）  -->
	<div class="hide" id="memberListBox">
		<@memberListPage/>
	</div>
	<#-- 会员来电（对应单条记录的情况）  -->
	<div class="hide" id="memberCallBox">
		<@memberCallPage/>
	</div>
	<#-- 非会员来电（有过来电的情况）  -->
	<div class="hide" id="nonMemberCallBox">
		<@nonMemberCallPage/>
	</div>
	<#-- 非会员来电（第一次来电的情况）  -->
	<div class="hide" id="newNonMemberCallBox">
		<@newNonMemberCallPage/>
	</div>
	
	<div class="hide" id="addCustomerInfoBox">
		<div id="addCustomerDiv" style="padding: 0 1em;">
		</div>
	</div>
	<div class="hide" id="getIssueListBox">
		<div id="getIssueDiv" style="padding: 0 1em;">
		</div>
	</div>
	<div class="hide" id="hideTextDiv">
		<@s.text name="mbcct.hideText" />
	</div>
	<div class="hide" id="showTextDiv">
		<@s.text name="mbcct.showText" />
	</div>
</body>
</@s.i18n>
</html>