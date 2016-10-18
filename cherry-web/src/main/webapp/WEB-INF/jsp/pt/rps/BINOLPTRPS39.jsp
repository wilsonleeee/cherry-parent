<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS39.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<style>
	.reminder_selected{
		background:#ff6d06;
	}
</style>
<s:i18n name="i18n.pt.BINOLPTRPS39">
<div id="errorMessage"></div>
<div id="actionResultDisplay"></div>
<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
	</span>
    <span class="right">
    	<input name="cargoType" class="hide" value="${cargoType}" id="cargoType"/>
    	<s:url action="BINOLPTRPS39_reminderInit" id="remModeInitUrl"></s:url>
    	<s:url action="BINOLPTRPS39_deliverInit" id="delModeInitUrl"></s:url>
    	<s:url action="/pt/BINOLPTRPS39_addDeliverInit" id="addDeliverInit_Url"></s:url>
    	<a id="addDeliverInitUrl" href="${addDeliverInit_Url }"></a>
    	<s:url action="/pt/BINOLPTRPS39_handleDeliver" id="handleDeliver_Url"></s:url>
    	<a id="handleDeliverUrl" href="${handleDeliver_Url }"></a>
    	<small id="displayModel"><s:text name="rps39_displayModel"/>:</small>
    	<span id="slideButton">
			<a class="add" onclick="BINOLPTRPS39.changeRemOrDel(this,'${remModeInitUrl}');return false;">
				<span class="ui-icon icon-search"></span>
				<span class="button-text reminder_selected"><s:text name="rps39_reverseReminder" /></span>
			</a>
	    	<a class="add" onclick="BINOLPTRPS39.changeRemOrDel(this,'${delModeInitUrl}');return false;">
				<span class="ui-icon icon-search"></span>
				<span class="button-text"><s:text name="rps39_receiverReminder" /></span>
			</a>
		</span>
		<%-- 
    	<a class="display display-tree" title='<s:text name="rps39_receiverReminder"></s:text>' onclick="BINOLPTRPS39.changeRemOrDel(this,'${delModeInitUrl}');return false;"></a>
    	<a class="display display-table display-table-on"  title='<s:text name="rps39_reverseReminder"></s:text>' onclick="BINOLPTRPS39.changeRemOrDel(this,'${remModeInitUrl}');return false;"></a>
    	 --%>
    </span>
    </div>
</div>
<div id="reminderInitId">
	<s:action name="BINOLPTRPS39_reminderInit" executeResult="true">
		<s:param name="cargoType">${cargoType}</s:param>
	</s:action>
</div>
<div class="hide" id="dialogInit"></div>
<div class="hide">
<div id="reminderTitle"><s:text name="rps39_remind"/></div>
<div id="operateResultTitle"><s:text name="rps39_operateResult"/></div>


<div id="reminderText">
<p class="message"><s:text name="rps39_reminderText"/></p>
</div>
<div id="deliverTips">
	<p class="message"><s:text name="rps39_deliverTips"/></p>
</div>
<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
<div id="dialogClose"><s:text name="global.page.close" /></div>
<div id="pushTitle"><s:text name="header.pushTitle"/></div>
<div id="existsTitle"><s:text name="rps39_exists"/></div>
<div id="notExistsTitle"><s:text name="rps39_notExists"/></div>
<!-- 隐藏国际化文本内容 -->
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>