<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/bs/emp/BINOLBSEMP07.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>

<s:i18n name="i18n.bs.BINOLBSEMP07">
<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
	</span>
    <span class="right">
    	<s:url action="BINOLBSEMP07_baInit" id="baModeInitUrl"></s:url>
    	<s:url action="BINOLBSEMP07_batchInit" id="batchModeInitUrl"></s:url>
    	<small><s:text name="EMP07_display_mode"></s:text>:</small>
    	<a class="display display-tree" title='<s:text name="EMP07_batchModel"></s:text>' onclick="BINOLBSEMP07.changeBaOrCoupon(this,'${batchModeInitUrl}');return false;"></a><a class="display display-table display-table-on" title='<s:text name="EMP07_baModel"></s:text>' onclick="BINOLBSEMP07.changeBaOrCoupon(this,'${baModeInitUrl}');return false;"></a>
    </span>
    </div>
</div>
<div id="baOrBatchId">
	<s:action name="BINOLBSEMP07_baInit" executeResult="true"></s:action>
</div>
<div class="hide" id="dialogInit"></div>
<div class="hide">
<!-- 隐藏国际化文本内容 -->
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>