<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<div class="hide">
	<s:url id="downLoad_Url" value="/download/销售产品导入模版.xls"/>
</div>
<div id ="productImportDialog">
	<div id="errorMessage"></div>
	<div id="errMsg">
    	<input type="hidden" id="errMsgNoFile" value='<s:text name="EMO00010"/>'/><%-- 请选择上传文件。 --%>
    </div>
    <p></p>
	<table class="detail">
		<tr>
			<th><span class="highlight">※</span><s:text name="global.page.downloadPrompt"/></th>
			<td>
				<a href="${downLoad_Url}"><span style="color:#3366FF"><s:text name="global.page.modelDownload"/></span></a>
			</td>
		</tr>
		<tr>
			<th><s:text name="global.page.productImport"/></th>
			<td>
				<input type="text" id="pathExcel" name="pathExcel" class="input_text">
				<input type="button" value="<s:text name="global.page.browse"/>">
				<input type="file" id="upExcel" name="upExcel" onchange="$('#pathExcel').val($(this).val());return false;" size="40" style="height:auto;" class="input_file">
				<input type="button" id="upload" name="upload" value="<s:text name="global.page.importExcel"/>" onclick="BINOLSTSFH14.saleProductUpload();return false;">
				<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
			</td>
		</tr>
		<tr>
			<th><s:text name="global.page.importRepeat"/></th>
			<td>
				<input id="importRepeatFlag" name="importRepeatFlag" class="checkbox" type="checkbox"  checked="checked" value="1" />
				<s:text name="global.page.repeatException"/>
			</td>
		</tr>
	</table>
</div>

