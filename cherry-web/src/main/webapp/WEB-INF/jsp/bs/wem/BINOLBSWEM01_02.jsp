<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/wem/BINOLBSWEM01.js"></script>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSWEM01">
	<div class="section">
		<div class="section-content">
			<div class="toolbar clearfix">
				<cherry:form id="empForm" csrftoken="false" onsubmit="BINOLBSWEM01.assignFilter();return false;">
					<input id="employeeName" name="employeeName" onKeyup="BINOLBSWEM01.assignFilter();return false;"/>&nbsp;
		  			<a class="search" onclick="BINOLBSWEM01.assignFilter();return false;">
		  				<span class="ui-icon icon-search"></span>
		  				<span class="button-text"><s:text name="global.page.search" /></span>
		  			</a>
	  			</cherry:form>
			</div>
			<table id="empDataTable" class="jquery_table" width="100%">
				<thead>
					<tr>
						<th class="center">No</th>
						<th class="center"><s:text name="WEM01_employeeCode" /></th>
						<th class="center"><s:text name="WEM01_name" /></th>
						<th class="center"><s:text name="WEM01_mobilePhone" /></th>
						<th class="center"><s:text name="WEM01_organizationLevel" /></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
