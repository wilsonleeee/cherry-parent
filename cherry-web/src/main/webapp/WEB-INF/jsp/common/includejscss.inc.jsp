<link rel="stylesheet" href="/Cherry/css/common/sunny/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/print.css" type="text/css" media="print">
<link rel="stylesheet" href="/Cherry/css/common/menuleft.css" type="text/css">
<link rel="stylesheet" href="/Cherry/css/common/datatable.css" type="text/css">
<link rel="stylesheet" href="/Cherry/css/cherry/cherry.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/autocomplete/jquery.autocomplete.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<!--[if lt IE 8]><link rel="stylesheet" href="/Cherry/css/common/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
<!--[if lte IE 8]><link rel="stylesheet" href="/Cherry/css/common/blueprint/ie-corner-fix.css" type="text/css" media="screen, projection"><![endif]-->

<script type="text/javascript" src="/Cherry/js/lib/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.form.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-1.8.22.custom.min.js"></script>
<script type="text/javascript" src="/Cherry/js/common/commonAjax.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherry.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryMenu.js"></script>

<script type="text/javascript" src="/Cherry/js/lib/jquery.jeditable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.cluetip.js"></script>
<script type="text/javascript" src="/Cherry/plugins/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery-ztree-2.1.js"></script>
<script type="text/javascript" src="/Cherry/js/common/IEcssrepair.js"></script>
<%-- dataTable--%>

<script type="text/javascript" src="/Cherry/plugins/jquery-validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/Cherry/plugins/jquery-validate/additional-methods.js"></script>
<%
	String  language =  String.valueOf(session.getAttribute("language"));
	if("zh_CN".equals(language)){
%>
<script type="text/javascript" src="/Cherry/plugins/jquery-validate/localization/messages_cn.js"></script>
<% 
	}else if("en_US".equals(language)){
%>
<script type="text/javascript" src="/Cherry/plugins/jquery-validate/localization/messages_en.js"></script>
<% 
	}
%>
