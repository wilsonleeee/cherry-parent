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
function doBatch(url,flg) {
	
    if(confirm("您确定要执行吗？")) {
        $("#actionResultDisplay").empty();
        var param = $('#prm01Form').serialize();
        if(flg == 2) {
        	param = $('#prm01RecalForm').serialize();
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
				促销产品月度库存统计
			</span>  
		  </div>
		</div> 
		<div class="panel-content">
		    <div id="actionResultDisplay"></div>
		    <div class="section">
		      <form id="prm01Form">
		          <s:hidden name="brandInfoId"></s:hidden>
	  			  <s:hidden name="brandCode"></s:hidden>
		      </form>
		      <div class="clearfix" style="margin-top:10px;">
		      	<span class="left">促销产品月度库存统计（以业务日期为计算时间点）：</span>
		        <s:url action="ss/BINBESSPRM01_prm01Exec" id="prm01ExecUrl"></s:url>
		        <a class="add" onclick="doBatch('${prm01ExecUrl }',1);return false;">
				  <span class="ui-icon icon-enable"></span><span class="button-text">执行</span>
			    </a>
			  </div>
			  <form id="prm01RecalForm">
			      <s:hidden name="brandInfoId"></s:hidden>
	  			  <s:hidden name="brandCode"></s:hidden>
				  <div class="clearfix" style="margin-top:10px;">
				 	<span class="left">重算促销产品月度库存统计的时间范围：<s:textfield name="controlDateStart"></s:textfield>-<s:textfield name="controlDateEnd"></s:textfield></span> 
				  	<s:url action="ss/BINBESSPRM01_prm01RecalExec" id="prm01RecalExecUrl"></s:url>
				  	<a class="add" onclick="doBatch('${prm01RecalExecUrl }',2);return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">重算</span>
				    </a>	
			      </div>
		      </form>
		    </div>
		</div>
	</div>
	
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>