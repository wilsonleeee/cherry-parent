<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/st/jcs/BINOLSTJCS11.js"></script>
<div class="hide">
	<span id="errorMsg5"><s:text name="STJCS11_errorMsg5" /></span>
</div>
<s:i18n name="i18n.st.BINOLSTJCS11">
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left"> 
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<span class="right">
				<s:url id="addUrl" action="BINOLSTJCS11_add"></s:url>
				<cherry:show domId="STJCS11ADD">
					<a class="add" href="${addUrl }" onclick="openWin(this);return false;">
						<span class="ui-icon icon-add"></span>
						<span class="button-text"><s:text name="STJCS11_add" /></span>
					</a>
				</cherry:show>
				<s:url id="importUrl" action="BINOLSTJCS11_importInit"></s:url>
				<cherry:show domId="STJCS11IMPORT">
					<a class="add" href="${importUrl }" onclick="openWin(this);return false;">
						<span class="ui-icon icon-import"></span>
						<span class="button-text"><s:text name="STJCS11_import" /></span>
					</a>
				</cherry:show>
			</span>
		</div>
	</div>
	<div id="actionResultDisplay"></div>
	<div id="errorMessage"></div>
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="errorDiv2" style="display: none">
		<div class="actionError">
			<ul>
				<li><span id="errorSpan2"></span></li>
			</ul>
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
		<cherry:form id="mainForm">
			<cherry:brand name="brandInfoId" />
			<div class="box">
				<div class="box-header">
					<strong> 
						<span class="ui-icon icon-ttl-search"></span>
						<s:text name="global.page.condition" />
					</strong>
					<input id="validFlag" type="checkbox" value="1" name="validFlag" checked="checked" />
					<s:text name="STJCS11_tips2" />
				</div>
				<div class="box-content clearfix">
					<div class="column" style="width: 49%;">
						<p>
							<label><s:text name="STJCS11_batchCode" /></label>
							<span><input name="batchCode" class="text" /></span>
						</p>
					</div>
					<div class="column last" style="width: 50%;">
						<p>
							<label><s:text name="STJCS11_counterName" /></label>
							<span><input id="departName" name="departName" class="text" /></span>
						</p>
					</div>
				</div>
				<p class="clearfix">
					<%-- 查询 --%>
					<button class="right search" type="submit" onclick="BINOLSTJCS11.search();return false;">
						<span class="ui-icon icon-search-big"></span> 
						<span class="button-text"><s:text name="global.page.search" /></span>
					</button>
				</p>
			</div>
		</cherry:form>
		<div id="section" class="section hide">
			<div class="section-header clearfix">
				<strong> 
					<span class="ui-icon icon-ttl-section-search-result"></span> 
					<s:text name="global.page.list" />
				</strong>
			</div>
			<div class="section-content">
				<div class="toolbar clearfix">
					<span class="left">
						<s:url id="disabledUrl" action="BINOLSTJCS11_disabled"></s:url>
						<cherry:show domId="STJCS11DISABLE">
						<a href="${disabledUrl}" class="add" onclick="BINOLSTJCS11.disable('0', this);return false;">
							<span class="ui-icon icon-disable"></span>
							<span class="button-text"><s:text name="global.page.disable" /></span>
						</a>
						</cherry:show>
						<cherry:show domId="STJCS11ENABLE">
						<a href="${disabledUrl}"  class="add" onclick="BINOLSTJCS11.disable('1', this);return false;">
							<span class="ui-icon icon-enable"></span>
							<span class="button-text"><s:text name="global.page.enable" /></span>
						</a>
						</cherry:show>
					</span>
					<span class="right"> 
						<%-- 设置列 --%> 
						<a class="setting"> 
							<span class="ui-icon icon-setting"></span> 
							<span class="button-text"><s:text name="global.page.colSetting" /></span>
						</a>
					</span>
				</div>
				<table id="dataTable" class="jquery_table" style="width:100%;">
					<thead>
						<tr>
							<th><input id="checkAll" type="checkbox" onclick="BINOLSTJCS11.checkRecord(this,'#dataTable');" /></th>
							<th><s:text name="No." /></th>
							<th><s:text name="STJCS11_batchCode" /></th>
							<th><s:text name="STJCS11_counterCode" /></th>
							<th><s:text name="STJCS11_counterName" /></th>
							<th><s:text name="STJCS11_startTime" /></th>
							<th><s:text name="STJCS11_endTime" /></th>
							<th><s:text name="STJCS11_author" /></th>
							<th><s:text name="STJCS11_comments" /></th>
							<th><s:text name="STJCS11_validFlag" /></th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />