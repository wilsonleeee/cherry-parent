<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/st/jcs/BINOLSTJCS11_03.js"></script>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/cherry_timepicker.css" type="text/css">
<s:i18n name="i18n.st.BINOLSTJCS11">
<div class="hide">
	<div id="timeOnlyTitle"><s:text name="STJCS11_timeOnlyTitle" /></div>
	<div id="currentText"><s:text name="STJCS11_currentText" /></div>
	<div id="closeText"><s:text name="STJCS11_closeText" /></div>
</div>
<div class="panel ui-corner-all">
	<div id="div_main">
	    <div class="panel-header">
	        <div class="clearfix"> 
				<span class="breadcrumb left">	    
					<span class="ui-icon icon-breadcrumb"></span><s:text name="STJCS11_title1" />&nbsp;&gt;&nbsp;<s:text name="STJCS11_title3"></s:text>
				</span>
	        </div>
	    </div>
	    <div class="panel-content">
	    	<div id="actionResultDisplay"></div>
	    	<form id="mainForm">
	    		<s:if test="BBCounterInfo == null">
			    	<cherry:brand name="brandInfoId" />
			    </s:if>
			    <s:else>
					<span><input name="brandInfoId" value="<s:property value="BBCounterInfo.brandInfoId" />" class="hide" /></span>
					<span><input name="BBCounterInfoId" value="<s:property value="BBCounterInfo.BBCounterInfoId" />" class="hide" /></span>
					<span><input id="organizationId" name="organizationId" value="<s:property value="BBCounterInfo.organizationId" />" class="hide" /></span>
					<span><input name="validFlag" value="<s:property value="BBCounterInfo.validFlag" />" class="hide" /></span>
					<span><input id="batchCode" name="batchCode" value="<s:property value="BBCounterInfo.batchCode" />" class="hide" /></span>
				</s:else>
		 		<div class="section">
		 			<div class="section-header">
		 				<strong>
		 					<span class="ui-icon icon-ttl-section-info"></span>
		 					<s:text name="global.page.title" />
		 					<s:if test="BBCounterInfo == null">
		 						<span class="highlight">（<s:text name="STJCS11_tips1" />）</span>
		 					 </s:if>
		 				</strong>
		 			</div>
		 			<div class="section-content">
		 				<table class="detail">
		 					<s:if test="BBCounterInfo == null">
						    	<tr>
			 						<th><s:text name="STJCS11_batchCode" /><span class="highlight">*</span></th>
			 						<td><span><input id="batchCode" name="batchCode" class="text" /></span></td>
			 						<th><s:text name="STJCS11_counterName" /><span class="highlight">*</span></th>
			 						<td>
			 							<span id="counterSpan"></span>
			 							<span><input id="organizationId" name="organizationId" class="hide" />&nbsp;</span>
			 							<s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
			 							<a class="search" onclick="BINOLSTJCS11_03.popCounterDialog('${searchCounterInitUrl}');return false;">
			 								<span class="ui-icon icon-search"></span>
			 								<span class="button-text"><s:text name="global.page.select" /></span>
			 							</a>
			 						</td>
			 					</tr>
						    </s:if>
						    <s:else>
								<tr>
			 						<th><s:text name="STJCS11_counterCode" /></th>
			 						<td><s:property value="BBCounterInfo.departCode" /></td>
			 						<th><s:text name="STJCS11_counterName" /></th>
			 						<td><s:property value="BBCounterInfo.departName" /></td>
			 					</tr>
							</s:else>
		 					<tr>
		 						<th><s:text name="STJCS11_startTime" /><span class="highlight">*</span></th>
		 						<td>
		 							<span>
		 								<input class="date" id="startDate" name="startDate" value="<s:property value="BBCounterInfo.startDate" />" />
		 							</span>
		 							<span style="margin-left:10px">
		 								<input class="date" id="startTime" name="startTime" value="<s:property value="BBCounterInfo.startTime" />" />
		 							</span>
		 						</td>
		 						<th><s:text name="STJCS11_endTime" /><span class="highlight">*</span></th>
		 						<td>
		 							<span>
		 								<input class="date" id="endDate" name="endDate" value="<s:property value="BBCounterInfo.endDate" />" />
		 							</span>
		 							<span style="margin-left:10px">
		 								<input class="date" id="endTime" name="endTime" value="<s:property value="BBCounterInfo.endTime" />" />
		 							</span>
		 						</td>
		 					</tr>
		 					<s:if test="BBCounterInfo != null">
						    	<tr>
			 						<th><s:text name="STJCS11_batchCode" /></th>
			 						<td><s:property value="BBCounterInfo.batchCode" /></td>
			 						<th><s:text name="STJCS11_author" /></th>
			 						<td><s:property value="BBCounterInfo.author" /></td>
			 					</tr>
						    </s:if>
		 					<tr>
		 						<th><s:text name="STJCS11_comments" /></th>
		 						<td colspan="3">
		 							<span>
		 								<input id="comments" name="comments" value="<s:property value="BBCounterInfo.comments" />"  style="width: 700px;height: 30px;" maxlength="200" class="text" />
		 							</span>
		 						</td>
		 					</tr>
		 				</table>
		 			</div>
		 		</div>
	 		</form>
	 		<div class="center">
	 			<s:url id="saveUrl" action="BINOLSTJCS11_save" />
	 			<button class="save" onclick="BINOLSTJCS11_03.doSave('${saveUrl}');return false;">
	 				<span class="ui-icon icon-save"></span>
	 				<span class="button-text"><s:text name="global.page.save" /></span>
	 			</button>
	 			<button class="close" onclick="window.close();return false;">
	 				<span class="ui-icon icon-close"></span>
	 				<span class="button-text"><s:text name="global.page.close" /></span>
	 			</button>
	 		</div>
	    </div>
	</div>
</div>
</s:i18n>
