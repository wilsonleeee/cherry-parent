<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/wy/wyl/BINOLWYWYL01.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/cherry_timepicker.css" type="text/css">
<s:i18n name="i18n.wp.BINOLWYWYL01">
<div class="hide">
	<s:url id="s_searchURL" value="/wy/BINOLWYWYL01_search" />
	<a id="searchUrl" href="${s_searchURL}"></a>
</div>
<div class="panel-header">
	<div class="clearfix"> 
		<span class="breadcrumb left">      
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
	</div>
</div>
<div id="actionResultDisplay"></div>
<%-- ================== 错误信息提示 START ======================= --%>
<div id="errorDiv2" class="actionError" style="display:none">
	<ul>
		<li><span id="errorSpan2"></span></li>
	</ul>
</div>
<%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content">
	<div class="box">
		<s:text name="wywyl01.selectAll" id="selectAll"/>
		<cherry:form id="mainForm" class="inline">
			<div class="box-header">
			<strong>
            	<span class="ui-icon icon-ttl-search"></span>
            	<s:text name="wywyl01.condition"/>
            </strong>
			</div>
			<div class="box-content clearfix">
				<div class="column" style="height:120px;width:50%">
		            <p>
						<s:text name="wywyl01.memberName" />
						<span><input id="memberName" name="memberName" class="text" maxlength="20" value="" /></span>
	                </p>
	                <p>
						<s:text name="wywyl01.birthday" />
						<span>
							<select id="birthDayMonthQ" name="birthDayMonthQ" style="width:50px;height:20px;">
								<option value=""></option>
							</select>
							<s:text name="wywyl01.month" />
							<select id="birthDayDateQ" name="birthDayDateQ" style="width:50px;height:20px;">
								<option value=""></option>
							</select>
							<s:text name="wywyl01.day" />
						</span>
	                </p>
                    <p>
						<s:text name="wywyl01.activityType" />
						<input id="campaignId" name="campaignId" type="hidden" />
           	  			<span><input id="subCampaignName" name="subCampaignName" maxlength="30" class="text" /></span>
                    </p>
                    <p>
						<s:text name="wywyl01.city" />
						<input id="cityId" name="cityId" type="hidden" />
           	  			<span><input id="city" name="city" maxlength="20" class="text" /></span>
                    </p>
				</div>
				<div class="column last" style="width:49%">      
					<p>
						<s:text name="wywyl01.mobilePhone" />
						<span><input id="mobilePhone" name="mobilePhone" class="text" maxlength="20" value="" /></span>
					</p>
                    <p>
                    	<s:text name="wywyl01.gender" />
                    	<s:select id="gender" name="gender" list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" />
                    </p>
					<p>
						<s:text name="wywyl01.state" />
                    	<s:select id="state" name="state" list='#application.CodeTable.getCodes("1325")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#selectAll}" />
					</p>
					<p>
						<s:text name="wywyl01.applyGetDate" />
                    	<span><s:textfield id="applyGetDate" name="applyGetDate" cssClass="date" cssStyle="width:90px"></s:textfield></span>
					</p>
				</div>
			</div>
			<p class="clearfix">
				<button class="right search"  onclick="BINOLWYWYL15.search();return false;">
					<span class="ui-icon icon-search-big"></span>
					<span class="button-text"><s:text name="wywyl01.search"/></span>
				</button>
			</p>
		</cherry:form>
	</div>
	<div class="section">
		<div class="section-header">
			<strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="wywyl01.detail"/></strong>
		</div>
		<div class="section-content">
			<div class="toolbar clearfix">
				<span class="right">
					<a class="setting">
                           <span class="ui-icon icon-setting"></span>
           	            <span class="button-text">
                            <s:text name="wywyl01.colSetting"/>
                           </span>
					</a>
				</span>
			</div>
			<div style="width:100%;overflow-x:scroll;">
				<table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
					<thead>
						<tr>
							<th class="tableheader" width="5%"><s:text name="wywyl01.rowNumber"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl01.memberName"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl01.mobilePhone"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl01.birthday"/></th>
							<th class="tableheader" width="5%"><s:text name="wywyl01.gender"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl01.activityType"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl01.applyGetDate"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl01.city"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl01.state"/></th>
							<th class="tableheader" width="10%"><s:text name="wywyl01.act"/></th>
							<th style="display:none">
						</tr>
					</thead>
					<tbody id="databody">
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
</s:i18n>
<%-- ================== dataTable共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通END  ======================= --%>