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
<link rel="stylesheet" href="../css/screen.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="../js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="../js/common/commonAjax.js"></script>
<script type="text/javascript" src="../js/common/cherry.js"></script>

<script type="text/javascript">
/**
 * 验证是否为正负整数
 */
function is_Int(obj){
	var reg = /^(-|\+)?\d+$/;
	var result = reg.test(obj.value);
	if(!result){
		$('#'+obj.id).val('');
		return false;
	}
}

function doBatch(url) {
	
	if($('#AHEAD_DAY').val() == null || $('#AHEAD_DAY').val() == ''){
		alert("查找N天后生日的会员(N),不能为空");
		return false;
	}
	if($('#START_DAY').val() == null || $('#START_DAY').val() == ''){
		alert("任务的开始时间(X),不能为空");
		return false;
	}
	if($('#END_DAY').val() == null || $('#END_DAY').val() == ''){
		alert("任务的结束时间(Y),不能为空");
		return false;
	}
	
    if(confirm("您确定要执行吗？")) {
        $("#actionResultDisplay").empty();
        var param = $('#mbVis02BatchUrlForm').serialize();
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
				会员回访任务生成
			</span>  
		  </div>
		</div> 
		<div class="panel-content">
		  <div id="actionResultDisplay"></div>
          <div class="section-content">
            <form id="mbVis02BatchUrlForm">
			     <s:hidden name="brandInfoId"></s:hidden>
	  			 <s:hidden name="brandCode"></s:hidden>
	            <table cellpadding="0" cellspacing="0" border="0" class="detail"  width="100%" id="editListTable">
	                <tr>
		                <th><s:text name="查找N天后生日的会员(N)："/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		                <td colspan=3>
		                	<span>
	                			<s:textfield id="AHEAD_DAY" name="AHEAD_DAY" cssClass="number" maxlength="4" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></s:textfield>
	                		</span>
						    <span>
								<span class="highlight">※</span>
								<span class="gray"><s:text name="以当前日期为基准计算出N天后生日的会员"></s:text></span>
						    </span>
	                	</td>
	                </tr>
	                <tr>
		                <th><s:text name="任务的开始时间(X)："/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		                <td colspan=3>
		                	<span>
	                			<s:textfield id="START_DAY" name="START_DAY" cssClass="number" maxlength="3" onblur="is_Int(this)"></s:textfield>
	                		</span>
						    <span>
								<span class="highlight">※</span>
								<span class="gray"><s:text name="以会员生日为基准，提前X天开始，注意X不限正负"></s:text></span>
						    </span>
	                	</td>
	                </tr>
	                <tr>
		                <th><s:text name="任务的结束时间(Y)："/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		                <td colspan=3>
		                	<span>
	                			<s:textfield id="END_DAY" name="END_DAY" cssClass="number" maxlength="3" onblur="is_Int(this)"></s:textfield>
	                		</span>
						    <span>
								<span class="highlight">※</span>
								<span class="gray"><s:text name="以会员生日为基准，提前Y天结束，注意Y不限正负"></s:text></span>
						    </span>
	                	</td>
	                </tr>
	                <tr>
		                <th><s:text name="绑定回访问卷ID："/></th>
		                <td colspan=3>
		                	<span>
	                			<s:textfield id="PAPERID" name="PAPERID" cssClass="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></s:textfield>
	                		</span>
						    <span>
								<span class="highlight">※</span>
								<span class="gray"><s:text name="如果有则填入"></s:text></span>
						    </span>
	                	</td>
	                </tr>
	            </table>
            </form>
          </div>
		  <div class="center clearfix" id="pageButton">
		    <s:url action="mb/BINBEMBVIS02_exec" id="mbVis02BatchUrl"></s:url>
		  	<a class="add" onclick="doBatch('${mbVis02BatchUrl }');return false;">
				<span class="ui-icon icon-enable"></span><span class="button-text">执行</span>
			</a>
		  </div>  
		</div>
	</div>
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>