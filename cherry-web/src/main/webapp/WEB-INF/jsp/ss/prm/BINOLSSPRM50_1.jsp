<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="/Cherry/css/common/sunny/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/cherry/cherry.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/lib/jquery-1.4.4.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	window.onbeforeunload = function(){
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	};		
	if (window.opener) {	
	  window.opener.lockParentWindow();	
	}	
} );

function endupload(){
	
	if($('#errorMsgDivTOP2').html()==null && $('#actionMsgDivTOP2').html()==null){		
		window.close(); 
		return ;
	}
	if($('#errorMsgDivTOP2').html()!=null){		
		window.close();
		return ;
	}
	
	window.opener.parsefile();
	window.close();
	return false;
}
</script>
<s:i18n name="i18n.ss.BINOLSSPRM50">
<div style="height:30px"></div>
<div class="panel ui-corner-all">
	<div class="panel-content">
	<div class="center clearfix">
		<div class="center clearfix" style="margin-top:15px">
		      <s:if test="hasActionErrors()">
				<div class="actionError" id="errorMsgDivTOP2">
				  	<s:actionerror/>
				</div>
			 </s:if>
		      <s:if test="hasActionMessages()">
				<div class="actionSuccess" id="actionMsgDivTOP2">
				  <s:actionmessage/>
				</div>
			 </s:if>
			<s:form action ="/ss/BINOLSSPRM50_UPLOAD.action" method ="POST" enctype ="multipart/form-data" id="fileForm"> 
			       <input type="file" value='<s:text name="btnChoose"/>' name="upExcel" id="upExcel" />  
				   <input type="hidden" value="" name="csrftoken" id="csrftoken">
				   <input type="submit" value='<s:text name="btnUpload"/>'/>
			</s:form > 
		</div>
		<div class="center clearfix" style="margin-top:15px">
		    <button class="confirm" type="button" onclick="endupload()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnClose"/></span></button>
		</div>
	</div>
</div>
</div>
<script type="text/javascript">
$("#csrftoken").val(window.opener.document.getElementById("csrftoken").value)
</script>
</s:i18n>