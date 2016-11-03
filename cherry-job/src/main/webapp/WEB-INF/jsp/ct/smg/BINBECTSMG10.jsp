<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>WITPOS店务通</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">
<link rel="stylesheet" href="../css/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="../css/datatable.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="../css/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="../css/cherry.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="../js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="../js/common/commonAjax.js"></script>
<script type="text/javascript" src="../js/common/cherry.js"></script>
  <style type="text/css">
  .template-content{word-break:normal; width:auto; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;}
  </style>
<script type="text/javascript">
$(function(){
	searchTemp();
});
function searchTemp() {
	var param = $('#execForm').serialize();
	$.ajax({
        url :$("#searchUrl").attr("href"), 
        type:'post',
        dataType:'html',
        data:param,
        success:function(msg){
            $('#template-tbody').html(msg);
            
        }
     });
}
function updateCode(obj) {
	
    if(confirm("您确定要保存吗？")) {
    	 $("#actionResultDisplay").empty();
         var param = $('#execForm').serialize();
         var $tr = $(obj).closest("tr");
  
         param += "&" + $tr.find("#templateId").serialize();
         param += "&" + $tr.find("#tempCode").serialize();
         if ($tr.find("#content").length > 0) {
        	 param += "&" + $tr.find("#content").serialize();
         }
         $.ajax({
             url :$("#updateUrl").attr("href"), 
             type:'post',
             dataType:'html',
             data:param,
             success:function(msg){
                 $('#actionResultDisplay').html(msg);
                 searchTemp();
             }
          });
    }
}
function edit(obj) {
	var $tr = $(obj).closest("tr");
	$tr.find(".btn-edit").hide();
	$tr.find(".btn-save").show();
	$tr.find(".btn-cancel").show();
	var value = $tr.find("#oldCode").val();
	$tr.find(".template-code").html('<input type="text" id="tempCode" name="tempCode" value="' + value + '"/>')
}

function cancel(obj) {
	var $tr = $(obj).closest("tr");
	$tr.find(".btn-cancel").hide();
	$tr.find(".btn-save").hide();
	$tr.find(".btn-edit").show();
	var value = $tr.find("#oldCode").val();
	$tr.find(".template-code").html(value);
}
</script>
</head>
<body>
	<div class="header container clearfix">
		<h1 class="logo left"><a href="#">WITPOS 店务通后台BATCH管理</a></h1>	
	</div> 
	<s:url action="BINBECTSMG10_update" id="update_Url"></s:url>
	<s:url action="BINBECTSMG10_search" id="search_Url"></s:url>
	<div class="main container clearfix">
		<div class="panel-header">
		  <div class="clearfix"> 
			<span class="breadcrumb left"> 
				短信模板一览
			</span>  
		  </div>
		</div> 
		<div class="panel-content">
		    <div id="actionResultDisplay"></div>
		    <div class="section">
		      <div class="section-content">
		      <form id="execForm">
			      <s:hidden name="brandInfoId"></s:hidden>
	  			  <s:hidden name="brandCode"></s:hidden>
		      </form>
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
			  		<thead>
			    		<tr>
			      			<th width="10%">No.</th>
			      			<th width="50%">模板内容</th>
			      			<th width="20%">供应商模板编号</th>
			      			<th width="20%">操作</th>
			    		</tr>
			  		</thead>
			  		<tbody id="template-tbody">
			  			
			  		</tbody>
				</table>
		 	  </div>
		    </div>
		</div>
	</div>
	<div class="hide">
	<a href="${search_Url }" id="searchUrl"></a>
	<a href="${update_Url }" id="updateUrl"></a>
	</div>
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>