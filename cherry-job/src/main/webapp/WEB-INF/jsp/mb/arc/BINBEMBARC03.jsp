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

<script type="text/javascript">
function doBatch(url, flag) {
	
    if(confirm("您确定要执行吗？")) {
        $("#actionResultDisplay").empty();
        var param = $('#arc03ExecForm').serialize();
        if (flag) {
        	param += "&zflag=1";
        }
        $.ajax({
            url :url, 
            type:'post',
            dataType:'html',
            data:param,
            success:function(msg){
                $('#actionResultDisplay').html(msg);
            }
         });
    }
}
</script>
</head>
<body>
	<div class="header container clearfix">
		<h1 class="logo left"><a href="#">WITPOS 店务通后台BATCH管理</a></h1>	
	</div> 
	
	<div class="main container clearfix">
		<div class="panel-header">
		  <div class="clearfix"> 
			<span class="breadcrumb left"> 
				积分明细初始导入
			</span>  
		  </div>
		</div> 
		<div class="panel-content">
		    <div id="actionResultDisplay"></div>
		    <div class="section">
			  <form id="arc03ExecForm">
			      <s:hidden name="brandInfoId"></s:hidden>
	  			  <s:hidden name="brandCode"></s:hidden>
				  <div class="clearfix" style="margin-top:10px;">
				 	<span class="left">需要导入的明细ID范围：<s:textfield name="dtlIdStart"></s:textfield>-<s:textfield name="dtlIdEnd"></s:textfield></span>
				  	<s:url action="BINBEMBARC03_arcExec" id="arc03ExecUrl"></s:url>
				  	<a class="add" onclick="doBatch('${arc03ExecUrl }');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">执行</span>
				    </a>	
				     <a class="add" onclick="doBatch('${arc03ExecUrl }','1');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">仅导入标记的记录</span>
				    </a>
			      </div>
		      </form>
		      <div class="clearfix" style="margin-top:10px;">
				 	<span class="left"><label style="color:red;">包含起始和结束ID，两者都不输入时，导入全部记录</label></span> 
		    </div>
		</div>
	</div>
	
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>