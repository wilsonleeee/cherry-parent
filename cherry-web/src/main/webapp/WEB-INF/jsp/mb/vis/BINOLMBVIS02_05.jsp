<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:i18n name="i18n.mb.BINOLMBVIS02">
<div id ="memImportDialog">
   	<%-- ================== 信息提示区 START ======================= --%>
   	<s:if test="hasActionMessages()">
   		<div class="actionSuccess">
   			<s:actionmessage/>
   		</div>
   	</s:if>
   	<s:if test="hasActionErrors()">
		<div class="actionError">
		  	<s:actionerror/>
		</div>
	</s:if>
   	<hr class="space" />
	<%-- ================== 信息提示区   END  ======================= --%>
  	<div class="box2-active clearfix">
  		<s:hidden name="visitObjCodeImport" value="%{visitObjCode}"></s:hidden>
  		<table class="detail">
  			<tbody>
  				<s:if test='null != visitObjCode && !"".equals(visitObjCode)'>
  				<s:hidden name="visitObjNameImport" value="%{visitObjName}"></s:hidden>
  				<tr>
  					<th><s:text name="mbvis02_visitObjNameImport"/><span class="highlight">*</span></th>
  					<td><span><s:property value="%{visitObjName}"/></span></td>
  				</tr>
  				<tr>
  					<th><s:text name="mbvis02_importType"/><span class="highlight">*</span></th>
  					<td>
  						<select name="importType">
  							<option value="1"><s:text name="mbvis02_importType1"/></option>
  							<option value="2"><s:text name="mbvis02_importType2"/></option>  						
  						</select>
  					</td>
  				</tr>
  				</s:if>
  				<s:else>
  				<tr>
  					<th><s:text name="mbvis02_visitObjNameImport"/><span class="highlight">*</span></th>
  					<td><span><s:textfield name="visitObjNameImport" cssClass="text"></s:textfield></span></td>
  				</tr>
  				</s:else>
  				<tr>
  					<th><s:text name="mbvis02_importMem"/><span class="highlight">*</span></th>
  					<td>
  						<input type="text" name="pathExcel" id="pathExcel_import" class="input_text"><input type="button" value="<s:text name="global.page.browse"/>">
  						<input type="file" onchange="$('#pathExcel_import').val($(this).val());return false;" size="40" id="upExcel" name="upExcel" style="height:auto;" class="input_file">
  						<input type="button" id="upload" value="<s:text name="global.page.memImportExcel"/>">
  						<img height="15px" src="/Cherry/css/cherry/img/loading.gif" style="display: none;" id="loadingImg">
  					</td>
  				</tr>
  				<tr>
  					<th><s:text name="mbvis02_importTemplate"/></th>
  					<td><a href="/Cherry/download/回访对象导入模板.xls"><span style="color:#3366FF"><s:text name="mbvis02_importTemplateName"/></span></a></td>
  				</tr>
  			</tbody>
  		</table>
  	</div>
</div>
</s:i18n>

<script type="text/javascript">
$(function(){
	$("#upload").click(function(){
		
    	if($("#visitObjNameImport").val() == '') {
    		return false;
    	}
    	if($('#upExcel').val()=='') {
    		return false;
    	}
    	var params = {};
    	params.csrftoken = getTokenVal();
    	params.visitObjCode = $("#memImportDialog").find(':input[name="visitObjCodeImport"]').val();
    	params.visitObjName = $("#memImportDialog").find(':input[name="visitObjNameImport"]').val();
    	params.importType = $("#memImportDialog").find(':input[name="importType"]').val();
    	
    	// AJAX登陆图片
    	$ajaxLoading = $("#loadingImg");
    	$ajaxLoading.ajaxStart(function(){$(this).show();});
    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
    	$.ajaxFileUpload({
	        url: getBaseUrl()+'/mb/BINOLMBVIS02_excelImport',
	        secureuri:false,
	        data:params,
	        fileElementId:'upExcel',
	        dataType: 'html',
	        success: function (data){
	        	$("#visitObjImportDialog").html(data);
	        }
        });
	});
});
</script>
