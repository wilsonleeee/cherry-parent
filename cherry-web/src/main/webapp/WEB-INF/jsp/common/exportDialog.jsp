<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/common/commExport.js"></script> 
<div id="exportDialog" class="hide">
	<form id="exportForm" action="/Cherry/common/BINOLCM99_export">
		<div id="exportMsg"></div>
		<div id="export_mainDiv" class="center clearfix" style="margin-top:50px">
			<input type="hidden" id="e_csrftoken" value="" name="csrftoken"/>
			<input type="hidden" id="e_params" value="" name="params"/>
			<input type="hidden" id="e_pageId" value="" name="pageId"/>
			<input type="hidden" id="e_reportMode" value="" name="reportMode"/>
			<label><s:text name="report.name" /></label><input name="exportName" class="text"/>
			<label><s:text name="report.type" /></label>
			<s:select name="exportType" list="#application.CodeTable.getCodes('1147')" listKey="CodeKey" listValue="Value"/>
	     </div>
	     <div class="center clearfix" style="margin-top:15px">
		    <button type="button" class="close" onclick="removeDialog('#exportDialog');return false;">
		    	<span class="ui-icon icon-cancel"></span>
		    	<span class="button-text"><s:text name="global.page.cancle" /></span>
		    </button>
		    <button class="confirm" type="submit">
		    	<span class="ui-icon icon-confirm"></span>
		    	<span class="button-text"><s:text name="global.page.ok" /></span>
		    </button>
		</div>
	</form>
	<!--<a id="exportFileLink" class="hide" href="" target=""></a>-->
</div>
<span id ="BillExportTitle" style="display:none"><s:text name="global.page.BillExportTitle"/></span><%--报表导出 --%>