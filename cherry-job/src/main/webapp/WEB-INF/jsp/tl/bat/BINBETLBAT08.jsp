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
function doBatch(url,handleType) {
	if($("#tableName").val() == null || $("#tableName").val() == ""){
		alert("请填写【数据库名.架构名.表名】，格式为：【数据库名.架构名.表名】！");
		return;
	}
	if($("#tableColumn").val() == null || $("#tableColumn").val() == ""){
		alert("请填写【该表待加密列名】,列名不带方括号！");
		return;
	}
	if($("#identityColumn").val() == null || $("#identityColumn").val() == ""){
		alert("请填写【该表的唯一性标识列】，列名不带方括号！");
		return;
	}
    if(confirm("您确定要执行吗？")) {
        $("#actionResultDisplay").empty();
        var param = "handleType="+handleType+"&"+$('#urlForm').serialize();
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
				<strong>数据库数据加密解密</strong>
			</span>  
		  </div>
		</div> 
		<div class="panel-content">
		    <div id="actionResultDisplay"></div>
		    <div class="section">
			  <form id="urlForm">
				  <div class="clearfix" style="margin-top:10px;">
				  	<table class="detail">
				  		<tbody>
				  			<tr>
				  				<th>品牌代码</th>
				  				<td><s:textfield name="brandCode"></s:textfield></td>
				  				<th>数据库名.架构名.表名(按此格式填写)</th>
				  				<td><s:textfield name="tableName"></s:textfield></td>
				  			</tr>
				  			<tr>
				  				<th>该表待加密列名</th>
				  				<td><s:textfield name="tableColumn"></s:textfield></td>
				  				<th>该表的唯一性标识列</th>
				  				<td><s:textfield name="identityColumn"></s:textfield></td>
				  			</tr>
				  		</tbody>
				  	</table>
				  	<div class="center clearfix">
					  	<s:url action="bat/BINBETLBAT08_encryptData" id="encryptDataUrl"></s:url>
					  	<s:url action="bat/BINBETLBAT08_decryptData" id="decryptDataUrl"></s:url>
					  	<a class="add" onclick="doBatch('${encryptDataUrl }','0');return false;">
						  <span class="ui-icon icon-enable"></span><span class="button-text">AES加密</span>
					    </a>
					    <a class="add" onclick="doBatch('${decryptDataUrl }','0');return false;">
						  <span class="ui-icon icon-enable"></span><span class="button-text">AES解密</span>
					    </a>
				    </div>
				    <p></p>
				    <div class="center clearfix">
					  	<a class="add" onclick="doBatch('${encryptDataUrl }','1');return false;">
						  <span class="ui-icon icon-enable"></span><span class="button-text">DES加密</span>
					    </a>
					    <a class="add" onclick="doBatch('${decryptDataUrl }','1');return false;">
						  <span class="ui-icon icon-enable"></span><span class="button-text">DES解密</span>
					    </a>
				    </div>
				    <div class="center clearfix">
						  <span class="button-text">解密时如果某一条数据出错，则会保留原值，不影响其它行数据解密；加密时如果有一条出错，则全部回滚。请注意，不要在原始表上运用此工具，最好是将数据放到临时表中处理。</span>					    
				    </div>
				    <div class="center clearfix">
						  <span class="button-text">使用此功能需要对待加密的表额外增加一个字段【EncryptFlag】nvarchar(1);初始值设置为'0'(0：待加密（即未加密，此时才能加密）；1：已加密（只有已加密的才能解密）)</span>					    
				    </div>
			      </div>
		      </form>
		    </div>
		</div>
	</div>
	
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>