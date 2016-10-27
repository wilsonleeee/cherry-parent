<%-- 盘点机软件版本信息查询 --%>
<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	  type="text/css">
<script type="text/javascript" src="/Cherry/js/mo/mup/BINOLMOMUP01.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.mo.BINOLMOMUP01">
	<s:url id="search_url" value="/mo/BINOLMOMUP01_search"></s:url>
	<div class="hide">
		<s:text name="global.page.select" id="select_default" />
		<input type="hidden" id="_select_default" value="${select_default }" />
		<a id="searchUrl" href="${search_url}"></a>
		<s:url id="add_url" action="BINOLMOMUP04_init"/>
	</div>
	<div class="panel-header">
			<%-- 盘点机软件版本信息查询--%>
		<div class="clearfix">
			<span class="breadcrumb left"> <jsp:include
					page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<span class="right"> 
			    
	     	 	<a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
					<span class="button-text"><s:text name="MUP01_add"/></span>
					<span class="ui-icon icon-add"></span>
				</a>
			     
		    </span>
		</div>
			<%-- 添加按钮 --%>

	</div>
	<div id="errorMessage"></div>
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="errorDiv2" class="actionError" style="display: none">
		<ul>
			<li><span id="errorSpan2"></span></li>
		</ul>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
		<cherry:form id="main_Form" class="inline">
		</cherry:form>
		<form id="mainForm1" class="inline">
			<div class="box">
				<div class="box-header"></div>
				<p class="clearfix">
					<button class="right search" type="submit"
							onclick="search();return false;">
						<span class="ui-icon icon-search-big"></span>
							<%-- 查询 --%>
						<span class="button-text"><s:text name="MUP01_search" /></span>
					</button>
				</p>
			</div>
		</form>
		<div id="section1" class="section hide">
			<div class="section-header">
				<strong> <span
						class="ui-icon icon-ttl-section-search-result"></span> <%-- 查询结果一览 --%>
					<s:text name="MUP01_results_list" />
				</strong>
			</div>
			<div class="section-content">
				<table id="dataTable1" cellpadding="0" cellspacing="0" border="0"
					   class="jquery_table" width="100%">
					<thead>
					<tr>
							<%-- No. --%>
						<th><s:text name="MUP01_num" /></th>
							<%-- U盘序列号  --%>
						<th><s:text name="MUP01_version" /></th>
							<%-- 员工名称  --%>
						<th><s:text name="MUP01_downloadUrl" /></th>
							<%-- md5Key  --%>
						<th><s:text name="MUP01_md5Key" /></th>
							<%-- 放开更新时间  --%>
						<th><s:text name="MUP01_openUpdateTime" /></th>
					</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>

	<%-- ================== 关键时间段DIV  END  ======================= --%>
</s:i18n>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
