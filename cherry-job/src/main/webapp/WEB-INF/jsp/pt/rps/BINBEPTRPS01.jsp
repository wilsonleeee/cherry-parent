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
function doBatch(url) {
	if($("#bussinessDate").val() == null || $("#bussinessDate").val() == ""){
		alert("请填写指定日期，格式为：yyyy-mm-dd！");
		return;
	}
	if($("#bussinessDate").val() > $("#bussinessDateH").val()){
		alert("指定日期不能大于当前业务日期，当前业务日期为："+$("#bussinessDateH").val());
		return;
	}
    if(confirm("您确定要执行吗？")) {
        $("#actionResultDisplay").empty();
        var param = $('#ptrps01BatchUrlForm').serialize();
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
				获取指定日期进销存统计数据
			</span>  
		  </div>
		</div> 
		<div class="panel-content">
		  <div id="actionResultDisplay"></div>
          <div class="section-content">
            <form id="ptrps01BatchUrlForm">
			     <s:hidden name="brandInfoId"></s:hidden>
	  			 <s:hidden name="brandCode"></s:hidden>
	            <table cellpadding="0" cellspacing="0" border="0" class="detail"  width="100%" id="editListTable">
	                <tr>
		                <th><s:text name="指定日期："/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		                <td>
		                	<span>
		                		<input type="hidden" value='<s:property value="bussinessDate" />'  id="bussinessDateH">
	                			<s:textfield id="bussinessDate" name="bussinessDate" cssClass="text" />
	                		</span>
	                	</td>
	                </tr>
	                <tr>
	                	<th colspan="2">
	                		<span class="highlight">※</span>
	                		<span class="highlight">通过该方法获取的各类业务的剩余笔数数据为当前业务日期的数据，非指定日期数据</span>
	                	</th>
	                </tr>
	            </table>
            </form>
          </div>
		  <div class="center clearfix" id="pageButton">
		    <s:url action="pt/BINBEPTRPS01_exec" id="PTRPS_BatchUrl"></s:url>
		  	<a class="add" onclick="doBatch('${PTRPS_BatchUrl }');return false;">
				<span class="ui-icon icon-enable"></span><span class="button-text">执行</span>
			</a>
		  </div>  
		</div>
	</div>
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>