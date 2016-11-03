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
function deleteJob(url,obj) {
	
    if(confirm("您确定要删除该调度任务吗？")) {
        $("#actionResultDisplay").empty();
        $.ajax({
            url :url, 
            type:'post',
            dataType:'html',
            data:null,
            success:function(msg){
                $('#actionResultDisplay').html(msg);
                if($('#actionResultDisplay').find(".actionSuccess").length > 0) {
                	$(obj).parents('tr').remove();
                }
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
				调度任务一览
			</span>  
		  </div>
		</div> 
		<div class="panel-content">
		    <div id="actionResultDisplay"></div>
		    <div class="section">
		      <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
			  		<thead>
			    		<tr>
			      			<th width="20%">任务ID</th>
			      			<th width="20%">任务类型</th>
			      			<th width="25%">执行时间</th>
			      			<th width="20%">执行状态</th>
			      			<th width="15%">操作</th>
			    		</tr>
			  		</thead>
			  		<tbody>
			  			<s:iterator value="jobList" id="jobMap" status="status">
			  				<tr class='<s:if test="%{#status.index%2 == 0}">odd</s:if><s:else>even</s:else>'>
			  					<td><s:property value="schedulesId"/></td>
			  					<td>
			  					  <s:property value="taskType"/>
			  					  <s:if test='%{"CS".equals(taskType)}'>(沟通调度)</s:if>
			  					  <s:if test='%{"IS".equals(taskType)}'>(进销存操作统计)</s:if>
			  					</td>
			  					<td><s:property value="runTime"/></td>
			  					<td>
			  					  <s:if test="%{status == 1}">待运行</s:if>
			  					  <s:if test="%{status == 2}">正在运行</s:if>
			  					  <s:if test="%{status == 3}">失败结束</s:if>
			  					  <s:if test="%{status == 4}">警告结束</s:if>
			  					  <s:if test="%{status == 5}">成功结束</s:if>
			  					</td>
			  					<td>
			  						<s:if test="%{status != 2}">
			  						<s:url action="BINBECTSMG02_deleteJob" id="deleteJobUrl">
			  							<s:param name="brandInfoId" value="brandInfoId"></s:param>
			  							<s:param name="brandCode" value="brandCode"></s:param>
			  							<s:param name="jobId" value="%{#jobMap.schedulesId}"></s:param>
			  						</s:url>
			  						<a class="add" onclick="deleteJob('${deleteJobUrl }',this);return false;">
				  						<span class="ui-icon icon-enable"></span><span class="button-text">删除</span>
				  					</a>
			  						</s:if>
			  					</td>
			  				</tr>
			  			</s:iterator>
			  		</tbody>
				</table>
		 	  </div>
		    </div>
		</div>
	</div>
	
	<div class="footer">© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.</div>
</body>
</html>