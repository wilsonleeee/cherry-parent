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
        var param;
        if (formId) {
        	param = $(formId).serialize();
        }
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

function exportCsv(formId) {
	$("#actionResultDisplay").empty();
    var param;
    if (formId) {
    	param = $(formId).serialize();
    }
    $('#commForm').find(':input').each(function(){
		if ($(this).is(':checkbox') && !$(this).is(':checked')
				|| $(this).is(':radio') && !$(this).is(':checked')) {
			return true;
		}
		param += "&" + $(this).serialize();
	});
    $.ajax({
        url : $("#lel03ExportCheckUrl").attr("href"), 
        type:'post',
        data:param,
        success:function(msg){
        	var json = eval('(' + msg + ')'); 
			if(json.exportStatus == "1") {
				var url = $("#lel03ExportUrl").attr("href");
				url += "?" + param;
			    document.location.href = url;
        	} else {
        		alert(json.message);
        	}
        }
     });
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
				 	</div>
				 </span>
			    </div>
			 </form>
			 <form id="lel03ExecForm05">
				  <div class="clearfix" style="margin-top:10px;">
				 <span class="left"> <strong>从老后台导入会员等级及有效期</strong></span>
				  <br/>
				 	<s:url action="BINBEMBLEL03_WitImptExec" id="lel03WitImptUrl"></s:url>
				  	<a class="add" onclick="doBatch('${lel03WitImptUrl }');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">开始导入</span>
				    </a>
			      </div>
		      </form>
			<form id="lel03ExecForm01">
				  <div class="clearfix" style="margin-top:10px;">
				 <span class="left"> <strong>初始化会员等级</strong></span>
				 <br/>
				 	<span class="left">初始化日期：<s:textfield name="initialDate" cssStyle="width:100px;"></s:textfield>
				 	</span>
				 	<s:url action="BINBEMBLEL03_ImptExec" id="lel03ImptUrl"></s:url>
				  	<a class="add" onclick="doBatch('${lel03ImptUrl }','#lel03ExecForm01');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">开始初始化</span>
				    </a>
			      </div>
		      </form>
		      <form id="lel03ExecForm02">
				  <div class="clearfix" style="margin-top:10px;">
				   <span class="left"><strong>根据销售计算会员等级</strong></span>
				   <br/>
				   <span class="left">截止时间：<s:textfield name="levelLimitTime" cssStyle="width:100px;"></s:textfield> 00:00:00
				 	</span>
				 	<s:url action="BINBEMBLEL03_detailExec" id="lel03DetailExecUrl"></s:url>
				  	<a class="add" onclick="doBatch('${lel03DetailExecUrl }','#lel03ExecForm02');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">等级计算</span>
				    </a>
				    <s:url action="BINBEMBLEL03_recalcExec" id="lel03recalcUrl"></s:url>
				  	<a class="add" onclick="doBatch('${lel03recalcUrl }','#lel03ExecForm02');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">等级重算</span>
				    </a>
			      </div>
				</form>
		    	<form id="lel03ExecForm03">
				  <div class="clearfix" style="margin-top:10px;">
				 <span class="left"> <strong>记录某个时间点会员等级</strong></span>
				 <br/>
				 	<span class="left">日期：<s:textfield name="levelDate" cssStyle="width:100px;"></s:textfield>
				 		<br/>
				 		类别：<select name="dateKbn" style="width:100px;">
				 			<option value="1">期初</option>
				 			<option value="2">期末</option>
				 		</select>
				 	</span>
				 	<s:url action="BINBEMBLEL03_lelExec" id="lel03LevelUrl"></s:url>
				  	<a class="add" onclick="doBatch('${lel03LevelUrl }','#lel03ExecForm03');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">开始记录</span>
				    </a>
			      </div>
		      </form>
		      <form id="lel03ExecForm04">
				  <div class="clearfix" style="margin-top:10px;">
				 <span class="left"> <strong>铂金会员购买情况报表</strong></span>
				 <br/>
				 	<span class="left">日期：
				 		<s:textfield name="reportStartDate" cssStyle="width:100px;"></s:textfield> - <s:textfield name="reportEndDate" cssStyle="width:100px;"></s:textfield>
				 	</span>
				 	<s:url action="BINBEMBLEL03_exportCsv" id="lel03ExportUrl"></s:url>
				  	<a class="add" onclick="exportCsv('#lel03ExecForm04');return false;">
					  <span class="ui-icon icon-enable"></span><span class="button-text">CSV导出</span>
				    </a>
			      </div>
		      </form>
			  <s:url id="exportCsvUrl" action="BINBEMBLEL03_exportCsv" ></s:url>
				<a id="lel03ExportUrl" href="${exportCsvUrl}"></a>
				 <s:url id="exportCheckUrl" action="BINBEMBLEL03_exportCheck" ></s:url>
				<a id="lel03ExportCheckUrl" href="${exportCheckUrl}"></a>
		      <div class="clearfix" style="margin-top:10px;">
				 	<span class="left"><label style="color:red;">日期格式：YYYY-MM-DD</label></span> 
		    </div>
		</div>
	</div>
	
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>