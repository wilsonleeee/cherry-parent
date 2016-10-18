<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id="fileUpDialog"  class="hide">
	<div id="fileUpMsg" class="actionSuccess" style="display: none"></div>
	<div class="center clearfix" style="margin:30px 0">
       	<span style="margin-left:10px; display: inline;" class="left hide"> 
		    <input class="input_text" type="text" id="filePath" name="filePath" />
		    <input type="button" value="<s:text name="global.page.browse"/>"/>
		    <input class="input_file" type="file" name="fileUp" id="fileUp" size="33" onchange="filePath.value=this.value;return false;" /> 
		    <input type="button" value="<s:text name="global.page.upload"/>" onclick="doAjaxFileUpload();" id="upload" /> 
		    <img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display: none;">
		</span>
     </div>
     <div class="center clearfix">
	    <button class="confirm" type="button" onclick="fileUpDialogClose();"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
	</div>
</div>
<s:url action="BINOLCM06_uploadImage" namespace="/common" id="uploadImage"/>
<span id ="uploadImageUrl" style="display:none">${uploadImage}</span>
<span id ="UploadImage" style="display:none"><s:text name="global.page.UploadImage"/></span><%--上传图片 --%>
