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
function doBatch(url) {
	
    if(confirm("您确定要执行吗？")) {
        $("#actionResultDisplay").empty();
        var param = $('#execForm').serialize();
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
				家化优惠券推送
			</span>  
		  </div>
		</div> 
		<div class="panel-content">
		    <div id="actionResultDisplay"></div>
		    <div class="section">
			  <form id="execForm">
			      <s:hidden name="brandInfoId"></s:hidden>
	  			  <s:hidden name="brandCode"></s:hidden>
				  <div class="clearfix" style="margin-top:10px;">
				  <s:url action="BINBESSPRM08_sendCoupon" id="sendCouponUrl"></s:url>
				  <s:url action="BINBESSPRM08_sendUpdateCoupon" id="sendUpdateCouponUrl"></s:url>
				  <s:url action="BINBESSPRM08_sendGenerate" id="sendGenerateUrl"></s:url>
				 	<span class="left">活动代码(多个活动","分隔)：<s:textfield name="ruleCodes"></s:textfield>
				 	<a class="add" onclick="doBatch('${sendGenerateUrl }');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">优惠券汇总信息同步至CRM</span>
				    </a>
				  	<a class="add" onclick="doBatch('${sendCouponUrl }');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">优惠券发放清单明细同步至CRM</span>
				    </a>	
				    <a class="add" onclick="doBatch('${sendUpdateCouponUrl }');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">优惠券状态变更传输</span>
				    </a>
			      </div>
		      </form>
		       <div class="clearfix" style="margin-top:10px;">
				 	<span class="left"><label style="color:red;">活动代码不输入时，推送全部活动</label></span> 
		    </div>
		</div>
	</div>
	
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>
