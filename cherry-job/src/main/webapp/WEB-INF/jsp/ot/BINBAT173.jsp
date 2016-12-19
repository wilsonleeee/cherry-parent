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
function doBatch(url,flag) {
	
    if(confirm("您确定要执行吗？")) {
        $("#actionResultDisplay").empty();
        var param = $('#prm01Form').serialize();
        if(flag == 2) {
        	if($('#activityCode').val() == ''){
        		alert("规则码不能为空");
        		return;
        	}
        	param += '&ruleCode=' + $('#ruleCode').val();
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
	<s:url action="ot/BINBAT173_exec" id="url"></s:url>
	<div class="header container clearfix">
		<h1 class="logo left"><a href="#">WITPOS 店务通后台BATCH管理</a></h1>	
	</div> 
	
	<div class="main container clearfix">
		<div class="panel-header">
			<div class="clearfix">
			<span class="breadcrumb left">
				家化历史电子券规则兼容性数据处理
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
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
					<tbody>
					<tr>
						<th>BATCH类型</th>
						<th>操作</th>
					</tr>
					<tr>
						<td width="70%">全部规则执行</td>
						<td width="10%">
							<a class="add" onclick="doBatch('${url}',1);return false;">
								<span class="ui-icon icon-enable"></span><span class="button-text">执行</span>
							</a>
						</td>
					</tr>
					<tr>
						<td>
							指定规则执行
							<input id="ruleCode" type="text" style="width:350px;" value=""/>
						</td>
						<td>
							<a class="add" onclick="doBatch('${url}',2);return false;">
								<span class="ui-icon icon-enable"></span><span class="button-text">执行</span>
							</a>
						</td>
					</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>