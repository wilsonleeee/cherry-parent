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
function doBatch(url, formId) {
	
    if(confirm("您确定要执行吗？")) {
        $("#actionResultDisplay").empty();
        var param = $(formId).serialize();
        $('#commForm').find(':input').each(function(){
			if ($(this).is(':checkbox') && !$(this).is(':checked')
					|| $(this).is(':radio') && !$(this).is(':checked')) {
				return true;
			}
			param += "&" + $(this).serialize();
		});
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


function selRange() {
	if($("#rangeKbn1").is(':checked')) {
		$("input[name='memIdStr']").val('');
		$("input[name='memIdEnd']").val('');
		$("#rangeDiv").hide();
	} else {
		$("#rangeDiv").show();
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
				推算等级变化明细
			</span>  
		  </div>
		</div> 
		<div class="panel-content">
		    <div id="actionResultDisplay"></div>
		    <div class="section">
		    <form id="commForm">
		    	<s:hidden name="brandInfoId"></s:hidden>
	  			<s:hidden name="brandCode"></s:hidden>
	  			<div class="clearfix" style="margin-top:10px;">
	  			<span class="left"> <strong>会员范围</strong></span>
				 <br/>
				 	<span class="left">
				 	<input type="radio" name="rangeKbn" value="0" id="rangeKbn0" checked="checked" onchange="selRange()"/><label for="rangeKbn0">自定义范围</label>
				 	<input type="radio" name="rangeKbn" value="1" id="rangeKbn1" onchange="selRange()"/><label for="rangeKbn1">系统已标记的会员</label>
				 	<div id="rangeDiv">
				 	会员：<s:textfield name="memIdStr" cssStyle="width:100px;"></s:textfield> - 
				 	<s:textfield name="memIdEnd" cssStyle="width:100px;"></s:textfield>
				 	&nbsp;&nbsp;&nbsp;&nbsp;
				 	 入会时间：不晚于<s:textfield name="joinLimit" cssStyle="width:100px;"></s:textfield>
				 	</div>
				 	 <br/>
				 	<input type="checkbox" name="memFlag" value="1" id="memFlagId"/><label for="memFlagId">所选范围内仅做新增处理，对于已生成记录的会员不做更新处理</label>
				 </span>
			    </div>
			 </form>
		    	<form id="lel02ExecForm">
				  <div class="clearfix" style="margin-top:10px;">
				 <span class="left"> <strong>会员等级计算</strong></span>
				 <br/>
				 	<span class="left">日期：<s:textfield name="levelDate" cssStyle="width:100px;"></s:textfield>
				 		<br/>
				 		类别：<select name="dateKbn" style="width:100px;">
				 			<option value="1">期初</option>
				 			<option value="2">期末</option>
				 		</select>
				 	</span>
				  	<s:url action="BINBEMBLEL02_lelExec" id="lel02ExecUrl"></s:url>
				  	<a class="add" onclick="doBatch('${lel02ExecUrl }','#lel02ExecForm');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">执行</span>
				    </a>	
			      </div>
		      </form>
			  <form id="lel02ExecForm1">
				  <div class="clearfix" style="margin-top:10px;">
				   <span class="left"><strong>等级变化明细生成</strong></span>
				   <br/>
				 	<span class="left">
				 	期初：<s:textfield name="beginDate" cssStyle="width:100px;"></s:textfield> 00:00:00
				 	<br/>
				 	期末：<s:textfield name="endDate" cssStyle="width:100px;"></s:textfield> 23:59:59
				 	</span>
				  	<s:url action="BINBEMBLEL02_detailExec" id="lel02DetailUrl"></s:url>
				  	<a class="add" onclick="doBatch('${lel02DetailUrl }','#lel02ExecForm1');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">执行</span>
				    </a>	
			      </div>
		      </form>
		      <div class="clearfix" style="margin-top:10px;">
				 	<span class="left"><label style="color:red;">日期格式：YYYY-MM-DD</label></span> 
		    </div>
		</div>
	</div>
	
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>