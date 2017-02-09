<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>WITPOS店务通</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<!-- 设置缓存时间为60*60*24*7=7天 Pragma是http1.0协议下的参数，Cache-Control属于http1.1 -->
<meta http-equiv="Pragma" content="max-age=604800">
<meta http-equiv="Cache-Control" content="max-age=604800">
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/sunny/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/blueprint/print.css" type="text/css" media="print">
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/menuleft.css" type="text/css">
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/datatable.css" type="text/css">
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/cherry.css" type="text/css">
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/plugins/autocomplete/jquery.autocomplete.css" type="text/css">

<!--[if lt IE 8]><link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
<!--[if lte IE 8]><link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/blueprint/ie-corner-fix.css" type="text/css" media="screen, projection"><![endif]-->

<%
String cherry_brandcss = (String)session.getAttribute("cherry_brandcss");
if(cherry_brandcss != null && !"".equals(cherry_brandcss)) {
	out.print("<link rel=\"stylesheet\" href=\"/"+application.getAttribute("CHERRY_CONTEXT_PATH")+"/css/cherry/"+cherry_brandcss+"\" type=\"text/css\">");
}
%>

<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-1.8.22.custom.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery.form.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/commonAjax.js"></script>

<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherry.js?v=20170207"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/json2.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery.jeditable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery.cluetip.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/plugins/autocomplete/jquery.autocomplete.min.js"></script>
<%-- dataTable相关js --%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/ColVis.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/FixedColumns.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/plugins/jquery-validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/plugins/jquery-validate/additional-methods.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/plugins/jquery-validate/localization/messages.js"></script>
<%-- 圆角js --%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery.corner.min.js"></script>
<%-- 系统消息推送相关js --%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery.atmosphere.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryPush.js"></script>

</head>
	<%-- 判断session失效DUMMY_URL --%>
     <s:url id="DUMMY_URL" action="BINOLCM05_isSessionTimeout" namespace="/common"/>
     <div >
     	<a id="DUMMYURL" href="${DUMMY_URL}"></a>
     </div>
     <span id="CHERRY_LANGUAGE" class="hide"><%=String.valueOf(session.getAttribute("cherry_language"))%></span>

<div id="pushMsgDialog" class="hide">
    <input id="kickUserText" type="hidden" value='<s:text name="header.kickUser"></s:text>'/>
    <input id="pushTitleText" type="hidden" value='<s:text name="header.pushTitle"></s:text>'/>
    <input id="confirmText" type="hidden" value='<s:text name="global.page.ok"></s:text>'/>
    <input id="pushDownloadButton" type="hidden" value='<s:text name="global.page.download"></s:text>'/>
    <input id="pushCancelButton" type="hidden" value='<s:text name="global.page.cancle"></s:text>'/>
    <input id="pushCloseButton" type="hidden" value='<s:text name="global.page.dialogClose"></s:text>'/>
</div>