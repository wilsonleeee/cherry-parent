<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/blueprint/webpos.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-timepicker-addon.js"></script>
<script  type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery.hotkeys.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/plugins/chart/jquery.flot.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/plugins/chart/jquery.flot.pie.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/plugins/chart/jquery.flot.stack.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/plugins/chart/excanvas.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/mbm/BINOLWPMBM01.js?V=20161020"></script>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/cherry_timepicker.css" type="text/css">
<script type="text/javascript">
$(function(){
	document.onhelp=function(){return false};  
	window.onhelp=function(){return false}; 
});
</script>
<body>
<s:if test='null != counterCode && !"".equals(counterCode)'>
	<div id="webpos_main">
		<jsp:include page="/WEB-INF/jsp/wp/sal/BINOLWPSAL02.jsp" flush="true" />
	</div>
	<div id="member_main" class="hide">
	</div>
</s:if>
<s:else>
	<div id="webpos_main">
		<jsp:include page="/WEB-INF/jsp/wp/sal/BINOLWPSAL09.jsp" flush="true" />
	</div>
</s:else>
<!-- 此段参数为打印功能必须参数 -->
<div id="print_param_hide" class="hide">
	<!-- 用于查找打印模板文件名 -->
	<input type="hidden" id="pageId" name="pageId" value="BINOLWPSAL07"/>
	<!-- 销售单据ID -->
	<input type="hidden" id="billId" name="billId" value=""/>
</div>
<div id="messageBoxDiv" class="center hide" style="background:#fffde5;height:100px;width:200px;position:absolute;">操作成功</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
</body>

