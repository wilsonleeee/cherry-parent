<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/man/BINOLMOMAN08.js"></script>
<style>
/* Firefox hack */
@
-moz-document url-prefix (){
	td {height: 26px;
}
}
</style>
<s:i18n name="i18n.mo.BINOLMOMAN08">
    <div class="hide">
        <s:url id="add_url" value="/mo/BINOLMOMAN08_add"/>
        <a id="addUrl" href="${add_url}"></a>
    </div>
	<div class="hide">
		<s:url id="search_url" value="/mo/BINOLMOMAN08_search" />
		<a id="searchUrl" href="${search_url}"></a>
	</div>
	<div class="hide">
		<s:url id="editPaper_url" value="/mo/BINOLMOMAN08_edit" />
		<a id="editPaper" href="${editPaper_url}"></a>
	</div>
	<div class="hide">
		<s:url id="save_url" value="/mo/BINOLMOMAN08_save" />
		<a id="saveUrl" href="${save_url}"></a>
	</div>
	<div class="panel-header">
		<%-- --%>
		<div class="clearfix">
			<span class="breadcrumb left"> <jsp:include
					page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span> <span class="right"> </span>
			<a class="add right" id="addPaper" target="_blank">
		        <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="MAN08_add"/></span>
		    </a>
		</div>
	</div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="mainForm" class="inline"
				onsubmit="BINOLMOMAN08.search(); return false;">
				<%-- IE只有一个text框 ，按 Enter键跳转其他页面hack --%>
				<input class="hide" />
				<div class="box-header">
					<strong> <span class="ui-icon icon-ttl-search"></span> <%-- 查询条件 --%>
						<s:text name="MAN08_condition" />
					</strong>
				</div>
				<div class="box-content clearfix">
					<div class="column" style="width: 50%;">
						<p>
							<%-- 配置项代码 --%>
							<label><s:text name="MAN08_configCode" /></label>
							<s:textfield name="configCode" cssClass="text" maxlength="30" />
						</p>
					</div>
					<div class="column last" style="width: 49%;">
						<p>
							<%-- 配置项说明 --%>
							<label><s:text name="MAN08_configNote" /></label>
							<s:textfield name="configNote" cssClass="text" maxlength="30" />
						</p>
					</div>
				</div>
				<p class="clearfix">
					<%--<cherry:show domId="MOMAN0801QUERY">--%>
					<button class="right search" type="submit"
						onclick="BINOLMOMAN08.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<%-- 查询 --%>
						<span class="button-text"><s:text name="MAN08_search" /></span>
					</button>
					<%--</cherry:show>--%>
				</p>
			</cherry:form>
		</div>
		<div id="section" class="section hide">
			<div class="section-header">
				<strong> <span
					class="ui-icon icon-ttl-section-search-result"></span> <%-- 查询结果一览 --%>
					<s:text name="MAN08_results_list" />
				</strong>
			</div>

			<div class="section-content">
				<div class="toolbar clearfix">
					<span class="left"> </span> <span class="right"></span>
				</div>
				<table id="dataTable" cellpadding="0" cellspacing="0" border="0"
					class="jquery_table" width="100%">
					<thead>
						<tr>
							<th><s:text name="MAN08_num" /></th>
							<%-- No. --%>
							<th><s:text name="MAN08_configCode" /></th>
							<%-- 配置项代码  --%>
							<th><s:text name="MAN08_configNote" /></th>
							<%-- 配置项说明 --%>
							<th><s:text name="MAN08_configType" /></th>
							<%-- 配置类型 --%>
							<th><s:text name="MAN08_configValue" /></th>
							<%-- 配置项内容 --%>
							<th><s:text name="MAN08_operate" /></th>
							<%-- 操作 --%>
						</tr>
					</thead>
				</table>
			</div>
			<div class="hide" id="dialogInit"></div>
			<div class="hide">
			 	<div id="addTitle">
			 		<s:text name="MAN08_add" />
			 	</div>
				<div id="updateTitle">
					<s:text name="MAN08_updateTitle" />
				</div>
				<div id="confirm">
					<s:text name="MAN08_yes" />
				</div>
				<div id="cancel">
					<s:text name="MAN08_cancel" />
				</div>
			</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>