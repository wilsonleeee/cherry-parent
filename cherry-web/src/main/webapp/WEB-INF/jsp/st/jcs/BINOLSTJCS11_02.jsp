<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/st/jcs/BINOLSTJCS11_02.js"></script>
<s:i18n name="i18n.st.BINOLSTJCS11">
<div class="panel ui-corner-all">
	<div id="div_main">
	    <div class="panel-header">
	        <div class="clearfix"> 
				<span class="breadcrumb left">	    
					<span class="ui-icon icon-breadcrumb"></span><s:text name="STJCS11_title1" />&nbsp;&gt;&nbsp;<s:text name="STJCS11_title2"></s:text>
				</span>
	        </div>
	    </div>
	    <div class="panel-content">
	 		<div class="section">
	 			<div class="section-header">
	 				<strong>
	 					<span class="ui-icon icon-ttl-section-info"></span>
	 					<s:text name="global.page.title" />
	 				</strong>
	 			</div>
	 			<div class="section-content">
	 				<table class="detail">
	 					<tr>
	 						<th><s:text name="STJCS11_counterCode" /></th>
	 						<td><s:property value="BBCounterInfo.departCode" /></td>
	 						<th><s:text name="STJCS11_counterName" /></th>
	 						<td><s:property value="BBCounterInfo.departName" /></td>
	 					</tr>
	 					<tr>
	 						<th><s:text name="STJCS11_startTime" /></th>
	 						<td><s:property value="BBCounterInfo.startDate" /> <s:property value="BBCounterInfo.startTime" /></td>
	 						<th><s:text name="STJCS11_endTime" /></th>
	 						<td><s:property value="BBCounterInfo.endDate" /> <s:property value="BBCounterInfo.endTime" /></td>
	 					</tr>
	 					<tr>
	 						<th><s:text name="STJCS11_batchCode" /></th>
	 						<td><s:property value="BBCounterInfo.batchCode" /></td>
	 						<th><s:text name="STJCS11_author" /></th>
	 						<td><s:property value="BBCounterInfo.author" /></td>
	 					</tr>
	 					<tr>
	 						<th><s:text name="STJCS11_comments" /></th>
	 						<td colspan="3"><s:property value="BBCounterInfo.comments" /></td>
	 					</tr>
	 				</table>
	 			</div>
	 		</div>
	 		<div class="center">
	 			<s:url id="editUrl" action="BINOLSTJCS11_edit">
	 				<s:param name="brandInfoId" value="brandInfoId" />
	 				<s:param name="BBCounterInfoId" value="BBCounterInfoId" />
	 			</s:url>
	 			<cherry:show domId="STJCS11EDIT">
	 			<button class="edit" onclick="BINOLSTJCS11_02.edit('${editUrl}');return false;">
	 				<span class="ui-icon icon-edit"></span>
	 				<span class="button-text"><s:text name="global.page.edit" /></span>
	 			</button>
	 			</cherry:show>
	 			<button class="close" onclick="window.close();return false;">
	 				<span class="ui-icon icon-close"></span>
	 				<span class="button-text"><s:text name="global.page.close" /></span>
	 			</button>
	 		</div>
	    </div>
	</div>
</div>
</s:i18n>
