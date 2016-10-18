<#-- struts 标签库 -->
<#global s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"] />
<#-- 自定义标签库 -->
<#global c=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"] />
<#-- 全局环境语言 -->
<#global language=Session.language />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>WITPOS店务通</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">
<link rel="stylesheet" href="/Cherry/css/common/sunny/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/print.css" type="text/css" media="print">
<link rel="stylesheet" href="/Cherry/css/common/menuleft.css" type="text/css">
<link rel="stylesheet" href="/Cherry/css/common/datatable.css" type="text/css">
<link rel="stylesheet" href="/Cherry/css/cherry/cherry.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/autocomplete/jquery.autocomplete.css" type="text/css">
<!--[if lt IE 8]><link rel="stylesheet" href="/Cherry/css/common/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
<!--[if lte IE 8]><link rel="stylesheet" href="/Cherry/css/common/blueprint/ie-corner-fix.css" type="text/css" media="screen, projection"><![endif]-->

<script type="text/javascript" src="/Cherry/js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-1.8.22.custom.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.form.min.js"></script>
<script type="text/javascript" src="/Cherry/js/common/commonAjax.js"></script>

<script type="text/javascript" src="/Cherry/js/common/cherry.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/json2.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.jeditable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.cluetip.js"></script>
<script type="text/javascript" src="/Cherry/plugins/autocomplete/jquery.autocomplete.min.js"></script>
<#-- dataTable相关js -->
<script type="text/javascript" src="/Cherry/js/lib/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/ColVis.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/FixedColumns.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDataTable.js"></script>
<script type="text/javascript" src="/Cherry/plugins/jquery-validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/Cherry/plugins/jquery-validate/additional-methods.js"></script>  
<script type="text/javascript" src="/Cherry/js/common/IEcssrepair.js"></script>
<#if language == "zh_CN">
<script type="text/javascript" src="/Cherry/plugins/jquery-validate/localization/messages_cn.js"></script>
<#elseif language == "en_US">
<script type="text/javascript" src="/Cherry/plugins/jquery-validate/localization/messages_en.js"></script>
</#if>
</head>
<div id="ERROR_MSG" class="hide">
<@s.if test="hasActionErrors()">
	<div class="actionError" id="actionResultDiv">
	  	<@s.actionerror/>
	</div>
</@s.if>
<@s.if test="hasActionMessages()">
	<div class="actionSuccess" id="actionResultDiv">
	  	<@s.actionmessage/>
	</div>
</@s.if>
<@s.if test="hasFieldErrors()">
	<div id="fieldErrorDiv" Style="display:none">
		<input name="hidFieldErrorMsg" value="<@s.property value='fieldErrors'/>" />		
	</div>	
</@s.if>
</div>

<#-- 判断session失效DUMMY_URL -->
     <@s.url id="DUMMY_URL" action="BINOLCM05_isSessionTimeout" namespace="/common"/>
     <div >
     	<a id="DUMMYURL" href="${DUMMY_URL}"></a>
     </div>

<#-- CSV导出共通-->
<div id="pushMsgDialog" class="hide">
    <input id="kickUserText" type="hidden" value='<@s.text name="header.kickUser" />'/>
    <input id="pushTitleText" type="hidden" value='<@s.text name="header.pushTitle"/>'/>
    <input id="confirmText" type="hidden" value='<@s.text name="global.page.ok"/>'/>
    <input id="pushDownloadButton" type="hidden" value='<@s.text name="global.page.download"/>'/>
    <input id="pushCancelButton" type="hidden" value='<@s.text name="global.page.cancle"/>'/>
    <input id="pushCloseButton" type="hidden" value='<@s.text name="global.page.dialogClose"/>'/>
</div>