<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM18_1.js?v=2017020703"></script>
<s:i18n name="i18n.mb.BINOLMBMBM18">
	<s:url id="Excel_url" value="BINOLMBMBM18_export"/>
	<s:text name="global.page.all" id="select_default"/>
	<div class="panel ui-corner-all">
		<div class="panel-header">
			<div class="clearfix">
				<span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="binolmbmbm18_importDetail"></s:text>(<s:text name="binolmbmbm18_billNo"></s:text>：<s:property value="billNo"/>)</span>
			</div>
		</div>
		<div id="errorMessage"></div>
		<div id="actionResultDisplay"></div>
		<div class="panel-content clearfix">
			<div class="box">
				<form id="detailMainForm"  class="inline" onsubmit="BINOLMBMBM18_1.search();return false;" >
					<s:hidden name="memImportId"></s:hidden>
					<s:hidden name="billNo"></s:hidden>
					<s:hidden name="impType"></s:hidden>
					<div class="box-header">
						<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
					</div>
					<div class="box-content clearfix">
						<div class="column" style="width:50%; height: auto;">
							<p>
								<label style="width:80px;"><s:text name="binolmbmbm18_importType" /></label>
          <span class="task-verified">
          <s:if test='impType=="1"'><span><s:text name="binolmbmbm18_import_Type_1"></s:text></span></s:if>
          <s:if test='impType=="2"'><span><s:text name="binolmbmbm18_import_Type_2"></s:text></span></s:if>
          </span>
							</p>
							<p>
								<label style="width:80px;"><s:text name="binolmbmbm18_resultFlag" /></label>
								<s:select list='#application.CodeTable.getCodes("1250")' listKey="CodeKey" listValue="Value" id="resultFlag" name="resultFlag"  onchange="BINOLMBMBM18_1.search();return false;"
										  headerKey="" cssStyle="width:120px;" headerValue="%{#select_default}"></s:select>
							</p>
						</div>
						<div class="column last" style="width:49%; height: auto;">
							<p>
								<label><s:text name="binolmbmbm18_memCode"/></label>
								<span><input type="text"  class="text" id="memberCode" value="" maxlength="40" name="memberCode"></span>
							</p>
						</div>
					</div>
					<p class="clearfix">
						<button class="right search" onclick="BINOLMBMBM18_1.search();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
					</p>
				</form>
			</div>
			<div class="section hide" id="detailInfo">
				<div class="section-header">
					<strong>
						<span class="ui-icon icon-ttl-section-search-result"></span>
							<%-- 查询结果一览 --%>
						<s:text name="binolmbmbm18_resultShow"/>
					</strong>
					<span id="headInfo"  style="margin: 0px 20px;" ></span>
				</div>
				<div class="section-content" id="result_list">
					<div class="toolbar clearfix">
						<cherry:show domId="MBMBM18EXPORT">
							<a id="exportPointInfo" class="export left" onclick="BINOLMBMBM18_1.exportExcel('${Excel_url}');return false;">
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><s:text name="binolmbmbm18_excelExport"/></span>
							</a>
						</cherry:show>
              	<span class="right">
              	<span class="bg_yew"><span class="highlight" style="line-height:20px;"><s:text name="binolmbmbm18_moreShow"/></span></span>
              	<a class="setting"><span class="ui-icon icon-setting"></span>
              	<span class="button-text">
              	<%-- 设置列 --%>
              	<s:text name="binolmbmbm18_column"/></span></a></span>
					</div>
					<s:if test='impType=="1"'>
						<table  id="detailTable_1">
							<thead>
							<tr>
								<th><s:text name="NO."></s:text></th>
								<th><s:text name="binolmbmbm18_memCode"></s:text></th>
								<th><s:text name="binolmbmbm18_memName"></s:text></th>
								<th><s:text name="binolmbmbm18_memPhone" /></th>
								<th><s:text name="binolmbmbm18_mobilePhone" /></th>
								<th><s:text name="binolmbmbm18_sex"></s:text></th>
								<th><s:text name="binolmbmbm18_province"></s:text></th>
								<th><s:text name="binolmbmbm18_city"></s:text></th>
								<th><s:text name="binolmbmbm18_address"></s:text></th>
								<th><s:text name="binolmbmbm18_postcode" /></th>
								<th><s:text name="binolmbmbm18_birthday"></s:text></th>
								<th><s:text name="binolmbmbm18_ageGetMethod"></s:text></th>
								<th><s:text name="binolmbmbm18_eMail"></s:text></th>
								<th><s:text name="binolmbmbm18_Granddate"></s:text></th>
								<th><s:text name="binolmbmbm18_bacode" /></th>
								<th><s:text name="binolmbmbm18_cardCounter"></s:text></th>
								<th><s:text name="binolmbmbm18_memLevel"></s:text></th>
								<th><s:text name="binolmbmbm18_initTotalAmount"></s:text></th>
								<th><s:text name="binolmbmbm18_referrer"></s:text></th>
								<th><s:text name="binolmbmbm18_isReceiveMsg"></s:text></th>
								<th><s:text name="binolmbmbm18_testMemFlag"></s:text></th>
								<th><s:text name="binolmbmbm18_channelCode"></s:text></th>
								<th><s:text name="binolmbmbm18_returnVisit"></s:text></th>
								<th><s:text name="binolmbmbm18_skinType"></s:text></th>
								<th><s:text name="binolmbmbm18_profession"></s:text></th>
								<th><s:text name="binolmbmbm18_income"></s:text></th>
								<th><s:text name="binolmbmbm18_memo1"></s:text></th>
								<th><s:text name="binolmbmbm18_resultFlag"></s:text></th>
								<th><s:text name="binolmbmbm18_common"></s:text></th>
							</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</s:if>
					<s:else>
						<table id="detailTable_2">
							<thead>
							<tr>
								<th><s:text name="NO."></s:text></th>
								<th><s:text name="binolmbmbm18_memCode"></s:text></th>
								<th><s:text name="binolmbmbm18_memName"></s:text></th>
								<th><s:text name="binolmbmbm18_memPhone" /></th>
								<th><s:text name="binolmbmbm18_mobilePhone" /></th>
								<th><s:text name="binolmbmbm18_sex"></s:text></th>
								<th><s:text name="binolmbmbm18_province"></s:text></th>
								<th><s:text name="binolmbmbm18_city"></s:text></th>
								<th><s:text name="binolmbmbm18_address"></s:text></th>
								<th><s:text name="binolmbmbm18_postcode" /></th>
								<th><s:text name="binolmbmbm18_birthday"></s:text></th>
								<th><s:text name="binolmbmbm18_eMail"></s:text></th>
								<th><s:text name="binolmbmbm18_bacode" /></th>
								<th><s:text name="binolmbmbm18_cardCounter"></s:text></th>
								<th><s:text name="binolmbmbm18_initTotalAmount"></s:text></th>
								<th><s:text name="binolmbmbm18_referrer"></s:text></th>
								<th><s:text name="binolmbmbm18_isReceiveMsg"></s:text></th>
								<th><s:text name="binolmbmbm18_testMemFlag"></s:text></th>
								<th><s:text name="binolmbmbm18_channelCode"></s:text></th>
								<th><s:text name="binolmbmbm18_returnVisit"></s:text></th>
								<th><s:text name="binolmbmbm18_skinType"></s:text></th>
								<th><s:text name="binolmbmbm18_profession"></s:text></th>
								<th><s:text name="binolmbmbm18_income"></s:text></th>
								<th><s:text name="binolmbmbm18_memo1"></s:text></th>
								<th><s:text name="binolmbmbm18_resultFlag"></s:text></th>
								<th><s:text name="binolmbmbm18_common"></s:text></th>
							</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</s:else>
				</div>
			</div>
		</div>
	</div>
</s:i18n>
<div class="hide">
	<s:url action="BINOLMBMBM18_detailSearch" id="detailSearchUrl"></s:url>
	<a href="${detailSearchUrl}" id="detailSearch_Url"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
